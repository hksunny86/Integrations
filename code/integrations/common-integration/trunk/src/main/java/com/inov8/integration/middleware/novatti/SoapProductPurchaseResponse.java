
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
 *         &lt;element name="products" type="{http://soap.api.novatti.com/types}ProductItems" minOccurs="0"/&gt;
 *         &lt;element name="bulletins" type="{http://soap.api.novatti.com/types}Bulletins" minOccurs="0"/&gt;
 *         &lt;element name="wallets" type="{http://soap.api.novatti.com/types}Wallets" minOccurs="0"/&gt;
 *         &lt;element name="transactions" type="{http://soap.api.novatti.com/types}Transactions" minOccurs="0"/&gt;
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
    "products",
    "bulletins",
    "wallets",
    "transactions"
})
@XmlRootElement(name = "SoapProductPurchaseResponse")
public class SoapProductPurchaseResponse
    extends SoapResponseBase
{

    protected ProductItems products;
    protected Bulletins bulletins;
    protected Wallets wallets;
    protected Transactions transactions;

    /**
     * Gets the value of the products property.
     * 
     * @return
     *     possible object is
     *     {@link ProductItems }
     *     
     */
    public ProductItems getProducts() {
        return products;
    }

    /**
     * Sets the value of the products property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductItems }
     *     
     */
    public void setProducts(ProductItems value) {
        this.products = value;
    }

    /**
     * Gets the value of the bulletins property.
     * 
     * @return
     *     possible object is
     *     {@link Bulletins }
     *     
     */
    public Bulletins getBulletins() {
        return bulletins;
    }

    /**
     * Sets the value of the bulletins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bulletins }
     *     
     */
    public void setBulletins(Bulletins value) {
        this.bulletins = value;
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
     * Gets the value of the transactions property.
     * 
     * @return
     *     possible object is
     *     {@link Transactions }
     *     
     */
    public Transactions getTransactions() {
        return transactions;
    }

    /**
     * Sets the value of the transactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transactions }
     *     
     */
    public void setTransactions(Transactions value) {
        this.transactions = value;
    }

}
