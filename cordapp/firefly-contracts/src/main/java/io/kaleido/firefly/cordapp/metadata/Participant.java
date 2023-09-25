package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.identity.Party;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CordaSerializable
public class Participant {
    
    private Party identity;
    
    private String legalName;
    
    private String lei;
    
    private String bic;
    
    private String addressLn1;
    
    private String addressLn2;

    
    public Party getIdentity() {
        return this.identity;
    }

    
    public String getLegalName() {
        return this.legalName;
    }

    
    public String getLei() {
        return this.lei;
    }

    
    public String getBic() {
        return this.bic;
    }

    
    public String getAddressLn1() {
        return this.addressLn1;
    }

    
    public String getAddressLn2() {
        return this.addressLn2;
    }

    public Participant( Party identity,  String legalName,  String lei,  String bic,  String addressLn1,  String addressLn2) {

        this.identity = identity;
        this.legalName = legalName;
        this.lei = lei;
        this.bic = bic;
        this.addressLn1 = addressLn1;
        this.addressLn2 = addressLn2;
    }

    public Participant(){

    }

    
    public String toString() {
        return "Participant(identity=" + this.identity + ", legalName=" + this.legalName + ", lei=" + this.lei + ", bic=" + this.bic + ", addressLn1=" + this.addressLn1 + ", addressLn2=" + this.addressLn2 + ")";
    }

    public void setIdentity( Party identity) {
        this.identity = identity;
    }

    public void setLegalName( String legalName) {
        this.legalName = legalName;
    }

    public void setLei( String lei) {
        this.lei = lei;
    }

    public void setBic( String bic) {
        this.bic = bic;
    }

    public void setAddressLn1( String addressLn1) {
        this.addressLn1 = addressLn1;
    }

    public void setAddressLn2( String addressLn2) {
        this.addressLn2 = addressLn2;
    }
}
