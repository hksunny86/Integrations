
package com.inov8.integration.channel.fonepay.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P2M_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "responseCode",
        "responseDescription",
        "transactionCode",
        "p2MID"
})
@XmlRootElement(name = "MPassPaymentResponse")
public class MPassPaymentResponse extends Response {

    @XmlElement(name = "ResponseCode")
    protected String responseCode;
    @XmlElement(name = "ResponseDescription")
    protected String responseDescription;
    @XmlElement(name = "TransactionCode")
    protected String transactionCode;
    @XmlElement(name = "P2M_ID")
    protected String p2MID;

    /**
     * Gets the value of the responseCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseDescription property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    /**
     * Sets the value of the responseDescription property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseDescription(String value) {
        this.responseDescription = value;
    }

    /**
     * Gets the value of the transactionCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the transactionCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTransactionCode(String value) {
        this.transactionCode = value;
    }

    /**
     * Gets the value of the p2MID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getP2MID() {
        return p2MID;
    }

    /**
     * Sets the value of the p2MID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setP2MID(String value) {
        this.p2MID = value;
    }


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setError(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setTranCode(this.getTransactionCode());
        i8SBSwitchControllerResponseVO.setP2M_ID(this.getP2MID());

        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public String toString() {
        return null;
    }
}
