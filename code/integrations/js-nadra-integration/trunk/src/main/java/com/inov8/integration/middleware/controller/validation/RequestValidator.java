package com.inov8.integration.middleware.controller.validation;

import com.inov8.integration.middleware.pdu.request.BiometricVerificationRequest;
import com.inov8.integration.middleware.pdu.request.OTCVerificationRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 6/8/2017.
 */
public class RequestValidator {

    private static Logger logger = LoggerFactory.getLogger(RequestValidator.class);

    public static void validateFingerPrintVerification(BiometricVerificationRequest verificationRequest) {

        BiometricVerificationRequest.Forward forward = verificationRequest.getForward();
        BiometricVerificationRequest.Data data = verificationRequest.getData();

        if (StringUtils.isEmpty(forward.getFinger())) {
            logger.info("getFinger is  missing ");
            throw new ValidationException("Finger Is Missing " + forward.getFinger());
        }

        if (StringUtils.isEmpty(data.getSession())) {
            logger.info("getSession is  missing ");
            throw new ValidationException("getSession Is Missing " + forward.getSession());
        }

        if (StringUtils.isEmpty(forward.getIdentifier())) {
            logger.info("getIdentifier is  missing ");
            throw new ValidationException("getIdentifier Is Missing " + forward.getIdentifier());
        }

        if (StringUtils.isEmpty(forward.getInstitution())) {
            logger.info("getInstitution is  missing ");
            throw new ValidationException("getInstitution Is Missing " + forward.getInstitution());
        }

        if (StringUtils.isEmpty(forward.getWsq())) {
            logger.info("getWsq is  missing ");
            throw new ValidationException("getWsq Is Missing " + forward.getWsq());
        }
    }

    public static void validateOTCVerification(OTCVerificationRequest otcVerificationRequest) {

        OTCVerificationRequest.Forward forward = otcVerificationRequest.getForward();
        OTCVerificationRequest.Data data = otcVerificationRequest.getData();
        OTCVerificationRequest.Forward.Tags tags = forward.getTags();

        if (StringUtils.isEmpty(forward.getFinger())) {
            logger.info("getFinger is  missing ");
            throw new ValidationException("Finger Is Missing " + forward.getFinger());
        }

        if (StringUtils.isEmpty(data.getSession())) {
            logger.info("getSession is  missing ");
            throw new ValidationException("getSession Is Missing " + forward.getSession());
        }

        if (StringUtils.isEmpty(forward.getIdentifier())) {
            logger.info("getIdentifier is  missing ");
            throw new ValidationException("getIdentifier Is Missing " + forward.getIdentifier());
        }

        if (StringUtils.isEmpty(forward.getInstitution())) {
            logger.info("getInstitution is  missing ");
            throw new ValidationException("getInstitution Is Missing " + forward.getInstitution());
        }

        if (StringUtils.isEmpty(forward.getWsq())) {
            logger.info("getWsq is  missing ");
            throw new ValidationException("getWsq Is Missing " + forward.getWsq());
        }

        if (StringUtils.isEmpty(tags.getRemittanceType())) {
            logger.info("getRemittanceType is  missing ");
            throw new ValidationException("getRemittanceType Is Missing " + tags.getRemittanceType());
        }

        if (StringUtils.isEmpty(tags.getAreaName())) {
            logger.info("getAreaName is  missing ");
            throw new ValidationException("getAreaName Is Missing " + tags.getAreaName());
        }

        if (StringUtils.isEmpty(tags.getContactNumber())) {
            logger.info("getContactNumber is  missing ");
            throw new ValidationException("getContactNumber Is Missing " + tags.getContactNumber());
        }

    }
}
