package com.inov8.integration.channel.JSBookMe.bo;

import com.inov8.integration.channel.AppInSnap.request.CustomerDataSet.CustomerTransactionDataSetRequest;
import com.inov8.integration.channel.AppInSnap.request.LoanManagement.LoanRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.enums.ETPaymentCollectionEnum;
import com.inov8.integration.channel.JSBookMe.request.JSBookMeRequest;
import com.inov8.integration.channel.JSBookMe.request.Request;
import com.inov8.integration.channel.JSBookMe.response.JSBBookMeResponse;
import com.inov8.integration.channel.JSBookMe.response.Response;
import com.inov8.integration.channel.JSBookMe.service.JSBookMeService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JSBookMeBo implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(JSBookMeBo.class.getSimpleName());

    @Autowired
    JSBookMeService jsBookMeService;

    private Object[] initializeRequestAndResponseObjects(String requestType) throws Exception {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BookMeIPN)) {
            request = new JSBookMeRequest();
            response = new JSBBookMeResponse();
        }
        else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }

        objects[0] = request;
        objects[1] = response;
        return objects;

    }
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
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Validating request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());

        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            jsBookMeService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BookMeIPN)) {
                response = jsBookMeService.sendJSBookMeRequest((JSBookMeRequest) request);

            }
            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseJSON = JSONUtil.getJSON(response);
            i8SBSwitchControllerResponseVO.setResponseXML(responseJSON);
        }
        else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        }
            return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        return null;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        return null;
    }
}
