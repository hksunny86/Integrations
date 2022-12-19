package com.inov8.microbank.debitcard.util;

import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

public class DebitCardUtill {

    public static final Log logger = LogFactory.getLog(DebitCardUtill.class);

    public static WebServiceVO verifyDebitCard(WebServiceVO webServiceVO, DebitCardModel debitCardModel)
    {
        if(debitCardModel == null)
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_NOT_FOUND);
        /*else if(debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_BLOCKED))
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_BLOCKED);
        else if(debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_DE_ACTIVATED))
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);*/
        else
        {
            webServiceVO.setMobileNo(debitCardModel.getMobileNo());
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        }
        return webServiceVO;
    }

    public static Boolean isValidCardDescription(String cardDescription)
    {
        Boolean isValidCard;
        boolean isValid = GenericValidator.isBlankOrNull(cardDescription) || cardDescription.length() > 19 || cardDescription.length() < 3;
        if(!isValid)
        {
            String tempDecription = cardDescription.replaceAll(" ","");
            isValidCard = StringUtils.isAllUpperCase(tempDecription);
        }
        else
            isValidCard = Boolean.FALSE;
        return isValidCard;
    }
}
