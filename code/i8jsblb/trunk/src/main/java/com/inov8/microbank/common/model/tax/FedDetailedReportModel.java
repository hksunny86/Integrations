package com.inov8.microbank.common.model.tax;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Attique on 8/18/2017.
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FED_DETAILED_REPORT")
public class FedDetailedReportModel extends BasePersistableModel implements Serializable {

    private Long pk;
    private Long taxRegimeId;
    private Long taxPayerId;
    private Long appUserTypeId;
    private String accountType;
    private String transactionId;
    private String fedRate;
    private Long productId;
    private String productName;
    private Date transactionDate;
    private Date transactionLastUpdatedOn;
    private String amount;
    private String inclusiveCharges;
    private String exclusiveCharges;
    private String fed;
    private String bankComission;
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
    private String taxRegime;
/*    private Long agentCommissionSum;
    private Date transactionDate;*/
private Date transactionStartDate;
    private Date transactionEndDate;
    private Date transactionStartDateUpdated;
    private Date transactionEndDateUpdated;
    
    private String blbSettlementComission;

    public FedDetailedReportModel(){

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
        return "&taxRegimeId=" + getPk();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        return "taxRegimeId";
    }

    @Column(name = "TAX_PAYER_ID")
    public Long getTaxPayerId() {
        return taxPayerId;
    }

    public void setTaxPayerId(Long taxPayerId) {
        this.taxPayerId = taxPayerId;
    }
    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }
    @Column(name = "ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    @Column(name = "TRANSACTION_CODE")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    @Column(name = "FED_RATE")
    public String getFedRate() {
        return fedRate;
    }

    public void setFedRate(String fedRate) {
        this.fedRate = fedRate;
    }
    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "CREATED_ON")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    @Column(name = "UPDATED_ON")
    public Date getTransactionLastUpdatedOn() {
        return transactionLastUpdatedOn;
    }

    public void setTransactionLastUpdatedOn(Date transactionLastUpdatedOn) {
        this.transactionLastUpdatedOn = transactionLastUpdatedOn;
    }
    @Column(name = "TRANSACTION_AMOUNT")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    @Column(name = "SERVICE_CHARGES_INCLUSIVE")
    public String getInclusiveCharges() {
        return inclusiveCharges;
    }

    public void setInclusiveCharges(String inclusiveCharges) {
        this.inclusiveCharges = inclusiveCharges;
    }
    @Column(name = "SERVICE_CHARGES_EXCLUSIVE")
    public String getExclusiveCharges() {
        return exclusiveCharges;
    }

    public void setExclusiveCharges(String exclusiveCharges) {
        this.exclusiveCharges = exclusiveCharges;
    }
    @Column(name = "FED")
    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }
    @Column(name = "TO_BANK")
    public String getBankComission() {
        return bankComission;
    }

    public void setBankComission(String bankComission) {
        this.bankComission = bankComission;
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
    @Column(name = "AGENT1_NET_COMMISSION")
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

    @Column(name = "SUP_PROCESSING_STATUS_ID")
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
    @Column(name = "PROCESSING_STATUS_NAME")
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Column(name = "TAX_REGIME")
    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
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
    @Column(name = "TAX_REGIME_ID", insertable=false, updatable=false)
    public Long getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(Long taxRegimeId) {
        this.taxRegimeId = taxRegimeId;
    }

    @Transient
    public Date getTransactionStartDate() {
        return transactionStartDate;
    }

    public void setTransactionStartDate(Date transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    @Transient
    public Date getTransactionEndDate() {
        return transactionEndDate;
    }

    public void setTransactionEndDate(Date transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }
    @Transient
    public Date getTransactionStartDateUpdated() {
        return transactionStartDateUpdated;
    }

    public void setTransactionStartDateUpdated(Date transactionStartDateUpdated) {
        this.transactionStartDateUpdated = transactionStartDateUpdated;
    }
    @Transient
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

