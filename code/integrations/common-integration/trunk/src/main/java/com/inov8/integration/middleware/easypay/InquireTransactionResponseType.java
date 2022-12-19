
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
 *         &lt;element name="accountNum" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="storeId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="storeName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="paymentToken" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transactionStatus" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transactionAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="transactionDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="paymentTokenExiryDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="transactionPaidDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="msisdn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="paymentMode" type="{http://dto.transaction.partner.pg.systems.com/}TransactionType"/&gt;
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
    "accountNum",
    "storeId",
    "storeName",
    "paymentToken",
    "transactionId",
    "transactionStatus",
    "transactionAmount",
    "transactionDateTime",
    "paymentTokenExiryDateTime",
    "transactionPaidDateTime",
    "msisdn",
    "paymentMode"
})
@XmlRootElement(name = "inquireTransactionResponseType")
public class InquireTransactionResponseType
    extends BaseResponseType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String orderId;
    @XmlElement(required = true)
    protected String accountNum;
    protected long storeId;
    @XmlElement(required = true)
    protected String storeName;
    @XmlElement(required = true)
    protected String paymentToken;
    @XmlElement(required = true)
    protected String transactionId;
    @XmlElement(required = true)
    protected String transactionStatus;
    protected double transactionAmount;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionDateTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar paymentTokenExiryDateTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionPaidDateTime;
    @XmlElement(required = true)
    protected String msisdn;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TransactionType paymentMode;

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
     * Gets the value of the accountNum property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountNum() {
        return accountNum;
    }

    /**
     * Sets the value of the accountNum property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountNum(String value) {
        this.accountNum = value;
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
     * Gets the value of the storeName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Sets the value of the storeName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStoreName(String value) {
        this.storeName = value;
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
     * Gets the value of the transactionStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Sets the value of the transactionStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransactionStatus(String value) {
        this.transactionStatus = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     *
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     *
     */
    public void setTransactionAmount(double value) {
        this.transactionAmount = value;
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
     * Gets the value of the transactionPaidDateTime property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getTransactionPaidDateTime() {
        return transactionPaidDateTime;
    }

    /**
     * Sets the value of the transactionPaidDateTime property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setTransactionPaidDateTime(XMLGregorianCalendar value) {
        this.transactionPaidDateTime = value;
    }

    /**
     * Gets the value of the msisdn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMsisdn(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the paymentMode property.
     *
     * @return
     *     possible object is
     *     {@link TransactionType }
     *
     */
    public TransactionType getPaymentMode() {
        return paymentMode;
    }

    /**
     * Sets the value of the paymentMode property.
     *
     * @param value
     *     allowed object is
     *     {@link TransactionType }
     *
     */
    public void setPaymentMode(TransactionType value) {
        this.paymentMode = value;
    }

}
