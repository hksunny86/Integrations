package com.inov8.microbank.fonepay.model;

import java.util.Date;

import com.inov8.framework.common.model.BasePersistableModel;

public class CustomerVO extends BasePersistableModel {

    private Date updatedOn;
    private String customerId;
    private Boolean isWebServiceEnabled;
    private String updatedBy;
    
    private String mobileNo;
    private String name;

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getIsWebServiceEnabled() {
        return isWebServiceEnabled;
    }

    public void setIsWebServiceEnabled(Boolean isWebServiceEnabled) {
    	this.isWebServiceEnabled = isWebServiceEnabled;
    }
	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
