package com.inov8.integration.webservice.l2Account;

import java.io.Serializable;
import java.util.List;

public class L2Account implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String id;
    private String accountId;
    private String accountName;
    private String description;
    private List<Details> details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }
}
