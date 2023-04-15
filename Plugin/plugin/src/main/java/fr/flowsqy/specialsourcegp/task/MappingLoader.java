package fr.flowsqy.specialsourcegp.task;

import fr.flowsqy.specialsourcegp.data.MappingData;
import fr.flowsqy.specialsourcegp.data.ShadeRelocationData;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.InheritanceProvider;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.TaskExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MappingLoader {

    private final Task task;

    public MappingLoader(Task task) {
        this.task = task;
    }

    public JarMapping load(MappingData mappingData) throws IOException {
        final ConfigurableFileCollection mappingFilesProperty = mappingData.getMappingFiles();

        if (mappingFilesProperty.isEmpty()) {
            throw new TaskExecutionException(task, new IllegalArgumentException("No mapping field specified"));
        }

        final boolean numeric = mappingData.getNumeric().getOrElse(false);
        final boolean reverse = mappingData.getReverse().getOrElse(false);
        final ShadeRelocationData shadeRelocationData = mappingData.getShadeRelocation();
        final String inShadeRelocation = shadeRelocationData.getInbound().getOrNull();
        final String outShadeRelocation = shadeRelocationData.getOutbound().getOrNull();
        final List<String> excludedPackages = mappingData.getExcludedPackages().get();

        final JarMapping jarMapping = new JarMapping();

        loadPackages(jarMapping, excludedPackages);

        for (File mappingFilePath : mappingFilesProperty.getFiles()) {
            jarMapping.loadMappings(mappingFilePath.getAbsolutePath(), reverse, numeric, inShadeRelocation, outShadeRelocation);
        }

        final InheritanceLoader inheritanceLoader = new InheritanceLoader();
        final InheritanceProvider inheritanceProvider = inheritanceLoader.load(mappingData.getInheritance(), jarMapping);
        jarMapping.setFallbackInheritanceProvider(inheritanceProvider);

        return jarMapping;
    }

    private void loadPackages(JarMapping jarMapping, List<String> excludedPackages) {
        for (String excludedPackage : excludedPackages) {
            jarMapping.addExcludedPackage(excludedPackage);
        }
    }

}
