package com.inov8.integration.i8sb.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 4/8/2019.
 */
public class DebitCardStatusVO implements Serializable{
    private final static long serialVersionUID = 1L;
    private String pan;
    private String accountNo;
    private String relationshipNo;
    private String createdOn;
    private String status;
    private String flag;
    private String updatedOn;
    private String cardTitle;






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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }
}
