
package com.inov8.integration.middleware.bop.iflexService;

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
 *         &lt;element name="ChannelActivationResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "channelActivationResult"
})
@XmlRootElement(name = "ChannelActivationResponse", namespace = "http://tempuri.org/")
public class ChannelActivationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ChannelActivationResult", namespace = "http://tempuri.org/", nillable = true)
    protected String channelActivationResult;

    /**
     * Gets the value of the channelActivationResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelActivationResult() {
        return channelActivationResult;
    }

    /**
     * Sets the value of the channelActivationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelActivationResult(String value) {
        this.channelActivationResult = value;
    }

}
