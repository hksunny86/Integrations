package com.inov8.integration.middleware.mock.model;

import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 12/23/2015.
 */
public class JDBCConsumer implements Serializable{

    private String consumerNo;
    private String companyCode;
    private String companyCategory;
    private String userIdentifier;

    private String responseCode;
    private int ttl;

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
