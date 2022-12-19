package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("CustomerAccount")
public class CustomerAccountVO implements Serializable
{
    private String relationshipNo;
    private String accountNo;

    public String getRelationshipNo() {
        return relationshipNo;
    }

    public void setRelationshipNo(String relationshipNo) {
        this.relationshipNo = relationshipNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
