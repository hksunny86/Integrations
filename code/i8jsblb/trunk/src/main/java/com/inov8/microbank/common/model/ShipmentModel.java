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
 * The ShipmentModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ShipmentModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SHIPMENT_seq",sequenceName = "SHIPMENT_seq", allocationSize=1)
@Table(name = "SHIPMENT")
public class ShipmentModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -5502198141793557760L;
private SupplierModel supplierIdSupplierModel;
   private ShipmentTypeModel shipmentTypeIdShipmentTypeModel;
   private ShipmentModel shipmentReferenceIdShipmentModel;
   private ProductModel productIdProductModel;
   private PaymentModeModel paymentModeIdPaymentModeModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<ProductUnitModel> shipmentIdProductUnitModelList = new ArrayList<ProductUnitModel>();
   private Collection<ShipmentModel> shipmentReferenceIdShipmentModelList = new ArrayList<ShipmentModel>();

   private Long shipmentId;
   private Double price;
   private Double creditAmount;
   private Date purchaseDate;
   private Date expiryDate;
   private Date shipmentDate;
   private Long quantity;
   private Double outstandingCredit;
   private Date quantityUpdatedOn;
   private String description;
   private String comments;
   private Boolean active;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public ShipmentModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getShipmentId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setShipmentId(primaryKey);
    }

   /**
    * Returns the value of the <code>shipmentId</code> property.
    *
    */
      @Column(name = "SHIPMENT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SHIPMENT_seq")
   public Long getShipmentId() {
      return shipmentId;
   }

   /**
    * Sets the value of the <code>shipmentId</code> property.
    *
    * @param shipmentId the value for the <code>shipmentId</code> property
    *    
		    */

   public void setShipmentId(Long shipmentId) {
      this.shipmentId = shipmentId;
   }

   /**
    * Returns the value of the <code>price</code> property.
    *
    */
      @Column(name = "PRICE" , nullable = false )
   public Double getPrice() {
      return price;
   }

   /**
    * Sets the value of the <code>price</code> property.
    *
    * @param price the value for the <code>price</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setPrice(Double price) {
      this.price = price;
   }

   /**
    * Returns the value of the <code>creditAmount</code> property.
    *
    */
      @Column(name = "CREDIT_AMOUNT"  )
   public Double getCreditAmount() {
      return creditAmount;
   }

   /**
    * Sets the value of the <code>creditAmount</code> property.
    *
    * @param creditAmount the value for the <code>creditAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCreditAmount(Double creditAmount) {
      this.creditAmount = creditAmount;
   }

   /**
    * Returns the value of the <code>purchaseDate</code> property.
    *
    */
      @Column(name = "PURCHASE_DATE" , nullable = false )
   public Date getPurchaseDate() {
      return purchaseDate;
   }

   /**
    * Sets the value of the <code>purchaseDate</code> property.
    *
    * @param purchaseDate the value for the <code>purchaseDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setPurchaseDate(Date purchaseDate) {
      this.purchaseDate = purchaseDate;
   }

   /**
    * Returns the value of the <code>expiryDate</code> property.
    *
    */
      @Column(name = "EXPIRY_DATE"  )
   public Date getExpiryDate() {
      return expiryDate;
   }

   /**
    * Sets the value of the <code>expiryDate</code> property.
    *
    * @param expiryDate the value for the <code>expiryDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
   }

   /**
    * Returns the value of the <code>shipmentDate</code> property.
    *
    */
      @Column(name = "SHIPMENT_DATE"  )
   public Date getShipmentDate() {
      return shipmentDate;
   }

   /**
    * Sets the value of the <code>shipmentDate</code> property.
    *
    * @param shipmentDate the value for the <code>shipmentDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setShipmentDate(Date shipmentDate) {
      this.shipmentDate = shipmentDate;
   }

   /**
    * Returns the value of the <code>quantity</code> property.
    *
    */
      @Column(name = "QUANTITY"  )
   public Long getQuantity() {
      return quantity;
   }

   /**
    * Sets the value of the <code>quantity</code> property.
    *
    * @param quantity the value for the <code>quantity</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setQuantity(Long quantity) {
      this.quantity = quantity;
   }

   /**
    * Returns the value of the <code>outstandingCredit</code> property.
    *
    */
      @Column(name = "OUTSTANDING_CREDIT"  )
   public Double getOutstandingCredit() {
      return outstandingCredit;
   }

   /**
    * Sets the value of the <code>outstandingCredit</code> property.
    *
    * @param outstandingCredit the value for the <code>outstandingCredit</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setOutstandingCredit(Double outstandingCredit) {
      this.outstandingCredit = outstandingCredit;
   }

   /**
    * Returns the value of the <code>quantityUpdatedOn</code> property.
    *
    */
      @Column(name = "QUANTITY_UPDATED_ON"  )
   public Date getQuantityUpdatedOn() {
      return quantityUpdatedOn;
   }

   /**
    * Sets the value of the <code>quantityUpdatedOn</code> property.
    *
    * @param quantityUpdatedOn the value for the <code>quantityUpdatedOn</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setQuantityUpdatedOn(Date quantityUpdatedOn) {
      this.quantityUpdatedOn = quantityUpdatedOn;
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
    * Returns the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @return the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUPPLIER_ID")    
   public SupplierModel getRelationSupplierIdSupplierModel(){
      return supplierIdSupplierModel;
   }
    
   /**
    * Returns the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @return the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SupplierModel getSupplierIdSupplierModel(){
      return getRelationSupplierIdSupplierModel();
   }

   /**
    * Sets the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @param supplierModel a value for <code>supplierIdSupplierModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSupplierIdSupplierModel(SupplierModel supplierModel) {
      this.supplierIdSupplierModel = supplierModel;
   }
   
   /**
    * Sets the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @param supplierModel a value for <code>supplierIdSupplierModel</code>.
    */
   @javax.persistence.Transient
   public void setSupplierIdSupplierModel(SupplierModel supplierModel) {
      if(null != supplierModel)
      {
      	setRelationSupplierIdSupplierModel((SupplierModel)supplierModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    * @return the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SHIPMENT_TYPE_ID")    
   public ShipmentTypeModel getRelationShipmentTypeIdShipmentTypeModel(){
      return shipmentTypeIdShipmentTypeModel;
   }
    
   /**
    * Returns the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    * @return the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ShipmentTypeModel getShipmentTypeIdShipmentTypeModel(){
      return getRelationShipmentTypeIdShipmentTypeModel();
   }

   /**
    * Sets the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    * @param shipmentTypeModel a value for <code>shipmentTypeIdShipmentTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationShipmentTypeIdShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      this.shipmentTypeIdShipmentTypeModel = shipmentTypeModel;
   }
   
   /**
    * Sets the value of the <code>shipmentTypeIdShipmentTypeModel</code> relation property.
    *
    * @param shipmentTypeModel a value for <code>shipmentTypeIdShipmentTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setShipmentTypeIdShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      if(null != shipmentTypeModel)
      {
      	setRelationShipmentTypeIdShipmentTypeModel((ShipmentTypeModel)shipmentTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    * @return the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SHIPMENT_REFERENCE_ID")    
   public ShipmentModel getRelationShipmentReferenceIdShipmentModel(){
      return shipmentReferenceIdShipmentModel;
   }
    
   /**
    * Returns the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    * @return the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ShipmentModel getShipmentReferenceIdShipmentModel(){
      return getRelationShipmentReferenceIdShipmentModel();
   }

   /**
    * Sets the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    * @param shipmentModel a value for <code>shipmentReferenceIdShipmentModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationShipmentReferenceIdShipmentModel(ShipmentModel shipmentModel) {
      this.shipmentReferenceIdShipmentModel = shipmentModel;
   }
   
   /**
    * Sets the value of the <code>shipmentReferenceIdShipmentModel</code> relation property.
    *
    * @param shipmentModel a value for <code>shipmentReferenceIdShipmentModel</code>.
    */
   @javax.persistence.Transient
   public void setShipmentReferenceIdShipmentModel(ShipmentModel shipmentModel) {
      if(null != shipmentModel)
      {
      	setRelationShipmentReferenceIdShipmentModel((ShipmentModel)shipmentModel.clone());
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
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PAYMENT_MODE_ID")    
   public PaymentModeModel getRelationPaymentModeIdPaymentModeModel(){
      return paymentModeIdPaymentModeModel;
   }
    
   /**
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PaymentModeModel getPaymentModeIdPaymentModeModel(){
      return getRelationPaymentModeIdPaymentModeModel();
   }

   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      this.paymentModeIdPaymentModeModel = paymentModeModel;
   }
   
   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      if(null != paymentModeModel)
      {
      	setRelationPaymentModeIdPaymentModeModel((PaymentModeModel)paymentModeModel.clone());
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
    * Add the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be added.
    */
    
   public void addShipmentIdProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationShipmentIdShipmentModel(this);
      shipmentIdProductUnitModelList.add(productUnitModel);
   }
   
   /**
    * Remove the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be removed.
    */
   
   public void removeShipmentIdProductUnitModel(ProductUnitModel productUnitModel) {      
      productUnitModel.setRelationShipmentIdShipmentModel(null);
      shipmentIdProductUnitModelList.remove(productUnitModel);      
   }

   /**
    * Get a list of related ProductUnitModel objects of the ShipmentModel object.
    * These objects are in a bidirectional one-to-many relation by the ShipmentId member.
    *
    * @return Collection of ProductUnitModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationShipmentIdShipmentModel")
   @JoinColumn(name = "SHIPMENT_ID")
   public Collection<ProductUnitModel> getShipmentIdProductUnitModelList() throws Exception {
   		return shipmentIdProductUnitModelList;
   }


   /**
    * Set a list of ProductUnitModel related objects to the ShipmentModel object.
    * These objects are in a bidirectional one-to-many relation by the ShipmentId member.
    *
    * @param productUnitModelList the list of related objects.
    */
    public void setShipmentIdProductUnitModelList(Collection<ProductUnitModel> productUnitModelList) throws Exception {
		this.shipmentIdProductUnitModelList = productUnitModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */
    
   public void addShipmentReferenceIdShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationShipmentReferenceIdShipmentModel(this);
      shipmentReferenceIdShipmentModelList.add(shipmentModel);
   }
   
   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */
   
   public void removeShipmentReferenceIdShipmentModel(ShipmentModel shipmentModel) {      
      shipmentModel.setRelationShipmentReferenceIdShipmentModel(null);
      shipmentReferenceIdShipmentModelList.remove(shipmentModel);      
   }

   /**
    * Get a list of related ShipmentModel objects of the ShipmentModel object.
    * These objects are in a bidirectional one-to-many relation by the ShipmentReferenceId member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationShipmentReferenceIdShipmentModel")
   @JoinColumn(name = "SHIPMENT_REFERENCE_ID")
   public Collection<ShipmentModel> getShipmentReferenceIdShipmentModelList() throws Exception {
   		return shipmentReferenceIdShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the ShipmentModel object.
    * These objects are in a bidirectional one-to-many relation by the ShipmentReferenceId member.
    *
    * @param shipmentModelList the list of related objects.
    */
    public void setShipmentReferenceIdShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
		this.shipmentReferenceIdShipmentModelList = shipmentModelList;
   }



   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSupplierId() {
      if (supplierIdSupplierModel != null) {
         return supplierIdSupplierModel.getSupplierId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
													    * @spring.validator type="required"
																																			    */
   
   @javax.persistence.Transient
   public void setSupplierId(Long supplierId) {
      if(supplierId == null)
      {      
      	supplierIdSupplierModel = null;
      }
      else
      {
        supplierIdSupplierModel = new SupplierModel();
      	supplierIdSupplierModel.setSupplierId(supplierId);
      }      
   }

   /**
    * Returns the value of the <code>shipmentTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getShipmentTypeId() {
      if (shipmentTypeIdShipmentTypeModel != null) {
         return shipmentTypeIdShipmentTypeModel.getShipmentTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>shipmentTypeId</code> property.
    *
    * @param shipmentTypeId the value for the <code>shipmentTypeId</code> property
							    * @spring.validator type="required"
																																									    */
   
   @javax.persistence.Transient
   public void setShipmentTypeId(Long shipmentTypeId) {
      if(shipmentTypeId == null)
      {      
      	shipmentTypeIdShipmentTypeModel = null;
      }
      else
      {
        shipmentTypeIdShipmentTypeModel = new ShipmentTypeModel();
      	shipmentTypeIdShipmentTypeModel.setShipmentTypeId(shipmentTypeId);
      }      
   }

   /**
    * Returns the value of the <code>shipmentId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getShipmentReferenceId() {
      if (shipmentReferenceIdShipmentModel != null) {
         return shipmentReferenceIdShipmentModel.getShipmentId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>shipmentId</code> property.
    *
    * @param shipmentId the value for the <code>shipmentId</code> property
																																															    */
   
   @javax.persistence.Transient
   public void setShipmentReferenceId(Long shipmentId) {
      if(shipmentId == null)
      {      
      	shipmentReferenceIdShipmentModel = null;
      }
      else
      {
        shipmentReferenceIdShipmentModel = new ShipmentModel();
      	shipmentReferenceIdShipmentModel.setShipmentId(shipmentId);
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
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPaymentModeId() {
      if (paymentModeIdPaymentModeModel != null) {
         return paymentModeIdPaymentModeModel.getPaymentModeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
					    * @spring.validator type="required"
																																											    */
   
   @javax.persistence.Transient
   public void setPaymentModeId(Long paymentModeId) {
      if(paymentModeId == null)
      {      
      	paymentModeIdPaymentModeModel = null;
      }
      else
      {
        paymentModeIdPaymentModeModel = new PaymentModeModel();
      	paymentModeIdPaymentModeModel.setPaymentModeId(paymentModeId);
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
        checkBox += "_"+ getShipmentId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&shipmentId=" + getShipmentId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "shipmentId";
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
    	
    	associationModel.setClassName("SupplierModel");
    	associationModel.setPropertyName("relationSupplierIdSupplierModel");   		
   		associationModel.setValue(getRelationSupplierIdSupplierModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ShipmentTypeModel");
    	associationModel.setPropertyName("relationShipmentTypeIdShipmentTypeModel");   		
   		associationModel.setValue(getRelationShipmentTypeIdShipmentTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ShipmentModel");
    	associationModel.setPropertyName("relationShipmentReferenceIdShipmentModel");   		
   		associationModel.setValue(getRelationShipmentReferenceIdShipmentModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
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
