
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="docId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="docCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="docDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="docContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
    "docId",
    "docCode",
    "docDescription",
    "docContent"
})
public class Document implements Serializable {

    protected int docId;
    protected String docCode;
    protected String docDescription;
    protected String docContent;

    /**
     * Gets the value of the docId property.
     * 
     */
    public int getDocId() {
        return docId;
    }

    /**
     * Sets the value of the docId property.
     * 
     */
    public void setDocId(int value) {
        this.docId = value;
    }

    /**
     * Gets the value of the docCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocCode() {
        return docCode;
    }

    /**
     * Sets the value of the docCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocCode(String value) {
        this.docCode = value;
    }

    /**
     * Gets the value of the docDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocDescription() {
        return docDescription;
    }

    /**
     * Sets the value of the docDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocDescription(String value) {
        this.docDescription = value;
    }

    /**
     * Gets the value of the docContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocContent() {
        return docContent;
    }

    /**
     * Sets the value of the docContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocContent(String value) {
        this.docContent = value;
    }

}
