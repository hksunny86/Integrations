package com.inov8.integration.m3.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="sUserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sMsgId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sMsgHeader" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sUserId", "sPassword", "sMobile", "sMsgId",
		"sMsg", "sMsgHeader" })
@XmlRootElement(name = "SendSMS")
public class SendSMS {

	protected String sUserId;
	protected String sPassword;
	protected String sMobile;
	protected String sMsgId;
	protected String sMsg;
	protected String sMsgHeader;

	/**
	 * Gets the value of the sUserId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSUserId() {
		return sUserId;
	}

	/**
	 * Sets the value of the sUserId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSUserId(String value) {
		this.sUserId = value;
	}

	/**
	 * Gets the value of the sPassword property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSPassword() {
		return sPassword;
	}

	/**
	 * Sets the value of the sPassword property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSPassword(String value) {
		this.sPassword = value;
	}

	/**
	 * Gets the value of the sMobile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSMobile() {
		return sMobile;
	}

	/**
	 * Sets the value of the sMobile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSMobile(String value) {
		this.sMobile = value;
	}

	/**
	 * Gets the value of the sMsgId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSMsgId() {
		return sMsgId;
	}

	/**
	 * Sets the value of the sMsgId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSMsgId(String value) {
		this.sMsgId = value;
	}

	/**
	 * Gets the value of the sMsg property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSMsg() {
		return sMsg;
	}

	/**
	 * Sets the value of the sMsg property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSMsg(String value) {
		this.sMsg = value;
	}

	/**
	 * Gets the value of the sMsgHeader property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSMsgHeader() {
		return sMsgHeader;
	}

	/**
	 * Sets the value of the sMsgHeader property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSMsgHeader(String value) {
		this.sMsgHeader = value;
	}

}
