package ren.crux.rainbow.core.docs.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.model.FieldDesc;
import ren.crux.rainbow.core.desc.model.MethodDesc;
import ren.crux.rainbow.core.desc.model.ParameterDesc;
import ren.crux.rainbow.core.docs.model.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MergeHelper {

    public static Map<String, Entry> build(Set<String> entryClassNames, Set<String> entryPackageSet) {
        return entryClassNames
                .stream()
                .filter(type -> {
                    for (String pkg : entryPackageSet) {
                        if (StringUtils.startsWith(type, pkg)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(MergeHelper::process)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Entry::getType, e -> e));
    }

    public static Entry process(String entryClassName) {
        Entry entry = new Entry();
        entry.setType(entryClassName);
        List<EntryField> fields = new LinkedList<>();
        Class<?> cls;
        try {
            cls = Class.forName(entryClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
        do {
            if (entry.getName() == null) {
                entry.setName(cls.getSimpleName());
            }
            Field[] declaredFields = cls.getDeclaredFields();
            List<EntryField> tmp = Arrays.stream(declaredFields).map(MergeHelper::process).collect(Collectors.toList());
            fields.addAll(tmp);
            cls = cls.getSuperclass();
        } while (!(cls.equals(Object.class)));
        entry.setFields(fields);
        return entry;
    }

    public static EntryField process(Field field) {
        EntryField entryField = new EntryField();
        entryField.setName(field.getName());
        entryField.setType(field.getGenericType().getTypeName());
        entryField.setActualParamTypes(getActualTypeArguments(field.getGenericType()));
        entryField.setAnnotations(Arrays.stream(field.getAnnotations()).map(MergeHelper::process).collect(Collectors.toList()));
        return entryField;
    }

    public static Annotation process(java.lang.annotation.Annotation annotation) {
        Class<? extends java.lang.annotation.Annotation> annotationType = annotation.annotationType();
        Annotation annotationDetail = new Annotation();
        annotationDetail.setName(annotationType.getSimpleName());
        annotationDetail.setType(annotationType.getCanonicalName());
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);
            annotationDetail.setAttribute(memberValues);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return annotationDetail;
    }


    public static String[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            if (actualTypeArguments != null) {
                return Arrays.stream(actualTypeArguments).map(Type::getTypeName).toArray(String[]::new);
            }
        }
        return null;
    }


    public static void merge(@NonNull RequestParam requestParam, ParameterDesc parameterDesc) {
        String typeName = StringUtils.substringBefore(requestParam.getType(), "<");
        if (StringUtils.equals(typeName, parameterDesc.getType())) {
            if (StringUtils.startsWith(requestParam.getName(), "arg")) {
                requestParam.setName(parameterDesc.getName());
            }
            requestParam.setCommentText(parameterDesc.getCommentText());
        } else {
            log.warn("no matching param type name : {}", requestParam.getType());
        }
    }

    public static void merge(@NonNull Request request, MethodDesc methodDesc) {
        if (methodDesc == null) {
            return;
        }
        request.setCommentText(methodDesc.getCommentText());
        List<RequestParam> requestParams = request.getParams();
        List<ParameterDesc> parameterDescs = methodDesc.getParameters();
        if (requestParams != null && parameterDescs != null && requestParams.size() == parameterDescs.size()) {
            for (int i = 0; i < requestParams.size(); i++) {
                merge(requestParams.get(i), parameterDescs.get(i));
            }
        } else {
            log.warn("no matching method type name : {}", request.getType());
        }
    }

    public static void merge(@NonNull RequestGroup requestGroup, ClassDesc classDesc) {
        if (classDesc == null) {
            return;
        }
        requestGroup.setCommentText(classDesc.getCommentText());
        List<MethodDesc> methodDescs = classDesc.getMethods();
        List<Request> requests = requestGroup.getRequests();
        if (methodDescs != null && requests != null) {
            Map<String, MethodDesc> methodDict = methodDescs.stream().collect(Collectors.toMap(MethodDesc::getType, d -> d));
            for (Request request : requests) {
                merge(request, methodDict.get(request.getType()));
            }
        }
    }

    public static void merge(@NonNull EntryField entryField, FieldDesc fieldDesc) {
        String typeName = StringUtils.substringBefore(entryField.getType(), "<");
        if (StringUtils.equals(fieldDesc.getType(), typeName)) {
            entryField.setCommentText(fieldDesc.getCommentText());
        } else {
            log.warn("no matching type name : {}", typeName);
        }
    }

    public static void merge(@NonNull Entry entry, ClassDesc classDesc) {
        if (classDesc == null) {
            return;
        }
        entry.setCommentText(classDesc.getCommentText());
        List<FieldDesc> fieldDescs = classDesc.getFields();
        List<EntryField> entryFields = entry.getFields();
        if (entryFields != null && fieldDescs != null) {
            for (int i = 0; i < entryFields.size(); i++) {
                if (i >= fieldDescs.size()) {
                    break;
                }
                merge(entryFields.get(i), fieldDescs.get(i));
            }
        }
    }

    public static void merge(@NonNull Document document, @NonNull List<ClassDesc> descs) {
        Map<String, ClassDesc> dict = descs.stream().collect(Collectors.toMap(ClassDesc::getType, e -> e));
        Map<String, Entry> entryMap = document.getEntryMap();
        List<RequestGroup> requestGroups = document.getRequestGroups();
        if (entryMap != null) {
            entryMap.forEach((type, entry) -> merge(entry, dict.get(type)));
        }
        if (requestGroups != null) {
            requestGroups.forEach(rg -> merge(rg, dict.get(rg.getType())));
        }
    }

}
