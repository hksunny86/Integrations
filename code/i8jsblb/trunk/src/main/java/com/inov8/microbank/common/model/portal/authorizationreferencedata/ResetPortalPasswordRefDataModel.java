package com.inov8.microbank.common.model.portal.authorizationreferencedata;

public class ResetPortalPasswordRefDataModel {
	private String encriptedAppUserId;
	private String notifyBySms;
	private String MfsId;
	private String username;
	
	public ResetPortalPasswordRefDataModel() {	
	}	
	
	
	public ResetPortalPasswordRefDataModel(String encriptedAppUserId,String notifyBySms, String mfsId,String username) {
	
		this.encriptedAppUserId = encriptedAppUserId;
		this.notifyBySms = notifyBySms;
		this.MfsId = mfsId;
		this.username=username;
	}


	public String getEncriptedAppUserId() {
		return encriptedAppUserId;
	}
	public void setEncriptedAppUserId(String encriptedAppUserId) {
		this.encriptedAppUserId = encriptedAppUserId;
	}
	public String getNotifyBySms() {
		return notifyBySms;
	}	
	public void setNotifyBySms(String notifyBySms) {
		this.notifyBySms = notifyBySms;
	}


	public String getMfsId() {
		return MfsId;
	}

	public void setMfsId(String mfsId) {
		MfsId = mfsId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

}
