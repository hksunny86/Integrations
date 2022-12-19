
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
 *         &lt;element name="currencies" type="{http://soap.api.novatti.com/types}Currencies" minOccurs="0"/&gt;
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
    "currencies"
})
@XmlRootElement(name = "SoapCurrencyInfoResponse")
public class SoapCurrencyInfoResponse
    extends SoapResponseBase
{

    protected Currencies currencies;

    /**
     * Gets the value of the currencies property.
     * 
     * @return
     *     possible object is
     *     {@link Currencies }
     *     
     */
    public Currencies getCurrencies() {
        return currencies;
    }

    /**
     * Sets the value of the currencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Currencies }
     *     
     */
    public void setCurrencies(Currencies value) {
        this.currencies = value;
    }

}
