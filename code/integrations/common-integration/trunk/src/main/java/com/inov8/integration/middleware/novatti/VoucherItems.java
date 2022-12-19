
package com.inov8.integration.middleware.novatti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VoucherItems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VoucherItems"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="voucher" type="{http://soap.api.novatti.com/types}ProductMap" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherItems", propOrder = {
    "voucher"
})
public class VoucherItems implements Serializable {

    @XmlElement(nillable = true)
    protected List<ProductMap> voucher;

    /**
     * Gets the value of the voucher property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the voucher property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVoucher().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductMap }
     * 
     * 
     */
    public List<ProductMap> getVoucher() {
        if (voucher == null) {
            voucher = new ArrayList<ProductMap>();
        }
        return this.voucher;
    }

}
