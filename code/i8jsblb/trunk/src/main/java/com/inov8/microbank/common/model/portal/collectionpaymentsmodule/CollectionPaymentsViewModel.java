package com.inov8.microbank.common.model.portal.collectionpaymentsmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 10, 2013 8:39:25 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@Table(name = "COLLECTION_PAYMENTS_VIEW")
public class CollectionPaymentsViewModel extends BasePersistableModel implements Serializable
{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3805646988822960372L;

    // Fields
    private Long transactionId;
    private String transactionCode;
	private Date createdOn;
	private String mfsId;
	private String saleMobileNo;
	private String walkinMobileNo;
	private String consumerNo;
	private Long supplierId;
	private Long productId;
	private String productName;
	private Double transactionAmount;
	private Double serviceChargesExclusive;
	private Double serviceChargesInclusive;
	private Double totalAmount;
	private Long supProcessingStatusId;
	private String supplierProcessingStatus;

	private DateRangeHolderModel dateRangeHolderModel;

	/** default constructor */
	public CollectionPaymentsViewModel() {
	    dateRangeHolderModel = new DateRangeHolderModel();
	}

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
        setTransactionId( primaryKey );
    }

	@Transient
    @Override
    public Long getPrimaryKey()
    {
        return getTransactionId();
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

    // Property accessors
    @Id
    @Column( name = "TRANSACTION_ID" )
    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId( Long transactionId )
    {
        this.transactionId = transactionId;
    }

    @Column( name = "TRANSACTION_CODE" )
    public String getTransactionCode()
    {
        return this.transactionCode;
    }

    public void setTransactionCode( String transactionCode )
    {
        this.transactionCode = transactionCode;
    }

    @Column( name = "CREATED_ON" )
    public Date getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn( Date createdOn )
    {
        this.createdOn = createdOn;
    }

    @Column( name = "MFS_ID" )
    public String getMfsId()
    {
        return this.mfsId;
    }

    public void setMfsId( String mfsId )
    {
        this.mfsId = mfsId;
    }

    @Column( name = "SALE_MOBILE_NO" )
    public String getSaleMobileNo()
    {
        return this.saleMobileNo;
    }

    public void setSaleMobileNo( String saleMobileNo )
    {
        this.saleMobileNo = saleMobileNo;
    }

    @Column( name = "WALKIN_MOBILE_NO" )
    public String getWalkinMobileNo()
    {
        return this.walkinMobileNo;
    }

    public void setWalkinMobileNo( String walkinMobileNo )
    {
        this.walkinMobileNo = walkinMobileNo;
    }

    @Column( name = "CONSUMER_NO" )
    public String getConsumerNo()
    {
        return this.consumerNo;
    }

    public void setConsumerNo( String consumerNo )
    {
        this.consumerNo = consumerNo;
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

    @Column( name = "PRODUCT_ID" )
    public Long getProductId()
    {
        return this.productId;
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

    @Column( name = "TRANSACTION_AMOUNT" )
    public Double getTransactionAmount()
    {
        return this.transactionAmount;
    }

    public void setTransactionAmount( Double transactionAmount )
    {
        this.transactionAmount = transactionAmount;
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

    @Column( name = "SERVICE_CHARGES_INCLUSIVE" )
    public Double getServiceChargesInclusive()
    {
        return this.serviceChargesInclusive;
    }

    public void setServiceChargesInclusive( Double serviceChargesInclusive )
    {
        this.serviceChargesInclusive = serviceChargesInclusive;
    }

    @Column( name = "TOTAL_AMOUNT" )
    public Double getTotalAmount()
    {
        return this.totalAmount;
    }

    public void setTotalAmount( Double totalAmount )
    {
        this.totalAmount = totalAmount;
    }

    @Column( name = "SUP_PROCESSING_STATUS_ID" )
    public Long getSupProcessingStatusId()
    {
        return this.supProcessingStatusId;
    }

    public void setSupProcessingStatusId( Long supProcessingStatusId )
    {
        this.supProcessingStatusId = supProcessingStatusId;
    }

    @Column( name = "SUPPLIER_PROCESSING_STATUS" )
    public String getSupplierProcessingStatus()
    {
        return this.supplierProcessingStatus;
    }

    public void setSupplierProcessingStatus( String supplierProcessingStatus )
    {
        this.supplierProcessingStatus = supplierProcessingStatus;
    }

    @Transient
    public DateRangeHolderModel getDateRangeHolderModel()
    {
        return dateRangeHolderModel;
    }

    public void setDateRangeHolderModel( DateRangeHolderModel dateRangeHolderModel )
    {
        this.dateRangeHolderModel = dateRangeHolderModel;
    }
}