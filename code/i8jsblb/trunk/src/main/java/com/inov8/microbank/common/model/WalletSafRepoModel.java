package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.common.model.OlaTransactionTypeModel;
import com.inov8.integration.common.model.ReasonModel;
import com.inov8.integration.middleware.novatti.Wallet;
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
@org.hibernate.annotations.GenericGenerator(name="WALLET_SAF_REPO_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="WALLET_SAF_REPO_seq") } )
//@javax.persistence.SequenceGenerator(name = "SAF_REPO_seq", sequenceName = "SAF_REPO_seq")
@Table(name = "WALLET_SAF_REPO")
public class WalletSafRepoModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long walletSafRepoId;
    private String transactionCode;
    private AccountModel accountIdAccountModel;
    private OlaTransactionTypeModel transactionTypeIdTransactionTypeModel;
    private ReasonModel reasonIdReasonModel;
    private OlaCustomerAccountTypeModel customerAccountTypeModel;
    private Double transactionAmount;
    private Double transactionProcessingAmount;
    private Double commissionAmount;
    private Double totalAmount;
    private Date transactionTime;
    private Long category;
    private ProductModel productIdProductModel;
    private Long segmentId;
    private Long ledgerId;
    private String transactionStatus;
    private Long isComplete;
    private String senderMobileNumber;
    private String senderCnic;
    private String receiverMobileNumber;

    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;

    @Override
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setWalletSafRepoId(primaryKey);
    }

    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getWalletSafRepoId();
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&walletSafRepoId=" + getWalletSafRepoId();
        return parameters;
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "walletSafRepoId";
        return primaryKeyFieldName;
    }

    @Column(name = "WALLET_SAF_REPO_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="WALLET_SAF_REPO_seq")
    public Long getWalletSafRepoId() { return walletSafRepoId; }

    public void setWalletSafRepoId(Long walletSafRepoId) { this.walletSafRepoId = walletSafRepoId; }

    @Column(name = "TRANSACTION_CODE")
    public String getTransactionCode() { return transactionCode; }

    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    public AccountModel getRelationAccountIdAccountModel(){
        return accountIdAccountModel;
    }

    @javax.persistence.Transient
    public void setRelationAccountIdAccountModel(AccountModel accountModel) {
        this.accountIdAccountModel = accountModel;
    }

    @javax.persistence.Transient
    public AccountModel getAccountIdAccountModel() { return accountIdAccountModel; }

    @javax.persistence.Transient
    public void setAccountIdAccountModel(AccountModel accountModel) {
        if(null != accountModel)
        {
            setRelationAccountIdAccountModel((AccountModel)accountModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getAccountId() {
        if (accountIdAccountModel != null) {
            return accountIdAccountModel.getAccountId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>accountId</code> property.
     *
     * @param accountId the value for the <code>accountId</code> property
     */

    @javax.persistence.Transient
    public void setAccountId(Long accountId) {
        if(accountId == null)
        {
            accountIdAccountModel = null;
        }
        else
        {
            accountIdAccountModel = new AccountModel();
            accountIdAccountModel.setAccountId(accountId);
        }
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_TYPE_ID" , nullable = true)
    public OlaTransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel(){
        return transactionTypeIdTransactionTypeModel;
    }

    /**
     * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public OlaTransactionTypeModel getTransactionTypeIdTransactionTypeModel(){
        return getRelationTransactionTypeIdTransactionTypeModel();
    }

    /**
     * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
        this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
    }

    /**
     * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
        if(null != transactionTypeModel)
        {
            setRelationTransactionTypeIdTransactionTypeModel((OlaTransactionTypeModel)transactionTypeModel.clone());
        }
    }


    @Transient
    public Long getReasonId() {

        ReasonModel ReasonModel = getRelationReasonIdReasonModel();
        return (reasonIdReasonModel != null ? reasonIdReasonModel.getReasonId() : null);
    }

    public void setReasonId(Long reasonId) {
        if(reasonId == null){
            reasonIdReasonModel = null;
        }else{
            reasonIdReasonModel = new ReasonModel();
            reasonIdReasonModel.setReasonId(reasonId);
        }
    }
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "REASON_ID")
    public ReasonModel getRelationReasonIdReasonModel(){
        return reasonIdReasonModel;
    }

    /**
     * Returns the value of the <code>reasonIdReasonModel</code> relation property.
     *
     * @return the value of the <code>reasonIdReasonModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public ReasonModel getReasonIdReasonModel(){
        return getRelationReasonIdReasonModel();
    }

    /**
     * Sets the value of the <code>reasonIdReasonModel</code> relation property.
     *
     * @param reasonModel a value for <code>reasonIdReasonModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationReasonIdReasonModel(ReasonModel reasonModel) {
        this.reasonIdReasonModel = reasonModel;
    }

    /**
     * Sets the value of the <code>reasonIdReasonModel</code> relation property.
     *
     * @param reasonModel a value for <code>reasonIdReasonModel</code>.
     */
    @javax.persistence.Transient
    public void setReasonIdReasonModel(ReasonModel reasonModel) {
        if(null != reasonModel)
        {
            setRelationReasonIdReasonModel((ReasonModel)reasonModel.clone());
        }
    }

    @Transient
    public Long getCustomerAccountTypeId() {

        OlaCustomerAccountTypeModel customerAccountTypeModel = getCustomerAccountTypeModel();
        return (customerAccountTypeModel != null ? customerAccountTypeModel.getCustomerAccountTypeId() : null);
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        if(customerAccountTypeId == null){
            customerAccountTypeModel = null;
        }else{
            customerAccountTypeModel = new OlaCustomerAccountTypeModel();
            customerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);
        }
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUST_ACC_TYPE_ID")
    public OlaCustomerAccountTypeModel getCustomerAccountTypeModel() {
        return customerAccountTypeModel;
    }

    public void setCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
        this.customerAccountTypeModel = customerAccountTypeModel;
    }

    @Column(name = "TRANSACTION_AMOUNT" , nullable = false )
    public Double getTransactionAmount() { return transactionAmount; }

    public void setTransactionAmount(Double transactionAmount) { this.transactionAmount = transactionAmount; }

    @Column(name = "TX_PROCESSING_AMOUNT" , nullable = false )
    public Double getTransactionProcessingAmount() { return transactionProcessingAmount; }

    public void setTransactionProcessingAmount(Double transactionProcessingAmount) { this.transactionProcessingAmount = transactionProcessingAmount; }

    @Column(name = "COMMISSION_AMOUNT" , nullable = false )
    public Double getCommissionAmount() { return commissionAmount; }

    public void setCommissionAmount(Double commissionAmount) { this.commissionAmount = commissionAmount; }

    @Column(name = "TOTAL_AMOUNT" , nullable = false )
    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    @Column(name = "TRANSACTION_TIME" , nullable = false )
    public Date getTransactionTime() { return transactionTime; }

    public void setTransactionTime(Date transactionTime) { this.transactionTime = transactionTime; }

    @Column(name = "CATEGORY")
    public Long getCategory() { return category; }

    public void setCategory(Long category) { this.category = category; }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID" , nullable = true)
    public ProductModel getRelationProductIdProductModel(){
        return productIdProductModel;
    }

    @javax.persistence.Transient
    public ProductModel getProductIdProductModel(){
        return getRelationProductIdProductModel();
    }

    @javax.persistence.Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
        this.productIdProductModel = productModel;
    }

    @javax.persistence.Transient
    public void setProductIdProductModel(ProductModel productModel) {
        if(null != productModel)
        {
            setRelationProductIdProductModel((ProductModel)productModel.clone());
        }
    }

    @Transient
    public Long getProductId() {

        ProductModel productModel = getProductIdProductModel();
        return (productModel != null ? productModel.getProductId() : null);
    }

    @Transient
    public void setProductId(Long productId) {
        if(productId == null){
            this.productIdProductModel = null;
        }else{
            productIdProductModel = new ProductModel();
            productIdProductModel.setProductId(productId);
        }
    }

    @Column(name = "SEGMENT_ID" )
    public Long getSegmentId() { return segmentId; }

    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }

    @Column(name = "LEDGER_ID" )
    public Long getLedgerId() { return ledgerId; }

    public void setLedgerId(Long ledgerId) { this.ledgerId = ledgerId; }

    @Column(name = "TRANSACTION_STATUS" )
    public String getTransactionStatus() { return transactionStatus; }

    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

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

    @Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn() { return updatedOn; }

    public void setUpdatedOn(Date updatedOn) { this.updatedOn = updatedOn; }

    @Version
    @Column(name = "VERSION_NO" , nullable = false )
    public Integer getVersionNo() { return versionNo; }

    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

    @Column(name = "SENDER_MOBILE_NO" )
    public String getSenderMobileNumber() { return senderMobileNumber; }

    public void setSenderMobileNumber(String senderMobileNumber) { this.senderMobileNumber = senderMobileNumber; }

    @Column(name = "SENDER_CNIC" )
    public String getSenderCnic() { return senderCnic; }

    public void setSenderCnic(String senderCnic) { this.senderCnic = senderCnic; }

    @Column(name = "RECIPIENT_MOBILE_NO" )
    public String getReceiverMobileNumber() { return receiverMobileNumber; }

    public void setReceiverMobileNumber(String receiverMobileNumber) { this.receiverMobileNumber = receiverMobileNumber; }

    @Column(name = "IS_COMPLETE" )
    public Long getIsComplete() { return isComplete; }

    public void setIsComplete(Long isComplete) { this.isComplete = isComplete; }

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("OlaTransactionTypeModel");
        associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");
        associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("AccountModel");
        associationModel.setPropertyName("relationAccountIdAccountModel");
        associationModel.setValue(getRelationAccountIdAccountModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        WalletSafRepoModel walletSafRepoModel = new WalletSafRepoModel();
        walletSafRepoModel.setWalletSafRepoId(resultSet.getLong("WALLET_SAF_REPO_ID"));
        walletSafRepoModel.setTransactionCode(resultSet.getString("TRANSACTION_CODE"));
//        walletSafRepoModel.setTransactionTypeIdTransactionTypeModel(resultSet.getString("TRANSACTION_TYPE_ID"));
        walletSafRepoModel.setSenderMobileNumber(resultSet.getString("SENDER_MOBILE_NO"));
        walletSafRepoModel.setSenderCnic(resultSet.getString("SENDER_CNIC"));
        walletSafRepoModel.setCustomerAccountTypeId(resultSet.getLong("CUST_ACC_TYPE_ID"));
        walletSafRepoModel.setProductId(resultSet.getLong("PRODUCT_ID"));
        walletSafRepoModel.setReasonId(resultSet.getLong("REASON_ID"));
        walletSafRepoModel.setAccountId(resultSet.getLong("ACCOUNT_ID"));
        walletSafRepoModel.setTransactionAmount(resultSet.getDouble("TRANSACTION_AMOUNT"));
        walletSafRepoModel.setTransactionProcessingAmount(resultSet.getDouble("TX_PROCESSING_AMOUNT"));
        walletSafRepoModel.setCommissionAmount(resultSet.getDouble("COMMISSION_AMOUNT"));
        walletSafRepoModel.setTotalAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        walletSafRepoModel.setTransactionTime(resultSet.getDate("TRANSACTION_TIME"));
        walletSafRepoModel.setTransactionStatus(resultSet.getString("TRANSACTION_STATUS"));
        walletSafRepoModel.setCategory(resultSet.getLong("CATEGORY"));
        walletSafRepoModel.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        walletSafRepoModel.setLedgerId(resultSet.getLong("LEDGER_ID"));
        walletSafRepoModel.setReceiverMobileNumber(resultSet.getString("RECIPIENT_MOBILE_NO"));
//        walletSafRepoModel.set(resultSet.getString("SENDER_MOBILE_NO"));
        walletSafRepoModel.setIsComplete(resultSet.getLong("IS_COMPLETE"));
        walletSafRepoModel.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        walletSafRepoModel.setCreatedBy(resultSet.getLong("CREATED_BY"));
        walletSafRepoModel.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        walletSafRepoModel.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return walletSafRepoModel;
    }
}
