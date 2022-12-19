
package com.inov8.integration.middleware.bop.IExternalCommunication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHeadsDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHeadsDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="HeadsDetail" type="{http://schemas.datacontract.org/2004/07/BOP.TBG.PRI.WSModel.eGovt}HeadsDetail" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHeadsDetail", propOrder = {
    "headsDetail"
})
public class ArrayOfHeadsDetail
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "HeadsDetail", nillable = true)
    protected List<HeadsDetail> headsDetail;

    /**
     * Gets the value of the headsDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headsDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHeadsDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HeadsDetail }
     * 
     * 
     */
    public List<HeadsDetail> getHeadsDetail() {
        if (headsDetail == null) {
            headsDetail = new ArrayList<HeadsDetail>();
        }
        return this.headsDetail;
    }

}
