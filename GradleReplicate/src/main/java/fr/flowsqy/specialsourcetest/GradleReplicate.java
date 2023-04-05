package fr.flowsqy.specialsourcetest;

import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.InheritanceProvider;
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

        final InheritanceProvider inheritanceProvider = createInheritanceProvider(assetsFolder, originalJar);
        mapping.setFallbackInheritanceProvider(inheritanceProvider);

        final File outputFile = new File(assetsFolder, "Output.jar");

        remap(originalJar, outputFile, mapping);
    }

    private static void remap(final Jar originalJar, final File outputFile, final JarMapping mapping) throws IOException {
        // Do the remap
        JarRemapper remapper = new JarRemapper(null, mapping, null);
        remapper.setGenerateAPI(false);
        remapper.remapJar(originalJar, outputFile);
    }

    private static InheritanceProvider createInheritanceProvider(File assetsFolder, Jar originalJar) throws IOException {
        final JointProvider inheritanceProviders = new JointProvider();

        // In the context of a GradlePlugin, this should be off
        boolean useRuntimeClassPath = false; // default to false
        if (useRuntimeClassPath) {
            inheritanceProviders.add(new ClassLoaderProvider(ClassLoader.getSystemClassLoader()));
        }

        // Add every object that contains class that are inherited by the jar that we want remap
        // Here all dependencies
        final File spigotDependency = new File(assetsFolder, "spigot-1.19.3-R0.1-SNAPSHOT-remapped-mojang.jar");
        inheritanceProviders.add(new JarProvider(Jar.init(spigotDependency)));

        // Add ourselves
        inheritanceProviders.add(new JarProvider(originalJar));

        /*
        Can load custom inheritance map from a file (Shouldn't be useful in our case)

        InheritanceMap inheritanceMap = new InheritanceMap();

        inheritanceMap.load(reader, inverseClassMap);

        inheritanceProviders.add(inheritanceMap);
        */
        return inheritanceProviders;
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
