
package com.inov8.integration.middleware.abl.mobapp;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Account complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Account"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountBranchCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountBranchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AvailableBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BankImd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CurrencyMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CurrentBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IbanAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Account", propOrder = {
    "accountBranchCode",
    "accountBranchName",
    "accountCurrency",
    "accountNumber",
    "accountTitle",
    "accountType",
    "availableBalance",
    "bankImd",
    "currencyMnemonic",
    "currentBalance",
    "ibanAccountNumber"
})
public class Account
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountBranchCode", nillable = true)
    protected String accountBranchCode;
    @XmlElement(name = "AccountBranchName", nillable = true)
    protected String accountBranchName;
    @XmlElement(name = "AccountCurrency", nillable = true)
    protected String accountCurrency;
    @XmlElement(name = "AccountNumber", nillable = true)
    protected String accountNumber;
    @XmlElement(name = "AccountTitle", nillable = true)
    protected String accountTitle;
    @XmlElement(name = "AccountType", nillable = true)
    protected String accountType;
    @XmlElement(name = "AvailableBalance", nillable = true)
    protected String availableBalance;
    @XmlElement(name = "BankImd", nillable = true)
    protected String bankImd;
    @XmlElement(name = "CurrencyMnemonic", nillable = true)
    protected String currencyMnemonic;
    @XmlElement(name = "CurrentBalance", nillable = true)
    protected String currentBalance;
    @XmlElement(name = "IbanAccountNumber", nillable = true)
    protected String ibanAccountNumber;

    /**
     * Gets the value of the accountBranchCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountBranchCode() {
        return accountBranchCode;
    }

    /**
     * Sets the value of the accountBranchCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountBranchCode(String value) {
        this.accountBranchCode = value;
    }

    /**
     * Gets the value of the accountBranchName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountBranchName() {
        return accountBranchName;
    }

    /**
     * Sets the value of the accountBranchName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountBranchName(String value) {
        this.accountBranchName = value;
    }

    /**
     * Gets the value of the accountCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCurrency() {
        return accountCurrency;
    }

    /**
     * Sets the value of the accountCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCurrency(String value) {
        this.accountCurrency = value;
    }

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
     * Gets the value of the availableBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Sets the value of the availableBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailableBalance(String value) {
        this.availableBalance = value;
    }

    /**
     * Gets the value of the bankImd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankImd() {
        return bankImd;
    }

    /**
     * Sets the value of the bankImd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankImd(String value) {
        this.bankImd = value;
    }

    /**
     * Gets the value of the currencyMnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyMnemonic() {
        return currencyMnemonic;
    }

    /**
     * Sets the value of the currencyMnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyMnemonic(String value) {
        this.currencyMnemonic = value;
    }

    /**
     * Gets the value of the currentBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the value of the currentBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentBalance(String value) {
        this.currentBalance = value;
    }

    /**
     * Gets the value of the ibanAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanAccountNumber() {
        return ibanAccountNumber;
    }

    /**
     * Sets the value of the ibanAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanAccountNumber(String value) {
        this.ibanAccountNumber = value;
    }

}
