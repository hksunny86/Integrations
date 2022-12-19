package com.inov8.integration.channel.JSDebitCardApi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"processingCode",
		"traceNo",
		"dateTime",
		"merchantType",
		"messageType",
		"transactionCode",
		"transmissionDateTime",
		"stan",
		"userId",
		"password",
		"channelId",
		"accountNum",
		"acctTypeCode",
		"acctStatusCode",
		"branchCode",
		"currencyCode",
		"accountTitle",
		"odLimitReviewDate",
		"lastFinTranDate",
		"interestFromDat",
		"interestToDate",
		"openedDate",
		"limitProfile1",
		"accountTitleOther",
		"officerCode",
		"availableBalance",
		"ledgerBalance",
		"holdAmount",
		"statementFrequency",
		"lastStatementDate",
		"nextStatementDate",
		"introRelationshipNum",
		"introAddress",
		"introAccountNum",
		"comments",
		"closedDate",
		"closedRemarks",
		"whenDeleted",
		"jointDescription",
		"address1",
		"address2",
		"city",
		"countryCode",
		"zipCode",
		"t1",
		"t2",
		"t3",
		"t4",
		"t5",
		"t6",
		"t7",
		"t8",
		"reserved1",
		"iban",
		"relationshipNum"
		})
public class ImportAccountRequest extends Request implements Serializable {

	private String username =  PropertyReader.getProperty("jsdebitcatrdapi.username");
	private String pass = PropertyReader.getProperty("jsdebitcatrdapi.password");

	@JsonProperty("processingCode")
	private String processingCode;
	@JsonProperty("traceNo")
	private String traceNo;
	@JsonProperty("dateTime")
	private String dateTime;
	@JsonProperty("merchantType")
	private String merchantType;
	@JsonProperty("messageType")
	private String messageType;
	@JsonProperty("transactionCode")
	private String transactionCode;
	@JsonProperty("transmissionDateTime")
	private String transmissionDateTime;
	@JsonProperty("stan")
	private String stan;
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("password")
	private String password;
	@JsonProperty("channelId")
	private String channelId;
	@JsonProperty("accountNum")
	private String accountNum;
	@JsonProperty("acctTypeCode")
	private String acctTypeCode;
	@JsonProperty("acctStatusCode")
	private String acctStatusCode;
	@JsonProperty("branchCode")
	private String branchCode;
	@JsonProperty("currencyCode")
	private String currencyCode;
	@JsonProperty("accountTitle")
	private String accountTitle;
	@JsonProperty("odLimitReviewDate")
	private String odLimitReviewDate;
	@JsonProperty("lastFinTranDate")
	private String lastFinTranDate;
	@JsonProperty("interestFromDat")
	private String interestFromDat;
	@JsonProperty("interestToDate")
	private String interestToDate;
	@JsonProperty("openedDate")
	private String openedDate;
	@JsonProperty("limitProfile1")
	private String limitProfile1;
	@JsonProperty("accountTitleOther")
	private String accountTitleOther;
	@JsonProperty("officerCode")
	private String officerCode;
	@JsonProperty("availableBalance")
	private String availableBalance;
	@JsonProperty("ledgerBalance")
	private String ledgerBalance;
	@JsonProperty("holdAmount")
	private String holdAmount;
	@JsonProperty("statementFrequency")
	private String statementFrequency;
	@JsonProperty("lastStatementDate")
	private String lastStatementDate;
	@JsonProperty("nextStatementDate")
	private String nextStatementDate;
	@JsonProperty("introRelationshipNum")
	private String introRelationshipNum;
	@JsonProperty("introAddress")
	private String introAddress;
	@JsonProperty("introAccountNum")
	private String introAccountNum;
	@JsonProperty("comments")
	private String comments;
	@JsonProperty("closedDate")
	private String closedDate;
	@JsonProperty("closedRemarks ")
	private String closedRemarks;
	@JsonProperty("whenDeleted ")
	private String whenDeleted;
	@JsonProperty("jointDescription ")
	private String jointDescription;
	@JsonProperty("address1")
	private String address1;
	@JsonProperty("address2")
	private String address2;
	@JsonProperty("city")
	private String city;
	@JsonProperty("countryCode")
	private String countryCode;
	@JsonProperty("zipCode")
	private String zipCode;
	@JsonProperty("t1")
	private String t1;
	@JsonProperty("t2")
	private String t2;
	@JsonProperty("t3 ")
	private String t3;
	@JsonProperty("t4")
	private String t4;
	@JsonProperty("t5")
	private String t5;
	@JsonProperty("t6")
	private String t6;
	@JsonProperty("t7")
	private String t7;
	@JsonProperty("t8")
	private String t8;
	@JsonProperty("reserved1")
	private String reserved1;
	@JsonProperty("iban")
	private String iban;
	@JsonProperty("relationshipNum")
	private String relationshipNum;

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransmissionDateTime() {
		return transmissionDateTime;
	}

	public void setTransmissionDateTime(String transmissionDateTime) {
		this.transmissionDateTime = transmissionDateTime;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAcctTypeCode() {
		return acctTypeCode;
	}

	public void setAcctTypeCode(String acctTypeCode) {
		this.acctTypeCode = acctTypeCode;
	}

	public String getAcctStatusCode() {
		return acctStatusCode;
	}

	public void setAcctStatusCode(String acctStatusCode) {
		this.acctStatusCode = acctStatusCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getOdLimitReviewDate() {
		return odLimitReviewDate;
	}

	public void setOdLimitReviewDate(String odLimitReviewDate) {
		this.odLimitReviewDate = odLimitReviewDate;
	}

	public String getLastFinTranDate() {
		return lastFinTranDate;
	}

	public void setLastFinTranDate(String lastFinTranDate) {
		this.lastFinTranDate = lastFinTranDate;
	}

	public String getInterestFromDat() {
		return interestFromDat;
	}

	public void setInterestFromDat(String interestFromDat) {
		this.interestFromDat = interestFromDat;
	}

	public String getInterestToDate() {
		return interestToDate;
	}

	public void setInterestToDate(String interestToDate) {
		this.interestToDate = interestToDate;
	}

	public String getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(String openedDate) {
		this.openedDate = openedDate;
	}

	public String getLimitProfile1() {
		return limitProfile1;
	}

	public void setLimitProfile1(String limitProfile1) {
		this.limitProfile1 = limitProfile1;
	}

	public String getAccountTitleOther() {
		return accountTitleOther;
	}

	public void setAccountTitleOther(String accountTitleOther) {
		this.accountTitleOther = accountTitleOther;
	}

	public String getOfficerCode() {
		return officerCode;
	}

	public void setOfficerCode(String officerCode) {
		this.officerCode = officerCode;
	}

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(String ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public String getHoldAmount() {
		return holdAmount;
	}

	public void setHoldAmount(String holdAmount) {
		this.holdAmount = holdAmount;
	}

	public String getStatementFrequency() {
		return statementFrequency;
	}

	public void setStatementFrequency(String statementFrequency) {
		this.statementFrequency = statementFrequency;
	}

	public String getLastStatementDate() {
		return lastStatementDate;
	}

	public void setLastStatementDate(String lastStatementDate) {
		this.lastStatementDate = lastStatementDate;
	}

	public String getNextStatementDate() {
		return nextStatementDate;
	}

	public void setNextStatementDate(String nextStatementDate) {
		this.nextStatementDate = nextStatementDate;
	}

	public String getIntroRelationshipNum() {
		return introRelationshipNum;
	}

	public void setIntroRelationshipNum(String introRelationshipNum) {
		this.introRelationshipNum = introRelationshipNum;
	}

	public String getIntroAddress() {
		return introAddress;
	}

	public void setIntroAddress(String introAddress) {
		this.introAddress = introAddress;
	}

	public String getIntroAccountNum() {
		return introAccountNum;
	}

	public void setIntroAccountNum(String introAccountNum) {
		this.introAccountNum = introAccountNum;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}

	public String getClosedRemarks() {
		return closedRemarks;
	}

	public void setClosedRemarks(String closedRemarks) {
		this.closedRemarks = closedRemarks;
	}

	public String getWhenDeleted() {
		return whenDeleted;
	}

	public void setWhenDeleted(String whenDeleted) {
		this.whenDeleted = whenDeleted;
	}

	public String getJointDescription() {
		return jointDescription;
	}

	public void setJointDescription(String jointDescription) {
		this.jointDescription = jointDescription;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getT4() {
		return t4;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public String getT5() {
		return t5;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

	public String getT6() {
		return t6;
	}

	public void setT6(String t6) {
		this.t6 = t6;
	}

	public String getT7() {
		return t7;
	}

	public void setT7(String t7) {
		this.t7 = t7;
	}

	public String getT8() {
		return t8;
	}

	public void setT8(String t8) {
		this.t8 = t8;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getRelationshipNum() {
		return relationshipNum;
	}

	public void setRelationshipNum(String relationshipNum) {
		this.relationshipNum = relationshipNum;
	}

	@Override
	public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

		this.setProcessingCode("ImportAccount");
		this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
		this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
		this.setMerchantType("0088");
		this.setMessageType("0200");
		this.setTransactionCode("402");
		this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
		this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
		this.setUserId(username);
		this.setPassword(pass);
		this.setChannelId("00");
		this.setAccountNum(i8SBSwitchControllerRequestVO.getAccountNumber());
		this.setAcctTypeCode(i8SBSwitchControllerRequestVO.getAccountTypeCode());
		this.setAcctStatusCode(i8SBSwitchControllerRequestVO.getAccountStatusCode());
		this.setBranchCode(i8SBSwitchControllerRequestVO.getBranchCode());
		this.setCurrencyCode(i8SBSwitchControllerRequestVO.getCurrencyCode());
		this.setAccountTitle(i8SBSwitchControllerRequestVO.getAccountTitle());
		this.setOdLimitReviewDate(i8SBSwitchControllerRequestVO.getOdLimitReviewDate());
		this.setLastFinTranDate(i8SBSwitchControllerRequestVO.getLastFinTranDate());
		this.setInterestFromDat(i8SBSwitchControllerRequestVO.getInterestFromDate());
		this.setInterestToDate(i8SBSwitchControllerRequestVO.getInterestToDate());
		this.setOpenedDate(i8SBSwitchControllerRequestVO.getOpenedDate());
		this.setLimitProfile1(i8SBSwitchControllerRequestVO.getLimitProfile1());
		this.setAccountTitleOther(i8SBSwitchControllerRequestVO.getAccountTitleOther());
		this.setOfficerCode(i8SBSwitchControllerRequestVO.getOfficerCode());
		this.setAvailableBalance(i8SBSwitchControllerRequestVO.getAvailableBalance());
		this.setLedgerBalance(i8SBSwitchControllerRequestVO.getLedgerBalance());
		this.setHoldAmount(i8SBSwitchControllerRequestVO.getHoldAmount());
		this.setStatementFrequency(i8SBSwitchControllerRequestVO.getStatementFrequency());
		this.setLastStatementDate(i8SBSwitchControllerRequestVO.getLastStatementDate());
		this.setNextStatementDate(i8SBSwitchControllerRequestVO.getNextStatementDate());
		this.setIntroRelationshipNum(i8SBSwitchControllerRequestVO.getIntroRelationshipNum());
		this.setIntroAddress(i8SBSwitchControllerRequestVO.getIntroAddress());
		this.setIntroAccountNum(i8SBSwitchControllerRequestVO.getIntroAccountNum());
		this.setComments(i8SBSwitchControllerRequestVO.getComments());
		this.setClosedDate(i8SBSwitchControllerRequestVO.getClosedDate());
		this.setClosedRemarks(i8SBSwitchControllerRequestVO.getClosedRemarks());
		this.setWhenDeleted(i8SBSwitchControllerRequestVO.getWhenDeleted());
		this.setJointDescription(i8SBSwitchControllerRequestVO.getJointDescription());
		this.setAddress1(i8SBSwitchControllerRequestVO.getAddress1());
		this.setAddress2(i8SBSwitchControllerRequestVO.getAddress2());
		this.setCity(i8SBSwitchControllerRequestVO.getCity());
		this.setCountryCode(i8SBSwitchControllerRequestVO.getCountryCode());
		this.setZipCode(i8SBSwitchControllerRequestVO.getZipCode());
		this.setT1(i8SBSwitchControllerRequestVO.getT1());
		this.setT2(i8SBSwitchControllerRequestVO.getT2());
		this.setT3(i8SBSwitchControllerRequestVO.getT3());
		this.setT4(i8SBSwitchControllerRequestVO.getT4());
		this.setT5(i8SBSwitchControllerRequestVO.getT5());
		this.setT6(i8SBSwitchControllerRequestVO.getT6());
		this.setT7(i8SBSwitchControllerRequestVO.getT7());
		this.setT8(i8SBSwitchControllerRequestVO.getT8());
		this.setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
		this.setIban(i8SBSwitchControllerRequestVO.getIban());
		this.setRelationshipNum(i8SBSwitchControllerRequestVO.getRelationshipNumber());

	}

	@Override
	public boolean validateRequest() throws I8SBValidationException {

		if (StringUtils.isEmpty(this.getProcessingCode())) {
			throw new I8SBValidationException("[Failed] Processing Code:" + this.getProcessingCode());
		}
		if (StringUtils.isEmpty(this.getTraceNo())) {
			throw new I8SBValidationException("[Failed] Trace No:" + this.getTraceNo());
		}
		if (StringUtils.isEmpty(this.getMerchantType())) {
			throw new I8SBValidationException("[Failed] Merchant Type:" + this.getMerchantType());
		}
		if (StringUtils.isEmpty(this.getMessageType())) {
			throw new I8SBValidationException("[Failed] Message Type:" + this.getMessageType());
		}
		if (StringUtils.isEmpty(this.getTransactionCode())) {
			throw new I8SBValidationException("[Failed] Transaction Code:" + this.getTransactionCode());
		}
		if (StringUtils.isEmpty(this.getTransmissionDateTime())) {
			throw new I8SBValidationException("[Failed] Transmission Date Time :" + this.getTransmissionDateTime());
		}
		if (StringUtils.isEmpty(this.getStan())) {
			throw new I8SBValidationException("[Failed] STAN :" + this.getStan());
		}
		if (StringUtils.isEmpty(this.getChannelId())) {
			throw new I8SBValidationException("[Failed] Channel ID:" + this.getChannelId());
		}

		return true;
	}

	@Override
	public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

	}
}
