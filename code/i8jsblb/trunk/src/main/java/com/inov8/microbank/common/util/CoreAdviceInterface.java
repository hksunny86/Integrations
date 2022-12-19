package com.inov8.microbank.common.util;

import java.util.Date;

/* This interface is used in CoreAdviceUtil.prepareMiddlewareAdviceVO()
 * This interface is used while pushing CoreAdvice to Queue from two tables:
 * SAF_REPO_CORE (after transaction) & MIDDLEWARE_RETRY_ADVICE_REPORT(Retry from report)
 */
public interface CoreAdviceInterface {

	//IBFT

	public String getBeneAccountTitle();

	public void setBeneAccountTitle(String accountTitle);

	public String getSenderBankImd();

	public void setSenderBankImd(String senderBankImd);

	public String getBeneBankImd();

	public void setBeneBankImd(String beneBankImd);

	public String getCrDr();

	public void setCrDr(String crDr) ;

	public String getBeneIBAN();

	public void setBeneIBAN(String beneIBAN);

	public String getBeneBankName();

	public void setBeneBankName(String beneBankName) ;

	public String getBeneBranchName() ;

	public void setBeneBranchName(String beneBranchName);

	public String getSenderName();

	public void setSenderName(String senderName);

	public String getCardAcceptorNameAndLocation();

	public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) ;

	public String getAgentId();

	public void setAgentId(String agentId);

	public String getPurposeOfPayment();

	public void setPurposeOfPayment(String purposeOfPayment);

	public String getSenderIBAN();

	public void setSenderIBAN(String senderIBAN);

	public Long getProductId();
	public void setProductId(Long productId);
	
	public Long getIntgTransactionTypeId();
	public void setIntgTransactionTypeId(Long intgTransactionTypeId);
	
	public Long getTransactionCodeId();
	public void setTransactionCodeId(Long transactionCodeId);
	
	public String getTransactionCode();
	public void setTransactionCode(String transactionCode);
	
	public String getFromAccount();
	public void setFromAccount(String fromAccount);
	
	public String getToAccount();
	public void setToAccount(String toAccount);
	
	public Double getTransactionAmount();
	public void setTransactionAmount(Double transactionAmount);
	
	public String getStan();
	public void setStan(String stan);
	
	public String getReversalStan();
	public void setReversalStan(String reversalStan);
	
	public String getStatus();
	public void setStatus(String status);
	
	public String getResponseCode();
	public void setResponseCode(String responseCode);
	
	public Date getTransmissionTime();
	public void setTransmissionTime(Date transmissionTime);
	
	public String getReversalRequestTime();
	public void setReversalRequestTime(String reversalRequestTime);
	
	public Date getRequestTime();
	public void setRequestTime(Date requestTime);
	
	public String getAdviceType();
	public void setAdviceType(String adviceType);
	
	public String getBillAggregator();
	public void setBillAggregator(String billAggregator);
	
	public String getCnicNo();
	public void setCnicNo(String cnicNo);
	
	public String getConsumerNo();
	public void setConsumerNo(String consumerNo);
	
	public String getBillCategoryCode();
	public void setBillCategoryCode(String billCategoryCode);
	
	public String getCompnayCode();
	public void setCompnayCode(String compnayCode);
	
	public Long getTransactionId();
	public void setTransactionId(Long transactionId);
	
	public Long getActionLogId();
	public void setActionLogId(Long actionLogId);
	
	public String getRetrievalReferenceNumber();
	public void setRetrievalReferenceNumber(String retrievalReferenceNumber);
	
	public String getUbpBBStan();
	public void setUbpBBStan(String ubpBBStan);
	
}
