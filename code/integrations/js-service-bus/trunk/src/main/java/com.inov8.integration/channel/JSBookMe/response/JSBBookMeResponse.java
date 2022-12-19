package com.inov8.integration.channel.JSBookMe.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "OrderRefId",
        "Type",
        "Status"
})
public class JSBBookMeResponse extends Response {
    private String Data;
    private String responseCode;

    @JsonProperty("OrderRefId")
    private List<String> orderRefId = null;

    @JsonProperty("Type")
    private List<String> type = null;

    @JsonProperty("Status")
    private List<String> status = null;

    @JsonProperty("OrderRefId")
    public List<String> getOrderRefId() {
        return orderRefId;
    }

    @JsonProperty("OrderRefId")
    public void setOrderRefId(List<String> orderRefId) {
        this.orderRefId = orderRefId;
    }

    @JsonProperty("Type")
    public List<String> getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(List<String> type) {
        this.type = type;
    }

    @JsonProperty("Status")
    public List<String> getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());

        if (this.getOrderRefId() != null) {
            i8SBSwitchControllerResponseVO.setDescription(String.valueOf(this.getOrderRefId()));
        } else if (this.getType() == null) {
            i8SBSwitchControllerResponseVO.setDescription(this.getData());
        } else {
            i8SBSwitchControllerResponseVO.setDescription(String.valueOf(this.getType()));
        }
        i8SBSwitchControllerResponseVO.setStatus(String.valueOf(this.getStatus()));

        return i8SBSwitchControllerResponseVO;
    }
}
