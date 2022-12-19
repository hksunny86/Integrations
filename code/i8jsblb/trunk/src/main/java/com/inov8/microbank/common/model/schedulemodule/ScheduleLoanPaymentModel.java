package com.inov8.microbank.common.model.schedulemodule;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Muhammad Sajid on 3/20/2017.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SCHEDULE_LOAN_PAYMENT_SEQ",sequenceName = "SCHEDULE_LOAN_PAYMENT_SEQ",allocationSize=1)
@Table(name = "SCHEDULE_LOAN_PAYMENT")
public class ScheduleLoanPaymentModel extends BasePersistableModel implements Serializable {


    //private ProductModel productIdProductModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    //private RegisteredConsumerModel registeredConsumerModel;
    private CustomerModel customerIdCustomerModel;

    private Long scheduleLOANPaymentId;
    private String transactionType;
    //private String senderAccountNo;
    //private String senderAccountTitle;
    //private String senderAccountTypeCode;
    //private String senderBankImd;
    //private String senderCardNo;
    //private String senderCardExpiry;
    private String transactionAmount;
    private String recurPatern;
    private String recuringEndType;
    private String fromDate;
    private String toDate;
    private String noOfOccurence;
    private Date createdOn;
    private Date updatedOn;
    private Boolean isExpired;
    private Boolean isDeleted;
    private Long occurenceConsumed;
    private String lastTxDate;
    private String nickName;
    private String comments;
    private String thirdPartyVerificationRRN;

    private Collection<ScheduleLoanPaymentDetailModel> scheduleLoanPaymentDetailModelList = new ArrayList<ScheduleLoanPaymentDetailModel>();

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setScheduleLOANPaymentId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getScheduleLOANPaymentId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&scheduleLOANPaymentId=" + getScheduleLOANPaymentId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "scheduleLOANPaymentId";
        return primaryKeyFieldName;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SCHEDULE_LOAN_PAYMENT_SEQ")
    @Column(name = "SCHEDULE_LOAN_PAYMENT_ID" , nullable = false )
    public Long getScheduleLOANPaymentId() {
        return scheduleLOANPaymentId;
    }

    public void setScheduleLOANPaymentId(Long scheduleLOANPaymentId) {
        this.scheduleLOANPaymentId = scheduleLOANPaymentId;
    }


   /* @Column(name = "TRANSACTION_AMOUNT")
    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
*/
    @Column(name = "RECUR_PATERN")
    public String getRecurPatern() {
        return recurPatern;
    }

    public void setRecurPatern(String recurPatern) {
        this.recurPatern = recurPatern;
    }

    @Column(name = "FROM_DATE")
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name = "TO_DATE")
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Column(name = "NICK_NAME")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName= nickName;
    }

    @Column(name = "NO_OF_OCCURENCE")
    public String getNoOfOccurence() {
        return noOfOccurence;
    }

    public void setNoOfOccurence(String noOfOccurence) {
        this.noOfOccurence = noOfOccurence;
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
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerIdCustomerModel() {
        return customerIdCustomerModel;
    }

    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @javax.persistence.Transient
    public CustomerModel getCustomerIdCustomerModel() {
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
        if (null != customerModel) {
            setRelationCustomerIdCustomerModel((CustomerModel) customerModel.clone());
        }
    }

    /**
     * Returns the value of the <code>customerId</code> property.
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
        if (customerId == null) {
            customerIdCustomerModel = null;
        } else {
            customerIdCustomerModel = new CustomerModel();
            customerIdCustomerModel.setCustomerId(customerId);
        }
    }

    @Column(name = "RECUR_END_TYPE")
    public String getRecuringEndType() {
        return recuringEndType;
    }

    public void setRecuringEndType(String recuringEndType) {
        this.recuringEndType = recuringEndType;
    }

    @Column(name = "TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    @Column(name = "IS_EXPIRED")
    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean expired) {
        isExpired = expired;
    }

    @Column(name = "OCCURENCE_CONSUMED")
    public Long getOccurenceConsumed() {
        return occurenceConsumed;
    }

    public void setOccurenceConsumed(Long occurenceConsumed) {
        this.occurenceConsumed = occurenceConsumed;
    }

    @Column(name = "LAST_TX_DATE")
    public String getLastTxDate() {
        return lastTxDate;
    }

    public void setLastTxDate(String lastTxDate) {
        this.lastTxDate = lastTxDate;
    }

    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }



    public void removeScheduleBillPaymentDetailModelList(ScheduleLoanPaymentDetailModel scheduleLoanPaymentDetailModel) {
        // scheduleFundsTransferDetailModel.setRelationScheduleFunds(null);
        scheduleLoanPaymentDetailModelList.remove(scheduleLoanPaymentDetailModel);
    }

    public void addCustomerIdSmartMoneyAccountModel(ScheduleLoanPaymentDetailModel scheduleLoanPaymentDetailModel) {
        // scheduleFundsTransferDetailModel.setRelationScheduleFunds(this);
        scheduleLoanPaymentDetailModelList.add(scheduleLoanPaymentDetailModel);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SCHEDULE_LOAN_PAYMENT_ID")
    public Collection<ScheduleLoanPaymentDetailModel> getScheduleLoanPaymentDetailModelList() throws Exception {
        return scheduleLoanPaymentDetailModelList;
    }

    public void setScheduleLoanPaymentDetailModelList(Collection<ScheduleLoanPaymentDetailModel> scheduleLoanPaymentDetailModelList) throws Exception {
        this.scheduleLoanPaymentDetailModelList = scheduleLoanPaymentDetailModelList;
    }

    @Column(name = "THIRD_PARTY_VERIFICATION_RRN")
    public String getThirdPartyVerificationRRN() { return thirdPartyVerificationRRN; }

    public void setThirdPartyVerificationRRN(String thirdPartyVerificationRRN) { this.thirdPartyVerificationRRN = thirdPartyVerificationRRN; }



    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;



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

        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerIdCustomerModel");
        associationModel.setValue(getRelationCustomerIdCustomerModel());
        associationModelList.add(associationModel);

        return associationModelList;
    }

}
