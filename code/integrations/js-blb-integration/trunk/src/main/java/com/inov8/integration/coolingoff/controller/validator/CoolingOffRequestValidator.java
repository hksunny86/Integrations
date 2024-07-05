package com.inov8.integration.coolingoff.controller.validator;

import com.inov8.integration.coolingoff.pdu.request.ReleaseIbftRequest;
import com.inov8.integration.coolingoff.pdu.request.ToggleNotificationRequest;
import com.inov8.integration.corporate.pdu.request.LoginRequest;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import org.apache.commons.lang.StringUtils;

public class CoolingOffRequestValidator {
    public static void validateReleaseIbftAmount(ReleaseIbftRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
//        if (StringUtils.isEmpty(integrationVO.getReferenceNo())) {
//            throw new ValidationException("[FAILED] Validation Failed Portal Password: " + integrationVO.getReferenceNo());
//        }

    }

    public static void validateToggleNotification(ToggleNotificationRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed Rrn: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Mobile number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getIsEnable())) {
            throw new ValidationException("[FAILED] Validation Failed IsEnable: " + integrationVO.getIsEnable());
        }
        if (StringUtils.isEmpty(integrationVO.getType())) {
            throw new ValidationException("[FAILED] Validation Failed Type: " + integrationVO.getType());
        }

    }
}
