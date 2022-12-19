package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionShAcctsModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionShAcctsModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMISSION_SH_ACCTS_seq",sequenceName = "COMMISSION_SH_ACCTS_seq", allocationSize=1)
@Table(name = "COMMISSION_SH_ACCTS")
public class CommissionShAcctsModel extends BasePersistableModel implements Serializable {
  

   private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;
   private CommissionShAcctsTypeModel cmshaccttypeIdCommissionShAcctsTypeModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;


   private Long cmshacctsId;
   private String accountNo;
   private String accountTitle;
   private Boolean active;
   private Date createdOn;
   private Date updatedOn;

   /**
    * Default constructor.
    */
   public CommissionShAcctsModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCmshacctsId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCmshacctsId(primaryKey);
    }

   /**
    * Returns the value of the <code>cmshacctsId</code> property.
    *
    */
      @Column(name = "CMSHACCTS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMISSION_SH_ACCTS_seq")
   public Long getCmshacctsId() {
      return cmshacctsId;
   }

   /**
    * Sets the value of the <code>cmshacctsId</code> property.
    *
    * @param cmshacctsId the value for the <code>cmshacctsId</code> property
    *    
		    */

   public void setCmshacctsId(Long cmshacctsId) {
      this.cmshacctsId = cmshacctsId;
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
    * Returns the value of the <code>accountTitle</code> property.
    *
    */
      @Column(name = "ACCOUNT_TITLE" , nullable = false , length=50 )
   public String getAccountTitle() {
      return accountTitle;
   }

   /**
    * Sets the value of the <code>accountTitle</code> property.
    *
    * @param accountTitle the value for the <code>accountTitle</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountTitle(String accountTitle) {
      this.accountTitle = accountTitle;
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
        checkBox += "_"+ getCmshacctsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&cmshacctsId=" + getCmshacctsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "cmshacctsId";
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
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
