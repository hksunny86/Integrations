package com.inov8.integration.channel.JSBookMe.service;

import com.inov8.integration.channel.AppInSnap.request.CustomerDataSet.CustomerTransactionDataSetRequest;
import com.inov8.integration.channel.AppInSnap.response.CustomerDataSet.CustomerTransactioDataSetResponse;
import com.inov8.integration.channel.AppInSnap.service.AppInSnapService;
import com.inov8.integration.channel.JSBookMe.request.JSBookMeRequest;
import com.inov8.integration.channel.JSBookMe.response.JSBBookMeResponse;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Iterator;

@Service
public class JSBookMeService {
    private static Logger logger = LoggerFactory.getLogger(JSBookMeService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${jsbookme.endpoint}")
    private String jsbookmeUrl;

    @Value("${jsbookme.AUTH_HEADER}")
    private String authHeader;

    public JSBBookMeResponse sendJSBookMeRequest(JSBookMeRequest jsBookMeRequest) throws Exception {
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to JSBOOKME server for RRN: " + rrn);
        JSBBookMeResponse jsbBookMeResponse = new JSBBookMeResponse();
        long start = System.currentTimeMillis();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(jsbookmeUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String request = JSONUtil.getJSON(jsBookMeRequest);
        logger.info("Request :"+request);

        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        Iterator res = this.restTemplate.getMessageConverters().iterator();

        while (res.hasNext()) {
            HttpMessageConverter endTime = (HttpMessageConverter) res.next();
            if (endTime instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
            }
        }

        try {
             ResponseEntity<String> res1 = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
            if (res1.getStatusCode().toString().equals("200")){
                String data = res1.getBody();
                jsbBookMeResponse.setData(data);
                jsbBookMeResponse.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
            }
        }
        catch (RestClientException e){
            if (e instanceof HttpStatusCodeException){
                String response = ((HttpStatusCodeException)e).getStatusCode().toString();
                if (response.equals("422")){
                    String result = ((HttpStatusCodeException)e).getResponseBodyAsString();
                    jsbBookMeResponse = (JSBBookMeResponse) JSONUtil.jsonToObject(result,JSBBookMeResponse.class);
                jsbBookMeResponse.setResponseCode(((HttpStatusCodeException)e).getStatusCode().toString());
                }
                else if (response.equals("500")){
                    String resp = ((HttpStatusCodeException)e).getResponseBodyAsString();
                    jsbBookMeResponse.setData(resp);
                    jsbBookMeResponse.setResponseCode(((HttpStatusCodeException)e).getStatusCode().toString());
                }
            }
        }

        long endTime = new Date().getTime();
        long difference = endTime - start;
        logger.debug("JSBookMe request processed in:" + difference + "millisecond");


        return jsbBookMeResponse;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
