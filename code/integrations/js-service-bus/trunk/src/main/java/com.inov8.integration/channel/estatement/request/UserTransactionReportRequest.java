package com.inov8.integration.channel.estatement.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "account_number",
        "pdf_start_date",
        "pdf_end_date",
        "generated_by"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTransactionReportRequest extends Request {

    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("pdf_start_date")
    private String pdfStartDate;
    @JsonProperty("pdf_end_date")
    private String pdfEndDate;
    @JsonProperty("generated_by")
    private String generatedBy;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPdfStartDate() {
        return pdfStartDate;
    }

    public void setPdfStartDate(String pdfStartDate) {
        this.pdfStartDate = pdfStartDate;
    }

    public String getPdfEndDate() {
        return pdfEndDate;
    }

    public void setPdfEndDate(String pdfEndDate) {
        this.pdfEndDate = pdfEndDate;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setAccountNumber(i8SBSwitchControllerRequestVO.getAccountNumber());
        this.setPdfStartDate(i8SBSwitchControllerRequestVO.getPdfStartDate());
        this.setPdfEndDate(i8SBSwitchControllerRequestVO.getPdfEndDate());
        this.setGeneratedBy(i8SBSwitchControllerRequestVO.getGeneratedBy());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


