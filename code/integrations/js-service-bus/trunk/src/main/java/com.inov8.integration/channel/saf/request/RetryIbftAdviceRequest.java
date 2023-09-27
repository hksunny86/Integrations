package com.inov8.integration.channel.saf.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "saf1linkIntegrationId"
})
public class RetryIbftAdviceRequest extends Request {

    @JsonProperty("saf1linkIntegrationId")
    private String saf1linkIntegrationId;

    public String getSaf1linkIntegrationId() {
        return saf1linkIntegrationId;
    }

    public void setSaf1linkIntegrationId(String saf1linkIntegrationId) {
        this.saf1linkIntegrationId = saf1linkIntegrationId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setSaf1linkIntegrationId(i8SBSwitchControllerRequestVO.getSaf1linkIntegrationId());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getSaf1linkIntegrationId())) {
            throw new I8SBValidationException("[Failed] SAF Integration Id:" + this.getSaf1linkIntegrationId());
        }
        return true;
    }
}
