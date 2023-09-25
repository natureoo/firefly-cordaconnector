package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CordaSerializable
public final class BankInformation {
    
    private List<String> bankInfos = new ArrayList<>();

    
    public final List<String> getBankInfos() {
        return this.bankInfos;
    }

    public BankInformation( List<String> bankInfos) {
        this.bankInfos = bankInfos;
    }

    public BankInformation(){

    }

    public void setBankInfos( List<String> bankInfos) {
        this.bankInfos = bankInfos;
    }
}
