package com.inov8.microbank.common.model.portal.ola;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BB_SETTLEMENT_ACCOUNTS_VIEW")
public class BBSettlementAccountsViewModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = -1657882954021506736L;
	
	private Long accountId;
	private Long accountHolderId;
	private String accountNumber;
	private String balance;
	private String accountTitle;
	private double balanceDecrypted;

	/**
	 * Added by atif hussain
	 */
	private Long accountTypeId;
	private String accountTypeName;
	private String productId;
	private String productName;
	
	public BBSettlementAccountsViewModel() {
	}

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return accountId;
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "accountId";
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&accountId="+accountId;
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setAccountId( primaryKey );
    }

    @Id
    @Column( name = "ACCOUNT_ID" )
    public Long getAccountId()
    {
        return this.accountId;
    }

    public void setAccountId( Long accountId )
    {
        this.accountId = accountId;
    }

    @Column( name = "ACCOUNT_HOLDER_ID" )
	public Long getAccountHolderId() {
		return accountHolderId;
	}

	public void setAccountHolderId(Long accountHolderId) {
		this.accountHolderId = accountHolderId;
	}

    @Column( name = "ACCOUNT_NUMBER" )
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

    @Column( name = "BALANCE" )
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

    @Column( name = "ACCOUNT_TITLE" )
	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	@Transient
	public double getBalanceDecrypted() {
		return balanceDecrypted;
	}

	public void setBalanceDecrypted(double balanceDecrypted) {
		this.balanceDecrypted = balanceDecrypted;
	}

	@Column( name = "CUSTOMER_ACCOUNT_TYPE_ID" )
	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	@Column( name = "CUSTOMER_ACCOUNT_TYPE_NAME" )
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	@Column( name = "PRODUCT_ID" )
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@Column( name = "PRODUCT_NAME" )
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}