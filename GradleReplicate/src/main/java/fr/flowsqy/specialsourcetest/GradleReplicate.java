package fr.flowsqy.specialsourcetest;

import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GradleReplicate {

    public static void main(String[] args) throws IOException {
        final File assetsFolder = new File("assets");

        configureGlobal();

        final JarMapping mapping = loadMappings(assetsFolder);

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

    private static void configureGlobal() {
        // Global settings
        SpecialSource.kill_source = false;
        SpecialSource.kill_lvt = false; // Could be set to true according to the wiki
        SpecialSource.kill_generics = false;
        SpecialSource.identifier = null;
        SpecialSource.stable = false;
    }

    private static JarMapping loadMappings(File assetsFolder) throws IOException {
        // Load mappings
        final List<String> srgIn = new LinkedList<>(); // requires one element

        final File testMappingFile = new File(assetsFolder, "mojang-server-mappings.txt");
        srgIn.add(testMappingFile.getAbsolutePath());

        boolean numeric = false; // default false
        boolean reverse = true; // default false
        String inShadeRelocation = null; // default null
        String outShadeRelocation = null; // default null
        List<String> excludedPackages = new LinkedList<>(); // default empty

        final JarMapping mapping = new JarMapping();

        for (String excludedPackage : excludedPackages) {
            mapping.addExcludedPackage(excludedPackage);
        }

        //BufferedReader mappingReader = new BufferedReader(new FileReader(mappingFile));
        for (String mappingFilePath : srgIn) {
            mapping.loadMappings(mappingFilePath, reverse, numeric, inShadeRelocation, outShadeRelocation);
        }
        return mapping;
    }

}
