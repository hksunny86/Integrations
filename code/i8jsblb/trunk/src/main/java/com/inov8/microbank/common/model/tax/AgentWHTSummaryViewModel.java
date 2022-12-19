package com.inov8.microbank.common.model.tax;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_WHT_SUMMARY_VIEW")
public class AgentWHTSummaryViewModel extends BasePersistableModel {

private static final long serialVersionUID = -2914631301844610140L;
	
	private Long agentId;
	private String agentWHTSum;
	private Long WHTConfigId;
	private String accType;
	private String paymentSection;
	private String taxPayerCategory;
	private Long WHTRate;
	private String taxPayerNTN;
	private String taxPayerCNIC;
	private String agentMobNo;
	private String taxPayerName;
	private String taxPayerCity;
	private String taxPayerAddress;
	private String taxPayerStatus;
	private String businessName;
	private Long agentCommissionSum;
	
	public AgentWHTSummaryViewModel(){
		
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



	@Column(name = "AGENT_MOBILE_NO")
	public String getAgentMobNo() {
		return agentMobNo;
	}



	public void setAgentMobNo(String agentMobNo) {
		this.agentMobNo = agentMobNo;
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



	@Column(name = "AGENT_ID")
	public Long getAgentId() {
		return agentId;
	}



	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}


	@Column(name = "AGENT_WHT_SUM")
	public String getAgentWHTSum() {
		return agentWHTSum;
	}



	public void setAgentWHTSum(String agentWHTSum) {
		this.agentWHTSum = agentWHTSum;
	}


	@Column(name = "ACCOUNT_TYPE")
	public String getAccType() {
		return accType;
	}



	public void setAccType(String accType) {
		this.accType = accType;
	}


	@Column(name = "TAX_PAYER_CATEGORY")
	public String getTaxPayerCategory() {
		return taxPayerCategory;
	}



	public void setTaxPayerCategory(String taxPayerCategory) {
		this.taxPayerCategory = taxPayerCategory;
	}


	@Column(name = "WHT_RATE")
	public Long getWHTRate() {
		return WHTRate;
	}



	public void setWHTRate(Long WHTRate) {
		this.WHTRate = WHTRate;
	}


	@Column(name = "AGENT_COMMISSION_SUM")
	public Long getAgentCommissionSum() {
		return agentCommissionSum;
	}



	public void setAgentCommissionSum(Long agentCommissionSum) {
		this.agentCommissionSum = agentCommissionSum;
	}

	

}
