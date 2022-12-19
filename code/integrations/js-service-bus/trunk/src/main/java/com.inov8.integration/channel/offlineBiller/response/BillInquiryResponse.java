package com.inov8.integration.channel.offlineBiller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

public class BillInquiryResponse extends Response {

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
            i8SBSwitchControllerResponseVO.setConsumerNumber(this.getData().getCustomerRef());
            i8SBSwitchControllerResponseVO.setDueDate(this.getData().getDueDate());
            i8SBSwitchControllerResponseVO.setBillStatus(this.getData().getBillStatus());

            i8SBSwitchControllerResponseVO.setMessage(this.getData().getMessage());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponsecode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }
        return i8SBSwitchControllerResponseVO;
    }

    @XmlRootElement
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Data {
        private String customerRef;
        private String customerName;
        private String billingMonth;
        private String billingYear;
        private String billStatus;
        private String dueDate;
        private String amount;
        private String amountAfterDd;
        private String message;


        public String getBillStatus() {
            return billStatus;
        }

        public void setBillStatus(String billStatus) {
            this.billStatus = billStatus;
        }

        public String getCustomerRef() {
            return customerRef;
        }

        public void setCustomerRef(String customerRef) {
            this.customerRef = customerRef;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
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

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
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
