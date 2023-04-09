package fr.flowsqy.specialsourcegp.data;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

public abstract class ShadeRelocationData {

    @Input
    @Optional
    public abstract Property<String> getInbound();

    @Input
    @Optional
    public abstract Property<String> getOutbound();


}
