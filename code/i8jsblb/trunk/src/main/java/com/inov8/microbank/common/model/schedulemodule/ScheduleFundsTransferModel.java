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
 * Created by Muhammad Sajid on 3/16/2017.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SCHEDULE_FUNDS_TRANSFER_SEQ",sequenceName = "SCHEDULE_FUNDS_TRANSFER_SEQ",allocationSize=1)
@Table(name = "SCHEDULE_FUNDS_TRANSFER")
public class ScheduleFundsTransferModel extends BasePersistableModel implements Serializable {

    private Long scheduleFundsTransferId;
    //private ProductModel productIdProductModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    //private BeneficiaryModel beneficiaryModel;
    private CustomerModel customerIdCustomerModel;

    private String transactionType;
    private String senderAccountNo;
    //private String senderAccountTitle;
    //private String senderAccountTypeCode;
    //private String senderBankImd;
    //private String senderCardNo;
    //private String senderCardExpiry;
//    private String transactionAmount;
    private String recurPatern;
    private String recuringEndType;
    private String fromDate;
    private String toDate;
    private String noOfOccurence;
    private Date createdOn;
    private Date updatedOn;
    //private String beneficiaryEmail;
    //private String beneficiaryNick;
    private Boolean isExpired;
    private Boolean isDeleted;
    private Long occurenceConsumed;
    private String lastTxDate;
    private String comments;

    private Collection<ScheduleFundsTransferDetailModel> scheduleFundsTransferDetailModelList = new ArrayList<ScheduleFundsTransferDetailModel>();




    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setScheduleFundsTransferId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getScheduleFundsTransferId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&scheduleFundsTransferId=" + getScheduleFundsTransferId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "scheduleFundsTransferId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "SCHEDULE_FUNDS_TRANSFER_ID" , nullable = false )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SCHEDULE_FUNDS_TRANSFER_SEQ")
    public Long getScheduleFundsTransferId() {
        return scheduleFundsTransferId;
    }

    public void setScheduleFundsTransferId(Long scheduleFundsTransferId) {
        this.scheduleFundsTransferId = scheduleFundsTransferId;
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
    @Transient
    public CustomerModel getCustomerIdCustomerModel() {
        return getRelationCustomerIdCustomerModel();
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @Transient
    public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
        this.customerIdCustomerModel = customerModel;
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @Transient
    public void setCustomerIdCustomerModel(CustomerModel customerModel) {
        if (null != customerModel) {
            setRelationCustomerIdCustomerModel((CustomerModel) customerModel.clone());
        }
    }

    /**
     * Returns the value of the <code>customerId</code> property.
     */
    @Transient
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

    @Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            customerIdCustomerModel = null;
        } else {
            customerIdCustomerModel = new CustomerModel();
            customerIdCustomerModel.setCustomerId(customerId);
        }
    }

    @Column(name = "TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

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

    @Column(name = "NO_OF_OCCURENCE")
    public String getNoOfOccurence() {
        return noOfOccurence;
    }

    public void setNoOfOccurence(String noOfOccurence) {
        this.noOfOccurence = noOfOccurence;
    }







    public void removeScheduleFundsTransferDetailModelList(ScheduleFundsTransferDetailModel scheduleFundsTransferDetailModel) {
       // scheduleFundsTransferDetailModel.setRelationScheduleFunds(null);
        scheduleFundsTransferDetailModelList.remove(scheduleFundsTransferDetailModel);
    }

    public void addCustomerIdSmartMoneyAccountModel(ScheduleFundsTransferDetailModel scheduleFundsTransferDetailModel) {
       // scheduleFundsTransferDetailModel.setRelationScheduleFunds(this);
        scheduleFundsTransferDetailModelList.add(scheduleFundsTransferDetailModel);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SCHEDULE_FUNDS_TRANSFER_ID")
    public Collection<ScheduleFundsTransferDetailModel> getScheduleFundsTransferDetailModelList() throws Exception {
        return scheduleFundsTransferDetailModelList;
    }

    public void setScheduleFundsTransferDetailModelList(Collection<ScheduleFundsTransferDetailModel> scheduleFundsTransferDetailModelList) throws Exception {
        this.scheduleFundsTransferDetailModelList = scheduleFundsTransferDetailModelList;
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

    @Column(name = "RECUR_END_TYPE")
    public String getRecuringEndType() {
        return recuringEndType;
    }

    public void setRecuringEndType(String recuringEndType) {
        this.recuringEndType = recuringEndType;
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

    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
       this.isDeleted = isDeleted;
    }


    @Column(name = "IS_EXPIRED")
    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean expired) {
        isExpired = expired;
    }


    @Column(name = "LAST_TX_DATE")
    public String getLastTxDate() {
        return lastTxDate;
    }

    public void setLastTxDate(String lastTxDate) {
        this.lastTxDate = lastTxDate;
    }


    @Column(name = "OCCURENCE_CONSUMED")
    public Long getOccurenceConsumed() {
        return occurenceConsumed;
    }

    public void setOccurenceConsumed(Long occurenceConsumed) {
        this.occurenceConsumed = occurenceConsumed;
    }



    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        //associationModel = new AssociationModel();
        //associationModel.setClassName("ProductModel");
        //associationModel.setPropertyName("relationProductIdProductModel");
   //     associationModel.setValue(getRelationProductIdProductModel());
        //associationModelList.add(associationModel);


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

        //associationModel = new AssociationModel();
        //associationModel.setClassName("BeneficiaryModel");
        //associationModel.setPropertyName("beneficiaryModel");
        //associationModel.setValue(getBeneficiaryModel());
        //associationModelList.add(associationModel);


        return associationModelList;
    }
}
