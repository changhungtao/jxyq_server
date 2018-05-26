package com.jxyq.model.others;

public class ErrorMessage {
    private String debuginfo;
    private Integer faultcode;
    private String faultstring;

    public ErrorMessage(){
        this.debuginfo = null;
        this.faultcode = null;
        this.faultstring = null;
    }

    public ErrorMessage(Integer faultcode){
        this.debuginfo = null;
        this.faultcode = faultcode;
        this.faultstring = null;
    }

    public ErrorMessage(String faultstring){
        this.debuginfo = null;
        this.faultcode = null;
        this.faultstring = faultstring;
    }

    public ErrorMessage(Integer faultcode, String faultstring){
        this.debuginfo = null;
        this.faultcode = faultcode;
        this.faultstring = faultstring;
    }

    public String getDebuginfo() {
        return debuginfo;
    }

    public void setDebuginfo(String debuginfo) {
        this.debuginfo = debuginfo;
    }

    public Integer getFaultcode() {
        return faultcode;
    }

    public void setFaultcode(Integer faultcode) {
        this.faultcode = faultcode;
    }

    public String getFaultstring() {
        return faultstring;
    }

    public void setFaultstring(String faultstring) {
        this.faultstring = faultstring;
    }
}
