package com.inov8.microbank.common.model.portal.reportmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_WHT_SUMMARY_VIEW")
public class WHTAgentReportViewModel extends BasePersistableModel {
	
	private Long pk;
	
	private String agentId;
	private String mobileNo;
	private String paymentSection;
	private String taxPayerCategory;
	private Double taxRate;
	private String ntn;
	private String cnic;
	private String name;
	private String city;
	private String address;
	private String businessName;
	private String accountTypeName;
	private Double totalCommission;
	private Double totalWht;
	
	private Date startDate;
	private Date endDate;

	
   public WHTAgentReportViewModel() {
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
   
 
   
 
   
   
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

    
    @Column(name="AGENT_ID", insertable=false, updatable=false)
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

    @Column(name="Agent_MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

    @Column(name="PAYMENT_SECTION")
	public String getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}
	
		
	@Column(name="TAX_PAYER_CATEGORY")
    public String getTaxPayerCategory() {
		return taxPayerCategory;
	}

	public void setTaxPayerCategory(String taxPayerCategory) {
		this.taxPayerCategory = taxPayerCategory;
	}

	@Column(name="WHT_RATE")
	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}




	@Column(name="TAXPAYER_NTN")
	public String getNtn() {
		return ntn;
	}

	public void setNtn(String ntn) {
		this.ntn = ntn;
	}

    @Column(name="TAXPAYER_CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

    @Column(name="TAXPAYER_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Column(name="TAXPAYER_CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

    @Column(name="TAXPAYER_ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    @Column(name="BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

    @Column(name="TAXPAYER_STATUS")
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

    @Column(name="AGENT_COMMISSION_SUM")
	public Double getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(Double totalCommission) {
		this.totalCommission = totalCommission;
	}

    @Column(name="AGENT_WHT_SUM")
	public Double getTotalWht() {
		return totalWht;
	}

	public void setTotalWht(Double totalWht) {
		this.totalWht = totalWht;
	}
 
    @Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate ){
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate(){
        return endDate;
    }

    public void setEndDate( Date endDate ){
        this.endDate = endDate;
    }

}