package com.inov8.integration.channel.crp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.crp.request.ScoreAndRatingRequest;
import com.inov8.integration.channel.crp.response.ScoreAnRatingResponse;
import com.inov8.integration.channel.sbp.request.ReturnPaymentRequest;
import com.inov8.integration.channel.sbp.response.ReturnPaymentResponse;
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
public class CrpService {

    private static Logger logger = LoggerFactory.getLogger(CrpService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String crpScoreAndRating = PropertyReader.getProperty("crp.score.and.rating");
    private String bearerToken = PropertyReader.getProperty("crp.bearer.token");
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

    public ScoreAnRatingResponse scoreAnRatingResponse(ScoreAndRatingRequest request) {

        ScoreAnRatingResponse response = new ScoreAnRatingResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock89")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "    \"responseCode\": \"170000\",\n" +
                    "    \"message\": \"Success\",\n" +
                    "    \"channel\": null,\n" +
                    "    \"terminal\": null,\n" +
                    "    \"transactionDate\": null,\n" +
                    "    \"reterivalReferenceNumber\": null,\n" +
                    "    \"payLoad\": {\n" +
                    "        \"score\": 0.16,\n" +
                    "        \"rating\": \"H\",\n" +
                    "        \"crpDate\": \"2024-08-05T06:37:13.098+00:00\",\n" +
                    "        \"nextCrpDate\": \"2029-08-05T06:37:13.098+00:00\",\n" +
                    "        \"lastKycDate\": \"2024-08-02T08:16:17.000+00:00\"\n" +
                    "    },\n" +
                    "    \"errors\": null,\n" +
                    "    \"checkSum\": null\n" +
                    "}";
            response = (ScoreAnRatingResponse) JSONUtil.jsonToObject(json, ScoreAnRatingResponse.class);
            logger.info("Mock Response Code for CRP Get Score And Rating Response: " + response.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            postParam.put("data", request.getData());
            logger.info("Request body of CRP Get Score And Rating  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, crpScoreAndRating);
                if (responseBody != null && responseBody.length() > 0) {
                    response = objectMapper.readValue(responseBody, ScoreAnRatingResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("CRP Get Score And Rating Request processed in: " + difference + " millisecond");
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
