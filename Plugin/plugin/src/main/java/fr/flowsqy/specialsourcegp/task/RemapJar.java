package fr.flowsqy.specialsourcegp.task;

import fr.flowsqy.specialsourcegp.data.MappingData;
import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;

public abstract class RemapJar extends DefaultTask {

    //TODO Add Global settings
    //TODO Add pre-processor settings
    //TODO Add post-processor settings

    @InputFile
    public abstract RegularFileProperty getInput();

    @OutputFile
    public abstract RegularFileProperty getOutput();

    @Nested
    public abstract MappingData getMapping();

    public void mapping(@NotNull Action<? super MappingData> action) {
        action.execute(getMapping());
    }

    @Input
    @Optional
    public abstract Property<Boolean> getGenerateAPI();

    @Input
    @Optional
    public abstract ListProperty<String> getIncludes();

    @TaskAction
    public void remapJar() throws IOException {
        final MappingLoader mappingLoader = new MappingLoader(this);
        final JarMapping jarMapping = mappingLoader.load(getMapping());
        final JarRemapper remapper = new JarRemapper(null, jarMapping, null);
        remapper.setGenerateAPI(getGenerateAPI().getOrElse(false));
        remapper.remapJar(
                Jar.init(getInput().get().getAsFile()),
                getOutput().get().getAsFile(),
                new HashSet<>(getIncludes().get())
        );
    }

}
