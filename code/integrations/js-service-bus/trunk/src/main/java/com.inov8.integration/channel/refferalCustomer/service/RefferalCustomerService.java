package com.inov8.integration.channel.refferalCustomer.service;

import com.inov8.integration.channel.T24Api.service.T24ApiMockService;
import com.inov8.integration.channel.refferalCustomer.request.DynamicReffrelRequest;
import com.inov8.integration.channel.refferalCustomer.request.NovaCustomerSMSAlertRequest;
import com.inov8.integration.channel.refferalCustomer.request.RefferalCustomerRequest;
import com.inov8.integration.channel.refferalCustomer.response.DynamicReffrelResponse;
import com.inov8.integration.channel.refferalCustomer.response.NovaCustomerSMSAlertResponse;
import com.inov8.integration.channel.refferalCustomer.response.RefferalCustomerResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.Iterator;

@Service
public class RefferalCustomerService {

    private static Logger logger = LoggerFactory.getLogger(RefferalCustomerService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${refferal.customer.url}")
    private String refferalCustomer;
    @Value("${nova.customer.alert.url}")
    private String novaAlertSMS;
    @Value("${nova.customer.alert.access.token}")
    private String accessToken;

    @Value("${nova.customer.reffrel.url}")
    private String dynamicURL;


    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public RefferalCustomerResponse refferalCustomerResponse(RefferalCustomerRequest request) throws Exception {

        RefferalCustomerResponse refferalCustomerResponse = new RefferalCustomerResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Refferal Customer: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();
            refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(response, RefferalCustomerResponse.class);

            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Refferal Customer " + refferalCustomer);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(refferalCustomer);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Refferal Customer Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {

                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(res.getBody(), RefferalCustomerResponse.class);
                logger.info("Refferal Customer Response Received from Server : " + res.getBody());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(result, RefferalCustomerResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(resp, RefferalCustomerResponse.class);

                    }

                }
            }
        }
        return refferalCustomerResponse;
    }

    public NovaCustomerSMSAlertResponse refferalCustomerResponse(NovaCustomerSMSAlertRequest request) throws Exception {
        NovaCustomerSMSAlertResponse refferalCustomerResponse = new NovaCustomerSMSAlertResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        String requesJson;
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Nova Customer Notification ALert: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();
            requesJson = mock.transactionValidation();
            refferalCustomerResponse = (NovaCustomerSMSAlertResponse) JSONUtil.jsonToObject(requesJson, NovaCustomerSMSAlertResponse.class);
        } else {
            logger.info("Nova Customer Notification ALert " + this.novaAlertSMS);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.novaAlertSMS);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_Token", this.accessToken);
            requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Nova Customer Notification ALert Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity(requesJson, headers);
            Iterator var8 = this.restTemplate.getMessageConverters().iterator();

            while (var8.hasNext()) {
                HttpMessageConverter converter = (HttpMessageConverter) var8.next();
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            try {
                ResponseEntity<String> res = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Nova Customer Notification ALert Response Received from Server : " + res.getStatusCode());
                if (res.getStatusCodeValue() == 200) {
                    refferalCustomerResponse.setResponseCode("00");
                    refferalCustomerResponse.setResponseDesc("SuccessFull");
                }
            } catch (RestClientException var11) {
                if (var11 instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) var11).getStatusCode().toString();
                    String resp;
                    if (response.equals("204")) {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode("204");
                        refferalCustomerResponse.setResponseDesc("No Content");
                    } else if (response.equals("405")) {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode("405");
                        refferalCustomerResponse.setResponseDesc("Method Not Allowed");
                    } else {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode(((HttpStatusCodeException) var11).getStatusCode().toString());
                        refferalCustomerResponse.setResponseDesc("Method Not Allowed");
                    }
                }
            }
        }

        return refferalCustomerResponse;
    }

    public DynamicReffrelResponse dynamicRefferalCustomerResponse(DynamicReffrelRequest request) throws Exception {
        DynamicReffrelResponse refferalCustomerResponse = new DynamicReffrelResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        String requesJson;
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Nova Customer Notification ALert: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();
            requesJson = mock.transactionValidation();
            refferalCustomerResponse = (DynamicReffrelResponse) JSONUtil.jsonToObject(requesJson, DynamicReffrelResponse.class);
        } else {
            logger.info("Dynamic Reffrel Customer " + this.dynamicURL);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.dynamicURL);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_Token", this.accessToken);
            requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Dynamic Reffrel Customer Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity(requesJson, headers);
            Iterator var8 = this.restTemplate.getMessageConverters().iterator();

            while (var8.hasNext()) {
                HttpMessageConverter converter = (HttpMessageConverter) var8.next();
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            try {
                ResponseEntity<String> res = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Dynamic Reffrel Customer Received from Server : " + res.getStatusCode());
                if (String.valueOf(res.getStatusCode()).equals("200")) {
                    refferalCustomerResponse.setResponseCode("00");
                    refferalCustomerResponse.setResponseMessage("SuccessFull");
                } else {
                    refferalCustomerResponse.setResponseCode(String.valueOf(res.getStatusCode()));
                }

            } catch (RestClientException var11) {
                if (var11 instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) var11).getStatusCode().toString();
                    String resp;
                    if (response.equals("204")) {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode("204");
                        refferalCustomerResponse.setResponseMessage("No Content");
                    } else if (response.equals("405")) {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode("405");
                        refferalCustomerResponse.setResponseMessage("Method Not Allowed");
                    } else {
                        resp = ((HttpStatusCodeException) var11).getResponseBodyAsString();
                        refferalCustomerResponse.setResponseCode(((HttpStatusCodeException) var11).getStatusCode().toString());
                        refferalCustomerResponse.setResponseMessage("Method Not Allowed");
                    }
                }
            }
        }

        return refferalCustomerResponse;
    }

}
