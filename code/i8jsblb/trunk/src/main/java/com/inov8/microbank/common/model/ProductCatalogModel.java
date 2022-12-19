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
 * The ProductCatalogModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductCatalogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_CATALOG_seq",sequenceName = "PRODUCT_CATALOG_seq", allocationSize=1)
@Table(name = "PRODUCT_CATALOG")
public class ProductCatalogModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 3869094774414413893L;
private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<ProductCatalogDetailModel> productCatalogIdProductCatalogDetailModelList = new ArrayList<ProductCatalogDetailModel>();
   private Collection<RetailerModel> productCatalogueIdRetailerModelList = new ArrayList<RetailerModel>();

   private Long productCatalogId;
   private AppUserTypeModel appUserTypeModel;
   private Long appUserTypeId;
   private String name;
   private String description;
   private Boolean active;
   private String comments;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
  
   /**
    * Default constructor.
    */
   public ProductCatalogModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductCatalogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductCatalogId(primaryKey);
    }

   /**
    * Returns the value of the <code>productCatalogId</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_CATALOG_seq")
   public Long getProductCatalogId() {
      return productCatalogId;
   }

   /**
    * Sets the value of the <code>productCatalogId</code> property.
    *
    * @param productCatalogId the value for the <code>productCatalogId</code> property
    *    
		    */

   public void setProductCatalogId(Long productCatalogId) {
      this.productCatalogId = productCatalogId;
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
    * Add the related ProductCatalogDetailModel to this one-to-many relation.
    *
    * @param productCatalogDetailModel object to be added.
    */
    
   public void addProductCatalogIdProductCatalogDetailModel(ProductCatalogDetailModel productCatalogDetailModel) {
      productCatalogDetailModel.setRelationProductCatalogIdProductCatalogModel(this);
      productCatalogIdProductCatalogDetailModelList.add(productCatalogDetailModel);
   }
   
   /**
    * Remove the related ProductCatalogDetailModel to this one-to-many relation.
    *
    * @param productCatalogDetailModel object to be removed.
    */
   
   public void removeProductCatalogIdProductCatalogDetailModel(ProductCatalogDetailModel productCatalogDetailModel) {      
      productCatalogDetailModel.setRelationProductCatalogIdProductCatalogModel(null);
      productCatalogIdProductCatalogDetailModelList.remove(productCatalogDetailModel);      
   }

   /**
    * Get a list of related ProductCatalogDetailModel objects of the ProductCatalogModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductCatalogId member.
    *
    * @return Collection of ProductCatalogDetailModel objects.
    *
    */
   
//   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductCatalogIdProductCatalogModel")
//   @JoinColumn(name = "PRODUCT_CATALOG_ID")
   @OneToMany(cascade =
   {
   CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
   fetch = FetchType.LAZY,
   mappedBy = "relationProductCatalogIdProductCatalogModel")
	@JoinColumn(name = "PRODUCT_CATALOG_ID")
	@org.hibernate.annotations.Cascade(
	{org.hibernate.annotations.CascadeType.
	SAVE_UPDATE,
	org.hibernate.annotations.CascadeType.
	PERSIST,
	org.hibernate.annotations.CascadeType.
	MERGE,
	org.hibernate.annotations.CascadeType.
	REFRESH})
   
   public Collection<ProductCatalogDetailModel> getProductCatalogIdProductCatalogDetailModelList() throws Exception {
   		return productCatalogIdProductCatalogDetailModelList;
   }


   /**
    * Set a list of ProductCatalogDetailModel related objects to the ProductCatalogModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductCatalogId member.
    *
    * @param productCatalogDetailModelList the list of related objects.
    */
    public void setProductCatalogIdProductCatalogDetailModelList(Collection<ProductCatalogDetailModel> productCatalogDetailModelList) throws Exception {
		this.productCatalogIdProductCatalogDetailModelList = productCatalogDetailModelList;
   }


   /**
    * Add the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be added.
    */
    
   public void addProductCatalogueIdRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationProductCatalogueIdProductCatalogModel(this);
      productCatalogueIdRetailerModelList.add(retailerModel);
   }
   
   /**
    * Remove the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be removed.
    */
   
   public void removeProductCatalogueIdRetailerModel(RetailerModel retailerModel) {      
      retailerModel.setRelationProductCatalogueIdProductCatalogModel(null);
      productCatalogueIdRetailerModelList.remove(retailerModel);      
   }

   /**
    * Get a list of related RetailerModel objects of the ProductCatalogModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductCatalogueId member.
    *
    * @return Collection of RetailerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductCatalogueIdProductCatalogModel")
   @JoinColumn(name = "PRODUCT_CATALOGUE_ID")
   public Collection<RetailerModel> getProductCatalogueIdRetailerModelList() throws Exception {
   		return productCatalogueIdRetailerModelList;
   }


   /**
    * Set a list of RetailerModel related objects to the ProductCatalogModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductCatalogueId member.
    *
    * @param retailerModelList the list of related objects.
    */
    public void setProductCatalogueIdRetailerModelList(Collection<RetailerModel> retailerModelList) throws Exception {
		this.productCatalogueIdRetailerModelList = retailerModelList;
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
        checkBox += "_"+ getProductCatalogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productCatalogId=" + getProductCatalogId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productCatalogId";
			return primaryKeyFieldName;				
    }

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public AppUserTypeModel getAppUserTypeModel() {return appUserTypeModel;}

   public void setAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      this.appUserTypeModel = appUserTypeModel;
      if(this.appUserTypeModel!=null)
         this.appUserTypeId=appUserTypeModel.getAppUserTypeId();
   }

   @javax.persistence.Transient
   public Long getAppUserTypeId() {
      if(appUserTypeModel!=null) {
         return appUserTypeModel.getAppUserTypeId();
      }
      else{
         return null;
      }
   }
   @javax.persistence.Transient
   public void setAppUserTypeId(Long appUserTypeId) {
      if(appUserTypeId!=null) {
         appUserTypeModel=new AppUserTypeModel();
         appUserTypeModel.setAppUserTypeId(appUserTypeId);
      }else
         appUserTypeModel=null;
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
          
}
