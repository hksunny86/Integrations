
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuditLoggingRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuditLoggingRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AmountCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AuthorizationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Balance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryAccountTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryNickName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillDueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillingMonth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BranchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DateRange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LoginDatetime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LogoutDatetime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NewCardStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfMonths" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumberofLeaves" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PurposeID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StatementPeriodType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ToAccountBankImd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ToAccountBankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ToAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionReferencNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuditLoggingRequest", propOrder = {
    "accountNumber",
    "accountTitle",
    "accountType",
    "accountTypeCode",
    "amount",
    "amountCurrency",
    "authorizationId",
    "balance",
    "beneficiaryAccountTitle",
    "beneficiaryNickName",
    "billDueDate",
    "billingMonth",
    "branchName",
    "cardType",
    "companyCategory",
    "companyId",
    "consumerNumber",
    "currencyCode",
    "dateRange",
    "loginDatetime",
    "logoutDatetime",
    "newCardStatus",
    "numberOfMonths",
    "numberofLeaves",
    "purposeID",
    "statementPeriodType",
    "toAccountBankImd",
    "toAccountBankName",
    "toAccountNumber",
    "transactionReferencNumber",
    "transactionResponseCode"
})
public class AuditLoggingRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountNumber", nillable = true)
    protected String accountNumber;
    @XmlElement(name = "AccountTitle", nillable = true)
    protected String accountTitle;
    @XmlElement(name = "AccountType", nillable = true)
    protected String accountType;
    @XmlElement(name = "AccountTypeCode", nillable = true)
    protected String accountTypeCode;
    @XmlElement(name = "Amount", nillable = true)
    protected String amount;
    @XmlElement(name = "AmountCurrency", nillable = true)
    protected String amountCurrency;
    @XmlElement(name = "AuthorizationId", nillable = true)
    protected String authorizationId;
    @XmlElement(name = "Balance", nillable = true)
    protected String balance;
    @XmlElement(name = "BeneficiaryAccountTitle", nillable = true)
    protected String beneficiaryAccountTitle;
    @XmlElement(name = "BeneficiaryNickName", nillable = true)
    protected String beneficiaryNickName;
    @XmlElement(name = "BillDueDate", nillable = true)
    protected String billDueDate;
    @XmlElement(name = "BillingMonth", nillable = true)
    protected String billingMonth;
    @XmlElement(name = "BranchName", nillable = true)
    protected String branchName;
    @XmlElement(name = "CardType", nillable = true)
    protected String cardType;
    @XmlElement(name = "CompanyCategory", nillable = true)
    protected String companyCategory;
    @XmlElement(name = "CompanyId", nillable = true)
    protected String companyId;
    @XmlElement(name = "ConsumerNumber", nillable = true)
    protected String consumerNumber;
    @XmlElement(name = "CurrencyCode", nillable = true)
    protected String currencyCode;
    @XmlElement(name = "DateRange", nillable = true)
    protected String dateRange;
    @XmlElement(name = "LoginDatetime", nillable = true)
    protected String loginDatetime;
    @XmlElement(name = "LogoutDatetime", nillable = true)
    protected String logoutDatetime;
    @XmlElement(name = "NewCardStatus", nillable = true)
    protected String newCardStatus;
    @XmlElement(name = "NumberOfMonths", nillable = true)
    protected String numberOfMonths;
    @XmlElement(name = "NumberofLeaves", nillable = true)
    protected String numberofLeaves;
    @XmlElement(name = "PurposeID", nillable = true)
    protected String purposeID;
    @XmlElement(name = "StatementPeriodType", nillable = true)
    protected String statementPeriodType;
    @XmlElement(name = "ToAccountBankImd", nillable = true)
    protected String toAccountBankImd;
    @XmlElement(name = "ToAccountBankName", nillable = true)
    protected String toAccountBankName;
    @XmlElement(name = "ToAccountNumber", nillable = true)
    protected String toAccountNumber;
    @XmlElement(name = "TransactionReferencNumber", nillable = true)
    protected String transactionReferencNumber;
    @XmlElement(name = "TransactionResponseCode", nillable = true)
    protected String transactionResponseCode;

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the accountTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountTitle() {
        return accountTitle;
    }

    /**
     * Sets the value of the accountTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountTitle(String value) {
        this.accountTitle = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the accountTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    /**
     * Sets the value of the accountTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountTypeCode(String value) {
        this.accountTypeCode = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the amountCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmountCurrency() {
        return amountCurrency;
    }

    /**
     * Sets the value of the amountCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmountCurrency(String value) {
        this.amountCurrency = value;
    }

    /**
     * Gets the value of the authorizationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizationId() {
        return authorizationId;
    }

    /**
     * Sets the value of the authorizationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizationId(String value) {
        this.authorizationId = value;
    }

    /**
     * Gets the value of the balance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalance(String value) {
        this.balance = value;
    }

    /**
     * Gets the value of the beneficiaryAccountTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryAccountTitle() {
        return beneficiaryAccountTitle;
    }

    /**
     * Sets the value of the beneficiaryAccountTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryAccountTitle(String value) {
        this.beneficiaryAccountTitle = value;
    }

    /**
     * Gets the value of the beneficiaryNickName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryNickName() {
        return beneficiaryNickName;
    }

    /**
     * Sets the value of the beneficiaryNickName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryNickName(String value) {
        this.beneficiaryNickName = value;
    }

    /**
     * Gets the value of the billDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillDueDate() {
        return billDueDate;
    }

    /**
     * Sets the value of the billDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillDueDate(String value) {
        this.billDueDate = value;
    }

    /**
     * Gets the value of the billingMonth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingMonth() {
        return billingMonth;
    }

    /**
     * Sets the value of the billingMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingMonth(String value) {
        this.billingMonth = value;
    }

    /**
     * Gets the value of the branchName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * Sets the value of the branchName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchName(String value) {
        this.branchName = value;
    }

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the companyCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyCategory() {
        return companyCategory;
    }

    /**
     * Sets the value of the companyCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyCategory(String value) {
        this.companyCategory = value;
    }

    /**
     * Gets the value of the companyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Sets the value of the companyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyId(String value) {
        this.companyId = value;
    }

    /**
     * Gets the value of the consumerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNumber() {
        return consumerNumber;
    }

    /**
     * Sets the value of the consumerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNumber(String value) {
        this.consumerNumber = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the dateRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateRange() {
        return dateRange;
    }

    /**
     * Sets the value of the dateRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRange(String value) {
        this.dateRange = value;
    }

    /**
     * Gets the value of the loginDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginDatetime() {
        return loginDatetime;
    }

    /**
     * Sets the value of the loginDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginDatetime(String value) {
        this.loginDatetime = value;
    }

    /**
     * Gets the value of the logoutDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogoutDatetime() {
        return logoutDatetime;
    }

    /**
     * Sets the value of the logoutDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogoutDatetime(String value) {
        this.logoutDatetime = value;
    }

    /**
     * Gets the value of the newCardStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCardStatus() {
        return newCardStatus;
    }

    /**
     * Sets the value of the newCardStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCardStatus(String value) {
        this.newCardStatus = value;
    }

    /**
     * Gets the value of the numberOfMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfMonths() {
        return numberOfMonths;
    }

    /**
     * Sets the value of the numberOfMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfMonths(String value) {
        this.numberOfMonths = value;
    }

    /**
     * Gets the value of the numberofLeaves property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberofLeaves() {
        return numberofLeaves;
    }

    /**
     * Sets the value of the numberofLeaves property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberofLeaves(String value) {
        this.numberofLeaves = value;
    }

    /**
     * Gets the value of the purposeID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeID() {
        return purposeID;
    }

    /**
     * Sets the value of the purposeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeID(String value) {
        this.purposeID = value;
    }

    /**
     * Gets the value of the statementPeriodType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatementPeriodType() {
        return statementPeriodType;
    }

    /**
     * Sets the value of the statementPeriodType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatementPeriodType(String value) {
        this.statementPeriodType = value;
    }

    /**
     * Gets the value of the toAccountBankImd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToAccountBankImd() {
        return toAccountBankImd;
    }

    /**
     * Sets the value of the toAccountBankImd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToAccountBankImd(String value) {
        this.toAccountBankImd = value;
    }

    /**
     * Gets the value of the toAccountBankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToAccountBankName() {
        return toAccountBankName;
    }

    /**
     * Sets the value of the toAccountBankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToAccountBankName(String value) {
        this.toAccountBankName = value;
    }

    /**
     * Gets the value of the toAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToAccountNumber() {
        return toAccountNumber;
    }

    /**
     * Sets the value of the toAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToAccountNumber(String value) {
        this.toAccountNumber = value;
    }

    /**
     * Gets the value of the transactionReferencNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionReferencNumber() {
        return transactionReferencNumber;
    }

    /**
     * Sets the value of the transactionReferencNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionReferencNumber(String value) {
        this.transactionReferencNumber = value;
    }

    /**
     * Gets the value of the transactionResponseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionResponseCode() {
        return transactionResponseCode;
    }

    /**
     * Sets the value of the transactionResponseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionResponseCode(String value) {
        this.transactionResponseCode = value;
    }

}
