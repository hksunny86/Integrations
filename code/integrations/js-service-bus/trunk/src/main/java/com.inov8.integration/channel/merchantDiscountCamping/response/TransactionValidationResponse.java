package com.inov8.integration.channel.merchantDiscountCamping.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionValidationResponse extends Response {
    private String responsecode;
    private String messages;
    Data data;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponsecode().equals("1")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponsecode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }

        return i8SBSwitchControllerResponseVO;
    }
    @XmlRootElement
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Data {
        private String statusDescr;
        private String status;
        private String statusCode;

        public String getStatusDescr() {
            return statusDescr;
        }

        public void setStatusDescr(String statusDescr) {
            this.statusDescr = statusDescr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }
    }
}
