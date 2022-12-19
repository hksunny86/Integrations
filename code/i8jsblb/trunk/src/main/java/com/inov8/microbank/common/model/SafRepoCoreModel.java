package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CoreAdviceInterface;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SAF_REPO_CORE_seq", sequenceName = "SAF_REPO_CORE_seq", allocationSize = 1)
@Table(name = "SAF_REPO_CORE")
public class SafRepoCoreModel extends BasePersistableModel implements Serializable, CoreAdviceInterface{
	
	private static final long serialVersionUID = 6001343621547585982L;
	
	private Long safRepoCoreId;
	private Long productId;
	private Long intgTransactionTypeId;
	private Long transactionCodeId;
	private String transactionCode;
	private String fromAccount;
	private String toAccount;
	private Double transactionAmount;
	private String stan;
	private String reversalStan;
	private String status;
	private String responseCode;
	private Date transmissionTime;
	private String reversalRequestTime;
	private Date requestTime;
	private String adviceType;
	private String billAggregator;
	private String cnicNo;
	private String consumerNo;
	private String billCategoryCode;
	private String compnayCode;
	private Long transactionId;
	private Long actionLogId;
	private String retrievalReferenceNumber;
	private String ubpBBStan;

	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

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
	private String beneAccountTitle;
	private String beneBankImd;

	@Column(name = "SAF_REPO_CORE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAF_REPO_CORE_seq")
	public Long getSafRepoCoreId() {
		return safRepoCoreId;
	}

	public void setSafRepoCoreId(Long safRepoCoreId) {
		this.safRepoCoreId = safRepoCoreId;
	}

	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return null;
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "safRepoCoreId";
		return primaryKeyFieldName;
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&safRepoCoreId=" + getSafRepoCoreId();
		return parameters;
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setSafRepoCoreId(primaryKey);
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return getRelationUpdatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationUpdatedByAppUserModel((AppUserModel) appUserModel.clone());
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
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

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if (appUserId == null) {
			updatedByAppUserModel = null;
		} else {
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (createdByAppUserModel != null) {
			return createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByAppUserModel = null;
		} else {
			createdByAppUserModel = new AppUserModel();
			createdByAppUserModel.setAppUserId(appUserId);
		}
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());

		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "INTG_TRANSACTION_TYPE_ID")
	public Long getIntgTransactionTypeId() {
		return intgTransactionTypeId;
	}

	public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
		this.intgTransactionTypeId = intgTransactionTypeId;
	}

	@Column(name = "TRANSACTION_CODE_ID")
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "FROM_ACCOUNT")
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	@Column(name = "TO_ACCOUNT")
	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	@Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "STAN")
	public String getStan() {
		return stan;
	}
	
	public void setStan(String stan) {
		this.stan = stan;
	}

	@Column(name = "REVERSAL_STAN")
	public String getReversalStan() {
		return reversalStan;
	}

	public void setReversalStan(String reversalStan) {
		this.reversalStan = reversalStan;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "TRANSMISSION_TIME")
	public Date getTransmissionTime() {
		return transmissionTime;
	}

	public void setTransmissionTime(Date transmissionTime) {
		this.transmissionTime = transmissionTime;
	}

	@Column(name = "REVERSAL_REQUEST_TIME")
	public String getReversalRequestTime() {
		return reversalRequestTime;
	}

	public void setReversalRequestTime(String reversalRequestTime) {
		this.reversalRequestTime = reversalRequestTime;
	}

	@Column(name = "REQUEST_TIME")
	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	@Column(name = "ADVICE_TYPE")
	public String getAdviceType() {
		return adviceType;
	}

	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}

	@Column(name = "BILL_AGGREGATOR")
	public String getBillAggregator() {
		return billAggregator;
	}

	public void setBillAggregator(String billAggregator) {
		this.billAggregator = billAggregator;
	}

	@Column(name = "CNIC")
	public String getCnicNo() {
		return cnicNo;
	}

	public void setCnicNo(String cnicNo) {
		this.cnicNo = cnicNo;
	}

	@Column(name = "CONSUMER_NUMBER")
	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}

	@Column(name = "BILL_CATEGORY_CODE")
	public String getBillCategoryCode() {
		return billCategoryCode;
	}

	public void setBillCategoryCode(String billCategoryCode) {
		this.billCategoryCode = billCategoryCode;
	}

	@Column(name = "BILL_COMPANY_CODE")
	public String getCompnayCode() {
		return compnayCode;
	}

	public void setCompnayCode(String compnayCode) {
		this.compnayCode = compnayCode;
	}

	@Column(name = "TRANSACTION_ID")
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "ACTION_LOG_ID")
	public Long getActionLogId() {
		return actionLogId;
	}

	public void setActionLogId(Long actionLogId) {
		this.actionLogId = actionLogId;
	}

	@Column(name = "RRN")
	public String getRetrievalReferenceNumber() {
		return retrievalReferenceNumber;
	}

	public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
		this.retrievalReferenceNumber = retrievalReferenceNumber;
	}

	@Column(name = "UBP_BB_STAN")
	public String getUbpBBStan() {
		return ubpBBStan;
	}

	public void setUbpBBStan(String ubpBBStan) {
		this.ubpBBStan = ubpBBStan;
	}

	@Column(name = "SENDER_BANK_IMD")
	public String getSenderBankImd() {
		return senderBankImd;
	}

	public void setSenderBankImd(String senderBankImd) {
		this.senderBankImd = senderBankImd;
	}

	@Column(name = "CR_DR")
	public String getCrDr() {
		return crDr;
	}

	public void setCrDr(String crDr) {
		this.crDr = crDr;
	}

	@Column(name = "BENE_IBAN")
	public String getBeneIBAN() {
		return beneIBAN;
	}

	public void setBeneIBAN(String beneIBAN) {
		this.beneIBAN = beneIBAN;
	}

	@Column(name = "BENE_BANK_NAME")
	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	@Column(name = "BENE_BRANCH_NAME")
	public String getBeneBranchName() {
		return beneBranchName;
	}

	public void setBeneBranchName(String beneBranchName) {
		this.beneBranchName = beneBranchName;
	}

	@Column(name = "SENDER_NAME")
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@Column(name = "ACCEPTOR_DETAILS")
	public String getCardAcceptorNameAndLocation() {
		return cardAcceptorNameAndLocation;
	}

	public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
		this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
	}

	@Column(name = "AGENT_ID")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@Column(name = "PAYMENT_PURPOSE")
	public String getPurposeOfPayment() {
		return purposeOfPayment;
	}

	public void setPurposeOfPayment(String purposeOfPayment) {
		this.purposeOfPayment = purposeOfPayment;
	}

	@Column(name = "SENDER_IBAN")
	public String getSenderIBAN() {
		return senderIBAN;
	}

	public void setSenderIBAN(String senderIBAN) {
		this.senderIBAN = senderIBAN;
	}

	@Column(name = "BENE_NAME")
	public String getBeneAccountTitle() {
		return beneAccountTitle;
	}

	public void setBeneAccountTitle(String beneAccountTitle) {
		this.beneAccountTitle = beneAccountTitle;
	}

	@Column(name = "BENE_BANK_IMD")
	public String getBeneBankImd() {
		return beneBankImd;
	}

	public void setBeneBankImd(String beneBankImd) {
		this.beneBankImd = beneBankImd;
	}
}
