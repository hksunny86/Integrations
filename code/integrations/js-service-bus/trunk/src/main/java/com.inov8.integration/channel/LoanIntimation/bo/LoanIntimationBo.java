package com.inov8.integration.channel.LoanIntimation.bo;

import com.inov8.integration.channel.LoanIntimation.request.LoanIntimationRequest;
import com.inov8.integration.channel.LoanIntimation.request.Request;
import com.inov8.integration.channel.LoanIntimation.response.LoanIntimationResponse;
import com.inov8.integration.channel.LoanIntimation.response.Response;
import com.inov8.integration.channel.LoanIntimation.service.LoanIntimationService;
import com.inov8.integration.controller.I8SBChannelInterface;
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
public class LoanIntimationBo implements I8SBChannelInterface {
    private static final Logger logger = LoggerFactory.getLogger(LoanIntimationBo.class.getSimpleName());
    @Autowired
    LoanIntimationService loanIntimationService;

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
            loanIntimationService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LOANINTIMATION)){
                response=loanIntimationService.sendLoanIntimation((LoanIntimationRequest) request );
            }

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
        return null;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        return null;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws I8SBValidationException {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request Type " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LOANINTIMATION)){
            request=new LoanIntimationRequest();
            response=new LoanIntimationResponse();
        }

            objects[0] = request;
        objects[1] = response;

        return objects;
    }
}
