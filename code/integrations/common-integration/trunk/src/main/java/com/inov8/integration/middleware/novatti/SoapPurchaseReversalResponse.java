
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
 *         &lt;element name="reversedCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="wallets" type="{http://soap.api.novatti.com/types}Wallets" minOccurs="0"/&gt;
 *         &lt;element name="reversedTransactions" type="{http://soap.api.novatti.com/types}Transactions" minOccurs="0"/&gt;
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
    "reversedCount",
    "wallets",
    "reversedTransactions"
})
@XmlRootElement(name = "SoapPurchaseReversalResponse")
public class SoapPurchaseReversalResponse
    extends SoapResponseBase
{

    protected Integer reversedCount;
    protected Wallets wallets;
    protected Transactions reversedTransactions;

    /**
     * Gets the value of the reversedCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReversedCount() {
        return reversedCount;
    }

    /**
     * Sets the value of the reversedCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReversedCount(Integer value) {
        this.reversedCount = value;
    }

    /**
     * Gets the value of the wallets property.
     * 
     * @return
     *     possible object is
     *     {@link Wallets }
     *     
     */
    public Wallets getWallets() {
        return wallets;
    }

    /**
     * Sets the value of the wallets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wallets }
     *     
     */
    public void setWallets(Wallets value) {
        this.wallets = value;
    }

    /**
     * Gets the value of the reversedTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link Transactions }
     *     
     */
    public Transactions getReversedTransactions() {
        return reversedTransactions;
    }

    /**
     * Sets the value of the reversedTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transactions }
     *     
     */
    public void setReversedTransactions(Transactions value) {
        this.reversedTransactions = value;
    }

}
