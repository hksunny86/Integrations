
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentRule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ListOfAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MaximumAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MinimumAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MultiplierAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentRule", propOrder = {
    "listOfAmount",
    "maximumAmount",
    "minimumAmount",
    "multiplierAmount",
    "type"
})
public class PaymentRule
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ListOfAmount", nillable = true)
    protected String listOfAmount;
    @XmlElement(name = "MaximumAmount", nillable = true)
    protected String maximumAmount;
    @XmlElement(name = "MinimumAmount", nillable = true)
    protected String minimumAmount;
    @XmlElement(name = "MultiplierAmount", nillable = true)
    protected String multiplierAmount;
    @XmlElement(name = "Type", nillable = true)
    protected String type;

    /**
     * Gets the value of the listOfAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListOfAmount() {
        return listOfAmount;
    }

    /**
     * Sets the value of the listOfAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListOfAmount(String value) {
        this.listOfAmount = value;
    }

    /**
     * Gets the value of the maximumAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumAmount() {
        return maximumAmount;
    }

    /**
     * Sets the value of the maximumAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumAmount(String value) {
        this.maximumAmount = value;
    }

    /**
     * Gets the value of the minimumAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinimumAmount() {
        return minimumAmount;
    }

    /**
     * Sets the value of the minimumAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinimumAmount(String value) {
        this.minimumAmount = value;
    }

    /**
     * Gets the value of the multiplierAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultiplierAmount() {
        return multiplierAmount;
    }

    /**
     * Sets the value of the multiplierAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultiplierAmount(String value) {
        this.multiplierAmount = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
