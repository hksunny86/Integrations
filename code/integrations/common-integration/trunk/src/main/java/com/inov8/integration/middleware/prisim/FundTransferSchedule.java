
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InputHeader" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}InputHeader" minOccurs="0"/&gt;
 *         &lt;element name="ChannelUserIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerAccount" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}CustomerAccount" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryAccount" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BeneficiaryAccount" minOccurs="0"/&gt;
 *         &lt;element name="PaymentDetailDTO" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}PaymentDetailDTO" minOccurs="0"/&gt;
 *         &lt;element name="FinancialPin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "inputHeader",
    "channelUserIdentifier",
    "customerUserName",
    "customerAccount",
    "beneficiaryType",
    "beneficiaryAccount",
    "paymentDetailDTO",
    "financialPin"
})
@XmlRootElement(name = "FundTransferSchedule", namespace = "http://tempuri.org/")
public class FundTransferSchedule
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "InputHeader", namespace = "http://tempuri.org/", nillable = true)
    protected InputHeader inputHeader;
    @XmlElement(name = "ChannelUserIdentifier", namespace = "http://tempuri.org/", nillable = true)
    protected String channelUserIdentifier;
    @XmlElement(name = "CustomerUserName", namespace = "http://tempuri.org/", nillable = true)
    protected String customerUserName;
    @XmlElement(name = "CustomerAccount", namespace = "http://tempuri.org/", nillable = true)
    protected CustomerAccount customerAccount;
    @XmlElement(name = "BeneficiaryType", namespace = "http://tempuri.org/", nillable = true)
    protected String beneficiaryType;
    @XmlElement(name = "BeneficiaryAccount", namespace = "http://tempuri.org/", nillable = true)
    protected BeneficiaryAccount beneficiaryAccount;
    @XmlElement(name = "PaymentDetailDTO", namespace = "http://tempuri.org/", nillable = true)
    protected PaymentDetailDTO paymentDetailDTO;
    @XmlElement(name = "FinancialPin", namespace = "http://tempuri.org/", nillable = true)
    protected String financialPin;

    /**
     * Gets the value of the inputHeader property.
     * 
     * @return
     *     possible object is
     *     {@link InputHeader }
     *     
     */
    public InputHeader getInputHeader() {
        return inputHeader;
    }

    /**
     * Sets the value of the inputHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputHeader }
     *     
     */
    public void setInputHeader(InputHeader value) {
        this.inputHeader = value;
    }

    /**
     * Gets the value of the channelUserIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelUserIdentifier() {
        return channelUserIdentifier;
    }

    /**
     * Sets the value of the channelUserIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelUserIdentifier(String value) {
        this.channelUserIdentifier = value;
    }

    /**
     * Gets the value of the customerUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerUserName() {
        return customerUserName;
    }

    /**
     * Sets the value of the customerUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerUserName(String value) {
        this.customerUserName = value;
    }

    /**
     * Gets the value of the customerAccount property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerAccount }
     *     
     */
    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    /**
     * Sets the value of the customerAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerAccount }
     *     
     */
    public void setCustomerAccount(CustomerAccount value) {
        this.customerAccount = value;
    }

    /**
     * Gets the value of the beneficiaryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    /**
     * Sets the value of the beneficiaryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryType(String value) {
        this.beneficiaryType = value;
    }

    /**
     * Gets the value of the beneficiaryAccount property.
     * 
     * @return
     *     possible object is
     *     {@link BeneficiaryAccount }
     *     
     */
    public BeneficiaryAccount getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    /**
     * Sets the value of the beneficiaryAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BeneficiaryAccount }
     *     
     */
    public void setBeneficiaryAccount(BeneficiaryAccount value) {
        this.beneficiaryAccount = value;
    }

    /**
     * Gets the value of the paymentDetailDTO property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentDetailDTO }
     *     
     */
    public PaymentDetailDTO getPaymentDetailDTO() {
        return paymentDetailDTO;
    }

    /**
     * Sets the value of the paymentDetailDTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentDetailDTO }
     *     
     */
    public void setPaymentDetailDTO(PaymentDetailDTO value) {
        this.paymentDetailDTO = value;
    }

    /**
     * Gets the value of the financialPin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinancialPin() {
        return financialPin;
    }

    /**
     * Sets the value of the financialPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinancialPin(String value) {
        this.financialPin = value;
    }

}
