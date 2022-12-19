
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
 *         &lt;element name="RequestCardResult" type="{http://tempuri.org/}CardRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestCardResult"
})
@XmlRootElement(name = "RequestCardResponse")
public class RequestCardResponse {

    @XmlElement(name = "RequestCardResult")
    protected CardRequest requestCardResult;

    /**
     * Gets the value of the requestCardResult property.
     *
     * @return possible object is
     * {@link CardRequest }
     */
    public CardRequest getRequestCardResult() {
        return requestCardResult;
    }

    /**
     * Sets the value of the requestCardResult property.
     *
     * @param value allowed object is
     *              {@link CardRequest }
     */
    public void setRequestCardResult(CardRequest value) {
        this.requestCardResult = value;
    }

}
