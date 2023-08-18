package com.inov8.integration.webservice.l2Account;


import java.io.Serializable;
import java.util.List;

public class Details implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String title;
    private List<String> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
