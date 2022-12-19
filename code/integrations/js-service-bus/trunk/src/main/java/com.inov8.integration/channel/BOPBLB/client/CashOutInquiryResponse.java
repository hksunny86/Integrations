
package com.inov8.integration.channel.BOPBLB.client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CashOutInquiryResult" type="{http://tempuri.org/}CashoutInquiryResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cashOutInquiryResult"
})
@XmlRootElement(name = "CashOutInquiryResponse")
public class CashOutInquiryResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CashOutInquiryResult")
    protected CashoutInquiryResponse2 cashOutInquiryResult;

    /**
     * Gets the value of the cashOutInquiryResult property.
     * 
     * @return
     *     possible object is
     *     {@link CashoutInquiryResponse2 }
     *     
     */
    public CashoutInquiryResponse2 getCashOutInquiryResult() {
        return cashOutInquiryResult;
    }

    /**
     * Sets the value of the cashOutInquiryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CashoutInquiryResponse2 }
     *     
     */
    public void setCashOutInquiryResult(CashoutInquiryResponse2 value) {
        this.cashOutInquiryResult = value;
    }

}
