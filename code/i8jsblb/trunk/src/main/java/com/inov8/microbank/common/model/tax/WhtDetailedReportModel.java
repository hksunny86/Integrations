package com.inov8.microbank.common.model.tax;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Attique on 8/22/2017.
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WHT_DETAILED_REPORT")
public class WhtDetailedReportModel extends BasePersistableModel {
    private Long pk;
    private Long taxPayerId;
    private String taxPayerCategory;
    private String accountType;
    private String transactionId;
    private String taxRate;

    private Long productId;
    private String productName;
    private Date transactionDate;
    private Date transactionLastUpdatedOn;

    private String amount;
    private String inclusiveCharges;
    private String exclusiveCharges;
    private String bankComission;

    private String agent1Cnic;
    private Long agent1Id;
    private String agent1Mobile;
    private String senderCnic;
    private String senderId;
    private String senderMobile;


    private String agent2Cnic;
    private Long agent2Id;
    private String agent2Mobile;
    private String recepientCnic;
    private String recepientId;
    private String recepientMobile;

    private String agent1GrossComission;
    private String agent1Wht;
    private String agent1NetComission;
    private String agent2GrossComission;
    private String agent2Wht;
    private String agent2NetComission;


    private String salesTeamGrossComission;
    private String salesTeamWht;
    private String salesTeamNetComission;

    private Long statusId;
    private String statusName;

    private String totalWht;

    private Long whtConfigId;

    private String agent1233;
    private String agent2233;
    private String p236;
    private String a231;

    /*    private Long agentCommissionSum;
        private Date transactionDate;*/
    private Date transactionStartDate;
    private Date transactionEndDate;
    private Date transactionStartDateUpdated;
    private Date transactionEndDateUpdated;
    
    private String blbSettlementComission;

    public WhtDetailedReportModel(){

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
        return "&taxPayerId=" + getPk();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        return "taxPayerId";
    }


    @Column(name = "TAX_PAYER_ID", insertable=false, updatable=false)
    public Long getTaxPayerId() {
        return taxPayerId;
    }

    public void setTaxPayerId(Long taxPayerId) {
        this.taxPayerId = taxPayerId;
    }
    @Column(name = "TAX_PAYER_CATEGORY")
    public String getTaxPayerCategory() {
        return taxPayerCategory;
    }

    public void setTaxPayerCategory(String taxPayerCategory) {
        this.taxPayerCategory = taxPayerCategory;
    }
    @Column(name = "ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    @Column(name = "TRANSACTION_ID")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    @Column(name = "TAX_RATE")
    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @Column(name = "PRODUCT")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    @Column(name = "TRANSACTION_LAST_UPDATED_ON")
    public Date getTransactionLastUpdatedOn() {
        return transactionLastUpdatedOn;
    }

    public void setTransactionLastUpdatedOn(Date transactionLastUpdatedOn) {
        this.transactionLastUpdatedOn = transactionLastUpdatedOn;
    }
    @Column(name = "AMOUNT")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    @Column(name = "INCLUSIVE_CHARGES")
    public String getInclusiveCharges() {
        return inclusiveCharges;
    }

    public void setInclusiveCharges(String inclusiveCharges) {
        this.inclusiveCharges = inclusiveCharges;
    }
    @Column(name = "EXCLUSIVE_CHARGES")
    public String getExclusiveCharges() {
        return exclusiveCharges;
    }

    public void setExclusiveCharges(String exclusiveCharges) {
        this.exclusiveCharges = exclusiveCharges;
    }
    @Column(name = "BANK_COMMISSION")
    public String getBankComission() {
        return bankComission;
    }

    public void setBankComission(String bankComission) {
        this.bankComission = bankComission;
    }
    @Column(name = "AGENT1_CNIC_NO")
    public String getAgent1Cnic() {
        return agent1Cnic;
    }

    public void setAgent1Cnic(String agent1Cnic) {
        this.agent1Cnic = agent1Cnic;
    }
    @Column(name = "AGENT1_ID")
    public Long getAgent1Id() {
        return agent1Id;
    }

    public void setAgent1Id(Long agent1Id) {
        this.agent1Id = agent1Id;
    }
    @Column(name = "AGENT1_MOBILE_NO")
    public String getAgent1Mobile() {
        return agent1Mobile;
    }

    public void setAgent1Mobile(String agent1Mobile) {
        this.agent1Mobile = agent1Mobile;
    }
    @Column(name = "SENDER_CNIC_NO")
    public String getSenderCnic() {
        return senderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        this.senderCnic = senderCnic;
    }
    @Column(name = "SENDER_ID")
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    @Column(name = "SENDER_MOBILE_NO")
    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }
    @Column(name = "AGENT2_CNIC_NO")
    public String getAgent2Cnic() {
        return agent2Cnic;
    }

    public void setAgent2Cnic(String agent2Cnic) {
        this.agent2Cnic = agent2Cnic;
    }
    @Column(name = "AGENT2_ID")
    public Long getAgent2Id() {
        return agent2Id;
    }

    public void setAgent2Id(Long agent2Id) {
        this.agent2Id = agent2Id;
    }
    @Column(name = "AGENT2_MOBILE_NO")
    public String getAgent2Mobile() {
        return agent2Mobile;
    }

    public void setAgent2Mobile(String agent2Mobile) {
        this.agent2Mobile = agent2Mobile;
    }
    @Column(name = "RECIPIENT_CNIC_NO")
    public String getRecepientCnic() {
        return recepientCnic;
    }

    public void setRecepientCnic(String recepientCnic) {
        this.recepientCnic = recepientCnic;
    }
    @Column(name = "RECIPIENT_ID")
    public String getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(String recepientId) {
        this.recepientId = recepientId;
    }
    @Column(name = "RECIPIENT_MOBILE_NO")
    public String getRecepientMobile() {
        return recepientMobile;
    }

    public void setRecepientMobile(String recepientMobile) {
        this.recepientMobile = recepientMobile;
    }
    @Column(name = "AGENT1_GROSS_COMMISSION")
    public String getAgent1GrossComission() {
        return agent1GrossComission;
    }

    public void setAgent1GrossComission(String agent1GrossComission) {
        this.agent1GrossComission = agent1GrossComission;
    }
    @Column(name = "AGENT1_WHT")
    public String getAgent1Wht() {
        return agent1Wht;
    }

    public void setAgent1Wht(String agent1Wht) {
        this.agent1Wht = agent1Wht;
    }
    @Column(name = "AGENT_1_NET_COMMISSION")
    public String getAgent1NetComission() {
        return agent1NetComission;
    }

    public void setAgent1NetComission(String agent1NetComission) {
        this.agent1NetComission = agent1NetComission;
    }
    @Column(name = "AGENT2_GROSS_COMMISSION")
    public String getAgent2GrossComission() {
        return agent2GrossComission;
    }

    public void setAgent2GrossComission(String agent2GrossComission) {
        this.agent2GrossComission = agent2GrossComission;
    }
    @Column(name = "AGENT2_WHT")
    public String getAgent2Wht() {
        return agent2Wht;
    }

    public void setAgent2Wht(String agent2Wht) {
        this.agent2Wht = agent2Wht;
    }
    @Column(name = "AGENT2_NET_COMMISSION")
    public String getAgent2NetComission() {
        return agent2NetComission;
    }

    public void setAgent2NetComission(String agent2NetComission) {
        this.agent2NetComission = agent2NetComission;
    }
    @Column(name = "SALESTEAM_GROSS_COMMISSION")
    public String getSalesTeamGrossComission() {
        return salesTeamGrossComission;
    }

    public void setSalesTeamGrossComission(String salesTeamGrossComission) {
        this.salesTeamGrossComission = salesTeamGrossComission;
    }
    @Column(name = "SALESTEAM_WHT")
    public String getSalesTeamWht() {
        return salesTeamWht;
    }

    public void setSalesTeamWht(String salesTeamWht) {
        this.salesTeamWht = salesTeamWht;
    }
    @Column(name = "SALESTEAM_NET_COMMISSION")
    public String getSalesTeamNetComission() {
        return salesTeamNetComission;
    }

    public void setSalesTeamNetComission(String salesTeamNetComission) {
        this.salesTeamNetComission = salesTeamNetComission;
    }
    @Column(name = "SUP_PROCESSING_STATUS_ID")
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
    @Column(name = "STATUS")
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    @Column(name = "TOTAL_WHT")
    public String getTotalWht() {
        return totalWht;
    }

    public void setTotalWht(String totalWht) {
        this.totalWht = totalWht;
    }
    @Column(name = "WHT_CONFIG_ID")
    public Long getWhtConfigId() {
        return whtConfigId;
    }

    public void setWhtConfigId(Long whtConfigId) {
        this.whtConfigId = whtConfigId;
    }
    @Column(name = "AGENT1_233")
    public String getAgent1233() {
        return agent1233;
    }

    public void setAgent1233(String agent1233) {
        this.agent1233 = agent1233;
    }
    @Column(name = "AGENT2_233")
    public String getAgent2233() {
        return agent2233;
    }

    public void setAgent2233(String agent2233) {
        this.agent2233 = agent2233;
    }
    @Column(name = "P236")
    public String getP236() {
        return p236;
    }

    public void setP236(String p236) {
        this.p236 = p236;
    }
    @Column(name = "A231")
    public String getA231() {
        return a231;
    }

    public void setA231(String a231) {
        this.a231 = a231;
    }
    @javax.persistence.Transient
    public Date getTransactionStartDate() {
        return transactionStartDate;
    }

    public void setTransactionStartDate(Date transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }
    @javax.persistence.Transient
    public Date getTransactionEndDate() {
        return transactionEndDate;
    }

    public void setTransactionEndDate(Date transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }
    @javax.persistence.Transient
    public Date getTransactionStartDateUpdated() {
        return transactionStartDateUpdated;
    }

    public void setTransactionStartDateUpdated(Date transactionStartDateUpdated) {
        this.transactionStartDateUpdated = transactionStartDateUpdated;
    }
    @javax.persistence.Transient
    public Date getTransactionEndDateUpdated() {
        return transactionEndDateUpdated;
    }

    public void setTransactionEndDateUpdated(Date transactionEndDateUpdated) {
        this.transactionEndDateUpdated = transactionEndDateUpdated;
    }

    @Id
    @Column(name = "PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "BLB_COMM_SETTLEMENT")
	public String getBlbSettlementComission() {
		return blbSettlementComission;
	}

	public void setBlbSettlementComission(String blbSettlementComission) {
		this.blbSettlementComission = blbSettlementComission;
	}
}
