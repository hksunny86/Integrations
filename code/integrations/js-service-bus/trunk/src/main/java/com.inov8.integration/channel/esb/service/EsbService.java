package com.inov8.integration.channel.esb.service;

import com.inov8.integration.channel.esb.request.EsbBillInquiryRequest;
import com.inov8.integration.channel.esb.request.EsbBillPaymentRequest;
import com.inov8.integration.channel.esb.response.EsbBillInquiryResponse;
import com.inov8.integration.channel.esb.response.EsbBillPaymentResponse;
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
public class EsbService {

    private static Logger logger = LoggerFactory.getLogger(EsbService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String esbBillInquiryUrl = PropertyReader.getProperty("esb.billInquiry");
    private String esbBillPaymentUrl = PropertyReader.getProperty("esb.billPayment");
    private String esbAccessToken = PropertyReader.getProperty("esb.access.token");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public EsbBillInquiryResponse esbBillInquiryResponse(EsbBillInquiryRequest esbBillInquiryRequest) {

        EsbBillInquiryResponse esbBillInquiryResponse = new EsbBillInquiryResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"processingCode\": \"RCDPbillInquiry\",\n" +
                    "    \"merchantType\": \"0088\",\n" +
                    "    \"traceNo\": \"500202\",\n" +
                    "    \"dateTime\": \"20230915201527\",\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"response\": {\n" +
                    "        \"Response_code\": \"00\",\n" +
                    "        \"Consumer_Detail\": \"Shasita Parveen \",\n" +
                    "        \"Bill_Status\": \"U\",\n" +
                    "        \"Due_Date\": \"20201018\",\n" +
                    "        \"Amount_Within_DueDate\": \"+0000000500000\",\n" +
                    "        \"Amount_After_DueDate\": \"+0000000500000\",\n" +
                    "        \"Billing_Month\": \"2010\",\n" +
                    "        \"Date_Paid\": \"      \",\n" +
                    "        \"Amount_Paid\": \" 5000\",\n" +
                    "        \"Tran_Auth_Id\": \"      \",\n" +
                    "        \"Reserved\": \"\"\n" +
                    "    }\n" +
                    "}";
            esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(response, EsbBillInquiryResponse.class);
            Objects.requireNonNull(esbBillInquiryResponse).setResponseCode("00");
            logger.info("Response Code for ESB Bill Inquiry Request: " + esbBillInquiryResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.esbBillInquiryUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", esbAccessToken);
            String requestJSON = JSONUtil.getJSON(esbBillInquiryRequest);
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
                logger.info("ESB Bill Inquiry Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of ESB Bill Inquiry Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of ESB Bill Inquiry Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(res1.getBody(), EsbBillInquiryResponse.class);
                }
//                Objects.requireNonNull(esbBillInquiryResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillInquiryResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(result, EsbBillInquiryResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillInquiryResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(result, EsbBillInquiryResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillInquiryResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(result, EsbBillInquiryResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                        Objects.requireNonNull(esbBillInquiryResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillInquiryResponse = (EsbBillInquiryResponse) JSONUtil.jsonToObject(result, EsbBillInquiryResponse.class);
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(esbBillInquiryResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("ESB Bill Inquiry Request processed in: " + difference + " millisecond");
        }
        return esbBillInquiryResponse;
    }

    public EsbBillPaymentResponse esbBillPaymentResponse(EsbBillPaymentRequest esbBillPaymentRequest) {

        EsbBillPaymentResponse esbBillPaymentResponse = new EsbBillPaymentResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"processingCode\": \"RCDPbillInquiry\",\n" +
                    "    \"merchantType\": \"0088\",\n" +
                    "    \"traceNo\": \"500202\",\n" +
                    "    \"dateTime\": \"20230915201527\",\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"response\": {\n" +
                    "        \"Response_code\": \"00\",\n" +
                    "        \"Identification_Parameter\": \"\",\n" +
                    "        \"Reserved\": \"\"\n" +
                    "    }\n" +
                    "}";
            esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(response, EsbBillPaymentResponse.class);
            Objects.requireNonNull(esbBillPaymentResponse).setResponseCode("00");
            logger.info("Response Code for ESB Bill Payment Request: " + esbBillPaymentResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.esbBillPaymentUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", esbAccessToken);
            String requestJSON = JSONUtil.getJSON(esbBillPaymentRequest);
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
                logger.info("ESB Bill Payment Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of ESB Bill Payment Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of ESB Bill Payment Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(res1.getBody(), EsbBillPaymentResponse.class);
                }
//                Objects.requireNonNull(esbBillPaymentResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    if (response.equals("400")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(result, EsbBillPaymentResponse.class);
                    } else if (response.equals("422")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(result, EsbBillPaymentResponse.class);
                    } else if (response.equals("500")) {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        Objects.requireNonNull(esbBillPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(result, EsbBillPaymentResponse.class);
                    } else {
                        result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                        Objects.requireNonNull(esbBillPaymentResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                        esbBillPaymentResponse = (EsbBillPaymentResponse) JSONUtil.jsonToObject(result, EsbBillPaymentResponse.class);
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(esbBillPaymentResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("ESB Bill Payment Request processed in: " + difference + " millisecond");
        }
        return esbBillPaymentResponse;
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
