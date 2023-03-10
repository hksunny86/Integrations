package com.inov8.integration.channel.offlineBiller.service;



import com.inov8.integration.channel.offlineBiller.response.BillInquiryResponse;
import com.inov8.integration.channel.offlineBiller.response.BillPaymentResponse;
import com.inov8.integration.channel.offlineBiller.resquest.BillInquiryRequest;
import com.inov8.integration.channel.offlineBiller.resquest.BillPaymentRequest;
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
public class OffLineBillerService {

    private static Logger logger = LoggerFactory.getLogger(OffLineBillerService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${fetch.offline.biller.url}")
    private String fetchOfflineBiller;
    @Value("${pay.offline.biller.url}")
    private String payOfflineBiller;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");


    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public BillInquiryResponse billInquiryResponse(BillInquiryRequest request) throws Exception {

        BillInquiryResponse billInquiryResponse = new BillInquiryResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To OffLine Biller Server : " + requesJson);
            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Bill  Inquiry " + fetchOfflineBiller);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(fetchOfflineBiller);
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
                billInquiryResponse = (BillInquiryResponse) JSONUtil.jsonToObject(res.getBody(), BillInquiryResponse.class);
                logger.info("Bill Inquiry  Response Received from Server : " + res.getBody());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        billInquiryResponse = (BillInquiryResponse) JSONUtil.jsonToObject(result, BillInquiryResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        billInquiryResponse = (BillInquiryResponse) JSONUtil.jsonToObject(resp, BillInquiryResponse.class);

                    }

                }
            }
        }
        return billInquiryResponse;
    }


    public BillPaymentResponse billPaymentResponse(BillPaymentRequest request) throws
            Exception {

        BillPaymentResponse billPaymentResponse = new BillPaymentResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Zmiles Server : " + requesJson);

        } else {
            logger.info("Bill Payment: " + payOfflineBiller);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(payOfflineBiller);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Bill Payment Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }


            try {
                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                billPaymentResponse = (BillPaymentResponse) JSONUtil.jsonToObject(res.getBody(), BillPaymentResponse.class);
                logger.info("Bill Payment Response Received from Server : " + res.getBody());

            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        billPaymentResponse = (BillPaymentResponse) JSONUtil.jsonToObject(result, BillPaymentResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        billPaymentResponse = (BillPaymentResponse) JSONUtil.jsonToObject(resp, BillPaymentResponse.class);

                    }
                }
            }
        }
        return billPaymentResponse;
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
