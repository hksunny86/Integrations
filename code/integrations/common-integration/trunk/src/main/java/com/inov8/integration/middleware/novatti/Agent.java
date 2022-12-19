
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Agent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Agent"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="masterDistributorId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="masterDistributorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prepaid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="wallets" type="{http://soap.api.novatti.com/types}Wallets" minOccurs="0"/&gt;
 *         &lt;element name="bulletins" type="{http://soap.api.novatti.com/types}Bulletins" minOccurs="0"/&gt;
 *         &lt;element name="parentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="parentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="parentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="updatedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stateName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timezone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billingInvoiceEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="businessNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="companyNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentCategoryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentComments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Agent", propOrder = {
    "agentId",
    "agentCode",
    "agentDescription",
    "countryCode",
    "countryName",
    "masterDistributorId",
    "masterDistributorCode",
    "prepaid",
    "wallets",
    "bulletins",
    "parentCode",
    "parentDescription",
    "parentType",
    "agentType",
    "status",
    "creationDate",
    "updatedDate",
    "state",
    "stateName",
    "stateCode",
    "timezone",
    "tax",
    "billingInvoiceEmail",
    "businessNumber",
    "companyNumber",
    "agentCategoryId",
    "agentComments"
})
public class Agent implements Serializable {

    protected int agentId;
    protected String agentCode;
    protected String agentDescription;
    protected String countryCode;
    protected String countryName;
    protected Integer masterDistributorId;
    protected String masterDistributorCode;
    protected Boolean prepaid;
    protected Wallets wallets;
    protected Bulletins bulletins;
    protected String parentCode;
    protected String parentDescription;
    protected String parentType;
    protected String agentType;
    protected String status;
    protected String creationDate;
    protected String updatedDate;
    protected String state;
    protected String stateName;
    protected String stateCode;
    protected String timezone;
    protected String tax;
    protected String billingInvoiceEmail;
    protected String businessNumber;
    protected String companyNumber;
    protected String agentCategoryId;
    protected String agentComments;

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
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

    /**
     * Gets the value of the masterDistributorId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMasterDistributorId() {
        return masterDistributorId;
    }

    /**
     * Sets the value of the masterDistributorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMasterDistributorId(Integer value) {
        this.masterDistributorId = value;
    }

    /**
     * Gets the value of the masterDistributorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMasterDistributorCode() {
        return masterDistributorCode;
    }

    /**
     * Sets the value of the masterDistributorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMasterDistributorCode(String value) {
        this.masterDistributorCode = value;
    }

    /**
     * Gets the value of the prepaid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrepaid() {
        return prepaid;
    }

    /**
     * Sets the value of the prepaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrepaid(Boolean value) {
        this.prepaid = value;
    }

    /**
     * Gets the value of the wallets property.
     * 
     * @return
     *     possible object is
     *     {@link Wallets }
     *     
     */
    public Wallets getWallets() {
        return wallets;
    }

    /**
     * Sets the value of the wallets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wallets }
     *     
     */
    public void setWallets(Wallets value) {
        this.wallets = value;
    }

    /**
     * Gets the value of the bulletins property.
     * 
     * @return
     *     possible object is
     *     {@link Bulletins }
     *     
     */
    public Bulletins getBulletins() {
        return bulletins;
    }

    /**
     * Sets the value of the bulletins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bulletins }
     *     
     */
    public void setBulletins(Bulletins value) {
        this.bulletins = value;
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
     * Gets the value of the parentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentDescription() {
        return parentDescription;
    }

    /**
     * Sets the value of the parentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentDescription(String value) {
        this.parentDescription = value;
    }

    /**
     * Gets the value of the parentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentType() {
        return parentType;
    }

    /**
     * Sets the value of the parentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentType(String value) {
        this.parentType = value;
    }

    /**
     * Gets the value of the agentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentType() {
        return agentType;
    }

    /**
     * Sets the value of the agentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentType(String value) {
        this.agentType = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationDate(String value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the updatedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Sets the value of the updatedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedDate(String value) {
        this.updatedDate = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the stateName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Sets the value of the stateName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateName(String value) {
        this.stateName = value;
    }

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCode(String value) {
        this.stateCode = value;
    }

    /**
     * Gets the value of the timezone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the value of the timezone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimezone(String value) {
        this.timezone = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTax() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTax(String value) {
        this.tax = value;
    }

    /**
     * Gets the value of the billingInvoiceEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingInvoiceEmail() {
        return billingInvoiceEmail;
    }

    /**
     * Sets the value of the billingInvoiceEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingInvoiceEmail(String value) {
        this.billingInvoiceEmail = value;
    }

    /**
     * Gets the value of the businessNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessNumber() {
        return businessNumber;
    }

    /**
     * Sets the value of the businessNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessNumber(String value) {
        this.businessNumber = value;
    }

    /**
     * Gets the value of the companyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyNumber() {
        return companyNumber;
    }

    /**
     * Sets the value of the companyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyNumber(String value) {
        this.companyNumber = value;
    }

    /**
     * Gets the value of the agentCategoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCategoryId() {
        return agentCategoryId;
    }

    /**
     * Sets the value of the agentCategoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCategoryId(String value) {
        this.agentCategoryId = value;
    }

    /**
     * Gets the value of the agentComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentComments() {
        return agentComments;
    }

    /**
     * Sets the value of the agentComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentComments(String value) {
        this.agentComments = value;
    }

}
