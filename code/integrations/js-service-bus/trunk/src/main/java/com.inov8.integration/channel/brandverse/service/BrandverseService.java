package com.inov8.integration.channel.brandverse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.brandverse.request.AccessTokenRequest;
import com.inov8.integration.channel.brandverse.request.BrandverseNotifyRequest;
import com.inov8.integration.channel.brandverse.response.AccessTokenResponse;
import com.inov8.integration.channel.brandverse.response.BrandverseNotifyResponse;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class BrandverseService {

    private static Logger logger = LoggerFactory.getLogger(BrandverseService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String accessTokenUrl = PropertyReader.getProperty("brandverse.accessToken");
    private String notifyUrl = PropertyReader.getProperty("brandverse.notify");
    private String proxyIp = PropertyReader.getProperty("brandverse.proxy.ip");
    private String proxyPort = PropertyReader.getProperty("brandverse.proxy.port");
    private Boolean proxyFlag = Boolean.valueOf(PropertyReader.getProperty("brandverse.proxy.flag"));
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    private SimpleClientHttpRequestFactory getClientHttpRequestFactoryWithProxy() {
        // Create a new instance of SimpleClientHttpRequestFactory
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        // Set the connection timeout to 60,000 milliseconds (60 seconds)
        clientHttpRequestFactory.setConnectTimeout(60000);

        // Set the read timeout to 60,000 milliseconds (60 seconds)
        clientHttpRequestFactory.setReadTimeout(60000);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, Integer.parseInt(proxyPort)));
        clientHttpRequestFactory.setProxy(proxy);

        // Return the configured SimpleClientHttpRequestFactory instance
        return clientHttpRequestFactory;
    }

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
            if (proxyFlag) {
                restTemplate = new RestTemplate(getClientHttpRequestFactoryWithProxy());
            }

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

    public AccessTokenResponse accessTokenResponse(AccessTokenRequest accessTokenRequest) {

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3RVc2VyIiwiaWF0IjoxNjg2MDU4NDUwLCJleHAiOjE2ODYwNjIwNTB9.Xt7HTQ5aP3WnbqvStkNTGFfh-l0lBkdvUvL6iY93t7o\"\n" +
                    "}";
            accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(response, AccessTokenResponse.class);
            Objects.requireNonNull(accessTokenResponse).setResponseCode("200");
            logger.info("Response Code for Brandverse Access Token Request: " + accessTokenResponse.getResponseCode());
        } else {

            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("accept", "application/json");
            postParam.put("username", accessTokenRequest.getUserName());
            postParam.put("password", accessTokenRequest.getPassword());
            logger.info("Request body of Brandverse Access Token " + JSONUtil.getJSON(accessTokenRequest));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, accessTokenUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    accessTokenResponse = objectMapper.readValue(responseBody, AccessTokenResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                logger.info("[ Exception ] accessTokenResponse() " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Brandverse Access Token Request processed in: " + difference + " millisecond");
//            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.accessTokenUrl);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            String requestJSON = JSONUtil.getJSON(accessTokenRequest);
//            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
//            logger.info("Prepared Request HttpEntity " + httpEntity);
//            Iterator res = this.restTemplate.getMessageConverters().iterator();
//
//            while (res.hasNext()) {
//                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
//                if (endTime instanceof StringHttpMessageConverter) {
//                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
//                }
//            }
//            String response;
//            try {
//                logger.info("Requesting URL " + uri.toUriString());
//                logger.info("Sending Brandverse Access Token Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
//                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
//
//                logger.info("Response Entity: " + res1);
//                logger.info("Response Code of Brandverse Access Token Request received from client " + res1.getStatusCode().toString());
//                logger.info("Response of Brandverse Access Token Request received from client " + res1.getBody());
//                accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(res1.getBody(), AccessTokenResponse.class);
//                Objects.requireNonNull(accessTokenResponse).setResponseCode(res1.getStatusCode().toString());
//            } catch (RestClientException e) {
//                if (e instanceof HttpStatusCodeException) {
//                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
//                    String result;
//                    if (response.equals("400")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
//                    } else if (response.equals("422")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
//                    } else if (response.equals("500")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
//                    } else {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
//                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
//                    }
//                }
//                if (e instanceof ResourceAccessException) {
//                    String result = e.getMessage();
//                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
//                    Objects.requireNonNull(accessTokenResponse).setResponseCode("500");
////                    accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
//                }
//            } catch (Exception e) {
//                logger.error("Exception Occurred: " + e.getMessage());
//            }
//
//            long endTime = (new Date()).getTime();
//            long difference = endTime - start;
//            logger.debug("Brandverse Access Token Request processed in: " + difference + " millisecond");
        }
        return accessTokenResponse;
    }

    public BrandverseNotifyResponse brandverseNotifyResponse(BrandverseNotifyRequest brandverseNotifyRequest) {
        BrandverseNotifyResponse brandverseNotifyResponse = new BrandverseNotifyResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + this.i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"notify\": true\n" +
                    "}";
            brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(response, BrandverseNotifyResponse.class);
            Objects.requireNonNull(brandverseNotifyResponse).setResponseCode("200");
            logger.info("Mock Response Code for Brandverse Notify Request: " + brandverseNotifyResponse.getResponseCode());
        } else {
            String token = i8SBSwitchControllerRequestVO.getAccessToken();
            Map<String, Object> postParam = new HashMap<String, Object>();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("accept", "application/json");
            headers.put("Authorization", "Bearer " + token);
            postParam.put("merchantXid", brandverseNotifyRequest.getMerchantXid());
            postParam.put("transactionId", brandverseNotifyRequest.getTransactionId());
            postParam.put("amount", brandverseNotifyRequest.getAmount());
            postParam.put("timestamp", brandverseNotifyRequest.getTimestamp());
            postParam.put("billNumber", brandverseNotifyRequest.getBillNumber());
            logger.info("Access Token " + token);
            logger.info("Request body of Brandverse Notify Request " + JSONUtil.getJSON(brandverseNotifyRequest));
            try {
                String responseBody = getResponseFromAPI(headers, postParam, notifyUrl);
                if (responseBody != null && responseBody.length() > 0) {
                    brandverseNotifyResponse = objectMapper.readValue(responseBody, BrandverseNotifyResponse.class);
                }
            } catch (RestClientException e) {
                handleRestClientException(e, i8SBSwitchControllerResponseVO);
            } catch (Exception e) {
                logger.info("[ Exception ] accessTokenResponse() " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Brandverse Notify Request processed in: " + difference + " millisecond");

//            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.notifyUrl);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            String token = i8SBSwitchControllerRequestVO.getAccessToken();
//            logger.info("Access Token " + token);
//            headers.add("Authorization", "Bearer " + token);
//            String requestJSON = JSONUtil.getJSON(brandverseNotifyRequest);
//            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
//            logger.info("Prepared Request HttpEntity " + httpEntity);
//            Iterator res = this.restTemplate.getMessageConverters().iterator();
//
//            while (res.hasNext()) {
//                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
//                if (endTime instanceof StringHttpMessageConverter) {
//                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
//                }
//            }
//            String response;
//            try {
//                logger.info("Requesting URL " + uri.toUriString());
//                logger.info("Sending Brandverse Notify Request Sent to Client " + httpEntity.getBody().toString());
//                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
//                logger.info("Response Entity: " + res1);
//                logger.info("Response Code of Brandverse Notify Request received from client " + res1.getStatusCode().toString());
//                logger.info("Response of Brandverse Notify Request received from client " + res1.getBody());
//                brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(res1.getBody(), BrandverseNotifyResponse.class);
//                Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(res1.getStatusCode().toString());
//            } catch (RestClientException e) {
//                if (e instanceof HttpStatusCodeException) {
//                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
//                    String result;
//                    if (response.equals("400")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
//                    } else if (response.equals("422")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
//                    } else if (response.equals("500")) {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
//                    } else {
//                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received " + ((HttpStatusCodeException) e).getStatusCode().toString());
//                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
//                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
//                    }
//                }
//                if (e instanceof ResourceAccessException) {
//                    String result = e.getMessage();
//                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
//                    Objects.requireNonNull(brandverseNotifyResponse).setResponseCode("500");
////                    brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
//                }
//
//            } catch (Exception e) {
//                logger.error("Exception Occurred: " + e.getMessage());
//            }
//
//            long endTime = (new Date()).getTime();
//            long difference = endTime - start;
//            logger.debug("Brandverse Notify Request processed in: " + difference + " millisecond");
        }

        return brandverseNotifyResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

//    public RestTemplate getRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        if (proxyFlag) {
//            restTemplate = new RestTemplate(getClientHttpRequestFactoryWithProxy());
//        }
//        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
//            @Override
//            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
//                return true;
//            }
//
//        };
//
//        SSLContext sslContext = null;
//        try {
//            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
//                    .build();
//        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
//            e.printStackTrace();
//        }
//        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//
//        restTemplate.setRequestFactory(requestFactory);
//        return restTemplate;
//    }
}
