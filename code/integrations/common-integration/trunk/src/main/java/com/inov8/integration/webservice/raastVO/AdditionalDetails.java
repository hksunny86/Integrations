package com.inov8.integration.webservice.raastVO;


import java.io.Serializable;

public class AdditionalDetails implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    private String dba;
    private String mcc;

    public String getDba() {
        return dba;
    }

    public void setDba(String dba) {
        this.dba = dba;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }
}
