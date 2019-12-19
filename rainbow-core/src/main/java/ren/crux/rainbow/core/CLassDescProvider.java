package ren.crux.rainbow.core;

import ren.crux.rainbow.core.desc.model.ClassDesc;

import java.util.Optional;

public interface CLassDescProvider {

    Optional<ClassDesc> get(String className);

}
