//package ren.crux.rainbow.core.old.parser;
//
//import com.sun.javadoc.AnnotationDesc;
//import com.sun.javadoc.MethodDoc;
//import com.sun.javadoc.Parameter;
//import ren.crux.rainbow.core.model.Body;
//import ren.crux.rainbow.core.parser.Context;
//import ren.crux.rainbow.core.parser.JavaDocParser;
//
///**
// * @author wangzhihui
// */
//public class ParameterParser implements JavaDocParser<MethodDoc, Body> {
//
//    @Override
//    public boolean condition(Context context, MethodDoc source) {
//        return source.isMethod() && context.isRequest(source);
//    }
//
//    @Override
//    public Body parse(Context context, MethodDoc source) {
//        Body body = new Body();
//        Parameter[] parameters = source.parameters();
//        for (Parameter parameter : parameters) {
//            System.out.println("parameter.name() = " + parameter.name());
//            String typeQualifiedName = parameter.type().qualifiedTypeName();
//            System.out.println("parameter.typeName() = " + typeQualifiedName);
//            context.getEntry(typeQualifiedName).map(context::getRef).ifPresent(ref -> {
//            });
//
//            AnnotationDesc[] annotations = parameter.annotations();
//            for (AnnotationDesc annotation : annotations) {
//                System.out.println("annotation = " + annotation);
//            }
//        }
//        return body;
//    }
//}
