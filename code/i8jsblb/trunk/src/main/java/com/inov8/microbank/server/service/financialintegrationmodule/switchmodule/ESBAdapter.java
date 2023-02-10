package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.*;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.debitcard.model.DebitCardExportDataViewModel;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import com.inov8.microbank.server.facade.AuditLogFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ESBAdapter {

    protected static Log logger = LogFactory.getLog(ESBAdapter.class);

    public SwitchWrapper makeI8SBCall(SwitchWrapper switchWrapper) throws WorkFlowException {
        I8SBSwitchControllerRequestVO requestVO = switchWrapper.getI8SBSwitchControllerRequestVO();
        PayMtncRequestModel payMtncRequestModel = null;
        AuditLogModel auditLogModel = new AuditLogModel();
        String responseCode = null;
        if (requestVO.getRequestType().equals(I8SBConstants.RequestType_PayMTCN)) {
            if (switchWrapper.getObject("payMtncRequestModel") != null) {
                payMtncRequestModel = (PayMtncRequestModel) switchWrapper.getObject("payMtncRequestModel");
            }
            if (payMtncRequestModel == null) {
                payMtncRequestModel = new PayMtncRequestModel();
                payMtncRequestModel.setCnic(requestVO.getCNIC());
                payMtncRequestModel.setMobileNo(requestVO.getMobilePhone());
                payMtncRequestModel.setRrn(requestVO.getRRN());
                payMtncRequestModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                payMtncRequestModel.setCreatedOn(new Date());
                payMtncRequestModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                payMtncRequestModel.setUpdatedOn(new Date());
                payMtncRequestModel.setIsValid(1L);
                payMtncRequestModel = savePayMtncReqest(payMtncRequestModel);
                switchWrapper.putObject("payMtncRequestModel", payMtncRequestModel);
            }
        }
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        if (requestVO.getFingerTemplete() == null) {
            auditLogModel = this.auditLogBeforeCall(switchWrapper, CommonUtils.getJSON(requestVO));

        } else {


            String inputparam = CommonUtils.getJSON(requestVO);
            JSONObject json = new JSONObject(inputparam);
            json.put("fingerTemplete", "**********");
            auditLogModel = this.auditLogBeforeCall(switchWrapper, json.toString(1));
        }
        try {
            I8SBSwitchController switchController = HttpInvokerUtil.getHttpInvokerFactoryBean(I8SBSwitchController.class,
                    MessageUtil.getMessage("I8SB.URL"));

            if (switchController == null) {
                logger.error("Unable to load I8SBSwitchController...");
                responseCode = WorkFlowErrorCodeConstants.GENERAL_ERROR;
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
            }
            requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
            responseVO = requestVO.getI8SBSwitchControllerResponseVO();
            responseCode = responseVO.getResponseCode();
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.SUCCESS);
            auditLogModel.setIntegrationPartnerIdentifier(responseVO.getRequestingTranCode());
//            parseResponseCode(responseVO);
            switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            switchWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        } catch (Exception ex) {
            responseCode = WorkFlowErrorCodeConstants.FAILURE;
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            logger.error("Exception occured in ESBAdapterImpl.invokeI8SwitchController: " + ex.getMessage(), ex);
//            if(ex.getMessage().contains(WorkFlowErrorCodeConstants.REMOTE_CONNECTION_FAILURE_PREFIX) || ex.getMessage().contains(WorkFlowErrorCodeConstants.REMOTE_ACCESS_FAILURE_PREFIX))
//            {
//                auditLogModel.setCustomField2(WorkFlowErrorCodeConstants.GENERAL_ERROR);
//                auditLogModel.setCustomField3(ex.getMessage());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
//            }
//            throw new WorkFlowException(ex.getMessage());
        } finally {
//            this.auditLogAfterCall( auditLogModel, xStream.toXML(olaVO) ) ;
            if (requestVO != null) {
                if (requestVO.getFingerTemplete() == null) {
                    this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(requestVO));
                } else {
                    String inputparam = CommonUtils.getJSON(requestVO);
                    JSONObject json = new JSONObject(inputparam);
                    json.put("fingerTemplete", "**********");
                    this.auditLogAfterCall(auditLogModel, json.toString(1));
                }
//                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(requestVO));
                if (requestVO.getRequestType().equals(I8SBConstants.RequestType_PayMTCN)) {
                    payMtncRequestModel.setThirdPartyResponseCode(responseCode);
                    payMtncRequestModel.setUpdatedOn(new Date());
                    if (responseCode.equals("I8SB-200"))
                        payMtncRequestModel.setIsValid(0L);
                    savePayMtncReqest(payMtncRequestModel);
                }
            }
            logger.info("Response received from I8SB at Time :: " + new Date());
        }
        return switchWrapper;
    }

    public static I8SBSwitchControllerRequestVO prepareCollectionInquiryRequest(String consumerNumber, ProductModel productModel) {
        String bankMnemonic = productModel.getProductCode();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setConsumerNumber(consumerNumber);
        if (bankMnemonic.equalsIgnoreCase(MessageUtil.getMessage("VALLENCIA_BANK_MNEMONIC"))) {
            requestVO.setUserId(MessageUtil.getMessage("VALLENCIA_USERNAME"));
            requestVO.setPassword(MessageUtil.getMessage("VALLENCIA_PASSWORD"));
        } else {
            requestVO.setUserId(MessageUtil.getMessage("VRG_USERNAME"));
            requestVO.setPassword(MessageUtil.getMessage("VRG_PASSWORD"));
        }
        requestVO.setBankMnemonic(bankMnemonic);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_VRG_ECHALLAN);
        requestVO.setRequestType(I8SBConstants.RequestType_EChallanInquiry);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Collection Payment Inquiry - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareOfflineBillerInquiryRequest(String consumerNumber, ProductModel productModel) {
        String bankMnemonic = productModel.getProductCode();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setConsumerNumber(consumerNumber);
//        requestVO.setBankMnemonic(bankMnemonic);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_OFFLINE_BILLER);
        requestVO.setRequestType(I8SBConstants.RequestType_BillInquiry);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Collection Payment Inquiry - params", requestVO);

        return requestVO;
    }


    public static I8SBSwitchControllerRequestVO prepareRefferalCustomerRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_REFFERAL_CUSTOMER);
        requestVO.setRequestType(requestType);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Refferal Customer - params", requestVO);

        return requestVO;
    }


    public static I8SBSwitchControllerRequestVO prepareCustomerDeviceVerificationRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_CUSTOMER_DEVICE_VERIFICATION);
        requestVO.setRequestType(requestType);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Device Verification Customer - params", requestVO);

        return requestVO;
    }


    //getAssessmentDetails
    public static I8SBSwitchControllerRequestVO prepareGetAssessmentDetailsRequest(String vehicleRegNo, String chesisNo, String mobileNo, String bankMnemonic) {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setUserId(MessageUtil.getMessage("VRG_USERNAME"));
        requestVO.setPassword(MessageUtil.getMessage("VRG_PASSWORD"));

        requestVO.setVehicleRegistrationNumber(vehicleRegNo);
        requestVO.setChesisNumber(chesisNo);
        requestVO.setMobileNumber(mobileNo);
        requestVO.setBankMnemonic(bankMnemonic);

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ETPaymentCollection);
        requestVO.setRequestType(I8SBConstants.RequestType_GetAssesment_Detail);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Get Assessment Details - params", requestVO);

        return requestVO;
    }

    //generateChallan Call
    public static I8SBSwitchControllerRequestVO prepareGenerateChallanRequest(String assessmentNo, String vehicleRegNo, String assessmentTotalAmount,
                                                                              String challanStatus, String bankMnemonic,
                                                                              String transactionId, String customerMobileNumber) {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setUserId(MessageUtil.getMessage("VRG_USERNAME"));
        requestVO.setPassword(MessageUtil.getMessage("VRG_PASSWORD"));

        requestVO.setAssessmentNumber(assessmentNo);
        requestVO.setVehicleRegistrationNumber(vehicleRegNo);
        requestVO.setAssesmentTotalAmount(assessmentTotalAmount);
        requestVO.setChallanStatus(challanStatus);
        requestVO.setTransactionId(transactionId);
        requestVO.setMobileNumber(customerMobileNumber);
        requestVO.setBankMnemonic(bankMnemonic);

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ETPaymentCollection);
        requestVO.setRequestType(I8SBConstants.RequestType_Generate_Challan);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Generate Challan - params", requestVO);

        return requestVO;
    }

    //challanStatus call
    public static I8SBSwitchControllerRequestVO prepareChallanStatusRequest(String vehicleRegNo, String challanNumber, String bankMnemonic) {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setUserId(MessageUtil.getMessage("VRG_USERNAME"));
        requestVO.setPassword(MessageUtil.getMessage("VRG_PASSWORD"));

        requestVO.setVehicleRegistrationNumber(vehicleRegNo);
        requestVO.setChallanNumber(challanNumber);
        requestVO.setBankMnemonic(bankMnemonic);

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ETPaymentCollection);
        requestVO.setRequestType(I8SBConstants.RequestType_Challan_Status);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Challan Status - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO preparePayMTNCRequest(String cnic, String mobile, String address, String name, String lName, String customerCity) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setCNIC(cnic);
        requestVO.setMobilePhone(mobile);
        requestVO.setAddress(address);
        requestVO.setFirstName(name);
        requestVO.setLastName(lName);
        requestVO.setSenderCity(customerCity);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_PayMTCN);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB PayMTNC - params", requestVO);

        return requestVO;
    }

    private PayMtncRequestModel savePayMtncReqest(PayMtncRequestModel model) throws WorkFlowException {
        return getPayMtncRequestDAO().saveOrUpdate(model);
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForDebitCard(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARD);
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForUpdateAccountToBlink(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ZINDIGI);
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForMinorCustomer(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ZINDIGI);
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForDebitCardReIssuance(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_VISION);
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForUpdateCardStatus(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARDAPI);
        requestVO.setRequestType(requestType);
        return requestVO;
    }


    public static I8SBSwitchControllerRequestVO prepareRequestVoForApiGee(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf(100000 + (new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForBOP(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_BOPBLB);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);
        return requestVO;
    }


//    public static I8SBSwitchControllerRequestVO prepareRequestVoForDebitCardActivation(String requestType) {
//        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
//        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
//        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
//        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
//        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
//        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
//        requestVO.setRequestType(requestType);
//        return requestVO;
//    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForJsBookMe(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSBOOKME);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareCLSRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_CLSJS);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        logCompleteObject("I8SB CLS request - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareMerchantCampingRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_MERCHANTCAMPING);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setRequestType(requestType);

        logCompleteObject("I8SB Merchant Camping request - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareUpdateAma(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_AMMA);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        logCompleteObject("I8SB Update AMA request - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareEoceanRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_EOCEAN);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        logCompleteObject("I8SB Eocean request - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareM3TechSmsRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_M3TECH);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        logCompleteObject("I8SB Eocean request - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareZindigiCustomerSyncRequest(String fullName, String mobileNo, String cnic, String cnicIssuanceDate, String cnicExpiryDate,
                                                                                  String gender, String dob, String address, String email, String fatherName, String motherName,
                                                                                  String mobileNetwork) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setFullName15(fullName);
        requestVO.setMobileNumber(mobileNo);
        requestVO.setCNIC(cnic);
        requestVO.setCnicIssuanceDate(cnicIssuanceDate);
        requestVO.setCnicExpiry(cnicExpiryDate);
        requestVO.setGenderCode(gender);
        requestVO.setDateOfBirth(dob);
        requestVO.setAddress(address);
        requestVO.setEmail(email);
        requestVO.setFatherName(fatherName);
        requestVO.setMotherMaidenName(motherName);
        requestVO.setMobileNetwork(mobileNetwork);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ZINDIGI);
        requestVO.setRequestType(I8SBConstants.RequestType_ZINDIGI_CUSTOMER_SYNC);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyMMHHss").format(new Date()));
        requestVO.setSTAN(String.valueOf((new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB CLS request - params", requestVO);

        return requestVO;
    }

    private static String prepareNicExpiry(String cNicExpiry) {
        String splitVal = null;
        String[] parts = null;
        if (cNicExpiry.contains(" ") || cNicExpiry.contains("T"))
            parts = cNicExpiry.split(" ");
        String originalDate = parts[0];
        String[] dateParts = originalDate.split("-");
        return dateParts[0] + dateParts[1] + dateParts[2];
    }

    public static I8SBSwitchControllerRequestVO prepareDebitCardIssuenceRequest(I8SBSwitchControllerRequestVO requestVO, List<DebitCardExportDataViewModel> modelList) {
        String firstName = null;
        String lastName = null;
        String[] parts;
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<AccountVO> accountVOList = new ArrayList<>();
        List<DebitCardVO> debitCardVOList = new ArrayList<>();
        List<CustomerAccountVO> customerAccountVOList = new ArrayList<>();
        CustomerVO customerVO;
        AccountVO accountVO;
        DebitCardVO debitCardVO;
        CustomerAccountVO customerAccountVO;
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        HashMap<String, Object> objectHashMap = new HashMap<>();
        for (DebitCardExportDataViewModel model : modelList) {
            parts = model.getCardEmbossingName().split(" ");
            firstName = parts[0];
            if (parts.length > 1)
                lastName = model.getCardEmbossingName().substring(firstName.length() + 1);
            else
                lastName = null;
            customerVO = new CustomerVO();
            customerVO.setNationalityCode(model.getCustomerCountryCode());
            customerVO.setFirstName(firstName);
            customerVO.setLastName(lastName);
            customerVO.setRelationshipNo(model.getcNic());
            customerVO.setCustomerCNIC(model.getcNic());
            customerVO.setCustomerTypeCode(MessageUtil.getMessage("debit.card.customer.type.code"));
            customerVO.setBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
            customerVO.setSegmantCode(MessageUtil.getMessage("debit.card.segment.code"));
            customerVO.setCustomerStatusCode(model.getCustomerStatusCode());
            customerVO.setFullName(model.getCardEmbossingName());
            customerVO.setGenderCode(model.getGenderCode());
            customerVO.setDateOfBirth(createNewDateFormat.getFormattedDate(model.getDob()));
            customerVO.setPlaceOfBirth(model.getBirthPlace());
            customerVO.setMotherMaidenName(model.getMotherMaidenName());
            customerVO.setFatherName(model.getFatherHusbandName());
            customerVO.setOfficeAddress1(model.getDebitCardMailingAddress());
            customerVO.setOfficeAddress2(model.getDebitCardMailingAddress());
            customerVO.setHomeCity(model.getCustomerCity());
            customerVO.setHomeAddress1(model.getDebitCardMailingAddress());
            customerVO.setHomeAddress2(model.getDebitCardMailingAddress());
            customerVO.setTempAddress1(model.getDebitCardMailingAddress());
            customerVO.setTempAddress2(model.getDebitCardMailingAddress());
            customerVO.setMailingAddress(model.getDebitCardMailingAddress());
            customerVO.setOtherAddress1(model.getDebitCardMailingAddress());
            customerVO.setOtherAddress2(model.getDebitCardMailingAddress());
            customerVO.setReserved3(prepareNicExpiry(model.getcNicExpiryDate()));
            customerVO.setMobilePhone(model.getMobileNo());
            customerVOList.add(customerVO);
            //
            accountVO = new AccountVO();
            accountVO.setAccountNo(model.getMobileNo());
            accountVO.setAccountTypeCode(MessageUtil.getMessage("debit.card.account.type.code"));
            accountVO.setAccountStatusCode(model.getAccountStatusCode());
            accountVO.setBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
            accountVO.setCurrencyCode(model.getCurrencyCode());
            accountVO.setAccountTitle(model.getCardEmbossingName());
            accountVO.setIBAN(model.getIban());
            accountVO.setAddress1(model.getDebitCardMailingAddress());
            accountVO.setAddress2(model.getDebitCardMailingAddress());
            accountVO.setIntroAddress(model.getDebitCardMailingAddress());
            accountVO.setCity(model.getCustomerCity());
            accountVOList.add(accountVO);
            //
            debitCardVO = new DebitCardVO();
            debitCardVO.setRelationShipNo(model.getcNic());
            debitCardVO.setCardEmborsingName(model.getCardEmbossingName());
            debitCardVO.setAccountNo(model.getMobileNo());
            if (model.getCardProductCode().equals("002")) {
                debitCardVO.setCardTypeCode(MessageUtil.getMessage("debit.card.type.code"));
                debitCardVO.setCardProdCode(MessageUtil.getMessage("debit.card.prod.code"));
            } else {
                debitCardVO.setCardTypeCode(model.getCardTypeCode());
                debitCardVO.setCardProdCode(model.getCardProductCode());
            }
            debitCardVO.setCardBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
            debitCardVO.setPrimaryCNIC(model.getcNic());
            debitCardVO.setIssuedDate(createNewDateFormat.getFormattedDate(new Date()));
            debitCardVO.setRequestType(CardConstantsInterface.DEBIT_CARD_ONLINE_RQST_TYPE_CODE);
            debitCardVOList.add(debitCardVO);
            //
            customerAccountVO = new CustomerAccountVO();
            customerAccountVO.setRelationshipNo(model.getcNic());
            customerAccountVO.setAccountNo(model.getMobileNo());
            customerAccountVOList.add(customerAccountVO);
        }
        objectHashMap.put(I8SBKeysOfCollectionEnum.Customer.getValue(), customerVOList);
        objectHashMap.put(I8SBKeysOfCollectionEnum.Account.getValue(), accountVOList);
        objectHashMap.put(I8SBKeysOfCollectionEnum.DebitCard.getValue(), debitCardVOList);
        objectHashMap.put(I8SBKeysOfCollectionEnum.CustomerAccount.getValue(), customerAccountVOList);
        requestVO.setCollectionOfList(objectHashMap);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForAppInSnap(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APPINSNAP);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);
        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareRequestVoForDebitCardReissuance(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARDAPI);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareCollectionPaymentRequest(String consumerNumber, String billAmount, ProductModel productModel) {
        String bankMnemonic = productModel.getProductCode();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setConsumerNumber(consumerNumber);
        requestVO.setTransactionAmount(billAmount);
        if (bankMnemonic.equalsIgnoreCase(MessageUtil.getMessage("VALLENCIA_BANK_MNEMONIC"))) {
            requestVO.setUserId(MessageUtil.getMessage("VALLENCIA_USERNAME"));
            requestVO.setPassword(MessageUtil.getMessage("VALLENCIA_PASSWORD"));
        } else {
            requestVO.setUserId(MessageUtil.getMessage("VRG_USERNAME"));
            requestVO.setPassword(MessageUtil.getMessage("VRG_PASSWORD"));
        }
        requestVO.setBankMnemonic(bankMnemonic);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_VRG_ECHALLAN);
        requestVO.setRequestType(I8SBConstants.RequestType_EChallanPayment);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Collection Payment - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareOfflineBillerPaymentRequest(String consumerNumber,ProductModel productModel) {
        String bankMnemonic = productModel.getProductCode();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setConsumerNumber(consumerNumber);
//        requestVO.setTransactionAmount(billAmount);
        requestVO.setBankMnemonic(bankMnemonic);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_OFFLINE_BILLER);
        requestVO.setRequestType(I8SBConstants.RequestType_BillPayment);

        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());

        logCompleteObject("I8SB Collection Payment - params", requestVO);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareTitleFetchForSalaryDisbursement(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_T24API);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareFundsTransferForSalaryDisbursement(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_T24API);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setDateLocalTransaction(new SimpleDateFormat("MMdd").format(new Date()));
        requestVO.setTimeLocalTransaction(new SimpleDateFormat("HHmmss").format(new Date()));
        requestVO.setTransactionDate(new SimpleDateFormat("MMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        return requestVO;
    }

    public static I8SBSwitchControllerRequestVO prepareAdvanceSalaryLoanIntimationRequest(String requestType) {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_LOANINTIMATION);
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMM").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestType(requestType);

        return requestVO;
    }

    public static void logCompleteObject(String desc, Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            String jsonRequestString = mapper.writeValueAsString(obj);
            logger.info("\n\n***********\n** " + desc + " **\n" + jsonRequestString + "\n***********");
        } catch (Exception e) {
            logger.error("Exception occurred while printing log... Exception details:", e);
        }
    }

    public static String processI8sbResponseCode(I8SBSwitchControllerResponseVO responseVO, boolean isInquiry) throws WorkFlowException {

        logCompleteObject("[processI8sbResponseCode] I8SB Response Object - params", responseVO);
        String responseCode = null;
        if (responseVO == null) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (null == responseVO.getResponseCode()) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb ErrorCode is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (responseVO.getResponseCode().equals(WorkFlowErrorCodeConstants.I8SB_SUCCESS_CODE)) {
            logger.info("[ESBAdapter.processI8sbResponseCode] Successful response received from i8sb : " + responseVO.getResponseCode());
        } else if (responseVO.getResponseCode().startsWith(WorkFlowErrorCodeConstants.I8SB_PREFIX)) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb internal Error response received : " + responseVO.getResponseCode());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else {
            responseCode = responseVO.getResponseCode();
            String errorMessageType = isInquiry ? "info" : "payment";
            String errorMessage = "";
            String errorCode = "";
            logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response received : " + responseCode);
            String msgKey = "i8sb.response." + errorMessageType + "." + responseCode;
            errorMessage = MessageUtil.getMessage(msgKey);
            if (StringUtil.isNullOrEmpty(errorMessage)) {
                logger.error("[ESBAdapter.processI8sbResponseCode] mapping against key not found: " + msgKey + " in applicationresources.properties");
                errorMessage = responseVO.getDescription();
            }
            if (responseCode.equals("009")) {
                errorCode = responseCode;
                throw new WorkFlowException(errorMessage, errorCode);
            }

            if (errorMessage == null) {
                throw new WorkFlowException("Service unavailable due to technical difficulties.");
            } else {
                logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response description : " + errorMessage);
                //if(!errorMessage.contains("Finger") && !errorMessage.contains("finger"))
                throw new WorkFlowException(errorMessage);
            }
        }
        return responseCode;
    }

    public static void processI8sbResponseCodeForEOBI(I8SBSwitchControllerResponseVO responseVO, boolean isInquiry) throws WorkFlowException {

        logCompleteObject("[processI8sbResponseCode] I8SB Response Object - params", responseVO);

        if (responseVO == null) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (null == responseVO.getResponseCode()) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb ErrorCode is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (responseVO.getResponseCode().equals(WorkFlowErrorCodeConstants.I8SB_SUCCESS_CODE)) {
            logger.info("[ESBAdapter.processI8sbResponseCode] Successful response received from i8sb : " + responseVO.getResponseCode());
        } else if (responseVO.getResponseCode().startsWith(WorkFlowErrorCodeConstants.I8SB_PREFIX)) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb internal Error response received : " + responseVO.getResponseCode());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else {
            String errorMessageType = isInquiry ? "inquiry" : "payment";
            String errorMessage = "";
            logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response received : " + responseVO.getResponseCode());
            String msgKey = "i8sb.response." + errorMessageType + "." + responseVO.getResponseCode();
            errorMessage = MessageUtil.getMessage(msgKey);
            if (StringUtil.isNullOrEmpty(errorMessage)) {
                logger.error("[ESBAdapter.processI8sbResponseCode] mapping against key not found: " + msgKey + " in applicationresources.properties");
                errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;
            }
            logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response description : " + errorMessage);
            //if(!errorMessage.contains("Finger") && !errorMessage.contains("finger"))
            throw new WorkFlowException(errorMessage);
        }
    }


    public static String processI8sbResponseCodeForBookMe(I8SBSwitchControllerResponseVO responseVO, boolean isInquiry) throws WorkFlowException {

        logCompleteObject("[processI8sbResponseCode] I8SB Response Object - params", responseVO);
        String responseCode = null;
        if (responseVO == null) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (null == responseVO.getResponseCode()) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb ErrorCode is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (responseVO.getResponseCode().equals(WorkFlowErrorCodeConstants.I8SB_SUCCESS_CODE)) {
            logger.info("[ESBAdapter.processI8sbResponseCode] Successful response received from i8sb : " + responseVO.getResponseCode());
        } else if (responseVO.getResponseCode().startsWith(WorkFlowErrorCodeConstants.I8SB_PREFIX)) {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb internal Error response received : " + responseVO.getResponseCode());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else {
            responseCode = responseVO.getResponseCode();
            String errorMessageType = isInquiry ? "info" : "payment";
            String errorMessage = "";
            String errorCode = "";
            logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response received : " + responseCode);
            String msgKey = "i8sb.response." + errorMessageType + "." + responseCode;
            errorMessage = MessageUtil.getMessage(msgKey);
            if (StringUtil.isNullOrEmpty(errorMessage)) {
                logger.error("[ESBAdapter.processI8sbResponseCode] mapping against key not found: " + msgKey + " in applicationresources.properties");
                errorMessage = responseVO.getDescription();
            }
            if (responseCode.equals("009")) {
                errorCode = responseCode;
                throw new WorkFlowException(errorMessage, errorCode);
            }
            if (!responseCode.equals("500")) {
                logger.error("[ESBAdapter.processI8sbResponseCode] 3rd Party Error response description : " + errorMessage);
                //if(!errorMessage.contains("Finger") && !errorMessage.contains("finger"))
                throw new WorkFlowException(errorMessage);
            }
        }
        return responseCode;
    }

    public static void processQRResponseCode(I8SBSwitchControllerResponseVO responseVO, boolean isInquiry) throws WorkFlowException {

        ESBAdapter.logCompleteObject("[processQRResponseCode] isInquiry=" + isInquiry + " I8SB Response Object - params", responseVO);

        if (responseVO == null) {
            logger.error("[processQRResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else if (responseVO.getResponseCode().equals(WorkFlowErrorCodeConstants.I8SB_SUCCESS_CODE)) {
            logger.info("[processQRResponseCode] Successful response received from i8sb : " + responseVO.getResponseCode());
        } else if (responseVO.getResponseCode().startsWith(WorkFlowErrorCodeConstants.I8SB_PREFIX)) {
            logger.error("[processQRResponseCode] i8sb internal Error response received : " + responseVO.getResponseCode());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        } else {
            if (!StringUtil.isNullOrEmpty(responseVO.getError())) {
                logger.error("[processQRResponseCode] i8sb 3rd party Error response received code: " + responseVO.getResponseCode() + " error: " + responseVO.getError());
                throw new WorkFlowException(responseVO.getError());
            } else {
                logger.error("[processQRResponseCode] i8sb 3rd party Error response received code: " + responseVO.getResponseCode() + " error: " + responseVO.getError());
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
            }
        }


    }

    public AuditLogModel auditLogBeforeCall(SwitchWrapper switchWrapper, String inputParam) throws WorkFlowException {
        AuditLogModel auditLogModel = new AuditLogModel();
        auditLogModel.setTransactionStartTime(new Timestamp(System.currentTimeMillis()));
        auditLogModel.setIntegrationModuleId(IntegrationModuleConstants.SWITCH_MODULE);

        if (switchWrapper.getTransactionTransactionModel() != null
                && switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel() != null) {
            auditLogModel
                    .setTransactionCodeId(switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());
            switchWrapper.getI8SBSwitchControllerRequestVO().setTranCode(switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
        }
        auditLogModel.setInputParam(inputParam);
        auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

        if (ThreadLocalActionLog.getActionLogId() == null) {
            auditLogModel.setActionLogId(1l);
        }
        if (switchWrapper.getI8SBSwitchControllerRequestVO() != null) {
            I8SBSwitchControllerRequestVO requestVO = switchWrapper.getI8SBSwitchControllerRequestVO();
            if (requestVO.getRequestType().equals(I8SBConstants.RequestType_CashWithdrawal))
                auditLogModel.setCustomField4(requestVO.getRequestType());
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(auditLogModel);
        try {
            auditLogModel = (AuditLogModel) this.getAuditLogModuleFacade().auditLogRequiresNewTransaction(baseWrapper).getBasePersistableModel();
        } catch (FrameworkCheckedException ex) {
            throw new WorkFlowException(ex.getMessage(), ex);
        }

        return auditLogModel;
    }

    public void auditLogAfterCall(AuditLogModel auditLogModel, String outputParam) throws WorkFlowException {
        auditLogModel.setTransactionEndTime(new Timestamp(System.currentTimeMillis()));
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        auditLogModel.setOutputParam(outputParam);
        baseWrapper.setBasePersistableModel(auditLogModel);

        try {
            this.getAuditLogModuleFacade().auditLogRequiresNewTransaction(baseWrapper);
        } catch (FrameworkCheckedException ex) {
            throw new WorkFlowException(ex.getMessage(), ex);
        }
    }

    public AuditLogFacadeImpl getAuditLogModuleFacade() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (AuditLogFacadeImpl) applicationContext.getBean("auditLogModuleFacade");
    }

    public PayMtncRequestDAO getPayMtncRequestDAO() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (PayMtncRequestDAO) applicationContext.getBean("payMtncRequestDAO");
    }

}