package com.inov8.microbank.common.vo.transactionreversal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;

public class ManualReversalVO implements Serializable{
	
	private static final long serialVersionUID = -1L;
	
    private String transactionCode;
    private Integer adjustmentType;
    private String comments;
    
    private Long productId;
    private String productName;
    private Date transactionDate;
    private Double transactionAmount;
    private Double exclusiveCharges;
    private Double inclusiveCharges;
    private Long supProcessingStatusId;
    private String supProcessingStatusName;
    private List<CommissionTransactionViewModel> commissionTransactionList;
    List<BbStatementAllViewModel>  fundTransferEntryList;
    private Double totalAmount;
    
    private Long initiatorAppUserId;
	private String initiatorName;
	private String isReversed;

    
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Integer getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(Integer adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}

	public Long getSupProcessingStatusId() {
		return supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}

	public String getSupProcessingStatusName() {
		return supProcessingStatusName;
	}

	public void setSupProcessingStatusName(String supProcessingStatusName) {
		this.supProcessingStatusName = supProcessingStatusName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<CommissionTransactionViewModel> getCommissionTransactionList() {
		return commissionTransactionList;
	}

	public void setCommissionTransactionList(List<CommissionTransactionViewModel> commissionTransactionList) {
		this.commissionTransactionList = commissionTransactionList;
	}

	public List<BbStatementAllViewModel> getFundTransferEntryList() {
		return fundTransferEntryList;
	}

	public void setFundTransferEntryList(List<BbStatementAllViewModel> fundTransferEntryList) {
		this.fundTransferEntryList = fundTransferEntryList;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getInitiatorAppUserId() {
		return initiatorAppUserId;
	}

	public void setInitiatorAppUserId(Long initiatorAppUserId) {
		this.initiatorAppUserId = initiatorAppUserId;
	}

	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public String getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(String isReversed) {
		this.isReversed = isReversed;
	}
}
