package fr.flowsqy.specialsourcegp.data;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

public abstract class InheritanceData {

    @Input
    @Optional
    public abstract Property<Boolean> getUseRunTimeClassPath();

    //TODO Add jars (optional, list files)

    //TODO Add inheritanceMap file loading (optional, list files)

}
