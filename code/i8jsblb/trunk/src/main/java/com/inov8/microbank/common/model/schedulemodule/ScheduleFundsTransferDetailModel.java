package com.inov8.microbank.common.model.schedulemodule;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BeneficiaryModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shaista iqbal on 2/10/2020.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "SCHEDULE_FUNDS_TRANSFER_DTL_SQ",sequenceName = "SCHEDULE_FUNDS_TRANSFER_DTL_SQ",allocationSize=1)
@Table(name = "SCHEDULE_FUNDS_TRANSFER_DETAIL")
public class ScheduleFundsTransferDetailModel extends BasePersistableModel implements Serializable {

    private Long scheduleFundsTransferDetailId;
    private ProductModel productIdProductModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private BeneficiaryModel beneficiaryModel;
    private CustomerModel customerIdCustomerModel;
   // private ScheduleFundsTransferModel scheduleFundsTransferModel;
    private String transactionType;
    private String senderAccountNo;
    private String senderAccountTitle;
    private String senderAccountTypeCode;
    private String senderBankImd;
    private String senderCardNo;
    private String senderCardExpiry;
    private String transactionAmount;

    private Date createdOn;
    private Date updatedOn;
    private Long bankID;
    private String beneficiaryAccountNo;
    private String notificationMobileNo;
//    private String beneficiaryEmail;
    private String beneficiaryNick;
    private Boolean isExpired;
    private Boolean isDeleted;
   // private Long occurenceConsumed;
    private Date transactionDate;
    private String status;

    @Transient
    public void setPrimaryKey(Long aLong) {
        setScheduleFundsTransferDetailId(aLong);
    }

    @Transient
    public Long getPrimaryKey() {
        return getScheduleFundsTransferDetailId();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&scheduleFundsTransferDetailId=" + getScheduleFundsTransferDetailId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "scheduleFundsTransferDetailId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "SCHEDULE_FUNDS_TRANSFER_DTL_ID" , nullable = false )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SCHEDULE_FUNDS_TRANSFER_DTL_SQ")
    public Long getScheduleFundsTransferDetailId() {
        return scheduleFundsTransferDetailId;
    }

    public void setScheduleFundsTransferDetailId(Long scheduleFundsTransferDetailId) {
        this.scheduleFundsTransferDetailId = scheduleFundsTransferDetailId;
    }

    @Column(name = "SENDER_ACCOUNT_NO")
    public String getSenderAccountNo() {
        return senderAccountNo;
    }

    public void setSenderAccountNo(String senderAccountNo) {
        this.senderAccountNo = senderAccountNo;
    }

    @Column(name = "SENDER_ACCOUNT_TITLE")
    public String getSenderAccountTitle() {
        return senderAccountTitle;
    }

    public void setSenderAccountTitle(String senderAccountTitle) {
        this.senderAccountTitle = senderAccountTitle;
    }

    @Column(name = "SENDER_ACCOUNT_TYPE_CODE")
    public String getSenderAccountTypeCode() {
        return senderAccountTypeCode;
    }

    public void setSenderAccountTypeCode(String senderAccountTypeCode) {
        this.senderAccountTypeCode = senderAccountTypeCode;
    }



    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SCHEDULE_FUNDS_TRANSFER_ID")
    public ScheduleFundsTransferModel  scheduleFundsTransferModel;








    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }











    @Column(name = "SENDER_BANK_IMD")
    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    @Column(name = "SENDER_CARD_NO")
    public String getSenderCardNo() {
        return senderCardNo;
    }

    public void setSenderCardNo(String senderCardNo) {
        this.senderCardNo = senderCardNo;
    }

    @Column(name = "SENDER_CARD_EXPIRY")
    public String getSenderCardExpiry() {
        return senderCardExpiry;
    }

    public void setSenderCardExpiry(String senderCardExpiry) {
        this.senderCardExpiry = senderCardExpiry;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "BENEFICIARY_ID")
    public BeneficiaryModel getBeneficiaryModel() {
        return beneficiaryModel;
    }

    public void setBeneficiaryModel(BeneficiaryModel beneficiaryModel) {
        this.beneficiaryModel= beneficiaryModel;
    }

    @Transient
    public Long getBeneficiaryId() {
        if (beneficiaryModel != null) {
            return beneficiaryModel.getBeneficiaryId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>productId</code> property.
     *
     * @param beneficiaryId the value for the <code>productId</code> property
     * @spring.validator type="required"
     */

    @Transient
    public void setBeneficiaryId(Long beneficiaryId) {
        if (beneficiaryId == null) {
            beneficiaryId = null;
        } else {
            beneficiaryModel = new BeneficiaryModel();
            beneficiaryModel.setBeneficiaryId(beneficiaryId);
        }
    }


    /**
     * Returns the value of the <code>productIdProductModel</code> relation property.
     *
     * @return the value of the <code>productIdProductModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public ProductModel getRelationProductIdProductModel(){
        return productIdProductModel;
    }

    /**
     * Returns the value of the <code>productIdProductModel</code> relation property.
     *
     * @return the value of the <code>productIdProductModel</code> relation property.
     *
     */
    @Transient
    public ProductModel getProductIdProductModel(){
        return getRelationProductIdProductModel();
    }

    /**
     * Sets the value of the <code>productIdProductModel</code> relation property.
     *
     * @param productModel a value for <code>productIdProductModel</code>.
     */
    @Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
        this.productIdProductModel = productModel;
    }

    /**
     * Sets the value of the <code>productIdProductModel</code> relation property.
     *
     * @param productModel a value for <code>productIdProductModel</code>.
     */
    @Transient
    public void setProductIdProductModel(ProductModel productModel) {
        if(null != productModel)
        {
            setRelationProductIdProductModel((ProductModel)productModel.clone());
        }
    }

    @Transient
    public Long getProductId() {
        if (productIdProductModel != null) {
            return productIdProductModel.getProductId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>productId</code> property.
     *
     * @param productId the value for the <code>productId</code> property
     * @spring.validator type="required"
     */

    @Transient
    public void setProductId(Long productId) {
        if (productId == null) {
            productIdProductModel = null;
        } else {
            productIdProductModel = new ProductModel();
            productIdProductModel.setProductId(productId);
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
    @Transient
    public AppUserModel getCreatedByAppUserModel(){
        return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @Transient
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
    @Transient
    public AppUserModel getUpdatedByAppUserModel(){
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @Transient
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
    @Transient
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

    @Transient
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
    @Transient
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

    @Transient
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

//    @Column(name = "BENEFICIARY_EMAIL")
//    public String getBeneficiaryEmail() {
//        return beneficiaryEmail;
//    }
//
//    public void setBeneficiaryEmail(String beneficiaryEmail) {
//        this.beneficiaryEmail = beneficiaryEmail;
//    }

    @Column(name = "BENEFICIARY_NICK")
    public String getBeneficiaryNick() {
        return beneficiaryNick;
    }

    public void setBeneficiaryNick(String beneficiaryNick) {
        this.beneficiaryNick = beneficiaryNick;
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

    @Column(name = "BANK_ID")
    public Long getBankID() {
        return bankID;
    }

    public void setBankID(Long bankID) {
        this.bankID = bankID;
    }

    @Column(name = "RECIPIENT_ACCOUNT_NO")
    public String getBeneficiaryAccountNo() {
        return beneficiaryAccountNo;
    }

    public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
        this.beneficiaryAccountNo = beneficiaryAccountNo;
    }

    @Column(name = "NOTIFICATION_MOBILE_NO")
    public String getNotificationMobileNo() {
        return notificationMobileNo;
    }

    public void setNotificationMobileNo(String notificationMobileNo) {
        this.notificationMobileNo = notificationMobileNo;
    }

    /*

    @Column(name = "OCCURENCE_CONSUMED")
    public Long getOccurenceConsumed() {
        return occurenceConsumed;
    }

    public void setOccurenceConsumed(Long occurenceConsumed) {
        this.occurenceConsumed = occurenceConsumed;
    }
*/

    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();
        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());
        associationModelList.add(associationModel);


//        associationModel = new AssociationModel();
//
//        associationModel.setClassName("ScheduleFundsTransferModel");
//        associationModel.setPropertyName("relationScheduleFunds");
//        associationModel.setValue(getRelationScheduleFunds());

     //   associationModelList.add(associationModel);

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

        associationModel = new AssociationModel();
        associationModel.setClassName("BeneficiaryModel");
        associationModel.setPropertyName("beneficiaryModel");
        associationModel.setValue(getBeneficiaryModel());
        associationModelList.add(associationModel);


        return associationModelList;
    }
}