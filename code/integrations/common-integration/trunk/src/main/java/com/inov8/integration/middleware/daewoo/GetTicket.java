
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
 *         &lt;element name="CLIENT_TOKEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="USER_PASSWORD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BOOK_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANSACTION_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANSACTION_AMOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANSACTION_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANSACTION_TIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANSACTION_REMARKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "clienttoken",
    "userid",
    "userpassword",
    "booknumber",
    "transactionid",
    "transactionamount",
    "transactiondate",
    "transactiontime",
    "transactionremarks"
})
@XmlRootElement(name = "getTicket")
public class GetTicket
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_TOKEN")
    protected String clienttoken;
    @XmlElement(name = "USER_ID")
    protected String userid;
    @XmlElement(name = "USER_PASSWORD")
    protected String userpassword;
    @XmlElement(name = "BOOK_NUMBER")
    protected String booknumber;
    @XmlElement(name = "TRANSACTION_ID")
    protected String transactionid;
    @XmlElement(name = "TRANSACTION_AMOUNT")
    protected String transactionamount;
    @XmlElement(name = "TRANSACTION_DATE")
    protected String transactiondate;
    @XmlElement(name = "TRANSACTION_TIME")
    protected String transactiontime;
    @XmlElement(name = "TRANSACTION_REMARKS")
    protected String transactionremarks;

    /**
     * Gets the value of the clienttoken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTTOKEN() {
        return clienttoken;
    }

    /**
     * Sets the value of the clienttoken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTTOKEN(String value) {
        this.clienttoken = value;
    }

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * Gets the value of the userpassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERPASSWORD() {
        return userpassword;
    }

    /**
     * Sets the value of the userpassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERPASSWORD(String value) {
        this.userpassword = value;
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
     * Gets the value of the transactionid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONID() {
        return transactionid;
    }

    /**
     * Sets the value of the transactionid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONID(String value) {
        this.transactionid = value;
    }

    /**
     * Gets the value of the transactionamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONAMOUNT() {
        return transactionamount;
    }

    /**
     * Sets the value of the transactionamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONAMOUNT(String value) {
        this.transactionamount = value;
    }

    /**
     * Gets the value of the transactiondate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONDATE() {
        return transactiondate;
    }

    /**
     * Sets the value of the transactiondate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONDATE(String value) {
        this.transactiondate = value;
    }

    /**
     * Gets the value of the transactiontime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONTIME() {
        return transactiontime;
    }

    /**
     * Sets the value of the transactiontime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONTIME(String value) {
        this.transactiontime = value;
    }

    /**
     * Gets the value of the transactionremarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONREMARKS() {
        return transactionremarks;
    }

    /**
     * Sets the value of the transactionremarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONREMARKS(String value) {
        this.transactionremarks = value;
    }

}
