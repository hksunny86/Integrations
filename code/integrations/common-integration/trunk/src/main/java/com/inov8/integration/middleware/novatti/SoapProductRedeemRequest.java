
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoapProductRedeemRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapProductRedeemRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="vouchers" type="{http://soap.api.novatti.com/types}VoucherKeys" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapProductRedeemRequest", propOrder = {
    "vouchers"
})
@XmlSeeAlso({
    SoapValidateProductRedeemRequest.class,
    SoapMakeProductRedeemRequest.class
})
public class SoapProductRedeemRequest
    extends SoapRequestBaseWithSession
{

    protected VoucherKeys vouchers;

    /**
     * Gets the value of the vouchers property.
     * 
     * @return
     *     possible object is
     *     {@link VoucherKeys }
     *     
     */
    public VoucherKeys getVouchers() {
        return vouchers;
    }

    /**
     * Sets the value of the vouchers property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoucherKeys }
     *     
     */
    public void setVouchers(VoucherKeys value) {
        this.vouchers = value;
    }

}
