package com.inov8.integration.middleware.service;

import com.inov8.integration.middleware.enums.RequestParamEnums;
import com.inov8.integration.middleware.pdu.request.BiometricVerificationRequest;
import com.inov8.integration.middleware.pdu.request.FingerPrintVerification;
import com.inov8.integration.middleware.pdu.request.OTCVerification;
import com.inov8.integration.middleware.pdu.request.OTCVerificationRequest;
import com.inov8.integration.middleware.pdu.response.FingerPrintVerificationResponse;
import com.inov8.integration.middleware.pdu.response.OTCResponse;
import com.inov8.integration.middleware.util.ConfigurationUtil;
import com.inov8.integration.middleware.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationService.class);

    private static final String BASE_URL = ConfigurationUtil.getValue("js.nadra.service.url");
    private static final String API_KEY = ConfigurationUtil.getValue("js.service.api.key");

    @Autowired
    private RestTemplate restTemplate;

    public OTCResponse otcVerification(OTCVerificationRequest verificationRequest) {

        logger.info("Enter in Service of OTC Verification");
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(BASE_URL).path(RequestParamEnums.OTC_OPERATION.getValue());
        OTCResponse otcResponse = new OTCResponse();

        OTCVerification otcVerification = new OTCVerification();
        OTCVerification.SDK sdkData = new OTCVerification.SDK();
        OTCVerification.Tags tagsData = new OTCVerification.Tags();

        OTCVerificationRequest.Forward forward = verificationRequest.getForward();
        OTCVerificationRequest.Forward.SDK mobileSDK = forward.getSdk();
        OTCVerificationRequest.Forward.Tags mobileTags = forward.getTags();
        OTCVerificationRequest.Data data = verificationRequest.getData();
        OTCVerificationRequest.Forward.META meta = verificationRequest.getForward().getMeta();


        otcVerification.setFinger(forward.getFinger());
        otcVerification.setSession(data.getSession());
        otcVerification.setIdentifier(forward.getIdentifier());
        otcVerification.setInstitution(forward.getInstitution());
        otcVerification.setWsq(forward.getWsq());
        otcVerification.setMeta(meta);


        sdkData.setApplication(mobileSDK.getApplication());
        sdkData.setDevice(mobileSDK.getDevice());
        sdkData.setVersion(mobileSDK.getVersion());
        sdkData.setApplicationVersion(mobileSDK.getApplicationVersion());
        sdkData.setManufacturer(mobileSDK.getManufacturer());
        sdkData.setModel(mobileSDK.getModel());

        tagsData.setRemittanceType(mobileTags.getRemittanceType());
        tagsData.setRemittanceAmount(mobileTags.getRemittanceAmount());
        tagsData.setAccountNumber(mobileTags.getAccountNumber());
        tagsData.setAreaName(mobileTags.getAreaName());
        tagsData.setContactNumber(mobileTags.getContactNumber());
        tagsData.setSecondaryContactNumber(mobileTags.getSecondaryContactNumber());
        tagsData.setSecondaryIdentifier(mobileTags.getSecondaryIdentifier());

        otcVerification.setSdk(sdkData);
        otcVerification.setTags(tagsData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(RequestParamEnums.SUBSCRIPTION_KEY.getValue(), API_KEY);

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);

        List<Charset> charsets = new ArrayList<Charset>();
        charsets.add(Charset.forName("UTF-8"));

        headers.setAcceptCharset(charsets);
        headers.setAccept(mediaTypes);

        try {

            String requestJson = JSONUtil.getJSON(otcVerification);
            HttpEntity<String> request = new HttpEntity<String>(requestJson, headers);

            logger.info("Request Header " + request.getHeaders());
            logger.info("Request Body " + request.getBody());
            logger.info("Request Forward to Server");
            ResponseEntity<String> response = restTemplate.postForEntity(uri.build().toUri(), request, String.class);

            logger.info("Response Object " + response);
            if (response != null) {
                if (response.getBody() != null) {

                    logger.info("Response Body " + response.getBody());
                    otcResponse = (OTCResponse) JSONUtil.jsonToObject(response.getBody(), OTCResponse.class);

                } else {
                    OTCResponse.Status status = new OTCResponse.Status();
                    status.setCode("404");
                    status.setMessage("Response Body is NUll");
                    otcResponse.setStatus(status);
                }
            } else {
                OTCResponse.Status status = new OTCResponse.Status();
                status.setCode("404");
                status.setMessage("Response Object is NULL");
                otcResponse.setStatus(status);
            }
        } catch (Exception e) {
            logger.error("General Exception Occurred ", e);
            OTCResponse.Status status = new OTCResponse.Status();
            status.setCode("505");
            status.setMessage("General Exception Occurred " + e.getMessage());
            otcResponse.setStatus(status);
        }

        logger.info("End: OTC Verification in Service ");
        return otcResponse;
    }

    public FingerPrintVerificationResponse fingerPrintVerification(BiometricVerificationRequest verificationRequest) {

        logger.info("Enter in Service of Finger Print Verification");
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(BASE_URL).path(RequestParamEnums.VERIFY_OPERATION.getValue());
        FingerPrintVerificationResponse verificationResponse = new FingerPrintVerificationResponse();

        FingerPrintVerification fingerPrintVerification = new FingerPrintVerification();

        BiometricVerificationRequest.Forward forward =  verificationRequest.getForward();
        BiometricVerificationRequest.Data data =  verificationRequest.getData();

        fingerPrintVerification.setFinger(forward.getFinger());
        fingerPrintVerification.setIdentifier(forward.getIdentifier());
        fingerPrintVerification.setInstitution(forward.getInstitution());
        fingerPrintVerification.setSession(data.getSession());
        fingerPrintVerification.setWsq(forward.getWsq());
        fingerPrintVerification.setSdk(forward.getSdk());
        fingerPrintVerification.setMeta(forward.getMeta());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(RequestParamEnums.SUBSCRIPTION_KEY.getValue(), API_KEY);

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);

        List<Charset> charsets = new ArrayList<Charset>();
        charsets.add(Charset.forName("UTF-8"));

        headers.setAcceptCharset(charsets);
        headers.setAccept(mediaTypes);

        try {

            String requestJson = JSONUtil.getJSON(fingerPrintVerification);
            HttpEntity<String> request = new HttpEntity<String>(requestJson, headers);

            logger.info("Request Header " + request.getHeaders());
            logger.info("Request Body " + request.getBody());
            logger.info("Request Forward to Server");

            ResponseEntity<String> response = restTemplate.postForEntity(uri.build().toUri(), request, String.class);

            logger.info("Response Object " + response);
            if (response != null) {
                if (response.getBody() != null) {

                    logger.info("Response Body " + response.getBody());
                    verificationResponse = (FingerPrintVerificationResponse) JSONUtil.jsonToObject(response.getBody(), FingerPrintVerificationResponse.class);
                    if (StringUtils.isNotEmpty(verificationResponse.getData().getTags().getExpiryDate()) && verificationResponse.getData().getTags().getExpiryDate().equals("Lifetime")) {
                        verificationResponse.getData().getTags().setExpiryDate("2099-12-31");
                    }else {
                        verificationResponse.getData().getTags().setExpiryDate(verificationResponse.getData().getTags().getExpiryDate());
                    }

                    if (StringUtils.isNotEmpty(verificationResponse.getData().getTags().getDateOfBirth())) {
                        String simpledate = verificationResponse.getData().getTags().getDateOfBirth();
                        String[] dateparts = simpledate.split("-");
                        String a = dateparts[0];
                        String b = dateparts[1];
                        String c = dateparts[2];
                        if (b.equals("00")) {
                            b = "01";
                        }
                        if (c.equals("00")) {
                            c = "01";
                        }
                        verificationResponse.getData().getTags().setDateOfBirth(a + "-" + b + "-" + c);
                    }
                } else {
                    FingerPrintVerificationResponse.Status status = new FingerPrintVerificationResponse.Status();
                    status.setCode("404");
                    status.setMessage("Response Body is NUll");
                    verificationResponse.setStatus(status);
                }
            } else {
                FingerPrintVerificationResponse.Status status = new FingerPrintVerificationResponse.Status();
                status.setCode("404");
                status.setMessage("Response Object is NULL");
                verificationResponse.setStatus(status);
            }
        } catch (Exception e) {
            logger.error("General Exception Occurred ", e);
            FingerPrintVerificationResponse.Status status = new FingerPrintVerificationResponse.Status();
            status.setCode("505");
            status.setMessage("General Exception Occurred " + e.getMessage());
            verificationResponse.setStatus(status);
        }
        logger.info("End: FingerPrint Verification Service ");
        return verificationResponse;
    }
}
