package com.inov8.integration.middleware.controller.restController;

import com.inov8.integration.middleware.controller.validator.HostRequestValidator;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.JSONUtil;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class JSMinorAccountController {

    private static Logger logger = LoggerFactory.getLogger(JSMinorAccountController.class.getSimpleName());
    @Autowired
    HostIntegrationService integrationService;

    @RequestMapping(value = "api/M0AccountOpening", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MinorAccountOpeningResponse m0AccountOpeningResponse(@RequestBody MinorAccountOpeningRequest request) throws ParseException {

        logger.info("M0 Account Opening  Request Recieve at Controller at time: ");

        MinorAccountOpeningResponse m0AccountOpeningResponse = new MinorAccountOpeningResponse();
        String res = null;
        long start = System.currentTimeMillis();
        logger.info("M0 Account Opening  Request Recieve at Controller at time: " + start);//        JSONParser parser = new JSONParser();
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getRrn())
                .append(request.getDateTime())
                .append(request.getName())
                .append(request.getNic())
                .append(request.getIssuanceDate())
                .append(request.getMobileNumber())
                .append(request.getMotherMedianName())
                .append(request.getFatherName())
                .append(request.getPlaceOfbirth())
                .append(request.getDateOfBirth())
                .append(request.getAddress())
                .append(request.getNicExpiry())
                .append(request.getFatherMotherMobileNumber())
                .append(request.getFatherCnic())
                .append(request.getFatherCnicIssuanceDate())
                .append(request.getFatherCnicExpiryDate())
                .append(request.getMotherCnic())
                .append(request.getEmail())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateM0AccountOpening(request);
                    m0AccountOpeningResponse = integrationService.m0AccountOpeningResponse(request);
//                    m0AccountOpeningResponse.setResponseCode("00");
//                    m0AccountOpeningResponse.setResponseDescription("Successfull");
//                    m0AccountOpeningResponse.setHashData("ABCD0026156156565645656");
                } catch (ValidationException ve) {
                    m0AccountOpeningResponse.setResponseCode("420");
                    m0AccountOpeningResponse.setResponseDescription(ve.getMessage());
                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    m0AccountOpeningResponse.setResponseCode("220");
                    m0AccountOpeningResponse.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR  Fee Payment Request  TRANSACTION *********");
                logger.info("ResponseCode: " + m0AccountOpeningResponse.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Fee Payment Request TRANSACTION AUTHENTICATION *********");
                m0AccountOpeningResponse = new MinorAccountOpeningResponse();
                m0AccountOpeningResponse.setResponseCode("420");
                m0AccountOpeningResponse.setResponseDescription("Request is not authenticated");
                m0AccountOpeningResponse.setRrn(request.getRrn());
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Fee Payment Request Request TRANSACTION *********");
            m0AccountOpeningResponse = new MinorAccountOpeningResponse();
            m0AccountOpeningResponse.setResponseCode("111");
            m0AccountOpeningResponse.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }
        long end = System.currentTimeMillis() - start;
        logger.info("M0 Account Opening  Request  Processed in : {} ms {}", end, m0AccountOpeningResponse);
        return m0AccountOpeningResponse;
    }

    @RequestMapping(value = "api/M0AccountVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    M0VerifyAccountResponse m0VerifyAccountResponse(@Valid @RequestBody M0VerifyAccountRequest request) {
        logger.info("Start Processing Account Verify Transaction Request with {}");
        long start = System.currentTimeMillis();
        M0VerifyAccountResponse response = new M0VerifyAccountResponse();
        String requestXML = JSONUtil.getJSON(request);
        logger.info("Start Processing Account Verify Transaction Request with {}", request);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getCnic() +
                        request.getDateTime() +
                        request.getMobileNumber() +
                        request.getRrn() +
                        request.getTransactionType() +
                        request.getChannelId() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
                    HostRequestValidator.validateM0VerifyAccount(request);
//                    //for Mock
//                    response.setResponseCode("14");
//                    response.setResponseDescription("No Customer Found Against Mobile number");
//                    response.setHashData("ABCD0026156156565645656");

                    response = integrationService.m0VerifyAccount(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Verify Account TRANSACTION AUTHENTICATION *********");
                response = new M0VerifyAccountResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR ACCOUNT VERIFY TRANSACTION *********");
            response = new M0VerifyAccountResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Account Verify Transaction Request  Processed in : {} ms {}", end, response);

        return response;
    }


//    @RequestMapping(value = "api/UpgradeMinorAccountInquiry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    UpgradeMinorAccountInquiryResponse upgradeMinorAccountInquiryResponse
//            (@Valid @RequestBody UpgradeMinorAccountInquiryRequest request) {
//        long start = System.currentTimeMillis();
//        UpgradeMinorAccountInquiryResponse response = new UpgradeMinorAccountInquiryResponse();
//        String requestXML = JSONUtil.getJSON(request);
//        logger.info("Start Processing Upgrade Account inquiry Response Request with {}", requestXML);
//        StringBuilder stringText = new StringBuilder()
//                .append(request.getUserName())
//                .append(request.getPassword())
//                .append(request.getChannelId())
//                .append(request.getRrn())
//                .append(request.getTerminalId())
//                .append(request.getMobileNumber())
//                .append(request.getDateTime())
//                .append(request.getReserved1())
//                .append(request.getReserved2())
//                .append(request.getReserved3())
//                .append(request.getReserved4())
//                .append(request.getReserved5());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
//            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
//                try {
//                    HostRequestValidator.validateMinorUpgradeAccountInquiry(request);
////                    response = integrationService.upgradeMinorAccountInquiryResponse(request);
//                    response.setResponseCode("00");
//                    response.setResponseDescription("Successfull");
//                    response.setRrn(request.getRrn());
//                    response.setHashData("ABCD00021545882500");
//
//                } catch (ValidationException ve) {
//                    response.setResponseCode("420");
//                    response.setResponseDescription(ve.getMessage());
//
//                    logger.error("ERROR: Request Validation", ve);
//                } catch (Exception e) {
//                    response.setResponseCode("220");
//                    response.setResponseDescription(e.getMessage());
//                    logger.error("ERROR: General Processing ", e);
//                }
//
//                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment *********");
//                logger.info("ResponseCode: " + response.getResponseCode());
//            } else {
//                logger.info("******* DEBUG LOGS FOR Wallet to wallet Payment AUTHENTICATION *********");
//                response = new UpgradeMinorAccountInquiryResponse();
//                response.setResponseCode("420");
//                response.setResponseDescription("Request is not authenticated");
//                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
//            }
//        } else {
//            logger.info("******* DEBUG LOGS FOR  Wallet to wallet Payment *********");
//            response = new UpgradeMinorAccountInquiryResponse();
//            response.setResponseCode("111");
//            response.setResponseDescription("Request is not recognized");
//            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
//        }
//
//        long end = System.currentTimeMillis() - start;
//        logger.info("Generate OTP Request Processed in : {} ms {}", end, response);
//
//        return response;
//    }

//    @RequestMapping(name = "api/UpgradeMinorAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody

    @RequestMapping(value = "api/UpgradeMinorAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    UpgradeMinorAccountResponse upgradeMinorAccountResponse(@Valid @RequestBody UpgradeMinorAccountRequest
                                                                    request) {
        long start = System.currentTimeMillis();
        UpgradeMinorAccountResponse response = new UpgradeMinorAccountResponse();
        String requestXML = JSONUtil.getJSON(request);
        logger.info("Start Processing Upgrade Account Transaction Request with {}", requestXML);
        StringBuilder stringText = new StringBuilder()
                .append(request.getUserName())
                .append(request.getPassword())
                .append(request.getDateTime())
                .append(request.getRrn())
                .append(request.getMobileNumber())
                .append(request.getChannelId())
                .append(request.getTerminalId())
                .append(request.getReserved1())
                .append(request.getReserved2())
                .append(request.getReserved3())
                .append(request.getReserved4())
                .append(request.getReserved5())
                .append(request.getReserved6())
                .append(request.getReserved7())
                .append(request.getReserved8())
                .append(request.getReserved9())
                .append(request.getReserved10());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (request.getHashData().equalsIgnoreCase(sha256hex)) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {
                try {
                    HostRequestValidator.validateM0UpgadeAccount(request);
                    response = integrationService.upgradeMinorAccountResponse(request);

//                    response.setResponseCode("00");
//                    response.setResponseDescription("Successful");
//                    response.setRrn(request.getRrn());
//                    response.setHashData("ABCDE2165465665656556");

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }

                logger.info("******* DEBUG LOGS FOR Upgrade Account TRANSACTION *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Upgrade Account TRANSACTION AUTHENTICATION *********");
                response = new UpgradeMinorAccountResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR  Upgrade Account TRANSACTION *********");
            response = new UpgradeMinorAccountResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }

        long end = System.currentTimeMillis() - start;
        logger.info("Upgrade Account Request Processed in : {} ms {}", end, response);

        return response;
    }


    @RequestMapping(value = "api/FatherBvsVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    FatherBvsVerificationResponse fatherBvsVerificationResponse(@Valid @RequestBody FatherBvsVerification request) {
        logger.info("Start Processing Minor Father Bvs Verification Request with {}");
        long start = System.currentTimeMillis();
        FatherBvsVerificationResponse response = new FatherBvsVerificationResponse();
        String requestXML = JSONUtil.getJSON(request);
        logger.info("Start Processing Minor Father Bvs Verification Request with {}", request);
        StringBuffer stringText = new StringBuffer(
                request.getUserName() +
                        request.getPassword() +
                        request.getRrn() +
                        request.getDateTime() +
                        request.getsNic() +
                        request.getMobileNumber() +
                        request.getFatherMobileNumber() +
                        request.getFatherCnic() +
                        request.getChannelId() +
                        request.getTerminalId() +
                        request.getReserved1() +
                        request.getReserved2() +
                        request.getReserved3() +
                        request.getReserved4() +
                        request.getReserved5() +
                        request.getReserved6() +
                        request.getReserved7() +
                        request.getReserved8() +
                        request.getReserved9() +
                        request.getReserved10()
        );
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (sha256hex.equalsIgnoreCase(request.getHashData())) {
            if (HostRequestValidator.authenticate(request.getUserName(), request.getPassword(), request.getChannelId())) {

                try {
//                    HostRequestValidator.validateMinorFatherBVSVerfication(request);
//                    //for Mock
//                    response.setResponseCode("00");
//                    response.setResponseDescription("SuccessFull");
//                    response.setHashData("ABCD0026156156565645656");

//                    response = integrationService.minorFatherBvsVerification(request);

                } catch (ValidationException ve) {
                    response.setResponseCode("420");
                    response.setResponseDescription(ve.getMessage());

                    logger.error("ERROR: Request Validation", ve);
                } catch (Exception e) {
                    response.setResponseCode("220");
                    response.setResponseDescription(e.getMessage());
                    logger.error("ERROR: General Processing ", e);
                }
                logger.info("******* DEBUG LOGS FOR Minor Father Bvs Verification *********");
                logger.info("ResponseCode: " + response.getResponseCode());
            } else {
                logger.info("******* DEBUG LOGS FOR  Minor Father Bvs Verification *********");
                response = new FatherBvsVerificationResponse();
                response.setResponseCode("420");
                response.setResponseDescription("Request is not authenticated");
                logger.info("******* REQUEST IS NOT AUTHENTICATED *********");
            }
        } else {
            logger.info("******* DEBUG LOGS FOR Minor Father Bvs Verification *********");
            response = new FatherBvsVerificationResponse();
            response.setResponseCode("111");
            response.setResponseDescription("Request is not recognized");
            logger.info("******* REQUEST IS NOT RECOGNIZED *********");
        }


        long end = System.currentTimeMillis() - start;
        logger.info("Minor Father Bvs Verification Request  Processed in : {} ms {}", end, response);

        return response;
    }
}