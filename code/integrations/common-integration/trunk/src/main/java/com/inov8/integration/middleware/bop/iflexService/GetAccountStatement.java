
package com.inov8.integration.middleware.bop.iflexService;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="Request" type="{http://schemas.datacontract.org/2004/07/CoExistance.FlexConsumer}IChannelRequest" minOccurs="0"/&gt;
 *         &lt;element name="FromDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ToDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsMiniStatemnet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    "request",
    "fromDate",
    "toDate",
    "accountNumber",
    "isMiniStatemnet"
})
@XmlRootElement(name = "GetAccountStatement", namespace = "http://tempuri.org/")
public class GetAccountStatement
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Request", namespace = "http://tempuri.org/", nillable = true)
    protected IChannelRequest request;
    @XmlElement(name = "FromDate", namespace = "http://tempuri.org/")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fromDate;
    @XmlElement(name = "ToDate", namespace = "http://tempuri.org/")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar toDate;
    @XmlElement(name = "AccountNumber", namespace = "http://tempuri.org/", nillable = true)
    protected String accountNumber;
    @XmlElement(name = "IsMiniStatemnet", namespace = "http://tempuri.org/")
    protected Boolean isMiniStatemnet;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link IChannelRequest }
     *     
     */
    public IChannelRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link IChannelRequest }
     *     
     */
    public void setRequest(IChannelRequest value) {
        this.request = value;
    }

    /**
     * Gets the value of the fromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFromDate(XMLGregorianCalendar value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setToDate(XMLGregorianCalendar value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the isMiniStatemnet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMiniStatemnet() {
        return isMiniStatemnet;
    }

    /**
     * Sets the value of the isMiniStatemnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMiniStatemnet(Boolean value) {
        this.isMiniStatemnet = value;
    }

}
