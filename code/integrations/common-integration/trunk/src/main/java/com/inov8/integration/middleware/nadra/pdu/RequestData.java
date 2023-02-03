//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.21 at 10:21:14 AM PKT 
//


package com.inov8.integration.middleware.nadra.pdu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SESSION_ID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="19"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TRANSACTION_ID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="19"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CITIZEN_NUMBER">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="13"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CONTACT_NUMBER">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FINGER_INDEX">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FINGER_TEMPLATE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}base64Binary">
 *               &lt;maxLength value="2147483647"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TEMPLATE_TYPE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AREA_NAME">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="32"/>
 *               &lt;minLength value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sessionId",
        "transactionId",
        "remittanceAmount",
        "remittanceType",
        "citizenNumber",
        "accountNumber",
        "accountLevel",
        "contactNumber",
        "fingerIndex",
        "fingerTemplate",
        "templateType",
        "secondaryCitizeNnumber",
        "secondaryContactNumber",
        "areaName",
        "verificationResults",
        "issueDate"
})
public class RequestData {

    @XmlElement(name = "SESSION_ID", required = true)
    protected String sessionId;
    @XmlElement(name = "TRANSACTION_ID", required = true)
    protected String transactionId;
    @XmlElement(name = "REMITTANCE_AMOUNT", required = true)
    protected String remittanceAmount;
    @XmlElement(name = "REMITTANCE_TYPE", required = true)
    protected String remittanceType;
    @XmlElement(name = "CITIZEN_NUMBER", required = true)
    protected String citizenNumber;
    @XmlElement(name = "ACCOUNT_NUMBER", required = true)
    protected String accountNumber;
    @XmlElement(name = "ACCOUNT_LEVEL", required = true)
    protected String accountLevel;
    @XmlElement(name = "CONTACT_NUMBER", required = true)
    protected String contactNumber;
    @XmlElement(name = "FINGER_INDEX")
    protected int fingerIndex;
    @XmlElement(name = "FINGER_TEMPLATE", required = true)
    protected byte[] fingerTemplate;
    @XmlElement(name = "TEMPLATE_TYPE", required = true)
    protected String templateType;
    @XmlElement(name = "SECONDARY_CITIZEN_NUMBER", required = true)
    protected String secondaryCitizeNnumber;
    @XmlElement(name = "SECONDARY_CONTACT_NUMBER", required = true)
    protected String secondaryContactNumber;
    @XmlElement(name = "AREA_NAME", required = true)
    protected String areaName;
    @XmlElement(name = "VERIFICATION_RESULTS", required = true)
    protected String verificationResults;
    @XmlElement(name = "ISSUE_DATE", required = true)
    protected String issueDate;

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(String remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public String getRemittanceType() {
        return remittanceType;
    }

    public void setRemittanceType(String remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(int fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public byte[] getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(byte[] fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSecondaryCitizeNnumber() {
        return secondaryCitizeNnumber;
    }

    public void setSecondaryCitizeNnumber(String secondaryCitizeNnumber) {
        this.secondaryCitizeNnumber = secondaryCitizeNnumber;
    }

    public String getSecondaryContactNumber() {
        return secondaryContactNumber;
    }

    public void setSecondaryContactNumber(String secondaryContactNumber) {
        this.secondaryContactNumber = secondaryContactNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getVerificationResults() {
        return verificationResults;
    }

    public void setVerificationResults(String verificationResults) {
        this.verificationResults = verificationResults;
    }
}