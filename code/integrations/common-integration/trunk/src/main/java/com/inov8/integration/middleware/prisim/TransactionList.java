
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AmountCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Balance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ClosingBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CreditAmountCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CreditAmountValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DebitAmountCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DebitAmountValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="InstrumentNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OpeningBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranValueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionList", propOrder = {
    "amount",
    "amountCurrency",
    "balance",
    "closingBalance",
    "creditAmountCurrency",
    "creditAmountValue",
    "debitAmountCurrency",
    "debitAmountValue",
    "instrumentNo",
    "openingBalance",
    "tranDate",
    "tranDescription",
    "tranTime",
    "tranValueDate",
    "transactionReferenceNumber",
    "transactionStatus"
})
public class TransactionList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Amount", nillable = true)
    protected String amount;
    @XmlElement(name = "AmountCurrency", nillable = true)
    protected String amountCurrency;
    @XmlElement(name = "Balance", nillable = true)
    protected String balance;
    @XmlElement(name = "ClosingBalance", nillable = true)
    protected String closingBalance;
    @XmlElement(name = "CreditAmountCurrency", nillable = true)
    protected String creditAmountCurrency;
    @XmlElement(name = "CreditAmountValue", nillable = true)
    protected String creditAmountValue;
    @XmlElement(name = "DebitAmountCurrency", nillable = true)
    protected String debitAmountCurrency;
    @XmlElement(name = "DebitAmountValue", nillable = true)
    protected String debitAmountValue;
    @XmlElement(name = "InstrumentNo", nillable = true)
    protected String instrumentNo;
    @XmlElement(name = "OpeningBalance", nillable = true)
    protected String openingBalance;
    @XmlElement(name = "TranDate", nillable = true)
    protected String tranDate;
    @XmlElement(name = "TranDescription", nillable = true)
    protected String tranDescription;
    @XmlElement(name = "TranTime", nillable = true)
    protected String tranTime;
    @XmlElement(name = "TranValueDate", nillable = true)
    protected String tranValueDate;
    @XmlElement(name = "TransactionReferenceNumber", nillable = true)
    protected String transactionReferenceNumber;
    @XmlElement(name = "TransactionStatus", nillable = true)
    protected String transactionStatus;

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
     * Gets the value of the closingBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosingBalance() {
        return closingBalance;
    }

    /**
     * Sets the value of the closingBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosingBalance(String value) {
        this.closingBalance = value;
    }

    /**
     * Gets the value of the creditAmountCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditAmountCurrency() {
        return creditAmountCurrency;
    }

    /**
     * Sets the value of the creditAmountCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditAmountCurrency(String value) {
        this.creditAmountCurrency = value;
    }

    /**
     * Gets the value of the creditAmountValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditAmountValue() {
        return creditAmountValue;
    }

    /**
     * Sets the value of the creditAmountValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditAmountValue(String value) {
        this.creditAmountValue = value;
    }

    /**
     * Gets the value of the debitAmountCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebitAmountCurrency() {
        return debitAmountCurrency;
    }

    /**
     * Sets the value of the debitAmountCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebitAmountCurrency(String value) {
        this.debitAmountCurrency = value;
    }

    /**
     * Gets the value of the debitAmountValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebitAmountValue() {
        return debitAmountValue;
    }

    /**
     * Sets the value of the debitAmountValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebitAmountValue(String value) {
        this.debitAmountValue = value;
    }

    /**
     * Gets the value of the instrumentNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrumentNo() {
        return instrumentNo;
    }

    /**
     * Sets the value of the instrumentNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrumentNo(String value) {
        this.instrumentNo = value;
    }

    /**
     * Gets the value of the openingBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpeningBalance() {
        return openingBalance;
    }

    /**
     * Sets the value of the openingBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpeningBalance(String value) {
        this.openingBalance = value;
    }

    /**
     * Gets the value of the tranDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranDate() {
        return tranDate;
    }

    /**
     * Sets the value of the tranDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranDate(String value) {
        this.tranDate = value;
    }

    /**
     * Gets the value of the tranDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranDescription() {
        return tranDescription;
    }

    /**
     * Sets the value of the tranDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranDescription(String value) {
        this.tranDescription = value;
    }

    /**
     * Gets the value of the tranTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranTime() {
        return tranTime;
    }

    /**
     * Sets the value of the tranTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranTime(String value) {
        this.tranTime = value;
    }

    /**
     * Gets the value of the tranValueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranValueDate() {
        return tranValueDate;
    }

    /**
     * Sets the value of the tranValueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranValueDate(String value) {
        this.tranValueDate = value;
    }

    /**
     * Gets the value of the transactionReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    /**
     * Sets the value of the transactionReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionReferenceNumber(String value) {
        this.transactionReferenceNumber = value;
    }

    /**
     * Gets the value of the transactionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Sets the value of the transactionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionStatus(String value) {
        this.transactionStatus = value;
    }

}
