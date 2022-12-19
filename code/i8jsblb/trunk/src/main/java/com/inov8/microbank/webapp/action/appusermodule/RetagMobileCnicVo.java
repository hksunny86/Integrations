package com.inov8.microbank.webapp.action.appusermodule;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

public class RetagMobileCnicVo extends BasePersistableModel {
	
	private String oldMobileNo;
	private String newMobileNo;
	private String oldCnic;
	private String newCnic;
	private String comments;
	private Boolean isMobileUpdate;
	private Boolean pendingTrxExists;
	private Boolean customerExits;
	private Boolean walkinExists;
	private Boolean isCnicUpdate;
	private Long appUserId;
	private Long createdBy;
	private Date createdOn;
	
	public RetagMobileCnicVo(String currentMobileNo, String currentCnic, Long appUserId) {
		this.oldMobileNo = currentMobileNo;
		this.oldCnic = currentCnic;
		this.appUserId= appUserId;
		this.pendingTrxExists=false;
		this.customerExits=false;
		this.walkinExists=false;
		this.isCnicUpdate=false;
	}
	
	public RetagMobileCnicVo() {
		super();
	}
	
	public String getOldMobileNo() {
		return oldMobileNo;
	}
	public void setOldMobileNo(String oldMobileNo) {
		this.oldMobileNo = oldMobileNo;
	}
	public String getNewMobileNo() {
		return newMobileNo;
	}
	public void setNewMobileNo(String newMobileNo) {
		this.newMobileNo = newMobileNo;
	}
	public String getOldCnic() {
		return oldCnic;
	}
	public void setOldCnic(String oldCnic) {
		this.oldCnic = oldCnic;
	}
	public String getNewCnic() {
		return newCnic;
	}
	public void setNewCnic(String newCnic) {
		this.newCnic = newCnic;
	}

	public Boolean getIsMobileUpdate() {
		return isMobileUpdate;
	}

	public void setIsMobileUpdate(Boolean isMobileUpdate) {
		this.isMobileUpdate = isMobileUpdate;
	}

	public Boolean getPendingTrxExists() {
		return pendingTrxExists;
	}

	public void setPendingTrxExists(Boolean pendingTrxExists) {
		this.pendingTrxExists = pendingTrxExists;
	}

	public Boolean getCustomerExits() {
		return customerExits;
	}

	public void setCustomerExits(Boolean customerExits) {
		this.customerExits = customerExits;
	}

	public Boolean getWalkinExists() {
		return walkinExists;
	}

	public void setWalkinExists(Boolean walkinExists) {
		this.walkinExists = walkinExists;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Boolean getIsCnicUpdate() {
		return isCnicUpdate;
	}

	public void setIsCnicUpdate(Boolean isCnicUpdate) {
		this.isCnicUpdate = isCnicUpdate;
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
