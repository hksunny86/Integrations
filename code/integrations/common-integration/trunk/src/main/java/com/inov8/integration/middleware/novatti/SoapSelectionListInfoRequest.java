
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
 *         &lt;element name="selectionListId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="selectionListCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="selectionListDefaultSelectionItemId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
    "selectionListId",
    "selectionListCode",
    "selectionListDefaultSelectionItemId"
})
@XmlRootElement(name = "SoapSelectionListInfoRequest")
public class SoapSelectionListInfoRequest
    extends SoapRequestBaseWithSession
{

    protected Integer selectionListId;
    protected String selectionListCode;
    protected Integer selectionListDefaultSelectionItemId;

    /**
     * Gets the value of the selectionListId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSelectionListId() {
        return selectionListId;
    }

    /**
     * Sets the value of the selectionListId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSelectionListId(Integer value) {
        this.selectionListId = value;
    }

    /**
     * Gets the value of the selectionListCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectionListCode() {
        return selectionListCode;
    }

    /**
     * Sets the value of the selectionListCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectionListCode(String value) {
        this.selectionListCode = value;
    }

    /**
     * Gets the value of the selectionListDefaultSelectionItemId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSelectionListDefaultSelectionItemId() {
        return selectionListDefaultSelectionItemId;
    }

    /**
     * Sets the value of the selectionListDefaultSelectionItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSelectionListDefaultSelectionItemId(Integer value) {
        this.selectionListDefaultSelectionItemId = value;
    }

}
