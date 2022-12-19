package com.inov8.microbank.common.model.retailermodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;

/**
 * @author Atif Hussain
 * 
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "HANDLER_SEARCH_VIEW")
public class HandlerSearchViewModel extends BasePersistableModel {

	private static final long serialVersionUID = -6948754761381338795L;
	private Long handlerId;
	private String userId;
	private String handlerName;
	private Long areaId;
	private String agentName;
	private String areaName;
	private String contactNo;
	private Boolean active;
	private String agentId;
	private String userName;
	private Long appUserId;
	private String cnic;
	private Integer index;
	private String accountClosed;
	private Long olaCustomerAccountTypeId;
	private String olaCustomerAccountTypeName;
	private Long retailerContactId;

	@Id
	@Column(name = "HANDLER_ID", nullable = false)
	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

	@Column(name = "AREA_ID", nullable = false)
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "AGENT_NAME", nullable = false)
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Column(name = "AREA_NAME", nullable = false)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "CONTACT_NO", nullable = false)
	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return getHandlerId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		String primaryKeyFieldName = "handlerId";
		return primaryKeyFieldName;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		String parameters = "";
		parameters += "&handlerId=" + getHandlerId();
		return parameters;
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long key) {
		// TODO Auto-generated method stub
		setHandlerId(key);
	}

	@Column(name = "AGENT_ID", nullable = false)
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@Column(name = "USER_NAME", nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "APP_USER_ID", nullable = false)
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name = "NIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Transient
	public Integer getIndex() {
		return index;
	}

	@Transient
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the isAccountClosed
	 */
	@Column(name = "IS_ACCOUNT_CLOSED")
	public String getAccountClosed() {
		return accountClosed;
	}

	public void setAccountClosed(String accountClosed) {
		this.accountClosed = accountClosed;
	}

	/**
	 * @return olaCustomerAccountTypeId
	 */
	@Column(name = "OLA_CUSTOMER_ACCOUNT_TYPE_ID", nullable = false)
	public Long getOlaCustomerAccountTypeId() {
		return olaCustomerAccountTypeId;
	}

	/**
	 * @param olaCustomerAccountTypeId
	 */
	public void setOlaCustomerAccountTypeId(Long olaCustomerAccountTypeId) {
		this.olaCustomerAccountTypeId = olaCustomerAccountTypeId;
	}

	/**
	 * @return olaCustomerAccountTypeName
	 */
	@Column(name = "OLA_CUSTOMER_ACCOUNT_TYPE_NAME", nullable = false)
	public String getOlaCustomerAccountTypeName() {
		return olaCustomerAccountTypeName;
	}

	/**
	 * @param olaCustomerAccountTypeName
	 */
	public void setOlaCustomerAccountTypeName(String olaCustomerAccountTypeName) {
		this.olaCustomerAccountTypeName = olaCustomerAccountTypeName;
	}

	@Column(name = "HANDLER_NAME", nullable = false)
	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	@Column(name = "RETAILER_CONTACT_ID")
	public Long getRetailerContactId() {
		return retailerContactId;
	}

	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId = retailerContactId;
	}

	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
