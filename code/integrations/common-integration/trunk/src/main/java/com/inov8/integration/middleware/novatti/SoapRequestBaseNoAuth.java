
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoapRequestBaseNoAuth complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapRequestBaseNoAuth"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://soap.api.novatti.com/types}RequestHeaderNoAuth" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapRequestBaseNoAuth", propOrder = {
    "header"
})
@XmlSeeAlso({
    SoapBulletinInfoRequest.class
})
public class SoapRequestBaseNoAuth
    extends SoapRequestBase
{

    protected RequestHeaderNoAuth header;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link RequestHeaderNoAuth }
     *     
     */
    public RequestHeaderNoAuth getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestHeaderNoAuth }
     *     
     */
    public void setHeader(RequestHeaderNoAuth value) {
        this.header = value;
    }

}
