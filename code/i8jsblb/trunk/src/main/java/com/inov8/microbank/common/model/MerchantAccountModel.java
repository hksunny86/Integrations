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
@SequenceGenerator(name = "MERCHAN_ACCOUNT_SEQ", sequenceName = "MERCHAN_ACCOUNT_SEQ", allocationSize = 2)
@Table(name = "merchan_account")
public class MerchantAccountModel extends BasePersistableModel implements
        Serializable {

    private static final long serialVersionUID = -6499330678059839685L;

    private Long merchanAccountId;
    private Long actionAuthorizationId;
    private String mobileNo;
    private String cnic;
    private String consumerName;
    private String city;
    private String businessName;
    private String businessAddress;
    private String typeOfBusiness;
    private String expectedMonthlySalary;
    private String latitude;
    private String longitude;
    private CustomerModel customerModel;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Date start;
    private Date end;
    private Long accUpdate;
    private String registrationStatus;
    private String comments;
    private String chkComments;
    private String registrationStateName;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String reserved1;
    private String reserved2;
    private String reserved6;
    private String reserved7;
    private String reserved8;
    private String reserved9;
    private String reserved10;

    private Long tillNumber;
    private Long idType;
    private Long idName;
    @XmlElement
    private ActionStatusModel actionStatusIdActionStatusModel;

    public MerchantAccountModel() {
    }





    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTION_STATUS_ID")
    public ActionStatusModel getRelationActionStatusIdActionStatusModel() {
        return actionStatusIdActionStatusModel;
    }

    @Transient
    public ActionStatusModel getActionStatusIdActionStatusModel() {
        return getRelationActionStatusIdActionStatusModel();
    }

    @Transient
    public void setRelationActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        this.actionStatusIdActionStatusModel = actionStatusModel;
    }

    @Transient
    public void setActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        if (null != actionStatusModel) {
            setRelationActionStatusIdActionStatusModel((ActionStatusModel) actionStatusModel.clone());
        }
    }

    @Transient
    public Long getActionStatusId() {
        if (actionStatusIdActionStatusModel != null) {
            return actionStatusIdActionStatusModel.getActionStatusId();
        } else {
            return null;
        }
    }


    @Transient
    public void setActionStatusId(Long actionStatusId) {
        if (actionStatusId == null) {
            actionStatusIdActionStatusModel = null;
        } else {
            actionStatusIdActionStatusModel = new ActionStatusModel();
            actionStatusIdActionStatusModel.setActionStatusId(actionStatusId);
        }
    }

    @Column(name = "REGISTRATION_STATE_ID")
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
        return getMerchanAccountId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "merchanAccountId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&merchanAccountId=" + getMerchanAccountId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setMerchanAccountId(primaryKey);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MERCHAN_ACCOUNT_SEQ")
    @Column(name = "MERCHAN_ACCOUNT_ID")
    public Long getMerchanAccountId() {
        return merchanAccountId;
    }

    public void setMerchanAccountId(Long merchanAccountId) {
        this.merchanAccountId = merchanAccountId;
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

    @Transient
    public void setRelationCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
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


    @Column(name = "ACC_UPDATE")
    public Long getAccUpdate() {
        return accUpdate;
    }

    public void setAccUpdate(Long accUpdate) {
        this.accUpdate = accUpdate;
    }

    @Column(name = "CITY")

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "BUSINESS_NAME")

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    @Column(name = "BUSINESS_ADDRESS")

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }


    @Column(name = "TYPE_OF_BUSINESS")

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }
    @Column(name = "EXPECTED_MONTHLY_SALES")
    public String getExpectedMonthlySalary() {
        return expectedMonthlySalary;
    }

    public void setExpectedMonthlySalary(String expectedMonthlySalary) {
        this.expectedMonthlySalary = expectedMonthlySalary;
    }

    @Column(name = "RESERVED3")
    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    @Column(name = "RESERVED4")
    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }
    @Column(name = "RESERVED5")
    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }



    @Column(name = "RESERVED1")
    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    @Column(name = "RESERVED2")
    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    @Column(name = "RESERVED6")
    public String getReserved6() {
        return reserved6;
    }

    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    @Column(name = "RESERVED7")
    public String getReserved7() {
        return reserved7;
    }

    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
    }

    @Column(name = "RESERVED8")
    public String getReserved8() {
        return reserved8;
    }

    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    @Column(name = "RESERVED9")
    public String getReserved9() {
        return reserved9;
    }

    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    @Column(name = "RESERVED10")
    public String getReserved10() {
        return reserved10;
    }

    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }

    @Column(name = "TILL_ID")

    public Long getTillNumber() {
        return tillNumber;
    }

    public void setTillNumber(Long tillNumber) {
        this.tillNumber = tillNumber;
    }

    @Column(name = "ID_TYPE")

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    @Column(name = "ID_N")

    public Long getIdName() {
        return idName;
    }

    public void setIdName(Long idName) {
        this.idName = idName;
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
