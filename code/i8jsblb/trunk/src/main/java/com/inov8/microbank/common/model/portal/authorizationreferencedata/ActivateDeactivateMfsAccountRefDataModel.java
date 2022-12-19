package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name="activateDeactivateMfsAccountRefDataModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ActivateDeactivateMfsAccountRefDataModel implements Serializable {
	
	private String mfsId;
	private Long usecaseId;
	private String encryptedAppUserId;
	private Long actionId;
	private String islockUnlock;
	private String isAgent;
	private String comments;
	
	private String customeField1;
	private String customeField2;

	//Added By Shehryaar

	private Long paymentModeId;
	private String accountType;
	private String action;
	private String isHandler;
	
	public ActivateDeactivateMfsAccountRefDataModel() {

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
	public ActivateDeactivateMfsAccountRefDataModel(String mfsId,
			Long usecaseId, String encryptedAppUserId, Long actionId,
			String islockUnlock, String isAgent, String customeField1,
			String customeField2, String comments,Long paymentModeId,String accountType,String action,String isHandler) {
	
		this.mfsId = mfsId;
		this.usecaseId = usecaseId;
		this.encryptedAppUserId = encryptedAppUserId;
		this.actionId = actionId;
		this.islockUnlock = islockUnlock;
		this.isAgent = isAgent;
		this.comments = comments;
		this.customeField1 = customeField1;
		this.customeField2 = customeField2;

		this.paymentModeId = paymentModeId;
		this.accountType = accountType;
		this.action = action;
		this.isHandler = isHandler;
	}

	public String getEncryptedAppUserId() {
		return encryptedAppUserId;
	}
	public void setEncryptedAppUserId(String encryptedAppUserId) {
		this.encryptedAppUserId = encryptedAppUserId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getIslockUnlock() {
		return islockUnlock;
	}
	public void setIslockUnlock(String islockUnlock) {
		this.islockUnlock = islockUnlock;
	}
	public String getIsAgent() {
		return isAgent;
	}
	public void setIsAgent(String isAgent) {
		this.isAgent = isAgent;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIsHandler() {
		return isHandler;
	}

	public void setIsHandler(String isHandler) {
		this.isHandler = isHandler;
	}
}
