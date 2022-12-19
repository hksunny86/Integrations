package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.controller.validation.RequestValidator;
import com.inov8.integration.middleware.controller.validation.ValidationException;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.vo.TransactionRequest;
import com.inov8.integration.vo.TransactionResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.inov8.integration.middleware.service.IntegrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RestController("nadraIntegrationControllerImpl")
public class NadraIntegrationControllerImpl implements NadraIntegrationController {

    private static Logger logger = LoggerFactory.getLogger(NadraIntegrationControllerImpl.class.getSimpleName());

    @Autowired
    private IntegrationService integrationService;


    @Override
    public NadraIntegrationVO fingerPrintVerification(@RequestBody NadraIntegrationVO messageVO) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Finger Print Verification for User :", messageVO.getUserName());
        try {
            logger.info("Validate Finger Print Verification Request");
            RequestValidator.validateFingerPrintVerification(messageVO);
            messageVO = integrationService.fingerPrintVerification(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Finger Print Verification *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Finger Print Verification Request  Processed in : {} ms ", end);
        return messageVO;
    }

    @POST
    @Path("/verify")
    @Consumes("application/json")
    @Produces("application/json")
    public TransactionResponse fingerPrintVerification(TransactionRequest request) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Finger Print Verification for User :", request.getUsername());
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        TransactionResponse response = new TransactionResponse();

        try {
            if (request != null) {
                messageVO.setCitizenNumber(request.getCitizenNumber());
                messageVO.setContactNo(request.getContactNumber());
                messageVO.setFingerIndex(request.getFingerIndex());
                messageVO.setFingerTemplate(request.getFingerTemplate());
                messageVO.setTemplateType(request.getTemplateType());
                messageVO.setServiceProviderTransactionId(request.getTransactionId());
                messageVO.setAreaName(request.getAreaName());
                messageVO.setVerificationResult(request.getVerificationResult());
                messageVO.setAccountNumber(request.getMobileBankAccountNumber());
                messageVO.setAccountLevel(request.getAccountLevel());
                messageVO.setRemittanceType(request.getRemittanceType());
                messageVO.setRemittanceAmount(request.getRemittanceAmount());
            }
            logger.info("Validate Finger Print Verification Request");
            RequestValidator.validateFingerPrintVerification(messageVO);
            messageVO = integrationService.fingerPrintVerification(messageVO);
        } catch (ValidationException e) {
            response.setResponseCode("420");
            response.setMessage(e.getMessage());
            logger.error("Validation ERROR: ", e);
            return response;
        } catch (Exception e) {
            response.setResponseCode("220");
            response.setMessage(e.getMessage());
            logger.error("ERROR: General Processing ", e);
            return response;
        }
        response.setResponseCode(messageVO.getResponseCode());
        response.setMessage(messageVO.getResponseDescription());
        response.setSessionId(messageVO.getSessionId());
        response.setTransactionId(messageVO.getServiceProviderTransactionId());
        response.setCitizenNumber(messageVO.getCitizenNumber());
        response.setCitizenName(messageVO.getFullName());
        response.setPresentAddress(messageVO.getPresentAddress());
        response.setBirthPlace(messageVO.getBirthPlace());
//        response.setCardExpired(messageVO.getCardExpiry());
//        response.setCardExpiry(messageVO.getCardExpire());
        if (StringUtils.isNotEmpty(messageVO.getCardExpire()) && messageVO.getCardExpire().equals("Lifetime")) {
            response.setCardExpiry("2099-12-31");
        } else {
            response.setCardExpiry(messageVO.getCardExpire());
        }
        if (StringUtils.isNotEmpty(messageVO.getDateOfBirth())) {
            String simpledate = messageVO.getDateOfBirth();
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

            response.setDateOfBirth(a + "-" + b + "-" + c);
        }

//        response.setDateOfBirth(messageVO.getDateOfBirth());
        response.setFingerIndex(messageVO.getFingerIndex());
        response.setReligion(messageVO.getReligion());
        response.setMotherName(messageVO.getMotherName());
        response.setNativeLanguage(messageVO.getNativeLanguage());
        response.setPhotograph(messageVO.getPhotograph());
        response.setVerificationFunctionality(messageVO.getVerificationFunctionality());
        response.setGender(messageVO.getGender());
        logger.info("******* DEBUG Logs For Finger Print Verification *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Finger Print Verification Request  Processed in : {} ms ", end);
        return response;
    }

    @POST
    @Path("/verifyOTC")
    @Consumes("application/json")
    @Produces("application/json")
    public TransactionResponse OTCFingerPrintVerification(TransactionRequest request) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Start Processing OTC Finger Print Verification for User :", request.getUsername());
        logger.info("Start Processing OTC Finger Print Verification for secondaryContact Number :", request.getSecondaryContactNumber());
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        TransactionResponse response = new TransactionResponse();

        try {
            if (request != null) {
                messageVO.setServiceProviderTransactionId(request.getTransactionId());
                messageVO.setCitizenNumber(request.getCitizenNumber());
                messageVO.setContactNo(request.getContactNumber());
                messageVO.setFingerIndex(request.getFingerIndex());
                messageVO.setFingerTemplate(request.getFingerTemplate());
                messageVO.setTemplateType(request.getTemplateType());
                messageVO.setRemittanceAmount(request.getRemittanceAmount());
                messageVO.setRemittanceType(request.getRemittanceType());
                if (request.getSecondaryCitizenNumber().isEmpty()) {
                    messageVO.setSecondaryCitizenNumber(request.getCitizenNumber());
                    messageVO.setSecondaryContactNo(request.getContactNumber());
                } else {
                    messageVO.setSecondaryCitizenNumber(request.getSecondaryCitizenNumber());
                    messageVO.setSecondaryContactNo(request.getSecondaryContactNumber());
                }
                messageVO.setAccountNumber(request.getMobileBankAccountNumber());
                messageVO.setAreaName(request.getAreaName());

            }
            logger.info("Validate OTC Finger Print Verification Request");
            RequestValidator.validateFingerPrintVerification(messageVO);
            messageVO = integrationService.OtcFingerPrintVerification(messageVO);
        } catch (ValidationException e) {
            response.setResponseCode("420");
            response.setMessage(e.getMessage());
            logger.error("Validation ERROR: ", e);
            return response;
        } catch (Exception e) {
            response.setResponseCode("220");
            response.setMessage(e.getMessage());
            logger.error("ERROR: General Processing ", e);
            return response;
        }
        response.setResponseCode(messageVO.getResponseCode());
        response.setMessage(messageVO.getResponseDescription());
        response.setSessionId(messageVO.getSessionId());
        response.setTransactionId(messageVO.getServiceProviderTransactionId());
        response.setCitizenNumber(messageVO.getCitizenNumber());
        response.setCitizenName(messageVO.getFullName());
        response.setFingerIndex(messageVO.getFingerIndex());
        response.setSecondaryCitizenNumber(messageVO.getSecondaryCitizenNumber());
        response.setSecondaryCitizenName(messageVO.getSecondaryFullName());
        logger.info("******* DEBUG Logs For OTC Finger Print Verification *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("OTC Finger Print Verification Request  Processed in : {} ms ", end);
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/SecretIdentityDemographicsData", method = RequestMethod.POST)
    public NadraIntegrationVO getSecretIdentityDemographicsData(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Secret Identity Demographics Data for User :", messageVO.getUserName());
        try {
            logger.info("Validate Secret Identity Demographics Data Request");
            RequestValidator.validateSecretIdentityDemographics(messageVO);
            messageVO = integrationService.getSecretIdentityDemographicsData(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Secret Identity Demographics Data *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Secret Identity Demographics Data Request  Processed in : {} ms ", end);
        return messageVO;
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getManualVerificationData", method = RequestMethod.POST)
    public NadraIntegrationVO getManualVerificationResult(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Manual Verification Data for User :", messageVO.getUserName());
        try {
            logger.info("Manual Verification Data Request");
            RequestValidator.validateManualVerification(messageVO);
            messageVO = integrationService.getManualVerificationData(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Manual Verification Data *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Manual Verification Data Request  Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getLastVerificationResult", method = RequestMethod.POST)
    public NadraIntegrationVO getLastVerificationResult(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Last Verification Result :", messageVO.getUserName());
        try {
            logger.info("Last Verification Result  Request");
            RequestValidator.validateGetLastVerificationResult(messageVO);
            messageVO = integrationService.getLastVerificationResult(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Last Verification Result  *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Last Verification Result Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/submitMobileBankAccountDetail", method = RequestMethod.POST)
    public NadraIntegrationVO submitMobileBankAccountDetail(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Submit Mobile Bank Account Detail:", messageVO.getUserName());
        try {
            logger.info("Submit Mobile Bank Account Detail Request");
            RequestValidator.validateSaveMobileBankAccountDetails(messageVO);
            messageVO = integrationService.submitMobileBankAccountDetail(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Submit Mobile Bank Account Detail  *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Submit Mobile Bank Account Detail Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/fingerPrintOTCVerification", method = RequestMethod.POST)
    public NadraIntegrationVO otcFingerPrintVerification(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing OTC Finger Print Verification for User :", messageVO.getUserName());
        try {
            logger.info("Validate OTC Finger Print Verification Request");
            RequestValidator.validateOtcFingerPrintVerification(messageVO);
            messageVO = integrationService.OtcFingerPrintVerification(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For OTC Finger Print Verification *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("OTC Finger Print Verification Request  Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/OtcSecretIdentityDemographicsData", method = RequestMethod.POST)
    public NadraIntegrationVO getOtcSecretIdentityDemographicsData(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Otc Secret Identity Demographics Data for User :", messageVO.getUserName());
        try {
            logger.info("Validate Otc Secret Identity Demographics Data Request");
            RequestValidator.validateOtcSecretIdentityDemographics(messageVO);
            messageVO = integrationService.getOtcSecretIdentityDemographicsData(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Otc Secret Identity Demographics Data *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Otc Secret Identity Demographics Data Request  Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getOtcManualVerificationData", method = RequestMethod.POST)
    public NadraIntegrationVO getOtcManualVerificationResult(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing OTC Manual Verification Data for User :", messageVO.getUserName());
        try {
            logger.info("OTC Manual Verification Data Request");
            RequestValidator.validateOtcManualVerification(messageVO);
            messageVO = integrationService.getOtcManualVerificationResult(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For OTC Manual Verification Data *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("OTC Manual Verification Data Request  Processed in : {} ms ", end);
        return messageVO;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getOtcLastVerificationResult", method = RequestMethod.POST)
    public NadraIntegrationVO getOtcLastVerificationResult(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing OTC Last Verification Result :", messageVO.getUserName());
        try {
            logger.info("OTC Last Verification Result  Request");
            RequestValidator.validateOtcLastVerificationResult(messageVO);
            messageVO = integrationService.getOtcLastVerificationResult(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For OTC Last Verification Result  *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("OTC Last Verification Result Processed in : {} ms ", end);
        return messageVO;
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getCitizenData", method = RequestMethod.POST)
    public NadraIntegrationVO getCitizenData(@RequestBody NadraIntegrationVO messageVO) throws RuntimeException {
        long start = System.currentTimeMillis();
        logger.info("Start Processing Citizen Data Verification Result :", messageVO.getUserName());
        try {
            logger.info("Citizen Verification Result  Request");
            RequestValidator.validateGetCitizenData(messageVO);
            messageVO = integrationService.getCitizenData(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }

        if (StringUtils.isNotEmpty(messageVO.getCardExpire()) && messageVO.getCardExpire().equalsIgnoreCase("Lifetime")) {
            messageVO.setCardExpire("2099-12-31");
        } else {
            messageVO.setCardExpire(messageVO.getCardExpire());
        }
        logger.info("******* DEBUG Logs For Get Citizen Verification  *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Get Citizen Verification Result Processed in : {} ms ", end);
        return messageVO;
    }


//    public NadraIntegrationVO photographVerification(NadraIntegrationVO messageVO) throws RuntimeException {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Photograph Verification for User :", messageVO.getUserName());
//        try {
//            logger.info("Validate Photograph Verification Request");
//            RequestValidator.validatePhotographVerification(messageVO);
//            messageVO = integrationService.photographVerification(messageVO);
//        } catch (Exception e) {
//            messageVO.setResponseCode("220");
//            messageVO.setResponseDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//        logger.info("******* DEBUG Logs For Photograph Verification *********");
//        logger.info("ResponseCode: " + messageVO.getResponseCode());
//        long end = System.currentTimeMillis() - start;
//        logger.debug("Photograph Verification Request  Processed in : {} ms ", end);
//        return messageVO;
//    }
//    public NadraIntegrationVO manualVerification(NadraIntegrationVO messageVO) throws RuntimeException {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Submit Manual Verification for User :", messageVO.getUserName());
//        try {
//            logger.info("Submit Manual Verification Request");
//            RequestValidator.validateManualVerification(messageVO);
//            messageVO = integrationService.manualVerification(messageVO);
//        } catch (Exception e) {
//            messageVO.setResponseCode("220");
//            messageVO.setResponseDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//        logger.info("******* DEBUG Logs For Submit Manual Verification *********");
//        logger.info("ResponseCode: " + messageVO.getResponseCode());
//        long end = System.currentTimeMillis() - start;
//        logger.debug("Submit Manual Verification Request  Processed in : {} ms ", end);
//        return messageVO;
//    }
//
//    public NadraIntegrationVO getLastVerificationResult(NadraIntegrationVO messageVO) throws RuntimeException {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Last Verification Result for User :", messageVO.getUserName());
//        try {
//            logger.info("Last Verification Result Request");
//            RequestValidator.validateGetLastVerificationResult(messageVO);
//            messageVO = integrationService.getLastVerificationResult(messageVO);
//        } catch (Exception e) {
//            messageVO.setResponseCode("220");
//            messageVO.setResponseDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//        logger.info("******* DEBUG Logs For Last Verification Result *********");
//        logger.info("ResponseCode: " + messageVO.getResponseCode());
//        long end = System.currentTimeMillis() - start;
//        logger.debug("Last Verification Result Request  Processed in : {} ms ", end);
//        return messageVO;
//    }
//
//    public NadraIntegrationVO saveMobileBankAccountDetail(NadraIntegrationVO messageVO) throws RuntimeException {
//        long start = System.currentTimeMillis();
//        logger.info("Start Processing Submit Mobile Bank Account Details for User :", messageVO.getUserName());
//        try {
//            logger.info("Last Submit Mobile Bank Account Details Request");
//            RequestValidator.validateSaveMobileBankAccountDetails(messageVO);
//            messageVO = integrationService.saveMobileBankAccountDetail(messageVO);
//        } catch (Exception e) {
//            messageVO.setResponseCode("220");
//            messageVO.setResponseDescription(e.getMessage());
//            logger.error("ERROR: General Processing ", e);
//        }
//        logger.info("******* DEBUG Logs For Submit Mobile Bank Account Details *********");
//        logger.info("ResponseCode: " + messageVO.getResponseCode());
//        long end = System.currentTimeMillis() - start;
//        logger.debug("Submit Mobile Bank Account Details Request  Processed in : {} ms ", end);
//        return messageVO;
//    }
}
