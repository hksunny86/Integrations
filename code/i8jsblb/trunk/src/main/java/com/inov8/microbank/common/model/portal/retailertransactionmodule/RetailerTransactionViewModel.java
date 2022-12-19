package com.inov8.microbank.common.model.portal.retailertransactionmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 30, 2013 3:52:54 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "RETAILER_TRANSACTION_VIEW")
public class RetailerTransactionViewModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = -5197204357160072838L;

	// Fields
	private String transactionCode;
	private Long transactionCodeId;
	private Long transactionId;
	private String senderAgentId;
	private String senderAgentUsername;
	private String senderAgentMobileNo;
	private String senderAgentCnic;
	private String senderAgentBusinessName;
	private Long   senderAgentRegionId;
	private String senderAgentRegionName;
	private String senderAgentAreaName;
	private String senderRetailerName;
	private Long   senderDistributorId;
	private String senderDistributorName;
	private String senderDistLevelId;
	private String senderDistLevelName;
	private String receiverAgentId;
	private String receiverAgentUsername;
	private String receiverAgentMobileNo;
	private String receiverAgentCnic;
	private String receiverAgentBusinessName;
	private Long   receiverAgentRegionId;
	private String receiverAgentRegionName;
	private String receiverAgentAreaName;
	private String receiverRetailerName;
	private Long   receiverDistributorId;
	private String receiverDistributorName;
	private String receiverDistLevelId;
	private String receiverDistLevelName;
	private String senderFranchiseUsername;
	private String senderFranchiseUserId;
	private String receiverFranchiseUsername;
	private String receiverFranchiseUserId;
	private String productName;
	private Long supplierId;
	private Long productId;
	private Date createdOn;
	private Date updatedOn;
	private Long supProcessingStatusId;
	private Double transactionAmount;
	private Double totalAmount;
	private String processingStatusName;
	private Double toAskari;
	private Double toAgent1;
	private Double taxDeducted;
	private Double serviceChargesInclusive;
	private Double serviceChargesExclusive;
	private Double toFranchise1;
	private Double salesTeamCommission;
	private Double othersCommission;
	private Long senderSalesHierarchyId;
	private Long recieverSalesHierarchyId;


	//Transient Properties
	private Long numOfSenderAgents;
	private Long numOfReceiverAgents;
	private Long numOfTransaction;
	
	/** default constructor */
	public RetailerTransactionViewModel() {
	}

	@Transient
	@Override
    public Long getPrimaryKey()
    {
        return getTransactionId();
    }

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
        setTransactionId( primaryKey );
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "transactionId";
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&transactionId=" + transactionId;
    }

    // Property accessors
	@Column(name = "TRANSACTION_CODE", nullable = false, length = 50)
	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "TRANSACTION_CODE_ID")
	public Long getTransactionCodeId() {
		return this.transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}

	@Id
	@Column(name = "TRANSACTION_ID")
	public Long getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "SENDER_AGENT_ID", length = 50)
	public String getSenderAgentId() {
		return this.senderAgentId;
	}

	public void setSenderAgentId(String senderAgentId) {
		this.senderAgentId = senderAgentId;
	}

	@Column(name = "SENDER_AGENT_USERNAME", length = 50)
	public String getSenderAgentUsername() {
		return this.senderAgentUsername;
	}

	public void setSenderAgentUsername(String senderAgentUsername) {
		this.senderAgentUsername = senderAgentUsername;
	}

	@Column(name = "SENDER_AGENT_MOBILE_NO", length = 50)
	public String getSenderAgentMobileNo() {
		return this.senderAgentMobileNo;
	}

	public void setSenderAgentMobileNo(String senderAgentMobileNo) {
		this.senderAgentMobileNo = senderAgentMobileNo;
	}

	@Column(name = "SENDER_AGENT_CNIC", length = 50)
	public String getSenderAgentCnic() {
		return this.senderAgentCnic;
	}

	public void setSenderAgentCnic(String senderAgentCnic) {
		this.senderAgentCnic = senderAgentCnic;
	}

	@Column(name = "SENDER_AGENT_BUSINESS_NAME", length = 50)
	public String getSenderAgentBusinessName() {
		return this.senderAgentBusinessName;
	}

	public void setSenderAgentBusinessName(String senderAgentBusinessName) {
		this.senderAgentBusinessName = senderAgentBusinessName;
	}

	@Column(name = "SENDER_AGENT_REGION_ID")
	public Long getSenderAgentRegionId() {
        return senderAgentRegionId;
    }

	public void setSenderAgentRegionId( Long senderAgentRegionId ) {
        this.senderAgentRegionId = senderAgentRegionId;
    }

	@Column(name = "SENDER_AGENT_REGION_NAME", length = 50)
	public String getSenderAgentRegionName() {
		return this.senderAgentRegionName;
	}

	public void setSenderAgentRegionName(String senderAgentRegionName) {
		this.senderAgentRegionName = senderAgentRegionName;
	}

	@Column(name = "SENDER_AGENT_AREA_NAME", length = 50)
	public String getSenderAgentAreaName() {
		return this.senderAgentAreaName;
	}

	public void setSenderAgentAreaName(String senderAgentAreaName) {
		this.senderAgentAreaName = senderAgentAreaName;
	}

	@Column(name = "SENDER_RETAILER_NAME", length = 50)
	public String getSenderRetailerName() {
		return this.senderRetailerName;
	}

	public void setSenderRetailerName(String senderRetailerName) {
		this.senderRetailerName = senderRetailerName;
	}

	@Column(name = "SENDER_DISTRIBUTOR_ID")
	public Long getSenderDistributorId() {
        return senderDistributorId;
    }

	public void setSenderDistributorId( Long senderDistributorId ) {
        this.senderDistributorId = senderDistributorId;
    }

	@Column(name = "SENDER_DISTRIBUTOR_NAME", length = 50)
	public String getSenderDistributorName() {
		return this.senderDistributorName;
	}

	public void setSenderDistributorName(String senderDistributorName) {
		this.senderDistributorName = senderDistributorName;
	}
	
	@Column(name = "SENDER_DIST_LEVEL_ID")
    public String getSenderDistLevelId() {
        return senderDistLevelId;
    }

    public void setSenderDistLevelId( String senderDistLevelId ) {
        this.senderDistLevelId = senderDistLevelId;
    }

	@Column(name = "SENDER_DIST_LEVEL_NAME", length = 50)
	public String getSenderDistLevelName() {
		return this.senderDistLevelName;
	}

	public void setSenderDistLevelName(String senderDistLevelName) {
		this.senderDistLevelName = senderDistLevelName;
	}

	@Column(name = "RECEIVER_AGENT_ID", length = 50)
	public String getReceiverAgentId() {
		return this.receiverAgentId;
	}

	public void setReceiverAgentId(String receiverAgentId) {
		this.receiverAgentId = receiverAgentId;
	}

	@Column(name = "RECEIVER_AGENT_USERNAME", length = 50)
	public String getReceiverAgentUsername() {
		return this.receiverAgentUsername;
	}

	public void setReceiverAgentUsername(String receiverAgentUsername) {
		this.receiverAgentUsername = receiverAgentUsername;
	}

	@Column(name = "RECEIVER_AGENT_MOBILE_NO", length = 50)
	public String getReceiverAgentMobileNo() {
		return this.receiverAgentMobileNo;
	}

	public void setReceiverAgentMobileNo(String receiverAgentMobileNo) {
		this.receiverAgentMobileNo = receiverAgentMobileNo;
	}

	@Column(name = "RECEIVER_AGENT_CNIC", length = 50)
	public String getReceiverAgentCnic() {
		return this.receiverAgentCnic;
	}

	public void setReceiverAgentCnic(String receiverAgentCnic) {
		this.receiverAgentCnic = receiverAgentCnic;
	}

	@Column(name = "RECEIVER_AGENT_BUSINESS_NAME", length = 50)
    public String getReceiverAgentBusinessName() {
        return receiverAgentBusinessName;
    }

    public void setReceiverAgentBusinessName( String receiverAgentBusinessName ) {
        this.receiverAgentBusinessName = receiverAgentBusinessName;
    }

    @Column(name = "RECEIVER_AGENT_REGION_ID")
    public Long getReceiverAgentRegionId() {
        return receiverAgentRegionId;
    }

    public void setReceiverAgentRegionId( Long receiverAgentRegionId ) {
        this.receiverAgentRegionId = receiverAgentRegionId;
    }

	@Column(name = "RECEIVER_AGENT_REGION_NAME", length = 50)
	public String getReceiverAgentRegionName() {
		return this.receiverAgentRegionName;
	}

	public void setReceiverAgentRegionName(String receiverAgentRegionName) {
		this.receiverAgentRegionName = receiverAgentRegionName;
	}

	@Column(name = "RECEIVER_AGENT_AREA_NAME", length = 50)
	public String getReceiverAgentAreaName() {
		return this.receiverAgentAreaName;
	}

	public void setReceiverAgentAreaName(String receiverAgentAreaName) {
		this.receiverAgentAreaName = receiverAgentAreaName;
	}

	@Column(name = "RECEIVER_RETAILER_NAME", length = 50)
	public String getReceiverRetailerName() {
		return this.receiverRetailerName;
	}

	public void setReceiverRetailerName(String receiverRetailerName) {
		this.receiverRetailerName = receiverRetailerName;
	}

	@Column(name = "RECEIVER_DISTRIBUTOR_ID")
	public Long getReceiverDistributorId() {
        return receiverDistributorId;
    }

	public void setReceiverDistributorId( Long receiverDistributorId ) {
        this.receiverDistributorId = receiverDistributorId;
    }

	@Column(name = "RECEIVER_DISTRIBUTOR_NAME", length = 50)
	public String getReceiverDistributorName() {
		return this.receiverDistributorName;
	}

	public void setReceiverDistributorName(String receiverDistributorName) {
		this.receiverDistributorName = receiverDistributorName;
	}

	@Column(name="RECEIVER_DIST_LEVEL_ID")
	public String getReceiverDistLevelId() {
        return receiverDistLevelId;
    }

	public void setReceiverDistLevelId( String receiverDistLevelId ) {
        this.receiverDistLevelId = receiverDistLevelId;
    }

	@Column(name = "RECEIVER_DIST_LEVEL_NAME", length = 50)
	public String getReceiverDistLevelName() {
		return this.receiverDistLevelName;
	}

	public void setReceiverDistLevelName(String receiverDistLevelName) {
		this.receiverDistLevelName = receiverDistLevelName;
	}

	@Column(name = "SENDER_FRANCHISE_USERNAME", length = 50)
	public String getSenderFranchiseUsername() {
		return this.senderFranchiseUsername;
	}

	public void setSenderFranchiseUsername(String senderFranchiseUsername) {
		this.senderFranchiseUsername = senderFranchiseUsername;
	}

	@Column(name = "SENDER_FRANCHISE_USER_ID", length = 50)
	public String getSenderFranchiseUserId() {
		return this.senderFranchiseUserId;
	}

	public void setSenderFranchiseUserId(String senderFranchiseUserId) {
		this.senderFranchiseUserId = senderFranchiseUserId;
	}

	@Column(name = "RECEIVER_FRANCHISE_USERNAME", length = 50)
	public String getReceiverFranchiseUsername() {
		return this.receiverFranchiseUsername;
	}

	public void setReceiverFranchiseUsername(String receiverFranchiseUsername) {
		this.receiverFranchiseUsername = receiverFranchiseUsername;
	}

	@Column(name = "RECEIVER_FRANCHISE_USER_ID", length = 50)
	public String getReceiverFranchiseUserId() {
		return this.receiverFranchiseUserId;
	}

	public void setReceiverFranchiseUserId(String receiverFranchiseUserId) {
		this.receiverFranchiseUserId = receiverFranchiseUserId;
	}

	@Column(name = "PRODUCT_NAME", nullable = false, length = 50)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "SUPPLIER_ID")
	public Long getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "CREATED_ON", nullable = false, length = 7)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON", nullable = false, length = 7)
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "SUP_PROCESSING_STATUS_ID")
	public Long getSupProcessingStatusId() {
		return this.supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}

	@Column(name = "TRANSACTION_AMOUNT", nullable = false)
	public Double getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "TOTAL_AMOUNT", nullable = false)
	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "PROCESSING_STATUS_NAME", length = 50)
	public String getProcessingStatusName() {
		return this.processingStatusName;
	}

	public void setProcessingStatusName(String processingStatusName) {
		this.processingStatusName = processingStatusName;
	}

	@Column(name = "TO_BANK")
	public Double getToAskari() {
		return this.toAskari;
	}

	public void setToAskari(Double toAskari) {
		this.toAskari = toAskari;
	}

	@Column(name = "TO_AGENT1")
	public Double getToAgent1() {
		return this.toAgent1;
	}

	public void setToAgent1(Double toAgent1) {
		this.toAgent1 = toAgent1;
	}

	@Column(name = "TAX_DEDUCTED")
	public Double getTaxDeducted() {
		return this.taxDeducted;
	}

	public void setTaxDeducted(Double taxDeducted) {
		this.taxDeducted = taxDeducted;
	}

	@Column(name = "SERVICE_CHARGES_INCLUSIVE")
	public Double getServiceChargesInclusive() {
		return this.serviceChargesInclusive;
	}

	public void setServiceChargesInclusive(Double serviceChargesInclusive) {
		this.serviceChargesInclusive = serviceChargesInclusive;
	}

	@Column(name = "SERVICE_CHARGES_EXCLUSIVE")
	public Double getServiceChargesExclusive() {
		return this.serviceChargesExclusive;
	}

	public void setServiceChargesExclusive(Double serviceChargesExclusive) {
		this.serviceChargesExclusive = serviceChargesExclusive;
	}

	@Column(name = "TO_FRANCHISE1")
	public Double getToFranchise1() {
		return this.toFranchise1;
	}

	public void setToFranchise1(Double toFranchise1) {
		this.toFranchise1 = toFranchise1;
	}

	@Transient
	public Long getNumOfTransaction() {
        return numOfTransaction;
    }

	public void setNumOfTransaction( Long numOfTransaction ) {
        this.numOfTransaction = numOfTransaction;
    }

	@Transient
    public Long getNumOfSenderAgents() {
        return numOfSenderAgents;
    }

    public void setNumOfSenderAgents( Long numOfSenderAgents ) {
        this.numOfSenderAgents = numOfSenderAgents;
    }

    @Transient
    public Long getNumOfReceiverAgents() {
        return numOfReceiverAgents;
    }

    public void setNumOfReceiverAgents( Long numOfReceiverAgents ) {
        this.numOfReceiverAgents = numOfReceiverAgents;
    }
    @Column( name = "TO_SALES_TEAM" )
    public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}
 
    public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}

    @Column( name = "OTHERS" )
    public Double getOthersCommission() {
		return othersCommission;
	}
    public void setOthersCommission(Double othersCommission) {
		this.othersCommission = othersCommission;
	}

	@Column(name="SENDER_SALES_HIERARCHY_ID")
    public Long getSenderSalesHierarchyId() {
		return senderSalesHierarchyId;
	}

	public void setSenderSalesHierarchyId(Long senderSalesHierarchyId) {
		this.senderSalesHierarchyId = senderSalesHierarchyId;
	}

	@Column(name="RECIPIENT_SALES_HIERARCHY_ID")
	public Long getRecieverSalesHierarchyId() {
		return recieverSalesHierarchyId;
	}

	public void setRecieverSalesHierarchyId(Long recieverSalesHierarchyId) {
		this.recieverSalesHierarchyId = recieverSalesHierarchyId;
	}

}