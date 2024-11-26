package com.inov8.integration.lending.controller.validator;

import com.inov8.integration.lending.pdu.request.DebitBlockRequest;
import com.inov8.integration.middleware.controller.validator.ValidationException;
import com.inov8.integration.middleware.util.ConfigReader;
import org.apache.commons.lang.StringUtils;

public class LendingHostRequestValidator {

    public static void validateDebitBlock(DebitBlockRequest integrationVO) throws ValidationException {

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
        if (integrationVO.getLienMark().equals("ADD")) {
            if (StringUtils.isEmpty(integrationVO.getLienAmount())) {
                throw new ValidationException("[FAILED] Validation Failed Lien Amount: " + integrationVO.getLienAmount());
            }
        }
        if (StringUtils.isEmpty(integrationVO.getLienMark())) {
            throw new ValidationException("[FAILED] Validation Failed Lien Mark: " + integrationVO.getLienMark());
        }
    }

    public static boolean authenticate(String userName, String password, String channelID) {
        if (ConfigReader.getInstance().getProperty("channel.ids", "").contains(channelID))
            return ConfigReader.getInstance().getProperty("channel.usernames", "").contains(userName) && ConfigReader.getInstance().getProperty("channel.passwords", "").contains(password);
        return Boolean.FALSE;
    }
}
