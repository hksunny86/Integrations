
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
 *         &lt;element name="products" type="{http://soap.api.novatti.com/types}ProductItems" minOccurs="0"/&gt;
 *         &lt;element name="reprintCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
    "products",
    "reprintCount"
})
@XmlRootElement(name = "SoapPurchaseReprintResponse")
public class SoapPurchaseReprintResponse
    extends SoapResponseBase
{

    protected ProductItems products;
    protected Integer reprintCount;

    /**
     * Gets the value of the products property.
     * 
     * @return
     *     possible object is
     *     {@link ProductItems }
     *     
     */
    public ProductItems getProducts() {
        return products;
    }

    /**
     * Sets the value of the products property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductItems }
     *     
     */
    public void setProducts(ProductItems value) {
        this.products = value;
    }

    /**
     * Gets the value of the reprintCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReprintCount() {
        return reprintCount;
    }

    /**
     * Sets the value of the reprintCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReprintCount(Integer value) {
        this.reprintCount = value;
    }

}
