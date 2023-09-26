package com.inov8.integration.channel.esb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "merchantType",
        "traceNo",
        "dateTime",
        "responseCode",
        "responseDetails",
        "response"
})
public class EsbBillPaymentResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(EsbBillInquiryResponse.class.getSimpleName());

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDetails")
    private List<String> responseDetails;
    @JsonProperty("response")
    private BillPayment billPayment;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<String> getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(List<String> responseDetails) {
        this.responseDetails = responseDetails;
    }

    public BillPayment getBillPayment() {
        return billPayment;
    }

    public void setBillPayment(BillPayment billPayment) {
        this.billPayment = billPayment;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDetails().get(0));
        }
        i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
        i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
        i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceNo());
        i8SBSwitchControllerResponseVO.setDateTime(this.getDateTime());
        if (this.getBillPayment() != null) {
            i8SBSwitchControllerResponseVO.setResponseCode(this.billPayment.getResponseCode());
            i8SBSwitchControllerResponseVO.setIdentificationParameter(this.billPayment.getIdentificationParameter());
            i8SBSwitchControllerResponseVO.setReserved(this.billPayment.getReserved());
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Response_code",
        "Identification_Parameter",
        "Reserved"
})
class BillPayment {

    @JsonProperty("Response_code")
    private String responseCode;
    @JsonProperty("Identification_Parameter")
    private String identificationParameter;
    @JsonProperty("Reserved")
    private String reserved;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getIdentificationParameter() {
        return identificationParameter;
    }

    public void setIdentificationParameter(String identificationParameter) {
        this.identificationParameter = identificationParameter;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
