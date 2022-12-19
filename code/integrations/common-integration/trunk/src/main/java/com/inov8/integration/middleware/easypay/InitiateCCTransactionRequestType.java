
package com.inov8.integration.middleware.easypay;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dto.common.pg.systems.com/}BaseRequestType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="storeId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="transactionAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="transactionType" type="{http://dto.transaction.partner.pg.systems.com/}TransactionType"/&gt;
 *         &lt;element name="msisdn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cardType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pan" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="expiryYear" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="expiryMonth" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cvv2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "transactionAmount",
    "transactionType",
    "msisdn",
    "emailAddress",
    "cardType",
    "pan",
    "expiryYear",
    "expiryMonth",
    "cvv2"
})
@XmlRootElement(name = "initiateCCTransactionRequestType")
public class InitiateCCTransactionRequestType
    extends BaseRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String orderId;
    protected long storeId;
    protected double transactionAmount;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TransactionType transactionType;
    @XmlElement(required = true)
    protected String msisdn;
    @XmlElement(required = true)
    protected String emailAddress;
    @XmlElement(required = true)
    protected String cardType;
    @XmlElement(required = true)
    protected String pan;
    @XmlElement(required = true)
    protected String expiryYear;
    @XmlElement(required = true)
    protected String expiryMonth;
    @XmlElement(required = true)
    protected String cvv2;

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
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionType }
     *     
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionType }
     *     
     */
    public void setTransactionType(TransactionType value) {
        this.transactionType = value;
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
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the pan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPan() {
        return pan;
    }

    /**
     * Sets the value of the pan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPan(String value) {
        this.pan = value;
    }

    /**
     * Gets the value of the expiryYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryYear() {
        return expiryYear;
    }

    /**
     * Sets the value of the expiryYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryYear(String value) {
        this.expiryYear = value;
    }

    /**
     * Gets .the value of the expiryMonth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryMonth() {
        return expiryMonth;
    }

    /**
     * Sets the value of the expiryMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryMonth(String value) {
        this.expiryMonth = value;
    }

    /**
     * Gets the value of the cvv2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCvv2() {
        return cvv2;
    }

    /**
     * Sets the value of the cvv2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCvv2(String value) {
        this.cvv2 = value;
    }

}
