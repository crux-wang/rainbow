package ren.crux.rainbow.openapi3;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.openapi3.utils.JAXBAnnotationsHelper;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.core.util.RefUtils.constructRef;

/**
 * RainbowModelResolver
 *
 * @author wangzhihui
 **/
@Slf4j
@Component
public class RainbowModelResolver extends ModelResolver {

    public RainbowModelResolver(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public io.swagger.v3.oas.models.media.Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {

        boolean isPrimitive = false;
        io.swagger.v3.oas.models.media.Schema model = null;

        if (annotatedType == null) {
            return null;
        }
        if (this.shouldIgnoreClass(annotatedType.getType())) {
            return null;
        }

        final JavaType type;
        if (annotatedType.getType() instanceof JavaType) {
            type = (JavaType) annotatedType.getType();
        } else {
            type = _mapper.constructType(annotatedType.getType());
        }

        final Annotation resolvedSchemaOrArrayAnnotation = AnnotationsUtils.mergeSchemaAnnotations(annotatedType.getCtxAnnotations(), type);
        final io.swagger.v3.oas.annotations.media.Schema resolvedSchemaAnnotation =
                resolvedSchemaOrArrayAnnotation == null ?
                        null :
                        resolvedSchemaOrArrayAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                ((io.swagger.v3.oas.annotations.media.ArraySchema) resolvedSchemaOrArrayAnnotation).schema() :
                                (io.swagger.v3.oas.annotations.media.Schema) resolvedSchemaOrArrayAnnotation;

        final io.swagger.v3.oas.annotations.media.ArraySchema resolvedArrayAnnotation =
                resolvedSchemaOrArrayAnnotation == null ?
                        null :
                        resolvedSchemaOrArrayAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                (io.swagger.v3.oas.annotations.media.ArraySchema) resolvedSchemaOrArrayAnnotation :
                                null;

        final BeanDescription beanDesc;

        {
            BeanDescription recurBeanDesc = _mapper.getSerializationConfig().introspect(type);

            HashSet<String> visited = new HashSet<>();
            JsonSerialize jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);
            while (jsonSerialize != null && !Void.class.equals(jsonSerialize.as())) {
                String asName = jsonSerialize.as().getName();
                if (visited.contains(asName)) {
                    break;
                }
                visited.add(asName);

                recurBeanDesc = _mapper.getSerializationConfig().introspect(
                        _mapper.constructType(jsonSerialize.as())
                );
                jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);
            }
            beanDesc = recurBeanDesc;
        }

        String name = annotatedType.getName();
        if (StringUtils.isBlank(name)) {
            // allow override of name from annotation
            if (!annotatedType.isSkipSchemaName() && resolvedSchemaAnnotation != null && !resolvedSchemaAnnotation.name().isEmpty()) {
                name = resolvedSchemaAnnotation.name();
            }
            if (StringUtils.isBlank(name) && !ReflectionUtils.isSystemType(type)) {
                name = _typeName(type, beanDesc);
            }
        }

        name = decorateModelName(annotatedType, name);

        // if we have a ref we don't consider anything else
        if (resolvedSchemaAnnotation != null &&
                StringUtils.isNotEmpty(resolvedSchemaAnnotation.ref())) {
            if (resolvedArrayAnnotation == null) {
                return new io.swagger.v3.oas.models.media.Schema().$ref(resolvedSchemaAnnotation.ref()).name(name);
            } else {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                return schema.items(new io.swagger.v3.oas.models.media.Schema().$ref(resolvedSchemaAnnotation.ref()).name(name));
            }
        }

        if (!annotatedType.isSkipOverride() && resolvedSchemaAnnotation != null && !Void.class.equals(resolvedSchemaAnnotation.implementation())) {
            Class<?> cls = resolvedSchemaAnnotation.implementation();

            log.debug("overriding datatype from {} to {}", type, cls.getName());

            Annotation[] ctxAnnotation = null;
            if (resolvedArrayAnnotation != null && annotatedType.getCtxAnnotations() != null) {
                List<Annotation> annList = new ArrayList<>();
                for (Annotation a : annotatedType.getCtxAnnotations()) {
                    if (!(a instanceof ArraySchema)) {
                        annList.add(a);
                    }
                }
                annList.add(resolvedSchemaAnnotation);
                ctxAnnotation = annList.toArray(new Annotation[annList.size()]);
            } else {
                ctxAnnotation = annotatedType.getCtxAnnotations();
            }

            AnnotatedType aType = new AnnotatedType()
                    .type(cls)
                    .ctxAnnotations(ctxAnnotation)
                    .parent(annotatedType.getParent())
                    .name(annotatedType.getName())
                    .resolveAsRef(annotatedType.isResolveAsRef())
                    .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                    .propertyName(annotatedType.getPropertyName())
                    .skipOverride(true);
            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                io.swagger.v3.oas.models.media.Schema innerSchema = null;

                io.swagger.v3.oas.models.media.Schema primitive = PrimitiveType.createProperty(cls);
                if (primitive != null) {
                    innerSchema = primitive;
                } else {
                    innerSchema = context.resolve(aType);
                    if (innerSchema != null && "object".equals(innerSchema.getType()) && StringUtils.isNotBlank(innerSchema.getName())) {
                        // create a reference for the items
                        if (context.getDefinedModels().containsKey(innerSchema.getName())) {
                            innerSchema = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(innerSchema.getName()));
                        }
                    } else if (innerSchema != null && innerSchema.get$ref() != null) {
                        innerSchema = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(innerSchema.get$ref()) ? innerSchema.get$ref() : innerSchema.getName());
                    }
                }
                schema.setItems(innerSchema);
                return schema;
            } else {
                io.swagger.v3.oas.models.media.Schema implSchema = context.resolve(aType);
                if (implSchema != null && aType.isResolveAsRef() && "object".equals(implSchema.getType()) && StringUtils.isNotBlank(implSchema.getName())) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(implSchema.getName())) {
                        implSchema = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(implSchema.getName()));
                    }
                } else if (implSchema != null && implSchema.get$ref() != null) {
                    implSchema = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(implSchema.get$ref()) ? implSchema.get$ref() : implSchema.getName());
                }
                return implSchema;
            }
        }

        if (!annotatedType.isSkipOverride() && resolvedSchemaAnnotation != null && StringUtils.isNotEmpty(resolvedSchemaAnnotation.type()) && !resolvedSchemaAnnotation.type().equals("object")) {
            PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(resolvedSchemaAnnotation.type(), resolvedSchemaAnnotation.format());
            if (primitiveType == null) {
                primitiveType = PrimitiveType.fromType(type);
            }
            if (primitiveType == null) {
                primitiveType = PrimitiveType.fromName(resolvedSchemaAnnotation.type());
            }
            if (primitiveType != null) {
                io.swagger.v3.oas.models.media.Schema primitive = primitiveType.createProperty();
                model = primitive;
                isPrimitive = true;

            }
        }

        if (model == null && type.isEnumType()) {
            model = new StringSchema();
            _addEnumProps(type.getRawClass(), model);
            isPrimitive = true;
        }
        if (model == null) {
            PrimitiveType primitiveType = PrimitiveType.fromType(type);
            if (primitiveType != null) {
                model = PrimitiveType.fromType(type).createProperty();
                isPrimitive = true;
            }
        }

        if (!annotatedType.isSkipJsonIdentity()) {
            JsonIdentityInfo jsonIdentityInfo = AnnotationsUtils.getAnnotation(JsonIdentityInfo.class, annotatedType.getCtxAnnotations());
            if (jsonIdentityInfo == null) {
                jsonIdentityInfo = type.getRawClass().getAnnotation(JsonIdentityInfo.class);
            }
            if (model == null && jsonIdentityInfo != null) {
                JsonIdentityReference jsonIdentityReference = AnnotationsUtils.getAnnotation(JsonIdentityReference.class, annotatedType.getCtxAnnotations());
                if (jsonIdentityReference == null) {
                    jsonIdentityReference = type.getRawClass().getAnnotation(JsonIdentityReference.class);
                }
                model = RainbowModelResolver.GeneratorWrapper.processJsonIdentity(annotatedType, context, _mapper, jsonIdentityInfo, jsonIdentityReference);
                if (model != null) {
                    return model;
                }
            }
        }

        if (model == null && annotatedType.getJsonUnwrappedHandler() != null) {
            model = annotatedType.getJsonUnwrappedHandler().apply(annotatedType);
            if (model == null) {
                return null;
            }
        }

        if ("Object".equals(name)) {
            return new io.swagger.v3.oas.models.media.Schema();
        }

        if (isPrimitive) {
            annotatedType.isSchemaProperty();//model.name(name);
            XML xml = resolveXml(beanDesc.getClassInfo(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
            if (xml != null) {
                model.xml(xml);
            }
            resolveSchemaMembers(model, annotatedType);

            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                schema.setItems(model);
                return schema;
            }
            if (type.isEnumType() &&
                    (resolvedSchemaAnnotation != null && resolvedSchemaAnnotation.enumAsRef()) ||
                    ModelResolver.enumsAsRef
            ) {
                // Store off the ref and add the enum as a top-level model
                context.defineModel(name, model, annotatedType, null);
                // Return the model as a ref only property
                model = new io.swagger.v3.oas.models.media.Schema().$ref(name);
            }
            return model;
        }

        /**
         * --Preventing parent/child hierarchy creation loops - Comment 1--
         * Creating a parent model will result in the creation of child models. Creating a child model will result in
         * the creation of a parent model, as per the second If statement following this comment.
         *
         * By checking whether a model has already been resolved (as implemented below), loops of parents creating
         * children and children creating parents can be short-circuited. This works because currently the
         * ModelConverterContextImpl will return null for a class that already been processed, but has not yet been
         * defined. This logic works in conjunction with the early immediate definition of model in the context
         * implemented later in this method (See "Preventing parent/child hierarchy creation loops - Comment 2") to
         * prevent such
         */
        io.swagger.v3.oas.models.media.Schema resolvedModel = context.resolve(annotatedType);
        if (resolvedModel != null) {
            if (name != null && name.equals(resolvedModel.getName())) {
                return resolvedModel;
            }
        }

        // using deprecated method to maintain compatibility with jackson version < 2.9
        //alternatively use AnnotatedMember jsonValueMember = beanDesc.findJsonValueAccessor();
        final AnnotatedMethod jsonValueMethod = beanDesc.findJsonValueMethod();
        if (jsonValueMethod != null) {
            AnnotatedType aType = new AnnotatedType()
                    .type(jsonValueMethod.getType())
                    .parent(annotatedType.getParent())
                    .name(annotatedType.getName())
                    .schemaProperty(annotatedType.isSchemaProperty())
                    .resolveAsRef(annotatedType.isResolveAsRef())
                    .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                    .propertyName(annotatedType.getPropertyName())
                    .skipOverride(true);
            return context.resolve(aType);
        }

        List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(type.getRawClass(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
        boolean isComposedSchema = composedSchemaReferencedClasses != null;

        if (type.isContainerType()) {
            // TODO currently a MapSchema or ArraySchema don't also support composed schema props (oneOf,..)
            isComposedSchema = false;
            JavaType keyType = type.getKeyType();
            JavaType valueType = type.getContentType();
            String pName = null;
            if (valueType != null) {
                BeanDescription valueTypeBeanDesc = _mapper.getSerializationConfig().introspect(valueType);
                pName = _typeName(valueType, valueTypeBeanDesc);
            }
            Annotation[] schemaAnnotations = null;
            if (resolvedSchemaAnnotation != null) {
                schemaAnnotations = new Annotation[]{resolvedSchemaAnnotation};
            }
            if (keyType != null && valueType != null) {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                io.swagger.v3.oas.models.media.Schema addPropertiesSchema = context.resolve(
                        new AnnotatedType()
                                .type(valueType)
                                .schemaProperty(annotatedType.isSchemaProperty())
                                .ctxAnnotations(schemaAnnotations)
                                .skipSchemaName(true)
                                .resolveAsRef(annotatedType.isResolveAsRef())
                                .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                                .propertyName(annotatedType.getPropertyName())
                                .parent(annotatedType.getParent()));
                if (addPropertiesSchema != null) {
                    if (StringUtils.isNotBlank(addPropertiesSchema.getName())) {
                        pName = addPropertiesSchema.getName();
                    }
                    if ("object".equals(addPropertiesSchema.getType()) && pName != null) {
                        // create a reference for the items
                        if (context.getDefinedModels().containsKey(pName)) {
                            addPropertiesSchema = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(pName));
                        }
                    } else if (addPropertiesSchema.get$ref() != null) {
                        addPropertiesSchema = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(addPropertiesSchema.get$ref()) ? addPropertiesSchema.get$ref() : addPropertiesSchema.getName());
                    }
                }
                io.swagger.v3.oas.models.media.Schema mapModel = new MapSchema().additionalProperties(addPropertiesSchema);
                mapModel.name(name);
                model = mapModel;
                //return model;
            } else if (valueType != null) {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                io.swagger.v3.oas.models.media.Schema items = context.resolve(new AnnotatedType()
                        .type(valueType)
                        .schemaProperty(annotatedType.isSchemaProperty())
                        .ctxAnnotations(schemaAnnotations)
                        .skipSchemaName(true)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .propertyName(annotatedType.getPropertyName())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .parent(annotatedType.getParent()));

                if (items == null) {
                    return null;
                }
                if (annotatedType.isSchemaProperty() && annotatedType.getCtxAnnotations() != null && annotatedType.getCtxAnnotations().length > 0) {
                    if (!"object".equals(items.getType())) {
                        for (Annotation annotation : annotatedType.getCtxAnnotations()) {
                            if (annotation instanceof XmlElement) {
                                XmlElement xmlElement = (XmlElement) annotation;
                                if (xmlElement != null && xmlElement.name() != null && !"".equals(xmlElement.name()) && !"##default".equals(xmlElement.name())) {
                                    XML xml = items.getXml() != null ? items.getXml() : new XML();
                                    xml.setName(xmlElement.name());
                                    items.setXml(xml);
                                }
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(items.getName())) {
                    pName = items.getName();
                }
                if ("object".equals(items.getType()) && pName != null) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(pName)) {
                        items = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(pName));
                    }
                } else if (items.get$ref() != null) {
                    items = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(items.get$ref()) ? items.get$ref() : items.getName());
                }

                io.swagger.v3.oas.models.media.Schema arrayModel =
                        new ArraySchema().items(items);
                if (_isSetType(type.getRawClass())) {
                    arrayModel.setUniqueItems(true);
                }
                arrayModel.name(name);
                model = arrayModel;
            } else {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    return null;
                }
            }
        } else if (isComposedSchema) {
            model = new ComposedSchema()
                    .type("object")
                    .name(name);
        } else {
            if (_isOptionalType(type)) {
                AnnotatedType aType = new AnnotatedType()
                        .type(type.containedType(0))
                        .ctxAnnotations(annotatedType.getCtxAnnotations())
                        .parent(annotatedType.getParent())
                        .schemaProperty(annotatedType.isSchemaProperty())
                        .name(annotatedType.getName())
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .propertyName(annotatedType.getPropertyName())
                        .skipOverride(true);
                model = context.resolve(aType);
                return model;
            } else {
                model = new io.swagger.v3.oas.models.media.Schema()
                        .type("object")
                        .name(name);
            }
        }

        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, annotatedType, null);
        }

        XML xml = resolveXml(beanDesc.getClassInfo(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
        if (xml != null) {
            model.xml(xml);
        }

        if (!(model instanceof ArraySchema) || (model instanceof ArraySchema && resolvedArrayAnnotation == null)) {
            resolveSchemaMembers(model, annotatedType);
        }

        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = new HashSet<String>();
        JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
        }

        List<io.swagger.v3.oas.models.media.Schema> props = new ArrayList<io.swagger.v3.oas.models.media.Schema>();
        Map<String, io.swagger.v3.oas.models.media.Schema> modelProps = new LinkedHashMap<String, io.swagger.v3.oas.models.media.Schema>();

        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        List<String> ignoredProps = getIgnoredProperties(beanDesc);
        properties.removeIf(p -> ignoredProps.contains(p.getName()));
        for (BeanPropertyDefinition propDef : properties) {
            io.swagger.v3.oas.models.media.Schema property = null;
            String propName = propDef.getName();
            Annotation[] annotations = null;

            AnnotatedMember member = propDef.getPrimaryMember();
            if (member == null) {
                final BeanDescription deserBeanDesc = _mapper.getDeserializationConfig().introspect(type);
                List<BeanPropertyDefinition> deserProperties = deserBeanDesc.findProperties();
                for (BeanPropertyDefinition prop : deserProperties) {
                    if (StringUtils.isNotBlank(prop.getInternalName()) && prop.getInternalName().equals(propDef.getInternalName())) {
                        member = prop.getPrimaryMember();
                        break;
                    }
                }
            }

            // hack to avoid clobbering properties with get/is names
            // it's ugly but gets around https://github.com/swagger-api/swagger-core/issues/415
            if (propDef.getPrimaryMember() != null) {
                final JsonProperty jsonPropertyAnn = propDef.getPrimaryMember().getAnnotation(JsonProperty.class);
                if (jsonPropertyAnn == null || !jsonPropertyAnn.value().equals(propName)) {
                    if (member != null) {
                        java.lang.reflect.Member innerMember = member.getMember();
                        if (innerMember != null) {
                            String altName = innerMember.getName();
                            if (altName != null) {
                                final int length = altName.length();
                                for (String prefix : Arrays.asList("get", "is")) {
                                    final int offset = prefix.length();
                                    if (altName.startsWith(prefix) && length > offset
                                            && !Character.isUpperCase(altName.charAt(offset))) {
                                        propName = altName;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            PropertyMetadata md = propDef.getMetadata();

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore)) {

                List<Annotation> annotationList = new ArrayList<Annotation>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);

                if (hiddenByJsonView(annotations, annotatedType)) {
                    continue;
                }

                JavaType propType = member.getType();
                if (propType != null && "void".equals(propType.getRawClass().getName())) {
                    if (member instanceof AnnotatedMethod) {
                        propType = ((AnnotatedMethod) member).getParameterType(0);
                    }

                }
                String propSchemaName = null;
                io.swagger.v3.oas.annotations.media.Schema ctxSchema = AnnotationsUtils.getSchemaAnnotation(annotations);
                if (AnnotationsUtils.hasSchemaAnnotation(ctxSchema)) {
                    if (!StringUtils.isBlank(ctxSchema.name())) {
                        propSchemaName = ctxSchema.name();
                    }
                }
                if (propSchemaName == null) {
                    io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema = AnnotationsUtils.getArraySchemaAnnotation(annotations);
                    if (AnnotationsUtils.hasArrayAnnotation(ctxArraySchema)) {
                        if (AnnotationsUtils.hasSchemaAnnotation(ctxArraySchema.schema())) {
                            if (!StringUtils.isBlank(ctxArraySchema.schema().name())) {
                                propSchemaName = ctxArraySchema.schema().name();
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(propSchemaName)) {
                    propName = propSchemaName;
                }
                Annotation propSchemaOrArray = AnnotationsUtils.mergeSchemaAnnotations(annotations, propType);
                final io.swagger.v3.oas.annotations.media.Schema propResolvedSchemaAnnotation =
                        propSchemaOrArray == null ?
                                null :
                                propSchemaOrArray instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                        ((io.swagger.v3.oas.annotations.media.ArraySchema) propSchemaOrArray).schema() :
                                        (io.swagger.v3.oas.annotations.media.Schema) propSchemaOrArray;

                io.swagger.v3.oas.annotations.media.Schema.AccessMode accessMode = resolveAccessMode(propDef, type, propResolvedSchemaAnnotation);


                AnnotatedType aType = new AnnotatedType()
                        .type(propType)
                        .ctxAnnotations(annotations)
                        //.name(propName)
                        .parent(model)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .skipSchemaName(true)
                        .schemaProperty(true)
                        .propertyName(propName);

                final AnnotatedMember propMember = member;
                aType.jsonUnwrappedHandler((t) -> {
                    JsonUnwrapped uw = propMember.getAnnotation(JsonUnwrapped.class);
                    if (uw != null && uw.enabled()) {
                        t
                                .ctxAnnotations(null)
                                .jsonUnwrappedHandler(null)
                                .resolveAsRef(false);
                        handleUnwrapped(props, context.resolve(t), uw.prefix(), uw.suffix());
                        return null;
                    } else {
                        return new io.swagger.v3.oas.models.media.Schema();
                        //t.jsonUnwrappedHandler(null);
                        //return context.resolve(t);
                    }
                });
                property = clone(context.resolve(aType));

                if (property != null) {
                    if (property.get$ref() == null) {
                        Boolean required = md.getRequired();
                        if (required != null && !Boolean.FALSE.equals(required)) {
                            addRequiredItem(model, propName);
                        } else {
                            if (propDef.isRequired()) {
                                addRequiredItem(model, propName);
                            }
                        }
                        if (accessMode != null) {
                            switch (accessMode) {
                                case AUTO:
                                    break;
                                case READ_ONLY:
                                    property.readOnly(true);
                                    break;
                                case READ_WRITE:
                                    break;
                                case WRITE_ONLY:
                                    property.writeOnly(true);
                                    break;
                                default:
                            }
                        }
                    }
                    final BeanDescription propBeanDesc = _mapper.getSerializationConfig().introspect(propType);
                    if (!propType.isContainerType()) {
                        if ("object".equals(property.getType())) {
                            // create a reference for the property
                            String pName = _typeName(propType, propBeanDesc);
                            if (StringUtils.isNotBlank(property.getName())) {
                                pName = property.getName();
                            }

                            if (context.getDefinedModels().containsKey(pName)) {
                                property = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(pName));
                            }
                        } else if (property.get$ref() != null) {
                            property = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName());
                        }
                    }
                    property.setName(propName);
                    property.description(getComment(propDef));
                    JAXBAnnotationsHelper.apply(propBeanDesc.getClassInfo(), annotations, property);
                    applyBeanValidatorAnnotations(property, annotations, model);

                    props.add(property);
                }
            }
        }
        for (io.swagger.v3.oas.models.media.Schema prop : props) {
            modelProps.put(prop.getName(), prop);
        }
        if (modelProps.size() > 0) {
            model.setProperties(modelProps);
        }

        /**
         * --Preventing parent/child hierarchy creation loops - Comment 2--
         * Creating a parent model will result in the creation of child models, as per the first If statement following
         * this comment. Creating a child model will result in the creation of a parent model, as per the second If
         * statement following this comment.
         *
         * The current model must be defined in the context immediately. This done to help prevent repeated
         * loops where  parents create children and children create parents when a hierarchy is present. This logic
         * works in conjunction with the "early checking" performed earlier in this method
         * (See "Preventing parent/child hierarchy creation loops - Comment 1"), to prevent repeated creation loops.
         *
         *
         * As an aside, defining the current model in the context immediately also ensures that child models are
         * available for modification by resolveSubtypes, when their parents are created.
         */
        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            context.defineModel(name, model, annotatedType, null);
        }

        /**
         * This must be done after model.setProperties so that the model's set
         * of properties is available to filter from any subtypes
         **/
        if (!resolveSubtypes(model, beanDesc, context)) {
            model.setDiscriminator(null);
        }

        Discriminator discriminator = resolveDiscriminator(type, context);
        if (discriminator != null) {
            model.setDiscriminator(discriminator);
        }

        if (resolvedSchemaAnnotation != null) {
            String ref = resolvedSchemaAnnotation.ref();
            // consider ref as is
            if (!StringUtils.isBlank(ref)) {
                model.$ref(ref);
            }
            Class<?> not = resolvedSchemaAnnotation.not();
            if (!Void.class.equals(not)) {
                model.not((new io.swagger.v3.oas.models.media.Schema().$ref(context.resolve(new AnnotatedType().type(not).jsonViewAnnotation(annotatedType.getJsonViewAnnotation())).getName())));
            }
            if (resolvedSchemaAnnotation.requiredProperties() != null &&
                    resolvedSchemaAnnotation.requiredProperties().length > 0 &&
                    StringUtils.isNotBlank(resolvedSchemaAnnotation.requiredProperties()[0])) {
                for (String prop : resolvedSchemaAnnotation.requiredProperties()) {
                    addRequiredItem(model, prop);
                }
            }
        }

        if (isComposedSchema) {

            ComposedSchema composedSchema = (ComposedSchema) model;

            Class<?>[] allOf = resolvedSchemaAnnotation.allOf();
            Class<?>[] anyOf = resolvedSchemaAnnotation.anyOf();
            Class<?>[] oneOf = resolvedSchemaAnnotation.oneOf();

            List<Class<?>> allOfFiltered = Stream.of(allOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            allOfFiltered.forEach(c -> {
                io.swagger.v3.oas.models.media.Schema allOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                io.swagger.v3.oas.models.media.Schema refSchema = new io.swagger.v3.oas.models.media.Schema().$ref(allOfRef.getName());
                // allOf could have already being added during subtype resolving
                if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                    composedSchema.addAllOfItem(refSchema);
                }
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(composedSchema, allOfRef);
                }
            });

            List<Class<?>> anyOfFiltered = Stream.of(anyOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            anyOfFiltered.forEach(c -> {
                io.swagger.v3.oas.models.media.Schema anyOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                composedSchema.addAnyOfItem(new io.swagger.v3.oas.models.media.Schema().$ref(anyOfRef.getName()));
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(composedSchema, anyOfRef);
                }

            });

            List<Class<?>> oneOfFiltered = Stream.of(oneOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            oneOfFiltered.forEach(c -> {
                io.swagger.v3.oas.models.media.Schema oneOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                if (oneOfRef != null) {
                    if (StringUtils.isBlank(oneOfRef.getName())) {
                        composedSchema.addOneOfItem(oneOfRef);
                    } else {
                        composedSchema.addOneOfItem(new io.swagger.v3.oas.models.media.Schema().$ref(oneOfRef.getName()));
                    }
                    // remove shared properties defined in the parent
                    if (isSubtype(beanDesc.getClassInfo(), c)) {
                        removeParentProperties(composedSchema, oneOfRef);
                    }
                }

            });

            if (!composedModelPropertiesAsSibling) {
                if (composedSchema.getAllOf() != null && !composedSchema.getAllOf().isEmpty()) {
                    if (composedSchema.getProperties() != null && !composedSchema.getProperties().isEmpty()) {
                        ObjectSchema propSchema = new ObjectSchema();
                        propSchema.properties(composedSchema.getProperties());
                        composedSchema.setProperties(null);
                        composedSchema.addAllOfItem(propSchema);
                    }
                }
            }
        }

        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, annotatedType, null);
        }

        if (model != null && annotatedType.isResolveAsRef() &&
                (isComposedSchema || "object".equals(model.getType())) &&
                StringUtils.isNotBlank(model.getName())) {
            if (context.getDefinedModels().containsKey(model.getName())) {
                model = new io.swagger.v3.oas.models.media.Schema().$ref(constructRef(model.getName()));
            }
        } else if (model != null && model.get$ref() != null) {
            model = new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(model.get$ref()) ? model.get$ref() : model.getName());
        }

        if (model != null && resolvedArrayAnnotation != null) {
            if (!"array".equals(model.getType())) {
                ArraySchema schema = new ArraySchema();
                schema.setItems(model);
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                return schema;
            } else {
                if (model instanceof ArraySchema) {
                    resolveArraySchema(annotatedType, (ArraySchema) model, resolvedArrayAnnotation);
                }
            }
        }

        resolveDiscriminatorProperty(type, context, model);

        return model;
    }

    private io.swagger.v3.oas.models.media.Schema clone(io.swagger.v3.oas.models.media.Schema property) {
        if (property == null)
            return property;
        try {
            String cloneName = property.getName();
            property = Json.mapper().readValue(Json.pretty(property), io.swagger.v3.oas.models.media.Schema.class);
            property.setName(cloneName);
        } catch (IOException e) {
            log.error("Could not clone property, e");
        }
        return property;
    }

    private boolean isSubtype(AnnotatedClass childClass, Class<?> parentClass) {
        final BeanDescription parentDesc = _mapper.getSerializationConfig().introspectClassAnnotations(parentClass);
        List<NamedType> subTypes = _intr.findSubtypes(parentDesc.getClassInfo());
        if (subTypes == null) {
            return false;
        }
        for (NamedType subtype : subTypes) {
            final Class<?> subtypeType = subtype.getType();
            if (childClass.getRawType().isAssignableFrom(subtypeType)) {
                return true;
            }
        }
        return false;
    }


    private void handleUnwrapped(List<io.swagger.v3.oas.models.media.Schema> props, io.swagger.v3.oas.models.media.Schema innerModel, String prefix, String suffix) {
        if (StringUtils.isBlank(suffix) && StringUtils.isBlank(prefix)) {
            if (innerModel.getProperties() != null) {
                props.addAll(innerModel.getProperties().values());
            }
        } else {
            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }
            if (innerModel.getProperties() != null) {
                for (io.swagger.v3.oas.models.media.Schema prop : (Collection<io.swagger.v3.oas.models.media.Schema>) innerModel.getProperties().values()) {
                    try {
                        io.swagger.v3.oas.models.media.Schema clonedProp = Json.mapper().readValue(Json.pretty(prop), io.swagger.v3.oas.models.media.Schema.class);
                        clonedProp.setName(prefix + prop.getName() + suffix);
                        props.add(clonedProp);
                    } catch (IOException e) {
                        log.error("Exception cloning property", e);
                        return;
                    }
                }
            }
        }
    }

    private boolean resolveSubtypes(io.swagger.v3.oas.models.media.Schema model, BeanDescription bean, ModelConverterContext context) {
        final List<NamedType> types = _intr.findSubtypes(bean.getClassInfo());
        if (types == null) {
            return false;
        }

        /**
         * Remove the current class from the child classes. This happens if @JsonSubTypes references
         * the annotated class as a subtype.
         */
        removeSelfFromSubTypes(types, bean);

        /**
         * As the introspector will find @JsonSubTypes for a child class that are present on its super classes, the
         * code segment below will also run the introspector on the parent class, and then remove any sub-types that are
         * found for the parent from the sub-types found for the child. The same logic all applies to implemented
         * interfaces, and is accounted for below.
         */
        removeSuperClassAndInterfaceSubTypes(types, bean);

        int count = 0;
        final Class<?> beanClass = bean.getClassInfo().getAnnotated();
        for (NamedType subtype : types) {
            final Class<?> subtypeType = subtype.getType();
            if (!beanClass.isAssignableFrom(subtypeType)) {
                continue;
            }

            final io.swagger.v3.oas.models.media.Schema subtypeModel = context.resolve(new AnnotatedType().type(subtypeType));

            if (StringUtils.isBlank(subtypeModel.getName()) ||
                    subtypeModel.getName().equals(model.getName())) {
                subtypeModel.setName(_typeNameResolver.nameForType(_mapper.constructType(subtypeType),
                        TypeNameResolver.Options.SKIP_API_MODEL));
            }

            // here schema could be not composed, but we want it to be composed, doing same work as done
            // in resolve method??

            ComposedSchema composedSchema = null;
            if (!(subtypeModel instanceof ComposedSchema)) {
                // create composed schema
                // TODO #2312 - smarter way needs clone implemented in #2227
                composedSchema = (ComposedSchema) new ComposedSchema()
                        .title(subtypeModel.getTitle())
                        .name(subtypeModel.getName())
                        .deprecated(subtypeModel.getDeprecated())
                        .additionalProperties(subtypeModel.getAdditionalProperties())
                        .description(subtypeModel.getDescription())
                        .discriminator(subtypeModel.getDiscriminator())
                        .example(subtypeModel.getExample())
                        .exclusiveMaximum(subtypeModel.getExclusiveMaximum())
                        .exclusiveMinimum(subtypeModel.getExclusiveMinimum())
                        .externalDocs(subtypeModel.getExternalDocs())
                        .format(subtypeModel.getFormat())
                        .maximum(subtypeModel.getMaximum())
                        .maxItems(subtypeModel.getMaxItems())
                        .maxLength(subtypeModel.getMaxLength())
                        .maxProperties(subtypeModel.getMaxProperties())
                        .minimum(subtypeModel.getMinimum())
                        .minItems(subtypeModel.getMinItems())
                        .minLength(subtypeModel.getMinLength())
                        .minProperties(subtypeModel.getMinProperties())
                        .multipleOf(subtypeModel.getMultipleOf())
                        .not(subtypeModel.getNot())
                        .nullable(subtypeModel.getNullable())
                        .pattern(subtypeModel.getPattern())
                        .properties(subtypeModel.getProperties())
                        .readOnly(subtypeModel.getReadOnly())
                        .required(subtypeModel.getRequired())
                        .type(subtypeModel.getType())
                        .uniqueItems(subtypeModel.getUniqueItems())
                        .writeOnly(subtypeModel.getWriteOnly())
                        .xml(subtypeModel.getXml())
                        .extensions(subtypeModel.getExtensions());

                composedSchema.setEnum(subtypeModel.getEnum());
            } else {
                composedSchema = (ComposedSchema) subtypeModel;
            }
            io.swagger.v3.oas.models.media.Schema refSchema = new io.swagger.v3.oas.models.media.Schema().$ref(model.getName());
            // allOf could have already being added during type resolving when @Schema(allOf..) is declared
            if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                composedSchema.addAllOfItem(refSchema);
            }
            removeParentProperties(composedSchema, model);
            if (!composedModelPropertiesAsSibling) {
                if (composedSchema.getAllOf() != null && !composedSchema.getAllOf().isEmpty()) {
                    if (composedSchema.getProperties() != null && !composedSchema.getProperties().isEmpty()) {
                        ObjectSchema propSchema = new ObjectSchema();
                        propSchema.properties(composedSchema.getProperties());
                        composedSchema.setProperties(null);
                        composedSchema.addAllOfItem(propSchema);
                    }
                }
            }


            // replace previous schema..
            Class<?> currentType = subtype.getType();
            if (StringUtils.isNotBlank(composedSchema.getName())) {
                context.defineModel(composedSchema.getName(), composedSchema, new AnnotatedType().type(currentType), null);
            }


        }
        return count != 0;
    }

    private void removeSelfFromSubTypes(List<NamedType> types, BeanDescription bean) {
        Class<?> beanClass = bean.getType().getRawClass();
        types.removeIf(type -> beanClass.equals(type.getType()));
    }

    private void removeSuperClassAndInterfaceSubTypes(List<NamedType> types, BeanDescription bean) {
        Class<?> beanClass = bean.getType().getRawClass();
        Class<?> superClass = beanClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            removeSuperSubTypes(types, superClass);
        }
        if (!types.isEmpty()) {
            Class<?>[] superInterfaces = beanClass.getInterfaces();
            for (Class<?> superInterface : superInterfaces) {
                removeSuperSubTypes(types, superInterface);
                if (types.isEmpty()) {
                    break;
                }
            }
        }
    }

    private void removeSuperSubTypes(List<NamedType> resultTypes, Class<?> superClass) {
        JavaType superType = _mapper.constructType(superClass);
        BeanDescription superBean = _mapper.getSerializationConfig().introspect(superType);
        final List<NamedType> superTypes = _intr.findSubtypes(superBean.getClassInfo());
        if (superTypes != null) {
            resultTypes.removeAll(superTypes);
        }
    }

    private void removeParentProperties(io.swagger.v3.oas.models.media.Schema child, io.swagger.v3.oas.models.media.Schema parent) {
        final Map<String, io.swagger.v3.oas.models.media.Schema> baseProps = parent.getProperties();
        final Map<String, io.swagger.v3.oas.models.media.Schema> subtypeProps = child.getProperties();
        if (baseProps != null && subtypeProps != null) {
            for (Map.Entry<String, io.swagger.v3.oas.models.media.Schema> entry : baseProps.entrySet()) {
                if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                    subtypeProps.remove(entry.getKey());
                }
            }
        }
        if (subtypeProps == null || subtypeProps.isEmpty()) {
            child.setProperties(null);
        }
    }

    private String getComment(BeanPropertyDefinition def) {
        if (def.hasField()) {
            Field field = def.getField().getAnnotated();
            return RuntimeJavadocUtils.getComment(field);
        } else if (def.hasGetter()) {
            Method method = def.getGetter().getAnnotated();
            return RuntimeJavadocUtils.getComment(method);
        }
        return "";
    }

    private List<String> getIgnoredProperties(BeanDescription beanDescription) {
        AnnotationIntrospector introspector = _mapper.getSerializationConfig().getAnnotationIntrospector();
        String[] ignored = introspector.findPropertiesToIgnore(beanDescription.getClassInfo(), true);
        return ignored == null ? Collections.emptyList() : Arrays.asList(ignored);
    }

    private boolean hiddenByJsonView(Annotation[] annotations,
                                     AnnotatedType type) {
        JsonView jsonView = type.getJsonViewAnnotation();
        if (jsonView == null) {
            return false;
        }

        Class<?>[] filters = jsonView.value();
        boolean containsJsonViewAnnotation = false;
        for (Annotation ant : annotations) {
            if (ant instanceof JsonView) {
                containsJsonViewAnnotation = true;
                Class<?>[] views = ((JsonView) ant).value();
                for (Class<?> f : filters) {
                    for (Class<?> v : views) {
                        if (v == f || v.isAssignableFrom(f)) {
                            return false;
                        }
                    }
                }
            }
        }
        return containsJsonViewAnnotation;
    }

    private void resolveArraySchema(AnnotatedType annotatedType, ArraySchema schema, io.swagger.v3.oas.annotations.media.ArraySchema resolvedArrayAnnotation) {
        Integer minItems = resolveMinItems(annotatedType, resolvedArrayAnnotation);
        if (minItems != null) {
            schema.minItems(minItems);
        }
        Integer maxItems = resolveMaxItems(annotatedType, resolvedArrayAnnotation);
        if (maxItems != null) {
            schema.maxItems(maxItems);
        }
        Boolean uniqueItems = resolveUniqueItems(annotatedType, resolvedArrayAnnotation);
        if (uniqueItems != null) {
            schema.uniqueItems(uniqueItems);
        }
        Map<String, Object> extensions = resolveExtensions(annotatedType, resolvedArrayAnnotation);
        if (extensions != null) {
            schema.extensions(extensions);
        }
        if (resolvedArrayAnnotation != null) {
            if (AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.arraySchema())) {
                resolveSchemaMembers(schema, null, null, resolvedArrayAnnotation.arraySchema());
            }
        }
    }

    @Override
    protected String resolveDescription(Annotated a, Annotation[] annotations, Schema schema) {
        String description = super.resolveDescription(a, annotations, schema);
        if (StringUtils.isBlank(description)) {
            if (a.getType() != null) {
                Class<?> rawClass = a.getType().getRawClass();
                return RuntimeJavadocUtils.getComment(rawClass);
            }
        }
        return "";
    }


    private enum GeneratorWrapper {
        PROPERTY(ObjectIdGenerators.PropertyGenerator.class) {
            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsProperty(String propertyName, AnnotatedType type,
                                                                              ModelConverterContext context, ObjectMapper mapper) {
                /*
                 * When generator = ObjectIdGenerators.PropertyGenerator.class and
                 * @JsonIdentityReference(alwaysAsId = false) then property is serialized
                 * in the same way it is done without @JsonIdentityInfo annotation.
                 */
                return null;
            }

            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsId(String propertyName, AnnotatedType type,
                                                                        ModelConverterContext context, ObjectMapper mapper) {
                final JavaType javaType;
                if (type.getType() instanceof JavaType) {
                    javaType = (JavaType) type.getType();
                } else {
                    javaType = mapper.constructType(type.getType());
                }
                final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
                for (BeanPropertyDefinition def : beanDesc.findProperties()) {
                    final String name = def.getName();
                    if (name != null && name.equals(propertyName)) {
                        final AnnotatedMember propMember = def.getPrimaryMember();
                        final JavaType propType = propMember.getType();
                        if (PrimitiveType.fromType(propType) != null) {
                            return PrimitiveType.createProperty(propType);
                        } else {
                            List<Annotation> list = new ArrayList<>();
                            for (Annotation a : propMember.annotations()) {
                                list.add(a);
                            }
                            Annotation[] annotations = list.toArray(new Annotation[list.size()]);
                            Annotation propSchemaOrArray = AnnotationsUtils.mergeSchemaAnnotations(annotations, propType);
                            AnnotatedType aType = new AnnotatedType()
                                    .type(propType)
                                    .ctxAnnotations(annotations)
                                    .jsonViewAnnotation(type.getJsonViewAnnotation())
                                    .schemaProperty(true)
                                    .propertyName(type.getPropertyName());

                            return context.resolve(aType);
                        }
                    }
                }
                return null;
            }
        },
        INT(ObjectIdGenerators.IntSequenceGenerator.class) {
            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsProperty(String propertyName, AnnotatedType type,
                                                                              ModelConverterContext context, ObjectMapper mapper) {
                io.swagger.v3.oas.models.media.Schema id = new IntegerSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsId(String propertyName, AnnotatedType type,
                                                                        ModelConverterContext context, ObjectMapper mapper) {
                return new IntegerSchema();
            }
        },
        UUID(ObjectIdGenerators.UUIDGenerator.class) {
            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsProperty(String propertyName, AnnotatedType type,
                                                                              ModelConverterContext context, ObjectMapper mapper) {
                io.swagger.v3.oas.models.media.Schema id = new UUIDSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsId(String propertyName, AnnotatedType type,
                                                                        ModelConverterContext context, ObjectMapper mapper) {
                return new UUIDSchema();
            }
        },
        NONE(ObjectIdGenerators.None.class) {
            // When generator = ObjectIdGenerators.None.class property should be processed as normal property.
            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsProperty(String propertyName, AnnotatedType type,
                                                                              ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }

            @Override
            protected io.swagger.v3.oas.models.media.Schema processAsId(String propertyName, AnnotatedType type,
                                                                        ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }
        };

        private final Class<? extends ObjectIdGenerator> generator;

        GeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
            this.generator = generator;
        }

        public static io.swagger.v3.oas.models.media.Schema processJsonIdentity(AnnotatedType type, ModelConverterContext context,
                                                                                ObjectMapper mapper, JsonIdentityInfo identityInfo,
                                                                                JsonIdentityReference identityReference) {
            final RainbowModelResolver.GeneratorWrapper wrapper = identityInfo != null ? getWrapper(identityInfo.generator()) : null;
            if (wrapper == null) {
                return null;
            }
            if (identityReference != null && identityReference.alwaysAsId()) {
                return wrapper.processAsId(identityInfo.property(), type, context, mapper);
            } else {
                return wrapper.processAsProperty(identityInfo.property(), type, context, mapper);
            }
        }

        private static RainbowModelResolver.GeneratorWrapper getWrapper(Class<?> generator) {
            for (RainbowModelResolver.GeneratorWrapper value : RainbowModelResolver.GeneratorWrapper.values()) {
                if (value.generator.isAssignableFrom(generator)) {
                    return value;
                }
            }
            return null;
        }

        private static io.swagger.v3.oas.models.media.Schema process(io.swagger.v3.oas.models.media.Schema id, String propertyName, AnnotatedType type,
                                                                     ModelConverterContext context) {

            io.swagger.v3.oas.models.media.Schema model = context.resolve(removeJsonIdentityAnnotations(type));
            io.swagger.v3.oas.models.media.Schema mi = model;
            mi.addProperties(propertyName, id);
            return new io.swagger.v3.oas.models.media.Schema().$ref(StringUtils.isNotEmpty(mi.get$ref())
                    ? mi.get$ref() : mi.getName());
        }

        private static AnnotatedType removeJsonIdentityAnnotations(AnnotatedType type) {
            return new AnnotatedType()
                    .jsonUnwrappedHandler(type.getJsonUnwrappedHandler())
                    .jsonViewAnnotation(type.getJsonViewAnnotation())
                    .name(type.getName())
                    .parent(type.getParent())
                    .resolveAsRef(false)
                    .schemaProperty(type.isSchemaProperty())
                    .skipOverride(type.isSkipOverride())
                    .skipSchemaName(type.isSkipSchemaName())
                    .type(type.getType())
                    .skipJsonIdentity(true)
                    .propertyName(type.getPropertyName())
                    .ctxAnnotations(AnnotationsUtils.removeAnnotations(type.getCtxAnnotations(), JsonIdentityInfo.class, JsonIdentityReference.class));
        }

        protected abstract io.swagger.v3.oas.models.media.Schema processAsProperty(String propertyName, AnnotatedType type,
                                                                                   ModelConverterContext context, ObjectMapper mapper);

        protected abstract io.swagger.v3.oas.models.media.Schema processAsId(String propertyName, AnnotatedType type,
                                                                             ModelConverterContext context, ObjectMapper mapper);
    }

}
