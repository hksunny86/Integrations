package com.inov8.microbank.common.model.portal.financereportsmodule;

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
 * Creation Date : Jan 12, 2013 2:54:48 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
 
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMMISSION_SUMMARY_REPORT_VIEW")
public class CommissionSummaryReportViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = 8704619593243697864L;

    // Fields
	private String transactionId;
	private Long productId;
	private String productName;
	private Long supplierId;
	private Date transactionDate;
	private Long transactionCodeId;
	private Double transactionAmount;
	private String processingStatusName;
	private String transactionBy;
	private Double toAskari;
	private Double toAgent1;
	private Double toAgent2;
	private Double wht;
	private Double fed;
	private Double serviceChargesInclusive;
	private Double serviceChargesExclusive;
	private Double toFranchise1;
	private Double toFranchise2;
	private Double totalCommissionTransferred;
	private Double otherCommission;
	private Double salesTeamCommission;

	//default constructor
    public CommissionSummaryReportViewModel()
    {
    }

    @Override
    @Transient
    public Long getPrimaryKey()
    {
        return getTransactionCodeId();
    }

    @Override
    @Transient
    public void setPrimaryKey( Long primaryKey )
    {
        setTransactionCodeId( primaryKey );
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter()
    {
        return "&transactionCodeId=" + getTransactionCodeId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName()
    {
        return "transactionCodeId";
    }

    // Property accessors
    @Column( name = "TRANSACTION_ID" )
    public String getTransactionId()
    {
        return this.transactionId;
    }

    public void setTransactionId( String transactionId )
    {
        this.transactionId = transactionId;
    }

    @Column( name = "PRODUCT_ID" )
    public Long getProductId()
    {
        return productId;
    }

    public void setProductId( Long productId )
    {
        this.productId = productId;
    }

    @Column( name = "PRODUCT_NAME" )
    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName( String productName )
    {
        this.productName = productName;
    }

    @Column( name = "SUPPLIER_ID" )
    public Long getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId( Long supplierId )
    {
        this.supplierId = supplierId;
    }

    @Column( name = "TRANSACTION_DATE", nullable = false, length = 7 )
    public Date getTransactionDate()
    {
        return this.transactionDate;
    }

    public void setTransactionDate( Date transactionDate )
    {
        this.transactionDate = transactionDate;
    }

    @Id
    @Column( name = "TRANSACTION_CODE_ID" )
    public Long getTransactionCodeId()
    {
        return this.transactionCodeId;
    }

    public void setTransactionCodeId( Long transactionCodeId )
    {
        this.transactionCodeId = transactionCodeId;
    }

    @Column( name = "TRANSACTION_AMOUNT" )
    public Double getTransactionAmount()
    {
        return this.transactionAmount;
    }

    public void setTransactionAmount( Double transactionAmount )
    {
        this.transactionAmount = transactionAmount;
    }

    @Column( name = "PROCESSING_STATUS_NAME" )
    public String getProcessingStatusName()
    {
        return this.processingStatusName;
    }

    public void setProcessingStatusName( String processingStatusName )
    {
        this.processingStatusName = processingStatusName;
    }

    @Column( name = "TRANSACTION_BY" )
    public String getTransactionBy()
    {
        return transactionBy;
    }

    public void setTransactionBy( String transactionBy )
    {
        this.transactionBy = transactionBy;
    }

    @Column( name = "TO_BANK" )
    public Double getToAskari()
    {
        return this.toAskari;
    }

    public void setToAskari( Double toAskari )
    {
        this.toAskari = toAskari;
    }

    @Column( name = "TO_AGENT1" )
    public Double getToAgent1()
    {
        return this.toAgent1;
    }

    public void setToAgent1( Double toAgent1 )
    {
        this.toAgent1 = toAgent1;
    }

    @Column( name = "TO_AGENT2" )
    public Double getToAgent2()
    {
        return this.toAgent2;
    }

    public void setToAgent2( Double toAgent2 )
    {
        this.toAgent2 = toAgent2;
    }

    @Column( name = "WHT" )
    public Double getWht()
    {
        return this.wht;
    }

    public void setWht( Double wht )
    {
        this.wht = wht;
    }

    @Column( name = "FED" )
    public Double getFed()
    {
        return this.fed;
    }

    public void setFed( Double fed )
    {
        this.fed = fed;
    }

    @Column( name = "SERVICE_CHARGES_INCLUSIVE" )
    public Double getServiceChargesInclusive()
    {
        return this.serviceChargesInclusive;
    }

    public void setServiceChargesInclusive( Double serviceChargesInclusive )
    {
        this.serviceChargesInclusive = serviceChargesInclusive;
    }

    @Column( name = "SERVICE_CHARGES_EXCLUSIVE" )
    public Double getServiceChargesExclusive()
    {
        return this.serviceChargesExclusive;
    }

    public void setServiceChargesExclusive( Double serviceChargesExclusive )
    {
        this.serviceChargesExclusive = serviceChargesExclusive;
    }

    @Column( name = "TO_FRANCHISE1" )
    public Double getToFranchise1()
    {
        return this.toFranchise1;
    }

    public void setToFranchise1( Double toFranchise1 )
    {
        this.toFranchise1 = toFranchise1;
    }

    @Column( name = "TO_FRANCHISE2" )
    public Double getToFranchise2()
    {
        return this.toFranchise2;
    }

    public void setToFranchise2( Double toFranchise2 )
    {
        this.toFranchise2 = toFranchise2;
    }

    @Column( name = "TOTAL_COMMISSION_TRANSFERRED" )
    public Double getTotalCommissionTransferred()
    {
        return this.totalCommissionTransferred;
    }

    public void setTotalCommissionTransferred( Double totalCommissionTransferred )
    {
        this.totalCommissionTransferred = totalCommissionTransferred;
    }
    
    @Column(name = "TO_OTHER")
	public Double getOtherCommission() {
		return otherCommission;
	}

	public void setOtherCommission(Double otherCommission) {
		this.otherCommission = otherCommission;
	}
	
	@Column(name = "TO_SALES_TEAM")
	public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}

	public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}
}