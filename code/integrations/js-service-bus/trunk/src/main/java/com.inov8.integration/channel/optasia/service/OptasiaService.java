package com.inov8.integration.channel.optasia.service;

import com.inov8.integration.channel.optasia.mock.OptasiaMock;
import com.inov8.integration.channel.optasia.request.*;
import com.inov8.integration.channel.optasia.response.*;
import com.inov8.integration.channel.sendPushNotification.mock.SendPushNotificationMock;
import com.inov8.integration.channel.sendPushNotification.response.SendPushNotificationResponse;
import com.inov8.integration.channel.sendPushNotification.service.SendPushNotificationService;
import com.inov8.integration.channel.tasdeeq.response.CustomAnalyticsResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.Base64;
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

@Service
public class OptasiaService {

    private static Logger logger = LoggerFactory.getLogger(OptasiaService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String optasiaOfferListForCommodity = PropertyReader.getProperty("optasia.offerListForCommodity");
    private String optasiaLoanOffer = PropertyReader.getProperty("optasia.loanOffer");
    private String optasiaCallback = PropertyReader.getProperty("optasia.callback");
    private String optasiaLoans = PropertyReader.getProperty("optasia.loans");
    private String optasiaInitiateLoan = PropertyReader.getProperty("optasia.projection");
    private String optasiaOutstanding = PropertyReader.getProperty("optasia.outstanding");
    private String optasiaTransactions = PropertyReader.getProperty("optasia.transactions");
    private String optasiaStatus = PropertyReader.getProperty("optasia.status");
    private String optasiaPayment = PropertyReader.getProperty("optasia.payment");

    public OfferListForCommodityResponse sendOfferListForCommodityResponse(OfferListForCommodityRequest offerListForCommodityRequest) {

        OfferListForCommodityResponse offerListForCommodityResponse = new OfferListForCommodityResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.offerListForCommodity();
            offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(response, OfferListForCommodityResponse.class);
            logger.info("Response of Offer List For Commodity Request : " + response);
            logger.info("Response Code for Offer List For Commodity Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }
        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaOfferListForCommodity)
                    .queryParam("identityType",offerListForCommodityRequest.getIdentityType())
                    .queryParam("identityValue",offerListForCommodityRequest.getIdentityValue())
                    .queryParam("origSource",offerListForCommodityRequest.getOrigSource())
                    .queryParam("commodityType",offerListForCommodityRequest.getCommodityType())
                    .queryParam("sourceRequestId",offerListForCommodityRequest.getSourceRequestId())
                    .queryParam("offerName",offerListForCommodityRequest.getOfferName())
                    .queryParam("amount",offerListForCommodityRequest.getAmount());

            String url = uri.toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(offerListForCommodityRequest);
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
                logger.info("Sending Offer List For Commodity Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    offerListForCommodityResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    offerListForCommodityResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        offerListForCommodityResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        offerListForCommodityResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        offerListForCommodityResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Offer List For Commodity Request processed in: " + difference + " millisecond");
        return offerListForCommodityResponse;
    }

    public LoanOfferResponse sendLoanOfferResponse(LoanOfferRequest loanOfferRequest) {
        LoanOfferResponse loanOfferResponse = new LoanOfferResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.loanOffer();
            loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(response, LoanOfferResponse.class);
            logger.info("Response of Loan Offer Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaLoanOffer);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(loanOfferRequest);
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
                logger.info("Sending Loan Offer Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    loanOfferResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    loanOfferResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        loanOfferResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        loanOfferResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        loanOfferResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Offer request processed in: " + difference + " millisecond");
        return loanOfferResponse;
    }

    public CallBackResponse sendCallBackResponse(CallBackRequest callBackRequest) {
        CallBackResponse callBackResponse = new CallBackResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.callBack();
            callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(response, CallBackResponse.class);
            logger.info("Response of Call Back Request : " + response);
            logger.info("Response Code for Call Back Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }
        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaCallback);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(callBackRequest);
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
                logger.info("Sending Call Back Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    callBackResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    callBackResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(result, CallBackResponse.class);
                        callBackResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(result, CallBackResponse.class);
                        callBackResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(result, CallBackResponse.class);
                        callBackResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Call Back Request processed in: " + difference + " millisecond");

        return callBackResponse;
    }

    public LoansResponse sendLoansResponse(LoansRequest loansRequest) {
        LoansResponse loansResponse = new LoansResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.loans();
            loansResponse = (LoansResponse) JSONUtil.jsonToObject(response, LoansResponse.class);
            logger.info("Response Code of Call Back Request : " + response);
            logger.info("Response Code for Call Back Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaLoans);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(loansRequest);
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
                logger.info("Sending Call Back Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    loansResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    loansResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        loansResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        loansResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        loansResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Call Back Request processed in: " + difference + " millisecond");
        return loansResponse;

    }

    public InitiateLoanResponse sendInitiateLoanResponse(InitiateLoanRequest initiateLoanRequest) {
        InitiateLoanResponse initiateLoanResponse = new InitiateLoanResponse();


        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.projection();
            initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(response, InitiateLoanResponse.class);
            logger.info("Response of Initiate Loan Request : " + response);
            logger.info("Response Code for Initiate Loan Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaInitiateLoan);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(initiateLoanRequest);
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
                logger.info("Sending Initiate Loan Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    initiateLoanResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    initiateLoanResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        initiateLoanResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        initiateLoanResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        initiateLoanResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Initiate Loan Request processed in: " + difference + " millisecond");

        return initiateLoanResponse;
    }

    public OutstandingResponse sendOutstandingResponse(OutstandingRequest outstandingRequest) {
        OutstandingResponse outstandingResponse = new OutstandingResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.outstanding();
            outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(response, OutstandingResponse.class);
            logger.info("Response Code of Outstanding Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaOutstanding);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(outstandingRequest);
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
                logger.info("Sending Outstanding Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    outstandingResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    outstandingResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        outstandingResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        outstandingResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        outstandingResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Outstanding request processed in: " + difference + " millisecond");


        return outstandingResponse;
    }

    public TransactionStatusResponse sendTransactionStatusResponse(TransactionStatusRequest transactionStatusRequest) {
        TransactionStatusResponse transactionStatusResponse = new TransactionStatusResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.outstanding();
            transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(response, TransactionStatusResponse.class);
            logger.info("Response Code of Transaction Status Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaTransactions);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(transactionStatusRequest);
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
                logger.info("Sending Transaction Status Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    transactionStatusResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    transactionStatusResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(result, TransactionStatusResponse.class);
                        transactionStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(result, TransactionStatusResponse.class);
                        transactionStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(result, TransactionStatusResponse.class);
                        transactionStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Transaction Status request processed in: " + difference + " millisecond");

        return transactionStatusResponse;
    }

    public LoanStatusResponse sendLoanStatusResponse(LoanStatusRequest loanStatusRequest) {
        LoanStatusResponse loanStatusResponse = new LoanStatusResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.status();
            loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(response, LoanStatusResponse.class);
            logger.info("Response Code of Loans Status Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaOutstanding);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(loanStatusRequest);
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
                logger.info("Sending Loan Status Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    loanStatusResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    loanStatusResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(result, LoanStatusResponse.class);
                        loanStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(result, LoanStatusResponse.class);
                        loanStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(result, LoanStatusResponse.class);
                        loanStatusResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Status request processed in: " + difference + " millisecond");

        return loanStatusResponse;
    }

    public LoanPaymentResponse sendLoanPaymentResponse(LoanPaymentRequest loanPaymentRequest) {
        LoanPaymentResponse loanPaymentResponse = new LoanPaymentResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.payment();
            loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(response, LoanPaymentResponse.class);
            logger.info("Response Code of Loan Payment Request : " + response);
            logger.info("Response Code for Loan Payment Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaPayment);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            String requestJSON = JSONUtil.getJSON(loanPaymentRequest);
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
                logger.info("Sending Loan Payment Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = (String) res1.getBody();
                    loanPaymentResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    loanPaymentResponse.setResponseDescription("Success");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        loanPaymentResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        loanPaymentResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        loanPaymentResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Payment request processed in: " + difference + " millisecond");
        return loanPaymentResponse;
    }

}
