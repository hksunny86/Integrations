package com.inov8.integration.webservice.raastVO;


import java.io.Serializable;

public class Uid implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
