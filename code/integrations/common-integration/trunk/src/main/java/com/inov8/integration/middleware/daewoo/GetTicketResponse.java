
package com.inov8.integration.middleware.daewoo;

import java.io.Serializable;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="getTicketResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getTicketResult"
})
@XmlRootElement(name = "getTicketResponse")
public class GetTicketResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String getTicketResult;

    /**
     * Gets the value of the getTicketResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetTicketResult() {
        return getTicketResult;
    }

    /**
     * Sets the value of the getTicketResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetTicketResult(String value) {
        this.getTicketResult = value;
    }

}
