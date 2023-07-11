package com.inov8.integration.channel.raast.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "aliasId",
        "processingCode",
        "merchantType"

})
public class DeleteAliasRequest extends Request implements Serializable {

    @JsonProperty("aliasId")
    private String aliasId;
    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("merchantType")
    private String merchantType;

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("211011");
        this.setMerchantType("0098");
        this.setAliasId(i8SBSwitchControllerRequestVO.getAliasId());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getAliasId())) {
            throw new I8SBValidationException("[Failed] Alias Id:" + this.getAliasId());
        }

        return true;
    }
}
