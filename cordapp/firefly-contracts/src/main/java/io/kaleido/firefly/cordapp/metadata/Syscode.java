package io.kaleido.firefly.cordapp.metadata;

import org.jetbrains.annotations.NotNull;

public enum Syscode {
    TRANSACTION_REF_NUM_EXIST("408", "transactionRefNum exist");

    
    private String code;
    
    private String msg;

    
    public String toString() {
        return "{code:" + this.code + ", msg:" + this.msg + '}';
    }

    
    public final String getCode() {
        return this.code;
    }

    
    public final String getMsg() {
        return this.msg;
    }

    private Syscode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode( String code) {
        this.code = code;
    }

    public void setMsg( String msg) {
        this.msg = msg;
    }
}
