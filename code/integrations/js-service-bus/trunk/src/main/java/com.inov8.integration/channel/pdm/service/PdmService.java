package com.inov8.integration.channel.pdm.service;

import com.inov8.integration.channel.brandverse.request.AccessTokenRequest;
import com.inov8.integration.channel.brandverse.request.BrandverseNotifyRequest;
import com.inov8.integration.channel.brandverse.response.AccessTokenResponse;
import com.inov8.integration.channel.brandverse.response.BrandverseNotifyResponse;
import com.inov8.integration.channel.pdm.request.ValidatePdmRequest;
import com.inov8.integration.channel.pdm.response.ValidatePdmResponse;
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
public class PdmService {

    private static Logger logger = LoggerFactory.getLogger(PdmService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String validatePdmUrl = PropertyReader.getProperty("pdm.validate");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public ValidatePdmResponse validatePdmResponse(ValidatePdmRequest validatePdmRequest) {

        ValidatePdmResponse validatePdmResponse = new ValidatePdmResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"ResponseCode\": \"00\",\n" +
                    "    \"ResponseDescription\": \"User Name OR Password incorrect\"\n" +
                    "}";
            validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(response, ValidatePdmResponse.class);
            Objects.requireNonNull(validatePdmResponse).setResponseCode("200");
            logger.info("Response Code for PDM Validate Request: " + validatePdmResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.validatePdmUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(validatePdmUrl);
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
                logger.info("PDM Validate Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of PDM Validate Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of PDM Validate Request received from client " + res1.getBody());
                validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(res1.getBody(), ValidatePdmResponse.class);
                Objects.requireNonNull(validatePdmResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(validatePdmResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(result, ValidatePdmResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(validatePdmResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(result, ValidatePdmResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(validatePdmResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(result, ValidatePdmResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                        Objects.requireNonNull(validatePdmResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        validatePdmResponse = (ValidatePdmResponse) JSONUtil.jsonToObject(result, ValidatePdmResponse.class);
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(validatePdmResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("PDM Validate Request processed in: " + difference + " millisecond");
        }
        return validatePdmResponse;
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
