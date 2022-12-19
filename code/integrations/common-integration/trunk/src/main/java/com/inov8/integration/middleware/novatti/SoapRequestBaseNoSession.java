
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoapRequestBaseNoSession complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapRequestBaseNoSession"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://soap.api.novatti.com/types}RequestHeader" minOccurs="0"/&gt;
 *         &lt;element name="keyValues" type="{http://soap.api.novatti.com/types}Map" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapRequestBaseNoSession", propOrder = {
    "header",
    "keyValues"
})
@XmlSeeAlso({
    SoapAgentLoginRequest.class
})
public class SoapRequestBaseNoSession
    extends SoapRequestBase
{

    protected RequestHeader header;
    protected Map keyValues;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link RequestHeader }
     *     
     */
    public RequestHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestHeader }
     *     
     */
    public void setHeader(RequestHeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the keyValues property.
     * 
     * @return
     *     possible object is
     *     {@link Map }
     *     
     */
    public Map getKeyValues() {
        return keyValues;
    }

    /**
     * Sets the value of the keyValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Map }
     *     
     */
    public void setKeyValues(Map value) {
        this.keyValues = value;
    }

}
