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
 *         &lt;element name="GetCardBalanceListResult" type="{http://tempuri.org/}ArrayOfCardBalance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getCardBalanceListResult"
})
@XmlRootElement(name = "GetCardBalanceListResponse")
public class GetCardBalanceListResponse {

    @XmlElement(name = "GetCardBalanceListResult")
    protected ArrayOfCardBalance getCardBalanceListResult;

    /**
     * Gets the value of the getCardBalanceListResult property.
     *
     * @return possible object is
     * {@link ArrayOfCardBalance }
     */
    public ArrayOfCardBalance getGetCardBalanceListResult() {
        return getCardBalanceListResult;
    }

    /**
     * Sets the value of the getCardBalanceListResult property.
     *
     * @param value allowed object is
     *              {@link ArrayOfCardBalance }
     */
    public void setGetCardBalanceListResult(ArrayOfCardBalance value) {
        this.getCardBalanceListResult = value;
    }

}
