
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
 *         &lt;element name="agentTemplates" type="{http://soap.api.novatti.com/types}AgentTemplates" minOccurs="0"/&gt;
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
    "agentTemplates"
})
@XmlRootElement(name = "SoapAgentTemplateResponse")
public class SoapAgentTemplateResponse
    extends SoapResponseBase
{

    protected AgentTemplates agentTemplates;

    /**
     * Gets the value of the agentTemplates property.
     * 
     * @return
     *     possible object is
     *     {@link AgentTemplates }
     *     
     */
    public AgentTemplates getAgentTemplates() {
        return agentTemplates;
    }

    /**
     * Sets the value of the agentTemplates property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentTemplates }
     *     
     */
    public void setAgentTemplates(AgentTemplates value) {
        this.agentTemplates = value;
    }

}
