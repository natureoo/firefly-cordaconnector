package io.kaleido.firefly.cordapp.metadata;

import kotlin.jvm.internal.Intrinsics;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CordaSerializable
public final class RemittanceInformation {
    
    private List<String> narratives = new ArrayList<>();

    
    public final List<String> getNarratives() {
        return this.narratives;
    }

    public RemittanceInformation( List<String> narratives) {
        this.narratives = narratives;
    }

    public RemittanceInformation(){}

    public void setNarratives( List<String> narratives) {
        this.narratives = narratives;
    }

    
    public String toString() {
        return "RemittanceInformation(narratives=" + this.narratives + ")";
    }


}
