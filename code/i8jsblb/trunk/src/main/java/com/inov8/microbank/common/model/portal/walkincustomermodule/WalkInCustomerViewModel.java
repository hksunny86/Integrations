package com.inov8.microbank.common.model.portal.walkincustomermodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 1, 2012 4:56:49 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WALK_IN_CUSTOMER_VIEW")
public class WalkInCustomerViewModel extends BasePersistableModel implements Serializable
{

	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7469631561863134376L;

    private Long walkInCustomerId;
    private Long appUserId;
	private String mobileNo;
	private String cnic;
	private String registeredBy;
	private Date createdOn;
	private Integer transactionsAsSender;
	private Integer transactionsAsReceiver;
	private Integer noOfTransactions;
	private Date lastTransactionDate;

    public WalkInCustomerViewModel()
    {
    }

    @Id
    @Column( name = "WALK_IN_CUSTOMER_ID", nullable = false )
    public Long getWalkInCustomerId()
    {
        return this.walkInCustomerId;
    }

    public void setWalkInCustomerId( Long walkinCustomerId )
    {
        this.walkInCustomerId = walkinCustomerId;
    }

    @Column( name = "APP_USER_ID")
    public Long getAppUserId()
    {
        return this.appUserId;
    }

    public void setAppUserId( Long appUserId )
    {
        this.appUserId = appUserId;
    }

    @Column( name = "MOBILE_NO" )
    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo( String mobileNo )
    {
        this.mobileNo = mobileNo;
    }

    @Column( name = "CNIC" )
    public String getCnic()
    {
        return cnic;
    }

    public void setCnic( String cnic )
    {
        this.cnic = cnic;
    }

    @Column( name = "REGISTERED_BY", nullable = false )
    public String getRegisteredBy()
    {
        return registeredBy;
    }

    public void setRegisteredBy( String registeredBy )
    {
        this.registeredBy = registeredBy;
    }

    @Column( name = "CREATED_ON", nullable = false )
    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn( Date createdOn )
    {
        this.createdOn = createdOn;
    }

    @Column( name = "TRANSACTIONS_AS_SENDER" )
    public Integer getTransactionsAsSender()
    {
        return transactionsAsSender;
    }
    
    public void setTransactionsAsSender( Integer transactionsAsSender )
    {
        this.transactionsAsSender = transactionsAsSender;
    }

    @Column( name = "TRANSACTIONS_AS_RECEIVER" )
    public Integer getTransactionsAsReceiver()
    {
        return transactionsAsReceiver;
    }

    public void setTransactionsAsReceiver( Integer transactionsAsReceiver )
    {
        this.transactionsAsReceiver = transactionsAsReceiver;
    }

    @Column( name = "NO_OF_TRANSACTIONS" )
    public Integer getNoOfTransactions()
    {
        return noOfTransactions;
    }

    public void setNoOfTransactions( Integer noOfTransactions )
    {
        this.noOfTransactions = noOfTransactions;
    }

    @Column( name = "LAST_TRANSACTION_DATE", nullable = false )
    public Date getLastTransactionDate()
    {
        return lastTransactionDate;
    }

    public void setLastTransactionDate( Date lastTransactionDate )
    {
        this.lastTransactionDate = lastTransactionDate;
    }
    
    @javax.persistence.Transient
    public Long getPrimaryKey()
    {
        return getWalkInCustomerId();
    }

    @javax.persistence.Transient
    public void setPrimaryKey( Long primaryKey )
    {
        setWalkInCustomerId( primaryKey );
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter()
    {
        return "&walkInCustomerId=" + getWalkInCustomerId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        return "walkInCustomerId";
    }

}