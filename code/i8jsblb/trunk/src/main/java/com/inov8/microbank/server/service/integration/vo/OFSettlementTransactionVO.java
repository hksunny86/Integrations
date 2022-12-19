package com.inov8.microbank.server.service.integration.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.SettlementTransactionModel;

public class OFSettlementTransactionVO {

	Date 	startDate;
	Date	endDate;
	Long 	productId;
	Long	transactionId;
	Long 	supplierId;
	
	List<SettlementTransactionModel>		settlementTransactionModelList = new ArrayList<SettlementTransactionModel>(0);
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public List<SettlementTransactionModel> getSettlementTransactionModelList() {
		return settlementTransactionModelList;
	}
	public void setSettlementTransactionModelList(
			List<SettlementTransactionModel> settlementTransactionModelList) {
		this.settlementTransactionModelList = settlementTransactionModelList;
	}
	
}
