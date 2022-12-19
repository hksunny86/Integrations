
package com.inov8.integration.middleware.novatti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Settlements complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Settlements"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="settlement" type="{http://soap.api.novatti.com/types}Settlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Settlements", propOrder = {
    "settlement"
})
public class Settlements implements Serializable {

    @XmlElement(nillable = true)
    protected List<Settlement> settlement;

    /**
     * Gets the value of the settlement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the settlement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSettlement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Settlement }
     * 
     * 
     */
    public List<Settlement> getSettlement() {
        if (settlement == null) {
            settlement = new ArrayList<Settlement>();
        }
        return this.settlement;
    }

}
