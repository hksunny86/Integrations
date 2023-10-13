package com.inov8.integration.debitCard.service;

import com.inov8.integration.debitCard.pdu.request.AppRebrandDebitCardIssuanceInquiryRequest;
import com.inov8.integration.debitCard.pdu.request.AppRebrandDebitCardIssuanceRequest;
import com.inov8.integration.debitCard.pdu.request.DebitCardDiscrepantRequest;
import com.inov8.integration.debitCard.pdu.request.DebitCardFeeRequest;
import com.inov8.integration.debitCard.pdu.response.AppRebrandDebitCardIssuanceInquiryResponse;
import com.inov8.integration.debitCard.pdu.response.AppRebrandDebitCardIssuanceResponse;
import com.inov8.integration.debitCard.pdu.response.DebitCardDiscrepantResponse;
import com.inov8.integration.debitCard.pdu.response.DebitCardFeeResponse;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.RSAEncryption;
import com.inov8.integration.webservice.controller.DebitCardRevampSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class DebitCardService {
    private static Logger logger = LoggerFactory.getLogger(DebitCardService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("debitCardRevamp.i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));
    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    @Autowired
    TransactionDAO transactionDAO;
    private DebitCardRevampSwitchController switchController = null;


    public DebitCardService() {
        disableSslVerification();
        buildSwitch();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void saveTransaction(TransactionLogModel transaction) {
        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[HOST] Transaction saved with RRN: " + transaction.getRetrievalRefNo());
    }

    private void buildSwitch() {
        try {
            if (switchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(DebitCardRevampSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                switchController = (DebitCardRevampSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    public DebitCardFeeResponse debitCardFeeResponse(DebitCardFeeRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Debit Card Fees Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        DebitCardFeeResponse response = new DebitCardFeeResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardFees");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Fees Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = switchController.debitCardFee(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Card Fees Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCardTypeFeeList(messageVO.getCardTypeFeeList());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Card Fees Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Fees Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Debit Card Fees Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        }
        return response;
    }

    public DebitCardDiscrepantResponse debitCardDiscrepantResponse(DebitCardDiscrepantRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Debit Card Discrepant Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        DebitCardDiscrepantResponse response = new DebitCardDiscrepantResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setName(request.getName());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setStreetNumber(request.getStreetNumber());
        messageVO.setHouseNumber(request.getHouseNumber());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardDiscrepant");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Discrepant Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = switchController.debitCardDiscrepant(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Card Discrepant Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Card Discrepant Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Discrepant Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Debit Card Discrepant Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        }
        return response;
    }

    public AppRebrandDebitCardIssuanceInquiryResponse appRebrandDebitCardIssuanceInquiryResponse(AppRebrandDebitCardIssuanceInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Debit Card Issuance Inquiry Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        AppRebrandDebitCardIssuanceInquiryResponse response = new AppRebrandDebitCardIssuanceInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setPinType(request.getPinType());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setCardTypeId(request.getCardType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardIssuanceInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Issuance Inquiry Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = switchController.appRebrandDebitCardIssuanceInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Card Issuance Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setCharges(messageVO.getCharges());
            response.setCardTypes(messageVO.getCardTypes());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Card Issuance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Issuance Inquiry Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Debit Card Issuance Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        }
        return response;
    }

    public AppRebrandDebitCardIssuanceResponse appRebrandDebitCardIssuanceResponse(AppRebrandDebitCardIssuanceRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Debit Card Issuance Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        AppRebrandDebitCardIssuanceResponse response = new AppRebrandDebitCardIssuanceResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setCardTypeId(request.getCardType());
        if (request.getPinType().equals("02")) {
//            messageVO.setOtpPin(request.getPin());
            try {
                if (request.getPin() != null && !request.getPin().equals("")) {
                    String text = request.getPin();
                    String otp = text.replaceAll("\\r|\\n", "");
                    messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
//            messageVO.setMobilePin(request.getPin());
            try {
                if (request.getPin() != null && !request.getPin().equals("")) {
                    String text = request.getPin();
                    String mpin = text.replaceAll("\\r|\\n", "");
                    messageVO.setMobilePin(RSAEncryption.decrypt(mpin, loginPrivateKey));
                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        messageVO.setReserved1(request.getPinType());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setCardDescription(request.getCardDescription());
        messageVO.setMailingAddress(request.getMailingAddress());
        messageVO.setCity(request.getCity());
        messageVO.setArea(request.getArea());
        messageVO.setStreetNumber(request.getStreetNumber());
        messageVO.setHouseNumber(request.getHouseNumber());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardIssuance");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Issuance  Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = switchController.appRebrandDebitCardIssuance(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Card Issuance  Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setCharges(messageVO.getCharges());
            response.setCardTypes(messageVO.getCardTypes());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Card Issuance Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Issuance Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Debit Card Issuance Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        }
        return response;
    }


}
