package com.inov8.integration.channel.raast.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseDescription",
        "till",
        "qrText"
})
public class QrRastRegistrationResponse extends Response {
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private String responseDescription;
    @JsonProperty("till")

    private String till;
    @JsonProperty("qrText")

    private String qrText;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getTill() {
        return till;
    }

    public void setTill(String till) {
        this.till = till;
    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("00")){
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
            i8SBSwitchControllerResponseVO.setQr(this.getQrText());
            i8SBSwitchControllerResponseVO.setMerchantId(this.getTill());

        }else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        }
        return i8SBSwitchControllerResponseVO;
    }
}
