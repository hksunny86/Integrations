
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
 *         &lt;element name="agentNodes" type="{http://soap.api.novatti.com/types}AgentNodes" minOccurs="0"/&gt;
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
    "agentNodes"
})
@XmlRootElement(name = "SoapAgentStructureResponse")
public class SoapAgentStructureResponse
    extends SoapResponseBase
{

    protected AgentNodes agentNodes;

    /**
     * Gets the value of the agentNodes property.
     * 
     * @return
     *     possible object is
     *     {@link AgentNodes }
     *     
     */
    public AgentNodes getAgentNodes() {
        return agentNodes;
    }

    /**
     * Sets the value of the agentNodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentNodes }
     *     
     */
    public void setAgentNodes(AgentNodes value) {
        this.agentNodes = value;
    }

}
