package com.inov8.microbank.common.model.portal.agentreportsmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

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
@Table(name = "HANDLER_TRANS_DETAIL_VIEW")
public class HandlerTransactionDetailViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = -2957837944212780742L;

    private Long transactionId;
    private String transactionCode;
	private String agentMsisdn;
	private String agentId;
	private Long distributorId;
	private String distributorName;
	private Long regionId;
	private String regionName;
	private Long retailerId;
	private String retailerName;
	private Date createdOn;
	private Long productId;
	private String productName;
	private Long supplierId;
	private String supplierName;
	private Double agentCommission;
	private Double franchiseCommission;
	private Long handlerId;
	private String handlerMobileNo;
	private Double inclusiveCharges;
	private Double exclusiveCharges;
	private Double amount;
	private String tranasactionStatus;

	private Date startDate;
	private Date endDate;

	/** default constructor */
	public HandlerTransactionDetailViewModel()
	{
	}

	@Transient
    @Override
    public Long getPrimaryKey()
    {
        return getTransactionId();
    }

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
        setTransactionId( primaryKey );
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&transactionId="+transactionId;
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "transactionId";
    }
	
	@Id
	@Column(name = "TRANSACTION_ID")
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
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

    @Column( name = "CREATED_ON")
    public Date getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn( Date createdOn )
    {
        this.createdOn = createdOn;
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

    @Column( name = "PRODUCT_NAME")
    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column( name = "SUPPLIER_ID", precision = 10, scale = 0 )
    public Long getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId( Long supplierId )
    {
        this.supplierId = supplierId;
    }

    @Column( name = "AGENT_COMMISSION" )
    public Double getAgentCommission() {
    	return agentCommission;
    }
    
    public void setAgentCommission(Double agentCommission) {
    	this.agentCommission = agentCommission;
    }
    
    @Column( name = "FRANCHISE_COMMISSION" )
    public Double getFranchiseCommission() {
    	return franchiseCommission;
    }
    
    public void setFranchiseCommission(Double franchiseCommission) {
    	this.franchiseCommission = franchiseCommission;
    }
    
    @Column( name = "AGENT_MSISDN " )
    public String getAgentMsisdn() {
		return agentMsisdn;
	}

	public void setAgentMsisdn(String agentMsisdn) {
		this.agentMsisdn = agentMsisdn;
	}

	@Column( name = "SUPPLIER_NAME" )
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	
	@Column( name = "HANDLER_ID " )
	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

	@Column( name = "HANDLER_MOBILE_NUMBER" )
	public String getHandlerMobileNo() {
		return handlerMobileNo;
	}

	public void setHandlerMobileNo(String handlerMobileNo) {
		this.handlerMobileNo = handlerMobileNo;
	}

	@Column( name = "INCLUSIVE_CHARGES" )
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}
	
	@Column( name = "EXCLUSIVE_CHARGES " )
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

	@Column( name = "AMOUNT" )
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column( name = "TRNASACTION_STATUS" )
	public String getTranasactionStatus() {
		return tranasactionStatus;
	}

	public void setTranasactionStatus(String tranasactionStatus) {
		this.tranasactionStatus = tranasactionStatus;
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