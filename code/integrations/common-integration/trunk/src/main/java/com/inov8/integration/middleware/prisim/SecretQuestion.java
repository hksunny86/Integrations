
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretQuestion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretQuestion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SecretQuestionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecretQuestionText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretQuestion", propOrder = {
    "secretQuestionId",
    "secretQuestionText"
})
public class SecretQuestion
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SecretQuestionId", nillable = true)
    protected String secretQuestionId;
    @XmlElement(name = "SecretQuestionText", nillable = true)
    protected String secretQuestionText;

    /**
     * Gets the value of the secretQuestionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretQuestionId() {
        return secretQuestionId;
    }

    /**
     * Sets the value of the secretQuestionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretQuestionId(String value) {
        this.secretQuestionId = value;
    }

    /**
     * Gets the value of the secretQuestionText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretQuestionText() {
        return secretQuestionText;
    }

    /**
     * Sets the value of the secretQuestionText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretQuestionText(String value) {
        this.secretQuestionText = value;
    }

}
