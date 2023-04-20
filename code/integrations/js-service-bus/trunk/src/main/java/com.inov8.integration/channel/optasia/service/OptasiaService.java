package com.inov8.integration.channel.optasia.service;


import com.inov8.integration.channel.optasia.mock.OptasiaMock;
import com.inov8.integration.channel.optasia.request.*;
import com.inov8.integration.channel.optasia.response.*;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@Service
public class OptasiaService {

    private static Logger logger = LoggerFactory.getLogger(OptasiaService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String optasiaEcibData = PropertyReader.getProperty("optasia.ecibData");
    private String optasiaOfferListForCommodity = PropertyReader.getProperty("optasia.offerListForCommodity");
    private String optasiaLoanOffer = PropertyReader.getProperty("optasia.loanOffer");
    private String optasiaCallback = PropertyReader.getProperty("optasia.callback");
    private String optasiaLoans = PropertyReader.getProperty("optasia.loans");
    private String optasiaInitiateLoan = PropertyReader.getProperty("optasia.projection");
    private String optasiaOutstanding = PropertyReader.getProperty("optasia.outstanding");
    private String optasiaTransactions = PropertyReader.getProperty("optasia.transactions");
    private String optasiaLoanStatus = PropertyReader.getProperty("optasia.status");
    private String optasiaPayment = PropertyReader.getProperty("optasia.payment");
    private String username = PropertyReader.getProperty("optasia.username");
    private String password = PropertyReader.getProperty("optasia.password");
    private String optasiaAuthorization = PropertyReader.getProperty("optasia.authorization");

    public ECIBDataResponse sendEcibDataResponse(ECIBDataRequest ecibDataRequest) {

        ECIBDataResponse ecibDataResponse = new ECIBDataResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.ecibData();
            ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(response, ECIBDataResponse.class);
            Objects.requireNonNull(ecibDataResponse).setResponseCode("200");
            logger.info("Response of ECIB Data Request : " + response);
            logger.info("Response Code for ECIB Data Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            String response;
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Username", username);
                headers.add("Password", password);
                headers.add("Authorization", "Basic " + optasiaAuthorization);

                UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaEcibData);
                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                String requestJSON = JSONUtil.getJSON(ecibDataRequest);
                HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
                logger.info("Sending ECIB Data Request to Client " + httpEntity);
                ResponseEntity<String> res = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
//                ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                logger.info("Response Code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
//                    ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(res.getBody(), ECIBDataResponse.class);
                    Objects.requireNonNull(ecibDataResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(ecibDataResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(result, ECIBDataResponse.class);
                        Objects.requireNonNull(ecibDataResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(result, ECIBDataResponse.class);
                        Objects.requireNonNull(ecibDataResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(result, ECIBDataResponse.class);
                        Objects.requireNonNull(ecibDataResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        ecibDataResponse = (ECIBDataResponse) JSONUtil.jsonToObject(result, ECIBDataResponse.class);
                        Objects.requireNonNull(ecibDataResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("ECIB Data Request processed in: " + difference + " millisecond");
        return ecibDataResponse;
    }

    public OfferListForCommodityResponse sendOfferListForCommodityResponse(OfferListForCommodityRequest offerListForCommodityRequest) {

        OfferListForCommodityResponse offerListForCommodityResponse = new OfferListForCommodityResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.offerListForCommodity();
            offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(response, OfferListForCommodityResponse.class);
            Objects.requireNonNull(offerListForCommodityResponse).setResponseCode("200");
            logger.info("Response of Offer List For Commodity Request : " + response);
            logger.info("Response Code for Offer List For Commodity Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            String response;
            try {
                String requestJson = JSONUtil.getJSON(offerListForCommodityRequest);
                logger.info("Offer List for Commodity Request " + requestJson);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Username", username);
                headers.add("Password", password);
                headers.add("Authorization", "Basic " + optasiaAuthorization);


                UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(optasiaOfferListForCommodity)
                        .queryParam("identityType", offerListForCommodityRequest.getIdentityType())
                        .queryParam("identityValue", offerListForCommodityRequest.getIdentityValue())
                        .queryParam("origSource", offerListForCommodityRequest.getOrigSource())
                        .queryParam("commodityType", offerListForCommodityRequest.getCommodityType())
                        .queryParam("info[fed]", offerListForCommodityRequest.getFed());

                String temp = uri.toUriString();
                String regx = temp.replaceAll("%5B", "[");
                String url = regx.replaceAll("%5D", "]");
                logger.info("Requesting URL " + url);
                HttpEntity httpEntity = new HttpEntity(headers);
                logger.info("Sending Offer List For Commodity Request Sent to Client " + httpEntity);
                ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
//                String body = "{\"identityValue\":\"94b3a44d36dcd7b8af291e1af799ca70c5d1b5b5fb6460842bba8421817c9f53\",\"identityType\":\"customerIdentity\",\"origSource\":\"mobileApp\",\"receivedTimestamp\":\"2023-03-20T16:41:13.786+05:00\",\"eligibilityStatus\":{\"eligibilityStatus\":\"ELIGIBLE\",\"isEligible\":true},\"loanOffersByProductGroup\":[{\"loanProductGroup\":\"XTRACASH\",\"loanOffers\":[{\"offerClass\":\"RANGE\",\"offerName\":\"XTRACASHFED1\",\"commodityType\":\"CASH\",\"currencyCode\":\"PKR\",\"setupFees\":0.0,\"loanProductGroup\":\"XTRACASH\",\"loanPlanId\":1,\"loanPlanName\":\"XtraCash plan FED1\",\"maturityDetails\":{\"maturityDuration\":28,\"oneOffCharges\":[{\"chargeName\":\"FED1\",\"chargeType\":\"CUSTOM_CALCULATION\",\"chargeValue\":null,\"chargeVAT\":0.0,\"daysOffset\":0},{\"chargeName\":\"Set-up fee\",\"chargeType\":\"CUSTOM_CALCULATION\",\"chargeValue\":null,\"chargeVAT\":0.0,\"daysOffset\":0}],\"recurringCharges\":[{\"chargeName\":\"Weekly Fee\",\"chargeType\":\"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\"chargeValue\":0.045,\"chargeVAT\":0.0,\"daysOffset\":0,\"interval\":7}]},\"principalFrom\":98.0,\"principalTo\":98000.0}]}],\"outstandingStatus\":[{\"currencyCode\":\"PKR\",\"availableCreditLimit\":98000.0,\"dynamicCreditLimit\":98000.0,\"numOutstandingLoans\":0,\"totalGross\":0.0,\"totalPrincipal\":0.0,\"totalSetupFees\":0.0,\"totalInterest\":0.0,\"totalInterestVAT\":0.0,\"totalCharges\":0.0,\"totalChargesVAT\":0.0,\"totalPendingLoans\":0.0,\"totalPendingRecoveries\":0.0}]}";
//                ResponseEntity<String> res = new ResponseEntity<>(body, HttpStatus.OK);
                logger.info("Response code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
                    offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(res.getBody(), OfferListForCommodityResponse.class);
                    Objects.requireNonNull(offerListForCommodityResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        Objects.requireNonNull(offerListForCommodityResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        Objects.requireNonNull(offerListForCommodityResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        Objects.requireNonNull(offerListForCommodityResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        offerListForCommodityResponse = (OfferListForCommodityResponse) JSONUtil.jsonToObject(result, OfferListForCommodityResponse.class);
                        Objects.requireNonNull(offerListForCommodityResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
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
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.loanOffer();
            loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(response, LoanOfferResponse.class);
            Objects.requireNonNull(loanOfferResponse).setResponseCode("200");
            logger.info("Response of Loan Offer Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Username", username);
            headers.add("Password", password);
            headers.add("Authorization", "Basic " + optasiaAuthorization);

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaLoanOffer)
                    .queryParam("identityType", loanOfferRequest.getIdentityType())
                    .queryParam("identityValue", loanOfferRequest.getIdentityValue())
                    .queryParam("origSource", loanOfferRequest.getOrigSource());

            String requestJSON = JSONUtil.getJSON(loanOfferRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String response;
            try {
                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                logger.info("Sending Loan Offer Request to Client " + httpEntity);
                ResponseEntity<String> res = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
                    response = res.getBody();
                    loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(response, LoanOfferResponse.class);
                    Objects.requireNonNull(loanOfferResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        Objects.requireNonNull(loanOfferResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        Objects.requireNonNull(loanOfferResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        Objects.requireNonNull(loanOfferResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanOfferResponse = (LoanOfferResponse) JSONUtil.jsonToObject(result, LoanOfferResponse.class);
                        Objects.requireNonNull(loanOfferResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Offer request processed in: " + difference + " millisecond");
        return loanOfferResponse;
    }

    public InitiateLoanResponse sendInitiateLoanResponse(InitiateLoanRequest initiateLoanRequest) {
        InitiateLoanResponse initiateLoanResponse = new InitiateLoanResponse();


        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.projection();
            initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(response, InitiateLoanResponse.class);
            Objects.requireNonNull(initiateLoanResponse).setResponseCode("200");
//            logger.info("Response of Projection Request : " + response);
            logger.info("Response Code for Projection Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            String response;
            try {
                UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaInitiateLoan)
                        .queryParam("identityType", initiateLoanRequest.getIdentityType())
                        .queryParam("origSource", initiateLoanRequest.getOrigSource())
                        .queryParam("identityValue", initiateLoanRequest.getIdentityValue())
                        .queryParam("offerName", initiateLoanRequest.getOfferName())
                        .queryParam("loanAmount", initiateLoanRequest.getLoanAmount())
                        .queryParam("upToPeriod", initiateLoanRequest.getUpToPeriod());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Username", username);
                headers.add("Password", password);
                headers.add("Authorization", "Basic " + optasiaAuthorization);

                String requestJSON = JSONUtil.getJSON(initiateLoanRequest);
                HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);

                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                logger.info("Sending Projection Request to Client " + httpEntity);
                ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
                logger.info("Response code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                response = res.getBody();
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
                    initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(response, InitiateLoanResponse.class);
                    Objects.requireNonNull(initiateLoanResponse).setResponseCode(responseCode);
                }

            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        Objects.requireNonNull(initiateLoanResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        Objects.requireNonNull(initiateLoanResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        Objects.requireNonNull(initiateLoanResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        initiateLoanResponse = (InitiateLoanResponse) JSONUtil.jsonToObject(result, InitiateLoanResponse.class);
                        Objects.requireNonNull(initiateLoanResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Initiate Loan Request processed in: " + difference + " millisecond");

        return initiateLoanResponse;
    }

    public LoansResponse sendLoansResponse(LoansRequest loansRequest) {
        LoansResponse loansResponse = new LoansResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.loans();
            loansResponse = (LoansResponse) JSONUtil.jsonToObject(response, LoansResponse.class);
            Objects.requireNonNull(loansResponse).setResponseCode("200");
//            logger.info("Response Code of Loans Request : " + response);
            logger.info("Response Code for Loans Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            String response;
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Username", username);
                headers.add("Password", password);
                headers.add("Authorization", "Basic " + optasiaAuthorization);


                UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(optasiaLoans)
                        .queryParam("identityType", loansRequest.getIdentityType())
                        .queryParam("origSource", loansRequest.getOrigSource())
                        .queryParam("identityValue", loansRequest.getIdentityValue())
                        .queryParam("filterLoanState", loansRequest.getFilterLoanState())
                        .queryParam("filterCommodityType", loansRequest.getFilterCommodityType());

                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                HttpEntity httpEntity = new HttpEntity(headers);
                logger.info("Sending Customer Loans Request to Client " + httpEntity);
                ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
                logger.info("Response code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
                    loansResponse = (LoansResponse) JSONUtil.jsonToObject(res.getBody(), LoansResponse.class);
                    Objects.requireNonNull(loansResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        Objects.requireNonNull(loansResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        Objects.requireNonNull(loansResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        Objects.requireNonNull(loansResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loansResponse = (LoansResponse) JSONUtil.jsonToObject(result, LoansResponse.class);
                        Objects.requireNonNull(loansResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }

        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Customer Loans Request processed in: " + difference + " millisecond");
        return loansResponse;

    }

    public OutstandingResponse sendOutstandingResponse(OutstandingRequest outstandingRequest) {
        OutstandingResponse outstandingResponse = new OutstandingResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.outstanding();
            outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(response, OutstandingResponse.class);
            Objects.requireNonNull(outstandingResponse).setResponseCode("200");
//            logger.info("Response Code of Outstanding Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            String response;
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Username", username);
                headers.add("Password", password);
                headers.add("Authorization", "Basic " + optasiaAuthorization);

                UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaOutstanding)
                        .queryParam("identityType", outstandingRequest.getIdentityType())
                        .queryParam("origSource", outstandingRequest.getOrigSource())
                        .queryParam("identityValue", outstandingRequest.getIdentityValue());


                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                HttpEntity httpEntity = new HttpEntity(headers);
                logger.info("Sending Outstanding Request to Client " + httpEntity);
                ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
                logger.info("Response code received from client " + res.getStatusCode().value());
                logger.info("Response received from client " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
                    outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(res.getBody(), OutstandingResponse.class);
                    Objects.requireNonNull(outstandingResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, OutstandingResponse.class);
                        Objects.requireNonNull(outstandingResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, OutstandingResponse.class);
                        Objects.requireNonNull(outstandingResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, OutstandingResponse.class);
                        Objects.requireNonNull(outstandingResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        outstandingResponse = (OutstandingResponse) JSONUtil.jsonToObject(result, OutstandingResponse.class);
                        Objects.requireNonNull(outstandingResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }


        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Outstanding request processed in: " + difference + " millisecond");


        return outstandingResponse;
    }

    public LoanPaymentResponse sendLoanPaymentResponse(LoanPaymentRequest loanPaymentRequest) {
        LoanPaymentResponse loanPaymentResponse = new LoanPaymentResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.payment();
            loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(response, LoanPaymentResponse.class);
            Objects.requireNonNull(loanPaymentResponse).setResponseCode("200");
//            logger.info("Response Code of Loan Payment Request : " + response);
            logger.info("Response Code for Loan Payment Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Username", username);
            headers.add("Password", password);
            headers.add("Authorization", "Basic " + optasiaAuthorization);

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaPayment)
                    .queryParam("identityType", loanPaymentRequest.getIdentityType())
                    .queryParam("origSource", loanPaymentRequest.getOrigSource())
                    .queryParam("identityValue", loanPaymentRequest.getIdentityValue());

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
                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                logger.info("Sending Loan Payment Request to Client " + httpEntity);
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().value());
                logger.info("Response received from client " + res1.getBody());
                String responseCode = String.valueOf(res1.getStatusCode().value());
                if (responseCode.equals("200")) {
                    response = res1.getBody();
                    loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(response, LoanPaymentResponse.class);
                    Objects.requireNonNull(loanPaymentResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        Objects.requireNonNull(loanPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        Objects.requireNonNull(loanPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        Objects.requireNonNull(loanPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        loanPaymentResponse = (LoanPaymentResponse) JSONUtil.jsonToObject(result, LoanPaymentResponse.class);
                        Objects.requireNonNull(loanPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Payment request processed in: " + difference + " millisecond");
        return loanPaymentResponse;
    }

    public TransactionStatusResponse sendTransactionStatusResponse(TransactionStatusRequest
                                                                           transactionStatusRequest) {
        TransactionStatusResponse transactionStatusResponse = new TransactionStatusResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.outstanding();
            transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(response, TransactionStatusResponse.class);
            logger.info("Response Code of Transaction Status Request : " + response);
            logger.info("Response Code for Loan Offer Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Username", username);
            headers.add("Password", password);
            headers.add("Authorization", "Basic " + optasiaAuthorization);

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaTransactions)
                    .queryParam("identityType", transactionStatusRequest.getIdentityType())
                    .queryParam("origSource", transactionStatusRequest.getOrigSource())
                    .queryParam("identityValue", transactionStatusRequest.getIdentityValue())
                    .queryParam("filterCommodityType", transactionStatusRequest.getFilterCommodityType());


            String url = uri.toUriString();
            logger.info("Requesting URL " + url);
            HttpEntity httpEntity = new HttpEntity(headers);
            logger.info("Sending Transaction Status Request Sent to Client " + httpEntity);
            ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
            logger.info("Response code received from client " + res.getStatusCode());
            logger.info("Response received from client " + res.getBody());
            transactionStatusResponse = (TransactionStatusResponse) JSONUtil.jsonToObject(res.getBody(), TransactionStatusResponse.class);
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Transaction Status request processed in: " + difference + " millisecond");

        return transactionStatusResponse;
    }

    public CallBackResponse sendCallBackResponse(CallBackRequest callBackRequest) {
        CallBackResponse callBackResponse = new CallBackResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.callBack();
            callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(response, CallBackResponse.class);
            logger.info("Response of Call Back Request : " + response);
            logger.info("Response Code for Call Back Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Username", username);
            headers.add("Password", password);
            headers.add("Authorization", "Basic " + optasiaAuthorization);

//            headers.add("auth_token", i8SBSwitchControllerResponseVO.getAuthToken());
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaCallback)
                    .queryParam("loanEvent", callBackRequest.getLoanEvent())
                    .queryParam("loanEventStatus", callBackRequest.getLoanEventStatus())
                    .queryParam("origSource", callBackRequest.getOrigSource())
                    .queryParam("sourceRequestId", callBackRequest.getSourceRequestId());


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
                String url = uri.toUriString();
                logger.info("Requesting URL " + url);
                logger.info("Sending Call Back Request Sent to Client " + httpEntity);
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code received from client " + res1.getStatusCode().toString());
                logger.info("Response received from client " + res1.getBody());
                String responseCode = String.valueOf(res1.getStatusCode().value());
                if (res1.getStatusCode().toString().equals("200")) {
                    response = res1.getBody();
                    callBackResponse.setResponseCode("200");
                    callBackResponse = (CallBackResponse) JSONUtil.jsonToObject(response, CallBackResponse.class);
                    callBackResponse.setResponseCode(responseCode);
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
                    } else {
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

    public LoanStatusResponse sendLoanStatusResponse(LoanStatusRequest loanStatusRequest) {
        LoanStatusResponse loanStatusResponse = new LoanStatusResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            OptasiaMock optasiaMock = new OptasiaMock();
            String response = optasiaMock.status();
            loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(response, LoanStatusResponse.class);
            logger.info("Response Code of Loans Status Request : " + response);
            logger.info("Response Code for Loan Status Request : " + i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Username", username);
            headers.add("Password", password);
            headers.add("Authorization", "Basic " + optasiaAuthorization);

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.optasiaLoanStatus)
                    .queryParam("identityType", loanStatusRequest.getIdentityType())
                    .queryParam("origSource", loanStatusRequest.getOrigSource())
                    .queryParam("identityValue", loanStatusRequest.getIdentityValue());


            String url = uri.toUriString();
            logger.info("Requesting URL " + url);
            HttpEntity httpEntity = new HttpEntity(headers);
            logger.info("Sending Loan Status Request Sent to Client " + httpEntity);
            ResponseEntity<String> res = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
            logger.info("Response code received from client " + res.getStatusCode().value());
            logger.info("Response received from client " + res.getBody());
            loanStatusResponse = (LoanStatusResponse) JSONUtil.jsonToObject(res.getBody(), LoanStatusResponse.class);
        }

        long endTime = (new Date()).getTime();
        long difference = endTime - start;
        logger.debug("Loan Status request processed in: " + difference + " millisecond");

        return loanStatusResponse;
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                return true;
            }

        };

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

}