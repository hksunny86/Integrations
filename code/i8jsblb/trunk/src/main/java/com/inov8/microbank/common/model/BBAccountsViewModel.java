package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BB_ACCOUNTS_VIEW")
public class BBAccountsViewModel extends BasePersistableModel {
	
	private static final long serialVersionUID = -524992424035103437L;
	private Long accountId;
	private Long accountHolderId;
	private String accountNumber;
	private String balance;
	private Long accountTypeId;
	private String title;
	private Boolean isActive;
	private Long statusId;
	private Boolean isCustomerAccountType;
	private String acState;
	private String mobileNo;

/**
    * Default constructor.
    */
   public BBAccountsViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAccountId(primaryKey);
    }


    @Column(name = "ACCOUNT_ID", nullable = false )
    @Id
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAccountId();
        checkBox += "\"/>";
        return checkBox;
    }


   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountId=" + getAccountId();
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "accountId";
			return primaryKeyFieldName;				
    }

    @Column(name = "ACCOUNT_HOLDER_ID" )
	public Long getAccountHolderId() {
		return accountHolderId;
	}

	public void setAccountHolderId(Long accountHolderId) {
		this.accountHolderId = accountHolderId;
	}

	@Column(name = "ACCOUNT_NUMBER" )
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "BALANCE" )
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Column(name = "CUSTOMER_ACCOUNT_TYPE_ID" )
	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	@Column(name = "ACCOUNT_TITLE" )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "IS_ACTIVE" )
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "STATUS_ID")
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "IS_CUSTOMER_ACCOUNT_TYPE" )
	public Boolean getIsCustomerAccountType() {
		return isCustomerAccountType;
	}

	public void setIsCustomerAccountType(Boolean isCustomerAccountType) {
		this.isCustomerAccountType = isCustomerAccountType;
	}

	@Column(name = "AC_STATE" )
	public String getAcState() {
		return acState;
	}

	public void setAcState(String acState) {
		this.acState = acState;
	}
	
	@Column(name = "MOBILE_NO" )
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}
