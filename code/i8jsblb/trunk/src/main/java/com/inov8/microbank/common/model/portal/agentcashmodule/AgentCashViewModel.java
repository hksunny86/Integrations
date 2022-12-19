package com.inov8.microbank.common.model.portal.agentcashmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_CASH_MANAGEMENT_VIEW")
public class AgentCashViewModel extends BasePersistableModel {

     private static final long serialVersionUID = -2914631301844610140L;
	 private Long pk;
     private Long cashBankMappingId;
     private Long agentOpeningBalanceId;
     private String msisdn;
     private String agentId;
     private Long transactionId;
     private Date createdOn;
     private String description;
     private Double bankDebitAmount;
     private Double bankCreditAmount;
     private Double bankBalance;
     private Double cashIn;
     private Double cashOut;
     private Double cashBalance;
     private Double totalBalance;
     private Double commission;
     private Date transactionDate;
     private Date transactionDateOnly;


    public AgentCashViewModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
    	setPk(primaryKey);
    }

    @Id
    @Column(name="PK")
    public Long getPk()
    {
        return pk;
    }

    public void setPk( Long pk )
    {
        this.pk = pk;
    }

    @Column(name="CASH_BANK_MAPPING_ID")
    public Long getCashBankMappingId() {
        return this.cashBankMappingId;
    }
    
    public void setCashBankMappingId(Long cashBankMappingId) {
        this.cashBankMappingId = cashBankMappingId;
    }

    @Column(name="AGENT_OPENING_BALANCE_ID")
    public Long getAgentOpeningBalanceId() {
        return this.agentOpeningBalanceId;
    }
    
    public void setAgentOpeningBalanceId(Long agentOpeningBalanceId) {
        this.agentOpeningBalanceId = agentOpeningBalanceId;
    }

    @Column(name="MSISDN")
    public String getMsisdn() {
        return this.msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Column(name="AGENT_ID")
    public String getAgentId() {
        return this.agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name="TRANSACTION_ID")
    public Long getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name="DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="BANK_DEBIT_AMOUNT")
    public Double getBankDebitAmount() {
        return this.bankDebitAmount;
    }
    
    public void setBankDebitAmount(Double bankDebitAmount) {
        this.bankDebitAmount = bankDebitAmount;
    }

    @Column(name="BANK_CREDIT_AMOUNT")
    public Double getBankCreditAmount() {
        return this.bankCreditAmount;
    }
    
    public void setBankCreditAmount(Double bankCreditAmount) {
        this.bankCreditAmount = bankCreditAmount;
    }

    @Column(name="BANK_BALANCE")
    public Double getBankBalance() {
        return this.bankBalance;
    }
    
    public void setBankBalance(Double bankBalance) {
        this.bankBalance = bankBalance;
    }

    @Column(name="CASH_IN")
    public Double getCashIn() {
        return this.cashIn;
    }
    
    public void setCashIn(Double cashIn) {
        this.cashIn = cashIn;
    }

    @Column(name="CASH_OUT")
    public Double getCashOut() {
        return this.cashOut;
    }
    
    public void setCashOut(Double cashOut) {
        this.cashOut = cashOut;
    }

    @Column(name="CASH_BALANCE")
    public Double getCashBalance() {
        return this.cashBalance;
    }
    
    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    @Column(name="TOTAL_BALANCE")
    public Double getTotalBalance() {
        return this.totalBalance;
    }
    
    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    @Column(name="COMMISSION")
    public Double getCommission() {
        return this.commission;
    }
    
    public void setCommission(Double commission) {
        this.commission = commission;
    }

	@Transient
	public String getPrimaryKeyFieldName() {
		return "pk";
	}

	@Transient
	public String getPrimaryKeyParameter() {
	    return "&pk=" + getPk();
	}

    @Column(name="TRANSACTION_DATE")
    public Date getTransactionDate() {
        return this.transactionDate;
    }
    
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    @Column(name="TRANSACTION_DATE_ONLY")
    public Date getTransactionDateOnly() {
        return this.transactionDateOnly;
    }
    
    public void setTransactionDateOnly(Date transactionDateOnly) {
        this.transactionDateOnly = transactionDateOnly;
    }

}