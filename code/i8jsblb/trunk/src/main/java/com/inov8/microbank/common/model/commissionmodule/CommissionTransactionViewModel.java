package com.inov8.microbank.common.model.commissionmodule;

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
 * Creation Date : Apr 26, 2013 3:31:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@Table(name = "COMMISSION_TRANSACTION_VIEW")
public class CommissionTransactionViewModel extends BasePersistableModel implements Serializable
{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3655889523487657108L;

    // Fields
	private Long commissionTransactionId;
	private String transactionId;
	private String product;
	private Long productId;
	private String commissionStakehoder;
	private Long commissionStakeholderId;
	private Long supplierId;
	private Double commissionAmount;
	private String settled;
	private String posted;
	private Date createdOn;
	private Date updatedOn;

	private Date startDate;
	private Date endDate;

	/** default constructor */
	public CommissionTransactionViewModel() {
	}

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setCommissionTransactionId( primaryKey );
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getCommissionTransactionId();
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&commissionTransactionId="+getCommissionTransactionId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "commissionTransactionId";
    }

    // Property accessors
    @Id
    @Column( name = "COMMISSION_TRANSACTION_ID", nullable = false, precision = 10, scale = 0 )
    public Long getCommissionTransactionId()
    {
        return this.commissionTransactionId;
    }

    public void setCommissionTransactionId( Long commissionTransactionId )
    {
        this.commissionTransactionId = commissionTransactionId;
    }

    @Column( name = "TRANSACTION_ID", nullable = false, length = 50 )
    public String getTransactionId()
    {
        return this.transactionId;
    }

    public void setTransactionId( String transactionId )
    {
        this.transactionId = transactionId;
    }

    @Column( name = "PRODUCT", nullable = false, length = 50 )
    public String getProduct()
    {
        return this.product;
    }

    public void setProduct( String product )
    {
        this.product = product;
    }

    @Column( name = "PRODUCT_ID", nullable = false, precision = 10, scale = 0 )
    public Long getProductId()
    {
        return this.productId;
    }

    public void setProductId( Long productId )
    {
        this.productId = productId;
    }

    @Column( name = "COMMISSION_STAKEHODER", nullable = false, length = 50 )
    public String getCommissionStakehoder()
    {
        return this.commissionStakehoder;
    }

    public void setCommissionStakehoder( String commissionStakehoder )
    {
        this.commissionStakehoder = commissionStakehoder;
    }

    @Column( name = "COMMISSION_STAKEHOLDER_ID", nullable = false, precision = 10, scale = 0 )
    public Long getCommissionStakeholderId()
    {
        return this.commissionStakeholderId;
    }

    public void setCommissionStakeholderId( Long commissionStakeholderId )
    {
        this.commissionStakeholderId = commissionStakeholderId;
    }

    @Column( name = "SUPPLIER_ID", nullable = false, precision = 10, scale = 0 )
    public Long getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId( Long supplierId )
    {
        this.supplierId = supplierId;
    }

    @Column( name = "COMMISSION_AMOUNT", nullable = false, precision = 16, scale = 4 )
    public Double getCommissionAmount()
    {
        return this.commissionAmount;
    }

    public void setCommissionAmount( Double commissionAmount )
    {
        this.commissionAmount = commissionAmount;
    }

    @Column( name = "SETTLED", length = 3 )
    public String getSettled()
    {
        return this.settled;
    }

    public void setSettled( String settled )
    {
        this.settled = settled;
    }

    @Column( name = "POSTED", length = 3 )
    public String getPosted()
    {
        return this.posted;
    }

    public void setPosted( String posted )
    {
        this.posted = posted;
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

    @Column( name = "UPDATED_ON", nullable = false, length = 7 )
    public Date getUpdatedOn()
    {
        return this.updatedOn;
    }

    public void setUpdatedOn( Date updatedOn )
    {
        this.updatedOn = updatedOn;
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