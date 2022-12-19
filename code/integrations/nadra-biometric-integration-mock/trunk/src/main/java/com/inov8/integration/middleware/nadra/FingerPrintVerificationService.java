package com.inov8.integration.middleware.nadra;

import com.inov8.integration.middleware.mock.model.JDBCUser;
import com.inov8.integration.middleware.nadra.pdu.BiometricVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.Serializable;
@Service
public class FingerPrintVerificationService implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(FingerPrintVerificationService.class);
    @Autowired
    private RestTemplate restTemplate;
    private String BASE_URL = "http://172.29.12.237:53423/fingerprintvarification";

    private String API_KEY = "";
    public String verifyFingerPrint(BiometricVerification nadraRequest, JDBCUser jdbcUser)
    {

        UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromUriString(BASE_URL);
        JasonFields jasonFields=new JasonFields();
        jasonFields.setImageBase(nadraRequest.getRequestData().getFingerTemplate());
        jasonFields.setCNIC(jdbcUser.getCNIC());
        jasonFields.setName(jdbcUser.getUsername());
        String response = restTemplate.postForObject(BASE_URL,jasonFields,String.class);
        return response;
    }

}
