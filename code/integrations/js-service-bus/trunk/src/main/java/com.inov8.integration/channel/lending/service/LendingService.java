package com.inov8.integration.channel.lending.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.lending.request.GetActiveLoanRequest;
import com.inov8.integration.channel.lending.request.GetLoanOutstandingRequest;
import com.inov8.integration.channel.lending.request.LoanRepaymentRequest;
import com.inov8.integration.channel.lending.request.SalaryDisburseRequest;
import com.inov8.integration.channel.lending.response.GetActiveLoanResponse;
import com.inov8.integration.channel.lending.response.GetLoanOutstandingResponse;
import com.inov8.integration.channel.lending.response.LoanRepaymentResponse;
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

import java.io.IOException;

import java.util.*;

@Service
public class LendingService {

    private static Logger logger = LoggerFactory.getLogger(LendingService.class.getSimpleName());
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String loanRepayment = PropertyReader.getProperty("lending.loanrepayment");
    private String getOutstandingLoan = PropertyReader.getProperty("lending.getloanoutstanding");
    private String getActiveLoan = PropertyReader.getProperty("lending.getActiveLoan");
    private String salaryDisburse = PropertyReader.getProperty("lending.salaryDisburse");
    private String bearerToken = PropertyReader.getProperty("lending.bearer.token");
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

    public LoanRepaymentResponse loanRepaymentResponse(LoanRepaymentRequest request) {

        LoanRepaymentResponse loanRepaymentResponse = new LoanRepaymentResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "    \"responseCode\": \"190101\",\n" +
                    "    \"message\": \"SUCCESS\",\n" +
                    "    \"channel\": null,\n" +
                    "    \"terminal\": null,\n" +
                    "    \"transactionDate\": null,\n" +
                    "    \"reterivalReferenceNumber\": null,\n" +
                    "    \"payLoad\": {\n" +
                    "        \"transactionId\": \"1\",\n" +
                    "        \"dateTime\": null,\n" +
                    "        \"loanAmount\": null,\n" +
                    "        \"fee\": null\n" +
                    "    },\n" +
                    "    \"errors\": null,\n" +
                    "    \"checkSum\": null\n" +
                    "}";
            loanRepaymentResponse = (LoanRepaymentResponse) JSONUtil.jsonToObject(json, LoanRepaymentResponse.class);
            Objects.requireNonNull(loanRepaymentResponse).setResponseCode("200");
            logger.info("Mock Response Code for Loan Repayment Response: " + loanRepaymentResponse.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            postParam.put("data", request.getData());
            logger.info("Request body of Loan Payment  " + JSONUtil.getJSON(request.getData()));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, loanRepayment);
                if (responseBody != null && responseBody.length() > 0) {
                    loanRepaymentResponse = objectMapper.readValue(responseBody, LoanRepaymentResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Loan Repayment Request processed in: " + difference + " millisecond");
        }
        return loanRepaymentResponse;
    }

    public GetLoanOutstandingResponse getLoanOutstandingResponse(GetLoanOutstandingRequest request) {

        GetLoanOutstandingResponse getLoanOutstandingResponse = new GetLoanOutstandingResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "    \"responseCode\": \"190101\",\n" +
                    "    \"message\": \"SUCCESS\",\n" +
                    "    \"channel\": null,\n" +
                    "    \"terminal\": null,\n" +
                    "    \"transactionDate\": null,\n" +
                    "    \"reterivalReferenceNumber\": null,\n" +
                    "    \"payLoad\": {\n" +
                    "        \"totalOutstanding\": 46760.04,\n" +
                    "        \"principalOutstanding\": 40000.02,\n" +
                    "        \"markupOutstanding\": 5760,\n" +
                    "        \"lpChargesOutstanding\": 1000.02,\n" +
                    "        \"epChargesOutstanding\": 0\n" +
                    "    },\n" +
                    "    \"errors\": null,\n" +
                    "    \"checkSum\": null\n" +
                    "}";
            getLoanOutstandingResponse = (GetLoanOutstandingResponse) JSONUtil.jsonToObject(json, GetLoanOutstandingResponse.class);
            Objects.requireNonNull(getLoanOutstandingResponse).setResponseCode("200");
            logger.info("Mock Response Code for get Loan Outstanding Response: " + getLoanOutstandingResponse.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            postParam.put("data", request.getData());
            logger.info("Request body of Get Outstanding Loan  " + JSONUtil.getJSON(request.getData()));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, getOutstandingLoan);
                if (responseBody != null && responseBody.length() > 0) {
                    getLoanOutstandingResponse = objectMapper.readValue(responseBody, GetLoanOutstandingResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Outstanding Loan Request processed in: " + difference + " millisecond");
        }
        return getLoanOutstandingResponse;
    }

    public GetActiveLoanResponse getActiveLoanResponse(GetActiveLoanRequest request) {

        GetActiveLoanResponse getActiveLoanResponse = new GetActiveLoanResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "    \"responseCode\": \"190101\",\n" +
                    "    \"message\": \"SUCCESS\",\n" +
                    "    \"channel\": null,\n" +
                    "    \"terminal\": null,\n" +
                    "    \"transactionDate\": null,\n" +
                    "    \"reterivalReferenceNumber\": null,\n" +
                    "    \"payLoad\": {\n" +
                    "        \"statusCode\": \"2\",\n" +
                    "        \"statusDescription\": \"No Loan available against this customer.\"\n" +
                    "    },\n" +
                    "    \"errors\": null,\n" +
                    "    \"checkSum\": null\n" +
                    "}";
            getActiveLoanResponse = (GetActiveLoanResponse) JSONUtil.jsonToObject(json, GetActiveLoanResponse.class);
            Objects.requireNonNull(getActiveLoanResponse).setResponseCode("200");
            logger.info("Mock Response Code for get Active Loan Response: " + getActiveLoanResponse.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            postParam.put("data", request.getData());
            logger.info("Request body of Get Active Loan  " + JSONUtil.getJSON(request.getData()));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, getActiveLoan);
                if (responseBody != null && responseBody.length() > 0) {
                    getActiveLoanResponse = objectMapper.readValue(responseBody, GetActiveLoanResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Active Loan Request processed in: " + difference + " millisecond");
        }
        return getActiveLoanResponse;
    }

    public SalaryDisburseResponse salaryDisburseResponse(SalaryDisburseRequest request) {

        SalaryDisburseResponse salaryDisburseResponse = new SalaryDisburseResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String json = "{\n" +
                    "\n" +
                    "    \"status\": \"200\",\n" +
                    "\n" +
                    "    \"response\": null,\n" +
                    "\n" +
                    "    \"message\": \"Salary Paylaod-Message has sent successfully to consumer\"\n" +
                    "\n" +
                    "}";
            salaryDisburseResponse = (SalaryDisburseResponse) JSONUtil.jsonToObject(json, SalaryDisburseResponse.class);
            Objects.requireNonNull(salaryDisburseResponse).setStatus("200");
            logger.info("Mock Response Code for Salary Disburse Response: " + salaryDisburseResponse.getStatus());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            postParam.put("mobileNumber", request.getMobileNumber());
            postParam.put("cnic", request.getCnic());
            postParam.put("salary", request.getSalary());
            logger.info("Request body of Salary Disburse  " + JSONUtil.getJSON(request));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, salaryDisburse);
                if (responseBody != null && responseBody.length() > 0) {
                    salaryDisburseResponse = objectMapper.readValue(responseBody, SalaryDisburseResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Salary Disburse Request processed in: " + difference + " millisecond");
        }
        return salaryDisburseResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
