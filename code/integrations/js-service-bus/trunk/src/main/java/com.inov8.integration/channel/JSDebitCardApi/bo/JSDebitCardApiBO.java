package com.inov8.integration.channel.JSDebitCardApi.bo;

import com.inov8.integration.channel.JSDebitCardApi.request.*;
import com.inov8.integration.channel.JSDebitCardApi.response.CardReissuanceResponse;
import com.inov8.integration.channel.JSDebitCardApi.response.GetCvvResponse;
import com.inov8.integration.channel.JSDebitCardApi.response.Response;
import com.inov8.integration.channel.JSDebitCardApi.service.JSDebitCardApiService;
import com.inov8.integration.channel.jsdebitcard.bo.JSDebitCardBO;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@Component
public class JSDebitCardApiBO implements I8SBChannelInterface {

    private static final Logger logger = LoggerFactory.getLogger(JSDebitCardBO.class.getSimpleName());
    @Autowired
    JSDebitCardApiService jsDebitCardApiService;

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }

        request.populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN " + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            jsDebitCardApiService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Card)) {
                response = jsDebitCardApiService.sendImportCardRequest((ImportCardRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_UpdateCardStatus)) {

                response = jsDebitCardApiService.sendUpdateCardStatusRequest((UpdateCardStatusRequest) request);

            }else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_CardReissuance)){
                response = jsDebitCardApiService.sendCardReissuanceRequest((CardReissuanceRequest) request);

            }else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GET_CARD_DETAILS)){
                response = jsDebitCardApiService.sendCardDetail((GetCvvRequest) request);

            }
//            else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Customer)) {
//                response = jsDebitCardApiService.sendImportCustomerRequest((ImportCustomerRequest) request);
//            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Account)) {
//                response = jsDebitCardApiService.sendImportAccountRequest((ImportAccountRequest) request);
//            }

            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
            i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
        } else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        }


        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {

        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setAccessToken(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getAccessToken());

        }
        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setExpiryDate(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getExpiryDate());

        }
        return i8SBSwitchControllerRequestVO;
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
        logger.info("Request Type " + requestType);

        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Card)) {
            request = new ImportCardRequest();
            response = new CardReissuanceResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_UpdateCardStatus)) {
            request = new UpdateCardStatusRequest();
            response = new CardReissuanceResponse();
        }
//        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Customer)) {
//            request = new ImportCustomerRequest();
//            response = new CardReissuanceResponse();
//        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_Import_Account)) {
//            request = new ImportAccountRequest();
//            response = new CardReissuanceResponse();
//        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_JSDEBITCARD_CardReissuance)){
            request=new CardReissuanceRequest();
            response=new CardReissuanceResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GET_CARD_DETAILS)){
            request=new GetCvvRequest();
            response=new GetCvvResponse();
        }

        else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;

        return objects;
    }
}
