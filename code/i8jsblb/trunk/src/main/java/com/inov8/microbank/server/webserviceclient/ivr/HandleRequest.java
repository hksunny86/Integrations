package com.inov8.microbank.server.webserviceclient.ivr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for handleRequest complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="handleRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ivrRequestDTO" type="{http://server.microbank.inov8.com/}ivrRequestDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "handleRequest", propOrder = { "ivrRequestDTO" })
public class HandleRequest {

	protected IvrRequestDTO ivrRequestDTO;

	/**
	 * Gets the value of the ivrRequestDTO property.
	 * 
	 * @return possible object is {@link IvrRequestDTO }
	 * 
	 */
	public IvrRequestDTO getIvrRequestDTO() {
		return ivrRequestDTO;
	}

	/**
	 * Sets the value of the ivrRequestDTO property.
	 * 
	 * @param value
	 *            allowed object is {@link IvrRequestDTO }
	 * 
	 */
	public void setIvrRequestDTO(IvrRequestDTO value) {
		this.ivrRequestDTO = value;
	}

}
