//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inov8.integration.channel.sendPushNotification.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"SendPushNotifications"})
public class SendPushNotificationsRequest extends Request {
    @JsonProperty("SendPushNotifications")
    private List<SendPushNotification> sendPushNotifications = null;
    private List<SendPushNotification> sendPushNotificationsRequestList;

    public SendPushNotificationsRequest() {
    }

    @JsonProperty("SendPushNotifications")
    public List<SendPushNotification> getSendPushNotifications() {
        return this.sendPushNotifications;
    }

    @JsonProperty("SendPushNotifications")
    public void setSendPushNotifications(List<SendPushNotification> sendPushNotifications) {
        this.sendPushNotifications = sendPushNotifications;
    }

    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        String message = i8SBSwitchControllerRequestVO.getMessage();
        String regex = "(?i) Your available balance is: \\w+\\.? \\d+\\.?";
        String replacedMessage = message.replaceAll(regex, " ");

        SendPushNotification request = new SendPushNotification();
        sendPushNotificationsRequestList = new ArrayList();

//        request.setToken(PropertyReader.getProperty("sendPushNotification.token"));
        request.setMobile(i8SBSwitchControllerRequestVO.getMobileNumber());
        request.setTitle(i8SBSwitchControllerRequestVO.getTitle());
        request.setMessage(replacedMessage);
        if (StringUtil.isNullOrEmpty(i8SBSwitchControllerRequestVO.getMessageType())) {
            request.setType("ZINDIGI");
        } else {
            request.setType(i8SBSwitchControllerRequestVO.getMessageType());
        }
        sendPushNotificationsRequestList.add(request);

        this.setSendPushNotifications(sendPushNotificationsRequestList);
    }

    public boolean validateRequest() throws I8SBValidationException {

        List<SendPushNotification> sendPushNotifications = this.getSendPushNotifications();
        SendPushNotification sendPushNotification = new SendPushNotification();
        sendPushNotifications.forEach(volist -> {
            sendPushNotification.setMobile(volist.getMobile());
            sendPushNotification.setMessage(volist.getMessage());
            sendPushNotification.setTitle(volist.getTitle());
            sendPushNotification.setType(volist.getType());
        });

        if (StringUtils.isEmpty(sendPushNotification.getMobile())) {
            throw new I8SBValidationException("[Failed] Mobile Number:" + sendPushNotification.getMobile());
        }
        if (StringUtils.isEmpty(sendPushNotification.getMessage())) {
            throw new I8SBValidationException("[Failed] Transaction Message:" + sendPushNotification.getMessage());
        }
        if (StringUtils.isEmpty(sendPushNotification.getTitle())) {
            throw new I8SBValidationException("[Failed] Message Title:" + sendPushNotification.getTitle());
        }

        return true;
    }
}

@XmlRootElement
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile",
        "Token",
        "Message",
        "Title",
        "Type"
})
class SendPushNotification {
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Type")
    private String type;

    SendPushNotification() {
    }

    @JsonProperty("Mobile")
    public String getMobile() {
        return this.mobile;
    }

    @JsonProperty("Mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty("Token")
    public String getToken() {
        return this.token;
    }

    @JsonProperty("Token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return this.message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("Title")
    public String getTitle() {
        return this.title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("Type")
    public String getType() {
        return this.type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }
}
