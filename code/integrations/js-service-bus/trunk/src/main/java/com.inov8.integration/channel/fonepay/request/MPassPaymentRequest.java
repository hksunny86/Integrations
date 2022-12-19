
package com.inov8.integration.channel.fonepay.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="MobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EmailID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MerchantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */

@XmlRootElement(name = "MPassPaymentRequest")
public class MPassPaymentRequest extends Request {

    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "EmailID")
    private String emailID;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "MerchantID")
    private String merchantID;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "RRN")
    private String rrn;
    private String bankID;

    @XmlElement(name = "FirstName")
    private String firstName;
    @XmlElement(name = "LastName")
    private String lastName;
    @XmlElement(name = "CNIC")
    private String CNIC;

    @XmlElement(name = "MerchantName")
    private String merchantName;

    @XmlElement(name = "PaymentMode")
    private String paymentMode;

    public MPassPaymentRequest() {
    }

    public MPassPaymentRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super(i8SBSwitchControllerRequestVO);

    }


    /**
     * Gets the value of the mobileNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of the mobileNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMobileNumber(String value) {
        this.mobileNumber = value;
    }

    /**
     * Gets the value of the emailID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmailID() {
        return emailID;
    }

    /**
     * Sets the value of the emailID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailID(String value) {
        this.emailID = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTransactionAmount(String value) {
        this.transactionAmount = value;
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
     * Gets the value of the rrn property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRRN() {
        return rrn;
    }

    /**
     * Sets the value of the rrn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRRN(String value) {
        this.rrn = value;
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



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }


    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }


    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobilePhone());
        this.setEmailID(i8SBSwitchControllerRequestVO.getEmail());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setMerchantID(i8SBSwitchControllerRequestVO.getMerchantId());
        this.setDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setRRN(i8SBSwitchControllerRequestVO.getRRN());
        this.setBankID(i8SBSwitchControllerRequestVO.getBankId());
        this.setFirstName(i8SBSwitchControllerRequestVO.getFirstName());
        this.setLastName(i8SBSwitchControllerRequestVO.getLastName());
        this.setCNIC(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMerchantName(i8SBSwitchControllerRequestVO.getMerchantName());
        if (!StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getI8sbClientTerminalID()) && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_BLB)) {
            this.setPaymentMode("11");
        } else {
            this.setPaymentMode("6");
        }
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getTransactionAmount())) {
            throw new I8SBValidationException("[FAILED] Transaction Amount validation: " + this.getTransactionAmount());
        }
        if (StringUtils.isEmpty(this.getMerchantID())) {
            throw new I8SBValidationException("[FAILED] Merchant ID validation: " + this.getMerchantID());
        }
        if (StringUtils.isEmpty(this.getBankID())) {
            throw new I8SBValidationException("[FAILED] Bank ID validation: " + this.getBankID());
        }
        if (!StringUtils.isEmpty(this.getTerminalID()) && this.getTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_BLB)) {

            if (StringUtils.isEmpty(this.getFirstName())) {
                throw new I8SBValidationException("[FAILED] First Name validation: " + this.getFirstName());
            }
            if (StringUtils.isEmpty(this.getLastName())) {
                throw new I8SBValidationException("[FAILED] Last Name validation: " + this.getLastName());
            }
            if (StringUtils.isEmpty(this.getMobileNumber())) {
                throw new I8SBValidationException("[FAILED] Mobile Number validation: " + this.getMobileNumber());
            }
            if (StringUtils.isEmpty(this.getCNIC())) {
                throw new I8SBValidationException("[FAILED] CNIC validation: " + this.getCNIC());
            }
            if (StringUtils.isEmpty(this.getMerchantName())) {
                throw new I8SBValidationException("[FAILED] Merchant Name validation: " + this.getMerchantName());
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return null;
    }
}
