package com.inov8.integration.channel.euronet.service;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.euronet.request.CardIssuanceRequest;
import com.inov8.integration.channel.euronet.response.CardIssuanceResponse;
import com.inov8.integration.channel.jazz.request.SendSmsRequest;
import com.inov8.integration.channel.jazz.response.SendSmsResponse;
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
public class EuronetService {

    private static Logger logger = LoggerFactory.getLogger(EuronetService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String euronetCardIssuanceUrl = PropertyReader.getProperty("euronet.card.issuance.url");
    private String euronetBearerToken = PropertyReader.getProperty("euronet.bearer.token");
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

    public CardIssuanceResponse cardIssuance(CardIssuanceRequest request) {

        CardIssuanceResponse response = new CardIssuanceResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock7")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = " {\n" +
                    "\"outPartID\":\"JZB\",\n" +
                    "\"outCard\":\"544422******0403\",\n" +
                    "\"outresponseCode\":\"00\",\n" +
                    "\"messageNumber\":\"GUI0000\",\n" +
                    "\"messageText\":\"\",\n" +
                    "\"outEmbCard\":\"5444220000000403\",\n" +
                    "\"outToken\":\"?C9FF2LV88P2YDDF\",\n" +
                    "\"outBIN\":\"54442200\",\n" +
                    "\"outExpDt\":\"20291130\",\n" +
                    "\"outIssDt\":\"20241127\",\n" +
                    "\"outEmbName\":\"SUFIYAN SUFIYAN AHSAN\",\n" +
                    "}";
            response = (CardIssuanceResponse) JSONUtil.jsonToObject(json, CardIssuanceResponse.class);
            logger.info("Mock Response Code for Euronet Card Issuance Response: " + Objects.requireNonNull(response).getOutResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + euronetBearerToken);
            postParam.put("inAuditDS", JSONUtil.getJSON(request.getInAuditDS()));
            postParam.put("inFunction", request.getInFunction());
            postParam.put("participantID", request.getParticipantID());
            postParam.put("inCzID", request.getInCzID());
            postParam.put("inCIFkey", request.getInCIFkey());
            postParam.put("incardNumber", request.getIncardNumber());
            postParam.put("inMember", request.getInMember());
            postParam.put("inBIN", request.getInBIN());
            postParam.put("inCustSchemeCode", request.getInCustSchemeCode());
            postParam.put("inReqCardType", request.getInReqCardType());
            postParam.put("inCardType", request.getInCardType());
            postParam.put("inIBAN", request.getInIBAN());
            postParam.put("inLanguage", request.getInLanguage());
            postParam.put("inSearchName", request.getInSearchName());
            postParam.put("inTitle", request.getInTitle());
            postParam.put("inFName", request.getInFName());
            postParam.put("inMName", request.getInMName());
            postParam.put("inLName", request.getInLName());
            postParam.put("inSufx", request.getInSufx());
            postParam.put("inEmbName", request.getInEmbName());
            postParam.put("inAddress1", request.getInAddress1());
            postParam.put("inAddress2", request.getInAddress2());
            postParam.put("inAddress3", request.getInAddress3());
            postParam.put("inAddress4", request.getInAddress4());
            postParam.put("inCity", request.getInCity());
            postParam.put("instate", request.getInstate());
            postParam.put("inCountry", request.getInCountry());
            postParam.put("inZIPCode", request.getInZIPCode());
            postParam.put("inAddtype", request.getInAddtype());
            postParam.put("inHPhone", request.getInHPhone());
            postParam.put("inDOB", request.getInDOB());
            postParam.put("inSex", request.getInSex());
            postParam.put("inEmpFlg", request.getInEmpFlg());
            postParam.put("inCorPer", request.getInCorPer());
            postParam.put("issuanceFee", request.getIssuanceFED());
            postParam.put("issuanceFED", request.getIssuanceFED());
            postParam.put("inAccountLits", JSONUtil.getJSON(request.getInAccountLits()));
            logger.info("Request body of Euronet Card Issuance  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, euronetCardIssuanceUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, CardIssuanceResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Euronet Card Issuance Request processed in: " + difference + " millisecond");
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
