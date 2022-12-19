package com.inov8.integration.channel.jsdebitcard.bo;


import com.inov8.integration.channel.jsdebitcard.request.DebitCardExportRequest;
import com.inov8.integration.channel.jsdebitcard.request.DebitCardImportRequest;
import com.inov8.integration.channel.jsdebitcard.request.Request;
import com.inov8.integration.channel.jsdebitcard.response.DebitCardExportResponse;
import com.inov8.integration.channel.jsdebitcard.response.DebitCardImportResponse;
import com.inov8.integration.channel.jsdebitcard.response.Response;
import com.inov8.integration.channel.jsdebitcard.service.JSDebitCardService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;;
import com.inov8.integration.i8sb.vo.*;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@Component
public class JSDebitCardBO implements I8SBChannelInterface {

    @Autowired
    JSDebitCardService jsDebitCardService;
    private static Logger logger = LoggerFactory.getLogger(JSDebitCardBO.class.getSimpleName());

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }

            String requestType = i8SBSwitchControllerRequestVO.getRequestType();

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_EXPORT)) {
                if (request.validateRequest(i8SBSwitchControllerRequestVO)) {

                    logger.info("request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
                    request.buildRequest(i8SBSwitchControllerRequestVO);
                    String requestXML = JSONUtil.getJSON(request);
                    i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
                    response = jsDebitCardService.exportCardsToFTP((DebitCardExportRequest) request);
                }
                else
                {
                    logger.info("[FAILED] request validation");
                    i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.BAD_REQUEST.getValue());
                    i8SBSwitchControllerResponseVO.setDescription("request Validation Failed");
                    return i8SBSwitchControllerResponseVO;
                }
            } else if (requestType.equals(I8SBConstants.RequestType_JSDEBITCARD_IMPORT)) {
                response = jsDebitCardService.importCardsFromFTP();
            }

            i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(response);
            i8SBSwitchControllerResponseVO.setResponseXML(responseXML);


        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        return null;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
            i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
        }
        return i8SBSwitchControllerRequestVO;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws I8SBValidationException {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        // logger.info("request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_EXPORT)) {
            request = new DebitCardExportRequest();
            response = new DebitCardExportResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_IMPORT)) {

            request = new DebitCardImportRequest();
            response = new DebitCardImportResponse();

        }else {
            logger.info("[FAILED] request type not supported");
            throw new I8SBValidationException("request type not supported");
        }

        objects[0] = request;
        objects[1] = response;

        return objects;
    }

}
