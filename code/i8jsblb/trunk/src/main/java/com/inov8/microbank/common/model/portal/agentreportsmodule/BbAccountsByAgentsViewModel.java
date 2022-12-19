package com.inov8.microbank.common.model.portal.agentreportsmodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 28, 2013 7:56:45 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BB_ACCOUNTS_BY_AGENTS_VIEW")
public class BbAccountsByAgentsViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = -2293809020712577550L;

    // Fields
	private Long customerId;
	private String customerUserId;
	private String customerUsername;
	private String customerMobileNo;
	private Date lastTransactionDate;
	private Long distributorId;
	private String distributorName;
	private Long regionId;
	private String regionName;
	private Long retailerId;
	private String retailerName;
	private String accountOpenerName;
	private String agentId;
	private String username;
	private String accountStatus;
	private Date createdOn;

	private Date startDate;
    private Date endDate;


	private String customerName;
	private String accountTypeName;
	private String areaName;
	private String cityName;
	private String businessName;


	@Column(name="CUSTOMER_NAME")
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@Column(name="CUSTOMER_ACCOUNT_TYPE_NAME")
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	@Column(name="AREA_NAME")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name="BUSINESS_CITY_NAME")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Column(name="BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/** default constructor */
	public BbAccountsByAgentsViewModel()
	{
	}

	@Transient
    @Override
    public Long getPrimaryKey()
    {
        return getCustomerId();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setCustomerId( primaryKey );
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&customerId="+customerId;
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "customerId";
    }

    // Property accessors
    @Id
    @Column( name = "CUSTOMER_ID", nullable = false, precision = 10, scale = 0 )
    public Long getCustomerId()
    {
        return this.customerId;
    }

    public void setCustomerId( Long customerId )
    {
        this.customerId = customerId;
    }

    @Column( name = "CUSTOMER_USER_ID", length = 50 )
    public String getCustomerUserId()
    {
        return this.customerUserId;
    }

    public void setCustomerUserId( String customerUserId )
    {
        this.customerUserId = customerUserId;
    }

    @Column( name = "CUSTOMER_USERNAME", nullable = false, length = 50 )
    public String getCustomerUsername()
    {
        return this.customerUsername;
    }

    public void setCustomerUsername( String customerUsername )
    {
        this.customerUsername = customerUsername;
    }

    @Column( name = "CUSTOMER_MOBILE_NO", length = 50 )
    public String getCustomerMobileNo()
    {
        return this.customerMobileNo;
    }

    public void setCustomerMobileNo( String customerMobileNo )
    {
        this.customerMobileNo = customerMobileNo;
    }

    @Column( name = "LAST_TRANSACTION_DATE", length = 7 )
    public Date getLastTransactionDate()
    {
        return this.lastTransactionDate;
    }

    public void setLastTransactionDate( Date lastTransactionDate )
    {
        this.lastTransactionDate = lastTransactionDate;
    }

    @Column( name = "DISTRIBUTOR_ID", nullable = false, precision = 10, scale = 0 )
    public Long getDistributorId()
    {
        return this.distributorId;
    }

    public void setDistributorId( Long distributorId )
    {
        this.distributorId = distributorId;
    }

    @Column( name = "DISTRIBUTOR_NAME", nullable = false, length = 50 )
    public String getDistributorName()
    {
        return this.distributorName;
    }

    public void setDistributorName( String distributorName )
    {
        this.distributorName = distributorName;
    }

    @Column( name = "REGION_ID", precision = 10, scale = 0 )
    public Long getRegionId()
    {
        return this.regionId;
    }

    public void setRegionId( Long regionId )
    {
        this.regionId = regionId;
    }

    @Column( name = "REGION_NAME", length = 50 )
    public String getRegionName()
    {
        return this.regionName;
    }

    public void setRegionName( String regionName )
    {
        this.regionName = regionName;
    }

    @Column( name = "RETAILER_ID", precision = 10, scale = 0 )
    public Long getRetailerId()
    {
        return this.retailerId;
    }

    public void setRetailerId( Long retailerId )
    {
        this.retailerId = retailerId;
    }

    @Column( name = "RETAILER_NAME", length = 50 )
    public String getRetailerName()
    {
        return this.retailerName;
    }

    public void setRetailerName( String retailerName )
    {
        this.retailerName = retailerName;
    }

    @Column( name = "ACCOUNT_OPENER_NAME", length = 101 )
    public String getAccountOpenerName()
    {
        return this.accountOpenerName;
    }

    public void setAccountOpenerName( String accountOpenerName )
    {
        this.accountOpenerName = accountOpenerName;
    }

    @Column( name = "AGENT_ID", length = 50 )
    public String getAgentId()
    {
        return this.agentId;
    }

    public void setAgentId( String agentId )
    {
        this.agentId = agentId;
    }

    @Column( name = "USERNAME", nullable = false, length = 50 )
    public String getUsername()
    {
        return this.username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    @Column( name="ACCOUNT_STATUS", nullable = false, length = 50 )
    public String getAccountStatus()
    {
        return accountStatus;
    }

    public void setAccountStatus( String accountStatus )
    {
        this.accountStatus = accountStatus;
    }

    @Column( name = "CREATED_ON", nullable = false, length = 7 )
    public Date getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn( Date createdOn )
    {
        this.createdOn = createdOn;
    }

    @Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }
}