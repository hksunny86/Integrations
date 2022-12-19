package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
 * The TransactionDetailModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionDetailModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="TRANSACTION_DETAIL_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="TRANSACTION_DETAIL_seq") } )
//@javax.persistence.SequenceGenerator(name = "TRANSACTION_DETAIL_seq",sequenceName = "TRANSACTION_DETAIL_seq")
@Table(name = "TRANSACTION_DETAIL")
public class TransactionDetailModel extends BasePersistableModel implements Serializable{
  

   private TransactionModel transactionIdTransactionModel;
   private ProductUnitModel productUnitIdProductUnitModel;
   private ProductModel productIdProductModel;

   private Collection<AllpayCommissionTransactionModel> transactionDetailIdAllpayCommissionTransactionModelList = new ArrayList<AllpayCommissionTransactionModel>();
   private Collection<CommissionTransactionModel> transactionDetailIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();

   private Long transactionDetailId;
   private String consumerNo;
   private Double productUnitPrice;
   private Double productTopupAmount;
   private Double actualBillableAmount;
   private Double productCostPrice;
   private Boolean settled;
   private String customField1;
   private String customField2;
   private String customField3;
   private String customField4;
   private String customField5;
   private String customField6;
   private String customField7;
   private String customField8;
   private String customField9;
   private String customField10;
   private String customField11;
   private String customField12;
   private String customField13;
   private String customField14;
   private String customField15;
   private Integer versionNo;
   private String suppResponseCode;
   

   /**
    * Default constructor.
    */
   public TransactionDetailModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionDetailId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionDetailId</code> property.
    *
    */
      @Column(name = "TRANSACTION_DETAIL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_DETAIL_seq")
   public Long getTransactionDetailId() {
      return transactionDetailId;
   }

   /**
    * Sets the value of the <code>transactionDetailId</code> property.
    *
    * @param transactionDetailId the value for the <code>transactionDetailId</code> property
    *    
		    */

   public void setTransactionDetailId(Long transactionDetailId) {
      this.transactionDetailId = transactionDetailId;
   }

   /**
    * Returns the value of the <code>consumerNo</code> property.
    *
    */
      @Column(name = "CONSUMER_NO"  , length=50 )
   public String getConsumerNo() {
      return consumerNo;
   }

   /**
    * Sets the value of the <code>consumerNo</code> property.
    *
    * @param consumerNo the value for the <code>consumerNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConsumerNo(String consumerNo) {
      this.consumerNo = consumerNo;
   }

   /**
    * Returns the value of the <code>productUnitPrice</code> property.
    *
    */
      @Column(name = "PRODUCT_UNIT_PRICE"  )
   public Double getProductUnitPrice() {
      return productUnitPrice;
   }

   /**
    * Sets the value of the <code>productUnitPrice</code> property.
    *
    * @param productUnitPrice the value for the <code>productUnitPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductUnitPrice(Double productUnitPrice) {
      this.productUnitPrice = productUnitPrice;
   }

   /**
    * Returns the value of the <code>productTopupAmount</code> property.
    *
    */
      @Column(name = "PRODUCT_TOPUP_AMOUNT"  )
   public Double getProductTopupAmount() {
      return productTopupAmount;
   }

   /**
    * Sets the value of the <code>productTopupAmount</code> property.
    *
    * @param productTopupAmount the value for the <code>productTopupAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductTopupAmount(Double productTopupAmount) {
      this.productTopupAmount = productTopupAmount;
   }

   /**
    * Returns the value of the <code>actualBillableAmount</code> property.
    *
    */
      @Column(name = "ACTUAL_BILLABLE_AMOUNT"  )
   public Double getActualBillableAmount() {
      return actualBillableAmount;
   }

   /**
    * Sets the value of the <code>actualBillableAmount</code> property.
    *
    * @param actualBillableAmount the value for the <code>actualBillableAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setActualBillableAmount(Double actualBillableAmount) {
      this.actualBillableAmount = actualBillableAmount;
   }

   /**
    * Returns the value of the <code>productCostPrice</code> property.
    *
    */
      @Column(name = "PRODUCT_COST_PRICE"  )
   public Double getProductCostPrice() {
      return productCostPrice;
   }

   /**
    * Sets the value of the <code>productCostPrice</code> property.
    *
    * @param productCostPrice the value for the <code>productCostPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductCostPrice(Double productCostPrice) {
      this.productCostPrice = productCostPrice;
   }

   /**
    * Returns the value of the <code>settled</code> property.
    *
    */
      @Column(name = "IS_SETTLED" , nullable = false )
   public Boolean getSettled() {
      return settled;
   }

   /**
    * Sets the value of the <code>settled</code> property.
    *
    * @param settled the value for the <code>settled</code> property
    *    
		    */

   public void setSettled(Boolean settled) {
      this.settled = settled;
   }

   /**
    * Returns the value of the <code>customField1</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD1"  , length=50 )
   public String getCustomField1() {
      return customField1;
   }

   /**
    * Sets the value of the <code>customField1</code> property.
    *
    * @param customField1 the value for the <code>customField1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField1(String customField1) {
      this.customField1 = customField1;
   }

   /**
    * Returns the value of the <code>customField2</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD2"  , length=50 )
   public String getCustomField2() {
      return customField2;
   }

   /**
    * Sets the value of the <code>customField2</code> property.
    *
    * @param customField2 the value for the <code>customField2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField2(String customField2) {
      this.customField2 = customField2;
   }

   /**
    * Returns the value of the <code>customField3</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD3"  , length=50 )
   public String getCustomField3() {
      return customField3;
   }

   /**
    * Sets the value of the <code>customField3</code> property.
    *
    * @param customField3 the value for the <code>customField3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField3(String customField3) {
      this.customField3 = customField3;
   }

   /**
    * Returns the value of the <code>customField4</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD4"  , length=50 )
   public String getCustomField4() {
      return customField4;
   }

   /**
    * Sets the value of the <code>customField4</code> property.
    *
    * @param customField4 the value for the <code>customField4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField4(String customField4) {
      this.customField4 = customField4;
   }

   /**
    * Returns the value of the <code>customField5</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD5"  , length=50 )
   public String getCustomField5() {
      return customField5;
   }

   /**
    * Sets the value of the <code>customField5</code> property.
    *
    * @param customField5 the value for the <code>customField5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField5(String customField5) {
      this.customField5 = customField5;
   }

   /**
    * Returns the value of the <code>customField6</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD6"  , length=50 )
   public String getCustomField6() {
      return customField6;
   }

   /**
    * Sets the value of the <code>customField6</code> property.
    *
    * @param customField6 the value for the <code>customField6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField6(String customField6) {
      this.customField6 = customField6;
   }

   /**
    * Returns the value of the <code>customField7</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD7"  , length=50 )
   public String getCustomField7() {
      return customField7;
   }

   /**
    * Sets the value of the <code>customField7</code> property.
    *
    * @param customField7 the value for the <code>customField7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField7(String customField7) {
      this.customField7 = customField7;
   }

   /**
    * Returns the value of the <code>customField8</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD8"  , length=50 )
   public String getCustomField8() {
      return customField8;
   }

   /**
    * Sets the value of the <code>customField8</code> property.
    *
    * @param customField8 the value for the <code>customField8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField8(String customField8) {
      this.customField8 = customField8;
   }

   /**
    * Returns the value of the <code>customField9</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD9"  , length=50 )
   public String getCustomField9() {
      return customField9;
   }

   /**
    * Sets the value of the <code>customField9</code> property.
    *
    * @param customField9 the value for the <code>customField9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField9(String customField9) {
      this.customField9 = customField9;
   }

   /**
    * Returns the value of the <code>customField10</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD10"  , length=50 )
   public String getCustomField10() {
      return customField10;
   }

   /**
    * Sets the value of the <code>customField10</code> property.
    *
    * @param customField10 the value for the <code>customField10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomField10(String customField10) {
      this.customField10 = customField10;
   }

   /**
    * Returns the value of the <code>customField11</code> property.
    *
    */
    @Column(name = "CUSTOM_FIELD11"  , length=250 )
   public String getCustomField11() {
	return customField11;
	}
	
    /**
     * Sets the value of the <code>customField11</code> property.
     *
     * @param customField10 the value for the <code>customField11</code> property
     *    
     * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setCustomField11(String customField11) {
    	this.customField11 = customField11;
    }

    /**
     * Returns the value of the <code>customField12</code> property.
     *
     */
    @Column(name = "CUSTOM_FIELD12"  , length=250 )
    public String getCustomField12() {
    	return customField12;
    }

    /**
     * Sets the value of the <code>customField12</code> property.
     *
     * @param customField10 the value for the <code>customField12</code> property
     *    
     * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setCustomField12(String customField12) {
    	this.customField12 = customField12;
    }

    /**
     * Returns the value of the <code>customField13</code> property.
     *
     */
    @Column(name = "CUSTOM_FIELD13"  , length=250 )
    public String getCustomField13() {
    	return customField13;
    }

    /**
     * Sets the value of the <code>customField13</code> property.
     *
     * @param customField10 the value for the <code>customField13</code> property
     *    
     * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setCustomField13(String customField13) {
    	this.customField13 = customField13;
    }

    /**
     * Returns the value of the <code>customField14</code> property.
     *
     */
    @Column(name = "CUSTOM_FIELD14"  , length=250 )
    public String getCustomField14() {
    	return customField14;
    }

    /**
     * Sets the value of the <code>customField12</code> property.
     *
     * @param customField10 the value for the <code>customField12</code> property
     *    
     * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setCustomField14(String customField14) {
    	this.customField14 = customField14;
    }

    /**
     * Returns the value of the <code>customField15</code> property.
     *
     */
    @Column(name = "CUSTOM_FIELD15"  , length=250 )
    public String getCustomField15() {
    	return customField15;
    }

    /**
     * Sets the value of the <code>customField15</code> property.
     *
     * @param customField10 the value for the <code>customField15</code> property
     *    
     * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setCustomField15(String customField15) {
    	this.customField15 = customField15;
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
    * Returns the value of the <code>suppResponseCode</code> property.
    *
    */
      @Column(name = "SUPP_RESPONSE_CODE"  , length=50 )
   public String getSuppResponseCode() {
      return suppResponseCode;
   }

   /**
    * Sets the value of the <code>suppResponseCode</code> property.
    *
    * @param suppResponseCode the value for the <code>suppResponseCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSuppResponseCode(String suppResponseCode) {
      this.suppResponseCode = suppResponseCode;
   }
   
   /**
    * Returns the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @return the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_ID")    
   public TransactionModel getRelationTransactionIdTransactionModel(){
      return transactionIdTransactionModel;
   }
    
   /**
    * Returns the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @return the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionModel getTransactionIdTransactionModel(){
      return getRelationTransactionIdTransactionModel();
   }

   /**
    * Sets the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @param transactionModel a value for <code>transactionIdTransactionModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionIdTransactionModel(TransactionModel transactionModel) {
      this.transactionIdTransactionModel = transactionModel;
   }
   
   /**
    * Sets the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @param transactionModel a value for <code>transactionIdTransactionModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionIdTransactionModel(TransactionModel transactionModel) {
      if(null != transactionModel)
      {
      	setRelationTransactionIdTransactionModel((TransactionModel)transactionModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @return the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_UNIT_ID")    
   public ProductUnitModel getRelationProductUnitIdProductUnitModel(){
      return productUnitIdProductUnitModel;
   }
    
   /**
    * Returns the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @return the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductUnitModel getProductUnitIdProductUnitModel(){
      return getRelationProductUnitIdProductUnitModel();
   }

   /**
    * Sets the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @param productUnitModel a value for <code>productUnitIdProductUnitModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductUnitIdProductUnitModel(ProductUnitModel productUnitModel) {
      this.productUnitIdProductUnitModel = productUnitModel;
   }
   
   /**
    * Sets the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @param productUnitModel a value for <code>productUnitIdProductUnitModel</code>.
    */
   @javax.persistence.Transient
   public void setProductUnitIdProductUnitModel(ProductUnitModel productUnitModel) {
      if(null != productUnitModel)
      {
      	setRelationProductUnitIdProductUnitModel((ProductUnitModel)productUnitModel.clone());
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
    * Add the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be added.
    */
    
   public void addTransactionDetailIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationTransactionDetailIdTransactionDetailModel(this);
      transactionDetailIdAllpayCommissionTransactionModelList.add(allpayCommissionTransactionModel);
   }
   
   /**
    * Remove the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be removed.
    */
   
   public void removeTransactionDetailIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {      
      allpayCommissionTransactionModel.setRelationTransactionDetailIdTransactionDetailModel(null);
      transactionDetailIdAllpayCommissionTransactionModelList.remove(allpayCommissionTransactionModel);      
   }

   /**
    * Get a list of related AllpayCommissionTransactionModel objects of the TransactionDetailModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionDetailId member.
    *
    * @return Collection of AllpayCommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionDetailIdTransactionDetailModel")
   @JoinColumn(name = "TRANSACTION_DETAIL_ID")
   public Collection<AllpayCommissionTransactionModel> getTransactionDetailIdAllpayCommissionTransactionModelList() throws Exception {
   		return transactionDetailIdAllpayCommissionTransactionModelList;
   }


   /**
    * Set a list of AllpayCommissionTransactionModel related objects to the TransactionDetailModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionDetailId member.
    *
    * @param allpayCommissionTransactionModelList the list of related objects.
    */
    public void setTransactionDetailIdAllpayCommissionTransactionModelList(Collection<AllpayCommissionTransactionModel> allpayCommissionTransactionModelList) throws Exception {
		this.transactionDetailIdAllpayCommissionTransactionModelList = allpayCommissionTransactionModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */
    
   public void addTransactionDetailIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationTransactionDetailIdTransactionDetailModel(this);
      transactionDetailIdCommissionTransactionModelList.add(commissionTransactionModel);
   }
   
   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */
   
   public void removeTransactionDetailIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {      
      commissionTransactionModel.setRelationTransactionDetailIdTransactionDetailModel(null);
      transactionDetailIdCommissionTransactionModelList.remove(commissionTransactionModel);      
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the TransactionDetailModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionDetailId member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionDetailIdTransactionDetailModel")
   @JoinColumn(name = "TRANSACTION_DETAIL_ID")
   public Collection<CommissionTransactionModel> getTransactionDetailIdCommissionTransactionModelList() throws Exception {
   		return transactionDetailIdCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the TransactionDetailModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionDetailId member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
    public void setTransactionDetailIdCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
		this.transactionDetailIdCommissionTransactionModelList = commissionTransactionModelList;
   }



   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionId() {
      if (transactionIdTransactionModel != null) {
         return transactionIdTransactionModel.getTransactionId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionId</code> property.
    *
    * @param transactionId the value for the <code>transactionId</code> property
							    * @spring.validator type="required"
																																											    */
   
   @javax.persistence.Transient
   public void setTransactionId(Long transactionId) {
      if(transactionId == null)
      {      
      	transactionIdTransactionModel = null;
      }
      else
      {
        transactionIdTransactionModel = new TransactionModel();
      	transactionIdTransactionModel.setTransactionId(transactionId);
      }      
   }

   /**
    * Returns the value of the <code>productUnitId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductUnitId() {
      if (productUnitIdProductUnitModel != null) {
         return productUnitIdProductUnitModel.getProductUnitId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productUnitId</code> property.
    *
    * @param productUnitId the value for the <code>productUnitId</code> property
																																																	    */
   
   @javax.persistence.Transient
   public void setProductUnitId(Long productUnitId) {
      if(productUnitId == null)
      {      
      	productUnitIdProductUnitModel = null;
      }
      else
      {
        productUnitIdProductUnitModel = new ProductUnitModel();
      	productUnitIdProductUnitModel.setProductUnitId(productUnitId);
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionDetailId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionDetailId=" + getTransactionDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionDetailId";
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
    	
    	associationModel.setClassName("TransactionModel");
    	associationModel.setPropertyName("relationTransactionIdTransactionModel");   		
   		associationModel.setValue(getRelationTransactionIdTransactionModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductUnitModel");
    	associationModel.setPropertyName("relationProductUnitIdProductUnitModel");   		
   		associationModel.setValue(getRelationProductUnitIdProductUnitModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
