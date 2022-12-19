package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author: Atif
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SETTLEMENT_CLOSING_BAL_VIEW")
public class SettlementClosingBalanceViewModel extends BasePersistableModel
		implements Serializable {

	private static final long serialVersionUID = -3795616964257045810L;

	private Long pk;
	private Long stakeHolderBankInfoId;
	private String blbAccountNumber;
	private String ofAccountNumber;
	private String bbAccountTitle;
	private String coreAccountTitle;
	private Double openingBalance;
	private Double closingBalance;
	private Double debitMovement;
	private Double creditMovement;
	private Long accountType;
	private Long cmshAcctTypeId;
	private Long commStakeHolderId;
	private Date statsDate;

	public SettlementClosingBalanceViewModel() {
	}

	@Column(name = "BB_ACCOUNT_NUMBER")
	public String getBlbAccountNumber() {
		return blbAccountNumber;
	}

	@Id
	@Column(name = "PK")
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getPk();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "pk";
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&pk=" + getPk();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long arg0) {
		setPk(arg0);
	}
	
	public void setBlbAccountNumber(String blbAccountNumber) {
		this.blbAccountNumber = blbAccountNumber;
	}

	@Column(name = "OF_ACCOUNT_NUMBER")
	public String getOfAccountNumber() {
		return ofAccountNumber;
	}

	public void setOfAccountNumber(String ofAccountNumber) {
		this.ofAccountNumber = ofAccountNumber;
	}

	@Column(name = "OPENING_BANALCE")
	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	@Column(name = "CLOSING_BANALCE")
	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	@Column(name = "DEBIT_MOVEMENT")
	public Double getDebitMovement() {
		return debitMovement;
	}

	public void setDebitMovement(Double debitMovement) {
		this.debitMovement = debitMovement;
	}

	@Column(name = "CREDIT_MOVEMENT")
	public Double getCreditMovement() {
		return creditMovement;
	}

	public void setCreditMovement(Double creditMovement) {
		this.creditMovement = creditMovement;
	}

	@Column(name = "OLA_CUSTOMER_ACCOUNT_TYPE_ID")
	public Long getAccountType() {
		return accountType;
	}

	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}

	@Column(name = "BB_ACCOUNT_TITLE")
	public String getBbAccountTitle() {
		return bbAccountTitle;
	}

	public void setBbAccountTitle(String bbAccountTitle) {
		this.bbAccountTitle = bbAccountTitle;
	}

	@Column(name = "CORE_ACCOUNT_TITLE")
	public String getCoreAccountTitle() {
		return coreAccountTitle;
	}

	public void setCoreAccountTitle(String coreAccountTitle) {
		this.coreAccountTitle = coreAccountTitle;
	}

	@Column(name = "STATS_DATE")
	public Date getStatsDate() {
		return statsDate;
	}

	public void setStatsDate(Date statsDate) {
		this.statsDate = statsDate;
	}

	@Column(name = "CMSHACCTTYPE_ID")
	public Long getCmshAcctTypeId() {
		return cmshAcctTypeId;
	}

	public void setCmshAcctTypeId(Long cmshAcctTypeId) {
		this.cmshAcctTypeId = cmshAcctTypeId;
	}

	@Column(name = "COMMISSION_STAKEHOLDER_ID")
	public Long getCommStakeHolderId() {
		return commStakeHolderId;
	}

	public void setCommStakeHolderId(Long commStakeHolderId) {
		this.commStakeHolderId = commStakeHolderId;
	}

	@Column(name = "STAKEHOLDER_BANK_INFO_ID")
	public Long getStakeHolderBankInfoId()
	{
		return stakeHolderBankInfoId;
	}

	public void setStakeHolderBankInfoId(Long stakeHolderBankInfoId)
	{
		this.stakeHolderBankInfoId = stakeHolderBankInfoId;
	}
}