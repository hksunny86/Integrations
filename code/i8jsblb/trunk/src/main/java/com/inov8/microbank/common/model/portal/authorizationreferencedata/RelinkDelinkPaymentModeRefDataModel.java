package com.inov8.microbank.common.model.portal.authorizationreferencedata;

public class RelinkDelinkPaymentModeRefDataModel {

	private String mfsId;
	private Long usecaseId;
	private String encryptedAppUserId;
	private String smaAccountId;
	private Long actionId;
	private String isDelete;
	private String comments;
	
	private String customeField1;
	private String customeField2;
	
	public RelinkDelinkPaymentModeRefDataModel() {
		
	}
	
	public RelinkDelinkPaymentModeRefDataModel(String mfsId, Long usecaseId,
			String encryptedAppUserId, String smaAccountId, Long actionId,
			String isDelete,String comments) {
		super();
		this.mfsId = mfsId;
		this.usecaseId = usecaseId;
		this.encryptedAppUserId = encryptedAppUserId;
		this.smaAccountId = smaAccountId;
		this.actionId = actionId;
		this.isDelete = isDelete;
		this.comments = comments;
	}
	public String getMfsId() {
		return mfsId;
	}

	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}

	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}

	public String getEncryptedAppUserId() {
		return encryptedAppUserId;
	}

	public void setEncryptedAppUserId(String encryptedAppUserId) {
		this.encryptedAppUserId = encryptedAppUserId;
	}

	public String getSmaAccountId() {
		return smaAccountId;
	}

	public void setSmaAccountId(String smaAccountId) {
		this.smaAccountId = smaAccountId;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getCustomeField1() {
		return customeField1;
	}

	public void setCustomeField1(String customeField1) {
		this.customeField1 = customeField1;
	}

	public String getCustomeField2() {
		return customeField2;
	}

	public void setCustomeField2(String customeField2) {
		this.customeField2 = customeField2;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

}
