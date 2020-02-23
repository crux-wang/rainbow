package ren.crux.rainbow.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import io.swagger.v3.core.util.*;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.module.CombinationModule;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.option.RevisableConfig;
import ren.crux.rainbow.core.utils.QdoxUtils;
import ren.crux.rainbow.core.utils.SpringWebUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author wangzhihui
 */
@Slf4j
public class DocumentReader implements OpenApiReader {

    protected OpenAPIConfiguration openApiConfiguration;
    private RevisableConfig config;
    private Map<String, String> implMap;
    private Context context;
    private CombinationModule combinationModule;
    private JavaProjectBuilder javaProjectBuilder;
    private Map<String, JavaClass> dict = new HashMap<>();
    private String basePath = "/";
    private OpenAPI openAPI;
    private Components components;
    private Paths paths;
    private Set<Tag> openApiTags;

    public DocumentReader(JavaProjectBuilder javaProjectBuilder, @NonNull RevisableConfig config, @NonNull Map<String, String> implMap, @NonNull List<Module> modules) {
        this.javaProjectBuilder = javaProjectBuilder;
        this.config = config;
        this.implMap = implMap;
    }

    public DocumentReader() {
        this.openAPI = new OpenAPI();
        paths = new Paths();
        openApiTags = new LinkedHashSet<>();
        components = new Components();
    }

    public DocumentReader(OpenAPI openAPI) {
        this();
        setConfiguration(new SwaggerConfiguration().openAPI(openAPI));
    }

    public DocumentReader(OpenAPIConfiguration openApiConfiguration) {
        this();
        setConfiguration(openApiConfiguration);
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
    }

    @Override
    public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
        dict = QdoxUtils.toMap(javaProjectBuilder.getClasses());
        return null;
    }

    public OpenAPI read(Class<?> cls) {

        if (!SpringWebUtils.isController(cls)) {
            return openAPI;
        }

        paths.addPathItem();
        PathItem pathItem = new PathItem();
        pathItem.options()

        String[] requestMappingPath = SpringWebUtils.getRequestMappingPath(cls);


        JavaClass javaClass = dict.get(cls.getCanonicalName());


        JavaType classType = TypeFactory.defaultInstance().constructType(cls);
        BeanDescription bd = Json.mapper().getSerializationConfig().introspect(classType);


        final List<Parameter> globalParameters = new ArrayList<>();

        // iterate class methods
        Method methods[] = cls.getMethods();
        for (Method method : methods) {

            AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), method.getParameterTypes());
            javax.ws.rs.Produces methodProduces = ReflectionUtils.getAnnotation(method, javax.ws.rs.Produces.class);
            javax.ws.rs.Consumes methodConsumes = ReflectionUtils.getAnnotation(method, javax.ws.rs.Consumes.class);

            if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                continue;
            }

            javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

            String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath, isSubresource);

            // skip if path is the same as parent, e.g. for @ApplicationPath annotated application
            // extending resource config.
            if (ignoreOperationPath(operationPath, parentPath) && !isSubresource) {
                continue;
            }

            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);
            if (operationPath != null) {
                if (config != null && ReaderUtils.isIgnored(operationPath, config)) {
                    continue;
                }

                final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(method);

                String httpMethod = ReaderUtils.extractOperationMethod(method, OpenAPIExtensions.chain());
                httpMethod = (httpMethod == null && isSubresource) ? parentMethod : httpMethod;

                if (StringUtils.isBlank(httpMethod) && subResource == null) {
                    continue;
                } else if (StringUtils.isBlank(httpMethod) && subResource != null) {
                    Type returnType = method.getGenericReturnType();
                    if (annotatedMethod != null && annotatedMethod.getType() != null) {
                        returnType = annotatedMethod.getType();
                    }

                    if (shouldIgnoreClass(returnType.getTypeName()) && !method.getGenericReturnType().equals(subResource)) {
                        continue;
                    }
                }

                io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);
                JsonView jsonViewAnnotation;
                JsonView jsonViewAnnotationForRequestBody;
                if (apiOperation != null && apiOperation.ignoreJsonView()) {
                    jsonViewAnnotation = null;
                    jsonViewAnnotationForRequestBody = null;
                } else {
                    jsonViewAnnotation = ReflectionUtils.getAnnotation(method, JsonView.class);
                    /* If one and only one exists, use the @JsonView annotation from the method parameter annotated
                       with @RequestBody. Otherwise fall back to the @JsonView annotation for the method itself. */
                    jsonViewAnnotationForRequestBody = (JsonView) Arrays.stream(ReflectionUtils.getParameterAnnotations(method))
                            .filter(arr ->
                                    Arrays.stream(arr)
                                            .anyMatch(annotation ->
                                                    annotation.annotationType()
                                                            .equals(io.swagger.v3.oas.annotations.parameters.RequestBody.class)
                                            )
                            ).flatMap(Arrays::stream)
                            .filter(annotation ->
                                    annotation.annotationType()
                                            .equals(JsonView.class)
                            ).reduce((a, b) -> null)
                            .orElse(jsonViewAnnotation);
                }

                Operation operation = parseMethod(
                        method,
                        globalParameters,
                        methodProduces,
                        classProduces,
                        methodConsumes,
                        classConsumes,
                        classSecurityRequirements,
                        classExternalDocumentation,
                        classTags,
                        classServers,
                        isSubresource,
                        parentRequestBody,
                        parentResponses,
                        jsonViewAnnotation,
                        classResponses,
                        annotatedMethod);
                if (operation != null) {

                    List<Parameter> operationParameters = new ArrayList<>();
                    List<Parameter> formParameters = new ArrayList<>();
                    Annotation[][] paramAnnotations = ReflectionUtils.getParameterAnnotations(method);
                    if (annotatedMethod == null) { // annotatedMethod not null only when method with 0-2 parameters
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        for (int i = 0; i < genericParameterTypes.length; i++) {
                            final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
                            io.swagger.v3.oas.annotations.Parameter paramAnnotation = AnnotationsUtils.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class, paramAnnotations[i]);
                            Type paramType = ParameterProcessor.getParameterType(paramAnnotation, true);
                            if (paramType == null) {
                                paramType = type;
                            } else {
                                if (!(paramType instanceof Class)) {
                                    paramType = type;
                                }
                            }
                            ResolvedParameter resolvedParameter = getParameters(paramType, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes, jsonViewAnnotation);
                            operationParameters.addAll(resolvedParameter.parameters);
                            // collect params to use together as request Body
                            formParameters.addAll(resolvedParameter.formParameters);
                            if (resolvedParameter.requestBody != null) {
                                processRequestBody(
                                        resolvedParameter.requestBody,
                                        operation,
                                        methodConsumes,
                                        classConsumes,
                                        operationParameters,
                                        paramAnnotations[i],
                                        type,
                                        jsonViewAnnotationForRequestBody);
                            }
                        }
                    } else {
                        for (int i = 0; i < annotatedMethod.getParameterCount(); i++) {
                            AnnotatedParameter param = annotatedMethod.getParameter(i);
                            final Type type = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);
                            io.swagger.v3.oas.annotations.Parameter paramAnnotation = AnnotationsUtils.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class, paramAnnotations[i]);
                            Type paramType = ParameterProcessor.getParameterType(paramAnnotation, true);
                            if (paramType == null) {
                                paramType = type;
                            } else {
                                if (!(paramType instanceof Class)) {
                                    paramType = type;
                                }
                            }
                            ResolvedParameter resolvedParameter = getParameters(paramType, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes, jsonViewAnnotation);
                            operationParameters.addAll(resolvedParameter.parameters);
                            // collect params to use together as request Body
                            formParameters.addAll(resolvedParameter.formParameters);
                            if (resolvedParameter.requestBody != null) {
                                processRequestBody(
                                        resolvedParameter.requestBody,
                                        operation,
                                        methodConsumes,
                                        classConsumes,
                                        operationParameters,
                                        paramAnnotations[i],
                                        type,
                                        jsonViewAnnotationForRequestBody);
                            }
                        }
                    }
                    // if we have form parameters, need to merge them into single schema and use as request body..
                    if (formParameters.size() > 0) {
                        Schema mergedSchema = new ObjectSchema();
                        for (Parameter formParam : formParameters) {
                            mergedSchema.addProperties(formParam.getName(), formParam.getSchema());
                            if (null != formParam.getRequired() && formParam.getRequired()) {
                                mergedSchema.addRequiredItem(formParam.getName());
                            }
                        }
                        Parameter merged = new Parameter().schema(mergedSchema);
                        processRequestBody(
                                merged,
                                operation,
                                methodConsumes,
                                classConsumes,
                                operationParameters,
                                new Annotation[0],
                                null,
                                jsonViewAnnotationForRequestBody);

                    }
                    if (operationParameters.size() > 0) {
                        for (Parameter operationParameter : operationParameters) {
                            operation.addParametersItem(operationParameter);
                        }
                    }

                    // if subresource, merge parent parameters
                    if (parentParameters != null) {
                        for (Parameter parentParameter : parentParameters) {
                            operation.addParametersItem(parentParameter);
                        }
                    }

                    if (subResource != null && !scannedResources.contains(subResource)) {
                        scannedResources.add(subResource);
                        read(subResource, operationPath, httpMethod, true, operation.getRequestBody(), operation.getResponses(), classTags, operation.getParameters(), scannedResources);
                        // remove the sub resource so that it can visit it later in another path
                        // but we have a room for optimization in the future to reuse the scanned result
                        // by caching the scanned resources in the reader instance to avoid actual scanning
                        // the the resources again
                        scannedResources.remove(subResource);
                        // don't proceed with root resource operation, as it's handled by subresource
                        continue;
                    }

                    final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
                    if (chain.hasNext()) {
                        final OpenAPIExtension extension = chain.next();
                        extension.decorateOperation(operation, method, chain);
                    }

                    PathItem pathItemObject;
                    if (openAPI.getPaths() != null && openAPI.getPaths().get(operationPath) != null) {
                        pathItemObject = openAPI.getPaths().get(operationPath);
                    } else {
                        pathItemObject = new PathItem();
                    }

                    if (StringUtils.isBlank(httpMethod)) {
                        continue;
                    }
                    setPathItemOperation(pathItemObject, httpMethod, operation);

                    paths.addPathItem(operationPath, pathItemObject);
                    if (openAPI.getPaths() != null) {
                        this.paths.putAll(openAPI.getPaths());
                    }

                    openAPI.setPaths(this.paths);

                }
            }
        }

        // if no components object is defined in openApi instance passed by client, set openAPI.components to resolved components (if not empty)
        if (!isEmptyComponents(components) && openAPI.getComponents() == null) {
            openAPI.setComponents(components);
        }

        // add tags from class to definition tags
        AnnotationsUtils
                .getTags(apiTags, true).ifPresent(tags -> openApiTags.addAll(tags));

        if (!openApiTags.isEmpty()) {
            Set<Tag> tagsSet = new LinkedHashSet<>();
            if (openAPI.getTags() != null) {
                for (Tag tag : openAPI.getTags()) {
                    if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                        tagsSet.add(tag);
                    }
                }
            }
            for (Tag tag : openApiTags) {
                if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                    tagsSet.add(tag);
                }
            }
            openAPI.setTags(new ArrayList<>(tagsSet));
        }

        return openAPI;
    }
}
