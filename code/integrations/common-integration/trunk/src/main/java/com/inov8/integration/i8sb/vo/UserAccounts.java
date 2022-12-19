package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("UserAccounts")
public class UserAccounts implements Serializable {

    private final static long serialVersionUID = 1L;

    private String userAccTitle;
    private String userAccType;

    public String getUserAccTitle() {
        return userAccTitle;
    }

    public void setUserAccTitle(String userAccTitle) {
        this.userAccTitle = userAccTitle;
    }

    public String getUserAccType() {
        return userAccType;
    }

    public void setUserAccType(String userAccType) {
        this.userAccType = userAccType;
    }
}
