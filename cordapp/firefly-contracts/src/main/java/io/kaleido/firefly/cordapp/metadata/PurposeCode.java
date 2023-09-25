package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

@CordaSerializable
public final class PurposeCode {
    
    private String purposeCode;

    
    public final String getPurposeCode() {
        return this.purposeCode;
    }

    public PurposeCode( String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public PurposeCode(){}

    public void setPurposeCode( String purposeCode) {
        this.purposeCode = purposeCode;
    }

    
    public String toString() {
        return "PurposeCode(purposeCode=" + this.purposeCode + ")";
    }


}
