package com.inov8.integration.channel.zindigi.bo;

import com.inov8.integration.channel.JSBookMe.request.JSBookMeRequest;
import com.inov8.integration.channel.zindigi.request.*;
import com.inov8.integration.channel.zindigi.response.*;
import com.inov8.integration.channel.zindigi.service.ZindigiCustomerSyncService;
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

import java.util.Objects;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@Component
public class ZindigiCustomerSyncBO implements I8SBChannelInterface {

    private static Logger logger = LoggerFactory.getLogger(ZindigiCustomerSyncBO.class.getSimpleName());

    @Autowired
    ZindigiCustomerSyncService zindigiCustomerSyncService;

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        Objects.requireNonNull(request).populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Validating request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());

        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            zindigiCustomerSyncService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ZINDIGI_CUSTOMER_SYNC)) {
                response = zindigiCustomerSyncService.zindigiCustomerSyncRequest((ZindigiCustomerSyncRequest) request);

            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_L2_ACCOUNT_UPGRADE_VALIDATION)) {
                response = zindigiCustomerSyncService.sendL2AccountUpgradeValidationResponse((L2AccountUpgradeValidationRequest) request);

            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MinorAccountSync)) {
                response = zindigiCustomerSyncService.sendMinorAccount((MinorAccountSyncRequest) request);

            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ZINDIGI_P2M_STATUS_UPDATE)) {
                response = zindigiCustomerSyncService.sendP2MStatusUpdateResponse((P2MStatusUpdateRequest) request);

            }

            if (Objects.requireNonNull(response).populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseJSON = JSONUtil.getJSON(response);
            Objects.requireNonNull(i8SBSwitchControllerResponseVO).setResponseXML(responseJSON);
        }
        else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        }
        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {

        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setAccessToken(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getAccessToken());

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

    private Object[] initializeRequestAndResponseObjects(String requestType) throws Exception {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);

        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ZINDIGI_CUSTOMER_SYNC)) {
            request = new ZindigiCustomerSyncRequest();
            response = new ZindigiCustomerSyncResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_L2_ACCOUNT_UPGRADE_VALIDATION)) {
            request = new L2AccountUpgradeValidationRequest();
            response = new L2AccountUpgradeValidationResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MinorAccountSync)) {
            request = new MinorAccountSyncRequest();
            response = new MinorAccountSyncResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ZINDIGI_P2M_STATUS_UPDATE)) {
            request = new P2MStatusUpdateRequest();
            response = new P2MStatusUpdateResponse();
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
