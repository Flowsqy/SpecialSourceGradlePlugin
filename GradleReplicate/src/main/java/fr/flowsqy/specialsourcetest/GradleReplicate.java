package fr.flowsqy.specialsourcetest;

import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GradleReplicate {

    public static void main(String[] args) throws IOException {
        final File assetsFolder = new File("assets");

        // Load mappings
        final File mappingFile = new File(assetsFolder, "mojang-server-mappings.txt");
        boolean reverse = true;
        final JarMapping mapping = new JarMapping();
        BufferedReader mappingReader = new BufferedReader(new FileReader(mappingFile));
        mapping.loadMappings(mappingReader, null, null, reverse);

        // Load original jar
        final File originalJarFile = new File(assetsFolder, "Original.jar");
        final Jar originalJar = Jar.init(originalJarFile);

        // Load dependencies
        final File spigotDependency = new File(assetsFolder, "spigot-1.19.3-R0.1-SNAPSHOT-remapped-mojang.jar");
        final JointProvider jointProvider = new JointProvider();
        jointProvider.add(new JarProvider(Jar.init(spigotDependency)));
        // (Add all project dependencies)

        // ?
        jointProvider.add(new JarProvider(originalJar));
        mapping.setFallbackInheritanceProvider(jointProvider);

        final File outputFile = new File(assetsFolder, "Output.jar");

        // Do the remap
        JarRemapper remapper = new JarRemapper(null, mapping, null);
        remapper.setGenerateAPI(false);
        remapper.remapJar(originalJar, outputFile);

    }

}
