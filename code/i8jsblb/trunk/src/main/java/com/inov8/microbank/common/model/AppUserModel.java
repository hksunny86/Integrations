package com.inov8.microbank.common.model;

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

import com.inov8.microbank.mobilenetworks.model.MobileNetworkModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.ola.util.StringUtil;

/**
 * The AppUserModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppUserModel"
 */
@XmlRootElement(name="actionAuthorizationModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_USER_seq",sequenceName = "APP_USER_seq", allocationSize=1)
@Table(name = "APP_USER")
public class AppUserModel extends BasePersistableModel implements UserDetails {

   private static final long serialVersionUID = 3832626162173359411L;
   private SupplierUserModel supplierUserIdSupplierUserModel;
   private RetailerContactModel retailerContactIdRetailerContactModel;
   private HandlerModel handlerIdHandlerModel;
   private OperatorUserModel operatorUserIdOperatorUserModel;
   private MobileTypeModel mobileTypeIdMobileTypeModel;
   private MnoUserModel mnoUserIdMnoUserModel;
   private DistributorContactModel distributorContactIdDistributorContactModel;
   private CustomerModel customerIdCustomerModel;
   private WalkinCustomerModel walkinCustomerModel;
   private BankUserModel bankUserIdBankUserModel;
   private AppUserTypeModel appUserTypeIdAppUserTypeModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel closedByAppUserModel;
   private AppUserModel settledByAppUserModel;
   private RegistrationStateModel registrationStateModel;
   private RegistrationStateModel prevRegistrationStateModel;
   private MnoModel  mnoModelIdMnoModel;
   private CountryModel countryIdCountryModel;
   private MobileNetworkModel mobileNetworkModel;

   private Collection<ActionModel> createdByActionModelList = new ArrayList<ActionModel>();
   private Collection<ActionModel> updatedByActionModelList = new ArrayList<ActionModel>();
   private Collection<ActionLogModel> appUserIdActionLogModelList = new ArrayList<ActionLogModel>();
   private Collection<ActionStatusModel> createdByActionStatusModelList = new ArrayList<ActionStatusModel>();
   private Collection<ActionStatusModel> updatedByActionStatusModelList = new ArrayList<ActionStatusModel>();
   private Collection<AllpayCommissionRateModel> updatedByAllpayCommissionRateModelList = new ArrayList<AllpayCommissionRateModel>();
   private Collection<AllpayCommissionRateModel> createdByAllpayCommissionRateModelList = new ArrayList<AllpayCommissionRateModel>();
   private Collection<AllpayCommissionTransactionModel> updatedByAllpayCommissionTransactionModelList = new ArrayList<AllpayCommissionTransactionModel>();
   private Collection<AllpayCommissionTransactionModel> createdByAllpayCommissionTransactionModelList = new ArrayList<AllpayCommissionTransactionModel>();
   private Collection<AppUserModel> updatedByAppUserModelList = new ArrayList<AppUserModel>();
   private Collection<AppUserModel> createdByAppUserModelList = new ArrayList<AppUserModel>();
   private Collection<AppUserMobileHistoryModel> createdByAppUserMobileHistoryModelList = new ArrayList<AppUserMobileHistoryModel>();
   private Collection<AppUserMobileHistoryModel> updatedByAppUserMobileHistoryModelList = new ArrayList<AppUserMobileHistoryModel>();
   private Collection<AppUserMobileHistoryModel> appUserIdAppUserMobileHistoryModelList = new ArrayList<AppUserMobileHistoryModel>();
   private Collection<AppUserPartnerGroupModel> updatedByAppUserPartnerGroupModelList = new ArrayList<AppUserPartnerGroupModel>();
   private Collection<AppUserPartnerGroupModel> createdByAppUserPartnerGroupModelList = new ArrayList<AppUserPartnerGroupModel>();
   private Collection<AppUserPartnerGroupModel> appUserIdAppUserPartnerGroupModelList = new ArrayList<AppUserPartnerGroupModel>();
   //added by turab
   private Collection<AppUserPasswordHistoryModel> createdByAppUserPasswordHistoryModelList = new ArrayList<AppUserPasswordHistoryModel>();
   private Collection<AppUserPasswordHistoryModel> updatedByAppUserPasswordHistoryModelList = new ArrayList<AppUserPasswordHistoryModel>();
   private Collection<AppUserPasswordHistoryModel> appUserIdAppUserPasswordHistoryModelList = new ArrayList<AppUserPasswordHistoryModel>();
   //end by turab
   private Collection<AppUserTypeModel> createdByAppUserTypeModelList = new ArrayList<AppUserTypeModel>();
   private Collection<AppUserTypeModel> updatedByAppUserTypeModelList = new ArrayList<AppUserTypeModel>();
   private Collection<AppVersionModel> createdByAppVersionModelList = new ArrayList<AppVersionModel>();
   private Collection<AppVersionModel> updatedByAppVersionModelList = new ArrayList<AppVersionModel>();
   private Collection<AreaModel> updatedByAreaModelList = new ArrayList<AreaModel>();
   private Collection<AreaModel> createdByAreaModelList = new ArrayList<AreaModel>();
   private Collection<BankModel> updatedByBankModelList = new ArrayList<BankModel>();
   private Collection<BankModel> createdByBankModelList = new ArrayList<BankModel>();
   private Collection<BankUserModel> updatedByBankUserModelList = new ArrayList<BankUserModel>();
   private Collection<BankUserModel> createdByBankUserModelList = new ArrayList<BankUserModel>();
   private Collection<CardTypeModel> createdByCardTypeModelList = new ArrayList<CardTypeModel>();
   private Collection<CardTypeModel> updatedByCardTypeModelList = new ArrayList<CardTypeModel>();
   private Collection<CommissionRateModel> createdByCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CommissionRateModel> updatedByCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CommissionReasonModel> createdByCommissionReasonModelList = new ArrayList<CommissionReasonModel>();
   private Collection<CommissionReasonModel> updatedByCommissionReasonModelList = new ArrayList<CommissionReasonModel>();
   private Collection<CommissionStakeholderModel> updatedByCommissionStakeholderModelList = new ArrayList<CommissionStakeholderModel>();
   private Collection<CommissionStakeholderModel> createdByCommissionStakeholderModelList = new ArrayList<CommissionStakeholderModel>();
   private Collection<CommissionTransactionModel> updatedByCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<CommissionTransactionModel> createdByCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<CommissionTypeModel> createdByCommissionTypeModelList = new ArrayList<CommissionTypeModel>();
   private Collection<CommissionTypeModel> updatedByCommissionTypeModelList = new ArrayList<CommissionTypeModel>();
   private Collection<CustomerModel> createdByCustomerModelList = new ArrayList<CustomerModel>();
   private Collection<CustomerModel> updatedByCustomerModelList = new ArrayList<CustomerModel>();
   private Collection<DeviceTypeModel> createdByDeviceTypeModelList = new ArrayList<DeviceTypeModel>();
   private Collection<DeviceTypeModel> updatedByDeviceTypeModelList = new ArrayList<DeviceTypeModel>();
   private Collection<DeviceTypeCommandModel> createdByDeviceTypeCommandModelList = new ArrayList<DeviceTypeCommandModel>();
   private Collection<DeviceTypeCommandModel> updatedByDeviceTypeCommandModelList = new ArrayList<DeviceTypeCommandModel>();
   private Collection<DispenseTypeModel> createdByDispenseTypeModelList = new ArrayList<DispenseTypeModel>();
   private Collection<DispenseTypeModel> updatedByDispenseTypeModelList = new ArrayList<DispenseTypeModel>();
   private Collection<DistributorModel> updatedByDistributorModelList = new ArrayList<DistributorModel>();
   private Collection<DistributorModel> createdByDistributorModelList = new ArrayList<DistributorModel>();
   private Collection<DistributorContactModel> createdByDistributorContactModelList = new ArrayList<DistributorContactModel>();
   private Collection<DistributorContactModel> updatedByDistributorContactModelList = new ArrayList<DistributorContactModel>();
   private Collection<DistributorLevelModel> createdByDistributorLevelModelList = new ArrayList<DistributorLevelModel>();
   private Collection<DistributorLevelModel> updatedByDistributorLevelModelList = new ArrayList<DistributorLevelModel>();
   private Collection<FailureReasonModel> updatedByFailureReasonModelList = new ArrayList<FailureReasonModel>();
   private Collection<FailureReasonModel> createdByFailureReasonModelList = new ArrayList<FailureReasonModel>();
   private Collection<FavoriteNumbersModel> updatedByFavoriteNumbersModelList = new ArrayList<FavoriteNumbersModel>();
   private Collection<FavoriteNumbersModel> appUserIdFavoriteNumbersModelList = new ArrayList<FavoriteNumbersModel>();
   private Collection<FavoriteNumbersModel> createdByFavoriteNumbersModelList = new ArrayList<FavoriteNumbersModel>();
   private Collection<IntegrationModuleModel> createdByIntegrationModuleModelList = new ArrayList<IntegrationModuleModel>();
   private Collection<IntegrationModuleModel> updatedByIntegrationModuleModelList = new ArrayList<IntegrationModuleModel>();
   private Collection<IssueModel> updatedByIssueModelList = new ArrayList<IssueModel>();
   private Collection<IssueModel> createdByIssueModelList = new ArrayList<IssueModel>();
   private Collection<IssueHistoryModel> createdByIssueHistoryModelList = new ArrayList<IssueHistoryModel>();
   private Collection<IssueHistoryModel> updatedByIssueHistoryModelList = new ArrayList<IssueHistoryModel>();
   private Collection<IssueStatusModel> createdByIssueStatusModelList = new ArrayList<IssueStatusModel>();
   private Collection<IssueStatusModel> updatedByIssueStatusModelList = new ArrayList<IssueStatusModel>();
   private Collection<IssueTypeModel> createdByIssueTypeModelList = new ArrayList<IssueTypeModel>();
   private Collection<IssueTypeModel> updatedByIssueTypeModelList = new ArrayList<IssueTypeModel>();
   private Collection<IssueTypeStatusModel> createdByIssueTypeStatusModelList = new ArrayList<IssueTypeStatusModel>();
   private Collection<IssueTypeStatusModel> updatedByIssueTypeStatusModelList = new ArrayList<IssueTypeStatusModel>();
   private Collection<MessageTypeModel> createdByMessageTypeModelList = new ArrayList<MessageTypeModel>();
   private Collection<MessageTypeModel> updatedByMessageTypeModelList = new ArrayList<MessageTypeModel>();
   private Collection<MnoModel> updatedByMnoModelList = new ArrayList<MnoModel>();
   private Collection<MnoModel> createdByMnoModelList = new ArrayList<MnoModel>();
   private Collection<MnoDialingCodeModel> createdByMnoDialingCodeModelList = new ArrayList<MnoDialingCodeModel>();
   private Collection<MnoUserModel> updatedByMnoUserModelList = new ArrayList<MnoUserModel>();
   private Collection<MnoUserModel> createdByMnoUserModelList = new ArrayList<MnoUserModel>();
   private Collection<NotificationMessageModel> updatedByNotificationMessageModelList = new ArrayList<NotificationMessageModel>();
   private Collection<NotificationMessageModel> createdByNotificationMessageModelList = new ArrayList<NotificationMessageModel>();
   private Collection<OperatorModel> createdByOperatorModelList = new ArrayList<OperatorModel>();
   private Collection<OperatorModel> updatedByOperatorModelList = new ArrayList<OperatorModel>();
   private Collection<OperatorUserModel> updatedByOperatorUserModelList = new ArrayList<OperatorUserModel>();
   private Collection<OperatorUserModel> createdByOperatorUserModelList = new ArrayList<OperatorUserModel>();
   private Collection<PartnerModel> createdByPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<PartnerModel> updatedByPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<PartnerIpAddressModel> updatedByPartnerIpAddressModelList = new ArrayList<PartnerIpAddressModel>();
   private Collection<PartnerIpAddressModel> createdByPartnerIpAddressModelList = new ArrayList<PartnerIpAddressModel>();
   private Collection<PartnerGroupModel> updatedByPartnerGroupModelList = new ArrayList<PartnerGroupModel>();
   private Collection<PartnerGroupModel> createdByPartnerGroupModelList = new ArrayList<PartnerGroupModel>();
   private Collection<PartnerGroupDetailModel> updatedByPartnerGroupDetailModelList = new ArrayList<PartnerGroupDetailModel>();
   private Collection<PartnerGroupDetailModel> createdByPartnerGroupDetailModelList = new ArrayList<PartnerGroupDetailModel>();
   private Collection<PartnerPermissionGroupModel> updatedByPartnerPermissionGroupModelList = new ArrayList<PartnerPermissionGroupModel>();
   private Collection<PartnerPermissionGroupModel> createdByPartnerPermissionGroupModelList = new ArrayList<PartnerPermissionGroupModel>();
   private Collection<PaymentModeModel> createdByPaymentModeModelList = new ArrayList<PaymentModeModel>();
   private Collection<PaymentModeModel> updatedByPaymentModeModelList = new ArrayList<PaymentModeModel>();
   private Collection<PermissionGroupModel> updatedByPermissionGroupModelList = new ArrayList<PermissionGroupModel>();
   private Collection<PermissionGroupModel> createdByPermissionGroupModelList = new ArrayList<PermissionGroupModel>();
   private Collection<PermissionGroupDetailModel> updatedByPermissionGroupDetailModelList = new ArrayList<PermissionGroupDetailModel>();
   private Collection<PermissionGroupDetailModel> createdByPermissionGroupDetailModelList = new ArrayList<PermissionGroupDetailModel>();
   private Collection<ProductModel> updatedByProductModelList = new ArrayList<ProductModel>();
   private Collection<ProductModel> createdByProductModelList = new ArrayList<ProductModel>();
   private Collection<ProductCatalogModel> createdByProductCatalogModelList = new ArrayList<ProductCatalogModel>();
   private Collection<ProductCatalogModel> updatedByProductCatalogModelList = new ArrayList<ProductCatalogModel>();
   private Collection<ProductUnitModel> createdByProductUnitModelList = new ArrayList<ProductUnitModel>();
   private Collection<ProductUnitModel> updatedByProductUnitModelList = new ArrayList<ProductUnitModel>();
   private Collection<RetailerModel> updatedByRetailerModelList = new ArrayList<RetailerModel>();
   private Collection<RetailerModel> createdByRetailerModelList = new ArrayList<RetailerModel>();
   private Collection<RetailerContactModel> updatedByRetailerContactModelList = new ArrayList<RetailerContactModel>();
   private Collection<RetailerContactModel> createdByRetailerContactModelList = new ArrayList<RetailerContactModel>();
   private Collection<RetailerTypeModel> createdByRetailerTypeModelList = new ArrayList<RetailerTypeModel>();
   private Collection<RetailerTypeModel> updatedByRetailerTypeModelList = new ArrayList<RetailerTypeModel>();
   private Collection<ServiceModel> updatedByServiceModelList = new ArrayList<ServiceModel>();
   private Collection<ServiceModel> createdByServiceModelList = new ArrayList<ServiceModel>();
   private Collection<ServiceTypeModel> createdByServiceTypeModelList = new ArrayList<ServiceTypeModel>();
   private Collection<ServiceTypeModel> updatedByServiceTypeModelList = new ArrayList<ServiceTypeModel>();
   private Collection<ShipmentModel> createdByShipmentModelList = new ArrayList<ShipmentModel>();
   private Collection<ShipmentModel> updatedByShipmentModelList = new ArrayList<ShipmentModel>();
   private Collection<ShipmentTypeModel> updatedByShipmentTypeModelList = new ArrayList<ShipmentTypeModel>();
   private Collection<ShipmentTypeModel> createdByShipmentTypeModelList = new ArrayList<ShipmentTypeModel>();
   private Collection<SmartMoneyAccountModel> updatedBySmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<SmartMoneyAccountModel> createdBySmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<StakeholderBankInfoModel> createdByStakeholderBankInfoModelList = new ArrayList<StakeholderBankInfoModel>();
   private Collection<StakeholderBankInfoModel> updatedByStakeholderBankInfoModelList = new ArrayList<StakeholderBankInfoModel>();
   private Collection<StakeholderTypeModel> createdByStakeholderTypeModelList = new ArrayList<StakeholderTypeModel>();
   private Collection<StakeholderTypeModel> updatedByStakeholderTypeModelList = new ArrayList<StakeholderTypeModel>();
   private Collection<SupplierModel> createdBySupplierModelList = new ArrayList<SupplierModel>();
   private Collection<SupplierModel> updatedBySupplierModelList = new ArrayList<SupplierModel>();
   private Collection<SupplierBankInfoModel> createdBySupplierBankInfoModelList = new ArrayList<SupplierBankInfoModel>();
   private Collection<SupplierBankInfoModel> updatedBySupplierBankInfoModelList = new ArrayList<SupplierBankInfoModel>();
   private Collection<SupplierUserModel> updatedBySupplierUserModelList = new ArrayList<SupplierUserModel>();
   private Collection<SupplierUserModel> createdBySupplierUserModelList = new ArrayList<SupplierUserModel>();
   private Collection<TickerModel> createdByTickerModelList = new ArrayList<TickerModel>();
   private Collection<TickerModel> updatedByTickerModelList = new ArrayList<TickerModel>();
   private Collection<TickerModel> appUserIdTickerModelList = new ArrayList<TickerModel>();
   private Collection<TransactionModel> updatedByTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<TransactionModel> createdByTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<TransactionCodeModel> updatedByTransactionCodeModelList = new ArrayList<TransactionCodeModel>();
   private Collection<TransactionCodeModel> createdByTransactionCodeModelList = new ArrayList<TransactionCodeModel>();
   private Collection<TransactionTypeModel> createdByTransactionTypeModelList = new ArrayList<TransactionTypeModel>();
   private Collection<TransactionTypeModel> updatedByTransactionTypeModelList = new ArrayList<TransactionTypeModel>();
   private Collection<UsecaseModel> createdByUsecaseModelList = new ArrayList<UsecaseModel>();
   private Collection<UsecaseModel> updatedByUsecaseModelList = new ArrayList<UsecaseModel>();
   private Collection<UserDeviceAccountsModel> createdByUserDeviceAccountsModelList = new ArrayList<UserDeviceAccountsModel>();
   private Collection<UserDeviceAccountsModel> updatedByUserDeviceAccountsModelList = new ArrayList<UserDeviceAccountsModel>();
   private Collection<UserDeviceAccountsModel> appUserIdUserDeviceAccountsModelList = new ArrayList<UserDeviceAccountsModel>();
   private Collection<UserPermissionModel> createdByUserPermissionModelList = new ArrayList<UserPermissionModel>();
   private Collection<UserPermissionModel> updatedByUserPermissionModelList = new ArrayList<UserPermissionModel>();
   //user permission list will be filled by security framework at the time of authenticating user
   private Collection<CustomUserPermissionViewModel> userPermissionList = new ArrayList<CustomUserPermissionViewModel>();
   private Collection<VeriflyModeModel> createdByVeriflyModeModelList = new ArrayList<VeriflyModeModel>();
   private Collection<VeriflyModeModel> updatedByVeriflyModeModelList = new ArrayList<VeriflyModeModel>();
   //Added by Sheheryaar Nawaz
   private List<String> accountTypeList = new ArrayList<>();

   private Long appUserId;
   private String firstName;
   private String middleName;
   private String lastName;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String country;
   private String zip;
   private String nic;
   private String email;
   private String fax;
   private String motherMaidenName;
   @XmlElement
   private String username;
   private String password;
   private String mobileNo;
   private String passwordHint;
   private Boolean verified;
   private Boolean accountEnabled;
   private Boolean accountExpired;
   private Boolean accountLocked;
   private Boolean accountClosedUnsettled;
   private Boolean accountClosedSettled;
   private Boolean credentialsExpired;
   private Boolean passwordChangeRequired;
   private Date createdOn;
   private Date updatedOn;
   private Date closedOn;
   private String closingComments;
   private Date settledOn;
   private String settlementComments;
   private Integer versionNo;
   private Date dob;
   private java.sql.Timestamp lastLoginAttemptTime;
   private Integer loginAttemptCount;
   private Date nicExpiryDate;
   private Long registrationStateID;
   private Boolean	cnicExpiryMsgSent;
   private Long	accountStateId;

   private java.sql.Timestamp lastLoginTime;
   private Long employeeId;
   private String tellerId;

   private Boolean filer;
   private Date dormancyRemovedOn;

   private String customerMobileNetwork;
   private Date cnicIssuanceDate;
   private Date dormantMarkedOn;
   private Long dormancyRemovedBy;


   /**
    * Default constructor.
    */
   public AppUserModel() {
   }
   public AppUserModel(Long appUserId) {
      setPrimaryKey(appUserId);
   }

   public AppUserHistoryModel toAppUserHistoryModel()
   {
      AppUserHistoryModel appUserHistoryModel = new AppUserHistoryModel();
      appUserHistoryModel.setAppUserId(appUserId);
      appUserHistoryModel.setNic(nic);
      appUserHistoryModel.setClosedByAppUserModel(closedByAppUserModel);
      appUserHistoryModel.setClosingComments(closingComments);
      appUserHistoryModel.setClosedOn(closedOn);

      appUserHistoryModel.setUpdatedOn(updatedOn);
      appUserHistoryModel.setUpdatedByAppUserModel(updatedByAppUserModel);
      appUserHistoryModel.setCreatedOn(createdOn);
      appUserHistoryModel.setCreatedByAppUserModel(createdByAppUserModel);
      return appUserHistoryModel;
   }

   /**
    * Return the primary key.
    *
    * @return Long with the primary key.
    */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
      return getAppUserId();
   }

   /**
    * Set the primary key.
    *
    * @param primaryKey the primary key
    */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
      setAppUserId(primaryKey);
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @Column(name = "APP_USER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_seq")
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>firstName</code> property.
    *
    */
   @Column(name = "FIRST_NAME" , nullable = false , length=50 )
   public String getFirstName() {
      return firstName;
   }

   /**
    * Sets the value of the <code>firstName</code> property.
    *
    * @param firstName the value for the <code>firstName</code> property
    *
    * @spring.validator type="required"
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * Returns the value of the <code>lastName</code> property.
    *
    */
   @Column(name = "LAST_NAME" , nullable = false , length=50 )
   public String getLastName() {
      return lastName;
   }

   /**
    * Sets the value of the <code>lastName</code> property.
    *
    * @param lastName the value for the <code>lastName</code> property
    *
    * @spring.validator type="required"
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   /**
    * Returns the value of the <code>address1</code> property.
    *
    */
   @Column(name = "ADDRESS1"  , length=250 )
   public String getAddress1() {
      return address1;
   }

   /**
    * Sets the value of the <code>address1</code> property.
    *
    * @param address1 the value for the <code>address1</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress1(String address1) {
      this.address1 = address1;
   }

   /**
    * Returns the value of the <code>address2</code> property.
    *
    */
   @Column(name = "ADDRESS2"  , length=250 )
   public String getAddress2() {
      return address2;
   }

   /**
    * Sets the value of the <code>address2</code> property.
    *
    * @param address2 the value for the <code>address2</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress2(String address2) {
      this.address2 = address2;
   }

   /**
    * Returns the value of the <code>city</code> property.
    *
    */
   @Column(name = "CITY"  , length=50 )
   public String getCity() {
      return city;
   }

   /**
    * Sets the value of the <code>city</code> property.
    *
    * @param city the value for the <code>city</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCity(String city) {
      this.city = city;
   }

   /**
    * Returns the value of the <code>state</code> property.
    *
    */
   @Column(name = "STATE"  , length=50 )
   public String getState() {
      return state;
   }

   /**
    * Sets the value of the <code>state</code> property.
    *
    * @param state the value for the <code>state</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setState(String state) {
      this.state = state;
   }

   /**
    * Returns the value of the <code>country</code> property.
    *
    */
   @Column(name = "COUNTRY"  , length=50 )
   public String getCountry() {
      return country;
   }

   /**
    * Sets the value of the <code>country</code> property.
    *
    * @param country the value for the <code>country</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCountry(String country) {
      this.country = country;
   }

   /**
    * Returns the value of the <code>zip</code> property.
    *
    */
   @Column(name = "ZIP"  , length=50 )
   public String getZip() {
      return zip;
   }

   /**
    * Sets the value of the <code>zip</code> property.
    *
    * @param zip the value for the <code>zip</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setZip(String zip) {
      this.zip = zip;
   }

   /**
    * Returns the value of the <code>nic</code> property.
    *
    */
   @Column(name = "NIC"  , length=50 )
   public String getNic() {
      return nic;
   }

   /**
    * Sets the value of the <code>nic</code> property.
    *
    * @param nic the value for the <code>nic</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setNic(String nic) {
      this.nic = nic;
   }

   /**
    * Returns the value of the <code>email</code> property.
    *
    */
   @Column(name = "EMAIL"  , length=50 )
   public String getEmail() {
      return email;
   }

   /**
    * Sets the value of the <code>email</code> property.
    *
    * @param email the value for the <code>email</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setEmail(String email) {
      this.email = email;
   }

   /**
    * Returns the value of the <code>fax</code> property.
    *
    */
   @Column(name = "FAX"  , length=50 )
   public String getFax() {
      return fax;
   }

   /**
    * Sets the value of the <code>fax</code> property.
    *
    * @param fax the value for the <code>fax</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFax(String fax) {
      this.fax = fax;
   }

   /**
    * Returns the value of the <code>motherMaidenName</code> property.
    *
    */
   @Column(name = "MOTHER_MAIDEN_NAME"  , length=50 )
   public String getMotherMaidenName() {
      return motherMaidenName;
   }

   /**
    * Sets the value of the <code>motherMaidenName</code> property.
    *
    * @param motherMaidenName the value for the <code>motherMaidenName</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setMotherMaidenName(String motherMaidenName) {
      this.motherMaidenName = motherMaidenName;
   }

   /**
    * Returns the value of the <code>username</code> property.
    *
    */
   @Column(name = "USERNAME" , nullable = false , length=50 )
   public String getUsername() {
      return username;
   }

   /**
    * Sets the value of the <code>username</code> property.
    *
    * @param username the value for the <code>username</code> property
    *
    * @spring.validator type="required"
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUsername(String username) {
      this.username = username;
   }

   /**
    * Returns the value of the <code>password</code> property.
    *
    */
   @Column(name = "PASSWORD" , nullable = false , length=250 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *
    * @spring.validator type="required"
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
   @Column(name = "MOBILE_NO" , nullable = false , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *
    * @spring.validator type="required"
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>passwordHint</code> property.
    *
    */
   @Column(name = "PASSWORD_HINT"  , length=250 )
   public String getPasswordHint() {
      return passwordHint;
   }

   /**
    * Sets the value of the <code>passwordHint</code> property.
    *
    * @param passwordHint the value for the <code>passwordHint</code> property
    *
    * @spring.validator type="maxlength"
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPasswordHint(String passwordHint) {
      this.passwordHint = passwordHint;
   }

   /**
    * Returns the value of the <code>verified</code> property.
    *
    */
   @Column(name = "IS_VERIFIED" , nullable = false )
   public Boolean getVerified() {
      return verified;
   }

   /**
    * Sets the value of the <code>verified</code> property.
    *
    * @param verified the value for the <code>verified</code> property
    *
    */

   public void setVerified(Boolean verified) {
      this.verified = verified;
   }

   /**
    * Returns the value of the <code>accountEnabled</code> property.
    *
    */
   @Column(name = "IS_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAccountEnabled() {
      return accountEnabled;
   }

   /**
    * Sets the value of the <code>accountEnabled</code> property.
    *
    * @param accountEnabled the value for the <code>accountEnabled</code> property
    *
    */

   public void setAccountEnabled(Boolean accountEnabled) {
      this.accountEnabled = accountEnabled;
   }

   /**
    * Returns the value of the <code>accountExpired</code> property.
    *
    */
   @Column(name = "IS_ACCOUNT_EXPIRED" , nullable = false )
   public Boolean getAccountExpired() {
      return accountExpired;
   }

   /**
    * Sets the value of the <code>accountExpired</code> property.
    *
    * @param accountExpired the value for the <code>accountExpired</code> property
    *
    */

   public void setAccountExpired(Boolean accountExpired) {
      this.accountExpired = accountExpired;
   }

   /**
    * Returns the value of the <code>accountLocked</code> property.
    *
    */
   @Column(name = "IS_ACCOUNT_LOCKED" , nullable = false )
   public Boolean getAccountLocked() {
      return accountLocked;
   }

   /**
    * Sets the value of the <code>accountLocked</code> property.
    *
    * @param accountLocked the value for the <code>accountLocked</code> property
    *
    */

   public void setAccountLocked(Boolean accountLocked) {
      this.accountLocked = accountLocked;
   }

   /**
    * Returns the value of the <code>credentialsExpired</code> property.
    *
    */
   @Column(name = "IS_CREDENTIALS_EXPIRED" , nullable = false )
   public Boolean getCredentialsExpired() {
      return credentialsExpired;
   }

   /**
    * Sets the value of the <code>credentialsExpired</code> property.
    *
    * @param credentialsExpired the value for the <code>credentialsExpired</code> property
    *
    */

   public void setCredentialsExpired(Boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
   }


   /**
    * Returns the value of the <code>passwordChangeRequired</code> property.
    *
    */
   @Column(name = "IS_PASSWORD_CHANGE_REQUIRED" , nullable = false )
   public Boolean getPasswordChangeRequired() {
      return passwordChangeRequired;
   }

   /**
    * Sets the value of the <code>passwordChangeRequired</code> property.
    *
    * @param passwordChangeRequired the value for the <code>passwordChangeRequired</code> property
    *
    */

   public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
      this.passwordChangeRequired = passwordChangeRequired;
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
    * Returns the value of the <code>dob</code> property.
    *
    */
   @Column(name = "DOB"  )
   public Date getDob() {
      return dob;
   }

   /**
    * Sets the value of the <code>dob</code> property.
    *
    * @param dob the value for the <code>dob</code> property
    *
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setDob(Date dob) {
      this.dob = dob;
   }

   /**
    * Returns the value of the <code>lastLoginAttemptTime</code> property.
    *
    */
   @Column(name = "LAST_LOGIN_ATTEMPT_TIME"  )
   public java.sql.Timestamp getLastLoginAttemptTime() {
      return lastLoginAttemptTime;
   }

   /**
    * Sets the value of the <code>lastLoginAttemptTime</code> property.
    *
    * @param lastLoginAttemptTime the value for the <code>lastLoginAttemptTime</code> property
    *
    */

   public void setLastLoginAttemptTime(java.sql.Timestamp lastLoginAttemptTime) {
      this.lastLoginAttemptTime = lastLoginAttemptTime;
   }

   /**
    * Returns the value of the <code>loginAttemptCount</code> property.
    *
    */
   @Column(name = "LOGIN_ATTEMPT_COUNT"  )
   public Integer getLoginAttemptCount() {
      return loginAttemptCount;
   }

   /**
    * Sets the value of the <code>loginAttemptCount</code> property.
    *
    * @param loginAttemptCount the value for the <code>loginAttemptCount</code> property
    *
    * @spring.validator type="integer"
    */

   public void setLoginAttemptCount(Integer loginAttemptCount) {
      this.loginAttemptCount = loginAttemptCount;
   }

   /**
    * Returns the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    * @return the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUPPLIER_USER_ID")
   public SupplierUserModel getRelationSupplierUserIdSupplierUserModel(){
      return supplierUserIdSupplierUserModel;
   }

   /**
    * Returns the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    * @return the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SupplierUserModel getSupplierUserIdSupplierUserModel(){
      return getRelationSupplierUserIdSupplierUserModel();
   }

   /**
    * Sets the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    * @param supplierUserModel a value for <code>supplierUserIdSupplierUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSupplierUserIdSupplierUserModel(SupplierUserModel supplierUserModel) {
      this.supplierUserIdSupplierUserModel = supplierUserModel;
   }

   /**
    * Sets the value of the <code>supplierUserIdSupplierUserModel</code> relation property.
    *
    * @param supplierUserModel a value for <code>supplierUserIdSupplierUserModel</code>.
    */
   @javax.persistence.Transient
   public void setSupplierUserIdSupplierUserModel(SupplierUserModel supplierUserModel) {
      if(null != supplierUserModel)
      {
         setRelationSupplierUserIdSupplierUserModel((SupplierUserModel)supplierUserModel.clone());
      }
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


   //-----------------------------------------------------------------------------

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

   /**
    * Sets the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    * @param HandlerModel a value for <code>HandlerIdHandlerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationHandlerIdHandlerModel(HandlerModel handlerModel) {
      this.handlerIdHandlerModel = handlerModel;
   }

   /**
    * Sets the value of the <code>HandlerIdHandlerModel</code> relation property.
    *
    * @param HandlerModel a value for <code>HandlerIdHandlerModel</code>.
    */
   @javax.persistence.Transient
   public void setHandlerIdHandlerModel(HandlerModel handlerModel) {
      if(null != handlerModel)
      {
         setRelationHandlerIdHandlerModel((HandlerModel)handlerModel.clone());
      }
   }

   //-----------------------------------------------------------------------------

   /**
    * Returns the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    * @return the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "OPERATOR_USER_ID")
   public OperatorUserModel getRelationOperatorUserIdOperatorUserModel(){
      return operatorUserIdOperatorUserModel;
   }

   /**
    * Returns the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    * @return the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OperatorUserModel getOperatorUserIdOperatorUserModel(){
      return getRelationOperatorUserIdOperatorUserModel();
   }

   /**
    * Sets the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    * @param operatorUserModel a value for <code>operatorUserIdOperatorUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationOperatorUserIdOperatorUserModel(OperatorUserModel operatorUserModel) {
      this.operatorUserIdOperatorUserModel = operatorUserModel;
   }

   /**
    * Sets the value of the <code>operatorUserIdOperatorUserModel</code> relation property.
    *
    * @param operatorUserModel a value for <code>operatorUserIdOperatorUserModel</code>.
    */
   @javax.persistence.Transient
   public void setOperatorUserIdOperatorUserModel(OperatorUserModel operatorUserModel) {
      if(null != operatorUserModel)
      {
         setRelationOperatorUserIdOperatorUserModel((OperatorUserModel)operatorUserModel.clone());
      }
   }


   /**
    * Returns the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    * @return the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "MOBILE_TYPE_ID")
   public MobileTypeModel getRelationMobileTypeIdMobileTypeModel(){
      return mobileTypeIdMobileTypeModel;
   }

   /**
    * Returns the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    * @return the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MobileTypeModel getMobileTypeIdMobileTypeModel(){
      return getRelationMobileTypeIdMobileTypeModel();
   }

   /**
    * Sets the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    * @param mobileTypeModel a value for <code>mobileTypeIdMobileTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMobileTypeIdMobileTypeModel(MobileTypeModel mobileTypeModel) {
      this.mobileTypeIdMobileTypeModel = mobileTypeModel;
   }

   /**
    * Sets the value of the <code>mobileTypeIdMobileTypeModel</code> relation property.
    *
    * @param mobileTypeModel a value for <code>mobileTypeIdMobileTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setMobileTypeIdMobileTypeModel(MobileTypeModel mobileTypeModel) {
      if(null != mobileTypeModel)
      {
         setRelationMobileTypeIdMobileTypeModel((MobileTypeModel)mobileTypeModel.clone());
      }
   }


   /**
    * Returns the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    * @return the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_OP_USER_ID")
   public MnoUserModel getRelationMnoUserIdMnoUserModel(){
      return mnoUserIdMnoUserModel;
   }

   /**
    * Returns the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    * @return the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MnoUserModel getMnoUserIdMnoUserModel(){
      return getRelationMnoUserIdMnoUserModel();
   }

   /**
    * Sets the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    * @param mnoUserModel a value for <code>mnoUserIdMnoUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMnoUserIdMnoUserModel(MnoUserModel mnoUserModel) {
      this.mnoUserIdMnoUserModel = mnoUserModel;
   }

   /**
    * Sets the value of the <code>mnoUserIdMnoUserModel</code> relation property.
    *
    * @param mnoUserModel a value for <code>mnoUserIdMnoUserModel</code>.
    */
   @javax.persistence.Transient
   public void setMnoUserIdMnoUserModel(MnoUserModel mnoUserModel) {
      if(null != mnoUserModel)
      {
         setRelationMnoUserIdMnoUserModel((MnoUserModel)mnoUserModel.clone());
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

   @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinColumn(name = "WALK_IN_CUSTOMER_ID")
   public WalkinCustomerModel getWalkinCustomerModel() {
      return walkinCustomerModel;
   }

   public void setWalkinCustomerModel(WalkinCustomerModel walkinCustomerModel) {
      this.walkinCustomerModel = walkinCustomerModel;
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
    * Returns the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    * @return the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "BANK_USER_ID")
   public BankUserModel getRelationBankUserIdBankUserModel(){
      return bankUserIdBankUserModel;
   }

   /**
    * Returns the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    * @return the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public BankUserModel getBankUserIdBankUserModel(){
      return getRelationBankUserIdBankUserModel();
   }

   /**
    * Sets the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    * @param bankUserModel a value for <code>bankUserIdBankUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationBankUserIdBankUserModel(BankUserModel bankUserModel) {
      this.bankUserIdBankUserModel = bankUserModel;
   }

   /**
    * Sets the value of the <code>bankUserIdBankUserModel</code> relation property.
    *
    * @param bankUserModel a value for <code>bankUserIdBankUserModel</code>.
    */
   @javax.persistence.Transient
   public void setBankUserIdBankUserModel(BankUserModel bankUserModel) {
      if(null != bankUserModel)
      {
         setRelationBankUserIdBankUserModel((BankUserModel)bankUserModel.clone());
      }
   }


   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public AppUserTypeModel getRelationAppUserTypeIdAppUserTypeModel(){
      return appUserTypeIdAppUserTypeModel;
   }

   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserTypeModel getAppUserTypeIdAppUserTypeModel(){
      return getRelationAppUserTypeIdAppUserTypeModel();
   }

   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      this.appUserTypeIdAppUserTypeModel = appUserTypeModel;
   }

   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      if(null != appUserTypeModel)
      {
         setRelationAppUserTypeIdAppUserTypeModel((AppUserTypeModel)appUserTypeModel.clone());
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
    * Add the related ActionModel to this one-to-many relation.
    *
    * @param actionModel object to be added.
    */

   public void addCreatedByActionModel(ActionModel actionModel) {
      actionModel.setRelationCreatedByAppUserModel(this);
      createdByActionModelList.add(actionModel);
   }

   /**
    * Remove the related ActionModel to this one-to-many relation.
    *
    * @param actionModel object to be removed.
    */

   public void removeCreatedByActionModel(ActionModel actionModel) {
      actionModel.setRelationCreatedByAppUserModel(null);
      createdByActionModelList.remove(actionModel);
   }

   /**
    * Get a list of related ActionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ActionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ActionModel> getCreatedByActionModelList() throws Exception {
      return createdByActionModelList;
   }


   /**
    * Set a list of ActionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param actionModelList the list of related objects.
    */
   public void setCreatedByActionModelList(Collection<ActionModel> actionModelList) throws Exception {
      this.createdByActionModelList = actionModelList;
   }


   /**
    * Add the related ActionModel to this one-to-many relation.
    *
    * @param actionModel object to be added.
    */

   public void addUpdatedByActionModel(ActionModel actionModel) {
      actionModel.setRelationUpdatedByAppUserModel(this);
      updatedByActionModelList.add(actionModel);
   }

   /**
    * Remove the related ActionModel to this one-to-many relation.
    *
    * @param actionModel object to be removed.
    */

   public void removeUpdatedByActionModel(ActionModel actionModel) {
      actionModel.setRelationUpdatedByAppUserModel(null);
      updatedByActionModelList.remove(actionModel);
   }

   /**
    * Get a list of related ActionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ActionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ActionModel> getUpdatedByActionModelList() throws Exception {
      return updatedByActionModelList;
   }


   /**
    * Set a list of ActionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param actionModelList the list of related objects.
    */
   public void setUpdatedByActionModelList(Collection<ActionModel> actionModelList) throws Exception {
      this.updatedByActionModelList = actionModelList;
   }


   /**
    * Add the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be added.
    */

   public void addAppUserIdActionLogModel(ActionLogModel actionLogModel) {
      actionLogModel.setRelationAppUserIdAppUserModel(this);
      appUserIdActionLogModelList.add(actionLogModel);
   }

   /**
    * Remove the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be removed.
    */

   public void removeAppUserIdActionLogModel(ActionLogModel actionLogModel) {
      actionLogModel.setRelationAppUserIdAppUserModel(null);
      appUserIdActionLogModelList.remove(actionLogModel);
   }

   /**
    * Get a list of related ActionLogModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of ActionLogModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<ActionLogModel> getAppUserIdActionLogModelList() throws Exception {
      return appUserIdActionLogModelList;
   }


   /**
    * Set a list of ActionLogModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param actionLogModelList the list of related objects.
    */
   public void setAppUserIdActionLogModelList(Collection<ActionLogModel> actionLogModelList) throws Exception {
      this.appUserIdActionLogModelList = actionLogModelList;
   }


   /**
    * Add the related ActionStatusModel to this one-to-many relation.
    *
    * @param actionStatusModel object to be added.
    */

   public void addCreatedByActionStatusModel(ActionStatusModel actionStatusModel) {
      actionStatusModel.setRelationCreatedByAppUserModel(this);
      createdByActionStatusModelList.add(actionStatusModel);
   }

   /**
    * Remove the related ActionStatusModel to this one-to-many relation.
    *
    * @param actionStatusModel object to be removed.
    */

   public void removeCreatedByActionStatusModel(ActionStatusModel actionStatusModel) {
      actionStatusModel.setRelationCreatedByAppUserModel(null);
      createdByActionStatusModelList.remove(actionStatusModel);
   }

   /**
    * Get a list of related ActionStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ActionStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ActionStatusModel> getCreatedByActionStatusModelList() throws Exception {
      return createdByActionStatusModelList;
   }


   /**
    * Set a list of ActionStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param actionStatusModelList the list of related objects.
    */
   public void setCreatedByActionStatusModelList(Collection<ActionStatusModel> actionStatusModelList) throws Exception {
      this.createdByActionStatusModelList = actionStatusModelList;
   }


   /**
    * Add the related ActionStatusModel to this one-to-many relation.
    *
    * @param actionStatusModel object to be added.
    */

   public void addUpdatedByActionStatusModel(ActionStatusModel actionStatusModel) {
      actionStatusModel.setRelationUpdatedByAppUserModel(this);
      updatedByActionStatusModelList.add(actionStatusModel);
   }

   /**
    * Remove the related ActionStatusModel to this one-to-many relation.
    *
    * @param actionStatusModel object to be removed.
    */

   public void removeUpdatedByActionStatusModel(ActionStatusModel actionStatusModel) {
      actionStatusModel.setRelationUpdatedByAppUserModel(null);
      updatedByActionStatusModelList.remove(actionStatusModel);
   }

   /**
    * Get a list of related ActionStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ActionStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ActionStatusModel> getUpdatedByActionStatusModelList() throws Exception {
      return updatedByActionStatusModelList;
   }


   /**
    * Set a list of ActionStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param actionStatusModelList the list of related objects.
    */
   public void setUpdatedByActionStatusModelList(Collection<ActionStatusModel> actionStatusModelList) throws Exception {
      this.updatedByActionStatusModelList = actionStatusModelList;
   }


   /**
    * Add the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be added.
    */

   public void addUpdatedByAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationUpdatedByAppUserModel(this);
      updatedByAllpayCommissionRateModelList.add(allpayCommissionRateModel);
   }

   /**
    * Remove the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be removed.
    */

   public void removeUpdatedByAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationUpdatedByAppUserModel(null);
      updatedByAllpayCommissionRateModelList.remove(allpayCommissionRateModel);
   }

   /**
    * Get a list of related AllpayCommissionRateModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AllpayCommissionRateModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AllpayCommissionRateModel> getUpdatedByAllpayCommissionRateModelList() throws Exception {
      return updatedByAllpayCommissionRateModelList;
   }


   /**
    * Set a list of AllpayCommissionRateModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param allpayCommissionRateModelList the list of related objects.
    */
   public void setUpdatedByAllpayCommissionRateModelList(Collection<AllpayCommissionRateModel> allpayCommissionRateModelList) throws Exception {
      this.updatedByAllpayCommissionRateModelList = allpayCommissionRateModelList;
   }


   /**
    * Add the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be added.
    */

   public void addCreatedByAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationCreatedByAppUserModel(this);
      createdByAllpayCommissionRateModelList.add(allpayCommissionRateModel);
   }

   /**
    * Remove the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be removed.
    */

   public void removeCreatedByAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationCreatedByAppUserModel(null);
      createdByAllpayCommissionRateModelList.remove(allpayCommissionRateModel);
   }

   /**
    * Get a list of related AllpayCommissionRateModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AllpayCommissionRateModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AllpayCommissionRateModel> getCreatedByAllpayCommissionRateModelList() throws Exception {
      return createdByAllpayCommissionRateModelList;
   }


   /**
    * Set a list of AllpayCommissionRateModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param allpayCommissionRateModelList the list of related objects.
    */
   public void setCreatedByAllpayCommissionRateModelList(Collection<AllpayCommissionRateModel> allpayCommissionRateModelList) throws Exception {
      this.createdByAllpayCommissionRateModelList = allpayCommissionRateModelList;
   }


   /**
    * Add the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be added.
    */

   public void addUpdatedByAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationUpdatedByAppUserModel(this);
      updatedByAllpayCommissionTransactionModelList.add(allpayCommissionTransactionModel);
   }

   /**
    * Remove the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be removed.
    */

   public void removeUpdatedByAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationUpdatedByAppUserModel(null);
      updatedByAllpayCommissionTransactionModelList.remove(allpayCommissionTransactionModel);
   }

   /**
    * Get a list of related AllpayCommissionTransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AllpayCommissionTransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AllpayCommissionTransactionModel> getUpdatedByAllpayCommissionTransactionModelList() throws Exception {
      return updatedByAllpayCommissionTransactionModelList;
   }


   /**
    * Set a list of AllpayCommissionTransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param allpayCommissionTransactionModelList the list of related objects.
    */
   public void setUpdatedByAllpayCommissionTransactionModelList(Collection<AllpayCommissionTransactionModel> allpayCommissionTransactionModelList) throws Exception {
      this.updatedByAllpayCommissionTransactionModelList = allpayCommissionTransactionModelList;
   }


   /**
    * Add the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be added.
    */

   public void addCreatedByAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationCreatedByAppUserModel(this);
      createdByAllpayCommissionTransactionModelList.add(allpayCommissionTransactionModel);
   }

   /**
    * Remove the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be removed.
    */

   public void removeCreatedByAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationCreatedByAppUserModel(null);
      createdByAllpayCommissionTransactionModelList.remove(allpayCommissionTransactionModel);
   }

   /**
    * Get a list of related AllpayCommissionTransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AllpayCommissionTransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AllpayCommissionTransactionModel> getCreatedByAllpayCommissionTransactionModelList() throws Exception {
      return createdByAllpayCommissionTransactionModelList;
   }


   /**
    * Set a list of AllpayCommissionTransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param allpayCommissionTransactionModelList the list of related objects.
    */
   public void setCreatedByAllpayCommissionTransactionModelList(Collection<AllpayCommissionTransactionModel> allpayCommissionTransactionModelList) throws Exception {
      this.createdByAllpayCommissionTransactionModelList = allpayCommissionTransactionModelList;
   }


   /**
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */

   public void addUpdatedByAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppUserModelList.add(appUserModel);
   }

   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */

   public void removeUpdatedByAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppUserModelList.remove(appUserModel);
   }

   /**
    * Get a list of related AppUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppUserModel> getUpdatedByAppUserModelList() throws Exception {
      return updatedByAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appUserModelList the list of related objects.
    */
   public void setUpdatedByAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
      this.updatedByAppUserModelList = appUserModelList;
   }


   /**
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */

   public void addCreatedByAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationCreatedByAppUserModel(this);
      createdByAppUserModelList.add(appUserModel);
   }

   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */

   public void removeCreatedByAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationCreatedByAppUserModel(null);
      createdByAppUserModelList.remove(appUserModel);
   }

   /**
    * Get a list of related AppUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppUserModel> getCreatedByAppUserModelList() throws Exception {
      return createdByAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appUserModelList the list of related objects.
    */
   public void setCreatedByAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
      this.createdByAppUserModelList = appUserModelList;
   }


   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addCreatedByAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationCreatedByAppUserModel(this);
      createdByAppUserMobileHistoryModelList.add(appUserMobileHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeCreatedByAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationCreatedByAppUserModel(null);
      createdByAppUserMobileHistoryModelList.remove(appUserMobileHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppUserMobileHistoryModel> getCreatedByAppUserMobileHistoryModelList() throws Exception {
      return createdByAppUserMobileHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setCreatedByAppUserMobileHistoryModelList(Collection<AppUserMobileHistoryModel> appUserMobileHistoryModelList) throws Exception {
      this.createdByAppUserMobileHistoryModelList = appUserMobileHistoryModelList;
   }


   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addUpdatedByAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppUserMobileHistoryModelList.add(appUserMobileHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeUpdatedByAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppUserMobileHistoryModelList.remove(appUserMobileHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppUserMobileHistoryModel> getUpdatedByAppUserMobileHistoryModelList() throws Exception {
      return updatedByAppUserMobileHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setUpdatedByAppUserMobileHistoryModelList(Collection<AppUserMobileHistoryModel> appUserMobileHistoryModelList) throws Exception {
      this.updatedByAppUserMobileHistoryModelList = appUserMobileHistoryModelList;
   }


   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addAppUserIdAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationAppUserIdAppUserModel(this);
      appUserIdAppUserMobileHistoryModelList.add(appUserMobileHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeAppUserIdAppUserMobileHistoryModel(AppUserMobileHistoryModel appUserMobileHistoryModel) {
      appUserMobileHistoryModel.setRelationAppUserIdAppUserModel(null);
      appUserIdAppUserMobileHistoryModelList.remove(appUserMobileHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<AppUserMobileHistoryModel> getAppUserIdAppUserMobileHistoryModelList() throws Exception {
      return appUserIdAppUserMobileHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setAppUserIdAppUserMobileHistoryModelList(Collection<AppUserMobileHistoryModel> appUserMobileHistoryModelList) throws Exception {
      this.appUserIdAppUserMobileHistoryModelList = appUserMobileHistoryModelList;
   }


   /**
    * Add the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be added.
    */

   public void addUpdatedByAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppUserPartnerGroupModelList.add(appUserPartnerGroupModel);
   }

   /**
    * Remove the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be removed.
    */

   public void removeUpdatedByAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppUserPartnerGroupModelList.remove(appUserPartnerGroupModel);
   }

   /**
    * Get a list of related AppUserPartnerGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppUserPartnerGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppUserPartnerGroupModel> getUpdatedByAppUserPartnerGroupModelList() throws Exception {
      return updatedByAppUserPartnerGroupModelList;
   }


   /**
    * Set a list of AppUserPartnerGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appUserPartnerGroupModelList the list of related objects.
    */
   public void setUpdatedByAppUserPartnerGroupModelList(Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList) throws Exception {
      this.updatedByAppUserPartnerGroupModelList = appUserPartnerGroupModelList;
   }


   /**
    * Add the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be added.
    */

   public void addCreatedByAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationCreatedByAppUserModel(this);
      createdByAppUserPartnerGroupModelList.add(appUserPartnerGroupModel);
   }

   /**
    * Remove the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be removed.
    */

   public void removeCreatedByAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationCreatedByAppUserModel(null);
      createdByAppUserPartnerGroupModelList.remove(appUserPartnerGroupModel);
   }

   /**
    * Get a list of related AppUserPartnerGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppUserPartnerGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppUserPartnerGroupModel> getCreatedByAppUserPartnerGroupModelList() throws Exception {
      return createdByAppUserPartnerGroupModelList;
   }


   /**
    * Set a list of AppUserPartnerGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appUserPartnerGroupModelList the list of related objects.
    */
   public void setCreatedByAppUserPartnerGroupModelList(Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList) throws Exception {
      this.createdByAppUserPartnerGroupModelList = appUserPartnerGroupModelList;
   }


   /**
    * Add the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be added.
    */

   public void addAppUserIdAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationAppUserIdAppUserModel(this);
      appUserIdAppUserPartnerGroupModelList.add(appUserPartnerGroupModel);
   }

   /**
    * Remove the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be removed.
    */

   public void removeAppUserIdAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationAppUserIdAppUserModel(null);
      appUserIdAppUserPartnerGroupModelList.remove(appUserPartnerGroupModel);
   }

   /**
    * Get a list of related AppUserPartnerGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of AppUserPartnerGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<AppUserPartnerGroupModel> getAppUserIdAppUserPartnerGroupModelList() throws Exception {
      return appUserIdAppUserPartnerGroupModelList;
   }


   /**
    * Set a list of AppUserPartnerGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param appUserPartnerGroupModelList the list of related objects.
    */
   public void setAppUserIdAppUserPartnerGroupModelList(Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList) throws Exception {
      this.appUserIdAppUserPartnerGroupModelList = appUserPartnerGroupModelList;
   }


   /**
    * Add the related AppUserTypeModel to this one-to-many relation.
    *
    * @param appUserTypeModel object to be added.
    */

   public void addCreatedByAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      appUserTypeModel.setRelationCreatedByAppUserModel(this);
      createdByAppUserTypeModelList.add(appUserTypeModel);
   }

   /**
    * Remove the related AppUserTypeModel to this one-to-many relation.
    *
    * @param appUserTypeModel object to be removed.
    */

   public void removeCreatedByAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      appUserTypeModel.setRelationCreatedByAppUserModel(null);
      createdByAppUserTypeModelList.remove(appUserTypeModel);
   }

   /**
    * Get a list of related AppUserTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppUserTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppUserTypeModel> getCreatedByAppUserTypeModelList() throws Exception {
      return createdByAppUserTypeModelList;
   }


   /**
    * Set a list of AppUserTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appUserTypeModelList the list of related objects.
    */
   public void setCreatedByAppUserTypeModelList(Collection<AppUserTypeModel> appUserTypeModelList) throws Exception {
      this.createdByAppUserTypeModelList = appUserTypeModelList;
   }


   /**
    * Add the related AppUserTypeModel to this one-to-many relation.
    *
    * @param appUserTypeModel object to be added.
    */

   public void addUpdatedByAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      appUserTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppUserTypeModelList.add(appUserTypeModel);
   }

   /**
    * Remove the related AppUserTypeModel to this one-to-many relation.
    *
    * @param appUserTypeModel object to be removed.
    */

   public void removeUpdatedByAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      appUserTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppUserTypeModelList.remove(appUserTypeModel);
   }

   /**
    * Get a list of related AppUserTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppUserTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppUserTypeModel> getUpdatedByAppUserTypeModelList() throws Exception {
      return updatedByAppUserTypeModelList;
   }


   /**
    * Set a list of AppUserTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appUserTypeModelList the list of related objects.
    */
   public void setUpdatedByAppUserTypeModelList(Collection<AppUserTypeModel> appUserTypeModelList) throws Exception {
      this.updatedByAppUserTypeModelList = appUserTypeModelList;
   }


   /**
    * Add the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be added.
    */

   public void addCreatedByAppVersionModel(AppVersionModel appVersionModel) {
      appVersionModel.setRelationCreatedByAppUserModel(this);
      createdByAppVersionModelList.add(appVersionModel);
   }

   /**
    * Remove the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be removed.
    */

   public void removeCreatedByAppVersionModel(AppVersionModel appVersionModel) {
      appVersionModel.setRelationCreatedByAppUserModel(null);
      createdByAppVersionModelList.remove(appVersionModel);
   }

   /**
    * Get a list of related AppVersionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppVersionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppVersionModel> getCreatedByAppVersionModelList() throws Exception {
      return createdByAppVersionModelList;
   }


   /**
    * Set a list of AppVersionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appVersionModelList the list of related objects.
    */
   public void setCreatedByAppVersionModelList(Collection<AppVersionModel> appVersionModelList) throws Exception {
      this.createdByAppVersionModelList = appVersionModelList;
   }


   /**
    * Add the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be added.
    */

   public void addUpdatedByAppVersionModel(AppVersionModel appVersionModel) {
      appVersionModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppVersionModelList.add(appVersionModel);
   }

   /**
    * Remove the related AppVersionModel to this one-to-many relation.
    *
    * @param appVersionModel object to be removed.
    */

   public void removeUpdatedByAppVersionModel(AppVersionModel appVersionModel) {
      appVersionModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppVersionModelList.remove(appVersionModel);
   }

   /**
    * Get a list of related AppVersionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppVersionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppVersionModel> getUpdatedByAppVersionModelList() throws Exception {
      return updatedByAppVersionModelList;
   }


   /**
    * Set a list of AppVersionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appVersionModelList the list of related objects.
    */
   public void setUpdatedByAppVersionModelList(Collection<AppVersionModel> appVersionModelList) throws Exception {
      this.updatedByAppVersionModelList = appVersionModelList;
   }


   /**
    * Add the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be added.
    */

   public void addUpdatedByAreaModel(AreaModel areaModel) {
      areaModel.setRelationUpdatedByAppUserModel(this);
      updatedByAreaModelList.add(areaModel);
   }

   /**
    * Remove the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be removed.
    */

   public void removeUpdatedByAreaModel(AreaModel areaModel) {
      areaModel.setRelationUpdatedByAppUserModel(null);
      updatedByAreaModelList.remove(areaModel);
   }

   /**
    * Get a list of related AreaModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AreaModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AreaModel> getUpdatedByAreaModelList() throws Exception {
      return updatedByAreaModelList;
   }


   /**
    * Set a list of AreaModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param areaModelList the list of related objects.
    */
   public void setUpdatedByAreaModelList(Collection<AreaModel> areaModelList) throws Exception {
      this.updatedByAreaModelList = areaModelList;
   }


   /**
    * Add the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be added.
    */

   public void addCreatedByAreaModel(AreaModel areaModel) {
      areaModel.setRelationCreatedByAppUserModel(this);
      createdByAreaModelList.add(areaModel);
   }

   /**
    * Remove the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be removed.
    */

   public void removeCreatedByAreaModel(AreaModel areaModel) {
      areaModel.setRelationCreatedByAppUserModel(null);
      createdByAreaModelList.remove(areaModel);
   }

   /**
    * Get a list of related AreaModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AreaModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AreaModel> getCreatedByAreaModelList() throws Exception {
      return createdByAreaModelList;
   }


   /**
    * Set a list of AreaModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param areaModelList the list of related objects.
    */
   public void setCreatedByAreaModelList(Collection<AreaModel> areaModelList) throws Exception {
      this.createdByAreaModelList = areaModelList;
   }


   /**
    * Add the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be added.
    */

   public void addUpdatedByBankModel(BankModel bankModel) {
      bankModel.setRelationUpdatedByAppUserModel(this);
      updatedByBankModelList.add(bankModel);
   }

   /**
    * Remove the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be removed.
    */

   public void removeUpdatedByBankModel(BankModel bankModel) {
      bankModel.setRelationUpdatedByAppUserModel(null);
      updatedByBankModelList.remove(bankModel);
   }

   /**
    * Get a list of related BankModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of BankModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<BankModel> getUpdatedByBankModelList() throws Exception {
      return updatedByBankModelList;
   }


   /**
    * Set a list of BankModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param bankModelList the list of related objects.
    */
   public void setUpdatedByBankModelList(Collection<BankModel> bankModelList) throws Exception {
      this.updatedByBankModelList = bankModelList;
   }


   /**
    * Add the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be added.
    */

   public void addCreatedByBankModel(BankModel bankModel) {
      bankModel.setRelationCreatedByAppUserModel(this);
      createdByBankModelList.add(bankModel);
   }

   /**
    * Remove the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be removed.
    */

   public void removeCreatedByBankModel(BankModel bankModel) {
      bankModel.setRelationCreatedByAppUserModel(null);
      createdByBankModelList.remove(bankModel);
   }

   /**
    * Get a list of related BankModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of BankModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<BankModel> getCreatedByBankModelList() throws Exception {
      return createdByBankModelList;
   }


   /**
    * Set a list of BankModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param bankModelList the list of related objects.
    */
   public void setCreatedByBankModelList(Collection<BankModel> bankModelList) throws Exception {
      this.createdByBankModelList = bankModelList;
   }


   /**
    * Add the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be added.
    */

   public void addUpdatedByBankUserModel(BankUserModel bankUserModel) {
      bankUserModel.setRelationUpdatedByAppUserModel(this);
      updatedByBankUserModelList.add(bankUserModel);
   }

   /**
    * Remove the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be removed.
    */

   public void removeUpdatedByBankUserModel(BankUserModel bankUserModel) {
      bankUserModel.setRelationUpdatedByAppUserModel(null);
      updatedByBankUserModelList.remove(bankUserModel);
   }

   /**
    * Get a list of related BankUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of BankUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<BankUserModel> getUpdatedByBankUserModelList() throws Exception {
      return updatedByBankUserModelList;
   }


   /**
    * Set a list of BankUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param bankUserModelList the list of related objects.
    */
   public void setUpdatedByBankUserModelList(Collection<BankUserModel> bankUserModelList) throws Exception {
      this.updatedByBankUserModelList = bankUserModelList;
   }


   /**
    * Add the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be added.
    */

   public void addCreatedByBankUserModel(BankUserModel bankUserModel) {
      bankUserModel.setRelationCreatedByAppUserModel(this);
      createdByBankUserModelList.add(bankUserModel);
   }

   /**
    * Remove the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be removed.
    */

   public void removeCreatedByBankUserModel(BankUserModel bankUserModel) {
      bankUserModel.setRelationCreatedByAppUserModel(null);
      createdByBankUserModelList.remove(bankUserModel);
   }

   /**
    * Get a list of related BankUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of BankUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<BankUserModel> getCreatedByBankUserModelList() throws Exception {
      return createdByBankUserModelList;
   }


   /**
    * Set a list of BankUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param bankUserModelList the list of related objects.
    */
   public void setCreatedByBankUserModelList(Collection<BankUserModel> bankUserModelList) throws Exception {
      this.createdByBankUserModelList = bankUserModelList;
   }


   /**
    * Add the related CardTypeModel to this one-to-many relation.
    *
    * @param cardTypeModel object to be added.
    */

   public void addCreatedByCardTypeModel(CardTypeModel cardTypeModel) {
      cardTypeModel.setRelationCreatedByAppUserModel(this);
      createdByCardTypeModelList.add(cardTypeModel);
   }

   /**
    * Remove the related CardTypeModel to this one-to-many relation.
    *
    * @param cardTypeModel object to be removed.
    */

   public void removeCreatedByCardTypeModel(CardTypeModel cardTypeModel) {
      cardTypeModel.setRelationCreatedByAppUserModel(null);
      createdByCardTypeModelList.remove(cardTypeModel);
   }

   /**
    * Get a list of related CardTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CardTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CardTypeModel> getCreatedByCardTypeModelList() throws Exception {
      return createdByCardTypeModelList;
   }


   /**
    * Set a list of CardTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param cardTypeModelList the list of related objects.
    */
   public void setCreatedByCardTypeModelList(Collection<CardTypeModel> cardTypeModelList) throws Exception {
      this.createdByCardTypeModelList = cardTypeModelList;
   }


   /**
    * Add the related CardTypeModel to this one-to-many relation.
    *
    * @param cardTypeModel object to be added.
    */

   public void addUpdatedByCardTypeModel(CardTypeModel cardTypeModel) {
      cardTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByCardTypeModelList.add(cardTypeModel);
   }

   /**
    * Remove the related CardTypeModel to this one-to-many relation.
    *
    * @param cardTypeModel object to be removed.
    */

   public void removeUpdatedByCardTypeModel(CardTypeModel cardTypeModel) {
      cardTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByCardTypeModelList.remove(cardTypeModel);
   }

   /**
    * Get a list of related CardTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CardTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CardTypeModel> getUpdatedByCardTypeModelList() throws Exception {
      return updatedByCardTypeModelList;
   }


   /**
    * Set a list of CardTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param cardTypeModelList the list of related objects.
    */
   public void setUpdatedByCardTypeModelList(Collection<CardTypeModel> cardTypeModelList) throws Exception {
      this.updatedByCardTypeModelList = cardTypeModelList;
   }


   /**
    * Add the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be added.
    */

   public void addCreatedByCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationCreatedByAppUserModel(this);
      createdByCommissionRateModelList.add(commissionRateModel);
   }

   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */

   public void removeCreatedByCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationCreatedByAppUserModel(null);
      createdByCommissionRateModelList.remove(commissionRateModel);
   }

   /**
    * Get a list of related CommissionRateModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CommissionRateModel> getCreatedByCommissionRateModelList() throws Exception {
      return createdByCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param commissionRateModelList the list of related objects.
    */
   public void setCreatedByCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
      this.createdByCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be added.
    */

   public void addUpdatedByCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationUpdatedByAppUserModel(this);
      updatedByCommissionRateModelList.add(commissionRateModel);
   }

   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */

   public void removeUpdatedByCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationUpdatedByAppUserModel(null);
      updatedByCommissionRateModelList.remove(commissionRateModel);
   }

   /**
    * Get a list of related CommissionRateModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CommissionRateModel> getUpdatedByCommissionRateModelList() throws Exception {
      return updatedByCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param commissionRateModelList the list of related objects.
    */
   public void setUpdatedByCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
      this.updatedByCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CommissionReasonModel to this one-to-many relation.
    *
    * @param commissionReasonModel object to be added.
    */

   public void addCreatedByCommissionReasonModel(CommissionReasonModel commissionReasonModel) {
      commissionReasonModel.setRelationCreatedByAppUserModel(this);
      createdByCommissionReasonModelList.add(commissionReasonModel);
   }

   /**
    * Remove the related CommissionReasonModel to this one-to-many relation.
    *
    * @param commissionReasonModel object to be removed.
    */

   public void removeCreatedByCommissionReasonModel(CommissionReasonModel commissionReasonModel) {
      commissionReasonModel.setRelationCreatedByAppUserModel(null);
      createdByCommissionReasonModelList.remove(commissionReasonModel);
   }

   /**
    * Get a list of related CommissionReasonModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CommissionReasonModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CommissionReasonModel> getCreatedByCommissionReasonModelList() throws Exception {
      return createdByCommissionReasonModelList;
   }


   /**
    * Set a list of CommissionReasonModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param commissionReasonModelList the list of related objects.
    */
   public void setCreatedByCommissionReasonModelList(Collection<CommissionReasonModel> commissionReasonModelList) throws Exception {
      this.createdByCommissionReasonModelList = commissionReasonModelList;
   }


   /**
    * Add the related CommissionReasonModel to this one-to-many relation.
    *
    * @param commissionReasonModel object to be added.
    */

   public void addUpdatedByCommissionReasonModel(CommissionReasonModel commissionReasonModel) {
      commissionReasonModel.setRelationUpdatedByAppUserModel(this);
      updatedByCommissionReasonModelList.add(commissionReasonModel);
   }

   /**
    * Remove the related CommissionReasonModel to this one-to-many relation.
    *
    * @param commissionReasonModel object to be removed.
    */

   public void removeUpdatedByCommissionReasonModel(CommissionReasonModel commissionReasonModel) {
      commissionReasonModel.setRelationUpdatedByAppUserModel(null);
      updatedByCommissionReasonModelList.remove(commissionReasonModel);
   }

   /**
    * Get a list of related CommissionReasonModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CommissionReasonModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CommissionReasonModel> getUpdatedByCommissionReasonModelList() throws Exception {
      return updatedByCommissionReasonModelList;
   }


   /**
    * Set a list of CommissionReasonModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param commissionReasonModelList the list of related objects.
    */
   public void setUpdatedByCommissionReasonModelList(Collection<CommissionReasonModel> commissionReasonModelList) throws Exception {
      this.updatedByCommissionReasonModelList = commissionReasonModelList;
   }


   /**
    * Add the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be added.
    */

   public void addUpdatedByCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationUpdatedByAppUserModel(this);
      updatedByCommissionStakeholderModelList.add(commissionStakeholderModel);
   }

   /**
    * Remove the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be removed.
    */

   public void removeUpdatedByCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationUpdatedByAppUserModel(null);
      updatedByCommissionStakeholderModelList.remove(commissionStakeholderModel);
   }

   /**
    * Get a list of related CommissionStakeholderModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CommissionStakeholderModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CommissionStakeholderModel> getUpdatedByCommissionStakeholderModelList() throws Exception {
      return updatedByCommissionStakeholderModelList;
   }


   /**
    * Set a list of CommissionStakeholderModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param commissionStakeholderModelList the list of related objects.
    */
   public void setUpdatedByCommissionStakeholderModelList(Collection<CommissionStakeholderModel> commissionStakeholderModelList) throws Exception {
      this.updatedByCommissionStakeholderModelList = commissionStakeholderModelList;
   }


   /**
    * Add the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be added.
    */

   public void addCreatedByCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationCreatedByAppUserModel(this);
      createdByCommissionStakeholderModelList.add(commissionStakeholderModel);
   }

   /**
    * Remove the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be removed.
    */

   public void removeCreatedByCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationCreatedByAppUserModel(null);
      createdByCommissionStakeholderModelList.remove(commissionStakeholderModel);
   }

   /**
    * Get a list of related CommissionStakeholderModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CommissionStakeholderModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CommissionStakeholderModel> getCreatedByCommissionStakeholderModelList() throws Exception {
      return createdByCommissionStakeholderModelList;
   }


   /**
    * Set a list of CommissionStakeholderModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param commissionStakeholderModelList the list of related objects.
    */
   public void setCreatedByCommissionStakeholderModelList(Collection<CommissionStakeholderModel> commissionStakeholderModelList) throws Exception {
      this.createdByCommissionStakeholderModelList = commissionStakeholderModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */

   public void addUpdatedByCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationUpdatedByAppUserModel(this);
      updatedByCommissionTransactionModelList.add(commissionTransactionModel);
   }

   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */

   public void removeUpdatedByCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationUpdatedByAppUserModel(null);
      updatedByCommissionTransactionModelList.remove(commissionTransactionModel);
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CommissionTransactionModel> getUpdatedByCommissionTransactionModelList() throws Exception {
      return updatedByCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
   public void setUpdatedByCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
      this.updatedByCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */

   public void addCreatedByCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationCreatedByAppUserModel(this);
      createdByCommissionTransactionModelList.add(commissionTransactionModel);
   }

   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */

   public void removeCreatedByCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationCreatedByAppUserModel(null);
      createdByCommissionTransactionModelList.remove(commissionTransactionModel);
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CommissionTransactionModel> getCreatedByCommissionTransactionModelList() throws Exception {
      return createdByCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
   public void setCreatedByCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
      this.createdByCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related CommissionTypeModel to this one-to-many relation.
    *
    * @param commissionTypeModel object to be added.
    */

   public void addCreatedByCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      commissionTypeModel.setRelationCreatedByAppUserModel(this);
      createdByCommissionTypeModelList.add(commissionTypeModel);
   }

   /**
    * Remove the related CommissionTypeModel to this one-to-many relation.
    *
    * @param commissionTypeModel object to be removed.
    */

   public void removeCreatedByCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      commissionTypeModel.setRelationCreatedByAppUserModel(null);
      createdByCommissionTypeModelList.remove(commissionTypeModel);
   }

   /**
    * Get a list of related CommissionTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CommissionTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CommissionTypeModel> getCreatedByCommissionTypeModelList() throws Exception {
      return createdByCommissionTypeModelList;
   }


   /**
    * Set a list of CommissionTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param commissionTypeModelList the list of related objects.
    */
   public void setCreatedByCommissionTypeModelList(Collection<CommissionTypeModel> commissionTypeModelList) throws Exception {
      this.createdByCommissionTypeModelList = commissionTypeModelList;
   }


   /**
    * Add the related CommissionTypeModel to this one-to-many relation.
    *
    * @param commissionTypeModel object to be added.
    */

   public void addUpdatedByCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      commissionTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByCommissionTypeModelList.add(commissionTypeModel);
   }

   /**
    * Remove the related CommissionTypeModel to this one-to-many relation.
    *
    * @param commissionTypeModel object to be removed.
    */

   public void removeUpdatedByCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      commissionTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByCommissionTypeModelList.remove(commissionTypeModel);
   }

   /**
    * Get a list of related CommissionTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CommissionTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CommissionTypeModel> getUpdatedByCommissionTypeModelList() throws Exception {
      return updatedByCommissionTypeModelList;
   }


   /**
    * Set a list of CommissionTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param commissionTypeModelList the list of related objects.
    */
   public void setUpdatedByCommissionTypeModelList(Collection<CommissionTypeModel> commissionTypeModelList) throws Exception {
      this.updatedByCommissionTypeModelList = commissionTypeModelList;
   }


   /**
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */

   public void addCreatedByCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationCreatedByAppUserModel(this);
      createdByCustomerModelList.add(customerModel);
   }

   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */

   public void removeCreatedByCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationCreatedByAppUserModel(null);
      createdByCustomerModelList.remove(customerModel);
   }

   /**
    * Get a list of related CustomerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of CustomerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<CustomerModel> getCreatedByCustomerModelList() throws Exception {
      return createdByCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param customerModelList the list of related objects.
    */
   public void setCreatedByCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
      this.createdByCustomerModelList = customerModelList;
   }


   /**
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */

   public void addUpdatedByCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationUpdatedByAppUserModel(this);
      updatedByCustomerModelList.add(customerModel);
   }

   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */

   public void removeUpdatedByCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationUpdatedByAppUserModel(null);
      updatedByCustomerModelList.remove(customerModel);
   }

   /**
    * Get a list of related CustomerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of CustomerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<CustomerModel> getUpdatedByCustomerModelList() throws Exception {
      return updatedByCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param customerModelList the list of related objects.
    */
   public void setUpdatedByCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
      this.updatedByCustomerModelList = customerModelList;
   }


   /**
    * Add the related DeviceTypeModel to this one-to-many relation.
    *
    * @param deviceTypeModel object to be added.
    */

   public void addCreatedByDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      deviceTypeModel.setRelationCreatedByAppUserModel(this);
      createdByDeviceTypeModelList.add(deviceTypeModel);
   }

   /**
    * Remove the related DeviceTypeModel to this one-to-many relation.
    *
    * @param deviceTypeModel object to be removed.
    */

   public void removeCreatedByDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      deviceTypeModel.setRelationCreatedByAppUserModel(null);
      createdByDeviceTypeModelList.remove(deviceTypeModel);
   }

   /**
    * Get a list of related DeviceTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DeviceTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DeviceTypeModel> getCreatedByDeviceTypeModelList() throws Exception {
      return createdByDeviceTypeModelList;
   }


   /**
    * Set a list of DeviceTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param deviceTypeModelList the list of related objects.
    */
   public void setCreatedByDeviceTypeModelList(Collection<DeviceTypeModel> deviceTypeModelList) throws Exception {
      this.createdByDeviceTypeModelList = deviceTypeModelList;
   }


   /**
    * Add the related DeviceTypeModel to this one-to-many relation.
    *
    * @param deviceTypeModel object to be added.
    */

   public void addUpdatedByDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      deviceTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByDeviceTypeModelList.add(deviceTypeModel);
   }

   /**
    * Remove the related DeviceTypeModel to this one-to-many relation.
    *
    * @param deviceTypeModel object to be removed.
    */

   public void removeUpdatedByDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      deviceTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByDeviceTypeModelList.remove(deviceTypeModel);
   }

   /**
    * Get a list of related DeviceTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DeviceTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DeviceTypeModel> getUpdatedByDeviceTypeModelList() throws Exception {
      return updatedByDeviceTypeModelList;
   }


   /**
    * Set a list of DeviceTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param deviceTypeModelList the list of related objects.
    */
   public void setUpdatedByDeviceTypeModelList(Collection<DeviceTypeModel> deviceTypeModelList) throws Exception {
      this.updatedByDeviceTypeModelList = deviceTypeModelList;
   }


   /**
    * Add the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be added.
    */

   public void addCreatedByDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationCreatedByAppUserModel(this);
      createdByDeviceTypeCommandModelList.add(deviceTypeCommandModel);
   }

   /**
    * Remove the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be removed.
    */

   public void removeCreatedByDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationCreatedByAppUserModel(null);
      createdByDeviceTypeCommandModelList.remove(deviceTypeCommandModel);
   }

   /**
    * Get a list of related DeviceTypeCommandModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DeviceTypeCommandModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DeviceTypeCommandModel> getCreatedByDeviceTypeCommandModelList() throws Exception {
      return createdByDeviceTypeCommandModelList;
   }


   /**
    * Set a list of DeviceTypeCommandModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param deviceTypeCommandModelList the list of related objects.
    */
   public void setCreatedByDeviceTypeCommandModelList(Collection<DeviceTypeCommandModel> deviceTypeCommandModelList) throws Exception {
      this.createdByDeviceTypeCommandModelList = deviceTypeCommandModelList;
   }


   /**
    * Add the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be added.
    */

   public void addUpdatedByDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationUpdatedByAppUserModel(this);
      updatedByDeviceTypeCommandModelList.add(deviceTypeCommandModel);
   }

   /**
    * Remove the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be removed.
    */

   public void removeUpdatedByDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationUpdatedByAppUserModel(null);
      updatedByDeviceTypeCommandModelList.remove(deviceTypeCommandModel);
   }

   /**
    * Get a list of related DeviceTypeCommandModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DeviceTypeCommandModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DeviceTypeCommandModel> getUpdatedByDeviceTypeCommandModelList() throws Exception {
      return updatedByDeviceTypeCommandModelList;
   }


   /**
    * Set a list of DeviceTypeCommandModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param deviceTypeCommandModelList the list of related objects.
    */
   public void setUpdatedByDeviceTypeCommandModelList(Collection<DeviceTypeCommandModel> deviceTypeCommandModelList) throws Exception {
      this.updatedByDeviceTypeCommandModelList = deviceTypeCommandModelList;
   }


   /**
    * Add the related DispenseTypeModel to this one-to-many relation.
    *
    * @param dispenseTypeModel object to be added.
    */

   public void addCreatedByDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      dispenseTypeModel.setRelationCreatedByAppUserModel(this);
      createdByDispenseTypeModelList.add(dispenseTypeModel);
   }

   /**
    * Remove the related DispenseTypeModel to this one-to-many relation.
    *
    * @param dispenseTypeModel object to be removed.
    */

   public void removeCreatedByDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      dispenseTypeModel.setRelationCreatedByAppUserModel(null);
      createdByDispenseTypeModelList.remove(dispenseTypeModel);
   }

   /**
    * Get a list of related DispenseTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DispenseTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DispenseTypeModel> getCreatedByDispenseTypeModelList() throws Exception {
      return createdByDispenseTypeModelList;
   }


   /**
    * Set a list of DispenseTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param dispenseTypeModelList the list of related objects.
    */
   public void setCreatedByDispenseTypeModelList(Collection<DispenseTypeModel> dispenseTypeModelList) throws Exception {
      this.createdByDispenseTypeModelList = dispenseTypeModelList;
   }


   /**
    * Add the related DispenseTypeModel to this one-to-many relation.
    *
    * @param dispenseTypeModel object to be added.
    */

   public void addUpdatedByDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      dispenseTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByDispenseTypeModelList.add(dispenseTypeModel);
   }

   /**
    * Remove the related DispenseTypeModel to this one-to-many relation.
    *
    * @param dispenseTypeModel object to be removed.
    */

   public void removeUpdatedByDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      dispenseTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByDispenseTypeModelList.remove(dispenseTypeModel);
   }

   /**
    * Get a list of related DispenseTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DispenseTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DispenseTypeModel> getUpdatedByDispenseTypeModelList() throws Exception {
      return updatedByDispenseTypeModelList;
   }


   /**
    * Set a list of DispenseTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param dispenseTypeModelList the list of related objects.
    */
   public void setUpdatedByDispenseTypeModelList(Collection<DispenseTypeModel> dispenseTypeModelList) throws Exception {
      this.updatedByDispenseTypeModelList = dispenseTypeModelList;
   }


   /**
    * Add the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be added.
    */

   public void addUpdatedByDistributorModel(DistributorModel distributorModel) {
      distributorModel.setRelationUpdatedByAppUserModel(this);
      updatedByDistributorModelList.add(distributorModel);
   }

   /**
    * Remove the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be removed.
    */

   public void removeUpdatedByDistributorModel(DistributorModel distributorModel) {
      distributorModel.setRelationUpdatedByAppUserModel(null);
      updatedByDistributorModelList.remove(distributorModel);
   }

   /**
    * Get a list of related DistributorModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DistributorModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DistributorModel> getUpdatedByDistributorModelList() throws Exception {
      return updatedByDistributorModelList;
   }


   /**
    * Set a list of DistributorModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param distributorModelList the list of related objects.
    */
   public void setUpdatedByDistributorModelList(Collection<DistributorModel> distributorModelList) throws Exception {
      this.updatedByDistributorModelList = distributorModelList;
   }


   /**
    * Add the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be added.
    */

   public void addCreatedByDistributorModel(DistributorModel distributorModel) {
      distributorModel.setRelationCreatedByAppUserModel(this);
      createdByDistributorModelList.add(distributorModel);
   }

   /**
    * Remove the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be removed.
    */

   public void removeCreatedByDistributorModel(DistributorModel distributorModel) {
      distributorModel.setRelationCreatedByAppUserModel(null);
      createdByDistributorModelList.remove(distributorModel);
   }

   /**
    * Get a list of related DistributorModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DistributorModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DistributorModel> getCreatedByDistributorModelList() throws Exception {
      return createdByDistributorModelList;
   }


   /**
    * Set a list of DistributorModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param distributorModelList the list of related objects.
    */
   public void setCreatedByDistributorModelList(Collection<DistributorModel> distributorModelList) throws Exception {
      this.createdByDistributorModelList = distributorModelList;
   }


   /**
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */

   public void addCreatedByDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationCreatedByAppUserModel(this);
      createdByDistributorContactModelList.add(distributorContactModel);
   }

   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */

   public void removeCreatedByDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationCreatedByAppUserModel(null);
      createdByDistributorContactModelList.remove(distributorContactModel);
   }

   /**
    * Get a list of related DistributorContactModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DistributorContactModel> getCreatedByDistributorContactModelList() throws Exception {
      return createdByDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param distributorContactModelList the list of related objects.
    */
   public void setCreatedByDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
      this.createdByDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */

   public void addUpdatedByDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationUpdatedByAppUserModel(this);
      updatedByDistributorContactModelList.add(distributorContactModel);
   }

   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */

   public void removeUpdatedByDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationUpdatedByAppUserModel(null);
      updatedByDistributorContactModelList.remove(distributorContactModel);
   }

   /**
    * Get a list of related DistributorContactModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DistributorContactModel> getUpdatedByDistributorContactModelList() throws Exception {
      return updatedByDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param distributorContactModelList the list of related objects.
    */
   public void setUpdatedByDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
      this.updatedByDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be added.
    */

   public void addCreatedByDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationCreatedByAppUserModel(this);
      createdByDistributorLevelModelList.add(distributorLevelModel);
   }

   /**
    * Remove the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be removed.
    */

   public void removeCreatedByDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationCreatedByAppUserModel(null);
      createdByDistributorLevelModelList.remove(distributorLevelModel);
   }

   /**
    * Get a list of related DistributorLevelModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of DistributorLevelModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<DistributorLevelModel> getCreatedByDistributorLevelModelList() throws Exception {
      return createdByDistributorLevelModelList;
   }


   /**
    * Set a list of DistributorLevelModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param distributorLevelModelList the list of related objects.
    */
   public void setCreatedByDistributorLevelModelList(Collection<DistributorLevelModel> distributorLevelModelList) throws Exception {
      this.createdByDistributorLevelModelList = distributorLevelModelList;
   }


   /**
    * Add the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be added.
    */

   public void addUpdatedByDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationUpdatedByAppUserModel(this);
      updatedByDistributorLevelModelList.add(distributorLevelModel);
   }

   /**
    * Remove the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be removed.
    */

   public void removeUpdatedByDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationUpdatedByAppUserModel(null);
      updatedByDistributorLevelModelList.remove(distributorLevelModel);
   }

   /**
    * Get a list of related DistributorLevelModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of DistributorLevelModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<DistributorLevelModel> getUpdatedByDistributorLevelModelList() throws Exception {
      return updatedByDistributorLevelModelList;
   }


   /**
    * Set a list of DistributorLevelModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param distributorLevelModelList the list of related objects.
    */
   public void setUpdatedByDistributorLevelModelList(Collection<DistributorLevelModel> distributorLevelModelList) throws Exception {
      this.updatedByDistributorLevelModelList = distributorLevelModelList;
   }


   /**
    * Add the related FailureReasonModel to this one-to-many relation.
    *
    * @param failureReasonModel object to be added.
    */

   public void addUpdatedByFailureReasonModel(FailureReasonModel failureReasonModel) {
      failureReasonModel.setRelationUpdatedByAppUserModel(this);
      updatedByFailureReasonModelList.add(failureReasonModel);
   }

   /**
    * Remove the related FailureReasonModel to this one-to-many relation.
    *
    * @param failureReasonModel object to be removed.
    */

   public void removeUpdatedByFailureReasonModel(FailureReasonModel failureReasonModel) {
      failureReasonModel.setRelationUpdatedByAppUserModel(null);
      updatedByFailureReasonModelList.remove(failureReasonModel);
   }

   /**
    * Get a list of related FailureReasonModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of FailureReasonModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<FailureReasonModel> getUpdatedByFailureReasonModelList() throws Exception {
      return updatedByFailureReasonModelList;
   }


   /**
    * Set a list of FailureReasonModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param failureReasonModelList the list of related objects.
    */
   public void setUpdatedByFailureReasonModelList(Collection<FailureReasonModel> failureReasonModelList) throws Exception {
      this.updatedByFailureReasonModelList = failureReasonModelList;
   }


   /**
    * Add the related FailureReasonModel to this one-to-many relation.
    *
    * @param failureReasonModel object to be added.
    */

   public void addCreatedByFailureReasonModel(FailureReasonModel failureReasonModel) {
      failureReasonModel.setRelationCreatedByAppUserModel(this);
      createdByFailureReasonModelList.add(failureReasonModel);
   }

   /**
    * Remove the related FailureReasonModel to this one-to-many relation.
    *
    * @param failureReasonModel object to be removed.
    */

   public void removeCreatedByFailureReasonModel(FailureReasonModel failureReasonModel) {
      failureReasonModel.setRelationCreatedByAppUserModel(null);
      createdByFailureReasonModelList.remove(failureReasonModel);
   }

   /**
    * Get a list of related FailureReasonModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of FailureReasonModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<FailureReasonModel> getCreatedByFailureReasonModelList() throws Exception {
      return createdByFailureReasonModelList;
   }


   /**
    * Set a list of FailureReasonModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param failureReasonModelList the list of related objects.
    */
   public void setCreatedByFailureReasonModelList(Collection<FailureReasonModel> failureReasonModelList) throws Exception {
      this.createdByFailureReasonModelList = failureReasonModelList;
   }


   /**
    * Add the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be added.
    */

   public void addUpdatedByFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationUpdatedByAppUserModel(this);
      updatedByFavoriteNumbersModelList.add(favoriteNumbersModel);
   }

   /**
    * Remove the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be removed.
    */

   public void removeUpdatedByFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationUpdatedByAppUserModel(null);
      updatedByFavoriteNumbersModelList.remove(favoriteNumbersModel);
   }

   /**
    * Get a list of related FavoriteNumbersModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of FavoriteNumbersModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<FavoriteNumbersModel> getUpdatedByFavoriteNumbersModelList() throws Exception {
      return updatedByFavoriteNumbersModelList;
   }


   /**
    * Set a list of FavoriteNumbersModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param favoriteNumbersModelList the list of related objects.
    */
   public void setUpdatedByFavoriteNumbersModelList(Collection<FavoriteNumbersModel> favoriteNumbersModelList) throws Exception {
      this.updatedByFavoriteNumbersModelList = favoriteNumbersModelList;
   }


   /**
    * Add the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be added.
    */

   public void addAppUserIdFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationAppUserIdAppUserModel(this);
      appUserIdFavoriteNumbersModelList.add(favoriteNumbersModel);
   }

   /**
    * Remove the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be removed.
    */

   public void removeAppUserIdFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationAppUserIdAppUserModel(null);
      appUserIdFavoriteNumbersModelList.remove(favoriteNumbersModel);
   }

   /**
    * Get a list of related FavoriteNumbersModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of FavoriteNumbersModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<FavoriteNumbersModel> getAppUserIdFavoriteNumbersModelList() throws Exception {
      return appUserIdFavoriteNumbersModelList;
   }


   /**
    * Set a list of FavoriteNumbersModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param favoriteNumbersModelList the list of related objects.
    */
   public void setAppUserIdFavoriteNumbersModelList(Collection<FavoriteNumbersModel> favoriteNumbersModelList) throws Exception {
      this.appUserIdFavoriteNumbersModelList = favoriteNumbersModelList;
   }


   /**
    * Add the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be added.
    */

   public void addCreatedByFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationCreatedByAppUserModel(this);
      createdByFavoriteNumbersModelList.add(favoriteNumbersModel);
   }

   /**
    * Remove the related FavoriteNumbersModel to this one-to-many relation.
    *
    * @param favoriteNumbersModel object to be removed.
    */

   public void removeCreatedByFavoriteNumbersModel(FavoriteNumbersModel favoriteNumbersModel) {
      favoriteNumbersModel.setRelationCreatedByAppUserModel(null);
      createdByFavoriteNumbersModelList.remove(favoriteNumbersModel);
   }

   /**
    * Get a list of related FavoriteNumbersModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of FavoriteNumbersModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<FavoriteNumbersModel> getCreatedByFavoriteNumbersModelList() throws Exception {
      return createdByFavoriteNumbersModelList;
   }


   /**
    * Set a list of FavoriteNumbersModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param favoriteNumbersModelList the list of related objects.
    */
   public void setCreatedByFavoriteNumbersModelList(Collection<FavoriteNumbersModel> favoriteNumbersModelList) throws Exception {
      this.createdByFavoriteNumbersModelList = favoriteNumbersModelList;
   }


   /**
    * Add the related IntegrationModuleModel to this one-to-many relation.
    *
    * @param integrationModuleModel object to be added.
    */

   public void addCreatedByIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      integrationModuleModel.setRelationCreatedByAppUserModel(this);
      createdByIntegrationModuleModelList.add(integrationModuleModel);
   }

   /**
    * Remove the related IntegrationModuleModel to this one-to-many relation.
    *
    * @param integrationModuleModel object to be removed.
    */

   public void removeCreatedByIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      integrationModuleModel.setRelationCreatedByAppUserModel(null);
      createdByIntegrationModuleModelList.remove(integrationModuleModel);
   }

   /**
    * Get a list of related IntegrationModuleModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IntegrationModuleModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IntegrationModuleModel> getCreatedByIntegrationModuleModelList() throws Exception {
      return createdByIntegrationModuleModelList;
   }


   /**
    * Set a list of IntegrationModuleModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param integrationModuleModelList the list of related objects.
    */
   public void setCreatedByIntegrationModuleModelList(Collection<IntegrationModuleModel> integrationModuleModelList) throws Exception {
      this.createdByIntegrationModuleModelList = integrationModuleModelList;
   }


   /**
    * Add the related IntegrationModuleModel to this one-to-many relation.
    *
    * @param integrationModuleModel object to be added.
    */

   public void addUpdatedByIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      integrationModuleModel.setRelationUpdatedByAppUserModel(this);
      updatedByIntegrationModuleModelList.add(integrationModuleModel);
   }

   /**
    * Remove the related IntegrationModuleModel to this one-to-many relation.
    *
    * @param integrationModuleModel object to be removed.
    */

   public void removeUpdatedByIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      integrationModuleModel.setRelationUpdatedByAppUserModel(null);
      updatedByIntegrationModuleModelList.remove(integrationModuleModel);
   }

   /**
    * Get a list of related IntegrationModuleModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IntegrationModuleModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IntegrationModuleModel> getUpdatedByIntegrationModuleModelList() throws Exception {
      return updatedByIntegrationModuleModelList;
   }


   /**
    * Set a list of IntegrationModuleModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param integrationModuleModelList the list of related objects.
    */
   public void setUpdatedByIntegrationModuleModelList(Collection<IntegrationModuleModel> integrationModuleModelList) throws Exception {
      this.updatedByIntegrationModuleModelList = integrationModuleModelList;
   }


   /**
    * Add the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be added.
    */

   public void addUpdatedByIssueModel(IssueModel issueModel) {
      issueModel.setRelationUpdatedByAppUserModel(this);
      updatedByIssueModelList.add(issueModel);
   }

   /**
    * Remove the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be removed.
    */

   public void removeUpdatedByIssueModel(IssueModel issueModel) {
      issueModel.setRelationUpdatedByAppUserModel(null);
      updatedByIssueModelList.remove(issueModel);
   }

   /**
    * Get a list of related IssueModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IssueModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IssueModel> getUpdatedByIssueModelList() throws Exception {
      return updatedByIssueModelList;
   }


   /**
    * Set a list of IssueModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param issueModelList the list of related objects.
    */
   public void setUpdatedByIssueModelList(Collection<IssueModel> issueModelList) throws Exception {
      this.updatedByIssueModelList = issueModelList;
   }


   /**
    * Add the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be added.
    */

   public void addCreatedByIssueModel(IssueModel issueModel) {
      issueModel.setRelationCreatedByAppUserModel(this);
      createdByIssueModelList.add(issueModel);
   }

   /**
    * Remove the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be removed.
    */

   public void removeCreatedByIssueModel(IssueModel issueModel) {
      issueModel.setRelationCreatedByAppUserModel(null);
      createdByIssueModelList.remove(issueModel);
   }

   /**
    * Get a list of related IssueModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IssueModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IssueModel> getCreatedByIssueModelList() throws Exception {
      return createdByIssueModelList;
   }


   /**
    * Set a list of IssueModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param issueModelList the list of related objects.
    */
   public void setCreatedByIssueModelList(Collection<IssueModel> issueModelList) throws Exception {
      this.createdByIssueModelList = issueModelList;
   }


   /**
    * Add the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be added.
    */

   public void addCreatedByIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationCreatedByAppUserModel(this);
      createdByIssueHistoryModelList.add(issueHistoryModel);
   }

   /**
    * Remove the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be removed.
    */

   public void removeCreatedByIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationCreatedByAppUserModel(null);
      createdByIssueHistoryModelList.remove(issueHistoryModel);
   }

   /**
    * Get a list of related IssueHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IssueHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IssueHistoryModel> getCreatedByIssueHistoryModelList() throws Exception {
      return createdByIssueHistoryModelList;
   }


   /**
    * Set a list of IssueHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param issueHistoryModelList the list of related objects.
    */
   public void setCreatedByIssueHistoryModelList(Collection<IssueHistoryModel> issueHistoryModelList) throws Exception {
      this.createdByIssueHistoryModelList = issueHistoryModelList;
   }


   /**
    * Add the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be added.
    */

   public void addUpdatedByIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationUpdatedByAppUserModel(this);
      updatedByIssueHistoryModelList.add(issueHistoryModel);
   }

   /**
    * Remove the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be removed.
    */

   public void removeUpdatedByIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationUpdatedByAppUserModel(null);
      updatedByIssueHistoryModelList.remove(issueHistoryModel);
   }

   /**
    * Get a list of related IssueHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IssueHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IssueHistoryModel> getUpdatedByIssueHistoryModelList() throws Exception {
      return updatedByIssueHistoryModelList;
   }


   /**
    * Set a list of IssueHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param issueHistoryModelList the list of related objects.
    */
   public void setUpdatedByIssueHistoryModelList(Collection<IssueHistoryModel> issueHistoryModelList) throws Exception {
      this.updatedByIssueHistoryModelList = issueHistoryModelList;
   }


   /**
    * Add the related IssueStatusModel to this one-to-many relation.
    *
    * @param issueStatusModel object to be added.
    */

   public void addCreatedByIssueStatusModel(IssueStatusModel issueStatusModel) {
      issueStatusModel.setRelationCreatedByAppUserModel(this);
      createdByIssueStatusModelList.add(issueStatusModel);
   }

   /**
    * Remove the related IssueStatusModel to this one-to-many relation.
    *
    * @param issueStatusModel object to be removed.
    */

   public void removeCreatedByIssueStatusModel(IssueStatusModel issueStatusModel) {
      issueStatusModel.setRelationCreatedByAppUserModel(null);
      createdByIssueStatusModelList.remove(issueStatusModel);
   }

   /**
    * Get a list of related IssueStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IssueStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IssueStatusModel> getCreatedByIssueStatusModelList() throws Exception {
      return createdByIssueStatusModelList;
   }


   /**
    * Set a list of IssueStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param issueStatusModelList the list of related objects.
    */
   public void setCreatedByIssueStatusModelList(Collection<IssueStatusModel> issueStatusModelList) throws Exception {
      this.createdByIssueStatusModelList = issueStatusModelList;
   }


   /**
    * Add the related IssueStatusModel to this one-to-many relation.
    *
    * @param issueStatusModel object to be added.
    */

   public void addUpdatedByIssueStatusModel(IssueStatusModel issueStatusModel) {
      issueStatusModel.setRelationUpdatedByAppUserModel(this);
      updatedByIssueStatusModelList.add(issueStatusModel);
   }

   /**
    * Remove the related IssueStatusModel to this one-to-many relation.
    *
    * @param issueStatusModel object to be removed.
    */

   public void removeUpdatedByIssueStatusModel(IssueStatusModel issueStatusModel) {
      issueStatusModel.setRelationUpdatedByAppUserModel(null);
      updatedByIssueStatusModelList.remove(issueStatusModel);
   }

   /**
    * Get a list of related IssueStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IssueStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IssueStatusModel> getUpdatedByIssueStatusModelList() throws Exception {
      return updatedByIssueStatusModelList;
   }


   /**
    * Set a list of IssueStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param issueStatusModelList the list of related objects.
    */
   public void setUpdatedByIssueStatusModelList(Collection<IssueStatusModel> issueStatusModelList) throws Exception {
      this.updatedByIssueStatusModelList = issueStatusModelList;
   }


   /**
    * Add the related IssueTypeModel to this one-to-many relation.
    *
    * @param issueTypeModel object to be added.
    */

   public void addCreatedByIssueTypeModel(IssueTypeModel issueTypeModel) {
      issueTypeModel.setRelationCreatedByAppUserModel(this);
      createdByIssueTypeModelList.add(issueTypeModel);
   }

   /**
    * Remove the related IssueTypeModel to this one-to-many relation.
    *
    * @param issueTypeModel object to be removed.
    */

   public void removeCreatedByIssueTypeModel(IssueTypeModel issueTypeModel) {
      issueTypeModel.setRelationCreatedByAppUserModel(null);
      createdByIssueTypeModelList.remove(issueTypeModel);
   }

   /**
    * Get a list of related IssueTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IssueTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IssueTypeModel> getCreatedByIssueTypeModelList() throws Exception {
      return createdByIssueTypeModelList;
   }


   /**
    * Set a list of IssueTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param issueTypeModelList the list of related objects.
    */
   public void setCreatedByIssueTypeModelList(Collection<IssueTypeModel> issueTypeModelList) throws Exception {
      this.createdByIssueTypeModelList = issueTypeModelList;
   }


   /**
    * Add the related IssueTypeModel to this one-to-many relation.
    *
    * @param issueTypeModel object to be added.
    */

   public void addUpdatedByIssueTypeModel(IssueTypeModel issueTypeModel) {
      issueTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByIssueTypeModelList.add(issueTypeModel);
   }

   /**
    * Remove the related IssueTypeModel to this one-to-many relation.
    *
    * @param issueTypeModel object to be removed.
    */

   public void removeUpdatedByIssueTypeModel(IssueTypeModel issueTypeModel) {
      issueTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByIssueTypeModelList.remove(issueTypeModel);
   }

   /**
    * Get a list of related IssueTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IssueTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IssueTypeModel> getUpdatedByIssueTypeModelList() throws Exception {
      return updatedByIssueTypeModelList;
   }


   /**
    * Set a list of IssueTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param issueTypeModelList the list of related objects.
    */
   public void setUpdatedByIssueTypeModelList(Collection<IssueTypeModel> issueTypeModelList) throws Exception {
      this.updatedByIssueTypeModelList = issueTypeModelList;
   }


   /**
    * Add the related IssueTypeStatusModel to this one-to-many relation.
    *
    * @param issueTypeStatusModel object to be added.
    */

   public void addCreatedByIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      issueTypeStatusModel.setRelationCreatedByAppUserModel(this);
      createdByIssueTypeStatusModelList.add(issueTypeStatusModel);
   }

   /**
    * Remove the related IssueTypeStatusModel to this one-to-many relation.
    *
    * @param issueTypeStatusModel object to be removed.
    */

   public void removeCreatedByIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      issueTypeStatusModel.setRelationCreatedByAppUserModel(null);
      createdByIssueTypeStatusModelList.remove(issueTypeStatusModel);
   }

   /**
    * Get a list of related IssueTypeStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of IssueTypeStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<IssueTypeStatusModel> getCreatedByIssueTypeStatusModelList() throws Exception {
      return createdByIssueTypeStatusModelList;
   }


   /**
    * Set a list of IssueTypeStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param issueTypeStatusModelList the list of related objects.
    */
   public void setCreatedByIssueTypeStatusModelList(Collection<IssueTypeStatusModel> issueTypeStatusModelList) throws Exception {
      this.createdByIssueTypeStatusModelList = issueTypeStatusModelList;
   }


   /**
    * Add the related IssueTypeStatusModel to this one-to-many relation.
    *
    * @param issueTypeStatusModel object to be added.
    */

   public void addUpdatedByIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      issueTypeStatusModel.setRelationUpdatedByAppUserModel(this);
      updatedByIssueTypeStatusModelList.add(issueTypeStatusModel);
   }

   /**
    * Remove the related IssueTypeStatusModel to this one-to-many relation.
    *
    * @param issueTypeStatusModel object to be removed.
    */

   public void removeUpdatedByIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      issueTypeStatusModel.setRelationUpdatedByAppUserModel(null);
      updatedByIssueTypeStatusModelList.remove(issueTypeStatusModel);
   }

   /**
    * Get a list of related IssueTypeStatusModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of IssueTypeStatusModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<IssueTypeStatusModel> getUpdatedByIssueTypeStatusModelList() throws Exception {
      return updatedByIssueTypeStatusModelList;
   }


   /**
    * Set a list of IssueTypeStatusModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param issueTypeStatusModelList the list of related objects.
    */
   public void setUpdatedByIssueTypeStatusModelList(Collection<IssueTypeStatusModel> issueTypeStatusModelList) throws Exception {
      this.updatedByIssueTypeStatusModelList = issueTypeStatusModelList;
   }


   /**
    * Add the related MessageTypeModel to this one-to-many relation.
    *
    * @param messageTypeModel object to be added.
    */

   public void addCreatedByMessageTypeModel(MessageTypeModel messageTypeModel) {
      messageTypeModel.setRelationCreatedByAppUserModel(this);
      createdByMessageTypeModelList.add(messageTypeModel);
   }

   /**
    * Remove the related MessageTypeModel to this one-to-many relation.
    *
    * @param messageTypeModel object to be removed.
    */

   public void removeCreatedByMessageTypeModel(MessageTypeModel messageTypeModel) {
      messageTypeModel.setRelationCreatedByAppUserModel(null);
      createdByMessageTypeModelList.remove(messageTypeModel);
   }

   /**
    * Get a list of related MessageTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of MessageTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<MessageTypeModel> getCreatedByMessageTypeModelList() throws Exception {
      return createdByMessageTypeModelList;
   }


   /**
    * Set a list of MessageTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param messageTypeModelList the list of related objects.
    */
   public void setCreatedByMessageTypeModelList(Collection<MessageTypeModel> messageTypeModelList) throws Exception {
      this.createdByMessageTypeModelList = messageTypeModelList;
   }


   /**
    * Add the related MessageTypeModel to this one-to-many relation.
    *
    * @param messageTypeModel object to be added.
    */

   public void addUpdatedByMessageTypeModel(MessageTypeModel messageTypeModel) {
      messageTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByMessageTypeModelList.add(messageTypeModel);
   }

   /**
    * Remove the related MessageTypeModel to this one-to-many relation.
    *
    * @param messageTypeModel object to be removed.
    */

   public void removeUpdatedByMessageTypeModel(MessageTypeModel messageTypeModel) {
      messageTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByMessageTypeModelList.remove(messageTypeModel);
   }

   /**
    * Get a list of related MessageTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of MessageTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<MessageTypeModel> getUpdatedByMessageTypeModelList() throws Exception {
      return updatedByMessageTypeModelList;
   }


   /**
    * Set a list of MessageTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param messageTypeModelList the list of related objects.
    */
   public void setUpdatedByMessageTypeModelList(Collection<MessageTypeModel> messageTypeModelList) throws Exception {
      this.updatedByMessageTypeModelList = messageTypeModelList;
   }


   /**
    * Add the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be added.
    */

   public void addUpdatedByMnoModel(MnoModel mnoModel) {
      mnoModel.setRelationUpdatedByAppUserModel(this);
      updatedByMnoModelList.add(mnoModel);
   }

   /**
    * Remove the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be removed.
    */

   public void removeUpdatedByMnoModel(MnoModel mnoModel) {
      mnoModel.setRelationUpdatedByAppUserModel(null);
      updatedByMnoModelList.remove(mnoModel);
   }

   /**
    * Get a list of related MnoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of MnoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<MnoModel> getUpdatedByMnoModelList() throws Exception {
      return updatedByMnoModelList;
   }


   /**
    * Set a list of MnoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param mnoModelList the list of related objects.
    */
   public void setUpdatedByMnoModelList(Collection<MnoModel> mnoModelList) throws Exception {
      this.updatedByMnoModelList = mnoModelList;
   }


   /**
    * Add the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be added.
    */

   public void addCreatedByMnoModel(MnoModel mnoModel) {
      mnoModel.setRelationCreatedByAppUserModel(this);
      createdByMnoModelList.add(mnoModel);
   }

   /**
    * Remove the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be removed.
    */

   public void removeCreatedByMnoModel(MnoModel mnoModel) {
      mnoModel.setRelationCreatedByAppUserModel(null);
      createdByMnoModelList.remove(mnoModel);
   }

   /**
    * Get a list of related MnoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of MnoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<MnoModel> getCreatedByMnoModelList() throws Exception {
      return createdByMnoModelList;
   }


   /**
    * Set a list of MnoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param mnoModelList the list of related objects.
    */
   public void setCreatedByMnoModelList(Collection<MnoModel> mnoModelList) throws Exception {
      this.createdByMnoModelList = mnoModelList;
   }


   /**
    * Add the related MnoDialingCodeModel to this one-to-many relation.
    *
    * @param mnoDialingCodeModel object to be added.
    */

   public void addCreatedByMnoDialingCodeModel(MnoDialingCodeModel mnoDialingCodeModel) {
      mnoDialingCodeModel.setRelationCreatedByAppUserModel(this);
      createdByMnoDialingCodeModelList.add(mnoDialingCodeModel);
   }

   /**
    * Remove the related MnoDialingCodeModel to this one-to-many relation.
    *
    * @param mnoDialingCodeModel object to be removed.
    */

   public void removeCreatedByMnoDialingCodeModel(MnoDialingCodeModel mnoDialingCodeModel) {
      mnoDialingCodeModel.setRelationCreatedByAppUserModel(null);
      createdByMnoDialingCodeModelList.remove(mnoDialingCodeModel);
   }

   /**
    * Get a list of related MnoDialingCodeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of MnoDialingCodeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<MnoDialingCodeModel> getCreatedByMnoDialingCodeModelList() throws Exception {
      return createdByMnoDialingCodeModelList;
   }


   /**
    * Set a list of MnoDialingCodeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param mnoDialingCodeModelList the list of related objects.
    */
   public void setCreatedByMnoDialingCodeModelList(Collection<MnoDialingCodeModel> mnoDialingCodeModelList) throws Exception {
      this.createdByMnoDialingCodeModelList = mnoDialingCodeModelList;
   }


   /**
    * Add the related MnoUserModel to this one-to-many relation.
    *
    * @param mnoUserModel object to be added.
    */

   public void addUpdatedByMnoUserModel(MnoUserModel mnoUserModel) {
      mnoUserModel.setRelationUpdatedByAppUserModel(this);
      updatedByMnoUserModelList.add(mnoUserModel);
   }

   /**
    * Remove the related MnoUserModel to this one-to-many relation.
    *
    * @param mnoUserModel object to be removed.
    */

   public void removeUpdatedByMnoUserModel(MnoUserModel mnoUserModel) {
      mnoUserModel.setRelationUpdatedByAppUserModel(null);
      updatedByMnoUserModelList.remove(mnoUserModel);
   }

   /**
    * Get a list of related MnoUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of MnoUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<MnoUserModel> getUpdatedByMnoUserModelList() throws Exception {
      return updatedByMnoUserModelList;
   }


   /**
    * Set a list of MnoUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param mnoUserModelList the list of related objects.
    */
   public void setUpdatedByMnoUserModelList(Collection<MnoUserModel> mnoUserModelList) throws Exception {
      this.updatedByMnoUserModelList = mnoUserModelList;
   }


   /**
    * Add the related MnoUserModel to this one-to-many relation.
    *
    * @param mnoUserModel object to be added.
    */

   public void addCreatedByMnoUserModel(MnoUserModel mnoUserModel) {
      mnoUserModel.setRelationCreatedByAppUserModel(this);
      createdByMnoUserModelList.add(mnoUserModel);
   }

   /**
    * Remove the related MnoUserModel to this one-to-many relation.
    *
    * @param mnoUserModel object to be removed.
    */

   public void removeCreatedByMnoUserModel(MnoUserModel mnoUserModel) {
      mnoUserModel.setRelationCreatedByAppUserModel(null);
      createdByMnoUserModelList.remove(mnoUserModel);
   }

   /**
    * Get a list of related MnoUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of MnoUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<MnoUserModel> getCreatedByMnoUserModelList() throws Exception {
      return createdByMnoUserModelList;
   }


   /**
    * Set a list of MnoUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param mnoUserModelList the list of related objects.
    */
   public void setCreatedByMnoUserModelList(Collection<MnoUserModel> mnoUserModelList) throws Exception {
      this.createdByMnoUserModelList = mnoUserModelList;
   }


   /**
    * Add the related NotificationMessageModel to this one-to-many relation.
    *
    * @param notificationMessageModel object to be added.
    */

   public void addUpdatedByNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      notificationMessageModel.setRelationUpdatedByAppUserModel(this);
      updatedByNotificationMessageModelList.add(notificationMessageModel);
   }

   /**
    * Remove the related NotificationMessageModel to this one-to-many relation.
    *
    * @param notificationMessageModel object to be removed.
    */

   public void removeUpdatedByNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      notificationMessageModel.setRelationUpdatedByAppUserModel(null);
      updatedByNotificationMessageModelList.remove(notificationMessageModel);
   }

   /**
    * Get a list of related NotificationMessageModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of NotificationMessageModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<NotificationMessageModel> getUpdatedByNotificationMessageModelList() throws Exception {
      return updatedByNotificationMessageModelList;
   }


   /**
    * Set a list of NotificationMessageModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param notificationMessageModelList the list of related objects.
    */
   public void setUpdatedByNotificationMessageModelList(Collection<NotificationMessageModel> notificationMessageModelList) throws Exception {
      this.updatedByNotificationMessageModelList = notificationMessageModelList;
   }


   /**
    * Add the related NotificationMessageModel to this one-to-many relation.
    *
    * @param notificationMessageModel object to be added.
    */

   public void addCreatedByNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      notificationMessageModel.setRelationCreatedByAppUserModel(this);
      createdByNotificationMessageModelList.add(notificationMessageModel);
   }

   /**
    * Remove the related NotificationMessageModel to this one-to-many relation.
    *
    * @param notificationMessageModel object to be removed.
    */

   public void removeCreatedByNotificationMessageModel(NotificationMessageModel notificationMessageModel) {
      notificationMessageModel.setRelationCreatedByAppUserModel(null);
      createdByNotificationMessageModelList.remove(notificationMessageModel);
   }

   /**
    * Get a list of related NotificationMessageModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of NotificationMessageModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<NotificationMessageModel> getCreatedByNotificationMessageModelList() throws Exception {
      return createdByNotificationMessageModelList;
   }


   /**
    * Set a list of NotificationMessageModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param notificationMessageModelList the list of related objects.
    */
   public void setCreatedByNotificationMessageModelList(Collection<NotificationMessageModel> notificationMessageModelList) throws Exception {
      this.createdByNotificationMessageModelList = notificationMessageModelList;
   }

   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */

   public void removeCreatedByPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationCreatedByAppUserModel(null);
      createdByPartnerModelList.remove(partnerModel);
   }

   /**
    * Get a list of related PartnerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PartnerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PartnerModel> getCreatedByPartnerModelList() throws Exception {
      return createdByPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param partnerModelList the list of related objects.
    */
   public void setCreatedByPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
      this.createdByPartnerModelList = partnerModelList;
   }


   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */

   public void addUpdatedByPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationUpdatedByAppUserModel(this);
      updatedByPartnerModelList.add(partnerModel);
   }

   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */

   public void removeUpdatedByPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationUpdatedByAppUserModel(null);
      updatedByPartnerModelList.remove(partnerModel);
   }

   /**
    * Get a list of related PartnerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PartnerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PartnerModel> getUpdatedByPartnerModelList() throws Exception {
      return updatedByPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param partnerModelList the list of related objects.
    */
   public void setUpdatedByPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
      this.updatedByPartnerModelList = partnerModelList;
   }


   /**
    * Add the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be added.
    */

   public void addUpdatedByPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      partnerGroupModel.setRelationUpdatedByAppUserModel(this);
      updatedByPartnerGroupModelList.add(partnerGroupModel);
   }

   /**
    * Remove the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be removed.
    */

   public void removeUpdatedByPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      partnerGroupModel.setRelationUpdatedByAppUserModel(null);
      updatedByPartnerGroupModelList.remove(partnerGroupModel);
   }

   /**
    * Get a list of related PartnerGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PartnerGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PartnerGroupModel> getUpdatedByPartnerGroupModelList() throws Exception {
      return updatedByPartnerGroupModelList;
   }


   /**
    * Set a list of PartnerGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param partnerGroupModelList the list of related objects.
    */
   public void setUpdatedByPartnerGroupModelList(Collection<PartnerGroupModel> partnerGroupModelList) throws Exception {
      this.updatedByPartnerGroupModelList = partnerGroupModelList;
   }


   /**
    * Add the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be added.
    */

   public void addCreatedByPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      partnerGroupModel.setRelationCreatedByAppUserModel(this);
      createdByPartnerGroupModelList.add(partnerGroupModel);
   }

   /**
    * Remove the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be removed.
    */

   public void removeCreatedByPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      partnerGroupModel.setRelationCreatedByAppUserModel(null);
      createdByPartnerGroupModelList.remove(partnerGroupModel);
   }

   /**
    * Get a list of related PartnerGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PartnerGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PartnerGroupModel> getCreatedByPartnerGroupModelList() throws Exception {
      return createdByPartnerGroupModelList;
   }


   /**
    * Set a list of PartnerGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param partnerGroupModelList the list of related objects.
    */
   public void setCreatedByPartnerGroupModelList(Collection<PartnerGroupModel> partnerGroupModelList) throws Exception {
      this.createdByPartnerGroupModelList = partnerGroupModelList;
   }


   /**
    * Add the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be added.
    */

   public void addUpdatedByPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationUpdatedByAppUserModel(this);
      updatedByPartnerGroupDetailModelList.add(partnerGroupDetailModel);
   }

   /**
    * Remove the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be removed.
    */

   public void removeUpdatedByPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationUpdatedByAppUserModel(null);
      updatedByPartnerGroupDetailModelList.remove(partnerGroupDetailModel);
   }

   /**
    * Get a list of related PartnerGroupDetailModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PartnerGroupDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PartnerGroupDetailModel> getUpdatedByPartnerGroupDetailModelList() throws Exception {
      return updatedByPartnerGroupDetailModelList;
   }


   /**
    * Set a list of PartnerGroupDetailModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param partnerGroupDetailModelList the list of related objects.
    */
   public void setUpdatedByPartnerGroupDetailModelList(Collection<PartnerGroupDetailModel> partnerGroupDetailModelList) throws Exception {
      this.updatedByPartnerGroupDetailModelList = partnerGroupDetailModelList;
   }


   /**
    * Add the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be added.
    */

   public void addCreatedByPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationCreatedByAppUserModel(this);
      createdByPartnerGroupDetailModelList.add(partnerGroupDetailModel);
   }

   /**
    * Remove the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be removed.
    */

   public void removeCreatedByPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationCreatedByAppUserModel(null);
      createdByPartnerGroupDetailModelList.remove(partnerGroupDetailModel);
   }

   /**
    * Get a list of related PartnerGroupDetailModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PartnerGroupDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PartnerGroupDetailModel> getCreatedByPartnerGroupDetailModelList() throws Exception {
      return createdByPartnerGroupDetailModelList;
   }


   /**
    * Set a list of PartnerGroupDetailModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param partnerGroupDetailModelList the list of related objects.
    */
   public void setCreatedByPartnerGroupDetailModelList(Collection<PartnerGroupDetailModel> partnerGroupDetailModelList) throws Exception {
      this.createdByPartnerGroupDetailModelList = partnerGroupDetailModelList;
   }

   /**
    * Add the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be added.
    */

   public void addUpdatedByPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {
      partnerIpAddressModel.setRelationUpdatedByAppUserModel(this);
      updatedByPartnerIpAddressModelList.add(partnerIpAddressModel);
   }

   /**
    * Remove the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be removed.
    */

   public void removeUpdatedByPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {
      partnerIpAddressModel.setRelationUpdatedByAppUserModel(null);
      updatedByPartnerIpAddressModelList.remove(partnerIpAddressModel);
   }

   /**
    * Get a list of related PartnerIpAddressModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PartnerIpAddressModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PartnerIpAddressModel> getUpdatedByPartnerIpAddressModelList() throws Exception {
      return updatedByPartnerIpAddressModelList;
   }


   /**
    * Set a list of PartnerIpAddressModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param partnerIpAddressModelList the list of related objects.
    */
   public void setUpdatedByPartnerIpAddressModelList(Collection<PartnerIpAddressModel> partnerIpAddressModelList) throws Exception {
      this.updatedByPartnerIpAddressModelList = partnerIpAddressModelList;
   }


   /**
    * Add the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be added.
    */

   public void addCreatedByPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {
      partnerIpAddressModel.setRelationCreatedByAppUserModel(this);
      createdByPartnerIpAddressModelList.add(partnerIpAddressModel);
   }

   /**
    * Remove the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be removed.
    */

   public void removeCreatedByPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {
      partnerIpAddressModel.setRelationCreatedByAppUserModel(null);
      createdByPartnerIpAddressModelList.remove(partnerIpAddressModel);
   }

   /**
    * Get a list of related PartnerIpAddressModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PartnerIpAddressModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PartnerIpAddressModel> getCreatedByPartnerIpAddressModelList() throws Exception {
      return createdByPartnerIpAddressModelList;
   }


   /**
    * Set a list of PartnerIpAddressModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param partnerIpAddressModelList the list of related objects.
    */
   public void setCreatedByPartnerIpAddressModelList(Collection<PartnerIpAddressModel> partnerIpAddressModelList) throws Exception {
      this.createdByPartnerIpAddressModelList = partnerIpAddressModelList;
   }

   /**
    * Add the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be added.
    */

   public void addUpdatedByPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationUpdatedByAppUserModel(this);
      updatedByPartnerPermissionGroupModelList.add(partnerPermissionGroupModel);
   }

   /**
    * Remove the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be removed.
    */

   public void removeUpdatedByPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationUpdatedByAppUserModel(null);
      updatedByPartnerPermissionGroupModelList.remove(partnerPermissionGroupModel);
   }

   /**
    * Get a list of related PartnerPermissionGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PartnerPermissionGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PartnerPermissionGroupModel> getUpdatedByPartnerPermissionGroupModelList() throws Exception {
      return updatedByPartnerPermissionGroupModelList;
   }


   /**
    * Set a list of PartnerPermissionGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param partnerPermissionGroupModelList the list of related objects.
    */
   public void setUpdatedByPartnerPermissionGroupModelList(Collection<PartnerPermissionGroupModel> partnerPermissionGroupModelList) throws Exception {
      this.updatedByPartnerPermissionGroupModelList = partnerPermissionGroupModelList;
   }


   /**
    * Add the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be added.
    */

   public void addCreatedByPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationCreatedByAppUserModel(this);
      createdByPartnerPermissionGroupModelList.add(partnerPermissionGroupModel);
   }

   /**
    * Remove the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be removed.
    */

   public void removeCreatedByPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationCreatedByAppUserModel(null);
      createdByPartnerPermissionGroupModelList.remove(partnerPermissionGroupModel);
   }

   /**
    * Get a list of related PartnerPermissionGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PartnerPermissionGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PartnerPermissionGroupModel> getCreatedByPartnerPermissionGroupModelList() throws Exception {
      return createdByPartnerPermissionGroupModelList;
   }


   /**
    * Set a list of PartnerPermissionGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param partnerPermissionGroupModelList the list of related objects.
    */
   public void setCreatedByPartnerPermissionGroupModelList(Collection<PartnerPermissionGroupModel> partnerPermissionGroupModelList) throws Exception {
      this.createdByPartnerPermissionGroupModelList = partnerPermissionGroupModelList;
   }


   /**
    * Add the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be added.
    */

   public void addUpdatedByPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      permissionGroupModel.setRelationUpdatedByAppUserModel(this);
      updatedByPermissionGroupModelList.add(permissionGroupModel);
   }

   /**
    * Remove the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be removed.
    */

   public void removeUpdatedByPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      permissionGroupModel.setRelationUpdatedByAppUserModel(null);
      updatedByPermissionGroupModelList.remove(permissionGroupModel);
   }

   /**
    * Get a list of related PermissionGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PermissionGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PermissionGroupModel> getUpdatedByPermissionGroupModelList() throws Exception {
      return updatedByPermissionGroupModelList;
   }


   /**
    * Set a list of PermissionGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param permissionGroupModelList the list of related objects.
    */
   public void setUpdatedByPermissionGroupModelList(Collection<PermissionGroupModel> permissionGroupModelList) throws Exception {
      this.updatedByPermissionGroupModelList = permissionGroupModelList;
   }


   /**
    * Add the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be added.
    */

   public void addCreatedByPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      permissionGroupModel.setRelationCreatedByAppUserModel(this);
      createdByPermissionGroupModelList.add(permissionGroupModel);
   }

   /**
    * Remove the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be removed.
    */

   public void removeCreatedByPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      permissionGroupModel.setRelationCreatedByAppUserModel(null);
      createdByPermissionGroupModelList.remove(permissionGroupModel);
   }

   /**
    * Get a list of related PermissionGroupModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PermissionGroupModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PermissionGroupModel> getCreatedByPermissionGroupModelList() throws Exception {
      return createdByPermissionGroupModelList;
   }


   /**
    * Set a list of PermissionGroupModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param permissionGroupModelList the list of related objects.
    */
   public void setCreatedByPermissionGroupModelList(Collection<PermissionGroupModel> permissionGroupModelList) throws Exception {
      this.createdByPermissionGroupModelList = permissionGroupModelList;
   }


   /**
    * Add the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be added.
    */

   public void addUpdatedByPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationUpdatedByAppUserModel(this);
      updatedByPermissionGroupDetailModelList.add(permissionGroupDetailModel);
   }

   /**
    * Remove the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be removed.
    */

   public void removeUpdatedByPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationUpdatedByAppUserModel(null);
      updatedByPermissionGroupDetailModelList.remove(permissionGroupDetailModel);
   }

   /**
    * Get a list of related PermissionGroupDetailModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PermissionGroupDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PermissionGroupDetailModel> getUpdatedByPermissionGroupDetailModelList() throws Exception {
      return updatedByPermissionGroupDetailModelList;
   }


   /**
    * Set a list of PermissionGroupDetailModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param permissionGroupDetailModelList the list of related objects.
    */
   public void setUpdatedByPermissionGroupDetailModelList(Collection<PermissionGroupDetailModel> permissionGroupDetailModelList) throws Exception {
      this.updatedByPermissionGroupDetailModelList = permissionGroupDetailModelList;
   }


   /**
    * Add the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be added.
    */

   public void addCreatedByPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationCreatedByAppUserModel(this);
      createdByPermissionGroupDetailModelList.add(permissionGroupDetailModel);
   }

   /**
    * Remove the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be removed.
    */

   public void removeCreatedByPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationCreatedByAppUserModel(null);
      createdByPermissionGroupDetailModelList.remove(permissionGroupDetailModel);
   }

   /**
    * Get a list of related PermissionGroupDetailModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PermissionGroupDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PermissionGroupDetailModel> getCreatedByPermissionGroupDetailModelList() throws Exception {
      return createdByPermissionGroupDetailModelList;
   }


   /**
    * Set a list of PermissionGroupDetailModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param permissionGroupDetailModelList the list of related objects.
    */
   public void setCreatedByPermissionGroupDetailModelList(Collection<PermissionGroupDetailModel> permissionGroupDetailModelList) throws Exception {
      this.createdByPermissionGroupDetailModelList = permissionGroupDetailModelList;
   }


   /**
    * Add the related OperatorModel to this one-to-many relation.
    *
    * @param operatorModel object to be added.
    */

   public void addCreatedByOperatorModel(OperatorModel operatorModel) {
      operatorModel.setRelationCreatedByAppUserModel(this);
      createdByOperatorModelList.add(operatorModel);
   }

   /**
    * Remove the related OperatorModel to this one-to-many relation.
    *
    * @param operatorModel object to be removed.
    */

   public void removeCreatedByOperatorModel(OperatorModel operatorModel) {
      operatorModel.setRelationCreatedByAppUserModel(null);
      createdByOperatorModelList.remove(operatorModel);
   }

   /**
    * Get a list of related OperatorModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of OperatorModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<OperatorModel> getCreatedByOperatorModelList() throws Exception {
      return createdByOperatorModelList;
   }


   /**
    * Set a list of OperatorModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param operatorModelList the list of related objects.
    */
   public void setCreatedByOperatorModelList(Collection<OperatorModel> operatorModelList) throws Exception {
      this.createdByOperatorModelList = operatorModelList;
   }


   /**
    * Add the related OperatorModel to this one-to-many relation.
    *
    * @param operatorModel object to be added.
    */

   public void addUpdatedByOperatorModel(OperatorModel operatorModel) {
      operatorModel.setRelationUpdatedByAppUserModel(this);
      updatedByOperatorModelList.add(operatorModel);
   }

   /**
    * Remove the related OperatorModel to this one-to-many relation.
    *
    * @param operatorModel object to be removed.
    */

   public void removeUpdatedByOperatorModel(OperatorModel operatorModel) {
      operatorModel.setRelationUpdatedByAppUserModel(null);
      updatedByOperatorModelList.remove(operatorModel);
   }

   /**
    * Get a list of related OperatorModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of OperatorModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<OperatorModel> getUpdatedByOperatorModelList() throws Exception {
      return updatedByOperatorModelList;
   }


   /**
    * Set a list of OperatorModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param operatorModelList the list of related objects.
    */
   public void setUpdatedByOperatorModelList(Collection<OperatorModel> operatorModelList) throws Exception {
      this.updatedByOperatorModelList = operatorModelList;
   }


   /**
    * Add the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be added.
    */

   public void addUpdatedByOperatorUserModel(OperatorUserModel operatorUserModel) {
      operatorUserModel.setRelationUpdatedByAppUserModel(this);
      updatedByOperatorUserModelList.add(operatorUserModel);
   }

   /**
    * Remove the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be removed.
    */

   public void removeUpdatedByOperatorUserModel(OperatorUserModel operatorUserModel) {
      operatorUserModel.setRelationUpdatedByAppUserModel(null);
      updatedByOperatorUserModelList.remove(operatorUserModel);
   }

   /**
    * Get a list of related OperatorUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of OperatorUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<OperatorUserModel> getUpdatedByOperatorUserModelList() throws Exception {
      return updatedByOperatorUserModelList;
   }


   /**
    * Set a list of OperatorUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param operatorUserModelList the list of related objects.
    */
   public void setUpdatedByOperatorUserModelList(Collection<OperatorUserModel> operatorUserModelList) throws Exception {
      this.updatedByOperatorUserModelList = operatorUserModelList;
   }


   /**
    * Add the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be added.
    */

   public void addCreatedByOperatorUserModel(OperatorUserModel operatorUserModel) {
      operatorUserModel.setRelationCreatedByAppUserModel(this);
      createdByOperatorUserModelList.add(operatorUserModel);
   }

   /**
    * Remove the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be removed.
    */

   public void removeCreatedByOperatorUserModel(OperatorUserModel operatorUserModel) {
      operatorUserModel.setRelationCreatedByAppUserModel(null);
      createdByOperatorUserModelList.remove(operatorUserModel);
   }

   /**
    * Get a list of related OperatorUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of OperatorUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<OperatorUserModel> getCreatedByOperatorUserModelList() throws Exception {
      return createdByOperatorUserModelList;
   }


   /**
    * Set a list of OperatorUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param operatorUserModelList the list of related objects.
    */
   public void setCreatedByOperatorUserModelList(Collection<OperatorUserModel> operatorUserModelList) throws Exception {
      this.createdByOperatorUserModelList = operatorUserModelList;
   }


   /**
    * Add the related PaymentModeModel to this one-to-many relation.
    *
    * @param paymentModeModel object to be added.
    */

   public void addCreatedByPaymentModeModel(PaymentModeModel paymentModeModel) {
      paymentModeModel.setRelationCreatedByAppUserModel(this);
      createdByPaymentModeModelList.add(paymentModeModel);
   }

   /**
    * Remove the related PaymentModeModel to this one-to-many relation.
    *
    * @param paymentModeModel object to be removed.
    */

   public void removeCreatedByPaymentModeModel(PaymentModeModel paymentModeModel) {
      paymentModeModel.setRelationCreatedByAppUserModel(null);
      createdByPaymentModeModelList.remove(paymentModeModel);
   }

   /**
    * Get a list of related PaymentModeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of PaymentModeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<PaymentModeModel> getCreatedByPaymentModeModelList() throws Exception {
      return createdByPaymentModeModelList;
   }


   /**
    * Set a list of PaymentModeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param paymentModeModelList the list of related objects.
    */
   public void setCreatedByPaymentModeModelList(Collection<PaymentModeModel> paymentModeModelList) throws Exception {
      this.createdByPaymentModeModelList = paymentModeModelList;
   }


   /**
    * Add the related PaymentModeModel to this one-to-many relation.
    *
    * @param paymentModeModel object to be added.
    */

   public void addUpdatedByPaymentModeModel(PaymentModeModel paymentModeModel) {
      paymentModeModel.setRelationUpdatedByAppUserModel(this);
      updatedByPaymentModeModelList.add(paymentModeModel);
   }

   /**
    * Remove the related PaymentModeModel to this one-to-many relation.
    *
    * @param paymentModeModel object to be removed.
    */

   public void removeUpdatedByPaymentModeModel(PaymentModeModel paymentModeModel) {
      paymentModeModel.setRelationUpdatedByAppUserModel(null);
      updatedByPaymentModeModelList.remove(paymentModeModel);
   }

   /**
    * Get a list of related PaymentModeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of PaymentModeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<PaymentModeModel> getUpdatedByPaymentModeModelList() throws Exception {
      return updatedByPaymentModeModelList;
   }


   /**
    * Set a list of PaymentModeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param paymentModeModelList the list of related objects.
    */
   public void setUpdatedByPaymentModeModelList(Collection<PaymentModeModel> paymentModeModelList) throws Exception {
      this.updatedByPaymentModeModelList = paymentModeModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */

   public void addUpdatedByProductModel(ProductModel productModel) {
      productModel.setRelationUpdatedByAppUserModel(this);
      updatedByProductModelList.add(productModel);
   }

   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */

   public void removeUpdatedByProductModel(ProductModel productModel) {
      productModel.setRelationUpdatedByAppUserModel(null);
      updatedByProductModelList.remove(productModel);
   }

   /**
    * Get a list of related ProductModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ProductModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ProductModel> getUpdatedByProductModelList() throws Exception {
      return updatedByProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param productModelList the list of related objects.
    */
   public void setUpdatedByProductModelList(Collection<ProductModel> productModelList) throws Exception {
      this.updatedByProductModelList = productModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */

   public void addCreatedByProductModel(ProductModel productModel) {
      productModel.setRelationCreatedByAppUserModel(this);
      createdByProductModelList.add(productModel);
   }

   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */

   public void removeCreatedByProductModel(ProductModel productModel) {
      productModel.setRelationCreatedByAppUserModel(null);
      createdByProductModelList.remove(productModel);
   }

   /**
    * Get a list of related ProductModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ProductModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ProductModel> getCreatedByProductModelList() throws Exception {
      return createdByProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param productModelList the list of related objects.
    */
   public void setCreatedByProductModelList(Collection<ProductModel> productModelList) throws Exception {
      this.createdByProductModelList = productModelList;
   }


   /**
    * Add the related ProductCatalogModel to this one-to-many relation.
    *
    * @param productCatalogModel object to be added.
    */

   public void addCreatedByProductCatalogModel(ProductCatalogModel productCatalogModel) {
      productCatalogModel.setRelationCreatedByAppUserModel(this);
      createdByProductCatalogModelList.add(productCatalogModel);
   }

   /**
    * Remove the related ProductCatalogModel to this one-to-many relation.
    *
    * @param productCatalogModel object to be removed.
    */

   public void removeCreatedByProductCatalogModel(ProductCatalogModel productCatalogModel) {
      productCatalogModel.setRelationCreatedByAppUserModel(null);
      createdByProductCatalogModelList.remove(productCatalogModel);
   }

   /**
    * Get a list of related ProductCatalogModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ProductCatalogModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ProductCatalogModel> getCreatedByProductCatalogModelList() throws Exception {
      return createdByProductCatalogModelList;
   }


   /**
    * Set a list of ProductCatalogModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param productCatalogModelList the list of related objects.
    */
   public void setCreatedByProductCatalogModelList(Collection<ProductCatalogModel> productCatalogModelList) throws Exception {
      this.createdByProductCatalogModelList = productCatalogModelList;
   }


   /**
    * Add the related ProductCatalogModel to this one-to-many relation.
    *
    * @param productCatalogModel object to be added.
    */

   public void addUpdatedByProductCatalogModel(ProductCatalogModel productCatalogModel) {
      productCatalogModel.setRelationUpdatedByAppUserModel(this);
      updatedByProductCatalogModelList.add(productCatalogModel);
   }

   /**
    * Remove the related ProductCatalogModel to this one-to-many relation.
    *
    * @param productCatalogModel object to be removed.
    */

   public void removeUpdatedByProductCatalogModel(ProductCatalogModel productCatalogModel) {
      productCatalogModel.setRelationUpdatedByAppUserModel(null);
      updatedByProductCatalogModelList.remove(productCatalogModel);
   }

   /**
    * Get a list of related ProductCatalogModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ProductCatalogModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ProductCatalogModel> getUpdatedByProductCatalogModelList() throws Exception {
      return updatedByProductCatalogModelList;
   }


   /**
    * Set a list of ProductCatalogModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param productCatalogModelList the list of related objects.
    */
   public void setUpdatedByProductCatalogModelList(Collection<ProductCatalogModel> productCatalogModelList) throws Exception {
      this.updatedByProductCatalogModelList = productCatalogModelList;
   }


   /**
    * Add the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be added.
    */

   public void addCreatedByProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationCreatedByAppUserModel(this);
      createdByProductUnitModelList.add(productUnitModel);
   }

   /**
    * Remove the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be removed.
    */

   public void removeCreatedByProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationCreatedByAppUserModel(null);
      createdByProductUnitModelList.remove(productUnitModel);
   }

   /**
    * Get a list of related ProductUnitModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ProductUnitModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ProductUnitModel> getCreatedByProductUnitModelList() throws Exception {
      return createdByProductUnitModelList;
   }


   /**
    * Set a list of ProductUnitModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param productUnitModelList the list of related objects.
    */
   public void setCreatedByProductUnitModelList(Collection<ProductUnitModel> productUnitModelList) throws Exception {
      this.createdByProductUnitModelList = productUnitModelList;
   }


   /**
    * Add the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be added.
    */

   public void addUpdatedByProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationUpdatedByAppUserModel(this);
      updatedByProductUnitModelList.add(productUnitModel);
   }

   /**
    * Remove the related ProductUnitModel to this one-to-many relation.
    *
    * @param productUnitModel object to be removed.
    */

   public void removeUpdatedByProductUnitModel(ProductUnitModel productUnitModel) {
      productUnitModel.setRelationUpdatedByAppUserModel(null);
      updatedByProductUnitModelList.remove(productUnitModel);
   }

   /**
    * Get a list of related ProductUnitModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ProductUnitModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ProductUnitModel> getUpdatedByProductUnitModelList() throws Exception {
      return updatedByProductUnitModelList;
   }


   /**
    * Set a list of ProductUnitModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param productUnitModelList the list of related objects.
    */
   public void setUpdatedByProductUnitModelList(Collection<ProductUnitModel> productUnitModelList) throws Exception {
      this.updatedByProductUnitModelList = productUnitModelList;
   }


   /**
    * Add the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be added.
    */

   public void addUpdatedByRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationUpdatedByAppUserModel(this);
      updatedByRetailerModelList.add(retailerModel);
   }

   /**
    * Remove the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be removed.
    */

   public void removeUpdatedByRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationUpdatedByAppUserModel(null);
      updatedByRetailerModelList.remove(retailerModel);
   }

   /**
    * Get a list of related RetailerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of RetailerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<RetailerModel> getUpdatedByRetailerModelList() throws Exception {
      return updatedByRetailerModelList;
   }


   /**
    * Set a list of RetailerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param retailerModelList the list of related objects.
    */
   public void setUpdatedByRetailerModelList(Collection<RetailerModel> retailerModelList) throws Exception {
      this.updatedByRetailerModelList = retailerModelList;
   }


   /**
    * Add the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be added.
    */

   public void addCreatedByRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationCreatedByAppUserModel(this);
      createdByRetailerModelList.add(retailerModel);
   }

   /**
    * Remove the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be removed.
    */

   public void removeCreatedByRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationCreatedByAppUserModel(null);
      createdByRetailerModelList.remove(retailerModel);
   }

   /**
    * Get a list of related RetailerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of RetailerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<RetailerModel> getCreatedByRetailerModelList() throws Exception {
      return createdByRetailerModelList;
   }


   /**
    * Set a list of RetailerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param retailerModelList the list of related objects.
    */
   public void setCreatedByRetailerModelList(Collection<RetailerModel> retailerModelList) throws Exception {
      this.createdByRetailerModelList = retailerModelList;
   }


   /**
    * Add the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be added.
    */

   public void addUpdatedByRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationUpdatedByAppUserModel(this);
      updatedByRetailerContactModelList.add(retailerContactModel);
   }

   /**
    * Remove the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be removed.
    */

   public void removeUpdatedByRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationUpdatedByAppUserModel(null);
      updatedByRetailerContactModelList.remove(retailerContactModel);
   }

   /**
    * Get a list of related RetailerContactModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of RetailerContactModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<RetailerContactModel> getUpdatedByRetailerContactModelList() throws Exception {
      return updatedByRetailerContactModelList;
   }


   /**
    * Set a list of RetailerContactModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param retailerContactModelList the list of related objects.
    */
   public void setUpdatedByRetailerContactModelList(Collection<RetailerContactModel> retailerContactModelList) throws Exception {
      this.updatedByRetailerContactModelList = retailerContactModelList;
   }


   /**
    * Add the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be added.
    */

   public void addCreatedByRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationCreatedByAppUserModel(this);
      createdByRetailerContactModelList.add(retailerContactModel);
   }

   /**
    * Remove the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be removed.
    */

   public void removeCreatedByRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationCreatedByAppUserModel(null);
      createdByRetailerContactModelList.remove(retailerContactModel);
   }

   /**
    * Get a list of related RetailerContactModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of RetailerContactModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<RetailerContactModel> getCreatedByRetailerContactModelList() throws Exception {
      return createdByRetailerContactModelList;
   }


   /**
    * Set a list of RetailerContactModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param retailerContactModelList the list of related objects.
    */
   public void setCreatedByRetailerContactModelList(Collection<RetailerContactModel> retailerContactModelList) throws Exception {
      this.createdByRetailerContactModelList = retailerContactModelList;
   }


   /**
    * Add the related RetailerTypeModel to this one-to-many relation.
    *
    * @param retailerTypeModel object to be added.
    */

   public void addCreatedByRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      retailerTypeModel.setRelationCreatedByAppUserModel(this);
      createdByRetailerTypeModelList.add(retailerTypeModel);
   }

   /**
    * Remove the related RetailerTypeModel to this one-to-many relation.
    *
    * @param retailerTypeModel object to be removed.
    */

   public void removeCreatedByRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      retailerTypeModel.setRelationCreatedByAppUserModel(null);
      createdByRetailerTypeModelList.remove(retailerTypeModel);
   }

   /**
    * Get a list of related RetailerTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of RetailerTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<RetailerTypeModel> getCreatedByRetailerTypeModelList() throws Exception {
      return createdByRetailerTypeModelList;
   }


   /**
    * Set a list of RetailerTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param retailerTypeModelList the list of related objects.
    */
   public void setCreatedByRetailerTypeModelList(Collection<RetailerTypeModel> retailerTypeModelList) throws Exception {
      this.createdByRetailerTypeModelList = retailerTypeModelList;
   }

   /**
    * Add the related VeriflyModeModel to this one-to-many relation.
    *
    * @param veriflyModeModel object to be added.
    */

   public void addCreatedByVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      veriflyModeModel.setRelationCreatedByAppUserModel(this);
      createdByVeriflyModeModelList.add(veriflyModeModel);
   }

   /**
    * Remove the related VeriflyModeModel to this one-to-many relation.
    *
    * @param veriflyModeModel object to be removed.
    */

   public void removeCreatedByVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      veriflyModeModel.setRelationCreatedByAppUserModel(null);
      createdByVeriflyModeModelList.remove(veriflyModeModel);
   }

   /**
    * Get a list of related VeriflyModeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of VeriflyModeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<VeriflyModeModel> getCreatedByVeriflyModeModelList() throws Exception {
      return createdByVeriflyModeModelList;
   }


   /**
    * Set a list of VeriflyModeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param veriflyModeModelList the list of related objects.
    */
   public void setCreatedByVeriflyModeModelList(Collection<VeriflyModeModel> veriflyModeModelList) throws Exception {
      this.createdByVeriflyModeModelList = veriflyModeModelList;
   }


   /**
    * Add the related VeriflyModeModel to this one-to-many relation.
    *
    * @param veriflyModeModel object to be added.
    */

   public void addUpdatedByVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      veriflyModeModel.setRelationUpdatedByAppUserModel(this);
      updatedByVeriflyModeModelList.add(veriflyModeModel);
   }

   /**
    * Remove the related VeriflyModeModel to this one-to-many relation.
    *
    * @param veriflyModeModel object to be removed.
    */

   public void removeUpdatedByVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      veriflyModeModel.setRelationUpdatedByAppUserModel(null);
      updatedByVeriflyModeModelList.remove(veriflyModeModel);
   }

   /**
    * Get a list of related VeriflyModeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of VeriflyModeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<VeriflyModeModel> getUpdatedByVeriflyModeModelList() throws Exception {
      return updatedByVeriflyModeModelList;
   }


   /**
    * Set a list of VeriflyModeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param veriflyModeModelList the list of related objects.
    */
   public void setUpdatedByVeriflyModeModelList(Collection<VeriflyModeModel> veriflyModeModelList) throws Exception {
      this.updatedByVeriflyModeModelList = veriflyModeModelList;
   }




   /**
    * Add the related RetailerTypeModel to this one-to-many relation.
    *
    * @param retailerTypeModel object to be added.
    */

   public void addUpdatedByRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      retailerTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByRetailerTypeModelList.add(retailerTypeModel);
   }

   /**
    * Remove the related RetailerTypeModel to this one-to-many relation.
    *
    * @param retailerTypeModel object to be removed.
    */

   public void removeUpdatedByRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      retailerTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByRetailerTypeModelList.remove(retailerTypeModel);
   }

   /**
    * Get a list of related RetailerTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of RetailerTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<RetailerTypeModel> getUpdatedByRetailerTypeModelList() throws Exception {
      return updatedByRetailerTypeModelList;
   }


   /**
    * Set a list of RetailerTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param retailerTypeModelList the list of related objects.
    */
   public void setUpdatedByRetailerTypeModelList(Collection<RetailerTypeModel> retailerTypeModelList) throws Exception {
      this.updatedByRetailerTypeModelList = retailerTypeModelList;
   }


   /**
    * Add the related ServiceModel to this one-to-many relation.
    *
    * @param serviceModel object to be added.
    */

   public void addUpdatedByServiceModel(ServiceModel serviceModel) {
      serviceModel.setRelationUpdatedByAppUserModel(this);
      updatedByServiceModelList.add(serviceModel);
   }

   /**
    * Remove the related ServiceModel to this one-to-many relation.
    *
    * @param serviceModel object to be removed.
    */

   public void removeUpdatedByServiceModel(ServiceModel serviceModel) {
      serviceModel.setRelationUpdatedByAppUserModel(null);
      updatedByServiceModelList.remove(serviceModel);
   }

   /**
    * Get a list of related ServiceModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ServiceModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ServiceModel> getUpdatedByServiceModelList() throws Exception {
      return updatedByServiceModelList;
   }


   /**
    * Set a list of ServiceModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param serviceModelList the list of related objects.
    */
   public void setUpdatedByServiceModelList(Collection<ServiceModel> serviceModelList) throws Exception {
      this.updatedByServiceModelList = serviceModelList;
   }


   /**
    * Add the related ServiceModel to this one-to-many relation.
    *
    * @param serviceModel object to be added.
    */

   public void addCreatedByServiceModel(ServiceModel serviceModel) {
      serviceModel.setRelationCreatedByAppUserModel(this);
      createdByServiceModelList.add(serviceModel);
   }

   /**
    * Remove the related ServiceModel to this one-to-many relation.
    *
    * @param serviceModel object to be removed.
    */

   public void removeCreatedByServiceModel(ServiceModel serviceModel) {
      serviceModel.setRelationCreatedByAppUserModel(null);
      createdByServiceModelList.remove(serviceModel);
   }

   /**
    * Get a list of related ServiceModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ServiceModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ServiceModel> getCreatedByServiceModelList() throws Exception {
      return createdByServiceModelList;
   }


   /**
    * Set a list of ServiceModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param serviceModelList the list of related objects.
    */
   public void setCreatedByServiceModelList(Collection<ServiceModel> serviceModelList) throws Exception {
      this.createdByServiceModelList = serviceModelList;
   }


   /**
    * Add the related ServiceTypeModel to this one-to-many relation.
    *
    * @param serviceTypeModel object to be added.
    */

   public void addCreatedByServiceTypeModel(ServiceTypeModel serviceTypeModel) {
      serviceTypeModel.setRelationCreatedByAppUserModel(this);
      createdByServiceTypeModelList.add(serviceTypeModel);
   }

   /**
    * Remove the related ServiceTypeModel to this one-to-many relation.
    *
    * @param serviceTypeModel object to be removed.
    */

   public void removeCreatedByServiceTypeModel(ServiceTypeModel serviceTypeModel) {
      serviceTypeModel.setRelationCreatedByAppUserModel(null);
      createdByServiceTypeModelList.remove(serviceTypeModel);
   }

   /**
    * Get a list of related ServiceTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ServiceTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ServiceTypeModel> getCreatedByServiceTypeModelList() throws Exception {
      return createdByServiceTypeModelList;
   }


   /**
    * Set a list of ServiceTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param serviceTypeModelList the list of related objects.
    */
   public void setCreatedByServiceTypeModelList(Collection<ServiceTypeModel> serviceTypeModelList) throws Exception {
      this.createdByServiceTypeModelList = serviceTypeModelList;
   }


   /**
    * Add the related ServiceTypeModel to this one-to-many relation.
    *
    * @param serviceTypeModel object to be added.
    */

   public void addUpdatedByServiceTypeModel(ServiceTypeModel serviceTypeModel) {
      serviceTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByServiceTypeModelList.add(serviceTypeModel);
   }

   /**
    * Remove the related ServiceTypeModel to this one-to-many relation.
    *
    * @param serviceTypeModel object to be removed.
    */

   public void removeUpdatedByServiceTypeModel(ServiceTypeModel serviceTypeModel) {
      serviceTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByServiceTypeModelList.remove(serviceTypeModel);
   }

   /**
    * Get a list of related ServiceTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ServiceTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ServiceTypeModel> getUpdatedByServiceTypeModelList() throws Exception {
      return updatedByServiceTypeModelList;
   }


   /**
    * Set a list of ServiceTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param serviceTypeModelList the list of related objects.
    */
   public void setUpdatedByServiceTypeModelList(Collection<ServiceTypeModel> serviceTypeModelList) throws Exception {
      this.updatedByServiceTypeModelList = serviceTypeModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */

   public void addCreatedByShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationCreatedByAppUserModel(this);
      createdByShipmentModelList.add(shipmentModel);
   }

   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */

   public void removeCreatedByShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationCreatedByAppUserModel(null);
      createdByShipmentModelList.remove(shipmentModel);
   }

   /**
    * Get a list of related ShipmentModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ShipmentModel> getCreatedByShipmentModelList() throws Exception {
      return createdByShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param shipmentModelList the list of related objects.
    */
   public void setCreatedByShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
      this.createdByShipmentModelList = shipmentModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */

   public void addUpdatedByShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationUpdatedByAppUserModel(this);
      updatedByShipmentModelList.add(shipmentModel);
   }

   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */

   public void removeUpdatedByShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationUpdatedByAppUserModel(null);
      updatedByShipmentModelList.remove(shipmentModel);
   }

   /**
    * Get a list of related ShipmentModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ShipmentModel> getUpdatedByShipmentModelList() throws Exception {
      return updatedByShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param shipmentModelList the list of related objects.
    */
   public void setUpdatedByShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
      this.updatedByShipmentModelList = shipmentModelList;
   }


   /**
    * Add the related ShipmentTypeModel to this one-to-many relation.
    *
    * @param shipmentTypeModel object to be added.
    */

   public void addUpdatedByShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      shipmentTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByShipmentTypeModelList.add(shipmentTypeModel);
   }

   /**
    * Remove the related ShipmentTypeModel to this one-to-many relation.
    *
    * @param shipmentTypeModel object to be removed.
    */

   public void removeUpdatedByShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      shipmentTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByShipmentTypeModelList.remove(shipmentTypeModel);
   }

   /**
    * Get a list of related ShipmentTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of ShipmentTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<ShipmentTypeModel> getUpdatedByShipmentTypeModelList() throws Exception {
      return updatedByShipmentTypeModelList;
   }


   /**
    * Set a list of ShipmentTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param shipmentTypeModelList the list of related objects.
    */
   public void setUpdatedByShipmentTypeModelList(Collection<ShipmentTypeModel> shipmentTypeModelList) throws Exception {
      this.updatedByShipmentTypeModelList = shipmentTypeModelList;
   }


   /**
    * Add the related ShipmentTypeModel to this one-to-many relation.
    *
    * @param shipmentTypeModel object to be added.
    */

   public void addCreatedByShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      shipmentTypeModel.setRelationCreatedByAppUserModel(this);
      createdByShipmentTypeModelList.add(shipmentTypeModel);
   }

   /**
    * Remove the related ShipmentTypeModel to this one-to-many relation.
    *
    * @param shipmentTypeModel object to be removed.
    */

   public void removeCreatedByShipmentTypeModel(ShipmentTypeModel shipmentTypeModel) {
      shipmentTypeModel.setRelationCreatedByAppUserModel(null);
      createdByShipmentTypeModelList.remove(shipmentTypeModel);
   }

   /**
    * Get a list of related ShipmentTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of ShipmentTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<ShipmentTypeModel> getCreatedByShipmentTypeModelList() throws Exception {
      return createdByShipmentTypeModelList;
   }


   /**
    * Set a list of ShipmentTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param shipmentTypeModelList the list of related objects.
    */
   public void setCreatedByShipmentTypeModelList(Collection<ShipmentTypeModel> shipmentTypeModelList) throws Exception {
      this.createdByShipmentTypeModelList = shipmentTypeModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */

   public void addUpdatedBySmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationUpdatedByAppUserModel(this);
      updatedBySmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }

   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */

   public void removeUpdatedBySmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationUpdatedByAppUserModel(null);
      updatedBySmartMoneyAccountModelList.remove(smartMoneyAccountModel);
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<SmartMoneyAccountModel> getUpdatedBySmartMoneyAccountModelList() throws Exception {
      return updatedBySmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
   public void setUpdatedBySmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
      this.updatedBySmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */

   public void addCreatedBySmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationCreatedByAppUserModel(this);
      createdBySmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }

   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */

   public void removeCreatedBySmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationCreatedByAppUserModel(null);
      createdBySmartMoneyAccountModelList.remove(smartMoneyAccountModel);
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<SmartMoneyAccountModel> getCreatedBySmartMoneyAccountModelList() throws Exception {
      return createdBySmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
   public void setCreatedBySmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
      this.createdBySmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be added.
    */

   public void addCreatedByStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationCreatedByAppUserModel(this);
      createdByStakeholderBankInfoModelList.add(stakeholderBankInfoModel);
   }

   /**
    * Remove the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be removed.
    */

   public void removeCreatedByStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationCreatedByAppUserModel(null);
      createdByStakeholderBankInfoModelList.remove(stakeholderBankInfoModel);
   }

   /**
    * Get a list of related StakeholderBankInfoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of StakeholderBankInfoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<StakeholderBankInfoModel> getCreatedByStakeholderBankInfoModelList() throws Exception {
      return createdByStakeholderBankInfoModelList;
   }


   /**
    * Set a list of StakeholderBankInfoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param stakeholderBankInfoModelList the list of related objects.
    */
   public void setCreatedByStakeholderBankInfoModelList(Collection<StakeholderBankInfoModel> stakeholderBankInfoModelList) throws Exception {
      this.createdByStakeholderBankInfoModelList = stakeholderBankInfoModelList;
   }


   /**
    * Add the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be added.
    */

   public void addUpdatedByStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationUpdatedByAppUserModel(this);
      updatedByStakeholderBankInfoModelList.add(stakeholderBankInfoModel);
   }

   /**
    * Remove the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be removed.
    */

   public void removeUpdatedByStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationUpdatedByAppUserModel(null);
      updatedByStakeholderBankInfoModelList.remove(stakeholderBankInfoModel);
   }

   /**
    * Get a list of related StakeholderBankInfoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of StakeholderBankInfoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<StakeholderBankInfoModel> getUpdatedByStakeholderBankInfoModelList() throws Exception {
      return updatedByStakeholderBankInfoModelList;
   }


   /**
    * Set a list of StakeholderBankInfoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param stakeholderBankInfoModelList the list of related objects.
    */
   public void setUpdatedByStakeholderBankInfoModelList(Collection<StakeholderBankInfoModel> stakeholderBankInfoModelList) throws Exception {
      this.updatedByStakeholderBankInfoModelList = stakeholderBankInfoModelList;
   }


   /**
    * Add the related StakeholderTypeModel to this one-to-many relation.
    *
    * @param stakeholderTypeModel object to be added.
    */

   public void addCreatedByStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      stakeholderTypeModel.setRelationCreatedByAppUserModel(this);
      createdByStakeholderTypeModelList.add(stakeholderTypeModel);
   }

   /**
    * Remove the related StakeholderTypeModel to this one-to-many relation.
    *
    * @param stakeholderTypeModel object to be removed.
    */

   public void removeCreatedByStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      stakeholderTypeModel.setRelationCreatedByAppUserModel(null);
      createdByStakeholderTypeModelList.remove(stakeholderTypeModel);
   }

   /**
    * Get a list of related StakeholderTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of StakeholderTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<StakeholderTypeModel> getCreatedByStakeholderTypeModelList() throws Exception {
      return createdByStakeholderTypeModelList;
   }


   /**
    * Set a list of StakeholderTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param stakeholderTypeModelList the list of related objects.
    */
   public void setCreatedByStakeholderTypeModelList(Collection<StakeholderTypeModel> stakeholderTypeModelList) throws Exception {
      this.createdByStakeholderTypeModelList = stakeholderTypeModelList;
   }


   /**
    * Add the related StakeholderTypeModel to this one-to-many relation.
    *
    * @param stakeholderTypeModel object to be added.
    */

   public void addUpdatedByStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      stakeholderTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByStakeholderTypeModelList.add(stakeholderTypeModel);
   }

   /**
    * Remove the related StakeholderTypeModel to this one-to-many relation.
    *
    * @param stakeholderTypeModel object to be removed.
    */

   public void removeUpdatedByStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      stakeholderTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByStakeholderTypeModelList.remove(stakeholderTypeModel);
   }

   /**
    * Get a list of related StakeholderTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of StakeholderTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<StakeholderTypeModel> getUpdatedByStakeholderTypeModelList() throws Exception {
      return updatedByStakeholderTypeModelList;
   }


   /**
    * Set a list of StakeholderTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param stakeholderTypeModelList the list of related objects.
    */
   public void setUpdatedByStakeholderTypeModelList(Collection<StakeholderTypeModel> stakeholderTypeModelList) throws Exception {
      this.updatedByStakeholderTypeModelList = stakeholderTypeModelList;
   }


   /**
    * Add the related SupplierModel to this one-to-many relation.
    *
    * @param supplierModel object to be added.
    */

   public void addCreatedBySupplierModel(SupplierModel supplierModel) {
      supplierModel.setRelationCreatedByAppUserModel(this);
      createdBySupplierModelList.add(supplierModel);
   }

   /**
    * Remove the related SupplierModel to this one-to-many relation.
    *
    * @param supplierModel object to be removed.
    */

   public void removeCreatedBySupplierModel(SupplierModel supplierModel) {
      supplierModel.setRelationCreatedByAppUserModel(null);
      createdBySupplierModelList.remove(supplierModel);
   }

   /**
    * Get a list of related SupplierModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of SupplierModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<SupplierModel> getCreatedBySupplierModelList() throws Exception {
      return createdBySupplierModelList;
   }


   /**
    * Set a list of SupplierModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param supplierModelList the list of related objects.
    */
   public void setCreatedBySupplierModelList(Collection<SupplierModel> supplierModelList) throws Exception {
      this.createdBySupplierModelList = supplierModelList;
   }


   /**
    * Add the related SupplierModel to this one-to-many relation.
    *
    * @param supplierModel object to be added.
    */

   public void addUpdatedBySupplierModel(SupplierModel supplierModel) {
      supplierModel.setRelationUpdatedByAppUserModel(this);
      updatedBySupplierModelList.add(supplierModel);
   }

   /**
    * Remove the related SupplierModel to this one-to-many relation.
    *
    * @param supplierModel object to be removed.
    */

   public void removeUpdatedBySupplierModel(SupplierModel supplierModel) {
      supplierModel.setRelationUpdatedByAppUserModel(null);
      updatedBySupplierModelList.remove(supplierModel);
   }

   /**
    * Get a list of related SupplierModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of SupplierModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<SupplierModel> getUpdatedBySupplierModelList() throws Exception {
      return updatedBySupplierModelList;
   }


   /**
    * Set a list of SupplierModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param supplierModelList the list of related objects.
    */
   public void setUpdatedBySupplierModelList(Collection<SupplierModel> supplierModelList) throws Exception {
      this.updatedBySupplierModelList = supplierModelList;
   }


   /**
    * Add the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be added.
    */

   public void addCreatedBySupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationCreatedByAppUserModel(this);
      createdBySupplierBankInfoModelList.add(supplierBankInfoModel);
   }

   /**
    * Remove the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be removed.
    */

   public void removeCreatedBySupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationCreatedByAppUserModel(null);
      createdBySupplierBankInfoModelList.remove(supplierBankInfoModel);
   }

   /**
    * Get a list of related SupplierBankInfoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of SupplierBankInfoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<SupplierBankInfoModel> getCreatedBySupplierBankInfoModelList() throws Exception {
      return createdBySupplierBankInfoModelList;
   }


   /**
    * Set a list of SupplierBankInfoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param supplierBankInfoModelList the list of related objects.
    */
   public void setCreatedBySupplierBankInfoModelList(Collection<SupplierBankInfoModel> supplierBankInfoModelList) throws Exception {
      this.createdBySupplierBankInfoModelList = supplierBankInfoModelList;
   }


   /**
    * Add the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be added.
    */

   public void addUpdatedBySupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationUpdatedByAppUserModel(this);
      updatedBySupplierBankInfoModelList.add(supplierBankInfoModel);
   }

   /**
    * Remove the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be removed.
    */

   public void removeUpdatedBySupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationUpdatedByAppUserModel(null);
      updatedBySupplierBankInfoModelList.remove(supplierBankInfoModel);
   }

   /**
    * Get a list of related SupplierBankInfoModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of SupplierBankInfoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<SupplierBankInfoModel> getUpdatedBySupplierBankInfoModelList() throws Exception {
      return updatedBySupplierBankInfoModelList;
   }


   /**
    * Set a list of SupplierBankInfoModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param supplierBankInfoModelList the list of related objects.
    */
   public void setUpdatedBySupplierBankInfoModelList(Collection<SupplierBankInfoModel> supplierBankInfoModelList) throws Exception {
      this.updatedBySupplierBankInfoModelList = supplierBankInfoModelList;
   }


   /**
    * Add the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be added.
    */

   public void addUpdatedBySupplierUserModel(SupplierUserModel supplierUserModel) {
      supplierUserModel.setRelationUpdatedByAppUserModel(this);
      updatedBySupplierUserModelList.add(supplierUserModel);
   }

   /**
    * Remove the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be removed.
    */

   public void removeUpdatedBySupplierUserModel(SupplierUserModel supplierUserModel) {
      supplierUserModel.setRelationUpdatedByAppUserModel(null);
      updatedBySupplierUserModelList.remove(supplierUserModel);
   }

   /**
    * Get a list of related SupplierUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of SupplierUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<SupplierUserModel> getUpdatedBySupplierUserModelList() throws Exception {
      return updatedBySupplierUserModelList;
   }


   /**
    * Set a list of SupplierUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param supplierUserModelList the list of related objects.
    */
   public void setUpdatedBySupplierUserModelList(Collection<SupplierUserModel> supplierUserModelList) throws Exception {
      this.updatedBySupplierUserModelList = supplierUserModelList;
   }


   /**
    * Add the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be added.
    */

   public void addCreatedBySupplierUserModel(SupplierUserModel supplierUserModel) {
      supplierUserModel.setRelationCreatedByAppUserModel(this);
      createdBySupplierUserModelList.add(supplierUserModel);
   }

   /**
    * Remove the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be removed.
    */

   public void removeCreatedBySupplierUserModel(SupplierUserModel supplierUserModel) {
      supplierUserModel.setRelationCreatedByAppUserModel(null);
      createdBySupplierUserModelList.remove(supplierUserModel);
   }

   /**
    * Get a list of related SupplierUserModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of SupplierUserModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<SupplierUserModel> getCreatedBySupplierUserModelList() throws Exception {
      return createdBySupplierUserModelList;
   }
   /**
    * Returns the value of the <code>nicExpiryDate</code> property.
    *
    */
   @Column(name = "NIC_EXPIRY_DATE"  )
   public Date getNicExpiryDate() {
      return nicExpiryDate;
   }

   /**
    * Sets the value of the <code>nicExpiryDate</code> property.
    *
    * @param nicExpiryDate the value for the <code>nicExpiryDate</code> property
    *
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setNicExpiryDate(Date nicExpiryDate) {
      this.nicExpiryDate = nicExpiryDate;
   }

   /**
    * Set a list of SupplierUserModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param supplierUserModelList the list of related objects.
    */
   public void setCreatedBySupplierUserModelList(Collection<SupplierUserModel> supplierUserModelList) throws Exception {
      this.createdBySupplierUserModelList = supplierUserModelList;
   }


   /**
    * Add the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be added.
    */

   public void addCreatedByTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationCreatedByAppUserModel(this);
      createdByTickerModelList.add(tickerModel);
   }

   /**
    * Remove the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be removed.
    */

   public void removeCreatedByTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationCreatedByAppUserModel(null);
      createdByTickerModelList.remove(tickerModel);
   }

   /**
    * Get a list of related TickerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of TickerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<TickerModel> getCreatedByTickerModelList() throws Exception {
      return createdByTickerModelList;
   }


   /**
    * Set a list of TickerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param tickerModelList the list of related objects.
    */
   public void setCreatedByTickerModelList(Collection<TickerModel> tickerModelList) throws Exception {
      this.createdByTickerModelList = tickerModelList;
   }


   /**
    * Add the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be added.
    */

   public void addUpdatedByTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationUpdatedByAppUserModel(this);
      updatedByTickerModelList.add(tickerModel);
   }

   /**
    * Remove the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be removed.
    */

   public void removeUpdatedByTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationUpdatedByAppUserModel(null);
      updatedByTickerModelList.remove(tickerModel);
   }

   /**
    * Get a list of related TickerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of TickerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<TickerModel> getUpdatedByTickerModelList() throws Exception {
      return updatedByTickerModelList;
   }


   /**
    * Set a list of TickerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param tickerModelList the list of related objects.
    */
   public void setUpdatedByTickerModelList(Collection<TickerModel> tickerModelList) throws Exception {
      this.updatedByTickerModelList = tickerModelList;
   }


   /**
    * Add the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be added.
    */

   public void addAppUserIdTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationAppUserIdAppUserModel(this);
      appUserIdTickerModelList.add(tickerModel);
   }

   /**
    * Remove the related TickerModel to this one-to-many relation.
    *
    * @param tickerModel object to be removed.
    */

   public void removeAppUserIdTickerModel(TickerModel tickerModel) {
      tickerModel.setRelationAppUserIdAppUserModel(null);
      appUserIdTickerModelList.remove(tickerModel);
   }

   /**
    * Get a list of related TickerModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of TickerModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<TickerModel> getAppUserIdTickerModelList() throws Exception {
      return appUserIdTickerModelList;
   }


   /**
    * Set a list of TickerModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param tickerModelList the list of related objects.
    */
   public void setAppUserIdTickerModelList(Collection<TickerModel> tickerModelList) throws Exception {
      this.appUserIdTickerModelList = tickerModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */

   public void addUpdatedByTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationUpdatedByAppUserModel(this);
      updatedByTransactionModelList.add(transactionModel);
   }

   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */

   public void removeUpdatedByTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationUpdatedByAppUserModel(null);
      updatedByTransactionModelList.remove(transactionModel);
   }

   /**
    * Get a list of related TransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of TransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<TransactionModel> getUpdatedByTransactionModelList() throws Exception {
      return updatedByTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param transactionModelList the list of related objects.
    */
   public void setUpdatedByTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
      this.updatedByTransactionModelList = transactionModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */

   public void addCreatedByTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationCreatedByAppUserModel(this);
      createdByTransactionModelList.add(transactionModel);
   }

   /**
    * Add the related UserPermissionModel to this one-to-many relation.
    *
    * @param userPermissionModel object to be added.
    */

   public void addCreatedByUserPermissionModel(UserPermissionModel userPermissionModel) {
      userPermissionModel.setRelationCreatedByAppUserModel(this);
      createdByUserPermissionModelList.add(userPermissionModel);
   }

   /**
    * Remove the related UserPermissionModel to this one-to-many relation.
    *
    * @param userPermissionModel object to be removed.
    */

   public void removeCreatedByUserPermissionModel(UserPermissionModel userPermissionModel) {
      userPermissionModel.setRelationCreatedByAppUserModel(null);
      createdByUserPermissionModelList.remove(userPermissionModel);
   }

   /**
    * Get a list of related UserPermissionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of UserPermissionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<UserPermissionModel> getCreatedByUserPermissionModelList() throws Exception {
      return createdByUserPermissionModelList;
   }


   /**
    * Set a list of UserPermissionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param userPermissionModelList the list of related objects.
    */
   public void setCreatedByUserPermissionModelList(Collection<UserPermissionModel> userPermissionModelList) throws Exception {
      this.createdByUserPermissionModelList = userPermissionModelList;
   }


   /**
    * Add the related UserPermissionModel to this one-to-many relation.
    *
    * @param userPermissionModel object to be added.
    */

   public void addUpdatedByUserPermissionModel(UserPermissionModel userPermissionModel) {
      userPermissionModel.setRelationUpdatedByAppUserModel(this);
      updatedByUserPermissionModelList.add(userPermissionModel);
   }

   /**
    * Remove the related UserPermissionModel to this one-to-many relation.
    *
    * @param userPermissionModel object to be removed.
    */

   public void removeUpdatedByUserPermissionModel(UserPermissionModel userPermissionModel) {
      userPermissionModel.setRelationUpdatedByAppUserModel(null);
      updatedByUserPermissionModelList.remove(userPermissionModel);
   }

   /**
    * Get a list of related UserPermissionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of UserPermissionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<UserPermissionModel> getUpdatedByUserPermissionModelList() throws Exception {
      return updatedByUserPermissionModelList;
   }


   /**
    * Set a list of UserPermissionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param userPermissionModelList the list of related objects.
    */
   public void setUpdatedByUserPermissionModelList(Collection<UserPermissionModel> userPermissionModelList) throws Exception {
      this.updatedByUserPermissionModelList = userPermissionModelList;
   }

   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */

   public void removeCreatedByTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationCreatedByAppUserModel(null);
      createdByTransactionModelList.remove(transactionModel);
   }

   /**
    * Get a list of related TransactionModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of TransactionModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<TransactionModel> getCreatedByTransactionModelList() throws Exception {
      return createdByTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param transactionModelList the list of related objects.
    */
   public void setCreatedByTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
      this.createdByTransactionModelList = transactionModelList;
   }


   /**
    * Add the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be added.
    */

   public void addUpdatedByTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      transactionCodeModel.setRelationUpdatedByAppUserModel(this);
      updatedByTransactionCodeModelList.add(transactionCodeModel);
   }

   /**
    * Remove the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be removed.
    */

   public void removeUpdatedByTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      transactionCodeModel.setRelationUpdatedByAppUserModel(null);
      updatedByTransactionCodeModelList.remove(transactionCodeModel);
   }

   /**
    * Get a list of related TransactionCodeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of TransactionCodeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<TransactionCodeModel> getUpdatedByTransactionCodeModelList() throws Exception {
      return updatedByTransactionCodeModelList;
   }


   /**
    * Set a list of TransactionCodeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param transactionCodeModelList the list of related objects.
    */
   public void setUpdatedByTransactionCodeModelList(Collection<TransactionCodeModel> transactionCodeModelList) throws Exception {
      this.updatedByTransactionCodeModelList = transactionCodeModelList;
   }


   /**
    * Add the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be added.
    */

   public void addCreatedByTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      transactionCodeModel.setRelationCreatedByAppUserModel(this);
      createdByTransactionCodeModelList.add(transactionCodeModel);
   }

   /**
    * Remove the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be removed.
    */

   public void removeCreatedByTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      transactionCodeModel.setRelationCreatedByAppUserModel(null);
      createdByTransactionCodeModelList.remove(transactionCodeModel);
   }

   /**
    * Get a list of related TransactionCodeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of TransactionCodeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<TransactionCodeModel> getCreatedByTransactionCodeModelList() throws Exception {
      return createdByTransactionCodeModelList;
   }


   /**
    * Set a list of TransactionCodeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param transactionCodeModelList the list of related objects.
    */
   public void setCreatedByTransactionCodeModelList(Collection<TransactionCodeModel> transactionCodeModelList) throws Exception {
      this.createdByTransactionCodeModelList = transactionCodeModelList;
   }


   /**
    * Add the related TransactionTypeModel to this one-to-many relation.
    *
    * @param transactionTypeModel object to be added.
    */

   public void addCreatedByTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      transactionTypeModel.setRelationCreatedByAppUserModel(this);
      createdByTransactionTypeModelList.add(transactionTypeModel);
   }

   /**
    * Remove the related TransactionTypeModel to this one-to-many relation.
    *
    * @param transactionTypeModel object to be removed.
    */

   public void removeCreatedByTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      transactionTypeModel.setRelationCreatedByAppUserModel(null);
      createdByTransactionTypeModelList.remove(transactionTypeModel);
   }

   /**
    * Get a list of related TransactionTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of TransactionTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<TransactionTypeModel> getCreatedByTransactionTypeModelList() throws Exception {
      return createdByTransactionTypeModelList;
   }


   /**
    * Set a list of TransactionTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param transactionTypeModelList the list of related objects.
    */
   public void setCreatedByTransactionTypeModelList(Collection<TransactionTypeModel> transactionTypeModelList) throws Exception {
      this.createdByTransactionTypeModelList = transactionTypeModelList;
   }


   /**
    * Add the related TransactionTypeModel to this one-to-many relation.
    *
    * @param transactionTypeModel object to be added.
    */

   public void addUpdatedByTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      transactionTypeModel.setRelationUpdatedByAppUserModel(this);
      updatedByTransactionTypeModelList.add(transactionTypeModel);
   }

   /**
    * Remove the related TransactionTypeModel to this one-to-many relation.
    *
    * @param transactionTypeModel object to be removed.
    */

   public void removeUpdatedByTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      transactionTypeModel.setRelationUpdatedByAppUserModel(null);
      updatedByTransactionTypeModelList.remove(transactionTypeModel);
   }

   /**
    * Get a list of related TransactionTypeModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of TransactionTypeModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<TransactionTypeModel> getUpdatedByTransactionTypeModelList() throws Exception {
      return updatedByTransactionTypeModelList;
   }


   /**
    * Set a list of TransactionTypeModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param transactionTypeModelList the list of related objects.
    */
   public void setUpdatedByTransactionTypeModelList(Collection<TransactionTypeModel> transactionTypeModelList) throws Exception {
      this.updatedByTransactionTypeModelList = transactionTypeModelList;
   }


   /**
    * Add the related UsecaseModel to this one-to-many relation.
    *
    * @param usecaseModel object to be added.
    */

   public void addCreatedByUsecaseModel(UsecaseModel usecaseModel) {
      usecaseModel.setRelationCreatedByAppUserModel(this);
      createdByUsecaseModelList.add(usecaseModel);
   }

   /**
    * Remove the related UsecaseModel to this one-to-many relation.
    *
    * @param usecaseModel object to be removed.
    */

   public void removeCreatedByUsecaseModel(UsecaseModel usecaseModel) {
      usecaseModel.setRelationCreatedByAppUserModel(null);
      createdByUsecaseModelList.remove(usecaseModel);
   }

   /**
    * Get a list of related UsecaseModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of UsecaseModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<UsecaseModel> getCreatedByUsecaseModelList() throws Exception {
      return createdByUsecaseModelList;
   }


   /**
    * Set a list of UsecaseModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param usecaseModelList the list of related objects.
    */
   public void setCreatedByUsecaseModelList(Collection<UsecaseModel> usecaseModelList) throws Exception {
      this.createdByUsecaseModelList = usecaseModelList;
   }


   /**
    * Add the related UsecaseModel to this one-to-many relation.
    *
    * @param usecaseModel object to be added.
    */

   public void addUpdatedByUsecaseModel(UsecaseModel usecaseModel) {
      usecaseModel.setRelationUpdatedByAppUserModel(this);
      updatedByUsecaseModelList.add(usecaseModel);
   }

   /**
    * Remove the related UsecaseModel to this one-to-many relation.
    *
    * @param usecaseModel object to be removed.
    */

   public void removeUpdatedByUsecaseModel(UsecaseModel usecaseModel) {
      usecaseModel.setRelationUpdatedByAppUserModel(null);
      updatedByUsecaseModelList.remove(usecaseModel);
   }

   /**
    * Get a list of related UsecaseModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of UsecaseModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<UsecaseModel> getUpdatedByUsecaseModelList() throws Exception {
      return updatedByUsecaseModelList;
   }


   /**
    * Set a list of UsecaseModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param usecaseModelList the list of related objects.
    */
   public void setUpdatedByUsecaseModelList(Collection<UsecaseModel> usecaseModelList) throws Exception {
      this.updatedByUsecaseModelList = usecaseModelList;
   }


   /**
    * Add the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be added.
    */

   public void addCreatedByUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationCreatedByAppUserModel(this);
      createdByUserDeviceAccountsModelList.add(userDeviceAccountsModel);
   }

   /**
    * Remove the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be removed.
    */

   public void removeCreatedByUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationCreatedByAppUserModel(null);
      createdByUserDeviceAccountsModelList.remove(userDeviceAccountsModel);
   }

   /**
    * Get a list of related UserDeviceAccountsModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of UserDeviceAccountsModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<UserDeviceAccountsModel> getCreatedByUserDeviceAccountsModelList() throws Exception {
      return createdByUserDeviceAccountsModelList;
   }


   /**
    * Set a list of UserDeviceAccountsModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param userDeviceAccountsModelList the list of related objects.
    */
   public void setCreatedByUserDeviceAccountsModelList(Collection<UserDeviceAccountsModel> userDeviceAccountsModelList) throws Exception {
      this.createdByUserDeviceAccountsModelList = userDeviceAccountsModelList;
   }


   /**
    * Add the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be added.
    */

   public void addUpdatedByUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationUpdatedByAppUserModel(this);
      updatedByUserDeviceAccountsModelList.add(userDeviceAccountsModel);
   }

   /**
    * Remove the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be removed.
    */

   public void removeUpdatedByUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationUpdatedByAppUserModel(null);
      updatedByUserDeviceAccountsModelList.remove(userDeviceAccountsModel);
   }

   /**
    * Get a list of related UserDeviceAccountsModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of UserDeviceAccountsModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<UserDeviceAccountsModel> getUpdatedByUserDeviceAccountsModelList() throws Exception {
      return updatedByUserDeviceAccountsModelList;
   }


   /**
    * Set a list of UserDeviceAccountsModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param userDeviceAccountsModelList the list of related objects.
    */
   public void setUpdatedByUserDeviceAccountsModelList(Collection<UserDeviceAccountsModel> userDeviceAccountsModelList) throws Exception {
      this.updatedByUserDeviceAccountsModelList = userDeviceAccountsModelList;
   }


   /**
    * Add the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be added.
    */

   public void addAppUserIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationAppUserIdAppUserModel(this);
      appUserIdUserDeviceAccountsModelList.add(userDeviceAccountsModel);
   }

   /**
    * Remove the related UserDeviceAccountsModel to this one-to-many relation.
    *
    * @param userDeviceAccountsModel object to be removed.
    */

   public void removeAppUserIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      userDeviceAccountsModel.setRelationAppUserIdAppUserModel(null);
      appUserIdUserDeviceAccountsModelList.remove(userDeviceAccountsModel);
   }

   /**
    * Get a list of related UserDeviceAccountsModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of UserDeviceAccountsModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<UserDeviceAccountsModel> getAppUserIdUserDeviceAccountsModelList() throws Exception {
      return appUserIdUserDeviceAccountsModelList;
   }


   /**
    * Set a list of UserDeviceAccountsModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param userDeviceAccountsModelList the list of related objects.
    */
   public void setAppUserIdUserDeviceAccountsModelList(Collection<UserDeviceAccountsModel> userDeviceAccountsModelList) throws Exception {
      this.appUserIdUserDeviceAccountsModelList = userDeviceAccountsModelList;
   }


   public void setUserPermissionList(
           Collection<CustomUserPermissionViewModel> userPermissionList) {
      this.userPermissionList = userPermissionList;
   }
   /**
    * Returns the value of the <code>supplierUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSupplierUserId() {
      if (supplierUserIdSupplierUserModel != null) {
         return supplierUserIdSupplierUserModel.getSupplierUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>supplierUserId</code> property.
    *
    * @param supplierUserId the value for the <code>supplierUserId</code> property
    */

   @javax.persistence.Transient
   public void setSupplierUserId(Long supplierUserId) {
      if(supplierUserId == null)
      {
         supplierUserIdSupplierUserModel = null;
      }
      else
      {
         supplierUserIdSupplierUserModel = new SupplierUserModel();
         supplierUserIdSupplierUserModel.setSupplierUserId(supplierUserId);
      }
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


   //---------------------------------------------------------------------------------------------------

   /**
    * Returns the value of the <code>handlerId</code> property.
    *
    */
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
    * Returns the value of the <code>operatorUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getOperatorUserId() {
      if (operatorUserIdOperatorUserModel != null) {
         return operatorUserIdOperatorUserModel.getOperatorUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>operatorUserId</code> property.
    *
    * @param operatorUserId the value for the <code>operatorUserId</code> property
    */

   @javax.persistence.Transient
   public void setOperatorUserId(Long operatorUserId) {
      if(operatorUserId == null)
      {
         operatorUserIdOperatorUserModel = null;
      }
      else
      {
         operatorUserIdOperatorUserModel = new OperatorUserModel();
         operatorUserIdOperatorUserModel.setOperatorUserId(operatorUserId);
      }
   }

   /**
    * Returns the value of the <code>mobileTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMobileTypeId() {
      if (mobileTypeIdMobileTypeModel != null) {
         return mobileTypeIdMobileTypeModel.getMobileTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>mobileTypeId</code> property.
    *
    * @param mobileTypeId the value for the <code>mobileTypeId</code> property
    * @spring.validator type="required"
    */

   @javax.persistence.Transient
   public void setMobileTypeId(Long mobileTypeId) {
      if(mobileTypeId == null)
      {
         mobileTypeIdMobileTypeModel = null;
      }
      else
      {
         mobileTypeIdMobileTypeModel = new MobileTypeModel();
         mobileTypeIdMobileTypeModel.setMobileTypeId(mobileTypeId);
      }
   }

   /**
    * Returns the value of the <code>mnoUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMnoUserId() {
      if (mnoUserIdMnoUserModel != null) {
         return mnoUserIdMnoUserModel.getMnoUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>mnoUserId</code> property.
    *
    * @param mnoUserId the value for the <code>mnoUserId</code> property
    */

   @javax.persistence.Transient
   public void setMnoUserId(Long mnoUserId) {
      if(mnoUserId == null)
      {
         mnoUserIdMnoUserModel = null;
      }
      else
      {
         mnoUserIdMnoUserModel = new MnoUserModel();
         mnoUserIdMnoUserModel.setMnoUserId(mnoUserId);
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
    * Returns the value of the <code>bankUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getBankUserId() {
      if (bankUserIdBankUserModel != null) {
         return bankUserIdBankUserModel.getBankUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>bankUserId</code> property.
    *
    * @param bankUserId the value for the <code>bankUserId</code> property
    */

   @javax.persistence.Transient
   public void setBankUserId(Long bankUserId) {
      if(bankUserId == null)
      {
         bankUserIdBankUserModel = null;
      }
      else
      {
         bankUserIdBankUserModel = new BankUserModel();
         bankUserIdBankUserModel.setBankUserId(bankUserId);
      }
   }

   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserTypeId() {
      if (appUserTypeIdAppUserTypeModel != null) {
         return appUserTypeIdAppUserTypeModel.getAppUserTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
    * @spring.validator type="required"
    */

   @javax.persistence.Transient
   public void setAppUserTypeId(Long appUserTypeId) {
      if(appUserTypeId == null)
      {
         appUserTypeIdAppUserTypeModel = null;
      }
      else
      {
         appUserTypeIdAppUserTypeModel = new AppUserTypeModel();
         appUserTypeIdAppUserTypeModel.setAppUserTypeId(appUserTypeId);
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
      checkBox += "_"+ getAppUserId();
      checkBox += "\"/>";
      return checkBox;
   }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserId=" + getAppUserId();
      return parameters;
   }
   /**
    * Helper method for default Sorting on Primary Keys
    */
   @javax.persistence.Transient
   public String getPrimaryKeyFieldName()
   {
      String primaryKeyFieldName = "appUserId";
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

      associationModel.setClassName("SupplierUserModel");
      associationModel.setPropertyName("relationSupplierUserIdSupplierUserModel");
      associationModel.setValue(getRelationSupplierUserIdSupplierUserModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("RetailerContactModel");
      associationModel.setPropertyName("relationRetailerContactIdRetailerContactModel");
      associationModel.setValue(getRelationRetailerContactIdRetailerContactModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("HandlerModel");
      associationModel.setPropertyName("relationHandlerIdHandlerModel");
      associationModel.setValue(getRelationHandlerIdHandlerModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("OperatorUserModel");
      associationModel.setPropertyName("relationOperatorUserIdOperatorUserModel");
      associationModel.setValue(getRelationOperatorUserIdOperatorUserModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("MobileTypeModel");
      associationModel.setPropertyName("relationMobileTypeIdMobileTypeModel");
      associationModel.setValue(getRelationMobileTypeIdMobileTypeModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("MnoUserModel");
      associationModel.setPropertyName("relationMnoUserIdMnoUserModel");
      associationModel.setValue(getRelationMnoUserIdMnoUserModel());

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

      associationModel.setClassName("BankUserModel");
      associationModel.setPropertyName("relationBankUserIdBankUserModel");
      associationModel.setValue(getRelationBankUserIdBankUserModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("AppUserTypeModel");
      associationModel.setPropertyName("relationAppUserTypeIdAppUserTypeModel");
      associationModel.setValue(getRelationAppUserTypeIdAppUserTypeModel());

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

      associationModel = new AssociationModel();

      associationModel.setClassName("AppUserModel");
      associationModel.setPropertyName("relationClosedByAppUserModel");
      associationModel.setValue(getRelationClosedByAppUserModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("AppUserModel");
      associationModel.setPropertyName("relationSettledByAppUserModel");
      associationModel.setValue(getRelationSettledByAppUserModel());

      associationModelList.add(associationModel);

      associationModel = new AssociationModel();

      associationModel.setClassName("RegistrationStateModel");
      associationModel.setPropertyName("relationRegistrationStateModel");
      associationModel.setValue(getRelationRegistrationStateModel());
      associationModelList.add(associationModel);

      return associationModelList;
   }


   /**
    * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
    */
   @javax.persistence.Transient
   public boolean isAccountNonExpired()
   {
      return!this.getAccountExpired();
   }

   /**
    * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
    */
   @javax.persistence.Transient
   public boolean isAccountNonLocked()
   {
      return!getAccountLocked();
   }

   /**
    * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
    */

   @javax.persistence.Transient
   public Collection<GrantedAuthority> getAuthorities()
   {
      List<GrantedAuthority> authorityList = new ArrayList<>(userPermissionList.size());
      for( CustomUserPermissionViewModel model : userPermissionList )
      {
         authorityList.add( model );
      }
      return authorityList;
   }

   /**
    * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
    */
   @javax.persistence.Transient
   public boolean isCredentialsNonExpired()
   {
      return!getCredentialsExpired();
   }

   @javax.persistence.Transient
   public boolean isEnabled()
   {
      return this.getAccountEnabled();
   }

   @javax.persistence.Transient
   public String toString()
   {
      ToStringBuilder sb = new ToStringBuilder(this,
              ToStringStyle.DEFAULT_STYLE).
              append("username", this.username)
              .append("enabled", this.accountEnabled)
              .append("accountExpired", this.accountExpired)
              .append("credentialsExpired", this.credentialsExpired)
              .append("accountLocked", this.accountLocked);

      GrantedAuthority[] auths = this.getAuthorities().toArray(new GrantedAuthority[this.getAuthorities().size()]);
      if (auths != null)
      {
         sb.append("Granted Authorities: ");

         for (int i = 0; i < auths.length; i++)
         {
            if (i > 0)
            {
               sb.append(", ");
            }
            sb.append(auths[i].toString());
         }
      }
      else
      {
         sb.append("No Granted Authorities");
      }
      return sb.toString();
   }

   @javax.persistence.Transient
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (! (o instanceof AppUserModel))
      {
         return false;
      }

      final AppUserModel user = (AppUserModel) o;

      if (username != null ? !username.equals(user.getUsername()) :
              user.getUsername() != null)
      {
         return false;
      }

      return true;
   }

   @javax.persistence.Transient
   public int hashCode()
   {
      return (username != null ? username.hashCode() : 0);
   }
   @javax.persistence.Transient
   public Collection<CustomUserPermissionViewModel> getUserPermissionList() {
      return userPermissionList;
   }
   @Column(name = "IS_CLOSED_UNSETTLED" )
   public Boolean getAccountClosedUnsettled() {
      return accountClosedUnsettled;
   }
   public void setAccountClosedUnsettled(Boolean accountClosedUnsettled) {
      this.accountClosedUnsettled = accountClosedUnsettled;
   }
   @Column(name = "IS_CLOSED_SETTLED" )
   public Boolean getAccountClosedSettled() {
      return accountClosedSettled;
   }
   public void setAccountClosedSettled(Boolean accountClosedSettled) {
      this.accountClosedSettled = accountClosedSettled;
   }
   @Column(name = "ACCOUNT_CLOSED_ON" )
   public Date getClosedOn() {
      return closedOn;
   }
   public void setClosedOn(Date closedOn) {
      this.closedOn = closedOn;
   }
   @Column(name = "ACCOUNT_SETTLED_ON")
   public Date getSettledOn() {
      return settledOn;
   }
   public void setSettledOn(Date settledOn) {
      this.settledOn = settledOn;
   }

/////Account ClosedBy///////

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_CLOSED_BY")
   public AppUserModel getRelationClosedByAppUserModel(){
      return closedByAppUserModel;
   }

   @javax.persistence.Transient
   public AppUserModel getClosedByAppUserModel(){
      return getRelationClosedByAppUserModel();
   }


   @javax.persistence.Transient
   public void setRelationClosedByAppUserModel(AppUserModel appUserModel) {
      this.closedByAppUserModel = appUserModel;
   }


   @javax.persistence.Transient
   public void setClosedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
         setRelationClosedByAppUserModel((AppUserModel)appUserModel.clone());
      }
   }

/////Account SettledBy///////

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_SETTLED_BY")
   public AppUserModel getRelationSettledByAppUserModel(){
      return settledByAppUserModel;
   }

   @javax.persistence.Transient
   public AppUserModel getSettledByAppUserModel(){
      return getRelationSettledByAppUserModel();
   }


   @javax.persistence.Transient
   public void setRelationSettledByAppUserModel(AppUserModel appUserModel) {
      this.settledByAppUserModel = appUserModel;
   }


   @javax.persistence.Transient
   public void setSettledByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
         setRelationSettledByAppUserModel((AppUserModel)appUserModel.clone());
      }
   }
   /**
    * @return the closingComments
    */
   @Column(name = "CLOSING_COMMENTS" )
   public String getClosingComments() {
      return closingComments;
   }
   /**
    * @param closingComments the closingComments to set
    */
   public void setClosingComments(String closingComments) {
      this.closingComments = closingComments;
   }
   /**
    * @return the settlementComments
    */
   @Column(name = "SETTLEMENT_COMMENTS" )
   public String getSettlementComments() {
      return settlementComments;
   }
   /**
    * @param settlementComments the settlementComments to set
    */
   public void setSettlementComments(String settlementComments) {
      this.settlementComments = settlementComments;
   }

   /////////////////////////turab appUserPasswordHistory/////////////////////
   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addCreatedByAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationCreatedByAppUserModel(this);
      createdByAppUserPasswordHistoryModelList.add(appUserPasswordHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeCreatedByAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationCreatedByAppUserModel(null);
      createdByAppUserPasswordHistoryModelList.remove(appUserPasswordHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCreatedByAppUserModel")
   @JoinColumn(name = "CREATED_BY")
   public Collection<AppUserPasswordHistoryModel> getCreatedByAppUserPasswordHistoryModelList() throws Exception {
      return createdByAppUserPasswordHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the CreatedBy member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setCreatedByAppUserPasswordHistoryModelList(Collection<AppUserPasswordHistoryModel> appUserPasswordHistoryModelList) throws Exception {
      this.createdByAppUserPasswordHistoryModelList = appUserPasswordHistoryModelList;
   }


   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addUpdatedByAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationUpdatedByAppUserModel(this);
      updatedByAppUserPasswordHistoryModelList.add(appUserPasswordHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeUpdatedByAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationUpdatedByAppUserModel(null);
      updatedByAppUserPasswordHistoryModelList.remove(appUserPasswordHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUpdatedByAppUserModel")
   @JoinColumn(name = "UPDATED_BY")
   public Collection<AppUserPasswordHistoryModel> getUpdatedByAppUserPasswordHistoryModelList() throws Exception {
      return updatedByAppUserPasswordHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the UpdatedBy member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setUpdatedByAppUserPasswordHistoryModelList(Collection<AppUserPasswordHistoryModel> appUserPasswordHistoryModelList) throws Exception {
      this.updatedByAppUserPasswordHistoryModelList = appUserPasswordHistoryModelList;
   }


   /**
    * Add the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be added.
    */

   public void addAppUserIdAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationAppUserIdAppUserModel(this);
      appUserIdAppUserPasswordHistoryModelList.add(appUserPasswordHistoryModel);
   }

   /**
    * Remove the related AppUserMobileHistoryModel to this one-to-many relation.
    *
    * @param appUserMobileHistoryModel object to be removed.
    */

   public void removeAppUserIdAppUserPasswordHistoryModel(AppUserPasswordHistoryModel appUserPasswordHistoryModel) {
      appUserPasswordHistoryModel.setRelationAppUserIdAppUserModel(null);
      appUserIdAppUserPasswordHistoryModelList.remove(appUserPasswordHistoryModel);
   }

   /**
    * Get a list of related AppUserMobileHistoryModel objects of the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @return Collection of AppUserMobileHistoryModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserIdAppUserModel")
   @JoinColumn(name = "APP_USER_ID")
   public Collection<AppUserPasswordHistoryModel> getAppUserIdAppUserPasswordHistoryModelList() throws Exception {
      return appUserIdAppUserPasswordHistoryModelList;
   }


   /**
    * Set a list of AppUserMobileHistoryModel related objects to the AppUserModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserId member.
    *
    * @param appUserMobileHistoryModelList the list of related objects.
    */
   public void setAppUserIdAppUserPasswordHistoryModelList(Collection<AppUserPasswordHistoryModel> appUserPasswordHistoryModelList) throws Exception {
      this.appUserIdAppUserPasswordHistoryModelList = appUserPasswordHistoryModelList;
   }

   @Column(name = "LAST_LOGIN_TIME"  )
   public java.sql.Timestamp getLastLoginTime() {
      return lastLoginTime;
   }
   public void setLastLoginTime(java.sql.Timestamp lastLoginTime) {
      this.lastLoginTime = lastLoginTime;
   }

   @Column(name = "IS_CNIC_EXPIRY_MSG_SENT" , nullable = true )
   public Boolean getCnicExpiryMsgSent() {
      return cnicExpiryMsgSent;
   }

   public void setCnicExpiryMsgSent(Boolean cnicExpiryMsgSent) {
      this.cnicExpiryMsgSent = cnicExpiryMsgSent;
   }
   ////////////////////////end by turab /////////////////////////////////////

   @Column(name = "MIDDLE_NAME"  )
   public String getMiddleName() {
      return middleName;
   }
   public void setMiddleName(String middleName) {
      this.middleName = middleName;
   }

   //--------------REGISTRATION STATE-----------------------------------------------------------------
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "REGISTRATION_STATE_ID")
   public RegistrationStateModel getRelationRegistrationStateModel() {
      return registrationStateModel;
   }

   @Transient
   public void setRelationRegistrationStateModel(RegistrationStateModel registrationStateModel) {
      this.registrationStateModel = registrationStateModel;
   }

   @Transient
   public RegistrationStateModel getRegistrationStateModel() {
      return getRelationRegistrationStateModel();
   }

   @Transient
   public void setRegistrationStateModel(
           RegistrationStateModel registrationStateModel) {
      this.registrationStateModel = registrationStateModel;
   }

   @Transient
   public Long getRegistrationStateId() {
      if (registrationStateModel != null) {
         return registrationStateModel.getRegistrationStateId();
      } else {
         return null;
      }
   }

   @Transient
   public void setRegistrationStateId(Long registrationStateId) {
      if (registrationStateId == null) {
         this.registrationStateModel = null;
      } else {
         registrationStateModel = new RegistrationStateModel();
         registrationStateModel.setRegistrationStateId(registrationStateId);
      }
   }
   //*******************************************************************************************************
   //--Previous Registration State
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "PREV_REG_STATE_ID")
   public RegistrationStateModel getRelationPrevRegistrationStateModel() {
      return prevRegistrationStateModel;
   }

   @Transient
   public void setRelationPrevRegistrationStateModel(RegistrationStateModel prevRegistrationStateModel) {
      this.prevRegistrationStateModel = prevRegistrationStateModel;
   }

   @Transient
   public RegistrationStateModel getPrevRegistrationStateModel() {
      return getRelationPrevRegistrationStateModel();
   }

   @Transient
   public void setPrevRegistrationStateModel(
           RegistrationStateModel prevRegistrationStateModel) {
      this.prevRegistrationStateModel = prevRegistrationStateModel;
   }

   @Transient
   public Long getPrevRegistrationStateId() {
      if (prevRegistrationStateModel != null) {
         return prevRegistrationStateModel.getRegistrationStateId();
      } else {
         return null;
      }
   }

   @Transient
   public void setPrevRegistrationStateId(Long prevRegistrationStateId) {
      if (prevRegistrationStateId == null) {
         this.prevRegistrationStateModel = null;
      } else {
         prevRegistrationStateModel = new RegistrationStateModel();
         prevRegistrationStateModel.setRegistrationStateId(prevRegistrationStateId);
      }
   }

   //******************************************************************************************

   //--Service Operator Model
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_OP_ID")
   public MnoModel getRelationMnoModel()
   {
      return mnoModelIdMnoModel;
   }

   @Transient
   public void setRelationMnoModel(MnoModel mnoModelIdMnoModel){
      this.mnoModelIdMnoModel = mnoModelIdMnoModel;
   }

   @Transient
   public MnoModel getMnoModel()
   {
      return getRelationMnoModel();
   }

   @Transient
   public void setMnoModel(MnoModel mnoModelIdMnoModel)
   {
      this.mnoModelIdMnoModel = mnoModelIdMnoModel;
   }

   @Transient
   public Long getMnoId()
   {
      if(mnoModelIdMnoModel != null)
         return mnoModelIdMnoModel.getMnoId();
      else
         return null;
   }

   @Transient
   public void setMnoId(Long mnoId)
   {
      if(mnoId == null)
         mnoModelIdMnoModel = null;
      else
      {
         mnoModelIdMnoModel = new MnoModel();
         mnoModelIdMnoModel.setMnoId(mnoId);
      }
   }
   @Column(name = "ACCOUNT_STATE_ID"  )
   public Long getAccountStateId() {
      return accountStateId;
   }
   public void setAccountStateId(Long accountStateId) {
      this.accountStateId = accountStateId;
   }

   @Column(name = "EMPLOYEE_ID")
   public Long getEmployeeId() {
      return employeeId;
   }

   public void setEmployeeId(Long employeeId) {
      this.employeeId = employeeId;
   }

   @Column(name = "TELLER_ID")
   public String getTellerId() {
      return tellerId;
   }
   public void setTellerId(String tellerId) {
      this.tellerId = tellerId;
   }

   @Transient
   public String getFullName() {
      String name = "";
      if (!StringUtil.isNullOrEmpty(firstName))
         name = firstName;

      if (!StringUtil.isNullOrEmpty(lastName))
         name += " " + lastName;

      return name;
   }

   @Column(name = "IS_FILER")
   public Boolean getFiler() {
      return filer;
   }

   public void setFiler(Boolean filer) {
      this.filer = filer;
   }

   @Column(name = "DORMANCY_REMOVED_ON")
   public Date getDormancyRemovedOn() {
      return dormancyRemovedOn;
   }

   public void setDormancyRemovedOn(Date dormancyRemovedOn) {
      this.dormancyRemovedOn = dormancyRemovedOn;
   }

   @javax.persistence.Transient
   public List<String> getAccountTypeList() {
      return accountTypeList;
   }

   public void setAccountTypeList(List<String> accountTypeList) {
      this.accountTypeList = accountTypeList;
   }

   /**
    * Returns the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @return the value of the <code>countryIdCountryModel</code> relation property.
    *
    */
   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */
   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */

   @javax.persistence.Transient
   public Long getCountryId() {
      if (countryIdCountryModel != null) {
         return countryIdCountryModel.getCountryId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>countryId</code> property.
    *
    * @param countryId the value for the <code>countryId</code> property
    */

   @Column(name = "CUST_MOB_NETWORK")
   public String getCustomerMobileNetwork() {
      return customerMobileNetwork;
   }

   public void setCustomerMobileNetwork(String customerMobileNetwork) {
      this.customerMobileNetwork = customerMobileNetwork;
   }

   @Column(name = "CNIC_ISSUANCE_DATE")
   public Date getCnicIssuanceDate() { return cnicIssuanceDate; }

   public void setCnicIssuanceDate(Date cnicIssuanceDate) { this.cnicIssuanceDate = cnicIssuanceDate; }


   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COUNTRY_ID")
   public CountryModel getRelationCountryIdCountryModel(){
      return countryIdCountryModel;
   }

   /**
    * Returns the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @return the value of the <code>countryIdCountryModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CountryModel getCountryIdCountryModel(){
      return getRelationCountryIdCountryModel();
   }

   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCountryIdCountryModel(CountryModel countryModel) {
      this.countryIdCountryModel = countryModel;
   }

   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */
   @javax.persistence.Transient
   public void setCountryIdCountryModel(CountryModel countryModel) {
      if(null != countryModel)
      {
         setRelationCountryIdCountryModel((CountryModel)countryModel.clone());
      }
   }

   /**
    * Sets the value of the <code>countryId</code> property.
    *
    * @param countryId the value for the <code>countryId</code> property
    */

   @javax.persistence.Transient
   public void setCountryId(Long countryId) {
      if(countryId == null)
      {
         countryIdCountryModel = null;
      }
      else
      {
         countryIdCountryModel = new CountryModel();
         countryIdCountryModel.setCountryId(countryId);
      }
   }

   //--------------REGISTRATION STATE-----------------------------------------------------------------
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "MOBILE_NETWORK_ID")
   public MobileNetworkModel getRelationMobileNetworkModel() {
      return mobileNetworkModel;
   }

   @Transient
   public void setRelationMobileNetworkModel(MobileNetworkModel mobileNetworkModel) {
      this.mobileNetworkModel = mobileNetworkModel;
   }

   @Transient
   public MobileNetworkModel getMobileNetworkModel() {
      return getRelationMobileNetworkModel();
   }

   @Transient
   public void setMobileNetworkModel(
           MobileNetworkModel mobileNetworkModel) {
      this.mobileNetworkModel = mobileNetworkModel;
   }

   @Transient
   public Long getMobileNetworkId() {
      if (mobileNetworkModel != null) {
         return mobileNetworkModel.getMobileNetworkId();
      } else {
         return null;
      }
   }

   @Transient
   public void setMobileNetworkId(Long mobileNetworkId) {
      if (mobileNetworkId == null) {
         this.mobileNetworkModel = null;
      } else {
         mobileNetworkModel = new MobileNetworkModel();
         mobileNetworkModel.setMobileNetworkId(mobileNetworkId);
      }
   }

   @Column(name = "DORMANT_MARKED_ON")
   public Date getDormantMarkedOn() {
      return dormantMarkedOn;
   }

   public void setDormantMarkedOn(Date dormantMarkedOn) {
      this.dormantMarkedOn = dormantMarkedOn;
   }

   @Column(name = "DORMANCY_REMOVED_BY")
   public Long getDormancyRemovedBy() {
      return dormancyRemovedBy;
   }

   public void setDormancyRemovedBy(Long dormancyRemovedBy) {
      this.dormancyRemovedBy = dormancyRemovedBy;
   }
}