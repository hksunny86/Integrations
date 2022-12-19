package com.inov8.integration.channel.vision.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MTI",
        "ProcessingCode",
        "TransmissionDatetime",
        "SystemsTraceAuditNumber",
        "TimeLocalTransaction",
        "DateLocalTransaction",
        "MerchantType",
        "CNIC"
})
public class VisionDebitCardRequest extends Request implements Serializable {

    @JsonProperty("MTI")
    private String mti;
    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDateTime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String stan;
    @JsonProperty("TimeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("DateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("MerchantType")
    private String merchantType;
    @JsonProperty("CNIC")
    private String cnic;

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        
        this.setMti("0200");
        this.setProcessingCode("768000");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setTimeLocalTransaction(i8SBSwitchControllerRequestVO.getTimeLocalTransaction());
        this.setDateLocalTransaction(i8SBSwitchControllerRequestVO.getDateLocalTransaction());
        this.setMerchantType("0069");
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMti())) {
            throw new I8SBValidationException("[Failed] MTI:" + this.getMti());
        }
        if (StringUtils.isEmpty(this.getProcessingCode())) {
            throw new I8SBValidationException("[Failed] Processing Code:" + this.getProcessingCode());
        }
        if (StringUtils.isEmpty(this.getTransmissionDateTime())) {
            throw new I8SBValidationException("[Failed] Transmission Date Time :" + this.getTransmissionDateTime());
        }
        if (StringUtils.isEmpty(this.getStan())) {
            throw new I8SBValidationException("[Failed] STAN :" + this.getStan());
        }
        if (StringUtils.isEmpty(this.getMerchantType())) {
            throw new I8SBValidationException("[Failed] Merchant Type:" + this.getMerchantType());
        }
        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC:" + this.getCnic());
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
