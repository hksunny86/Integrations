package com.inov8.microbank.updatecustomername.vo;

import java.io.Serializable;

public class UpdateCustomerNameVo implements Serializable, Cloneable {

    private Long updateCustomerNameId;
    private String cnic;
    private String mobileNo;
    private String firstName;
    private String lastName;
    private Boolean updated;
    private String nadraName;
    private Long actionStatus;


    public Long getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Long actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getNadraName() {
        return nadraName;
    }

    public void setNadraName(String nadraName) {
        this.nadraName = nadraName;
    }

    public Long getUpdateCustomerNameId() {
        return updateCustomerNameId;
    }

    public void setUpdateCustomerNameId(Long updateCustomerNameId) {
        this.updateCustomerNameId = updateCustomerNameId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }
}
