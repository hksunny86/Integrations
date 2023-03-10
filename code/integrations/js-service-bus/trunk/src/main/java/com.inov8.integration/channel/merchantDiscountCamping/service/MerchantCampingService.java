package com.inov8.integration.channel.merchantDiscountCamping.service;

import com.inov8.integration.channel.JSBookMe.response.JSBBookMeResponse;
import com.inov8.integration.channel.JSBookMe.service.JSBookMeService;
import com.inov8.integration.channel.T24Api.request.IbftTitleFetchRequest;
import com.inov8.integration.channel.T24Api.response.IbftTitleFetchResponse;
import com.inov8.integration.channel.T24Api.service.T24ApiMockService;
import com.inov8.integration.channel.merchantDiscountCamping.request.TransactionUpdateRequest;
import com.inov8.integration.channel.merchantDiscountCamping.request.TransactionValidationRequest;
import com.inov8.integration.channel.merchantDiscountCamping.response.TransactionUpdateResponse;
import com.inov8.integration.channel.merchantDiscountCamping.response.TransactionValidationResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class MerchantCampingService {

    private static Logger logger = LoggerFactory.getLogger(JSBookMeService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${transaction.validation.url}")
    private String transactionValidationURL;
    @Value("${transaction.status.url}")
    private String transactionStatusUpdateURL;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");


    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public TransactionValidationResponse transactionValidationResponse(TransactionValidationRequest request) throws Exception {

        TransactionValidationResponse transactionValidationResponse = new TransactionValidationResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Zmiles Server : " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();

            transactionValidationResponse = (TransactionValidationResponse) JSONUtil.jsonToObject(response, TransactionValidationResponse.class);
//            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Transaction Validation" + transactionValidationURL);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(transactionValidationURL);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Transaction Validation Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {

                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                transactionValidationResponse = (TransactionValidationResponse) JSONUtil.jsonToObject(res.getBody(), TransactionValidationResponse.class);
                logger.info("Transaction Validation Response Received from  Zmiles Server : " + res.getBody());
            }
            catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        transactionValidationResponse.setResponsecode("1");
                        transactionValidationResponse.setMessages("Validation Exception");
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        transactionValidationResponse.setResponsecode("40");
                        transactionValidationResponse.setMessages("Transaction Not Allowed");
                    }
                    else {
                        transactionValidationResponse.setResponsecode("1");
                        transactionValidationResponse.setMessages("SuccessFull");
                    }
                }
                if (e instanceof ResourceAccessException){
                    transactionValidationResponse.setResponsecode("1");
                    transactionValidationResponse.setMessages("SuccessFull");
                }
            }catch (Exception e){
                transactionValidationResponse.setResponsecode("1");
                transactionValidationResponse.setMessages("SuccessFull");
            }
        }
        return transactionValidationResponse;
    }


    public TransactionUpdateResponse transactionUpdateResponse(TransactionUpdateRequest request) throws Exception {

        TransactionUpdateResponse transactionUpdateResponse = new TransactionUpdateResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Zmiles Server : " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();
            String response = mock.transactionValidation();
            transactionUpdateResponse = (TransactionUpdateResponse) JSONUtil.jsonToObject(response, TransactionUpdateResponse.class);
        } else {
            logger.info("Transaction Status" + transactionStatusUpdateURL);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(transactionStatusUpdateURL);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Transaction Status Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }


            try {
                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                transactionUpdateResponse = (TransactionUpdateResponse) JSONUtil.jsonToObject(res.getBody(), TransactionUpdateResponse.class);
                logger.info("Transaction Status Response Received from  Zmiles Server : " + res.getBody());

            } catch (RestClientException e) {

                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    transactionUpdateResponse.setResponsecode("1");
                    transactionUpdateResponse.setMessages("SuccessFull");

            }
        }
        return transactionUpdateResponse;
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
