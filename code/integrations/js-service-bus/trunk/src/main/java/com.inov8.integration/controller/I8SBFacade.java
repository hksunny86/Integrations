package com.inov8.integration.controller;

import com.inov8.integration.channel.AppInSnap.enums.AppInSnapEnum;
import com.inov8.integration.channel.fonepay.enums.FonePay_ResponseCodeEnum;
//import com.inov8.integration.channel.rdv.mb.enums.RDVMB_ResponseCodeEnum;
//import com.inov8.integration.channel.tutuka.enums.Tutuka_ResponseCodeEnum;
import com.inov8.integration.channel.vrg.echallan.enums.VRG_EChallan_ResponseCodeEnum;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.enums.I8SBTransactionStatus;
import com.inov8.integration.exception.I8SBServiceNotAvailableException;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
import org.apache.commons.lang.BooleanUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * Created by inov8 on 8/28/2017.
 */
@Component
public class I8SBFacade {

    private static Logger logger = LoggerFactory.getLogger(I8SBFacade.class.getSimpleName());

    @Autowired
    I8SBBO i8SBBO;

    public I8SBSwitchControllerResponseVO process(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        if (i8SBBO.validateMandatoryI8SBSwitchControllerRequestVOParameters(i8SBSwitchControllerRequestVO)) {
            I8SBChannelInterface i8SBChannelInterface = i8SBBO.initializeChannel(i8SBSwitchControllerRequestVO);
            logger.info("Checking if linked requests exist");
            Map<String, Object> linkedRequests = i8SBBO.getLinkedRequests(i8SBSwitchControllerRequestVO);
            if (linkedRequests != null && linkedRequests.size() > 0) {
                logger.info("Linked requests found..");
                return processLinkedRequest(i8SBSwitchControllerRequestVO, linkedRequests, i8SBChannelInterface);
            } else {
                return processRequest(i8SBSwitchControllerRequestVO, i8SBChannelInterface);
            }
        } else {
            return i8SBBO.getI8SBSwitchControllerResponseVO();
        }
    }

    public I8SBSwitchControllerResponseVO processRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBChannelInterface i8SBChannelInterface) {
        logger.info("Processing request: " + i8SBSwitchControllerRequestVO.getRequestType());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        // All exception handling and transaction logging in database
        try {

            String i8sbRequestXml = JSONUtil.getJSON(i8SBSwitchControllerRequestVO);
            JSONObject json = new JSONObject(i8sbRequestXml);
            json.put("OPT", "****");
            logger.info("CashOut Request Recieved at Controller:" + "\n" + json);
            if (i8SBSwitchControllerRequestVO.getCollectionOfList().isEmpty()) {
                i8SBSwitchControllerRequestVO.setRequestXML(i8sbRequestXml);
            } else {
                i8SBSwitchControllerRequestVO.setRequestXML(String.valueOf(i8SBSwitchControllerRequestVO.getCollectionOfList()));
            }


            i8SBBO.generateSystemTraceableInfo(i8SBSwitchControllerRequestVO, i8SBChannelInterface);
            //saving request into i8sb db in transaction_log table
            i8SBBO.insertTransactionLog(i8SBSwitchControllerRequestVO);
            //i8SBSwitchControllerRequestVO.setRequestXML(null);

            // If I8SBSwitchControllerRequestVO already contain I8SBSwitchControllerResponseVO then request wont be sent to channel for execution.
            // This is just for database transaction logging. Because it means already some failure occurred.
            boolean allowRequestExecution = true;
            if (i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO() != null && i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO().getResponseCode() != null) {
                allowRequestExecution = false;
            }

            if (allowRequestExecution) {
                i8SBSwitchControllerResponseVO = i8SBBO.execute(i8SBSwitchControllerRequestVO, i8SBChannelInterface);
            } else {
                i8SBSwitchControllerResponseVO = i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO();
            }

            i8SBSwitchControllerResponseVO.setStatus(I8SBTransactionStatus.COMPLETED.getValue().toString());
            String responseCode = i8SBSwitchControllerResponseVO.getResponseCode();
            logger.info("response code for request " + i8SBSwitchControllerRequestVO.getRequestType() + " is " + responseCode);
            i8SBSwitchControllerResponseVO.setResponseCode(responseCode);
            if (responseCode != null && (
                    responseCode.equals(I8SBResponseCodeEnum.PROCESSED_000.getValue())
                            || responseCode.equals(I8SBResponseCodeEnum.PROCESSED_0000.getValue())
                            || responseCode.equals(I8SBResponseCodeEnum.PROCESSED_00.getValue())
                            || responseCode.equals(I8SBResponseCodeEnum.PROCESSED_00000.getValue())
            )) {
                logger.info("Setting I8SB response code: " + I8SBResponseCodeEnum.PROCESSED.getValue());
                i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
            }

        } catch (Exception ex) {
            logger.error("request processing failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            logger.error("request Type: " + i8SBSwitchControllerRequestVO.getRequestType());
            logger.error("Exception occurred ", ex);
            i8SBSwitchControllerResponseVO.setStatus(I8SBTransactionStatus.EXCEPTION.getValue().toString());
            i8SBSwitchControllerResponseVO.setError(ex.getMessage());


            boolean isTimeout = false;
            for (StackTraceElement element : ex.getStackTrace()) {
                if (element.getClassName() != null) {
                    if (element.getClassName().equals("SocketTimeoutException")) {
                        isTimeout = true;
                        break;
                    } else if (element.getClassName().equals("TimeoutException")) {
                        isTimeout = true;
                        break;
                    }
                }
            }

            if (ex instanceof I8SBValidationException) {
                if (i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MasterCard)
                        && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_OTHER)
                        && i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Channel_ID_MICROBANK)) {
//                    i8SBSwitchControllerResponseVO.setResponseCode(Tutuka_ResponseCodeEnum.DO_NOT_HONOR.getValue());
                } else
                    i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.BAD_REQUEST.getValue());
            } else if (isTimeout) {
                if (i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MasterCard)
                        && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_OTHER)
                        && i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Channel_ID_MICROBANK)) {
//                    i8SBSwitchControllerResponseVO.setResponseCode(Tutuka_ResponseCodeEnum.TRANSACTION_TIMEOUT.getValue());
                } else
                    i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.TIME_OUT.getValue());
            } else if (ex instanceof I8SBServiceNotAvailableException) {
                if (i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MasterCard)
                        && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_OTHER)
                        && i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Channel_ID_MICROBANK)) {
//                    i8SBSwitchControllerResponseVO.setResponseCode(Tutuka_ResponseCodeEnum.TRANSACTION_TIMEOUT.getValue());
                } else
                    i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.TIME_OUT.getValue());
            } else {
                if (i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MasterCard)
                        && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID().equalsIgnoreCase(I8SBConstants.I8SB_Client_Terminal_ID_OTHER)
                        && i8SBSwitchControllerRequestVO.getI8sbChannelID().equalsIgnoreCase(I8SBConstants.I8SB_Channel_ID_MICROBANK)) {
//                    i8SBSwitchControllerResponseVO.setResponseCode(Tutuka_ResponseCodeEnum.DO_NOT_HONOR.getValue());
                } else
                    i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            }
        } finally {
            try {
                i8SBBO.updateTransactionLog(i8SBSwitchControllerRequestVO, i8SBSwitchControllerResponseVO);
            } catch (Exception ex) {
                logger.error("Exception occurred ", ex);
                i8SBSwitchControllerResponseVO.setStatus(I8SBTransactionStatus.EXCEPTION.getValue().toString());
                i8SBSwitchControllerResponseVO.setError(ex.getMessage());
                i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            }
            i8SBSwitchControllerRequestVO.setRequestXML(null);
            i8SBSwitchControllerResponseVO.setResponseXML(null);
        }
        logger.info("request processed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        return i8SBSwitchControllerResponseVO;
    }

    public I8SBSwitchControllerResponseVO processLinkedRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, Map<String, Object> linkedRequest, I8SBChannelInterface i8SBChannelInterface) {
        I8SBSwitchControllerResponseVO accumulatedI8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        String[] linkedRequests = String.valueOf(linkedRequest.get("LINKED_REQUEST")).split(Pattern.quote(I8SBConstants.PIPE_DELIMITER), -1);
        String[] linkedDependencies = String.valueOf(linkedRequest.get("DEPENDENCY")).split(Pattern.quote(I8SBConstants.PIPE_DELIMITER), -1);
        boolean isRequestDependent = false;
        boolean isNextRequestDependent = false;
        for (int i = 0; i < linkedRequests.length; i++) {
            i8SBSwitchControllerRequestVO.setRequestType(linkedRequests[i]);
            logger.info("Processing linked request: " + linkedRequests[i]);
            I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = processRequest(i8SBSwitchControllerRequestVO, i8SBChannelInterface);
            logger.info("Linked request processed: " + linkedRequests[i]);
            isRequestDependent = BooleanUtils.toBoolean(Integer.valueOf(linkedDependencies[i]));
            logger.info("Preparing accumulated response for linked request: " + linkedRequests[i]);
            accumulatedI8SBSwitchControllerResponseVO = i8SBBO.populateAccumulatedI8SBSwitchControllerResponseVO(accumulatedI8SBSwitchControllerResponseVO, i8SBSwitchControllerResponseVO, isRequestDependent);

            // To check if next linked request will be processed or not
            if ((i + 1) < linkedRequests.length) {
                String responseCode = accumulatedI8SBSwitchControllerResponseVO.getResponseCode();
                logger.info("I8SB response Code: " + responseCode);
                isNextRequestDependent = BooleanUtils.toBoolean(Integer.valueOf(linkedDependencies[i + 1]));
                if (responseCode != null && (responseCode.equals(I8SBResponseCodeEnum.PROCESSED.getValue()) || !isNextRequestDependent)) {
                    logger.info("Next linked request is: " + linkedRequests[i + 1]);
                    logger.info("Preparing linked request: " + linkedRequests[i + 1]);
                    i8SBSwitchControllerRequestVO.setRequestType(linkedRequests[i + 1]);
                    i8SBSwitchControllerRequestVO = i8SBBO.prepareLinkedRequest(i8SBSwitchControllerRequestVO, accumulatedI8SBSwitchControllerResponseVO, i8SBChannelInterface);
                    if (i8SBSwitchControllerRequestVO.getRRN() != null && i8SBSwitchControllerRequestVO.getRRN().length() > 0 && i8SBSwitchControllerRequestVO.getParentRequestRRN() == null) {
                        i8SBSwitchControllerRequestVO.setParentRequestRRN(i8SBSwitchControllerRequestVO.getRRN());
                    }
                    // Setting RRN ,Stan , Transaction ID and TransmissionDateTime null to regenerate it for next linked request.
                    i8SBSwitchControllerRequestVO.setRRN(null);
                    i8SBSwitchControllerRequestVO.setSTAN(null);
                    i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(null);
                    if (!((i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_AccountBalanceInquiry)
                            || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_CashWithdrawal)
                            || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_CashWithdrawalReversal))
                            || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_AgentVerification))
                            || i8SBSwitchControllerRequestVO.getRequestType().equals(I8SBConstants.RequestType_Tasdeeq_CustomAnalytics))
                        i8SBSwitchControllerRequestVO.setTransactionId(null);
                } else {
                    logger.info("request was not processed as expected");
                    logger.info("I8SB can not process next linked request");
                    break;
                }
            }
        }
        logger.info("Accumulated response generated");
        return accumulatedI8SBSwitchControllerResponseVO;
    }
}
