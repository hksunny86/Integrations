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
 *         &lt;element name="ConfirmOrderResult" type="{http://tempuri.org/}CardRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "confirmOrderResult"
})
@XmlRootElement(name = "ConfirmOrderResponse")
public class ConfirmOrderResponse {

    @XmlElement(name = "ConfirmOrderResult")
    protected CardRequest confirmOrderResult;

    /**
     * Gets the value of the confirmOrderResult property.
     *
     * @return possible object is
     * {@link CardRequest }
     */
    public CardRequest getConfirmOrderResult() {
        return confirmOrderResult;
    }

    /**
     * Sets the value of the confirmOrderResult property.
     *
     * @param value allowed object is
     *              {@link CardRequest }
     */
    public void setConfirmOrderResult(CardRequest value) {
        this.confirmOrderResult = value;
    }

}
