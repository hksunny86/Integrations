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
 * The DeviceTypeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DeviceTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEVICE_TYPE_seq",sequenceName = "DEVICE_TYPE_seq", allocationSize=1)
@Table(name = "DEVICE_TYPE")
public class DeviceTypeModel extends BasePersistableModel implements Serializable {
  

   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private ProductCatalogModel agentDfltProdCatalogModel;
   private ProductCatalogModel custDfltProdCatalogModel;

   private Collection<ActionLogModel> deviceTypeIdActionLogModelList = new ArrayList<ActionLogModel>();
   private Collection<AppVersionModel> deviceTypeIdAppVersionModelList = new ArrayList<AppVersionModel>();
   private Collection<DeviceTypeCommandModel> deviceTypeIdDeviceTypeCommandModelList = new ArrayList<DeviceTypeCommandModel>();
   private Collection<GoldenNosModel> deviceTypeIdGoldenNosModelList = new ArrayList<GoldenNosModel>();
   private Collection<ProductDeviceFlowModel> deviceTypeIdProductDeviceFlowModelList = new ArrayList<ProductDeviceFlowModel>();
   private Collection<TransactionModel> deviceTypeIdTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<UserDeviceAccountsModel> deviceTypeIdUserDeviceAccountsModelList = new ArrayList<UserDeviceAccountsModel>();

   private Long deviceTypeId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Boolean active;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public DeviceTypeModel() {
   }   

   public DeviceTypeModel(Long deviceTypeId) {
		super();
		this.deviceTypeId = deviceTypeId;
	}


	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDeviceTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDeviceTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEVICE_TYPE_seq")
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
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
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
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
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
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
    * Add the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be added.
    */
    
   public void addDeviceTypeIdActionLogModel(ActionLogModel actionLogModel) {
      actionLogModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdActionLogModelList.add(actionLogModel);
   }
   
   /**
    * Remove the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be removed.
    */
   
   public void removeDeviceTypeIdActionLogModel(ActionLogModel actionLogModel) {      
      actionLogModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdActionLogModelList.remove(actionLogModel);      
   }

   /**
    * Get a list of related ActionLogModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of ActionLogModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<ActionLogModel> getDeviceTypeIdActionLogModelList() throws Exception {
   		return deviceTypeIdActionLogModelList;
   }


   /**
    * Set a list of ActionLogModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param actionLogModelList the list of related objects.
    */
    public void setDeviceTypeIdActionLogModelList(Collection<ActionLogModel> actionLogModelList) throws Exception {
		this.deviceTypeIdActionLogModelList = actionLogModelList;
   }


   /**
    * Add the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be added.
    */
    
   public void addDeviceTypeIdAppVersionModel(AppVersionModel appVersionModel) {
      appVersionModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdAppVersionModelList.add(appVersionModel);
   }
   
   /**
    * Remove the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be removed.
    */
   
   public void removeDeviceTypeIdAppVersionModel(AppVersionModel appVersionModel) {      
      appVersionModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdAppVersionModelList.remove(appVersionModel);      
   }

   /**
    * Get a list of related AppVersionModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of AppVersionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<AppVersionModel> getDeviceTypeIdAppVersionModelList() throws Exception {
   		return deviceTypeIdAppVersionModelList;
   }


   /**
    * Set a list of AppVersionModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param appVersionModelList the list of related objects.
    */
    public void setDeviceTypeIdAppVersionModelList(Collection<AppVersionModel> appVersionModelList) throws Exception {
		this.deviceTypeIdAppVersionModelList = appVersionModelList;
   }


   /**
    * Add the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be added.
    */
    
   public void addDeviceTypeIdDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdDeviceTypeCommandModelList.add(deviceTypeCommandModel);
   }
   
   /**
    * Remove the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be removed.
    */
   
   public void removeDeviceTypeIdDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {      
      deviceTypeCommandModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdDeviceTypeCommandModelList.remove(deviceTypeCommandModel);      
   }

   /**
    * Get a list of related DeviceTypeCommandModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of DeviceTypeCommandModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<DeviceTypeCommandModel> getDeviceTypeIdDeviceTypeCommandModelList() throws Exception {
   		return deviceTypeIdDeviceTypeCommandModelList;
   }


   /**
    * Set a list of DeviceTypeCommandModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param deviceTypeCommandModelList the list of related objects.
    */
    public void setDeviceTypeIdDeviceTypeCommandModelList(Collection<DeviceTypeCommandModel> deviceTypeCommandModelList) throws Exception {
		this.deviceTypeIdDeviceTypeCommandModelList = deviceTypeCommandModelList;
   }


   /**
    * Add the related GoldenNosModel to this one-to-many relation.
    *
    * @param goldenNosModel object to be added.
    */
    
   public void addDeviceTypeIdGoldenNosModel(GoldenNosModel goldenNosModel) {
      goldenNosModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdGoldenNosModelList.add(goldenNosModel);
   }
   
   /**
    * Remove the related GoldenNosModel to this one-to-many relation.
    *
    * @param goldenNosModel object to be removed.
    */
   
   public void removeDeviceTypeIdGoldenNosModel(GoldenNosModel goldenNosModel) {      
      goldenNosModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdGoldenNosModelList.remove(goldenNosModel);      
   }

   /**
    * Get a list of related GoldenNosModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of GoldenNosModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<GoldenNosModel> getDeviceTypeIdGoldenNosModelList() throws Exception {
   		return deviceTypeIdGoldenNosModelList;
   }


   /**
    * Set a list of GoldenNosModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param goldenNosModelList the list of related objects.
    */
    public void setDeviceTypeIdGoldenNosModelList(Collection<GoldenNosModel> goldenNosModelList) throws Exception {
		this.deviceTypeIdGoldenNosModelList = goldenNosModelList;
   }


   /**
    * Add the related ProductDeviceFlowModel to this one-to-many relation.
    *
    * @param productDeviceFlowModel object to be added.
    */
    
   public void addDeviceTypeIdProductDeviceFlowModel(ProductDeviceFlowModel productDeviceFlowModel) {
      productDeviceFlowModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdProductDeviceFlowModelList.add(productDeviceFlowModel);
   }
   
   /**
    * Remove the related ProductDeviceFlowModel to this one-to-many relation.
    *
    * @param productDeviceFlowModel object to be removed.
    */
   
   public void removeDeviceTypeIdProductDeviceFlowModel(ProductDeviceFlowModel productDeviceFlowModel) {      
      productDeviceFlowModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdProductDeviceFlowModelList.remove(productDeviceFlowModel);      
   }

   /**
    * Get a list of related ProductDeviceFlowModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of ProductDeviceFlowModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<ProductDeviceFlowModel> getDeviceTypeIdProductDeviceFlowModelList() throws Exception {
   		return deviceTypeIdProductDeviceFlowModelList;
   }


   /**
    * Set a list of ProductDeviceFlowModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param productDeviceFlowModelList the list of related objects.
    */
    public void setDeviceTypeIdProductDeviceFlowModelList(Collection<ProductDeviceFlowModel> productDeviceFlowModelList) throws Exception {
		this.deviceTypeIdProductDeviceFlowModelList = productDeviceFlowModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addDeviceTypeIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeDeviceTypeIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<TransactionModel> getDeviceTypeIdTransactionModelList() throws Exception {
   		return deviceTypeIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setDeviceTypeIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.deviceTypeIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be added.
    */
    
   public void addDeviceTypeIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationDeviceTypeIdDeviceTypeModel(this);
      deviceTypeIdUserDeviceAccountsModelList.add(userDeviceAccountsModel);
   }
   
   /**
    * Remove the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be removed.
    */
   
   public void removeDeviceTypeIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {      
      userDeviceAccountsModel.setRelationDeviceTypeIdDeviceTypeModel(null);
      deviceTypeIdUserDeviceAccountsModelList.remove(userDeviceAccountsModel);      
   }

   /**
    * Get a list of related UserDeviceAccountsModel objects of the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @return Collection of UserDeviceAccountsModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDeviceTypeIdDeviceTypeModel")
   @JoinColumn(name = "DEVICE_TYPE_ID")
   public Collection<UserDeviceAccountsModel> getDeviceTypeIdUserDeviceAccountsModelList() throws Exception {
   		return deviceTypeIdUserDeviceAccountsModelList;
   }


   /**
    * Set a list of UserDeviceAccountsModel related objects to the DeviceTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the DeviceTypeId member.
    *
    * @param userDeviceAccountsModelList the list of related objects.
    */
    public void setDeviceTypeIdUserDeviceAccountsModelList(Collection<UserDeviceAccountsModel> userDeviceAccountsModelList) throws Exception {
		this.deviceTypeIdUserDeviceAccountsModelList = userDeviceAccountsModelList;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDeviceTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&deviceTypeId=" + getDeviceTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "deviceTypeId";
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
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGNT_DFLT_PROD_CATALOG_ID")
	public ProductCatalogModel getAgentDfltProdCatalogModel() 
	{
		return agentDfltProdCatalogModel;
	}

	public void setAgentDfltProdCatalogModel(
			ProductCatalogModel agentDfltProdCatalogModel) {
		this.agentDfltProdCatalogModel = agentDfltProdCatalogModel;
	}

	
	@javax.persistence.Transient
	public Long getAgentDfltProdCatalogId() 
	{
		if (agentDfltProdCatalogModel != null) {
	          return agentDfltProdCatalogModel.getProductCatalogId();
	       } else {
	          return null;
	       }
    }

	@javax.persistence.Transient
	public void setAgentDfltProdCatalogId(Long agentDfltProdCatalogId) {
		if(agentDfltProdCatalogId == null)
		{
			this.agentDfltProdCatalogModel = null;
		}
		else
		{
			agentDfltProdCatalogModel = new ProductCatalogModel();
			agentDfltProdCatalogModel.setProductCatalogId(agentDfltProdCatalogId);
		}
    }
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUST_DFLT_PROD_CATALOG_ID")
	public ProductCatalogModel getCustDfltProdCatalogModel() 
	{
		return custDfltProdCatalogModel;
	}

	public void setCustDfltProdCatalogModel(ProductCatalogModel custDfltProdCatalogModel) 
	{
		this.custDfltProdCatalogModel = custDfltProdCatalogModel;
	}

	@javax.persistence.Transient
	public Long getCustDfltProdCatalogId() 
	{
		if (custDfltProdCatalogModel != null) {
	          return custDfltProdCatalogModel.getProductCatalogId();
	       } else {
	          return null;
	       }
    }

	@javax.persistence.Transient
	public void setCustDfltProdCatalogId(Long custDfltProdCatalogId) {
		if(custDfltProdCatalogId == null)
		{
			this.custDfltProdCatalogModel = null;
		}
		else
		{
			custDfltProdCatalogModel = new ProductCatalogModel();
			custDfltProdCatalogModel.setProductCatalogId(custDfltProdCatalogId);
		}
    }
          
}
