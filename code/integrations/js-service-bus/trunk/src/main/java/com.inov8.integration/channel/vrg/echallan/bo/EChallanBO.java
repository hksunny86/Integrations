package com.inov8.integration.channel.vrg.echallan.bo;

import com.inov8.integration.channel.vrg.echallan.request.BillInquiryRequest;
import com.inov8.integration.channel.vrg.echallan.request.BillPaymentRequest;
import com.inov8.integration.channel.vrg.echallan.request.Request;
import com.inov8.integration.channel.vrg.echallan.response.BillInquiryResponse;
import com.inov8.integration.channel.vrg.echallan.response.BillPaymentResponse;
import com.inov8.integration.channel.vrg.echallan.response.Response;
import com.inov8.integration.channel.vrg.echallan.service.ProcessEchallanSoap;
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
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;


@Component
public class EChallanBO implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(EChallanBO.class.getSimpleName());

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    @Qualifier("eChallanIntegration")
    ProcessEchallanSoap eChallanIntegration;



    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType(),
                i8SBSwitchControllerRequestVO);
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
        if (request.validateRequest()) {
            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            String requestXML = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EChallanInquiry)) {
                this.setupProxyForClient(eChallanIntegration);
                response.setReturnedString(eChallanIntegration.billInquiry(
                        request.getUserID(),
                        request.getPassword(),
                        request.getConsumerNo(),
                        request.getBankMnemonic(),
                        request.getReserved()
                ));
//                response.setReturnedString("00itpayuser                     U20211006+0000000001020+00000000010202109                                        ");

            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EChallanPayment)) {
                this.setupProxyForClient(eChallanIntegration);
                response.setReturnedString(eChallanIntegration.billPayment(
                        request.getUserID(),
                        request.getPassword(),
                        request.getConsumerNo(),
                        ((BillPaymentRequest) request).getTranAuthId(),
                        ((BillPaymentRequest) request).getTransactionAmount(),
                        ((BillPaymentRequest) request).getDate(),
                        ((BillPaymentRequest) request).getTime(),
                        request.getBankMnemonic(),
                        request.getReserved()
                ));
//                response.setReturnedString("00");
            }

            response.parseResponse(i8SBSwitchControllerResponseVO);
            i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(response);
            i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
        } else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        }

        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO,
                                                        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
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

    private Object[] initializeRequestAndResponseObjects(String requestType, I8SBSwitchControllerRequestVO
            i8SBSwitchControllerRequestVO) throws I8SBValidationException {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EChallanInquiry)) {
            request = new BillInquiryRequest(i8SBSwitchControllerRequestVO);
            response = new BillInquiryResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EChallanPayment)) {
            request = new BillPaymentRequest(i8SBSwitchControllerRequestVO);
            response = new BillPaymentResponse();

        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }

    private void setupProxyForClient(Object serviceProxy) {
        Client client = ClientProxy.getClient(serviceProxy);
        HTTPConduit http = (HTTPConduit) client.getConduit();

        // Set the proxy server and port
        http.getClient().setProxyServer("172.16.72.50");
        http.getClient().setProxyServerPort(3128);

        // Optional: Set proxy authentication if required
        http.getProxyAuthorization().setUserName("proxyuser");
        http.getProxyAuthorization().setPassword("proxypassword");
    }
}
