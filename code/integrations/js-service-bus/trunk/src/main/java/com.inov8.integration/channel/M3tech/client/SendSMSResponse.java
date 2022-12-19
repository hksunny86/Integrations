
package com.inov8.integration.channel.M3tech.client;

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
 *         &lt;element name="SendSMSResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "sendSMSResult"
})
@XmlRootElement(name = "SendSMSResponse")
public class SendSMSResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SendSMSResult")
    protected String sendSMSResult;

    /**
     * Gets the value of the sendSMSResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendSMSResult() {
        return sendSMSResult;
    }

    /**
     * Sets the value of the sendSMSResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendSMSResult(String value) {
        this.sendSMSResult = value;
    }

}
