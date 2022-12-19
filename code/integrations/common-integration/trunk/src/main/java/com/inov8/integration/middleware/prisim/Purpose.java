
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Purpose complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Purpose"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PurposeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PurposeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Purpose", propOrder = {
    "purposeId",
    "purposeText"
})
public class Purpose
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "PurposeId", nillable = true)
    protected String purposeId;
    @XmlElement(name = "PurposeText", nillable = true)
    protected String purposeText;

    /**
     * Gets the value of the purposeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeId() {
        return purposeId;
    }

    /**
     * Sets the value of the purposeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeId(String value) {
        this.purposeId = value;
    }

    /**
     * Gets the value of the purposeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeText() {
        return purposeText;
    }

    /**
     * Sets the value of the purposeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeText(String value) {
        this.purposeText = value;
    }

}
