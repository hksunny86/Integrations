package com.inov8.integration.channel.zindigi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.channel.zindigi.mock.ZindigiCustomerSyncMock;
import com.inov8.integration.channel.zindigi.request.*;
import com.inov8.integration.channel.zindigi.response.*;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class ZindigiCustomerSyncService {

    private static Logger logger = LoggerFactory.getLogger(ZindigiCustomerSyncService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");

    @Value("-${zindigi.customer.sync.url}")
    private String zindigiCustomerSyncUrl;
    @Value("${zindigi.customer.sync.access_token}")
    private String accessToken;
    @Value("${l2.account.upgrade.validation.url}")
    private String l2AccountUpgradeValidationUrl;
    @Value("${l2.merchant.account.upgrade.validation.url}")
    private String l2MerchantAccountUpgradeValidationUrl;
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
    private String transactionCaptureUrl = PropertyReader.getProperty("zindigi.transaction.capture");
    private String updateAccountStatusUrl = PropertyReader.getProperty("zindigi.UpdateAccountStatus.url");
    private String updateAccountStatusToken = PropertyReader.getProperty("zindigi.UpdateAccountStatus.AccessToken");


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
            UriComponentsBuilder uri;
            if (i8SBSwitchControllerRequestVO.getReserved1() != null && i8SBSwitchControllerRequestVO.getReserved1().equalsIgnoreCase("M")) {
                uri = UriComponentsBuilder.fromUriString(l2MerchantAccountUpgradeValidationUrl);
            } else {
                uri = UriComponentsBuilder.fromUriString(l2AccountUpgradeValidationUrl);
            }

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

    public TransactionCaptureResponse transactionCaptureResponse(TransactionCaptureRequest request) {
        TransactionCaptureResponse transactionCaptureResponse = new TransactionCaptureResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"responsecode\": \"01\",\n" +
                    "    \"data\": {\n" +
                    "        \"pointEarns\": 1,\n" +
                    "        \"isTierUpdated\": \"N\"\n" +
                    "    },\n" +
                    "    \"messages\": \"Point Save to Wallet Sucessfully\"\n" +
                    "}";
            transactionCaptureResponse = (TransactionCaptureResponse) JSONUtil.jsonToObject(response, TransactionCaptureResponse.class);
            Objects.requireNonNull(transactionCaptureResponse).setResponseCode("200");
            logger.info("Mock Response for Transaction Capture Request : " + transactionCaptureResponse.getResponseCode());
        } else {
            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.transactionCaptureUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(request);
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
                logger.info("Transaction Capture Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Transaction Capture Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Transaction Capture Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    transactionCaptureResponse = (TransactionCaptureResponse) JSONUtil.jsonToObject(res1.getBody(), TransactionCaptureResponse.class);
                    Objects.requireNonNull(transactionCaptureResponse).setResponseCode(transactionCaptureResponse.getResponseCode());
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
                            Objects.requireNonNull(transactionCaptureResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(transactionCaptureResponse).setMessages((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            transactionCaptureResponse = (TransactionCaptureResponse) JSONUtil.jsonToObject(result, TransactionCaptureResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(transactionCaptureResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(transactionCaptureResponse).setMessages((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            transactionCaptureResponse = (TransactionCaptureResponse) JSONUtil.jsonToObject(result, TransactionCaptureResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(transactionCaptureResponse).setResponseCode("500");
                    Objects.requireNonNull(transactionCaptureResponse).setMessages(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Transaction Capture Request processed in: " + difference + " millisecond");
        }

        return transactionCaptureResponse;
    }

    public UpdateAccountStatusResponse updateAccountStatusResponse(UpdateAccountStatusRequest request) {
        UpdateAccountStatusResponse updateAccountStatusResponse = new UpdateAccountStatusResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        long start = System.currentTimeMillis();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock5")) {
            // Mock response for testing
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"responseMessage\": \"SUCCESS\",\n" +
                    "    \"traceId\": \"2096ea70-3a3d-4ca2-87ba-102d327df310\",\n" +
                    "    \"body\": [\n" +
                    "        \"SUCCESS\"\n" +
                    "    ]\n" +
                    "}";
            updateAccountStatusResponse = (UpdateAccountStatusResponse) JSONUtil.jsonToObject(response, UpdateAccountStatusResponse.class);
            logger.info("Mock Response for Account Status Request : " + Objects.requireNonNull(updateAccountStatusResponse).getResponseCode());
        } else {
            String accessToken = i8SBSwitchControllerRequestVO.getAccessToken();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.updateAccountStatusUrl);
            String url = uri.toUriString();

            try {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("content-type", "application/json");
                headers.put("AccessToken", updateAccountStatusToken);
                Map<String, Object> postParam = new HashMap<String, Object>();
                postParam.put("Mobile", request.getMobile());
                postParam.put("Status", request.getStatus());
                String responseBody = getResponseFromPostAPI(headers, postParam, url);
                ObjectMapper objectMapper = new ObjectMapper();
                updateAccountStatusResponse = objectMapper.readValue(responseBody, UpdateAccountStatusResponse.class);
            } catch (RestClientException e) {
                handleRestClientException(e, updateAccountStatusResponse);
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long difference = endTime - start;
        logger.debug("Update Account Status Request processed in: " + difference + " millisecond");

        return updateAccountStatusResponse;
    }

    private void handleRestClientException(RestClientException e, UpdateAccountStatusResponse updateAccountStatusResponse) {
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
                    updateAccountStatusResponse = (UpdateAccountStatusResponse) JSONUtil.jsonToObject(responseBody, UpdateAccountStatusResponse.class);
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

    public String getResponseFromPostAPI(Map<String, String> headerMap, Map<String, Object> postParam, String url) throws Exception {
        try {
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(headerMap);

            // Create request entity
            HttpEntity<?> requestEntity = new HttpEntity<>(postParam, headers);

            logger.info("Sending POST request to " + url);
            logger.info("Request Entity: " + requestEntity);

            // Create RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Send POST request and get response
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // Log response details
            logger.info("Response: " + response);

            // Check if the status code is 200 (OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                String entityResponse = response.getBody();
                logger.info("Successful response: " + entityResponse);
                return !entityResponse.isEmpty() ? entityResponse : null;
            } else {
                logger.info("Unsuccessful response. HTTP Status Code: " + response.getStatusCodeValue());
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

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

}