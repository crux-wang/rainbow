//package ren.crux.rainbow.core.old.parser;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import com.sun.javadoc.*;
//import lombok.NonNull;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.tuple.Pair;
//import ren.crux.rainbow.core.model.Entry;
//import ren.crux.rainbow.core.model.Link;
//import ren.crux.rainbow.core.model.Tuple;
//import ren.crux.rainbow.core.model.Body;
//import ren.crux.rainbow.core.model.Requests;
//
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * @author wangzhihui
// */
//public class Context {
//
//    private static final Set<String> REQUEST_ANNOTATION_TYPES;
//    private static final String REST_CONTROLLER_ANNOTATION = RestController.class.getTypeName();
//    private static final String REQUEST_MAPPING_ANNOTATION = RequestMapping.class.getTypeName();
//
//    static {
//        REQUEST_ANNOTATION_TYPES = Stream.of(
//                RequestMapping.class,
//                GetMapping.class,
//                PostMapping.class,
//                PutMapping.class,
//                DeleteMapping.class,
//                PatchMapping.class).map(Class::getTypeName).collect(Collectors.toSet());
//    }
//
//    private final RootDoc rootDoc;
//    private final ClassDoc classDoc;
//    private final Map<String, Entry> entryMap = new HashMap<>();
//    private final FieldParser fieldParser = new FieldParser();
//    private final EntryParser entryParser = new EntryParser();
//    private final RequestParser requestParser = new RequestParser();
//    private final ControllerParser controllerParser = new ControllerParser();
//    private final ParameterParser parameterParser = new ParameterParser();
//    private final TagParser tagParser = new TagParser();
//    private final Cache<String, Link> refCache = CacheBuilder.newBuilder().build();
//
//    public Context(@NonNull RootDoc rootDoc) {
//        this.rootDoc = rootDoc;
//        ClassDoc[] classDocs = rootDoc.classes();
//        Objects.requireNonNull(classDocs);
//        if (classDocs.length > 0) {
//            this.classDoc = classDocs[0];
//        } else {
//            this.classDoc = null;
//        }
//    }
//
//    public Link getRef(Entry entry) {
//        try {
//            return refCache.get(entry.getQualifiedName(), () -> {
//                Link ref = new Link();
//                ref.setName(entry.getName());
//                ref.setTarget(entry.getQualifiedName());
//                ref.setTag("#Ref");
//                return ref;
//            });
//        } catch (ExecutionException e) {
//            return null;
//        }
//    }
//
//    public boolean isRestController(ClassDoc classDoc) {
//        for (AnnotationDesc annotation : classDoc.annotations()) {
//            if (StringUtils.equals(annotation.annotationType().qualifiedName(), REST_CONTROLLER_ANNOTATION)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public Optional<AnnotationDesc> getRequestMappingAnnotation(ClassDoc classDoc) {
//        for (AnnotationDesc annotation : classDoc.annotations()) {
//            if (StringUtils.equals(annotation.annotationType().qualifiedName(), REQUEST_MAPPING_ANNOTATION)) {
//                return Optional.of(annotation);
//            }
//        }
//        return Optional.empty();
//    }
//
//    public Optional<Pair<List<String>, List<String>>> getRequestMappingPathAndMethod(ClassDoc classDoc) {
//        return getRequestMappingAnnotation(classDoc).map(anno -> {
//            List<String> pathList = new LinkedList<>();
//            List<String> requestMethods = new LinkedList<>();
//            for (AnnotationDesc.ElementValuePair evPair : anno.elementValues()) {
//                AnnotationTypeElementDoc element = evPair.element();
//                if (StringUtils.equals("path", element.name()) || StringUtils.equals("value", element.name())) {
//                    AnnotationValue[] values = (AnnotationValue[]) evPair.value().value();
//                    for (AnnotationValue value : values) {
//                        pathList.add((String) value.value());
//                    }
//                } else if (StringUtils.equals("method", element.name())) {
//                    AnnotationValue[] values = (AnnotationValue[]) evPair.value().value();
//                    for (AnnotationValue value : values) {
//                        FieldDoc fd = (FieldDoc) value.value();
//                        requestMethods.add(fd.name());
//                    }
//                }
//            }
//            return pathList.isEmpty() && requestMethods.isEmpty() ? null : Pair.of(pathList, requestMethods);
//        });
//    }
//
//
//    public void logEntry(Entry entry) {
//        entryMap.put(entry.getQualifiedName(), entry);
//    }
//
//    public Tuple parse(FieldDoc fieldDoc) {
//        return fieldParser.parse(this, fieldDoc);
//    }
//
//    public Requests parse(MethodDoc methodDoc) {
//        return requestParser.parse(this, methodDoc);
//    }
//
//    public Requests parseController(ClassDoc classDoc) {
//        return controllerParser.parse(this, classDoc);
//    }
//
//    public Entry parseEntry(ClassDoc classDoc) {
//        return entryParser.parse(this, classDoc);
//    }
//
//    public boolean entryCondition(ClassDoc classDoc) {
//        return entryParser.condition(this, classDoc);
//    }
//
//    public Body parseParameter(MethodDoc methodDoc) {
//        return parameterParser.parse(this, methodDoc);
//    }
//
//    public List<Tuple> parse(FieldDoc[] fieldDocs) {
//        List<Tuple> fields = new LinkedList<>();
//        if (fieldDocs != null) {
//            for (FieldDoc fieldDoc : fieldDocs) {
//                fields.add(parse(fieldDoc));
//            }
//        }
//        return fields;
//    }
//
//    public List<Link> parse(Tag[] tags) {
//        return tagParser.parse(this, tags);
//    }
//
//    public Optional<Entry> getEntry(String qualifiedName) {
//        return Optional.ofNullable(entryMap.get(qualifiedName));
//    }
//
//    public Optional<ClassDoc> findClass(String className) {
//        return Optional.ofNullable(classDoc).map(cd -> cd.findClass(className));
//    }
//
//    public boolean isRequest(MethodDoc methodDoc) {
//        return isRequest(methodDoc.annotations());
//    }
//
//    public boolean isRequest(AnnotationDesc[] annotationDescList) {
//        return Arrays.stream(annotationDescList).anyMatch(a -> REQUEST_ANNOTATION_TYPES.contains(a.annotationType().qualifiedTypeName()));
//    }
//
//    public boolean isRequest(AnnotationDesc annotationDesc) {
//        return REQUEST_ANNOTATION_TYPES.contains(annotationDesc.annotationType().qualifiedTypeName());
//    }
//
//    public List<AnnotationDesc> getRequestAnnotations(ProgramElementDoc elementDoc) {
//        return Arrays.stream(elementDoc.annotations()).filter(this::isRequest).collect(Collectors.toList());
//    }
//}
