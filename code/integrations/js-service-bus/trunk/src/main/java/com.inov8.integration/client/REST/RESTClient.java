package com.inov8.integration.client.REST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by inov8 on 6/1/2018.
 */
public class RESTClient {

    private static Logger logger = LoggerFactory.getLogger(RESTClient.class);

    public Object sendRequest(String URL, Object requestObject, Object responeObject) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(URL);
        return restTemplate.postForObject(uri.build().toUri(), requestObject, responeObject.getClass());
    }
}
