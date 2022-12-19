package com.inov8.microbank.common.model.retailermodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author AbuTu
 *
 */
@XmlRootElement(name="retailerContactSearchViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name="RETAILER_CONTACT_SEARCH_VIEW")
public class RetailerContactSearchViewModel extends BasePersistableModel{
	
	private static final long serialVersionUID = -6948754761381338795L;
	private Long retailerContactId;
	private Long retailerId;
	private Long regionId;
	private Long distributorId;
	private Long areaId;
	private String agentName;
	private String regionName;
	private String distributorName;
	private String retailerName;
	private String areaName;
	private String contactNo;
	private Boolean active;
	private String agentId;
	private String userName;
	private String firstName;
	private String lastName;
	private String accountNick;
	private Long appUserId;
	private String agentLevelName;
	private String areaLevelName;
	private String partnerGroupName;
	private String cnic;
	private Date cnicExpiryDate;
	private Boolean head;
	private Integer index;
	private String accountClosed;
	private Long olaCustomerAccountTypeId;
	private String olaCustomerAccountTypeName;
	private String saleUserName;
	private String roleTitle;
	
	@Id
	@Column( name="RETAILER_CONTACT_ID" , nullable=false)
	public Long getRetailerContactId() {
		return retailerContactId;
	}
	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId = retailerContactId;
	}
	@Column( name="RETAILER_ID" , nullable=false)
	public Long getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}
	@Column( name="REGION_ID" , nullable=false)
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	@Column( name="DISTRIBUTOR_ID" , nullable=false)
	public Long getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}
	@Column( name="AREA_ID" , nullable=false)
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	@Column( name="AGENT_NAME" , nullable=false)
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	@Column( name="REGION_NAME" , nullable=false)
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	@Column( name="DISTRIBUTOR_NAME" , nullable=false)
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	@Column( name="RETAILER_NAME" , nullable=false)
	public String getRetailerName() {
		return retailerName;
	}
	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}
	@Column( name="AREA_NAME" , nullable=false)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Column( name="CONTACT_NO" , nullable=false)
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	@Column( name="IS_ACTIVE" , nullable=false)
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return getRetailerContactId();
	}
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		String primaryKeyFieldName = "retailerContactId";
		return primaryKeyFieldName;
	}
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		 String parameters = "";
	      parameters += "&retailerContactId=" + getRetailerContactId();
	      return parameters;
	}
	@javax.persistence.Transient
	public void setPrimaryKey(Long key) {
		// TODO Auto-generated method stub
		setRetailerContactId(key);
	}	
	@Column(name="AGENT_ID", nullable= false)
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	@Column(name="USER_NAME", nullable= false)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="FIRST_NAME", nullable= false)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Column(name="LAST_NAME", nullable= false)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Column(name="NICK_NAME", nullable= false)
	public String getAccountNick() {
		return accountNick;
	}
	public void setAccountNick(String accountNick) {
		this.accountNick = accountNick;
	}
	@Column(name="APP_USER_ID" , nullable= false)
	public Long getAppUserId() {
		return appUserId;
	}
	
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	
	@Column(name="AGENT_LEVEL_NAME")
	public String getAgentLevelName() {
		return agentLevelName;
	}
	public void setAgentLevelName(String agentLevelName) {
		this.agentLevelName = agentLevelName;
	}
	
	@Column(name="PARTNER_GROUP_NAME")
	public String getPartnerGroupName() {
		return partnerGroupName;
	}
	public void setPartnerGroupName(String partnerGroupName) {
		this.partnerGroupName = partnerGroupName;
	}
	
	@Column(name="NIC")
	public String getCnic() {
		return cnic;
	}
	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	
	@Column(name="IS_HEAD")
	public Boolean getHead() {
		return head;
	}
	public void setHead(Boolean head) {
		this.head = head;
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
	@Column(name="IS_ACCOUNT_CLOSED")
	public String getAccountClosed() {
		return accountClosed;
	}
	public void setAccountClosed(String accountClosed) {
		this.accountClosed = accountClosed;
	}
	
	/**
	 * @return olaCustomerAccountTypeId
	 */
	@Column(name="OLA_CUSTOMER_ACCOUNT_TYPE_ID", nullable=false)
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
	@Column(name="OLA_CUSTOMER_ACCOUNT_TYPE_NAME", nullable=false)
	public String getOlaCustomerAccountTypeName() {
		return olaCustomerAccountTypeName;
	}
	
	/**
	 * @param olaCustomerAccountTypeName
	 */
	public void setOlaCustomerAccountTypeName(String olaCustomerAccountTypeName) {
		this.olaCustomerAccountTypeName = olaCustomerAccountTypeName;
	}
	
	@Column(name="SALE_USER_NAME")
	public String getSaleUserName() {
		return saleUserName;
	}
	
	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}
	
	@Column(name="ROLE_TITLE")
	public String getRoleTitle() {
		return roleTitle;
	}
	
	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}
	
	@Column(name="NIC_EXPIRY_DATE")
	public Date getCnicExpiryDate() {
		return cnicExpiryDate;
	}
	
	public void setCnicExpiryDate(Date cnicExpiryDate) {
		this.cnicExpiryDate = cnicExpiryDate;
	}

	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}

	@Column(name="AREA_LEVEL_NAME")
	public String getAreaLevelName() {
		return areaLevelName;
	}
}
