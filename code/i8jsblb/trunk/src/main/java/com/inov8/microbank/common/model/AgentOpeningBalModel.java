package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;


/**
 * AgentOpeningBalModel entity.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="AGENT_OPENING_BALANCE_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="AGENT_OPENING_BALANCE_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "AGENT_OPENING_BALANCE_SEQ",sequenceName = "AGENT_OPENING_BALANCE_SEQ")
@Table(name = "AGENT_OPENING_BALANCE")
public class AgentOpeningBalModel  extends BasePersistableModel {

	 private static final long serialVersionUID = 8520926302352286877L;

	 private Long agentOpeningBalanceId;
     private String msisdn;
     private String agentId;
     private Double openingTillBalance;
     private Double openingAccountBalance;
     private String accountNumber;
     private Double runningTillBalance;
     private Double runningAccountBalance;
     private Date balDate;
     private AppUserModel createdByAppUserModel;
     private Date createdOn;
     private AppUserModel updatedByAppUserModel;
     private Date updatedOn;
     private Short versionNo;


    public AgentOpeningBalModel() {
    }

   
   	@Column(name = "AGENT_OPENING_BALANCE_ID" , nullable = false )
   	@Id
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AGENT_OPENING_BALANCE_SEQ")
    public Long getAgentOpeningBalanceId() {
        return this.agentOpeningBalanceId;
    }
    
    public void setAgentOpeningBalanceId(Long agentOpeningBalanceId) {
        this.agentOpeningBalanceId = agentOpeningBalanceId;
    }

    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAgentOpeningBalanceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAgentOpeningBalanceId(primaryKey);
    }

    
    
    @Column(name="MSISDN")
    public String getMsisdn() {
        return this.msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    @Column(name="AGENT_ID")
    public String getAgentId() {
        return this.agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    @Column(name="OPENING_TILL_BALANCE")
    public Double getOpeningTillBalance() {
        return this.openingTillBalance;
    }
    
    public void setOpeningTillBalance(Double openingTillBalance) {
        this.openingTillBalance = openingTillBalance;
    }
    
    @Column(name="OPENING_ACCOUNT_BALANCE")
    public Double getOpeningAccountBalance() {
        return this.openingAccountBalance;
    }
    
    public void setOpeningAccountBalance(Double openingAccountBalance) {
        this.openingAccountBalance = openingAccountBalance;
    }

    @Column(name="BAL_DATE")
    public Date getBalDate() {
        return this.balDate;
    }
    
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
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


	@Transient
	public String getPrimaryKeyFieldName() {
		return null;
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return null;
	}

    @Column(name="ACCOUNT_NUMBER")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

    @Column(name="RUNNING_TILL_BALANCE")
	public Double getRunningTillBalance() {
		return runningTillBalance;
	}

	public void setRunningTillBalance(Double runningTillBalance) {
		this.runningTillBalance = runningTillBalance;
	}

    @Column(name="RUNNING_ACCOUNT_BALANCE")
	public Double getRunningAccountBalance() {
		return runningAccountBalance;
	}

	public void setRunningAccountBalance(Double runningAccountBalance) {
		this.runningAccountBalance = runningAccountBalance;
	}

}