
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
 *         &lt;element name="VerifyPasswordResult" type="{http://schemas.datacontract.org/2004/07/ABLServiceModels}PasswordOutputParams" minOccurs="0"/&gt;
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
    "verifyPasswordResult"
})
@XmlRootElement(name = "VerifyPasswordResponse", namespace = "http://tempuri.org/")
public class VerifyPasswordResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "VerifyPasswordResult", namespace = "http://tempuri.org/", nillable = true)
    protected PasswordOutputParams verifyPasswordResult;

    /**
     * Gets the value of the verifyPasswordResult property.
     * 
     * @return
     *     possible object is
     *     {@link PasswordOutputParams }
     *     
     */
    public PasswordOutputParams getVerifyPasswordResult() {
        return verifyPasswordResult;
    }

    /**
     * Sets the value of the verifyPasswordResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PasswordOutputParams }
     *     
     */
    public void setVerifyPasswordResult(PasswordOutputParams value) {
        this.verifyPasswordResult = value;
    }

}
