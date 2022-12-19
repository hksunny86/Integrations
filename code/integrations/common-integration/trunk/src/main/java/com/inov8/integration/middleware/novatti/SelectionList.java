
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for SelectionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SelectionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="defaultSelectionItemId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="selectionItems" type="{http://soap.api.novatti.com/types}SelectionItems" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectionList", propOrder = {
    "id",
    "code",
    "defaultSelectionItemId",
    "selectionItems"
})
public class SelectionList implements Serializable {

    protected Integer id;
    protected String code;
    protected Integer defaultSelectionItemId;
    protected SelectionItems selectionItems;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the defaultSelectionItemId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDefaultSelectionItemId() {
        return defaultSelectionItemId;
    }

    /**
     * Sets the value of the defaultSelectionItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDefaultSelectionItemId(Integer value) {
        this.defaultSelectionItemId = value;
    }

    /**
     * Gets the value of the selectionItems property.
     * 
     * @return
     *     possible object is
     *     {@link SelectionItems }
     *     
     */
    public SelectionItems getSelectionItems() {
        return selectionItems;
    }

    /**
     * Sets the value of the selectionItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelectionItems }
     *     
     */
    public void setSelectionItems(SelectionItems value) {
        this.selectionItems = value;
    }

}
