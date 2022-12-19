
package com.inov8.integration.middleware.novatti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelectionItems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SelectionItems"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="selectionItem" type="{http://soap.api.novatti.com/types}SelectionItem" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectionItems", propOrder = {
    "selectionItem"
})
public class SelectionItems implements Serializable {

    @XmlElement(nillable = true)
    protected List<SelectionItem> selectionItem;

    /**
     * Gets the value of the selectionItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the selectionItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSelectionItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SelectionItem }
     * 
     * 
     */
    public List<SelectionItem> getSelectionItem() {
        if (selectionItem == null) {
            selectionItem = new ArrayList<SelectionItem>();
        }
        return this.selectionItem;
    }

}
