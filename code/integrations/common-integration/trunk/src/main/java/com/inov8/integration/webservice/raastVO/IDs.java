package com.inov8.integration.webservice.raastVO;


import java.io.Serializable;

public class IDs implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    private String customerID;
    private String aliasID;
    private String accountID;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getAliasID() {
        return aliasID;
    }

    public void setAliasID(String aliasID) {
        this.aliasID = aliasID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
}