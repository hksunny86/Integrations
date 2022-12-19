package com.inov8.microbank.common.model.portal.agentreportsmodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_TX_SUMMARY_VIEW")
public class AgentTransactionSummaryViewModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = -454652349625147309L;
	// Fields
	private Long rowId;
	private Long productId;
	private String productName;
	private String agentId;
	private Long supplierId;
	private Long retailerId;
	private String retailerName;
	private Long regionId;
	private Long distributorId;
	private String distributorName;
	private Long productCount;
	private Double bankDebitAmountSum;
	private Double bankCreditAmountSum;
	private Double bankBalance;
	private Double agentCommissionSum;
	private Double franchiseCommissionSum;
	private String agentBusinessName;
	private String areaName;
	private String areaLevelName;
	private String cityName;
	private String regionName;

	private Date startDate;
	private Date endDate;

	@Column(name="BUSINESS_CITY_NAME")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Column(name="AREA_NAME")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name="AREA_LEVEL_NAME")
	public String getAreaLevelName() {
		return areaLevelName;
	}

	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}

	/** default constructor */
	public AgentTransactionSummaryViewModel()
	{
	}

	@Transient
    @Override
    public Long getPrimaryKey()
    {
        return getRowId();
    }

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
		setRowId( primaryKey );
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&rowId="+rowId;
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "rowId";
    }

    // Property accessors
	@Id
	@Column( name = "ROW_ID")
	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	
    @Column( name = "PRODUCT_ID", precision = 10, scale = 0 )
    public Long getProductId()
    {
        return this.productId;
    }

    public void setProductId( Long productId )
    {
        this.productId = productId;
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

    @Column( name = "SUPPLIER_ID" )
    public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Column( name = "DISTRIBUTOR_ID", precision = 10, scale = 0 )
    public Long getDistributorId()
    {
        return this.distributorId;
    }

    public void setDistributorId( Long distributorId )
    {
        this.distributorId = distributorId;
    }

    @Column( name = "DISTRIBUTOR_NAME", length = 50 )
    public String getDistributorName()
    {
        return this.distributorName;
    }

    public void setDistributorName( String distributorName )
    {
        this.distributorName = distributorName;
    }

    @Column( name = "PRODUCT_COUNT" )
    public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
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

    @Column( name = "PRODUCT_NAME")
    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column( name = "BANK_DEBIT_AMOUNT_SUM")
	public Double getBankDebitAmountSum() {
		return bankDebitAmountSum;
	}

	public void setBankDebitAmountSum(Double bankDebitAmountSum) {
		if(bankDebitAmountSum!=null){
			this.bankDebitAmountSum = bankDebitAmountSum;
		}else{
			this.bankDebitAmountSum=0.0d;
		}
	}

	@Column( name = "BANK_CREDIT_AMOUNT_SUM")
	public Double getBankCreditAmountSum() {
		return bankCreditAmountSum;
	}

	public void setBankCreditAmountSum(Double bankCreditAmountSum) {
		if(bankCreditAmountSum!=null){
			this.bankCreditAmountSum = bankCreditAmountSum;
		}else{
			this.bankCreditAmountSum=0.0d;
		}
	}

	@Column( name = "BANK_BALANCE")
	public Double getBankBalance()
	{
		return bankBalance;
	}

	public void setBankBalance(Double bankBalance)
	{
		this.bankBalance = bankBalance;
	}

	@Column( name = "AGENT_COMMISSION_SUM")
	public Double getAgentCommissionSum() {
		return agentCommissionSum;
	}

	public void setAgentCommissionSum(Double agentCommissionSum) {
		this.agentCommissionSum = agentCommissionSum;
	}

	@Column( name = "FRANCHISE_COMMISSION_SUM")
	public Double getFranchiseCommissionSum() {
		return franchiseCommissionSum;
	}

	public void setFranchiseCommissionSum(Double franchiseCommissionSum) {
		this.franchiseCommissionSum = franchiseCommissionSum;
	}
	
    @Column(name="BUSINESS_NAME")
	public String getAgentBusinessName() {
		return agentBusinessName;
	}

	public void setAgentBusinessName(String agentBusinessName) {
		if (agentBusinessName != null) {
			this.agentBusinessName = agentBusinessName;
		}
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