package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingVo;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The CustomerModel entity bean.
 *
 * @author Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 * @spring.bean name="CustomerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_seq", sequenceName = "CUSTOMER_seq", allocationSize = 1)
@Table(name = "CUSTOMER")
public class CustomerModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 1854659766832597319L;
    public String registrationPlace;
    private SegmentModel segmentIdSegmentModel;
    private OlaCustomerAccountTypeModel customerAccountTypeIdCustomerAccountTypeModel;
    private LanguageModel languageIdLanguageModel;
    private FundSourceModel fundSourceIdFundSourceModel;
    private CustomerTypeModel customerTypeIdCustomerTypeModel;
    private TransactionModeModel transactionModeIdTransactionModeModel;
    private AccountPurposeModel accountPurposeIdAccountPurposeModel;
    private AccountReasonModel accountReasonIdAccountReasonModel;
    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;
    private AccountOpeningMethodModel accountOpeningMethodIdAccountOpeningMethodModel;
    private Collection<AppUserModel> customerIdAppUserModelList = new ArrayList<AppUserModel>();
    private Collection<CustomerAddressesModel> customerIdCustomerAddressesModelList = new ArrayList<CustomerAddressesModel>();
    private Collection<SmartMoneyAccountModel> customerIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
    private Collection<TransactionModel> customerIdTransactionModelList = new ArrayList<TransactionModel>();
    private Collection<CustomerPictureModel> customerPictureModelList = new ArrayList<CustomerPictureModel>();
    private Collection<CustomerRemitterModel> customerIdCustomerRemitterModelList = new ArrayList<CustomerRemitterModel>();
    private Long customerId;
    private String referringName1;
    private String referringNic1;
    private String referringName2;
    private String referringNic2;
    private String description;
    private Boolean register;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String comments;
    private String name;
    private String fundsSourceNarration;
    private Integer relationZong;
    private Integer relationAskari;
    private String fatherHusbandName;
    private String gender;
    private String otherFundSource;
    private String applicationN0;
    private String contactNo;
    private String landLineNo;
    private String mobileNo;
    private String email;
    //private byte[] customerPicture;
    private String employeeId;
    private String accountNature;
    private String typeOfAccount;
    private String currency;
    private String initialDeposit;
    private String occupation;
    private String ntn;
    private String residanceStatus;
    private String isMinor;
    private String guardianName;
    private String nokName;
    private String nokContactNo;
    private String nokRelationship;
    private String nokMobile;
    private String nokNic;
    private Boolean firstDebitAvailed;
    private Boolean firstCreditAvailed;
    private Boolean isCnicSeen;
    private String fax;
    private Boolean publicFigure;
    private String birthPlace;
    private String regStateComments;
    private String otherTransactionMode;
    private Long businessTypeId;
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
    private String incorporationDate;
    private String secpRegNo;
    private String salary;
    private String businessIncome;
    private String otherIncome;
    private Boolean screeningPerformed;
    private Long acNature;
    private Long nokIdType;
    private String nokIdNumber;
    private Boolean verisysDone;
    private String initialApplicationFormNumber;
    private TaxRegimeModel taxRegimeIdTaxRegimeModel;
    private Double fed;
    private Boolean isMPINGenerated = null;
    private Long isSetMpinLater;
    private Boolean isWebServiceEnabled = null;
    private Boolean isFonePayEnabled = null;

    private Long nadraNeagitiveReasonId;
    private String hraNokMob;
    private String hraOccupation;
    private String hraTrxnPurpose;
    private String nadraTrackingId;
    private String iban;
    private String companyName;

    private Boolean isCustomerUSSDEnabled;
    private String latitude;
    private String longitude;
    private String monthlyTurnOver;
    private String strockTrading;
    private String mutualFunds;
    private Boolean accountUpdate;
    private String clsResponseCode;
    private String purposeOfAccount;
    private String dualNationality;
    private String usCitizen;
    private Boolean blinkBvs;
    private Boolean Bvs;
    private String clsDebitBlock;
    private String clsCreditBlock;
    private Boolean accountOpenedByAma;
    private String amaCustomerContsent;
    private Date fatherCnicIssuanceDate;
    private Date fatherCnicExpiryDate;
    private String fatherCnicNo;
    private String motherCnicNo;
    private String fatherMotherMobileNo;
    private String custPicMakerComments;
    private String custPicCheckerComments;
    private String pNicPicMakerComments;
    private String pNicPicCheckerComments;
    private String bFormPicMakerComments;
    private String bFormPicCheckerComments;
    private String nicFrontPicMakerComments;
    private String nicFrontPicCheckerComments;
    private String nicBackPicMakerComments;
    private String nicBackPicCheckerComments;
    private String pNicBackPicMakerComments;
    private String pNicBackPicCheckerComments;
    private String riskLevel;
    private Boolean isPep;
    private Date pepMarkDate;

    private List<CustomerACNatureMarkingVo> customerACNatureMarkingVoList;

    /**
     * Default constructor.
     */
    public CustomerModel() {
    }

    public CustomerModel(Long customerId) {

        this.setCustomerId(customerId);
    }

    @Column(name = "CLS_RESPONSE_CODE")
    public String getClsResponseCode() {
        return clsResponseCode;
    }

    public void setClsResponseCode(String clsResponseCode) {
        this.clsResponseCode = clsResponseCode;
    }

    @Column(name = "STOCK_TRADING")
    public String getStrockTrading() {
        return strockTrading;
    }

    public void setStrockTrading(String strockTrading) {
        this.strockTrading = strockTrading;
    }

    @Column(name = "MUTUAL_FUNDS")
    public String getMutualFunds() {
        return mutualFunds;
    }

    public void setMutualFunds(String mutualFunds) {
        this.mutualFunds = mutualFunds;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCustomerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setCustomerId(primaryKey);
    }

    /**
     * Returns the value of the <code>customerId</code> property.
     */
    @Column(name = "CUSTOMER_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_seq")
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the <code>customerId</code> property.
     *
     * @param customerId the value for the <code>customerId</code> property
     */

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Column(name = "LATITUDE")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "EXPECTED_MONTHLY_TURNOVER")
    public String getMonthlyTurnOver() {
        return monthlyTurnOver;
    }

    public void setMonthlyTurnOver(String monthlyTurnOver) {
        this.monthlyTurnOver = monthlyTurnOver;
    }

    /**
     * Returns the value of the <code>referringName1</code> property.
     */
    @Column(name = "REFERRING_NAME1", length = 50)
    public String getReferringName1() {
        return referringName1;
    }

    /**
     * Sets the value of the <code>referringName1</code> property.
     *
     * @param referringName1 the value for the <code>referringName1</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setReferringName1(String referringName1) {
        this.referringName1 = referringName1;
    }

    /**
     * Returns the value of the <code>referringNic1</code> property.
     */
    @Column(name = "REFERRING_NIC1", length = 50)
    public String getReferringNic1() {
        return referringNic1;
    }

    /**
     * Sets the value of the <code>referringNic1</code> property.
     *
     * @param referringNic1 the value for the <code>referringNic1</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setReferringNic1(String referringNic1) {
        this.referringNic1 = referringNic1;
    }

    /**
     * Returns the value of the <code>referringName2</code> property.
     */
    @Column(name = "REFERRING_NAME2", length = 50)
    public String getReferringName2() {
        return referringName2;
    }

    /**
     * Sets the value of the <code>referringName2</code> property.
     *
     * @param referringName2 the value for the <code>referringName2</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setReferringName2(String referringName2) {
        this.referringName2 = referringName2;
    }

    /**
     * Returns the value of the <code>referringNic2</code> property.
     */
    @Column(name = "REFERRING_NIC2", length = 50)
    public String getReferringNic2() {
        return referringNic2;
    }

    /**
     * Sets the value of the <code>referringNic2</code> property.
     *
     * @param referringNic2 the value for the <code>referringNic2</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setReferringNic2(String referringNic2) {
        this.referringNic2 = referringNic2;
    }

    /**
     * Returns the value of the <code>description</code> property.
     */
    @Column(name = "DESCRIPTION", length = 250)
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the <code>description</code> property.
     *
     * @param description the value for the <code>description</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the value of the <code>register</code> property.
     */
    @Column(name = "IS_REGISTER", nullable = false)
    public Boolean getRegister() {
        return register;
    }

    /**
     * Sets the value of the <code>register</code> property.
     *
     * @param register the value for the <code>register</code> property
     */

    public void setRegister(Boolean register) {
        this.register = register;
    }

    /**
     * Returns the value of the <code>createdOn</code> property.
     */
    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the <code>createdOn</code> property.
     *
     * @param createdOn the value for the <code>createdOn</code> property
     */

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Returns the value of the <code>updatedOn</code> property.
     */
    @Column(name = "UPDATED_ON", nullable = false)
    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the value of the <code>updatedOn</code> property.
     *
     * @param updatedOn the value for the <code>updatedOn</code> property
     */

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     * Returns the value of the <code>versionNo</code> property.
     */
    @Version
    @Column(name = "VERSION_NO", nullable = false)
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * Sets the value of the <code>versionNo</code> property.
     *
     * @param versionNo the value for the <code>versionNo</code> property
     */

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Returns the value of the <code>comments</code> property.
     */
    @Column(name = "COMMENTS", length = 250)
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the <code>comments</code> property.
     *
     * @param comments the value for the <code>comments</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the value of the <code>fundsSourceNarration</code> property.
     */
    @Column(name = "FUNDS_SOURCE_NARRATION", length = 50)
    public String getFundsSourceNarration() {
        return fundsSourceNarration;
    }

    /**
     * Sets the value of the <code>fundsSourceNarration</code> property.
     *
     * @param fundsSourceNarration the value for the <code>fundsSourceNarration</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setFundsSourceNarration(String fundsSourceNarration) {
        this.fundsSourceNarration = fundsSourceNarration;
    }

    /**
     * Returns the value of the <code>relationZong</code> property.
     */
    @Column(name = "RELATION_TELCO")
    public Integer getRelationZong() {
        return relationZong;
    }

    /**
     * Sets the value of the <code>relationZong</code> property.
     *
     * @param relationZong the value for the <code>relationZong</code> property
     * @spring.validator type="integer"
     */

    public void setRelationZong(Integer relationZong) {
        this.relationZong = relationZong;
    }

    /**
     * Returns the value of the <code>relationAskari</code> property.
     */
    @Column(name = "RELATION_BANK")
    public Integer getRelationAskari() {
        return relationAskari;
    }

    /**
     * Sets the value of the <code>relationAskari</code> property.
     *
     * @param relationAskari the value for the <code>relationAskari</code> property
     * @spring.validator type="integer"
     */

    public void setRelationAskari(Integer relationAskari) {
        this.relationAskari = relationAskari;
    }

    /**
     * Returns the value of the <code>name</code> property.
     */
    @Column(name = "NAME", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the <code>name</code> property.
     *
     * @param name the value for the <code>name</code> property
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
     * Returns the value of the <code>fatherHusbandName</code> property.
     */
    @Column(name = "FATHER_HUSBAND_NAME", length = 50)
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    /**
     * Sets the value of the <code>fatherHusbandName</code> property.
     *
     * @param fatherHusbandName the value for the <code>fatherHusbandName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    /**
     * Returns the value of the <code>gender</code> property.
     */
    @Column(name = "GENDER", length = 1)
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the <code>gender</code> property.
     *
     * @param gender the value for the <code>gender</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="1"
     */

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the value of the <code>otherFundSource</code> property.
     */
    @Column(name = "OTHER_FUND_SOURCE", length = 50)
    public String getOtherFundSource() {
        return otherFundSource;
    }

    /**
     * Sets the value of the <code>otherFundSource</code> property.
     *
     * @param otherFundSource the value for the <code>otherFundSource</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setOtherFundSource(String otherFundSource) {
        this.otherFundSource = otherFundSource;
    }

    /**
     * Returns the value of the <code>applicationN0</code> property.
     */
    @Column(name = "APPLICATION_N0", length = 50)
    public String getApplicationN0() {
        return applicationN0;
    }

    /**
     * Sets the value of the <code>applicationN0</code> property.
     *
     * @param applicationN0 the value for the <code>applicationN0</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setApplicationN0(String applicationN0) {
        this.applicationN0 = applicationN0;
    }

    /**
     * Returns the value of the <code>contactNo</code> property.
     */
    @Column(name = "CONTACT_NO", length = 50)
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the value of the <code>contactNo</code> property.
     *
     * @param contactNo the value for the <code>contactNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Column(name = "BLINK_BVS")
    public Boolean getBlinkBvs() {
        return blinkBvs;
    }

    public void setBlinkBvs(Boolean blinkBvs) {
        this.blinkBvs = blinkBvs;
    }

    /**
     * Returns the value of the <code>landLineNo</code> property.
     */
    @Column(name = "LAND_LINE_NO", length = 50)
    public String getLandLineNo() {
        return landLineNo;
    }

    /**
     * Sets the value of the <code>landLineNo</code> property.
     *
     * @param landLineNo the value for the <code>landLineNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
    }

    /**
     * Returns the value of the <code>mobileNo</code> property.
     */
    @Column(name = "MOBILE_NO", length = 50)
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the <code>mobileNo</code> property.
     *
     * @param mobileNo the value for the <code>mobileNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Returns the value of the <code>email</code> property.
     */
    @Column(name = "EMAIL", length = 50)
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the <code>email</code> property.
     *
     * @param email the value for the <code>email</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the value of the <code>segmentIdSegmentModel</code> relation property.
     *
     * @return the value of the <code>segmentIdSegmentModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    public SegmentModel getRelationSegmentIdSegmentModel() {
        return segmentIdSegmentModel;
    }

    /**
     * Sets the value of the <code>segmentIdSegmentModel</code> relation property.
     *
     * @param segmentModel a value for <code>segmentIdSegmentModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
        this.segmentIdSegmentModel = segmentModel;
    }

    @Column(name = "COMPANY_NAME ", length = 50)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the value of the <code>segmentIdSegmentModel</code> relation property.
     *
     * @return the value of the <code>segmentIdSegmentModel</code> relation property.
     */
    @javax.persistence.Transient
    public SegmentModel getSegmentIdSegmentModel() {
        return getRelationSegmentIdSegmentModel();
    }

    /**
     * Sets the value of the <code>segmentIdSegmentModel</code> relation property.
     *
     * @param segmentModel a value for <code>segmentIdSegmentModel</code>.
     */
    @javax.persistence.Transient
    public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
        if (null != segmentModel) {
            setRelationSegmentIdSegmentModel((SegmentModel) segmentModel.clone());
        }
    }

    /**
     * Returns the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     *
     * @return the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ACCOUNT_TYPE_ID")
    public OlaCustomerAccountTypeModel getRelationCustomerAccountTypeIdCustomerAccountTypeModel() {
        return customerAccountTypeIdCustomerAccountTypeModel;
    }

    /**
     * Sets the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     *
     * @param OlaCustomerAccountTypeModel a value for <code>customerAccountTypeIdCustomerAccountTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCustomerAccountTypeIdCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
        this.customerAccountTypeIdCustomerAccountTypeModel = customerAccountTypeModel;
    }

    /**
     * Returns the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     *
     * @return the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     */
    @javax.persistence.Transient
    public OlaCustomerAccountTypeModel getCustomerAccountTypeIdCustomerAccountTypeModel() {
        return getRelationCustomerAccountTypeIdCustomerAccountTypeModel();
    }

    /**
     * Sets the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
     *
     * @param customerAccountTypeModel a value for <code>customerAccountTypeIdCustomerAccountTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setCustomerAccountTypeIdCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
        if (null != olaCustomerAccountTypeModel) {
            setRelationCustomerAccountTypeIdCustomerAccountTypeModel((OlaCustomerAccountTypeModel) olaCustomerAccountTypeModel.clone());
        }
    }

    /**
     * Returns the value of the <code>languageIdLanguageModel</code> relation property.
     *
     * @return the value of the <code>languageIdLanguageModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "LANGUAGE_ID")
    public LanguageModel getRelationLanguageIdLanguageModel() {
        return languageIdLanguageModel;
    }

    /**
     * Sets the value of the <code>languageIdLanguageModel</code> relation property.
     *
     * @param languageModel a value for <code>languageIdLanguageModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationLanguageIdLanguageModel(LanguageModel languageModel) {
        this.languageIdLanguageModel = languageModel;
    }

    /**
     * Returns the value of the <code>languageIdLanguageModel</code> relation property.
     *
     * @return the value of the <code>languageIdLanguageModel</code> relation property.
     */
    @javax.persistence.Transient
    public LanguageModel getLanguageIdLanguageModel() {
        return getRelationLanguageIdLanguageModel();
    }

    /**
     * Sets the value of the <code>languageIdLanguageModel</code> relation property.
     *
     * @param languageModel a value for <code>languageIdLanguageModel</code>.
     */
    @javax.persistence.Transient
    public void setLanguageIdLanguageModel(LanguageModel languageModel) {
        if (null != languageModel) {
            setRelationLanguageIdLanguageModel((LanguageModel) languageModel.clone());
        }
    }


    /**
     * Returns the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     *
     * @return the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "FUND_SOURCE_ID")
    public FundSourceModel getRelationFundSourceIdFundSourceModel() {
        return fundSourceIdFundSourceModel;
    }

    /**
     * Sets the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     *
     * @param fundSourceModel a value for <code>fundSourceIdFundSourceModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationFundSourceIdFundSourceModel(FundSourceModel fundSourceModel) {
        this.fundSourceIdFundSourceModel = fundSourceModel;
    }

    /**
     * Returns the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     *
     * @return the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     */
    @javax.persistence.Transient
    public FundSourceModel getFundSourceIdFundSourceModel() {
        return getRelationFundSourceIdFundSourceModel();
    }

    /**
     * Sets the value of the <code>fundSourceIdFundSourceModel</code> relation property.
     *
     * @param fundSourceModel a value for <code>fundSourceIdFundSourceModel</code>.
     */
    @javax.persistence.Transient
    public void setFundSourceIdFundSourceModel(FundSourceModel fundSourceModel) {
        if (null != fundSourceModel) {
            setRelationFundSourceIdFundSourceModel((FundSourceModel) fundSourceModel.clone());
        }
    }


    /**
     * Returns the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     *
     * @return the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_TYPE_ID")
    public CustomerTypeModel getRelationCustomerTypeIdCustomerTypeModel() {
        return customerTypeIdCustomerTypeModel;
    }

    /**
     * Sets the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     *
     * @param customerTypeModel a value for <code>customerTypeIdCustomerTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCustomerTypeIdCustomerTypeModel(CustomerTypeModel customerTypeModel) {
        this.customerTypeIdCustomerTypeModel = customerTypeModel;
    }

    /**
     * Returns the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     *
     * @return the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     */
    @javax.persistence.Transient
    public CustomerTypeModel getCustomerTypeIdCustomerTypeModel() {
        return getRelationCustomerTypeIdCustomerTypeModel();
    }

    /**
     * Sets the value of the <code>customerTypeIdCustomerTypeModel</code> relation property.
     *
     * @param customerTypeModel a value for <code>customerTypeIdCustomerTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setCustomerTypeIdCustomerTypeModel(CustomerTypeModel customerTypeModel) {
        if (null != customerTypeModel) {
            setRelationCustomerTypeIdCustomerTypeModel((CustomerTypeModel) customerTypeModel.clone());
        }
    }


    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel() {
        return updatedByAppUserModel;
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
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel() {
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationUpdatedByAppUserModel((AppUserModel) appUserModel.clone());
        }
    }


    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel() {
        return createdByAppUserModel;
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
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel() {
        return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
        }
    }


    /**
     * Add the related AppUserModel to this one-to-many relation.
     *
     * @param appUserModel object to be added.
     */

    public void addCustomerIdAppUserModel(AppUserModel appUserModel) {
        appUserModel.setRelationCustomerIdCustomerModel(this);
        customerIdAppUserModelList.add(appUserModel);
    }

    /**
     * Remove the related AppUserModel to this one-to-many relation.
     *
     * @param appUserModel object to be removed.
     */

    public void removeCustomerIdAppUserModel(AppUserModel appUserModel) {
        appUserModel.setRelationCustomerIdCustomerModel(null);
        customerIdAppUserModelList.remove(appUserModel);
    }

    /**
     * Get a list of related AppUserModel objects of the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @return Collection of AppUserModel objects.
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerIdCustomerModel")
    @JoinColumn(name = "CUSTOMER_ID")
    public Collection<AppUserModel> getCustomerIdAppUserModelList() throws Exception {
        return customerIdAppUserModelList;
    }


    /**
     * Set a list of AppUserModel related objects to the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @param appUserModelList the list of related objects.
     */
    public void setCustomerIdAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
        this.customerIdAppUserModelList = appUserModelList;
    }


    /**
     * Add the related CustomerAddressesModel to this one-to-many relation.
     *
     * @param customerAddressesModel object to be added.
     */

    public void addCustomerIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {
        customerAddressesModel.setRelationCustomerIdCustomerModel(this);
        customerIdCustomerAddressesModelList.add(customerAddressesModel);
    }

    /**
     * Remove the related CustomerAddressesModel to this one-to-many relation.
     *
     * @param customerAddressesModel object to be removed.
     */

    public void removeCustomerIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {
        customerAddressesModel.setRelationCustomerIdCustomerModel(null);
        customerIdCustomerAddressesModelList.remove(customerAddressesModel);
    }

    /**
     * Get a list of related CustomerAddressesModel objects of the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @return Collection of CustomerAddressesModel objects.
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerIdCustomerModel")
    @JoinColumn(name = "CUSTOMER_ID")
    public Collection<CustomerAddressesModel> getCustomerIdCustomerAddressesModelList() throws Exception {
        return customerIdCustomerAddressesModelList;
    }


    /**
     * Set a list of CustomerAddressesModel related objects to the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @param customerAddressesModelList the list of related objects.
     */
    public void setCustomerIdCustomerAddressesModelList(Collection<CustomerAddressesModel> customerAddressesModelList) throws Exception {
        this.customerIdCustomerAddressesModelList = customerAddressesModelList;
    }


    /**
     * Add the related SmartMoneyAccountModel to this one-to-many relation.
     *
     * @param smartMoneyAccountModel object to be added.
     */

    public void addCustomerIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
        smartMoneyAccountModel.setRelationCustomerIdCustomerModel(this);
        customerIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
    }

    /**
     * Remove the related SmartMoneyAccountModel to this one-to-many relation.
     *
     * @param smartMoneyAccountModel object to be removed.
     */

    public void removeCustomerIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
        smartMoneyAccountModel.setRelationCustomerIdCustomerModel(null);
        customerIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);
    }

    /**
     * Get a list of related SmartMoneyAccountModel objects of the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @return Collection of SmartMoneyAccountModel objects.
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerIdCustomerModel")
    @JoinColumn(name = "CUSTOMER_ID")
    public Collection<SmartMoneyAccountModel> getCustomerIdSmartMoneyAccountModelList() throws Exception {
        return customerIdSmartMoneyAccountModelList;
    }


    /**
     * Set a list of SmartMoneyAccountModel related objects to the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @param smartMoneyAccountModelList the list of related objects.
     */
    public void setCustomerIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
        this.customerIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
    }


    /**
     * Add the related TransactionModel to this one-to-many relation.
     *
     * @param transactionModel object to be added.
     */

    public void addCustomerIdTransactionModel(TransactionModel transactionModel) {
        transactionModel.setRelationCustomerIdCustomerModel(this);
        customerIdTransactionModelList.add(transactionModel);
    }

    /**
     * Remove the related TransactionModel to this one-to-many relation.
     *
     * @param transactionModel object to be removed.
     */

    public void removeCustomerIdTransactionModel(TransactionModel transactionModel) {
        transactionModel.setRelationCustomerIdCustomerModel(null);
        customerIdTransactionModelList.remove(transactionModel);
    }

    /**
     * Get a list of related TransactionModel objects of the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @return Collection of TransactionModel objects.
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerIdCustomerModel")
    @JoinColumn(name = "CUSTOMER_ID")
    public Collection<TransactionModel> getCustomerIdTransactionModelList() throws Exception {
        return customerIdTransactionModelList;
    }


    /**
     * Set a list of TransactionModel related objects to the CustomerModel object.
     * These objects are in a bidirectional one-to-many relation by the CustomerId member.
     *
     * @param transactionModelList the list of related objects.
     */
    public void setCustomerIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
        this.customerIdTransactionModelList = transactionModelList;
    }

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerModel")
    @JoinColumn(name = "CUSTOMER_ID")
    public Collection<CustomerPictureModel> getCustomerPictureModelList() {
        return customerPictureModelList;
    }

    public void setCustomerPictureModelList(
            Collection<CustomerPictureModel> customerPictureModelList) {
        this.customerPictureModelList = customerPictureModelList;
    }

    /**
     * Returns the value of the <code>segmentId</code> property.
     */
    @javax.persistence.Transient
    public Long getSegmentId() {
        if (segmentIdSegmentModel != null) {
            return segmentIdSegmentModel.getSegmentId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>segmentId</code> property.
     *
     * @param segmentId the value for the <code>segmentId</code> property
     */

    @javax.persistence.Transient
    public void setSegmentId(Long segmentId) {
        if (segmentId == null) {
            segmentIdSegmentModel = null;
        } else {
            segmentIdSegmentModel = new SegmentModel();
            segmentIdSegmentModel.setSegmentId(segmentId);
        }
    }

    /**
     * Returns the value of the <code>customerAccountTypeId</code> property.
     */
    @javax.persistence.Transient
    public Long getCustomerAccountTypeId() {
        if (customerAccountTypeIdCustomerAccountTypeModel != null) {
            return customerAccountTypeIdCustomerAccountTypeModel.getCustomerAccountTypeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>customerAccountTypeId</code> property.
     *
     * @param customerAccountTypeId the value for the <code>customerAccountTypeId</code> property
     */

    @javax.persistence.Transient
    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        if (customerAccountTypeId == null) {
            customerAccountTypeIdCustomerAccountTypeModel = null;
        } else {
            customerAccountTypeIdCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
            customerAccountTypeIdCustomerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);
        }
    }

    /**
     * Returns the value of the <code>languageId</code> property.
     */
    @javax.persistence.Transient
    public Long getLanguageId() {
        if (languageIdLanguageModel != null) {
            return languageIdLanguageModel.getLanguageId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>languageId</code> property.
     *
     * @param languageId the value for the <code>languageId</code> property
     */

    @javax.persistence.Transient
    public void setLanguageId(Long languageId) {
        if (languageId == null) {
            languageIdLanguageModel = null;
        } else {
            languageIdLanguageModel = new LanguageModel();
            languageIdLanguageModel.setLanguageId(languageId);
        }
    }

    /**
     * Returns the value of the <code>fundSourceId</code> property.
     */
    @javax.persistence.Transient
    public Long getFundSourceId() {
        if (fundSourceIdFundSourceModel != null) {
            return fundSourceIdFundSourceModel.getFundSourceId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>fundSourceId</code> property.
     *
     * @param fundSourceId the value for the <code>fundSourceId</code> property
     */

    @javax.persistence.Transient
    public void setFundSourceId(Long fundSourceId) {
        if (fundSourceId == null) {
            fundSourceIdFundSourceModel = null;
        } else {
            fundSourceIdFundSourceModel = new FundSourceModel();
            fundSourceIdFundSourceModel.setFundSourceId(fundSourceId);
        }
    }

    /**
     * Returns the value of the <code>customerTypeId</code> property.
     */
    @javax.persistence.Transient
    public Long getCustomerTypeId() {
        if (customerTypeIdCustomerTypeModel != null) {
            return customerTypeIdCustomerTypeModel.getCustomerTypeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>customerTypeId</code> property.
     *
     * @param customerTypeId the value for the <code>customerTypeId</code> property
     */

    @javax.persistence.Transient
    public void setCustomerTypeId(Long customerTypeId) {
        if (customerTypeId == null) {
            customerTypeIdCustomerTypeModel = null;
        } else {
            customerTypeIdCustomerTypeModel = new CustomerTypeModel();
            customerTypeIdCustomerTypeModel.setCustomerTypeId(customerTypeId);
        }
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
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
        if (appUserId == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
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
        if (appUserId == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     *
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + getCustomerId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&customerId=" + getCustomerId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "customerId";
        return primaryKeyFieldName;
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("SegmentModel");
        associationModel.setPropertyName("relationSegmentIdSegmentModel");
        associationModel.setValue(getRelationSegmentIdSegmentModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("OlaCustomerAccountTypeModel");
        associationModel.setPropertyName("relationCustomerAccountTypeIdCustomerAccountTypeModel");
        associationModel.setValue(getRelationCustomerAccountTypeIdCustomerAccountTypeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AccountOpeningMethodModel");
        associationModel.setPropertyName("relationAccountOpeningMethodIdAccountOpeningMethodModel");
        associationModel.setValue(getRelationAccountOpeningMethodIdAccountOpeningMethodModel());
        associationModelList.add(associationModel);


        associationModel = new AssociationModel();

        associationModel.setClassName("LanguageModel");
        associationModel.setPropertyName("relationLanguageIdLanguageModel");
        associationModel.setValue(getRelationLanguageIdLanguageModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("FundSourceModel");
        associationModel.setPropertyName("relationFundSourceIdFundSourceModel");
        associationModel.setValue(getRelationFundSourceIdFundSourceModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("CustomerTypeModel");
        associationModel.setPropertyName("relationCustomerTypeIdCustomerTypeModel");
        associationModel.setValue(getRelationCustomerTypeIdCustomerTypeModel());

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

        associationModel.setClassName("TransactionModeModel");
        associationModel.setPropertyName("relationTransactionModeIdTransactionModeModel");
        associationModel.setValue(getRelationTransactionModeIdTransactionModeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AccountPurposeModel");
        associationModel.setPropertyName("relationAccountPurposeIdAccountPurposeModel");
        associationModel.setValue(getRelationAccountPurposeIdAccountPurposeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AccountReasonModel");
        associationModel.setPropertyName("relationAccountReasonIdAccountReasonModel");
        associationModel.setValue(getRelationAccountReasonIdAccountReasonModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("TaxRegimeModel");
        associationModel.setPropertyName("relationTaxRegimeIdTaxRegimeModel");
        associationModel.setValue(getRelationTaxRegimeIdTaxRegimeModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    /*@Column(name = "CUSTOMER_PICTURE")
	public byte[] getCustomerPicture() {
		return customerPicture;
	}

	public void setCustomerPicture(byte[] customerPicture) {
		this.customerPicture = customerPicture;
	}*/
    @Column(name = "EMPLOYEE_ID", length = 50)
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the value of the <code>employeeId</code> property.
     *
     * @param description the value for the <code>employeeId</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name = "ACCOUNT_NATURE")
    public String getAccountNature() {
        return accountNature;
    }

    public void setAccountNature(String accountNature) {
        this.accountNature = accountNature;
    }

    @Column(name = "TYPE_OF_ACCOUNT")
    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    @Column(name = "CURRENCY")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "INITIAL_DEPOSIT")
    public String getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(String initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    @Column(name = "OCCUPATION")
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Column(name = "NTN")
    public String getNtn() {
        return ntn;
    }

    public void setNtn(String ntn) {
        this.ntn = ntn;
    }

    @Column(name = "RESIDANCE_STATUS")
    public String getResidanceStatus() {
        return residanceStatus;
    }

    public void setResidanceStatus(String residanceStatus) {
        this.residanceStatus = residanceStatus;
    }

    @Column(name = "MINOR")
    public String getIsMinor() {
        return isMinor;
    }

    public void setIsMinor(String isMinor) {
        this.isMinor = isMinor;
    }

    @Column(name = "GUARDIAN_NAME")
    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    @Column(name = "NOK_NAME")
    public String getNokName() {
        return nokName;
    }

    public void setNokName(String nokName) {
        this.nokName = nokName;
    }

    @Column(name = "NOK_CONTACT_NO")
    public String getNokContactNo() {
        return nokContactNo;
    }

    public void setNokContactNo(String nokContactNo) {
        this.nokContactNo = nokContactNo;
    }

    @Column(name = "NOK_RELATIONSHIP")
    public String getNokRelationship() {
        return nokRelationship;
    }

    public void setNokRelationship(String nokRelationship) {
        this.nokRelationship = nokRelationship;
    }

    @Column(name = "FIRST_DEBIT_AVAILED")
    public Boolean getFirstDebitAvailed() {
        return firstDebitAvailed;
    }

    public void setFirstDebitAvailed(Boolean firstDebitAvailed) {
        this.firstDebitAvailed = firstDebitAvailed;
    }

    @Column(name = "FIRST_CREDIT_AVAILED")
    public Boolean getFirstCreditAvailed() {
        return firstCreditAvailed;
    }

    public void setFirstCreditAvailed(Boolean firstCreditAvailed) {
        this.firstCreditAvailed = firstCreditAvailed;
    }

    @Column(name = "IS_CNIC_SEEN")
    public Boolean getIsCnicSeen() {
        return isCnicSeen;
    }

    public void setIsCnicSeen(Boolean isCnicSeen) {
        this.isCnicSeen = isCnicSeen;
    }

    @Column(name = "NOK_MOBILE")
    public String getNokMobile() {
        return nokMobile;
    }

    public void setNokMobile(String nokMobile) {
        this.nokMobile = nokMobile;
    }

    @Column(name = "FAX")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "PUBLIC_FIGURE")
    public Boolean getPublicFigure() {
        return publicFigure;
    }

    public void setPublicFigure(Boolean publicFigure) {
        this.publicFigure = publicFigure;
    }


    @javax.persistence.Transient
    public TransactionModeModel getTransactionModeIdTransactionModeModel() {
        return getRelationTransactionModeIdTransactionModeModel();
    }

    @javax.persistence.Transient
    public void setTransactionModeIdTransactionModeModel(TransactionModeModel transactionModeModel) {
        if (null != transactionModeModel) {
            setRelationTransactionModeIdTransactionModeModel((TransactionModeModel) transactionModeModel.clone());
        }
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TRANSACTION_MODE_ID")
    public TransactionModeModel getRelationTransactionModeIdTransactionModeModel() {
        return transactionModeIdTransactionModeModel;
    }

    @javax.persistence.Transient
    public void setRelationTransactionModeIdTransactionModeModel(TransactionModeModel transactionModeModel) {
        this.transactionModeIdTransactionModeModel = transactionModeModel;
    }

    @javax.persistence.Transient
    public Long getTransactionModeId() {
        if (transactionModeIdTransactionModeModel != null) {
            return transactionModeIdTransactionModeModel.getTransactionModeId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setTransactionModeId(Long transactionModeId) {
        if (transactionModeId == null) {
            transactionModeIdTransactionModeModel = null;
        } else {
            transactionModeIdTransactionModeModel = new TransactionModeModel();
            transactionModeIdTransactionModeModel.setTransactionModeId(transactionModeId);
        }
    }

    @javax.persistence.Transient
    public AccountPurposeModel getAccountPurposeIdAccountPurposeModel() {
        return getRelationAccountPurposeIdAccountPurposeModel();
    }

    @javax.persistence.Transient
    public void setAccountPurposeIdAccountPurposeModel(AccountPurposeModel accountPurposeModel) {
        if (null != accountPurposeModel) {
            setRelationAccountPurposeIdAccountPurposeModel((AccountPurposeModel) accountPurposeModel.clone());
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_PURPOSE_ID")
    public AccountPurposeModel getRelationAccountPurposeIdAccountPurposeModel() {
        return accountPurposeIdAccountPurposeModel;
    }

    @javax.persistence.Transient
    public void setRelationAccountPurposeIdAccountPurposeModel(AccountPurposeModel accountPurposeModel) {
        this.accountPurposeIdAccountPurposeModel = accountPurposeModel;
    }

    @javax.persistence.Transient
    public Long getAccountPurposeId() {
        if (accountPurposeIdAccountPurposeModel != null) {
            return accountPurposeIdAccountPurposeModel.getAccountPurposeId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setAccountPurposeId(Long accountPurposeId) {
        if (accountPurposeId == null) {
            accountPurposeIdAccountPurposeModel = null;
        } else {
            accountPurposeIdAccountPurposeModel = new AccountPurposeModel();
            accountPurposeIdAccountPurposeModel.setAccountPurposeId(accountPurposeId);
        }
    }

    /************************/
    //adding filed account_reason_id - turab
    @javax.persistence.Transient
    public AccountReasonModel getAccountReasonIdAccountReasonModel() {
        return getRelationAccountReasonIdAccountReasonModel();
    }

    @javax.persistence.Transient
    public void setAccountReasonIdAccountReasonModel(AccountReasonModel accountReasonModel) {
        if (null != accountReasonModel) {
            setRelationAccountReasonIdAccountReasonModel((AccountReasonModel) accountReasonModel.clone());
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_REASON_ID")
    public AccountReasonModel getRelationAccountReasonIdAccountReasonModel() {
        return accountReasonIdAccountReasonModel;
    }

    @javax.persistence.Transient
    public void setRelationAccountReasonIdAccountReasonModel(AccountReasonModel accountReasonModel) {
        this.accountReasonIdAccountReasonModel = accountReasonModel;
    }

    @javax.persistence.Transient
    public Long getAccountReasonId() {
        if (accountReasonIdAccountReasonModel != null) {
            return accountReasonIdAccountReasonModel.getAccountReasonId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setAccountReasonId(Long accountReasonId) {
        if (accountReasonId == null) {
            accountReasonIdAccountReasonModel = null;
        } else {
            accountReasonIdAccountReasonModel = new AccountReasonModel();
            accountReasonIdAccountReasonModel.setAccountReasonId(accountReasonId);
        }
    }

    /************************/

    @Column(name = "BIRTH_PLACE")
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Column(name = "REGISTRATION_STATE_COMMENTS")
    public String getRegStateComments() {
        return regStateComments;
    }

    public void setRegStateComments(String regStateComments) {
        this.regStateComments = regStateComments;
    }

    @Column(name = "OTHER_TX_MODE")
    public String getOtherTransactionMode() {
        return otherTransactionMode;
    }

    public void setOtherTransactionMode(String otherTransactionMode) {
        this.otherTransactionMode = otherTransactionMode;
    }

    @Column(name = "BUISNESS_TYPE_ID")
    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
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
    public String getIncorporationDate() {
        return incorporationDate;
    }

    public void setIncorporationDate(String incorporationDate) {
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

    @Column(name = "NOK_NIC")
    public String getNokNic() {
        return nokNic;
    }

    public void setNokNic(String nokNic) {
        this.nokNic = nokNic;
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


    @Column(name = "HRA_NOKMOB")
    public String getHraNokMob() {
        return hraNokMob;
    }

    public void setHraNokMob(String hraNokMob) {
        this.hraNokMob = hraNokMob;
    }

    @Column(name = "HRA_OCCUPATION")
    public String getHraOccupation() {
        return hraOccupation;
    }

    public void setHraOccupation(String hraOccupation) {
        this.hraOccupation = hraOccupation;
    }

    @Column(name = "HRA_TRX_PURPOSE")
    public String getHraTrxnPurpose() {
        return hraTrxnPurpose;
    }

    public void setHraTrxnPurpose(String hraTrxnPurpose) {
        this.hraTrxnPurpose = hraTrxnPurpose;
    }

    @Column(name = "NOK_ID_NO")
    public String getNokIdNumber() {
        return nokIdNumber;
    }

    public void setNokIdNumber(String nokIdNumber) {
        if (nokIdNumber != null) {
            this.nokIdNumber = nokIdNumber;
        }
    }

    @Column(name = "NOK_ID_TYPE")
    public Long getNokIdType() {
        return nokIdType;
    }

    public void setNokIdType(Long nokIdType) {
        if (nokIdType != null) {
            this.nokIdType = nokIdType;
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

    @Column(name = "INITIAL_APP_FORM_NUMBER")
    public String getInitialApplicationFormNumber() {
        return initialApplicationFormNumber;
    }

    public void setInitialApplicationFormNumber(String initialApplicationFormNumber) {
        this.initialApplicationFormNumber = initialApplicationFormNumber;
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

    @Column(name = "ACC_OPENED_BY_AMA")
    public Boolean getAccountOpenedByAma() {
        return accountOpenedByAma;
    }

    public void setAccountOpenedByAma(Boolean accountOpenedByAma) {
        this.accountOpenedByAma = accountOpenedByAma;
    }

    @Column(name = "AMA_ACC_CONSENT")
    public String getAmaCustomerContsent() {
        return amaCustomerContsent;
    }

    public void setAmaCustomerContsent(String amaCustomerContsent) {
        this.amaCustomerContsent = amaCustomerContsent;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TAX_REGIME_ID")
    public TaxRegimeModel getRelationTaxRegimeIdTaxRegimeModel() {
        return taxRegimeIdTaxRegimeModel;
    }

    @javax.persistence.Transient
    public void setRelationTaxRegimeIdTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
        this.taxRegimeIdTaxRegimeModel = taxRegimeModel;
    }

    @javax.persistence.Transient
    public TaxRegimeModel getTaxRegimeIdTaxRegimeModel() {
        return getRelationTaxRegimeIdTaxRegimeModel();
    }

    @javax.persistence.Transient
    public void setTaxRegimeIdTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
        if (null != taxRegimeModel) {
            setRelationTaxRegimeIdTaxRegimeModel((TaxRegimeModel) taxRegimeModel.clone());
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
        if (taxRegimeId == null) {
            taxRegimeIdTaxRegimeModel = null;
        } else {
            taxRegimeIdTaxRegimeModel = new TaxRegimeModel();
            taxRegimeIdTaxRegimeModel.setTaxRegimeId(taxRegimeId);
        }
    }

    @Column(name = "IS_MPIN_GENERATED")
    public Boolean getIsMPINGenerated() {
        return isMPINGenerated;
    }

    public void setIsMPINGenerated(Boolean isMPINGenerated) {
        this.isMPINGenerated = isMPINGenerated;
    }

    @Column(name = "NADRA_NEAGITIVE_REASON_ID")
    public Long getNadraNeagitiveReasonId() {
        return nadraNeagitiveReasonId;
    }

    public void setNadraNeagitiveReasonId(Long nadraNeagitiveReasonId) {
        this.nadraNeagitiveReasonId = nadraNeagitiveReasonId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_OPENING_METHOD_ID")
    public AccountOpeningMethodModel getRelationAccountOpeningMethodIdAccountOpeningMethodModel() {
        return accountOpeningMethodIdAccountOpeningMethodModel;
    }

    @javax.persistence.Transient
    public void setRelationAccountOpeningMethodIdAccountOpeningMethodModel(AccountOpeningMethodModel accountOpeningMethodIdAccountOpeningMethodModel) {
        this.accountOpeningMethodIdAccountOpeningMethodModel = accountOpeningMethodIdAccountOpeningMethodModel;
    }

    @javax.persistence.Transient
    public AccountOpeningMethodModel getAccountOpeningMethodIdAccountOpeningMethodModel() {
        return getRelationAccountOpeningMethodIdAccountOpeningMethodModel();
    }

    @javax.persistence.Transient
    public void setAccountOpeningMethodIdAccountOpeningMethodModel(AccountOpeningMethodModel accountOpeningMethodModel) {
        if (null != accountOpeningMethodModel) {
            setRelationAccountOpeningMethodIdAccountOpeningMethodModel((AccountOpeningMethodModel) accountOpeningMethodModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getAccountMethodId() {
        if (accountOpeningMethodIdAccountOpeningMethodModel != null)
            return accountOpeningMethodIdAccountOpeningMethodModel.getAccountOpeningMethodId();
        else
            return null;
    }

    @javax.persistence.Transient
    public void setAccountMethodId(Long accountMethodId) {
        if (accountMethodId != null) {
            accountOpeningMethodIdAccountOpeningMethodModel = new AccountOpeningMethodModel();
            accountOpeningMethodIdAccountOpeningMethodModel.setAccountOpeningMethodId(accountMethodId);
        } else {
            accountOpeningMethodIdAccountOpeningMethodModel = null;
        }
    }

    @Column(name = "IS_WEB_SERVICE_ENABLED")
    public Boolean getWebServiceEnabled() {
        return isWebServiceEnabled;
    }

    public void setWebServiceEnabled(Boolean webServiceEnabled) {
        isWebServiceEnabled = webServiceEnabled;
    }

    @Column(name = "IS_FONEPAY_ENABLED")
    public Boolean getFonePayEnabled() {
        return isFonePayEnabled;
    }

    public void setFonePayEnabled(Boolean fonePayEnabled) {
        isFonePayEnabled = fonePayEnabled;
    }

    public void addCustomerIdCustomerRemitterModel(CustomerRemitterModel customerRemitterModel) {
        customerRemitterModel.setRelationCustomerIdCustomerModel(this);
        customerIdCustomerRemitterModelList.add(customerRemitterModel);

    }

    @Column(name = "NADRA_TRACKING_ID")
    public String getNadraTrackingId() {
        return nadraTrackingId;
    }

    public void setNadraTrackingId(String nadraTrackingId) {
        this.nadraTrackingId = nadraTrackingId;
    }


    @Column(name = "IS_CUSTOMER_USSD_ENABLED")
    public Boolean getCustomerUSSDEnabled() {
        return isCustomerUSSDEnabled;
    }

    public void setCustomerUSSDEnabled(Boolean customerUSSDEnabled) {
        isCustomerUSSDEnabled = customerUSSDEnabled;
    }

    @Column(name = "IS_SET_MPIN_LATER")
    public Long getIsSetMpinLater() {
        return isSetMpinLater;
    }

    public void setIsSetMpinLater(Long isSetMpinLater) {
        this.isSetMpinLater = isSetMpinLater;
    }

    @Column(name = "IBAN")
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Transient
    public List<CustomerACNatureMarkingVo> getCustomerACNatureMarkingVoList() {
        return customerACNatureMarkingVoList;
    }

    public void setCustomerACNatureMarkingVoList(List<CustomerACNatureMarkingVo> customerACNatureMarkingVoList) {
        this.customerACNatureMarkingVoList = customerACNatureMarkingVoList;
    }

    @Column(name = "ACC_UPDATE")
    public Boolean getAccountUpdate() {
        return accountUpdate;
    }

    public void setAccountUpdate(Boolean accountUpdate) {
        this.accountUpdate = accountUpdate;
    }

    @Column(name = "PURPOSE_OF_ACCOUNT")
    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    @Column(name = "DUAL_NATIONALITY")
    public String getDualNationality() {
        return dualNationality;
    }

    public void setDualNationality(String dualNationality) {
        this.dualNationality = dualNationality;
    }

    @Column(name = "US_CITIZEN")
    public String getUsCitizen() {
        return usCitizen;
    }

    public void setUsCitizen(String usCitizen) {
        this.usCitizen = usCitizen;
    }

    @Column(name = "CLS_CREDIT_BLOCK")
    public String getClsCreditBlock() {
        return clsCreditBlock;
    }

    public void setClsCreditBlock(String clsCreditBlock) {
        this.clsCreditBlock = clsCreditBlock;
    }

    @Column(name = "CLS_DEBIT_BLOCK")
    public String getClsDebitBlock() {
        return clsDebitBlock;
    }

    public void setClsDebitBlock(String clsDebitBlock) {
        this.clsDebitBlock = clsDebitBlock;
    }

    @Column(name = "FATHER_CNIC_ISSUANCE_DATE")
    public Date getFatherCnicIssuanceDate() {
        return fatherCnicIssuanceDate;
    }

    public void setFatherCnicIssuanceDate(Date fatherCnicIssuanceDate) {
        this.fatherCnicIssuanceDate = fatherCnicIssuanceDate;
    }

    @Column(name = "FATHER_CNIC_EXPIRY_DATE")
    public Date getFatherCnicExpiryDate() {
        return fatherCnicExpiryDate;
    }

    public void setFatherCnicExpiryDate(Date fatherCnicExpiryDate) {
        this.fatherCnicExpiryDate = fatherCnicExpiryDate;
    }

    @Column(name = "FATHER_CNIC")
    public String getFatherCnicNo() {
        return fatherCnicNo;
    }

    public void setFatherCnicNo(String fatherCnicNo) {
        this.fatherCnicNo = fatherCnicNo;
    }

    @Column(name = "MOTHER_CNIC")
    public String getMotherCnicNo() {
        return motherCnicNo;
    }

    public void setMotherCnicNo(String motherCnicNo) {
        this.motherCnicNo = motherCnicNo;
    }

    @Column(name = "FATHER_MOTHER_MOBILE_NO")
    public String getFatherMotherMobileNo() {
        return fatherMotherMobileNo;
    }

    public void setFatherMotherMobileNo(String fatherMotherMobileNo) {this.fatherMotherMobileNo = fatherMotherMobileNo;}

    @Column(name = "CUST_PIC_MAKER_COMMENTS")
    public String getCustPicMakerComments() {return custPicMakerComments;}

    public void setCustPicMakerComments(String custPicMakerComments) {this.custPicMakerComments = custPicMakerComments;}

    @Column(name = "CUST_PIC_CHECKER_COMMENTS")
    public String getCustPicCheckerComments() {return custPicCheckerComments;}

    public void setCustPicCheckerComments(String custPicCheckerComments) {this.custPicCheckerComments = custPicCheckerComments;}

    @Column(name = "P_NIC_PIC_MAKER_COMMENTS")
    public String getpNicPicMakerComments() {return pNicPicMakerComments;}

    public void setpNicPicMakerComments(String pNicPicMakerComments) {this.pNicPicMakerComments = pNicPicMakerComments;}

    @Column(name = "P_NIC_PIC_CHECKER_COMMENTS")
    public String getpNicPicCheckerComments() {return pNicPicCheckerComments;}

    public void setpNicPicCheckerComments(String pNicPicCheckerComments) {this.pNicPicCheckerComments = pNicPicCheckerComments;}

    @Column(name = "B_FORM_PIC_MAKER_COMMENTS")
    public String getbFormPicMakerComments() {return bFormPicMakerComments;}

    public void setbFormPicMakerComments(String bFormPicMakerComments) {this.bFormPicMakerComments = bFormPicMakerComments;}

    @Column(name = "B_FORM_PIC_CHECKER_COMMENTS")
    public String getbFormPicCheckerComments() {return bFormPicCheckerComments;}

    public void setbFormPicCheckerComments(String bFormPicCheckerComments) {this.bFormPicCheckerComments = bFormPicCheckerComments;}

    @Column(name = "NIC_FRONT_PIC_MAKER_COMMENTS")
    public String getNicFrontPicMakerComments() {return nicFrontPicMakerComments;}

    public void setNicFrontPicMakerComments(String nicFrontPicMakerComments) {this.nicFrontPicMakerComments = nicFrontPicMakerComments;}

    @Column(name = "NIC_FRONT_PIC_CHECKER_COMMENTS")
    public String getNicFrontPicCheckerComments() {return nicFrontPicCheckerComments;}

    public void setNicFrontPicCheckerComments(String nicFrontPicCheckerComments) {this.nicFrontPicCheckerComments = nicFrontPicCheckerComments;}

    @Column(name = "NIC_BACK_PIC_MAKER_COMMENTS")
    public String getNicBackPicMakerComments() {return nicBackPicMakerComments;}

    public void setNicBackPicMakerComments(String nicBackPicMakerComments) {this.nicBackPicMakerComments = nicBackPicMakerComments;}

    @Column(name = "NIC_BACK_PIC_CHECKER_COMMENTS")
    public String getNicBackPicCheckerComments() {return nicBackPicCheckerComments;}

    public void setNicBackPicCheckerComments(String nicBackPicCheckerComments) {this.nicBackPicCheckerComments = nicBackPicCheckerComments;}

    @Column(name = "P_NIC_BACK_PIC_MAKER_COMMENTS")
    public String getpNicBackPicMakerComments() {return pNicBackPicMakerComments;}

    public void setpNicBackPicMakerComments(String pNicBackPicMakerComments) {this.pNicBackPicMakerComments = pNicBackPicMakerComments;}

    @Column(name = "P_NIC_BACK_CHECKER_COMMENTS")
    public String getpNicBackPicCheckerComments() {return pNicBackPicCheckerComments;}

    public void setpNicBackPicCheckerComments(String pNicBackPicCheckerComments) {this.pNicBackPicCheckerComments = pNicBackPicCheckerComments;}

    @Column(name = "BVS")
    public Boolean getBvs() {
        return Bvs;
    }

    public void setBvs(Boolean bvs) {
        Bvs = bvs;
    }

    @Column(name = "RISK_LEVEL")
    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    @Column(name = "IS_PEP")
    public Boolean getPep() {
        return isPep;
    }

    public void setPep(Boolean pep) {
        isPep = pep;
    }



    @Column(name = "PEP_MARK_DATE")
    public Date getPepMarkDate() {
        return pepMarkDate;
    }

    public void setPepMarkDate(Date pepMarkDate) {
        this.pepMarkDate = pepMarkDate;
    }
}
