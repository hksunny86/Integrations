
package com.inov8.integration.middleware.bop.iflexService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}openAttrs"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element ref="{http://www.w3.org/2001/XMLSchema}annotation"/&gt;
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}redefinable"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="schemaLocation" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "annotationsAndSimpleTypesAndComplexTypes"
})
@XmlRootElement(name = "redefine")
public class Redefine
    extends OpenAttrs
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElements({
        @XmlElement(name = "annotation", type = Annotation.class),
        @XmlElement(name = "simpleType", type = SimpleType.class),
        @XmlElement(name = "complexType", type = ComplexType.class),
        @XmlElement(name = "group", type = Group.class),
        @XmlElement(name = "attributeGroup", type = AttributeGroup.class)
    })
    protected List<OpenAttrs> annotationsAndSimpleTypesAndComplexTypes;
    @XmlAttribute(name = "schemaLocation", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String schemaLocation;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the annotationsAndSimpleTypesAndComplexTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotationsAndSimpleTypesAndComplexTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotationsAndSimpleTypesAndComplexTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Annotation }
     * {@link SimpleType }
     * {@link ComplexType }
     * {@link Group }
     * {@link AttributeGroup }
     * 
     * 
     */
    public List<OpenAttrs> getAnnotationsAndSimpleTypesAndComplexTypes() {
        if (annotationsAndSimpleTypesAndComplexTypes == null) {
            annotationsAndSimpleTypesAndComplexTypes = new ArrayList<OpenAttrs>();
        }
        return this.annotationsAndSimpleTypesAndComplexTypes;
    }

    /**
     * Gets the value of the schemaLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * Sets the value of the schemaLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaLocation(String value) {
        this.schemaLocation = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
