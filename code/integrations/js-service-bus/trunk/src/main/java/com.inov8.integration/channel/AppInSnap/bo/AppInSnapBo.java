package com.inov8.integration.channel.AppInSnap.bo;


import com.inov8.integration.channel.AppInSnap.request.CustomerDataSet.CustomerTransactionDataSetRequest;
import com.inov8.integration.channel.AppInSnap.request.LoanManagement.LoanRequest;
import com.inov8.integration.channel.AppInSnap.request.Request;
import com.inov8.integration.channel.AppInSnap.response.CustomerDataSet.CustomerTransactioDataSetResponse;
import com.inov8.integration.channel.AppInSnap.response.LoanManagement.LoanResponse;
import com.inov8.integration.channel.AppInSnap.response.Response;
import com.inov8.integration.channel.AppInSnap.service.AppInSnapService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
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
public class AppInSnapBo implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(AppInSnapBo.class.getSimpleName());
    @Autowired
    AppInSnapService appInSnapService;

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

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Valiadate Request For RRN" + i8SBSwitchControllerRequestVO.getRRN());
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());
            String requestXML = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            appInSnapService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AppInSnap_CustomerDataSet)) {
                response = appInSnapService.sendCustomerDataSetRequest((CustomerTransactionDataSetRequest) request);
            }else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LoanManagement)){
                response=appInSnapService.sendLoanManagement((LoanRequest)request);
            }
            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(response);
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

    private Object[] initializeRequestAndResponseObjects(String requestType) {
        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AppInSnap_CustomerDataSet)) {
            request = new CustomerTransactionDataSetRequest();
            response = new CustomerTransactioDataSetResponse();
        }else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LoanManagement)){
            request=new LoanRequest();
            response=new LoanResponse();
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
