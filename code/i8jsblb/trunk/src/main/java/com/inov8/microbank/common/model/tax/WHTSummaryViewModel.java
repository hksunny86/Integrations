package com.inov8.microbank.common.model.tax;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WHT_SUMMARY_VIEW")
public class WHTSummaryViewModel extends BasePersistableModel {

	private Long pk;
	
	private String agentId;
	private Long WHTConfigId;
	private String accType;
	private String paymentSection;
	private String taxPayerCategory;
	private Double WHTRate;
	private String taxPayerNTN;
	private String taxPayerCNIC;
	private String agentMobNo;
	private String taxPayerName;
	private String taxPayerCity;
	private String taxPayerAddress;
	private String taxPayerStatus;
	private String businessName;
	private String agentWHTSum;
	private Double agentCommissionSum;
	
	private Date startDate;
	private Date endDate;
	
	public WHTSummaryViewModel(){
		
	}
	
	
	
	@Id
	   @Column(name = "PK")
	   public Long getPk() {
			return pk;
		}

		public void setPk(Long pk) {
			this.pk = pk;
		}
	   
	   @Transient
	   public void setPrimaryKey(Long aLong) {
	       setPk( aLong );
	   }

	   @Transient
	   public Long getPrimaryKey() {
	       return getPk();
	   }

	   @Transient
	   public String getPrimaryKeyParameter() {
	       return "&agentId=" + getPk();
	   }

	   @Transient
	   public String getPrimaryKeyFieldName() {
	       return "agentId";
	   }
	
   /*@Id 
   @Column(name="AGENT_ID")
   public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}
    *//**
     * Return the primary key.
     *
     * @return Long with the primary key.
     *//*
   
   
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    *//**P
     * Set the primary key.
     *
     * @param primaryKey the primary key
     *//*
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setPk(primaryKey);
    }*/
	
    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
  /* @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "&pk=" + getPk();
      return parameters;
   }
	*//**
     * Helper method for default Sorting on Primary Keys
     *//*
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }*/
	
    @Column(name="AGENT_ID", insertable=false, updatable=false)
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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

	
	
	@Column(name = "WHT_CONFIG_ID")
	public Long getWHTConfigId() {
		return WHTConfigId;
	}



	public void setWHTConfigId(Long wHTConfigId) {
		WHTConfigId = wHTConfigId;
	}



	@Column(name = "XPAYER_CNIC")
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
	public Double getWHTRate() {
		return WHTRate;
	}



	public void setWHTRate(Double WHTRate) {
		this.WHTRate = WHTRate;
	}


	@Column(name = "AGENT_COMMISSION_SUM")
	public Double getAgentCommissionSum() {
		return agentCommissionSum;
	}



	public void setAgentCommissionSum(Double agentCommissionSum) {
		this.agentCommissionSum = agentCommissionSum;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	

}
