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
@Table(name = "P2P_TRANSACTION_REPORT_VIEW")
public class CashTransReportViewModel extends BasePersistableModel {
	
	private Long transactionCodeId;
	private String transactionCode;
//	private String transactionType;
	private String productName;
	private String senderChannel;
	private String paymentMode;
	private Date initiatedOn;
	private Date completedOn;
	
	private Long   senderAgentId;
	private String senderAgentName;
	private String senderAgentAccountNo;
	private String senderAgentAccountTitle;
	private String senderAgentCity;
	private String senderAgentRegion;
	
	private Long   recipientAgentId;
	private String recipientAgentName;
	private String recipientAgentAccountNo;
	private String recipientAgentAccountTitle;
	private String recipientAgentCity;
	private String recipientAgentRegion;

	private String senderCustomerMobile;
	private String senderCustomerCNIC;
	private String recipientCustomerMobile;
	private String recipientCustomerCNIC;

	private Double amount;
	private Double inclusiveCharges;
	private Double exclusiveCharges;
	private Double fed;
	private Double bankCommission;

	private Double senderAgentGrossCommission;
	private Double senderAgentNetCommission;
	private Double senderAgentWHT;

	private Double recipientAgentGrossCommission;
	private Double recipientAgentNetCommission;
	private Double recipientAgentWHT;

	private String status;

	private Date startDate; // transient
	private Date endDate; // transient
	
	
   public CashTransReportViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setTransactionCodeId(primaryKey);
    }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "&transactionCodeId=" + getTransactionCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionCodeId";
			return primaryKeyFieldName;				
    }
 
	@Id
    @Column(name="TRANSACTION_CODE_ID")
    public Long getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}

    @Column(name="TRANSACTION_CODE")
    public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

//    @Column(name="TRANSACTION_TYPE")
//	public String getTransactionType() {
//		return transactionType;
//	}
//
//	public void setTransactionType(String transactionType) {
//		this.transactionType = transactionType;
//	}

    @Column(name="PRODUCT")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

    @Column(name="SENDER_CHANNEL")
	public String getSenderChannel() {
		return senderChannel;
	}

	public void setSenderChannel(String senderChannel) {
		this.senderChannel = senderChannel;
	}

    @Column(name="PAYMENT_MODE")
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

    @Column(name="TRANSACTION_INITIATED")
	public Date getInitiatedOn() {
		return initiatedOn;
	}

	public void setInitiatedOn(Date initiatedOn) {
		this.initiatedOn = initiatedOn;
	}

    @Column(name="TRANSACTION_COMPLETED")
	public Date getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

    @Column(name="SENDER_AGENT_ID")
	public Long getSenderAgentId() {
		return senderAgentId;
	}

	public void setSenderAgentId(Long senderAgentId) {
		this.senderAgentId = senderAgentId;
	}

    @Column(name="SENDER_AGENT_NAME")
	public String getSenderAgentName() {
		return senderAgentName;
	}

	public void setSenderAgentName(String senderAgentName) {
		this.senderAgentName = senderAgentName;
	}

    @Column(name="SENDER_ACCOUNT_NO")
	public String getSenderAgentAccountNo() {
		return senderAgentAccountNo;
	}

	public void setSenderAgentAccountNo(String senderAgentAccountNo) {
		this.senderAgentAccountNo = senderAgentAccountNo;
	}

    @Column(name="SENDER_AGENT_AC_TITLE")
	public String getSenderAgentAccountTitle() {
		return senderAgentAccountTitle;
	}

	public void setSenderAgentAccountTitle(String senderAgentAccountTitle) {
		this.senderAgentAccountTitle = senderAgentAccountTitle;
	}

    @Column(name="SENDER_AGENT_CITY")
	public String getSenderAgentCity() {
		return senderAgentCity;
	}

	public void setSenderAgentCity(String senderAgentCity) {
		this.senderAgentCity = senderAgentCity;
	}

    @Column(name="SENDER_AGENT_REGION")
	public String getSenderAgentRegion() {
		return senderAgentRegion;
	}

	public void setSenderAgentRegion(String senderAgentRegion) {
		this.senderAgentRegion = senderAgentRegion;
	}

    @Column(name="RECIPIENT_AGENT_ID")
	public Long getRecipientAgentId() {
		return recipientAgentId;
	}

	public void setRecipientAgentId(Long recipientAgentId) {
		this.recipientAgentId = recipientAgentId;
	}

    @Column(name="RECIPIENT_AGENT_NAME")
	public String getRecipientAgentName() {
		return recipientAgentName;
	}

	public void setRecipientAgentName(String recipientAgentName) {
		this.recipientAgentName = recipientAgentName;
	}

    @Column(name="RECIPIENT_ACCOUNT_NO")
	public String getRecipientAgentAccountNo() {
		return recipientAgentAccountNo;
	}

	public void setRecipientAgentAccountNo(String recipientAgentAccountNo) {
		this.recipientAgentAccountNo = recipientAgentAccountNo;
	}

    @Column(name="RECIPIENT_AGENT_AC_TITLE")
	public String getRecipientAgentAccountTitle() {
		return recipientAgentAccountTitle;
	}

	public void setRecipientAgentAccountTitle(String recipientAgentAccountTitle) {
		this.recipientAgentAccountTitle = recipientAgentAccountTitle;
	}

    @Column(name="RECIPIENT_AGENT_CITY")
	public String getRecipientAgentCity() {
		return recipientAgentCity;
	}

	public void setRecipientAgentCity(String recipientAgentCity) {
		this.recipientAgentCity = recipientAgentCity;
	}

    @Column(name="RECIPIENT_AGENT_REGION")
	public String getRecipientAgentRegion() {
		return recipientAgentRegion;
	}

	public void setRecipientAgentRegion(String recipientAgentRegion) {
		this.recipientAgentRegion = recipientAgentRegion;
	}

    @Column(name="SENDER_MOBILE_NO")
	public String getSenderCustomerMobile() {
		return senderCustomerMobile;
	}

	public void setSenderCustomerMobile(String senderCustomerMobile) {
		this.senderCustomerMobile = senderCustomerMobile;
	}

    @Column(name="SENDER_CNIC")
	public String getSenderCustomerCNIC() {
		return senderCustomerCNIC;
	}

	public void setSenderCustomerCNIC(String senderCustomerCNIC) {
		this.senderCustomerCNIC = senderCustomerCNIC;
	}

    @Column(name="RECIPIENT_MOBILE_NO")
	public String getRecipientCustomerMobile() {
		return recipientCustomerMobile;
	}

	public void setRecipientCustomerMobile(String recipientCustomerMobile) {
		this.recipientCustomerMobile = recipientCustomerMobile;
	}

    @Column(name="RECIPIENT_CNIC")
	public String getRecipientCustomerCNIC() {
		return recipientCustomerCNIC;
	}

	public void setRecipientCustomerCNIC(String recipientCustomerCNIC) {
		this.recipientCustomerCNIC = recipientCustomerCNIC;
	}

    @Column(name="AMOUNT")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

    @Column(name="INCLUSIVE_CHARGES")
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}

    @Column(name="EXCLUSIVE_CHARGES")
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

    @Column(name="FED")
	public Double getFed() {
		return fed;
	}

	public void setFed(Double fed) {
		this.fed = fed;
	}

    @Column(name="BANK_GROSS_COMMISSSION")
	public Double getBankCommission() {
		return bankCommission;
	}

	public void setBankCommission(Double bankCommission) {
		this.bankCommission = bankCommission;
	}

    @Column(name="AGENT1_GROSS_COMMISSSION")
	public Double getSenderAgentGrossCommission() {
		return senderAgentGrossCommission;
	}

	public void setSenderAgentGrossCommission(Double senderAgentGrossCommission) {
		this.senderAgentGrossCommission = senderAgentGrossCommission;
	}

    @Column(name="AGENT1_NET_COM")
	public Double getSenderAgentNetCommission() {
		return senderAgentNetCommission;
	}

	public void setSenderAgentNetCommission(Double senderAgentNetCommission) {
		this.senderAgentNetCommission = senderAgentNetCommission;
	}

    @Column(name="AGENT1_WHT")
	public Double getSenderAgentWHT() {
		return senderAgentWHT;
	}

	public void setSenderAgentWHT(Double senderAgentWHT) {
		this.senderAgentWHT = senderAgentWHT;
	}

    @Column(name="AGENT2_GROSS_COMMISSSION")
	public Double getRecipientAgentGrossCommission() {
		return recipientAgentGrossCommission;
	}

	public void setRecipientAgentGrossCommission(
			Double recipientAgentGrossCommission) {
		this.recipientAgentGrossCommission = recipientAgentGrossCommission;
	}

    @Column(name="AGENT2_NET_COM")
	public Double getRecipientAgentNetCommission() {
		return recipientAgentNetCommission;
	}

	public void setRecipientAgentNetCommission(Double recipientAgentNetCommission) {
		this.recipientAgentNetCommission = recipientAgentNetCommission;
	}

    @Column(name="AGENT2_WHT")
	public Double getRecipientAgentWHT() {
		return recipientAgentWHT;
	}

	public void setRecipientAgentWHT(Double recipientAgentWHT) {
		this.recipientAgentWHT = recipientAgentWHT;
	}
	
    @Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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