package com.inov8.integration.channel.LoanIntimation.service;

import com.inov8.integration.channel.JSBookMe.response.JSBBookMeResponse;
import com.inov8.integration.channel.JSDebitCardApi.mock.JSDebitCardImport;
import com.inov8.integration.channel.JSDebitCardApi.request.ImportCardRequest;
import com.inov8.integration.channel.JSDebitCardApi.response.CardReissuanceResponse;
import com.inov8.integration.channel.LoanIntimation.request.LoanIntimationRequest;
import com.inov8.integration.channel.LoanIntimation.response.LoanIntimationResponse;
import com.inov8.integration.client.REST.RESTClient;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Service
public class LoanIntimationService {
    private static Logger logger = LoggerFactory.getLogger(LoanIntimationService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    RESTClient restClient;
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${loanIntimation.url}")
    private String loanIntimationUrl;
    @Value("${loanIntimation.access_token}")
    private String accessToken;

    public LoanIntimationResponse sendLoanIntimation(LoanIntimationRequest request) {
        LoanIntimationResponse loanIntimationResponse = new LoanIntimationResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            JSDebitCardImport jsDebitCardImport = new JSDebitCardImport();
            String response = jsDebitCardImport.importCardResponse();
            loanIntimationResponse = (LoanIntimationResponse) JSONUtil.jsonToObject(response, LoanIntimationResponse.class);
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(loanIntimationUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("AccessToken", accessToken);
            String requesJson = JSONUtil.getJSON(request);

            logger.info("Send Loan Intimation : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {
                ResponseEntity<String> res1 = getRestTemplate().postForEntity(uri.build().toUri(), httpEntity, String.class);
                if (res1.getStatusCode().toString().equals("200")){
                    loanIntimationResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                }
                else if (res1.getStatusCode().toString().equals("203")){
                    loanIntimationResponse = (LoanIntimationResponse) JSONUtil.jsonToObject(res1.getBody(),LoanIntimationResponse.class);
                    loanIntimationResponse.setResponseCode(res1.getStatusCode().toString());

                }
            }
                  catch (RestClientException e){
                    if (e instanceof HttpStatusCodeException){
                        String response = ((HttpStatusCodeException)e).getStatusCode().toString();
                        if (response.equals("422")){
                            String result = ((HttpStatusCodeException)e).getResponseBodyAsString();
                            loanIntimationResponse = (LoanIntimationResponse) JSONUtil.jsonToObject(result,LoanIntimationResponse.class);
                            loanIntimationResponse.setResponseCode(((HttpStatusCodeException)e).getStatusCode().toString());
                        }
                        else if (response.equals("500")){
                            String resp = ((HttpStatusCodeException)e).getResponseBodyAsString();
                            loanIntimationResponse = (LoanIntimationResponse) JSONUtil.jsonToObject(resp,LoanIntimationResponse.class);
                            loanIntimationResponse.setResponseCode(((HttpStatusCodeException)e).getStatusCode().toString());

                        }

                    }
                }

        }

        return loanIntimationResponse;
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
