package fr.flowsqy.specialsourcegp.data;

import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;

public abstract class InheritanceData {

    @Input
    @Optional
    public abstract Property<Boolean> getUseRunTimeClassPath();

    @InputFiles
    @Optional
    public abstract ListProperty<RegularFile> getJars();

    @InputFiles
    @Optional
    public abstract ListProperty<RegularFile> getConfigurations();

}
