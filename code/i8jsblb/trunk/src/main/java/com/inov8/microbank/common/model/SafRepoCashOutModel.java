package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Attique on 9/6/2018.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SAF_REPO_CASH_OUT_SEQ", sequenceName = "SAF_REPO_CASH_OUT_SEQ", allocationSize = 1)
@Table(name = "SAF_REPO_CASH_OUT")
public class SafRepoCashOutModel extends BasePersistableModel implements Serializable {


    private Long safRepoCashOutId;
    private String transactionCode;
    private Double transactionAmount;
    private Date transactionTime;
    private Long segmentId;
    private String transactionStatus;
    private String customerAccountNumber;
    private String agentAccountNumber;
    private String sellerCode;
    private String sessionId;
    private String customerCnic;
    private String projectCode;
    private String terminalId;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String transactionId;

    private ProductModel productIdProductModel;
    private TransactionTypeModel transactionTypeIdTransactionTypeModel;
    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;


    private Date createdOn;
    private Date updatedOn;

    private Integer versionNo;
    private Boolean isComplete;
    private String status;






    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getSafRepoCashOutId();
    }
    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "safRepoCashOutId";
        return primaryKeyFieldName;
    }
    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&safRepoCashOutId=" + getSafRepoCashOutId();
        return parameters;
    }
    @Override
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setSafRepoCashOutId(primaryKey);

    }

    @Column(name = "SAF_REPO_CASH_OUT_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SAF_REPO_CASH_OUT_SEQ")
    public Long getSafRepoCashOutId() {
        return safRepoCashOutId;
    }

    public void setSafRepoCashOutId(Long safRepoCashOutId) {
        this.safRepoCashOutId = safRepoCashOutId;
    }

    @Column(name = "TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "TRANSACTION_TIME")
    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    @Column(name = "TRANSACTION_STATUS")
    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Column(name = "CUSTOMER_ACC_NUMBER")
    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    @Column(name = "AGENT_ACC_NUMBER")
    public String getAgentAccountNumber() {
        return agentAccountNumber;
    }

    public void setAgentAccountNumber(String agentAccountNumber) {
        this.agentAccountNumber = agentAccountNumber;
    }

    @Column(name = "SELLER_CODE")
    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name = "CUSTOMER_CNIC")
    public String getCustomerCnic() {
        return customerCnic;
    }

    public void setCustomerCnic(String customerCnic) {
        this.customerCnic = customerCnic;
    }

    @Column(name = "PROJECT_CODE")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Column(name = "RESERVED_FILED_1")
    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    @Column(name = "RESERVED_FILED_2")
    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    @Column(name = "RESERVED_FILED_3")
    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    @Column(name = "RESERVED_FILED_4")
    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    @Column(name = "RESERVED_FILED_5")
    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
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

    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Column(name = "IS_COMPLETE")
    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_TYPE_ID" , nullable = true)
    public TransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel(){
        return transactionTypeIdTransactionTypeModel;
    }

    /**
     * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public TransactionTypeModel getTransactionTypeIdTransactionTypeModel(){
        return getRelationTransactionTypeIdTransactionTypeModel();
    }

    /**
     * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationTransactionTypeIdTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
        this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
    }

    /**
     * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
     *
     * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setTransactionTypeIdTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
        if(null != transactionTypeModel)
        {
            setRelationTransactionTypeIdTransactionTypeModel((TransactionTypeModel)transactionTypeModel.clone());
        }
    }

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("TransactionTypeModel");
        associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");
        associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());

        associationModelList.add(associationModel);


        return associationModelList;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "TRANSACTION_ID")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
