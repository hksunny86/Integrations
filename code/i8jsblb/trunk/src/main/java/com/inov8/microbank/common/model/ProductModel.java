package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.productmodule.CategoryModel;
import com.inov8.microbank.tax.model.WHTConfigModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The ProductModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_seq",sequenceName = "PRODUCT_seq", allocationSize=1)
@Table(name = "PRODUCT")
public class ProductModel extends BasePersistableModel implements Serializable {
  

   private SupplierModel supplierIdSupplierModel;
   private ServiceModel serviceIdServiceModel;
   private ProductIntgVoModel productIntgVoIdProductIntgVoModel;
   private ProductIntgModuleInfoModel productIntgModuleInfoIdProductIntgModuleInfoModel;
   private NotificationMessageModel helpLineNotificationMessageModel;
   private NotificationMessageModel instructionIdNotificationMessageModel;
   private NotificationMessageModel successMessageIdNotificationMessageModel;
   private NotificationMessageModel failureMessageIdNotificationMessageModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private CategoryModel categoryIdCategoryModel;
   private WHTConfigModel wHTConfigIdWHTConfigModel;
   private AppUserTypeModel appUserTypeModel;

   private StakeholderBankInfoModel thirdPartyStakeholderBankInfoModel;
   //commissionRateDefault transient items
   private Double exclusiveFixAmount;
   private Double exclusivePercentAmount;
   private Double inclusiveFixAmount;
   private Double inclusivePercentAmount;
   private Long	  commissionRateDefaultModelId;
   private Long commissionStakeHolderId;
   

//commissionShSharesDefault transient items
   private Long stakeHolderId;
   private String stakeHolderName;
   //private Double share;
   private Boolean isWithHolding;
   private Boolean isFed;
   private Double withHoldingShare;
   private Double fedShare;
   private Boolean taxable;

   private Collection<AllpayCommissionRateModel> productIdAllpayCommissionRateModelList = new ArrayList<AllpayCommissionRateModel>();
   private Collection<AllpayCommissionTransactionModel> productIdAllpayCommissionTransactionModelList = new ArrayList<AllpayCommissionTransactionModel>();
   private Collection<CommissionRateModel> productIdCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CommissionShSharesModel> productIdCommissionShSharesModelList = new ArrayList<CommissionShSharesModel>();
   private Collection<CommissionTransactionModel> productIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<BillServiceLabelModel> productIdBillServiceLabelModelList = new ArrayList<BillServiceLabelModel>();
   private Collection<ProductCatalogDetailModel> productIdProductCatalogDetailModelList = new ArrayList<ProductCatalogDetailModel>();
   private Collection<ProductDeviceFlowModel> productIdProductDeviceFlowModelList = new ArrayList<ProductDeviceFlowModel>();
   private Collection<ProductUnitModel> productIdProductUnitModelList = new ArrayList<ProductUnitModel>();
   private Collection<ShipmentModel> productIdShipmentModelList = new ArrayList<ShipmentModel>();
   private Collection<SwitchUtilityMappingModel> productIdSwitchUtilityMappingModelList = new ArrayList<SwitchUtilityMappingModel>();
   private Collection<TransactionDetailModel> productIdTransactionDetailModelList = new ArrayList<TransactionDetailModel>();
   
   private Collection<CommissionRateDefaultModel> productIdCommissionRateDefaultModelList = new ArrayList<CommissionRateDefaultModel>(); //added by Turab
   private Collection<CommissionShSharesDefaultModel> productIdCommissionShSharesDefaultModelList = new ArrayList<CommissionShSharesDefaultModel>(); //added by Turab
   

   private Long productId;
   private String name;
   private Double costPrice;
   private Double unitPrice;
   private Long minimumStockLevel;
   private String description;
   private String comments;
   private Boolean batchMode;
   private Boolean active;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
   private Double fixedDiscount;
   private Double percentDiscount;
   private String productCode;
   private String categoryCode;
   private String billType;
   private Integer uspProductidCheck;
   private Double minLimit;
   private Double maxLimit;
   private Boolean inclChargesCheck;
   private String consumerLabel;
   private String consumerInputType;
   private Boolean amtRequired;
   private Boolean doValidate;
   private Long multiples;
   private String url;
   private Long bvsConfigId;
   private String prodDenom;
   private Boolean denomFlag;
   private String denomString;


   //added by atif hussain 
	private transient String accountNo;
	private transient String accountNick;
   
   /**
    * Default constructor.
    */
   @SuppressWarnings( "unchecked" )
   public ProductModel() {
   }   
   
   public ProductModel(Long productId) {
   
	   this.productId = productId;
   }   

   public ProductModel(Long productId, String name) {
	   this.productId = productId;
	   this.name = name;
   }   
   
   
   
   public void setThirdPartyStakeholderBankInfoModel(StakeholderBankInfoModel thirdPartyStakeholderBankInfoModel) {
	      this.thirdPartyStakeholderBankInfoModel = thirdPartyStakeholderBankInfoModel;
	   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductId(primaryKey);
    }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_seq")
   public Long getProductId() {
      return productId;
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
    *    
		    */

   public void setProductId(Long productId) {
      this.productId = productId;
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
    * Returns the value of the <code>costPrice</code> property.
    *
    */
      @Column(name = "COST_PRICE"  )
   public Double getCostPrice() {
      return costPrice;
   }

   /**
    * Sets the value of the <code>costPrice</code> property.
    *
    * @param costPrice the value for the <code>costPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCostPrice(Double costPrice) {
      this.costPrice = costPrice;
   }

   /**
    * Returns the value of the <code>unitPrice</code> property.
    *
    */
      @Column(name = "UNIT_PRICE"  )
   public Double getUnitPrice() {
      return unitPrice;
   }

   /**
    * Sets the value of the <code>unitPrice</code> property.
    *
    * @param unitPrice the value for the <code>unitPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setUnitPrice(Double unitPrice) {
      this.unitPrice = unitPrice;
   }

   /**
    * Returns the value of the <code>minimumStockLevel</code> property.
    *
    */
      @Column(name = "MINIMUM_STOCK_LEVEL"  )
   public Long getMinimumStockLevel() {
      return minimumStockLevel;
   }

   /**
    * Sets the value of the <code>minimumStockLevel</code> property.
    *
    * @param minimumStockLevel the value for the <code>minimumStockLevel</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMinimumStockLevel(Long minimumStockLevel) {
      this.minimumStockLevel = minimumStockLevel;
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
    * Returns the value of the <code>batchMode</code> property.
    *
    */
      @Column(name = "IS_BATCH_MODE" , nullable = false )
   public Boolean getBatchMode() {
      return batchMode;
   }

   /**
    * Sets the value of the <code>batchMode</code> property.
    *
    * @param batchMode the value for the <code>batchMode</code> property
    *    
		    */

   public void setBatchMode(Boolean batchMode) {
      this.batchMode = batchMode;
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

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   /**
    * Returns the value of the <code>fixedDiscount</code> property.
    *
    */
      @Column(name = "FIXED_DISCOUNT" , nullable = false )
   public Double getFixedDiscount() {
      return fixedDiscount;
   }

   /**
    * Sets the value of the <code>fixedDiscount</code> property.
    *
    * @param fixedDiscount the value for the <code>fixedDiscount</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setFixedDiscount(Double fixedDiscount) {
      this.fixedDiscount = fixedDiscount;
   }

   /**
    * Returns the value of the <code>percentDiscount</code> property.
    *
    */
      @Column(name = "PERCENT_DISCOUNT" , nullable = false )
   public Double getPercentDiscount() {
      return percentDiscount;
   }

   /**
    * Sets the value of the <code>percentDiscount</code> property.
    *
    * @param percentDiscount the value for the <code>percentDiscount</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setPercentDiscount(Double percentDiscount) {
      this.percentDiscount = percentDiscount;
   }

   /**
    * Returns the value of the <code>productCode</code> property.
    *
    */
      @Column(name = "PRODUCT_CODE"  , length=50 )
   public String getProductCode() {
      return productCode;
   }

  	@Column(name = "CATEGORY_CODE")
  	public String getCategoryCode() {
  		return categoryCode;
  	}
  			
  	public void setCategoryCode(String categoryId) {
  		this.categoryCode = categoryId;
  	}

   /**
    * Sets the value of the <code>productCode</code> property.
    *
    * @param productCode the value for the <code>productCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setProductCode(String productCode) {
      this.productCode = productCode;
   }

   @Column( name = "BILL_TYPE" , length=50 )
   public String getBillType()
   {
       return billType;
   }

   public void setBillType( String productType )
   {
       this.billType = productType;
   }

   /**
    * Returns the value of the <code>uspProductidCheck</code> property.
    *
    */
      @Column(name = "USP_PRODUCTID_CHECK" , nullable = false )
   public Integer getUspProductidCheck() {
      return uspProductidCheck;
   }

   /**
    * Sets the value of the <code>uspProductidCheck</code> property.
    *
    * @param uspProductidCheck the value for the <code>uspProductidCheck</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="integer"
    */

   public void setUspProductidCheck(Integer uspProductidCheck) {
      this.uspProductidCheck = uspProductidCheck;
   }

   //***********************************************************
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "WHT_CONFIG_ID") 
	public WHTConfigModel getwHTConfigIdWHTConfigModel() {
		return wHTConfigIdWHTConfigModel;
	}
	
	@javax.persistence.Transient
	public void setwHTConfigIdWHTConfigModel(
			WHTConfigModel wHTConfigIdWHTConfigModel) {
		if(null!=wHTConfigIdWHTConfigModel)
			this.wHTConfigIdWHTConfigModel = (WHTConfigModel) wHTConfigIdWHTConfigModel.clone();
		else
			this.wHTConfigIdWHTConfigModel =null;
	}
	
	@javax.persistence.Transient
	  public Long getWhtConfigId() {
	     if (wHTConfigIdWHTConfigModel != null) {
	        return wHTConfigIdWHTConfigModel.getWhtConfigId();
	     } else {
	        return null;
	     }
	  }
																																																			    
  @javax.persistence.Transient
	  public void setWhtConfigId(Long wHTConfigId) {
	     if(wHTConfigId == null)
	     {      
	   	  wHTConfigIdWHTConfigModel = null;
	     }
	     else
	     {
	   	  wHTConfigIdWHTConfigModel = new WHTConfigModel();
	   	  wHTConfigIdWHTConfigModel.setWhtConfigId(wHTConfigId);
	     }      
	  }
	  
  //****************************************************************************
   
   
   
   
   
   
   
   
   
   
   
   
   
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
    * Returns the value of the <code>serviceIdServiceModel</code> relation property.
    *
    * @return the value of the <code>serviceIdServiceModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_ID")    
   public ServiceModel getRelationServiceIdServiceModel(){
      return serviceIdServiceModel;
   }
    
   /**
    * Returns the value of the <code>serviceIdServiceModel</code> relation property.
    *
    * @return the value of the <code>serviceIdServiceModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ServiceModel getServiceIdServiceModel(){
      return getRelationServiceIdServiceModel();
   }

   /**
    * Sets the value of the <code>serviceIdServiceModel</code> relation property.
    *
    * @param serviceModel a value for <code>serviceIdServiceModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationServiceIdServiceModel(ServiceModel serviceModel) {
      this.serviceIdServiceModel = serviceModel;
   }
   
   /**
    * Sets the value of the <code>serviceIdServiceModel</code> relation property.
    *
    * @param serviceModel a value for <code>serviceIdServiceModel</code>.
    */
   @javax.persistence.Transient
   public void setServiceIdServiceModel(ServiceModel serviceModel) {
      if(null != serviceModel)
      {
      	setRelationServiceIdServiceModel((ServiceModel)serviceModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    * @return the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_INTG_VO_ID")    
   public ProductIntgVoModel getRelationProductIntgVoIdProductIntgVoModel(){
      return productIntgVoIdProductIntgVoModel;
   }
    
   /**
    * Returns the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    * @return the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductIntgVoModel getProductIntgVoIdProductIntgVoModel(){
      return getRelationProductIntgVoIdProductIntgVoModel();
   }

   /**
    * Sets the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    * @param productIntgVoModel a value for <code>productIntgVoIdProductIntgVoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductIntgVoIdProductIntgVoModel(ProductIntgVoModel productIntgVoModel) {
      this.productIntgVoIdProductIntgVoModel = productIntgVoModel;
   }
   
   /**
    * Sets the value of the <code>productIntgVoIdProductIntgVoModel</code> relation property.
    *
    * @param productIntgVoModel a value for <code>productIntgVoIdProductIntgVoModel</code>.
    */
   @javax.persistence.Transient
   public void setProductIntgVoIdProductIntgVoModel(ProductIntgVoModel productIntgVoModel) {
      if(null != productIntgVoModel)
      {
      	setRelationProductIntgVoIdProductIntgVoModel((ProductIntgVoModel)productIntgVoModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    * @return the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_INTG_MODULE_INFO_ID")    
   public ProductIntgModuleInfoModel getRelationProductIntgModuleInfoIdProductIntgModuleInfoModel(){
      return productIntgModuleInfoIdProductIntgModuleInfoModel;
   }
    
   /**
    * Returns the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    * @return the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductIntgModuleInfoModel getProductIntgModuleInfoIdProductIntgModuleInfoModel(){
      return getRelationProductIntgModuleInfoIdProductIntgModuleInfoModel();
   }

   /**
    * Sets the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    * @param productIntgModuleInfoModel a value for <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductIntgModuleInfoIdProductIntgModuleInfoModel(ProductIntgModuleInfoModel productIntgModuleInfoModel) {
      this.productIntgModuleInfoIdProductIntgModuleInfoModel = productIntgModuleInfoModel;
   }
   
   /**
    * Sets the value of the <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code> relation property.
    *
    * @param productIntgModuleInfoModel a value for <code>productIntgModuleInfoIdProductIntgModuleInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setProductIntgModuleInfoIdProductIntgModuleInfoModel(ProductIntgModuleInfoModel productIntgModuleInfoModel) {
      if(null != productIntgModuleInfoModel)
      {
      	setRelationProductIntgModuleInfoIdProductIntgModuleInfoModel((ProductIntgModuleInfoModel)productIntgModuleInfoModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "HELP_LINE")    
   public NotificationMessageModel getRelationHelpLineNotificationMessageModel(){
      return helpLineNotificationMessageModel;
   }
    
   /**
    * Returns the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public NotificationMessageModel getHelpLineNotificationMessageModel(){
      return getRelationHelpLineNotificationMessageModel();
   }

   /**
    * Sets the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>helpLineNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationHelpLineNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      this.helpLineNotificationMessageModel = notificationMessageModel;
   }
   
   /**
    * Sets the value of the <code>helpLineNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>helpLineNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setHelpLineNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      if(null != notificationMessageModel)
      {
      	setRelationHelpLineNotificationMessageModel((NotificationMessageModel)notificationMessageModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "INSTRUCTION_ID")    
   public NotificationMessageModel getRelationInstructionIdNotificationMessageModel(){
      return instructionIdNotificationMessageModel;
   }
    
   /**
    * Returns the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public NotificationMessageModel getInstructionIdNotificationMessageModel(){
      return getRelationInstructionIdNotificationMessageModel();
   }

   /**
    * Sets the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>instructionIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationInstructionIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      this.instructionIdNotificationMessageModel = notificationMessageModel;
   }
   
   /**
    * Sets the value of the <code>instructionIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>instructionIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setInstructionIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      if(null != notificationMessageModel)
      {
      	setRelationInstructionIdNotificationMessageModel((NotificationMessageModel)notificationMessageModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUCCESS_MESSAGE_ID")    
   public NotificationMessageModel getRelationSuccessMessageIdNotificationMessageModel(){
      return successMessageIdNotificationMessageModel;
   }
    
   /**
    * Returns the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public NotificationMessageModel getSuccessMessageIdNotificationMessageModel(){
      return getRelationSuccessMessageIdNotificationMessageModel();
   }

   /**
    * Sets the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>successMessageIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSuccessMessageIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      this.successMessageIdNotificationMessageModel = notificationMessageModel;
   }
   
   /**
    * Sets the value of the <code>successMessageIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>successMessageIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setSuccessMessageIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      if(null != notificationMessageModel)
      {
      	setRelationSuccessMessageIdNotificationMessageModel((NotificationMessageModel)notificationMessageModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FAILURE_MESSAGE_ID")    
   public NotificationMessageModel getRelationFailureMessageIdNotificationMessageModel(){
      return failureMessageIdNotificationMessageModel;
   }
    
   /**
    * Returns the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    * @return the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public NotificationMessageModel getFailureMessageIdNotificationMessageModel(){
      return getRelationFailureMessageIdNotificationMessageModel();
   }

   /**
    * Sets the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>failureMessageIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFailureMessageIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      this.failureMessageIdNotificationMessageModel = notificationMessageModel;
   }
   
   /**
    * Sets the value of the <code>failureMessageIdNotificationMessageModel</code> relation property.
    *
    * @param notificationMessageModel a value for <code>failureMessageIdNotificationMessageModel</code>.
    */
   @javax.persistence.Transient
   public void setFailureMessageIdNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      if(null != notificationMessageModel)
      {
      	setRelationFailureMessageIdNotificationMessageModel((NotificationMessageModel)notificationMessageModel.clone());
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
    * Add the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be added.
    */
    
   public void addProductIdAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationProductIdProductModel(this);
      productIdAllpayCommissionRateModelList.add(allpayCommissionRateModel);
   }
   
   /**
    * Remove the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be removed.
    */
   
   public void removeProductIdAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {      
      allpayCommissionRateModel.setRelationProductIdProductModel(null);
      productIdAllpayCommissionRateModelList.remove(allpayCommissionRateModel);      
   }

   /**
    * Get a list of related AllpayCommissionRateModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of AllpayCommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<AllpayCommissionRateModel> getProductIdAllpayCommissionRateModelList() throws Exception {
   		return productIdAllpayCommissionRateModelList;
   }


   /**
    * Set a list of AllpayCommissionRateModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param allpayCommissionRateModelList the list of related objects.
    */
    public void setProductIdAllpayCommissionRateModelList(Collection<AllpayCommissionRateModel> allpayCommissionRateModelList) throws Exception {
		this.productIdAllpayCommissionRateModelList = allpayCommissionRateModelList;
   }


   /**
    * Add the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be added.
    */
    
   public void addProductIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationProductIdProductModel(this);
      productIdAllpayCommissionTransactionModelList.add(allpayCommissionTransactionModel);
   }
   
   /**
    * Remove the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be removed.
    */
   
   public void removeProductIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {      
      allpayCommissionTransactionModel.setRelationProductIdProductModel(null);
      productIdAllpayCommissionTransactionModelList.remove(allpayCommissionTransactionModel);      
   }

   /**
    * Get a list of related AllpayCommissionTransactionModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of AllpayCommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<AllpayCommissionTransactionModel> getProductIdAllpayCommissionTransactionModelList() throws Exception {
   		return productIdAllpayCommissionTransactionModelList;
   }


   /**
    * Set a list of AllpayCommissionTransactionModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param allpayCommissionTransactionModelList the list of related objects.
    */
    public void setProductIdAllpayCommissionTransactionModelList(Collection<AllpayCommissionTransactionModel> allpayCommissionTransactionModelList) throws Exception {
		this.productIdAllpayCommissionTransactionModelList = allpayCommissionTransactionModelList;
   }


   /**
    * Add the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be added.
    */
    
   public void addProductIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationProductIdProductModel(this);
      productIdCommissionRateModelList.add(commissionRateModel);
   }
   
   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */
   
   public void removeProductIdCommissionRateModel(CommissionRateModel commissionRateModel) {      
      commissionRateModel.setRelationProductIdProductModel(null);
      productIdCommissionRateModelList.remove(commissionRateModel);      
   }

   /**
    * Get a list of related CommissionRateModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<CommissionRateModel> getProductIdCommissionRateModelList() throws Exception {
   		return productIdCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param commissionRateModelList the list of related objects.
    */
    public void setProductIdCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
		this.productIdCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CommissionShSharesModel to this one-to-many relation.
    *
    * @param commissionShSharesModel object to be added.
    */
    
   public void addProductIdCommissionShSharesModel(CommissionShSharesModel commissionShSharesModel) {
      commissionShSharesModel.setRelationProductIdProductModel(this);
      productIdCommissionShSharesModelList.add(commissionShSharesModel);
   }
   
   /**
    * Remove the related CommissionShSharesModel to this one-to-many relation.
    *
    * @param commissionShSharesModel object to be removed.
    */
   
   public void removeProductIdCommissionShSharesModel(CommissionShSharesModel commissionShSharesModel) {      
      commissionShSharesModel.setRelationProductIdProductModel(null);
      productIdCommissionShSharesModelList.remove(commissionShSharesModel);      
   }
   
   /////////// added by Turab added commissionRateDefaultModel
   		public void addProductIdCommissionRateDefaultModel(CommissionRateDefaultModel commissionRateDefaultModel) {
   			commissionRateDefaultModel.setRelationProductIdProductModel(this);
	      productIdCommissionRateDefaultModelList.add(commissionRateDefaultModel);
	   }
	   
	   
	   public void removeProductIdCommissionRateDefaultModel(CommissionRateDefaultModel commissionRateDefaultModel) {      
		   commissionRateDefaultModel.setRelationProductIdProductModel(null);
		   productIdCommissionRateDefaultModelList.remove(commissionRateDefaultModel);      
	   }
	   
	   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
	   @JoinColumn(name = "PRODUCT_ID")
	   public Collection<CommissionRateDefaultModel> getProductIdCommissionRateDefaultModelList() throws Exception {
	   		return productIdCommissionRateDefaultModelList;
	   }


	   
	    public void setProductIdCommissionRateDefaultModelList(Collection<CommissionRateDefaultModel> commissionRateDefaultModelList) throws Exception {
			this.productIdCommissionRateDefaultModelList = commissionRateDefaultModelList;
	   }

	    //CommissionShSharesDefaultModel 
		public void addProductIdCommissionShShareDefaultModel(CommissionShSharesDefaultModel commissionShSharesDefaultModel) {
				commissionShSharesDefaultModel.setRelationProductIdProductModel(this);
				productIdCommissionShSharesDefaultModelList.add(commissionShSharesDefaultModel);
		}
		
		
		public void removeProductIdCommissionShShareDefaultModel(CommissionShSharesDefaultModel commissionShSharesDefaultModel) {      
			commissionShSharesDefaultModel.setRelationProductIdProductModel(null);
			productIdCommissionShSharesDefaultModelList.remove(commissionShSharesDefaultModel);      
		}
		
		@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
		@JoinColumn(name = "PRODUCT_ID")
		public Collection<CommissionShSharesDefaultModel> getProductIdCommissionShSharesDefaultModelList() throws Exception {
				return productIdCommissionShSharesDefaultModelList;
		}
		
		
		
		public void setProductIdCommissionShSharesDefaultModelList(Collection<CommissionShSharesDefaultModel> productIdCommissionShSharesDefaultModelList) throws Exception {
			this.productIdCommissionShSharesDefaultModelList = productIdCommissionShSharesDefaultModelList;
		}

   //////////

   /**
    * Get a list of related CommissionShSharesModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of CommissionShSharesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<CommissionShSharesModel> getProductIdCommissionShSharesModelList() throws Exception {
   		return productIdCommissionShSharesModelList;
   }


   /**
    * Set a list of CommissionShSharesModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param commissionShSharesModelList the list of related objects.
    */
    public void setProductIdCommissionShSharesModelList(Collection<CommissionShSharesModel> commissionShSharesModelList) throws Exception {
		this.productIdCommissionShSharesModelList = commissionShSharesModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */
    
   public void addProductIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationProductIdProductModel(this);
      productIdCommissionTransactionModelList.add(commissionTransactionModel);
   }
   
   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */
   
   public void removeProductIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {      
      commissionTransactionModel.setRelationProductIdProductModel(null);
      productIdCommissionTransactionModelList.remove(commissionTransactionModel);      
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<CommissionTransactionModel> getProductIdCommissionTransactionModelList() throws Exception {
   		return productIdCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
    public void setProductIdCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
		this.productIdCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related BillServiceLabelModel to this one-to-many relation.
    *
    * @param billServiceLabelModel object to be added.
    */
    
   public void addProductIdBillServiceLabelModel(BillServiceLabelModel billServiceLabelModel) {
      billServiceLabelModel.setRelationProductIdProductModel(this);
      productIdBillServiceLabelModelList.add(billServiceLabelModel);
   }
   
   /**
    * Remove the related BillServiceLabelModel to this one-to-many relation.
    *
    * @param billServiceLabelModel object to be removed.
    */
   
   public void removeProductIdBillServiceLabelModel(BillServiceLabelModel billServiceLabelModel) {      
      billServiceLabelModel.setRelationProductIdProductModel(null);
      productIdBillServiceLabelModelList.remove(billServiceLabelModel);      
   }

   /**
    * Get a list of related BillServiceLabelModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of BillServiceLabelModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<BillServiceLabelModel> getProductIdBillServiceLabelModelList() throws Exception {
   		return productIdBillServiceLabelModelList;
   }


   /**
    * Set a list of BillServiceLabelModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param billServiceLabelModelList the list of related objects.
    */
    public void setProductIdBillServiceLabelModelList(Collection<BillServiceLabelModel> billServiceLabelModelList) throws Exception {
		this.productIdBillServiceLabelModelList = billServiceLabelModelList;
   }


   /**
    * Add the related ProductCatalogDetailModel to this one-to-many relation.
    *
    * @param productCatalogDetailModel object to be added.
    */
    
   public void addProductIdProductCatalogDetailModel(ProductCatalogDetailModel productCatalogDetailModel) {
      productCatalogDetailModel.setRelationProductIdProductModel(this);
      productIdProductCatalogDetailModelList.add(productCatalogDetailModel);
   }
   
   /**
    * Remove the related ProductCatalogDetailModel to this one-to-many relation.
    *
    * @param productCatalogDetailModel object to be removed.
    */
   
   public void removeProductIdProductCatalogDetailModel(ProductCatalogDetailModel productCatalogDetailModel) {      
      productCatalogDetailModel.setRelationProductIdProductModel(null);
      productIdProductCatalogDetailModelList.remove(productCatalogDetailModel);      
   }

   /**
    * Get a list of related ProductCatalogDetailModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of ProductCatalogDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<ProductCatalogDetailModel> getProductIdProductCatalogDetailModelList() throws Exception {
   		return productIdProductCatalogDetailModelList;
   }


   /**
    * Set a list of ProductCatalogDetailModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param productCatalogDetailModelList the list of related objects.
    */
    public void setProductIdProductCatalogDetailModelList(Collection<ProductCatalogDetailModel> productCatalogDetailModelList) throws Exception {
		this.productIdProductCatalogDetailModelList = productCatalogDetailModelList;
   }


   /**
    * Add the related ProductDeviceFlowModel to this one-to-many relation.
    *
    * @param productDeviceFlowModel object to be added.
    */
    
   public void addProductIdProductDeviceFlowModel(ProductDeviceFlowModel productDeviceFlowModel) {
      productDeviceFlowModel.setRelationProductIdProductModel(this);
      productIdProductDeviceFlowModelList.add(productDeviceFlowModel);
   }
   
   /**
    * Remove the related ProductDeviceFlowModel to this one-to-many relation.
    *
    * @param productDeviceFlowModel object to be removed.
    */
   
   public void removeProductIdProductDeviceFlowModel(ProductDeviceFlowModel productDeviceFlowModel) {      
      productDeviceFlowModel.setRelationProductIdProductModel(null);
      productIdProductDeviceFlowModelList.remove(productDeviceFlowModel);      
   }

   /**
    * Get a list of related ProductDeviceFlowModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of ProductDeviceFlowModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<ProductDeviceFlowModel> getProductIdProductDeviceFlowModelList() throws Exception {
   		return productIdProductDeviceFlowModelList;
   }


   /**
    * Set a list of ProductDeviceFlowModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param productDeviceFlowModelList the list of related objects.
    */
    public void setProductIdProductDeviceFlowModelList(Collection<ProductDeviceFlowModel> productDeviceFlowModelList) throws Exception {
		this.productIdProductDeviceFlowModelList = productDeviceFlowModelList;
   }


   /**
    * Add the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be added.
    */
    
   public void addProductIdProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationProductIdProductModel(this);
      productIdProductUnitModelList.add(productUnitModel);
   }
   
   /**
    * Remove the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be removed.
    */
   
   public void removeProductIdProductUnitModel(ProductUnitModel productUnitModel) {      
      productUnitModel.setRelationProductIdProductModel(null);
      productIdProductUnitModelList.remove(productUnitModel);      
   }

   /**
    * Get a list of related ProductUnitModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of ProductUnitModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<ProductUnitModel> getProductIdProductUnitModelList() throws Exception {
   		return productIdProductUnitModelList;
   }


   /**
    * Set a list of ProductUnitModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param productUnitModelList the list of related objects.
    */
    public void setProductIdProductUnitModelList(Collection<ProductUnitModel> productUnitModelList) throws Exception {
		this.productIdProductUnitModelList = productUnitModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */
    
   public void addProductIdShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationProductIdProductModel(this);
      productIdShipmentModelList.add(shipmentModel);
   }
   
   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */
   
   public void removeProductIdShipmentModel(ShipmentModel shipmentModel) {      
      shipmentModel.setRelationProductIdProductModel(null);
      productIdShipmentModelList.remove(shipmentModel);      
   }

   /**
    * Get a list of related ShipmentModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<ShipmentModel> getProductIdShipmentModelList() throws Exception {
   		return productIdShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param shipmentModelList the list of related objects.
    */
    public void setProductIdShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
		this.productIdShipmentModelList = shipmentModelList;
   }


   /**
    * Add the related SwitchUtilityMappingModel to this one-to-many relation.
    *
    * @param switchUtilityMappingModel object to be added.
    */
    
   public void addProductIdSwitchUtilityMappingModel(SwitchUtilityMappingModel switchUtilityMappingModel) {
      switchUtilityMappingModel.setRelationProductIdProductModel(this);
      productIdSwitchUtilityMappingModelList.add(switchUtilityMappingModel);
   }
   
   /**
    * Remove the related SwitchUtilityMappingModel to this one-to-many relation.
    *
    * @param switchUtilityMappingModel object to be removed.
    */
   
   public void removeProductIdSwitchUtilityMappingModel(SwitchUtilityMappingModel switchUtilityMappingModel) {      
      switchUtilityMappingModel.setRelationProductIdProductModel(null);
      productIdSwitchUtilityMappingModelList.remove(switchUtilityMappingModel);      
   }

   /**
    * Get a list of related SwitchUtilityMappingModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of SwitchUtilityMappingModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<SwitchUtilityMappingModel> getProductIdSwitchUtilityMappingModelList() throws Exception {
   		return productIdSwitchUtilityMappingModelList;
   }


   /**
    * Set a list of SwitchUtilityMappingModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param switchUtilityMappingModelList the list of related objects.
    */
    public void setProductIdSwitchUtilityMappingModelList(Collection<SwitchUtilityMappingModel> switchUtilityMappingModelList) throws Exception {
		this.productIdSwitchUtilityMappingModelList = switchUtilityMappingModelList;
   }


   /**
    * Add the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be added.
    */
    
   public void addProductIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      transactionDetailModel.setRelationProductIdProductModel(this);
      productIdTransactionDetailModelList.add(transactionDetailModel);
   }
   
   /**
    * Remove the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be removed.
    */
   
   public void removeProductIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {      
      transactionDetailModel.setRelationProductIdProductModel(null);
      productIdTransactionDetailModelList.remove(transactionDetailModel);      
   }

   /**
    * Get a list of related TransactionDetailModel objects of the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @return Collection of TransactionDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIdProductModel")
   @JoinColumn(name = "PRODUCT_ID")
   public Collection<TransactionDetailModel> getProductIdTransactionDetailModelList() throws Exception {
   		return productIdTransactionDetailModelList;
   }


   /**
    * Set a list of TransactionDetailModel related objects to the ProductModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductId member.
    *
    * @param transactionDetailModelList the list of related objects.
    */
    public void setProductIdTransactionDetailModelList(Collection<TransactionDetailModel> transactionDetailModelList) throws Exception {
		this.productIdTransactionDetailModelList = transactionDetailModelList;
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
    * Returns the value of the <code>serviceId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getServiceId() {
      if (serviceIdServiceModel != null) {
         return serviceIdServiceModel.getServiceId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>serviceId</code> property.
    *
    * @param serviceId the value for the <code>serviceId</code> property
					    * @spring.validator type="required"
																																																			    */
   
   @javax.persistence.Transient
   public void setServiceId(Long serviceId) {
      if(serviceId == null)
      {      
      	serviceIdServiceModel = null;
      }
      else
      {
        serviceIdServiceModel = new ServiceModel();
      	serviceIdServiceModel.setServiceId(serviceId);
      }      
   }

   /**
    * Returns the value of the <code>productIntgVoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductIntgVoId() {
      if (productIntgVoIdProductIntgVoModel != null) {
         return productIntgVoIdProductIntgVoModel.getProductIntgVoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productIntgVoId</code> property.
    *
    * @param productIntgVoId the value for the <code>productIntgVoId</code> property
																																																							    */
   
   @javax.persistence.Transient
   public void setProductIntgVoId(Long productIntgVoId) {
      if(productIntgVoId == null)
      {      
      	productIntgVoIdProductIntgVoModel = null;
      }
      else
      {
        productIntgVoIdProductIntgVoModel = new ProductIntgVoModel();
      	productIntgVoIdProductIntgVoModel.setProductIntgVoId(productIntgVoId);
      }      
   }

   /**
    * Returns the value of the <code>productIntgModuleInfoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductIntgModuleInfoId() {
      if (productIntgModuleInfoIdProductIntgModuleInfoModel != null) {
         return productIntgModuleInfoIdProductIntgModuleInfoModel.getProductIntgModuleInfoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productIntgModuleInfoId</code> property.
    *
    * @param productIntgModuleInfoId the value for the <code>productIntgModuleInfoId</code> property
																	    * @spring.validator type="required"
																																							    */
   
   @javax.persistence.Transient
   public void setProductIntgModuleInfoId(Long productIntgModuleInfoId) {
      if(productIntgModuleInfoId == null)
      {      
      	productIntgModuleInfoIdProductIntgModuleInfoModel = null;
      }
      else
      {
        productIntgModuleInfoIdProductIntgModuleInfoModel = new ProductIntgModuleInfoModel();
      	productIntgModuleInfoIdProductIntgModuleInfoModel.setProductIntgModuleInfoId(productIntgModuleInfoId);
      }      
   }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getHelpLine() {
      if (helpLineNotificationMessageModel != null) {
         return helpLineNotificationMessageModel.getNotificationMessageId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>notificationMessageId</code> property.
    *
    * @param notificationMessageId the value for the <code>notificationMessageId</code> property
																																													    * @spring.validator type="required"
											    */
   
   @javax.persistence.Transient
   public void setHelpLine(Long notificationMessageId) {
      if(notificationMessageId == null)
      {      
      	helpLineNotificationMessageModel = null;
      }
      else
      {
        helpLineNotificationMessageModel = new NotificationMessageModel();
      	helpLineNotificationMessageModel.setNotificationMessageId(notificationMessageId);
      }      
   }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getInstructionId() {
      if (instructionIdNotificationMessageModel != null) {
         return instructionIdNotificationMessageModel.getNotificationMessageId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>notificationMessageId</code> property.
    *
    * @param notificationMessageId the value for the <code>notificationMessageId</code> property
									    * @spring.validator type="required"
																																															    */
   
   @javax.persistence.Transient
   public void setInstructionId(Long notificationMessageId) {
      if(notificationMessageId == null)
      {      
      	instructionIdNotificationMessageModel = null;
      }
      else
      {
        instructionIdNotificationMessageModel = new NotificationMessageModel();
      	instructionIdNotificationMessageModel.setNotificationMessageId(notificationMessageId);
      }      
   }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSuccessMessageId() {
      if (successMessageIdNotificationMessageModel != null) {
         return successMessageIdNotificationMessageModel.getNotificationMessageId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>notificationMessageId</code> property.
    *
    * @param notificationMessageId the value for the <code>notificationMessageId</code> property
											    * @spring.validator type="required"
																																													    */
   
   @javax.persistence.Transient
   public void setSuccessMessageId(Long notificationMessageId) {
      if(notificationMessageId == null)
      {      
      	successMessageIdNotificationMessageModel = null;
      }
      else
      {
        successMessageIdNotificationMessageModel = new NotificationMessageModel();
      	successMessageIdNotificationMessageModel.setNotificationMessageId(notificationMessageId);
      }      
   }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFailureMessageId() {
      if (failureMessageIdNotificationMessageModel != null) {
         return failureMessageIdNotificationMessageModel.getNotificationMessageId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>notificationMessageId</code> property.
    *
    * @param notificationMessageId the value for the <code>notificationMessageId</code> property
																																																							    */
   
   @javax.persistence.Transient
   public void setFailureMessageId(Long notificationMessageId) {
      if(notificationMessageId == null)
      {      
      	failureMessageIdNotificationMessageModel = null;
      }
      else
      {
        failureMessageIdNotificationMessageModel = new NotificationMessageModel();
      	failureMessageIdNotificationMessageModel.setNotificationMessageId(notificationMessageId);
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
        checkBox += "_"+ getProductId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productId=" + getProductId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productId";
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
    	
    	associationModel.setClassName("ServiceModel");
    	associationModel.setPropertyName("relationServiceIdServiceModel");   		
   		associationModel.setValue(getRelationServiceIdServiceModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductIntgVoModel");
    	associationModel.setPropertyName("relationProductIntgVoIdProductIntgVoModel");   		
   		associationModel.setValue(getRelationProductIntgVoIdProductIntgVoModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductIntgModuleInfoModel");
    	associationModel.setPropertyName("relationProductIntgModuleInfoIdProductIntgModuleInfoModel");   		
   		associationModel.setValue(getRelationProductIntgModuleInfoIdProductIntgModuleInfoModel());
   		
   		associationModelList.add(associationModel);
   		
   		//***********************************************
   		associationModel = new AssociationModel();
	 	
	 	associationModel.setClassName("WHTConfigModel");
	 	associationModel.setPropertyName("wHTConfigIdWHTConfigModel");   		
		associationModel.setValue(getwHTConfigIdWHTConfigModel());
		
		associationModelList.add(associationModel);
   		//************************************************
   		
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("NotificationMessageModel");
    	associationModel.setPropertyName("relationHelpLineNotificationMessageModel");   		
   		associationModel.setValue(getRelationHelpLineNotificationMessageModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("NotificationMessageModel");
    	associationModel.setPropertyName("relationInstructionIdNotificationMessageModel");   		
   		associationModel.setValue(getRelationInstructionIdNotificationMessageModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("NotificationMessageModel");
    	associationModel.setPropertyName("relationSuccessMessageIdNotificationMessageModel");   		
   		associationModel.setValue(getRelationSuccessMessageIdNotificationMessageModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("NotificationMessageModel");
    	associationModel.setPropertyName("relationFailureMessageIdNotificationMessageModel");   		
   		associationModel.setValue(getRelationFailureMessageIdNotificationMessageModel());
   		
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
   		
	    associationModel = new AssociationModel();
	    	
		associationModel.setClassName("CategoryModel");
		associationModel.setPropertyName("relationCategoryIdCategoryModel");   		
		associationModel.setValue(getRelationCategoryIdCategoryModel());
	
		associationModelList.add(associationModel);

        associationModel=new AssociationModel();
        associationModel.setClassName("AppUserTypeModel");
        associationModel.setPropertyName("appUserTypeModel");
        associationModel.setValue(getAppUserTypeModel());
        associationModelList.add(associationModel);

    	return associationModelList;
    }
    @Column(name = "MIN_LIMIT")
	public Double getMinLimit() {
		return minLimit;
	}
    public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}

	 @Column(name = "MAX_LIMIT")
	public Double getMaxLimit() {
		return maxLimit;
	}
	 public void setMaxLimit(Double maxLimit) {
			this.maxLimit = maxLimit;
		}

	@Column(name = "INCL_CHARGES_CHECK")
	public Boolean getInclChargesCheck() {
		return inclChargesCheck;
	}

	public void setInclChargesCheck(Boolean inclChargesCheck) {
		this.inclChargesCheck = inclChargesCheck;
	}   
	@Column(name = "CONSUMER_LABEL")
	public String getConsumerLabel() {
		return consumerLabel;
	}

	public void setConsumerLabel(String consumerLabel) {
		this.consumerLabel = consumerLabel;
	}
	
	
	@Column(name = "CNSMR_INPUT_TYPE")
	public String getConsumerInputType() {
		return consumerInputType;
	}

	public void setConsumerInputType(String consumerInputType) {
		this.consumerInputType = consumerInputType;
	}

	@Column(name = "AMT_REQUIRED")
	public Boolean getAmtRequired() {
		return amtRequired;
	}

	public void setAmtRequired(Boolean amtRequired) {
		this.amtRequired = amtRequired;
	}

	@Column(name = "DO_VALIDATE")
	public Boolean getDoValidate() {
		return doValidate;
	}

	public void setDoValidate(Boolean doValidate) {
		this.doValidate = doValidate;
	}
	
	@javax.persistence.Transient
	public Double getExclusiveFixAmount() {
		return exclusiveFixAmount;
	}

	@javax.persistence.Transient
	public void setExclusiveFixAmount(Double exclusiveFixAmount) {
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	@javax.persistence.Transient
	public Double getExclusivePercentAmount() {
		return exclusivePercentAmount;
	}

	@javax.persistence.Transient
	public void setExclusivePercentAmount(Double exclusivePercentAmount) {
		this.exclusivePercentAmount = exclusivePercentAmount;
	}

	@javax.persistence.Transient
	public Double getInclusiveFixAmount() {
		return inclusiveFixAmount;
	}

	@javax.persistence.Transient
	public void setInclusiveFixAmount(Double inclusiveFixAmount) {
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	@javax.persistence.Transient
	public Double getInclusivePercentAmount() {
		return inclusivePercentAmount;
	}

	@javax.persistence.Transient
	public void setInclusivePercentAmount(Double inclusivePercentAmount) {
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	@javax.persistence.Transient
	public Long getCommissionRateDefaultModelId() {
		return commissionRateDefaultModelId;
	}

	@javax.persistence.Transient
	public void setCommissionRateDefaultModelId(Long commissionRateDefaultModelId) {
		this.commissionRateDefaultModelId = commissionRateDefaultModelId;
	}

	@javax.persistence.Transient
	public Long getStakeHolderId() {
		return stakeHolderId;
	}

	@javax.persistence.Transient
	public void setStakeHolderId(Long stakeHolderId) {
		this.stakeHolderId = stakeHolderId;
	}

	@javax.persistence.Transient
	public String getStakeHolderName() {
		return stakeHolderName;
	}

	@javax.persistence.Transient
	public void setStakeHolderName(String stakeHolderName) {
		this.stakeHolderName = stakeHolderName;
	}

	/*@javax.persistence.Transient
	public Double getShare() {
		return share;
	}

	@javax.persistence.Transient
	public void setShare(Double share) {
		this.share = share;
	}*/

	@javax.persistence.Transient
	public Boolean getIsWithHolding() {
		return isWithHolding;
	}

	@javax.persistence.Transient
	public void setIsWithHolding(Boolean isWithHolding) {
		this.isWithHolding = isWithHolding;
	}

	@javax.persistence.Transient
	public Boolean getIsFed() {
		return isFed;
	}

	@javax.persistence.Transient
	public void setIsFed(Boolean isFed) {
		this.isFed = isFed;
	}
	
	 @Column(name = "IS_TAXABLE" , nullable = false )
	   	public Boolean getTaxable() {
			return taxable;
		}
		
		public void setTaxable(Boolean taxable) {
			this.taxable = taxable;
		}

	@javax.persistence.Transient
	public Double getWithHoldingShare() {
		return withHoldingShare;
	}

	@javax.persistence.Transient
	public void setWithHoldingShare(Double withHoldingShare) {
		this.withHoldingShare = withHoldingShare;
	}

	@javax.persistence.Transient
	public Double getFedShare() {
		if(fedShare == null){
			fedShare = 0.0;
		}
		return fedShare;
	}

	@javax.persistence.Transient
	public void setFedShare(Double fedShare) {
		this.fedShare = fedShare;
	}

	@javax.persistence.Transient
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@javax.persistence.Transient
	public String getAccountNick() {
		return accountNick;
	}

	public void setAccountNick(String accountNick) {
		this.accountNick = accountNick;
	}
	
	@Column(name = "MULTIPLES")
	public Long getMultiples() {
		return multiples;
	}

	public void setMultiples(Long multiples) {
		this.multiples = multiples;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY_ID")    
	public CategoryModel getRelationCategoryIdCategoryModel(){
		return categoryIdCategoryModel;
	}
	   
	@javax.persistence.Transient
	public CategoryModel getCategoryIdCategoryModel(){
		return getRelationCategoryIdCategoryModel();
	}

	@javax.persistence.Transient
	public void setRelationCategoryIdCategoryModel(CategoryModel categoryModel) {
		this.categoryIdCategoryModel = categoryModel;
	}
	   
	@javax.persistence.Transient
	public void setCategoryIdCategoryModel(CategoryModel categoryModel) {
		if(null != categoryModel) {
			setRelationCategoryIdCategoryModel((CategoryModel)categoryModel.clone());
	    }      
	}
	
	@javax.persistence.Transient
	public Long getCategoryId() {
		if (categoryIdCategoryModel != null) {
	         return categoryIdCategoryModel.getCategoryId();
	    } else {
	         return null;
	    }
	}
	   
	@javax.persistence.Transient
	public void setCategoryId(Long categoryId) {
		if(categoryId == null) {      
			categoryIdCategoryModel = null;
	    }else{
	    	categoryIdCategoryModel = new CategoryModel();
	    	categoryIdCategoryModel.setCategoryId(categoryId);
	    }      
	}
	
	@Column(name = "COMMISSION_STAKEHOLDER_ID")
	public Long getCommissionStakeHolderId() {
		return commissionStakeHolderId;
	}

	public void setCommissionStakeHolderId(Long commissionStakeHolderId) {
		this.commissionStakeHolderId = commissionStakeHolderId;
	}

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public AppUserTypeModel getAppUserTypeModel(){
      return appUserTypeModel;
   }

   public void setAppUserTypeModel(AppUserTypeModel appUserTypeModel){
      this.appUserTypeModel = appUserTypeModel;
   }

   @Transient
   public Long getAppUserTypeId(){
      return appUserTypeModel == null ? null:appUserTypeModel.getAppUserTypeId();
   }

   public void setAppUserTypeId(Long appUserTypeId){
      if(appUserTypeId == null){
         appUserTypeModel = null;
      }else{
         appUserTypeModel = new AppUserTypeModel();
         appUserTypeModel.setAppUserTypeId(appUserTypeId);
      }
   }

   @Column(name="URL")
   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   @Column(name="BVS_CONFIG_ID")
   public Long getBvsConfigId() {
      return bvsConfigId;
   }

   public void setBvsConfigId(Long bvsConfigId) {
      this.bvsConfigId = bvsConfigId;
   }

   @Column(name="PROD_DENOM")
   public String getProdDenom() { return prodDenom; }

   public void setProdDenom(String prodDenom) { this.prodDenom = prodDenom; }

   @Column(name="DENOM_FLAG")
   public Boolean getDenomFlag() { return denomFlag; }

   public void setDenomFlag(Boolean denomFlag) { this.denomFlag = denomFlag; }

   @Column(name="DENOM_STRING")
   public String getDenomString() { return denomString; }

   public void setDenomString(String denomString) { this.denomString = denomString; }
}
