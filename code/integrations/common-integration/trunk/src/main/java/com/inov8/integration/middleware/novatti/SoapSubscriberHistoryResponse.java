
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
 *         &lt;element name="history" type="{http://soap.api.novatti.com/types}HistoryItems" minOccurs="0"/&gt;
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
    "history"
})
@XmlRootElement(name = "SoapSubscriberHistoryResponse")
public class SoapSubscriberHistoryResponse
    extends SoapResponseBase
{

    protected HistoryItems history;

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link HistoryItems }
     *     
     */
    public HistoryItems getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link HistoryItems }
     *     
     */
    public void setHistory(HistoryItems value) {
        this.history = value;
    }

}
