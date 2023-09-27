package com.inov8.integration.channel.saf.service;

import com.inov8.integration.channel.saf.request.RetryIbftAdviceRequest;
import com.inov8.integration.channel.saf.response.RetryIbftAdviceResponse;
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
public class SafService {

    private static Logger logger = LoggerFactory.getLogger(SafService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String safRetryIbftAdvice = PropertyReader.getProperty("saf.retryIbftAdvice");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public RetryIbftAdviceResponse retryIbftAdviceResponse(RetryIbftAdviceRequest retryIbftAdviceRequest) {

        RetryIbftAdviceResponse retryIbftAdviceResponse = new RetryIbftAdviceResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"responseCode\":\"1\",\n" +
                    "    \"messages\": \"Transaction Successful\",\n" +
                    "    \"data\": {\n" +
                    "        \"saf1linkIntegrationId\": null,\n" +
                    "        \"accountbankname\": \"UBL\",\n" +
                    "        \"accountbranchname\": \"DHA Phase 2\",\n" +
                    "        \"accountno1\": \"03004568987\",\n" +
                    "        \"accountno2\": \"03054512368\",\n" +
                    "        \"accounttitle\": \"YAMEEN BUTT\",\n" +
                    "        \"authidresp\": null,\n" +
                    "        \"beneficiaryid\": \"PK14UBL0003004568987\",\n" +
                    "        \"cardacceptoridentificationcode\": null,\n" +
                    "        \"cardacceptornameandlocation\": null,\n" +
                    "        \"cardacceptorterminalid\": null,\n" +
                    "        \"createdate\": null,\n" +
                    "        \"createuser\": null,\n" +
                    "        \"currencycode\": \"PKR\",\n" +
                    "        \"lastupdatedate\": null,\n" +
                    "        \"lastupdateuser\": null,\n" +
                    "        \"merchanttype\": \"0088\",\n" +
                    "        \"networkidentifier\": null,\n" +
                    "        \"noOfRetries\": null,\n" +
                    "        \"pan\": \"03004568987\",\n" +
                    "        \"pointofentry\": null,\n" +
                    "        \"purposeofpayment\": null,\n" +
                    "        \"requestMessage\": null,\n" +
                    "        \"rrn\": \"202309271228\",\n" +
                    "        \"senderid\": null,\n" +
                    "        \"sendername\": null,\n" +
                    "        \"stan\": \"456123\",\n" +
                    "        \"status\": \"COMPLETED\",\n" +
                    "        \"statusdescr\": \"COMPLETED\",\n" +
                    "        \"tobankimd\": null,\n" +
                    "        \"transType\": \"\",\n" +
                    "        \"transactionamount\": \"100\",\n" +
                    "        \"transactiondatetime\": \"202309271228\",\n" +
                    "        \"updateindex\": null\n" +
                    "    }\n" +
                    "}";
            retryIbftAdviceResponse = (RetryIbftAdviceResponse) JSONUtil.jsonToObject(response, RetryIbftAdviceResponse.class);
            Objects.requireNonNull(retryIbftAdviceResponse).setResponseCode("1");
            logger.info("Response Code for SAF Retry IBFT Advice Request: " + retryIbftAdviceResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.safRetryIbftAdvice);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(retryIbftAdviceRequest);
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
                logger.info("SAF Retry IBFT Advice Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of SAF Retry IBFT Advice Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of SAF Retry IBFT Advice Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    retryIbftAdviceResponse = (RetryIbftAdviceResponse) JSONUtil.jsonToObject(res1.getBody(), RetryIbftAdviceResponse.class);
                }
//                Objects.requireNonNull(retryIbftAdviceResponse).setResponseCode(res1.getStatusCode().toString());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(retryIbftAdviceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            retryIbftAdviceResponse = (RetryIbftAdviceResponse) JSONUtil.jsonToObject(result, RetryIbftAdviceResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(retryIbftAdviceResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            retryIbftAdviceResponse = (RetryIbftAdviceResponse) JSONUtil.jsonToObject(result, RetryIbftAdviceResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(retryIbftAdviceResponse).setResponseCode("500");
//                    validatePdmResponse = (validatePdmResponse) JSONUtil.jsonToObject(result, validatePdmResponse.class);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("SAF Retry IBFT Advice Request processed in: " + difference + " millisecond");
        }
        return retryIbftAdviceResponse;
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
}
