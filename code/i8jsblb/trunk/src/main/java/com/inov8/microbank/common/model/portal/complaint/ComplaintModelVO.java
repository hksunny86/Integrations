package com.inov8.microbank.common.model.portal.complaint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.ComplaintReportModel;

public class ComplaintModelVO implements Serializable {
	
	private static final long serialVersionUID = 3610096515008770731L;

	//Utility Fields
	private String serviceProviderType;
	private String serviceProviderName;
	private String consumerNumber;
	private Date paymentDate;
	private Double paymentAmount;
	private String utilityTrxId;
	
	//FT Fields
	private String transactionId;
	private Date transactionDate;
	private Double amountTransferred;
	private String senderMSISDN;
	private String senderAccountNo;
	private String senderCNIC;
	private String recipientMSISDN;
	private String recipientAccountNo;
	private String recipientCNIC;

	//Agent Fields
	private String agentLocation;
	private String agentId;
	private String shopName;
	private String shopAddress;
	private String agentTransactionId;
	private Date   agentTransactionDate;
	private Double agentPaymentAmount;
	private String senderAgentMSISDN;
	private String receiverAgentMSISDN;

	//BB Account Issues
	private Date   bbAccountDate;
	private String bbCustomerMSISDN;
	private String bbTransactionId;
	private Double bbAmountTransferred;
	
	//TopUp
	private String topUpTrxId;
	private String topUpMobileNo;
	private Date topUpDate;
	private Double topUpAmount;

	//ChargeBack
	private String cbTrxId;
	private Date cbTrxDate;
	private Double cbAmount;
	private String cbSenderMSISDN;
	private String cbSenderCNIC;
	private String cbRecipientMSISDN;
	private String cbRecipientCNIC;

	private Long complaintCategoryId;
	private Long complaintSubcategoryId;
	private String appUserId;
	private String initiatorId;
    private String initiatorFirstName;
    private String initiatorLastName;
    private String initiatorMobileNo;
    private String initiatorNIC;
    private String initiatorCity;
	private String otherContactNo;
    private String complaintDescription;
    
	
    private Boolean isAgent;
    private Boolean isCustomer;
    private Boolean isWalkin;
    private Boolean isHandler;
	
    private List<ComplaintReportModel> oldComplaints = null;
    
	public String getServiceProviderType() {
		return serviceProviderType;
	}
	public void setServiceProviderType(String serviceProviderType) {
		this.serviceProviderType = serviceProviderType;
	}
	public String getServiceProviderName() {
		return serviceProviderName;
	}
	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}
	public String getConsumerNumber() {
		return consumerNumber;
	}
	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
    public String getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}

	public Boolean getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}

	public Boolean getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	public Boolean getIsWalkin() {
		return isWalkin;
	}

	public void setIsWalkin(Boolean isWalkin) {
		this.isWalkin = isWalkin;
	}

	public Long getComplaintCategoryId() {
		return complaintCategoryId;
	}
	public void setComplaintCategoryId(Long complaintCategoryId) {
		this.complaintCategoryId = complaintCategoryId;
	}
	public Long getComplaintSubcategoryId() {
		return complaintSubcategoryId;
	}
	public void setComplaintSubcategoryId(Long complaintSubcategoryId) {
		this.complaintSubcategoryId = complaintSubcategoryId;
	}
	public String getInitiatorFirstName() {
		return initiatorFirstName;
	}
	public void setInitiatorFirstName(String initiatorFirstName) {
		this.initiatorFirstName = initiatorFirstName;
	}
	public String getInitiatorLastName() {
		return initiatorLastName;
	}
	public void setInitiatorLastName(String initiatorLastName) {
		this.initiatorLastName = initiatorLastName;
	}
	public String getInitiatorMobileNo() {
		return initiatorMobileNo;
	}
	public void setInitiatorMobileNo(String initiatorMobileNo) {
		this.initiatorMobileNo = initiatorMobileNo;
	}
	public String getInitiatorNIC() {
		return initiatorNIC;
	}
	public void setInitiatorNIC(String initiatorNIC) {
		this.initiatorNIC = initiatorNIC;
	}
	public String getInitiatorCity() {
		return initiatorCity;
	}
	public void setInitiatorCity(String initiatorCity) {
		this.initiatorCity = initiatorCity;
	}
	public String getOtherContactNo() {
		return otherContactNo;
	}
	public void setOtherContactNo(String otherContactNo) {
		this.otherContactNo = otherContactNo;
	}
	public String getComplaintDescription() {
		return complaintDescription;
	}
	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}
    public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getRecipientMSISDN() {
		return recipientMSISDN;
	}
	public void setRecipientMSISDN(String recipientMSISDN) {
		this.recipientMSISDN = recipientMSISDN;
	}
	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}
	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}
	public String getSenderMSISDN() {
		return senderMSISDN;
	}
	public void setSenderMSISDN(String senderMSISDN) {
		this.senderMSISDN = senderMSISDN;
	}
	public String getSenderAccountNo() {
		return senderAccountNo;
	}
	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAgentLocation() {
		return agentLocation;
	}
	public void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	public String getAgentTransactionId() {
		return agentTransactionId;
	}
	public void setAgentTransactionId(String agentTransactionId) {
		this.agentTransactionId = agentTransactionId;
	}
	public String getUtilityTrxId() {
		return utilityTrxId;
	}
	public void setUtilityTrxId(String utilityTrxId) {
		this.utilityTrxId = utilityTrxId;
	}
	public Double getAmountTransferred() {
		return amountTransferred;
	}
	public void setAmountTransferred(Double amountTransferred) {
		this.amountTransferred = amountTransferred;
	}
	public String getSenderCNIC() {
		return senderCNIC;
	}
	public void setSenderCNIC(String senderCNIC) {
		this.senderCNIC = senderCNIC;
	}
	public String getRecipientCNIC() {
		return recipientCNIC;
	}
	public void setRecipientCNIC(String recipientCNIC) {
		this.recipientCNIC = recipientCNIC;
	}
	public Double getAgentPaymentAmount() {
		return agentPaymentAmount;
	}
	public void setAgentPaymentAmount(Double agentPaymentAmount) {
		this.agentPaymentAmount = agentPaymentAmount;
	}
	public Date getAgentTransactionDate() {
		return agentTransactionDate;
	}
	public void setAgentTransactionDate(Date agentTransactionDate) {
		this.agentTransactionDate = agentTransactionDate;
	}
	public String getSenderAgentMSISDN() {
		return senderAgentMSISDN;
	}
	public void setSenderAgentMSISDN(String senderAgentMSISDN) {
		this.senderAgentMSISDN = senderAgentMSISDN;
	}
	public String getReceiverAgentMSISDN() {
		return receiverAgentMSISDN;
	}
	public void setReceiverAgentMSISDN(String receiverAgentMSISDN) {
		this.receiverAgentMSISDN = receiverAgentMSISDN;
	}
	public Date getBbAccountDate() {
		return bbAccountDate;
	}
	public void setBbAccountDate(Date bbAccountDate) {
		this.bbAccountDate = bbAccountDate;
	}
	public String getBbCustomerMSISDN() {
		return bbCustomerMSISDN;
	}
	public void setBbCustomerMSISDN(String bbCustomerMSISDN) {
		this.bbCustomerMSISDN = bbCustomerMSISDN;
	}
	public String getBbTransactionId() {
		return bbTransactionId;
	}
	public void setBbTransactionId(String bbTransactionId) {
		this.bbTransactionId = bbTransactionId;
	}
	public Double getBbAmountTransferred() {
		return bbAmountTransferred;
	}
	public void setBbAmountTransferred(Double bbAmountTransferred) {
		this.bbAmountTransferred = bbAmountTransferred;
	}
	public String getTopUpTrxId() {
		return topUpTrxId;
	}
	public void setTopUpTrxId(String topUpTrxId) {
		this.topUpTrxId = topUpTrxId;
	}
	public String getTopUpMobileNo() {
		return topUpMobileNo;
	}
	public void setTopUpMobileNo(String topUpMobileNo) {
		this.topUpMobileNo = topUpMobileNo;
	}
	public Date getTopUpDate() {
		return topUpDate;
	}
	public void setTopUpDate(Date topUpDate) {
		this.topUpDate = topUpDate;
	}
	public Double getTopUpAmount() {
		return topUpAmount;
	}
	public void setTopUpAmount(Double topUpAmount) {
		this.topUpAmount = topUpAmount;
	}
	public String getCbTrxId() {
		return cbTrxId;
	}
	public void setCbTrxId(String cbTrxId) {
		this.cbTrxId = cbTrxId;
	}
	public Date getCbTrxDate() {
		return cbTrxDate;
	}
	public void setCbTrxDate(Date cbTrxDate) {
		this.cbTrxDate = cbTrxDate;
	}
	public Double getCbAmount() {
		return cbAmount;
	}
	public void setCbAmount(Double cbAmount) {
		this.cbAmount = cbAmount;
	}
	public String getCbSenderMSISDN() {
		return cbSenderMSISDN;
	}
	public void setCbSenderMSISDN(String cbSenderMSISDN) {
		this.cbSenderMSISDN = cbSenderMSISDN;
	}
	public String getCbSenderCNIC() {
		return cbSenderCNIC;
	}
	public void setCbSenderCNIC(String cbSenderCNIC) {
		this.cbSenderCNIC = cbSenderCNIC;
	}
	public String getCbRecipientMSISDN() {
		return cbRecipientMSISDN;
	}
	public void setCbRecipientMSISDN(String cbRecipientMSISDN) {
		this.cbRecipientMSISDN = cbRecipientMSISDN;
	}
	public String getCbRecipientCNIC() {
		return cbRecipientCNIC;
	}
	public void setCbRecipientCNIC(String cbRecipientCNIC) {
		this.cbRecipientCNIC = cbRecipientCNIC;
	}
	public List<ComplaintReportModel> getOldComplaints() {
		return oldComplaints;
	}
	public void setOldComplaints(List<ComplaintReportModel> oldComplaints) {
		this.oldComplaints = oldComplaints;
	}
	public Boolean getIsHandler() {
		return isHandler;
	}
	public void setIsHandler(Boolean isHandler) {
		this.isHandler = isHandler;
	}

}
