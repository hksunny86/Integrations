
package com.inov8.integration.middleware.abl.mobapp;

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
 *         &lt;element name="LoginResult" type="{http://schemas.datacontract.org/2004/07/ABLServiceModels}LoginOutputParams" minOccurs="0"/&gt;
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
    "loginResult"
})
@XmlRootElement(name = "LoginResponse", namespace = "http://tempuri.org/")
public class LoginResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "LoginResult", namespace = "http://tempuri.org/", nillable = true)
    protected LoginOutputParams loginResult;

    /**
     * Gets the value of the loginResult property.
     * 
     * @return
     *     possible object is
     *     {@link LoginOutputParams }
     *     
     */
    public LoginOutputParams getLoginResult() {
        return loginResult;
    }

    /**
     * Sets the value of the loginResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoginOutputParams }
     *     
     */
    public void setLoginResult(LoginOutputParams value) {
        this.loginResult = value;
    }

}
