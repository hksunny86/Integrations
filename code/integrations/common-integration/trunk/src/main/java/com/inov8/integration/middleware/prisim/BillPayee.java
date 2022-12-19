
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillPayee complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillPayee"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CompanyCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillPayee", propOrder = {
    "companyCategory",
    "companyId",
    "consumerNumber"
})
public class BillPayee
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CompanyCategory", nillable = true)
    protected String companyCategory;
    @XmlElement(name = "CompanyId", nillable = true)
    protected String companyId;
    @XmlElement(name = "ConsumerNumber", nillable = true)
    protected String consumerNumber;

    /**
     * Gets the value of the companyCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyCategory() {
        return companyCategory;
    }

    /**
     * Sets the value of the companyCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyCategory(String value) {
        this.companyCategory = value;
    }

    /**
     * Gets the value of the companyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Sets the value of the companyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyId(String value) {
        this.companyId = value;
    }

    /**
     * Gets the value of the consumerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNumber() {
        return consumerNumber;
    }

    /**
     * Sets the value of the consumerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNumber(String value) {
        this.consumerNumber = value;
    }

}
