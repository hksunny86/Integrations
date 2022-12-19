package com.inov8.integration.i8sb.vo;

import java.io.Serializable;

public class DebitCardCharges implements Serializable{
    private final static long serialVersionUID = 1L;
    private String pan;
    private String accountNo;
    private String relationshipNo;
    private String status;
    private String flag;


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRelationshipNo() {
        return relationshipNo;
    }

    public void setRelationshipNo(String relationshipNo) {
        this.relationshipNo = relationshipNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
