package com.inov8.integration.channel.refferalCustomer.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.M3tech.Request.SendSmsRequest;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NovaCustomerSMSAlertRequest extends Request {
    private static Logger logger = LoggerFactory.getLogger(NovaCustomerSMSAlertRequest.class.getSimpleName());

    @JsonProperty("Mobile")
    private String mobileNo;
    @JsonProperty("Text")
    private String text;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        try {
            String message = i8SBSwitchControllerRequestVO.getSmsText();
            String regex = "(?i) Your available balance is: \\w+\\.? \\d+\\.?";
            String replacedMessage = message.replaceAll(regex, " ");
            this.setText(replacedMessage);
        } catch (Exception e) {
            logger.error("[ Exception ]" + e.getLocalizedMessage(), e);
        }

//        this.setText(i8SBSwitchControllerRequestVO.getSmsText().replaceAll("\n", ""));
        this.setReserved1("");
        this.setReserved2("");
        this.setReserved3("");
        this.setReserved4("");
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }


}
