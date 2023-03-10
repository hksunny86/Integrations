package com.inov8.integration.channel.customerdeviceverification.service;

import com.inov8.integration.channel.T24Api.service.T24ApiMockService;
import com.inov8.integration.channel.customerdeviceverification.request.CustomerDeviceUpdateRequest;
import com.inov8.integration.channel.customerdeviceverification.request.CustomerDeviceVerificationRequest;
import com.inov8.integration.channel.customerdeviceverification.request.ZindigiCustomerUnBlock;
import com.inov8.integration.channel.customerdeviceverification.response.CustomerDeviceUpdateResponse;
import com.inov8.integration.channel.customerdeviceverification.response.CustomerDeviceVerificationResponse;
import com.inov8.integration.channel.customerdeviceverification.response.ZindigiCustomerUnBlockResponse;
import com.inov8.integration.channel.refferalCustomer.service.RefferalCustomerService;
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

@Service
public class CustomerDeviceVerificationService {

    private static Logger logger = LoggerFactory.getLogger(RefferalCustomerService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${customer.device.verification.url}")
    private String getDeviceVerificationList;
    @Value("${customer.device.update.url}")
    private String customerDeviceVerificationUpdate;

    @Value("${zindigi.customer.device.update.url}")
    private String zindigiCustomerDeviceVerificationUpdate;

    @Value("${accessToken}")
    private String accessToken;

    @Value("${zindigi.accessToken}")
    private String zindigiaccessToken;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public CustomerDeviceVerificationResponse customerDeviceVerificationResponse(CustomerDeviceVerificationRequest request) throws Exception {

        CustomerDeviceVerificationResponse customerDeviceVerificationResponse = new CustomerDeviceVerificationResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Get Customer Device Verification List: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();
            customerDeviceVerificationResponse = (CustomerDeviceVerificationResponse) JSONUtil.jsonToObject(response, CustomerDeviceVerificationResponse.class);

            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Refferal Customer " + getDeviceVerificationList);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(getDeviceVerificationList);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Get Customer Device Verification List:" + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {
            logger.info("Request Send to Get CustomerDevice Verification "+httpEntity);
            ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
            logger.info("Get Customer Device Verification List Response Received from Server 1: " + res.getBody());
            customerDeviceVerificationResponse = (CustomerDeviceVerificationResponse) JSONUtil.jsonToObject(res.getBody(), CustomerDeviceVerificationResponse.class);
            logger.info("Get Customer Device Verification List Response Received from Server : " + res.getStatusCode());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceVerificationResponse = (CustomerDeviceVerificationResponse) JSONUtil.jsonToObject(result, CustomerDeviceVerificationResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceVerificationResponse = (CustomerDeviceVerificationResponse) JSONUtil.jsonToObject(resp, CustomerDeviceVerificationResponse.class);

                    }
                    else if (response.equals("400")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceVerificationResponse = (CustomerDeviceVerificationResponse) JSONUtil.jsonToObject(resp, CustomerDeviceVerificationResponse.class);

                    } else if (response.equals("403")) {
                        logger.info("Get Customer Device Verification List Response Received from Server RestClient Error : " + response);

                    }
                    else {
                        logger.info("Get Customer Device Verification List Response Received from Server RestClient Error : " + response);

                    }

                }
            }

        }
        return customerDeviceVerificationResponse;
    }

    public CustomerDeviceUpdateResponse customerDeviceUpdateResponse(CustomerDeviceUpdateRequest request) throws Exception {

        CustomerDeviceUpdateResponse customerDeviceUpdateResponse = new CustomerDeviceUpdateResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Customer Device Verification: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();
            customerDeviceUpdateResponse = (CustomerDeviceUpdateResponse) JSONUtil.jsonToObject(response, CustomerDeviceUpdateResponse.class);

            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Customer Device Verification" + customerDeviceVerificationUpdate);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(customerDeviceVerificationUpdate);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);

            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Customer Device Verification: " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {

                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                customerDeviceUpdateResponse = (CustomerDeviceUpdateResponse) JSONUtil.jsonToObject(res.getBody(), CustomerDeviceUpdateResponse.class);
                logger.info("Customer Device Verification Response Received from Server : " + res.getBody());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceUpdateResponse = (CustomerDeviceUpdateResponse) JSONUtil.jsonToObject(result, CustomerDeviceUpdateResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceUpdateResponse = (CustomerDeviceUpdateResponse) JSONUtil.jsonToObject(resp, CustomerDeviceUpdateResponse.class);

                    }

                }
            }
        }
        return customerDeviceUpdateResponse;
    }

    public ZindigiCustomerUnBlockResponse zindigiCustomerUnBlockResponse(ZindigiCustomerUnBlock request) throws Exception {

        ZindigiCustomerUnBlockResponse customerDeviceUpdateResponse = new ZindigiCustomerUnBlockResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Customer Device Verification: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();
            customerDeviceUpdateResponse = (ZindigiCustomerUnBlockResponse) JSONUtil.jsonToObject(response, ZindigiCustomerUnBlockResponse.class);

            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Zindigi Customer Device Verification" + zindigiCustomerDeviceVerificationUpdate);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(zindigiCustomerDeviceVerificationUpdate);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", zindigiaccessToken);

            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Zindigi Customer Device Verification: " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {

                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                customerDeviceUpdateResponse = (ZindigiCustomerUnBlockResponse) JSONUtil.jsonToObject(res.getBody(), ZindigiCustomerUnBlockResponse.class);
                logger.info("Zindigi Customer Device Verification Response Received from Server : " + res.getBody());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceUpdateResponse = (ZindigiCustomerUnBlockResponse) JSONUtil.jsonToObject(result, ZindigiCustomerUnBlockResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        customerDeviceUpdateResponse = (ZindigiCustomerUnBlockResponse) JSONUtil.jsonToObject(resp, ZindigiCustomerUnBlockResponse.class);

                    }

                }
            }
        }
        return customerDeviceUpdateResponse;
    }

}
