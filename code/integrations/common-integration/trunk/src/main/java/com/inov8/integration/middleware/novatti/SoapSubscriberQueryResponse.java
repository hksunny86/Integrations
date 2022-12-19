
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accountBalance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="accountStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "accountBalance",
    "accountStatus",
    "expiryDate"
})
@XmlRootElement(name = "SoapSubscriberQueryResponse")
public class SoapSubscriberQueryResponse
    extends SoapResponseBase
{

    protected Double accountBalance;
    protected String accountStatus;
    protected String expiryDate;

    /**
     * Gets the value of the accountBalance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAccountBalance() {
        return accountBalance;
    }

    /**
     * Sets the value of the accountBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAccountBalance(Double value) {
        this.accountBalance = value;
    }

    /**
     * Gets the value of the accountStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the value of the accountStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountStatus(String value) {
        this.accountStatus = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

}
