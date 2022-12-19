
package com.inov8.integration.middleware.abl.client;

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
 *         &lt;element name="PAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sFrmAcc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sFrmAccType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sToAcc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sToAccType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sNarration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OriginalTransactionDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Commission" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pan",
    "sFrmAcc",
    "sFrmAccType",
    "sToAcc",
    "sToAccType",
    "amount",
    "rrn",
    "sNarration",
    "originalTransactionDateTime",
    "commission"
})
@XmlRootElement(name = "FundsTransfer")
public class FundsTransfer
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "PAN", nillable = true)
    protected String pan;
    @XmlElement(nillable = true)
    protected String sFrmAcc;
    @XmlElement(nillable = true)
    protected String sFrmAccType;
    @XmlElement(nillable = true)
    protected String sToAcc;
    @XmlElement(nillable = true)
    protected String sToAccType;
    @XmlElement(name = "Amount", nillable = true)
    protected String amount;
    @XmlElement(name = "RRN", nillable = true)
    protected String rrn;
    @XmlElement(nillable = true)
    protected String sNarration;
    @XmlElement(name = "OriginalTransactionDateTime", nillable = true)
    protected String originalTransactionDateTime;
    @XmlElement(name = "Commission", nillable = true)
    protected String commission;

    /**
     * Gets the value of the pan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAN() {
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
    public void setPAN(String value) {
        this.pan = value;
    }

    /**
     * Gets the value of the sFrmAcc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFrmAcc() {
        return sFrmAcc;
    }

    /**
     * Sets the value of the sFrmAcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFrmAcc(String value) {
        this.sFrmAcc = value;
    }

    /**
     * Gets the value of the sFrmAccType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFrmAccType() {
        return sFrmAccType;
    }

    /**
     * Sets the value of the sFrmAccType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFrmAccType(String value) {
        this.sFrmAccType = value;
    }

    /**
     * Gets the value of the sToAcc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSToAcc() {
        return sToAcc;
    }

    /**
     * Sets the value of the sToAcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSToAcc(String value) {
        this.sToAcc = value;
    }

    /**
     * Gets the value of the sToAccType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSToAccType() {
        return sToAccType;
    }

    /**
     * Sets the value of the sToAccType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSToAccType(String value) {
        this.sToAccType = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the rrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRRN() {
        return rrn;
    }

    /**
     * Sets the value of the rrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRRN(String value) {
        this.rrn = value;
    }

    /**
     * Gets the value of the sNarration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNarration() {
        return sNarration;
    }

    /**
     * Sets the value of the sNarration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNarration(String value) {
        this.sNarration = value;
    }

    /**
     * Gets the value of the originalTransactionDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalTransactionDateTime() {
        return originalTransactionDateTime;
    }

    /**
     * Sets the value of the originalTransactionDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalTransactionDateTime(String value) {
        this.originalTransactionDateTime = value;
    }

    /**
     * Gets the value of the commission property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommission() {
        return commission;
    }

    /**
     * Sets the value of the commission property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommission(String value) {
        this.commission = value;
    }

}
