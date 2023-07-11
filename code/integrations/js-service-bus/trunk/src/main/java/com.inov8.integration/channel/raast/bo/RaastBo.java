package com.inov8.integration.channel.raast.bo;

import com.inov8.integration.channel.raast.request.*;
import com.inov8.integration.channel.raast.response.*;
import com.inov8.integration.channel.raast.service.RaastService;
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
public class RaastBo implements I8SBChannelInterface {

    @Autowired
    RaastService raastService;
    private static Logger logger = LoggerFactory.getLogger(RaastBo.class.getSimpleName());

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
            raastService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_CustomerAliasAccountID)) {
                response = raastService.customerAliasAccountResponse((CustomerAliasAccountRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetDefaultAccountByAlias)) {
                response = raastService.getDefaultAccountByAliasResponse((GetDefaultAccountByAliasRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerInformation)) {
                response = raastService.getCustomerInformationResponse((GetCustomerInformationRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerAccounts)) {
                response = raastService.getCustomerAccountsResponse((GetCustomerAccountsRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerAliases)) {
                response = raastService.getCustomerAliasesResponse((GetCustomerAliasesRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteAccount)) {
                response = raastService.deleteAccountResponse((DeleteAccountRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteAlias)) {
                response = raastService.deleteAliasResponse((DeleteAliasRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteCustomer)) {
                response = raastService.deleteCustomerResponse((DeleteCustomerRequest) request);
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
            i8SBSwitchControllerRequestVO.setCustomerId(i8SBSwitchControllerRequestVO.getCustomerId());
            i8SBSwitchControllerRequestVO.setAccountId(i8SBSwitchControllerRequestVO.getCustomerId());
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
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_CustomerAliasAccountID)) {
            request = new CustomerAliasAccountRequest();
            response = new CustomerAliasAccountResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetDefaultAccountByAlias)) {
            request = new GetDefaultAccountByAliasRequest();
            response = new CustomerAliasAccountResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerInformation)) {
            request = new GetCustomerInformationRequest();
            response = new GetCustomerInformationResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerAccounts)) {
            request = new GetCustomerAccountsRequest();
            response = new GetCustomerAccountsResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_GetCustomerAliases)) {
            request = new GetCustomerAliasesRequest();
            response = new GetCustomerAliasesResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteAccount)) {
            request = new DeleteAccountRequest();
            response = new DeleteAccountResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteAlias)) {
            request = new DeleteAliasRequest();
            response = new DeleteAliasResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_RAAST_DeleteCustomer)) {
            request = new DeleteCustomerRequest();
            response = new DeleteCustomerResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
