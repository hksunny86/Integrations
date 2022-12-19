package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONCERN_seq",sequenceName = "CONCERN_seq", allocationSize=1)
@Table(name = "CONCERN")
public class ConcernModel extends BasePersistableModel implements Serializable{
  

   private ConcernStatusModel concernStatusIdConcernStatusModel;
   private ConcernPriorityModel concernPriorityIdConcernPriorityModel;
   private ConcernPartnerModel recipientPartnerIdConcernPartnerModel;
   private ConcernPartnerModel initiatorPartnerIdConcernPartnerModel;
   private ConcernCategoryModel concernCategoryIdConcernCategoryModel;
   private ConcernModel parentConcernIdConcernModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<ConcernModel> parentConcernIdConcernModelList = new ArrayList<ConcernModel>();
   private Collection<ConcernHistoryModel> concernIdConcernHistoryModelList = new ArrayList<ConcernHistoryModel>();

   private Long concernId;
   private String comments;
   private String concernCode;
   private String title;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private String description;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ConcernModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernId</code> property.
    *
    */
      @Column(name = "CONCERN_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONCERN_seq")
   public Long getConcernId() {
      return concernId;
   }

   /**
    * Sets the value of the <code>concernId</code> property.
    *
    * @param concernId the value for the <code>concernId</code> property
    *    
		    */

   public void setConcernId(Long concernId) {
      this.concernId = concernId;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS" , nullable = false , length=1000 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1000"
    */

   public void setComments(String comments) {
      this.comments = comments;
   }

   /**
    * Returns the value of the <code>concernCode</code> property.
    *
    */
      @Column(name = "CONCERN_CODE" , nullable = false , length=50 )
   public String getConcernCode() {
      return concernCode;
   }

   /**
    * Sets the value of the <code>concernCode</code> property.
    *
    * @param concernCode the value for the <code>concernCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConcernCode(String concernCode) {
      this.concernCode = concernCode;
   }

   /**
    * Returns the value of the <code>title</code> property.
    *
    */
      @Column(name = "TITLE" , nullable = false , length=250 )
   public String getTitle() {
      return title;
   }

   /**
    * Sets the value of the <code>title</code> property.
    *
    * @param title the value for the <code>title</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setTitle(String title) {
      this.title = title;
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
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Returns the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CONCERN_STATUS_ID")    
   public ConcernStatusModel getRelationConcernStatusIdConcernStatusModel(){
      return concernStatusIdConcernStatusModel;
   }
    
   /**
    * Returns the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernStatusModel getConcernStatusIdConcernStatusModel(){
      return getRelationConcernStatusIdConcernStatusModel();
   }

   /**
    * Sets the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>concernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      this.concernStatusIdConcernStatusModel = concernStatusModel;
   }
   
   /**
    * Sets the value of the <code>concernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>concernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      if(null != concernStatusModel)
      {
      	setRelationConcernStatusIdConcernStatusModel((ConcernStatusModel)concernStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    * @return the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CONCERN_PRIORITY_ID")    
   public ConcernPriorityModel getRelationConcernPriorityIdConcernPriorityModel(){
      return concernPriorityIdConcernPriorityModel;
   }
    
   /**
    * Returns the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    * @return the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPriorityModel getConcernPriorityIdConcernPriorityModel(){
      return getRelationConcernPriorityIdConcernPriorityModel();
   }

   /**
    * Sets the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    * @param concernPriorityModel a value for <code>concernPriorityIdConcernPriorityModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationConcernPriorityIdConcernPriorityModel(ConcernPriorityModel concernPriorityModel) {
      this.concernPriorityIdConcernPriorityModel = concernPriorityModel;
   }
   
   /**
    * Sets the value of the <code>concernPriorityIdConcernPriorityModel</code> relation property.
    *
    * @param concernPriorityModel a value for <code>concernPriorityIdConcernPriorityModel</code>.
    */
   @javax.persistence.Transient
   public void setConcernPriorityIdConcernPriorityModel(ConcernPriorityModel concernPriorityModel) {
      if(null != concernPriorityModel)
      {
      	setRelationConcernPriorityIdConcernPriorityModel((ConcernPriorityModel)concernPriorityModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RECIPIENT_PARTNER_ID")    
   public ConcernPartnerModel getRelationRecipientPartnerIdConcernPartnerModel(){
      return recipientPartnerIdConcernPartnerModel;
   }
    
   /**
    * Returns the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPartnerModel getRecipientPartnerIdConcernPartnerModel(){
      return getRelationRecipientPartnerIdConcernPartnerModel();
   }

   /**
    * Sets the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>recipientPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRecipientPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      this.recipientPartnerIdConcernPartnerModel = concernPartnerModel;
   }
   
   /**
    * Sets the value of the <code>recipientPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>recipientPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRecipientPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      if(null != concernPartnerModel)
      {
      	setRelationRecipientPartnerIdConcernPartnerModel((ConcernPartnerModel)concernPartnerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "INITIATOR_PARTNER_ID")    
   public ConcernPartnerModel getRelationInitiatorPartnerIdConcernPartnerModel(){
      return initiatorPartnerIdConcernPartnerModel;
   }
    
   /**
    * Returns the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    * @return the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPartnerModel getInitiatorPartnerIdConcernPartnerModel(){
      return getRelationInitiatorPartnerIdConcernPartnerModel();
   }

   /**
    * Sets the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>initiatorPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationInitiatorPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      this.initiatorPartnerIdConcernPartnerModel = concernPartnerModel;
   }
   
   /**
    * Sets the value of the <code>initiatorPartnerIdConcernPartnerModel</code> relation property.
    *
    * @param concernPartnerModel a value for <code>initiatorPartnerIdConcernPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setInitiatorPartnerIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      if(null != concernPartnerModel)
      {
      	setRelationInitiatorPartnerIdConcernPartnerModel((ConcernPartnerModel)concernPartnerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    * @return the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CONCERN_CATEGORY_ID")    
   public ConcernCategoryModel getRelationConcernCategoryIdConcernCategoryModel(){
      return concernCategoryIdConcernCategoryModel;
   }
    
   /**
    * Returns the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    * @return the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernCategoryModel getConcernCategoryIdConcernCategoryModel(){
      return getRelationConcernCategoryIdConcernCategoryModel();
   }

   /**
    * Sets the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    * @param concernCategoryModel a value for <code>concernCategoryIdConcernCategoryModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationConcernCategoryIdConcernCategoryModel(ConcernCategoryModel concernCategoryModel) {
      this.concernCategoryIdConcernCategoryModel = concernCategoryModel;
   }
   
   /**
    * Sets the value of the <code>concernCategoryIdConcernCategoryModel</code> relation property.
    *
    * @param concernCategoryModel a value for <code>concernCategoryIdConcernCategoryModel</code>.
    */
   @javax.persistence.Transient
   public void setConcernCategoryIdConcernCategoryModel(ConcernCategoryModel concernCategoryModel) {
      if(null != concernCategoryModel)
      {
      	setRelationConcernCategoryIdConcernCategoryModel((ConcernCategoryModel)concernCategoryModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    * @return the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PARENT_CONCERN_ID")    
   public ConcernModel getRelationParentConcernIdConcernModel(){
      return parentConcernIdConcernModel;
   }
    
   /**
    * Returns the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    * @return the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernModel getParentConcernIdConcernModel(){
      return getRelationParentConcernIdConcernModel();
   }

   /**
    * Sets the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    * @param concernModel a value for <code>parentConcernIdConcernModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationParentConcernIdConcernModel(ConcernModel concernModel) {
      this.parentConcernIdConcernModel = concernModel;
   }
   
   /**
    * Sets the value of the <code>parentConcernIdConcernModel</code> relation property.
    *
    * @param concernModel a value for <code>parentConcernIdConcernModel</code>.
    */
   @javax.persistence.Transient
   public void setParentConcernIdConcernModel(ConcernModel concernModel) {
      if(null != concernModel)
      {
      	setRelationParentConcernIdConcernModel((ConcernModel)concernModel.clone());
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
    * Add the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be added.
    */
    
   public void addParentConcernIdConcernModel(ConcernModel concernModel) {
      concernModel.setRelationParentConcernIdConcernModel(this);
      parentConcernIdConcernModelList.add(concernModel);
   }
   
   /**
    * Remove the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be removed.
    */
   
   public void removeParentConcernIdConcernModel(ConcernModel concernModel) {      
      concernModel.setRelationParentConcernIdConcernModel(null);
      parentConcernIdConcernModelList.remove(concernModel);      
   }

   /**
    * Get a list of related ConcernModel objects of the ConcernModel object.
    * These objects are in a bidirectional one-to-many relation by the ParentConcernId member.
    *
    * @return Collection of ConcernModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationParentConcernIdConcernModel")
   @JoinColumn(name = "PARENT_CONCERN_ID")
   public Collection<ConcernModel> getParentConcernIdConcernModelList() throws Exception {
   		return parentConcernIdConcernModelList;
   }


   /**
    * Set a list of ConcernModel related objects to the ConcernModel object.
    * These objects are in a bidirectional one-to-many relation by the ParentConcernId member.
    *
    * @param concernModelList the list of related objects.
    */
    public void setParentConcernIdConcernModelList(Collection<ConcernModel> concernModelList) throws Exception {
		this.parentConcernIdConcernModelList = concernModelList;
   }


   /**
    * Add the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be added.
    */
    
   public void addConcernIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {
      concernHistoryModel.setRelationConcernIdConcernModel(this);
      concernIdConcernHistoryModelList.add(concernHistoryModel);
   }
   
   /**
    * Remove the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be removed.
    */
   
   public void removeConcernIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {      
      concernHistoryModel.setRelationConcernIdConcernModel(null);
      concernIdConcernHistoryModelList.remove(concernHistoryModel);      
   }

   /**
    * Get a list of related ConcernHistoryModel objects of the ConcernModel object.
    * These objects are in a bidirectional one-to-many relation by the ConcernId member.
    *
    * @return Collection of ConcernHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationConcernIdConcernModel")
   @JoinColumn(name = "CONCERN_ID")
   public Collection<ConcernHistoryModel> getConcernIdConcernHistoryModelList() throws Exception {
   		return concernIdConcernHistoryModelList;
   }


   /**
    * Set a list of ConcernHistoryModel related objects to the ConcernModel object.
    * These objects are in a bidirectional one-to-many relation by the ConcernId member.
    *
    * @param concernHistoryModelList the list of related objects.
    */
    public void setConcernIdConcernHistoryModelList(Collection<ConcernHistoryModel> concernHistoryModelList) throws Exception {
		this.concernIdConcernHistoryModelList = concernHistoryModelList;
   }



   /**
    * Returns the value of the <code>concernStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getConcernStatusId() {
      if (concernStatusIdConcernStatusModel != null) {
         return concernStatusIdConcernStatusModel.getConcernStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernStatusId</code> property.
    *
    * @param concernStatusId the value for the <code>concernStatusId</code> property
											    * @spring.validator type="required"
																									    */
   
   @javax.persistence.Transient
   public void setConcernStatusId(Long concernStatusId) {
      if(concernStatusId == null)
      {      
      	concernStatusIdConcernStatusModel = null;
      }
      else
      {
        concernStatusIdConcernStatusModel = new ConcernStatusModel();
      	concernStatusIdConcernStatusModel.setConcernStatusId(concernStatusId);
      }      
   }

   /**
    * Returns the value of the <code>concernPriorityId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getConcernPriorityId() {
      if (concernPriorityIdConcernPriorityModel != null) {
         return concernPriorityIdConcernPriorityModel.getConcernPriorityId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernPriorityId</code> property.
    *
    * @param concernPriorityId the value for the <code>concernPriorityId</code> property
													    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setConcernPriorityId(Long concernPriorityId) {
      if(concernPriorityId == null)
      {      
      	concernPriorityIdConcernPriorityModel = null;
      }
      else
      {
        concernPriorityIdConcernPriorityModel = new ConcernPriorityModel();
      	concernPriorityIdConcernPriorityModel.setConcernPriorityId(concernPriorityId);
      }      
   }

   /**
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRecipientPartnerId() {
      if (recipientPartnerIdConcernPartnerModel != null) {
         return recipientPartnerIdConcernPartnerModel.getConcernPartnerId();
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
   public void setRecipientPartnerId(Long concernPartnerId) {
      if(concernPartnerId == null)
      {      
      	recipientPartnerIdConcernPartnerModel = null;
      }
      else
      {
        recipientPartnerIdConcernPartnerModel = new ConcernPartnerModel();
      	recipientPartnerIdConcernPartnerModel.setConcernPartnerId(concernPartnerId);
      }      
   }

   /**
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getInitiatorPartnerId() {
      if (initiatorPartnerIdConcernPartnerModel != null) {
         return initiatorPartnerIdConcernPartnerModel.getConcernPartnerId();
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
   public void setInitiatorPartnerId(Long concernPartnerId) {
      if(concernPartnerId == null)
      {      
      	initiatorPartnerIdConcernPartnerModel = null;
      }
      else
      {
        initiatorPartnerIdConcernPartnerModel = new ConcernPartnerModel();
      	initiatorPartnerIdConcernPartnerModel.setConcernPartnerId(concernPartnerId);
      }      
   }

   /**
    * Returns the value of the <code>concernCategoryId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getConcernCategoryId() {
      if (concernCategoryIdConcernCategoryModel != null) {
         return concernCategoryIdConcernCategoryModel.getConcernCategoryId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernCategoryId</code> property.
    *
    * @param concernCategoryId the value for the <code>concernCategoryId</code> property
									    * @spring.validator type="required"
																											    */
   
   @javax.persistence.Transient
   public void setConcernCategoryId(Long concernCategoryId) {
      if(concernCategoryId == null)
      {      
      	concernCategoryIdConcernCategoryModel = null;
      }
      else
      {
        concernCategoryIdConcernCategoryModel = new ConcernCategoryModel();
      	concernCategoryIdConcernCategoryModel.setConcernCategoryId(concernCategoryId);
      }      
   }

   /**
    * Returns the value of the <code>concernId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getParentConcernId() {
      if (parentConcernIdConcernModel != null) {
         return parentConcernIdConcernModel.getConcernId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernId</code> property.
    *
    * @param concernId the value for the <code>concernId</code> property
																																			    */
   
   @javax.persistence.Transient
   public void setParentConcernId(Long concernId) {
      if(concernId == null)
      {      
      	parentConcernIdConcernModel = null;
      }
      else
      {
        parentConcernIdConcernModel = new ConcernModel();
      	parentConcernIdConcernModel.setConcernId(concernId);
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
        checkBox += "_"+ getConcernId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernId=" + getConcernId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernId";
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
    	
    	associationModel.setClassName("ConcernStatusModel");
    	associationModel.setPropertyName("relationConcernStatusIdConcernStatusModel");   		
   		associationModel.setValue(getRelationConcernStatusIdConcernStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernPriorityModel");
    	associationModel.setPropertyName("relationConcernPriorityIdConcernPriorityModel");   		
   		associationModel.setValue(getRelationConcernPriorityIdConcernPriorityModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernPartnerModel");
    	associationModel.setPropertyName("relationRecipientPartnerIdConcernPartnerModel");   		
   		associationModel.setValue(getRelationRecipientPartnerIdConcernPartnerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernPartnerModel");
    	associationModel.setPropertyName("relationInitiatorPartnerIdConcernPartnerModel");   		
   		associationModel.setValue(getRelationInitiatorPartnerIdConcernPartnerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernCategoryModel");
    	associationModel.setPropertyName("relationConcernCategoryIdConcernCategoryModel");   		
   		associationModel.setValue(getRelationConcernCategoryIdConcernCategoryModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernModel");
    	associationModel.setPropertyName("relationParentConcernIdConcernModel");   		
   		associationModel.setValue(getRelationParentConcernIdConcernModel());
   		
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
