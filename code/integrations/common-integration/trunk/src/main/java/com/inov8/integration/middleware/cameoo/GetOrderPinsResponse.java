
package com.inov8.integration.middleware.cameoo;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetOrderPinsResult" type="{http://tempuri.org/}CardRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getOrderPinsResult"
})
@XmlRootElement(name = "GetOrderPinsResponse")
public class GetOrderPinsResponse {

    @XmlElement(name = "GetOrderPinsResult")
    protected CardRequest getOrderPinsResult;

    /**
     * Gets the value of the getOrderPinsResult property.
     *
     * @return possible object is
     * {@link CardRequest }
     */
    public CardRequest getGetOrderPinsResult() {
        return getOrderPinsResult;
    }

    /**
     * Sets the value of the getOrderPinsResult property.
     *
     * @param value allowed object is
     *              {@link CardRequest }
     */
    public void setGetOrderPinsResult(CardRequest value) {
        this.getOrderPinsResult = value;
    }

}
