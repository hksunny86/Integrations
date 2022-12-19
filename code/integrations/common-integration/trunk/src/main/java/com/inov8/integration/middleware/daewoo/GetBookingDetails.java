
package com.inov8.integration.middleware.daewoo;

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
 *         &lt;element name="CLIENT_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CLIENT_PASSWORD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BOOK_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHONE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "clientid",
    "clientpassword",
    "booknumber",
    "phonenumber"
})
@XmlRootElement(name = "getBookingDetails")
public class GetBookingDetails
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_ID")
    protected String clientid;
    @XmlElement(name = "CLIENT_PASSWORD")
    protected String clientpassword;
    @XmlElement(name = "BOOK_NUMBER")
    protected String booknumber;
    @XmlElement(name = "PHONE_NUMBER")
    protected String phonenumber;

    /**
     * Gets the value of the clientid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTID() {
        return clientid;
    }

    /**
     * Sets the value of the clientid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTID(String value) {
        this.clientid = value;
    }

    /**
     * Gets the value of the clientpassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTPASSWORD() {
        return clientpassword;
    }

    /**
     * Sets the value of the clientpassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTPASSWORD(String value) {
        this.clientpassword = value;
    }

    /**
     * Gets the value of the booknumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBOOKNUMBER() {
        return booknumber;
    }

    /**
     * Sets the value of the booknumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBOOKNUMBER(String value) {
        this.booknumber = value;
    }

    /**
     * Gets the value of the phonenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHONENUMBER() {
        return phonenumber;
    }

    /**
     * Sets the value of the phonenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHONENUMBER(String value) {
        this.phonenumber = value;
    }

}
