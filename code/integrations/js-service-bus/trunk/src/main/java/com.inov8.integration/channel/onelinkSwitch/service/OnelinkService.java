package com.inov8.integration.channel.onelinkSwitch.service;

import com.inov8.integration.channel.esb.request.EsbBillInquiryRequest;
import com.inov8.integration.channel.esb.request.EsbBillPaymentRequest;
import com.inov8.integration.channel.esb.response.EsbBillInquiryResponse;
import com.inov8.integration.channel.esb.response.EsbBillPaymentResponse;
import com.inov8.integration.channel.onelinkSwitch.request.IbftAdviceRequest;
import com.inov8.integration.channel.onelinkSwitch.request.IbftTitleFetchRequest;
import com.inov8.integration.channel.onelinkSwitch.response.IbftAdviceResponse;
import com.inov8.integration.channel.onelinkSwitch.response.IbftTitleFetchResponse;
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
public class OnelinkService {

    private static Logger logger = LoggerFactory.getLogger(OnelinkService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String oneLinkIbftTitleFetchUrl = PropertyReader.getProperty("oneLink.ibft.title.fetch");
    private String oneLinkIbftAdvice = PropertyReader.getProperty("oneLink.ibft.advice");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public IbftTitleFetchResponse oneLinkIbftTitleFetchResponse(IbftTitleFetchRequest ibftTitleFetchRequest) {

        IbftTitleFetchResponse ibftTitleFetchResponse = new IbftTitleFetchResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"pan\": \"\",\n" +
                    "    \"cardAcceptorNameAndLocation\": \"\",\n" +
                    "    \"cardAcceptorTerminalId\": \"\",\n" +
                    "    \"identifier\": \"\",\n" +
                    "    \"transactionAmount\": \"100\",\n" +
                    "    \"requestTime\": \"20230915201527\",\n" +
                    "    \"transmissionTime\": \"20230915201527\",\n" +
                    "    \"stan\": \"20230915201527\",\n" +
                    "    \"rrn\": \"\",\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"accountTitle\": \"AHSAN ALI\",\n" +
                    "    \"pointOfEntry\": \"\",\n" +
                    "    \"accountNo1\": \"\",\n" +
                    "    \"accountNo2\": \"\",\n" +
                    "    \"purposeOfPayment\": \"\",\n" +
                    "    \"toBankImd\": \"\",\n" +
                    "    \"merchantType\": \"0088\",\n" +
                    "    \"networkIdentifier\": \"\",\n" +
                    "    \"message\": \"Success\",\n" +
                    "    \"dateLocalTransaction\": \"20230915201527\",\n" +
                    "    \"timeLocalTransaction\": \"20230915201527\",\n" +
                    "    \"accountBranchName\": \"DHA Branch\",\n" +
                    "    \"accountBankName\": \"UBL\",\n" +
                    "    \"beneficiaryIban\": \"PK07UBL0021009960214440\",\n" +
                    "    \"beneficiaryId\": \"PK07UBL\"\n" +
                    "}";
            ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(response, IbftTitleFetchResponse.class);
            Objects.requireNonNull(ibftTitleFetchResponse).setResponseCode("00");
            logger.info("Response Code for One Link IBFT Title Fetch Request: " + ibftTitleFetchResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.oneLinkIbftTitleFetchUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(ibftTitleFetchRequest);
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
                logger.info("One Link IBFT Title Fetch Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of One Link IBFT Title Fetch Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of One Link IBFT Title Fetch Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(res1.getBody(), IbftTitleFetchResponse.class);
                }
//                Objects.requireNonNull(ibftTitleFetchResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(ibftTitleFetchResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(result, IbftTitleFetchResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(ibftTitleFetchResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(result, IbftTitleFetchResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(ibftTitleFetchResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("One Link IBFT Title Fetch Request processed in: " + difference + " millisecond");
        }
        return ibftTitleFetchResponse;
    }

    public IbftAdviceResponse oneLinkIbftAdviceResponse(IbftAdviceRequest ibftTitleFetchRequest) {

        IbftAdviceResponse ibftAdviceResponse = new IbftAdviceResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"rrn\": \"20230915201527\",\n" +
                    "    \"transmissionTime\": \"20230915201527\",\n" +
                    "    \"transDateTime\": \"20230915201527\",\n" +
                    "    \"responseCode\": \"00\",\n" +
                    "    \"stan\": \"20230915201527\",\n" +
                    "    \"transactionAmount\": \"100\",\n" +
                    "    \"accountTitle\": \"AHSAN RAZA\",\n" +
                    "    \"accountNo1\": \"\",\n" +
                    "    \"accountNo2\": \"\",\n" +
                    "    \"cardAcceptorIdentificationCode\": \"000000\",\n" +
                    "    \"cardAcceptorTerminalId\": \"00000000\",\n" +
                    "    \"purposeOfPayment\": \"BILL\",\n" +
                    "    \"senderName\": \"\",\n" +
                    "    \"accountBankName\": \"UBL\",\n" +
                    "    \"accountBranchName\": \"DHA Branch\",\n" +
                    "    \"toBankImd\": \"\",\n" +
                    "    \"cardAcceptorNameAndLocation\": \"JSBL Branchless Banking Channel Pakistan\",\n" +
                    "    \"authIdResp\": \"\",\n" +
                    "    \"networkIdentifier\": \"\",\n" +
                    "    \"merchantType\": \"0088\",\n" +
                    "    \"senderId\": \"\",\n" +
                    "    \"receiverId\": \"\",\n" +
                    "    \"pan\": \"\",\n" +
                    "    \"message\": \"Success\",\n" +
                    "    \"dateLocalTransaction\": \"20230915201527\",\n" +
                    "    \"timeLocalTransaction\": \"20230915201527\",\n" +
                    "    \"identifier\": \"RDV\"\n" +
                    "}";
            ibftAdviceResponse = (IbftAdviceResponse) JSONUtil.jsonToObject(response, IbftAdviceResponse.class);
            Objects.requireNonNull(ibftAdviceResponse).setResponseCode("00");
            logger.info("Response Code for One Link IBFT Advice Request: " + ibftAdviceResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.oneLinkIbftAdvice);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(ibftTitleFetchRequest);
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
                logger.info("One Link IBFT Advice Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of One Link IBFT Advice Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of One Link IBFT Advice Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    ibftAdviceResponse = (IbftAdviceResponse) JSONUtil.jsonToObject(res1.getBody(), IbftAdviceResponse.class);
                }
//                Objects.requireNonNull(ibftAdviceResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(ibftAdviceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            ibftAdviceResponse = (IbftAdviceResponse) JSONUtil.jsonToObject(result, IbftAdviceResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(ibftAdviceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            ibftAdviceResponse = (IbftAdviceResponse) JSONUtil.jsonToObject(result, IbftAdviceResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(ibftAdviceResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("One Link IBFT Advice Request processed in: " + difference + " millisecond");
        }
        return ibftAdviceResponse;
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
