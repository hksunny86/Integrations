
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
 *         &lt;element name="agent" type="{http://soap.api.novatti.com/types}AgentNode" minOccurs="0"/&gt;
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
    "agent"
})
@XmlRootElement(name = "SoapAgentUpdateResponse")
public class SoapAgentUpdateResponse
    extends SoapResponseBase
{

    protected AgentNode agent;

    /**
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link AgentNode }
     *     
     */
    public AgentNode getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentNode }
     *     
     */
    public void setAgent(AgentNode value) {
        this.agent = value;
    }

}
