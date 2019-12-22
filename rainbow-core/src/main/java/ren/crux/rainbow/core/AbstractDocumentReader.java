package ren.crux.rainbow.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.*;
import ren.crux.rainbow.core.module.DefaultModule;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.module.filter.DefaultEntryClassNameFilter;
import ren.crux.rainbow.core.module.filter.DefaultEntryFieldFilter;
import ren.crux.rainbow.core.module.filter.DefaultRequestGroupFilter;
import ren.crux.rainbow.core.utils.EntryUtils;
import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.model.FieldDesc;
import ren.crux.rainbow.javadoc.model.MethodDesc;
import ren.crux.rainbow.javadoc.model.ParameterDesc;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractDocumentReader implements DocumentReader {

    protected ClassDocProvider classDocProvider;
    protected RequestGroupProvider requestGroupProvider;
    protected Map<String, Object> options = new HashMap<>();
    protected Map<String, String> implMap = new HashMap<>();
    protected List<Module> modules = new LinkedList<>();

    public static void merge(@NonNull Entry entry, ClassDesc classDesc) {
        if (classDesc == null) {
            log.warn("desc not found : {}", entry.getName());
            return;
        }
        entry.setCommentText(classDesc.getCommentText());
        List<FieldDesc> fieldDescs = classDesc.getFields();
        Map<String, FieldDesc> dict = fieldDescs.stream().collect(Collectors.toMap(FieldDesc::getName, f -> f));
        List<EntryField> fieldDetails = entry.getFields();
        fieldDetails.forEach(f -> merge(f, dict.get(f.getName())));
    }

    public static void merge(@NonNull EntryField fieldDetail, FieldDesc fieldDesc) {
        if (fieldDesc == null) {
            log.warn("desc not found : {}", fieldDetail.getName());
            return;
        }
        fieldDetail.setCommentText(fieldDesc.getCommentText());
    }

    @Override
    public DocumentReader with(ClassDocProvider classDocProvider) {
        this.classDocProvider = classDocProvider;
        this.classDocProvider.owner(this);
        return this;
    }

    @Override
    public DocumentReader with(RequestGroupProvider requestGroupProvider) {
        this.requestGroupProvider = requestGroupProvider;
        this.requestGroupProvider.owner(this);
        return this;
    }

    @Override
    public DocumentReader option(String key, Object value) {
        options.put(key, value);
        return this;
    }

    @Override
    public DocumentReader impl(String source, String impl) {
        implMap.put(source, impl);
        return this;
    }

    @Override
    public DocumentReader modules(Module... modules) {
        if (ArrayUtils.isNotEmpty(modules)) {
            this.modules.addAll(Arrays.asList(modules));
            this.modules.sort(Comparator.comparingInt(Module::order));
        }
        return this;
    }

    protected Context newContext() {
        Context context = new Context();
        context.getProperties().putAll(options);
        return context;
    }

    @Override
    public Optional<Document> read() {
        return read(newContext());
    }

    protected Optional<Document> read(Context context) {
        if (requestGroupProvider == null) {
            return Optional.empty();
        }
        if (classDocProvider == null) {
            classDocProvider = ClassDocProvider.EMPTY;
        }
        // 初始化
        classDocProvider.setUp(context);
        List<RequestGroup> requestGroups = requestGroupProvider.get(context)
                .stream()
                .filter(rg -> modules.stream().allMatch(m -> m.doFilterAndEnhance(context, rg)))
                .peek(rg -> classDocProvider.get(context, rg.getType()).ifPresent(cd -> merge(context, rg, cd)))
                .collect(Collectors.toList());
        if (requestGroups.isEmpty()) {
            return Optional.empty();
        }
        Document document = new Document();
        document.setRequestGroups(requestGroups);
        Map<String, Entry> entryMap = context.getEntryClassNames()
                .stream()
                .filter(cln -> modules.stream().allMatch(m -> m.doFilter(context, cln)))
                .map(cln -> {
                            try {
                                return Class.forName(cln);
                            } catch (ClassNotFoundException e) {
                                log.error("class not found : {}", cln, e);
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .filter(cls -> modules.stream().allMatch(m -> m.doFilter(context, cls)))
                .map(cls -> process(context, cls))
                .peek(e -> classDocProvider.get(context, e.getType()).ifPresent(cd -> merge(e, cd)))
                .peek(e -> modules.forEach(m -> m.enhance(context, e)))
                .collect(Collectors.toMap(Entry::getType, e -> e));
        document.setEntryMap(entryMap);
        document.getProperties().putAll(context.getProperties());
        return Optional.of(document);
    }

    public Entry process(Context context, Class<?> cls) {
        Entry entry = new Entry();
        entry.setInterfaceType(cls.isInterface());
        entry.setEnumType(cls.isEnum());
        entry.setType(cls.getCanonicalName());
        List<EntryField> fields = new LinkedList<>();
        do {
            if (entry.getName() == null) {
                entry.setName(cls.getSimpleName());
            }
            Field[] declaredFields = cls.getDeclaredFields();
            List<EntryField> tmp = Arrays.stream(declaredFields)
                    .map(this::process)
                    .filter(ef -> modules.stream().allMatch(m -> m.doFilterAndEnhance(context, ef)))
                    .collect(Collectors.toList());
            fields.addAll(tmp);
            cls = cls.getSuperclass();
        } while (cls != null && !(cls.equals(Object.class)));
        entry.setFields(fields);
        if (entry.isInterfaceType()) {
            String impl = implMap.get(entry.getType());
            if (impl != null) {
                try {
                    entry.setImpl(process(context, Class.forName(impl)));
                } catch (ClassNotFoundException e) {
                    log.error("class not found : {}", impl, e);
                }
            }
        }
        return entry;
    }

    public EntryField process(Field field) {
        EntryField entryField = new EntryField();
        entryField.setName(field.getName());
        entryField.setType(EntryUtils.build(field));
        entryField.setAnnotations(Arrays.stream(field.getAnnotations()).map(EntryUtils::process).collect(Collectors.toList()));
        return entryField;
    }

    public void merge(Context context, @NonNull RequestParam requestParam, ParameterDesc parameterDesc) {
        TypeDesc type = requestParam.getType();
        String typeName = StringUtils.substringBefore(type.getType(), "<");
        if (StringUtils.equals(typeName, parameterDesc.getType())) {
            // 添加参数类型
            context.addEntryClassName(typeName);
            context.addEntryClassName(type);
            if (StringUtils.startsWith(requestParam.getName(), "arg")) {
                requestParam.setName(parameterDesc.getName());
            }
            requestParam.setCommentText(parameterDesc.getCommentText());
        } else {
            log.warn("no matching param type name : {}", type);
        }
    }

    public void merge(Context context, @NonNull Request request, MethodDesc methodDesc) {
        if (methodDesc == null) {
            log.warn("no method desc found! {}", request.getType());
            return;
        }
        request.setCommentText(methodDesc.getCommentText().clearTags());
        request.setReturnCommentText(methodDesc.getReturnCommentText());
        // 添加返回值类型
        context.addEntryClassName(request.getReturnType());
        List<RequestParam> requestParams = request.getParams();
        List<ParameterDesc> parameterDescs = methodDesc.getParameters();
        if (requestParams != null && parameterDescs != null && requestParams.size() == parameterDescs.size()) {
            for (int i = 0; i < requestParams.size(); i++) {
                merge(context, requestParams.get(i), parameterDescs.get(i));
            }
        } else {
            log.warn("no matching method type name : {}", request.getType());
        }
    }

    public void merge(Context context, @NonNull RequestGroup requestGroup, ClassDesc classDesc) {
        if (classDesc == null) {
            log.warn("no class desc found! {}", requestGroup.getType());
            return;
        }
        requestGroup.setCommentText(classDesc.getCommentText());
        List<MethodDesc> methodDescs = classDesc.getMethods();
        List<Request> requests = requestGroup.getRequests();
        if (methodDescs != null && requests != null) {
            Map<String, MethodDesc> methodDict = methodDescs.stream().collect(Collectors.toMap(MethodDesc::getType, d -> d));
            for (Request request : requests) {
                merge(context, request, methodDict.get(request.getType()));
            }
        }
    }

    @Override
    public ClassDocProvider cdp() {
        return classDocProvider;
    }

    @Override
    public RequestGroupProvider rgp() {
        return requestGroupProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ClassDocProvider> T cdp(Class<T> tClass) {
        return (T) classDocProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RequestGroupProvider> T rgp(Class<T> tClass) {
        return (T) requestGroupProvider;
    }

    @Override
    public DocumentReader useDefaultModule() {
        Module module = new DefaultModule("default")
                .filter(new DefaultEntryClassNameFilter().useDefault())
                .filter(new DefaultEntryFieldFilter())
                .filter(new DefaultRequestGroupFilter());
        modules(module);
        return this;
    }
}
