package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionAllViewModel entity bean.
 *
 * @author  Naseer Ullah
 * @since $Date: 2012/07/18 20:00:00 $
 */
@XmlRootElement(name="transactionAllViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANSACTION_ALL_VIEW")
public class TransactionAllViewModel extends BasePersistableModel implements java.io.Serializable {

    private static final long serialVersionUID = -4484430755496777855L;

    // Fields
    private Long transactionId;
    private Long transactionCodeId;
    private String mfsId;
    private String saleMobileNo;
    private String transactionCode;
    private Date createdOn;
    private Long productId;
    private String productName;
    private Double transactionAmount;
    private Double totalAmount;
    private Long supProcessingStatusId;
    private String processingStatusName;
    private String agent1Id;
    private String agent2Id;
    private String senderCnic;
    private String recipientMobileNo;
    private String recipientMfsId;
    private String recipientCnic;
    private Double serviceChargesInclusive;
    private Double serviceChargesExclusive;
    private Boolean issue;
    private Date billDueDate;
    private String agent1Name;
    private Long tellerAppUserId;
    private Boolean updateP2PFlag;
    private String transactionPurpose;

    /** default constructor */
    public TransactionAllViewModel() {
    }

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getTransactionId();
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        return "transactionId";
    }

    @Override
    @javax.persistence.Transient
    public void setPrimaryKey( Long primaryKey ) {
        setTransactionId( primaryKey );
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "&transactionId=" + getTransactionId();
        return parameters;
    }

    // Property accessors
    @Id
    @Column(name = "TRANSACTION_ID", nullable = false, precision = 10, scale = 0)
    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Returns the value of the <code>transactionCodeId</code> property.
     *
     */
    @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
    public Long getTransactionCodeId() {
        return transactionCodeId;
    }

    /**
     * Sets the value of the <code>transactionCodeId</code> property.
     *
     * @param transactionCodeId the value for the <code>transactionCodeId</code> property
     *
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setTransactionCodeId(Long transactionCodeId) {
        this.transactionCodeId = transactionCodeId;
    }

    @Column(name = "MFS_ID", length = 50)
    public String getMfsId() {
        return this.mfsId;
    }

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    @Column(name = "SALE_MOBILE_NO", length = 50)
    public String getSaleMobileNo() {
        return this.saleMobileNo;
    }

    public void setSaleMobileNo(String saleMobileNo) {
        this.saleMobileNo = saleMobileNo;
    }

    @Column(name = "TRANSACTION_CODE", nullable = false, length = 50)
    public String getTransactionCode() {
        return this.transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "CREATED_ON", nullable = false, length = 7)
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "PRODUCT_NAME", nullable = false, length = 50)
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "TRANSACTION_AMOUNT", nullable = false, precision = 16, scale = 4)
    public Double getTransactionAmount() {
        return this.transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "TOTAL_AMOUNT", precision = 0)
    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "SUP_PROCESSING_STATUS_ID", nullable = false, precision = 10, scale = 0)
    public Long getSupProcessingStatusId() {
        return this.supProcessingStatusId;
    }

    public void setSupProcessingStatusId(Long supProcessingStatusId) {
        this.supProcessingStatusId = supProcessingStatusId;
    }

    @Column(name = "PROCESSING_STATUS_NAME", length = 50)
    public String getProcessingStatusName() {
        return this.processingStatusName;
    }

    public void setProcessingStatusName(String processingStatusName) {
        this.processingStatusName = processingStatusName;
    }

    @Column(name = "AGENT1_ID", length = 50)
    public String getAgent1Id() {
        return this.agent1Id;
    }

    public void setAgent1Id(String agent1Id) {
        this.agent1Id = agent1Id;
    }

    @Column(name = "AGENT2_ID", length = 50)
    public String getAgent2Id() {
        return this.agent2Id;
    }

    public void setAgent2Id(String agent2Id) {
        this.agent2Id = agent2Id;
    }

    @Column(name = "SENDER_CNIC", length = 250)
    public String getSenderCnic() {
        return this.senderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        this.senderCnic = senderCnic;
    }

    @Column(name = "RECIPIENT_MOBILE_NO", length = 13)
    public String getRecipientMobileNo() {
        return this.recipientMobileNo;
    }

    public void setRecipientMobileNo(String recipientMobileNo) {
        this.recipientMobileNo = recipientMobileNo;
    }

    @Column(name = "RECIPIENT_MFS_ID", length = 50)
    public String getRecipientMfsId() {
        return this.recipientMfsId;
    }

    public void setRecipientMfsId(String recipientMfsId) {
        this.recipientMfsId = recipientMfsId;
    }

    @Column(name = "RECIPIENT_CNIC", length = 50)
    public String getRecipientCnic() {
        return this.recipientCnic;
    }

    public void setRecipientCnic(String recipientCnic) {
        this.recipientCnic = recipientCnic;
    }

    @Column(name = "SERVICE_CHARGES_INCLUSIVE", precision = 0)
    public Double getServiceChargesInclusive() {
        return serviceChargesInclusive;
    }

    public void setServiceChargesInclusive( Double serviceChargesInclusive ) {
        this.serviceChargesInclusive = serviceChargesInclusive;
    }

    @Column(name = "SERVICE_CHARGES_EXCLUSIVE", precision = 0)
    public Double getServiceChargesExclusive() {
        return this.serviceChargesExclusive;
    }

    public void setServiceChargesExclusive(Double serviceChargesExclusive) {
        this.serviceChargesExclusive = serviceChargesExclusive;
    }

    @Column(name = "IS_ISSUE", precision = 1, scale = 0)
    public Boolean getIssue() {
        return issue;
    }

    public void setIssue( Boolean issue ) {
        this.issue = issue;
    }

    @Column( name = "BILL_DUE_DATE", nullable = false, length = 7)
    public Date getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }

    @Column( name = "AGENT1_NAME" )
    public String getAgent1Name() {
        return agent1Name;
    }

    public void setAgent1Name(String agent1Name) {
        if (agent1Name != null) {
            this.agent1Name = agent1Name;
        }
    }

    @Column( name = "TELLER_APP_USER_ID" )
    public Long getTellerAppUserId() {
        return tellerAppUserId;
    }

    public void setTellerAppUserId(Long tellerAppUserId) {
        this.tellerAppUserId = tellerAppUserId;
    }

    @Column( name = "PRODUCT_ID" )
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "UPDATE_P2P_FLAG")
    public Boolean getUpdateP2PFlag() {
        return updateP2PFlag;
    }

    public void setUpdateP2PFlag(Boolean updateP2PFlag) {
        this.updateP2PFlag = updateP2PFlag;
    }

    @Transient
    public String getUpdateP2PFlagString(){
        String updateP2PFlagString = "";
        if(this.updateP2PFlag == null){
            updateP2PFlagString = "";
        }else if(this.updateP2PFlag == false){
            updateP2PFlagString = "Yes";
        }else if(this.updateP2PFlag == true){
            updateP2PFlagString = "No";
        }
        return updateP2PFlagString;
    }

    @Column(name = "TRANSACTION_PURPOSE")
    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }
}