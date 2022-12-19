package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by Administrator on 1/30/2018.
 */
@XStreamAlias("ActiveDirectoryUser")
public class ActiveDirectoryUser implements Serializable {
    private final static long serialVersionUID = 1L;

    private String name;
    private String fullName;
    private String email;
    private String userId;
    private String distinguishName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDistinguishName() {
        return distinguishName;
    }

    public void setDistinguishName(String distinguishName) {
        this.distinguishName = distinguishName;
    }
}
