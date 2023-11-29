package com.inov8.integration.channel.wasa.service;

import com.inov8.integration.channel.wasa.request.*;
import com.inov8.integration.channel.wasa.response.*;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
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
import org.springframework.web.client.ResourceAccessException;
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
public class WasaService {

    private static Logger logger = LoggerFactory.getLogger(WasaService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String wasaLoginUrl = PropertyReader.getProperty("wasa.login");
    private String wasaGetBillUrl = PropertyReader.getProperty("wasa.getBill");
    private String wasaPostBillUrl = PropertyReader.getProperty("wasa.postBill");
    private String wasaReversalUrl = PropertyReader.getProperty("wasa.reversal");
    private String wasaClearanceUrl = PropertyReader.getProperty("wasa.clearance");
    private String wasaLogoutUrl = PropertyReader.getProperty("wasa.logout");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public WasaLoginResponse wasaLoginResponse(WasaLoginRequest wasaLoginRequest) {

        WasaLoginResponse wasaLoginResponse = new WasaLoginResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"access_token\":\"000201010211287600321d06bcb1a9b1487aa6c4962d6dbd622e0108JSBLPKKA0224PK27JSBL99999033479111775204581453035865802PK5912SudaisPharma6005Badin62520312SudaisPharma07099462411760819FastFoodRestaurants80230019FastFoodRestaurants63043E42\",\n" +
                    "    \"token_type\":\"bearer\",\n" +
                    "    \"expires_in\":\"2591999\"\n" +
                    "}";
            wasaLoginResponse = (WasaLoginResponse) JSONUtil.jsonToObject(response, WasaLoginResponse.class);
            Objects.requireNonNull(wasaLoginResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Login: " + wasaLoginResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaLoginUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("username", wasaLoginRequest.getUsername());
            headers.add("password", wasaLoginRequest.getPassword());
            headers.add("ipaddress", wasaLoginRequest.getIpaddress());
            headers.add("grant_type", wasaLoginRequest.getGrantType());
            String requestJSON = JSONUtil.getJSON(wasaLoginRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Login Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Login Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Login Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaLoginResponse = (WasaLoginResponse) JSONUtil.jsonToObject(res1.getBody(), WasaLoginResponse.class);
                    Objects.requireNonNull(wasaLoginResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaLoginResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLoginResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaLoginResponse = (WasaLoginResponse) JSONUtil.jsonToObject(result, WasaLoginResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLoginResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLoginResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaLoginResponse = (WasaLoginResponse) JSONUtil.jsonToObject(result, WasaLoginResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaLoginResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaLoginResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Login Request processed in: " + difference + " millisecond");
        }
        return wasaLoginResponse;
    }

    public WasaGetBillResponse wasaGetBillResponse(WasaGetBillRequest wasaGetBillRequest) {

        WasaGetBillResponse wasaGetBillResponse = new WasaGetBillResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "        \"Transaction\":false,\n" +
                    "        \"BillAmount\":64392.0,\n" +
                    "        \"BillAmountAfterDueDate\":70831.0,\n" +
                    "        \"DueDate\":\"02-02-2023\",\n" +
                    "        \"ReceivedAmount\":0.0,\n" +
                    "        \"Period\":\"202302\",\n" +
                    "        \"ConsumerNumber\":\"60003153\",\n" +
                    "        \"ConsumerName\":\"ABDUL RASHID\",\n" +
                    "        \"status\":\"1\"\n" +
                    "}";
            wasaGetBillResponse = (WasaGetBillResponse) JSONUtil.jsonToObject(response, WasaGetBillResponse.class);
            Objects.requireNonNull(wasaGetBillResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Get Bill Request: " + wasaGetBillResponse.getResponseCode());
        } else {

            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaGetBillUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            String requestJSON = JSONUtil.getJSON(wasaGetBillRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Get Bill Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Get Bill Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Get Bill Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaGetBillResponse = (WasaGetBillResponse) JSONUtil.jsonToObject(res1.getBody(), WasaGetBillResponse.class);
                    Objects.requireNonNull(wasaGetBillResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaGetBillResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaGetBillResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaGetBillResponse = (WasaGetBillResponse) JSONUtil.jsonToObject(result, WasaGetBillResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaGetBillResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaGetBillResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaGetBillResponse = (WasaGetBillResponse) JSONUtil.jsonToObject(result, WasaGetBillResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaGetBillResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaGetBillResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Get Bill Request processed in: " + difference + " millisecond");
        }
        return wasaGetBillResponse;
    }

    public WasaPostBillResponse wasaPostBillResponse(WasaPostBillRequest wasaPostBillRequest) {

        WasaPostBillResponse wasaPostBillResponse = new WasaPostBillResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "        \"Transaction\":false,\n" +
                    "        \"status\":\"2\"\n" +
                    "}";
            wasaPostBillResponse = (WasaPostBillResponse) JSONUtil.jsonToObject(response, WasaPostBillResponse.class);
            Objects.requireNonNull(wasaPostBillResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Post Bill Request: " + wasaPostBillResponse.getResponseCode());
        } else {

            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaPostBillUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            String requestJSON = JSONUtil.getJSON(wasaPostBillRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Post Bill Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Post Bill Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Post Bill Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaPostBillResponse = (WasaPostBillResponse) JSONUtil.jsonToObject(res1.getBody(), WasaPostBillResponse.class);
                    Objects.requireNonNull(wasaPostBillResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaPostBillResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaPostBillResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaPostBillResponse = (WasaPostBillResponse) JSONUtil.jsonToObject(result, WasaPostBillResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaPostBillResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaPostBillResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaPostBillResponse = (WasaPostBillResponse) JSONUtil.jsonToObject(result, WasaPostBillResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaPostBillResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaPostBillResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Post Bill Request processed in: " + difference + " millisecond");
        }
        return wasaPostBillResponse;
    }

    public WasaBillReversalResponse wasaBillReversalResponse(WasaBillReversalRequest wasaBillReversalRequest) {

        WasaBillReversalResponse wasaBillReversalResponse = new WasaBillReversalResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "        \"Transaction\":false,\n" +
                    "        \"status\":\"2\"\n" +
                    "}";
            wasaBillReversalResponse = (WasaBillReversalResponse) JSONUtil.jsonToObject(response, WasaBillReversalResponse.class);
            Objects.requireNonNull(wasaBillReversalResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Bill Reversal: " + wasaBillReversalResponse.getResponseCode());
        } else {

            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaReversalUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            String requestJSON = JSONUtil.getJSON(wasaBillReversalRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Bill Reversal Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Bill Reversal Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Bill Reversal Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaBillReversalResponse = (WasaBillReversalResponse) JSONUtil.jsonToObject(res1.getBody(), WasaBillReversalResponse.class);
                    Objects.requireNonNull(wasaBillReversalResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaBillReversalResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillReversalResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaBillReversalResponse = (WasaBillReversalResponse) JSONUtil.jsonToObject(result, WasaBillReversalResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillReversalResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillReversalResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaBillReversalResponse = (WasaBillReversalResponse) JSONUtil.jsonToObject(result, WasaBillReversalResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaBillReversalResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaBillReversalResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Bill Reversal Request processed in: " + difference + " millisecond");
        }
        return wasaBillReversalResponse;
    }

    public WasaBillClearanceResponse wasaBillClearanceResponse(WasaBillClearanceRequest wasaBillClearanceRequest) {

        WasaBillClearanceResponse wasaBillClearanceResponse = new WasaBillClearanceResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "        \"Transaction\":false,\n" +
                    "        \"status\":\"2\"\n" +
                    "}";
            wasaBillClearanceResponse = (WasaBillClearanceResponse) JSONUtil.jsonToObject(response, WasaBillClearanceResponse.class);
            Objects.requireNonNull(wasaBillClearanceResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Bill Clearance: " + wasaBillClearanceResponse.getResponseCode());
        } else {

            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaClearanceUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            String requestJSON = JSONUtil.getJSON(wasaBillClearanceRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Bill Clearnace Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Bill Clearance Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Bill Clearance Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaBillClearanceResponse = (WasaBillClearanceResponse) JSONUtil.jsonToObject(res1.getBody(), WasaBillClearanceResponse.class);
                    Objects.requireNonNull(wasaBillClearanceResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaBillClearanceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillClearanceResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaBillClearanceResponse = (WasaBillClearanceResponse) JSONUtil.jsonToObject(result, WasaBillClearanceResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillClearanceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaBillClearanceResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaBillClearanceResponse = (WasaBillClearanceResponse) JSONUtil.jsonToObject(result, WasaBillClearanceResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaBillClearanceResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaBillClearanceResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Bill Clearance Request processed in: " + difference + " millisecond");
        }
        return wasaBillClearanceResponse;
    }

    public WasaLogoutResponse wasaLogoutResponse(WasaLogoutRequest wasaLogoutRequest) {

        WasaLogoutResponse wasaLogoutResponse = new WasaLogoutResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "        \"Transaction\":false,\n" +
                    "        \"status\":\"2\"\n" +
                    "}";
            wasaLogoutResponse = (WasaLogoutResponse) JSONUtil.jsonToObject(response, WasaLogoutResponse.class);
            Objects.requireNonNull(wasaLogoutResponse).setResponseCode("200");
            logger.info("Response Code for Wasa Bill Logout: " + wasaLogoutResponse.getResponseCode());
        } else {

            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.wasaLogoutUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            String requestJSON = JSONUtil.getJSON(wasaLogoutRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            String url = uri.toUriString();
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + url);
                logger.info("Wasa Bill Logout Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Wasa Bill Logout Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Wasa Bill Logout Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    wasaLogoutResponse = (WasaLogoutResponse) JSONUtil.jsonToObject(res1.getBody(), WasaLogoutResponse.class);
                    Objects.requireNonNull(wasaLogoutResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(wasaLogoutResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLogoutResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaLogoutResponse = (WasaLogoutResponse) JSONUtil.jsonToObject(result, WasaLogoutResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLogoutResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(wasaLogoutResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            wasaLogoutResponse = (WasaLogoutResponse) JSONUtil.jsonToObject(result, WasaLogoutResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(wasaLogoutResponse).setResponseCode("500");
                    Objects.requireNonNull(wasaLogoutResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Wasa Bill Logout Request processed in: " + difference + " millisecond");
        }
        return wasaLogoutResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
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
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
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
