package com.inov8.integration.channel.warmbyte.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.estatement.request.UserTransactionReportRequest;
import com.inov8.integration.channel.estatement.response.UserTransactionReportResponse;
import com.inov8.integration.channel.warmbyte.request.DeductionIntimationRequest;
import com.inov8.integration.channel.warmbyte.request.ReferrerStatusRequest;
import com.inov8.integration.channel.warmbyte.response.DeductionIntimationResponse;
import com.inov8.integration.channel.warmbyte.response.ReferrerStatusResponse;
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
import java.util.Objects;

@Service
public class WarmbyteService {

    private static Logger logger = LoggerFactory.getLogger(WarmbyteService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String deductionIntimationUrl = PropertyReader.getProperty("warmbyte.deduction.url");
    private String referrerStatusUrl = PropertyReader.getProperty("warmbyte.referrer.status.url");
    private String referrerStatusToken = PropertyReader.getProperty("warmbyte.referrer.status.token");
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

    private void handleRestClientException(RestClientException e, I8SBSwitchControllerResponseVO responseVO) {
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
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        responseVO = objectMapper.readValue(responseBody, I8SBSwitchControllerResponseVO.class);
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
            logger.error("Unexpected RestClientException: " + e.getMessage());
        }
    }

    public DeductionIntimationResponse deductionIntimationResponse(DeductionIntimationRequest request) {

        DeductionIntimationResponse response = new DeductionIntimationResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock7")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"responseMessage\": \"Success\"\n" +
                    "}";
            response = (DeductionIntimationResponse) JSONUtil.jsonToObject(json, DeductionIntimationResponse.class);
            logger.info("Mock Response Code for Deduction Intimation Response: " + Objects.requireNonNull(response).getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            postParam.put("mobileNo", request.getMobileNo());
            logger.info("Request body of Deduction Intimation  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, deductionIntimationUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, DeductionIntimationResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Deduction Intimation Request processed in: " + difference + " millisecond");
        }
        return response;
    }

    public ReferrerStatusResponse referrerStatusResponse(ReferrerStatusRequest request) {

        ReferrerStatusResponse response = new ReferrerStatusResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock78")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\"responseCode\":\"0020\",\"responseMessage\":\"Processing\"}";
            response = (ReferrerStatusResponse) JSONUtil.jsonToObject(json, ReferrerStatusResponse.class);
            logger.info("Mock Response Code for Referrer Status Response: " + Objects.requireNonNull(response).getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + referrerStatusToken);
            postParam.put("mobileNo", request.getMsisdn());
            postParam.put("cnic", request.getCnic());
            logger.info("Request body of Referrer Status  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, referrerStatusUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, ReferrerStatusResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Referrer Status Request processed in: " + difference + " millisecond");
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
