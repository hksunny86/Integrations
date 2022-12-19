package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="SETTLEMENT_TRANSACTION_DET_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="SETTLEMENT_TRANSACTION_DET_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "SETTLEMENT_TRANSACTION_DET_SEQ",sequenceName = "SETTLEMENT_TRANSACTION_DET_SEQ")
@Table(name = "SETTLEMENT_TRANSACTION_DETAIL")
public class SettlementTransactionDetailModel extends BasePersistableModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long settlementTransactionDetailId;

	private Date settlementDate;
	private String oracleNumber;
	private String daoRegion;
	private String daoBranch;
	private String daoRm;
	private String company;
	private String product;
	private String currency;
	private Double debitMovement;
	private Double creditMovement;
	private String inputUser;
	private String authUser;
	private String settlementDecription;
	private String transactionReference;

    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Short versionNo;

    private Date startDate;
    private Date endDate;
    
	private Long stakeholderBankInfoId;

	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSettlementTransactionDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setSettlementTransactionDetailId(primaryKey);
    }


   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&settlementTransactionID=" + getSettlementTransactionDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "settlementTransactionDetailId";
			return primaryKeyFieldName;				
    }
    
    @Id
    @Column(name = "SETTLEMENT_TRANSACTION_DET_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SETTLEMENT_TRANSACTION_DET_SEQ")
	public Long getSettlementTransactionDetailId() {
		return settlementTransactionDetailId;
	}

	public void setSettlementTransactionDetailId(Long settlementTransactionDetailId) {
		this.settlementTransactionDetailId = settlementTransactionDetailId;
	}
	
    @Column(name = "SETTLEMENT_DATE")
	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

    @Column(name = "ORACLE_NUMBER")
	public String getOracleNumber() {
		return oracleNumber;
	}

	public void setOracleNumber(String oracleNumber) {
		this.oracleNumber = oracleNumber;
	}

    @Column(name = "DAO_REGION")
	public String getDaoRegion() {
		return daoRegion;
	}

	public void setDaoRegion(String daoRegion) {
		this.daoRegion = daoRegion;
	}

    @Column(name = "DAO_BRANCH")
	public String getDaoBranch() {
		return daoBranch;
	}

	public void setDaoBranch(String daoBranch) {
		this.daoBranch = daoBranch;
	}

    @Column(name = "DAO_RM")
	public String getDaoRm() {
		return daoRm;
	}

	public void setDaoRm(String daoRm) {
		this.daoRm = daoRm;
	}

    @Column(name = "COMPANY")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

    @Column(name = "PRODUCT")
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

    @Column(name = "CURRENCY")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

    @Column(name = "DEBIT_MOVEMENT")
	public Double getDebitMovement() {
		return debitMovement;
	}

	public void setDebitMovement(Double debitMovement) {
		this.debitMovement = debitMovement;
	}

    @Column(name = "CREDIT_MOVEMENT")
	public Double getCreditMovement() {
		return creditMovement;
	}

	public void setCreditMovement(Double creditMovement) {
		this.creditMovement = creditMovement;
	}

    @Column(name = "INPUT_USER")
	public String getInputUser() {
		return inputUser;
	}

	public void setInputUser(String inputUser) {
		this.inputUser = inputUser;
	}

    @Column(name = "AUTH_USER")
	public String getAuthUser() {
		return authUser;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

    @Column(name = "SETTLEMENT_DESC")
	public String getSettlementDecription() {
		return settlementDecription;
	}

	public void setSettlementDecription(String settlementDecription) {
		this.settlementDecription = settlementDecription;
	}

    @Column(name = "TRANS_REF")
	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}


	
	
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

    @Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    @Column(name="STAKEHOLDER_BANK_INFO_ID")
    public Long getStakeholderBankInfoId() {
		return stakeholderBankInfoId;
	}

	public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
		this.stakeholderBankInfoId = stakeholderBankInfoId;
	}
	
	
}
