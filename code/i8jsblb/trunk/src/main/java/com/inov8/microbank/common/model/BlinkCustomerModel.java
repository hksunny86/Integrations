package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.BlinkCustomerRegistrationStateConstantsInterface;
import com.inov8.ola.util.CustomerAccountTypeConstants;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BLINK_CUSTOMER_seq", sequenceName = "BLINK_CUSTOMER_seq", allocationSize = 2)
@Table(name = "BLINK_CUSTOMER")
public class BlinkCustomerModel extends BasePersistableModel implements
        Serializable {

    private static final long serialVersionUID = -6499330678059839685L;

    private Long blinkCustomerId;
    private Long actionAuthorizationId;
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
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Date start;
    private Date end;
    private Integer versionNo;
    private Long customerAccountTypeId;
    private Long accUpdate;
    private String clsResponseCode;
    private String registrationStatus;
    private String comments;
    private String chkComments;
    private String blinkName;
    private String registrationStateName;
    private Boolean isBVS;
    private String bvsVerification;
    @XmlElement
    private ActionStatusModel actionStatusIdActionStatusModel;
  /*  @XmlElement
    private  BlinkCustomerRegistrationStateModel registrationStateIdBlinkCustomerRegistrationStateModel;*/

    public BlinkCustomerModel() {
    }










    /*@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "BLINK_REGISTRATION_STATE_ID")
    public BlinkCustomerRegistrationStateModel getRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel() {
        return registrationStateIdBlinkCustomerRegistrationStateModel;
    }

    @javax.persistence.Transient
    public BlinkCustomerRegistrationStateModel getRegistrationStateIdBlinkCustomerRegistrationStateModel() {
        return getRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel();
    }

    @javax.persistence.Transient
    public void setRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel(BlinkCustomerRegistrationStateModel blinkCustomerRegistrationStateModel) {
        this.registrationStateIdBlinkCustomerRegistrationStateModel = blinkCustomerRegistrationStateModel;
    }

    @javax.persistence.Transient
    public void setRegistrationStateIdBlinkCustomerRegistrationStateModel(BlinkCustomerRegistrationStateModel blinkCustomerRegistrationStateModel) {
        if (null != blinkCustomerRegistrationStateModel) {
            setRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel((BlinkCustomerRegistrationStateModel) blinkCustomerRegistrationStateModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getRegistrationStateId() {
        if (registrationStateIdBlinkCustomerRegistrationStateModel != null) {
            return registrationStateIdBlinkCustomerRegistrationStateModel.getRegistrationStateId();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setRegistrationStateId(Long registrationStateId) {
        if (registrationStateId == null) {
            registrationStateIdBlinkCustomerRegistrationStateModel = null;
        } else {
            registrationStateIdBlinkCustomerRegistrationStateModel = new BlinkCustomerRegistrationStateModel();
            registrationStateIdBlinkCustomerRegistrationStateModel.setRegistrationStateId(registrationStateId);
        }
    }

*/


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTION_STATUS_ID")
    public ActionStatusModel getRelationActionStatusIdActionStatusModel() {
        return actionStatusIdActionStatusModel;
    }

    @javax.persistence.Transient
    public ActionStatusModel getActionStatusIdActionStatusModel() {
        return getRelationActionStatusIdActionStatusModel();
    }

    @javax.persistence.Transient
    public void setRelationActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        this.actionStatusIdActionStatusModel = actionStatusModel;
    }

    @javax.persistence.Transient
    public void setActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        if (null != actionStatusModel) {
            setRelationActionStatusIdActionStatusModel((ActionStatusModel) actionStatusModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getActionStatusId() {
        if (actionStatusIdActionStatusModel != null) {
            return actionStatusIdActionStatusModel.getActionStatusId();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setActionStatusId(Long actionStatusId) {
        if (actionStatusId == null) {
            actionStatusIdActionStatusModel = null;
        } else {
            actionStatusIdActionStatusModel = new ActionStatusModel();
            actionStatusIdActionStatusModel.setActionStatusId(actionStatusId);
        }
    }

    @Column(name = "BLINK_REGISTRATION_STATE_ID")
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    @Transient
    public String getRegistrationStateName() {
        if (registrationStatus.equals(BlinkCustomerRegistrationStateConstantsInterface.RQST_RCVD.toString())) {
            return "Request Received";
        }
        if (registrationStatus.equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED.toString())) {
            return "Approved";
        }
        if (registrationStatus.equals(BlinkCustomerRegistrationStateConstantsInterface.REJECTED.toString())) {
            return "Rejected";
        }
        if (registrationStatus.equals(BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT.toString())) {
            return "Discrepant";
        } else {
            return registrationStateName;
        }
    }

    public void setRegistrationStateName(String registrationStateName) {
        this.registrationStateName = registrationStateName;
    }

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getBlinkCustomerId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "blinkCustomerId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&blinkCustomerId=" + getBlinkCustomerId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setBlinkCustomerId(primaryKey);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLINK_CUSTOMER_seq")
    @Column(name = "BLINK_CUSTOMER_ID")
    public Long getBlinkCustomerId() {
        return blinkCustomerId;
    }

    public void setBlinkCustomerId(Long blinkCustomerId) {
        this.blinkCustomerId = blinkCustomerId;
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

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    @Column(name = "CHK_COMMENTS")
    public String getChkComments() {
        return chkComments;
    }

    @Column(name = "BVS_FLAG")
    public Boolean getBVS() {
        return isBVS;
    }

    public void setBVS(Boolean BVS) {
        isBVS = BVS;
    }

    @Transient
    public String getBvsVerification() {
        if (getBVS() == true) {
            return "Yes";
        }
        if (getBVS() == false) {
            return "No";
        } else {
            return bvsVerification;
        }
    }

    public void setBvsVerification(String bvsVerification) {
        this.bvsVerification = bvsVerification;
    }

    public void setChkComments(String chkComments) {
        this.chkComments = chkComments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    @Transient
    public String getBlinkName() {
        return blinkName;
    }

    public void setBlinkName(String blinkName) {
        this.blinkName = blinkName;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerModel() {
        return customerModel;
    }

    @javax.persistence.Transient
    public void setRelationCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @javax.persistence.Transient
    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    @javax.persistence.Transient
    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @javax.persistence.Transient
    public Long getCustomerId() {
        if (customerModel != null) {
            return customerModel.getCustomerId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            this.customerModel = null;
        } else {
            customerModel = new CustomerModel();
            customerModel.setCustomerId(customerId);
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
        if (this.customerAccountTypeId != null) {
            if (this.customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_1)) {
                setBlinkName("Level 1");
                return customerAccountTypeId;
            }
            if (this.customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
                setBlinkName("Level 0");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_2)) {
                setBlinkName("Level 2");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_3)) {
                setBlinkName("Level 3");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.HRA)) {
                setBlinkName("HRA");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.WALK_IN_CUSTOMER)) {
                setBlinkName("Walk-In Customer");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                setBlinkName("Blink");
                return customerAccountTypeId;
            }
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.RETAILER)) {
                setBlinkName("Retailer");
                return customerAccountTypeId;
            } else {
                return customerAccountTypeId;
            }
        } else {
            return customerAccountTypeId;
        }
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name = "CLS_RESPONSE_CODE")
    public String getClsResponseCode() {
        return clsResponseCode;
    }

    public void setClsResponseCode(String clsResponseCode) {
        this.clsResponseCode = clsResponseCode;
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

        associationModel.setClassName("ActionStatusModel");
        associationModel.setPropertyName("relationActionStatusIdActionStatusModel");
        associationModel.setValue(getRelationActionStatusIdActionStatusModel());

       /* associationModel = new AssociationModel();

        associationModel.setClassName("BlinkCustomerRegistrationStateModel");
        associationModel.setPropertyName("relationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel");
        associationModel.setValue(getRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel());
*/
        associationModelList.add(associationModel);
        return associationModelList;
    }

    @Column(name = "ACTION_AUTHORIZATION_ID")
    public Long getActionAuthorizationId() {
        return actionAuthorizationId;
    }

    public void setActionAuthorizationId(Long actionAuthorizationId) {
        this.actionAuthorizationId = actionAuthorizationId;
    }
}
