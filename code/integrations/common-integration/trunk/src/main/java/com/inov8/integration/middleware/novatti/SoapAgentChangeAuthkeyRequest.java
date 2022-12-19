
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
 *         &lt;element name="newAuthkey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="confirmAuthkey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "newAuthkey",
    "confirmAuthkey"
})
@XmlRootElement(name = "SoapAgentChangeAuthkeyRequest")
public class SoapAgentChangeAuthkeyRequest
    extends SoapRequestBaseWithSession
{

    protected String newAuthkey;
    protected String confirmAuthkey;

    /**
     * Gets the value of the newAuthkey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewAuthkey() {
        return newAuthkey;
    }

    /**
     * Sets the value of the newAuthkey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewAuthkey(String value) {
        this.newAuthkey = value;
    }

    /**
     * Gets the value of the confirmAuthkey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmAuthkey() {
        return confirmAuthkey;
    }

    /**
     * Sets the value of the confirmAuthkey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmAuthkey(String value) {
        this.confirmAuthkey = value;
    }

}
