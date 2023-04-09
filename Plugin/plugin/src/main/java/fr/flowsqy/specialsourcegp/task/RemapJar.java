package fr.flowsqy.specialsourcegp.task;

import fr.flowsqy.specialsourcegp.data.MappingData;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

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

    @TaskAction
    public void remapJar() {
        System.out.println("Remap Jar");
    }

}
