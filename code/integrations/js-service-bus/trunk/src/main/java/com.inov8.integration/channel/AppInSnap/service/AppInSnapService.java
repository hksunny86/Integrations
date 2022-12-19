package com.inov8.integration.channel.AppInSnap.service;


import com.inov8.integration.channel.AppInSnap.request.CustomerDataSet.CustomerTransactionDataSetRequest;
import com.inov8.integration.channel.AppInSnap.request.LoanManagement.LoanRequest;
import com.inov8.integration.channel.AppInSnap.response.CustomerDataSet.CustomerTransactioDataSetResponse;
import com.inov8.integration.channel.AppInSnap.response.LoanManagement.LoanResponse;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
public class AppInSnapService {
    private static Logger logger = LoggerFactory.getLogger(AppInSnapService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${appinsnap.CutomerDataSet.api.url:#{null}}")
    private String appInSnapUrl;

    @Value("${appinsnap.LoanManagement.api.url:#{null}}")
    private String appInSnapLoanURL;

    public CustomerTransactioDataSetResponse sendCustomerDataSetRequest(CustomerTransactionDataSetRequest customerTransactionDataSetRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to AppInSnap server for RRN: " + rrn);
        CustomerTransactioDataSetResponse customerTransactioDataSetResponse = new CustomerTransactioDataSetResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(appInSnapUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String request = JSONUtil.getJSON(customerTransactionDataSetRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        customerTransactioDataSetResponse = (CustomerTransactioDataSetResponse) JSONUtil.jsonToObject(res.getBody(), CustomerTransactioDataSetResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("CustomerDataSet request processed in:" + difference + "millisecond");
        return customerTransactioDataSetResponse;
    }

    public LoanResponse sendLoanManagement(LoanRequest customerTransactionDataSetRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to AppInSnap server for RRN: " + rrn);
        LoanResponse loanResponse = new LoanResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(appInSnapLoanURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String request = JSONUtil.getJSON(customerTransactionDataSetRequest);
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        loanResponse = (LoanResponse) JSONUtil.jsonToObject(res.getBody(), LoanResponse.class);
        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("Loan Management request processed in:" + difference + "millisecond");
        return loanResponse;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
