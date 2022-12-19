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
 * @author Muhammad Atif Hussain Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2015/07/29 15:58:45 $
 * 
 * 
 * @spring.bean name="FedBreakupDetailListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANS_COM_FEDBREAKUP_VIEW")
public class FedBreakupDetailListViewModel extends BasePersistableModel
{
	private static final long serialVersionUID = 1018603907555337161L;

	private Long transactionId;
	private String transactionCode;
	private String senderAgentId;
	private String senderId;
	private String senderMobileNo;
	private String senderCnic;
	private String senderChannel;
	private String paymentMode;
	private String accountNo;
	private Date transactionDate;
	private Long productId;
	private String product;
	private String recipientId;
	private String recipientAccountNo;
	private String recipientMobileNo;
	private String recipientCnic;
	private Double amount;
	private Double inclusiveCharges;
	private Double exclusiveCharges;
	private String transactionStatus;
	private Double fedComm;
	private Double netFee;
	private Double bankComm;
	private Double agent1Comm;
	private Double agent2Comm;
	private Double salesTeamComm;
	private Double othersComm;
	private Long agentRetUserId;
	private Double agentRetGrossComm;

	
	// A1 = Hierarchy 1 First Column Set
	private Long   hierarchyA1UserId;
	private Double hierarchyA1GrossCom;

	// B1 = Hierarchy 1 Second Column Set
	private Long   hierarchyB1UserId;
	private Double hierarchyB1GrossCom;

	// C1 = Hierarchy 1 Third Column Set
	private Long   hierarchyC1UserId;
	private Double hierarchyC1GrossCom;

	// D1 = Hierarchy 1 Fourth Column Set
	private Long   hierarchyD1UserId;
	private Double hierarchyD1GrossCom;

	// A2 = Hierarchy 2 First Column Set
	private Long   hierarchyA2UserId;
	private Double hierarchyA2GrossCom;

	// B2 = Hierarchy 2 Second Column Set
	private Long   hierarchyB2UserId;
	private Double hierarchyB2GrossCom;

	// C2 = Hierarchy 2 Third Column Set
	private Long   hierarchyC2UserId;
	private Double hierarchyC2GrossCom;

	// D2 = Hierarchy 2 Fourth Column Set
	private Long   hierarchyD2UserId;
	private Double hierarchyD2GrossCom;


	private Date startDate;
	private Date endDate;
	private String agent2Id;

	/**
	 * Default constructor.
	 */
	public FedBreakupDetailListViewModel()
	{
	}

	/**
	 * Return the primary key.
	 * 
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey()
	{
		return getTransactionId();
	}

	/**
	 * Set the primary key.
	 * 
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey)
	{
		setTransactionId(primaryKey);
	}

	/**
	 * Used by the display tag library for rendering a checkbox in the list.
	 * 
	 * @return private String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox()
	{
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getTransactionId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter()
	{
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
	@Column(name = "TRANSACTION_ID", nullable = false)
	@Id
	public Long getTransactionId()
	{
		return transactionId;
	}

	public void setTransactionId(Long transactionId)
	{
		this.transactionId = transactionId;
	}

	@Transient
	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	@Column(name = "SENDER_AGENT_ID")
	public String getSenderAgentId()
	{
		return senderAgentId;
	}

	public void setSenderAgentId(String senderAgentId)
	{
		this.senderAgentId = senderAgentId;
	}

	@Column(name = "SENDER_ID")
	public String getSenderId()
	{
		return senderId;
	}

	public void setSenderId(String senderId)
	{
		this.senderId = senderId;
	}

	@Column(name = "SENDER_MOBILE_NO")
	public String getSenderMobileNo()
	{
		return senderMobileNo;
	}

	public void setSenderMobileNo(String senderMobileNo)
	{
		this.senderMobileNo = senderMobileNo;
	}

	@Column(name = "SENDER_CNIC")
	public String getSenderCnic()
	{
		return senderCnic;
	}

	public void setSenderCnic(String senderCnic)
	{
		this.senderCnic = senderCnic;
	}

	@Column(name = "SENDER_CHANNEL")
	public String getSenderChannel()
	{
		return senderChannel;
	}

	public void setSenderChannel(String senderChannel)
	{
		this.senderChannel = senderChannel;
	}

	@Column(name = "PAYMENT_MODE")
	public String getPaymentMode()
	{
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	@Column(name = "ACCOUNT_NO")
	public String getAccountNo()
	{
		return accountNo;
	}

	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}

	@Column(name = "TRANSACTION_DATE")
	public Date getTransactionDate()
	{
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate)
	{
		this.transactionDate = transactionDate;
	}

	@Column(name = "PRODUCT")
	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	@Column(name = "RECIPIENT_ID")
	public String getRecipientId()
	{
		return recipientId;
	}

	public void setRecipientId(String recipientId)
	{
		this.recipientId = recipientId;
	}

	@Column(name = "RECIPIENT_ACCOUNT_NO")
	public String getRecipientAccountNo()
	{
		return recipientAccountNo;
	}

	public void setRecipientAccountNo(String recipientAccountNo)
	{
		this.recipientAccountNo = recipientAccountNo;
	}

	@Column(name = "RECIPIENT_MOBILE_NO")
	public String getRecipientMobileNo()
	{
		return recipientMobileNo;
	}

	public void setRecipientMobileNo(String recipientMobileNo)
	{
		this.recipientMobileNo = recipientMobileNo;
	}

	@Column(name = "RECIPIENT_CNIC")
	public String getRecipientCnic()
	{
		return recipientCnic;
	}

	public void setRecipientCnic(String recipientCnic)
	{
		this.recipientCnic = recipientCnic;
	}

	@Column(name = "AMOUNT")
	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	@Column(name = "INCLUSIVE_CHARGES")
	public Double getInclusiveCharges()
	{
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges)
	{
		this.inclusiveCharges = inclusiveCharges;
	}

	@Column(name = "EXCLUSIVE_CHARGES")
	public Double getExclusiveCharges()
	{
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges)
	{
		this.exclusiveCharges = exclusiveCharges;
	}

	@Column(name = "TRANSACTION_STATUS")
	public String getTransactionStatus()
	{
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus)
	{
		this.transactionStatus = transactionStatus;
	}

	@Column(name = "FED_COM")
	public Double getFedComm()
	{
		return fedComm;
	}

	public void setFedComm(Double fedComm)
	{
		this.fedComm = fedComm;
	}

	@Column(name = "NET_FEE")
	public Double getNetFee()
	{
		return netFee;
	}

	public void setNetFee(Double netFee)
	{
		this.netFee = netFee;
	}

	@Column(name = "BANK_GROSS_COMMISSSION")
	public Double getBankComm()
	{
		return bankComm;
	}

	public void setBankComm(Double bankComm)
	{
		this.bankComm = bankComm;
	}

	@Column(name = "AGENT1_GROSS_COMMISSSION")
	public Double getAgent1Comm()
	{
		return agent1Comm;
	}

	public void setAgent1Comm(Double agent1Comm)
	{
		this.agent1Comm = agent1Comm;
	}

	@Column(name = "AGENT2_GROSS_COMMISSSION")
	public Double getAgent2Comm()
	{
		return agent2Comm;
	}

	public void setAgent2Comm(Double agent2Comm)
	{
		this.agent2Comm = agent2Comm;
	}

	@Column(name = "SALES_TEAM_GROSS_COMMISSSION")
	public Double getSalesTeamComm()
	{
		return salesTeamComm;
	}

	public void setSalesTeamComm(Double salesTeamComm)
	{
		this.salesTeamComm = salesTeamComm;
	}

	@Column(name = "OTHERS_GROSS_COMMISSSION")
	public Double getOthersComm()
	{
		return othersComm;
	}

	public void setOthersComm(Double othersComm)
	{
		this.othersComm = othersComm;
	}
	
	@Column(name = "PRODUCT_ID")
	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	@Column(name = "AGENT_RET_GROSS_COMMISSSION")
	public Double getAgentRetGrossComm()
	{
		return agentRetGrossComm;
	}

	public void setAgentRetGrossComm(Double agentRetGrossComm)
	{
		this.agentRetGrossComm = agentRetGrossComm;
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode()
	{
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode)
	{
		this.transactionCode = transactionCode;
	}   
	
	@Column(name="AGENT2_ID")
	public String getAgent2Id() {
		return agent2Id;
	}

	public void setAgent2Id(String agent2Id) {
		this.agent2Id = agent2Id;
	}

	@Column(name = "A_HIERARCHY1_USER_ID")
	public Long getHierarchyA1UserId() {
		return hierarchyA1UserId;
	}

	public void setHierarchyA1UserId(Long hierarchyA1UserId) {
		this.hierarchyA1UserId = hierarchyA1UserId;
	}

	@Column(name = "A_HIERARCHY1_GROSS_COM")
	public Double getHierarchyA1GrossCom() {
		return hierarchyA1GrossCom;
	}

	public void setHierarchyA1GrossCom(Double hierarchyA1GrossCom) {
		this.hierarchyA1GrossCom = hierarchyA1GrossCom;
	}

	@Column(name = "B_HIERARCHY1_USER_ID")
	public Long getHierarchyB1UserId() {
		return hierarchyB1UserId;
	}

	public void setHierarchyB1UserId(Long hierarchyB1UserId) {
		this.hierarchyB1UserId = hierarchyB1UserId;
	}

	@Column(name = "B_HIERARCHY1_GROSS_COM")
	public Double getHierarchyB1GrossCom() {
		return hierarchyB1GrossCom;
	}

	public void setHierarchyB1GrossCom(Double hierarchyB1GrossCom) {
		this.hierarchyB1GrossCom = hierarchyB1GrossCom;
	}

	@Column(name = "C_HIERARCHY1_USER_ID")
	public Long getHierarchyC1UserId() {
		return hierarchyC1UserId;
	}

	public void setHierarchyC1UserId(Long hierarchyC1UserId) {
		this.hierarchyC1UserId = hierarchyC1UserId;
	}

	@Column(name = "C_HIERARCHY1_GROSS_COM")
	public Double getHierarchyC1GrossCom() {
		return hierarchyC1GrossCom;
	}

	public void setHierarchyC1GrossCom(Double hierarchyC1GrossCom) {
		this.hierarchyC1GrossCom = hierarchyC1GrossCom;
	}

	@Column(name = "D_HIERARCHY1_USER_ID")
	public Long getHierarchyD1UserId() {
		return hierarchyD1UserId;
	}

	public void setHierarchyD1UserId(Long hierarchyD1UserId) {
		this.hierarchyD1UserId = hierarchyD1UserId;
	}

	@Column(name = "D_HIERARCHY1_GROSS_COM")
	public Double getHierarchyD1GrossCom() {
		return hierarchyD1GrossCom;
	}

	public void setHierarchyD1GrossCom(Double hierarchyD1GrossCom) {
		this.hierarchyD1GrossCom = hierarchyD1GrossCom;
	}

	@Column(name = "A_HIERARCHY2_USER_ID")
	public Long getHierarchyA2UserId() {
		return hierarchyA2UserId;
	}

	public void setHierarchyA2UserId(Long hierarchyA2UserId) {
		this.hierarchyA2UserId = hierarchyA2UserId;
	}

	@Column(name = "A_HIERARCHY2_GROSS_COM")
	public Double getHierarchyA2GrossCom() {
		return hierarchyA2GrossCom;
	}

	public void setHierarchyA2GrossCom(Double hierarchyA2GrossCom) {
		this.hierarchyA2GrossCom = hierarchyA2GrossCom;
	}

	@Column(name = "B_HIERARCHY2_USER_ID")
	public Long getHierarchyB2UserId() {
		return hierarchyB2UserId;
	}

	public void setHierarchyB2UserId(Long hierarchyB2UserId) {
		this.hierarchyB2UserId = hierarchyB2UserId;
	}

	@Column(name = "B_HIERARCHY2_GROSS_COM")
	public Double getHierarchyB2GrossCom() {
		return hierarchyB2GrossCom;
	}

	public void setHierarchyB2GrossCom(Double hierarchyB2GrossCom) {
		this.hierarchyB2GrossCom = hierarchyB2GrossCom;
	}

	@Column(name = "C_HIERARCHY2_USER_ID")
	public Long getHierarchyC2UserId() {
		return hierarchyC2UserId;
	}

	public void setHierarchyC2UserId(Long hierarchyC2UserId) {
		this.hierarchyC2UserId = hierarchyC2UserId;
	}

	@Column(name = "C_HIERARCHY2_GROSS_COM")
	public Double getHierarchyC2GrossCom() {
		return hierarchyC2GrossCom;
	}

	public void setHierarchyC2GrossCom(Double hierarchyC2GrossCom) {
		this.hierarchyC2GrossCom = hierarchyC2GrossCom;
	}

	@Column(name = "D_HIERARCHY2_USER_ID")
	public Long getHierarchyD2UserId() {
		return hierarchyD2UserId;
	}

	public void setHierarchyD2UserId(Long hierarchyD2UserId) {
		this.hierarchyD2UserId = hierarchyD2UserId;
	}

	@Column(name = "D_HIERARCHY2_GROSS_COM")
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
