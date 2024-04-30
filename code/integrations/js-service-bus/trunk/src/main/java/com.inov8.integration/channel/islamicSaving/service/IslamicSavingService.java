package com.inov8.integration.channel.islamicSaving.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.islamicSaving.request.IslamicSavingProfitRequest;
import com.inov8.integration.channel.islamicSaving.request.IslamicSavingWithdrawalRequest;
import com.inov8.integration.channel.islamicSaving.response.IslamicSavingProfitResponse;
import com.inov8.integration.channel.islamicSaving.response.IslamicSavingWithdrawalResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class IslamicSavingService {

    private static Logger logger = LoggerFactory.getLogger(IslamicSavingService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String islamicSavingWithdrawalUrl = PropertyReader.getProperty("islamic.saving.withdrawl");
    private String islamicSavingWithdrawalAccessToken = PropertyReader.getProperty("islamic.saving.withdrawl.accessToken");
    private String islamicSavingProfitUrl = PropertyReader.getProperty("islamic.saving.profit");
    private String islamicSavingProfitAccessToken = PropertyReader.getProperty("islamic.saving.profit.accessToken");
    private I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public String getResponseFromAPI(Map<String, String> headerMap, Map<String, Object> postParam, String url) throws Exception {
        try {
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(headerMap);

            // Create request entity
            HttpEntity<?> requestEntity = new HttpEntity<>(postParam, headers);

            logger.info("Sending request to " + url);
            logger.info("Request Entity: " + requestEntity);

            // Create RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response;

            // Send POST request and get response
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // Log response details
            logger.info("Response: " + response);

            // Check if the status code is 200 (OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                String entityResponse = response.getBody();
                logger.info("Successful response: " + entityResponse);
                return !entityResponse.isEmpty() ? entityResponse : null;
            } else {
                logger.info("Unsuccessful response. HTTP Status Code: " + response.getStatusCode().value());
                return response.getBody();
            }
        } catch (HttpStatusCodeException e) {
            logger.info("HTTP Status Code Exception: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new Exception(e.getMessage());
        } catch (RestClientException e) {
            logger.info("RestClientException: " + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            logger.info("Exception occurred: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public IslamicSavingWithdrawalResponse islamicSavingWithdrawalResponse(IslamicSavingWithdrawalRequest request) throws RestClientException, HttpStatusCodeException, Exception {

        IslamicSavingWithdrawalResponse response = new IslamicSavingWithdrawalResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "\n" +
                    "    \"responseMessage\": \"Success\",\n" +
                    "\n" +
                    "    \"traceId\": \"\",\n" +
                    "\n" +
                    "    \"body\": {\n" +
                    "\n" +
                    "        \"responseCode\": \"\",\n" +
                    "\n" +
                    "        \"responseDescription\": \"\"\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "}";
            response = (IslamicSavingWithdrawalResponse) JSONUtil.jsonToObject(json, IslamicSavingWithdrawalResponse.class);
            logger.info("Mock Response Code for Islamic Saving Withdrawal Response: " + response.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("AccessToken", islamicSavingWithdrawalAccessToken);
            postParam.put("id", request.getId());
            postParam.put("mobileNumber", request.getMobileNumber());
            postParam.put("amount", request.getAmount());
            postParam.put("transactionId", request.getTransactionId());
            logger.info("Request body of Islamic Saving Withdrawal " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, islamicSavingWithdrawalUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, IslamicSavingWithdrawalResponse.class);
                }
            } catch (Exception e) {
                if (e instanceof HttpStatusCodeException) {
                    HttpStatusCodeException httpException = (HttpStatusCodeException) e;
                    String responseCode = httpException.getStatusCode().toString();
                    String responseBody = httpException.getResponseBodyAsString();
                    switch (responseCode) {
                        case "203":
                        case "400":
                        case "401":
                        case "422":
                        case "500":
                            try {
                                response = objectMapper.readValue(responseBody, IslamicSavingWithdrawalResponse.class);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                logger.info("Negative Response from Client " + responseBody +
                                        "\nStatus Code received " + httpException.getStatusCode().toString());
                            }
                            break;
                        default:
                            logger.info("Negative Response from Client " + responseBody +
                                    "\nStatus Code received " + httpException.getStatusCode().toString());
                            break;
                    }
                } else if (e instanceof ResourceAccessException) {
                    logger.info("ResourceAccessException " + e.getMessage() +
                            "\nMessage received " + e.getMessage());
                } else {
                    logger.error("Unexpected Exception:  " + e.getMessage());
                    response.setResponseCode("500");
                    response.setResponseMessage(e.getLocalizedMessage());
                }
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Islamic Saving Withdrawal Response processed in: " + difference + " millisecond");
        }
        return response;
    }

    public IslamicSavingProfitResponse islamicSavingProfitResponse(IslamicSavingProfitRequest request) throws RestClientException, HttpStatusCodeException, Exception {

        IslamicSavingProfitResponse response = new IslamicSavingProfitResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "\n" +
                    "    \"responseMessage\": \"Success\",\n" +
                    "\n" +
                    "    \"traceId\": \"\",\n" +
                    "\n" +
                    "    \"body\": {\n" +
                    "\n" +
                    "        \"responseCode\": \"\",\n" +
                    "\n" +
                    "        \"responseDescription\": \"\"\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "}";
            response = (IslamicSavingProfitResponse) JSONUtil.jsonToObject(json, IslamicSavingProfitResponse.class);
            logger.info("Mock Response Code for Islamic Saving Profit Response: " + response.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("AccessToken", islamicSavingProfitAccessToken);
            postParam.put("mobileNumber", request.getMobileNumber());
            postParam.put("amount", request.getAmount());
            logger.info("Request body of Islamic Saving Profit " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, islamicSavingProfitUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, IslamicSavingProfitResponse.class);
                }
            } catch (Exception e) {
                if (e instanceof HttpStatusCodeException) {
                    HttpStatusCodeException httpException = (HttpStatusCodeException) e;
                    String responseCode = httpException.getStatusCode().toString();
                    String responseBody = httpException.getResponseBodyAsString();
                    switch (responseCode) {
                        case "203":
                        case "400":
                        case "401":
                        case "422":
                        case "500":
                            try {
                                response = objectMapper.readValue(responseBody, IslamicSavingProfitResponse.class);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                logger.info("Negative Response from Client " + responseBody +
                                        "\nStatus Code received " + httpException.getStatusCode().toString());
                            }
                            break;
                        default:
                            logger.info("Negative Response from Client " + responseBody +
                                    "\nStatus Code received " + httpException.getStatusCode().toString());
                            break;
                    }
                } else if (e instanceof ResourceAccessException) {
                    logger.info("ResourceAccessException " + e.getMessage() +
                            "\nMessage received " + e.getMessage());
                } else {
                    logger.error("Unexpected Exception:  " + e.getMessage());
                    response.setResponseCode("500");
                    response.setResponseMessage(e.getLocalizedMessage());
                }
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Islamic Saving Profit Response processed in: " + difference + " millisecond");
        }
        return response;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
