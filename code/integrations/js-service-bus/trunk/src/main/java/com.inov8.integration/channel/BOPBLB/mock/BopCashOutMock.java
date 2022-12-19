package com.inov8.integration.channel.BOPBLB.mock;


import com.inov8.integration.channel.BOPBLB.request.*;
import com.inov8.integration.channel.BOPBLB.response.*;
import com.inov8.integration.client.REST.RESTClient;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class BopCashOutMock {
    RESTClient restClient;
    private static Logger logger = LoggerFactory.getLogger(BopCashOutMock.class.getSimpleName());

    private RestTemplate restTemplate = new RestTemplate();
    String cashOutInquiryURL = PropertyReader.getProperty("i8sb.url.cashOutInquiry");
    String cashOutURL = PropertyReader.getProperty("i8sb.url.cashOut");
    String cashOutAccessTokenURL = PropertyReader.getProperty("i8sb.url.accessToken");
    String cashOutAccountRegistrationInquiryURL = PropertyReader.getProperty("i8sb.url.accountRegistrationInquiry");
    String cashOutAccountRegistrationURL = PropertyReader.getProperty("i8sb.url.cashOutAccountRegistration");
    String cashOutCashWithdrawalReversalURL = PropertyReader.getProperty("i8sb.url.CashWithdrawalReversal");
    String cashOutProofOfLifeVerificationInquiryURL = PropertyReader.getProperty("i8sb.url.proofOfLifeVerificationInquiry");
    String cashOutProofOfLifeVerificationURL = PropertyReader.getProperty("i8sb.url.proofOfLifeVerification");
    String bopCashOutUsername = PropertyReader.getProperty("i8sb.cred.cashOutUser");
    String bopCashOutPassword = PropertyReader.getProperty("i8sb.cred.cashOutPass");

    public CashOutInquiryResonse cashOutInquiry(CashOutInquiryRequest cashOutInquiryRequest) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutInquiryURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
//        com.inov8.integration.gateway.Rest.BOPBLB.request.CashOutInquiryRequest request = new com.inov8.integration.gateway.Rest.BOPBLB.request.CashOutInquiryRequest();
//        request.setDebitCardNumber(cashOutInquiryRequest.getCnic());
//        request.setMobileNo(cashOutInquiryRequest.getMobileNumber());
//        request.setSegmentId(cashOutInquiryRequest.getSegmentId());
//        request.setCnic(cashOutInquiryRequest.getCnic());
//        request.setAmount(cashOutInquiryRequest.getAmount());
//        request.setDateTime(cashOutInquiryRequest.getDateTime());
//        request.setAgentLocation(cashOutInquiryRequest.getAgentLocation());
//        request.setAgentCity(cashOutInquiryRequest.getAgentCity());
//        request.setTerminalId(cashOutInquiryRequest.getTerminalId());
        String req = JSONUtil.getJSON("");
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        CashOutInquiryResonse response = (CashOutInquiryResonse) JSONUtil.jsonToObject((String) res1.getBody(), CashOutInquiryResonse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//        CashOutInquiryResonse Response=new CashOutInquiryResonse();
        return response;
    }

    public CashOutResponse cashOut(CashOutRequest cashOutRequest) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.CashOutRequest request = new com.inov8.integration.channel.BOPBLB.request.CashOutRequest() ;
        request.setDebitCardNumber(cashOutRequest.getDebitCardNumber());
        request.setMobileNo(cashOutRequest.getMobileNo());
        request.setCnic(cashOutRequest.getCnic());
        request.setSegmentID(cashOutRequest.getSegmentID());
        request.setAmount(cashOutRequest.getAmount());
        request.setRrn(cashOutRequest.getRrn());
        request.setOtp(cashOutRequest.getOtp());
        request.setFingerIndex(cashOutRequest.getFingerIndex());
        request.setFingerTemplate(cashOutRequest.getFingerTemplate());
        request.setTemplateType(cashOutRequest.getTemplateType());
        request.setAgentLocation(cashOutRequest.getAgentLocation());
        request.setAgentCity(cashOutRequest.getAgentCity());
        request.setTerminalId(cashOutRequest.getTerminalId());
        request.setTrnsactionID(cashOutRequest.getTrnsactionID());
        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        CashOutResponse response = (CashOutResponse) JSONUtil.jsonToObject((String) res1.getBody(), CashOutResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//        Response.setResponseCode("I8SB-500");
//        CashOutResponse Response=new CashOutResponse();
        return response;
    }

    public AccesTokenResponse accessToken(com.inov8.integration.channel.BOPBLB.request.AccessTokenRequest accessTokenRequest) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String requestJson = JSONUtil.getJSON(accessTokenRequest);
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
        AccesTokenResponse response = (AccesTokenResponse) JSONUtil.jsonToObject((String) res1.getBody(), AccesTokenResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public AccountRegistrationInquiryResponse accountRegistrationInquiry(AccountRegistrationInquiryRequest accountRegistrationInquiryRequest) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);

//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);

//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutAccountRegistrationInquiryURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.AccountRegistrationInquiryRequest request = new      com.inov8.integration.channel.BOPBLB.request.AccountRegistrationInquiryRequest();
        request.setMobileNo(accountRegistrationInquiryRequest.getMobileNo());
        request.setDebitCardNumber(accountRegistrationInquiryRequest.getDebitCardNumber());
        request.setSegmentID(accountRegistrationInquiryRequest.getSegmentID());
        request.setCnic(accountRegistrationInquiryRequest.getAgentCity());
        request.setAgentLocation(accountRegistrationInquiryRequest.getAgentLocation());
        request.setTerminalId(accountRegistrationInquiryRequest.getTerminalId());
        request.setRrn(accountRegistrationInquiryRequest.getRrn());
        request.setTransactionType(accountRegistrationInquiryRequest.getTransactionType());
        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        AccountRegistrationInquiryResponse response = (AccountRegistrationInquiryResponse) JSONUtil.jsonToObject((String) res1.getBody(), AccountRegistrationInquiryResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public AccountRegistrationInquiryResponse cardIssuanceInquiry(CardIssuanceReissuanceInquiry cardIssuanceReissuanceInquiry) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

//        HttpEntity httpEntity = new HttpEntity(requestJson,header);

//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);

//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutAccountRegistrationInquiryURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.AccountRegistrationInquiryRequest request = new  com.inov8.integration.channel.BOPBLB.request.AccountRegistrationInquiryRequest();
        request.setCnic(cardIssuanceReissuanceInquiry.getCnic());
        request.setMobileNo(cardIssuanceReissuanceInquiry.getMobileNo());
        request.setDebitCardNumber(cardIssuanceReissuanceInquiry.getDebitCardNumber());
        request.setSegmentID(cardIssuanceReissuanceInquiry.getSegmentID());
        request.setCnic(cardIssuanceReissuanceInquiry.getAgentCity());
        request.setAgentLocation(cardIssuanceReissuanceInquiry.getAgentLocation());
        request.setTerminalId(cardIssuanceReissuanceInquiry.getTerminalId());
        request.setRrn(cardIssuanceReissuanceInquiry.getRrn());
        request.setTransactionType(cardIssuanceReissuanceInquiry.getTransactionType());
        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        AccountRegistrationInquiryResponse response = (AccountRegistrationInquiryResponse) JSONUtil.jsonToObject((String) res1.getBody(), AccountRegistrationInquiryResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public AccountRegistrationResponse accountRegistration(AccountRegistrationRequest accountRegistrationRequest) {
//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutAccountRegistrationURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.AccountRegistrationRequest accountRegistrationRequest1 = new com.inov8.integration.channel.BOPBLB.request.AccountRegistrationRequest();
        accountRegistrationRequest1.setAgentCity(accountRegistrationRequest.getAgentCity());
        accountRegistrationRequest1.setAgentLocation(accountRegistrationRequest.getAgentLocation());
        accountRegistrationRequest1.setCnic(accountRegistrationRequest.getCnic());
        accountRegistrationRequest1.setFingerIndex(accountRegistrationRequest.getFingerIndex());
        accountRegistrationRequest1.setFingerTemplate(accountRegistrationRequest.getFingerTemplate());
        accountRegistrationRequest1.setMobileNo(accountRegistrationRequest.getMobileNo());
        accountRegistrationRequest1.setRrn(accountRegistrationRequest.getRrn());
        accountRegistrationRequest1.setSegmentID(accountRegistrationRequest.getSegmentID());
        accountRegistrationRequest1.setSessionId(accountRegistrationRequest.getSessionId());
        accountRegistrationRequest1.setTerminalId(accountRegistrationRequest.getTerminalId());
        accountRegistrationRequest1.setDebitCardNumber(accountRegistrationRequest.getDebitCardNumber());
        accountRegistrationRequest1.setTransactionType(accountRegistrationRequest.getTransactionType());
        String req = JSONUtil.getJSON(accountRegistrationRequest1);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        AccountRegistrationResponse response = (AccountRegistrationResponse) JSONUtil.jsonToObject((String) res1.getBody(), AccountRegistrationResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public AccountRegistrationResponse cardIssuaneReissuance(CardIssuanceReissuanceRequest cardIssuanceReissuanceRequest) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutAccountRegistrationURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.AccountRegistrationRequest accountRegistrationRequest1 = new com.inov8.integration.channel.BOPBLB.request.AccountRegistrationRequest();
        accountRegistrationRequest1.setAgentCity(cardIssuanceReissuanceRequest.getAgentCity());
        accountRegistrationRequest1.setAgentLocation(cardIssuanceReissuanceRequest.getAgentLocation());
        accountRegistrationRequest1.setCnic(cardIssuanceReissuanceRequest.getCnic());
        accountRegistrationRequest1.setFingerIndex(cardIssuanceReissuanceRequest.getFingerIndex());
        accountRegistrationRequest1.setFingerTemplate(cardIssuanceReissuanceRequest.getFingerTemplate());
        accountRegistrationRequest1.setMobileNo(cardIssuanceReissuanceRequest.getMobileNo());
        accountRegistrationRequest1.setRrn(cardIssuanceReissuanceRequest.getRrn());
        accountRegistrationRequest1.setSegmentID(cardIssuanceReissuanceRequest.getSegmentID());
        accountRegistrationRequest1.setSessionId(cardIssuanceReissuanceRequest.getSessionId());
        accountRegistrationRequest1.setTerminalId(cardIssuanceReissuanceRequest.getTerminalId());
        accountRegistrationRequest1.setDebitCardNumber(cardIssuanceReissuanceRequest.getDebitCardNumber());
        accountRegistrationRequest1.setTransactionType(cardIssuanceReissuanceRequest.getTransactionType());
        String req = JSONUtil.getJSON(accountRegistrationRequest1);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        AccountRegistrationResponse response = (AccountRegistrationResponse) JSONUtil.jsonToObject((String) res1.getBody(), AccountRegistrationResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public CashWithdrawlReversalResponse cashWithdrawlReversal(CashWithdrawlReversalRequest cashWithdrawlReversalRequest) {

        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutCashWithdrawalReversalURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.CashWithdrawlReversalRequest request = new com.inov8.integration.channel.BOPBLB.request.CashWithdrawlReversalRequest();
        request.setMobileNo(cashWithdrawlReversalRequest.getMobileNo());
        request.setTransactionCode(cashWithdrawlReversalRequest.getTransactionCode());
        request.setRrn(cashWithdrawlReversalRequest.getRrn());
        request.setOrignalRRN(cashWithdrawlReversalRequest.getOrignalRRN());
        request.setOrignalTransactionDateTime(cashWithdrawlReversalRequest.getOrignalTransactionDateTime());
        request.setTerminalId(cashWithdrawlReversalRequest.getTerminalId());

        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);


        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        CashWithdrawlReversalResponse response = (CashWithdrawlReversalResponse) JSONUtil.jsonToObject((String) res1.getBody(), CashWithdrawlReversalResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//        CashWithdrawlReversalResponse Response=new CashWithdrawlReversalResponse();
        return response;
    }

    public ProofOfLifeVerificationInquiryResponse proofOfLifeVerificationInquiry(ProofOfLifeVerificationInquiryRequest proofOfLifeVerificationInquiryRequest) {
//
//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutProofOfLifeVerificationInquiryURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationInquiryRequest request = new com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationInquiryRequest();
        request.setUdid(proofOfLifeVerificationInquiryRequest.getUdid());
        request.setMobileNumber(proofOfLifeVerificationInquiryRequest.getMobileNumber());
        request.setMachineName(proofOfLifeVerificationInquiryRequest.getMachineName());
        request.setMacAddress(proofOfLifeVerificationInquiryRequest.getMacAddress());
        request.setIpAddress(proofOfLifeVerificationInquiryRequest.getIpAddress());
        request.setCnic(proofOfLifeVerificationInquiryRequest.getCnic());
        request.setAgentLocation(proofOfLifeVerificationInquiryRequest.getAgentLocation());
        request.setAgentID(proofOfLifeVerificationInquiryRequest.getAgentID());
        request.setSegmentID(proofOfLifeVerificationInquiryRequest.getSegmentID());
        request.setRrn(proofOfLifeVerificationInquiryRequest.getRrn());
        request.setAgentCity(proofOfLifeVerificationInquiryRequest.getAgentCity());
        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);
        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        ProofOfLifeVerificationInquiryResponse response = (ProofOfLifeVerificationInquiryResponse) JSONUtil.jsonToObject((String) res1.getBody(), ProofOfLifeVerificationInquiryResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

    public ProofOfLifeVerificationResponse proofOfLifeVerification(ProofOfLifeVerificationRequest proofOfLifeVerificationRequest) {

//        UriComponentsBuilder builderAccessToken = UriComponentsBuilder.fromHttpUrl(cashOutAccessTokenURL);
//        UserAuthenticationRequest userAuthenticationRequest=new UserAuthenticationRequest();
//        userAuthenticationRequest.setUserName("BOP@BLB123");
//        userAuthenticationRequest.setPassword("a4abop@blb");
//        String requestJson = JSONUtil.getJSON(userAuthenticationRequest);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//        HttpEntity httpEntity = new HttpEntity(requestJson,header);
//
//        ResponseEntity result = this.restTemplate.postForEntity(builderAccessToken.build().toUri(), httpEntity, String.class);
//
//        com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse response1 = ( com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse) JSONUtil.jsonToObject((String) result.getBody(),  com.inov8.integration.channel.BOPBLB.response.AccesTokenResponse.class);
//        String token=response1.getAccess_token();
        long start = System.currentTimeMillis();
        String plainCreds = bopCashOutUsername + ":" + bopCashOutPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cashOutProofOfLifeVerificationURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        String Request = JSONUtil.getJSON(accountRegistrationRequest);
        List acceptCharset = Collections.singletonList(StandardCharsets.UTF_8);
        headers.setAcceptCharset(acceptCharset);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }
        com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationRequest request = new   com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationRequest();
        request.setUdid(proofOfLifeVerificationRequest.getUdid());
        request.setMobileNo(proofOfLifeVerificationRequest.getMobileNo());
        request.setMachineName(proofOfLifeVerificationRequest.getMachineName());
        request.setMacAddress(proofOfLifeVerificationRequest.getMacAddress());
        request.setIpAddress(proofOfLifeVerificationRequest.getIpAddress());
        request.setCnic(proofOfLifeVerificationRequest.getCnic());
        request.setAgentLocation(proofOfLifeVerificationRequest.getAgentLocation());
        request.setAgentID(proofOfLifeVerificationRequest.getAgentID());
        request.setSegmentId(proofOfLifeVerificationRequest.getSegmentId());
        request.setRrn(proofOfLifeVerificationRequest.getRrn());
        request.setAgentCity(proofOfLifeVerificationRequest.getAgentCity());
        request.setFingeIndex(proofOfLifeVerificationRequest.getFingeIndex());
        request.setFingerTemplate(proofOfLifeVerificationRequest.getFingerTemplate());
        request.setTemplateType(proofOfLifeVerificationRequest.getTemplateType());
        request.setAcknowledgmentFlag(proofOfLifeVerificationRequest.getAcknowledgmentFlag());
        String req = JSONUtil.getJSON(request);
        HttpEntity httpEntit = new HttpEntity(req, headers);

        ResponseEntity res1 = this.restTemplate.postForEntity(builder.build().toUri(), httpEntit, String.class);
        ProofOfLifeVerificationResponse response = (ProofOfLifeVerificationResponse) JSONUtil.jsonToObject((String) res1.getBody(), ProofOfLifeVerificationResponse.class);
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        return response;
    }

}
