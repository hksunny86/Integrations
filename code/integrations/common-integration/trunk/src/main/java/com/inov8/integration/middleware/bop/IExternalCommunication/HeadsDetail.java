
package com.inov8.integration.middleware.bop.IExternalCommunication;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeadsDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeadsDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="HeadAcount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HeadAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="HeadDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeadsDetail", propOrder = {
    "headAcount",
    "headAmount",
    "headDescription"
})
public class HeadsDetail
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "HeadAcount", nillable = true)
    protected String headAcount;
    @XmlElement(name = "HeadAmount")
    protected BigDecimal headAmount;
    @XmlElement(name = "HeadDescription", nillable = true)
    protected String headDescription;

    /**
     * Gets the value of the headAcount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeadAcount() {
        return headAcount;
    }

    /**
     * Sets the value of the headAcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeadAcount(String value) {
        this.headAcount = value;
    }

    /**
     * Gets the value of the headAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHeadAmount() {
        return headAmount;
    }

    /**
     * Sets the value of the headAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHeadAmount(BigDecimal value) {
        this.headAmount = value;
    }

    /**
     * Gets the value of the headDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeadDescription() {
        return headDescription;
    }

    /**
     * Sets the value of the headDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeadDescription(String value) {
        this.headDescription = value;
    }

}
