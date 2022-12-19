package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;


/**
 * CashBankMappingModel entity.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="CASH_BANK_MAPPING_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="CASH_BANK_MAPPING_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "CASH_BANK_MAPPING_SEQ",sequenceName = "CASH_BANK_MAPPING_SEQ")
@Table(name = "CASH_BANK_MAPPING")
public class CashBankMappingModel extends BasePersistableModel {

	 private static final long serialVersionUID = -5619396320258962808L;
	 
	 private Long cashBankMappingId;
     private Long agentOpeningBalanceId;
     private Long transactionId;
     private Long productId;
     private Double tillDebitAmount;
     private Double tillCreditAmount;
     private Double bankDebitAmount;
     private Double bankCreditAmount;
     private Double tillBalAfterTx;
     private Double bankBalAfterTx;
     private AppUserModel createdByAppUserModel;
     private Date createdOn;
     private AppUserModel updatedByAppUserModel;
     private Date updatedOn;
     private Short versionNo;
     private Date transactionDate;
     private String description;
     
     //Following fields are used in Manual Upload
     private Boolean validRecord;
     private String cscId;
     private String agentAccountNo;
     private String agentMobileNo;

    public CashBankMappingModel() {
    }

    
   	@Column(name = "CASH_BANK_MAPPING_ID" , nullable = false )
   	@Id
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CASH_BANK_MAPPING_SEQ")
    public Long getCashBankMappingId() {
        return this.cashBankMappingId;
    }
    
    public void setCashBankMappingId(Long cashBankMappingId) {
        this.cashBankMappingId = cashBankMappingId;
    }
    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
   	public Long getPrimaryKey() {
        return getCashBankMappingId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   	@javax.persistence.Transient
   	public void setPrimaryKey(Long primaryKey) {
   		setCashBankMappingId(primaryKey);
    }

    
    @Column(name="AGENT_OPENING_BALANCE_ID")
    public Long getAgentOpeningBalanceId() {
        return this.agentOpeningBalanceId;
    }
    
    public void setAgentOpeningBalanceId(Long agentOpeningBalanceId) {
        this.agentOpeningBalanceId = agentOpeningBalanceId;
    }
    
    @Column(name="TRANSACTION_ID")
    public Long getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    @Column(name="PRODUCT_ID")
    public Long getProductId() {
        return this.productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    @Column(name="TILL_DEBIT_AMOUNT")
    public Double getTillDebitAmount() {
        return this.tillDebitAmount;
    }
    
    public void setTillDebitAmount(Double tillDebitAmount) {
        this.tillDebitAmount = tillDebitAmount;
    }
    
    @Column(name="TILL_CREDIT_AMOUNT")
    public Double getTillCreditAmount() {
        return this.tillCreditAmount;
    }
    
    public void setTillCreditAmount(Double tillCreditAmount) {
        this.tillCreditAmount = tillCreditAmount;
    }
    
    @Column(name="BANK_DEBIT_AMOUNT")
    public Double getBankDebitAmount() {
        return this.bankDebitAmount;
    }
    
    public void setBankDebitAmount(Double bankDebitAmount) {
        this.bankDebitAmount = bankDebitAmount;
    }
    
    @Column(name="BANK_CREDIT_AMOUNT")
    public Double getBankCreditAmount() {
        return this.bankCreditAmount;
    }
    
    public void setBankCreditAmount(Double bankCreditAmount) {
        this.bankCreditAmount = bankCreditAmount;
    }
    
    @Column(name="TILL_BAL_AFTER_TX")
    public Double getTillBalAfterTx() {
        return this.tillBalAfterTx;
    }
    
    public void setTillBalAfterTx(Double tillBalAfterTx) {
        this.tillBalAfterTx = tillBalAfterTx;
    }
    
    @Column(name="BANK_BAL_AFTER_TX")
    public Double getBankBalAfterTx() {
        return this.bankBalAfterTx;
    }
    
    public void setBankBalAfterTx(Double bankBalAfterTx) {
        this.bankBalAfterTx = bankBalAfterTx;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
    

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")    
    public AppUserModel getRelationCreatedByAppUserModel(){
       return createdByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
       return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
       this.createdByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	updatedByAppUserModel = null;
       }
       else
       {
         updatedByAppUserModel = new AppUserModel();
       	updatedByAppUserModel.setAppUserId(appUserId);
       }      
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	createdByAppUserModel = null;
       }
       else
       {
         createdByAppUserModel = new AppUserModel();
       	createdByAppUserModel.setAppUserId(appUserId);
       }      
    }
    
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Version
    @Column(name="VERSION_NO")
    public Short getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Short versionNo) {
        this.versionNo = versionNo;
    }

	@javax.persistence.Transient
	public Boolean getValidRecord() {
		return validRecord;
	}

	public void setValidRecord(Boolean validRecord) {
		this.validRecord = validRecord;
	}

	@Transient
	public String getPrimaryKeyFieldName() {
		return null;
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return null;
	}

	@javax.persistence.Transient
	public String getCscId() {
		return cscId;
	}

	public void setCscId(String cscId) {
		this.cscId = cscId;
	}

	@javax.persistence.Transient
	public String getAgentAccountNo() {
		return agentAccountNo;
	}

	public void setAgentAccountNo(String agentAccountNo) {
		this.agentAccountNo = agentAccountNo;
	}

	@javax.persistence.Transient
	public String getAgentMobileNo() {
		return agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

    @Column(name="TRANSACTION_DATE")
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

    @Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}