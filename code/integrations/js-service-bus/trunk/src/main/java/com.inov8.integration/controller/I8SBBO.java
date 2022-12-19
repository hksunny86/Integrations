package com.inov8.integration.controller;

import com.inov8.integration.channel.APIGEE.bo.APIGEEBO;
import com.inov8.integration.channel.AppInSnap.bo.AppInSnapBo;
import com.inov8.integration.channel.fonepay.bo.FonePayBO;
//import com.inov8.integration.channel.kmbl.bo.KMBLBO;
import com.inov8.integration.channel.microbank.bo.MicrobankBO;
//import com.inov8.integration.channel.rdv.mb.bo.RDVMobileBankingBO;
//import com.inov8.integration.channel.rdv.ws.bo.RDVWebServiceBO;
import com.inov8.integration.channel.vrg.echallan.bo.EChallanBO;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.dao.i8sb.TransactionDAO;
import com.inov8.integration.dao.i8sb.model.TransactionLog;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.enums.I8SBTransactionStatus;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

/**
 * Created by inov8 on 9/5/2017.
 */
@Component
public class I8SBBO implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(I8SBBO.class.getSimpleName());

    private String gateway_transactionlog = PropertyReader.getProperty("i8sb.gateway.transactionLog");
    private String channel_transactionlog = PropertyReader.getProperty("i8sb.channel.transactionLog");
    @Autowired
    private TransactionDAO transactionDAO;

    private ApplicationContext applicationContext;

    private Map<String, Map<String, Object>> linkedRequestCache = new HashMap<String, Map<String, Object>>();

    I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    public boolean validateMandatoryI8SBSwitchControllerRequestVOParameters(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        boolean validationResult = true;
        i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (i8SBSwitchControllerRequestVO.getI8sbClientID() != null && i8SBSwitchControllerRequestVO.getI8sbClientTerminalID() != null && i8SBSwitchControllerRequestVO.getI8sbChannelID() != null && i8SBSwitchControllerRequestVO.getRequestType() != null) {

        } else {
            validationResult = false;
            logger.error("Request processing failed, I8SB ClientID, I8SB Client Terminal ID, I8SB ChannelID or RequestType is NULL");
            i8SBSwitchControllerResponseVO.setError("Request processing failed, I8SB ClientID, I8SB Client Terminal ID, I8SB ChannelID or RequestType is NULL");
        }

        if (!validationResult) {
            i8SBSwitchControllerResponseVO.setStatus(I8SBTransactionStatus.REJECTED.getValue().toString());
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
        }
        return validationResult;
    }

    public I8SBSwitchControllerResponseVO getI8SBSwitchControllerResponseVO() {
        return i8SBSwitchControllerResponseVO;
    }

    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBChannelInterface i8SBChannelInterface) throws Exception {
        logger.info("Client ID: " + i8SBSwitchControllerRequestVO.getI8sbClientID());
        logger.info("Terminal ID: " + i8SBSwitchControllerRequestVO.getI8sbClientTerminalID());
        logger.info("Channel ID: " + i8SBSwitchControllerRequestVO.getI8sbChannelID());
        return i8SBChannelInterface.execute(i8SBSwitchControllerRequestVO);
    }

    public I8SBChannelInterface initializeChannel(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        logger.info("Initializing Channel");
        I8SBChannelInterface i8SBChannelInterface = null;
        StringBuffer beanProperty = new StringBuffer();
        beanProperty.append(i8SBSwitchControllerRequestVO.getI8sbClientID()).append(I8SBConstants.PIPE_DELIMITER);
        beanProperty.append(i8SBSwitchControllerRequestVO.getI8sbClientTerminalID()).append(I8SBConstants.PIPE_DELIMITER);
        beanProperty.append(i8SBSwitchControllerRequestVO.getI8sbChannelID());
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        String beanName = PropertyReader.getProperty(beanProperty.toString());
        i8SBChannelInterface = (I8SBChannelInterface) applicationContext.getBean(beanName);
        return i8SBChannelInterface;
    }

    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBChannelInterface i8SBChannelInterface) throws Exception {
        logger.info("Generating RRN for ClientID: " + i8SBSwitchControllerRequestVO.getI8sbClientID());
        return i8SBChannelInterface.generateSystemTraceableInfo(i8SBSwitchControllerRequestVO);

//        if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_JSBL)) {
//            // generate RRN and other info here based of channel id
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
//                i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
//                i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
//                i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
//                i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
//            }
//        } else if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_AKBL)) {
//            // generate RRN and other info here based of channel id
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
//                i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
//                i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
//                i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
//                i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
//            }
//        } else if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MBL)) {
//            // generate RRN and other info here based of channel id
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
//                i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
//            }
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
//                i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
//            }
//            // 12 digit number
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
//                i8SBSwitchControllerRequestVO.setRRN(CommonUtils.generateSTAN() + CommonUtils.generateSTAN());
//            }
//        } else if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_MasterCard)) {
//            // generate RRN and other info here based of channel id
//            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
//                i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
//            }
//            // 20 digit number
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
//                i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
//            }
//        } else if (i8SBSwitchControllerRequestVO.getI8sbClientID().equalsIgnoreCase(I8SBConstants.I8SB_Client_ID_KMBL)) {
//            // generate RRN and other info here based of channel id
//            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
//                i8SBSwitchControllerRequestVO.setRRN(CommonUtils.generateSTAN() + CommonUtils.generateSTAN());
//            }
//            // 20 digit number
//            if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
//                i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
//            }
//        }


    }

    /*public Map<String, Object> getLinkedRequests(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        // Get linked requests from database based on BANK_ID, Channel_ID, RequestType
        Map<String, Object> requests = this.transactionDAO.getLinkedRequest(i8SBSwitchControllerRequestVO);
        if (requests.size() > 0) {
            logger.info("Parent Request type: " + i8SBSwitchControllerRequestVO.getRequestType());
            logger.info("Linked requests are: " + Arrays.toString(String.valueOf(requests.get("LINKED_REQUEST")).split(Pattern.quote(I8SBConstants.PIPE_DELIMITER), -1)));
        }
        return requests;
    }*/

    @PostConstruct
    public void loadLinkedRequests() {
        logger.info("Loading all linked Request configurations in cache..");
        List<Map<String, Object>> allLinkedRequests = this.transactionDAO.loadLinkedRequest();
        for (int i = 0; i < allLinkedRequests.size(); i++) {
            Map<String, Object> linkedRequests = allLinkedRequests.get(i);
            StringBuilder key = new StringBuilder();
            key.append(linkedRequests.get("CLIENT_ID")).append(linkedRequests.get("TERMINAL_ID")).append(linkedRequests.get("CHANNEL_ID")).append(linkedRequests.get("REQUEST_TYPE"));
            linkedRequestCache.put(key.toString(), linkedRequests);
        }
    }

    public Map<String, Object> getLinkedRequests(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        // Get linked requests from database based on BANK_ID, Terminal_ID, Channel_ID, RequestType
        logger.info("Linked requests cache size is " + linkedRequestCache.size());
        StringBuilder key = new StringBuilder();
        key.append(i8SBSwitchControllerRequestVO.getI8sbClientID()).append(i8SBSwitchControllerRequestVO.getI8sbClientTerminalID()).append(i8SBSwitchControllerRequestVO.getI8sbChannelID()).append(i8SBSwitchControllerRequestVO.getRequestType());
        logger.info("Getting linked Request from cache..");
        Map<String, Object> linkedRequests = linkedRequestCache.get(key.toString());

        if (linkedRequests != null && linkedRequests.size() > 0) {
            logger.info("Parent Request type: " + i8SBSwitchControllerRequestVO.getRequestType());
            logger.info("Linked requests are: " + Arrays.toString(String.valueOf(linkedRequests.get("LINKED_REQUEST")).split(Pattern.quote(I8SBConstants.PIPE_DELIMITER), -1)));
        }
        return linkedRequests;
    }

    public void
    insertTransactionLog(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        TransactionLog transactionLog = new TransactionLog();
        //BankID, ChannelID, RRN, RequestType, ParentRequestType
        transactionLog.setGateway(i8SBSwitchControllerRequestVO.getI8sbGateway());
        transactionLog.setClientID(i8SBSwitchControllerRequestVO.getI8sbClientID());
        transactionLog.setTerminalID(i8SBSwitchControllerRequestVO.getI8sbClientTerminalID());
        transactionLog.setChannelID(i8SBSwitchControllerRequestVO.getI8sbChannelID());
        transactionLog.setRRN(i8SBSwitchControllerRequestVO.getRRN());
        transactionLog.setRequestType(i8SBSwitchControllerRequestVO.getRequestType());
        transactionLog.setParentRequestRRN(i8SBSwitchControllerRequestVO.getParentRequestRRN());
        //saving Request params
//        transactionLog.setI8sbRequest(i8SBSwitchControllerRequestVO.getRequestXML());
        String i8sbRequestXml=i8SBSwitchControllerRequestVO.getRequestXML();

        if (StringUtils.isNotEmpty(gateway_transactionlog) && gateway_transactionlog.equals("enable")) {
            transactionLog.setI8sbRequest(i8sbRequestXml);
        }
        else {
            transactionLog.setI8sbRequest(null);
        }


        logger.info("Inserting transaction log for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        this.transactionDAO.insert(transactionLog);
    }

    public I8SBSwitchControllerRequestVO prepareLinkedRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO, I8SBChannelInterface i8SBChannelInterface) {
        logger.info("Client ID: " + i8SBSwitchControllerRequestVO.getI8sbClientID());
        logger.info("Terminal ID: " + i8SBSwitchControllerRequestVO.getI8sbClientTerminalID());
        logger.info("Channel ID: " + i8SBSwitchControllerRequestVO.getI8sbChannelID());

        try {
            return i8SBChannelInterface.prepareRequest(i8SBSwitchControllerRequestVO, i8SBSwitchControllerResponseVO);
        } catch (Exception ex) {
            logger.error("Error occurred while preparing linked Request: " + i8SBSwitchControllerRequestVO.getRequestType());
        }

        return null;
    }

    public void updateTransactionLog(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        TransactionLog transactionLog = new TransactionLog();
        //RRN, Request, Response, ResponseCode, Status, Error
        transactionLog.setRRN(i8SBSwitchControllerRequestVO.getRRN());
        JSONObject json = new JSONObject(i8SBSwitchControllerRequestVO.getRequestXML());

        json.put("fingerTemplete","****");
        JSONObject json1 = new JSONObject(i8SBSwitchControllerRequestVO.getRequestXML());
        json1.put("fingerTemplete","****");
        if (StringUtils.isNotEmpty(channel_transactionlog) && channel_transactionlog.equals("enable")) {
            transactionLog.setChannelRequest(json.toString());
            transactionLog.setChannelResponse(json1.toString());
        }else {
        transactionLog.setChannelRequest(null);
        transactionLog.setChannelResponse(null);
        }
        //We don't have to set this xml in I8SB resposne Vo
        i8SBSwitchControllerResponseVO.setResponseXML(null);
        String i8sbResponseXml = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
        JSONObject json2 = new JSONObject(i8sbResponseXml);

        json.put("fingerTemplete","****");

        if (StringUtils.isNotEmpty(gateway_transactionlog) && gateway_transactionlog.equals("enable")) {
            transactionLog.setI8sbResponse(json2.toString());
        }else {
            transactionLog.setI8sbResponse(null);
        }
        logger.info("Response Send to MicroBank"+i8sbResponseXml);
        transactionLog.setResponseCode(i8SBSwitchControllerResponseVO.getResponseCode());
        transactionLog.setStatus(i8SBSwitchControllerResponseVO.getStatus());
        transactionLog.setError(i8SBSwitchControllerResponseVO.getError());
        transactionLog.setRequestType(i8SBSwitchControllerRequestVO.getRequestType());
        logger.info("Updating transaction log for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        //uncomment below line to save response xml into transaction_log table.
        //transactionLog.setI8sbResponse(i8sbResponseXml);
        this.transactionDAO.update(transactionLog);
    }

    public I8SBSwitchControllerResponseVO populateAccumulatedI8SBSwitchControllerResponseVO(I8SBSwitchControllerResponseVO accumulatedI8SBResponseVO, I8SBSwitchControllerResponseVO i8SBResponseVO, boolean isRequestDependent) {
        // Adding all Response in I8SBSwitchControllerResponseVO
        accumulatedI8SBResponseVO.addI8SBSwitchControllerResponseVO(i8SBResponseVO);

        // Populate Response
        if (accumulatedI8SBResponseVO.getResponseCode() == null || isRequestDependent) {
            accumulatedI8SBResponseVO.setResponseCode(i8SBResponseVO.getResponseCode());
        } else {
            if (!isRequestDependent && !accumulatedI8SBResponseVO.getResponseCode().equals(I8SBResponseCodeEnum.PROCESSED.getValue())) {
                accumulatedI8SBResponseVO.setResponseCode(i8SBResponseVO.getResponseCode());
            }
        }

        accumulatedI8SBResponseVO.setStatus(i8SBResponseVO.getStatus());
        accumulatedI8SBResponseVO.setError(i8SBResponseVO.getError());

        // Common Headers
        accumulatedI8SBResponseVO.setTransmissionDateAndTime(i8SBResponseVO.getTransmissionDateAndTime());
        accumulatedI8SBResponseVO.setTransactionId(i8SBResponseVO.getTransactionId());
        accumulatedI8SBResponseVO.setSTAN(i8SBResponseVO.getSTAN());


        if (i8SBResponseVO.getResponseCode().equalsIgnoreCase(I8SBResponseCodeEnum.PROCESSED.getValue())) {
            if (i8SBResponseVO.getCollectionOfList().size() > 0) {
                accumulatedI8SBResponseVO.getCollectionOfList().putAll(i8SBResponseVO.getCollectionOfList());
            }
            if (accumulatedI8SBResponseVO.getMobilePhone() == null && i8SBResponseVO.getMobilePhone() != null) {
                accumulatedI8SBResponseVO.setMobilePhone(i8SBResponseVO.getMobilePhone());
            }

            if (accumulatedI8SBResponseVO.getPAN() == null && i8SBResponseVO.getPAN() != null) {
                accumulatedI8SBResponseVO.setPAN(i8SBResponseVO.getPAN());
            }
            if (accumulatedI8SBResponseVO.getCardTitle() == null && i8SBResponseVO.getCardTitle() != null) {
                accumulatedI8SBResponseVO.setCardTitle(i8SBResponseVO.getCardTitle());
            }
            if (accumulatedI8SBResponseVO.getCNIC() == null && i8SBResponseVO.getCNIC() != null) {
                accumulatedI8SBResponseVO.setCNIC(i8SBResponseVO.getCNIC());
            }
            if (accumulatedI8SBResponseVO.getEmail() == null && i8SBResponseVO.getEmail() != null) {
                accumulatedI8SBResponseVO.setEmail(i8SBResponseVO.getEmail());
            }
            if (accumulatedI8SBResponseVO.getMobilePhone() == null && i8SBResponseVO.getMobilePhone() != null) {
                accumulatedI8SBResponseVO.setMobilePhone(i8SBResponseVO.getMobilePhone());
            }
            if (accumulatedI8SBResponseVO.getRelationshipNumber() == null && i8SBResponseVO.getRelationshipNumber() != null) {
                accumulatedI8SBResponseVO.setRelationshipNumber(i8SBResponseVO.getRelationshipNumber());
            }
            if (accumulatedI8SBResponseVO.getOverallDefaultAccount() == null && i8SBResponseVO.getOverallDefaultAccount() != null) {
                accumulatedI8SBResponseVO.setOverallDefaultAccount(i8SBResponseVO.getOverallDefaultAccount());
            }
            if (accumulatedI8SBResponseVO.getCardType() == null && i8SBResponseVO.getCardType() != null) {
                accumulatedI8SBResponseVO.setCardType(i8SBResponseVO.getCardType());
            }
            if (accumulatedI8SBResponseVO.getAccountType() == null && i8SBResponseVO.getAccountType() != null) {
                accumulatedI8SBResponseVO.setAccountType(i8SBResponseVO.getAccountType());
            }
            if (accumulatedI8SBResponseVO.getCustomerName() == null && i8SBResponseVO.getCustomerName() != null) {
                accumulatedI8SBResponseVO.setCustomerName(i8SBResponseVO.getCustomerName());
            }
            if (accumulatedI8SBResponseVO.getMotherMaidenName() == null && i8SBResponseVO.getMotherMaidenName() != null) {
                accumulatedI8SBResponseVO.setMotherMaidenName(i8SBResponseVO.getMotherMaidenName());
            }
            if (accumulatedI8SBResponseVO.getDateOfBirth() == null && i8SBResponseVO.getDateOfBirth() != null) {
                accumulatedI8SBResponseVO.setDateOfBirth(i8SBResponseVO.getDateOfBirth());
            }
            if (accumulatedI8SBResponseVO.getAccountBalance() == null && i8SBResponseVO.getAccountBalance() != null) {
                accumulatedI8SBResponseVO.setAccountBalance(i8SBResponseVO.getAccountBalance());
            }
            if (accumulatedI8SBResponseVO.getUserId() == null && i8SBResponseVO.getUserId() != null) {
                accumulatedI8SBResponseVO.setUserId(i8SBResponseVO.getUserId());
            }
            if (accumulatedI8SBResponseVO.getPasswordBitmap() == null && i8SBResponseVO.getPasswordBitmap() != null) {
                accumulatedI8SBResponseVO.setPasswordBitmap(i8SBResponseVO.getPasswordBitmap());
            }
            if (accumulatedI8SBResponseVO.getPassword() == null && i8SBResponseVO.getPassword() != null) {
                accumulatedI8SBResponseVO.setPassword(i8SBResponseVO.getPassword());
            }
            if (accumulatedI8SBResponseVO.getAccountId1() == null && i8SBResponseVO.getAccountId1() != null) {
                accumulatedI8SBResponseVO.setAccountId1(i8SBResponseVO.getAccountId1());
            }
            if (accumulatedI8SBResponseVO.getSessionId() == null && i8SBResponseVO.getSessionId() != null) {
                accumulatedI8SBResponseVO.setSessionId(i8SBResponseVO.getSessionId());
            }
            if (accumulatedI8SBResponseVO.getAvailableBalance() == null && i8SBResponseVO.getAvailableBalance() != null) {
                accumulatedI8SBResponseVO.setAvailableBalance(i8SBResponseVO.getAvailableBalance());
            }
            if (accumulatedI8SBResponseVO.getAccountTitle() == null && i8SBResponseVO.getAccountTitle() != null) {
                accumulatedI8SBResponseVO.setAccountTitle(i8SBResponseVO.getAccountTitle());
            }
            if (accumulatedI8SBResponseVO.getConsumerNumber() == null && i8SBResponseVO.getConsumerNumber() != null) {
                accumulatedI8SBResponseVO.setConsumerNumber(i8SBResponseVO.getConsumerNumber());
            }
            if (accumulatedI8SBResponseVO.getAccessToken() == null && i8SBResponseVO.getAccessToken() != null) {
                accumulatedI8SBResponseVO.setAccessToken(i8SBResponseVO.getAccessToken());
            }
            if (accumulatedI8SBResponseVO.getAvailableLimit() == null && i8SBResponseVO.getAvailableLimit() != null) {
                accumulatedI8SBResponseVO.setAvailableLimit(i8SBResponseVO.getAvailableLimit());
            }
            if (accumulatedI8SBResponseVO.getTransactionNumber() == null && i8SBResponseVO.getTransactionNumber() != null) {
                accumulatedI8SBResponseVO.setTransactionNumber(i8SBResponseVO.getTransactionNumber());
            }
            if (accumulatedI8SBResponseVO.getCustomerAccNumber() == null && i8SBResponseVO.getCustomerAccNumber() != null) {
                accumulatedI8SBResponseVO.setCustomerAccNumber(i8SBResponseVO.getCustomerAccNumber());
            }
            if (accumulatedI8SBResponseVO.getSessionIdNadra() == null && i8SBResponseVO.getSessionIdNadra() != null) {
                accumulatedI8SBResponseVO.setSessionIdNadra(i8SBResponseVO.getSessionIdNadra());
            }
            if (accumulatedI8SBResponseVO.getWalletId() == null && i8SBResponseVO.getWalletId() != null) {
                accumulatedI8SBResponseVO.setWalletId(i8SBResponseVO.getWalletId());
            }
            if (accumulatedI8SBResponseVO.getTaxpaid() == null && i8SBResponseVO.getTaxpaid() != null) {
                accumulatedI8SBResponseVO.setTaxpaid(i8SBResponseVO.getTaxpaid());
            }
            if (accumulatedI8SBResponseVO.getAuthToken() == null && i8SBResponseVO.getAuthToken() != null) {
                accumulatedI8SBResponseVO.setAuthToken(i8SBResponseVO.getAuthToken());
            }


        }
        return accumulatedI8SBResponseVO;
    }
}


