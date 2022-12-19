package com.inov8.microbank.common.model.portal.level3account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Level3AccountsViewModel entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "LEVEL3_ACCOUNTS_VIEW")
public class Level3AccountsViewModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = 1313372860054927107L;

	// Fields
	private Long appUserId;  
	private Long retailerContactId;
	private Long retailerId;
	private String cnic;
	private String mobileNo;
	private Date createdOn;
	private Long registrationStateId;
	private String registrationStateName;
	private String userId;
	private Long accountTypeId;
	private String accountTypeName;
	private Date startDate;
	private Date endDate;
	private String initialAppFormNo;
	private Date dormancyRemovedOn;
	// Constructors

	/** default constructor */
	public Level3AccountsViewModel()
	{
	}

	@Override
	public void setPrimaryKey(Long primaryKey)
	{
		getAppUserId();
	}

	@Transient
	@Override
	public Long getPrimaryKey()
	{
		return getAppUserId();
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter()
	{
		return "&appUserId="+getAppUserId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName()
	{
		return "appUserId";
	}

	// Property accessors
	@Id
	@Column(name = "APP_USER_ID")
	public Long getAppUserId()
	{
		return this.appUserId;
	}

	public void setAppUserId(Long appUserId)
	{
		this.appUserId = appUserId;
	}

    @Column(name = "RETAILER_CONTACT_ID")
    public Long getRetailerContactId()
    {
       return retailerContactId;
    }

    public void setRetailerContactId(Long retailerContactId)
    {
       this.retailerContactId = retailerContactId;
    }

    @Column(name = "RETAILER_ID")
    public Long getRetailerId()
    {
       return retailerId;
    }

    public void setRetailerId(Long retailerId)
    {
       this.retailerId = retailerId;
    }

	@Column(name = "CNIC")
	public String getCnic()
	{
		return this.cnic;
	}

	public void setCnic(String cnic)
	{
		this.cnic = cnic;
	}

	@Column(name = "MOBILE_NO")
	public String getMobileNo()
	{
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn()
	{
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	@Column(name = "REGISTRATION_STATE_ID")
	public Long getRegistrationStateId()
	{
		return this.registrationStateId;
	}

	public void setRegistrationStateId(Long registrationStateId)
	{
		this.registrationStateId = registrationStateId;
	}

	@Column(name = "REGISTRATION_STATE_NAME")
	public String getRegistrationStateName()
	{
		return registrationStateName;
	}

	public void setRegistrationStateName(String registrationStateName)
	{
		this.registrationStateName = registrationStateName;
	}

	@Column(name = "USER_ID")
	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Column(name = "ACCOUNT_TYPE_ID")
	public Long getAccountTypeId()
	{
		return this.accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId)
	{
		this.accountTypeId = accountTypeId;
	}

	@Column(name = "DORMANCY_REMOVED_ON")
	public Date getDormancyRemovedOn() {
		return dormancyRemovedOn;
	}

	public void setDormancyRemovedOn(Date dormancyRemovedOn) {
		this.dormancyRemovedOn = dormancyRemovedOn;
	}

	@Column(name = "ACCOUNT_TYPE_NAME")
	public String getAccountTypeName()
	{
		return this.accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName)
	{
		this.accountTypeName = accountTypeName;
	}

	@Transient
	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	@Column(name = "INITIAL_APP_FORM_NUMBER")
	public String getInitialAppFormNo()
	{
		return initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo)
	{
		this.initialAppFormNo = initialAppFormNo;
	}
}