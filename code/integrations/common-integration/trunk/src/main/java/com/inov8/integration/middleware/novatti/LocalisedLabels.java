
package com.inov8.integration.middleware.novatti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocalisedLabels complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocalisedLabels"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="localisedLabel" type="{http://soap.api.novatti.com/types}LocalisedLabel" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalisedLabels", propOrder = {
    "localisedLabel"
})
public class LocalisedLabels implements Serializable {

    @XmlElement(nillable = true)
    protected List<LocalisedLabel> localisedLabel;

    /**
     * Gets the value of the localisedLabel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localisedLabel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalisedLabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalisedLabel }
     * 
     * 
     */
    public List<LocalisedLabel> getLocalisedLabel() {
        if (localisedLabel == null) {
            localisedLabel = new ArrayList<LocalisedLabel>();
        }
        return this.localisedLabel;
    }

}
