//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inov8.integration.channel.sendPushNotification.service;

import com.inov8.integration.channel.sendPushNotification.mock.SendPushNotificationMock;
import com.inov8.integration.channel.sendPushNotification.request.SendPushNotificationsRequest;
import com.inov8.integration.channel.sendPushNotification.response.SendPushNotificationResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SendPushNotificationService {
    private static Logger logger = LoggerFactory.getLogger(SendPushNotificationService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String sendPushNotificationURL = PropertyReader.getProperty("sendPushNotification.endpoint.url");
    private String accessToken = PropertyReader.getProperty("sendPushNotification.token");

    public SendPushNotificationService() {
    }

    public SendPushNotificationResponse sendPushNotificationResponse(SendPushNotificationsRequest sendPushNotificationRequest) {
        SendPushNotificationResponse sendPushNotificationResponse = new SendPushNotificationResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        new I8SBSwitchControllerResponseVO();
        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            SendPushNotificationMock sendPushNotificationMock = new SendPushNotificationMock();
            String response = sendPushNotificationMock.sendPushNotificationMockResponse();
            sendPushNotificationResponse = (SendPushNotificationResponse) JSONUtil.jsonToObject(response, SendPushNotificationResponse.class);
            logger.info("Response Code for Send Push Notification : " + sendPushNotificationResponse.getResponseCode());
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.sendPushNotificationURL);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            String requestJSON = JSONUtil.getJSON(sendPushNotificationRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Sending Push Notification Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    sendPushNotificationResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    sendPushNotificationResponse.setResponseDescription("Success");
                    sendPushNotificationResponse.setPushNotification("Sent");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        sendPushNotificationResponse = (SendPushNotificationResponse) JSONUtil.jsonToObject(result, SendPushNotificationResponse.class);
                        sendPushNotificationResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        sendPushNotificationResponse = (SendPushNotificationResponse) JSONUtil.jsonToObject(result, SendPushNotificationResponse.class);
                        sendPushNotificationResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        sendPushNotificationResponse = (SendPushNotificationResponse) JSONUtil.jsonToObject(result, SendPushNotificationResponse.class);
                        sendPushNotificationResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Push Notification request processed in: " + difference + " millisecond");
        }

        return sendPushNotificationResponse;
    }
}
