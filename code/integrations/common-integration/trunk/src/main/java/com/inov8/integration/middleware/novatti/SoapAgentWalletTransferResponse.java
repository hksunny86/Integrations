
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
 *         &lt;element name="transactions" type="{http://soap.api.novatti.com/types}Transactions" minOccurs="0"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agent" type="{http://soap.api.novatti.com/types}Agent" minOccurs="0"/&gt;
 *         &lt;element name="destinationAgent" type="{http://soap.api.novatti.com/types}Agent" minOccurs="0"/&gt;
 *         &lt;element name="destinationWalletBalance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
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
    "transactions",
    "value",
    "agentCode",
    "agent",
    "destinationAgent",
    "destinationWalletBalance"
})
@XmlRootElement(name = "SoapAgentWalletTransferResponse")
public class SoapAgentWalletTransferResponse
    extends SoapResponseBase
{

    protected Transactions transactions;
    protected Double value;
    protected String agentCode;
    protected Agent agent;
    protected Agent destinationAgent;
    protected Double destinationWalletBalance;

    /**
     * Gets the value of the transactions property.
     * 
     * @return
     *     possible object is
     *     {@link Transactions }
     *     
     */
    public Transactions getTransactions() {
        return transactions;
    }

    /**
     * Sets the value of the transactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transactions }
     *     
     */
    public void setTransactions(Transactions value) {
        this.transactions = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
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
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link Agent }
     *     
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Agent }
     *     
     */
    public void setAgent(Agent value) {
        this.agent = value;
    }

    /**
     * Gets the value of the destinationAgent property.
     * 
     * @return
     *     possible object is
     *     {@link Agent }
     *     
     */
    public Agent getDestinationAgent() {
        return destinationAgent;
    }

    /**
     * Sets the value of the destinationAgent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Agent }
     *     
     */
    public void setDestinationAgent(Agent value) {
        this.destinationAgent = value;
    }

    /**
     * Gets the value of the destinationWalletBalance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDestinationWalletBalance() {
        return destinationWalletBalance;
    }

    /**
     * Sets the value of the destinationWalletBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDestinationWalletBalance(Double value) {
        this.destinationWalletBalance = value;
    }

}
