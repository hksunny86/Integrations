
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoapProductRedeemResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapProductRedeemResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="vouchers" type="{http://soap.api.novatti.com/types}VoucherDetails" minOccurs="0"/&gt;
 *         &lt;element name="wallets" type="{http://soap.api.novatti.com/types}Wallets" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapProductRedeemResponse", propOrder = {
    "vouchers",
    "wallets"
})
@XmlSeeAlso({
    SoapValidateProductRedeemResponse.class,
    SoapMakeProductRedeemResponse.class
})
public class SoapProductRedeemResponse
    extends SoapResponseBase
{

    protected VoucherDetails vouchers;
    protected Wallets wallets;

    /**
     * Gets the value of the vouchers property.
     * 
     * @return
     *     possible object is
     *     {@link VoucherDetails }
     *     
     */
    public VoucherDetails getVouchers() {
        return vouchers;
    }

    /**
     * Sets the value of the vouchers property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoucherDetails }
     *     
     */
    public void setVouchers(VoucherDetails value) {
        this.vouchers = value;
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

}
