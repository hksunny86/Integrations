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
 * The ProductUnitModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductUnitModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_UNIT_seq",sequenceName = "PRODUCT_UNIT_seq", allocationSize=1)
@Table(name = "PRODUCT_UNIT")
public class ProductUnitModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -3429402466658713828L;
private ShipmentModel shipmentIdShipmentModel;
   private ProductModel productIdProductModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<CommissionTransactionModel> productUnitIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<TransactionDetailModel> productUnitIdTransactionDetailModelList = new ArrayList<TransactionDetailModel>();

   private Long productUnitId;
   private String serialNo;
   private String pin;
   private Boolean sold;
   private Boolean active;
   private String userName;
   private String additionalField1;
   private String additionalField2;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public ProductUnitModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductUnitId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductUnitId(primaryKey);
    }

   /**
    * Returns the value of the <code>productUnitId</code> property.
    *
    */
      @Column(name = "PRODUCT_UNIT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_UNIT_seq")
   public Long getProductUnitId() {
      return productUnitId;
   }

   /**
    * Sets the value of the <code>productUnitId</code> property.
    *
    * @param productUnitId the value for the <code>productUnitId</code> property
    *    
		    */

   public void setProductUnitId(Long productUnitId) {
      this.productUnitId = productUnitId;
   }

   /**
    * Returns the value of the <code>serialNo</code> property.
    *
    */
      @Column(name = "SERIAL_NO" , nullable = false , length=50 )
   public String getSerialNo() {
      return serialNo;
   }

   /**
    * Sets the value of the <code>serialNo</code> property.
    *
    * @param serialNo the value for the <code>serialNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSerialNo(String serialNo) {
      this.serialNo = serialNo;
   }

   /**
    * Returns the value of the <code>pin</code> property.
    *
    */
      @Column(name = "PIN" , nullable = false , length=50 )
   public String getPin() {
      return pin;
   }

   /**
    * Sets the value of the <code>pin</code> property.
    *
    * @param pin the value for the <code>pin</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPin(String pin) {
      this.pin = pin;
   }

   /**
    * Returns the value of the <code>sold</code> property.
    *
    */
      @Column(name = "IS_SOLD" , nullable = false )
   public Boolean getSold() {
      return sold;
   }

   /**
    * Sets the value of the <code>sold</code> property.
    *
    * @param sold the value for the <code>sold</code> property
    *    
		    */

   public void setSold(Boolean sold) {
      this.sold = sold;
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
    * Returns the value of the <code>userName</code> property.
    *
    */
      @Column(name = "USER_NAME"  , length=50 )
   public String getUserName() {
      return userName;
   }

   /**
    * Sets the value of the <code>userName</code> property.
    *
    * @param userName the value for the <code>userName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUserName(String userName) {
      this.userName = userName;
   }

   /**
    * Returns the value of the <code>additionalField1</code> property.
    *
    */
      @Column(name = "ADDITIONAL_FIELD1"  , length=50 )
   public String getAdditionalField1() {
      return additionalField1;
   }

   /**
    * Sets the value of the <code>additionalField1</code> property.
    *
    * @param additionalField1 the value for the <code>additionalField1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAdditionalField1(String additionalField1) {
      this.additionalField1 = additionalField1;
   }

   /**
    * Returns the value of the <code>additionalField2</code> property.
    *
    */
      @Column(name = "ADDITIONAL_FIELD2"  , length=50 )
   public String getAdditionalField2() {
      return additionalField2;
   }

   /**
    * Sets the value of the <code>additionalField2</code> property.
    *
    * @param additionalField2 the value for the <code>additionalField2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAdditionalField2(String additionalField2) {
      this.additionalField2 = additionalField2;
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
    * Returns the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    * @return the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SHIPMENT_ID")    
   public ShipmentModel getRelationShipmentIdShipmentModel(){
      return shipmentIdShipmentModel;
   }
    
   /**
    * Returns the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    * @return the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ShipmentModel getShipmentIdShipmentModel(){
      return getRelationShipmentIdShipmentModel();
   }

   /**
    * Sets the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    * @param shipmentModel a value for <code>shipmentIdShipmentModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationShipmentIdShipmentModel(ShipmentModel shipmentModel) {
      this.shipmentIdShipmentModel = shipmentModel;
   }
   
   /**
    * Sets the value of the <code>shipmentIdShipmentModel</code> relation property.
    *
    * @param shipmentModel a value for <code>shipmentIdShipmentModel</code>.
    */
   @javax.persistence.Transient
   public void setShipmentIdShipmentModel(ShipmentModel shipmentModel) {
      if(null != shipmentModel)
      {
      	setRelationShipmentIdShipmentModel((ShipmentModel)shipmentModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_ID")    
   public ProductModel getRelationProductIdProductModel(){
      return productIdProductModel;
   }
    
   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductModel getProductIdProductModel(){
      return getRelationProductIdProductModel();
   }

   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductIdProductModel(ProductModel productModel) {
      this.productIdProductModel = productModel;
   }
   
   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setProductIdProductModel(ProductModel productModel) {
      if(null != productModel)
      {
      	setRelationProductIdProductModel((ProductModel)productModel.clone());
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
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */
    
   public void addProductUnitIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationProductUnitIdProductUnitModel(this);
      productUnitIdCommissionTransactionModelList.add(commissionTransactionModel);
   }
   
   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */
   
   public void removeProductUnitIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {      
      commissionTransactionModel.setRelationProductUnitIdProductUnitModel(null);
      productUnitIdCommissionTransactionModelList.remove(commissionTransactionModel);      
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the ProductUnitModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductUnitId member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductUnitIdProductUnitModel")
   @JoinColumn(name = "PRODUCT_UNIT_ID")
   public Collection<CommissionTransactionModel> getProductUnitIdCommissionTransactionModelList() throws Exception {
   		return productUnitIdCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the ProductUnitModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductUnitId member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
    public void setProductUnitIdCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
		this.productUnitIdCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be added.
    */
    
   public void addProductUnitIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      transactionDetailModel.setRelationProductUnitIdProductUnitModel(this);
      productUnitIdTransactionDetailModelList.add(transactionDetailModel);
   }
   
   /**
    * Remove the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be removed.
    */
   
   public void removeProductUnitIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {      
      transactionDetailModel.setRelationProductUnitIdProductUnitModel(null);
      productUnitIdTransactionDetailModelList.remove(transactionDetailModel);      
   }

   /**
    * Get a list of related TransactionDetailModel objects of the ProductUnitModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductUnitId member.
    *
    * @return Collection of TransactionDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductUnitIdProductUnitModel")
   @JoinColumn(name = "PRODUCT_UNIT_ID")
   public Collection<TransactionDetailModel> getProductUnitIdTransactionDetailModelList() throws Exception {
   		return productUnitIdTransactionDetailModelList;
   }


   /**
    * Set a list of TransactionDetailModel related objects to the ProductUnitModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductUnitId member.
    *
    * @param transactionDetailModelList the list of related objects.
    */
    public void setProductUnitIdTransactionDetailModelList(Collection<TransactionDetailModel> transactionDetailModelList) throws Exception {
		this.productUnitIdTransactionDetailModelList = transactionDetailModelList;
   }



   /**
    * Returns the value of the <code>shipmentId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getShipmentId() {
      if (shipmentIdShipmentModel != null) {
         return shipmentIdShipmentModel.getShipmentId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>shipmentId</code> property.
    *
    * @param shipmentId the value for the <code>shipmentId</code> property
							    * @spring.validator type="required"
																											    */
   
   @javax.persistence.Transient
   public void setShipmentId(Long shipmentId) {
      if(shipmentId == null)
      {      
      	shipmentIdShipmentModel = null;
      }
      else
      {
        shipmentIdShipmentModel = new ShipmentModel();
      	shipmentIdShipmentModel.setShipmentId(shipmentId);
      }      
   }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductId() {
      if (productIdProductModel != null) {
         return productIdProductModel.getProductId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
					    * @spring.validator type="required"
																													    */
   
   @javax.persistence.Transient
   public void setProductId(Long productId) {
      if(productId == null)
      {      
      	productIdProductModel = null;
      }
      else
      {
        productIdProductModel = new ProductModel();
      	productIdProductModel.setProductId(productId);
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
        checkBox += "_"+ getProductUnitId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productUnitId=" + getProductUnitId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productUnitId";
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
    	
    	associationModel.setClassName("ShipmentModel");
    	associationModel.setPropertyName("relationShipmentIdShipmentModel");   		
   		associationModel.setValue(getRelationShipmentIdShipmentModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
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
