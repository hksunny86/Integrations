
package com.inov8.integration.channel.fonepay.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;


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
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="merchantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlRootElement(name = "MerchantSummaryRequest")
public class MerchantSummaryRequest extends Request {


    private String qrCode;
    private String dateTime;
    private String merchantID;
    private String bankID;

    public MerchantSummaryRequest() {
    }

    public MerchantSummaryRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super(i8SBSwitchControllerRequestVO);

    }


    /**
     * Gets the value of the qrCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the value of the qrCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setQrCode(String value) {
        this.qrCode = value;
    }

    /**
     * Gets the value of the dateTime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDateTime(String value) {
        this.dateTime = value;
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
     * Gets the value of the bankID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankID() {
        return bankID;
    }

    /**
     * Sets the value of the bankID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankID(String value) {
        this.bankID = value;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setQrCode(i8SBSwitchControllerRequestVO.getMerchantQRCode());
        this.setDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setMerchantID(i8SBSwitchControllerRequestVO.getMerchantId());
        this.setBankID(i8SBSwitchControllerRequestVO.getBankId());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getMerchantID())) {
            throw new I8SBValidationException("[FAILED] Merchant ID validation: " + this.getMerchantID());
        }
        if (StringUtils.isEmpty(this.getBankID())) {
            throw new I8SBValidationException("[FAILED] Bank ID validation: " + this.getBankID());
        }

        return true;
    }

    @Override
    public String toString() {
        return null;
    }
}
