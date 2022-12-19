package com.inov8.integration.channel.fonepay.bo;

import com.inov8.integration.channel.fonepay.request.MPassPaymentRequest;
import com.inov8.integration.channel.fonepay.request.MerchantSummaryRequest;
import com.inov8.integration.channel.fonepay.request.Request;
import com.inov8.integration.channel.fonepay.response.MPassPaymentResponse;
import com.inov8.integration.channel.fonepay.response.MerchantSummaryResponse;
import com.inov8.integration.channel.fonepay.response.Response;
import com.inov8.integration.channel.fonepay.service.FonePayIntegration;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.exception.I8SBValidationException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

/**
 * Created by inov8 on 11/8/2017.
 */
@Component
public class FonePayBO implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(FonePayBO.class.getSimpleName());


    @Value("${fonepay.webservice.userid:#{null}}")
    private String fonepay_webservice_userid;

    @Value("${fonepay.webservice.password:#{null}}")
    private String fonepay_webservice_password;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    @Qualifier("fonePayIntegrationService")
    FonePayIntegration fonePayIntegrationService;

    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        i8SBSwitchControllerRequestVO.setUserId(fonepay_webservice_userid);
        i8SBSwitchControllerRequestVO.setPassword(fonepay_webservice_password);
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType(), i8SBSwitchControllerRequestVO);
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
        logger.info("Validating request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        if (request.validateRequest()) {
            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            String requestXML = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MPASS_MerchantDetail)) {
                response = fonePayIntegrationService.merchantDetail((MerchantSummaryRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MPASS_Payment)) {
                response = fonePayIntegrationService.mPassPayment((MPassPaymentRequest) request);
            }

            logger.info("Parsing response for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
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

        //         generate RRN and other info here based of channel id
        if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_JSBL)||i8SBSwitchControllerRequestVO.getI8sbClientID().equals(I8SBConstants.I8SB_Client_ID_AKBL)) {
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
        }
        return i8SBSwitchControllerRequestVO;
    }


    private Object[] initializeRequestAndResponseObjects(String requestType, I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MPASS_MerchantDetail)) {
            request = new MerchantSummaryRequest(i8SBSwitchControllerRequestVO);

            response = new MerchantSummaryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_MPASS_Payment)) {
            request = new MPassPaymentRequest(i8SBSwitchControllerRequestVO);
            response = new MPassPaymentResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
