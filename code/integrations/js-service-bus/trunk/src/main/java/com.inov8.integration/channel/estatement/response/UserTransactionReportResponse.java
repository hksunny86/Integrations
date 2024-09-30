package com.inov8.integration.channel.estatement.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pdf_data_uri_base64",
        "response_code",
        "response_details"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTransactionReportResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(UserTransactionReportResponse.class.getSimpleName());

    @JsonProperty("pdf_data_uri_base64")
    private String pdfDataUriBase64;
    @JsonProperty("response_code")
    private String responseCode;
    @JsonProperty("response_details")
    private String responseDetails;

    public String getPdfDataUriBase64() {
        return pdfDataUriBase64;
    }

    public void setPdfDataUriBase64(String pdfDataUriBase64) {
        this.pdfDataUriBase64 = pdfDataUriBase64;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode() != null) {
            if (this.getResponseCode().equalsIgnoreCase("00")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            }
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDetails());
            i8SBSwitchControllerResponseVO.setPdfDataUriBase64(this.getPdfDataUriBase64());
        }

        return i8SBSwitchControllerResponseVO;
    }
}