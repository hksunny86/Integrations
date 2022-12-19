package com.inov8.microbank.common.model.portal.inovtransactiondetailmodule;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.des.DESEncryption;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name="bIspTransactionDetailModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BISP_TRANSACTION_DETAIL_VIEW")
public class BispTransactionDetailViewModel extends BasePersistableModel implements Serializable {

    DESEncryption desUtil = DESEncryption.getInstance();
    private Long pk;
    private Long transactionId;
    private String mfsId;
    private String recipientMfsId;
    private Long segmentId;
    private String segment;
    private String recipientAccountNo;
    private String saleMobileNo;
    private String senderCnic;
    private String recipientMobileNo;
    private String recipientCnic;
    private String authorizationCode;
    private String bankAccountNo;
    private String bankAccountNoLastFive;
    private String transactionCode;
    private Date createdOn;
    private Date updatedOn;
    private Long productId;
    private String productName;
    private String productCode;
    private String billType;
    private Long supplierId;
    private String supplierName;
    private Long paymentModeId;
    private String paymentMode;
    private Long bankId;
    private String bankName;
    private Long transactionCodeId;
    private Long transactionDetailId;
    private Double transactionAmount;
    private String veriflyStatus;
    private String cashDepositorCnic;
    private String processingStatusName;
    private String processingStatusId;
    private String agent1Id;
    private String agent2Id;
    private String agent1MobileNo;
    private String consumerNo;
    private Double totalAmount;
    private Long senderDeviceTypeId;
    private String deviceType;
    private Long recipientDeviceTypeId;
    private String recipientDeviceType;
    private Boolean isManualOTPin;
    private String isManualOTPinString;

    private Double fed;
    private Double wht;
    private Double taxDeducted;
    private Double akblCommission;
    private Double agentCommission;
    private Double agent2Commission;
    private Double franchise1Commission;
    private Double retailerShare;
    private Double salesTeamCommission;
    private Double blbSettlementCommission;
    private Double othersCommission;

    private Double inclusiveCharges;
    private Double exclusiveCharges;

    private String titleFetchRrn;
    private String billInquiryRrn;
    private String fundTransferRrn;
    private String billPaymentRrn;
    private String checkBalanceRrn;
    private Date billDueDate;
    private String sendingRegion;
    private String receivingRegion;
    //   private Double toSupplier;
//   private Double totalBankComm;
//   private Double mcpl;
    private String billAggregator;
    private String agent2AccountNo;
    private Long failureReasonId;
    private String failureReason;
    private String agentBusinessName;
    private Boolean updateP2PFlag;
    private String updateP2PFlagString;
    private String senderAgentAccountNo;
    private String recipientAgentAccountNo;

    private Date reversedDate;
    private String reversedByName;
    private String reversedComments;

    private String senderBVS;
    private String receiverBVS;
    private String fonepayTransactionCode;
    private String fonepayTransactionType;

    private String terminalId;
    private String externalProductName;

    private String senderDistributorName;
    private String senderServiceOPName;

    private String receiverDistributorName;
    private String receiverServiceOPName;

    private String sendingRegionName;
    private String receivingRegionName;

    private Long senderDistributorId;
    private Long senderServiceOPId;

    private Long receiverDistributorId;
    private Long receiverServiceOPId;

    private String sendingAreaName;
    private String receivingAreaName;

    private Long senderArearId;
    private Long receiverAreaId;

    private Double scoCommission;

    private String channelId;

    private String nIfq;
    private String mintaieCount;

    private String transactionPurpose;

    private Long bispRetryCount;
    private String bispResponseCode;
    private String debitCardNumber;
    private String bankShortName;

    private String macAddress;
    private String ipAddress;
    private String latitude;
    private String longitude;
    private String imeiNumber;


    @Column(name = "SEGMENT")
    public String getSegment() { return segment; }

    public void setSegment(String segment) { this.segment = segment; }


    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() { return segmentId; }

    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }



    @Column(name = "BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        this.agentBusinessName = agentBusinessName;
    }

    private transient String transactionNo;

    /**
     * Default constructor.
     */
    public BispTransactionDetailViewModel() {
    }

    @Transient
    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    /**
     * Returns the value of the <code>pk</code> property.
     */
    @Column(name = "PK")
    @Id
    public Long getPk() {
        return pk;
    }

    /**
     * Sets the value of the <code>pk</code> property.
     *
     * @param pk the value for the <code>pk</code> property
     */

    public void setPk(Long pk) {
        this.pk = pk;
    }

    /**
     * Returns the value of the <code>transactionId</code> property.
     */
    @Column(name = "TRANSACTION_ID", nullable = false)
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the <code>transactionId</code> property.
     *
     * @param transactionId the value for the <code>transactionId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Returns the value of the <code>mfsId</code> property.
     */
    @Column(name = "MFS_ID", length = 50)
    public String getMfsId() {
        return mfsId;
    }

    /**
     * Sets the value of the <code>mfsId</code> property.
     *
     * @param mfsId the value for the <code>mfsId</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    /**
     * Returns the value of the <code>recipientMfsId</code> property.
     */
    @Column(name = "RECIPIENT_MFS_ID", length = 50)
    public String getRecipientMfsId() {
        return recipientMfsId;
    }

    /**
     * Sets the value of the <code>recipientMfsId</code> property.
     *
     * @param recipientMfsId the value for the <code>recipientMfsId</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setRecipientMfsId(String recipientMfsId) {
        this.recipientMfsId = recipientMfsId;
    }

    @Column(name = "RECIPIENT_ACCOUNT_NO", length = 30)
    public String getRecipientAccountNo() {
        return recipientAccountNo;
    }

    public void setRecipientAccountNo(String recipientAccountNo) {
        this.recipientAccountNo = recipientAccountNo;
    }

    /**
     * Returns the value of the <code>saleMobileNo</code> property.
     */
    @Column(name = "SALE_MOBILE_NO", length = 50)
    public String getSaleMobileNo() {
        return saleMobileNo;
    }

    /**
     * Sets the value of the <code>saleMobileNo</code> property.
     *
     * @param saleMobileNo the value for the <code>saleMobileNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setSaleMobileNo(String saleMobileNo) {
        this.saleMobileNo = saleMobileNo;
    }

    /**
     * Returns the value of the <code>recipientMobileNo</code> property.
     */
    @Column(name = "RECIPIENT_MOBILE_NO", length = 50)
    public String getRecipientMobileNo() {
        return recipientMobileNo;
    }

    /**
     * Sets the value of the <code>recipientMobileNo</code> property.
     *
     * @param saleMobileNo the value for the <code>recipientMobileNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="13"
     */

    public void setRecipientMobileNo(String recipientMobileNo) {
        this.recipientMobileNo = recipientMobileNo;
    }

    /**
     * Returns the value of the <code>authorizationCode</code> property.
     */
    @Column(name = "AUTHORIZATION_CODE", length = 50)
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * Sets the value of the <code>authorizationCode</code> property.
     *
     * @param authorizationCode the value for the <code>authorizationCode</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    /**
     * Returns the value of the <code>bankAccountNo</code> property.
     */
    @Column(name = "BANK_ACCOUNT_NO", length = 20)
    public String getBankAccountNo() {
        return bankAccountNo;
    }

    /**
     * Sets the value of the <code>bankAccountNo</code> property.
     *
     * @param bankAccountNo the value for the <code>bankAccountNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="20"
     */

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    /**
     * Returns the value of the <code>bankAccountNoLastFive</code> property.
     */
    @Column(name = "BANK_ACCOUNT_NO_LAST_FIVE", length = 5)
    public String getBankAccountNoLastFive() {
        return bankAccountNoLastFive;
    }

    /**
     * Sets the value of the <code>bankAccountNoLastFive</code> property.
     *
     * @param bankAccountNoLastFive the value for the <code>bankAccountNoLastFive</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="5"
     */

    public void setBankAccountNoLastFive(String bankAccountNoLastFive) {
        this.bankAccountNoLastFive = bankAccountNoLastFive;
    }

    /**
     * Returns the value of the <code>transactionCode</code> property.
     */
    @Column(name = "TRANSACTION_CODE", nullable = false, length = 50)
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the <code>transactionCode</code> property.
     *
     * @param transactionCode the value for the <code>transactionCode</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    /**
     * Returns the value of the <code>createdOn</code> property.
     */
    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the <code>createdOn</code> property.
     *
     * @param createdOn the value for the <code>createdOn</code> property
     */

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

    /**
     * Returns the value of the <code>productId</code> property.
     */
    @Column(name = "PRODUCT_ID", nullable = false)
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the value of the <code>productId</code> property.
     *
     * @param productId the value for the <code>productId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Returns the value of the <code>productName</code> property.
     */
    @Column(name = "PRODUCT_NAME", nullable = false, length = 50)
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the value of the <code>productName</code> property.
     *
     * @param productName the value for the <code>productName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Returns the value of the <code>productCode</code> property.
     */
    @Column(name = "PRODUCT_CODE", length = 50)
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Column(name = "BILL_TYPE", length = 50)
    public String getBillType() {
        return billType;
    }

    public void setBillType(String productType) {
        this.billType = productType;
    }

    /**
     * Returns the value of the <code>supplierId</code> property.
     */
    @Column(name = "SUPPLIER_ID", nullable = false)
    public Long getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the value of the <code>supplierId</code> property.
     *
     * @param supplierId the value for the <code>supplierId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Returns the value of the <code>supplierName</code> property.
     */
    @Column(name = "SUPPLIER_NAME", nullable = false, length = 50)
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the value of the <code>supplierName</code> property.
     *
     * @param supplierName the value for the <code>supplierName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * Returns the value of the <code>paymentModeId</code> property.
     */
    @Column(name = "PAYMENT_MODE_ID", nullable = false)
    public Long getPaymentModeId() {
        return paymentModeId;
    }

    /**
     * Sets the value of the <code>paymentModeId</code> property.
     *
     * @param paymentModeId the value for the <code>paymentModeId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setPaymentModeId(Long paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    /**
     * Returns the value of the <code>paymentMode</code> property.
     */
    @Column(name = "PAYMENT_MODE", nullable = false, length = 50)
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * Sets the value of the <code>paymentMode</code> property.
     *
     * @param paymentMode the value for the <code>paymentMode</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * Returns the value of the <code>bankId</code> property.
     */
    @Column(name = "BANK_ID")
    public Long getBankId() {
        return bankId;
    }

    /**
     * Sets the value of the <code>bankId</code> property.
     *
     * @param bankId the value for the <code>bankId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    /**
     * Returns the value of the <code>bankName</code> property.
     */
    @Column(name = "BANK_NAME", length = 50)
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the <code>bankName</code> property.
     *
     * @param bankName the value for the <code>bankName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Returns the value of the <code>transactionCodeId</code> property.
     */
    @Column(name = "TRANSACTION_CODE_ID", nullable = false)
    public Long getTransactionCodeId() {
        return transactionCodeId;
    }

    /**
     * Sets the value of the <code>transactionCodeId</code> property.
     *
     * @param transactionCodeId the value for the <code>transactionCodeId</code> property
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

    /**
     * Returns the value of the <code>transactionDetailId</code> property.
     */
    @Column(name = "TRANSACTION_DETAIL_ID", nullable = false)
    public Long getTransactionDetailId() {
        return transactionDetailId;
    }

    /**
     * Sets the value of the <code>transactionDetailId</code> property.
     *
     * @param transactionDetailId the value for the <code>transactionDetailId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setTransactionDetailId(Long transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    /**
     * Returns the value of the <code>transactionAmount</code> property.
     */
    @Column(name = "TRANSACTION_AMOUNT", nullable = false)
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the <code>transactionAmount</code> property.
     *
     * @param transactionAmount the value for the <code>transactionAmount</code> property
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * Returns the value of the <code>veriflyStatus</code> property.
     */
    @Column(name = "VERIFLY_STATUS", length = 2)
    public String getVeriflyStatus() {
        return veriflyStatus;
    }

    /**
     * Sets the value of the <code>veriflyStatus</code> property.
     *
     * @param veriflyStatus the value for the <code>veriflyStatus</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="2"
     */

    public void setVeriflyStatus(String veriflyStatus) {
        this.veriflyStatus = veriflyStatus;
    }

    /**
     * Returns the value of the <code>processingStatusName</code> property.
     */
    @Column(name = "PROCESSING_STATUS_NAME", length = 50)
    public String getProcessingStatusName() {
        return processingStatusName;
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

    /**
     * Sets the value of the <code>processingStatusName</code> property.
     *
     * @param processingStatusName the value for the <code>processingStatusName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setProcessingStatusName(String processingStatusName) {
        this.processingStatusName = processingStatusName;
    }

    @Column(name = "SUP_PROCESSING_STATUS_ID")
    public String getProcessingStatusId() {
        return processingStatusId;
    }

    public void setProcessingStatusId(String processingStatusId) {
        this.processingStatusId = processingStatusId;
    }

    @Column(name = "AGENT1_MOBILE_NO")
    public String getAgent1MobileNo() {
        return agent1MobileNo;
    }

    public void setAgent1MobileNo(String agent1MobileNo) {
        this.agent1MobileNo = agent1MobileNo;
    }

    /**
     * Returns the value of the <code>consumerNo</code> property.
     */
    @Column(name = "CONSUMER_NO", length = 50)
    public String getConsumerNo() {
        return consumerNo;
    }

    /**
     * Sets the value of the <code>consumerNo</code> property.
     *
     * @param consumerNo the value for the <code>consumerNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    /**
     * Returns the value of the <code>inclusiveCharges</code> property.
     */
    @Column(name = "SERVICE_CHARGES_INCLUSIVE")
    public Double getInclusiveCharges() {
        return inclusiveCharges;
    }

    /**
     * Sets the value of the <code>inclusiveCharges</code> property.
     */

    public void setInclusiveCharges(Double inclusiveCharges) {
        this.inclusiveCharges = inclusiveCharges;
    }

    /**
     * Returns the value of the <code>exclusiveCharges</code> property.
     */
    @Column(name = "SERVICE_CHARGES_EXCLUSIVE")
    public Double getExclusiveCharges() {
        return exclusiveCharges;
    }

    /**
     * Sets the value of the <code>exclusiveCharges</code> property.
     */
    public void setExclusiveCharges(Double exclusiveCharges) {
        this.exclusiveCharges = exclusiveCharges;
    }

    /**
     * Returns the value of the <code>totalAmount</code> property.
     */
    @Column(name = "TOTAL_AMOUNT", nullable = false)
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the value of the <code>totalAmount</code> property.
     *
     * @param totalAmount the value for the <code>totalAmount</code> property
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }


    /**
     * Returns the value of the <code>toSupplier</code> property.
     *
     */
//      @Column(name = "TO_SUPPLIER"  )
//   public Double getToSupplier() {
//      return toSupplier;
//   }

    /**
     * Sets the value of the <code>toSupplier</code> property.
     *
     * @param toSupplier the value for the <code>toSupplier</code> property
     *
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

//   public void setToSupplier(Double toSupplier) {
//      this.toSupplier = toSupplier;
//   }

    /**
     * Returns the value of the <code>totalBankComm</code> property.
     *
     */
//      @Column(name = "TOTAL_BANK_COMM"  )
//   public Double getTotalBankComm() {
//      return totalBankComm;
//   }

    /**
     * Sets the value of the <code>totalBankComm</code> property.
     *
     * @param totalBankComm the value for the <code>totalBankComm</code> property
     *
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

//   public void setTotalBankComm(Double totalBankComm) {
//      this.totalBankComm = totalBankComm;
//   }

    /**
     * Returns the value of the <code>mcpl</code> property.
     *
     */
//      @Column(name = "MCPL"  )
//   public Double getMcpl() {
//      return mcpl;
//   }

    /**
     * Sets the value of the <code>mcpl</code> property.
     *
     * @param mcpl the value for the <code>mcpl</code> property
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

//   public void setMcpl(Double mcpl) {
//      this.mcpl = mcpl;
//   }
    @Column(name = "SENDER_DEVICE_TYPE_ID")
    public Long getSenderDeviceTypeId() {
        return senderDeviceTypeId;
    }

    public void setSenderDeviceTypeId(Long senderDeviceTypeId) {
        this.senderDeviceTypeId = senderDeviceTypeId;
    }

    @Column(name = "DEVICE_TYPE")
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Column(name = "RECIPIENT_DEVICE_TYPE_ID")
    public Long getRecipientDeviceTypeId() {
        return recipientDeviceTypeId;
    }

    public void setRecipientDeviceTypeId(Long recipientDeviceTypeId) {
        this.recipientDeviceTypeId = recipientDeviceTypeId;
    }

    @Column(name = "RECIPIENT_DEVICE_TYPE")
    public String getRecipientDeviceType() {
        return recipientDeviceType;
    }

    public void setRecipientDeviceType(String recipientDeviceType) {
        this.recipientDeviceType = recipientDeviceType;
    }

    @Column(name = "IS_MANUAL_OT_PIN")
    public Boolean getIsManualOTPin() {
        return isManualOTPin;
    }

    public void setIsManualOTPin(Boolean isManualOTPin) {
        this.isManualOTPin = isManualOTPin;
    }


    @Column(name = "IS_MANUAL_OT_PIN_STRING")
    public String getIsManualOTPinString() {
        return isManualOTPinString;
    }

    public void setIsManualOTPinString(String isManualOTPinString) {
        this.isManualOTPinString = isManualOTPinString;
    }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     *
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + getPk();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Column(name = "FED")
    public Double getFed() {
        return fed;
    }

    public void setFed(Double fed) {
        this.fed = fed;
    }

    @Column(name = "WHT")
    public Double getWht() {
        return wht;
    }

    public void setWht(Double wht) {
        this.wht = wht;
    }

    @Column(name = "TAX_DEDUCTED")
    public Double getTaxDeducted() {
        return taxDeducted;
    }

    public void setTaxDeducted(Double taxDeducted) {
        this.taxDeducted = taxDeducted;
    }

    @Column(name = "TO_BANK")
    public Double getAkblCommission() {
        return akblCommission;
    }

    public void setAkblCommission(Double akblCommission) {
        this.akblCommission = akblCommission;
    }

    @Column(name = "TO_AGENT1")
    public Double getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(Double agentCommission) {
        this.agentCommission = agentCommission;
    }

    @Column(name = "TO_AGENT2")
    public Double getAgent2Commission() {
        return agent2Commission;
    }

    public void setAgent2Commission(Double agent2Commission) {
        this.agent2Commission = agent2Commission;
    }


    @Column(name = "TO_FRANCHISE1")
    public Double getFranchise1Commission() {
        return franchise1Commission;
    }

    public void setFranchise1Commission(Double franchise1Commission) {
        this.franchise1Commission = franchise1Commission;
    }


    @Column(name = "TO_SALES_TEAM")
    public Double getSalesTeamCommission() {
        return salesTeamCommission;
    }

    public void setSalesTeamCommission(Double salesTeamCommission) {
        this.salesTeamCommission = salesTeamCommission;
    }

    @Column(name = "OTHERS")
    public Double getOthersCommission() {
        return othersCommission;
    }

    public void setOthersCommission(Double othersCommission) {
        this.othersCommission = othersCommission;
    }

    @Column(name = "RETAILER_SHARE")
    public Double getRetailerShare() {
        return retailerShare;
    }

    public void setRetailerShare(Double retailerShare) {
        this.retailerShare = retailerShare;
    }

    @Column(name = "SENDER_CNIC")
    public String getSenderCnic() {
        return senderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        this.senderCnic = senderCnic;
    }

    @Column(name = "RECIPIENT_CNIC")
    public String getRecipientCnic() {
        return recipientCnic;
    }

    public void setRecipientCnic(String recipientCnic) {
        this.recipientCnic = recipientCnic;
    }

    @Column(name = "DEPOSITOR_CNIC")
    public String getCashDepositorCnic() {
        return cashDepositorCnic;
    }

    public void setCashDepositorCnic(String cashDepositorCnic) {
        this.cashDepositorCnic = cashDepositorCnic;
    }

    @Column(name = "TITLE_FETCH_RRN")
    public String getTitleFetchRrn() {
        return titleFetchRrn;
    }

    public void setTitleFetchRrn(String titleFetchRrn) {
        this.titleFetchRrn = titleFetchRrn;
    }

    @Column(name = "BILL_INQUIRY_RRN")
    public String getBillInquiryRrn() {
        return billInquiryRrn;
    }

    public void setBillInquiryRrn(String billInquiryRrn) {
        this.billInquiryRrn = billInquiryRrn;
    }

    @Column(name = "FUND_TRANSFER_RRN")
    public String getFundTransferRrn() {
        return fundTransferRrn;
    }

    public void setFundTransferRrn(String fundTransferRrn) {
        this.fundTransferRrn = fundTransferRrn;
    }

    @Column(name = "BILL_PAYMENT_RRN")
    public String getBillPaymentRrn() {
        return billPaymentRrn;
    }

    public void setBillPaymentRrn(String billPaymentRrn) {
        this.billPaymentRrn = billPaymentRrn;
    }

    @Column(name = "CHECK_BALANCE_RRN")
    public String getCheckBalanceRrn() {
        return checkBalanceRrn;
    }

    public void setCheckBalanceRrn(String checkBalanceRrn) {
        this.checkBalanceRrn = checkBalanceRrn;
    }

    @Column(name = "BILL_DUE_DATE")
    public Date getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }

    @Column(name = "SENDING_REGION")
    public String getSendingRegion() {
        return sendingRegion;
    }

    public void setSendingRegion(String sendingRegion) {
        this.sendingRegion = sendingRegion;
    }

    @Column(name = "RECEIVING_REGION")
    public String getReceivingRegion() {
        return receivingRegion;
    }

    public void setReceivingRegion(String receivingRegion) {
        this.receivingRegion = receivingRegion;
    }

    @Column(name = "BILL_AGGREGATOR")
    public String getBillAggregator() {
        return billAggregator;
    }

    public void setBillAggregator(String billAggregator) {
        this.billAggregator = billAggregator;
    }

    @Column(name = "AGENT2_ACCOUNT_NO")
    public String getAgent2AccountNo() {
        String agent2AccountNo = null;
        if (null != this.agent2AccountNo) {
            try {
                agent2AccountNo = desUtil.decrypt(this.agent2AccountNo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return agent2AccountNo;
    }

    public void setAgent2AccountNo(String agent2AccountNo) {
        if (agent2AccountNo != null) {
            this.agent2AccountNo = agent2AccountNo;
        }
    }

    @Column(name = "FAILURE_REASON_ID")
    public Long getFailureReasonId() {
        return failureReasonId;
    }

    public void setFailureReasonId(Long failureReasonId) {
        this.failureReasonId = failureReasonId;
    }

    @Column(name = "FAILURE_REASON")
    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }


    @Column(name = "UPDATE_P2P_FLAG")
    public Boolean getUpdateP2PFlag() {
        return updateP2PFlag;
    }

    public void setUpdateP2PFlag(Boolean updateP2PFlag) {
        this.updateP2PFlag = updateP2PFlag;
    }

    @Column(name = "UPDATE_P2P_FLAG_STRING")
    public String getUpdateP2PFlagString() {
        return updateP2PFlagString;
    }

    public void setUpdateP2PFlagString(String updateP2PFlagString) {
        this.updateP2PFlagString = updateP2PFlagString;
    }

    @Column(name = "SENDER_AGENT_ACCOUNT_NO")
    public String getSenderAgentAccountNo() {
        return senderAgentAccountNo;
    }

    public void setSenderAgentAccountNo(String senderAgentAccountNo) {
        this.senderAgentAccountNo = senderAgentAccountNo;
    }

    @Column(name = "RECIPIENT_AGENT_ACCOUNT_NO")
    public String getRecipientAgentAccountNo() {
        return recipientAgentAccountNo;
    }

    public void setRecipientAgentAccountNo(String recipientAgentAccountNo) {
        this.recipientAgentAccountNo = recipientAgentAccountNo;
    }

    @Column(name = "REVERSED_BY_NAME")
    public String getReversedByName() {
        return reversedByName;
    }

    public void setReversedByName(String reversedByName) {
        this.reversedByName = reversedByName;
    }

    @Column(name = "REVERSED_ON")
    public Date getReversedDate() {
        return reversedDate;
    }

    public void setReversedDate(Date reversedDate) {
        this.reversedDate = reversedDate;
    }

    @Column(name = "REVERSED_COMMENTS")
    public String getReversedComments() {
        return reversedComments;
    }

    public void setReversedComments(String reversedComments) {
        this.reversedComments = reversedComments;
    }

    @Column(name = "SENDER_BVS")
    public String getSenderBVS() {
        return senderBVS;
    }

    public void setSenderBVS(String senderBVS) {
        this.senderBVS = senderBVS;
    }

    @Column(name = "RECEIVER_BVS")
    public String getReceiverBVS() {
        return receiverBVS;
    }

    public void setReceiverBVS(String receiverBVS) {
        this.receiverBVS = receiverBVS;
    }

    @Column(name = "BLB_COMM_SETTLEMENT")
    public Double getBlbSettlementCommission() {
        return blbSettlementCommission;
    }

    public void setBlbSettlementCommission(Double blbSettlementCommission) {
        this.blbSettlementCommission = blbSettlementCommission;
    }

    @Column(name = "FONEPAY_TRANSACTION_CODE")
    public String getFonepayTransactionCode() {
        return fonepayTransactionCode;
    }

    public void setFonepayTransactionCode(String fonepayTransactionCode) {
        this.fonepayTransactionCode = fonepayTransactionCode;
    }

    @Column(name = "FONEPAY_TRANSACTION_TYPE")
    public String getFonepayTransactionType() {
        return fonepayTransactionType;
    }

    public void setFonepayTransactionType(String fonepayTransactionType) {
        this.fonepayTransactionType = fonepayTransactionType;
    }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Column(name = "EXTERNAL_PRODUCT_NAME")
    public String getExternalProductName() {
        return externalProductName;
    }

    public void setExternalProductName(String externalProductName) {
        this.externalProductName = externalProductName;
    }

    @Column(name = "SENDER_DISTRIBUTOR_NAME")
    public String getSenderDistributorName() {
        return senderDistributorName;
    }

    public void setSenderDistributorName(String senderDistributorName) {
        this.senderDistributorName = senderDistributorName;
    }

    @Column(name = "SENDER_SERVICE_OP_NAME")
    public String getSenderServiceOPName() {
        return senderServiceOPName;
    }

    public void setSenderServiceOPName(String senderServiceOPName) {
        this.senderServiceOPName = senderServiceOPName;
    }

    @Column(name = "RECEIVER_DISTRIBUTOR_NAME")
    public String getReceiverDistributorName() {
        return receiverDistributorName;
    }

    public void setReceiverDistributorName(String receiverDistributorName) {
        this.receiverDistributorName = receiverDistributorName;
    }

    @Column(name = "RECEIVER_SERVICE_OP_NAME")
    public String getReceiverServiceOPName() {
        return receiverServiceOPName;
    }

    public void setReceiverServiceOPName(String receiverServiceOPName) {
        this.receiverServiceOPName = receiverServiceOPName;
    }

    @Column(name = "SENDER_REGION_NAME")
    public String getSendingRegionName() {
        return sendingRegionName;
    }

    public void setSendingRegionName(String sendingRegionName) {
        this.sendingRegionName = sendingRegionName;
    }

    @Column(name = "RECEIVER_REGION_NAME")
    public String getReceivingRegionName() {
        return receivingRegionName;
    }

    public void setReceivingRegionName(String receivingRegionName) {
        this.receivingRegionName = receivingRegionName;
    }

    @Column(name = "SENDER_DISTRIBUTOR_ID")
    public Long getSenderDistributorId() {
        return senderDistributorId;
    }

    public void setSenderDistributorId(Long senderDistributorId) {
        this.senderDistributorId = senderDistributorId;
    }

    @Column(name = "SENDER_SERVICE_OP_ID")
    public Long getSenderServiceOPId() {
        return senderServiceOPId;
    }

    public void setSenderServiceOPId(Long senderServiceOPId) {
        this.senderServiceOPId = senderServiceOPId;
    }

    @Column(name = "RECEIVER_DISTRIBUTOR_ID")
    public Long getReceiverDistributorId() {
        return receiverDistributorId;
    }

    public void setReceiverDistributorId(Long receiverDistributorId) {
        this.receiverDistributorId = receiverDistributorId;
    }

    @Column(name = "RECEIVER_SERVICE_OP_ID")
    public Long getReceiverServiceOPId() {
        return receiverServiceOPId;
    }

    public void setReceiverServiceOPId(Long receiverServiceOPId) {
        this.receiverServiceOPId = receiverServiceOPId;
    }

    @Column(name = "SENDER_AREA_NAME")
    public String getSendingAreaName() {
        return sendingAreaName;
    }

    public void setSendingAreaName(String sendingAreaName) {
        this.sendingAreaName = sendingAreaName;
    }

    @Column(name = "RECEIVER_AREA_NAME")
    public String getReceivingAreaName() {
        return receivingAreaName;
    }

    public void setReceivingAreaName(String receivingAreaName) {
        this.receivingAreaName = receivingAreaName;
    }

    @Column(name = "SENDER_AREA_ID")
    public Long getSenderArearId() {
        return senderArearId;
    }

    public void setSenderArearId(Long senderArearId) {
        this.senderArearId = senderArearId;
    }

    @Column(name = "RECEIVER_AREA_ID")
    public Long getReceiverAreaId() {
        return receiverAreaId;
    }

    public void setReceiverAreaId(Long receiverAreaId) {
        this.receiverAreaId = receiverAreaId;
    }

    @Column(name = "SCO_COMMISSION")
    public Double getScoCommission() {
        return scoCommission;
    }

    public void setScoCommission(Double scoCommission) {
        this.scoCommission = scoCommission;
    }

    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Column(name = "NADRA_NIFQ")
    public String getnIfq() {
        return nIfq;
    }

    public void setnIfq(String nIfq) {
        this.nIfq = nIfq;
    }

    @Column(name = "NADRA_MINTAEI_COUNT")
    public String getMintaieCount() {
        return mintaieCount;
    }

    public void setMintaieCount(String mintaieCount) {
        this.mintaieCount = mintaieCount;
    }

    @Column(name = "TRANSACTION_PURPOSE")
    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }

    @Column(name = "BISP_RETRY_COUNT")
    public Long getBispRetryCount() {
        return bispRetryCount;
    }

    public void setBispRetryCount(Long bispRetryCount) {
        this.bispRetryCount = bispRetryCount;
    }

    @Column(name = "RESPONSE_CODE")
    public String getBispResponseCode() {
        return bispResponseCode;
    }

    public void setBispResponseCode(String bispResponseCode) {
        this.bispResponseCode = bispResponseCode;
    }

    @Column(name = "CARD_NO")
    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    @Column(name = "BANK_SHORT_NAME")
    public String getBankShortName() { return bankShortName; }

    public void setBankShortName(String bankShortName) { this.bankShortName = bankShortName; }

    @Column(name = "MAC_ADDRESS")
    public String getMacAddress() { return macAddress; }

    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    @Column(name = "IP_ADDRESS")
    public String getIpAddress() { return ipAddress; }

    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    @Column(name = "LATITUDE")
    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    @Column(name = "LONGITUDE")
    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    @Column(name = "IMEI_NUMBER")
    public String getImeiNumber() { return imeiNumber; }

    public void setImeiNumber(String imeiNumber) { this.imeiNumber = imeiNumber; }

}
