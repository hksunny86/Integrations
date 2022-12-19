
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
 *         &lt;element name="getBookingDetailsResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getBookingDetailsResult"
})
@XmlRootElement(name = "getBookingDetailsResponse")
public class GetBookingDetailsResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String getBookingDetailsResult;

    /**
     * Gets the value of the getBookingDetailsResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetBookingDetailsResult() {
        return getBookingDetailsResult;
    }

    /**
     * Sets the value of the getBookingDetailsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetBookingDetailsResult(String value) {
        this.getBookingDetailsResult = value;
    }

}
