package com.inov8.integration.channel.offlineBiller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

public class BillPaymentResponse extends Response {

    private String responsecode;
    private String Messages;
    private Data data;

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponsecode().equals("1")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
            i8SBSwitchControllerResponseVO.setBillAmount(this.getData().getAmount());
            i8SBSwitchControllerResponseVO.setBillAmountAfterDueDate(this.getData().getAmountAfterDd());
            i8SBSwitchControllerResponseVO.setCustomerName(this.getData().getCustomerName());
            i8SBSwitchControllerResponseVO.setConsumerNumber(this.getData().getCustomerReference());
            i8SBSwitchControllerResponseVO.setDueDate(this.getData().getDueDate());
            i8SBSwitchControllerResponseVO.setBillStatus(this.getData().getStatus());
            i8SBSwitchControllerResponseVO.setMessage(this.getData().getMessage());
            i8SBSwitchControllerResponseVO.setUserName(this.getData().getCreateuser());

        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponsecode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }

        return i8SBSwitchControllerResponseVO;
    }


    @XmlRootElement
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Data {
        private String fileDetailId;
        private String amount;
        private String amountAfterDd;
        private String billingMonth;
        private String billingYear;
        private String createuser;
        private String customerName;
        private String customerReference;
        private String dueDate;
        private String microbankTransactionCode;
        private String status;
        private String fileHeadId;
        private String message;


        public String getFileDetailId() {
            return fileDetailId;
        }

        public void setFileDetailId(String fileDetailId) {
            this.fileDetailId = fileDetailId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmountAfterDd() {
            return amountAfterDd;
        }

        public void setAmountAfterDd(String amountAfterDd) {
            this.amountAfterDd = amountAfterDd;
        }

        public String getBillingMonth() {
            return billingMonth;
        }

        public void setBillingMonth(String billingMonth) {
            this.billingMonth = billingMonth;
        }

        public String getBillingYear() {
            return billingYear;
        }

        public void setBillingYear(String billingYear) {
            this.billingYear = billingYear;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerReference() {
            return customerReference;
        }

        public void setCustomerReference(String customerReference) {
            this.customerReference = customerReference;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getMicrobankTransactionCode() {
            return microbankTransactionCode;
        }

        public void setMicrobankTransactionCode(String microbankTransactionCode) {
            this.microbankTransactionCode = microbankTransactionCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFileHeadId() {
            return fileHeadId;
        }

        public void setFileHeadId(String fileHeadId) {
            this.fileHeadId = fileHeadId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessages() {
        return Messages;
    }

    public void setMessages(String messages) {
        Messages = messages;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
