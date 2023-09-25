package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CordaSerializable
public class AccountInfo {
    
    private String accountNumber;
    
    private String accountName;
    
    private String addressLn1;
    
    private String addressLn2;
    
    private String bic;
    
    private String ultimateName;

    
    public String getAccountNumber() {
        return this.accountNumber;
    }

    
    public String getAccountName() {
        return this.accountName;
    }

    
    public String getAddressLn1() {
        return this.addressLn1;
    }

    
    public String getAddressLn2() {
        return this.addressLn2;
    }

    
    public String getBic() {
        return this.bic;
    }

    
    public String getUltimateName() {
        return this.ultimateName;
    }

    public AccountInfo( String accountNumber,  String accountName,  String addressLn1,  String addressLn2,  String bic,  String ultimateName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.addressLn1 = addressLn1;
        this.addressLn2 = addressLn2;
        this.bic = bic;
        this.ultimateName = ultimateName;
    }

    // $FF: synthetic method
    public AccountInfo() {

    }

    
    public String toString() {
        return "AccountInfo(accountNumber=" + this.accountNumber + ", accountName=" + this.accountName + ", addressLn1=" + this.addressLn1 + ", addressLn2=" + this.addressLn2 + ", bic=" + this.bic + ", ultimateName=" + this.ultimateName + ")";
    }

    public void setAccountNumber( String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountName( String accountName) {
        this.accountName = accountName;
    }

    public void setAddressLn1( String addressLn1) {
        this.addressLn1 = addressLn1;
    }

    public void setAddressLn2( String addressLn2) {
        this.addressLn2 = addressLn2;
    }

    public void setBic( String bic) {
        this.bic = bic;
    }

    public void setUltimateName( String ultimateName) {
        this.ultimateName = ultimateName;
    }
}
