
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BankList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BankList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountNumberFormatMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountNumberLength" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BankIMD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BankMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SwiftCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankList", propOrder = {
    "accountNumberFormatMessage",
    "accountNumberLength",
    "bankIMD",
    "bankMnemonic",
    "name",
    "swiftCode"
})
public class BankList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountNumberFormatMessage", nillable = true)
    protected String accountNumberFormatMessage;
    @XmlElement(name = "AccountNumberLength", nillable = true)
    protected String accountNumberLength;
    @XmlElement(name = "BankIMD", nillable = true)
    protected String bankIMD;
    @XmlElement(name = "BankMnemonic", nillable = true)
    protected String bankMnemonic;
    @XmlElement(name = "Name", nillable = true)
    protected String name;
    @XmlElement(name = "SwiftCode", nillable = true)
    protected String swiftCode;

    /**
     * Gets the value of the accountNumberFormatMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumberFormatMessage() {
        return accountNumberFormatMessage;
    }

    /**
     * Sets the value of the accountNumberFormatMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumberFormatMessage(String value) {
        this.accountNumberFormatMessage = value;
    }

    /**
     * Gets the value of the accountNumberLength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumberLength() {
        return accountNumberLength;
    }

    /**
     * Sets the value of the accountNumberLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumberLength(String value) {
        this.accountNumberLength = value;
    }

    /**
     * Gets the value of the bankIMD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankIMD() {
        return bankIMD;
    }

    /**
     * Sets the value of the bankIMD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankIMD(String value) {
        this.bankIMD = value;
    }

    /**
     * Gets the value of the bankMnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankMnemonic() {
        return bankMnemonic;
    }

    /**
     * Sets the value of the bankMnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankMnemonic(String value) {
        this.bankMnemonic = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the swiftCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * Sets the value of the swiftCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwiftCode(String value) {
        this.swiftCode = value;
    }

}
