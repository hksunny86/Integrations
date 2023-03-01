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
import javax.xml.bind.annotation.*;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The SmartMoneyAccountModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SmartMoneyAccountModel"
 */
@XmlRootElement(name="smartMoneyAccountModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SMART_MONEY_ACCOUNT_seq",sequenceName = "SMART_MONEY_ACCOUNT_seq", allocationSize=1)
@Table(name = "SMART_MONEY_ACCOUNT")
public class SmartMoneyAccountModel extends BasePersistableModel implements Serializable {

   private RetailerContactModel retailerContactIdRetailerContactModel;
   private PaymentModeModel paymentModeIdPaymentModeModel;
   private DistributorContactModel distributorContactIdDistributorContactModel;
   private CustomerModel customerIdCustomerModel;
   private CurrencyCodeModel currencyCodeIdCurrencyCodeModel;
   private CardTypeModel cardTypeIdCardTypeModel;
   private CardProdCodeModel cardTypeProdIdCardProdCodeModel;
   private BankModel bankIdBankModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private AccountTypeModel accountTypeIdAccountTypeModel;
   private HandlerModel handlerIdHandlerModel;
   
   private Collection<TransactionModel> smartMoneyAccountIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long smartMoneyAccountId;
   @XmlElement
   private String name;
   @XmlElement
   private String description;
   @XmlElement
   private Boolean active;
   @XmlElement
   private Boolean defAccount;
   @XmlElement
   private Boolean changePinRequired;
   @XmlElement
   private Date createdOn;
   @XmlElement
   private Date updatedOn;
   @XmlElement
   private Integer versionNo;
   @XmlElement
   private String comments;
   @XmlElement
   private Boolean deleted;
   @XmlTransient
   private WalkinCustomerModel walkinCustomerModel;

   @XmlElement
   private Long accountOpeningMethodId;
   @XmlElement
   private Long accountOpeningDeviceTypeId;
   @XmlElement
   private Date dormancySmsSentOn;
   @XmlElement
   private Date dormancyRemovedOn;
   @XmlElement
   private Long statusId;
   @XmlElement
   private Long registrationStateId;
   @XmlElement
   private Long accountStateId;
   @XmlElement
   private Long previousRegStateId;
   @XmlElement
   private Long isAccountClosedUnsetteled;
   @XmlElement
   private Long isAccountClosedSetteled;
   private Double debitBlockAmount;
   private Boolean isDebitBlocked;
   private Boolean isOptasiaDebitBlocked;
   private String debitBlockReason;

   /**
    * Default constructor.
    */
   public SmartMoneyAccountModel() {
   }

   public SmartMoneyAccountModel(Long smartMoneyAccountId) {
      setPrimaryKey(smartMoneyAccountId);
   }
   /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSmartMoneyAccountId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSmartMoneyAccountId(primaryKey);
    }

   /**
    * Returns the value of the <code>smartMoneyAccountId</code> property.
    *
    */
   @XmlAttribute
      @Column(name = "SMART_MONEY_ACCOUNT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SMART_MONEY_ACCOUNT_seq")
   public Long getSmartMoneyAccountId() {
      return smartMoneyAccountId;
   }

   /**
    * Sets the value of the <code>smartMoneyAccountId</code> property.
    *
    * @param smartMoneyAccountId the value for the <code>smartMoneyAccountId</code> property
    *    
		    */

   public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
      this.smartMoneyAccountId = smartMoneyAccountId;
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
    * Returns the value of the <code>defAccount</code> property.
    *
    */
      @Column(name = "IS_DEF_ACCOUNT" , nullable = false )
   public Boolean getDefAccount() {
      return defAccount;
   }

   /**
    * Sets the value of the <code>defAccount</code> property.
    *
    * @param defAccount the value for the <code>defAccount</code> property
    *    
		    */

   public void setDefAccount(Boolean defAccount) {
      this.defAccount = defAccount;
   }

   /**
    * Returns the value of the <code>changePinRequired</code> property.
    *
    */
      @Column(name = "IS_CHANGE_PIN_REQUIRED" , nullable = false )
   public Boolean getChangePinRequired() {
      return changePinRequired;
   }

   /**
    * Sets the value of the <code>changePinRequired</code> property.
    *
    * @param changePinRequired the value for the <code>changePinRequired</code> property
    *    
		    */

   public void setChangePinRequired(Boolean changePinRequired) {
      this.changePinRequired = changePinRequired;
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
    * Returns the value of the <code>deleted</code> property.
    *
    */
      @Column(name = "IS_DELETED" , nullable = false )
   public Boolean getDeleted() {
      return deleted;
   }

   /**
    * Sets the value of the <code>deleted</code> property.
    *
    * @param deleted the value for the <code>deleted</code> property
    *    
		    */

   public void setDeleted(Boolean deleted) {
      this.deleted = deleted;
   }

   /**
    * Returns the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETAILER_CONTACT_ID")    
   public RetailerContactModel getRelationRetailerContactIdRetailerContactModel(){
      return retailerContactIdRetailerContactModel;
   }
    
   /**
    * Returns the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerContactModel getRetailerContactIdRetailerContactModel(){
      return getRelationRetailerContactIdRetailerContactModel();
   }

   /**
    * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      this.retailerContactIdRetailerContactModel = retailerContactModel;
   }
   
   /**
    * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      if(null != retailerContactModel)
      {
      	setRelationRetailerContactIdRetailerContactModel((RetailerContactModel)retailerContactModel.clone());
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
    * Returns the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRIBUTOR_CONTACT_ID")    
   public DistributorContactModel getRelationDistributorContactIdDistributorContactModel(){
      return distributorContactIdDistributorContactModel;
   }
    
   /**
    * Returns the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorContactModel getDistributorContactIdDistributorContactModel(){
      return getRelationDistributorContactIdDistributorContactModel();
   }

   /**
    * Sets the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>distributorContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistributorContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      this.distributorContactIdDistributorContactModel = distributorContactModel;
   }
   
   /**
    * Sets the value of the <code>distributorContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>distributorContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setDistributorContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      if(null != distributorContactModel)
      {
      	setRelationDistributorContactIdDistributorContactModel((DistributorContactModel)distributorContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CUSTOMER_ID")    
   public CustomerModel getRelationCustomerIdCustomerModel(){
      return customerIdCustomerModel;
   }
    
   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CustomerModel getCustomerIdCustomerModel(){
      return getRelationCustomerIdCustomerModel();
   }

   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
      this.customerIdCustomerModel = customerModel;
   }
   
   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setCustomerIdCustomerModel(CustomerModel customerModel) {
      if(null != customerModel)
      {
      	setRelationCustomerIdCustomerModel((CustomerModel)customerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    * @return the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CURRENCY_CODE_ID")    
   public CurrencyCodeModel getRelationCurrencyCodeIdCurrencyCodeModel(){
      return currencyCodeIdCurrencyCodeModel;
   }
    
   /**
    * Returns the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    * @return the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CurrencyCodeModel getCurrencyCodeIdCurrencyCodeModel(){
      return getRelationCurrencyCodeIdCurrencyCodeModel();
   }

   /**
    * Sets the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    * @param currencyCodeModel a value for <code>currencyCodeIdCurrencyCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCurrencyCodeIdCurrencyCodeModel(CurrencyCodeModel currencyCodeModel) {
      this.currencyCodeIdCurrencyCodeModel = currencyCodeModel;
   }
   
   /**
    * Sets the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
    *
    * @param currencyCodeModel a value for <code>currencyCodeIdCurrencyCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setCurrencyCodeIdCurrencyCodeModel(CurrencyCodeModel currencyCodeModel) {
      if(null != currencyCodeModel)
      {
      	setRelationCurrencyCodeIdCurrencyCodeModel((CurrencyCodeModel)currencyCodeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    * @return the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CARD_TYPE_ID")    
   public CardTypeModel getRelationCardTypeIdCardTypeModel(){
      return cardTypeIdCardTypeModel;
   }
    
   /**
    * Returns the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    * @return the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CardTypeModel getCardTypeIdCardTypeModel(){
      return getRelationCardTypeIdCardTypeModel();
   }

   /**
    * Sets the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    * @param cardTypeModel a value for <code>cardTypeIdCardTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCardTypeIdCardTypeModel(CardTypeModel cardTypeModel) {
      this.cardTypeIdCardTypeModel = cardTypeModel;
   }
   
   /**
    * Sets the value of the <code>cardTypeIdCardTypeModel</code> relation property.
    *
    * @param cardTypeModel a value for <code>cardTypeIdCardTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCardTypeIdCardTypeModel(CardTypeModel cardTypeModel) {
      if(null != cardTypeModel)
      {
      	setRelationCardTypeIdCardTypeModel((CardTypeModel)cardTypeModel.clone());
      }      
   }






   /**
    * Returns the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    * @return the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CARD_PRODUCT_TYPE_ID")
   public CardProdCodeModel getRelationCardTypeProdIdCardProdCodeModel(){
      return cardTypeProdIdCardProdCodeModel;
   }

   /**
    * Returns the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    * @return the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CardProdCodeModel getCardTypeProdIdCardProdCodeModel(){
      return getRelationCardTypeProdIdCardProdCodeModel();
   }

   /**
    * Sets the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    * @param cardTypeModel a value for <code>cardTypeProdIdCardProdCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCardTypeProdIdCardProdCodeModel(CardProdCodeModel cardTypeModel) {
      this.cardTypeProdIdCardProdCodeModel = cardTypeModel;
   }

   /**
    * Sets the value of the <code>cardTypeProdIdCardProdCodeModel</code> relation property.
    *
    * @param cardTypeModel a value for <code>cardTypeProdIdCardProdCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setCardTypeProdIdCardProdCodeModel(CardProdCodeModel cardTypeModel) {
      if(null != cardTypeModel)
      {
         setRelationCardTypeProdIdCardProdCodeModel((CardProdCodeModel) cardTypeModel.clone());
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
    * Returns the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    * @return the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_TYPE_ID")    
   public AccountTypeModel getRelationAccountTypeIdAccountTypeModel(){
      return accountTypeIdAccountTypeModel;
   }
    
   /**
    * Returns the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    * @return the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AccountTypeModel getAccountTypeIdAccountTypeModel(){
      return getRelationAccountTypeIdAccountTypeModel();
   }

   /**
    * Sets the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    * @param accountTypeModel a value for <code>accountTypeIdAccountTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAccountTypeIdAccountTypeModel(AccountTypeModel accountTypeModel) {
      this.accountTypeIdAccountTypeModel = accountTypeModel;
   }
   
   /**
    * Sets the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
    *
    * @param accountTypeModel a value for <code>accountTypeIdAccountTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAccountTypeIdAccountTypeModel(AccountTypeModel accountTypeModel) {
      if(null != accountTypeModel)
      {
      	setRelationAccountTypeIdAccountTypeModel((AccountTypeModel)accountTypeModel.clone());
      }      
   }
   


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addSmartMoneyAccountIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationSmartMoneyAccountIdSmartMoneyAccountModel(this);
      smartMoneyAccountIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeSmartMoneyAccountIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationSmartMoneyAccountIdSmartMoneyAccountModel(null);
      smartMoneyAccountIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the SmartMoneyAccountModel object.
    * These objects are in a bidirectional one-to-many relation by the SmartMoneyAccountId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSmartMoneyAccountIdSmartMoneyAccountModel")
   @JoinColumn(name = "SMART_MONEY_ACCOUNT_ID")
   public Collection<TransactionModel> getSmartMoneyAccountIdTransactionModelList() throws Exception {
   		return smartMoneyAccountIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the SmartMoneyAccountModel object.
    * These objects are in a bidirectional one-to-many relation by the SmartMoneyAccountId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setSmartMoneyAccountIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.smartMoneyAccountIdTransactionModelList = transactionModelList;
   }



   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRetailerContactId() {
      if (retailerContactIdRetailerContactModel != null) {
         return retailerContactIdRetailerContactModel.getRetailerContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
																																													    */
   
   @javax.persistence.Transient
   public void setRetailerContactId(Long retailerContactId) {
      if(retailerContactId == null)
      {      
      	retailerContactIdRetailerContactModel = null;
      }
      else
      {
        retailerContactIdRetailerContactModel = new RetailerContactModel();
      	retailerContactIdRetailerContactModel.setRetailerContactId(retailerContactId);
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
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributorContactId() {
      if (distributorContactIdDistributorContactModel != null) {
         return distributorContactIdDistributorContactModel.getDistributorContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
																																													    */
   
   @javax.persistence.Transient
   public void setDistributorContactId(Long distributorContactId) {
      if(distributorContactId == null)
      {      
      	distributorContactIdDistributorContactModel = null;
      }
      else
      {
        distributorContactIdDistributorContactModel = new DistributorContactModel();
      	distributorContactIdDistributorContactModel.setDistributorContactId(distributorContactId);
      }      
   }

   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCustomerId() {
      if (customerIdCustomerModel != null) {
         return customerIdCustomerModel.getCustomerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
							    * @spring.validator type="required"
																																							    */
   
   @javax.persistence.Transient
   public void setCustomerId(Long customerId) {
      if(customerId == null)
      {      
      	customerIdCustomerModel = null;
      }
      else
      {
        customerIdCustomerModel = new CustomerModel();
      	customerIdCustomerModel.setCustomerId(customerId);
      }      
   }

   /**
    * Returns the value of the <code>currencyCodeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCurrencyCodeId() {
      if (currencyCodeIdCurrencyCodeModel != null) {
         return currencyCodeIdCurrencyCodeModel.getCurrencyCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>currencyCodeId</code> property.
    *
    * @param currencyCodeId the value for the <code>currencyCodeId</code> property
																																													    */
   
   @javax.persistence.Transient
   public void setCurrencyCodeId(Long currencyCodeId) {
      if(currencyCodeId == null)
      {      
      	currencyCodeIdCurrencyCodeModel = null;
      }
      else
      {
        currencyCodeIdCurrencyCodeModel = new CurrencyCodeModel();
      	currencyCodeIdCurrencyCodeModel.setCurrencyCodeId(currencyCodeId);
      }      
   }

   /**
    * Returns the value of the <code>cardTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCardTypeId() {
      if (cardTypeIdCardTypeModel != null) {
         return cardTypeIdCardTypeModel.getCardTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>cardTypeId</code> property.
    *
    * @param cardTypeId the value for the <code>cardTypeId</code> property
																																													    */
   
   @javax.persistence.Transient
   public void setCardTypeId(Long cardTypeId) {
      if(cardTypeId == null)
      {      
      	cardTypeIdCardTypeModel = null;
      }
      else
      {
        cardTypeIdCardTypeModel = new CardTypeModel();
      	cardTypeIdCardTypeModel.setCardTypeId(cardTypeId);
      }      
   }



   @javax.persistence.Transient
   public Long getCardProdId() {
      if (cardTypeProdIdCardProdCodeModel != null) {
         return cardTypeProdIdCardProdCodeModel.getCardProductCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>cardTypeId</code> property.
    *
    * @param cardTypeId the value for the <code>cardTypeId</code> property
    */

   @javax.persistence.Transient
   public void setCardProdId(Long cardTypeId) {
      if(cardTypeId == null)
      {
         cardTypeProdIdCardProdCodeModel = null;
      }
      else
      {
         cardTypeProdIdCardProdCodeModel = new CardProdCodeModel();
         cardTypeProdIdCardProdCodeModel.setCardProductCodeId(cardTypeId);
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
    * Returns the value of the <code>accountTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAccountTypeId() {
      if (accountTypeIdAccountTypeModel != null) {
         return accountTypeIdAccountTypeModel.getAccountTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>accountTypeId</code> property.
    *
    * @param accountTypeId the value for the <code>accountTypeId</code> property
																																													    */
   
   @javax.persistence.Transient
   public void setAccountTypeId(Long accountTypeId) {
      if(accountTypeId == null)
      {      
      	accountTypeIdAccountTypeModel = null;
      }
      else
      {
        accountTypeIdAccountTypeModel = new AccountTypeModel();
      	accountTypeIdAccountTypeModel.setAccountTypeId(accountTypeId);
      }      
   }

//-------------------------------------------------------------------------------------------------------------
   
   /**
    * Returns the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    * @return the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "HANDLER_ID")    
   public HandlerModel getRelationHandlerIdHandlerModel(){
      return handlerIdHandlerModel;
   }
    
   /**
    * Returns the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    * @return the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public HandlerModel getHandlerIdHandlerModel(){
      return getRelationHandlerIdHandlerModel();
   }


   @javax.persistence.Transient
   public void setRelationHandlerIdHandlerModel(HandlerModel handlerModel) {
      this.handlerIdHandlerModel = handlerModel;
   }

   @javax.persistence.Transient
   public void setHandlerIdHandlerModel(HandlerModel handlerModel) {
      if(null != handlerModel)
      {
      	setRelationHandlerIdHandlerModel((HandlerModel)handlerModel.clone());
      }      
   }
 
   
   @javax.persistence.Transient
   public Long getHandlerId() {
      if (handlerIdHandlerModel != null) {
         return handlerIdHandlerModel.getHandlerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>handlerId</code> property.
    *
    * @param handlerId the value for the <code>handlerId</code> property
																																																																																	    */
   
   @javax.persistence.Transient
   public void setHandlerId(Long handlerId) {
      if(handlerId == null)
      {      
      	handlerIdHandlerModel = null;
      }
      else
      {
        handlerIdHandlerModel = new HandlerModel();
      	handlerIdHandlerModel.setHandlerId(handlerId);
      }      
   }

 //-------------------------------------------------------------------------------------------------------------
   
    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSmartMoneyAccountId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&smartMoneyAccountId=" + getSmartMoneyAccountId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "smartMoneyAccountId";
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
    	
    	associationModel.setClassName("RetailerContactModel");
    	associationModel.setPropertyName("relationRetailerContactIdRetailerContactModel");   		
   		associationModel.setValue(getRelationRetailerContactIdRetailerContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorContactModel");
    	associationModel.setPropertyName("relationDistributorContactIdDistributorContactModel");   		
   		associationModel.setValue(getRelationDistributorContactIdDistributorContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CustomerModel");
    	associationModel.setPropertyName("relationCustomerIdCustomerModel");   		
   		associationModel.setValue(getRelationCustomerIdCustomerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CurrencyCodeModel");
    	associationModel.setPropertyName("relationCurrencyCodeIdCurrencyCodeModel");   		
   		associationModel.setValue(getRelationCurrencyCodeIdCurrencyCodeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CardTypeModel");
    	associationModel.setPropertyName("relationCardTypeIdCardTypeModel");   		
   		associationModel.setValue(getRelationCardTypeIdCardTypeModel());
   		
   		associationModelList.add(associationModel);


       associationModel = new AssociationModel();

       associationModel.setClassName("CardProdCodeModel");
       associationModel.setPropertyName("relationCardTypeProdIdCardProdCodeModel");
       associationModel.setValue(getRelationCardTypeProdIdCardProdCodeModel());

       associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
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
    	
    	associationModel.setClassName("AccountTypeModel");
    	associationModel.setPropertyName("relationAccountTypeIdAccountTypeModel");   		
   		associationModel.setValue(getRelationAccountTypeIdAccountTypeModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();

   		associationModel.setClassName("HandlerModel");
   		associationModel.setPropertyName("relationHandlerIdHandlerModel");   		
   		associationModel.setValue(getRelationHandlerIdHandlerModel());

   		associationModelList.add(associationModel);
 			    	
    	return associationModelList;
    }    
    
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "WALK_IN_CUSTOMER_ID")     
    public WalkinCustomerModel getWalkinCustomerModel() {
 	   return walkinCustomerModel;
    }

    public void setWalkinCustomerModel(WalkinCustomerModel walkinCustomerModel) {
 	   this.walkinCustomerModel = walkinCustomerModel;
    }

   @Column(name = "AC_OPENING_METHOD_ID")
   public Long getAccountOpeningMethodId() {
      return accountOpeningMethodId;
   }

   public void setAccountOpeningMethodId(Long accountOpeningMethodId) {
      this.accountOpeningMethodId = accountOpeningMethodId;
   }

   @Column(name = "AC_OPENING_DEVICE_TYPE_ID")
   public Long getAccountOpeningDeviceTypeId() {
      return accountOpeningDeviceTypeId;
   }

   public void setAccountOpeningDeviceTypeId(Long accountOpeningDeviceTypeId) {
      this.accountOpeningDeviceTypeId = accountOpeningDeviceTypeId;
   }

   @Column(name = "STATUS_ID")
   public Long getStatusId() {
      return statusId;
   }

   public void setStatusId(Long statusId) {
      this.statusId = statusId;
   }

   @Column(name = "DORMANCY_SMS_SENT_ON")
   public Date getDormancySmsSentOn() {
      return dormancySmsSentOn;
   }

   public void setDormancySmsSentOn(Date dormancySmsSentOn) {
      this.dormancySmsSentOn = dormancySmsSentOn;
   }

   @Column(name = "DORMANCY_REMOVED_ON")
   public Date getDormancyRemovedOn() {
      return dormancyRemovedOn;
   }

   public void setDormancyRemovedOn(Date dormancyRemovedOn) {
      this.dormancyRemovedOn = dormancyRemovedOn;
   }

   @Column(name = "REGISTRATION_STATE_ID")
   public Long getRegistrationStateId() {
      return registrationStateId;
   }

   public void setRegistrationStateId(Long registrationStateId) {
      this.registrationStateId = registrationStateId;
   }

   @Column(name = "ACCOUNT_STATE_ID")
   public Long getAccountStateId() {
      return accountStateId;
   }

   public void setAccountStateId(Long accountStateId) {
      this.accountStateId = accountStateId;
   }

   @Column(name = "PREV_REG_STATE_ID")
   public Long getPreviousRegStateId() {
      return previousRegStateId;
   }

   public void setPreviousRegStateId(Long previousRegStateId) {
      this.previousRegStateId = previousRegStateId;
   }

   @Column(name = "IS_CLOSED_UNSETTELED")
   public Long getAccountClosedUnsetteled() {
      return isAccountClosedUnsetteled;
   }

   public void setAccountClosedUnsetteled(Long accountClosedUnsetteled) {
      this.isAccountClosedUnsetteled = accountClosedUnsetteled;
   }

   @Column(name = "IS_CLOSED_SETTELED")
   public Long getAccountClosedSetteled() {
      return isAccountClosedSetteled;
   }

   public void setAccountClosedSetteled(Long accountClosedSetteled) {
      this.isAccountClosedSetteled = accountClosedSetteled;
   }

   @Column(name="DEBIT_BLOCK_AMOUNT")
   public Double getDebitBlockAmount() {
      return debitBlockAmount;
   }

   public void setDebitBlockAmount(Double debitBlockAmount) {
      this.debitBlockAmount = debitBlockAmount;
   }

   @Column(name="IS_DEBIT_BLOCKED")
   public Boolean getIsDebitBlocked() {
      return isDebitBlocked;
   }

   public void setIsDebitBlocked(Boolean isDebitBlocked) {
      this.isDebitBlocked = isDebitBlocked;
   }

   @Column(name="IS_OPTASIA_DEBIT_BLOCKED")
   public Boolean getIsOptasiaDebitBlocked() {
      return isOptasiaDebitBlocked;
   }

   public void setIsOptasiaDebitBlocked(Boolean isOptasiaDebitBlocked) {
      this.isOptasiaDebitBlocked = isOptasiaDebitBlocked;
   }

   @Column(name="DEBIT_BLOCK_REASON")
   public String getDebitBlockReason() {
      return debitBlockReason;
   }

   public void setDebitBlockReason(String debitBlockReason) {
      this.debitBlockReason = debitBlockReason;
   }
}
