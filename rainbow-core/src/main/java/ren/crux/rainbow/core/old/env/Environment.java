//package ren.crux.rainbow.core.old.env;
//
//import com.google.common.base.MoreObjects;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import ren.crux.rainbow.core.tuple.Describable;
//
//import java.util.List;
//
//@EqualsAndHashCode(callSuper = true)
//@Data
//public class Environment extends Describable {
//    private List<Variable> variables;
//
//    @Override
//    public String toString() {
//        return MoreObjects.toStringHelper(this)
//                .add("variables", variables)
//                .add("name", name)
//                .add("qualifiedName", qualifiedName)
//                .add("description", description)
//                .omitNullValues()
//                .toString();
//    }
//}
