
package com.inov8.integration.middleware.bop.avenza;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Header" type="{http://BOP_MobileApp.org}Header"/&gt;
 *         &lt;element name="MiniStatementBlk" type="{http://BOP_MobileApp.org}MiniStatementBlk" maxOccurs="unbounded"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "miniStatementBlk",
    "responseCode"
})
@XmlRootElement(name = "ViewAccountStatementResponse")
public class ViewAccountStatementResponse {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "MiniStatementBlk", required = true)
    protected List<MiniStatementBlk> miniStatementBlk;
    @XmlElement(name = "ResponseCode", required = true)
    protected String responseCode;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the miniStatementBlk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the miniStatementBlk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMiniStatementBlk().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MiniStatementBlk }
     * 
     * 
     */
    public List<MiniStatementBlk> getMiniStatementBlk() {
        if (miniStatementBlk == null) {
            miniStatementBlk = new ArrayList<MiniStatementBlk>();
        }
        return this.miniStatementBlk;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

}
