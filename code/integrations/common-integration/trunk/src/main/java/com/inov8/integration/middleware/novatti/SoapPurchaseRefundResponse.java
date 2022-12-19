
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
 *         &lt;element name="wallets" type="{http://soap.api.novatti.com/types}Wallets" minOccurs="0"/&gt;
 *         &lt;element name="refundTransactions" type="{http://soap.api.novatti.com/types}Transactions" minOccurs="0"/&gt;
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
    "wallets",
    "refundTransactions"
})
@XmlRootElement(name = "SoapPurchaseRefundResponse")
public class SoapPurchaseRefundResponse
    extends SoapResponseBase
{

    protected Wallets wallets;
    protected Transactions refundTransactions;

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
     * Gets the value of the refundTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link Transactions }
     *     
     */
    public Transactions getRefundTransactions() {
        return refundTransactions;
    }

    /**
     * Sets the value of the refundTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transactions }
     *     
     */
    public void setRefundTransactions(Transactions value) {
        this.refundTransactions = value;
    }

}
