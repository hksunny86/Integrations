package com.inov8.microbank.common.model.portal.ola;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 17, 2013 12:35:02 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@Table(name = "OLA_CUSTOMER_TX_LIMIT_VIEW")
public class OlaCustomerTxLimitViewModel extends BasePersistableModel implements Serializable
{

    private static final long serialVersionUID = 4698857717630834917L;

    // Fields
    private Long              limitId;
    private Long              olaCustomerAccountTypeId;
    private String            customerAccountType;
    private Long              olaTransactionTypeId;
    private String            olaTransactionType;
    private Long              limitTypeId;
    private String            limitType;
    private Long              limitMinimum;
    private Long              limitMaximum;

    /** default constructor */
    public OlaCustomerTxLimitViewModel()
    {
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getLimitId();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setLimitId( primaryKey );
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "limitId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&limitId=" + getLimitId();
    }

    // Property accessors
    @Id
    @Column( name = "LIMIT_ID", nullable = false, precision = 10, scale = 0 )
    public Long getLimitId()
    {
        return this.limitId;
    }

    public void setLimitId( Long limitId )
    {
        this.limitId = limitId;
    }

    @Column( name = "OLA_CUSTOMER_ACCOUNT_TYPE_ID", nullable = false, precision = 10, scale = 0 )
    public Long getOlaCustomerAccountTypeId()
    {
        return this.olaCustomerAccountTypeId;
    }

    public void setOlaCustomerAccountTypeId( Long olaCustomerAccountTypeId )
    {
        this.olaCustomerAccountTypeId = olaCustomerAccountTypeId;
    }

    @Column( name = "CUSTOMER_ACCOUNT_TYPE", nullable = false, length = 50 )
    public String getCustomerAccountType()
    {
        return this.customerAccountType;
    }

    public void setCustomerAccountType( String customerAccountType )
    {
        this.customerAccountType = customerAccountType;
    }

    @Column( name = "OLA_TRANSACTION_TYPE_ID", nullable = false, precision = 10, scale = 0 )
    public Long getOlaTransactionTypeId()
    {
        return this.olaTransactionTypeId;
    }

    public void setOlaTransactionTypeId( Long olaTransactionTypeId )
    {
        this.olaTransactionTypeId = olaTransactionTypeId;
    }

    @Column( name = "OLA_TRANSACTION_TYPE", nullable = false, length = 50 )
    public String getOlaTransactionType()
    {
        return this.olaTransactionType;
    }

    public void setOlaTransactionType( String olaTransactionType )
    {
        this.olaTransactionType = olaTransactionType;
    }

    @Column( name = "LIMIT_TYPE_ID", nullable = false, precision = 10, scale = 0 )
    public Long getLimitTypeId()
    {
        return this.limitTypeId;
    }

    public void setLimitTypeId( Long limitTypeId )
    {
        this.limitTypeId = limitTypeId;
    }

    @Column( name = "LIMIT_TYPE", nullable = false, length = 50 )
    public String getLimitType()
    {
        return this.limitType;
    }

    public void setLimitType( String limitType )
    {
        this.limitType = limitType;
    }

    @Column( name = "LIMIT_MINIMUM", nullable = false, precision = 10, scale = 0 )
    public Long getLimitMinimum()
    {
        return this.limitMinimum;
    }

    public void setLimitMinimum( Long limitMinimum )
    {
        this.limitMinimum = limitMinimum;
    }

    @Column( name = "LIMIT_MAXIMUM", nullable = false, precision = 10, scale = 0 )
    public Long getLimitMaximum()
    {
        return this.limitMaximum;
    }

    public void setLimitMaximum( Long limitMaximum )
    {
        this.limitMaximum = limitMaximum;
    }

}