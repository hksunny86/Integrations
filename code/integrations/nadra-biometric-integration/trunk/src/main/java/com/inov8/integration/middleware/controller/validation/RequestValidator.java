package com.inov8.integration.middleware.controller.validation;

import com.inov8.integration.vo.NadraIntegrationVO;
import org.apache.commons.lang.StringUtils;

public class RequestValidator {


    public static void validateFingerPrintVerification(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

        if (StringUtils.isEmpty(messageVO.getContactNo())) {
            throw new ValidationException("Validation Failed Contact Number [FAILED] Contact Number: " + messageVO.getContactNo());
        }
        if (StringUtils.isEmpty(messageVO.getFingerIndex())) {
            throw new ValidationException("Validation Failed Finger Index [FAILED] Finger Index: " + messageVO.getFingerIndex());
        }
        if (StringUtils.isEmpty(messageVO.getFingerTemplate())) {
            throw new ValidationException("Validation Failed Finger Template [FAILED] Finger Template: " + messageVO.getFingerTemplate());
        }
        if (StringUtils.isEmpty(messageVO.getTemplateType())) {
            throw new ValidationException("Validation Failed Template Type [FAILED] Template Type: " + messageVO.getTemplateType());
        }

        if (StringUtils.isEmpty(messageVO.getAreaName())) {
            throw new ValidationException("Validation Failed Area Name [FAILED] Area Name: " + messageVO.getAreaName());
        }
    }

    public static void validatePhotographVerification(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateSecretIdentityDemographics(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateCitizenDemograpicsVerification(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateManualVerification(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }
        if (StringUtils.isEmpty(messageVO.getVerificationResult())) {
            throw new ValidationException("Validation Failed Verification Result [FAILED] Verification Result : " + messageVO.getVerificationResult());
        }

    }

    public static void validateGetLastVerificationResult(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }


    }

    public static void validateSaveMobileBankAccountDetails(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateOtcFingerPrintVerification(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

        if (StringUtils.isEmpty(messageVO.getFingerIndex())) {
            throw new ValidationException("Validation Failed Finger Index [FAILED] Finger Index: " + messageVO.getFingerIndex());
        }
        if (StringUtils.isEmpty(messageVO.getFingerTemplate())) {
            throw new ValidationException("Validation Failed Finger Template [FAILED] Finger Template: " + messageVO.getFingerTemplate());
        }
        if (StringUtils.isEmpty(messageVO.getTemplateType())) {
            throw new ValidationException("Validation Failed Template Type [FAILED] Template Type: " + messageVO.getTemplateType());
        }

        if (StringUtils.isEmpty(messageVO.getRemittanceType())) {
            throw new ValidationException("Validation Failed Remittance Remittance type [FAILED] Remittance type: " + messageVO.getRemittanceType());
        }

        if (!(messageVO.getRemittanceType().equalsIgnoreCase("IBFT")) && StringUtils.isEmpty(messageVO.getSecondaryCitizenNumber())) {
            throw new ValidationException("Validation Failed Secondary/Receiver Citizen Number [FAILED] Secondary/Receiver Citizen Number: " + messageVO.getSecondaryCitizenNumber());
        }

        if (StringUtils.isEmpty(messageVO.getAreaName())) {
            throw new ValidationException("Validation Failed Area Name [FAILED] Area Name: " + messageVO.getAreaName());
        }
    }

    public static void validateOtcSecretIdentityDemographics(NadraIntegrationVO messageVO) {


        if (StringUtils.isEmpty(messageVO.getCitizenNumber()) || messageVO.getCitizenNumber().length() > 13) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateOtcManualVerification(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }

    }

    public static void validateOtcLastVerificationResult(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }


    }

    public static void validateGetCitizenData(NadraIntegrationVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getCitizenNumber())) {
            throw new ValidationException("Validation Failed Citizen Number [FAILED] Citizen Number: " + messageVO.getCitizenNumber());
        }
        if (StringUtils.isEmpty(messageVO.getContactNo())) {
            throw new ValidationException("Validation Failed Contact Number [FAILED] Contact Number: " + messageVO.getContactNo());
        }
        if (StringUtils.isEmpty(messageVO.getCnicIssuanceDate())){

            throw new ValidationException("Validation Failed Cnic Issuance Date [FAILED] Cnic Issuance Date: " + messageVO.getCnicIssuanceDate());
        }
        if (StringUtils.isEmpty(messageVO.getAreaName())) {
            throw new ValidationException("Validation Failed Area Name[FAILED] Area Name:" + messageVO.getAreaName());
        }


    }

}
