package com.inov8.integration.thirdParty.controller.validator;

import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.thirdParty.pdu.request.*;
import org.apache.commons.lang.StringUtils;

public class ThirdPartyHostRequestValidator {

    public static void validateAccountInfo(ReferralAccountInfoRequest integrationVO) throws ValidationException {

        if (StringUtils.isEmpty(integrationVO.getRrn())) {
            throw new ValidationException("[FAILED] Validation Failed RRN: " + integrationVO.getRrn());
        }
        if (StringUtils.isEmpty(integrationVO.getMobileNumber())) {
            throw new ValidationException("[FAILED] Validation Failed Mobile Number: " + integrationVO.getMobileNumber());
        }
        if (StringUtils.isEmpty(integrationVO.getDateTime())) {
            throw new ValidationException("[FAILED] Validation Failed Date Time: " + integrationVO.getDateTime());
        }
        if (StringUtils.isEmpty(integrationVO.getChannelId())) {
            throw new ValidationException("[FAILED] Validation Failed Channel Id: " + integrationVO.getChannelId());
        }
    }

    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }
}
