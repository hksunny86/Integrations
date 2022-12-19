package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The StakeholderBankInfoModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="StakeholderBankInfoModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "STAKEHOLDER_BANK_INFO_seq",sequenceName = "STAKEHOLDER_BANK_INFO_seq", allocationSize=1)
@Table(name = "STAKEHOLDER_BANK_INFO")
public class StakeholderBankInfoModel extends BasePersistableModel implements Serializable {
 
   private static final long serialVersionUID = 1583976697778561985L;
   
   public static final String ACCOUNT_TYPE_BLB = "BLB";
   public static final String ACCOUNT_TYPE_CORE = "T24";
   public static final String ACCOUNT_TYPE_OF_SET = "OF_SET";
   
   private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;
   private CommissionShAcctsTypeModel cmshaccttypeIdCommissionShAcctsTypeModel;
   private StakeholderBankInfoModel ofSettlementStakeholderBankInfoModel;
   private StakeholderBankInfoModel t24StakeholderBankInfoModel;
   private BankModel bankIdBankModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private DistributorModel distributorIdDistributorModel;
   private OlaCustomerAccountTypeModel olaCustomerAccountTypeIdModel;
   private ProductModel	productIdModel;

   private Long stakeholderBankInfoId;
   private String name;
   private String accountNo;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean active;
   private String accountType;
   private Long glTypeModel;
   private Long parentGlTypeModel;
   private Boolean filer;
   /**
    * Default constructor.
    */
   public StakeholderBankInfoModel() {
   }

    public StakeholderBankInfoModel(Long stakeholderBankInfoId)
    {
		super();
		this.setPrimaryKey(stakeholderBankInfoId);
	}
    
    public StakeholderBankInfoModel(String accountNo) {
        this.accountNo = accountNo;
     }

	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStakeholderBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStakeholderBankInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>stakeholderBankInfoId</code> property.
    *
    */
      @Column(name = "STAKEHOLDER_BANK_INFO_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STAKEHOLDER_BANK_INFO_seq")
   public Long getStakeholderBankInfoId() {
      return stakeholderBankInfoId;
   }

   /**
    * Sets the value of the <code>stakeholderBankInfoId</code> property.
    *
    * @param stakeholderBankInfoId the value for the <code>stakeholderBankInfoId</code> property
    *    
		    */

   public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
      this.stakeholderBankInfoId = stakeholderBankInfoId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=100 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>accountNo</code> property.
    *
    */
      @Column(name = "ACCOUNT_NO" , nullable = false , length=50 )
   public String getAccountNo() {
      return accountNo;
   }

   /**
    * Sets the value of the <code>accountNo</code> property.
    *
    * @param accountNo the value for the <code>accountNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNo(String accountNo) {
      this.accountNo = accountNo;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
 * @return the ofSettlementStakeholderBankInfoModel
 */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "OF_SETT_SHBI_ID")    
public StakeholderBankInfoModel getOfSettlementStakeholderBankInfoModel() {
	return ofSettlementStakeholderBankInfoModel;
}

/**
 * @param ofSettlementStakeholderBankInfoModel the ofSettlementStakeholderBankInfoModel to set
 */
@javax.persistence.Transient
public void setOfSettlementStakeholderBankInfoModel(StakeholderBankInfoModel ofSettlementStakeholderBankInfoModel) {
   if(null != ofSettlementStakeholderBankInfoModel)
   {
	   this.ofSettlementStakeholderBankInfoModel = (StakeholderBankInfoModel)ofSettlementStakeholderBankInfoModel.clone();
   }      
}

@javax.persistence.Transient
public Long getOfSettlementStakeholderBankInfoModelId() {
   if (ofSettlementStakeholderBankInfoModel != null) {
      return ofSettlementStakeholderBankInfoModel.getStakeholderBankInfoId();
   } else {
      return null;
   }
}
																			   
@javax.persistence.Transient
public void setOfSettlementStakeholderBankInfoModelId(Long id) {
   if(id == null)
   {      
	   ofSettlementStakeholderBankInfoModel = null;
   }
   else
   {
	   ofSettlementStakeholderBankInfoModel = new StakeholderBankInfoModel();
	   ofSettlementStakeholderBankInfoModel.setStakeholderBankInfoId(id);
   }      
}


/**
 * @return the t24StakeholderBankInfoModel
 */
@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
@JoinColumn(name = "T24_SHBI_ID")    
public StakeholderBankInfoModel getT24StakeholderBankInfoModel() {
	return t24StakeholderBankInfoModel;
}

/**
 * @param t24StakeholderBankInfoModel the t24StakeholderBankInfoModel to set
 */
public void setT24StakeholderBankInfoModel(StakeholderBankInfoModel t24StakeholderBankInfoModel) {
	 if(null != t24StakeholderBankInfoModel)
	   {
		   this.t24StakeholderBankInfoModel = (StakeholderBankInfoModel)t24StakeholderBankInfoModel.clone();
	   } 
}
@javax.persistence.Transient
public Long getT24StakeholderBankInfoModelId() {
   if (t24StakeholderBankInfoModel != null) {
      return t24StakeholderBankInfoModel.getStakeholderBankInfoId();
   } else {
      return null;
   }
}
																			   
@javax.persistence.Transient
public void setT24StakeholderBankInfoModelId(Long id) {
   if(id == null)
   {      
	   t24StakeholderBankInfoModel = null;
   }
   else
   {
	   t24StakeholderBankInfoModel = new StakeholderBankInfoModel();
	   t24StakeholderBankInfoModel.setStakeholderBankInfoId(id);
   }      
}

/**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")    
   public CommissionStakeholderModel getRelationCommissionStakeholderIdCommissionStakeholderModel(){
      return commissionStakeholderIdCommissionStakeholderModel;
   }
    
   /**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionStakeholderModel getCommissionStakeholderIdCommissionStakeholderModel(){
      return getRelationCommissionStakeholderIdCommissionStakeholderModel();
   }

   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      this.commissionStakeholderIdCommissionStakeholderModel = commissionStakeholderModel;
   }
   
   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      if(null != commissionStakeholderModel)
      {
      	setRelationCommissionStakeholderIdCommissionStakeholderModel((CommissionStakeholderModel)commissionStakeholderModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @return the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CMSHACCTTYPE_ID")    
   public CommissionShAcctsTypeModel getRelationCmshaccttypeIdCommissionShAcctsTypeModel(){
      return cmshaccttypeIdCommissionShAcctsTypeModel;
   }
    
   /**
    * Returns the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @return the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionShAcctsTypeModel getCmshaccttypeIdCommissionShAcctsTypeModel(){
      return getRelationCmshaccttypeIdCommissionShAcctsTypeModel();
   }

   /**
    * Sets the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @param commissionShAcctsTypeModel a value for <code>cmshaccttypeIdCommissionShAcctsTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCmshaccttypeIdCommissionShAcctsTypeModel(CommissionShAcctsTypeModel commissionShAcctsTypeModel) {
      this.cmshaccttypeIdCommissionShAcctsTypeModel = commissionShAcctsTypeModel;
   }
   
   /**
    * Sets the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @param commissionShAcctsTypeModel a value for <code>cmshaccttypeIdCommissionShAcctsTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCmshaccttypeIdCommissionShAcctsTypeModel(CommissionShAcctsTypeModel commissionShAcctsTypeModel) {
      if(null != commissionShAcctsTypeModel)
      {
      	setRelationCmshaccttypeIdCommissionShAcctsTypeModel((CommissionShAcctsTypeModel)commissionShAcctsTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "BANK_ID")    
   public BankModel getRelationBankIdBankModel(){
      return bankIdBankModel;
   }
    
   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public BankModel getBankIdBankModel(){
      return getRelationBankIdBankModel();
   }

   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationBankIdBankModel(BankModel bankModel) {
      this.bankIdBankModel = bankModel;
   }
   
   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setBankIdBankModel(BankModel bankModel) {
      if(null != bankModel)
      {
      	setRelationBankIdBankModel((BankModel)bankModel.clone());
      }      
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
   



   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommissionStakeholderId() {
      if (commissionStakeholderIdCommissionStakeholderModel != null) {
         return commissionStakeholderIdCommissionStakeholderModel.getCommissionStakeholderId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
							    * @spring.validator type="required"
																					    */
   
   @javax.persistence.Transient
   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      if(commissionStakeholderId == null)
      {      
      	commissionStakeholderIdCommissionStakeholderModel = null;
      }
      else
      {
        commissionStakeholderIdCommissionStakeholderModel = new CommissionStakeholderModel();
      	commissionStakeholderIdCommissionStakeholderModel.setCommissionStakeholderId(commissionStakeholderId);
      }      
   }

   /**
    * Returns the value of the <code>cmshacctstypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCmshaccttypeId() {
      if (cmshaccttypeIdCommissionShAcctsTypeModel != null) {
         return cmshaccttypeIdCommissionShAcctsTypeModel.getCmshacctstypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>cmshacctstypeId</code> property.
    *
    * @param cmshacctstypeId the value for the <code>cmshacctstypeId</code> property
																									    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setCmshaccttypeId(Long cmshacctstypeId) {
      if(cmshacctstypeId == null)
      {      
      	cmshaccttypeIdCommissionShAcctsTypeModel = null;
      }
      else
      {
        cmshaccttypeIdCommissionShAcctsTypeModel = new CommissionShAcctsTypeModel();
      	cmshaccttypeIdCommissionShAcctsTypeModel.setCmshacctstypeId(cmshacctstypeId);
      }      
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getBankId() {
      if (bankIdBankModel != null) {
         return bankIdBankModel.getBankId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
					    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setBankId(Long bankId) {
      if(bankId == null)
      {      
      	bankIdBankModel = null;
      }
      else
      {
        bankIdBankModel = new BankModel();
      	bankIdBankModel.setBankId(bankId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																									    */
   
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

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCreatedBy() {
      if (createdByAppUserModel != null) {
         return createdByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																									    */
   
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


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getStakeholderBankInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&stakeholderBankInfoId=" + getStakeholderBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "stakeholderBankInfoId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	 	  associationModel = new AssociationModel();
     	
     	associationModel.setClassName("StakeholderBankInfoModel");
     	associationModel.setPropertyName("ofSettlementStakeholderBankInfoModel");   		
   		associationModel.setValue(getOfSettlementStakeholderBankInfoModel());
    		
    	associationModelList.add(associationModel);
    	
    	  associationModel = new AssociationModel();
       	
       	associationModel.setClassName("StakeholderBankInfoModel");
       	associationModel.setPropertyName("t24StakeholderBankInfoModel");   		
     		associationModel.setValue(getT24StakeholderBankInfoModel());
      		
      	associationModelList.add(associationModel);
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionStakeholderModel");
   		associationModel.setPropertyName("relationCommissionStakeholderIdCommissionStakeholderModel");   		
    	associationModel.setValue(getRelationCommissionStakeholderIdCommissionStakeholderModel());
    		
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionShAcctsTypeModel");
    	associationModel.setPropertyName("relationCmshaccttypeIdCommissionShAcctsTypeModel");   		
   		associationModel.setValue(getRelationCmshaccttypeIdCommissionShAcctsTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
   		associationModelList.add(associationModel);
   		
   			associationModel = new AssociationModel();
    	
    	associationModel.setClassName("OlaCustomerAccountTypeModel");
    	associationModel.setPropertyName("customerAccountTypeIdModel");   		
   		associationModel.setValue(getCustomerAccountTypeIdModel());
   		
   		associationModelList.add(associationModel);

			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
	   	 associationModel.setClassName("ProductModel");
	     associationModel.setPropertyName("productIdModel");
	     associationModel.setValue(getProductIdModel());
	     
	     associationModelList.add(associationModel);
	   		
			    	
    	return associationModelList;
    }    
    

    /**
     * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     * @return the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRIBUTOR_ID")    
    public DistributorModel getRelationDistributorIdDistributorModel(){
       return distributorIdDistributorModel;
    }
     
    /**
     * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     * @return the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public DistributorModel getDistributorIdDistributorModel(){
       return getRelationDistributorIdDistributorModel();
    }

    /**
     * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
       this.distributorIdDistributorModel = distributorModel;
    }
    
    /**
     * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
     *
     * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
     */
    @javax.persistence.Transient
    public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
       if(null != distributorModel)
       {
       	setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
       }      
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ACCOUNT_TYPE_ID")  
	public OlaCustomerAccountTypeModel getCustomerAccountTypeIdModel() {
		return olaCustomerAccountTypeIdModel;
	}

	public void setCustomerAccountTypeIdModel(
			OlaCustomerAccountTypeModel olaCustomerAccountTypeIdModel) {
		this.olaCustomerAccountTypeIdModel = olaCustomerAccountTypeIdModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")  
	public ProductModel getProductIdModel() {
		return productIdModel;
	}

	public void setProductIdModel(ProductModel productIdModel) {
		this.productIdModel = productIdModel;
	}
    
	@javax.persistence.Transient
	public Long getProductId() {
      if (productIdModel != null) {
          return productIdModel.getProductId();
       } else {
          return null;
       }
	}

	public void setProductId(Long productId) {
	      if(productId == null)
	      {      
	    	  productIdModel = null;
	      }
	      else
	      {
	    	  productIdModel = new ProductModel();
	    	  productIdModel.setProductId(productId);
	      }      
	}

	@Column(name = "ACC_TYPE" )
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

   @Column(name = "GL_TYPE_ID" )
   public Long getGlTypeModel(){
      return  glTypeModel;
   }

   public void setGlTypeModel(Long glTypeId){
      this.glTypeModel=glTypeId;
   }

   @Column(name = "PARENT_GL_ID" )
   public Long getParentGlTypeModel(){
      return parentGlTypeModel;
   }

   public void setParentGlTypeModel(Long parentGlTypeId){
      this.parentGlTypeModel=parentGlTypeId;
   }

@Column(name = "IS_FILER" )
public Boolean getFiler() {
	return filer;
}

public void setFiler(Boolean filer) {
	this.filer = filer;
}

}