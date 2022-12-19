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
 *         &lt;element name="GetCardBalanceResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getCardBalanceResult"
})
@XmlRootElement(name = "GetCardBalanceResponse")
public class GetCardBalanceResponse {

    @XmlElement(name = "GetCardBalanceResult")
    protected int getCardBalanceResult;

    /**
     * Gets the value of the getCardBalanceResult property.
     */
    public int getGetCardBalanceResult() {
        return getCardBalanceResult;
    }

    /**
     * Sets the value of the getCardBalanceResult property.
     */
    public void setGetCardBalanceResult(int value) {
        this.getCardBalanceResult = value;
    }

}
