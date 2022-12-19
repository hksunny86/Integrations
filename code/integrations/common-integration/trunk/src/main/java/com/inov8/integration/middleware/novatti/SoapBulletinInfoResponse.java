
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bulletins" type="{http://soap.api.novatti.com/types}Bulletins" minOccurs="0"/&gt;
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
    "bulletins"
})
@XmlRootElement(name = "SoapBulletinInfoResponse")
public class SoapBulletinInfoResponse
    extends SoapResponseBase
{

    protected Bulletins bulletins;

    /**
     * Gets the value of the bulletins property.
     * 
     * @return
     *     possible object is
     *     {@link Bulletins }
     *     
     */
    public Bulletins getBulletins() {
        return bulletins;
    }

    /**
     * Sets the value of the bulletins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bulletins }
     *     
     */
    public void setBulletins(Bulletins value) {
        this.bulletins = value;
    }

}
