package com.inov8.integration.internationalRemittance.controller.validator;

import com.inov8.integration.internationalRemittance.pdu.request.CoreToWalletCreditRequest;
import com.inov8.integration.internationalRemittance.pdu.request.TitleFetchRequestV2;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import org.apache.commons.lang.StringUtils;

public class InternationalRemittanceRequestValidator {

    public static void validateCoreToWalletCredit(CoreToWalletCreditRequest integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getUserName())) {
            throw new ValidationException("[FAILED] Validation Failed Username: " + integrationVO.getUserName());
        }
        if (StringUtils.isEmpty(integrationVO.getPassword())) {
            throw new ValidationException("[FAILED] Validation Failed Password: " + integrationVO.getPassword());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountNo())) {
            throw new ValidationException("[FAILED] Validation Failed Account Number: " + integrationVO.getAccountNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getStan())) {
            throw new ValidationException("[FAILED] Validation Failed Stan: " + integrationVO.getStan());
        }
        if (StringUtils.isEmpty(integrationVO.getBankIMD())) {
            throw new ValidationException("[FAILED] Validation Failed Bank IMD: " + integrationVO.getBankIMD());
        }
        if (StringUtils.isEmpty(integrationVO.getTransactionAmount())) {
            throw new ValidationException("[FAILED] Validation Failed Transaction Amount: " + integrationVO.getTransactionAmount());
        }
    }

    public static void validateTitleFetchV2(TitleFetchRequestV2 integrationVO) {

        if (StringUtils.isEmpty(integrationVO.getUserName())) {
            throw new ValidationException("[FAILED] Validation Failed Username: " + integrationVO.getUserName());
        }
        if (StringUtils.isEmpty(integrationVO.getPassword())) {
            throw new ValidationException("[FAILED] Validation Failed Password: " + integrationVO.getPassword());
        }
        if (StringUtils.isEmpty(integrationVO.getAccountNo())) {
            throw new ValidationException("[FAILED] Validation Failed Account Numbers: " + integrationVO.getAccountNo());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getTerminalId())) {
            throw new ValidationException("[FAILED] Validation Failed Terminal ID: " + integrationVO.getTerminalId());
        }
        if (StringUtils.isEmpty(integrationVO.getSegmentCode())) {
            throw new ValidationException("[FAILED] Validation Failed Segment Code: " + integrationVO.getSegmentCode());
        }
    }

}
