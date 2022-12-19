
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LoginHistoryTransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoginHistoryTransactionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CustomerIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LoginDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LoginStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LogoutDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginHistoryTransactionList", propOrder = {
    "customerIP",
    "loginDateTime",
    "loginStatus",
    "logoutDateTime",
    "status"
})
public class LoginHistoryTransactionList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CustomerIP", nillable = true)
    protected String customerIP;
    @XmlElement(name = "LoginDateTime", nillable = true)
    protected String loginDateTime;
    @XmlElement(name = "LoginStatus", nillable = true)
    protected String loginStatus;
    @XmlElement(name = "LogoutDateTime", nillable = true)
    protected String logoutDateTime;
    @XmlElement(name = "Status", nillable = true)
    protected String status;

    /**
     * Gets the value of the customerIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerIP() {
        return customerIP;
    }

    /**
     * Sets the value of the customerIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerIP(String value) {
        this.customerIP = value;
    }

    /**
     * Gets the value of the loginDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginDateTime() {
        return loginDateTime;
    }

    /**
     * Sets the value of the loginDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginDateTime(String value) {
        this.loginDateTime = value;
    }

    /**
     * Gets the value of the loginStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginStatus() {
        return loginStatus;
    }

    /**
     * Sets the value of the loginStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginStatus(String value) {
        this.loginStatus = value;
    }

    /**
     * Gets the value of the logoutDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogoutDateTime() {
        return logoutDateTime;
    }

    /**
     * Sets the value of the logoutDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogoutDateTime(String value) {
        this.logoutDateTime = value;
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

}
