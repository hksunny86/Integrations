
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
 *         &lt;element name="agentProfiles" type="{http://soap.api.novatti.com/types}AgentProfiles" minOccurs="0"/&gt;
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
    "agentProfiles"
})
@XmlRootElement(name = "SoapAgentProfileResponse")
public class SoapAgentProfileResponse
    extends SoapResponseBase
{

    protected AgentProfiles agentProfiles;

    /**
     * Gets the value of the agentProfiles property.
     * 
     * @return
     *     possible object is
     *     {@link AgentProfiles }
     *     
     */
    public AgentProfiles getAgentProfiles() {
        return agentProfiles;
    }

    /**
     * Sets the value of the agentProfiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentProfiles }
     *     
     */
    public void setAgentProfiles(AgentProfiles value) {
        this.agentProfiles = value;
    }

}
