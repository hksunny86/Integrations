package com.inov8.microbank.common.model.tax;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TAX_PAYER_INFO_VIEW")
public class TaxPayerInfoViewModel extends BasePersistableModel  {

	private static final long serialVersionUID = -2914631301844610140L;
	
	
	private String paymentSection;
	private String taxPayerNTN;
	private String taxPayerCNIC;
	private Long appuserId;
	private String agentMobNo;
	private String userId;
	private String taxPayerName;
	private String taxPayerCity;
	private String taxPayerAddress;
	private String taxPayerStatus;
	private String businessName;
	private Boolean isFiler;
	
	public TaxPayerInfoViewModel(){
		
	}
	
	
	
	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}


	@Column(name = "PAYMENT_SECTION")
	public String getPaymentSection() {
		return paymentSection;
	}



	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}


	@Column(name = "TAXPAYER_NTN")
	public String getTaxPayerNTN() {
		return taxPayerNTN;
	}



	public void setTaxPayerNTN(String taxPayerNTN) {
		this.taxPayerNTN = taxPayerNTN;
	}


	@Column(name = "TAXPAYER_CNIC")
	public String getTaxPayerCNIC() {
		return taxPayerCNIC;
	}



	public void setTaxPayerCNIC(String taxPayerCNIC) {
		this.taxPayerCNIC = taxPayerCNIC;
	}


	@Column(name = "APP_USER_ID")
	public Long getAppuserId() {
		return appuserId;
	}



	public void setAppuserId(Long appuserId) {
		this.appuserId = appuserId;
	}


	@Column(name = "AGENT_MOBILE_NO")
	public String getAgentMobNo() {
		return agentMobNo;
	}



	public void setAgentMobNo(String agentMobNo) {
		this.agentMobNo = agentMobNo;
	}


	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}


	@Column(name = "TAXPAYER_NAME")
	public String getTaxPayerName() {
		return taxPayerName;
	}



	public void setTaxPayerName(String taxPayerName) {
		this.taxPayerName = taxPayerName;
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


	@Column(name = "TAXPAYER_STATUS")
	public String getTaxPayerStatus() {
		return taxPayerStatus;
	}



	public void setTaxPayerStatus(String taxPayerStatus) {
		this.taxPayerStatus = taxPayerStatus;
	}


	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}



	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}


	@Column(name = "IS_FILER")
	public Boolean getIsFiler() {
		return isFiler;
	}



	public void setIsFiler(Boolean isFiler) {
		this.isFiler = isFiler;
	}
	
	
	
	

}
