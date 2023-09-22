package com.inov8.integration.channel.esb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "merchantType",
        "traceNo",
        "dateTime",
        "responseCode",
        "response"
})
public class EsbBillInquiryResponse extends Response {

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
    @JsonProperty("response")
    private BillInquiry billInquiry;

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

    public BillInquiry getBillInquiry() {
        return billInquiry;
    }

    public void setBillInquiry(BillInquiry billInquiry) {
        this.billInquiry = billInquiry;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        }
        i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
        i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
        i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceNo());
        i8SBSwitchControllerResponseVO.setDateTime(this.getDateTime());
        i8SBSwitchControllerResponseVO.setConsumerDetail(this.billInquiry.getConsumerDetail());
        i8SBSwitchControllerResponseVO.setBillStatus(this.billInquiry.getBillStatus());
        i8SBSwitchControllerResponseVO.setDueDate(this.billInquiry.getDueDate());
        i8SBSwitchControllerResponseVO.setAmountWithinDueDate(this.billInquiry.getAmountWithinDueDate());
        i8SBSwitchControllerResponseVO.setAmountAfterDueDate(this.billInquiry.getAmountAfterDueDate());
        i8SBSwitchControllerResponseVO.setBillingMonth(this.billInquiry.getBillingMonth());
        i8SBSwitchControllerResponseVO.setDatePaid(this.billInquiry.getDatePaid());
        i8SBSwitchControllerResponseVO.setAmountPaid(this.billInquiry.getAmountPaid());
        i8SBSwitchControllerResponseVO.setTransactionId(this.billInquiry.getTranAuthId());
        i8SBSwitchControllerResponseVO.setReserved(this.billInquiry.getReserved());

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Response_code",
        "Consumer_Detail",
        "Bill_Status",
        "Due_Date",
        "Amount_Within_DueDate",
        "Amount_After_DueDate",
        "Billing_Month",
        "Date_Paid",
        "Amount_Paid",
        "Tran_Auth_Id",
        "Reserved"
})
class BillInquiry {

    @JsonProperty("Response_code")
    private String responseCode;
    @JsonProperty("Consumer_Detail")
    private String consumerDetail;
    @JsonProperty("Bill_Status")
    private String billStatus;
    @JsonProperty("Due_Date")
    private String dueDate;
    @JsonProperty("Amount_Within_DueDate")
    private String amountWithinDueDate;
    @JsonProperty("Amount_After_DueDate")
    private String amountAfterDueDate;
    @JsonProperty("Billing_Month")
    private String billingMonth;
    @JsonProperty("Date_Paid")
    private String datePaid;
    @JsonProperty("Amount_Paid")
    private String amountPaid;
    @JsonProperty("Tran_Auth_Id")
    private String tranAuthId;
    @JsonProperty("Reserved")
    private String reserved;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getConsumerDetail() {
        return consumerDetail;
    }

    public void setConsumerDetail(String consumerDetail) {
        this.consumerDetail = consumerDetail;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmountWithinDueDate() {
        return amountWithinDueDate;
    }

    public void setAmountWithinDueDate(String amountWithinDueDate) {
        this.amountWithinDueDate = amountWithinDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getTranAuthId() {
        return tranAuthId;
    }

    public void setTranAuthId(String tranAuthId) {
        this.tranAuthId = tranAuthId;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
