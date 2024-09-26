package com.inov8.integration.channel.brandverse.service;

import com.inov8.integration.channel.brandverse.request.AccessTokenRequest;
import com.inov8.integration.channel.brandverse.request.BrandverseNotifyRequest;
import com.inov8.integration.channel.brandverse.response.AccessTokenResponse;
import com.inov8.integration.channel.brandverse.response.BrandverseNotifyResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

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

    public AccessTokenResponse accessTokenResponse(AccessTokenRequest accessTokenRequest) {

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

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

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.accessTokenUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(accessTokenRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
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
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Sending Brandverse Access Token Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Brandverse Access Token Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Brandverse Access Token Request received from client " + res1.getBody());
                accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(res1.getBody(), AccessTokenResponse.class);
                Objects.requireNonNull(accessTokenResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                        Objects.requireNonNull(accessTokenResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(accessTokenResponse).setResponseCode("500");
//                    accessTokenResponse = (AccessTokenResponse) JSONUtil.jsonToObject(result, AccessTokenResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Brandverse Access Token Request processed in: " + difference + " millisecond");
        }
        return accessTokenResponse;
    }

    public BrandverseNotifyResponse brandverseNotifyResponse(BrandverseNotifyRequest brandverseNotifyRequest) {
        BrandverseNotifyResponse brandverseNotifyResponse = new BrandverseNotifyResponse();

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

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.notifyUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String token = i8SBSwitchControllerRequestVO.getAccessToken();
            logger.info("Access Token " + token);
            headers.add("Authorization", "Bearer " + token);
            String requestJSON = JSONUtil.getJSON(brandverseNotifyRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
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
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Sending Brandverse Notify Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Brandverse Notify Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Brandverse Notify Request received from client " + res1.getBody());
                brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(res1.getBody(), BrandverseNotifyResponse.class);
                Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received " + ((HttpStatusCodeException) e).getStatusCode().toString());
                        Objects.requireNonNull(brandverseNotifyResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(brandverseNotifyResponse).setResponseCode("500");
//                    brandverseNotifyResponse = (BrandverseNotifyResponse) JSONUtil.jsonToObject(result, BrandverseNotifyResponse.class);
                }

            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Brandverse Notify Request processed in: " + difference + " millisecond");
        }

        return brandverseNotifyResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        if (proxyFlag) {
            restTemplate = new RestTemplate(getClientHttpRequestFactoryWithProxy());
        }
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
}
