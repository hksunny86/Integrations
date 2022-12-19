package com.inov8.integration.channel.APIGEE.request.CardDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CardDetailRequest extends Request implements Serializable {

    @JsonProperty("MTI")
    private String MTI;
    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDatetime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String systemdTraceAuditNumber;
    @JsonProperty("TimeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("DateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("MerchantType")
    private String merchantType;
    @JsonProperty("CardNumber")
    private String cardNumber;
    @JsonProperty("CardType")
    private String cardType;

    public String getMTI() { return MTI; }

    public void setMTI(String MTI) { this.MTI = MTI; }

    public String getProcessingCode() { return processingCode; }

    public void setProcessingCode(String processingCode) { this.processingCode = processingCode; }

    public String getTransmissionDatetime() { return transmissionDatetime; }

    public void setTransmissionDatetime(String transmissionDatetime) { this.transmissionDatetime = transmissionDatetime; }

    public String getSystemdTraceAuditNumber() { return systemdTraceAuditNumber; }

    public void setSystemdTraceAuditNumber(String systemdTraceAuditNumber) { this.systemdTraceAuditNumber = systemdTraceAuditNumber; }

    public String getTimeLocalTransaction() { return timeLocalTransaction; }

    public void setTimeLocalTransaction(String timeLocalTransaction) { this.timeLocalTransaction = timeLocalTransaction; }

    public String getDateLocalTransaction() { return dateLocalTransaction; }

    public void setDateLocalTransaction(String dateLocalTransaction) { this.dateLocalTransaction = dateLocalTransaction; }

    public String getMerchantType() { return merchantType; }

    public void setMerchantType(String merchantType) { this.merchantType = merchantType; }

    public String getCardNumber() { return cardNumber; }

    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardType() { return cardType; }

    public void setCardType(String cardType) { this.cardType = cardType; }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMTI(i8SBSwitchControllerRequestVO.getMTI());
        this.setProcessingCode(i8SBSwitchControllerRequestVO.getProcessingCode());
        this.setTransmissionDatetime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setSystemdTraceAuditNumber(i8SBSwitchControllerRequestVO.getSystemTraceAuditNumber());
        this.setTimeLocalTransaction(i8SBSwitchControllerRequestVO.getTimeLocalTransaction());
        this.setDateLocalTransaction(i8SBSwitchControllerRequestVO.getDateLocalTransaction());
        this.setMerchantType(i8SBSwitchControllerRequestVO.getMerchantType());
        this.setCardNumber(i8SBSwitchControllerRequestVO.getCardNumber());
        this.setCardType(i8SBSwitchControllerRequestVO.getCardType());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getMTI())) {
            throw new I8SBValidationException("[Failed] MTI:" + this.getMTI());
        }
        if (StringUtils.isEmpty(this.getProcessingCode())) {
            throw new I8SBValidationException("[Failed] PROCESSING CODE:" + this.getProcessingCode());
        }
        if (StringUtils.isEmpty(this.getTransmissionDatetime())) {
            throw new I8SBValidationException("[Failed] TRANSMISSION DATE TIME:" + this.getCardNumber());
        }
        if (StringUtils.isEmpty(this.getSystemdTraceAuditNumber())) {
            throw new I8SBValidationException("[Failed] SYSTEM TRACE AUDIT NUMBER" + this.getSystemdTraceAuditNumber());
        }
        if (StringUtils.isEmpty(this.getTimeLocalTransaction())) {
            throw new I8SBValidationException("[Failed] TIME LOCAL TRANSACTION" + this.getTimeLocalTransaction());
        }
        if (StringUtils.isEmpty(this.getDateLocalTransaction())) {
            throw new I8SBValidationException("[Failed] DATE LOCAL TRANSACTION" + this.getDateLocalTransaction());
        }
        if (StringUtils.isEmpty(this.getMerchantType())) {
            throw new I8SBValidationException("[Failed] MERCHANT TYPE" + this.getMerchantType());
        }
        if (StringUtils.isEmpty(this.getCardNumber())) {
            throw new I8SBValidationException("[Failed] CARD NUMBER" + this.getCardNumber());
        }
        if (StringUtils.isEmpty(this.getCardType())) {
            throw new I8SBValidationException("[Failed] CARD TYPE" + this.getCardType());
        }
        return true;
    }
}
