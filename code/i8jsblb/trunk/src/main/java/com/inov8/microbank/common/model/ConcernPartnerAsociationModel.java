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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernPartnerAsociationModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernPartnerAsociationModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONCERN_PARTNER_ASOCIATION_seq",sequenceName = "CONCERN_PARTNER_ASOCIATION_seq", allocationSize=1)
@Table(name = "CONCERN_PARTNER_ASOCIATION")
public class ConcernPartnerAsociationModel extends BasePersistableModel implements Serializable{
  

   private ConcernPartnerModel associatedPartnerIdConcernPartnerModel;
   private ConcernPartnerModel partnerIdConcernPartnerModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;


   private Long concernPartnerAsociationId;
   private Boolean active;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public ConcernPartnerAsociationModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernPartnerAsociationId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernPartnerAsociationId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernPartnerAsociationId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ASOCIATION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONCERN_PARTNER_ASOCIATION_seq")
   public Long getConcernPartnerAsociationId() {
      return concernPartnerAsociationId;
   }

   /**
    * Sets the value of the <code>concernPartnerAsociationId</code> property.
    *
    * @param concernPartnerAsociationId the value for the <code>concernPartnerAsociationId</code> property
    *    
		    */

   public void setConcernPartnerAsociationId(Long concernPartnerAsociationId) {
      this.concernPartnerAsociationId = concernPartnerAsociationId;
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
    * Returns the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ASSOCIATED_PARTNER_ID")    
   public ConcernPartnerModel getRelationAssociatedPartnerIdConcernPartnerModel(){
      return associatedPartnerIdConcernPartnerModel;
   }
    
   /**
    * Returns the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPartnerModel getAssociatedPartnerIdConcernPartnerModel(){
      return getRelationAssociatedPartnerIdConcernPartnerModel();
   }

   /**
    * Sets the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>associatedPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAssociatedPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      this.associatedPartnerIdConcernPartnerModel = concernPartnerModel;
   }
   
   /**
    * Sets the value of the <code>associatedPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>associatedPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setAssociatedPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      if(null != concernPartnerModel)
      {
      	setRelationAssociatedPartnerIdConcernPartnerModel((ConcernPartnerModel)concernPartnerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PARTNER_ID")    
   public ConcernPartnerModel getRelationPartnerIdConcernPartnerModel(){
      return partnerIdConcernPartnerModel;
   }
    
   /**
    * Returns the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPartnerModel getPartnerIdConcernPartnerModel(){
      return getRelationPartnerIdConcernPartnerModel();
   }

   /**
    * Sets the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>partnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      this.partnerIdConcernPartnerModel = concernPartnerModel;
   }
   
   /**
    * Sets the value of the <code>partnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>partnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      if(null != concernPartnerModel)
      {
      	setRelationPartnerIdConcernPartnerModel((ConcernPartnerModel)concernPartnerModel.clone());
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
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAssociatedPartnerId() {
      if (associatedPartnerIdConcernPartnerModel != null) {
         return associatedPartnerIdConcernPartnerModel.getConcernPartnerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernPartnerId</code> property.
    *
    * @param concernPartnerId the value for the <code>concernPartnerId</code> property
							    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setAssociatedPartnerId(Long concernPartnerId) {
      if(concernPartnerId == null)
      {      
      	associatedPartnerIdConcernPartnerModel = null;
      }
      else
      {
        associatedPartnerIdConcernPartnerModel = new ConcernPartnerModel();
      	associatedPartnerIdConcernPartnerModel.setConcernPartnerId(concernPartnerId);
      }      
   }

   /**
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPartnerId() {
      if (partnerIdConcernPartnerModel != null) {
         return partnerIdConcernPartnerModel.getConcernPartnerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernPartnerId</code> property.
    *
    * @param concernPartnerId the value for the <code>concernPartnerId</code> property
					    * @spring.validator type="required"
																	    */
   
   @javax.persistence.Transient
   public void setPartnerId(Long concernPartnerId) {
      if(concernPartnerId == null)
      {      
      	partnerIdConcernPartnerModel = null;
      }
      else
      {
        partnerIdConcernPartnerModel = new ConcernPartnerModel();
      	partnerIdConcernPartnerModel.setConcernPartnerId(concernPartnerId);
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
        checkBox += "_"+ getConcernPartnerAsociationId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernPartnerAsociationId=" + getConcernPartnerAsociationId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernPartnerAsociationId";
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
    	
    	associationModel.setClassName("ConcernPartnerModel");
    	associationModel.setPropertyName("relationAssociatedPartnerIdConcernPartnerModel");   		
   		associationModel.setValue(getRelationAssociatedPartnerIdConcernPartnerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernPartnerModel");
    	associationModel.setPropertyName("relationPartnerIdConcernPartnerModel");   		
   		associationModel.setValue(getRelationPartnerIdConcernPartnerModel());
   		
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
