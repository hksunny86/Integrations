package com.inov8.integration.vo;

import java.io.Serializable;
import java.util.Date;

public class MiddlewareMessageVO implements Serializable {

    private static final long serialVersionUID = 5824473488070381027L;

    private String rrnKey;
    private String retrievalReferenceNumber;
    private String microbankTransactionCode;
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
    private String ubpSTAN;

    private Long parentTransactionId;
    private String reversalSTAN;
    private String reversalRequestTime;

    //Request by Kashif
    private Date timeLocalTransaction;
    private Date dateLocalTransaction;

    private String bankIMD;
    //added by ahsan raza debit card for js
    private String merchantType;
    private String posEntryMode;
    private String accquireIdentificationCode;
    private String cardAcceptorNameAndLocation;
    private String recievingInstitutionCode;
    private String transactionCurrenyCode;
    private String terminalId;
    private String responseContentXML;
    private String orignalStan;
    private String orignalTransactionDateTime;
    private String amountCardHolderBilling;

    public String getAmountCardHolderBilling() {
        return amountCardHolderBilling;
    }

    public void setAmountCardHolderBilling(String amountCardHolderBilling) {
        this.amountCardHolderBilling = amountCardHolderBilling;
    }

    public String getOrignalStan() {
        return orignalStan;
    }

    public void setOrignalStan(String orignalStan) {
        this.orignalStan = orignalStan;
    }

    public String getOrignalTransactionDateTime() {
        return orignalTransactionDateTime;
    }

    public void setOrignalTransactionDateTime(String orignalTransactionDateTime) {
        this.orignalTransactionDateTime = orignalTransactionDateTime;
    }

    public String getResponseContentXML() {
        return responseContentXML;
    }

    public void setResponseContentXML(String responseContentXML) {
        this.responseContentXML = responseContentXML;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getRecievingInstitutionCode() {
        return recievingInstitutionCode;
    }

    public void setRecievingInstitutionCode(String recievingInstitutionCode) {
        this.recievingInstitutionCode = recievingInstitutionCode;
    }

    public String getTransactionCurrenyCode() {
        return transactionCurrenyCode;
    }

    public void setTransactionCurrenyCode(String transactionCurrenyCode) {
        this.transactionCurrenyCode = transactionCurrenyCode;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }

    public String getAccquireIdentificationCode() {
        return accquireIdentificationCode;
    }

    public void setAccquireIdentificationCode(String accquireIdentificationCode) {
        this.accquireIdentificationCode = accquireIdentificationCode;
    }

    public Date getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(Date timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public Date getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(Date dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

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

    public String getRrnKey() {
        return rrnKey;
    }

    public void setRrnKey(String rrnKey) {
        this.rrnKey = rrnKey;
    }

    public String getUbpSTAN() {
        return ubpSTAN;
    }

    public void setUbpSTAN(String ubpSTAN) {
        this.ubpSTAN = ubpSTAN;
    }


    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    @Override
    public String toString() {
        return "MiddlewareMessageVO [rrnKey=" + rrnKey
                + ", retrievalReferenceNumber=" + retrievalReferenceNumber
                + ", microbankTransactionCode=" + microbankTransactionCode
                + ", transmissionTime=" + transmissionTime + ", requestTime="
                + requestTime + ", responseCode=" + responseCode + ", stan="
                + stan + ", PAN=" + PAN + ", transactionAmount="
                + transactionAmount + ", settlemetAmount=" + settlemetAmount
                + ", transactionFee=" + transactionFee + ", settlemetFee="
                + settlemetFee + ", settlemetProcessingFee="
                + settlemetProcessingFee + ", accountTitle=" + accountTitle
                + ", accountBalance=" + accountBalance + ", acountType="
                + acountType + ", amountType=" + amountType + ", currencyCode="
                + currencyCode + ", balanceType=" + balanceType
                + ", accountNo1=" + accountNo1 + ", accountNo2=" + accountNo2
                + ", cnicNo=" + cnicNo + ", consumerNo=" + consumerNo
                + ", consumerName=" + consumerName + ", compnayCode="
                + compnayCode + ", billDueDate=" + billDueDate
                + ", billAggregator=" + billAggregator + ", billStatus="
                + billStatus + ", billingMonth=" + billingMonth
                + ", amountDueDate=" + amountDueDate + ", amountAfterDueDate="
                + amountAfterDueDate + ", billCategoryId=" + billCategoryId
                + ", ubpSTAN=" + ubpSTAN + ", parentTransactionId="
                + parentTransactionId + ", reversalSTAN=" + reversalSTAN
                + ", reversalRequestTime=" + reversalRequestTime
                + ", timeLocalTransaction=" + timeLocalTransaction
                + ", dateLocalTransaction=" + dateLocalTransaction
                + ", responseContentXml=" + responseContentXML
                + ", bankIMD=" + bankIMD
                + ",OrignalStan=" + orignalStan
                + ".OrignalTransactionDateTime=" + orignalTransactionDateTime +
                ".CardHolderBillingAmount=" + amountCardHolderBilling +
                "]";
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }


}
