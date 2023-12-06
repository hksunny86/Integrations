package com.inov8.integration.channel.zindigi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responsecode",
        "data",
        "messages",
})
public class TransactionCaptureResponse extends Response implements Serializable {

    @JsonProperty("responsecode")
    private String responseCode;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("messages")
    private String messages;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equalsIgnoreCase("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED_00.getValue());
            if (this.getData() != null) {
                i8SBSwitchControllerResponseVO.setPointEarns(this.data.getPointEarns());
                if (this.getData().getIsTierUpdated().equalsIgnoreCase("Y")) {
                    i8SBSwitchControllerResponseVO.setIsTierUpdated("1");
                } else {
                    i8SBSwitchControllerResponseVO.setIsTierUpdated("0");

                }
            }
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        }
        i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        return i8SBSwitchControllerResponseVO;

    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pointEarns",
        "isTierUpdated"
})
class Data implements Serializable {

    @JsonProperty("pointEarns")
    private String pointEarns;
    @JsonProperty("isTierUpdated")
    private String isTierUpdated;

    public String getPointEarns() {
        return pointEarns;
    }

    public void setPointEarns(String pointEarns) {
        this.pointEarns = pointEarns;
    }

    public String getIsTierUpdated() {
        return isTierUpdated;
    }

    public void setIsTierUpdated(String isTierUpdated) {
        this.isTierUpdated = isTierUpdated;
    }
}