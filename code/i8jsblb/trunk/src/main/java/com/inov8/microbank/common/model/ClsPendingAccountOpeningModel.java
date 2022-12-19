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
@SequenceGenerator(name = "CLS_PENDING_ACC_OPENING_SEQ", sequenceName = "CLS_PENDING_ACC_OPENING_SEQ", allocationSize = 2)
@Table(name = "CLS_PENDING_ACCOUNT_OPENING")
public class ClsPendingAccountOpeningModel extends BasePersistableModel implements
        Serializable, Cloneable, RowMapper {

    private Long clsPendingAccountOpeningId;
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
    private CustomerModel customerModel;
    private AppUserModel appUserModel;
    private Long registrationStateId;
    private Long accountStateId;
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
    private Date createdOnEndDate;
    private Date createdOnStartDate;
    private String isCompleted;
    private Date start;
    private Date end;
    private AccountStateModel accountStateModel;
    private RegistrationStateModel registrationStateModel;
    private String name;
    private Boolean isSmsRequired;
    private Boolean accountOpenedByAma;
    private Boolean updateAMA;


    @Transient
    public String getName() {
        return name;
    }

    @Transient
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getClsPendingAccountOpeningId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setClsPendingAccountOpeningId(primaryKey);
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "clsPendingAccountOpeningId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&clsPendingAccountOpeningId=" + getClsPendingAccountOpeningId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLS_PENDING_ACC_OPENING_SEQ")
    @Column(name = "CLS_PENDING_ACCOUNT_OPENING_ID")
    public Long getClsPendingAccountOpeningId() {
        return clsPendingAccountOpeningId;
    }

    public void setClsPendingAccountOpeningId(Long clsPendingAccountOpeningId) {
        this.clsPendingAccountOpeningId = clsPendingAccountOpeningId;
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerModel() {
        return customerModel;
    }

    @Transient
    public void setRelationCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Transient
    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
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


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    public AppUserModel getRelationAppUserModel() {
        return appUserModel;
    }

    @Transient
    public void setRelationAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    @Transient
    public AppUserModel getAppUserModel() {
        return appUserModel;
    }

    public void setAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }

    @Transient
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Transient
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_STATE_ID")
    public AccountStateModel getRelationAccountStateModel() {
        return accountStateModel;
    }

    @Transient
    public void setRelationAccountStateModel(AccountStateModel accountStateModel) {
        this.accountStateModel = accountStateModel;
    }

    @Transient
    public AccountStateModel getAccountStateModel() {
        return getRelationAccountStateModel();
    }

    @Transient
    public void setAccountStateModel(
            AccountStateModel accountStateModel) {
        this.accountStateModel = accountStateModel;
    }

    @Transient
    public Long getAccountStateId() {
        if (accountStateModel != null) {
            return accountStateModel.getAccountStateId();
        } else {
            return null;
        }
    }

    @Transient
    public void setAccountStateId(Long accountStateId) {
        if (accountStateId == null) {
            this.accountStateId = null;
        } else {
            accountStateModel = new AccountStateModel();
            accountStateModel.setAccountStateId(accountStateId);
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

    @Version
    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
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

    @Column(name = "CLS_BOT_STATUS")
    public Integer getClsBotStatus() {
        return clsBotStatus;
    }

    public void setClsBotStatus(Integer clsBotStatus) {
        this.clsBotStatus = clsBotStatus;
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

    @Column(name = "IS_COMPLETED")
    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Column(name = "ACC_OPENED_BY_AMA")
    public Boolean getAccountOpenedByAma() {
        return accountOpenedByAma;
    }

    public void setAccountOpenedByAma(Boolean accountOpenedByAma) {
        this.accountOpenedByAma = accountOpenedByAma;
    }

    @Column(name = "UPDATE_AMA")
    public Boolean getUpdateAMA() {
        return updateAMA;
    }

    public void setUpdateAMA(Boolean updateAMA) {
        this.updateAMA = updateAMA;
    }

    @Column(name = "IS_SMS_REQUIRED")
    public Boolean getIsSmsRequired() {
        return isSmsRequired;
    }

    public void setIsSmsRequired(Boolean isSmsRequired) {
        this.isSmsRequired = isSmsRequired;
    }
    //    @Column(name = "REGISTRATION_STATE_ID")
//    public Long getRegistrationStateId() {
//        return registrationStateId;
//    }
//
//    public void setRegistrationStateId(Long registrationStateId) {
//        this.registrationStateId = registrationStateId;
//    }

//    @Column(name = "ACCOUNT_STATE_ID")
//    public Long getAccountStateId() {
//        return accountStateId;
//    }
//
//    public void setAccountStateId(Long accountStateId) {
//        this.accountStateId = accountStateId;
//    }

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

        associationModel = new AssociationModel();

        associationModel.setClassName("RegistrationStateModel");
        associationModel.setPropertyName("relationRegistrationStateModel");
        associationModel.setValue(getRelationRegistrationStateModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AccountStateModel");
        associationModel.setPropertyName("relationAccountStateModel");
        associationModel.setValue(getRelationAccountStateModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClsPendingAccountOpeningModel model = new ClsPendingAccountOpeningModel();
        model.setClsPendingAccountOpeningId(resultSet.getLong("CLS_PENDING_ACCOUNT_OPENING_ID"));
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
        model.setCustomerId(resultSet.getLong("CUSTOMER_ID"));
        model.setAppUserId(resultSet.getLong("APP_USER_ID"));
        model.setClsBotStatus(resultSet.getInt("CLS_BOT_STATUS"));
        model.setClsStatus(resultSet.getString("CLS_STATUS"));
        model.setClsComments(resultSet.getString("CLS_COMMENTS"));
        model.setCaseID(resultSet.getString("CASE_ID"));
        model.setCaseStatus(resultSet.getString("CASE_STATUS"));
        model.setCustomerAccountTypeId(resultSet.getLong("CUSTOMER_ACCOUNT_TYPE_ID"));
        model.setVersionNo(resultSet.getInt("VERSION_NO"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        model.setRegistrationStateId(resultSet.getLong("REGISTRATION_STATE_ID"));
        model.setAccountStateId(resultSet.getLong("ACCOUNT_STATE_ID"));
        model.setIsCompleted(resultSet.getString("IS_COMPLETED"));
        model.setIsSmsRequired(resultSet.getBoolean("IS_SMS_REQUIRED"));
        model.setAccountOpenedByAma(resultSet.getBoolean("ACC_OPENED_BY_AMA"));
        model.setUpdateAMA(resultSet.getBoolean("UPDATE_AMA"));
        return model;
    }
}
