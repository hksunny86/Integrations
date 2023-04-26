package com.inov8.integration.channel.zindigi.service;

import com.inov8.integration.channel.JSBookMe.service.JSBookMeService;
//import com.inov8.integration.channel.telenor.response.ServiceDetailResponse;
import com.inov8.integration.channel.zindigi.mock.ZindigiCustomerSyncMock;
import com.inov8.integration.channel.zindigi.request.L2AccountUpgradeValidationRequest;
import com.inov8.integration.channel.zindigi.request.MinorAccountSyncRequest;
import com.inov8.integration.channel.zindigi.request.P2MStatusUpdateRequest;
import com.inov8.integration.channel.zindigi.request.ZindigiCustomerSyncRequest;
import com.inov8.integration.channel.zindigi.response.L2AccountUpgradeValidationResponse;
import com.inov8.integration.channel.zindigi.response.MinorAccountSyncResponse;
import com.inov8.integration.channel.zindigi.response.P2MStatusUpdateResponse;
import com.inov8.integration.channel.zindigi.response.ZindigiCustomerSyncResponse;
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
import java.util.Objects;

@Service
public class ZindigiCustomerSyncService {

    private static Logger logger = LoggerFactory.getLogger(JSBookMeService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");

    @Value("${zindigi.customer.sync.url}")
    private String zindigiCustomerSyncUrl;
    @Value("${zindigi.customer.sync.access_token}")
    private String accessToken;
    @Value("${l2.account.upgrade.validation.url}")
    private String l2AccountUpgradeValidationUrl;
    @Value("${l2.account.upgrade.validation.access_token}")
    private String l2AccountUpgradeValidationAccessToken;
    @Value("${p2m.status.update.access_token}")
    private String p2mStatusUpdateAccessToken;
    @Value("${p2m.status.update.url}")
    private String p2mStatusUpdateUrl;

    @Value("${zindigi.account.url}")
    private String minorAccountOpeningUrl;
    @Value("${zindigi.access_token}")
    private String minorAccountOpeningToken;

    public ZindigiCustomerSyncResponse zindigiCustomerSyncRequest(ZindigiCustomerSyncRequest request) throws Exception {

        ZindigiCustomerSyncResponse zindigiCustomerSyncResponse = new ZindigiCustomerSyncResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            ZindigiCustomerSyncMock mock = new ZindigiCustomerSyncMock();
            String response = mock.ZindigiCustomerSyncMock();

            zindigiCustomerSyncResponse = (ZindigiCustomerSyncResponse) JSONUtil.jsonToObject(response, ZindigiCustomerSyncResponse.class);
            logger.info("Status for Zindigi Customer Sync Request : " + zindigiCustomerSyncResponse.getDescription());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(zindigiCustomerSyncUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);

            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Zindigi Customer Sync Request : " + requesJson);

            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {
                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);

                if (res.getStatusCode().toString().equals("200")) {
                    logger.info("Successful Response Received");
                    zindigiCustomerSyncResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                    zindigiCustomerSyncResponse.setDescription("Completed");
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("422")) {
                        zindigiCustomerSyncResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        zindigiCustomerSyncResponse.setDescription(((HttpStatusCodeException) e).getStatusText());
                    }
                    if (response.equals("404")) {
                        zindigiCustomerSyncResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        zindigiCustomerSyncResponse.setDescription(((HttpStatusCodeException) e).getStatusText());


                    } else if (response.equals("500")) {
                        zindigiCustomerSyncResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        zindigiCustomerSyncResponse.setDescription(((HttpStatusCodeException) e).getStatusText());

                    } else {
                        zindigiCustomerSyncResponse.setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        zindigiCustomerSyncResponse.setDescription(((HttpStatusCodeException) e).getStatusText());

                    }
                }
            }

        }

        return zindigiCustomerSyncResponse;
    }

    public L2AccountUpgradeValidationResponse sendL2AccountUpgradeValidationResponse(L2AccountUpgradeValidationRequest request) {
        L2AccountUpgradeValidationResponse l2AccountUpgradeValidationResponse = new L2AccountUpgradeValidationResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            ZindigiCustomerSyncMock mock = new ZindigiCustomerSyncMock();
            String response = mock.L2AccountUpgradeValidation();

            l2AccountUpgradeValidationResponse = (L2AccountUpgradeValidationResponse) JSONUtil.jsonToObject(response, L2AccountUpgradeValidationResponse.class);
            logger.info("Status for L2 Account Upgrade Validation Request : " + l2AccountUpgradeValidationResponse.getResponseDescription());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(l2AccountUpgradeValidationUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", l2AccountUpgradeValidationAccessToken);

            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending L2 Account Upgrade Validation Request : " + requesJson);

            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

            logger.info("L2 Account Upgrade Validation Detail Response Received : " + res1.getBody());
            l2AccountUpgradeValidationResponse = (L2AccountUpgradeValidationResponse) JSONUtil.jsonToObject(res1.getBody(), L2AccountUpgradeValidationResponse.class);

        }

        return l2AccountUpgradeValidationResponse;
    }

    public MinorAccountSyncResponse sendMinorAccount(MinorAccountSyncRequest request) {
        MinorAccountSyncResponse minorAccountSyncResponse = new MinorAccountSyncResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            ZindigiCustomerSyncMock mock = new ZindigiCustomerSyncMock();
            String response = mock.L2AccountUpgradeValidation();

            minorAccountSyncResponse = (MinorAccountSyncResponse) JSONUtil.jsonToObject(response, MinorAccountSyncResponse.class);
            logger.info("Status for Minor Account Opening Sync Request : " + minorAccountSyncResponse.getDescription());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(minorAccountOpeningUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", minorAccountOpeningToken);

            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Minor Account Opening Sync Request : " + requesJson);

            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

            logger.info("Minor Account Opening Sync Response Received : " + res1.getBody());
            minorAccountSyncResponse = (MinorAccountSyncResponse) JSONUtil.jsonToObject(res1.getBody(), MinorAccountSyncResponse.class);

        }

        return minorAccountSyncResponse;
    }

    public P2MStatusUpdateResponse sendP2MStatusUpdateResponse(P2MStatusUpdateRequest request) {
        P2MStatusUpdateResponse p2MStatusUpdateResponse = new P2MStatusUpdateResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            ZindigiCustomerSyncMock mock = new ZindigiCustomerSyncMock();
            String response = mock.p2mStatusUpdate();

            p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(response, P2MStatusUpdateResponse.class);
            Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode("200");
            logger.info("Status for P2M Status Update Request : " + p2MStatusUpdateResponse.getResponseDescription());
        } else {
            String response;
            try {
                UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(p2mStatusUpdateUrl);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Access_token", p2mStatusUpdateAccessToken);

                String requesJson = JSONUtil.getJSON(request);
                logger.info("Sending P2M Status Update Request to Client : " + requesJson);

                HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

                for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                    if (converter instanceof StringHttpMessageConverter) {
                        ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                    }
                }

                ResponseEntity<String> res = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
//                ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
                logger.info("Response Code received from client " + res.getStatusCode().value());
                logger.info("P2M Status Update Response Received : " + res.getBody());
                String responseCode = String.valueOf(res.getStatusCode().value());
                if (responseCode.equals("200")) {
//                    p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(res.getBody(), P2MStatusUpdateResponse.class);
                    Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(result, P2MStatusUpdateResponse.class);
                        Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(result, P2MStatusUpdateResponse.class);
                        Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(result, P2MStatusUpdateResponse.class);
                        Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                        p2MStatusUpdateResponse = (P2MStatusUpdateResponse) JSONUtil.jsonToObject(result, P2MStatusUpdateResponse.class);
                        Objects.requireNonNull(p2MStatusUpdateResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                    }
                }
            }
        }

        return p2MStatusUpdateResponse;
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
