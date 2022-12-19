package com.inov8.microbank.server.service.integration.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MiddlewareAdviceVO implements Serializable{
    private static final long serialVersionUID = -1121652379255326975L;

    private String retrievalReferenceNumber;
    private String microbankTransactionCode;
    private Long MicrobankTransactionCodeId;
    private Date transmissionTime;
    private Date requestTime;

    private String responseCode;
    private String stan;
    private String PAN;
    private String transactionAmount;
    private String settlemetAmount;

    private String transactionFee;
    private String settlemetFee;
    private String settlemetProcessingFee;

    private String accountTitle;
    private String accountBalance;
    private String acountType;
    private String amountType;
    private String currencyCode;
    private String balanceType;

    private String accountNo1;
    private String accountNo2;

    private String cnicNo;
    private String consumerNo;
    private String consumerName;
    private String compnayCode;
    private Date billDueDate;
    private String billAggregator;
    private String billStatus;
    private String billingMonth;
    private String amountDueDate;
    private String amountAfterDueDate;
    private String billCategoryId;
    private String ubpStan;

    private Long parentTransactionId;
    private String reversalSTAN;
    private String reversalRequestTime;
    private Date dateTimeLocalTransaction;

    private Map<String, Object> dataMap = new HashMap<String, Object>(0);

    //switchWrapper related properties
    Long intgTransactionTypeId;
    Long productId;
    Boolean isCreditAdvice;
    Boolean isBillPaymentRequest = Boolean.FALSE;

    private String adviceType;
    private String bankIMD;

    private String vehicleRegNumber;
    private String vehicleChesisNumber;
    private String exciseAssessmentNumber;
    private String exciseAssessmentTotalAmount;

    //IBFT
    private String senderBankImd;
    private String crDr;
    private String senderIBAN;
    private String beneIBAN;
    private String beneBankName;
    private String beneBranchName;
    private String senderName;
    private String cardAcceptorNameAndLocation;
    private String agentId;
    private String purposeOfPayment;



    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSettlemetAmount() {
        return settlemetAmount;
    }

    public void setSettlemetAmount(String settlemetAmount) {
        this.settlemetAmount = settlemetAmount;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getSettlemetFee() {
        return settlemetFee;
    }

    public void setSettlemetFee(String settlemetFee) {
        this.settlemetFee = settlemetFee;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getCompnayCode() {
        return compnayCode;
    }

    public void setCompnayCode(String compnayCode) {
        this.compnayCode = compnayCode;
    }

    public String getSettlemetProcessingFee() {
        return settlemetProcessingFee;
    }

    public void setSettlemetProcessingFee(String settlemetProcessingFee) {
        this.settlemetProcessingFee = settlemetProcessingFee;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Date getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }

    public String getBillAggregator() {
        return billAggregator;
    }

    public void setBillAggregator(String billAggregator) {
        this.billAggregator = billAggregator;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getReversalSTAN() {
        return reversalSTAN;
    }

    public void setReversalSTAN(String reversalSTAN) {
        this.reversalSTAN = reversalSTAN;
    }

    public String getReversalRequestTime() {
        return reversalRequestTime;
    }

    public void setReversalRequestTime(String reversalRequestTime) {
        this.reversalRequestTime = reversalRequestTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public Date getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(Date transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    @Override
    public String toString() {
        return "MiddlewareMessageVO [retrievalReferenceNumber=" + retrievalReferenceNumber + ", microbankTransactionCode=" + microbankTransactionCode
                + ", transmissionTime=" + transmissionTime + ", responseCode=" + responseCode + ", stan=" + stan + ", PAN=" + PAN + ", transactionAmount="
                + transactionAmount + ", settlemetAmount=" + settlemetAmount + ", transactionFee=" + transactionFee + ", settlemetFee=" + settlemetFee
                + ", settlemetProcessingFee=" + settlemetProcessingFee + ", accountTitle=" + accountTitle + ", accountNo1=" + accountNo1 + ", accountNo2="
                + accountNo2 + ", cnicNo=" + cnicNo + ", consumerNo=" + consumerNo + ", consumerName=" + consumerName + ", compnayCode=" + compnayCode
                + ", billDueDate=" + billDueDate + ", billAggregator=" + billAggregator + ", reversalSTAN=" + reversalSTAN + ", reversalRequestTime="
                + reversalRequestTime + "]";
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getAmountDueDate() {
        return amountDueDate;
    }

    public void setAmountDueDate(String amountDueDate) {
        this.amountDueDate = amountDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getBillCategoryId() {
        return billCategoryId;
    }

    public void setBillCategoryId(String billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public Long getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(Long parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public String getAcountType() {
        return acountType;
    }

    public void setAcountType(String acountType) {
        this.acountType = acountType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Long getIntgTransactionTypeId() {
        return intgTransactionTypeId;
    }

    public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
        this.intgTransactionTypeId = intgTransactionTypeId;
    }

    public Long getMicrobankTransactionCodeId() {
        return MicrobankTransactionCodeId;
    }

    public void setMicrobankTransactionCodeId(Long microbankTransactionCodeId) {
        MicrobankTransactionCodeId = microbankTransactionCodeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getIsCreditAdvice() {
        return isCreditAdvice;
    }

    public void setIsCreditAdvice(Boolean isCreditAdvice) {
        this.isCreditAdvice = isCreditAdvice;
    }

    public Boolean getIsBillPaymentRequest() {
        return this.isBillPaymentRequest;
    }

    public void setIsBillPaymentRequest(Boolean isBillPaymentRequest) {
        this.isBillPaymentRequest = isBillPaymentRequest;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Date getDateTimeLocalTransaction() {
        return dateTimeLocalTransaction;
    }

    public void setDateTimeLocalTransaction(Date dateTimeLocalTransaction) {
        this.dateTimeLocalTransaction = dateTimeLocalTransaction;
    }

    public String getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(String adviceType) {
        this.adviceType = adviceType;
    }

    public String getUbpStan() {
        return ubpStan;
    }

    public void setUbpStan(String ubpStan) {
        this.ubpStan = ubpStan;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public String getVehicleChesisNumber() {
        return vehicleChesisNumber;
    }

    public void setVehicleChesisNumber(String vehicleChesisNumber) {
        this.vehicleChesisNumber = vehicleChesisNumber;
    }

    public String getExciseAssessmentNumber() {
        return exciseAssessmentNumber;
    }

    public void setExciseAssessmentNumber(String exciseAssessmentNumber) {
        this.exciseAssessmentNumber = exciseAssessmentNumber;
    }

    public String getExciseAssessmentTotalAmount() {
        return exciseAssessmentTotalAmount;
    }

    public void setExciseAssessmentTotalAmount(String exciseAssessmentTotalAmount) {
        this.exciseAssessmentTotalAmount = exciseAssessmentTotalAmount;
    }

    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    public String getCrDr() {
        return crDr;
    }

    public void setCrDr(String crDr) {
        this.crDr = crDr;
    }

    public String getBeneIBAN() {
        return beneIBAN;
    }

    public void setBeneIBAN(String beneIBAN) {
        this.beneIBAN = beneIBAN;
    }

    public String getBeneBankName() {
        return beneBankName;
    }

    public void setBeneBankName(String beneBankName) {
        this.beneBankName = beneBankName;
    }

    public String getBeneBranchName() {
        return beneBranchName;
    }

    public void setBeneBranchName(String beneBranchName) {
        this.beneBranchName = beneBranchName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }


}
