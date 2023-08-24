package com.inov8.integration.channel.debitCard.service;

import com.inov8.integration.channel.debitCard.mock.DebitCardDiscrepancyStatusMock;
import com.inov8.integration.channel.debitCard.pdu.request.DebitCardDiscrepancyStatusRequest;
import com.inov8.integration.channel.debitCard.pdu.response.DebitCardDiscrepancyStatusResponse;
import com.inov8.integration.channel.tasdeeq.mock.TasdeeqMock;
import com.inov8.integration.channel.tasdeeq.response.AuthenticateUpdatedResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Objects;

@Service
public class DebitCardService {

    private static Logger logger = LoggerFactory.getLogger(DebitCardService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String discrepancyStatusUrl = PropertyReader.getProperty("debitCard.discrepancyStatus");
    private String accessToken = PropertyReader.getProperty("debitCard.accessToken");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public DebitCardDiscrepancyStatusResponse debitCardDiscrepancyStatusResponse(DebitCardDiscrepancyStatusRequest debitCardDiscrepancyStatusRequest) {

        DebitCardDiscrepancyStatusResponse debitCardDiscrepancyStatusResponse = new DebitCardDiscrepancyStatusResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            DebitCardDiscrepancyStatusMock debitCardDiscrepancyStatusMock = new DebitCardDiscrepancyStatusMock();
            String response = debitCardDiscrepancyStatusMock.debitCardDiscrepancyStatus();
            debitCardDiscrepancyStatusResponse = (DebitCardDiscrepancyStatusResponse) JSONUtil.jsonToObject(response, AuthenticateUpdatedResponse.class);
            logger.info("Response Code for Debit Card  Discrepancy Status Request: " + Objects.requireNonNull(debitCardDiscrepancyStatusResponse).getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.discrepancyStatusUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("AccessToken ", accessToken);
            String requestJSON = JSONUtil.getJSON(debitCardDiscrepancyStatusRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            logger.info("Prepared Request HttpEntity " + httpEntity);
            String response;
            try {
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Sending Debit Card  Discrepancy Status Request Sent to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Debit Card  Discrepancy Status received from client " + res1.getStatusCode().toString());
                logger.info("Response of Debit Card  Discrepancy Status received from client " + res1.getBody());
                String responseCode = String.valueOf(res1.getStatusCode().value());
                if (responseCode.equalsIgnoreCase("200")) {
                    debitCardDiscrepancyStatusResponse = (DebitCardDiscrepancyStatusResponse) JSONUtil.jsonToObject(res1.getBody(), DebitCardDiscrepancyStatusResponse.class);
                    Objects.requireNonNull(debitCardDiscrepancyStatusResponse).setResponseCode(responseCode);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            Objects.requireNonNull(debitCardDiscrepancyStatusResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            debitCardDiscrepancyStatusResponse.setResponseMessage((e).getMessage());
                            debitCardDiscrepancyStatusResponse = (DebitCardDiscrepancyStatusResponse) JSONUtil.jsonToObject(result, DebitCardDiscrepancyStatusResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(debitCardDiscrepancyStatusResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            debitCardDiscrepancyStatusResponse.setResponseMessage(result);
                            debitCardDiscrepancyStatusResponse = (DebitCardDiscrepancyStatusResponse) JSONUtil.jsonToObject(result, DebitCardDiscrepancyStatusResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(debitCardDiscrepancyStatusResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Debit Card  Discrepancy Status Request processed in: " + difference + " millisecond");
        }
        return debitCardDiscrepancyStatusResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
