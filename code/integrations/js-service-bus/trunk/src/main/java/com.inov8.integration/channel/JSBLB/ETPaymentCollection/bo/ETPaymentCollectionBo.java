package com.inov8.integration.channel.JSBLB.ETPaymentCollection.bo;


import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.AssesmentDetails;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.ChallanDetail;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.enums.ETPaymentCollectionEnum;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GenerateChallanRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GetAssesmentDetailRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GetchallanStatusRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.Request;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.response.*;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.service.ETPaymentCollectionService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
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


/**
 * Created by Inov8 on 10/15/2019.
 */
@Component
public class ETPaymentCollectionBo implements I8SBChannelInterface {

    private static Logger logger = LoggerFactory.getLogger(ETPaymentCollectionBo.class.getSimpleName());
    @Autowired
    ETPaymentCollectionService etPaymentCollectionService;

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
        String requestType = i8SBSwitchControllerRequestVO.getRequestType();
        request.populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {
            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            logger.info("Validating request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GetAssesment_Detail)) {
                GetAssesmentDetailResponse assesmentDetailResponse = (GetAssesmentDetailResponse) response;
                AssesmentDetails getAssesmentDetailResponse = etPaymentCollectionService.getAssesmentDetailResponse((GetAssesmentDetailRequest) request, i8SBSwitchControllerRequestVO);
                logger.info("Parsing response for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
                i8SBSwitchControllerResponseVO = assesmentDetailResponse.populateI8SBSwitchControllerResponseVO(getAssesmentDetailResponse);
                String responseXML = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
                i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Generate_Challan)) {
                GenerateChallanResponse generateChallanResponse = (GenerateChallanResponse) response;
                ChallanDetail challanDetailResponse = etPaymentCollectionService.generateChallanResponse((GenerateChallanRequest) request, i8SBSwitchControllerRequestVO);
                logger.info("Parsing response for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
                i8SBSwitchControllerResponseVO = generateChallanResponse.populateI8SBSwitchControllerResponseVO(challanDetailResponse);

                String responseXML = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
                i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Challan_Status)) {
                GetChallanStatusResponse getChallanStatusResponse = (GetChallanStatusResponse) response;
                ChallanDetail challanStatusResponse = new ChallanDetail();
                challanStatusResponse = etPaymentCollectionService.getChallanStatus((GetchallanStatusRequest) request, i8SBSwitchControllerRequestVO);
                logger.info("Parsing response for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
                i8SBSwitchControllerResponseVO = getChallanStatusResponse.populateI8SBSwitchControllerResponseVO(challanStatusResponse);
                String responseXML = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
                i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
            }
        } else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());

        }
        if (i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_GetAssesment_Detail) || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_Generate_Challan) || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_Challan_Status)) {
            if (i8SBSwitchControllerResponseVO.getResponseCode().equals(ETPaymentCollectionEnum.PROCESSED.getValue())) {
                i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED_00.getValue());
            }
        }
        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setChallanNumber(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getVctChallanNumber());

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
            i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
        }
        return i8SBSwitchControllerRequestVO;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws I8SBValidationException {
        Object[] objects = new Object[2];

        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GetAssesment_Detail)) {
            request = new GetAssesmentDetailRequest();
            response = new GetAssesmentDetailResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Generate_Challan)) {
            request = new GenerateChallanRequest();
            response = new GenerateChallanResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Challan_Status)) {
            request = new GetchallanStatusRequest();
            response = new GetChallanStatusResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
