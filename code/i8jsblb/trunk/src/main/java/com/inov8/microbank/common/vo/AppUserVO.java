package com.inov8.microbank.common.vo;

import java.util.Date;

import com.inov8.framework.common.model.BasePersistableModel;

public class AppUserVO extends BasePersistableModel {
	
	private String appUserId;
	private String userName;
	private String mobileNo;
	private Date updatedOn;
	private String registerationStatus;
	private String accountState;
	
	

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getRegisterationStatus() {
		return registerationStatus;
	}

	public void setRegisterationStatus(String registerationStatus) {
		this.registerationStatus = registerationStatus;
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

}
