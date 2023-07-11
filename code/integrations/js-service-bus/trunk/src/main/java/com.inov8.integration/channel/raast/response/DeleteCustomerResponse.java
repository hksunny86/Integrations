package com.inov8.integration.channel.raast.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Response"
})
public class DeleteCustomerResponse extends Response implements Serializable {

    @JsonProperty("Response")
    private DelCustomer response;
    private String responseCode;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("Response")
    public DelCustomer getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(DelCustomer response) {
        this.response = response;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getResponse().getResponseDescription().get(0));
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponse().getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription((Objects.requireNonNull(this.getResponse().getResponseDescription().get(0))));
        }
        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseDescription"
})
class DelCustomer {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private List<String> responseDescription;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<String> getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(List<String> responseDescription) {
        this.responseDescription = responseDescription;
    }

}
