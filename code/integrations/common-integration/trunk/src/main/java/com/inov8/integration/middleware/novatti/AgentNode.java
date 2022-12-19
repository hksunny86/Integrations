
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for AgentNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AgentNode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="agentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentStatus" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentType" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="parentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="parentType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="hierarchyLevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="children" type="{http://soap.api.novatti.com/types}AgentNodes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgentNode", propOrder = {
    "agentId",
    "agentCode",
    "agentDescription",
    "agentStatus",
    "gps",
    "agentType",
    "parentCode",
    "parentType",
    "hierarchyLevel",
    "children"
})
public class AgentNode implements Serializable {

    protected int agentId;
    @XmlElement(required = true)
    protected String agentCode;
    protected String agentDescription;
    protected int agentStatus;
    protected String gps;
    protected int agentType;
    protected String parentCode;
    protected Integer parentType;
    protected Integer hierarchyLevel;
    protected AgentNodes children;

    /**
     * Gets the value of the agentId property.
     * 
     */
    public int getAgentId() {
        return agentId;
    }

    /**
     * Sets the value of the agentId property.
     * 
     */
    public void setAgentId(int value) {
        this.agentId = value;
    }

    /**
     * Gets the value of the agentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * Sets the value of the agentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCode(String value) {
        this.agentCode = value;
    }

    /**
     * Gets the value of the agentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentDescription() {
        return agentDescription;
    }

    /**
     * Sets the value of the agentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentDescription(String value) {
        this.agentDescription = value;
    }

    /**
     * Gets the value of the agentStatus property.
     * 
     */
    public int getAgentStatus() {
        return agentStatus;
    }

    /**
     * Sets the value of the agentStatus property.
     * 
     */
    public void setAgentStatus(int value) {
        this.agentStatus = value;
    }

    /**
     * Gets the value of the gps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGps() {
        return gps;
    }

    /**
     * Sets the value of the gps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGps(String value) {
        this.gps = value;
    }

    /**
     * Gets the value of the agentType property.
     * 
     */
    public int getAgentType() {
        return agentType;
    }

    /**
     * Sets the value of the agentType property.
     * 
     */
    public void setAgentType(int value) {
        this.agentType = value;
    }

    /**
     * Gets the value of the parentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * Sets the value of the parentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentCode(String value) {
        this.parentCode = value;
    }

    /**
     * Gets the value of the parentType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getParentType() {
        return parentType;
    }

    /**
     * Sets the value of the parentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setParentType(Integer value) {
        this.parentType = value;
    }

    /**
     * Gets the value of the hierarchyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHierarchyLevel() {
        return hierarchyLevel;
    }

    /**
     * Sets the value of the hierarchyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHierarchyLevel(Integer value) {
        this.hierarchyLevel = value;
    }

    /**
     * Gets the value of the children property.
     * 
     * @return
     *     possible object is
     *     {@link AgentNodes }
     *     
     */
    public AgentNodes getChildren() {
        return children;
    }

    /**
     * Sets the value of the children property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentNodes }
     *     
     */
    public void setChildren(AgentNodes value) {
        this.children = value;
    }

}
