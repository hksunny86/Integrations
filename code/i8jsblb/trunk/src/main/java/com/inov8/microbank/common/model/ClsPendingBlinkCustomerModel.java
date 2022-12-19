package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "CLS_PENDING_BLINK_CUSTOMER_SEQ", sequenceName = "CLS_PENDING_BLINK_CUSTOMER_SEQ", allocationSize = 2)
@Table(name = "CLS_PENDING_BLINK_CUSTOMER")
public class ClsPendingBlinkCustomerModel extends BasePersistableModel implements
        Serializable,Cloneable, RowMapper {

    private static final long serialVersionUID = -6499330678059839685L;
    private Long clsBlinkCustomerId;
    private String mobileNo;
    private String cnic;
    private String consumerName;
    private String fatherHusbandName;
    private String gender;
    private Date cnicIssuanceDate;
    private Date dob;
    private String birthPlace;
    private String motherMaidenName;
    private String emailAddress;
    private String mailingAddress;
    private String permanentAddress;
    private String purposeOfAccount;
    private String sourceOfIncome;
    private String expectedMonthlyTurnOver;
    private String nextOfKin;
    private String longitude;
    private String latitude;
    private String dualNationality;
    private String usCitizen;
    private CustomerModel customerModel;
    private AppUserModel appUserModel;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Long customerAccountTypeId;
    private Long accUpdate;
    private String caseStatus;
    private String caseID;
    private Integer clsBotStatus;
    private String clsStatus;
    private String clsComments;
    private byte[] picture;
    private Date createdOnEndDate;
    private Date createdOnStartDate;
    private String riskLevelStatus;
    private String isCompleted;

    public ClsPendingBlinkCustomerModel() {
    }
@Column(name = "RISK_LEVEL_STATUS")
    public String getRiskLevelStatus() {
        return riskLevelStatus;
    }

    public void setRiskLevelStatus(String riskLevelStatus) {
        this.riskLevelStatus = riskLevelStatus;
    }
@Column(name = "IS_COMPLETED")
    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getClsBlinkCustomerId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "clsBlinkCustomerId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&clsBlinkCustomerId=" + getClsBlinkCustomerId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setClsBlinkCustomerId(primaryKey);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLS_PENDING_BLINK_CUSTOMER_SEQ")
    @Column(name = "CLS_BLINK_CUSTOMER_ID")
    public Long getClsBlinkCustomerId() {
        return clsBlinkCustomerId;
    }

    public void setClsBlinkCustomerId(Long clsBlinkCustomerId) {
        this.clsBlinkCustomerId = clsBlinkCustomerId;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "CONSUMER_NAME")
    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    @Column(name = "FATHER_HUSBAND_NAME")
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    @Column(name = "GENDER")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "CNIC_ISSUANCE_DATE")
    public Date getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(Date cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    @Column(name = "DOB")
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Column(name = "BIRTH_PLACE")
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Column(name = "MOTHER_MAIDEN_NAME")
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    @Column(name = "EMAIL_ADDRESS")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "MAILING_ADDRESS")
    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @Column(name = "PERMANENT_ADDRESS")
    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    @Column(name = "PURPOSE_OF_ACCOUNT")
    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    @Column(name = "SOURCE_OF_INCOME")
    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    @Column(name = "EXPECTED_MONTHLY_TURNOVER")
    public String getExpectedMonthlyTurnOver() {
        return expectedMonthlyTurnOver;
    }

    public void setExpectedMonthlyTurnOver(String expectedMonthlyTurnOver) {
        this.expectedMonthlyTurnOver = expectedMonthlyTurnOver;
    }

    @Column(name = "NEXT_OF_KIN")
    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    @Column(name = "LONGITUDE")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    @Column(name = "LATITUDE")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerModel() {
        return customerModel;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    public AppUserModel getRelationAppUserModel() {
        return appUserModel;
    }

    @Column(name = "CLS_BOT_STATUS")
    public Integer getClsBotStatus() {
        return clsBotStatus;
    }

    @Column(name = "CLS_STATUS")
    public String getClsStatus() {
        return clsStatus;
    }

    public void setClsStatus(String clsStatus) {
        this.clsStatus = clsStatus;
    }

    @Column(name = "CLS_COMMENTS")
    public String getClsComments() {
        return clsComments;
    }

    public void setClsComments(String clsComments) {
        this.clsComments = clsComments;
    }

    public void setClsBotStatus(Integer clsBotStatus) {
        this.clsBotStatus = clsBotStatus;
    }

    @Transient
    public void setRelationCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Transient
    public void setRelationAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    @Transient
    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    @Transient
    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Transient
    public AppUserModel getAppUserModel() {
        return appUserModel;
    }

    @Transient
    public void setAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }


    @Transient
    public Long getCustomerId() {
        if (customerModel != null) {
            return customerModel.getCustomerId();
        } else {
            return null;
        }
    }

    @Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            this.customerModel = null;
        } else {
            customerModel = new CustomerModel();
            customerModel.setCustomerId(customerId);
        }
    }

    @Transient
    public Long getAppUserId() {
        if (appUserModel != null) {
            return appUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @Transient
    public void setAppUserId(Long appUserId) {
        if (appUserId == null) {
            this.appUserModel = null;
        } else {
            appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);
        }
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name = "CASE_STATUS")
    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    @Column(name = "CASE_ID")
    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    @Column(name = "ACC_UPDATE")
    public Long getAccUpdate() {
        return accUpdate;
    }

    public void setAccUpdate(Long accUpdate) {
        this.accUpdate = accUpdate;
    }

    @Version
    @Column(name = "VERSION_NO", nullable = false)
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
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

    @Column(name = "PICTURE")
    public byte[] getPicture() {
        return this.picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @javax.persistence.Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }
    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }

    @javax.persistence.Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
    }


    @Override
    @Transient
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerModel");
        associationModel.setValue(getRelationCustomerModel());

        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationAppUserModel");
        associationModel.setValue(getRelationAppUserModel());

        return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClsPendingBlinkCustomerModel model = new ClsPendingBlinkCustomerModel();
        model.setClsBlinkCustomerId(resultSet.getLong("CLS_BLINK_CUSTOMER_ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setConsumerName(resultSet.getString("CONSUMER_NAME"));
        model.setFatherHusbandName(resultSet.getString("FATHER_HUSBAND_NAME"));
        model.setGender(resultSet.getString("GENDER"));
        model.setCnicIssuanceDate(resultSet.getTimestamp("CNIC_ISSUANCE_DATE"));
        model.setDob(resultSet.getTimestamp("DOB"));
        model.setBirthPlace(resultSet.getString("BIRTH_PLACE"));
        model.setMotherMaidenName(resultSet.getString("MOTHER_MAIDEN_NAME"));
        model.setEmailAddress(resultSet.getString("EMAIL_ADDRESS"));
        model.setMailingAddress(resultSet.getString("MAILING_ADDRESS"));
        model.setPermanentAddress(resultSet.getString("PERMANENT_ADDRESS"));
        model.setSourceOfIncome(resultSet.getString("SOURCE_OF_INCOME"));
        model.setExpectedMonthlyTurnOver(resultSet.getString("EXPECTED_MONTHLY_TURNOVER"));
        model.setNextOfKin(resultSet.getString("NEXT_OF_KIN"));
        model.setLongitude(resultSet.getString("LONGITUDE"));
        model.setLatitude(resultSet.getString("LATITUDE"));
        model.setCustomerId(resultSet.getLong("CUSTOMER_ID"));
        model.setAppUserId(resultSet.getLong("APP_USER_ID"));
        model.setClsBotStatus(resultSet.getInt("CLS_BOT_STATUS"));
        model.setClsStatus(resultSet.getString("CLS_STATUS"));
        model.setClsComments(resultSet.getString("CLS_COMMENTS"));
        model.setCaseID(resultSet.getString("CASE_ID"));
        model.setCaseStatus(resultSet.getString("CASE_STATUS"));
        model.setDualNationality(resultSet.getString("DUAL_NATIONALITY"));
        model.setUsCitizen(resultSet.getString("US_CITIZEN"));
        model.setAccUpdate(resultSet.getLong("ACC_UPDATE"));
        model.setCustomerAccountTypeId(resultSet.getLong("CUSTOMER_ACCOUNT_TYPE_ID"));
        model.setPurposeOfAccount(resultSet.getString("PURPOSE_OF_ACCOUNT"));
        model.setVersionNo(resultSet.getInt("VERSION_NO"));
        model.setPicture(resultSet.getBytes("PICTURE"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        model.setIsCompleted(resultSet.getString("IS_COMPLETED"));
        model.setRiskLevelStatus(resultSet.getString("RISK_LEVEL_STATUS"));

        return model;
    }
}
