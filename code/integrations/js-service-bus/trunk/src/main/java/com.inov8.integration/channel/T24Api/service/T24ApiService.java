package com.inov8.integration.channel.T24Api.service;

import com.inov8.integration.channel.JSBookMe.service.JSBookMeService;
import com.inov8.integration.channel.T24Api.request.IbftRequest;
import com.inov8.integration.channel.T24Api.request.IbftTitleFetchRequest;
import com.inov8.integration.channel.T24Api.response.IbftResponse;
import com.inov8.integration.channel.T24Api.response.IbftTitleFetchResponse;
import com.inov8.integration.channel.zindigi.mock.ZindigiCustomerSyncMock;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public class T24ApiService {

    private static Logger logger = LoggerFactory.getLogger(JSBookMeService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${t24.ibfttitlefetch.url}")
    private String t24IbftTitleFetchUrl;
    @Value("${t24.ibft.url}")
    private String t24IbftUrl;
    @Value("${t24.IBFTTITLEFETCH.access_token}")
    private String ibftTitleFetchAccessToken;
    @Value("${t24.IBFT.access_token}")
    private String ibftAccessToken;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");


    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public IbftTitleFetchResponse ibftTitleFetchResponse(IbftTitleFetchRequest request) throws Exception {

        IbftTitleFetchResponse ibftTitleFetchResponse = new IbftTitleFetchResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To RDV : " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.ibftTitleFetch();

            ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(response, IbftTitleFetchResponse.class);
            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(t24IbftTitleFetchUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", ibftTitleFetchAccessToken);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Ibft Title Fetch Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
            ibftTitleFetchResponse = (IbftTitleFetchResponse) JSONUtil.jsonToObject(res.getBody(), IbftTitleFetchResponse.class);

        }
        return ibftTitleFetchResponse;
    }


    public IbftResponse ibftResponse(IbftRequest request) throws Exception {

        IbftResponse ibftResponse = new IbftResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To RDV : " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();
            String response = mock.ibft();

            ibftResponse = (IbftResponse) JSONUtil.jsonToObject(response, IbftResponse.class);
            logger.info("Response Code for Ibft Request : " + ibftResponse.getISOMessage().getResponseCode_039());
        } else {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(t24IbftUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", ibftAccessToken);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Ibft Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }

            ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
            ibftResponse = (IbftResponse) JSONUtil.jsonToObject(res.getBody(), IbftResponse.class);

        }
        return ibftResponse;
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
