package com.inov8.microbank.common.model.portal.reportmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionTransactionWHListViewModel entity bean.
 *
 * @author  Abu Turab Munir  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2015/07/29 15:58:45 $
 *
 *
 * @spring.bean name="CommissionTransactionWHListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANS_COM_WHT_VIEW")
public class CommissionTransactionWHListViewModel extends BasePersistableModel {
  

	/**
	 * 
	 */
	private static final long serialVersionUID = -4539098273683649688L;
	
	private Long transactionId; //TRANSACTION_ID
	private String transactionCode; //TRANSACTION_CODE
	private String accountNo; //ACCOUNT_NO
	
	private Double agent1GrossCommission; //AGENT1_GROSS_COMMISSSION
	private Double agent1NetCommission; //AGENT1_NET_COM
	private Double agent1WH; //AGENT1_WHT
	private Double agent1HierarchyNetCommission; //AGENT_HIERARCHY_NET_COM
	private Double agent1HierarchyGrossCommission; //AGENT_HIER_GROSS_COMMISSSION
	private Double agent1HierarchyWHT; //AGENT_HIER_WHT
	
	private Double agent1RetGrossCommission; //AGENT_RET_GROSS_COMMISSSION
	private Double agent1RetNetCommission; //AGENT_RET_NET_COM
	private Double agent1RetWHT; //AGENT_RET_WHT
	private Long agentRetUserId;
	
	
	private Double agent2GrossCommission; //AGENT2_GROSS_COMMISSSION
	private Double agent2NetCommission; //AGENT2_NET_COM
	private Double agent2WH; //AGENT2_WHT;
	private Double agent2HierarchyGrossCommission; //AGENT_HIER2_GROSS_COMMISSSION
	private Double agent2HierarchyWHT; //AGENT_HIER2_WHT
	private Double agent2HierarchyNetCommission; //AGENT_HIERARCHY2_NET_COM
	
	private Double amount; //AMOUNT
	private Double bankGrossCommission; //BANK_GROSS_COMMISSSION
	private Double bankNetCommission; //BANK_NET_COM
	private Double bankWHT; //BANK_WHT
	private Double exclusiveCharges; //EXCLUSIVE_CHARGES
	private Double inclusiveCharges; //INCLUSIVE_CHARGES
	
	private Double otherGrossCommission; //OTHERS_GROSS_COMMISSSION
	private Double otherNetCommission; //OTHERS_NET_COM
	private Double otherWHT; //OTHERS_WHT
	
	private String paymentMode; //PAYMENT_MODE
	private String processingStatusName; //PROCESSING_STATUS_NAME
	private String product; //PRODUCT
	
	private String recipientAccountNo; //RECIPIENT_ACCOUNT_NO
	private String recipeintCNIC; //RECIPIENT_CNIC
	private String recipientId; //RECIPIENT_ID
	private String recipientMobileNo; //RECIPIENT_MOBILE_NO
	
	private Double salesTeamGrossCommission; //SALES_TEAM_GROSS_COMMISSSION
	private Double salesTeamNetCommission; //SALES_TEAM_NET_COM
	private Double salesTeamWHT; //SALES_TEAM_WHT
	
	private String senderAgentId; //SENDER_AGENT_ID
	private String senderChannel; //SENDER_CHANNEL
	private String senderCNIC; //SENDER_CNIC
	private String senderId; //SENDER_ID
	private String senderMobileNo; //SENDER_MOBILE_NO
	
	private Double totalGrossCommission; //TOTAL_GROSS_COMMISSSION
	private Double totalNetCommission; //TOTAL_NET_COM
	private Double totalWHT; //TOTAL_WHT
	private Date transactionDate; //TRANSACTION_DATE
	
	// A1 = Hierarchy 1 First Column Set
	private Long   hierarchyA1UserId;
	private Double hierarchyA1Comm;
	private Double hierarchyA1WHT;
	private Double hierarchyA1GrossCom;

	// B1 = Hierarchy 1 Second Column Set
	private Long   hierarchyB1UserId;
	private Double hierarchyB1Comm;
	private Double hierarchyB1WHT;
	private Double hierarchyB1GrossCom;

	// C1 = Hierarchy 1 Third Column Set
	private Long   hierarchyC1UserId;
	private Double hierarchyC1Comm;
	private Double hierarchyC1WHT;
	private Double hierarchyC1GrossCom;

	// D1 = Hierarchy 1 Fourth Column Set
	private Long   hierarchyD1UserId;
	private Double hierarchyD1Comm;
	private Double hierarchyD1WHT;
	private Double hierarchyD1GrossCom;

	// A2 = Hierarchy 2 First Column Set
	private Long   hierarchyA2UserId;
	private Double hierarchyA2Comm;
	private Double hierarchyA2WHT;
	private Double hierarchyA2GrossCom;

	// B2 = Hierarchy 2 Second Column Set
	private Long   hierarchyB2UserId;
	private Double hierarchyB2Comm;
	private Double hierarchyB2WHT;
	private Double hierarchyB2GrossCom;

	// C2 = Hierarchy 2 Third Column Set
	private Long   hierarchyC2UserId;
	private Double hierarchyC2Comm;
	private Double hierarchyC2WHT;
	private Double hierarchyC2GrossCom;

	// D2 = Hierarchy 2 Fourth Column Set
	private Long   hierarchyD2UserId;
	private Double hierarchyD2Comm;
	private Double hierarchyD2WHT;
	private Double hierarchyD2GrossCom;
	
	private Date startDate;
	private Date endDate;
	private String agent2Id;

	/**
    * Default constructor.
    */
   public CommissionTransactionWHListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionId(primaryKey);
    }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionId=" + getTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionId";
			return primaryKeyFieldName;				
    }

    /**
     * Returns the value of the <code>transactionId</code> property.
     *
     */
    @Column(name = "TRANSACTION_ID" , nullable = false )
    @Id 
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	
	@Column(name="ACCOUNT_NO")
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	
	@Column(name="AGENT1_GROSS_COMMISSSION")
	public Double getAgent1GrossCommission() {
		return agent1GrossCommission;
	}

	public void setAgent1GrossCommission(Double agent1GrossCommission) {
		this.agent1GrossCommission = agent1GrossCommission;
	}

	
	@Column(name="AGENT1_NET_COM")
	public Double getAgent1NetCommission() {
		return agent1NetCommission;
	}

	public void setAgent1NetCommission(Double agent1NetCommission) {
		this.agent1NetCommission = agent1NetCommission;
	}

	
	@Column(name="AGENT1_WHT")
	public Double getAgent1WH() {
		return agent1WH;
	}

	public void setAgent1WH(Double agent1wh) {
		agent1WH = agent1wh;
	}

	
	@Column(name="AGENT_HIERARCHY_NET_COM")
	public Double getAgent1HierarchyNetCommission() {
		return agent1HierarchyNetCommission;
	}

	public void setAgent1HierarchyNetCommission(Double agent1HierarchyNetCommission) {
		this.agent1HierarchyNetCommission = agent1HierarchyNetCommission;
	}

	
	@Column(name="AGENT_HIER_GROSS_COMMISSSION")
	public Double getAgent1HierarchyGrossCommission() {
		return agent1HierarchyGrossCommission;
	}

	public void setAgent1HierarchyGrossCommission(
			Double agent1HierarchyGrossCommission) {
		this.agent1HierarchyGrossCommission = agent1HierarchyGrossCommission;
	}

	
	@Column(name="AGENT_HIER_WHT")
	public Double getAgent1HierarchyWHT() {
		return agent1HierarchyWHT;
	}

	public void setAgent1HierarchyWHT(Double agent1HierarchyWHT) {
		this.agent1HierarchyWHT = agent1HierarchyWHT;
	}

	
	@Column(name="AGENT_RET_GROSS_COMMISSSION")
	public Double getAgent1RetGrossCommission() {
		return agent1RetGrossCommission;
	}

	public void setAgent1RetGrossCommission(
			Double agent1RetGrossCommission) {
		this.agent1RetGrossCommission = agent1RetGrossCommission;
	}

	
	@Column(name="AGENT_RET_NET_COM")
	public Double getAgent1RetNetCommission() {
		return agent1RetNetCommission;
	}

	public void setAgent1RetNetCommission(Double agent1RetNetCommission) {
		this.agent1RetNetCommission = agent1RetNetCommission;
	}

	
	@Column(name="AGENT_RET_WHT")
	public Double getAgent1RetWHT() {
		return agent1RetWHT;
	}

	public void setAgent1RetWHT(Double agent1RetWHT) {
		this.agent1RetWHT = agent1RetWHT;
	}

	
	@Column(name="AGENT2_GROSS_COMMISSSION")
	public Double getAgent2GrossCommission() {
		return agent2GrossCommission;
	}

	public void setAgent2GrossCommission(Double agent2GrossCommission) {
		this.agent2GrossCommission = agent2GrossCommission;
	}

	
	@Column(name="AGENT2_NET_COM")
	public Double getAgent2NetCommission() {
		return agent2NetCommission;
	}

	public void setAgent2NetCommission(Double agent2NetCommission) {
		this.agent2NetCommission = agent2NetCommission;
	}

	@Column(name="AGENT2_WHT")
	public Double getAgent2WH() {
		return agent2WH;
	}

	public void setAgent2WH(Double agent2wh) {
		agent2WH = agent2wh;
	}

	
	@Column(name="AGENT_HIER2_GROSS_COMMISSSION")
	public Double getAgent2HierarchyGrossCommission() {
		return agent2HierarchyGrossCommission;
	}

	public void setAgent2HierarchyGrossCommission(
			Double agent2HierarchyGrossCommission) {
		this.agent2HierarchyGrossCommission = agent2HierarchyGrossCommission;
	}

	
	@Column(name="AGENT_HIER2_WHT")
	public Double getAgent2HierarchyWHT() {
		return agent2HierarchyWHT;
	}

	public void setAgent2HierarchyWHT(Double agent2HierarchyWHT) {
		this.agent2HierarchyWHT = agent2HierarchyWHT;
	}

	
	@Column(name="AGENT_HIERARCHY2_NET_COM")
	public Double getAgent2HierarchyNetCommission() {
		return agent2HierarchyNetCommission;
	}

	public void setAgent2HierarchyNetCommission(Double agent2HierarchyNetCommission) {
		this.agent2HierarchyNetCommission = agent2HierarchyNetCommission;
	}

	
	@Column(name="AMOUNT")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	
	@Column(name="BANK_GROSS_COMMISSSION")
	public Double getBankGrossCommission() {
		return bankGrossCommission;
	}

	public void setBankGrossCommission(Double bankGrossCommission) {
		this.bankGrossCommission = bankGrossCommission;
	}

	
	@Column(name="BANK_NET_COM")
	public Double getBankNetCommission() {
		return bankNetCommission;
	}

	public void setBankNetCommission(Double bankNetCommission) {
		this.bankNetCommission = bankNetCommission;
	}

	
	@Column(name="BANK_WHT")
	public Double getBankWHT() {
		return bankWHT;
	}

	public void setBankWHT(Double bankWHT) {
		this.bankWHT = bankWHT;
	}

	
	@Column(name="EXCLUSIVE_CHARGES")
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

	
	@Column(name="INCLUSIVE_CHARGES")
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}

	
	@Column(name="OTHERS_GROSS_COMMISSSION")
	public Double getOtherGrossCommission() {
		return otherGrossCommission;
	}

	public void setOtherGrossCommission(Double otherGrossCommission) {
		this.otherGrossCommission = otherGrossCommission;
	}

	
	@Column(name="OTHERS_NET_COM")
	public Double getOtherNetCommission() {
		return otherNetCommission;
	}

	public void setOtherNetCommission(Double otherNetCommission) {
		this.otherNetCommission = otherNetCommission;
	}

	
	@Column(name="OTHERS_WHT")
	public Double getOtherWHT() {
		return otherWHT;
	}

	public void setOtherWHT(Double otherWHT) {
		this.otherWHT = otherWHT;
	}

	
	@Column(name="PAYMENT_MODE")
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	
	@Column(name="PROCESSING_STATUS_NAME")
	public String getProcessingStatusName() {
		return processingStatusName;
	}

	public void setProcessingStatusName(String processingStatusName) {
		this.processingStatusName = processingStatusName;
	}

	
	@Column(name="PRODUCT")
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	
	@Column(name="RECIPIENT_ACCOUNT_NO")
	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}

	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}

	
	@Column(name="RECIPIENT_CNIC")
	public String getRecipeintCNIC() {
		return recipeintCNIC;
	}

	public void setRecipeintCNIC(String recipeintCNIC) {
		this.recipeintCNIC = recipeintCNIC;
	}

	
	@Column(name="RECIPIENT_ID")
	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	
	@Column(name="RECIPIENT_MOBILE_NO")
	public String getRecipientMobileNo() {
		return recipientMobileNo;
	}

	public void setRecipientMobileNo(String recipientMobileNo) {
		this.recipientMobileNo = recipientMobileNo;
	}

	
	@Column(name="SALES_TEAM_GROSS_COMMISSSION")
	public Double getSalesTeamGrossCommission() {
		return salesTeamGrossCommission;
	}

	public void setSalesTeamGrossCommission(Double salesTeamGrossCommission) {
		this.salesTeamGrossCommission = salesTeamGrossCommission;
	}

	
	@Column(name="SALES_TEAM_NET_COM")
	public Double getSalesTeamNetCommission() {
		return salesTeamNetCommission;
	}

	public void setSalesTeamNetCommission(Double salesTeamNetCommission) {
		this.salesTeamNetCommission = salesTeamNetCommission;
	}

	
	@Column(name="SALES_TEAM_WHT")
	public Double getSalesTeamWHT() {
		return salesTeamWHT;
	}

	public void setSalesTeamWHT(Double salesTeamWHT) {
		this.salesTeamWHT = salesTeamWHT;
	}

	
	@Column(name="SENDER_AGENT_ID")
	public String getSenderAgentId() {
		return senderAgentId;
	}

	public void setSenderAgentId(String senderAgentId) {
		this.senderAgentId = senderAgentId;
	}

	
	@Column(name="SENDER_CHANNEL")
	public String getSenderChannel() {
		return senderChannel;
	}

	public void setSenderChannel(String senderChannel) {
		this.senderChannel = senderChannel;
	}

	
	@Column(name="SENDER_CNIC")
	public String getSenderCNIC() {
		return senderCNIC;
	}

	public void setSenderCNIC(String senderCNIC) {
		this.senderCNIC = senderCNIC;
	}

	
	@Column(name="SENDER_ID")
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	
	@Column(name="SENDER_MOBILE_NO")
	public String getSenderMobileNo() {
		return senderMobileNo;
	}

	public void setSenderMobileNo(String senderMobileNo) {
		this.senderMobileNo = senderMobileNo;
	}

	
	@Column(name="TOTAL_GROSS_COMMISSSION")
	public Double getTotalGrossCommission() {
		return totalGrossCommission;
	}

	public void setTotalGrossCommission(Double totalGrossCommission) {
		this.totalGrossCommission = totalGrossCommission;
	}

	
	@Column(name="TOTAL_NET_COM")
	public Double getTotalNetCommission() {
		return totalNetCommission;
	}

	public void setTotalNetCommission(Double totalNetCommission) {
		this.totalNetCommission = totalNetCommission;
	}

	
	@Column(name="TOTAL_WHT")
	public Double getTotalWHT() {
		return totalWHT;
	}

	public void setTotalWHT(Double totalWHT) {
		this.totalWHT = totalWHT;
	}

	
	@Column(name="TRANSACTION_DATE")
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
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

	
	@Column(name="TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}       
	
	@Column(name="AGENT2_ID")
	public String getAgent2Id() {
		return agent2Id;
	}

	public void setAgent2Id(String agent2Id) {
		this.agent2Id = agent2Id;
	}

	@Column(name="A_HIERARCHY1_USER_ID")
	public Long getHierarchyA1UserId() {
		return hierarchyA1UserId;
	}

	public void setHierarchyA1UserId(Long hierarchyA1UserId) {
		this.hierarchyA1UserId = hierarchyA1UserId;
	}

	@Column(name="A_HIERARCHY1_COM")
	public Double getHierarchyA1Comm() {
		return hierarchyA1Comm;
	}

	public void setHierarchyA1Comm(Double hierarchyA1Comm) {
		this.hierarchyA1Comm = hierarchyA1Comm;
	}

	@Column(name="A_HIERARCHY1_WHT")
	public Double getHierarchyA1WHT() {
		return hierarchyA1WHT;
	}

	public void setHierarchyA1WHT(Double hierarchyA1WHT) {
		this.hierarchyA1WHT = hierarchyA1WHT;
	}

	@Column(name="A_HIERARCHY1_GROSS_COM")
	public Double getHierarchyA1GrossCom() {
		return hierarchyA1GrossCom;
	}

	public void setHierarchyA1GrossCom(Double hierarchyA1GrossCom) {
		this.hierarchyA1GrossCom = hierarchyA1GrossCom;
	}

	@Column(name="B_HIERARCHY1_USER_ID")
	public Long getHierarchyB1UserId() {
		return hierarchyB1UserId;
	}

	public void setHierarchyB1UserId(Long hierarchyB1UserId) {
		this.hierarchyB1UserId = hierarchyB1UserId;
	}

	@Column(name="B_HIERARCHY1_COM")
	public Double getHierarchyB1Comm() {
		return hierarchyB1Comm;
	}

	public void setHierarchyB1Comm(Double hierarchyB1Comm) {
		this.hierarchyB1Comm = hierarchyB1Comm;
	}

	@Column(name="B_HIERARCHY1_WHT")
	public Double getHierarchyB1WHT() {
		return hierarchyB1WHT;
	}

	public void setHierarchyB1WHT(Double hierarchyB1WHT) {
		this.hierarchyB1WHT = hierarchyB1WHT;
	}

	@Column(name="B_HIERARCHY1_GROSS_COM")
	public Double getHierarchyB1GrossCom() {
		return hierarchyB1GrossCom;
	}

	public void setHierarchyB1GrossCom(Double hierarchyB1GrossCom) {
		this.hierarchyB1GrossCom = hierarchyB1GrossCom;
	}

	@Column(name="C_HIERARCHY1_USER_ID")
	public Long getHierarchyC1UserId() {
		return hierarchyC1UserId;
	}

	public void setHierarchyC1UserId(Long hierarchyC1UserId) {
		this.hierarchyC1UserId = hierarchyC1UserId;
	}

	@Column(name="C_HIERARCHY1_COM")
	public Double getHierarchyC1Comm() {
		return hierarchyC1Comm;
	}

	public void setHierarchyC1Comm(Double hierarchyC1Comm) {
		this.hierarchyC1Comm = hierarchyC1Comm;
	}

	@Column(name="C_HIERARCHY1_WHT")
	public Double getHierarchyC1WHT() {
		return hierarchyC1WHT;
	}

	public void setHierarchyC1WHT(Double hierarchyC1WHT) {
		this.hierarchyC1WHT = hierarchyC1WHT;
	}

	@Column(name="C_HIERARCHY1_GROSS_COM")
	public Double getHierarchyC1GrossCom() {
		return hierarchyC1GrossCom;
	}

	public void setHierarchyC1GrossCom(Double hierarchyC1GrossCom) {
		this.hierarchyC1GrossCom = hierarchyC1GrossCom;
	}

	@Column(name="D_HIERARCHY1_USER_ID")
	public Long getHierarchyD1UserId() {
		return hierarchyD1UserId;
	}

	public void setHierarchyD1UserId(Long hierarchyD1UserId) {
		this.hierarchyD1UserId = hierarchyD1UserId;
	}

	@Column(name="D_HIERARCHY1_COM")
	public Double getHierarchyD1Comm() {
		return hierarchyD1Comm;
	}

	public void setHierarchyD1Comm(Double hierarchyD1Comm) {
		this.hierarchyD1Comm = hierarchyD1Comm;
	}

	@Column(name="D_HIERARCHY1_WHT")
	public Double getHierarchyD1WHT() {
		return hierarchyD1WHT;
	}

	public void setHierarchyD1WHT(Double hierarchyD1WHT) {
		this.hierarchyD1WHT = hierarchyD1WHT;
	}

	@Column(name="D_HIERARCHY1_GROSS_COM")
	public Double getHierarchyD1GrossCom() {
		return hierarchyD1GrossCom;
	}

	public void setHierarchyD1GrossCom(Double hierarchyD1GrossCom) {
		this.hierarchyD1GrossCom = hierarchyD1GrossCom;
	}

	@Column(name="A_HIERARCHY2_USER_ID")
	public Long getHierarchyA2UserId() {
		return hierarchyA2UserId;
	}

	public void setHierarchyA2UserId(Long hierarchyA2UserId) {
		this.hierarchyA2UserId = hierarchyA2UserId;
	}

	@Column(name="A_HIERARCHY2_COM")
	public Double getHierarchyA2Comm() {
		return hierarchyA2Comm;
	}

	public void setHierarchyA2Comm(Double hierarchyA2Comm) {
		this.hierarchyA2Comm = hierarchyA2Comm;
	}

	@Column(name="A_HIERARCHY2_WHT")
	public Double getHierarchyA2WHT() {
		return hierarchyA2WHT;
	}

	public void setHierarchyA2WHT(Double hierarchyA2WHT) {
		this.hierarchyA2WHT = hierarchyA2WHT;
	}

	@Column(name="A_HIERARCHY2_GROSS_COM")
	public Double getHierarchyA2GrossCom() {
		return hierarchyA2GrossCom;
	}

	public void setHierarchyA2GrossCom(Double hierarchyA2GrossCom) {
		this.hierarchyA2GrossCom = hierarchyA2GrossCom;
	}

	@Column(name="B_HIERARCHY2_USER_ID")
	public Long getHierarchyB2UserId() {
		return hierarchyB2UserId;
	}

	public void setHierarchyB2UserId(Long hierarchyB2UserId) {
		this.hierarchyB2UserId = hierarchyB2UserId;
	}

	@Column(name="B_HIERARCHY2_COM")
	public Double getHierarchyB2Comm() {
		return hierarchyB2Comm;
	}

	public void setHierarchyB2Comm(Double hierarchyB2Comm) {
		this.hierarchyB2Comm = hierarchyB2Comm;
	}

	@Column(name="B_HIERARCHY2_WHT")
	public Double getHierarchyB2WHT() {
		return hierarchyB2WHT;
	}

	public void setHierarchyB2WHT(Double hierarchyB2WHT) {
		this.hierarchyB2WHT = hierarchyB2WHT;
	}

	@Column(name="B_HIERARCHY2_GROSS_COM")
	public Double getHierarchyB2GrossCom() {
		return hierarchyB2GrossCom;
	}

	public void setHierarchyB2GrossCom(Double hierarchyB2GrossCom) {
		this.hierarchyB2GrossCom = hierarchyB2GrossCom;
	}

	@Column(name="C_HIERARCHY2_USER_ID")
	public Long getHierarchyC2UserId() {
		return hierarchyC2UserId;
	}

	public void setHierarchyC2UserId(Long hierarchyC2UserId) {
		this.hierarchyC2UserId = hierarchyC2UserId;
	}

	@Column(name="C_HIERARCHY2_COM")
	public Double getHierarchyC2Comm() {
		return hierarchyC2Comm;
	}

	public void setHierarchyC2Comm(Double hierarchyC2Comm) {
		this.hierarchyC2Comm = hierarchyC2Comm;
	}

	@Column(name="C_HIERARCHY2_WHT")
	public Double getHierarchyC2WHT() {
		return hierarchyC2WHT;
	}

	public void setHierarchyC2WHT(Double hierarchyC2WHT) {
		this.hierarchyC2WHT = hierarchyC2WHT;
	}

	@Column(name="C_HIERARCHY2_GROSS_COM")
	public Double getHierarchyC2GrossCom() {
		return hierarchyC2GrossCom;
	}

	public void setHierarchyC2GrossCom(Double hierarchyC2GrossCom) {
		this.hierarchyC2GrossCom = hierarchyC2GrossCom;
	}

	@Column(name="D_HIERARCHY2_USER_ID")
	public Long getHierarchyD2UserId() {
		return hierarchyD2UserId;
	}

	public void setHierarchyD2UserId(Long hierarchyD2UserId) {
		this.hierarchyD2UserId = hierarchyD2UserId;
	}

	@Column(name="D_HIERARCHY2_COM")
	public Double getHierarchyD2Comm() {
		return hierarchyD2Comm;
	}

	public void setHierarchyD2Comm(Double hierarchyD2Comm) {
		this.hierarchyD2Comm = hierarchyD2Comm;
	}

	@Column(name="D_HIERARCHY2_WHT")
	public Double getHierarchyD2WHT() {
		return hierarchyD2WHT;
	}

	public void setHierarchyD2WHT(Double hierarchyD2WHT) {
		this.hierarchyD2WHT = hierarchyD2WHT;
	}

	@Column(name="D_HIERARCHY2_GROSS_COM")
	public Double getHierarchyD2GrossCom() {
		return hierarchyD2GrossCom;
	}

	public void setHierarchyD2GrossCom(Double hierarchyD2GrossCom) {
		this.hierarchyD2GrossCom = hierarchyD2GrossCom;
	}

	@Column(name = "RETENTION_USER_ID")
	public Long getAgentRetUserId() {
		return agentRetUserId;
	}

	public void setAgentRetUserId(Long agentRetUserId) {
		this.agentRetUserId = agentRetUserId;
	}

}
