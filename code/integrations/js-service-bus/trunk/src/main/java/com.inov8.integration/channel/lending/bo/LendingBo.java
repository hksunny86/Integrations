package com.inov8.integration.channel.lending.bo;

import com.inov8.integration.channel.lending.request.*;
import com.inov8.integration.channel.lending.response.*;
import com.inov8.integration.channel.lending.service.LendingService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LendingBo implements I8SBChannelInterface {

    @Autowired
    LendingService lendingService;
    private static Logger logger = LoggerFactory.getLogger(LendingBo.class.getSimpleName());

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
        Objects.requireNonNull(request).populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN " + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            lendingService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_LOANREPAYMENT)) {
                response = lendingService.loanRepaymentResponse((LoanRepaymentRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_GETLOANOUTSTANDING)) {
                response = lendingService.getLoanOutstandingResponse((GetLoanOutstandingRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_GETACTIVELOAN)) {
                response = lendingService.getActiveLoanResponse((GetActiveLoanRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_SALARY_DISBURSE)) {
                response = lendingService.salaryDisburseResponse((SalaryDisburseRequest) request);
            }

            if (Objects.requireNonNull(response).populateI8SBSwitchControllerResponseVO() != null)
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
        return i8SBSwitchControllerRequestVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(DateFormatEnum.TRANSACTION_DATE.getValue()));
        }

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(DateUtil.formatCurrentDate(DateFormatEnum.TIME_LOCAL_TRANSACTION.getValue()));
        }

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getSTAN());
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
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_LOANREPAYMENT)) {
            request = new LoanRepaymentRequest();
            response = new LoanRepaymentResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_GETLOANOUTSTANDING)) {
            request = new GetLoanOutstandingRequest();
            response = new GetLoanOutstandingResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_GETACTIVELOAN)) {
            request = new GetActiveLoanRequest();
            response = new GetActiveLoanResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LENDING_SALARY_DISBURSE)) {
            request = new SalaryDisburseRequest();
            response = new SalaryDisburseResponse();

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
