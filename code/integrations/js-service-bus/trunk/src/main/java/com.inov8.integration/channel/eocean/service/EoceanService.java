package com.inov8.integration.channel.eocean.service;

import com.inov8.integration.channel.JSDebitCardApi.service.JSDebitCardApiService;
import com.inov8.integration.channel.eocean.mock.EoceanMock;
import com.inov8.integration.channel.eocean.request.EoceanRequest;
import com.inov8.integration.channel.eocean.response.Messages;
import com.inov8.integration.client.REST.RESTClient;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public class EoceanService {

    private static Logger logger = LoggerFactory.getLogger(JSDebitCardApiService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    RESTClient restClient;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${eocean_api_url}")
    private String eocean_api_url;

    public Messages eoceanResponse(EoceanRequest request) {
        Messages messages = new Messages();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            EoceanMock eoceanMock = new EoceanMock();
            String response = eoceanMock.eoceanResp();
            messages = (Messages) XMLUtil.converXMLtoObj(response, messages);
        } else {

            HttpHeaders headers = new HttpHeaders();
            String requestJson = JSONUtil.getJSON(request);
            logger.info("Eocean SMS Request : " + requestJson);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(eocean_api_url)
                    .queryParam("username", request.getUsername())
                    .queryParam("password", request.getPassword())
                    .queryParam("message", request.getMessage())
                    .queryParam("receiver", request.getReceiver())
                    .queryParam("network", request.getNetwork());


            String url = builder.toUriString();
            logger.info("Requesting URL" + " " + url);
            HttpEntity httpEntity = new HttpEntity(headers);
            logger.info("EOCEAN SMS Request : " + httpEntity);
            HttpEntity<String> resp = getRestTemplate().postForEntity(url, httpEntity, String.class);
            messages = (Messages) XMLUtil.converXMLtoObj(resp.getBody(), messages);

            logger.info("Response Body:" + resp);
        }

        return messages;

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
