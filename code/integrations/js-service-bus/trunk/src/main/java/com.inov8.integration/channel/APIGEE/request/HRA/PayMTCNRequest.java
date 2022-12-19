package com.inov8.integration.channel.APIGEE.request.HRA;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by inov8 on 5/28/2018.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayMTCNRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMobile(i8SBSwitchControllerRequestVO.getMobilePhone());
        this.setDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        Kycs kycs = new Kycs();
        kycs.setFirstName(i8SBSwitchControllerRequestVO.getFirstName());
        kycs.setLastName(i8SBSwitchControllerRequestVO.getLastName());
        kycs.setCity(i8SBSwitchControllerRequestVO.getSenderCity());
        this.setKycs(kycs);


    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[FAILED] CNIC: " + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getMobile())) {
            throw new I8SBValidationException("[FAILED] Mobile Phone: " + this.getMobile());
        }
        if (StringUtils.isEmpty(this.getKycs().getFirstName())) {
            throw new I8SBValidationException("[FAILED] FirstName: " + this.getKycs().getFirstName());
        }
        if (StringUtils.isEmpty(this.getKycs().getLastName())) {
            throw new I8SBValidationException("[FAILED] LastName: " + this.getKycs().getLastName());
        }

        return true;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("kycs")
    private Kycs kycs;




    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Kycs getKycs() {
        return kycs;
    }

    public void setKycs(Kycs kycs) {
        this.kycs = kycs;
    }
}
