package com.inov8.integration.channel.jsdebitcard.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by inov8 on 5/28/2018.
 */

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebitCardExportResponse extends Response {

    @JsonProperty("code")
    private String code;

    @JsonProperty("description")
    private String description;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.code);
        i8SBSwitchControllerResponseVO.setError(this.description);
        return i8SBSwitchControllerResponseVO;
    }
}
