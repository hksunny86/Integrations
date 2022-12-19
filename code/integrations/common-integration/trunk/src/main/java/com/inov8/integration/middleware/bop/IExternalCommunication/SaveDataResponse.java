
package com.inov8.integration.middleware.bop.IExternalCommunication;

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
 *         &lt;element name="SaveDataResult" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
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
    "saveDataResult"
})
@XmlRootElement(name = "SaveDataResponse", namespace = "http://tempuri.org/")
public class SaveDataResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SaveDataResult", namespace = "http://tempuri.org/")
    protected Short saveDataResult;

    /**
     * Gets the value of the saveDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSaveDataResult() {
        return saveDataResult;
    }

    /**
     * Sets the value of the saveDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSaveDataResult(Short value) {
        this.saveDataResult = value;
    }

}
