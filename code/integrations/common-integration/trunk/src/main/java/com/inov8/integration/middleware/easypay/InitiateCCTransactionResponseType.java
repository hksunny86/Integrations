
package com.inov8.integration.middleware.easypay;

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
 *     &lt;extension base="{http://dto.common.pg.systems.com/}BaseResponseType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="storeId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="paymentToken" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transactionDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="paymentTokenExiryDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="transactionRefNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="authorizationId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="batchNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orderId",
    "storeId",
    "paymentToken",
    "transactionId",
    "transactionDateTime",
    "paymentTokenExiryDateTime",
    "transactionRefNumber",
    "authorizationId",
    "batchNumber"
})
@XmlRootElement(name = "initiateCCTransactionResponseType")
public class InitiateCCTransactionResponseType
    extends BaseResponseType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String orderId;
    protected long storeId;
    @XmlElement(required = true)
    protected String paymentToken;
    @XmlElement(required = true)
    protected String transactionId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionDateTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar paymentTokenExiryDateTime;
    @XmlElement(required = true)
    protected String transactionRefNumber;
    @XmlElement(required = true)
    protected String authorizationId;
    @XmlElement(required = true)
    protected String batchNumber;

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the storeId property.
     * 
     */
    public long getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     */
    public void setStoreId(long value) {
        this.storeId = value;
    }

    /**
     * Gets the value of the paymentToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentToken() {
        return paymentToken;
    }

    /**
     * Sets the value of the paymentToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentToken(String value) {
        this.paymentToken = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the transactionDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * Sets the value of the transactionDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransactionDateTime(XMLGregorianCalendar value) {
        this.transactionDateTime = value;
    }

    /**
     * Gets the value of the paymentTokenExiryDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaymentTokenExiryDateTime() {
        return paymentTokenExiryDateTime;
    }

    /**
     * Sets the value of the paymentTokenExiryDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaymentTokenExiryDateTime(XMLGregorianCalendar value) {
        this.paymentTokenExiryDateTime = value;
    }

    /**
     * Gets the value of the transactionRefNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionRefNumber() {
        return transactionRefNumber;
    }

    /**
     * Sets the value of the transactionRefNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionRefNumber(String value) {
        this.transactionRefNumber = value;
    }

    /**
     * Gets the value of the authorizationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizationId() {
        return authorizationId;
    }

    /**
     * Sets the value of the authorizationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizationId(String value) {
        this.authorizationId = value;
    }

    /**
     * Gets the value of the batchNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchNumber() {
        return batchNumber;
    }

    /**
     * Sets the value of the batchNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchNumber(String value) {
        this.batchNumber = value;
    }

}
