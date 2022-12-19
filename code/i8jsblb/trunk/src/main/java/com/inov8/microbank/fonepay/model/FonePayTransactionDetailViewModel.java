package com.inov8.microbank.fonepay.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "JCASH_TRANSACTION_DETAIL_VIEW")
public class FonePayTransactionDetailViewModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4810343425682939009L;
	
	private Long   pk;
	private Long   transactionId;
	private String saleMobileNo;
	private String senderCNIC;
	private Long   senderDeviceTypeId;
	private String deviceType;
	private String senderBVS;
	private Long   paymentModeId;
	private String paymentMode;
	private String senderAgentAccountNo;
	private Date   createdOn;
	private Date   updatedOn;
	private Long   supplierId;
	private String supplierName;
	private Long   productId;
	private String productName;
	private String recipientMfsId;
	private String recipientAccountNo;
	private String recipientMobileNo;
	private String recipientCNIC;
	private String receiverBVS;
	private Double receivableFromFonepay;
	private Double payableToFonepay;
	private Double netSettlement;
	private Double serviceChargesInclusive;
	private Double serviceChargesExclusive;
	private Double totalCustomerCharges;
	private String fundTransferRRN;
	private Long   supProcessingStatusId;
	private String processingStatusName;
	private String fonePayTransactionCode;
	private String fonePayTransactionType;
	private String mfsId;
	

	@Column(name = "PK" , nullable = false )
	@Id
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	@Column(name = "TRANSACTION_ID" , nullable = false )
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "SALE_MOBILE_NO" , nullable = false )
	public String getSaleMobileNo() {
		return saleMobileNo;
	}

	public void setSaleMobileNo(String saleMobileNo) {
		this.saleMobileNo = saleMobileNo;
	}

	@Column(name = "SENDER_CNIC" , nullable = false )
	public String getSenderCNIC() {
		return senderCNIC;
	}

	public void setSenderCNIC(String senderCNIC) {
		this.senderCNIC = senderCNIC;
	}

	@Column(name = "SENDER_DEVICE_TYPE_ID" , nullable = false )
	public Long getSenderDeviceTypeId() {
		return senderDeviceTypeId;
	}

	public void setSenderDeviceTypeId(Long senderDeviceTypeId) {
		this.senderDeviceTypeId = senderDeviceTypeId;
	}

	@Column(name = "DEVICE_TYPE" , nullable = false )
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Column(name = "SENDER_BVS" , nullable = false )
	public String getSenderBVS() {
		return senderBVS;
	}

	public void setSenderBVS(String senderBVS) {
		this.senderBVS = senderBVS;
	}

	@Column(name = "PAYMENT_MODE_ID" , nullable = false )
	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	@Column(name = "PAYMENT_MODE" , nullable = false )
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	@Column(name = "SENDER_AGENT_ACCOUNT_NO" , nullable = false )
	public String getSenderAgentAccountNo() {
		return senderAgentAccountNo;
	}

	public void setSenderAgentAccountNo(String senderAgentAccountNo) {
		this.senderAgentAccountNo = senderAgentAccountNo;
	}

	@Column(name = "CREATED_ON" , nullable = false )
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON" , nullable = false )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "SUPPLIER_ID" , nullable = false )
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name = "SUPPLIER_NAME" , nullable = false )
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name = "PRODUCT_ID" , nullable = false )
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_NAME" , nullable = false )
	public String getProductName() {
		return productName;
	}

	
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "RECIPIENT_MFS_ID" , nullable = false )
	public String getRecipientMfsId() {
		return recipientMfsId;
	}

	public void setRecipientMfsId(String recipientMfsId) {
		this.recipientMfsId = recipientMfsId;
	}

	@Column(name = "RECIPIENT_ACCOUNT_NO" , nullable = false )
	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}

	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}

	@Column(name = "RECIPIENT_MOBILE_NO" , nullable = false )
	public String getRecipientMobileNo() {
		return recipientMobileNo;
	}

	public void setRecipientMobileNo(String recipientMobileNo) {
		this.recipientMobileNo = recipientMobileNo;
	}

	@Column(name = "RECIPIENT_CNIC" , nullable = false )
	public String getRecipientCNIC() {
		return recipientCNIC;
	}

	public void setRecipientCNIC(String recipientCNIC) {
		this.recipientCNIC = recipientCNIC;
	}

	@Column(name = "RECEIVER_BVS" , nullable = false )
	public String getReceiverBVC() {
		return receiverBVS;
	}

	public void setReceiverBVC(String receiverBVS) {
		this.receiverBVS = receiverBVS;
	}

	@Column(name = "RECEIVABLE_FROM_FONEPAY" , nullable = false )
	public Double getReceivableFromFonepay() {
		return receivableFromFonepay;
	}

	public void setReceivableFromFonepay(Double receivableFromFonepay) {
		this.receivableFromFonepay = receivableFromFonepay;
	}

	@Column(name = "PAYABLE_TO_FONEPAY" , nullable = false )
	public Double getPayableToFonepay() {
		return payableToFonepay;
	}

	public void setPayableToFonepay(Double payableToFonepay) {
		this.payableToFonepay = payableToFonepay;
	}

	@Column(name = "NET_SETTLEMENT" , nullable = false )
	public Double getNetSettlement() {
		return netSettlement;
	}

	public void setNetSettlement(Double netSettlement) {
		this.netSettlement = netSettlement;
	}

	@Column(name = "SERVICE_CHARGES_INCLUSIVE" , nullable = false )
	public Double getServiceChargesInclusive() {
		return serviceChargesInclusive;
	}

	public void setServiceChargesInclusive(Double serviceChargesInclusive) {
		this.serviceChargesInclusive = serviceChargesInclusive;
	}

	@Column(name = "SERVICE_CHARGES_EXCLUSIVE" , nullable = false )
	public Double getServiceChargesExclusive() {
		return serviceChargesExclusive;
	}

	public void setServiceChargesExclusive(Double serviceChargesExclusive) {
		this.serviceChargesExclusive = serviceChargesExclusive;
	}

	@Column(name = "TOTAL_CUSTOMER_CHARGES" , nullable = false )
	public Double getTotalCustomerCharges() {
		return totalCustomerCharges;
	}

	public void setTotalCustomerCharges(Double totalCustomerCharges) {
		this.totalCustomerCharges = totalCustomerCharges;
	}

	@Column(name = "FUND_TRANSFER_RRN" , nullable = false )
	public String getFundTransferRRN() {
		return fundTransferRRN;
	}

	public void setFundTransferRRN(String fundTransferRRN) {
		this.fundTransferRRN = fundTransferRRN;
	}

	@Column(name = "SUP_PROCESSING_STATUS_ID" , nullable = false )
	public Long getSupProcessingStatusId() {
		return supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}

	@Column(name = "PROCESSING_STATUS_NAME" , nullable = false )
	public String getProcessingStatusName() {
		return processingStatusName;
	}

	public void setProcessingStatusName(String processingStatusName) {
		this.processingStatusName = processingStatusName;
	}

	@Column(name = "FONEPAY_TRANSACTION_CODE" , nullable = false )
	public String getFonePayTransactionCode() {
		return fonePayTransactionCode;
	}

	
	public void setFonePayTransactionCode(String fonePayTransactionCode) {
		this.fonePayTransactionCode = fonePayTransactionCode;
	}

	@Column(name = "FONEPAY_TRANSACTION_TYPE" , nullable = false )
	public String getFonePayTransactionType() {
		return fonePayTransactionType;
	}

	public void setFonePayTransactionType(String fonePayTransactionType) {
		this.fonePayTransactionType = fonePayTransactionType;
	}

	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return getPk();
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "pk";
		return primaryKeyFieldName;	
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
	    parameters += "&pk=" + getPk();
		return parameters;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setPk(arg0);
	}

	@Column(name = "MFS_ID")
	public String getMfsId() {
		return mfsId;
	}

	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}

}
