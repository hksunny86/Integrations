
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
 *         &lt;element name="AccountRegisterationInquiryResult" type="{http://tempuri.org/}AccountRegisterInquiryResponse" minOccurs="0"/&gt;
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
    "accountRegisterationInquiryResult"
})
@XmlRootElement(name = "AccountRegisterationInquiryResponse")
public class AccountRegisterationInquiryResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountRegisterationInquiryResult")
    protected AccountRegisterInquiryResponse accountRegisterationInquiryResult;

    /**
     * Gets the value of the accountRegisterationInquiryResult property.
     * 
     * @return
     *     possible object is
     *     {@link AccountRegisterInquiryResponse }
     *     
     */
    public AccountRegisterInquiryResponse getAccountRegisterationInquiryResult() {
        return accountRegisterationInquiryResult;
    }

    /**
     * Sets the value of the accountRegisterationInquiryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountRegisterInquiryResponse }
     *     
     */
    public void setAccountRegisterationInquiryResult(AccountRegisterInquiryResponse value) {
        this.accountRegisterationInquiryResult = value;
    }

}
