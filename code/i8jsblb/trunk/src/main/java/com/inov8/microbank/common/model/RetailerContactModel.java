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
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;



/**
 * The RetailerContactModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerContactModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "RETAILER_CONTACT_seq",sequenceName = "RETAILER_CONTACT_seq", allocationSize=1)
@Table(name = "RETAILER_CONTACT")
public class RetailerContactModel extends BasePersistableModel implements Serializable {
   private static final long serialVersionUID = -3541322687786137348L;

   private RetailerModel retailerIdRetailerModel;
   private NatureOfBusinessModel natureOfBusinessIdNatureOfBusinessModel;
   private AreaModel areaIdAreaModel;
   private DistributorLevelModel distributorLevelModel; 
   private ProductCatalogModel productCatalogModel; 
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private RetailerContactModel parentRetailerContactModel;
   private OlaCustomerAccountTypeModel	olaCustomerAccountTypeModel; //added by Turab
   private SalesHierarchyModel salesHierarchyModel;

   private Collection<AppUserModel> retailerContactIdAppUserModelList = new ArrayList<AppUserModel>();
   private Collection<RetailerContactAddressesModel> retailerContactIdRetailerContactAddressesModelList = new ArrayList<RetailerContactAddressesModel>();
   private Collection<SmartMoneyAccountModel> retailerContactIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<TransactionModel> toRetContactIdTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<TransactionModel> fromRetContactIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long retailerContactId;
   private Double balance;
   private Boolean head;
   private Boolean active;
   private Boolean smsToAgent;
   private Boolean smsToHandler;
   private String description;
   private String comments;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
   private String applicationNo;
   private String zongMsisdn;
   private Date accountOpeningDate;
   private String ntn;
   private String contactNo;
   private String landLineNo;
   private String mobileNo;
   private String operatorsNo;
   private String businessName;
   private String name;
   private String cnic;
   private Date cnicExpiry;
   private Boolean rso;
   private String regStateComments;
   private CustomerTypeModel customerTypeModel;
   private Boolean publicFigure;
   private FundSourceModel fundSourceModel;
   private String fundsSourceNarration;
   private TransactionModeModel transactionModeModel;
   private String otherTransactionMode;
   private AccountReasonModel accountReasonModel;
   private String nokName;
   private String nokRelationship;
   private String nokContactNo;
   private String nokMobile;
   private String nokComments;
   private BusinessTypeModel businessTypeModel;
   private String mobile1;
   private String mobile2;
   private String mobile3;
   private String mobile4;
   private String mobile5;
   private String mobile6;
   private String otherBankName;
   private String otherBankAddress;
   private String otherBankACNo;
   private String salesTaxRegNo;
   private String membershipNoTradeBody;
   private Date incorporationDate;
   private String secpRegNo;
   private String salary;
   private String businessIncome;
   private String otherIncome;
   private Boolean screeningPerformed;
   public String registrationPlace;
   private String initialAppFormNo;
   private AccountPurposeModel accountPurposeIdAccountPurposeModel;
   private Long currency;
   private Long acNature;
   private Date commencementDate;
   private Date secpRegDate;
   private String tradeBody;
   private BusinessNatureModel businessNatureIdBusinessNatureModel;
   private LocationTypeModel locationTypeIdLocationTypeModel;
   private LocationSizeModel locationSizeIdLocationSizeModel;
   private Long estSince;
   private Long nokIdType;
   private String nokIdNumber;
   private Boolean verisysDone;
   private TaxRegimeModel taxRegimeIdTaxRegimeModel;
   private Double fed;
   
   private Boolean bvsEnable;
   private Boolean isAgentWebEnabled;
   private GeoLocationModel geoLocationIdGeoLocationModel;

   private Boolean isAgentUssdEnabled;

   private Boolean isDebitCardFeeEnabled;


   /**
    * Default constructor.
    */
   public RetailerContactModel() {
   }   
   
   
   public RetailerContactModel(Long retailerContactId) {
	   
	   this.setRetailerContactId(retailerContactId);
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getRetailerContactId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setRetailerContactId(primaryKey);
    }

   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
      @Column(name = "RETAILER_CONTACT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RETAILER_CONTACT_seq")
   public Long getRetailerContactId() {
      return retailerContactId;
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
    *    
		    */

   public void setRetailerContactId(Long retailerContactId) {
      this.retailerContactId = retailerContactId;
   }

   /**
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE" , nullable = false )
   public Double getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalance(Double balance) {
      this.balance = balance;
   }

   /**
    * Returns the value of the <code>head</code> property.
    *
    */
      @Column(name = "IS_HEAD" , nullable = false )
   public Boolean getHead() {
      return head;
   }

   /**
    * Sets the value of the <code>head</code> property.
    *
    * @param head the value for the <code>head</code> property
    *    
		    */

   public void setHead(Boolean head) {
      this.head = head;
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
   
   @Column(name = "SMS_TO_AGENT")
   public Boolean getSmsToAgent() {
		return smsToAgent;
	}

	public void setSmsToAgent(Boolean smsToAgent) {
		this.smsToAgent = smsToAgent;
	}
	@Column(name = "SMS_TO_HANDLER")
	public Boolean getSmsToHandler() {
		return smsToHandler;
	}

	public void setSmsToHandler(Boolean smsToHandler) {
		this.smsToHandler = smsToHandler;
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
    * Returns the value of the <code>applicationNo</code> property.
    *
    */
      @Column(name = "APPLICATION_NO"  , length=50 )
   public String getApplicationNo() {
      return applicationNo;
   }

   /**
    * Sets the value of the <code>applicationNo</code> property.
    *
    * @param applicationNo the value for the <code>applicationNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setApplicationNo(String applicationNo) {
      this.applicationNo = applicationNo;
   }

   /**
    * Returns the value of the <code>zongMsisdn</code> property.
    *
    */
      @Column(name = "MSISDN"  , length=50 )
   public String getZongMsisdn() {
      return zongMsisdn;
   }

   /**
    * Sets the value of the <code>zongMsisdn</code> property.
    *
    * @param zongMsisdn the value for the <code>zongMsisdn</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setZongMsisdn(String zongMsisdn) {
      this.zongMsisdn = zongMsisdn;
   }

   /**
    * Returns the value of the <code>accountOpeningDate</code> property.
    *
    */
      @Column(name = "ACCOUNT_OPENING_DATE"  )
   public Date getAccountOpeningDate() {
      return accountOpeningDate;
   }

   /**
    * Sets the value of the <code>accountOpeningDate</code> property.
    *
    * @param accountOpeningDate the value for the <code>accountOpeningDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setAccountOpeningDate(Date accountOpeningDate) {
      this.accountOpeningDate = accountOpeningDate;
   }

   /**
    * Returns the value of the <code>ntn</code> property.
    *
    */
      @Column(name = "NTN"  , length=50 )
   public String getNtn() {
      return ntn;
   }

   /**
    * Sets the value of the <code>ntn</code> property.
    *
    * @param ntn the value for the <code>ntn</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setNtn(String ntn) {
      this.ntn = ntn;
   }

   /**
    * Returns the value of the <code>contactNo</code> property.
    *
    */
      @Column(name = "CONTACT_NO"  , length=50 )
   public String getContactNo() {
      return contactNo;
   }

   /**
    * Sets the value of the <code>contactNo</code> property.
    *
    * @param contactNo the value for the <code>contactNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setContactNo(String contactNo) {
      this.contactNo = contactNo;
   }

   /**
    * Returns the value of the <code>landLineNo</code> property.
    *
    */
      @Column(name = "LAND_LINE_NO"  , length=50 )
   public String getLandLineNo() {
      return landLineNo;
   }

   /**
    * Sets the value of the <code>landLineNo</code> property.
    *
    * @param landLineNo the value for the <code>landLineNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setLandLineNo(String landLineNo) {
      this.landLineNo = landLineNo;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO"  , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>operatorsNo</code> property.
    *
    */
      @Column(name = "OPERATORS_NO"  , length=50 )
   public String getOperatorsNo() {
      return operatorsNo;
   }

   /**
    * Sets the value of the <code>operatorsNo</code> property.
    *
    * @param operatorsNo the value for the <code>operatorsNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setOperatorsNo(String operatorsNo) {
      this.operatorsNo = operatorsNo;
   }

   /**
    * Returns the value of the <code>businessName</code> property.
    *
    */
      @Column(name = "BUSINESS_NAME"  , length=50 )
   public String getBusinessName() {
      return businessName;
   }

   /**
    * Sets the value of the <code>businessName</code> property.
    *
    * @param businessName the value for the <code>businessName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setBusinessName(String businessName) {
      this.businessName = businessName;
   }

   /**
    * Returns the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETAILER_ID")    
   public RetailerModel getRelationRetailerIdRetailerModel(){
      return retailerIdRetailerModel;
   }
    
   /**
    * Returns the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerModel getRetailerIdRetailerModel(){
      return getRelationRetailerIdRetailerModel();
   }

   /**
    * Sets the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerIdRetailerModel(RetailerModel retailerModel) {
      this.retailerIdRetailerModel = retailerModel;
   }
   
   /**
    * Sets the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerIdRetailerModel(RetailerModel retailerModel) {
      if(null != retailerModel)
      {
      	setRelationRetailerIdRetailerModel((RetailerModel)retailerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    * @return the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "NATURE_OF_BUSINESS_ID")    
   public NatureOfBusinessModel getRelationNatureOfBusinessIdNatureOfBusinessModel(){
      return natureOfBusinessIdNatureOfBusinessModel;
   }
    
   /**
    * Returns the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    * @return the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public NatureOfBusinessModel getNatureOfBusinessIdNatureOfBusinessModel(){
      return getRelationNatureOfBusinessIdNatureOfBusinessModel();
   }

   /**
    * Sets the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    * @param natureOfBusinessModel a value for <code>natureOfBusinessIdNatureOfBusinessModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationNatureOfBusinessIdNatureOfBusinessModel(NatureOfBusinessModel natureOfBusinessModel) {
      this.natureOfBusinessIdNatureOfBusinessModel = natureOfBusinessModel;
   }
   
   /**
    * Sets the value of the <code>natureOfBusinessIdNatureOfBusinessModel</code> relation property.
    *
    * @param natureOfBusinessModel a value for <code>natureOfBusinessIdNatureOfBusinessModel</code>.
    */
   @javax.persistence.Transient
   public void setNatureOfBusinessIdNatureOfBusinessModel(NatureOfBusinessModel natureOfBusinessModel) {
      if(null != natureOfBusinessModel)
      {
      	setRelationNatureOfBusinessIdNatureOfBusinessModel((NatureOfBusinessModel)natureOfBusinessModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>areaIdAreaModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "AREA_ID")    
   public AreaModel getRelationAreaIdAreaModel(){
      return areaIdAreaModel;
   }
    
   /**
    * Returns the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>areaIdAreaModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AreaModel getAreaIdAreaModel(){
      return getRelationAreaIdAreaModel();
   }

   /**
    * Sets the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>areaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAreaIdAreaModel(AreaModel areaModel) {
      this.areaIdAreaModel = areaModel;
   }
   
   /**
    * Sets the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>areaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setAreaIdAreaModel(AreaModel areaModel) {
      if(null != areaModel)
      {
      	setRelationAreaIdAreaModel((AreaModel)areaModel.clone());
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
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */
    
   public void addRetailerContactIdAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationRetailerContactIdRetailerContactModel(this);
      retailerContactIdAppUserModelList.add(appUserModel);
   }
   
   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */
   
   public void removeRetailerContactIdAppUserModel(AppUserModel appUserModel) {      
      appUserModel.setRelationRetailerContactIdRetailerContactModel(null);
      retailerContactIdAppUserModelList.remove(appUserModel);      
   }

   /**
    * Get a list of related AppUserModel objects of the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @return Collection of AppUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRetailerContactIdRetailerContactModel")
   @JoinColumn(name = "RETAILER_CONTACT_ID")
   public Collection<AppUserModel> getRetailerContactIdAppUserModelList() throws Exception {
   		return retailerContactIdAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @param appUserModelList the list of related objects.
    */
    public void setRetailerContactIdAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
		this.retailerContactIdAppUserModelList = appUserModelList;
   }


   /**
    * Add the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be added.
    */
    
   public void addRetailerContactIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {
      retailerContactAddressesModel.setRelationRetailerContactIdRetailerContactModel(this);
      retailerContactIdRetailerContactAddressesModelList.add(retailerContactAddressesModel);
   }
   
   /**
    * Remove the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be removed.
    */
   
   public void removeRetailerContactIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {      
      retailerContactAddressesModel.setRelationRetailerContactIdRetailerContactModel(null);
      retailerContactIdRetailerContactAddressesModelList.remove(retailerContactAddressesModel);      
   }

   /**
    * Get a list of related RetailerContactAddressesModel objects of the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @return Collection of RetailerContactAddressesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRetailerContactIdRetailerContactModel")
   @JoinColumn(name = "RETAILER_CONTACT_ID")
   public Collection<RetailerContactAddressesModel> getRetailerContactIdRetailerContactAddressesModelList() throws Exception {
   		return retailerContactIdRetailerContactAddressesModelList;
   }


   /**
    * Set a list of RetailerContactAddressesModel related objects to the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @param retailerContactAddressesModelList the list of related objects.
    */
    public void setRetailerContactIdRetailerContactAddressesModelList(Collection<RetailerContactAddressesModel> retailerContactAddressesModelList) throws Exception {
		this.retailerContactIdRetailerContactAddressesModelList = retailerContactAddressesModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addRetailerContactIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationRetailerContactIdRetailerContactModel(this);
      retailerContactIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removeRetailerContactIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationRetailerContactIdRetailerContactModel(null);
      retailerContactIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRetailerContactIdRetailerContactModel")
   @JoinColumn(name = "RETAILER_CONTACT_ID")
   public Collection<SmartMoneyAccountModel> getRetailerContactIdSmartMoneyAccountModelList() throws Exception {
   		return retailerContactIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerContactId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setRetailerContactIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.retailerContactIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addToRetContactIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationToRetContactIdRetailerContactModel(this);
      toRetContactIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeToRetContactIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationToRetContactIdRetailerContactModel(null);
      toRetContactIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ToRetContactId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationToRetContactIdRetailerContactModel")
   @JoinColumn(name = "TO_RET_CONTACT_ID")
   public Collection<TransactionModel> getToRetContactIdTransactionModelList() throws Exception {
   		return toRetContactIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ToRetContactId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setToRetContactIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.toRetContactIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addFromRetContactIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationFromRetContactIdRetailerContactModel(this);
      fromRetContactIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeFromRetContactIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationFromRetContactIdRetailerContactModel(null);
      fromRetContactIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the FromRetContactId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFromRetContactIdRetailerContactModel")
   @JoinColumn(name = "FROM_RET_CONTACT_ID")
   public Collection<TransactionModel> getFromRetContactIdTransactionModelList() throws Exception {
   		return fromRetContactIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the RetailerContactModel object.
    * These objects are in a bidirectional one-to-many relation by the FromRetContactId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setFromRetContactIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.fromRetContactIdTransactionModelList = transactionModelList;
   }



   /**
    * Returns the value of the <code>retailerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRetailerId() {
      if (retailerIdRetailerModel != null) {
         return retailerIdRetailerModel.getRetailerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerId</code> property.
    *
    * @param retailerId the value for the <code>retailerId</code> property
					    * @spring.validator type="required"
																																													    */
   
   @javax.persistence.Transient
   public void setRetailerId(Long retailerId) {
      if(retailerId == null)
      {      
      	retailerIdRetailerModel = null;
      }
      else
      {
        retailerIdRetailerModel = new RetailerModel();
      	retailerIdRetailerModel.setRetailerId(retailerId);
      }      
   }

   /**
    * Returns the value of the <code>natureOfBusinessId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getNatureOfBusinessId() {
      if (natureOfBusinessIdNatureOfBusinessModel != null) {
         return natureOfBusinessIdNatureOfBusinessModel.getNatureOfBusinessId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>natureOfBusinessId</code> property.
    *
    * @param natureOfBusinessId the value for the <code>natureOfBusinessId</code> property
																																																	    */
   
   @javax.persistence.Transient
   public void setNatureOfBusinessId(Long natureOfBusinessId) {
      if(natureOfBusinessId == null)
      {      
      	natureOfBusinessIdNatureOfBusinessModel = null;
      }
      else
      {
        natureOfBusinessIdNatureOfBusinessModel = new NatureOfBusinessModel();
      	natureOfBusinessIdNatureOfBusinessModel.setNatureOfBusinessId(natureOfBusinessId);
      }      
   }

   /**
    * Returns the value of the <code>areaId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAreaId() {
      if (areaIdAreaModel != null) {
         return areaIdAreaModel.getAreaId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
							    * @spring.validator type="required"
																																											    */
   
   @javax.persistence.Transient
   public void setAreaId(Long areaId) {
      if(areaId == null)
      {      
      	areaIdAreaModel = null;
      }
      else
      {
        areaIdAreaModel = new AreaModel();
      	areaIdAreaModel.setAreaId(areaId);
      }      
   }

   @javax.persistence.Transient
   public void setAreaName(String areaName) 
   {
      if(areaName != null)
      {
    	  if(areaIdAreaModel == null)
    	  {
    		  areaIdAreaModel = new AreaModel();
    		  areaIdAreaModel.setName(areaName);
    	  }
    	  else
    	  {
    		  areaIdAreaModel.setName(areaName);
    	  }
      }      
   }

   
   @javax.persistence.Transient
   public String getAreaName() {
      if (areaIdAreaModel != null) {
         return areaIdAreaModel.getName();
      } else {
         return null;
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
        checkBox += "_"+ getRetailerContactId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&retailerContactId=" + getRetailerContactId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "retailerContactId";
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
    	
    	associationModel.setClassName("RetailerModel");
    	associationModel.setPropertyName("relationRetailerIdRetailerModel");   		
   		associationModel.setValue(getRelationRetailerIdRetailerModel());
   		
   		associationModelList.add(associationModel);
   		
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("NatureOfBusinessModel");
    	associationModel.setPropertyName("relationNatureOfBusinessIdNatureOfBusinessModel");   		
   		associationModel.setValue(getRelationNatureOfBusinessIdNatureOfBusinessModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AreaModel");
    	associationModel.setPropertyName("relationAreaIdAreaModel");   		
   		associationModel.setValue(getRelationAreaIdAreaModel());
   		
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
   		
   	//added by turab
   		associationModel = new AssociationModel();
	    	
   		associationModel.setClassName("OlaCustomerAccountTypeModel");
   		associationModel.setPropertyName("relationOlaCustomerAccountTypeModel");   		
		associationModel.setValue(getRelationOlaCustomerAccountTypeModel());

		associationModelList.add(associationModel);
 	
		associationModel = new AssociationModel();
    	
   		associationModel.setClassName("CustomerTypeModel");
   		associationModel.setPropertyName("customerTypeModel");   		
		associationModel.setValue(getCustomerTypeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
    	
   		associationModel.setClassName("FundSourceModel");
   		associationModel.setPropertyName("fundSourceModel");
		associationModel.setValue(getFundSourceModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
    	
   		associationModel.setClassName("TransactionModeModel");
   		associationModel.setPropertyName("transactionModeModel");
		associationModel.setValue(getTransactionModeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
    	
   		associationModel.setClassName("AccountReasonModel");
   		associationModel.setPropertyName("accountReasonModel");
		associationModel.setValue(getAccountReasonModel());

		associationModelList.add(associationModel);
		
		//by atif hussain
		associationModel = new AssociationModel();
   		associationModel.setClassName("ProductCatalogModel");
   		associationModel.setPropertyName("relationProductCatalogModel");
		associationModel.setValue(getRelationProductCatalogModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("LocationTypeModel");
   		associationModel.setPropertyName("relationLocationTypeIdLocationTypeModel");
		associationModel.setValue(getRelationLocationTypeIdLocationTypeModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("LocationSizeModel");
   		associationModel.setPropertyName("relationLocationSizeIdLocationSizeModel");
		associationModel.setValue(getRelationLocationSizeIdLocationSizeModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("AccountPurposeModel");
   		associationModel.setPropertyName("relationAccountPurposeIdAccountPurposeModel");
		associationModel.setValue(getRelationAccountPurposeIdAccountPurposeModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("BusinessNatureModel");
   		associationModel.setPropertyName("relationBusinessNatureIdBusinessNatureModel");
		associationModel.setValue(getRelationBusinessNatureIdBusinessNatureModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("TaxRegimeModel");
   		associationModel.setPropertyName("relationTaxRegimeIdTaxRegimeModel");
		associationModel.setValue(getRelationTaxRegimeIdTaxRegimeModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		associationModel.setClassName("GeoLocationModel");
   		associationModel.setPropertyName("relationGeoLocationIdGeoLocationModel");
		associationModel.setValue(getRelationGeoLocationIdGeoLocationModel());
		associationModelList.add(associationModel);
		
    	return associationModelList;
    }
    
    @Column(name = "AGENT_NAME" , length=50 )
    public String getName() {
       return name;
    }

    public void setName(String name) {
       this.name = name;
    }

    @Column(name = "CNIC" , length=50 )
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Column(name = "CNIC_EXPIRY_DATE")
	public Date getCnicExpiry() {
		return cnicExpiry;
	}

	public void setCnicExpiry(Date cnicExpiry) {
		this.cnicExpiry = cnicExpiry;
	}

	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_LEVEL_ID")    
	public DistributorLevelModel getRelationDistributorLevelModel(){
		return distributorLevelModel;
	}
	
	@javax.persistence.Transient
	public DistributorLevelModel getDistributorLevelModel(){
		return getRelationDistributorLevelModel();
	}

	   
	@javax.persistence.Transient
	public void setRelationDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
		this.distributorLevelModel = distributorLevelModel;
	}
	   
	
	@javax.persistence.Transient
	public void setDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
		if(null != distributorLevelModel)
	    {
			setRelationDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
	    }      
	}
	   
	@javax.persistence.Transient
	public Long getDistributorLevelId() {
		if (distributorLevelModel != null) {
			return distributorLevelModel.getDistributorLevelId();
	    } else {
	    	return null;
	    }
	}																																												    
	   
	@javax.persistence.Transient
	public void setDistributorLevelId(Long distributorLevelId) {
		if(distributorLevelId == null)
	    {      
			distributorLevelModel = null;
	    }
	    else
	    {
	    	distributorLevelModel = new DistributorLevelModel();
	    	distributorLevelModel.setDistributorLevelId(distributorLevelId);
	    }      
	}
	
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "PRODUCT_CATALOGUE_ID")    
	   public ProductCatalogModel getRelationProductCatalogModel(){
	      return productCatalogModel;
	   }
	    
	   
	   @javax.persistence.Transient
	   public ProductCatalogModel getProductCatalogModel(){
	      return getRelationProductCatalogModel();
	   }

	   
	   @javax.persistence.Transient
	   public void setRelationProductCatalogModel(ProductCatalogModel productCatalogModel) {
	      this.productCatalogModel = productCatalogModel;
	   }
	   
	   @javax.persistence.Transient
	   public void setProductCatalogModel(ProductCatalogModel productCatalogModel) {
	      if(null != productCatalogModel)
	      {
	      	setRelationProductCatalogModel((ProductCatalogModel)productCatalogModel.clone());
	      }      
	   }
	   
	   @javax.persistence.Transient
	   public Long getProductCatalogId() {
	      if (productCatalogModel != null) {
	         return productCatalogModel.getProductCatalogId();
	      } else {
	         return null;
	      }
	   }

	   
	   @javax.persistence.Transient
	   public void setProductCatalogId(Long productCatalogId) {
	      if(productCatalogId == null)
	      {      
	      	productCatalogModel = null;
	      }
	      else
	      {
	        productCatalogModel = new ProductCatalogModel();
	      	productCatalogModel.setProductCatalogId(productCatalogId);
	      }      
	   }
	   
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "PARENT_RETAILER_CONTACT_ID")    
	   public RetailerContactModel getParentRetailerContactModel()
	   {
	       return this.parentRetailerContactModel;
	   }

	   
	   public void setParentRetailerContactModel(RetailerContactModel parentRetailerContactModel)
	   {
	       this.parentRetailerContactModel = parentRetailerContactModel;
	   }
	  	
	   
	   @javax.persistence.Transient
	   public Long getParentRetailerContactModelId() {
	      if (parentRetailerContactModel != null) {
	         return parentRetailerContactModel.getRetailerContactId();
	      } else {
	         return null;
	      }
	   }
	    
	   
	   @javax.persistence.Transient
		public void setParentRetailerContactModelId(Long parentRetailerContactModelId) 
		{
			if(parentRetailerContactModelId == null)
			{      
				this.parentRetailerContactModel = null;
			}
			else
			{
				parentRetailerContactModel = new RetailerContactModel();
				parentRetailerContactModel.setRetailerContactId(parentRetailerContactModelId);
			}      
	  }
	   
	   /**
	    * @return the value of <code>rso</code> property
	    */
	   @Column(name = "IS_RSO")
	   public Boolean getRso() {
	      return rso;
	   }

	   /**
	   * Sets the value of the <code>rso</code> property.
	   * @param rso the value for the <code>rso</code> property
	   *    
       */
	   public void setRso(Boolean rso) {
	      this.rso = rso;
	   }

    @Column(name = "REGISTRATION_STATE_COMMENTS")
	public String getRegStateComments() {
		return regStateComments;
	}

    public void setRegStateComments(String regStateComments) {
		this.regStateComments = regStateComments;
	}

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_TYPE_ID")
    public CustomerTypeModel getCustomerTypeModel() {
		return customerTypeModel;
	}

	public void setCustomerTypeModel(CustomerTypeModel customerTypeModel) {
		this.customerTypeModel = customerTypeModel;
	}

	@Transient
	public Long getCustomerTypeId()
	{
		return customerTypeModel == null ? null : customerTypeModel.getCustomerTypeId();
	}

	public void setCustomerTypeId(Long customerTypeId)
	{
		if(customerTypeId == null)
		{
			customerTypeModel = null;
		}
		else
		{
			customerTypeModel = new CustomerTypeModel(customerTypeId);
		}
	}

	@Column(name = "PUBLIC_FIGURE")
	public Boolean getPublicFigure() {
		return publicFigure;
	}

	public void setPublicFigure(Boolean publicFigure) {
		this.publicFigure = publicFigure;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "FUND_SOURCE_ID")
	public FundSourceModel getFundSourceModel() {
		return fundSourceModel;
	}

	public void setFundSourceModel(FundSourceModel fundSourceModel) {
		this.fundSourceModel = fundSourceModel;
	}

	@Transient
	public Long getFundSourceId()
	{
		return fundSourceModel == null ? null : fundSourceModel.getFundSourceId();
	}

	public void setFundSourceId(Long fundSourceId)
	{
		if(fundSourceId == null)
		{
			fundSourceModel = null;
		}
		else
		{
			fundSourceModel = new FundSourceModel(fundSourceId);
		}
	}

	@Column(name = "FUNDS_SOURCE_NARRATION")
	public String getFundsSourceNarration() {
		return fundsSourceNarration;
	}

	public void setFundsSourceNarration(String fundsSourceNarration) {
		this.fundsSourceNarration = fundsSourceNarration;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_MODE_ID")
	public TransactionModeModel getTransactionModeModel() {
		return transactionModeModel;
	}

	public void setTransactionModeModel(TransactionModeModel transactionModeModel) {
		this.transactionModeModel = transactionModeModel;
	}

	@Transient
	public Long getTransactionModeId()
	{
		return transactionModeModel == null ? null : transactionModeModel.getTransactionModeId();
	}

	public void setTransactionModeId(Long transactionModeId)
	{
		if(transactionModeId == null)
		{
			transactionModeModel = null;
		}
		else
		{
			transactionModeModel = new TransactionModeModel(transactionModeId);
		}
	}

	@Column(name = "OTHER_TX_MODE")
	public String getOtherTransactionMode() {
		return otherTransactionMode;
	}

	public void setOtherTransactionMode(String otherTransactionMode) {
		this.otherTransactionMode = otherTransactionMode;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_REASON_ID")
	public AccountReasonModel getAccountReasonModel() {
		return accountReasonModel;
	}

	public void setAccountReasonModel(AccountReasonModel accountReasonModel) {
		this.accountReasonModel = accountReasonModel;
	}

	@Transient
	public Long getAccountReasonId()
	{
		return accountReasonModel == null ? null : accountReasonModel.getAccountReasonId();
	}

	public void setAccountReasonId(Long accountReasonId)
	{
		if(accountReasonId == null)
		{
			accountReasonModel = null;
		}
		else
		{
			accountReasonModel = new AccountReasonModel(accountReasonId);
		}
	}

	@Column(name = "NOK_NAME")
	public String getNokName() {
		return nokName;
	}

	public void setNokName(String nokName) {
		this.nokName = nokName;
	}

	@Column(name = "NOK_RELATIONSHIP")
	public String getNokRelationship() {
		return nokRelationship;
	}

	public void setNokRelationship(String nokRelationship) {
		this.nokRelationship = nokRelationship;
	}

	@Column(name = "NOK_CONTACT_NO")
	public String getNokContactNo() {
		return nokContactNo;
	}

	public void setNokContactNo(String nokContactNo) {
		this.nokContactNo = nokContactNo;
	}

	@Column(name = "NOK_MOBILE")
	public String getNokMobile() {
		return nokMobile;
	}

	public void setNokMobile(String nokMobile) {
		this.nokMobile = nokMobile;
	}

	@Column(name = "NOK_COMMENTS")
	public String getNokComments() {
		return nokComments;
	}

	public void setNokComments(String nokComments) {
		this.nokComments = nokComments;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_TYPE_ID")
	public BusinessTypeModel getBusinessTypeModel()
	{
		return businessTypeModel;
	}

	public void setBusinessTypeModel(BusinessTypeModel businessTypeModel)
	{
		this.businessTypeModel = businessTypeModel;
	}

	@Transient
	public Long getBusinessTypeId()
	{
		if(businessTypeModel == null)
		{
			return null;
		}
		return businessTypeModel.getBusinessTypeId();
	}

	public void setBusinessTypeId(Long businessTypeId)
	{
		if(businessTypeId == null)
		{
			this.businessTypeModel = null;
		}
		else
		{
			businessTypeModel = new BusinessTypeModel();
			businessTypeModel.setBusinessTypeId(businessTypeId);
		}
	}

		@Column(name = "MOBILE_1")
		public String getMobile1() {
			return mobile1;
		}

		public void setMobile1(String mobile1) {
			this.mobile1 = mobile1;
		}

		@Column(name = "MOBILE_2")
		public String getMobile2() {
			return mobile2;
		}

		public void setMobile2(String mobile2) {
			this.mobile2 = mobile2;
		}

		@Column(name = "MOBILE_3")
		public String getMobile3() {
			return mobile3;
		}

		public void setMobile3(String mobile3) {
			this.mobile3 = mobile3;
		}

		@Column(name = "MOBILE_4")
		public String getMobile4() {
			return mobile4;
		}

		public void setMobile4(String mobile4) {
			this.mobile4 = mobile4;
		}

		@Column(name = "MOBILE_5")
		public String getMobile5() {
			return mobile5;
		}

		public void setMobile5(String mobile5) {
			this.mobile5 = mobile5;
		}

		@Column(name = "MOBILE_6")
		public String getMobile6() {
			return mobile6;
		}

		public void setMobile6(String mobile6) {
			this.mobile6 = mobile6;
		}

		@Column(name = "OTHER_BANK_NAME")
		public String getOtherBankName() {
			return otherBankName;
		}

		public void setOtherBankName(String otherBankName) {
			this.otherBankName = otherBankName;
		}

		@Column(name = "OTHER_BANK_ADDRESS")
		public String getOtherBankAddress() {
			return otherBankAddress;
		}

		public void setOtherBankAddress(String otherBankAddress) {
			this.otherBankAddress = otherBankAddress;
		}

		@Column(name = "OTHER_BANK_AC")
		public String getOtherBankACNo() {
			return otherBankACNo;
		}

		public void setOtherBankACNo(String otherBankACNo) {
			this.otherBankACNo = otherBankACNo;
		}

		@Column(name = "SALES_TAX_REG_NO")
		public String getSalesTaxRegNo() {
			return salesTaxRegNo;
		}

		public void setSalesTaxRegNo(String salesTaxRegNo) {
			this.salesTaxRegNo = salesTaxRegNo;
		}

		@Column(name = "MEM_NO_TRADE_BODY")
		public String getMembershipNoTradeBody() {
			return membershipNoTradeBody;
		}

		public void setMembershipNoTradeBody(String membershipNoTradeBody) {
			this.membershipNoTradeBody = membershipNoTradeBody;
		}

		@Column(name = "INC_DATE")
		public Date getIncorporationDate() {
			return incorporationDate;
		}

		public void setIncorporationDate(Date incorporationDate) {
			this.incorporationDate = incorporationDate;
		}

		@Column(name = "SECP_REG_NO")
		public String getSecpRegNo() {
			return secpRegNo;
		}

		public void setSecpRegNo(String secpRegNo) {
			this.secpRegNo = secpRegNo;
		}

		@Column(name = "SALARY")
		public String getSalary() {
			return salary;
		}

		public void setSalary(String salary) {
			this.salary = salary;
		}

		@Column(name = "BUSINESS_INCOME")
		public String getBusinessIncome() {
			return businessIncome;
		}

		public void setBusinessIncome(String businessIncome) {
			this.businessIncome = businessIncome;
		}

		@Column(name = "OTHER_INCOME")
		public String getOtherIncome() {
			return otherIncome;
		}

		public void setOtherIncome(String otherIncome) {
			this.otherIncome = otherIncome;
		}

	   //added by Turab
	   @javax.persistence.Transient
	   public OlaCustomerAccountTypeModel getOlaCustomerAccountTypeModel() {
			return olaCustomerAccountTypeModel;
		}
	   
	   	@javax.persistence.Transient
		public void setOlaCustomerAccountTypeModel(
				OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
			this.olaCustomerAccountTypeModel = olaCustomerAccountTypeModel;
		}
		
		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		@JoinColumn(name = "ACCOUNT_TYPE_ID")    
		public OlaCustomerAccountTypeModel getRelationOlaCustomerAccountTypeModel() {
			return olaCustomerAccountTypeModel;
		}
		
		@javax.persistence.Transient
		public void setRelationOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
			this.olaCustomerAccountTypeModel = olaCustomerAccountTypeModel;
		} 
		
		@javax.persistence.Transient
		public Long getOlaCustomerAccountTypeModelId() {
			if (olaCustomerAccountTypeModel != null){
				return olaCustomerAccountTypeModel.getCustomerAccountTypeId();
			}else{
				return null;
			}
		}
		   
		@javax.persistence.Transient
		public void setOlaCustomerAccountTypeModelId(
					Long olaCustomerAccountTypeModelId) {
			if(olaCustomerAccountTypeModelId == null)
		    {      
				olaCustomerAccountTypeModel = null;
		    }
		    else
		    {
		    	olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		    	olaCustomerAccountTypeModel.setCustomerAccountTypeId(olaCustomerAccountTypeModelId);
		    }
			}
		
		
		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn( name = "SALES_HIERARCHY_ID" )
	   public SalesHierarchyModel getSalesHierarchyModel()
	   {
	       return this.salesHierarchyModel;
	   }

	   
	   public void setSalesHierarchyModel( SalesHierarchyModel salesHierarchyModel)
	   {
	       this.salesHierarchyModel = salesHierarchyModel;
	   }
	  	
	   @javax.persistence.Transient
	   public Long getSalesHierarchyId() {
	      if (salesHierarchyModel != null) {
	         return salesHierarchyModel.getSalesHierarchyId();
	      } else {
	         return null;
	      }
	   }
	   
	   @javax.persistence.Transient
		public void setSalesHierarchyId(Long salesHierarchyId) 
		{
			if(salesHierarchyId == null)
			{      
				this.salesHierarchyModel = null;
			}
			else
			{
				salesHierarchyModel = new SalesHierarchyModel();
				salesHierarchyModel.setSalesHierarchyId(salesHierarchyId);
			}      
	  }
	   
	   @Column(name = "SCREENING_PERFORMED")
		public Boolean isScreeningPerformed() {
			return screeningPerformed;
		}

		public void setScreeningPerformed(Boolean screeningPerformed) {
			this.screeningPerformed = screeningPerformed;
		}
		
		@Column(name = "REGISTRATION_PLACE")
		public String getRegistrationPlace() {
			return registrationPlace;
		}
		public void setRegistrationPlace(String registrationPlace) {
			this.registrationPlace = registrationPlace;
		}

		@Column(name = "INITIAL_APP_FORM_NUMBER")
		public String getInitialAppFormNo() {
			return initialAppFormNo;
		}

		public void setInitialAppFormNo(String initialAppFormNo) {
			if (initialAppFormNo != null) {
				this.initialAppFormNo = initialAppFormNo;
			}
		}

		@Column(name = "CURRENCY")
		public Long getCurrency() {
			return currency;
		}

		public void setCurrency(Long currency) {
			if (currency != null) {
				this.currency = currency;
			}
		}

		@Column(name = "AC_NATURE")
		public Long getAcNature() {
			return acNature;
		}

		public void setAcNature(Long acNature) {
			if (acNature != null) {
				this.acNature = acNature;
			}
		}

		@Column(name = "COMMENCEMENT_DATE")
		public Date getCommencementDate() {
			return commencementDate;
		}

		public void setCommencementDate(Date commencementDate) {
			if (commencementDate != null) {
				this.commencementDate = commencementDate;
			}
		}

		@Column(name = "SECP_REG_DATE")
		public Date getSecpRegDate() {
			return secpRegDate;
		}

		public void setSecpRegDate(Date secpRegDate) {
			if (secpRegDate != null) {
				this.secpRegDate = secpRegDate;
			}
		}

		@Column(name = "TRADE_BODY")
		public String getTradeBody() {
			return tradeBody;
		}

		public void setTradeBody(String tradeBody) {
			if (tradeBody != null) {
				this.tradeBody = tradeBody;
			}
		}

		@Column(name = "EST_SINCE")
		public Long getEstSince() {
			return estSince;
		}

		public void setEstSince(Long estSince) {
			if (estSince != null) {
				this.estSince = estSince;
			}
		}

		@Column(name = "NOK_ID_TYPE")
		public Long getNokIdType() {
			return nokIdType;
		}

		public void setNokIdType(Long nokIdType) {
				this.nokIdType = nokIdType;
		}

		@Column(name = "NOK_ID_NO")
		public String getNokIdNumber() {
			return nokIdNumber;
		}

		public void setNokIdNumber(String nokIdNumber) {
				this.nokIdNumber = nokIdNumber;
		}

		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		@JoinColumn(name = "LOCATION_TYPE_ID")
		public LocationTypeModel getRelationLocationTypeIdLocationTypeModel() {
			return this.locationTypeIdLocationTypeModel;
		}

		@javax.persistence.Transient
		public LocationTypeModel getLocationTypeIdLocationTypeModel() {
			return this.getRelationLocationTypeIdLocationTypeModel();
		}


		@javax.persistence.Transient
		public void setRelationLocationTypeIdLocationTypeModel(LocationTypeModel locationTypeModel) {
			this.locationTypeIdLocationTypeModel = locationTypeModel;
		}

		@javax.persistence.Transient
		public void setLocationTypeIdLocationTypeModel(LocationTypeModel locationTypeModel) {
			if (null != locationTypeModel) {
				this.setRelationLocationTypeIdLocationTypeModel((LocationTypeModel) locationTypeModel
						.clone());
			}
		}	
			
		@javax.persistence.Transient
		public Long getLocationTypeId() {
			if (this.locationTypeIdLocationTypeModel != null) {
				return this.locationTypeIdLocationTypeModel.getLocationTypeId();
			} else {
				return null;
			}
		}

		@javax.persistence.Transient
		public void setLocationTypeId(Long locationTypeId) {
			if (locationTypeId == null) {
				this.locationTypeIdLocationTypeModel = null;
			} else {
				this.locationTypeIdLocationTypeModel = new LocationTypeModel();
				this.locationTypeIdLocationTypeModel.setLocationTypeId(locationTypeId);
			}
		}

		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		@JoinColumn(name = "LOCATION_SIZE_ID")
		public LocationSizeModel getRelationLocationSizeIdLocationSizeModel() {
			return this.locationSizeIdLocationSizeModel;
		}

		@javax.persistence.Transient
		public LocationSizeModel getLocationSizeIdLocationSizeModel() {
			return this.getRelationLocationSizeIdLocationSizeModel();
		}


		@javax.persistence.Transient
		public void setRelationLocationSizeIdLocationSizeModel(LocationSizeModel locationSizeModel) {
			this.locationSizeIdLocationSizeModel = locationSizeModel;
		}

		@javax.persistence.Transient
		public void setLocationSizeIdLocationSizeModel(LocationSizeModel locationSizeModel) {
			if (null != locationSizeModel) {
				this.setRelationLocationSizeIdLocationSizeModel((LocationSizeModel) locationSizeModel
						.clone());
			}
		}	
			
		@javax.persistence.Transient
		public Long getLocationSizeId() {
			if (this.locationSizeIdLocationSizeModel != null) {
				return this.locationSizeIdLocationSizeModel.getLocationSizeId();
			} else {
				return null;
			}
		}

		@javax.persistence.Transient
		public void setLocationSizeId(Long locationSizeId) {
			if (locationSizeId == null) {
				this.locationSizeIdLocationSizeModel = null;
			} else {
				this.locationSizeIdLocationSizeModel = new LocationSizeModel();
				this.locationSizeIdLocationSizeModel.setLocationSizeId(locationSizeId);
			}
		}

		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		@JoinColumn(name = "ACCOUNT_PURPOSE_ID")
		public AccountPurposeModel getRelationAccountPurposeIdAccountPurposeModel() {
			return this.accountPurposeIdAccountPurposeModel;
		}

		@javax.persistence.Transient
		public AccountPurposeModel getAccountPurposeIdAccountPurposeModel() {
			return this.getRelationAccountPurposeIdAccountPurposeModel();
		}


		@javax.persistence.Transient
		public void setRelationAccountPurposeIdAccountPurposeModel(AccountPurposeModel accountPurposeModel) {
			this.accountPurposeIdAccountPurposeModel = accountPurposeModel;
		}

		@javax.persistence.Transient
		public void setAccountPurposeIdAccountPurposeModel(AccountPurposeModel accountPurposeModel) {
			if (null != accountPurposeModel) {
				this.setRelationAccountPurposeIdAccountPurposeModel((AccountPurposeModel) accountPurposeModel
						.clone());
			}
		}	
			
		@javax.persistence.Transient
		public Long getAccountPurposeId() {
			if (this.accountPurposeIdAccountPurposeModel != null) {
				return this.accountPurposeIdAccountPurposeModel.getAccountPurposeId();
			} else {
				return null;
			}
		}

		@javax.persistence.Transient
		public void setAccountPurposeId(Long accountPurposeId) {
			if (accountPurposeId == null) {
				this.accountPurposeIdAccountPurposeModel = null;
			} else {
				this.accountPurposeIdAccountPurposeModel = new AccountPurposeModel();
				this.accountPurposeIdAccountPurposeModel.setAccountPurposeId(accountPurposeId);
			}
		}

		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		@JoinColumn(name = "BUSINESS_NATURE_ID")
		public BusinessNatureModel getRelationBusinessNatureIdBusinessNatureModel() {
			return this.businessNatureIdBusinessNatureModel;
		}

		@javax.persistence.Transient
		public BusinessNatureModel getBusinessNatureIdBusinessNatureModel() {
			return this.getRelationBusinessNatureIdBusinessNatureModel();
		}


		@javax.persistence.Transient
		public void setRelationBusinessNatureIdBusinessNatureModel(BusinessNatureModel businessNatureModel) {
			this.businessNatureIdBusinessNatureModel = businessNatureModel;
		}

		@javax.persistence.Transient
		public void setBusinessNatureIdBusinessNatureModel(BusinessNatureModel businessNatureModel) {
			if (null != businessNatureModel) {
				this.setRelationBusinessNatureIdBusinessNatureModel((BusinessNatureModel) businessNatureModel
						.clone());
			}
		}	
			
		@javax.persistence.Transient
		public Long getBusinessNatureId() {
			if (this.businessNatureIdBusinessNatureModel != null) {
				return this.businessNatureIdBusinessNatureModel.getBusinessNatureId();
			} else {
				return null;
			}
		}

		@javax.persistence.Transient
		public void setBusinessNatureId(Long businessNatureId) {
			if (businessNatureId == null) {
				this.businessNatureIdBusinessNatureModel = null;
			} else {
				this.businessNatureIdBusinessNatureModel = new BusinessNatureModel();
				this.businessNatureIdBusinessNatureModel.setBusinessNatureId(businessNatureId);
			}
		}

		@Column(name = "VERISYS")
		public Boolean getVerisysDone() {
			return verisysDone;
		}

		public void setVerisysDone(Boolean verisysDone) {
			if (verisysDone != null) {
				this.verisysDone = verisysDone;
			}
		}
		
		@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
		   @JoinColumn(name = "TAX_REGIME_ID")    
		   public TaxRegimeModel getRelationTaxRegimeIdTaxRegimeModel(){
		      return taxRegimeIdTaxRegimeModel;
		   }
		    
		   @javax.persistence.Transient
		   public TaxRegimeModel getTaxRegimeIdTaxRegimeModel(){
		      return getRelationTaxRegimeIdTaxRegimeModel();
		   }

		   @javax.persistence.Transient
		   public void setRelationTaxRegimeIdTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
		      this.taxRegimeIdTaxRegimeModel = taxRegimeModel;
		   }
		   
		   @javax.persistence.Transient
		   public void setTaxRegimeIdTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
		      if(null != taxRegimeModel)
		      {
		      	setRelationTaxRegimeIdTaxRegimeModel((TaxRegimeModel)taxRegimeModel.clone());
		      }      
		   }
		   
		   @javax.persistence.Transient
		   public Long getTaxRegimeId() {
		      if (taxRegimeIdTaxRegimeModel != null) {
		         return taxRegimeIdTaxRegimeModel.getTaxRegimeId();
		      } else {
		         return null;
		      }
		   }
		   
		   @javax.persistence.Transient
		   public void setTaxRegimeId(Long taxRegimeId) {
		      if(taxRegimeId == null)
		      {      
		      	taxRegimeIdTaxRegimeModel = null;
		      }
		      else
		      {
		        taxRegimeIdTaxRegimeModel = new TaxRegimeModel();
		      	taxRegimeIdTaxRegimeModel.setTaxRegimeId(taxRegimeId);
		      }      
		   }
		   
			@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
			   @JoinColumn(name = "GEO_LOCATION_ID")    
			   public GeoLocationModel getRelationGeoLocationIdGeoLocationModel(){
			      return geoLocationIdGeoLocationModel;
			   }
			    
			   @javax.persistence.Transient
			   public GeoLocationModel getGeoLocationIdGeoLocationModel(){
			      return getRelationGeoLocationIdGeoLocationModel();
			   }

			   @javax.persistence.Transient
			   public void setRelationGeoLocationIdGeoLocationModel(GeoLocationModel geoLocationModel) {
			      this.geoLocationIdGeoLocationModel = geoLocationModel;
			   }
			   
			   @javax.persistence.Transient
			   public void setGeoLocationIdGeoLocationModel(GeoLocationModel geoLocationModel) {
			      if(null != geoLocationModel)
			      {
			    	  setRelationGeoLocationIdGeoLocationModel((GeoLocationModel)geoLocationModel.clone());
			      }      
			   }
			   
			   @javax.persistence.Transient
			   public Long getGeoLocationId() {
			      if (geoLocationIdGeoLocationModel != null) {
			         return geoLocationIdGeoLocationModel.getGeoLocationId();
			      } else {
			         return null;
			      }
			   }
			   
			   @javax.persistence.Transient
			   public void setGeoLocationId(Long geoLocationId) {
			      if(geoLocationId == null)
			      {      
			    	  geoLocationIdGeoLocationModel = null;
			      }
			      else
			      {
			    	  geoLocationIdGeoLocationModel = new GeoLocationModel();
			    	  geoLocationIdGeoLocationModel.setGeoLocationId(geoLocationId);
			      }      
			   }
		   
			@Column(name = "FED")
			public Double getFed() {
				return fed;
			}

			public void setFed(Double fed) {
				if (fed != null) {
					this.fed = fed;
				}
			}

	   @Column(name="BVS_ENABLE")
	   public Boolean getBvsEnable() {
	      return bvsEnable;
	   }

	   public void setBvsEnable(Boolean bvsEnable) {
	      this.bvsEnable = bvsEnable;
	   }

	   @Column(name="IS_AGENT_WEB_ENABLE")
		public Boolean getIsAgentWebEnabled() {
			return isAgentWebEnabled;
		}
	
	
		public void setIsAgentWebEnabled(Boolean isAgentWebEnabled) {
			this.isAgentWebEnabled = isAgentWebEnabled;
		}
		//DEbit Card FEE enable
   @Column(name="IS_DEBIT_CARD_FEE_ENABLE")
   public Boolean getIsDebitCardFeeEnabled() {
      return isDebitCardFeeEnabled;
   }


   public void setIsDebitCardFeeEnabled(Boolean isDebitCardFeeEnabled) {
      this.isDebitCardFeeEnabled = isDebitCardFeeEnabled;
   }


   @Column(name="IS_AGENT_USSD_ENABLE")
   public Boolean getAgentUssdEnabled() {
      return isAgentUssdEnabled;
   }

   public void setAgentUssdEnabled(Boolean agentUssdEnabled) {
      isAgentUssdEnabled = agentUssdEnabled;
   }
}