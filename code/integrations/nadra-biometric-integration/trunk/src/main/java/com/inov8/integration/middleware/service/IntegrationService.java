package com.inov8.integration.middleware.service;

import com.inov8.integration.enums.TransactionStatus;
import com.inov8.integration.enums.TransactionType;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.jsnadra.JSNadraWebService;
import com.inov8.integration.middleware.nadra.biometric.IBioVeriSysMobileBankAccount;
import com.inov8.integration.middleware.nadra.otc.IBioVeriSysOTC;
import com.inov8.integration.middleware.nadra.pdu.*;
import com.inov8.integration.middleware.util.*;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.vo.TransactionRequest;
import com.inov8.integration.vo.TransactionResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import static com.inov8.integration.enums.TransactionType.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class IntegrationService {
    DateUtil dateUtil = new DateUtil();

    private Logger logger = LoggerFactory.getLogger(IntegrationService.class.getSimpleName());

    SessionService sessionService = new SessionService();
    @Autowired
    private TransactionDAO transactionDAO;
    @Autowired
    private IBioVeriSysMobileBankAccount bioVeriSysMobileBankAccount;
    @Autowired
    private IBioVeriSysOTC ibioVeriSysOTC;
    @Autowired
    private JSNadraWebService jsNadraWebService;


    private static String FRANCHISE_ID = ConfigReader.getInstance().getProperty("nadra.franchize.id", "");
    private static String USERNAME = ConfigReader.getInstance().getProperty("nadra.username", "");
    private static String PASSWORD = ConfigReader.getInstance().getProperty("nadra.password", "");
    private static Double SESSIONTIMEOUT = Double.valueOf(ConfigReader.getInstance().getProperty("nadra.sessionTimeOut", "10"));

    public NadraIntegrationVO fingerPrintVerification(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setContactNumber(messageVO.getContactNo());
        requestData.setFingerIndex(Integer.valueOf(messageVO.getFingerIndex()));
        messageVO.setUserName(USERNAME);
        messageVO.setFranchiseeID(FRANCHISE_ID);

        if (FieldUtil.isHex(messageVO.getFingerTemplate())) {
            requestData.setFingerTemplate(FieldUtil.toByteArray(messageVO.getFingerTemplate()));
        } else {
            requestData.setFingerTemplate(Base64Utils.decodeFromString(messageVO.getFingerTemplate()));
        }
        logger.info("template type received from mobile"+messageVO.getTemplateType());
        if (messageVO.getTemplateType().equals("ISO_19794_2") || messageVO.getTemplateType().equals("ANSI") || messageVO.getTemplateType().equals("RAW_IMAGE")
        ||messageVO.getTemplateType().equals("WSQ")){
            logger.info("template type received from mobile1"+messageVO.getTemplateType());
            requestData.setTemplateType(messageVO.getTemplateType());
        } else {
            logger.info("template type received from mobile2"+messageVO.getTemplateType());
            requestData.setTemplateType("ISO_19794_2");
        }
        String transactionId = null;

        TransactionResponse customerData = null;
        TransactionRequest transactionRequest = requestBuilder(VERIFICATION.getValue(), messageVO);
        try {

            String dateSeq = dateUtil.formatDate(new Date());
            transactionId = FRANCHISE_ID + dateSeq;
            customerData = transactionDAO.getCustomerLastData(transactionRequest);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription("Exception NADRA verify Finger Prints");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }
        if (customerData == null) {

            requestData.setTransactionId(transactionId);
            transactionRequest.setTransactionId(transactionId);
        }
        requestData.setAreaName(messageVO.getAreaName());

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        String requestXML = XMLUtil.convertRequest(biometricVerification);

        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
            BiometricVerification verification = biometricVerification;
            //   verification.getRequestData().setFingerTemplate(null);
            String nadraRequest = UnicodeUtil.escapeUnicode(XMLUtil.convertRequest(verification));

            TransactionLogModel transaction = saveTransaction(FINGER_PRINT_VERIFICATION.getValue(), nadraRequest);


            int fingerAttempts;
            int totalAttempts;
            int totalSessions;
            int totalAttemptsAgainstSession;

            if (customerData != null && customerData.getSessionId() != null) {
                transactionRequest.setSessionId(customerData.getSessionId());
                String responseCode = customerData.getResponseCode();
                customerData = transactionDAO.getCustomerFirstData(transactionRequest);
                // Setting latest response code in current customerData
                customerData.setResponseCode(responseCode);

                totalAttempts = transactionDAO.getTotalAttempts(transactionRequest);
                totalSessions = transactionDAO.getTotalSession(transactionRequest);
                totalAttemptsAgainstSession = transactionDAO.getFingerAttemptsAgainstSession(transactionRequest);

                if (totalSessions <= 5) {
                    if (totalAttempts >= 40) {
                        messageVO.setResponseCode("119");
                        messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                        return messageVO;
                    } else if (customerData != null && customerData.getResponseDateTime() != null && totalAttemptsAgainstSession < 8) {
                        long mints = FieldUtil.getMinitDifference(customerData.getResponseDateTime());
                        if (mints <= SESSIONTIMEOUT && !customerData.getResponseCode().equalsIgnoreCase("100")) {
                            transactionRequest.setSessionId(customerData.getSessionId());
                            fingerAttempts = transactionDAO.getFingerAttempts(transactionRequest);
                            if (fingerAttempts >= 2) {
                                messageVO.setResponseCode("118");
                                messageVO.setResponseDescription("finger verification has been exhausted for current finger");
                                return messageVO;
                            } else {
                                biometricVerification.getRequestData().setSessionId(customerData.getSessionId());
                                biometricVerification.getRequestData().setTransactionId(customerData.getTransactionId());
                                transactionRequest.setTransactionId(customerData.getTransactionId());
                            }
                        } else {
                            biometricVerification.getRequestData().setTransactionId(transactionId);
                            transactionRequest.setTransactionId(transactionId);
                        }
                        if (totalSessions >= 5 && (customerData.getResponseCode() == "100" || totalAttemptsAgainstSession >= 8)) {
                            messageVO.setResponseCode("119");
                            messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                            return messageVO;
                        }
                    } else {
                        biometricVerification.getRequestData().setTransactionId(transactionId);
                        transactionRequest.setTransactionId(transactionId);
                    }
                    if (totalSessions >= 5 && (customerData.getResponseCode() == "100" || totalAttemptsAgainstSession >= 8)) {
                        messageVO.setResponseCode("119");
                        messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                        return messageVO;
                    }
                } else {
                    messageVO.setResponseCode("119");
                    messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                    return messageVO;
                }

            } else if (customerData != null && customerData.getSessionId() == null) {
                biometricVerification.getRequestData().setTransactionId(transactionId);
                transactionRequest.setTransactionId(transactionId);
            }
//            if (customerData != null && customerData.getResponseCode() != null && customerData.getResponseCode().equalsIgnoreCase("100")) {
//                int daysDifference = FieldUtil.getDateDifference(customerData.getResponseDateTime());
//
//                logger.info(" Current System Date - Previous Finger Verify Date Difference is = " + daysDifference);
//
//                if (daysDifference == TransactionStatus.IS_TODAY_DATE.getValue()) {
//
//                    messageVO.setResponseCode(customerData.getResponseCode());
//                    messageVO.setResponseDescription(customerData.getMessage());
//                    messageVO.setSessionId(customerData.getSessionId());
//                    messageVO.setFullName(customerData.getCitizenName());
//                    messageVO.setPresentAddress(customerData.getPresentAddress());
//                    messageVO.setBirthPlace(customerData.getBirthPlace());
//                    messageVO.setDateOfBirth(customerData.getDateOfBirth());
//                    messageVO.setReligion(customerData.getReligion());
//                    messageVO.setMotherName(customerData.getMotherName());
//                    messageVO.setFingerIndex(customerData.getFingerIndex());
//                    messageVO.setNativeLanguage(customerData.getNativeLanguage());
//                    messageVO.setGender(customerData.getGender());
//
//                    /*
//                    if (customerData.getCardExpird().length()>3){
//                        messageVO.setCardExpire(FieldUtil.isCardExpired(customerData.getCardExpird()));
//                    }else
//                        messageVO.setCardExpire(customerData.getCardExpird());
//                    */
//
//                    messageVO.setCardExpire(customerData.getCardExpird());
//
//                    return messageVO;
//                }
//            }

            int detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

//            String nadraResponse = bioVeriSysMobileBankAccount.verifyFingerPrints(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            String nadraResponse = jsNadraWebService.verifyFingerPrints(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

                    updateTransactionInDB(transaction.getRetrievalRefNo(), UnicodeUtil.escapeUnicode(nadraResponse), messageVO);
                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription("Exception NADRA verify Finger Prints");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO getSecretIdentityDemographicsData(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());

        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());

        if (sessionID != null) {
            requestData.setSessionId(sessionID);
        }

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
            TransactionRequest transactionRequest = requestBuilder(GET_SECRET_IDENTITY.getValue(), messageVO);
            TransactionLogModel transaction = saveTransaction(SECRET_IDENTITY_DEMOGRAPHICS.getValue(), requestXML);


            int detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = bioVeriSysMobileBankAccount.getCitizenIdentityDemographicsData(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }


    public NadraIntegrationVO getCitizenData(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        String transactionId = null;
        String dateSeq = dateUtil.formatDate(new Date());
        transactionId = FRANCHISE_ID + dateSeq;
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setContactNumber(messageVO.getContactNo());
        requestData.setIssueDate(messageVO.getCnicIssuanceDate());
        requestData.setAreaName(messageVO.getAreaName());
        requestData.setTransactionId(transactionId);
        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
        if (sessionID != null) {
            requestData.setSessionId(sessionID);
        }
        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
            TransactionRequest transactionRequest = requestBuilder(GET_SECRET_IDENTITY.getValue(), messageVO);
            TransactionLogModel transaction = saveTransaction(SECRET_IDENTITY_DEMOGRAPHICS.getValue(), requestXML);


            int detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Get Citizen Data");
            //logger.info("Franchise Id : " + FRANCHISE_ID);


            String nadraResponse = jsNadraWebService.getCitizenData(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));

            logger.info("Request Response : \n" + nadraResponse);

            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }




    public NadraIntegrationVO getManualVerificationData(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setVerificationResults(messageVO.getVerificationResult());
        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);
//        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
//            TransactionRequest transactionRequest = requestBuilder(messageVO);
            TransactionLogModel transaction = saveTransaction(SUBMIT_MANUAL_VERIFICATION.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = bioVeriSysMobileBankAccount.submitManualVerificationResults(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO getLastVerificationResult(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);


        String dateSeq = dateUtil.formatDate(new Date());
        String transactionId = FRANCHISE_ID + dateSeq;
        requestData.setTransactionId(transactionId);

        requestData.setCitizenNumber(messageVO.getCitizenNumber());

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

//        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
            TransactionRequest transactionRequest = requestBuilder(LAST_VERIFICATION.getValue(), messageVO);
            TransactionLogModel transaction = saveTransaction(LAST_VERIFICATION_RESULT.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = bioVeriSysMobileBankAccount.getLastVerificationResults(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO submitMobileBankAccountDetail(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setAccountNumber(messageVO.getAccountNumber());
        requestData.setAccountLevel(messageVO.getAccountLevel());
        requestData.setSessionId(messageVO.getSessionId());

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        //        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
//            TransactionRequest transactionRequest = requestBuilder(messageVO);
            TransactionLogModel transaction = saveTransaction(SUBMIT_BANK_ACCOUNT_DETAIL.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = bioVeriSysMobileBankAccount.submitBankAccountDetails(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO OtcFingerPrintVerification(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        messageVO.setFranchiseeID(FRANCHISE_ID);
        messageVO.setUserName(USERNAME);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setContactNumber(messageVO.getContactNo());
        requestData.setFingerIndex(Integer.valueOf(messageVO.getFingerIndex()));
        if (FieldUtil.isHex(messageVO.getFingerTemplate())) {
            requestData.setFingerTemplate(FieldUtil.toByteArray(messageVO.getFingerTemplate()));
        } else {
            requestData.setFingerTemplate(Base64Utils.decodeFromString(messageVO.getFingerTemplate()));
        }
        if (messageVO.getTemplateType().equals("ISO_19794_2") || messageVO.getTemplateType().equals("ANSI") || messageVO.getTemplateType().equals("RAW_IMAGE")) {
            requestData.setTemplateType(messageVO.getTemplateType());
        } else {
            requestData.setTemplateType("ISO_19794_2");
        }
        requestData.setRemittanceAmount(messageVO.getRemittanceAmount());
        requestData.setRemittanceType(messageVO.getRemittanceType());

        // for IBFT , skip the secondary citizan number
        if (!messageVO.getRemittanceType().equalsIgnoreCase("IBFT"))
            requestData.setSecondaryCitizeNnumber(messageVO.getSecondaryCitizenNumber());
        else
            requestData.setSecondaryCitizeNnumber("");
        String transactionId = null;
        TransactionRequest transactionRequest = requestBuilder(OTC_VERIFICATION.getValue(), messageVO);
        TransactionResponse customerData = null;
        try {
            customerData = transactionDAO.getCustomerLastData(transactionRequest);
            String dateSeq = dateUtil.formatDate(new Date());
            transactionId = FRANCHISE_ID + dateSeq;
            if (customerData == null) {
                logger.debug("Customer is new assigning new transaction id");
                requestData.setTransactionId(transactionId);
                transactionRequest.setTransactionId(transactionId);

            }
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA OTC Verify Finger Prints", e);
            return messageVO;
        }


        requestData.setAccountNumber(messageVO.getAccountNumber());
        requestData.setSecondaryContactNumber(messageVO.getSecondaryContactNo());
        requestData.setAreaName(messageVO.getAreaName());
        messageVO.setFranchiseeID(FRANCHISE_ID);
        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        //        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
            BiometricVerification verification = biometricVerification;
            //verification.getRequestData().setFingerTemplate(null);
            String nadraRequest = UnicodeUtil.escapeUnicode(XMLUtil.convertRequest(verification));
            TransactionLogModel transaction = saveTransaction(OTC_VERIFY_FINGER_PRINT.getValue(), nadraRequest);

            // New Requirements of Nadra has implemented and tested successfully
            int fingerAttempts;
            int totalAttempts;
            int totalSessions;
            int totalAttemptsAgainstSession;


            if (customerData != null && customerData.getSessionId() != null) {
                transactionRequest.setSessionId(customerData.getSessionId());
                String responseCode = customerData.getResponseCode();
                customerData = transactionDAO.getCustomerFirstData(transactionRequest);
                // Setting latest response code in current customerData
                customerData.setResponseCode(responseCode);

                totalAttempts = transactionDAO.getTotalAttempts(transactionRequest);
                totalSessions = transactionDAO.getTotalSession(transactionRequest);
                totalAttemptsAgainstSession = transactionDAO.getFingerAttemptsAgainstSession(transactionRequest);

                if (totalSessions <= 5) {
                    if (totalAttempts >= 40) {
                        messageVO.setResponseCode("119");
                        messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                        return messageVO;
                    } else if (customerData != null && customerData.getResponseDateTime() != null && totalAttemptsAgainstSession < 8) {
                        long mints = FieldUtil.getMinitDifference(customerData.getResponseDateTime());
                        logger.debug("DEBUG MESSAGE 1");
                        if (mints <= SESSIONTIMEOUT && !customerData.getResponseCode().equalsIgnoreCase("100")) {
                            logger.debug("DEBUG MESSAGE 2");
                            transactionRequest.setSessionId(customerData.getSessionId());
                            fingerAttempts = transactionDAO.getFingerAttempts(transactionRequest);
                            logger.debug("DEBUG MESSAGE 3");
                            if (fingerAttempts >= 2) {
                                logger.debug("DEBUG MESSAGE 4");
                                messageVO.setResponseCode("118");
                                messageVO.setResponseDescription("finger verification has been exhausted for current finger");
                                return messageVO;
                            } else {
                                logger.debug("DEBUG MESSAGE 5");
                                biometricVerification.getRequestData().setSessionId(customerData.getSessionId());
                                biometricVerification.getRequestData().setTransactionId(customerData.getTransactionId());
                                transactionRequest.setTransactionId(customerData.getTransactionId());
                            }
                            logger.debug("DEBUG MESSAGE 6");
                        } else {
                            logger.debug("DEBUG MESSAGE 7");
                            biometricVerification.getRequestData().setTransactionId(transactionId);
                            transactionRequest.setTransactionId(transactionId);
                        }
                        if (totalSessions >= 5 && (customerData.getResponseCode() == "100" || totalAttemptsAgainstSession >= 8)) {
                            logger.debug("DEBUG MESSAGE 8");
                            messageVO.setResponseCode("119");
                            messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                            return messageVO;
                        }
                    } else {
                        logger.debug("DEBUG MESSAGE 9");
                        biometricVerification.getRequestData().setTransactionId(transactionId);
                        transactionRequest.setTransactionId(transactionId);
                    }
                    if (totalSessions >= 5 && (customerData.getResponseCode() == "100" || totalAttemptsAgainstSession >= 8)) {
                        logger.debug("DEBUG MESSAGE 10");
                        messageVO.setResponseCode("119");
                        messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                        return messageVO;
                    }
                } else {
                    logger.debug("DEBUG MESSAGE 11");
                    messageVO.setResponseCode("119");
                    messageVO.setResponseDescription("verification limit for current citizen number has been exhausted");
                    return messageVO;
                }

            } else if (customerData != null && customerData.getSessionId() == null) {
                logger.debug("DEBUG MESSAGE 12");
                biometricVerification.getRequestData().setTransactionId(transactionId);
                transactionRequest.setTransactionId(transactionId);
            }


//            if (customerData != null && customerData.getResponseCode() != null && customerData.getResponseCode().equalsIgnoreCase("100")) {
//                int daysDifference = FieldUtil.getDateDifference(customerData.getResponseDateTime());
//
//                logger.info(" Current System Date - Previous Finger Verify Date Difference is = " + daysDifference);
//
//                if (daysDifference == TransactionStatus.IS_TODAY_DATE.getValue()) {
//                    messageVO.setResponseCode("35600");
//                    messageVO.setResponseDescription("Already Verified. You Can not Verify Your Finger Twice a day.");
//                    return messageVO;
//                }
//            }


            int detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA OTC Verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);
//            String nadraResponse = ibioVeriSysOTC.verifyFingerPrints(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));

             String nadraResponse = jsNadraWebService.verifyFingerPrintsOTC(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), UnicodeUtil.escapeUnicode(nadraResponse), messageVO);
                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA OTC Verify Finger Prints", e);
            return messageVO;
        }

        return messageVO;
    }


    public NadraIntegrationVO getOtcSecretIdentityDemographicsData(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        //        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
//            TransactionRequest transactionRequest = requestBuilder(messageVO);
            TransactionLogModel transaction = saveTransaction(OTC_IDENTITY_DEMOGRAPHICS_DATA.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA OTC Verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = ibioVeriSysOTC.getCitizenIdentityDemographicsData(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA OTC Get Citizen Identity Demographics Data", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO getOtcManualVerificationResult(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();
        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);
        requestData.setCitizenNumber(messageVO.getCitizenNumber());
        requestData.setVerificationResults(messageVO.getVerificationResult());

        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        //        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
//            TransactionRequest transactionRequest = requestBuilder(messageVO);
            TransactionLogModel transaction = saveTransaction(OTC_SUBMIT_MANUAL_VERIFICATION.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA OTC Verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = ibioVeriSysOTC.submitManualVerificationResults(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA OTC Submit Manual Verification Results", e);
            return messageVO;
        }

        return messageVO;
    }

    public NadraIntegrationVO
    getOtcLastVerificationResult(NadraIntegrationVO messageVO) {
        BiometricVerification biometricVerification = new BiometricVerification();
        UserVerification userVerification = new UserVerification();
        RequestData requestData = new RequestData();

        userVerification.setUSERNAME(USERNAME);
        userVerification.setPASSWORD(PASSWORD);

        requestData.setTransactionId(messageVO.getServiceProviderTransactionId());
        requestData.setCitizenNumber(messageVO.getCitizenNumber());


        biometricVerification.setRequestData(requestData);
        biometricVerification.setUserVerification(userVerification);

        //        String sessionID = sessionService.getSession(messageVO.getCitizenNumber());
//
//        if (sessionID != null) {
//            requestData.setSessionId(sessionID);
//        }
        String requestXML = XMLUtil.convertRequest(biometricVerification);
        requestXML = XMLUtil.maskPassword(requestXML);
        logger.info("Request XML : \n" + requestXML);

        try {
//            TransactionRequest transactionRequest = requestBuilder(messageVO);
            TransactionLogModel transaction = saveTransaction(OTC_LAST_VERIFICATION_RESULT.getValue(), requestXML);
//            Long detailId = transactionDAO.saveTransactionDetail(transactionRequest);

            logger.info("NADRA OTC Verify Finger Prints");
            //logger.info("Franchise Id : " + FRANCHISE_ID);

            String nadraResponse = ibioVeriSysOTC.getLastVerificationResults(FRANCHISE_ID, XMLUtil.convertRequest(biometricVerification));
            logger.info("Request Response : \n" + nadraResponse);

//            TransactionResponse transactionResponse = new TransactionResponse();
            if (StringUtils.isNotEmpty(nadraResponse)) {
                BiometricVerification response = XMLUtil.convertResponse(nadraResponse);

                if (nadraResponse != null) {
//                    transactionResponse = responseBuilder(response);

                    ResponseData responseData = response.getResponseData();
                    if (responseData != null) {
                        populateResponse(messageVO, responseData);
                    }

//                    sessionService.saveSession(requestData.getCitizenNumber(), responseData.getSessionId());
                    updateTransactionInDB(transaction.getRetrievalRefNo(), nadraResponse, messageVO);
//                    transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                    return messageVO;
                }

            } else {
//                boolean result = transactionDAO.updateTransactionDetail(detailId, transactionResponse);
                return messageVO;
            }

        } catch (Exception e) {
            messageVO.setResponseCode("220");
            logger.error("Exception NADRA OTC Get Last Verification Results", e);
            return messageVO;
        }

        return messageVO;
    }

    private TransactionLogModel saveTransaction(String request, String nadraString) {
        TransactionLogModel transaction = this.prepareTransaction();
        transaction.setMessageType("SOAP");
        transaction.setTransactionCode(request);
        String requestPDUString = nadraString;
        transaction.setPduRequestHEX(requestPDUString);
        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transaction;
    }

    public TransactionLogModel prepareTransaction() {
        TransactionLogModel transaction = new TransactionLogModel();
        transaction.setMessageType("");

        String transactionKey = String.valueOf(System.currentTimeMillis());

        transaction.setRetrievalRefNo(FieldUtil.buildMessageRecieveTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(String.valueOf(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        transaction.setTransactionDateTime(txDateTime);
        transaction.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        return transaction;
    }

    private void updateTransactionInDB(String rrnKey, String response, NadraIntegrationVO messageVO) {
        TransactionLogModel trx = new TransactionLogModel();
        trx.setStatus(TransactionStatus.RECEIVED.getValue().longValue());
        trx.setResponseCode(messageVO.getResponseCode());
        trx.setPduResponseHEX(response);
        trx.setRetrievalRefNo(rrnKey);
        trx.setProcessedTime(0L);
        this.transactionDAO.update(trx);
        logger.debug("RRN Recieved and Inserted in DB: " + rrnKey);
    }

    private TransactionRequest requestBuilder(String transactionType, NadraIntegrationVO messageVo) {
        TransactionRequest request = new TransactionRequest();
        request.setTransactionType(transactionType);
        request.setFranchiseeID(messageVo.getFranchiseeID());
        request.setUsername(messageVo.getUserName());
        request.setSessionId(messageVo.getSessionId());
        request.setCitizenNumber(messageVo.getCitizenNumber());
        request.setContactNumber(messageVo.getContactNo());
        request.setFingerIndex(messageVo.getFingerIndex());
        request.setFingerTemplate(messageVo.getFingerTemplate());
        request.setTemplateType(messageVo.getTemplateType());
        request.setTransactionId(messageVo.getServiceProviderTransactionId());
        request.setAreaName(messageVo.getAreaName());
        request.setVerificationResult(messageVo.getVerificationResult());
        request.setMobileBankAccountNumber(messageVo.getAccountNumber());
        request.setAccountLevel(messageVo.getAccountLevel());
        request.setRemittanceAmount(messageVo.getRemittanceAmount());
        request.setRemittanceType(messageVo.getRemittanceType());

        return request;
    }

    private TransactionResponse responseBuilder(BiometricVerification biometricVerification) {
        TransactionResponse response = new TransactionResponse();
        ResponseData responseData = biometricVerification.getResponseData();


        if (responseData != null) {
            response.setCitizenNumber(responseData.getCitizenNumber());
            response.setCitizenName(responseData.getUrduName());
            response.setVerificationFunctionality(responseData.getVerificationFunctionality());
            response.setSessionId(responseData.getSessionId());

            ResponsStatus responseStatus = responseData.getResponsStatus();
            if (responseStatus != null) {
                response.setResponseCode(responseStatus.getCode());
                response.setMessage(responseStatus.getMessage());
            }

            PersonalData personalData = responseData.getPersonalData();

            if (personalData != null) {
                response.setPresentAddress(personalData.getPresentAddress());
                response.setBirthPlace(personalData.getBirthPlace());
                response.setCardExpired(personalData.getCardExpired());
                response.setDateOfBirth(personalData.getDateOfBirth());
                response.setGender(personalData.getGender());

                if (responseData.getFingerIndex() != null) {
                    if (CollectionUtils.isNotEmpty(responseData.getFingerIndex().getFINGER())) {
                        response.setFingerIndex(StringUtils.join(responseData.getFingerIndex().getFINGER(), ","));
                    }
                }
                response.setReligion(personalData.getReligion());
                response.setMotherName(personalData.getMotherName());
                response.setNativeLanguage(personalData.getNativeLanguage());
                if (personalData.getPhotograph() != null) {
                    response.setPhotograph(FieldUtil.toHexString(personalData.getPhotograph()));
                }
            }
        }

        return response;
    }


    private void populateResponse(NadraIntegrationVO integrationVO, ResponseData responseData) {
        if (responseData != null) {

            if (StringUtils.isNotEmpty(responseData.getSessionId())) {
                integrationVO.setSessionId(responseData.getSessionId());
            }
            if (StringUtils.isNotEmpty(responseData.getCitizenNumber())) {
                integrationVO.setCitizenNumber(responseData.getCitizenNumber());
            }
            if (StringUtils.isNotEmpty(responseData.getUrduName())) {
                integrationVO.setFullName(responseData.getUrduName());
            }
            if (StringUtils.isNotEmpty(responseData.getVerificationFunctionality())) {
                integrationVO.setVerificationFunctionality(responseData.getVerificationFunctionality());
            }

            if (StringUtils.isNotEmpty(responseData.getSecondaryCitizenUrduName())) {
                integrationVO.setSecondaryFullName(responseData.getSecondaryCitizenUrduName());
            }

            if (responseData.getFingerIndex() != null) {
                if (CollectionUtils.isNotEmpty(responseData.getFingerIndex().getFINGER())) {
                    integrationVO.setFingerIndex(StringUtils.join(responseData.getFingerIndex().getFINGER(), ","));
                }
            }

            ResponsStatus responseStatus = responseData.getResponsStatus();
            if (responseStatus != null) {
                if (StringUtils.isNotEmpty(responseStatus.getCode())) {
                    integrationVO.setResponseCode(responseStatus.getCode());
                }
                if (StringUtils.isNotEmpty(responseStatus.getMessage())) {
                    integrationVO.setResponseDescription(responseStatus.getMessage());
                }
            }

            PersonalData personalData = responseData.getPersonalData();

            if (personalData != null) {
                if (StringUtils.isNotEmpty(personalData.getPresentAddress())) {
                    integrationVO.setPresentAddress(personalData.getPresentAddress());
                }
                if (StringUtils.isNotEmpty(personalData.getBirthPlace())) {
                    integrationVO.setBirthPlace(personalData.getBirthPlace());
                }
                if (StringUtils.isNotEmpty(personalData.getCardExpired())) {
                   /* if (personalData.getCardExpired().length() > 3) {
                        integrationVO.setCardExpire(FieldUtil.isCardExpired(personalData.getCardExpired()));
                    } else
                        integrationVO.setCardExpire(personalData.getCardExpired());*/

                    integrationVO.setCardExpire(personalData.getCardExpired());
                }
                if (StringUtils.isNotEmpty(personalData.getDateOfBirth())) {
                    integrationVO.setDateOfBirth(personalData.getDateOfBirth());
                }
                if (StringUtils.isNotEmpty(personalData.getReligion())) {
                    integrationVO.setReligion(personalData.getReligion());
                }

                if (StringUtils.isNotEmpty(personalData.getGender())) {
                    integrationVO.setGender(personalData.getGender());
                }

                if (StringUtils.isNotEmpty(personalData.getName())) {
                    integrationVO.setFullName(personalData.getName());
                }
                if (StringUtils.isNotEmpty(personalData.getMotherName())) {
                    integrationVO.setMotherName(personalData.getMotherName());
                }
                if (StringUtils.isNotEmpty(personalData.getNativeLanguage())) {
                    integrationVO.setNativeLanguage(personalData.getNativeLanguage());
                }
                if (personalData.getPhotograph() != null) {
                    integrationVO.setPhotograph(FieldUtil.toHexString(personalData.getPhotograph()));
                }
            }
        }
    }
}
