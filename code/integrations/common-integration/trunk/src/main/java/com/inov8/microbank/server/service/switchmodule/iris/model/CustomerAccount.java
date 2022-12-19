/**
 * 
 */
package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.io.Serializable;

/**
 * Project Name: 			Financial-Integration	
 * @author Imran Sarwar
 * Creation Date: 			Nov 19, 2007
 * Creation Time: 			5:05:22 PM
 * Description:				
 */
public class CustomerAccount implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4930503568095282472L;
	public static final String	ACC_STATUS_ACTIVE	= "00";
	public static final String	ACC_STATUS_INACTIVE	= "01";

	private String				number;
	private String				type;
	private String				currency;
	private String				titleOfTheAccount;
	private String				accountBankIMD;
	private String				status;
	private String				availableBalance;
	private String				actualBalance;
	private String				withdrawlLimit;
	private String				availableWithdrawlLimit;
	private String				draftLimit;
	private String				limitExpiryDate;
	private String				currencyName;
	private String				currencyMnemonic;
	private String				currencyDecimalPoints;
	
	private String				openingBalance;
	private String				transactionAmount;
	
	private String				bankName;
    private String				branchCode;
	private String				branchName;
	private String				cardNo;
	private String				cardStatus;
	private String				cardExpiry;
	private String              benificieryIBAN;
	private String				fromBankImd;
	private String				toBankImd;
	private String              transactionType;


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getBenificieryIBAN() {
		return benificieryIBAN;
	}

	public void setBenificieryIBAN(String benificieryIBAN) {
		this.benificieryIBAN = benificieryIBAN;
	}

	public String getFromBankImd() {
		return fromBankImd;
	}

	public void setFromBankImd(String fromBankImd) {
		this.fromBankImd = fromBankImd;
	}

	public String getToBankImd() {
		return toBankImd;
	}

	public void setToBankImd(String toBankImd) {
		this.toBankImd = toBankImd;
	}

	public CustomerAccount(String number, String type, String currency, String status)
	{
		super();
		this.number = number;
		this.type = type;
		this.currency = currency;
		this.status = status;
	}
	
	public CustomerAccount()
	{
		super();
	}

	/**
	 * @return the currency
	 */
	public String getCurrency()
	{
		return currency;
	}

	/**
	 * @return the number
	 */
	public String getNumber()
	{
		return number;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @return the actualBalance
	 */
	public String getActualBalance()
	{
		return actualBalance;
	}

	/**
	 * @param actualBalance the actualBalance to set
	 */
	public void setActualBalance(String actualBalance)
	{
		this.actualBalance = actualBalance;
	}

	/**
	 * @return the availableBalance
	 */
	public String getAvailableBalance()
	{
		return availableBalance;
	}

	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(String availableBalance)
	{
		this.availableBalance = availableBalance;
	}

	/**
	 * @return the availableWithdrawlLimit
	 */
	public String getAvailableWithdrawlLimit()
	{
		return availableWithdrawlLimit;
	}

	/**
	 * @param availableWithdrawlLimit the availableWithdrawlLimit to set
	 */
	public void setAvailableWithdrawlLimit(String availableWithdrawlLimit)
	{
		this.availableWithdrawlLimit = availableWithdrawlLimit;
	}

	/**
	 * @return the currencyDecimalPoints
	 */
	public String getCurrencyDecimalPoints()
	{
		return currencyDecimalPoints;
	}

	/**
	 * @param currencyDecimalPoints the currencyDecimalPoints to set
	 */
	public void setCurrencyDecimalPoints(String currencyDecimalPoints)
	{
		this.currencyDecimalPoints = currencyDecimalPoints;
	}

	/**
	 * @return the currencyMnemonic
	 */
	public String getCurrencyMnemonic()
	{
		return currencyMnemonic;
	}

	/**
	 * @param currencyMnemonic the currencyMnemonic to set
	 */
	public void setCurrencyMnemonic(String currencyMnemonic)
	{
		this.currencyMnemonic = currencyMnemonic;
	}

	/**
	 * @return the currencyName
	 */
	public String getCurrencyName()
	{
		return currencyName;
	}

	/**
	 * @param currencyName the currencyName to set
	 */
	public void setCurrencyName(String currencyName)
	{
		this.currencyName = currencyName;
	}

	/**
	 * @return the draftLimit
	 */
	public String getDraftLimit()
	{
		return draftLimit;
	}

	/**
	 * @param draftLimit the draftLimit to set
	 */
	public void setDraftLimit(String draftLimit)
	{
		this.draftLimit = draftLimit;
	}

	/**
	 * @return the limitExpiryDate
	 */
	public String getLimitExpiryDate()
	{
		return limitExpiryDate;
	}

	/**
	 * @param limitExpiryDate the limitExpiryDate to set
	 */
	public void setLimitExpiryDate(String limitExpiryDate)
	{
		this.limitExpiryDate = limitExpiryDate;
	}

	/**
	 * @return the withdrawlLimit
	 */
	public String getWithdrawlLimit()
	{
		return withdrawlLimit;
	}

	/**
	 * @param withdrawlLimit the withdrawlLimit to set
	 */
	public void setWithdrawlLimit(String withdrawlLimit)
	{
		this.withdrawlLimit = withdrawlLimit;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpeningBalance()
	{
		return openingBalance;
	}

	public void setOpeningBalance(String openingBalance)
	{
		this.openingBalance = openingBalance;
	}

	public String getTransactionAmount()
	{
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	public String getTitleOfTheAccount()
	{
		return titleOfTheAccount;
	}

	public void setTitleOfTheAccount(String titleOfTheAccount)
	{
		this.titleOfTheAccount = titleOfTheAccount;
	}

	public String getAccountBankIMD()
	{
		return accountBankIMD;
	}

	public void setAccountBankIMD(String accountBankIMD)
	{
		this.accountBankIMD = accountBankIMD;
	}

	public String getBankName()
	{
		return bankName;
	}

	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}

	public String getBranchName()
	{
		return branchName;
	}

	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getCardExpiry() {
		return cardExpiry;
	}

	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
	}
}
