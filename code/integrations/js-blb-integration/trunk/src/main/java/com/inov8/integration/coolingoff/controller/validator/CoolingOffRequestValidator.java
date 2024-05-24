package com.inov8.integration.coolingoff.controller.validator;

import com.inov8.integration.coolingoff.pdu.request.ReleaseIbftRequest;
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
        if (StringUtils.isEmpty(integrationVO.getTransactionId())) {
            throw new ValidationException("[FAILED] Validation Failed Portal Id: " + integrationVO.getTransactionId());
        }
        if (StringUtils.isEmpty(integrationVO.getReferenceNo())) {
            throw new ValidationException("[FAILED] Validation Failed Portal Password: " + integrationVO.getReferenceNo());
        }

    }
}
