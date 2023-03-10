package com.inov8.integration.channel.JSDebitCardApi.service;

import com.inov8.integration.channel.JSDebitCardApi.mock.JSDebitCardImport;
import com.inov8.integration.channel.JSDebitCardApi.request.CardReissuanceRequest;
import com.inov8.integration.channel.JSDebitCardApi.request.GetCvvRequest;
import com.inov8.integration.channel.JSDebitCardApi.request.ImportCardRequest;
import com.inov8.integration.channel.JSDebitCardApi.request.UpdateCardStatusRequest;
import com.inov8.integration.channel.JSDebitCardApi.response.CardReissuanceResponse;
import com.inov8.integration.channel.JSDebitCardApi.response.GetCvvResponse;
import com.inov8.integration.client.REST.RESTClient;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public class JSDebitCardApiService {

    private static Logger logger = LoggerFactory.getLogger(JSDebitCardApiService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    RESTClient restClient;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${jsdebitcardapi.accessToken}")
    private String accessToken;
    @Value("${jsdebitcardapi.cardReissuance.accessToken}")
    private String cardReissuanceAccessToken;
    @Value("${jsdebitcardapi.cardReIssuance.api.url}")
    private String cardReIssuance;
    @Value("${getcvv.url}")
    private String getCVV;
    @Value("${updateCardStatus.url}")
    private String updateCardStatus;
    @Value("${jsdebitcardapi.importCustomer.url}")
    private String importCustomerUrl;
    @Value("${jsdebitcardapi.importAccount.url}")
    private String importAccountUrl;
    @Value("${jsdebitcardapi.importCard.url}")
    private String importCardUrl;
    @Value("${updatedCardStatus.accessToken}")
    private String updateCardStatus_accessToke;

    public CardReissuanceResponse sendCardReissuanceRequest(CardReissuanceRequest request) {
        CardReissuanceResponse cardReissuanceResponse = new CardReissuanceResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Import Card Request Sent to RDV : " + requesJson);
            String response = jsDebitCardImport.cardReIssuance();
            cardReissuanceResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(response, CardReissuanceResponse.class);
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(cardReIssuance);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", cardReissuanceAccessToken);
            String requesJson = JSONUtil.getJSON(request);

            logger.info("Card Re-Issuance Request Sent to RDV : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
            logger.info("Card Re-Issuance Response Received from RDV : " + res);
            cardReissuanceResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(res, CardReissuanceResponse.class);
        }

        return cardReissuanceResponse;
    }


    public GetCvvResponse sendCardDetail(GetCvvRequest request) {
        GetCvvResponse getCvvResponse = new GetCvvResponse();
        CardReissuanceResponse updateCardStatusResponse = new CardReissuanceResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Import Card Request Sent to RDV : " + requesJson);
            String response = jsDebitCardImport.getCVV();
           getCvvResponse=(GetCvvResponse) JSONUtil.jsonToObject(response, GetCvvResponse.class);
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(getCVV);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);
            String requesJson = JSONUtil.getJSON(request);

            logger.info("Get Cvv Request Sent to RDV : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            String res = restTemplate.postForObject(uri.build().toUri(), httpEntity, String.class);
            logger.info("Get Cvv Response Received from RDV : " + res);
            getCvvResponse = (GetCvvResponse) JSONUtil.jsonToObject(res, GetCvvResponse.class);
        }

        return getCvvResponse;
    }


    public CardReissuanceResponse sendUpdateCardStatusRequest(UpdateCardStatusRequest request) {
        CardReissuanceResponse updateCardStatusResponse = new CardReissuanceResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
            String response = jsDebitCardImport.updateCardStatus();
            updateCardStatusResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(response, CardReissuanceResponse.class);
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(updateCardStatus);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", updateCardStatus_accessToke);
            String requesJson = JSONUtil.getJSON(request);

            logger.info("Update Card Status Request Sent to RDV : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

            logger.info("Update Card Status Response Received from RDV : " + res1.getBody());
            updateCardStatusResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(res1.getBody(), CardReissuanceResponse.class);
        }

        return updateCardStatusResponse;
    }

//    public CardReissuanceResponse sendImportCustomerRequest(ImportCustomerRequest request) {
//        CardReissuanceResponse importCustomerResponse = new CardReissuanceResponse();
//
//        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
//            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
//            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
//            String response = jsDebitCardImport.importCustomerResponse();
//            importCustomerResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(response, CardReissuanceResponse.class);
//        } else {
//            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(importCustomerUrl);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("Access_token", accessToken);
//            String requesJson = JSONUtil.getJSON(request);
//
//            logger.info("Import Customer Request Sent to RDV : " + requesJson);
//            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);
//
//            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
//                if (converter instanceof StringHttpMessageConverter) {
//                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
//                }
//            }
//            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
//
//            logger.info("Import Customer Response Received from RDV : " + res1.getBody());
//            importCustomerResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(res1.getBody(), CardReissuanceResponse.class);
//        }
//
//        return importCustomerResponse;
//    }
//
//    public CardReissuanceResponse sendImportAccountRequest(ImportAccountRequest request) {
//        CardReissuanceResponse importAccountResponse = new CardReissuanceResponse();
//
//        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
//            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
//            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
//            String response = jsDebitCardImport.importAccountResponse();
//            importAccountResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(response, CardReissuanceResponse.class);
//        } else {
//            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(importAccountUrl);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("Access_token", accessToken);
//            String requesJson = JSONUtil.getJSON(request);
//
//            logger.info("Import Account Request Sent to RDV : " + requesJson);
//            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);
//
//            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
//                if (converter instanceof StringHttpMessageConverter) {
//                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
//                }
//            }
//            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
//
//            logger.info("Import Account Response Received from RDV : " + res1.getBody());
//            importAccountResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(res1.getBody(), CardReissuanceResponse.class);
//        }
//
//        return importAccountResponse;
//    }

    public CardReissuanceResponse sendImportCardRequest(ImportCardRequest request) {
        CardReissuanceResponse importCardResponse = new CardReissuanceResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Import Card Request Sent to RDV : " + requesJson);

            String response = jsDebitCardImport.importCardResponse();
            importCardResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(response, CardReissuanceResponse.class);
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(importCardUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);
            String requesJson = JSONUtil.getJSON(request);

            logger.info("Import Card Request Sent to RDV : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

            logger.info("Import Card Response Received from RDV : " + res1.getBody());
            importCardResponse = (CardReissuanceResponse) JSONUtil.jsonToObject(res1.getBody(), CardReissuanceResponse.class);
        }

        return importCardResponse;
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
