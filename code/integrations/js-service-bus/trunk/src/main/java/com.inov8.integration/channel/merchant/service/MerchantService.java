package com.inov8.integration.channel.merchant.service;

import com.inov8.integration.channel.merchant.request.StaticQrRequest;
import com.inov8.integration.channel.merchant.response.StaticQrResponse;
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
public class MerchantService {

    private static Logger logger = LoggerFactory.getLogger(MerchantService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String staticQrUrl = PropertyReader.getProperty("merchant.qr.staticQR");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public StaticQrResponse staticQrResponse(StaticQrRequest staticQrRequest) {

        StaticQrResponse staticQrResponse = new StaticQrResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"staticQrResponses\": [\n" +
                    "        {\n" +
                    "            \"QR\": \"000201010211287600321d06bcb1a9b1487aa6c4962d6dbd622e0108JSBLPKKA0224PK27JSBL99999033479111775204581453035865802PK5912SudaisPharma6005Badin62520312SudaisPharma07099462411760819FastFoodRestaurants80230019FastFoodRestaurants63043E42\",\n" +
                    "            \"UUID\": \"300072123020603008123982753109060930\",\n" +
                    "            \"UETR\": \"7658c2b4-5aec-4945-96d7-2fa4c0fa8a56\",\n" +
                    "            \"Amount\": \"0\",\n" +
                    "            \"Expiry\": \"\",\n" +
                    "            \"CreatedDate\": \"07/08/2023 22:06:39\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            staticQrResponse = (StaticQrResponse) JSONUtil.jsonToObject(response, StaticQrResponse.class);
            Objects.requireNonNull(staticQrResponse).setResponseCode("200");
            logger.info("Response Code for Static QR Request: " + staticQrResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.staticQrUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJSON = JSONUtil.getJSON(staticQrRequest);
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
                logger.info("Static QR Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Static QR Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Static QR Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    staticQrResponse = (StaticQrResponse) JSONUtil.jsonToObject(res1.getBody(), StaticQrResponse.class);
                    Objects.requireNonNull(staticQrResponse).setResponseCode(responseCode);
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
                            Objects.requireNonNull(staticQrResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            staticQrResponse = (StaticQrResponse) JSONUtil.jsonToObject(result, StaticQrResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            staticQrResponse = (StaticQrResponse) JSONUtil.jsonToObject(result, StaticQrResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(staticQrResponse).setResponseCode("500");
                    Objects.requireNonNull(staticQrResponse).setDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Static QR Request processed in: " + difference + " millisecond");
        }
        return staticQrResponse;
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
