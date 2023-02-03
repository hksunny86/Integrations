
package com.inov8.integration.middleware.abl.mobapp;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LoginOutputParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoginOutputParams"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OutputHeader" type="{http://schemas.datacontract.org/2004/07/ABLServiceModels}OutputHeader" minOccurs="0"/&gt;
 *         &lt;element name="PasswordPositions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginOutputParams", propOrder = {
    "outputHeader",
    "passwordPositions",
    "responseCode",
    "responseDescription"
})
public class LoginOutputParams
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "OutputHeader", nillable = true)
    protected OutputHeader outputHeader;
    @XmlElement(name = "PasswordPositions", nillable = true)
    protected String passwordPositions;
    @XmlElement(name = "ResponseCode", nillable = true)
    protected String responseCode;
    @XmlElement(name = "ResponseDescription", nillable = true)
    protected String responseDescription;

    /**
     * Gets the value of the outputHeader property.
     * 
     * @return
     *     possible object is
     *     {@link OutputHeader }
     *     
     */
    public OutputHeader getOutputHeader() {
        return outputHeader;
    }

    /**
     * Sets the value of the outputHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutputHeader }
     *     
     */
    public void setOutputHeader(OutputHeader value) {
        this.outputHeader = value;
    }

    /**
     * Gets the value of the passwordPositions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordPositions() {
        return passwordPositions;
    }

    /**
     * Sets the value of the passwordPositions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordPositions(String value) {
        this.passwordPositions = value;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    /**
     * Sets the value of the responseDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseDescription(String value) {
        this.responseDescription = value;
    }

}