package com.inov8.integration.m3.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SendSMSUniCodeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sendSMSUniCodeResult" })
@XmlRootElement(name = "SendSMSUniCodeResponse")
public class SendSMSUniCodeResponse {

	@XmlElement(name = "SendSMSUniCodeResult")
	protected String sendSMSUniCodeResult;

	/**
	 * Gets the value of the sendSMSUniCodeResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSendSMSUniCodeResult() {
		return sendSMSUniCodeResult;
	}

	/**
	 * Sets the value of the sendSMSUniCodeResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSendSMSUniCodeResult(String value) {
		this.sendSMSUniCodeResult = value;
	}

}
