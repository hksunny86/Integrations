package com.inov8.integration.channel.vision.service;

import com.inov8.integration.channel.JSDebitCardApi.mock.JSDebitCardImport;
import com.inov8.integration.channel.JSDebitCardApi.response.CardReissuanceResponse;
import com.inov8.integration.channel.JSDebitCardApi.service.JSDebitCardApiService;
import com.inov8.integration.channel.vision.mock.VisionDebitCardMock;
import com.inov8.integration.channel.vision.request.VisionDebitCardRequest;
import com.inov8.integration.channel.vision.response.VisionDebitCardResponse;
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
import org.springframework.http.HttpMethod;
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
public class VisionDebitCardService {

    private static Logger logger = LoggerFactory.getLogger(VisionDebitCardService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    RESTClient restClient;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${visiondebitcardapi.accessToken}")
    private String accessToken;
    @Value("${visiondebitcatrdapi.debitCardInquiry.api.url}")
    private String visionDebitCardInquiry;

    public VisionDebitCardResponse visionDebitCardResponse (VisionDebitCardRequest visionDebitCardRequest){
        VisionDebitCardResponse visionDebitCardResponse = new VisionDebitCardResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment !=null && this.i8sb_target_environment.equalsIgnoreCase("mock1")){
            logger.info("Preparing request for Request Type : "+i8SBSwitchControllerRequestVO.getRequestType());
            VisionDebitCardMock visionDebitCardMock = new VisionDebitCardMock();
            String response = visionDebitCardMock.VisionMock();
            logger.info("Mock Response : "+response);
            visionDebitCardResponse = (VisionDebitCardResponse) JSONUtil.jsonToObject(response, VisionDebitCardResponse.class);
        }
        else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(visionDebitCardInquiry);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", accessToken);
            String requesJson = JSONUtil.getJSON(visionDebitCardRequest);

            logger.info("Vision Debit Card Inquiry Request Sent  : "+ requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            HttpEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);

            logger.info("Vision Debit Card Inquiry Response Received : "+ res1.getBody());
            visionDebitCardResponse = (VisionDebitCardResponse) JSONUtil.jsonToObject(res1.getBody(), VisionDebitCardResponse.class);
        }
        return  visionDebitCardResponse;
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
