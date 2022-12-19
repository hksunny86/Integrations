package com.inov8.integration.channel.AMA.service;

import com.inov8.integration.channel.AMA.request.UpdateAmaRequest;
import com.inov8.integration.channel.AMA.response.UpdateAmaResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AmaService {

    private static Logger logger = LoggerFactory.getLogger(AmaService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String updateAmaUrl = PropertyReader.getProperty("Update.AMA.url");
    private String accessToken = PropertyReader.getProperty("update.AMA.access_token");


    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

    public UpdateAmaResponse updateAmaResponse(UpdateAmaRequest request) throws Exception {

        UpdateAmaResponse updateAmaResponse = new UpdateAmaResponse();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(updateAmaUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Access_token", accessToken);
        String requesJson = JSONUtil.getJSON(request);
        logger.info("Sending Update AMA Request : " + requesJson);
        HttpEntity<?> httpEntity = new HttpEntity<>(requesJson, headers);

        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }

        ResponseEntity<String> res = restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
        updateAmaResponse = (UpdateAmaResponse) JSONUtil.jsonToObject(res.getBody(), UpdateAmaResponse.class);


        return updateAmaResponse;
    }

}
