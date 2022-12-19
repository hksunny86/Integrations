package com.inov8.integration.vo;

import java.io.Serializable;

public class CatalogList implements Serializable {

    private String catalogId;
    private String catalogName;

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
