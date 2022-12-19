package com.inov8.microbank.common.model.postedrransactionreportmodule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 23, 2013 8:00:37 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
 
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table( name = "POSTED_TRANSACTION_NADRA_VIEW" )
public class PostedTransactionNadraViewModel extends BasePersistableModel implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 351878670364114461L;

    // Fields
    private Long      postedTransRptId;

    private Long      intgTransactionTypeId;

    private String    transactionType;

    private String    transactionCode;

    private Long      productId;

    private String    productName;

    private Double    amount;

    private String    responseCode;

    private String    refCode;

    private String    consumerNo;

    private Long      createdBy;

    private Timestamp createdOn;

    private Date startDate;

    private Date endDate;

    /** default constructor */
    public PostedTransactionNadraViewModel()
    {
    }

    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey()
    {
        return postedTransRptId;
    }

    @Override
    @javax.persistence.Transient
    public void setPrimaryKey( Long primaryKey )
    {
        setPostedTransRptId( primaryKey );
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter()
    {
        String parameters = "&postedTransRptId=" + getPostedTransRptId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        return "postedTransRptId";
    }

    // Property accessors
    @Id
    @Column( name = "POSTED_TRANS_RPT_ID", nullable = false, precision = 10, scale = 0 )
    public Long getPostedTransRptId()
    {
        return this.postedTransRptId;
    }

    public void setPostedTransRptId( Long postedTransRptId )
    {
        this.postedTransRptId = postedTransRptId;
    }

    @Column( name = "INTG_TRANSACTION_TYPE_ID", nullable = false, precision = 10, scale = 0 )
    public Long getIntgTransactionTypeId()
    {
        return this.intgTransactionTypeId;
    }

    public void setIntgTransactionTypeId( Long intgTransactionTypeId )
    {
        this.intgTransactionTypeId = intgTransactionTypeId;
    }

    @Column( name = "TRANSACTION_TYPE", length = 50 )
    public String getTransactionType()
    {
        return this.transactionType;
    }

    public void setTransactionType( String transactionType )
    {
        this.transactionType = transactionType;
    }

    @Column( name = "TRANSACTION_CODE", nullable = false, length = 50 )
    public String getTransactionCode()
    {
        return this.transactionCode;
    }

    public void setTransactionCode( String transactionCode )
    {
        this.transactionCode = transactionCode;
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

    @Column( name = "PRODUCT_NAME", nullable = false, length = 50 )
    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName( String productName )
    {
        this.productName = productName;
    }

    @Column( name = "AMOUNT", precision = 10, scale = 0 )
    public Double getAmount()
    {
        return this.amount;
    }

    public void setAmount( Double amount )
    {
        this.amount = amount;
    }

    @Column( name = "RESPONSE_CODE", length = 50 )
    public String getResponseCode()
    {
        return this.responseCode;
    }

    public void setResponseCode( String responseCode )
    {
        this.responseCode = responseCode;
    }

    @Column( name = "REF_CODE", length = 50 )
    public String getRefCode()
    {
        return this.refCode;
    }

    public void setRefCode( String refCode )
    {
        this.refCode = refCode;
    }

    @Column( name = "CONSUMER_NO", length = 30 )
    public String getConsumerNo()
    {
        return this.consumerNo;
    }

    public void setConsumerNo( String consumerNo )
    {
        this.consumerNo = consumerNo;
    }

    @Column( name = "CREATED_BY", precision = 10, scale = 0 )
    public Long getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy( Long createdBy )
    {
        this.createdBy = createdBy;
    }

    @Column( name = "CREATED_ON", length = 7 )
    public Timestamp getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn( Timestamp createdOn )
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