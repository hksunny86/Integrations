package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "VIRTUAL_CARD_SEQUENCE", sequenceName = "VIRTUAL_CARD_SEQUENCE", allocationSize = 2)
@Table(name = "VC_FILE")
public class VCFileModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private long vcFileId;
    private String productType;
    private String valueDate;
    private String programName;
    private String channel;
    private String transactionOrigin;
    private String transactionCode;
    private String transactionType;
    private String deviceType;
    private String deviceNumber;
    private String authorizationNumber;
    private String arn;
    private String transactionCurrency;
    private double transactionAmount;
    private String settlementCurrency;
    private double settleAmount;
    private String billingCurrency;
    private double billingAmount;
    private long conversionRate;
    private Date transactionDate;
    private long markupFee;
    private String cardAcceptorId;
    private String totalFeesCurrency;
    private double totalFeesAndCharges;
    private String reversalFlag;
    private String terminalId;
    private String ecommIndicator;
    private String ucafIndicator;
    private double transactionFees;
    private String taxAmount;
    private String mcc;
    private String toleranceType;
    private long toleranceUnit;
    private String cbsTransactionId;
    private String foreignCurrencyTransaction;
    private String agentEntityName;
    private String branchEntityId;
    private String branceEntityName;
    private String agencyName;
    private String walletNumber;
    private String traceAuditNumber;
    private String merchantNameAndLocation;
    private String settlementDate;
    private String multiClearingIndicator;
    private String multiClearingTotalAmount;
    private String multiClearingSeqNo;
    private String multiClearingSeqCount;
    private String drCrtoCardHolder;
    private String dccPreConversionAmount;
    private String dccPreConversionCurrency;
    private String corporateName;
    private String transactionOriginatedForm;
    private String deviceNumberAlias;
    private String memo;
    private String virtualCardNumber;
    private String cashbackAmount;
    private String cashbackCurrency;
    private String partialApproval;
    private String cardPackId;
    private String corporateId;
    private String travelType;
    private String tokenTransactionType;
    private String loyaltyPointsEarned;
    private Long isCompleted;
    private Long isMatched;
    private String microbankTransactionCode;
    private Date createdOnEndDate;
    private Date createdOnStartDate;
    private Date start;
    private Date end;
    private Double runBal;
    private String mobileNo;

    public VCFileModel() {
    }

    @Transient
    public Long getPrimaryKey() {
        return getVcFileId();
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setVcFileId(primaryKey);

    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "vcFileId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&vcFileId=" + getVcFileId();
        return parameters;
    }

    @javax.persistence.Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }

    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }

    @javax.persistence.Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VIRTUAL_CARD_SEQUENCE")
    @Column(name = "VC_FILE_ID")
    public long getVcFileId() {
        return vcFileId;
    }

    public void setVcFileId(long vcFileId) {
        this.vcFileId = vcFileId;
    }

    @Column(name = "PRODUCT_TYPE")
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Column(name = "MOBILE_NUMBER")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "VALUE_DATE")
    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    @Column(name = "PROGRAM_NAME")
    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Column(name = "CHANNEL")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Column(name = "TRANSACTION_ORIGIN")
    public String getTransactionOrigin() {
        return transactionOrigin;
    }

    public void setTransactionOrigin(String transactionOrigin) {
        this.transactionOrigin = transactionOrigin;
    }

    @Column(name = "TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = "DEVICE_TYPE")
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Column(name = "DEVICE_NUMBER")
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Column(name = "AUTHORIZATION_NUMBER")
    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    @Column(name = "ARN")
    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    @Column(name = "TRANSACTION_CURRENCY")
    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "SETTLEMENT_CURRENCY")
    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    @Column(name = "SETTLEMENT_AMOUNT")
    public double getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(double settleAmount) {
        this.settleAmount = settleAmount;
    }

    @Column(name = "BILLING_CURRENCY")
    public String getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(String billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    @Column(name = "BILLING_AMOUNT")
    public double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(double billingAmount) {
        this.billingAmount = billingAmount;
    }

    @Column(name = "CONVERSION_RATE")
    public long getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(long conversionRate) {
        this.conversionRate = conversionRate;
    }


    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;

    }

    @Column(name = "MARKUP_FEE")
    public long getMarkupFee() {
        return markupFee;
    }

    public void setMarkupFee(long markupFee) {
        this.markupFee = markupFee;
    }

    @Column(name = "CARD_ACCEPTOR_ID")
    public String getCardAcceptorId() {
        return cardAcceptorId;
    }

    public void setCardAcceptorId(String cardAcceptorId) {
        this.cardAcceptorId = cardAcceptorId;
    }

    @Column(name = "TOTAL_FEES_CURRENCY")
    public String getTotalFeesCurrency() {
        return totalFeesCurrency;
    }

    public void setTotalFeesCurrency(String totalFeesCurrency) {
        this.totalFeesCurrency = totalFeesCurrency;
    }

    @Column(name = "TOTAL_FEES_AND_CHARGES")
    public double getTotalFeesAndCharges() {
        return totalFeesAndCharges;
    }

    public void setTotalFeesAndCharges(double totalFeesAndCharges) {
        this.totalFeesAndCharges = totalFeesAndCharges;
    }

    @Column(name = "REVERSAL_FLAG")
    public String getReversalFlag() {
        return reversalFlag;
    }

    public void setReversalFlag(String reversalFlag) {
        this.reversalFlag = reversalFlag;
    }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Column(name = "ECOMM_INDICATOR")
    public String getEcommIndicator() {
        return ecommIndicator;
    }

    public void setEcommIndicator(String ecommIndicator) {
        this.ecommIndicator = ecommIndicator;
    }

    @Column(name = "UCAF_INDICATOR")
    public String getUcafIndicator() {
        return ucafIndicator;
    }

    public void setUcafIndicator(String ucafIndicator) {
        this.ucafIndicator = ucafIndicator;
    }

    @Column(name = "TRANSACTION_FEES")
    public double getTransactionFees() {
        return transactionFees;
    }

    public void setTransactionFees(double transactionFees) {
        this.transactionFees = transactionFees;
    }

    @Column(name = "TAX_AMOUNT")
    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Column(name = "MCC")
    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    @Column(name = "TOLERANCE_TYPE")
    public String getToleranceType() {
        return toleranceType;
    }

    public void setToleranceType(String toleranceType) {
        this.toleranceType = toleranceType;
    }

    @Column(name = "TOLERANCE_UNIT")
    public long getToleranceUnit() {
        return toleranceUnit;
    }

    public void setToleranceUnit(long toleranceUnit) {
        this.toleranceUnit = toleranceUnit;
    }

    @Column(name = "CBS_TRANSACTION_ID")
    public String getCbsTransactionId() {
        return cbsTransactionId;
    }

    public void setCbsTransactionId(String cbsTransactionId) {
        this.cbsTransactionId = cbsTransactionId;
    }

    @Column(name = "FOREIGN_CURRENCY_TRANSACTION")
    public String getForeignCurrencyTransaction() {
        return foreignCurrencyTransaction;
    }

    public void setForeignCurrencyTransaction(String foreignCurrencyTransaction) {
        this.foreignCurrencyTransaction = foreignCurrencyTransaction;
    }

    @Column(name = "AGENT_ENTITY_NAME")
    public String getAgentEntityName() {
        return agentEntityName;
    }

    public void setAgentEntityName(String agentEntityName) {
        this.agentEntityName = agentEntityName;
    }

    @Column(name = "BRANCH_ENTITY_ID")
    public String getBranchEntityId() {
        return branchEntityId;
    }

    public void setBranchEntityId(String branchEntityId) {
        this.branchEntityId = branchEntityId;
    }

    @Column(name = "BRANCH_ENTITY_NAME")
    public String getBranceEntityName() {
        return branceEntityName;
    }

    public void setBranceEntityName(String branceEntityName) {
        this.branceEntityName = branceEntityName;
    }

    @Column(name = "AGENCY_NAME")
    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    @Column(name = "WALLET_NUMBER")
    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    @Column(name = "TRACE_AUDIT_NUMBER")
    public String getTraceAuditNumber() {
        return traceAuditNumber;
    }

    public void setTraceAuditNumber(String traceAuditNumber) {
        this.traceAuditNumber = traceAuditNumber;
    }

    @Column(name = "MERCHANT_NAME_AND_LOCATION")
    public String getMerchantNameAndLocation() {
        return merchantNameAndLocation;
    }

    public void setMerchantNameAndLocation(String merchantNameAndLocation) {
        this.merchantNameAndLocation = merchantNameAndLocation;
    }

    @Column(name = "SETTLEMENT_DATE")
    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Column(name = "MULTICLEARING_INDICATOR")
    public String getMultiClearingIndicator() {
        return multiClearingIndicator;
    }

    public void setMultiClearingIndicator(String multiClearingIndicator) {
        this.multiClearingIndicator = multiClearingIndicator;
    }

    @Column(name = "MULTICLEARING_TOTAL_AMOUNT")
    public String getMultiClearingTotalAmount() {
        return multiClearingTotalAmount;
    }

    public void setMultiClearingTotalAmount(String multiClearingTotalAmount) {
        this.multiClearingTotalAmount = multiClearingTotalAmount;
    }

    @Column(name = "MULTICLEARING_SEQ_NO")
    public String getMultiClearingSeqNo() {
        return multiClearingSeqNo;
    }

    public void setMultiClearingSeqNo(String multiClearingSeqNo) {
        this.multiClearingSeqNo = multiClearingSeqNo;
    }

    @Column(name = "MULTICLEARING_SEQ_COUNT")
    public String getMultiClearingSeqCount() {
        return multiClearingSeqCount;
    }

    public void setMultiClearingSeqCount(String multiClearingSeqCount) {
        this.multiClearingSeqCount = multiClearingSeqCount;
    }

    @Column(name = "DR_CR_TO_CARDHOLDER")
    public String getDrCrtoCardHolder() {
        return drCrtoCardHolder;
    }

    public void setDrCrtoCardHolder(String drCrtoCardHolder) {
        this.drCrtoCardHolder = drCrtoCardHolder;
    }

    @Column(name = "DCC_PRE_CONVERSION_AMOUNT")
    public String getDccPreConversionAmount() {
        return dccPreConversionAmount;
    }

    public void setDccPreConversionAmount(String dccPreConversionAmount) {
        this.dccPreConversionAmount = dccPreConversionAmount;
    }

    @Column(name = "DCC_PRE_CONVERSION_CURRENCY")
    public String getDccPreConversionCurrency() {
        return dccPreConversionCurrency;
    }

    public void setDccPreConversionCurrency(String dccPreConversionCurrency) {
        this.dccPreConversionCurrency = dccPreConversionCurrency;
    }

    @Column(name = "CORPORATE_NAME")
    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    @Column(name = "TRANSACTION_ORIGINATED_FROM")
    public String getTransactionOriginatedForm() {
        return transactionOriginatedForm;
    }

    public void setTransactionOriginatedForm(String transactionOriginatedForm) {
        this.transactionOriginatedForm = transactionOriginatedForm;
    }

    @Column(name = "DEVICE_NUMBER_ALIAS")
    public String getDeviceNumberAlias() {
        return deviceNumberAlias;
    }

    public void setDeviceNumberAlias(String deviceNumberAlias) {
        this.deviceNumberAlias = deviceNumberAlias;
    }

    @Column(name = "MEMO")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "VIRTUAL_CARD_NUMBER")
    public String getVirtualCardNumber() {
        return virtualCardNumber;
    }

    public void setVirtualCardNumber(String virtualCardNumber) {
        this.virtualCardNumber = virtualCardNumber;
    }

    @Column(name = "CASHBACK_CURRENCY")
    public String getCashbackCurrency() {
        return cashbackCurrency;
    }

    public void setCashbackCurrency(String cashbackCurrency) {
        this.cashbackCurrency = cashbackCurrency;
    }

    @Column(name = "PARTIAL_APPROVAL")
    public String getPartialApproval() {
        return partialApproval;
    }

    public void setPartialApproval(String partialApproval) {
        this.partialApproval = partialApproval;
    }

    @Column(name = "CARD_PACK_ID")
    public String getCardPackId() {
        return cardPackId;
    }

    public void setCardPackId(String cardPackId) {
        this.cardPackId = cardPackId;
    }

    @Column(name = "CORPORATE_ID")
    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    @Column(name = "TRAVEL_TYPE")
    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    @Column(name = "TOKEN_TRANSACTION_TYPE")
    public String getTokenTransactionType() {
        return tokenTransactionType;
    }

    public void setTokenTransactionType(String tokenTransactionType) {
        this.tokenTransactionType = tokenTransactionType;
    }

    @Column(name = "LOYALTY_POINTS_EARNED")
    public String getLoyaltyPointsEarned() {
        return loyaltyPointsEarned;
    }

    public void setLoyaltyPointsEarned(String loyaltyPointsEarned) {
        this.loyaltyPointsEarned = loyaltyPointsEarned;
    }

    @Column(name = "IS_COMPLETED")
    public Long getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Long isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Column(name = "IS_MATCHED")
    public Long getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(Long isMatched) {
        this.isMatched = isMatched;
    }

    @Column(name = "CASHBACK_AMOUNT")
    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(String cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    @Column(name = "RUN_BAL")
    public Double getRunBal() {
        return runBal;
    }

    public void setRunBal(Double runBal) {
        this.runBal = runBal;
    }

    @Column(name = "MICROBANK_TRANSACTION_CODE")
    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        VCFileModel model = new VCFileModel();
        model.setVcFileId(resultSet.getLong("VC_FILE_ID"));
        model.setProductType(resultSet.getString("PRODUCT_TYPE"));
        model.setValueDate(resultSet.getString("VALUE_DATE"));
        model.setProgramName(resultSet.getString("PROGRAM_NAME"));
        model.setChannel(resultSet.getString("CHANNEL"));
        model.setTransactionOrigin(resultSet.getString("TRANSACTION_ORIGIN"));
        model.setTransactionCode(resultSet.getString("TRANSACTION_CODE"));
        model.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
        model.setDeviceType(resultSet.getString("DEVICE_TYPE"));
        model.setDeviceNumber(resultSet.getString("DEVICE_NUMBER"));
        model.setAuthorizationNumber(resultSet.getString("AUTHORIZATION_NUMBER"));
        model.setArn(resultSet.getString("ARN"));
        model.setTransactionCurrency(resultSet.getString("TRANSACTION_CURRENCY"));
        model.setTransactionAmount(resultSet.getDouble("TRANSACTION_AMOUNT"));
        model.setSettlementCurrency(resultSet.getString("SETTLEMENT_CURRENCY"));
        model.setSettleAmount(resultSet.getDouble("SETTLEMENT_AMOUNT"));
        model.setBillingCurrency(resultSet.getString("BILLING_CURRENCY"));
        model.setBillingAmount(resultSet.getDouble("BILLING_AMOUNT"));
        model.setConversionRate(resultSet.getLong("CONVERSION_RATE"));
        model.setTransactionDate(resultSet.getDate("TRANSACTION_DATE"));
        model.setMarkupFee(resultSet.getLong("MARKUP_FEE"));
        model.setCardAcceptorId(resultSet.getString("CARD_ACCEPTOR_ID"));
        model.setTotalFeesCurrency(resultSet.getString("TOTAL_FEES_CURRENCY"));
        model.setTotalFeesAndCharges(resultSet.getLong("TOTAL_FEES_AND_CHARGES"));
        model.setReversalFlag(resultSet.getString("REVERSAL_FLAG"));
        model.setTerminalId(resultSet.getString("TERMINAL_ID"));
        model.setEcommIndicator(resultSet.getString("ECOMM_INDICATOR"));
        model.setUcafIndicator(resultSet.getString("UCAF_INDICATOR"));
        model.setTransactionFees(resultSet.getLong("TRANSACTION_FEES"));
        model.setTaxAmount(resultSet.getString("TAX_AMOUNT"));
        model.setMcc(resultSet.getString("MCC"));
        model.setToleranceType(resultSet.getString("TOLERANCE_TYPE"));
        model.setToleranceUnit(resultSet.getLong("TOLERANCE_UNIT"));
        model.setCbsTransactionId(resultSet.getString("CBS_TRANSACTION_ID"));
        model.setForeignCurrencyTransaction(resultSet.getString("FOREIGN_CURRENCY_TRANSACTION"));
        model.setAgentEntityName(resultSet.getString("AGENT_ENTITY_NAME"));
        model.setBranchEntityId(resultSet.getString("BRANCH_ENTITY_ID"));
        model.setBranceEntityName(resultSet.getString("BRANCH_ENTITY_NAME"));
        model.setAgencyName(resultSet.getString("AGENCY_NAME"));
        model.setWalletNumber(resultSet.getString("WALLET_NUMBER"));
        model.setTraceAuditNumber(resultSet.getString("TRACE_AUDIT_NUMBER"));
        model.setMerchantNameAndLocation(resultSet.getString("MERCHANT_NAME_AND_LOCATION"));
        model.setSettlementDate(resultSet.getString("SETTLEMENT_DATE"));
        model.setMultiClearingIndicator(resultSet.getString("MULTICLEARING_INDICATOR"));
        model.setMultiClearingTotalAmount(resultSet.getString("MULTICLEARING_TOTAL_AMOUNT"));
        model.setMultiClearingSeqCount(resultSet.getString("MULTICLEARING_SEQ_COUNT"));
        model.setMultiClearingSeqNo(resultSet.getString("MULTICLEARING_SEQ_NO"));
        model.setDrCrtoCardHolder(resultSet.getString("DR_CR_TO_CARDHOLDER"));
        model.setDccPreConversionAmount(resultSet.getString("DCC_PRE_CONVERSION_AMOUNT"));
        model.setDccPreConversionCurrency(resultSet.getString("DCC_PRE_CONVERSION_CURRENCY"));
        model.setCorporateName(resultSet.getString("CORPORATE_NAME"));
        model.setTransactionOriginatedForm(resultSet.getString("TRANSACTION_ORIGINATED_FROM"));
        model.setDeviceNumberAlias(resultSet.getString("DEVICE_NUMBER_ALIAS"));
        model.setMemo(resultSet.getString("MEMO"));
        model.setVirtualCardNumber(resultSet.getString("VIRTUAL_CARD_NUMBER"));
        model.setCashbackAmount(resultSet.getString("CASHBACK_AMOUNT"));
        model.setCashbackCurrency(resultSet.getString("CASHBACK_CURRENCY"));
        model.setPartialApproval(resultSet.getString("PARTIAL_APPROVAL"));
        model.setCardPackId(resultSet.getString("CARD_PACK_ID"));
        model.setCorporateId(resultSet.getString("CORPORATE_ID"));
        model.setTravelType(resultSet.getString("TRAVEL_TYPE"));
        model.setTokenTransactionType(resultSet.getString("TOKEN_TRANSACTION_TYPE"));
        model.setLoyaltyPointsEarned(resultSet.getString("LOYALTY_POINTS_EARNED"));
        model.setIsCompleted(resultSet.getLong("IS_COMPLETED"));
        model.setIsMatched(resultSet.getLong("IS_MATCHED"));
        model.setMicrobankTransactionCode(resultSet.getString("MICROBANK_TRANSACTION_CODE"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        return model;
    }
}
