
package com.inov8.integration.middleware.novatti;

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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="msisdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originalTransactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="voucherSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productEan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reasonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="refundUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "msisdn",
    "originalTransactionId",
    "voucherSerial",
    "productEan",
    "reasonCode",
    "comments",
    "refundUserID"
})
@XmlRootElement(name = "SoapPurchaseRefundRequest")
public class SoapPurchaseRefundRequest
    extends SoapRequestBaseWithSession
{

    protected String msisdn;
    protected String originalTransactionId;
    protected String voucherSerial;
    protected String productEan;
    protected String reasonCode;
    protected String comments;
    protected String refundUserID;

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
     * Gets the value of the originalTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    /**
     * Sets the value of the originalTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalTransactionId(String value) {
        this.originalTransactionId = value;
    }

    /**
     * Gets the value of the voucherSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVoucherSerial() {
        return voucherSerial;
    }

    /**
     * Sets the value of the voucherSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVoucherSerial(String value) {
        this.voucherSerial = value;
    }

    /**
     * Gets the value of the productEan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductEan() {
        return productEan;
    }

    /**
     * Sets the value of the productEan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductEan(String value) {
        this.productEan = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the refundUserID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefundUserID() {
        return refundUserID;
    }

    /**
     * Sets the value of the refundUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefundUserID(String value) {
        this.refundUserID = value;
    }

}
