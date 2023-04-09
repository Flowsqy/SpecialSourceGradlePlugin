package fr.flowsqy.specialsourcegp.data;

import org.gradle.api.Action;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class MappingData {

    //TODO Add mapping files (required, list files)

    @Input
    @Optional
    public abstract Property<Boolean> getReverse();

    @Input
    @Optional
    public abstract Property<Boolean> getNumeric();

    @Nested
    public abstract InheritanceData getInheritance();

    public void inheritance(@NotNull Action<? super InheritanceData> action) {
        action.execute(getInheritance());
    }

    @Nested
    public abstract ShadeRelocationData getShadeRelocation();

    public void shadeRelocation(@NotNull Action<? super ShadeRelocationData> action) {
        action.execute(getShadeRelocation());
    }

    @Input
    @Optional
    public abstract ListProperty<String> getExcludedPackages();

}
