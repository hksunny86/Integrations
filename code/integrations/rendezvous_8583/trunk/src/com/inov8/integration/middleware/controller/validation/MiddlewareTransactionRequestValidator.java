package com.inov8.integration.middleware.controller.validation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.vo.MiddlewareMessageVO;

public class MiddlewareTransactionRequestValidator {

    private static Logger logger = LoggerFactory.getLogger(MiddlewareTransactionRequestValidator.class.getSimpleName());

    public static void validateCheckBillRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }

        if (StringUtils.isEmpty(messageVO.getCompanyCode())) {
            logger.debug("[FAILED] Utility Company Code Not not provided: " + messageVO.getCompanyCode());
            throw new MiddlewareValidationException("Validation Failed Utility Company Not Found [FAILED] Utility Company Code: " + messageVO.getCompanyCode());
        }

        if (StringUtils.isEmpty(messageVO.getCnicNo())) {
            logger.debug("[FAILED] CNIC Number not provided: " + messageVO.getCnicNo());
            throw new MiddlewareValidationException("Validation CNIC Number [FAILED] CNIC Number: " + messageVO.getCnicNo());
        }

        if (StringUtils.isEmpty(messageVO.getConsumerNo())) {
            logger.debug("[FAILED] Consumer Number is Missing: " + messageVO.getConsumerNo());
            throw new MiddlewareValidationException("Validation Failed Consumer Number" + " [FAILED] Consumer Number: " + messageVO.getConsumerNo());
        }
    }

    public static void validatePayBillRequest(MiddlewareMessageVO messageVO) {

        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }

        if (StringUtils.isEmpty(messageVO.getCompanyCode())) {
            logger.debug("[FAILED] Utility Company Code Not not provided: " + messageVO.getCompanyCode());
            throw new MiddlewareValidationException("Validation Failed Utility Company Not Found [FAILED] Utility Company Code: " + messageVO.getCompanyCode());
        }

        if (StringUtils.isEmpty(messageVO.getCnicNo())) {
            logger.debug("[FAILED] CNIC Number not provided: " + messageVO.getCnicNo());
            throw new MiddlewareValidationException("Validation CNIC Number [FAILED] CNIC Number: " + messageVO.getCnicNo());
        }

        if (StringUtils.isEmpty(messageVO.getConsumerNo())) {
            logger.debug("[FAILED] Consumer Number is Missing: " + messageVO.getConsumerNo());
            throw new MiddlewareValidationException("Validation Failed Consumer Number" + " [FAILED] Consumer Number: " + messageVO.getConsumerNo());
        }

        if (StringUtils.isEmpty(messageVO.getTransactionAmount())) {
            logger.debug("[FAILED] Amount Paid is Missing: " + messageVO.getTransactionAmount());
            throw new MiddlewareValidationException("Validation Failed Amount Paid " + "[FAILED] Amount Paid: " + messageVO.getTransactionAmount());
        }

        if (StringUtils.isEmpty(messageVO.getBillAggregator())) {
            logger.debug("[FAILED] Bill Payment Aggregator Code is Missing: " + messageVO.getBillAggregator());
            throw new MiddlewareValidationException("Validation Failed Bill Payment Aggregator Code " + "[FAILED] Bill Payment Aggregator Code: " + messageVO.getBillAggregator());
        }
    }

    public static void validateTitleFetchRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }
    }

    public static void validateFundTransferRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }

        if (StringUtils.isEmpty(messageVO.getAccountNo2())) {
            logger.debug("[FAILED] To Account Number not provided: " + messageVO.getAccountNo2());
            throw new MiddlewareValidationException("Validation Failed To Account Number" + " [FAILED] To Account Number: " + messageVO.getAccountNo2());
        }

        if (StringUtils.isEmpty(messageVO.getTransactionAmount())) {
            logger.debug("[FAILED] Amount Paid is Missing: " + messageVO.getTransactionAmount());
            throw new MiddlewareValidationException("Validation Failed Amount Paid " + "[FAILED] Amount Paid: " + messageVO.getTransactionAmount());
        }
    }

    public static void validateAcquirerReversalAdviceRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getReversalSTAN())) {
            logger.debug("[FAILED] Reversal STAN is Missing: " + messageVO.getReversalSTAN());
            throw new MiddlewareValidationException("Validation Reversal STAN " + "[FAILED] Reversal STAN: " + messageVO.getReversalSTAN());
        }

        if (StringUtils.isEmpty(messageVO.getReversalRequestTime())) {
            logger.debug("[FAILED] Reversal Request Time is Missing: " + messageVO.getReversalRequestTime());
            throw new MiddlewareValidationException("Validation Failed Reversal Request Time " + "[FAILED] Reversal Request Time: " + messageVO.getReversalRequestTime());
        }
    }

    public static void validateFundTransferAdviceRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }

        if (StringUtils.isEmpty(messageVO.getAccountNo2())) {
            logger.debug("[FAILED] To Account Number not provided: " + messageVO.getAccountNo2());
            throw new MiddlewareValidationException("Validation Failed To Account Number" + " [FAILED] To Account Number: " + messageVO.getAccountNo2());
        }

        if (StringUtils.isEmpty(messageVO.getTransactionAmount())) {
            logger.debug("[FAILED] Amount Paid is Missing: " + messageVO.getTransactionAmount());
            throw new MiddlewareValidationException("Validation Failed Amount Paid " + "[FAILED] Amount Paid: " + messageVO.getTransactionAmount());
        }
    }

    public static void validateAccountBalanceInquiryRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number" + " [FAILED] Account Number: " + messageVO.getAccountNo1());
        }
    }

    public static void validateIBFTTitleFetchRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number 1" + " [FAILED] Account Number 1: " + messageVO.getAccountNo1());
        }
        if (StringUtils.isEmpty(messageVO.getAccountNo2())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo2());
            throw new MiddlewareValidationException("Validation Failed Account Number 2" + " [FAILED] Account Number 2: " + messageVO.getAccountNo2());
        }
    }


    public static void validateIBFTAdviceRequest(MiddlewareMessageVO messageVO) {
        if (StringUtils.isEmpty(messageVO.getAccountNo1())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo1());
            throw new MiddlewareValidationException("Validation Failed Account Number 1" + " [FAILED] Account Number 1: " + messageVO.getAccountNo1());
        }
        if (StringUtils.isEmpty(messageVO.getAccountNo2())) {
            logger.debug("[FAILED] From Account Number not provided: " + messageVO.getAccountNo2());
            throw new MiddlewareValidationException("Validation Failed Account Number 2" + " [FAILED] Account Number 2: " + messageVO.getAccountNo2());
        }
    }
}
