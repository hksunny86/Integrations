
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="roleNames" type="{http://soap.api.novatti.com/types}RoleNames" minOccurs="0"/&gt;
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
    "roleNames"
})
@XmlRootElement(name = "SoapRoleInfoRequest")
public class SoapRoleInfoRequest
    extends SoapRequestBaseWithSession
{

    protected RoleNames roleNames;

    /**
     * Gets the value of the roleNames property.
     * 
     * @return
     *     possible object is
     *     {@link RoleNames }
     *     
     */
    public RoleNames getRoleNames() {
        return roleNames;
    }

    /**
     * Sets the value of the roleNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleNames }
     *     
     */
    public void setRoleNames(RoleNames value) {
        this.roleNames = value;
    }

}
