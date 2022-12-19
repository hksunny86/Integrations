package com.inov8.microbank.common.model.tax;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@Table (name = "TARGET_WHT_DETAILS")
public class TargetedWhtDetailReportModel extends BasePersistableModel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8952956795686310522L;
	
	private Long pk;
	private Long wht;
	private Long transactionAmount;
	private Long dailyTransactionAmount;
	private Float whtRate;
	private Date createdOn;
	private String regionName;
	private String transactionCode;
	private String userId;
	private String appUserTypeId;
	private String productName;
	private String taxPayerCnic;
	private String taxPayerName;
	private String businessName;
	private String taxPayerCity;
	private String taxPayerAddress;
	private String mobileNo;
	private String paymentSection;
	private String taxPayerCategory;
	
	private Date startDate;
	private Date endDate;
	
	
	@Column(name = "WHT")
	public Long getWht() {
		return wht;
	}
	public void setWht(Long wht) {
		this.wht = wht;
	}
	
	@Column(name = "TRANSACTION_AMOUNT")
	public Long getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Column(name = "DAILY_TRANSACTION_AMOUNT")
	public Long getDailyTransactionAmount() {
		return dailyTransactionAmount;
	}
	public void setDailyTransactionAmount(Long dailyTransactionAmount) {
		this.dailyTransactionAmount = dailyTransactionAmount;
	}
	
	@Column(name = "WHT_RATE")
	public Float getWhtRate() {
		return whtRate;
	}
	public void setWhtRate(Float whtRate) {
		this.whtRate = whtRate;
	}
	
	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "APP_USER_TYPE_ID")
	public String getAppUserTypeId() {
		return appUserTypeId;
	}
	public void setAppUserTypeId(String appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}
	
	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "TAXPAYER_CNIC")
	public String getTaxPayerCnic() {
		return taxPayerCnic;
	}
	public void setTaxPayerCnic(String taxPayerCnic) {
		this.taxPayerCnic = taxPayerCnic;
	}
	
	@Column(name = "TAXPAYER_NAME")
	public String getTaxPayerName() {
		return taxPayerName;
	}
	public void setTaxPayerName(String taxPayerName) {
		this.taxPayerName = taxPayerName;
	}
	
	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	@Column(name = "TAXPAYER_CITY")
	public String getTaxPayerCity() {
		return taxPayerCity;
	}
	public void setTaxPayerCity(String taxPayerCity) {
		this.taxPayerCity = taxPayerCity;
	}
	
	@Column(name = "TAXPAYER_ADDRESS")
	public String getTaxPayerAddress() {
		return taxPayerAddress;
	}
	public void setTaxPayerAddress(String taxPayerAddress) {
		this.taxPayerAddress = taxPayerAddress;
	}
	
	@Column(name = "MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	@Column(name = "PAYMENT_SECTION")
	public String getPaymentSection() {
		return paymentSection;
	}
	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}
	
	@Column(name = "TAX_PAYER_CATEGORY")
	public String getTaxPayerCategory() {
		return taxPayerCategory;
	}
	public void setTaxPayerCategory(String taxPayerCategory) {
		this.taxPayerCategory = taxPayerCategory;
	}
	

	
	@Id
	@Column (name = "PK")
	public Long getPk() {
		return pk;
	}
	public void setPk(Long pk) {
		this.pk = pk;
	}
	
	@Transient
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Transient
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Transient
	public Long getPrimaryKey() {
		return this.getPk();
	}
	
	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "pk";
	}
	
	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&pk=" + getPk();
	}
	@Override
	public void setPrimaryKey(Long arg0) {
		this.setPk(arg0);
	}
	

}
