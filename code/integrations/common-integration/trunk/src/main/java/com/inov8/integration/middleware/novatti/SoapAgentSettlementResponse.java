
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="totalTransCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="pageCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="pageTransId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settlements" type="{http://soap.api.novatti.com/types}Settlements" minOccurs="0"/&gt;
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
    "totalTransCount",
    "pageCount",
    "pageTransId",
    "settlements"
})
@XmlRootElement(name = "SoapAgentSettlementResponse")
public class SoapAgentSettlementResponse
    extends SoapResponseBase
{

    protected Integer totalTransCount;
    protected Integer pageCount;
    protected String pageTransId;
    protected Settlements settlements;

    /**
     * Gets the value of the totalTransCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalTransCount() {
        return totalTransCount;
    }

    /**
     * Sets the value of the totalTransCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalTransCount(Integer value) {
        this.totalTransCount = value;
    }

    /**
     * Gets the value of the pageCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * Sets the value of the pageCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageCount(Integer value) {
        this.pageCount = value;
    }

    /**
     * Gets the value of the pageTransId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageTransId() {
        return pageTransId;
    }

    /**
     * Sets the value of the pageTransId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageTransId(String value) {
        this.pageTransId = value;
    }

    /**
     * Gets the value of the settlements property.
     * 
     * @return
     *     possible object is
     *     {@link Settlements }
     *     
     */
    public Settlements getSettlements() {
        return settlements;
    }

    /**
     * Sets the value of the settlements property.
     * 
     * @param value
     *     allowed object is
     *     {@link Settlements }
     *     
     */
    public void setSettlements(Settlements value) {
        this.settlements = value;
    }

}
