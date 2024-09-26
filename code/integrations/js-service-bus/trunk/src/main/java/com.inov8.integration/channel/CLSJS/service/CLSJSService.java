package com.inov8.integration.channel.CLSJS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.CLSJS.request.ImportScreeningRequest;
import com.inov8.integration.channel.CLSJS.response.ScreeningResponse;
import com.inov8.integration.channel.CLSJS.client.ImportScreeningResponse;
import com.inov8.integration.channel.lending.response.SalaryDisburseResponse;
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

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class CLSJSService {

//    @Autowired(required = false)
//    @Qualifier("CLSJSWebService")
//    ScreenMSPortType screenMSPortType;

    //    @Value("${i8sb.target.environment:#{null}}")
    private static Logger logger = LoggerFactory.getLogger(CLSJSService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String clsScreeningUrl = PropertyReader.getProperty("clsjs.endpoint.url");
    private String bearerToken = PropertyReader.getProperty("clsjs.bearer.token");

    //    private CLSJSMock clsjsMock = new CLSJSMock();
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

    public ScreeningResponse importScreeningResponse(ImportScreeningRequest request) throws SOAPException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        ObjectMapper objectMapper = new ObjectMapper();
        ImportScreeningResponse response = new ImportScreeningResponse();
        ScreeningResponse importScreeningResponse = new ScreeningResponse();
        com.inov8.integration.channel.CLSJS.client.ImportScreening importScreeningRequest = new com.inov8.integration.channel.CLSJS.client.ImportScreening();
        long start = System.currentTimeMillis();

//        logger.info("Service" + CLSJSService.class.getSimpleName() + "of CLS ");
//
//        importScreeningRequest.setRequestID(request.getRequestID());
//        importScreeningRequest.setCNIC(request.getCnic());
//        importScreeningRequest.setCustomerName(request.getCustomerName());
//        importScreeningRequest.setFatherName(request.getFatherName());
//        importScreeningRequest.setDateOfBirth(request.getDateOfBirth());
//        importScreeningRequest.setNationality(request.getNationality());
//        importScreeningRequest.setCity(request.getCity());
//        importScreeningRequest.setCustomerNumber(request.getCustomerNumber());
//        importScreeningRequest.setUserId(request.getUserId());

        if (i8sb_target_environment.equals("mock89")) {
//            importScreeningResponse = clsjsMock.importScreeningResponse(importScreeningRequest);
            logger.info("Preparing Mock request for Request Type : CLSJSImportScreening");
            String json = "{\n    \"message\": \"Success\",\n    \"code\": \"00\",\n    \"reqId\": \"000010\",\n    \"data\": {\n        \"caseId\": \"6272308\",\n        \"caseStatus\": \"GWL-Open|PEP/EDD-Open|Private-Open\",\n        \"totalGWL\": \"6\",\n        \"totalPEPEDD\": \"9\",\n        \"totalPrivate\": \"21\",\n        \"importStatus\": \"NoChange\"\n    }\n}";
            importScreeningResponse = (ScreeningResponse) JSONUtil.jsonToObject(json, ScreeningResponse.class);
            Objects.requireNonNull(importScreeningResponse).setCode("00");
            logger.info("Mock Response Code for CLS Service: " + importScreeningResponse.getCode());
        } else {
//            HTTPConduit httpConduit=(HTTPConduit) ClientProxy.getClient(SAMPLE_PORT).getConduit();
//            TLSClientParameters tlsCP = new TLSClientParameters();
//            tlsCP.setDisableCNCheck(true);
//            httpConduit.setTlsClientParameters(tlsCP);

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("accept", "application/json");
            headers.put("x-req-id", "000010");
            headers.put("x-channel-id", "BLB");
            headers.put("x-sub-channel-id", "BLB");
            headers.put("x-country-code", "PK");
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            postParam.put("cnic", request.getCnic());
            postParam.put("name", request.getName());
            postParam.put("fatherName", request.getFatherName());
            postParam.put("dateOfBirth", request.getDateOfBirth());
            postParam.put("nationality", request.getNationality());
            postParam.put("city", request.getCity());
            postParam.put("customerId", request.getCustomerId());
            postParam.put("userId", request.getUserId());
            postParam.put("isCustomerIndividual", request.getIsCustomerIndividual());
            logger.info("Request body of CLS Screening Service  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, clsScreeningUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    importScreeningResponse = objectMapper.readValue(responseBody, ScreeningResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                logger.info("[ Exception ] importScreeningResponse() " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("CLS Screening Service Request processed in: " + difference + " millisecond");

//            response = screenMSPortType.screenOperation(importScreeningRequest);
//            logger.info("Response received from client");
//            if (response != null) {
//                importScreeningResponse.setRequestID(response.getRequestID());
//                importScreeningResponse.setCaseId(response.getCaseId());
//                importScreeningResponse.setCaseStatus(response.getCaseStatus());
//                importScreeningResponse.setImportStatus(response.getImportStatus());
//                importScreeningResponse.setIsHit(response.getIsHit());
//                importScreeningResponse.setScreeningStatus(response.getScreeningStatus());
//                importScreeningResponse.setTotalCWL(response.getTotalCWL());
//                importScreeningResponse.setTotalGWL(response.getTotalGWL());
//                importScreeningResponse.setTotalPEPEDD(response.getTotalPEPEDD());
//                importScreeningResponse.setTotalPrivate(response.getTotalPrivate());
//
//            } else {
//                throw new I8SBValidationException("Response Not Received from Client");
//            }
        }
        return importScreeningResponse;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
