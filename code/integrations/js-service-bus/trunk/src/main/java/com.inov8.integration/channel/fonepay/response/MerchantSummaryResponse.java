
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
 *         &lt;element name="ResponseDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="merchantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="merchantName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "responseCode",
        "responseDesc",
        "merchantID",
        "merchantName"
})
@XmlRootElement(name = "MerchantSummaryResponse")
public class MerchantSummaryResponse extends Response {

    @XmlElement(name = "ResponseCode")
    protected String responseCode;
    @XmlElement(name = "ResponseDesc")
    protected String responseDesc;
    protected String merchantID;
    protected String merchantName;

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
     * Gets the value of the responseDesc property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseDesc() {
        return responseDesc;
    }

    /**
     * Sets the value of the responseDesc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseDesc(String value) {
        this.responseDesc = value;
    }

    /**
     * Gets the value of the merchantID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMerchantID() {
        return merchantID;
    }

    /**
     * Sets the value of the merchantID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMerchantID(String value) {
        this.merchantID = value;
    }

    /**
     * Gets the value of the merchantName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * Sets the value of the merchantName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMerchantName(String value) {
        this.merchantName = value;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setError(this.getResponseDesc());
        i8SBSwitchControllerResponseVO.setMerchantId(this.getMerchantID());
        i8SBSwitchControllerResponseVO.setMerchantName(this.getMerchantName());

        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public String toString() {
        return null;
    }
}
