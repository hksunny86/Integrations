package com.inov8.integration.channel.tasdeeq.service;

import com.inov8.integration.channel.tasdeeq.mock.TasdeeqMock;
import com.inov8.integration.channel.tasdeeq.request.AuthenticateUpdatedRequest;
import com.inov8.integration.channel.tasdeeq.request.CustomAnalyticsRequest;
import com.inov8.integration.channel.tasdeeq.response.AuthenticateUpdatedResponse;
import com.inov8.integration.channel.tasdeeq.response.CustomAnalyticsResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
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

import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@Service
public class TasdeeqService {

    private static Logger logger = LoggerFactory.getLogger(TasdeeqService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String authenticateUpdatedUrl = PropertyReader.getProperty("tasdeeq.AuthenticateUpdated");
    private String customAnalyticsUrl = PropertyReader.getProperty("tasdeeq.CustomAnalytics");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public AuthenticateUpdatedResponse authenticateUpdatedResponse(AuthenticateUpdatedRequest authenticateUpdatedRequest) {

        AuthenticateUpdatedResponse authenticateUpdatedResponse = new AuthenticateUpdatedResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            TasdeeqMock tasdeeqMock = new TasdeeqMock();
            String response = tasdeeqMock.authenticatedUpdated();
            authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(response, AuthenticateUpdatedResponse.class);
            logger.info("Response Code for Authenticate Updated Request: " + authenticateUpdatedResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.authenticateUpdatedUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(authenticateUpdatedRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Sending Authenticate Updated Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                logger.info("Response received from client " + res1.getBody());
                authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(res1.getBody(), AuthenticateUpdatedResponse.class);
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        authenticateUpdatedResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(result, AuthenticateUpdatedResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        authenticateUpdatedResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(result, AuthenticateUpdatedResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        authenticateUpdatedResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(result, AuthenticateUpdatedResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        authenticateUpdatedResponse = (AuthenticateUpdatedResponse) JSONUtil.jsonToObject(result, AuthenticateUpdatedResponse.class);
                    }
                }
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Authenticate Updated Request processed in: " + difference + " millisecond");
        }
        return authenticateUpdatedResponse;
    }

    public CustomAnalyticsResponse customAnalyticsResponse(CustomAnalyticsRequest customAnalyticsRequest) {
        CustomAnalyticsResponse customAnalyticsResponse = new CustomAnalyticsResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + this.i8SBSwitchControllerRequestVO.getRequestType());
            TasdeeqMock tasdeeqMock = new TasdeeqMock();
            String response = tasdeeqMock.customAnalytics();
            logger.info("Access Token " + i8SBSwitchControllerRequestVO.getAccessToken());
            customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(response, CustomAnalyticsResponse.class);
            logger.info("Mock Response Code for Custom Analytics Request: " + customAnalyticsResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.customAnalyticsUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String token = i8SBSwitchControllerRequestVO.getAccessToken();
            logger.info("Access Token " + token);
            headers.add("Authorization", "bearer " + token);
            String requestJSON = JSONUtil.getJSON(customAnalyticsRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Sending Custom Analytics Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                logger.info("Response received from client " + res1.getBody());
                customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(res1.getBody(), CustomAnalyticsResponse.class);
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customAnalyticsResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(result, CustomAnalyticsResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customAnalyticsResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(result, CustomAnalyticsResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customAnalyticsResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(result, CustomAnalyticsResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received " + ((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse.setStatusCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        customAnalyticsResponse = (CustomAnalyticsResponse) JSONUtil.jsonToObject(result, CustomAnalyticsResponse.class);
                    }
                }
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Custom Analytics Request processed in: " + difference + " millisecond");
        }

        return customAnalyticsResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
