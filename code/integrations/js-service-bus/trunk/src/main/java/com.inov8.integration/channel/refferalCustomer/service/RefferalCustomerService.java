package com.inov8.integration.channel.refferalCustomer.service;

import com.inov8.integration.channel.T24Api.service.T24ApiMockService;
import com.inov8.integration.channel.offlineBiller.response.BillInquiryResponse;
import com.inov8.integration.channel.offlineBiller.resquest.BillInquiryRequest;
import com.inov8.integration.channel.offlineBiller.service.OffLineBillerService;
import com.inov8.integration.channel.refferalCustomer.request.RefferalCustomerRequest;
import com.inov8.integration.channel.refferalCustomer.response.RefferalCustomerResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RefferalCustomerService {

    private static Logger logger = LoggerFactory.getLogger(RefferalCustomerService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${refferal.customer.url}")
    private String refferalCustomer;

    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public RefferalCustomerResponse refferalCustomerResponse(RefferalCustomerRequest request) throws Exception {

        RefferalCustomerResponse refferalCustomerResponse = new RefferalCustomerResponse();

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Request Send To Refferal Customer: " + requesJson);
            T24ApiMockService mock = new T24ApiMockService();

            String response = mock.transactionValidation();
            refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(response, RefferalCustomerResponse.class);

            //            logger.info("Response Code for Ibft Title Fetch Request : " + ibftTitleFetchResponse.getISOMessage().getResponseCode_039());
        } else {
            logger.info("Refferal Customer " + refferalCustomer);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(refferalCustomer);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requesJson = JSONUtil.getJSON(request);
            logger.info("Sending Refferal Customer Request : " + requesJson);
            HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

            for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
                if (converter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                }
            }
            try {

                ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(res.getBody(), RefferalCustomerResponse.class);
                logger.info("Refferal Customer Response Received from Server : " + res.getBody());
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    if (response.equals("204")) {
                        String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(result, RefferalCustomerResponse.class);
                    } else if (response.equals("405")) {
                        String resp = ((HttpStatusCodeException) e).getResponseBodyAsString();
                        refferalCustomerResponse = (RefferalCustomerResponse) JSONUtil.jsonToObject(resp, RefferalCustomerResponse.class);

                    }

                }
            }
        }
        return refferalCustomerResponse;
    }
}
