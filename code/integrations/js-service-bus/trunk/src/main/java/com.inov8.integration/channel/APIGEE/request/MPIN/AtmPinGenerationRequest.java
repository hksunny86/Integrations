package com.inov8.integration.channel.APIGEE.request.MPIN;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.channel.APIGEE.util.DESEncyption;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class AtmPinGenerationRequest extends Request implements Serializable {

    @JsonProperty("TransmissionDateTime")
    private String transmissionDateTime;
    @JsonProperty("STAN")
    private String stan;
    @JsonProperty("CardNumber")
    private String cardNumber;
    @JsonProperty("CustomerIdentification")
    private String customerIdentification;
    @JsonProperty("NewPIN")
    private String newPIN;

    public String getTransmissionDateTime() { return transmissionDateTime; }

    public void setTransmissionDateTime(String transmissionDateTime) { this.transmissionDateTime = transmissionDateTime; }

    public String getStan() { return stan; }

    public void setStan(String stan) { this.stan = stan; }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerIdentification() {
        return customerIdentification;
    }

    public void setCustomerIdentification(String customerIdentification) {
        this.customerIdentification = customerIdentification;
    }

    public String getNewPIN() {
        return newPIN;
    }

    public void setNewPIN(String newPIN) {
        this.newPIN = newPIN;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        DESEncyption desEncryption = new DESEncyption();

        this.setTransmissionDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setCardNumber(i8SBSwitchControllerRequestVO.getCardNumber());
        this.setCustomerIdentification(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setNewPIN(desEncryption.encrypt(i8SBSwitchControllerRequestVO.getNewMPin()));

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getTransmissionDateTime())) {
            throw new I8SBValidationException("[Failed] TRANSMISSION_DATE_TIME:" + this.getTransmissionDateTime());
        }
        if (StringUtils.isEmpty(this.getStan())) {
            throw new I8SBValidationException("[Failed] STAN:" + this.getStan());
        }
        if (StringUtils.isEmpty(this.getCardNumber())) {
            throw new I8SBValidationException("[Failed] CARD_NUMBER:" + this.getCardNumber());
        }
        if (StringUtils.isEmpty(this.getCustomerIdentification())) {
            throw new I8SBValidationException("[Failed] CUSTOMER_IDENTIFICATION" + this.getCustomerIdentification());
        }
        if (StringUtils.isEmpty(this.getNewPIN())) {
            throw new I8SBValidationException("[Failed] NEW_PIN" + this.getNewPIN());
        }
        return true;
    }
}
