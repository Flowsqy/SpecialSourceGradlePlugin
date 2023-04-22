package fr.flowsqy.specialsourcegp.task;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.flowsqy.specialsourcegp.data.InheritanceData;
import net.md_5.specialsource.InheritanceMap;
import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.gradle.api.file.RegularFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InheritanceLoader {

    public InheritanceProvider load(InheritanceData inheritanceData, JarMapping jarMapping) throws IOException {
        final JointProvider inheritanceProvider = new JointProvider();

        final boolean useRuntimeClassPath = inheritanceData.getUseRunTimeClassPath().getOrElse(false);
        if (useRuntimeClassPath) {
            inheritanceProvider.add(new ClassLoaderProvider(ClassLoader.getSystemClassLoader()));
        }

        final List<RegularFile> jars = inheritanceData.getJars().get();
        if (!jars.isEmpty()) {
            final Jar jar = Jar.init(jars.stream().map(RegularFile::getAsFile).collect(Collectors.toList()));
            inheritanceProvider.add(new JarProvider(jar));
        }

        final List<RegularFile> configurations = inheritanceData.getConfigurations().get();
        if (!configurations.isEmpty()) {
            final InheritanceMap inheritanceMap = new InheritanceMap();
            final BiMap<String, String> inverseClassMap = HashBiMap.create(jarMapping.classes).inverse();
            for (RegularFile configuration : configurations) {
                final BufferedReader reader = new BufferedReader(new FileReader(configuration.getAsFile()));
                inheritanceMap.load(reader, inverseClassMap);
                reader.close();
            }
            inheritanceProvider.add(inheritanceMap);
        }

        return inheritanceProvider;
    }

}
