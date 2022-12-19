package com.inov8.integration.channel.APIGEE.service;

import com.inov8.integration.channel.APIGEE.mock.MPIN;
import com.inov8.integration.channel.APIGEE.request.CardDetail.CardDetailRequest;
import com.inov8.integration.channel.APIGEE.request.HRA.PayMTCNRequest;
import com.inov8.integration.channel.APIGEE.request.HRA.PaymtcnAccessToken;
import com.inov8.integration.channel.APIGEE.request.MPIN.AtmPinGenerationRequest;
import com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut.*;
import com.inov8.integration.channel.APIGEE.response.CardDetail.CardDetailResponse;
import com.inov8.integration.channel.APIGEE.response.HRA.PaymtcnAccessTokenResponse;
import com.inov8.integration.channel.APIGEE.response.MPIN.AtmPinGenerationResponse;
import com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut.EobiAccessTokenResponse;
import com.inov8.integration.channel.APIGEE.response.HRA.PayMTCNResponse;
import com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut.*;
import com.inov8.integration.client.REST.RESTClient;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.middleware.prisim.PaymentDetailDTO;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by inov8 on 5/28/2018.
 */
@Service
public class
APIGEEService {

    private static Logger logger = LoggerFactory.getLogger(APIGEEService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    RESTClient restClient;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${apigee.hra.mtcn.api.url:#{null}}")
    private String mtcnServiceURL;
    @Value("${apigee.hra.mtcn.api.username}")
    private String mtcnUserName;
    @Value("${apigee.hra.mtcn.api.password}")
    private String mtcnPassword;
    @Value("${apigee.thirdpartycashout.balanceinquiry.api.url:#{null}}")
    private String balanceInquiryURL;
    @Value("${apigee.thirdpartycashout.api.username:#{null}}")
    private String thirdPartyCashOutUserName;
    @Value("${apigee.thirdpartycashout.api.password:#{null}}")
    private String thirdPartyCashOutPassword;
    @Value("${apigee.thridpartycashout.accesstoken.api.url:#{null}}")
    private String accessTokenUrl;
    @Value("${apigee.thirdpartycashout.cashwithdrawl.api.url:#{null}}")
    private String cashWithdrawlUrl;
    @Value("${apigee.thirdpartycashout.eobi.titlefetch.api.url:#{null}}")
    private String eobiTitleFetchUrl;
    @Value("${apigee.thirdpartycashout.cashwithdrawlreversal.api.url:#{null}}")
    private String cashWithdrawlReversalUrl;
    @Value("${apigee.thirdpartycashout.eobi.api.username}")
    private String eobiUserName;
    @Value("${apigee.thirdpartycashout.eobi.api.password}")
    private String eobiPassword;
    @Value("${apigee.thridpartycashout.eobi.accesstoken.api.url:#{null}}")
    private String eobiAccessTokenVerification;
    @Value("${apigee.thridpartycashout.eobi.moneytransfer.api.url:#{null}}")
    private String eobiMoneyTransferUrl;
    @Value("${apigee.hra.mtcn.accesstoken.api.url:#{null}}")
    private String payMtcnAccessTokenUrl;
    @Value("${apigee.thirdPartyCashOut.agentVerification.api.url:#{null}}")
    private String agentVerificationUrl;
    @Value("${apigee.mpin.atmPinGeneration.api.url}")
    private String atmPinGenerationUrl;
    @Value("${apigee.mpin.getCardDetails.api.url}")
    private String getCardDetailsUrl;
    @Value("${apigee.mpin.getCardDetails.accessToken}")
    private String accessToken;

    public PaymtcnAccessTokenResponse sendPayMtcnAccessTokenRequest(PaymtcnAccessToken accessTokenRequest) throws Exception {


        PaymtcnAccessTokenResponse paymtcnAccessTokenResponse = new PaymtcnAccessTokenResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(payMtcnAccessTokenUrl);
        String plainCreds = mtcnUserName + ":" + mtcnPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("grant_type", "client_credentials");
        String requestJson = JSONUtil.getJSON(accessTokenRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(bodyMap, headers);
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        paymtcnAccessTokenResponse = (PaymtcnAccessTokenResponse) JSONUtil.jsonToObject(res, PaymtcnAccessTokenResponse.class);
        paymtcnAccessTokenResponse.setStatus("approved");
        return paymtcnAccessTokenResponse;
    }

    public PayMTCNResponse sendPayMTCNRequest(PayMTCNRequest request) throws Exception {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to APIGEE server for RRN: " + rrn);
        new PayMTCNResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.mtcnServiceURL);
        String plainCreds = this.mtcnUserName + ":" + this.mtcnPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(request);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        HttpEntity httpEntity = new HttpEntity(requestJson, headers);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntity, String.class);
        PayMTCNResponse response = (PayMTCNResponse) JSONUtil.jsonToObject((String) res1.getBody(), PayMTCNResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return response;

    }

    public BalanceInquiryResponse balanceInquiryRequest(BalanceInquiryRequest balanceInquiryRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to APIGEE server for RRN: " + rrn);
//        String token = balanceInquiryRequest.getAccessToken();
        String token = i8SBSwitchControllerRequestVO.getAccessToken();
        balanceInquiryRequest.setAccessToken("");
        //balanceInquiryRequest.setReserved1(" ");
        BalanceInquiryResponse response = new BalanceInquiryResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(balanceInquiryURL);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(balanceInquiryRequest);
        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        System.out.print(headers.toString());
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        response = (BalanceInquiryResponse) JSONUtil.jsonToObject(res.getBody(), BalanceInquiryResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return response;
    }

    public CashWithdrawlResponse sendCashWithdrawlRequest(CashWithdrawlRequest cashWithdrawlRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to APIGEE server for RRN: " + rrn);
        String token = i8SBSwitchControllerRequestVO.getAccessToken();
        cashWithdrawlRequest.setAccessToken("");
        CashWithdrawlResponse cashWithdrawlResponse = new CashWithdrawlResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(cashWithdrawlUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(cashWithdrawlRequest);
        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }

        ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        cashWithdrawlResponse = (CashWithdrawlResponse) JSONUtil.jsonToObject(res.getBody(), CashWithdrawlResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("CashWithDrawl Request Processed In:" + difference + "millisecond");
        return cashWithdrawlResponse;
    }

    public CashWithdrawlReversalResponse SendCashWithdrawlReversalRequest(CashWithdrawlReversalRequest cashWithdrawlReversalRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to APIGEE server for RRN: " + rrn);
        String token = i8SBSwitchControllerRequestVO.getAccessToken();
//        cashWithdrawlReversalRequest.getBispWithdarawalReversal().setTransmissionDateTime("");
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(cashWithdrawlReversalUrl);
        CashWithdrawlReversalResponse cashWithdrawlReversalResponse = new CashWithdrawlReversalResponse();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(cashWithdrawlReversalRequest);
        List<Charset> acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }

         ResponseEntity<String>  res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        cashWithdrawlReversalResponse = (CashWithdrawlReversalResponse) JSONUtil.jsonToObject(res.getBody(), CashWithdrawlReversalResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("CashWithDrawlReversal Request Processed In:" + difference + "millisecond");
        return cashWithdrawlReversalResponse;
    }

    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AccesTokenResponse sendAccessTokenRequest(AccessTokenRequest accessTokenRequest) throws Exception {
        AccesTokenResponse accesTokenResponse = new AccesTokenResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(accessTokenUrl);
        String plainCreds = thirdPartyCashOutUserName + ":" + thirdPartyCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        logger.info("credential "+base64Creds);
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + base64Creds);
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("grant_type", "client_credentials");
        String requestJson = JSONUtil.getJSON(accessTokenRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(bodyMap, headers);
        logger.info("Access token Request Send to Apigee");
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        logger.info("Access Token response Recieve from Apigee");
        accesTokenResponse = (AccesTokenResponse) JSONUtil.jsonToObject(res, AccesTokenResponse.class);
        return accesTokenResponse;
    }

    public TitlefetchResponse sendTitleFetchRequest(TitleFetchRequest titleFetchRequest) throws Exception {
        TitlefetchResponse titlefetchResponse = new TitlefetchResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(eobiTitleFetchUrl);
        String token = titleFetchRequest.getAccessToken();
        titleFetchRequest.setAccessToken("");
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(titleFetchRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        titlefetchResponse = (TitlefetchResponse) JSONUtil.jsonToObject(res, TitlefetchResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("EobiTitleFetch Request Processed In:" + difference + "millisecond");
        return titlefetchResponse;
    }

    public EobiAccessTokenResponse sendEobiAccessTokenRequest(EobiAccessTokenRequest eobiAccessTokenRequest) throws Exception {
        EobiAccessTokenResponse eobiAccessTokenResponse = new EobiAccessTokenResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(eobiAccessTokenVerification);
        String plainCreds = eobiUserName + ":" + eobiPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(eobiAccessTokenRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        //restTemplate.pos
        eobiAccessTokenResponse = (EobiAccessTokenResponse) JSONUtil.jsonToObject(res, EobiAccessTokenResponse.class);
        eobiAccessTokenResponse.setStatus("Approved");
        return eobiAccessTokenResponse;
    }

    public MoneyTransferResponse sendMoneyTransferresponse(MoneyTransferRequest moneyTransferRequest) throws Exception {
        MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(eobiMoneyTransferUrl);
        String token = moneyTransferRequest.getAccessToken();
        moneyTransferRequest.setAccessToken("");
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(moneyTransferRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        moneyTransferResponse = (MoneyTransferResponse) JSONUtil.jsonToObject(res, MoneyTransferResponse.class);
        return moneyTransferResponse;
    }

    public AgentVerificaionResponse agentVerificaionResponse(AgentVerificationRequest agentVerificationRequest) throws Exception {
        AgentVerificaionResponse agentVerificaionResponse = new AgentVerificaionResponse();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(agentVerificationUrl);
        String token = agentVerificationRequest.getAccessToken();
        agentVerificationRequest.setAccessToken("");
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(agentVerificationRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestJson, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
        agentVerificaionResponse = (AgentVerificaionResponse) JSONUtil.jsonToObject(res, AgentVerificaionResponse.class);
        return agentVerificaionResponse;
    }

    public AtmPinGenerationResponse atmPinGenerationResponse (AtmPinGenerationRequest atmPinGenerationRequest) throws Exception{
        AtmPinGenerationResponse atmPinGenerationResponse = new AtmPinGenerationResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment !=null && this.i8sb_target_environment.equalsIgnoreCase("mok")){
            logger.info("Preparing request for Request Type : "+i8SBSwitchControllerRequestVO.getRequestType());
            MPIN mpin = new MPIN();
            String response = mpin.ATMPinGeneration();
            atmPinGenerationResponse = (AtmPinGenerationResponse) JSONUtil.jsonToObject(response, AtmPinGenerationResponse.class);
            logger.info("Status for ATM PIN Generation : "+atmPinGenerationResponse.getMessage());
        }
        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(atmPinGenerationUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(atmPinGenerationRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson,headers);
            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
            atmPinGenerationResponse = (AtmPinGenerationResponse) JSONUtil.jsonToObject(res, AtmPinGenerationResponse.class);
        }

        return atmPinGenerationResponse;
    }

    public CardDetailResponse getCardDetailResponse(CardDetailRequest cardDetailRequest) throws Exception{

        CardDetailResponse cardDetailResponse = new CardDetailResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment !=null && this.i8sb_target_environment.equalsIgnoreCase("mock")){
            logger.info("Preparing request for Request Type : "+i8SBSwitchControllerRequestVO.getRequestType());
            MPIN mpin = new MPIN();
            String response = mpin.CardDetails();
            cardDetailResponse = (CardDetailResponse) JSONUtil.jsonToObject(response, CardDetailResponse.class);
            logger.info("Response Code for Card Details : "+cardDetailResponse.getResponseCode());
        }

        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(getCardDetailsUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access-Token",accessToken);
            String requestJSON = JSONUtil.getJSON(cardDetailRequest);

            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON,headers);

            for (HttpMessageConverter converter: restTemplate.getMessageConverters()){
                if(converter instanceof StringHttpMessageConverter){
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            String res = restTemplate.postForObject(uri.build().toUri(),httpEntity,String.class);
            cardDetailResponse = (CardDetailResponse) JSONUtil.jsonToObject(res, CardDetailResponse.class);

        }
        return cardDetailResponse;
    }
}