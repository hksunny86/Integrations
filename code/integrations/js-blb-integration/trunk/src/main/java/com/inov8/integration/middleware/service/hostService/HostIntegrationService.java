package com.inov8.integration.middleware.service.hostService;

import com.inov8.integration.middleware.constants.CashInResponseEnum;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.mock.OptasiaMock;
import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.util.*;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class HostIntegrationService {
    private static Logger logger = LoggerFactory.getLogger(HostIntegrationService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    //    private static String I8_QUEUE_SERVER = ConfigReader.getInstance().getProperty("i8-queue-ip", "127.0.0.1");
//    private static String I8_QUEUE_PORT = (ConfigReader.getInstance().getProperty("i8-queue-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));

    private static String publicKey = ConfigReader.getInstance().getProperty("publicKey", "");
    private static String privateKey = ConfigReader.getInstance().getProperty("privateKey", "");

    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");
    private String i8sb_target_environment = ConfigReader.getInstance().getProperty("i8sb.target.environment", "");
    private OptasiaMock optasiaMock = new OptasiaMock();

    @Autowired
    TransactionDAO transactionDAO;
    private WebServiceSwitchController switchController = null;

    public HostIntegrationService() {
        disableSslVerification();
        buildSwtich();
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
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) {
        WebServiceVO messageVO = new WebServiceVO();
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Verify Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        VerifyAccountResponse response = new VerifyAccountResponse();
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            logger.error("Date Parsing Error ", e);
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("verifyAccount");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);
        saveTransaction(logModel);
        try {
            logger.info("[HOST] Sent Account Verify Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.verifyAccount(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Verify Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setAccountStatus(messageVO.getAccountStatus());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setFirstName(messageVO.getFirstName());
            response.setLastName(messageVO.getLastName());
            response.setCnicExpiry(messageVO.getCnicExpiry());
            response.setAccountType(messageVO.getAccountType());
            response.setDateOfBirth(messageVO.getDateOfBirth());
            response.setIsPinSet(messageVO.getOtpPin());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());

        }

        StringBuffer stringText = new StringBuffer(response.getRrn() +
                response.getResponseCode() + response.getResponseDescription() + response.getAccountTitle() +
                response.getMobileNumber() + response.getCnic() + response.getAccountStatus() +
                response.getFirstName() + response.getLastName() + response.getAccountType() +
                response.getCnicExpiry() + response.getDateOfBirth()
                + response.getIsPinSet());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** ACCOUNT VERIFY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);


        updateTransactionInDB(logModel);

        return response;
    }


    public VerifyLoginAccountResponse verifyLoginAccountResponse(VerifyLoginAccountRequest request) {
        WebServiceVO messageVO = new WebServiceVO();
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Verify Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        VerifyLoginAccountResponse response = new VerifyLoginAccountResponse();
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            logger.error("Date Parsing Error ", e);
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("verifyAccount");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);
        saveTransaction(logModel);
        try {
            logger.info("[HOST] Sent Account Verify Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.verifyLoginAccount(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Verify Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setAccountStatus(messageVO.getAccountStatus());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setAccountType(messageVO.getAccountType());
            response.setIsPinSet(messageVO.getOtpPin());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());

        }

        StringBuffer stringText = new StringBuffer(response.getRrn() +
                response.getResponseCode() + response.getResponseDescription() + response.getAccountTitle() +
                response.getMobileNumber() + response.getCnic() + response.getAccountStatus() +
                response.getAccountType()
                + response.getIsPinSet());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** ACCOUNT VERIFY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);


        updateTransactionInDB(logModel);

        return response;
    }


//    public ChequeBookResponse chequeBookResponse(ChequeBookRequest request) {
//        WebServiceVO messageVO = new WebServiceVO();
//        long startTime = new Date().getTime(); // start time
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Checque Book Request RRN: " + messageVO.getRetrievalReferenceNumber());
//        transactionKey = request.getChannelId() + request.getRrn();
//        ChequeBookResponse response = new ChequeBookResponse();
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setCnicNo(request.getCnic());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setMicrobankTransactionCode(request.getOrignalTransactionRRN());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            logger.error("Date Parsing Error ", e);
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("ChechqueBookStatus");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//        saveTransaction(logModel);
//        try {
//            logger.info("[HOST] Sent Checque Book Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.checqueBookStatus(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Checque Book Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Checque Book Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Checque Book Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//
//        }
//
//        StringBuffer stringText = new StringBuffer(
//                response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] **** Checque Book REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//
//
//        updateTransactionInDB(logModel);
//
//        return response;
//    }


    public M0VerifyAccountResponse m0VerifyAccount(M0VerifyAccountRequest request) {
        WebServiceVO messageVO = new WebServiceVO();
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Verify Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        M0VerifyAccountResponse response = new M0VerifyAccountResponse();
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            logger.error("Date Parsing Error ", e);
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("verifyAccount");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);
        saveTransaction(logModel);
        try {
            logger.info("[HOST] Sent Account Verify Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.verifyAccount(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Verify Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setAccountStatus(messageVO.getAccountStatus());
            response.setFirstName(messageVO.getFirstName());
            response.setLastName(messageVO.getLastName());
            response.setCnicExpiry(messageVO.getCnicExpiry());
            response.setAccountType(messageVO.getAccountType());
            response.setDateOfBirth(messageVO.getDateOfBirth());
            response.setIsPinSet(messageVO.getOtpPin());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Verify Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());

        }

        StringBuffer stringText = new StringBuffer(
                response.getResponseCode() + response.getResponseDescription() + response.getAccountTitle() +
                        response.getMobileNumber() + response.getCnic() + response.getAccountStatus() +
                        response.getFirstName() + response.getLastName() + response.getAccountType() +
                        response.getCnicExpiry() + response.getDateOfBirth()
                        + response.getIsPinSet());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** ACCOUNT VERIFY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);


        updateTransactionInDB(logModel);

        return response;
    }


    public AccountOpeningResponse accountOpening(AccountOpeningRequest request) {
        AccountOpeningResponse response = new AccountOpeningResponse();
//        response.setResponseCode("550");
//        response.setResponseDescription("Service is Temporarily Unavailable");
//        // below lines are comment to stop account opening from apiggee temporarily
        WebServiceVO messageVO = new WebServiceVO();
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Opening Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
//        AccountOpeningResponse response = new AccountOpeningResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setConsumerName(StringUtils.trim(request.getConsumerName()));
        messageVO.setAccountTitle(StringUtils.trim(request.getAccountTitle()));
        messageVO.setBirthPlace(request.getBirthPlace());
        messageVO.setPresentAddress(request.getPresentAddress());
        messageVO.setCnicStatus(request.getCnicStatus());
        messageVO.setCnicExpiry(request.getCnicExpiry());
        messageVO.setDateOfBirth(request.getDob());
        messageVO.setFatherHusbandName(request.getFatherHusbandName());
        messageVO.setMotherMaiden(request.getMotherMaiden());
        messageVO.setGender(request.getGender());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setAccountType(request.getAccountType());
        messageVO.setTrackingId(request.getTrackingId());
        messageVO.setCnicIssuanceDate(request.getCnicIssuanceDate());
        messageVO.setEmailAddress(request.getEmailAddress());
        messageVO.setCustomerMobileNetwork(request.getMobileNetwork());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            logger.error("Date Parsing Error ", e);
        }
        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AccountOpening");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);
        saveTransaction(logModel);
        // Call i8
        try {
            logger.info("[HOST] Sent Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountOpening(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Opening Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getRrn() + response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** ACCOUNT OPENING REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public ConventionalAccountOpeningResponse conventionalAccountOpening(ConventionalAccountOpening request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Conventional Account Opening Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        ConventionalAccountOpeningResponse response = new ConventionalAccountOpeningResponse();
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setConsumerName(request.getConsumerName());
        messageVO.setCnicExpiry(request.getCnicExpiry());
        messageVO.setDateOfBirth(request.getDob());
        messageVO.setCustomerPhoto(request.getCustomerPhoto());
        messageVO.setCnicFrontPhoto(request.getCnicFrontPhoto());
        messageVO.setCnicBackPhoto(request.getCnicBackPhoto());
        messageVO.setSignaturePhoto(request.getSignaturePhoto());
        messageVO.setTermsPhoto(request.getTermsPhoto());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setAccountType(request.getAccountType());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            logger.error("Date Parsing Error ", e);
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("ConventionalAccountOpening");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Conventional Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.conventionalAccountOpening(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Conventional Account Opening Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Conventional Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Conventional Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getRrn() + response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** CONVENTIONAL ACCOUNT OPENING REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public PaymentInquiryResponse paymentInquiry(PaymentInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        PaymentInquiryResponse response = new PaymentInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("PaymentInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);
        // Call i8
        try {
            logger.info("[HOST] Sent Account Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.paymentInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setCharges(messageVO.getCharges());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());


        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getRrn() + response.getResponseCode() + response.getResponseDescription() + response.getCharges());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** ACCOUNT INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public PaymentResponse paymentRequest(PaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Payment Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        PaymentResponse response = new PaymentResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getmPin());
        messageVO.setAccountNo1(request.getAccountNumber());
        //As per demand of abubakar to validate otp
        messageVO.setMobileNo(request.getAccountNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setCharges(request.getCharges());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPaymentType(request.getPaymentType());
        messageVO.setChannelId(request.getChannelId());
//        messageVO.setOtpPin(request.getOtp());
        try {
            String text;
            String pin;
            if (request.getOtp() != null && !request.getOtp().equals("")) {
                text = request.getOtp();
                pin = text.replaceAll("\\r|\\n", "");
                messageVO.setOtpPin(RSAEncryption.decrypt(pin, loginPrivateKey));
            } else if (request.getmPin() != null && !request.getmPin().equals("")) {
                text = request.getmPin();
                pin = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (request.getChannelId().equalsIgnoreCase("FONEPAY")) {
            messageVO.setReserved1("03");
        } else {
            messageVO.setReserved1(request.getReserved1());
        }

        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setMicrobankTransactionCode(request.getChannelId() + request.getTransactionCode());
        messageVO.setSettlementType(request.getSettlementType());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("Payment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.paymentRequest(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setTransactionCode(messageVO.getMicrobankTransactionCode());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getRrn() +
                response.getResponseCode() +
                response.getResponseDescription() +
                response.getTransactionCode());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public PaymentReversalResponse paymentReversal(PaymentReversalRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Payment Reversal Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        PaymentReversalResponse response = new PaymentReversalResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMicrobankTransactionCode(request.getTransactionCode());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("PaymentReversal");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);
        // Call i8
        try {
            logger.info("[HOST] Sent Payment Reversal Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.paymentReversal(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Payment Reversal Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Payment Reversal Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Payment Reversal Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** PAYMENT REVERSAL REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public OtpVerificationResponse otpVerification(OtpVerificationRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing OTP Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        OtpVerificationResponse response = new OtpVerificationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setOtpPin(request.getOtpPin());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OtpVerificationRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent OTP Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.otpVerification(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] OTP Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] OTP Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] OTP Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****OTP REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CardTaggingResponse cardTagging(CardTagging request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime();// + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Card Tagging Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        CardTaggingResponse response = new CardTaggingResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setCardExpiry(request.getCardExpiry());
        messageVO.setCardNo(request.getCardNumber());
        messageVO.setFirstName(request.getFirstName());
        messageVO.setLastName(request.getLastName());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setTransactionId(request.getTransactionId());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CardTagging");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);


        // Call i8
        try {
            logger.info("[HOST] Sent Card Tagging Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cardTagging(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Card Tagging Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Card Tagging Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Card Tagging Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****CARD TAGGING REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AccountLinkDeLinkResponse accountLInkDeLink(AccountLinkDeLink request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Account Link De-Link Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AccountLinkDeLinkResponse response = new AccountLinkDeLinkResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
//        messageVO.setOtpPin(request.getOtp());
//        messageVO.setMobilePin(request.getmPin());
        try {
            String text;
            String mpin;
            if (request.getmPin() != null && !request.getmPin().equals("")) {
                text = request.getmPin();
                mpin = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            } else if (request.getOtp() != null && !request.getOtp().equals("")) {
                text = request.getOtp();
                mpin = text.replaceAll("\\r|\\n", "");
                messageVO.setOtpPin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            }


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setTerminalId("Apigee");
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AccountLinkDeLink");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account Link De-Link Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountLinkDelink(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Link De-Link Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountType(messageVO.getAccountType());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Link De-Link Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Link De-Link Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****ACCOUNT LINK DE-LINK REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public SetCardStatusResponse setCardStatus(SetCardStatus request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Set Card Status Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        SetCardStatusResponse response = new SetCardStatusResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setTransactionType(request.getTransactionType());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setCardNo(request.getCardNo());
        messageVO.setChannelId(request.getChannelId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("SetCardStatus");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Set Card Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.setCardStatus(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Set Card Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Set Card Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Set Card Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****SET CARD STATUS REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public GenerateOtpResponse generateOTP(GenerateOtpRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Generate OTP Request RRN: " + messageVO.getRetrievalReferenceNumber());


        transactionKey = request.getChannelId() + request.getRrn();

        GenerateOtpResponse response = new GenerateOtpResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setOtpPurpose(request.getOtpPurpose());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("GenerateOtp");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Generate Otp Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.generateOTP(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Generate OTP Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Generate OTP Response Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Generate OTP Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Generate OTP REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public BalanceInquiryResponse balanceInquiry(BalanceInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Balance Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        BalanceInquiryResponse response = new BalanceInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getMpin());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setOtpPin(request.getOtpPin());
        try {
            if (request.getOtpPin() != null && !request.getOtpPin().equals("")) {
                String text = request.getOtpPin();
                String otp = text.replaceAll("\\r|\\n", "");
                messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            } else if (request.getMpin() != null && !request.getMpin().equals("")) {
                String text = request.getMpin();
                String mpin = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            }

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("BalanceInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Balance Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.balanceInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Balance Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setBalance(messageVO.getBalance());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription() + response.getBalance());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****BALANCE INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public MiniStatementResponse miniStatement(MiniStatementRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Mini Statement Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        MiniStatementResponse response = new MiniStatementResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getMpin());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
//        messageVO.setOtpPin(request.getOtpPin());
        try {
            if (request.getOtpPin() != null && !request.getOtpPin().equals("")) {
                String text = request.getOtpPin();
                String otp = text.replaceAll("\\r|\\n", "");
                messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            } else if (request.getMpin() != null && !request.getMpin().equals("")) {
                String text = request.getMpin();
                String mpin = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("MiniStatement");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Mini Statement Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.miniStatement(messageVO);
        } catch (Exception e) {


            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Mini Statement Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setTransactions(messageVO.getTransactions());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Mini Statement Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Mini Statement Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription() + response.getTransactions());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****MINI STATEMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public BillPaymentInquiryResponse billPaymentInquiry(BillPaymentInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Bill Payment Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        BillPaymentInquiryResponse response = new BillPaymentInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setProductID(request.getProductId());
        messageVO.setConsumerNo(request.getConsumerNo());
        messageVO.setBillAmount(request.getAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("BillPaymentInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Bill Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.billPaymentInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Bill Payment Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setProductName(messageVO.getProductName());
            response.setBillAmount(messageVO.getBillAmount());
            response.setLateBillAmount(messageVO.getLateBillAmount());
            response.setBillPaid(messageVO.getBillPaid());
            response.setDueDate(messageVO.getDueDate());
            response.setOverDue(messageVO.getOverDue());
//            response.setComissionAmount(messageVO.getCommissionAmount());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Bill Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Bill Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getProductName())
                .append(response.getBillAmount())
                .append(response.getLateBillAmount())
                .append(response.getBillPaid())
                .append(response.getDueDate())
                .append(response.getOverDue());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Bill Payment Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public BillPaymentResponse billPayment(BillPaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Bill Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        BillPaymentResponse response = new BillPaymentResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
//        messageVO.setOtpPin(request.getOtpPin());

        try {
            if (request.getOtpPin() != null && !request.getOtpPin().equals("")) {
                String text = request.getOtpPin();
                String otp = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(otp, loginPrivateKey));
                messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            }


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

//        messageVO.setMobilePin(request.getOtpPin());
        messageVO.setProductID(request.getProductId());
        messageVO.setConsumerNo(request.getConsumerNo());
        messageVO.setBillAmount(request.getAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("BillPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Bill Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.billPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Bill Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setBillAmount(messageVO.getBillAmount());
            response.setCommissionAmount(messageVO.getCommissionAmount());
            response.setConsumerNo(messageVO.getConsumerNo());
            response.setPaymentDateTime(messageVO.getDateTime());
            response.setLateBillAmount(messageVO.getLateBillAmount());
            response.setProductName(messageVO.getProductName());
            response.setTotalAmount(messageVO.getTotalAmount());
//            response.setTransactionId(messageVO.getTransactionId());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setRemainingBalance(messageVO.getRemainingBalance());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Bill Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Bill Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getBillAmount())
                .append(response.getCommissionAmount())
                .append(response.getConsumerNo())
                .append(response.getPaymentDateTime())
                .append(response.getLateBillAmount())
                .append(response.getProductName())
                .append(response.getTotalAmount())
                .append(response.getTransactionAmount())
                .append(response.getRemainingBalance());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****BILL PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CashInInquiryResponse cashInInquiry(CashInInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Cash In Inquiry  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CashInInquiryResponse response = new CashInInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());

        messageVO.setTransactionAmount(request.getAmount());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CashInInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Cash In Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cashInInquiry(messageVO);
//            messageVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            messageVO.setResponseCodeDescription("Successful");
//            messageVO.setMobileNo("03362020420");
//            messageVO.setConsumerName("Faisal Mehmood M");
//            messageVO.setCnicNo("4946495656565");
//            messageVO.setTransactionAmount("126.0");
//            messageVO.setCommissionAmount("0.0");
//            messageVO.setTransactionProcessingAmount("0.0");
//            messageVO.setTotalAmount("126.0");
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Cash In Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            response.setCustomerMobile(messageVO.getMobileNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setCnic(messageVO.getCnicNo());
            response.setAmount(messageVO.getTransactionAmount());
            response.setCommissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingCharges(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getCustomerMobile())
                .append(response.getCustomerName())
                .append(response.getCnic())
                .append(response.getAmount())
                .append(response.getCommissionAmount())
                .append(response.getTransactionProcessingCharges())
                .append(response.getTotalAmount());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Cash In Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CashInResponse cashIn(CashInRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Cash In Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CashInResponse response = new CashInResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPaymentMode(request.getPaymentMode());
        messageVO.setSegmentCode(request.getSegmentCode());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CashIn");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Cash In Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cashIn(messageVO);

        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Cash In Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            response.setCustomerMobile(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setTransactionDateTime(messageVO.getDateTime());
            response.setTransactionProcessingCharges(messageVO.getTransactionProcessingAmount());
            response.setCommissionAmount(messageVO.getCommissionAmount());
            response.setTotalAmount(messageVO.getTotalAmount());
            response.setTransactionId(messageVO.getTransactionId());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setRemainingBalance(messageVO.getRemainingBalance());
            if (StringUtils.isNotEmpty(messageVO.getResponseContentXML()))
                XMLUtil.populateFromResponse(response, messageVO.getResponseContentXML(), CashInResponseEnum.values());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Cash In Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Cash In Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getCustomerMobile())
                .append(response.getCnic())
                .append(response.getTransactionDateTime())
                .append(response.getTransactionProcessingCharges())
                .append(response.getCommissionAmount())
                .append(response.getTotalAmount())
                .append(response.getTransactionId())
                .append(response.getTransactionAmount())
                .append(response.getRemainingBalance());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Cash In REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public TitleFetchResponse titleFetch(TitleFetchRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Cash In Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        TitleFetchResponse response = new TitleFetchResponse();

        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setPaymentMode(request.getPaymentMode());
        messageVO.setSegmentCode(request.getSegmentCode());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
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
        logModel.setTransactionCode("Titlefetch");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Title Fetch Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.TitleFetch(messageVO);

        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Title Fetch Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setDateTime(messageVO.getDateTime());
            response.setBalance(messageVO.getBalance());
            response.setRemainigCreditLimit(messageVO.getRemainingCreditLimit());
            response.setRemainingDebitLimit(messageVO.getRemainingDebitLimit());
            response.setConsumedVilocity(messageVO.getConsumedVelocity());
            response.setCustomerMobile(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setAccountTitle(messageVO.getAccountTitle());
            if (StringUtils.isNotEmpty(messageVO.getResponseContentXML()))
                XMLUtil.populateFromResponse(response, messageVO.getResponseContentXML(), CashInResponseEnum.values());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Title Fetch Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Title Fetch Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getCustomerMobile())
                .append(response.getDateTime())
                .append(response.getCnic())
                .append(response.getCustomerName())
                .append(response.getAccountTitle())
                .append(response.getBalance())
                .append(response.getRemainigCreditLimit())
                .append(response.getRemainingDebitLimit())
                .append(response.getConsumedVilocity());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setDataHash(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Title Fetch REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public CashInAgentResponse cashInAgent(CashInAgentRequest cashInAgentRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = cashInAgentRequest.getDateTime() + cashInAgentRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(cashInAgentRequest.getRrn());
        logger.info("[HOST] Starting Processing Cash In Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = cashInAgentRequest.getChannelId() + cashInAgentRequest.getRrn();

        CashInAgentResponse response = new CashInAgentResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(cashInAgentRequest.getUserName());
        webServiceVO.setCustomerPassword(cashInAgentRequest.getPassword());
        webServiceVO.setChannelId(cashInAgentRequest.getChannelId());
        webServiceVO.setTerminalId(cashInAgentRequest.getTerminalID());
        webServiceVO.setTransactionAmount(cashInAgentRequest.getAmount());
        webServiceVO.setDateTime(cashInAgentRequest.getDateTime());
        webServiceVO.setAgentMobileNumber(cashInAgentRequest.getMobileNumber());
        webServiceVO.setPaymentMode(cashInAgentRequest.getPaymentMode());
        webServiceVO.setSegmentCode(cashInAgentRequest.getSegmentCode());
        webServiceVO.setReserved1(cashInAgentRequest.getReserved1());
        webServiceVO.setReserved2(cashInAgentRequest.getReserved2());
        webServiceVO.setReserved3(cashInAgentRequest.getReserved3());
        webServiceVO.setReserved4(cashInAgentRequest.getReserved4());
        webServiceVO.setReserved5(cashInAgentRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(cashInAgentRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(cashInAgentRequest.getChannelId());
        logModel.setTransactionCode("AgentCashIn");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(cashInAgentRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Cash In Agent Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            String input = "1";

            if (input.equals("1")) {
                webServiceVO = switchController.cashInAgent(webServiceVO);
            } else {
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("successFull");
                webServiceVO.setRetrievalReferenceNumber("12345678822");
                webServiceVO.setDateTime("20170706121212");
                webServiceVO.setMobileNo("03201234567");
                webServiceVO.setCommissionAmount("0.00");
                webServiceVO.setTransactionProcessingAmount("100.00");
                webServiceVO.setTotalAmount("2100.00");
                webServiceVO.setTransactionId("396382751019");
                webServiceVO.setCnicNo("3520243953533");
                webServiceVO.setTransactionAmount("2020.00");
                webServiceVO.setRemainingBalance("10000.00");
                webServiceVO.setResponseContentXML("");

            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Title Fetch Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionDateTime(webServiceVO.getDateTime());
            response.setAgentMobileNumber(webServiceVO.getMobileNo());
            response.setCommisionAmount(webServiceVO.getCommissionAmount());
            response.setTransactionProcessingAmount(webServiceVO.getTransactionProcessingAmount());
            response.setTotalAmount(webServiceVO.getTotalAmount());
            response.setTransactionId(webServiceVO.getTransactionId());
            response.setCnic(webServiceVO.getCnicNo());
            response.setTransactionAmount(webServiceVO.getTransactionAmount());
            response.setRemainingBalance(webServiceVO.getRemainingBalance());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Agent Cash In Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Cash In Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getAgentMobileNumber())
                .append(response.getCnic())
                .append(response.getTransactionDateTime())
                .append(response.getTransactionProcessingAmount())
                .append(response.getCommisionAmount())
                .append(response.getTotalAmount())
                .append(response.getTransactionId())
                .append(response.getTransactionAmount())
                .append(response.getRemainingBalance());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Cash In REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public CashOutInquiryResponse cashOutInquiry(CashOutInquiryRequest cashOutInquiryRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = cashOutInquiryRequest.getDateTime() + cashOutInquiryRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(cashOutInquiryRequest.getRrn());
        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = cashOutInquiryRequest.getChannelId() + cashOutInquiryRequest.getRrn();

        CashOutInquiryResponse response = new CashOutInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(cashOutInquiryRequest.getUserName());
        webServiceVO.setCustomerPassword(cashOutInquiryRequest.getPassword());
        webServiceVO.setChannelId(cashOutInquiryRequest.getChannelId());
        webServiceVO.setTerminalId(cashOutInquiryRequest.getTerminalID());
        webServiceVO.setTransactionAmount(cashOutInquiryRequest.getAmount());
        webServiceVO.setReserved1(cashOutInquiryRequest.getReserved1());
        webServiceVO.setReserved2(cashOutInquiryRequest.getReserved2());
        webServiceVO.setReserved3(cashOutInquiryRequest.getReserved3());
        webServiceVO.setReserved4(cashOutInquiryRequest.getReserved4());
        webServiceVO.setReserved5(cashOutInquiryRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(cashOutInquiryRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(cashOutInquiryRequest.getChannelId());
        logModel.setTransactionCode("CashOutInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(cashOutInquiryRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Cash Out Inquiry Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            String input = "1";

            if (input.equals("1")) {
                webServiceVO = switchController.cashInAgent(webServiceVO);
            } else {
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("successFull");
                webServiceVO.setRetrievalReferenceNumber("12345678822");
                webServiceVO.setMobileNo("03201234567");
                webServiceVO.setTotalAmount("2100.00");
                webServiceVO.setCnicNo("3520243953533");
                webServiceVO.setConsumerName("Ahsan");
                webServiceVO.setResponseContentXML("");

            }
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);
        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Cash Out Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setCustomerName(webServiceVO.getConsumerName());
            response.setMobileNumber(webServiceVO.getMobileNo());
            response.setAmount(webServiceVO.getTotalAmount());
            response.setCnic(webServiceVO.getCnicNo());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Cash Out Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Cash Out Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getCustomerName())
                .append(response.getCnic())
                .append(response.getMobileNumber())
                .append(response.getAmount());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Customer Cash Out Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public CashOutResponse cashOut(CashOutRequest cashOutRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = cashOutRequest.getTransactiondateTime() + cashOutRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(cashOutRequest.getRrn());
        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = cashOutRequest.getChannelId() + cashOutRequest.getRrn();

        CashOutResponse response = new CashOutResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(cashOutRequest.getUserName());
        webServiceVO.setCustomerPassword(cashOutRequest.getPassword());
        webServiceVO.setChannelId(cashOutRequest.getChannelId());
        webServiceVO.setTerminalId(cashOutRequest.getTerminalID());
        webServiceVO.setTransactionAmount(cashOutRequest.getAmount());
        webServiceVO.setCharges(cashOutRequest.getCharges());
//        webServiceVO.setOtpPin(cashOutRequest.getOtp());
//        webServiceVO.setMobilePin(cashOutRequest.getMpin());

        try {
            if (cashOutRequest.getMpin() != null && !cashOutRequest.getMpin().equals("")) {
                String text = cashOutRequest.getMpin();
                String mpin = text.replaceAll("\\r|\\n", "");
                webServiceVO.setMobilePin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            } else if (cashOutRequest.getOtp() != null && !cashOutRequest.getOtp().equals("")) {
                String text = cashOutRequest.getOtp();
                String otp = text.replaceAll("\\r|\\n", "");
                webServiceVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            }

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setMobileNo(cashOutRequest.getMobileNumber());
        webServiceVO.setPaymentMode(cashOutRequest.getPaymentType());
        webServiceVO.setReserved1(cashOutRequest.getReserved1());
        webServiceVO.setReserved2(cashOutRequest.getReserved2());
        webServiceVO.setReserved3(cashOutRequest.getReserved3());
        webServiceVO.setReserved4(cashOutRequest.getReserved4());
        webServiceVO.setReserved5(cashOutRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(cashOutRequest.getTransactiondateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(cashOutRequest.getChannelId());
        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(cashOutRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Cash Out  Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            String input = "1";

            if (input.equals("1")) {
                webServiceVO = switchController.cashOut(webServiceVO);
            } else {
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("successFull");
                webServiceVO.setRetrievalReferenceNumber("12345678822");
                webServiceVO.setMicrobankTransactionCode("1222025");
                webServiceVO.setConsumerName("Ahsan");
                webServiceVO.setResponseContentXML("");

            }
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);
        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Cash Out  Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
            response.setTransactionId(webServiceVO.getTransactionId());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Cash Out  Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Cash Out  Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Customer Cash Out  REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public MpinRegistrationResponse mpinRegistration(MpinRegistrationRequest mpinRegistrationRequest) {
        long startTime = new Date().getTime(); // start time
//        String transactionKey = mpinRegistrationRequest.getTransactiondateTime() + mpinRegistrationRequest.getRrn();
//        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = mpinRegistrationRequest.getChannelId() + mpinRegistrationRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(mpinRegistrationRequest.getRrn());

        MpinRegistrationResponse response = new MpinRegistrationResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(mpinRegistrationRequest.getUserName());
        webServiceVO.setCustomerPassword(mpinRegistrationRequest.getPassword());
        webServiceVO.setChannelId(mpinRegistrationRequest.getChannelId());
        webServiceVO.setTerminalId(mpinRegistrationRequest.getTerminalId());
//        webServiceVO.setMobilePin(EncryptionUtil.encrypt(mpinRegistrationRequest.getMpin()));
        try {
            String text;
            String newPin;
            text = mpinRegistrationRequest.getMpin();
            newPin = text.replaceAll("\\r|\\n", "");
            String newMpin = (RSAEncryption.decrypt(newPin, loginPrivateKey));
            webServiceVO.setMobilePin(EncryptionUtil.encrypt(newMpin));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(mpinRegistrationRequest.getConfirmMpin()));
        try {
            String text;
            String confimPin;
            text = mpinRegistrationRequest.getConfirmMpin();
            confimPin = text.replaceAll("\\r|\\n", "");
            String confirmMpin = (RSAEncryption.decrypt(confimPin, loginPrivateKey));
            webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(confirmMpin));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setMobileNo(mpinRegistrationRequest.getMobileNumber());
        webServiceVO.setReserved1(mpinRegistrationRequest.getReserved1());
        webServiceVO.setReserved2(mpinRegistrationRequest.getReserved2());
        webServiceVO.setReserved3(mpinRegistrationRequest.getReserved3());
        webServiceVO.setReserved4(mpinRegistrationRequest.getReserved4());
        webServiceVO.setReserved5(mpinRegistrationRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(mpinRegistrationRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(mpinRegistrationRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(mpinRegistrationRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Mpin Restration Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.mpinRegistration(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Mpin Registration  Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Mpin Registration  Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Mpin Registration  Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Customer Mpin Registration  REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public MpinChangeResponse mpinChange(MpinChangeRequest mpinChangeRequest) {
        long startTime = new Date().getTime(); // start time
//        String transactionKey = mpinRegistrationRequest.getTransactiondateTime() + mpinRegistrationRequest.getRrn();
//        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = mpinChangeRequest.getChannelId() + mpinChangeRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(mpinChangeRequest.getRrn());

        MpinChangeResponse response = new MpinChangeResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(mpinChangeRequest.getUserName());
        webServiceVO.setCustomerPassword(mpinChangeRequest.getPassword());
        webServiceVO.setChannelId(mpinChangeRequest.getChannelId());
        webServiceVO.setTerminalId(mpinChangeRequest.getTerminalId());
//        webServiceVO.setOldMpin(EncryptionUtil.encrypt(mpinChangeRequest.getOldMpin()));
        try {
            String text;
            String oldMpin;
            text = mpinChangeRequest.getOldMpin();
            oldMpin = text.replaceAll("\\r|\\n", "");
            String oldPin = (RSAEncryption.decrypt(oldMpin, loginPrivateKey));
            webServiceVO.setOldMpin(EncryptionUtil.encrypt(oldPin));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        webServiceVO.setMobilePin(EncryptionUtil.encrypt(mpinChangeRequest.getNewMpin()));
        try {
            String text;
            String newPin;
            text = mpinChangeRequest.getNewMpin();
            newPin = text.replaceAll("\\r|\\n", "");
            String newMpin = (RSAEncryption.decrypt(newPin, loginPrivateKey));
            webServiceVO.setMobilePin(EncryptionUtil.encrypt(newMpin));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(mpinChangeRequest.getConfirmMpin()));
        try {
            String text;
            String confirmMPin;
            text = mpinChangeRequest.getConfirmMpin();
            confirmMPin = text.replaceAll("\\r|\\n", "");
            String confirmPin = (RSAEncryption.decrypt(confirmMPin, loginPrivateKey));
            webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(confirmPin));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setMobileNo(mpinChangeRequest.getMobileNumber());
        webServiceVO.setReserved1(mpinChangeRequest.getReserved1());
        webServiceVO.setReserved2(mpinChangeRequest.getReserved2());
        webServiceVO.setReserved3(mpinChangeRequest.getReserved3());
        webServiceVO.setReserved4(mpinChangeRequest.getReserved4());
        webServiceVO.setReserved5(mpinChangeRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(mpinChangeRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(mpinChangeRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(mpinChangeRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Mpin Change Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.mpinChange(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Mpin Change Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Mpin Change Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Mpin Change Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime();    // end time
        long difference = endTime - startTime;  // check different
        logger.debug("[HOST] ****Customer Registration REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public WalletToWalletPaymentInquiryResponse walletToWalletPaymentInquiryResponse(WalletToWalletPaymentInquiryRequest walletToWalletPaymentInquiryRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = walletToWalletPaymentInquiryRequest.getDateTime() + walletToWalletPaymentInquiryRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(walletToWalletPaymentInquiryRequest.getRrn());
        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = walletToWalletPaymentInquiryRequest.getChannelId() + walletToWalletPaymentInquiryRequest.getRrn();

        WalletToWalletPaymentInquiryResponse response = new WalletToWalletPaymentInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(walletToWalletPaymentInquiryRequest.getUserName());
        webServiceVO.setCustomerPassword(walletToWalletPaymentInquiryRequest.getPassword());
        webServiceVO.setMobileNo(walletToWalletPaymentInquiryRequest.getMobileNumber());
        webServiceVO.setDateTime(walletToWalletPaymentInquiryRequest.getDateTime());
        webServiceVO.setTotalAmount(walletToWalletPaymentInquiryRequest.getAmount());
        webServiceVO.setChannelId(walletToWalletPaymentInquiryRequest.getChannelId());
        webServiceVO.setTerminalId(walletToWalletPaymentInquiryRequest.getTerminalId());
        webServiceVO.setReceiverMobileNumber(walletToWalletPaymentInquiryRequest.getReceiverMobileNumber());
        webServiceVO.setReserved1(walletToWalletPaymentInquiryRequest.getReserved1());
        webServiceVO.setReserved2(walletToWalletPaymentInquiryRequest.getReserved2());
        webServiceVO.setReserved3(walletToWalletPaymentInquiryRequest.getReserved3());
        webServiceVO.setReserved4(walletToWalletPaymentInquiryRequest.getReserved4());
        webServiceVO.setReserved5(walletToWalletPaymentInquiryRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(walletToWalletPaymentInquiryRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(walletToWalletPaymentInquiryRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(walletToWalletPaymentInquiryRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Wallet Payment Inquiry Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.walletToWalletPaymentInquiry(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Wallet Payment Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setCustomerMobile(webServiceVO.getMobileNo());
            response.setTransactionDateTime(webServiceVO.getTransactionDateTime());
            response.setTransactionProcessingAmount(webServiceVO.getTransactionProcessingAmount());
            response.setCommissionAmount(webServiceVO.getCommissionAmount());
            response.setTotalAmount(webServiceVO.getTotalAmount());
            response.setTransactionId(webServiceVO.getTransactionId());
            response.setTransactionAmount(webServiceVO.getTransactionAmount());
            response.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
            response.setRecieverAccountTitle(webServiceVO.getRecieverAccountTilte());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getCustomerMobile())
                .append(response.getTransactionDateTime())
                .append(response.getTransactionProcessingAmount())
                .append(response.getCommissionAmount())
                .append(response.getTotalAmount())
                .append(response.getTransactionId())
                .append(response.getTransactionAmount())
                .append(response.getReceiverMobileNumber());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet to wallet payment inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public WalletToWalletPaymentResponse walletToWalletPaymentResponse(WalletToWalletPaymentRequest walletToWalletPaymentRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = walletToWalletPaymentRequest.getDateTime() + walletToWalletPaymentRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(walletToWalletPaymentRequest.getRrn());
        logger.info("[HOST] Starting Processing Customer Cash Out Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = walletToWalletPaymentRequest.getChannelId() + walletToWalletPaymentRequest.getRrn();

        WalletToWalletPaymentResponse response = new WalletToWalletPaymentResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(walletToWalletPaymentRequest.getUserName());
        webServiceVO.setCustomerPassword(walletToWalletPaymentRequest.getPassword());
//        webServiceVO.setMobilePin(walletToWalletPaymentRequest.getMpin());
        webServiceVO.setMobileNo(walletToWalletPaymentRequest.getMobileNumber());
        webServiceVO.setDateTime(walletToWalletPaymentRequest.getDateTime());
        webServiceVO.setChannelId(walletToWalletPaymentRequest.getChannelId());
        webServiceVO.setTerminalId(walletToWalletPaymentRequest.getTerminalId());
//        webServiceVO.setOtpPin(walletToWalletPaymentRequest.getOtp());
        try {
            if (walletToWalletPaymentRequest.getOtp() != null && !walletToWalletPaymentRequest.getOtp().equals("")) {
                String text = walletToWalletPaymentRequest.getOtp();
                String otp = text.replaceAll("\\r|\\n", "");
                webServiceVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            } else if (walletToWalletPaymentRequest.getMpin() != null && !walletToWalletPaymentRequest.getMpin().equals("")) {
                String text = walletToWalletPaymentRequest.getMpin();
                String pin = text.replaceAll("\\r|\\n", "");
                webServiceVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
            }


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setReceiverMobileNumber(walletToWalletPaymentRequest.getReceiverMobileNumber());
        webServiceVO.setTotalAmount(walletToWalletPaymentRequest.getAmount());
        webServiceVO.setReserved1(walletToWalletPaymentRequest.getReserved1());
        webServiceVO.setReserved2(walletToWalletPaymentRequest.getReserved2());
        webServiceVO.setReserved3(walletToWalletPaymentRequest.getReserved3());
        webServiceVO.setReserved4(walletToWalletPaymentRequest.getReserved4());
        webServiceVO.setReserved5(walletToWalletPaymentRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(walletToWalletPaymentRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(walletToWalletPaymentRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(walletToWalletPaymentRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Wallet Payment Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.walletToWalletPayment(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Wallet Payment Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setCustomerMobile(webServiceVO.getMobileNo());
            response.setTransactionDateTime(webServiceVO.getTransactionDateTime());
            response.setTransactionProcessingAmount(webServiceVO.getTransactionProcessingAmount());
            response.setCommissionAmount(webServiceVO.getCommissionAmount());
            response.setTotalAmount(webServiceVO.getTotalAmount());
            response.setTransactionId(webServiceVO.getTransactionId());
            response.setTransactionAmount(webServiceVO.getTransactionAmount());
            response.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
//                        if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getCustomerMobile())
                .append(response.getTransactionDateTime());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet to wallet payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public UpgradeAccountInquiryResponse upgradeAccountInquiry(UpgradeAccountInquiryRequest upgradeAccountInquiryRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = upgradeAccountInquiryRequest.getDateTime() + upgradeAccountInquiryRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(upgradeAccountInquiryRequest.getRrn());
        logger.info("[HOST] Starting Processing Upgrade Account Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = upgradeAccountInquiryRequest.getChannelId() + upgradeAccountInquiryRequest.getRrn();

        UpgradeAccountInquiryResponse response = new UpgradeAccountInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(upgradeAccountInquiryRequest.getUserName());
        webServiceVO.setCustomerPassword(upgradeAccountInquiryRequest.getPassword());
        webServiceVO.setMobileNo(upgradeAccountInquiryRequest.getMobileNumber());
        webServiceVO.setDateTime(upgradeAccountInquiryRequest.getDateTime());
        webServiceVO.setChannelId(upgradeAccountInquiryRequest.getChannelId());
        webServiceVO.setTerminalId(upgradeAccountInquiryRequest.getTerminalId());
        webServiceVO.setReserved1(upgradeAccountInquiryRequest.getReserved1());
        webServiceVO.setReserved2(upgradeAccountInquiryRequest.getReserved2());
        webServiceVO.setReserved3(upgradeAccountInquiryRequest.getReserved3());
        webServiceVO.setReserved4(upgradeAccountInquiryRequest.getReserved4());
        webServiceVO.setReserved5(upgradeAccountInquiryRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(upgradeAccountInquiryRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(upgradeAccountInquiryRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(upgradeAccountInquiryRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Upgrade Account inquiry Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.upgradeAccountInquiry(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Upgrade Account Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Upgrade Account Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public UpgradeMinorAccountInquiryResponse upgradeMinorAccountInquiryResponse(UpgradeMinorAccountInquiryRequest upgradeAccountInquiryRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = upgradeAccountInquiryRequest.getDateTime() + upgradeAccountInquiryRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(upgradeAccountInquiryRequest.getRrn());
        logger.info("[HOST] Starting Processing Upgrade Account Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = upgradeAccountInquiryRequest.getChannelId() + upgradeAccountInquiryRequest.getRrn();

        UpgradeMinorAccountInquiryResponse response = new UpgradeMinorAccountInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(upgradeAccountInquiryRequest.getUserName());
        webServiceVO.setCustomerPassword(upgradeAccountInquiryRequest.getPassword());
        webServiceVO.setMobileNo(upgradeAccountInquiryRequest.getMobileNumber());
        webServiceVO.setDateTime(upgradeAccountInquiryRequest.getDateTime());
        webServiceVO.setChannelId(upgradeAccountInquiryRequest.getChannelId());
        webServiceVO.setTerminalId(upgradeAccountInquiryRequest.getTerminalId());
        webServiceVO.setReserved1(upgradeAccountInquiryRequest.getReserved1());
        webServiceVO.setReserved2(upgradeAccountInquiryRequest.getReserved2());
        webServiceVO.setReserved3(upgradeAccountInquiryRequest.getReserved3());
        webServiceVO.setReserved4(upgradeAccountInquiryRequest.getReserved4());
        webServiceVO.setReserved5(upgradeAccountInquiryRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(upgradeAccountInquiryRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(upgradeAccountInquiryRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(upgradeAccountInquiryRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Upgrade Account inquiry Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.upgradeAccountInquiry(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Upgrade Account Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Upgrade Account Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public UpgradeAccountResponse upgradeAccountResponse(UpgradeAccountRequest upgradeAccountRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = upgradeAccountRequest.getDateTime() + upgradeAccountRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(upgradeAccountRequest.getRrn());
        logger.info("[HOST] Starting Processing Upgrade Account Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = upgradeAccountRequest.getChannelId() + upgradeAccountRequest.getRrn();

        UpgradeAccountResponse response = new UpgradeAccountResponse();

        webServiceVO.setUserName(upgradeAccountRequest.getUserName());
        webServiceVO.setCustomerPassword(upgradeAccountRequest.getPassword());
        webServiceVO.setMobileNo(upgradeAccountRequest.getMobileNumber());
        webServiceVO.setDateTime(upgradeAccountRequest.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(upgradeAccountRequest.getChannelId());
        webServiceVO.setTerminalId(upgradeAccountRequest.getTerminalId());
        if (upgradeAccountRequest.getReserved1().equals("02")) {
//            webServiceVO.setOtpPin(upgradeAccountRequest.getMpin());
            try {
                String text;
                String pin;
                if (upgradeAccountRequest.getMpin() != null) {
                    text = upgradeAccountRequest.getMpin();
                    pin = text.replaceAll("\\r|\\n", "");
                    webServiceVO.setOtpPin(RSAEncryption.decrypt(pin, loginPrivateKey));
                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String text;
                String pin;
                if (upgradeAccountRequest.getMpin() != null) {
                    text = upgradeAccountRequest.getMpin();
                    pin = text.replaceAll("\\r|\\n", "");
                    webServiceVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        webServiceVO.setCnicNo(upgradeAccountRequest.getCnic());
        webServiceVO.setFingerIndex(upgradeAccountRequest.getFingerIndex());
        webServiceVO.setFingerTemplate(upgradeAccountRequest.getFingerTemplate());
        webServiceVO.setTemplateType(upgradeAccountRequest.getTemplateType());

//        webServiceVO.setMobilePin(upgradeAccountRequest.getMpin());
//        webServiceVO.setOtpPin(upgradeAccountRequest.getMpin());
        webServiceVO.setReserved1(upgradeAccountRequest.getReserved1());
//        if (upgradeAccountRequest.getReserved2() != null || upgradeAccountRequest.getReserved2().equals("")) {
//            webServiceVO.setReserved2("0");
//        } else {
//            webServiceVO.setReserved2(upgradeAccountRequest.getReserved2());
//        }
        if (upgradeAccountRequest.getReserved2().equals("")) {
            webServiceVO.setReserved2("0");
        } else {
            webServiceVO.setReserved2(upgradeAccountRequest.getReserved2());
        }
//        webServiceVO.setReserved2(upgradeAccountRequest.getReserved2());
        webServiceVO.setReserved3(upgradeAccountRequest.getReserved3());
        webServiceVO.setReserved4(upgradeAccountRequest.getReserved4());
        webServiceVO.setReserved5(upgradeAccountRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(upgradeAccountRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(upgradeAccountRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(upgradeAccountRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Upgrade Account Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.upgradeAccount(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);
        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Upgrade Account Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Customer Upgrade Account REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public UpgradeMinorAccountResponse upgradeMinorAccountResponse(UpgradeMinorAccountRequest upgradeAccountRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = upgradeAccountRequest.getDateTime() + upgradeAccountRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(upgradeAccountRequest.getRrn());
        logger.info("[HOST] Starting Processing Upgrade Account Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = upgradeAccountRequest.getChannelId() + upgradeAccountRequest.getRrn();

        UpgradeMinorAccountResponse response = new UpgradeMinorAccountResponse();

        webServiceVO.setUserName(upgradeAccountRequest.getUserName());
        webServiceVO.setCustomerPassword(upgradeAccountRequest.getPassword());
        webServiceVO.setMobileNo(upgradeAccountRequest.getMobileNumber());
        webServiceVO.setDateTime(upgradeAccountRequest.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(upgradeAccountRequest.getChannelId());
        webServiceVO.setTerminalId(upgradeAccountRequest.getTerminalId());
        webServiceVO.setParentCnicPic(upgradeAccountRequest.getParentCnicPic());
        webServiceVO.setSnicPic(upgradeAccountRequest.getSnicPic());
        webServiceVO.setMinorCustomerPic(upgradeAccountRequest.getMinorCutomerPic());
        webServiceVO.setbFormPic(upgradeAccountRequest.getBFormPic());
        webServiceVO.setCnicBackPhoto(upgradeAccountRequest.getSnicBackPic());
        webServiceVO.setsNicBackPic(upgradeAccountRequest.getSnicBackPic());
        webServiceVO.setParentNicBackPic(upgradeAccountRequest.getParentnicBackPic());
        webServiceVO.setReserved3(upgradeAccountRequest.getReserved3());
        webServiceVO.setReserved4(upgradeAccountRequest.getReserved4());
        webServiceVO.setReserved5(upgradeAccountRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(upgradeAccountRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(upgradeAccountRequest.getChannelId());
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(upgradeAccountRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Customer Upgrade Account Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            webServiceVO = switchController.updateMinorAccount(webServiceVO);

        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Customer Upgrade Account Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Customer Upgrade Account Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Customer Upgrade Account REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }


//    public FatherBvsVerificationResponse minorFatherBvsVerification(FatherBvsVerification fatherBvsVerification) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO webServiceVO = new WebServiceVO();
//        String transactionKey = fatherBvsVerification.getDateTime() + fatherBvsVerification.getRrn();
//        webServiceVO.setRetrievalReferenceNumber(fatherBvsVerification.getRrn());
//        logger.info("[HOST] Starting Processing Minor Father BVS Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
//        transactionKey = fatherBvsVerification.getChannelId() + fatherBvsVerification.getRrn();
//
//        FatherBvsVerificationResponse response = new FatherBvsVerificationResponse();
//
//        webServiceVO.setUserName(fatherBvsVerification.getUserName());
//        webServiceVO.setCustomerPassword(fatherBvsVerification.getPassword());
//        webServiceVO.setMobileNo(fatherBvsVerification.getMobileNumber());
//        webServiceVO.setDateTime(fatherBvsVerification.getDateTime());
//        webServiceVO.setCnicNo(fatherBvsVerification.getsNic());
//        webServiceVO.setFatherCnic(fatherBvsVerification.getFatherCnic());
//        webServiceVO.setFatherMotherMobileNumber(fatherBvsVerification.getFatherMobileNumber());
//        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
//        webServiceVO.setChannelId(fatherBvsVerification.getChannelId());
//        webServiceVO.setTerminalId(fatherBvsVerification.getTerminalId());
//        webServiceVO.setReserved3(fatherBvsVerification.getReserved3());
//        webServiceVO.setReserved4(fatherBvsVerification.getReserved4());
//        webServiceVO.setReserved5(fatherBvsVerification.getReserved5());
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(fatherBvsVerification.getDateTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(fatherBvsVerification.getChannelId());
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = JSONUtil.getJSON(fatherBvsVerification);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Minor Father BVS Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//
//            webServiceVO = switchController.minorFatherBvsVerification(webServiceVO);
//
//        } catch (Exception e) {
//
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);
//
//        }
//
//        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Minor Father BVS Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
//            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
//        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
//            logger.info("[HOST] Minor Father BVS Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            response.setResponseCode(webServiceVO.getResponseCode());
//            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Minor Father BVS Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuilder stringText = new StringBuilder()
//                .append(response.getResponseCode())
//                .append(response.getResponseDescription())
//                .append(response.getRrn());
//        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Minor Father BVS REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = JSONUtil.getJSON(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//
//
//        return response;
//    }


    public AccountStatusChangeResponse accountStatusChange(AccountStatusChangeRequest accountStatusChangeRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = accountStatusChangeRequest.getDateTime() + accountStatusChangeRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(accountStatusChangeRequest.getRrn());
        logger.info("[HOST] Starting Processing Account status change Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = accountStatusChangeRequest.getChannelId() + accountStatusChangeRequest.getRrn();

        AccountStatusChangeResponse response = new AccountStatusChangeResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(accountStatusChangeRequest.getUserName());
        webServiceVO.setCustomerPassword(accountStatusChangeRequest.getPassword());
        webServiceVO.setCnicNo(accountStatusChangeRequest.getCnic());
        webServiceVO.setAccountStatus(accountStatusChangeRequest.getAccountStatus());
//        webServiceVO.setOtpPin(accountStatusChangeRequest.getMpin());
        try {
            String text;
            String pin;
            if (accountStatusChangeRequest.getMpin() != null) {
                text = accountStatusChangeRequest.getMpin();
                pin = text.replaceAll("\\r|\\n", "");
                webServiceVO.setOtpPin(RSAEncryption.decrypt(pin, loginPrivateKey));

            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setMobileNo(accountStatusChangeRequest.getMobileNumber());
        webServiceVO.setDateTime(accountStatusChangeRequest.getDateTime());
        webServiceVO.setChannelId(accountStatusChangeRequest.getChannelId());
        webServiceVO.setTerminalId(accountStatusChangeRequest.getTerminalId());
        webServiceVO.setReserved1(accountStatusChangeRequest.getReserved1());
        webServiceVO.setReserved2(accountStatusChangeRequest.getReserved2());
        webServiceVO.setReserved3(accountStatusChangeRequest.getReserved3());
        webServiceVO.setReserved4(accountStatusChangeRequest.getReserved4());
        webServiceVO.setReserved5(accountStatusChangeRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(accountStatusChangeRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(accountStatusChangeRequest.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(accountStatusChangeRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account Status Change Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.accountStatusChange(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Status Change Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//
//            logModel.setResponseCode(webServiceVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
            //}
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Account Status Change Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Status Change Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getTransactionCode());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Account Status Change REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);


        return response;
    }

    public IbftTitleFetchResponse ibftTitleFetchResponse(IbftTitleFetchRequest ibftTitleFetchRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = ibftTitleFetchRequest.getDateTime() + ibftTitleFetchRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(ibftTitleFetchRequest.getRrn());
        logger.info("[HOST] Starting Processing Account status change Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = ibftTitleFetchRequest.getChannelId() + ibftTitleFetchRequest.getRrn();

        IbftTitleFetchResponse response = new IbftTitleFetchResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(ibftTitleFetchRequest.getUserName());
        webServiceVO.setCustomerPassword(ibftTitleFetchRequest.getPassword());
        webServiceVO.setDestinationAccountNumber(ibftTitleFetchRequest.getDestinationAccount());
        webServiceVO.setSourceBankImd(ibftTitleFetchRequest.getSourceBankImd());
        webServiceVO.setDestinationBankImd(ibftTitleFetchRequest.getDestinationBankIMD());
        webServiceVO.setSenderMobileNumber(ibftTitleFetchRequest.getSenderMobileNumber());
        webServiceVO.setMobileNo(ibftTitleFetchRequest.getRecieverMobileNumber());
        webServiceVO.setTransactionAmount(ibftTitleFetchRequest.getAmount());
        webServiceVO.setDateTime(ibftTitleFetchRequest.getDateTime());
        webServiceVO.setChannelId(ibftTitleFetchRequest.getChannelId());
        webServiceVO.setTerminalId(ibftTitleFetchRequest.getTerminalId());
        webServiceVO.setReserved1(ibftTitleFetchRequest.getReserved1());
        webServiceVO.setReserved2(ibftTitleFetchRequest.getReserved2());
        webServiceVO.setReserved3(ibftTitleFetchRequest.getReserved3());
        webServiceVO.setReserved4(ibftTitleFetchRequest.getReserved4());
        webServiceVO.setReserved5(ibftTitleFetchRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(ibftTitleFetchRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(ibftTitleFetchRequest.getChannelId());
        logModel.setTransactionCode("IBFTTitleFetch");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(ibftTitleFetchRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent IBFT Title Fetch Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.ibftTitleFetch(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] IBFT Title Fetch Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setAccountBankName(webServiceVO.getBankName());
            response.setRecieverAccountTitle(webServiceVO.getRecieverAccountTilte());
            response.setRecieverAcountNumber(webServiceVO.getRecieverAccountNumber());
            response.setSenderMobileNumber(webServiceVO.getSenderMobileNumber());
            response.setSenderAccountTitle(webServiceVO.getSenderAccountTitle());
            response.setAmount(webServiceVO.getTotalAmount());
            response.setCharges(webServiceVO.getCharges());
            response.setAccountBranchName(webServiceVO.getBranchName());
            response.setBenificieryIBAN(webServiceVO.getBenificieryIban());
            response.setSourceBankImd(webServiceVO.getSourceBankImd());
            response.setDateTime(webServiceVO.getDateTime());

//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//                logModel.setResponseCode(webServiceVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//            }
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] IBFT Title Fetch Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] IBFT Title Fetch Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()

                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getSenderMobileNumber())
                .append(response.getRecieverAcountNumber())
                .append(response.getSenderAccountTitle())
                .append(response.getRecieverAccountTitle())
                .append(response.getAmount())
                .append(response.getCharges())
                .append(response.getDateTime())
                .append(response.getAccountBankName())
                .append(response.getAccountBranchName())
                .append(response.getBenificieryIBAN())
                .append(response.getSourceBankImd())
                .append(response.getRrn());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****IBFT Title Fetch REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public IbftAdviceResponse ibftAdviceResponse(IbftAdviceRequest ibftAdviceRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = ibftAdviceRequest.getDateTime() + ibftAdviceRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(ibftAdviceRequest.getRrn());
        logger.info("[HOST] Starting Processing IBFT Title Fetch Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = ibftAdviceRequest.getChannelId() + ibftAdviceRequest.getRrn();
        IbftAdviceResponse response = new IbftAdviceResponse();
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(ibftAdviceRequest.getUserName());
        webServiceVO.setCustomerPassword(ibftAdviceRequest.getPassword());
        webServiceVO.setDestinationAccountNumber(ibftAdviceRequest.getDestinationAccount());
        webServiceVO.setSourceBankImd(ibftAdviceRequest.getSourceBankImd());
        webServiceVO.setDestinationBankImd(ibftAdviceRequest.getDestinationBankIMD());
        webServiceVO.setSenderMobileNumber(ibftAdviceRequest.getSenderMobileNumber());
        webServiceVO.setMobileNo(ibftAdviceRequest.getRecieverMobileNumber());
        webServiceVO.setTransactionAmount(ibftAdviceRequest.getAmount());
        webServiceVO.setDateTime(ibftAdviceRequest.getDateTime());
        webServiceVO.setChannelId(ibftAdviceRequest.getChannelId());
        webServiceVO.setTerminalId(ibftAdviceRequest.getTerminalId());
        webServiceVO.setSenderAccountTitle(ibftAdviceRequest.getSenderAccountTitle());
        webServiceVO.setRecieverAccountTilte(ibftAdviceRequest.getRecieverAccountTitle());
        webServiceVO.setBankName(ibftAdviceRequest.getToBankName());
        webServiceVO.setBranchName(ibftAdviceRequest.getToBranchName());
        webServiceVO.setBenificieryIban(ibftAdviceRequest.getBenificieryIban());
        webServiceVO.setPurposeOfPayment(ibftAdviceRequest.getPurposeOfPayment());
        webServiceVO.setReserved1(ibftAdviceRequest.getReserved());
//        webServiceVO.setOtpPin(ibftAdviceRequest.getmPIN());// ya otp ha
//        webServiceVO.setMobilePin(ibftAdviceRequest.getOtpPin());//ya mpin ha
        try {
            if (ibftAdviceRequest.getOtpPin() != null && !ibftAdviceRequest.getOtpPin().equals("")) {
                String text = ibftAdviceRequest.getOtpPin();
                String otp = text.replaceAll("\\r|\\n", "");
                webServiceVO.setMobilePin(RSAEncryption.decrypt(otp, loginPrivateKey));
            } else if (ibftAdviceRequest.getmPIN() != null && !ibftAdviceRequest.getmPIN().equals("")) {
                String text = ibftAdviceRequest.getmPIN();
                String mpin = text.replaceAll("\\r|\\n", "");
                webServiceVO.setOtpPin(RSAEncryption.decrypt(mpin, loginPrivateKey));
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        webServiceVO.setReserved2(ibftAdviceRequest.getReserved2());
        webServiceVO.setReserved3(ibftAdviceRequest.getReserved3());
        webServiceVO.setReserved4(ibftAdviceRequest.getReserved4());
        webServiceVO.setReserved5(ibftAdviceRequest.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(ibftAdviceRequest.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(ibftAdviceRequest.getChannelId());
        logModel.setTransactionCode("IBFTAdvice");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(ibftAdviceRequest);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent IBFT Advice Request to Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.ibftAdvice(webServiceVO);
//            } else {
//                webServiceVO.setResponseCode("00");
//                webServiceVO.setResponseCodeDescription("successFull");
//                webServiceVO.setRetrievalReferenceNumber("12345678822");
//                webServiceVO.setMicrobankTransactionCode("1222025");
//                webServiceVO.setConsumerName("Ahsan");
//                webServiceVO.setResponseContentXML("");
//
//            }
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] IBFT Advice Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setDestinationAccountNumber(webServiceVO.getDestinationAccountNumber());
            response.setDestinationBankImd(webServiceVO.getDestinationBankImd());
//            response.setTransactionId(webServiceVO.getTransactionId());

//            if (StringUtils.isNotEmpty(webServiceVO.getResponseContentXML())) {
//                XMLUtil.populateFromResponse(response, webServiceVO.getResponseContentXML(), CashInResponseEnum.values());
//                logModel.setResponseCode(webServiceVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//            }
        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] IBFT Advice Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] IBFT Advcie Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getDestinationBankImd())
                .append(response.getDestinationAccountNumber());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****IBFT Advice REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    //
//	/*
//	 * SAVES TRANSACTION REQUEST PDU INTO THE DATABASE
//	 */
    private TransactionLogModel saveTransaction(TransactionLogModel transaction) {
        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[HOST] Transaction saved with RRN: " + transaction.getRetrievalRefNo());
        return transaction;
    }

    private void buildSwtich() {
        try {
            if (switchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(WebServiceSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                switchController = (WebServiceSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }


    public DebitCardIssuanceInquiryResponse debitCardIssuanceInquiryResponse(DebitCardIssuanceInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Debit Card Issuance Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        DebitCardIssuanceInquiryResponse response = new DebitCardIssuanceInquiryResponse();


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

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardIssuanceInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Issuance Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.debitCardIssuanceInquiry(messageVO);
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
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Issuance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getMobileNumber())
                .append(response.getCnic())
                .append(response.getCharges());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Debit Card Issuance Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public DebitCardIssuanceResponse debitCardIssuanceResponse(DebitCardIssuanceRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Debit Card Issuance Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        DebitCardIssuanceResponse response = new DebitCardIssuanceResponse();


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
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardIssuance");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Issuance Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.debitCardIssuance(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Card Issuance Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCnic(messageVO.getCnicNo());
            response.setRrn(messageVO.getRetrievalReferenceNumber());


            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Card Issuance Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Card Issuance Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());

            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getCnic());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Debit Card Issuance REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public ChallanPaymentResponse challanPaymentResponse(ChallanPaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Challan Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        ChallanPaymentResponse response = new ChallanPaymentResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setReserved2(request.getReserved2());
        messageVO.setChallanNumber(request.getChallanNumber());
        messageVO.setProductCode(request.getProductCode());
        messageVO.setBillAmount(request.getChallanAmount());
        messageVO.setTotalAmount(request.getTotalAmount());
        messageVO.setCommissionAmount(request.getCommisionAmount());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("ChallanPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Challan Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.challanPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Challan Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setChallanNumber(messageVO.getChallanNumber());
            response.setChallanAmount(messageVO.getChallanAmount());
            response.setCommissionAmount(messageVO.getCommissionAmount());
            response.setTotalAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Challan Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Challan Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getTransactionId())
                .append(response.getMobileNumber())
                .append(response.getChallanNumber())
                .append(response.getChallanAmount())
                .append(response.getCommissionAmount())
                .append(response.getTotalAmount())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Challan PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public ChallanPaymentInquiryResponse challanPaymentInquiryResponse(ChallanPaymentInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Challan Payment Inquiry  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        ChallanPaymentInquiryResponse response = new ChallanPaymentInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChallanNumber(request.getChallanNumber());
        messageVO.setPinType(request.getPinType());
        messageVO.setProductCode(request.getProductCode());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("ChallanPaymentInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Challan Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.challanPaymentInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Challan Payment Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionType(messageVO.getTransactionType());
            response.setChallanNumber(messageVO.getChallanNumber());
            response.setMobileNumber(messageVO.getMobileNo());
            response.setStatus(messageVO.getStatus());
            response.setDueDate(messageVO.getDueDate());
            response.setChallanAmount(messageVO.getChallanAmount());
            response.setCommissionAmount(messageVO.getCharges());
            response.setTotalAmount(messageVO.getTotalAmount());
            response.setLateBillAmount(messageVO.getLateBillAmount());
            response.setReserved1("");
            response.setReserved2("");


            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Challan Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Challan Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());

            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getTransactionType())
                .append(response.getChallanNumber())
                .append(response.getMobileNumber())
                .append(response.getStatus())
                .append(response.getDueDate())
                .append(response.getChallanAmount())
                .append(response.getCommissionAmount())
                .append(response.getTotalAmount())
                .append(response.getLateBillAmount())
                .append(response.getReserved1())
                .append(response.getReserved2());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Challan PAYMENT Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public HRARegistrationInquiryResponse hraRegistrationInquiryResponse(HRARegistrationInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA Registration Inquiry Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRARegistrationInquiryResponse response = new HRARegistrationInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setPinType(request.getPinType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRARegistrationInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA Registration  Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraRegistrationInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA Registration Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setName(messageVO.getFirstName());
            response.setFatherName(messageVO.getLastName());
            response.setDob(messageVO.getDateOfBirth());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA Registration  Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA Registration  Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getName())
                .append(response.getFatherName())
                .append(response.getDob())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA Registration  Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public HRARegistrationResponse hraRegistrationResponse(HRARegistrationRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA Registration  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRARegistrationResponse response = new HRARegistrationResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setFirstName(request.getName());
        messageVO.setLastName(request.getFatherName());
        messageVO.setDateOfBirth(request.getDateOfBirth());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setSourceOfIncome(request.getSourceOfIncome());
        messageVO.setOccupation(request.getOccupation());
        messageVO.setPurposeOfAccount(request.getPurposeOfAccount());
        messageVO.setKINName(request.getKinName());
        messageVO.setKINMobileNumber(request.getKinMobileNumber());
        messageVO.setKINCNIC(request.getKinCnic());
        messageVO.setKINRelation(request.getKinRelation());
        messageVO.setInternationalRemittanceLocation(request.getInternationalRemittanceLocation());
        messageVO.setOriginatorLocation(request.getOriginatorRelation());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRARegistration");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA Registration  Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraRegistration(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA Registration Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA Registration Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA Registration  Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA Registration PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public WalletToCoreInquiryResponse walletToCoreInquiryResponse(WalletToCoreInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Wallet To Core Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCoreInquiryResponse response = new WalletToCoreInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setRecieverAccountNumber(request.getReceiverAccountNumber());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPinType(request.getPinType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("WalletToCoreInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Core Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.walletToCoreInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverAccountNumber(messageVO.getRecieverAccountNumber());
            response.setReceiverAccountTitle(messageVO.getRecieverAccountTilte());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet To Core Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }


    public WalletToCoreInquiryResponse fundWalletToCoreInquiryResponse(WalletToCoreInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Fund Wallet To Core Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCoreInquiryResponse response = new WalletToCoreInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setRecieverAccountNumber(request.getReceiverAccountNumber());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPinType(request.getPinType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("FundWalletToCoreInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Fund Wallet To Core Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.fundWalletToCoreInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverAccountNumber(messageVO.getRecieverAccountNumber());
            response.setReceiverAccountTitle(messageVO.getRecieverAccountTilte());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Wallet To Core Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet To Core Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public WalletToCoreResponse walletToCoreResponse(WalletToCoreRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Wallet To Core  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCoreResponse response = new WalletToCoreResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setSenderMobileNumber(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setRecieverAccountNumber(request.getReceiverAccountNumber());
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("WalletToCore");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Core Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.walletToCore(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Core Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverAccountNumber(messageVO.getRecieverAccountNumber());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet To Core PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public WalletToCoreResponse fundWalletToCoreResponse(WalletToCoreRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Fund  Wallet To Core  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCoreResponse response = new WalletToCoreResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setSenderMobileNumber(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setRecieverAccountNumber(request.getReceiverAccountNumber());
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("FundWalletToCore");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Core Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.fundWalletToCore(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Fund Wallet To Core Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverAccountNumber(messageVO.getRecieverAccountNumber());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Fund Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Fund Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Fund Wallet To Core PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }


    public WalletToCnicResponse walletToCnicResponse(WalletToCnicRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Wallet To Cnic Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCnicResponse response = new WalletToCnicResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setReceiverCNIC(request.getReceiverCnic());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
//        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("WalletToCnic");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Cnic Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.walletToCnic(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Cnic Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverCnic(messageVO.getReceiverCNIC());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Wallet To Cnic Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Wallet To Cnic Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getTransactionId())
                .append(response.getReceiverMobileNumber())
                .append(response.getReceiverCnic())
                .append(response.getTransactionAmount())
                .append(response.getComissionAmount())
                .append(response.getTotalTransactionAmount())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet To CNIC PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public WalletToCnicInquiryResponse walletToCnicInquiryResponse(WalletToCnicInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Wallet To CNIC Inquiry Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        WalletToCnicInquiryResponse response = new WalletToCnicInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        messageVO.setReceiverCNIC(request.getReceiverCnic());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
        messageVO.setPinType(request.getPinType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("WalletToCnicInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To CNIC Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.walletToCnicInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Wallet To Cnic Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
            response.setReceiverCnic(messageVO.getReceiverCNIC());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Wallet To Cnic Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Wallet To Cnic Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getTransactionId())
                .append(response.getReceiverMobileNumber())
                .append(response.getReceiverCnic())
                .append(response.getTransactionAmount())
                .append(response.getComissionAmount())
                .append(response.getTotalTransactionAmount())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Wallet To CNIC Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public HRAToWalletInquiryResponse hraToWalletInquiryResponse(HRAToWalletInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA To Wallet Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRAToWalletInquiryResponse response = new HRAToWalletInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPinType(request.getPinType());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRATOWalletInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA To Wallet Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraToWalletInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA To Wallet Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA To Wallet Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA To Wallet Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA To Wallet Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public HRAToWalletResponse hraToWalletResponse(HRAToWalletRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA To Wallet  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRAToWalletResponse response = new HRAToWalletResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setTerminalId(request.getTerminalId());
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
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRAToWallet");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA To Wallet Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraToWallet(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA TO Wallet Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            response.setReserved1("");
            response.setReserved2("");

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA ToWallet Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA TO Wallet Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime())
                .append(response.getReserved1())
                .append(response.getReserved2());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA TO Wallet PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public DebitInquiryResponse debitInquiryResponse(DebitInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Debit Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        DebitInquiryResponse response = new DebitInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        messageVO.setPinType(request.getPinType());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.debitInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTotalAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit Inquiry Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Debit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public DebitResponse debitResponse(DebitRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Debit Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        DebitResponse response = new DebitResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
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
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("Debit");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.debit(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//
//            if (e instanceof RemoteAccessException) {
//                if (!(e instanceof RemoteConnectFailureException)) {
//
//                    try {
//                        MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
//                        middlewareMessageVO.setAccountNo1(messageVO.getMobileNo());
//                        middlewareMessageVO.setAccountNo2(messageVO.getMobileNo());
//                        middlewareMessageVO.setStan(messageVO.getReserved2());
//                        middlewareMessageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//                        middlewareMessageVO.setRequestTime(getRequestTime(messageVO.getDateTime()));
//                        middlewareMessageVO.setDateTime(messageVO.getDateTime());
//                        middlewareMessageVO.setTransactionAmount(messageVO.getTransactionAmount());
//                        middlewareMessageVO.setProductId(Long.parseLong(messageVO.getProductID()));
//                        this.sentDebitPaymentRequest(middlewareMessageVO);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        messageVO.setResponseCode("550");
//                        messageVO.setResponseCodeDescription("Host Not In Reach");
//                    }
//
//                    messageVO.setResponseCode(" ");
//                    messageVO.setResponseCodeDescription("");
//                    messageVO.setRetrievalReferenceNumber("");
//                }
//            }
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String stackTrace = sw.toString();
//            if (stackTrace.contains("status code = 503")) {
//                messageVO.setResponseCode("550");
//                messageVO.setResponseCodeDescription("Host Not In Reach");
//            }
        }


        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Debit Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

//            if (messageVO.getProductID().equals("10245364")){
//                PaymentReversalRequest reversalRequest=new PaymentReversalRequest();
//                String requestTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                String stan = String.valueOf((100000 + new Random().nextInt(900000)));
//                reversalRequest.setTransactionCode(request.getRrn());
//                reversalRequest.setRrn(requestTime+stan);
//                reversalRequest.setDateTime(request.getDateTime());
//                reversalRequest.setUserName(request.getUserName());
//                reversalRequest.setPassword(request.getPassword());
//                reversalRequest.setChannelId(request.getChannelId());
//                this.paymentReversal(reversalRequest);
//            }
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Debit  Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Debit  Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Debit PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }


    public AgentBillPaymentInquiryResponse agentBillPaymentInquiry(AgentBillPaymentInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Bill Payment Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        AgentBillPaymentInquiryResponse response = new AgentBillPaymentInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        messageVO.setPinType(request.getPinType());
        messageVO.setAgentMobileNumber(request.getAgentMobileNo());
        messageVO.setConsumerNo(request.getConsumerNo());
        messageVO.setBillAmount(request.getAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentBillPaymentInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Bill Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentBillPaymentInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Bill Payment Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setProductName(messageVO.getProductName());
            response.setConsumerMobileNumber(messageVO.getConsumerMobileNo());
            response.setBillAmount(messageVO.getBillAmount());
            response.setLateBillAmount(messageVO.getLateBillAmount());
            response.setBillPaid(messageVO.getBillPaid());
            response.setDueDate(messageVO.getDueDate());
            response.setOverDue(messageVO.getOverDue());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Bill Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Bill Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getProductName())
                .append(response.getConsumerMobileNumber())
                .append(response.getBillAmount())
                .append(response.getLateBillAmount())
                .append(response.getBillPaid())
                .append(response.getDueDate())
                .append(response.getOverDue());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Bill Payment Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentBillPaymentResponse agentBillPaymentResponse(AgentBillPaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Bill Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        AgentBillPaymentResponse response = new AgentBillPaymentResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        if (request.getPinType().equals("02")) {
            messageVO.setOtpPin(request.getPin());
        } else {
            messageVO.setMobilePin(request.getPin());
        }

        messageVO.setAgentMobileNumber(request.getAgentMobileNo());
        messageVO.setConsumerNo(request.getConsumerNo());
        messageVO.setBillAmount(request.getAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("Debit");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Bill Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentBillPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Bill Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setBillAmount(messageVO.getBillAmount());
            response.setCommissionAmount(messageVO.getCommissionAmount());
            response.setConsumerNo(messageVO.getConsumerNo());
            response.setConsumerMobileNumber(messageVO.getConsumerMobileNo());
            response.setPaymentDateTime(messageVO.getDateTime());
            response.setLateBillAmount(messageVO.getLateBillAmount());
            response.setProductName(messageVO.getProductName());
            response.setAmount(messageVO.getTotalAmount());
            response.setTransactionId(messageVO.getTransactionId());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setRemainingBalance(messageVO.getRemainingBalance());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Bill Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Bill Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Bill PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CreditInquiryResponse creditInquiryResponse(CreditInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Credit Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CreditInquiryResponse response = new CreditInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        messageVO.setPinType(request.getPinType());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CreditInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Credit Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.creditInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Credit Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setInclusiveExclusiveComissionAmount(messageVO.getReserved3());
            response.setTotalAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Credit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CreditResponse creditResponse(CreditRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Credit Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CreditResponse response = new CreditResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
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
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();

        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("Credit");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);
        try {
            logger.info("[HOST] Sent Credit Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.credit(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

//below code comment disscuss with zulfiqar sir
//            if (e instanceof RemoteAccessException) {
//                if (!(e instanceof RemoteConnectFailureException)) {
//                    messageVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                }
//            }
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String stackTrace = sw.toString();
//            if (stackTrace.contains("status code = 503")) {
//                MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
//                middlewareMessageVO.setAccountNo1(messageVO.getMobileNo());
//                middlewareMessageVO.setAccountNo2(messageVO.getMobileNo());
//                middlewareMessageVO.setStan(messageVO.getReserved2());
//                middlewareMessageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//                middlewareMessageVO.setRequestTime(getRequestTime(messageVO.getDateTime()));
//                middlewareMessageVO.setTransactionAmount(messageVO.getTransactionAmount());
//                middlewareMessageVO.setProductId(Long.parseLong(messageVO.getProductID()));
//
//                try {
//                    this.sentWalletRequest(middlewareMessageVO);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    messageVO.setResponseCode("550");
//                    messageVO.setResponseCodeDescription("Host Not In Reach");
//                }
//            } else {
//                messageVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                messageVO.setResponseCodeDescription("Successful");
//
//            }

        }


        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().

                equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Credit Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Credit  Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Credit Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }

        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Credit PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);

        updateTransactionInDB(logModel);
        return response;
    }

    public HRACashWithdrawalInquiryResponse hraCashWithdrawalInquiryResponse(HRACashWithdrawalInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA Cash Withdrawal Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRACashWithdrawalInquiryResponse response = new HRACashWithdrawalInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
        messageVO.setCnicNo(request.getCNIC());
        messageVO.setPinType(request.getPinType());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRACashWithdrawalInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA Cash Withdrawal Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraCashWithDrawlInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA Cash Withdrawal Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
            response.setCustomerName(messageVO.getConsumerName());
            response.setCustomerMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA Cash Withdrawal Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA Cash Withdrawal Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA Cash Withdrawal Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public HRACashWithdrawalResponse hraCashWithdrawalResponse(HRACashWithdrawalRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing HRA Cash Withdrawal Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        HRACashWithdrawalResponse response = new HRACashWithdrawalResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
        messageVO.setCnicNo(request.getCnic());
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
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("HRACashWithdrawal");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent HRA Cash Withdrawal Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.hraCashWithDrawl(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] HRA Cash Withdrawal Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
            response.setCustomerName(messageVO.getConsumerName());
            response.setCustomerMobileNumber(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setTransactionId(messageVO.getTransactionId());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalTransactionAmount(messageVO.getTotalAmount());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] HRA Cash Withdrawal Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] HRA Cash Withdrawal Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****HRA Cash Withdrawal PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public LoginAuthenticationResponse loginAuthentication(LoginAuthenticationRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Login Authentication Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        LoginAuthenticationResponse response = new LoginAuthenticationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getPin());

        try {
            String text;
            String pin;
            text = request.getPin();
            pin = text.replaceAll("\\r|\\n", "");
            messageVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoginAuthenticationRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Login Authentication Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountAuthentication(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Login Authentication Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setIban(messageVO.getBenificieryIban());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountLevel(messageVO.getAccountType());
            response.setBalance(messageVO.getBalance());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setDailyCreditLimit(messageVO.getDailyCreditLimit());
            response.setDailyDebitLimit(messageVO.getDailyDebitLimit());
            response.setSegment(messageVO.getReserved2());
//            response.setBvs(messageVO.getIsBVSAccount());
//            response.setBlinlBvs(messageVO.getReserved3());
            response.setMonthlyCreditLimit(messageVO.getMonthlyCreditLimit());
            response.setMonthlyDebitLimit(messageVO.getMonthlyDebitLimit());
            response.setYearlyCreditLimit(messageVO.getYearlyCreditLimit());
            response.setYearlyDebitLimit(messageVO.getYearlyDebitLimit());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Login Authentication Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Login Authentication Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Login Authentication REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }


    public ZindigiLoginAuthenticationResponse zindigiLoginAuthenticationResponse(ZindigiLoginAuthenticationRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Zindigi Login Authentication Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        ZindigiLoginAuthenticationResponse response = new ZindigiLoginAuthenticationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getPin());

        try {
            String text;
            String pin;
            text = request.getPin();
            pin = text.replaceAll("\\r|\\n", "");
            messageVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setCnicNo(request.getCnic());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoginAuthenticationRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Login Authentication Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountAuthentication(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Login Authentication Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setIban(messageVO.getBenificieryIban());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountLevel(messageVO.getAccountType());
            response.setBalance(messageVO.getBalance());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setDailyCreditLimit(messageVO.getDailyCreditLimit());
            response.setDailyDebitLimit(messageVO.getDailyDebitLimit());
            response.setSegment(messageVO.getReserved2());
            response.setBvs(messageVO.getIsBVSAccount());
            response.setBlinlBvs(messageVO.getReserved3());
            response.setMonthlyCreditLimit(messageVO.getMonthlyCreditLimit());
            response.setMonthlyDebitLimit(messageVO.getMonthlyDebitLimit());
            response.setYearlyCreditLimit(messageVO.getYearlyCreditLimit());
            response.setYearlyDebitLimit(messageVO.getYearlyDebitLimit());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Login Authentication Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Login Authentication Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Login Authentication REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public LoginPinResponse loginPin(LoginPinRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Login Pin Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        LoginPinResponse response = new LoginPinResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setLoginPin(request.getPin());

        try {
            String text;
            String pin;
            text = request.getPin();
            pin = text.replaceAll("\\r|\\n", "");
            messageVO.setLoginPin(RSAEncryption.decrypt(pin, loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setReserved2("");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoginPinRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Login PIN Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.loginPin(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Login PIN Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Login PIN Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Login PIN Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Login PIN REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public LoginPinChangeResponse loginPinChange(LoginPinChangeRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Login Pin Change Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        LoginPinChangeResponse response = new LoginPinChangeResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setOldLoginPin(request.getOldLoginPin());
        try {
            String text;
            String oldPin;
            text = request.getOldLoginPin();
            oldPin = text.replaceAll("\\r|\\n", "");
            messageVO.setOldLoginPin(RSAEncryption.decrypt(request.getOldLoginPin(), loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setNewLoginPin(request.getNewLoginPin());
        try {
            String text;
            String newPin;
            text = request.getNewLoginPin();
            newPin = text.replaceAll("\\r|\\n", "");
            messageVO.setNewLoginPin(RSAEncryption.decrypt(newPin, loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setConfirmLoginPin(request.getConfirmLoginPin());
        try {
            String text;
            String confirmPin;
            text = request.getConfirmLoginPin();
            confirmPin = text.replaceAll("\\r|\\n", "");
            messageVO.setConfirmLoginPin(RSAEncryption.decrypt(confirmPin, loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoginPinChangeRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Login PIN Change Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.loginPinChange(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Login PIN Change Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Login PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Login PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Login PIN Change REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public ResetPinResponse resetPin(ResetPinRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Reset PIN Change Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        ResetPinResponse response = new ResetPinResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());

        try {
            String text;
            String newLoginPin;
            text = request.getNewLoginPin();
            newLoginPin = text.replaceAll("\\r|\\n", "");


            String newMpin = (RSAEncryption.decrypt(newLoginPin, loginPrivateKey));
            messageVO.setNewLoginPin(newMpin);
            logger.info("[HOST] Reset New Login Pin " + newMpin);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            String text;
            String confirmLoginPin;
            text = request.getConfirmLoginPin();
            confirmLoginPin = text.replaceAll("\\r|\\n", "");
            String confirmPin = (RSAEncryption.decrypt(confirmLoginPin, loginPrivateKey));
            logger.info("[HOST] Reset confirm Login Pin " + confirmPin);


            messageVO.setConfirmLoginPin(confirmPin);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setCnicNo(request.getCnic());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("ResetPinRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Reset PIN Change Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.resetPin(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Reset PIN Change Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Reset PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Reset PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Reset PIN Change REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AdvanceLoanSalaryResponse advanceLoanSalary(AdvanceLoanSalaryRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Advance Loan Salary Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AdvanceLoanSalaryResponse response = new AdvanceLoanSalaryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setLoanAmount(request.getLoanAmount());
        messageVO.setNumberOfInstallments(request.getNumberOfInstallments());
        messageVO.setInstallmentAmount(request.getInstallmentAmount());
        messageVO.setProductID(request.getProductId());
        messageVO.setGracePeriod(request.getGracePeriod());
        messageVO.setEarlyPaymentCharges(request.getEarlyPaymentCharges());
        messageVO.setLatePaymentCharges(request.getLatePaymentCharges());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AdvanceLoanSalary");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Advance Loan Salary Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.advanceLoanSalary(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Cash Out Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Advance Loan Salary Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Advance Loan Salary Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Advance Loan Salary REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public SmsGenerationResponse smsGeneration(SmsGenerationRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] SMS Generation Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        SmsGenerationResponse response = new SmsGenerationResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setMessage(request.getMessage());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("SMSGeneration");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent SMS Generation Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.smsGeneration(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] SMS Generation Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] SMS Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] SMS Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****SMS Generation REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentAccountLoginResponse agentAccountLogin(AgentAccountLoginRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Account Login Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AgentAccountLoginResponse response = new AgentAccountLoginResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setAgentId(request.getAgentId());
        try {

            messageVO.setLoginPin(RSAEncryption.decrypt(request.getPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setLoginPin(request.getPin());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentAccountLoginRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Account Login Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentAccountLogin(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Account Login Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAgentNetwork(messageVO.getAgentNetwork());
            response.setBalance(messageVO.getBalance());
            response.setDailyDebitLimit(messageVO.getDailyDebitLimit());
            response.setMonthlyDebitLimit(messageVO.getMonthlyDebitLimit());
            response.setDailyCreditLimit(messageVO.getDailyCreditLimit());
            response.setMonthlyCreditLimit(messageVO.getMonthlyCreditLimit());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Account Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Account Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Agent Account Login REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentLoginPinGenerationResponse agentLoginPinGeneration(AgentLoginPinGenerationRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Login PIN Generation Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AgentLoginPinGenerationResponse response = new AgentLoginPinGenerationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setAgentId(request.getAgentId());
        try {
            messageVO.setLoginPin(RSAEncryption.decrypt(request.getPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setLoginPin(request.getPin());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentLoginPinGenerationRequest");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Login PIN Generation Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentLoginPinGeneration(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Login PIN Generation Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Account Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Account Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Agent Account Login REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentLoginPinResetResponse agentLoginPinReset(AgentLoginPinResetRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Login Reset PIN Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AgentLoginPinResetResponse response = new AgentLoginPinResetResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());

        try {
            messageVO.setOldLoginPin(RSAEncryption.decrypt(request.getOldLoginPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setOldLoginPin(request.getOldLoginPin());
        try {
            messageVO.setNewLoginPin(RSAEncryption.decrypt(request.getNewLoginPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setNewLoginPin(request.getNewLoginPin());
        try {
            messageVO.setConfirmLoginPin(RSAEncryption.decrypt(request.getConfirmLoginPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        messageVO.setConfirmLoginPin(request.getConfirmLoginPin());
        messageVO.setAgentId(request.getAgentId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentLoginPinReset");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Login PIN Change Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentLoginPinReset(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Login PIN Change Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Login PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Login PIN Change Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Agent Login PIN Change REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentMpinGenerationResponse agentMpinGeneration(AgentMpinGenerationRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent MPIN Generation Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AgentMpinGenerationResponse response = new AgentMpinGenerationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setAgentId(request.getAgentId());
//        try {
//            messageVO.setMobilePin(RSAEncryption.decrypt(request.getMpin(),privateKey));
//        } catch (BadPaddingException | IllegalBlockSizeException |InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            messageVO.setConfirmMpin(RSAEncryption.decrypt(request.getConfirmMPIN(),privateKey));
//        } catch (BadPaddingException | IllegalBlockSizeException |InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        messageVO.setMobilePin(EncryptionUtil.encrypt(request.getMpin()));
        messageVO.setConfirmMpin(EncryptionUtil.encrypt(request.getConfirmMPIN()));
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentMpinGeneration");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent MPIN Generation Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentMpinGeneration(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent MPIN Generation Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent MPIN Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent MPIN Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Agent MPIN Generation REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentMpinResetResponse agentMpinReset(AgentMpinResetRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent MPIN Reset Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AgentMpinResetResponse response = new AgentMpinResetResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setOldMpin(EncryptionUtil.encrypt(request.getOldMpin()));
        messageVO.setMobilePin(EncryptionUtil.encrypt(request.getNewMpin()));
        messageVO.setConfirmMpin(EncryptionUtil.encrypt(request.getConfirmMpin()));
//        try {
//            messageVO.setOldMpin(RSAEncryption.decrypt(request.getOldMpin(), privateKey));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            messageVO.setNewMpin(RSAEncryption.decrypt(request.getNewMpin(), privateKey));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            messageVO.setConfirmMpin(RSAEncryption.decrypt(request.getConfirmMpin(), privateKey));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        messageVO.setAgentId(request.getAgentId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentMpinReset");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent MPIN Reset Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentMpinReset(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent MPIN Reset Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent MPIN Reset Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent MPIN Reset Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Agent MPIN Reset REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }
//
//    public AgentMpinVerificationResponse agentMpinVerification(AgentMpinVerificationRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent MPIN Verification Request RRN: " + messageVO.getRetrievalReferenceNumber());
//        transactionKey = request.getChannelId() + request.getRrn();
//        AgentMpinVerificationResponse response = new AgentMpinVerificationResponse();
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setAgentMpin(request.getMpin());
//        messageVO.setConfirmMpin(request.getConfirmMPIN());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentMpinVerification");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent MPIN Verification Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentMpinVerification(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent MPIN Verification Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent MPIN Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent MPIN Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] **** Agent MPIN Verification REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

    public AgentBalanceInquiryResponse agentBalanceInquiry(AgentBalanceInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Agent Balance Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentBalanceInquiryResponse response = new AgentBalanceInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getPin());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setAgentMpin(request.getPin());
        try {
            messageVO.setAgentMpin(RSAEncryption.decrypt(request.getPin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            if (request.getPinType().equals("02")) {
                messageVO.setOtpPin(RSAEncryption.decrypt(request.getPin(), privateKey));
            } else {
                messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(), privateKey));
            }
//            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setAgentId(request.getAgentId());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentBalanceInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Balance Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentBalanceInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Balance Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setBalance(messageVO.getBalance());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription() + response.getBalance());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent BALANCE INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

//    public AgentToAgentInquiryResponse agentToAgentInquiry(AgentToAgentInquiryRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent To Agent Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentToAgentInquiryResponse response = new AgentToAgentInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobilePin(request.getMpin());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setAgentMpin(request.getMpin());
//        messageVO.setAgentMobileNumber(request.getReceiverAgentMobileNumber());
//        messageVO.setTransactionAmount(request.getTransactionAmount());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentToAgentInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent To Agent Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentToAgentInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent To Agent Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setReceiverAgentMobileNumber(messageVO.getAgentMobileNumber());
//            response.setCnic(messageVO.getCnicNo());
//            response.setReceiversName(messageVO.getConsumerName());
//            response.setCharges(messageVO.getCharges());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent To Agent Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent To Agent Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent To Agent INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentToAgentPaymentResponse agentToAgentPayment(AgentToAgentPaymentRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent To Agent Payment Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentToAgentPaymentResponse response = new AgentToAgentPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setAgentMpin(request.getMpin());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setTransactionAmount(request.getTransactionAmount());
//        messageVO.setCharges(request.getCharges());
//        messageVO.setTransactionType(request.getTransactionType());
//        messageVO.setPaymentType(request.getPaymentType());
//        messageVO.setTransactionId(request.getTransactionCode());
//        messageVO.setSettlementType(request.getSettlementType());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentToAgentPayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent To Agent Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentToAgentPayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent To Agent Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setMobileNumber(messageVO.getAgentMobileNumber());
//            response.setCnic(messageVO.getCnicNo());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setTransactionCode(messageVO.getTransactionId());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent To Agent Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent To Agent Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent To Agent Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentAccountOpeningResponse agentAccountOpening(AgentAccountOpeningRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent Account Opening Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentAccountOpeningResponse response = new AgentAccountOpeningResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setIsHRA(request.getIsHRA());
//        messageVO.setCnicStatus(request.getCnicStatus());
//        messageVO.setCnicExpiry(request.getCnicExpiry());
//        messageVO.setBirthPlace(request.getBirthPlace());
//        messageVO.setConsumerName(request.getConsumerName());
//        messageVO.setMotherMaiden(request.getMotherMaiden());
//        messageVO.setDateOfBirth(request.getDob());
//        messageVO.setPresentAddress(request.getPresentAddress());
//        messageVO.setPresentCity(request.getPresentCity());
//        messageVO.setPermanentAddress(request.getPermanentAddress());
//        messageVO.setPermanentCity(request.getPermanentCity());
//        messageVO.setAccountTitle(request.getAccountTitle());
//        messageVO.setGender(request.getGender());
//        messageVO.setFatherHusbandName(request.getFatherHusbandName());
//        messageVO.setIsCnicSeen(request.getIsCnicSeen());
//        messageVO.setDepositAmount(request.getDepositAmount());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setIsBVSAccount(request.getIsBVSAccount());
//        messageVO.setSegmentCode(request.getSegmentId());
//        messageVO.setAccountType(request.getAccountType());
//        messageVO.setTransactionId(request.getTransactionId());
//        messageVO.setCustomerMobileNetwork(request.getMobileNetwork());
//        messageVO.setIsHRA(request.getIsHRA());
//        messageVO.setKINMobileNumber(request.getNokMobileNumber());
//        messageVO.setTransactionPurpose(request.getTransactionPurpose());
//        messageVO.setOccupation(request.getOccupation());
//        messageVO.setOrgLocation1(request.getOrgLocation1());
//        messageVO.setOrgLocation2(request.getOrgLocation2());
//        messageVO.setOrgLocation3(request.getOrgLocation3());
//        messageVO.setOrgLocation4(request.getOrgLocation4());
//        messageVO.setOrgLocation5(request.getOrgLocation5());
//        messageVO.setOrgRelation1(request.getOrgRelation1());
//        messageVO.setOrgRelation2(request.getOrgRelation2());
//        messageVO.setOrgRelation3(request.getOrgRelation3());
//        messageVO.setOrgRelation4(request.getOrgRelation4());
//        messageVO.setOrgRelation5(request.getOrgRelation5());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentAccountOpening");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentAccountOpening(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Account Opening Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Account Opening REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentUpgradeAccountInquiryResponse agentUpgradeAccountInquiry(AgentUpgradeAccountInquiryRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent Upgrade Account Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentUpgradeAccountInquiryResponse response = new AgentUpgradeAccountInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setCnicNo(request.getCustomerCnic());
//        messageVO.setIsReceiveCash(request.getIsReceiveCash());
//        messageVO.setIsHRA(request.getIsHRA());
//        messageVO.setIsUpgrade(request.getIsUpgrade());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setSegmentCode(request.getSegmentId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentUpgradeAccountInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Upgrade Account Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentUpgradeAccountInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Upgrade Account Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setCustomerCnic(messageVO.getCnicNo());
//            response.setCnicExpiry(messageVO.getCnicExpiry());
//            response.setCustomerMobileNumber(messageVO.getMobileNo());
//            response.setCustomerName(messageVO.getConsumerName());
//            response.setCustomerRegState(messageVO.getCustomerRegState());
//            response.setCustomerRegStateId(messageVO.getCustomerRegStateId());
//            response.setTransactionCode(messageVO.getTransactionId());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Upgrade Account Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Upgrade Account Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentUpgradeAccountResponse agentUpgradeAccount(AgentUpgradeAccountRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent Upgrade Account Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentUpgradeAccountResponse response = new AgentUpgradeAccountResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setIsHRA(request.getIsHRA());
//        messageVO.setCnicStatus(request.getCnicStatus());
//        messageVO.setCnicExpiry(request.getCnicExpiry());
//        messageVO.setBirthPlace(request.getBirthPlace());
//        messageVO.setConsumerName(request.getConsumerName());
//        messageVO.setMotherMaiden(request.getMotherMaiden());
//        messageVO.setDateOfBirth(request.getDob());
//        messageVO.setPresentAddress(request.getPresentAddress());
//        messageVO.setPresentCity(request.getPresentCity());
//        messageVO.setPermanentAddress(request.getPermanentAddress());
//        messageVO.setPermanentCity(request.getPermanentCity());
//        messageVO.setAccountTitle(request.getAccountTitle());
//        messageVO.setGender(request.getGender());
//        messageVO.setFatherHusbandName(request.getFatherHusbandName());
//        messageVO.setIsCnicSeen(request.getIsCnicSeen());
//        messageVO.setDepositAmount(request.getDepositAmount());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setIsBVSAccount(request.getIsBVSAccount());
//        messageVO.setSegmentCode(request.getSegmentId());
//        messageVO.setAccountType(request.getAccountType());
//        messageVO.setTransactionId(request.getTransactionId());
//        messageVO.setCustomerMobileNetwork(request.getMobileNetwork());
//        messageVO.setIsHRA(request.getIsHRA());
//        messageVO.setKINMobileNumber(request.getNokMobileNumber());
//        messageVO.setTransactionPurpose(request.getTransactionPurpose());
//        messageVO.setOccupation(request.getOccupation());
//        messageVO.setOrgLocation1(request.getOrgLocation1());
//        messageVO.setOrgLocation2(request.getOrgLocation2());
//        messageVO.setOrgLocation3(request.getOrgLocation3());
//        messageVO.setOrgLocation4(request.getOrgLocation4());
//        messageVO.setOrgLocation5(request.getOrgLocation5());
//        messageVO.setOrgRelation1(request.getOrgRelation1());
//        messageVO.setOrgRelation2(request.getOrgRelation2());
//        messageVO.setOrgRelation3(request.getOrgRelation3());
//        messageVO.setOrgRelation4(request.getOrgRelation4());
//        messageVO.setOrgRelation5(request.getOrgRelation5());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentUpgradeAccount");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Upgrade Account Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentUpgradeAccount(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Upgrade Account Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Upgrade Account Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Upgrade Account Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Upgrade Account REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCashInInquiryResponse agentCashInInquiry(AgentCashInInquiryRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cash In Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCashInInquiryResponse response = new AgentCashInInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCashInInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cash In Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCashInInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Cash In Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setToAccount(messageVO.getToAccount());
//            response.setFromAccount(messageVO.getFromAccount());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cash In Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCashInResponse agentCashIn(AgentCashInRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cash In Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCashInResponse response = new AgentCashInResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setBranchlessAccountId(request.getBranchlessAccountId());
//        messageVO.setCoreAccountId(request.getCoreAccountId());
//        messageVO.setBankId(request.getBankId());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCashIn");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cash In Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCashIn(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Cash In Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
//            response.setBranchlessAccountId(messageVO.getBranchlessAccountId());
//            response.setCoreAccountId(messageVO.getCoreAccountId());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setBalance(messageVO.getBalance());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cash In Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cash In Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cash In Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCashOutInquiryResponse agentCashOutInquiry(AgentCashOutInquiryRequest request) {
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cash Out Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCashOutInquiryResponse response = new AgentCashOutInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setBankId(request.getBankId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCashOutInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cash Out Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCashOutInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Cash Out Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setCoreAccountId(messageVO.getCoreAccountId());
//            response.setCoreAccountTitle(messageVO.getAccountTitle());
//            response.setBranchlessAccountId(messageVO.getBranchlessAccountId());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cash Out Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cash Out Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cash Out Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCashOutResponse agentCashOut(AgentCashOutRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cash Out Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCashOutResponse response = new AgentCashOutResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setBranchlessAccountId(request.getBranchlessAccountId());
//        messageVO.setCoreAccountId(request.getCoreAccountId());
//        messageVO.setAccountTitle(request.getCoreAccountTitle());
//        messageVO.setBankId(request.getBankId());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCashOut");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cash Out Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCashOut(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Cash Out Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
//            response.setBranchlessAccountId(messageVO.getBranchlessAccountId());
//            response.setCoreAccountId(messageVO.getCoreAccountId());
//            response.setCoreAccountTitle(messageVO.getAccountTitle());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setBalance(messageVO.getBalance());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cash Out Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cash Out Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cash Out Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentWalletToWalletInquiryResponse agentWalletToWalletInquiry(AgentWalletToWalletInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Wallet Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToWalletInquiryResponse response = new AgentWalletToWalletInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToWalletInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Wallet Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToWalletInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Wallet Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setTransactionDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setCustomerMobile(messageVO.getMobileNo());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setCommissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Wallet Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Wallet Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Wallet Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentWalletToWalletPaymentResponse agentWalletToWalletPayment(AgentWalletToWalletPaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Wallet Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToWalletPaymentResponse response = new AgentWalletToWalletPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setMobilePin(request.getMpin());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setOtpPin(request.getOtp());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getCommissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToWalletPayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Wallet Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToWalletPayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Wallet Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setTransactionDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setCustomerMobile(messageVO.getMobileNo());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setCommissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Wallet Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Wallet Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Wallet Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentWalletToCnicInquiryResponse agentWalletToCnicInquiry(AgentWalletToCnicInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Cnic Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToCnicInquiryResponse response = new AgentWalletToCnicInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
//        messageVO.setPinType(request.getPinType());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToCnicInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Cnic Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToCnicInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Cnic Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setCustomerMobile(messageVO.getConsumerMobileNo());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setCommissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Cnic Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Cnic Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Cnic Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentWalletToCnicPaymentResponse agentWalletToCnicPayment(AgentWalletToCnicPaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Cnic Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToCnicPaymentResponse response = new AgentWalletToCnicPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getCommissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToCnicPayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Cnic Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToCnicPayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Cnic Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setTransactionDateTime(messageVO.getDateTime());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setCustomerMobile(messageVO.getConsumerMobileNo());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setCommissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Cnic Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Cnic Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Cnic Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

    public AgentIbftInquiryResponse agentIbftInquiry(AgentIbftInquiryRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Ibft Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentIbftInquiryResponse response = new AgentIbftInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        messageVO.setAgentId(request.getAgentId());
        messageVO.setProductID(request.getProductId());
        messageVO.setCoreAccountId(request.getCoreAccountId());
        messageVO.setBankId(request.getBankIMD());
        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
        messageVO.setBankName(request.getBeneficiaryBankName());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentIbftInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Ibft Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentIbftInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Ibft Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCoreAccountId(messageVO.getCoreAccountId());
            response.setCoreAccountTitle(messageVO.getAccountTitle());
            response.setBankAccountId(messageVO.getBranchlessAccountId());
            response.setBeneficiaryBankName(messageVO.getBankName());
            response.setBeneficiaryBranchName(messageVO.getBranchName());
            response.setBeneficiaryIban(messageVO.getBenificieryIban());
            response.setAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Ibft Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Ibft Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Ibft Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }


    public AgentIbftPaymentResponse agentIbftPayment(AgentIbftPaymentRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Ibft Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentIbftPaymentResponse response = new AgentIbftPaymentResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        try {
            messageVO.setMobilePin(RSAEncryption.decrypt(request.getMpin(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            messageVO.setOtpPin(RSAEncryption.decrypt(request.getOtp(), privateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageVO.setAgentId(request.getAgentId());
        messageVO.setProductID(request.getProductId());
        messageVO.setCoreAccountId(request.getCoreAccountId());
        messageVO.setAccountTitle(request.getCoreAccountTitle());
        messageVO.setBankId(request.getBankIMD());
        messageVO.setPurposeOfPayment(request.getPaymentPurpose());
        messageVO.setBankName(request.getBeneficiaryBankName());
        messageVO.setBranchName(request.getBeneficiaryiciaryBranchName());
        messageVO.setBenificieryIban(request.getBeneficiaryIban());
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setTotalAmount(request.getTotalAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentIbftPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Ibft Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentIbftPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Ibft Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
            response.setBeneficiaryBankName(messageVO.getBankName());
            response.setBeneficiaryiciaryBranchName(messageVO.getBranchName());
            response.setBeneficiaryIban(messageVO.getBenificieryIban());
            response.setCoreAccountTitle(messageVO.getAccountTitle());
            response.setCoreAccountNumber(messageVO.getCoreAccountId());
            response.setTransactionId(messageVO.getTransactionId());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());


            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Ibft Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Ibft Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Ibft Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

//    public AgentRetailPaymentInquiryResponse agentRetailPaymentInquiry(AgentRetailPaymentInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Retail Payment Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentRetailPaymentInquiryResponse response = new AgentRetailPaymentInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentRetailPaymentInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Retail Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentRetailPaymentInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Retail Payment Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setMobileNumber(messageVO.getMobileNo());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Retail Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Retail Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Retail Payment Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentRetailPaymentResponse agentRetailPayment(AgentRetailPaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Retail Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentRetailPaymentResponse response = new AgentRetailPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentRetailPayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Retail Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentRetailPayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Retail Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setMobileNumber(messageVO.getMobileNo());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Retail Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Retail Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Retail Payment Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//
//    public AgentWalletToCoreInquiryResponse agentWalletToCoreInquiry(AgentWalletToCoreInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Core Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToCoreInquiryResponse response = new AgentWalletToCoreInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setCoreAccountId(request.getCoreAccountId());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToCoreInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Core Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToCoreInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Core Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setCoreAccountTitle(messageVO.getAccountTitle());
//            response.setCoreAccountId(messageVO.getCoreAccountId());
//            response.setReceiverMobileNumber(messageVO.getMobileNo());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalTransactionAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Core Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Core Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Core Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentWalletToCorePaymentResponse agentWalletToCorePayment(AgentWalletToCorePaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Wallet To Core Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentWalletToCorePaymentResponse response = new AgentWalletToCorePaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setRecieverAccountTilte(request.getReceiverCustomerName());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setRecieverAccountNumber(request.getReceiverAccountNumber());
//        messageVO.setTransactionAmount(request.getTransactionAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalTransactionAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentWalletToCorePayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Wallet To Core Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentWalletToCorePayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Wallet To Core Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverAccountTitle(messageVO.getRecieverAccountTilte());
//            response.setReceiverAccountNumber(messageVO.getRecieverAccountNumber());
//            response.setTransactionAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalTransactionAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Wallet To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Wallet To Core Payment Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentReceiveMoneyInquiryResponse agentReceiveMoneyInquiry(AgentReceiveMoneyInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Receive Money Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentReceiveMoneyInquiryResponse response = new AgentReceiveMoneyInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionId(request.getTransactionId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentReceiveMoneyInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Receive Money Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentReceiveMoneyInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Receive Money Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setSenderAccountTitle(messageVO.getSenderAccountTitle());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setReceiverAccountTitle(messageVO.getRecieverAccountTilte());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setProductId(messageVO.getProductID());
//            response.setPaymentType(messageVO.getPaymentType());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Receive Money Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Receive Money Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Receive Money Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentReceiveMoneyPaymentResponse agentReceiveMoneyPayment(AgentReceiveMoneyPaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Receive Money Payment Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentReceiveMoneyPaymentResponse response = new AgentReceiveMoneyPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setTransactionId(request.getTransactionId());
//        messageVO.setMobilePin(request.getPin());
//        messageVO.setOtpPin(request.getOtp());
//        messageVO.setSenderMobileNumber(request.getSenderMobileNumber());
//        messageVO.setCnicNo(request.getSenderCnic());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getCommissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentReceiveMoneyInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Receive Money Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentReceiveMoneyPayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Receive Money Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setBalance(messageVO.getBalance());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Receive Money Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Receive Money Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Receive Money Payment Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

//    public AgentCnicToCnicInquiryResponse agentCnicToCnicInquiry(AgentCnicToCnicInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cnic To Cnic Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCnicToCnicInquiryResponse response = new AgentCnicToCnicInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setSenderMobileNumber(request.getSenderMobileNumber());
//        messageVO.setCnicNo(request.getSenderCnic());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCnicToCnicInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cnic To Cnic Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCnicToCnicInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Cnic To Cnic Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setSenderCity(messageVO.getSenderCity());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cnic To Cnic Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cnic To Cnic Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cnic To Cnic Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCnicToCnicPaymentResponse agentCnicToCnicPayment(AgentCnicToCnicPaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cnic To Cnic Payment Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCnicToCnicPaymentResponse response = new AgentCnicToCnicPaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setBvsRequest(request.getBvsRequest());
//        try {
//            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
////        messageVO.setMobilePin(request.getPin());
//        messageVO.setSenderCity(request.getSenderCity());
//        messageVO.setSenderMobileNumber(request.getSenderMobileNumber());
//        messageVO.setCnicNo(request.getSenderCnic());
//        messageVO.setReceiverCity(request.getReceiverCity());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionPurpose(request.getTransactionPurposeCode());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCnicToCnicPayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cnic To Cnic Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCnicToCnicPayment( messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Cnic To Cnic Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setBalance(messageVO.getBalance());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setReceiverAccountTitle(messageVO.getRecieverAccountTilte());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cnic To Cnic Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cnic To Cnic Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cnic To Cnic Payment Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

//    public AgentHRARegistrationInquiryResponse agentHRARegistrationInquiry(AgentHRARegistrationInquiryRequest request) {
//        long startTime = new Date().getTime(); // start time
//        String transactionKey = request.getDateTime() + request.getRrn();
//        WebServiceVO messageVO = new WebServiceVO();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing Agent HRA Registration Inquiry Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentHRARegistrationInquiryResponse response = new AgentHRARegistrationInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setCnicNo(request.getCnic());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        // messageVO.setReserved1("1");
//
////        messageVO.setReserved1(request.getReserved1());
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentHRARegistrationInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent HRA Registration  Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentHRARegistrationInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent HRA Registration Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setCnic(messageVO.getCnicNo());
//            response.setMobileNumber(messageVO.getMobileNo());
//            response.setName(messageVO.getFirstName());
//            response.setFatherName(messageVO.getLastName());
//            response.setDob(messageVO.getDateOfBirth());
//            response.setReserved1("");
//            response.setReserved2("");
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent HRA Registration  Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent HRA Registration  Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuilder stringText = new StringBuilder()
//                .append(response.getRrn())
//                .append(response.getResponseCode())
//                .append(response.getResponseDescription())
//                .append(response.getResponseDateTime())
//                .append(response.getCnic())
//                .append(response.getMobileNumber())
//                .append(response.getName())
//                .append(response.getFatherName())
//                .append(response.getDob())
//                .append(response.getReserved1())
//                .append(response.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent HRA Registration  Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentHRARegistrationResponse agentHRARegistration(AgentHRARegistrationRequest request) {
//        long startTime = new Date().getTime(); // start time
//        String transactionKey = request.getDateTime() + request.getRrn();
//        WebServiceVO messageVO = new WebServiceVO();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Starting Processing HRA Registration  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentHRARegistrationResponse response = new AgentHRARegistrationResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setMobileNo(request.getMobileNumber());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        try {
//            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (request.getPinType().equals("02")) {
//                messageVO.setOtpPin(RSAEncryption.decrypt(request.getPin(),privateKey));
//            } else {
//                messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
//            }
////            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setReserved1(request.getPinType());
//        messageVO.setFirstName(request.getName());
//        messageVO.setLastName(request.getFatherName());
//        messageVO.setDateOfBirth(request.getDateOfBirth());
//        messageVO.setCnicNo(request.getCnic());
//        messageVO.setSourceOfIncome(request.getSourceOfIncome());
//        messageVO.setOccupation(request.getOccupation());
//        messageVO.setCustomerMobileNetwork(request.getCustomerMobileNetwork());
//        messageVO.setPurposeOfAccount(request.getPurposeOfAccount());
//        messageVO.setKINName(request.getKinName());
//        messageVO.setKINMobileNumber(request.getKinMobileNumber());
//        messageVO.setKINCNIC(request.getKinCnic());
//        messageVO.setKINRelation(request.getKinRelation());
//        messageVO.setInternationalRemittanceLocation(request.getInternationalRemittanceLocation());
//        messageVO.setOriginatorLocation(request.getOriginatorRelation());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        // messageVO.setReserved1("1");
//
////        messageVO.setReserved1(request.getReserved1());
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("HRARegistration");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent HRA Registration  Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentHRARegistration(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent HRA Registration Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setReserved1("");
//            response.setReserved2("");
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent HRA Registration Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent HRA Registration  Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            response.setRrn(messageVO.getRetrievalReferenceNumber());
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuilder stringText = new StringBuilder()
//                .append(response.getRrn())
//                .append(response.getResponseCode())
//                .append(response.getResponseDescription())
//                .append(response.getResponseDateTime())
//                .append(response.getReserved1())
//                .append(response.getReserved2());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent HRA Registration PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }
//
//    public AgentCnicToCoreInquiryResponse agentCnicToCoreInquiry(AgentCnicToCoreInquiryRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cnic To Core Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCnicToCoreInquiryResponse response = new AgentCnicToCoreInquiryResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentId(request.getAgentId());
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setSenderMobileNumber(request.getSenderMobileNumber());
//        messageVO.setCnicNo(request.getSenderCnic());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverCNIC(request.getReceiverCnic());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCnicToCoreInquiry");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cnic To Core Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCnicToCoreInquiry(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Cnic To Core Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setProductId(messageVO.getProductID());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setReceiverCnic(messageVO.getReceiverCNIC());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cnic To Core Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cnic To Core Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cnic To Core Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

//    public AgentCnicToCorePaymentResponse agentCnicToCorePayment(AgentCnicToCorePaymentRequest request) {
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Agent Cnic To Core Payment Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        AgentCnicToCorePaymentResponse response = new AgentCnicToCorePaymentResponse();
//
//
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalID());
//        messageVO.setProductID(request.getProductId());
//        messageVO.setAgentId(request.getAgentId());
//        try {
//            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(),privateKey));
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setBvsRequest(request.getBvsRequest());
//        messageVO.setFingerIndex(request.getFingerIndex());
//        messageVO.setTemplateType(request.getTemplateType());
//        messageVO.setFingerTemplate(request.getFingerTemplate());
//        messageVO.setSenderMobileNumber(request.getSenderMobileNumber());
//        messageVO.setCnicNo(request.getSenderCnic());
//        messageVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
//        messageVO.setReceiverAccountNumber(request.getReceiverAccountNumber());
//        messageVO.setCoreAccountId(request.getCoreAccountId());
//        messageVO.setAccountTitle(request.getCoreAccountTitle());
//        messageVO.setTransactionAmount(request.getAmount());
//        messageVO.setCommissionAmount(request.getComissionAmount());
//        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
//        messageVO.setTotalAmount(request.getTotalAmount());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//
//
//
//        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
//        Should be reverted once otp optional implemented
//        on APIGEE End*/
//        //messageVO.setReserved1("1");
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("AgentCnicToCorePayment");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request XML
//        String requestXml = XMLUtil.convertToXML(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        // Call i8
//        try {
//            logger.info("[HOST] Sent Agent Cnic To Core Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            messageVO = switchController.agentCnicToCorePayment(messageVO);
//        } catch (Exception e) {
//            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//        }
//
//        // Set Response from i8
//        if (messageVO != null
//                && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//            logger.info("[HOST] Agent Agent Cnic To Core Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            response.setResponseDateTime(messageVO.getDateTime());
//            response.setBalance(messageVO.getBalance());
//            response.setTransactionId(messageVO.getTransactionId());
//            response.setProductId(messageVO.getProductID());
//            response.setReceiverMobileNumber(messageVO.getReceiverMobileNumber());
//            response.setReceiverAccountNumber(messageVO.getReceiverAccountNumber());
//            response.setReceiverTitle(messageVO.getAccountTitle());
//            response.setSenderMobileNumber(messageVO.getSenderMobileNumber());
//            response.setSenderCnic(messageVO.getCnicNo());
//            response.setAmount(messageVO.getTransactionAmount());
//            response.setComissionAmount(messageVO.getCommissionAmount());
//            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
//            response.setTotalAmount(messageVO.getTotalAmount());
//
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//            logger.info("[HOST] Agent Cnic To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(messageVO.getResponseCode());
//            response.setResponseDescription(messageVO.getResponseCodeDescription());
//            logModel.setResponseCode(messageVO.getResponseCode());
//            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//        } else {
//            logger.info("[HOST] Agent Cnic To Core Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            response.setResponseDescription("Host Not In Reach");
//            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//        }
//        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//        response.setHashData(sha256hex);
//
//        long endTime = new Date().getTime(); // end time
//        long difference = endTime - startTime; // check different
//        logger.debug("[HOST] ****Agent Cnic To Core Payment Request PROCESSED IN ****: " + difference + " milliseconds");
//
//        //preparing request XML
//        String responseXml = XMLUtil.convertToXML(response);
//        //Setting in logModel
//        logModel.setPduResponseHEX(responseXml);
//        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
//        return response;
//    }

    public AgentCashDepositInquiryResponse agentCashDepositInquiry(AgentCashDepositInquiryRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Cash In Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentCashDepositInquiryResponse response = new AgentCashDepositInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        messageVO.setProductID(request.getProductId());
        messageVO.setAgentId(request.getAgentId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentCashDepositInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Cash Deposit Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentCashDepositInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Ibft Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCustomerMobilenNumber(messageVO.getMobileNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setCustomerCnic(messageVO.getCnicNo());
            response.setAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Cash In Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Cash In Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentCashDepositPaymentResponse agentCashDepositPayment(AgentCashDepositPaymentRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Cash In Payment Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentCashDepositPaymentResponse response = new AgentCashDepositPaymentResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        messageVO.setProductID(request.getProductId());
        messageVO.setAgentId(request.getAgentId());
        try {
            messageVO.setMobilePin(RSAEncryption.decrypt(request.getPin(), loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
//        messageVO.setCnicNo(request.getCnic());
//        messageVO.setFingerIndex(request.getFingerIndex());
//        messageVO.setFingerTemplate(request.getFingerTemplate());
//        messageVO.setTemplateType(request.getTemplateType());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setCommissionAmount(request.getComissionAmount());
        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
        messageVO.setTotalAmount(request.getTotalAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentCashDepositPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Cash Deposit Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentCashDepositPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Cash Deposit Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCustomerMobilenNumber(messageVO.getMobileNo());
            response.setCustomerCnic(messageVO.getCnicNo());
            response.setBalance(messageVO.getBalance());
            response.setTransactionId(messageVO.getTransactionId());
            response.setAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());


            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Cash In Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Cash In Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Cash In Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentCashWithdrawalInquiryResponse agentCashWithdrawalInquiry(AgentCashWithdrawalInquiryRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Cash Withdrawal Inquiry Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentCashWithdrawalInquiryResponse response = new AgentCashWithdrawalInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        messageVO.setAgentId(request.getAgentId());
        messageVO.setProductID(request.getProductId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setPaymentMode(request.getPaymentMode());
        messageVO.setIsOtpRequest(request.getIsOtpRequest());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentCashWithdrawalInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Cash Withdrawal Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentCashWithdrawalInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Cash Withdrawal Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCustomerMobileNumber(messageVO.getMobileNo());
            response.setAgentMobileNumber(messageVO.getAgentMobileNumber());
            response.setCustomerName(messageVO.getConsumerName());
            response.setCustomerCnic(messageVO.getCnicNo());
            response.setAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Cash Withdrawal Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Cash Withdrawal Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Cash Withdrawal Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentCashWithdrawalPaymentResponse agentCashWithdrawalPayment(AgentCashWithdrawalPaymentRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Agent Cash Withdrawal Payment Starting Processing Agent Cas Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentCashWithdrawalPaymentResponse response = new AgentCashWithdrawalPaymentResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalID());
        messageVO.setProductID(request.getProductId());
        messageVO.setAgentId(request.getAgentId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());

        try {
            if (request.getPin() != null && !request.getPin().equals("")) {
                String text = request.getPin();
                String pin = text.replaceAll("\\r|\\n", "");
                messageVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
                logger.info("Decrypted Mobile Pin: " + messageVO.getMobilePin());
            }

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            if (request.getOtp() != null && !request.getOtp().equals("")) {
                String text = request.getOtp();
                String otp = text.replaceAll("\\r|\\n", "");
                messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
                logger.info("Decrypted OTP : " + messageVO.getOtpPin());
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

//        messageVO.setOtpPin(request.getOtp());
        messageVO.setTransactionId(request.getTransactionId());
        messageVO.setAgentMobileNumber(request.getAgentMobileNumber());
        messageVO.setMobileNo(request.getCustomerMobileNumber());
        messageVO.setCnicNo(request.getCustomerCnic());
//        messageVO.setFingerIndex(request.getFingerIndex());
//        messageVO.setFingerTemplate(request.getFingerTemplate());
//        messageVO.setTemplateType(request.getTemplateType());
        messageVO.setTransactionAmount(request.getAmount());
        messageVO.setCommissionAmount(request.getComissionAmount());
        messageVO.setTransactionProcessingAmount(request.getTransactionProcessingAmount());
        messageVO.setTotalAmount(request.getTotalAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AgentCashWithdrawalPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Agent Cash Withdrawal Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.agentCashWithdrawalPayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Agent Cash Withdrawal Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setBalance(messageVO.getBalance());
            response.setCustomerMobileNumber(messageVO.getMobileNo());
            response.setCustomerCnic(messageVO.getCnicNo());
            response.setTransactionId(messageVO.getTransactionId());
            response.setAmount(messageVO.getTransactionAmount());
            response.setComissionAmount(messageVO.getCommissionAmount());
            response.setTransactionProcessingAmount(messageVO.getTransactionProcessingAmount());
            response.setTotalAmount(messageVO.getTotalAmount());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Agent Cash Withdrawal Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Agent Cash Withdrawal Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Agent Cash Withdrawal Payment Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public MpinVerificationResponse mpinVerification(MpinVerificationRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] MPIN Verification Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        MpinVerificationResponse response = new MpinVerificationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        try {
            messageVO.setMobilePin(RSAEncryption.decrypt(request.getMpin(), loginPrivateKey));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("MpinVerification");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent MPIN Verification Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.mpinVerification(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] MPIN Verification Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] MPIN Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] MPIN Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****MPIN Verification Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public SegmentListResponse segmentList(SegmentListRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Segment List Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        SegmentListResponse response = new SegmentListResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("SegmentList");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Segment List Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.listSegments(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Segment List Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setSegmentNames(messageVO.getSegmentNames());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Segment List Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Segment List Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Segment List Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AgentCatalogResponse catalogList(AgentCatalogsRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Catalog List Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AgentCatalogResponse response = new AgentCatalogResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CatalogList");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Catalog List Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.listCatalogs(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Segment List Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCatalogNames(messageVO.getCatalogNames());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Catalog List Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Catalog List Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Catalog List Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public L2AccountOpeningResponse l2AccountOpening(L2AccountOpeningRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] L2 Account Opening Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        L2AccountOpeningResponse response = new L2AccountOpeningResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setConsumerName(request.getConsumerName());
        messageVO.setFatherHusbandName(request.getFatherHusbandName());
        messageVO.setGender(request.getGender());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setCnicIssuanceDate(request.getCnicIssuanceDate());
        messageVO.setDateOfBirth(request.getDob());
        messageVO.setBirthPlace(request.getBirthPlace());
        messageVO.setMotherMaiden(request.getMotherMaiden());
        messageVO.setEmailAddress(request.getEmailAddress());
        messageVO.setMailingAddress(request.getMailingAddress());
        messageVO.setPresentAddress(request.getPermanentAddress());
        messageVO.setPurposeOfAccount(request.getPurposeOfAccount());
        messageVO.setSourceOfIncome(request.getSourceOfIncome());
        messageVO.setExpectedMonthlyTurnover(request.getExpectedMonthlyTurnover());
        messageVO.setNextOfKin(request.getNextOfKin());
        messageVO.setCnicFrontPhoto(request.getCnicFrontPic());
        messageVO.setCnicBackPhoto(request.getCnicBackPic());
        messageVO.setCustomerPhoto(request.getCustomerPic());
        messageVO.setLatitude(request.getLatitude());
        messageVO.setLongitude(request.getLongitude());
        messageVO.setAccountType(request.getAccountType());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("L2AccountOpening");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent L2 Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.l2AccountOpening(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Segment List Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] L2 Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] L2 Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****L2 Account Opening Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public L2AccountUpgradeResponse l2AccountUpgrade(L2AccountUpgradeRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] L2 Account Upgrade Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        L2AccountUpgradeResponse response = new L2AccountUpgradeResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        if (request.getReserved1().equals("02")) {
//            messageVO.setOtpPin(request.getMpin());
            try {
                if (request.getMpin() != null) {
                    String text = request.getMpin();
                    String otp = text.replaceAll("\\r|\\n", "");
                    messageVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));

                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
//            messageVO.setMobilePin(request.getMpin());
            try {
                if (request.getMpin() != null) {
                    String text = request.getMpin();
                    String otp = text.replaceAll("\\r|\\n", "");
                    messageVO.setMobilePin(RSAEncryption.decrypt(otp, loginPrivateKey));
                }
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        messageVO.setCnicNo(request.getCnic());
        messageVO.setFingerIndex(request.getFingerIndex());
        messageVO.setFingerTemplate(request.getFingerTemplate());
        messageVO.setTemplateType(request.getTemplateType());
        messageVO.setConsumerName(request.getConsumerName());
        messageVO.setFatherHusbandName(request.getFatherHusbandName());
        messageVO.setGender(request.getGender());
        messageVO.setCnicIssuanceDate(request.getCnicIssuanceDate());
        messageVO.setDateOfBirth(request.getDob());
        messageVO.setBirthPlace(request.getBirthPlace());
        messageVO.setMotherMaiden(request.getMotherMaiden());
        messageVO.setEmailAddress(request.getEmailAddress());
        messageVO.setMailingAddress(request.getMailingAddress());
        messageVO.setPermanentAddress(request.getPermanentAddress());
        messageVO.setPurposeOfAccount(request.getPurposeOfAccount());
        messageVO.setSourceOfIncome(request.getSourceOfIncome());
        messageVO.setSourceOfIncomePic(request.getSourceOfIncomePic());
        messageVO.setExpectedMonthlyTurnover(request.getExpectedMonthlyTurnover());
        messageVO.setNextOfKin(request.getNextOfKin());
        messageVO.setCnicFrontPhoto(request.getCnicFrontPic());
        messageVO.setCnicBackPhoto(request.getCnicBackPic());
        messageVO.setCustomerPhoto(request.getCustomerPic());
        messageVO.setLatitude(request.getLatitude());
        messageVO.setLongitude(request.getLongitude());
        messageVO.setReserved1(request.getReserved1());
        if (request.getReserved2().equals("")) {
            messageVO.setReserved2("0");
        } else {
            messageVO.setReserved2(request.getReserved2());
        }
//        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("L2AccountUpgrade");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent L2 Account Upgrade Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.l2AccountUpgrade(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] L2 Account Upgrade Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionCode(messageVO.getTransactionId());


            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] L2 Account Upgrade Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] L2 Account Upgrade Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****L2 Account Upgrade Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public AccountDetailResponse accountDetailResponse(AccountDetails request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Account Detail Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountDetailResponse response = new AccountDetailResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setStockTrading(request.getStrockTrading());
        messageVO.setMutulaFunds(request.getMutualFunds());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AccountDetail");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent  Account Detail Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountDetail(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Account Detail Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Account Detail Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Account Detail Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Account Detail Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CustomerNameUpdateResponse customerNameUpdateResponse(CustomerNameUpdateRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Customer Name Update Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        CustomerNameUpdateResponse response = new CustomerNameUpdateResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setFirstName(request.getFirstName());
        messageVO.setLastName(request.getLastName());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CustomerNameUpdate");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent  Customer Name Update Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.customerNameUpdate(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Customer Name Update Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Customer Name Update Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Customer Name Update Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Customer Name Update Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public CLSStatusUpdateResponse clsStatusUpdateResponse(CLSStatusUpdateRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  CLS Status Update Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        CLSStatusUpdateResponse response = new CLSStatusUpdateResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCaseId(request.getCaseId());
        messageVO.setCaseStatus(request.getCaseStatus());
        messageVO.setClsComment(request.getClsComment());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("CLSStatusUpdate");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent CLS Status Update Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.clsStatusUpdate(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  CLS Status Update Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  CLS Status Update Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  CLS Status Update Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** CLS Status Update Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);


        logger.info("Response Send To Compliance " + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public BlinkAccountVerificationInquiryResponse blinkAccountVerificationInquiryResponse
            (BlinkAccountVerificationInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Blink Account Verification Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        BlinkAccountVerificationInquiryResponse response = new BlinkAccountVerificationInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("BlinkAccountVerificationInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Blink Account Verification Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.blinkAccountVerificationInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);


        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Blink Account Verification Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setRegistrationState(messageVO.getCustomerRegState());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Blink Account Verification Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Blink Account Verification Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Blink Account Verification Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public BlinkAccountVerificationResponse blinkAccountVerificationResponse(BlinkAccountVerificationRequest
                                                                                     request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Blink Account Verification Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        BlinkAccountVerificationResponse response = new BlinkAccountVerificationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setFingerIndex(request.getFingerIndex());
        messageVO.setFingerTemplate(request.getFingerTemplate());
        messageVO.setTemplateType(request.getTemplateType());
        messageVO.setConsumerName(request.getConsumerName());
        messageVO.setFatherHusbandName(request.getFatherHusbandName());
        messageVO.setMotherMaiden(request.getMotherMaiden());
        messageVO.setGender(request.getGender());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("BlinkAccountVerification");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Blink Account Verification Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.blinkAccountVerification(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Blink Account Verification Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Blink Account Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Blink Account Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Blink Account Verification Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }


    public DebitCardStatusReponse debitCardStatusReponse(DebitCardStatusRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Debit Card Status Verification Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        DebitCardStatusReponse response = new DebitCardStatusReponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("DebitCardStatus");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Debit Card Verification Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.debitCardStatusVerification(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Debit Card Verification Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setCardStatus(messageVO.getCardDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Debit Card Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Debit Card Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Debit Card Verification Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }


    public AdvanceLoanEarlyPyamentResponse advanceLoanEarlyPyamentResponse(AdvanceLoanEarlyPaymentSettlementRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Advance Early Payment Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AdvanceLoanEarlyPyamentResponse response = new AdvanceLoanEarlyPyamentResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setReserved1(request.getReserved());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AdvanceEarlyPaymentSettlement");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Advance Early Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.advanceLoanPaymentSettlement(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Advance Early Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Advance Early Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Advance Early Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Advance Early Payment Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public FeePaymentInquiryResponse feePaymentInquiryResponse(FeePaymentInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Fee Payment Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        FeePaymentInquiryResponse response = new FeePaymentInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPinType(request.getPinType());
        messageVO.setCardTypeId(request.getCardFeeType());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setProductID(request.getProductId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AdvanceEarlyPaymentSettlement");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent  Fee Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.feePaymentInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]   Fee Payment Inquiry Reuest Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCharges(messageVO.getCharges());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]   Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Fee Payment Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }


    public FeePaymentResponse feePaymentResponse(FeePaymentRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  Fee Payment Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        FeePaymentResponse response = new FeePaymentResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPinType(request.getPinType());
        if (request.getReserved1().equals("02")) {
            messageVO.setOtpPin(request.getPin());
        } else {
            messageVO.setMobilePin(request.getPin());
        }
        messageVO.setCnicNo(request.getCnic());
        messageVO.setProductID(request.getProductId());
        messageVO.setCardTypeId(request.getCardfeetypeid());
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("AdvanceEarlyPaymentSettlement");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent  Fee Payment Inquiry Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.feePayment(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]   Fee Payment Inquiry Reuest Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]   Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Fee Payment Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }


    public MinorAccountOpeningResponse m0AccountOpeningResponse(MinorAccountOpeningRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST]  M0Account Opening Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        MinorAccountOpeningResponse response = new MinorAccountOpeningResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setConsumerName(request.getName());
        messageVO.setCnicNo(request.getNic());
        messageVO.setCnicIssuanceDate(request.getIssuanceDate());
        messageVO.setMotherMaiden(request.getMotherMedianName());
        messageVO.setFatherHusbandName(request.getFatherName());
        messageVO.setBirthPlace(request.getPlaceOfbirth());
        messageVO.setDateOfBirth(request.getDateOfBirth());
        messageVO.setPermanentAddress(request.getAddress());
        messageVO.setPresentAddress(request.getAddress());
        messageVO.setCnicExpiry(request.getNicExpiry());
        messageVO.setParentCnicPic(request.getParentCnicPic());
        messageVO.setSnicPic(request.getSnicPic());
        messageVO.setMinorCustomerPic(request.getMinorCutomerPic());
        messageVO.setFatherMotherMobileNumber(request.getFatherMotherMobileNumber());
        messageVO.setFatherCnic(request.getFatherCnic());
        messageVO.setFatherCnicIssuanceDate(request.getFatherCnicIssuanceDate());
        messageVO.setFatherCnicExpiryDate(request.getFatherCnicExpiryDate());
        messageVO.setAccountType("01");
        messageVO.setMotherCnic(request.getMotherCnic());
        messageVO.setbFormPic(request.getBFormPic());
        messageVO.setEmailAddress(request.getEmail());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved4("1");
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setGender(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("M0AccountOpening");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent  M0 Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.accountOpening(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  M0 Account Opening Reuest Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]   Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST]  Fee Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Fee Payment Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

//    public void sentWalletRequest(MiddlewareMessageVO middlewareMessageVO) throws Exception {
//        logger.info("Core To Wallet Push To SAF against RRN: " + middlewareMessageVO.getRetrievalReferenceNumber());
//        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
//        middlewareAdviceVO.setAccountNo1(middlewareMessageVO.getAccountNo1());
//        middlewareAdviceVO.setAccountNo2(middlewareMessageVO.getAccountNo2());
//        middlewareAdviceVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
//        middlewareAdviceVO.setRequestTime(middlewareMessageVO.getRequestTime());
//        middlewareAdviceVO.setStan(middlewareMessageVO.getStan());
//        middlewareAdviceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
//        middlewareAdviceVO.setAdviceType("creditPayment"); // Used in DLQ
//        middlewareAdviceVO.setBankIMD(middlewareMessageVO.getBankIMD());
//        middlewareAdviceVO.setProductId(middlewareMessageVO.getProductId());
//
////        boolean isAlreadyExists = transactionDAO.getIbftDuplicateRequest(middlewareAdviceVO.getStan(),middlewareAdviceVO.getRequestTime());
//
//        // Create a ConnectionFactory
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://" + I8_QUEUE_SERVER + ":" + I8_QUEUE_PORT);
//
//        // Create a Connection
//        Connection connection = connectionFactory.createConnection();
//        connection.start();
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination destination = session.createQueue("queue/creditPaymentQueue");
//
//        MessageProducer producer = session.createProducer(destination);
//
//        ObjectMessage message = session.createObjectMessage(middlewareAdviceVO);
//
//        producer.send(message);
//        connection.close();
//
//    }
//
//
//    public void sentDebitPaymentRequest(MiddlewareMessageVO middlewareMessageVO) throws Exception {
//        logger.info("Debit Payment Reversal Push To SAF against RRN: " + middlewareMessageVO.getRetrievalReferenceNumber());
//        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
//        middlewareAdviceVO.setAccountNo1(middlewareMessageVO.getAccountNo1());
//        middlewareAdviceVO.setAccountNo2(middlewareMessageVO.getAccountNo2());
//        middlewareAdviceVO.setRequestTime(middlewareMessageVO.getRequestTime());
//        middlewareAdviceVO.setStan(middlewareMessageVO.getStan());
//        middlewareAdviceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
//        middlewareAdviceVO.setAdviceType("debitPayment"); // Used in DLQ
//        middlewareAdviceVO.setBankIMD(middlewareMessageVO.getBankIMD());
//        middlewareAdviceVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
//        middlewareAdviceVO.setProductId(middlewareMessageVO.getProductId());
//        middlewareAdviceVO.setReversalRequestTime(middlewareMessageVO.getDateTime());
//        // Create a ConnectionFactory
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://" + I8_QUEUE_SERVER + ":" + I8_QUEUE_PORT);
//
//        // Create a Connection
//        Connection connection = connectionFactory.createConnection();
//        connection.start();
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination destination = session.createQueue("queue/debitPaymentQueue");
//
//        MessageProducer producer = session.createProducer(destination);
//
//        ObjectMessage message = session.createObjectMessage(middlewareAdviceVO);
//
//        producer.send(message);
//        connection.close();
//
//    }
//
//    private Date getRequestTime(String date) {
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
//            Date dt = formatter.parse(date);
//            return dt;
//        } catch (Exception e) {
//            logger.error("Exception", e);
//        }
//        return null;
//    }

    public OptasiaCreditInquiryResponse optasiaCreditInquiryResponse(OptasiaCreditInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Optasia Credit Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        OptasiaCreditInquiryResponse response = new OptasiaCreditInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        messageVO.setPinType(request.getPinType());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OptasiaCreditInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);
        // Call i8
        try {
            logger.info("[HOST] Sent Optasia Credit Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cnicTo256(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Optasia Credit Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            CreditInquiryRequest creditInquiryRequest = new CreditInquiryRequest();

            creditInquiryRequest.setUserName(request.getUserName());
            creditInquiryRequest.setPassword(request.getPassword());
            creditInquiryRequest.setMobileNumber(messageVO.getMobileNo());
            creditInquiryRequest.setDateTime(request.getDateTime());
            creditInquiryRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            creditInquiryRequest.setChannelId(request.getChannelId());
            creditInquiryRequest.setTerminalId(request.getTerminalId());
            creditInquiryRequest.setProductId(request.getProductId());
            creditInquiryRequest.setPinType(request.getPinType());
            creditInquiryRequest.setTransactionAmount(request.getTransactionAmount());
            creditInquiryRequest.setReserved1(request.getReserved1());
            creditInquiryRequest.setReserved2(request.getReserved2());
            creditInquiryRequest.setReserved3(request.getReserved3());
            creditInquiryRequest.setReserved4(request.getReserved4());
            creditInquiryRequest.setReserved5(request.getReserved5());
            creditInquiryRequest.setReserved6(request.getReserved6());
            creditInquiryRequest.setReserved7(request.getReserved7());
            creditInquiryRequest.setReserved8(request.getReserved8());
            creditInquiryRequest.setReserved9(request.getReserved9());
            creditInquiryRequest.setReserved10(request.getReserved10());
            logger.info("[HOST] Optasia Credit Inquiry Payment Request Sent to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            CreditInquiryResponse creditInquiryResponse = new CreditInquiryResponse();

            creditInquiryResponse = this.creditInquiryResponse(creditInquiryRequest);

            if (creditInquiryResponse != null
                    && StringUtils.isNotEmpty(creditInquiryResponse.getResponseCode())
                    && creditInquiryResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                response.setRrn(creditInquiryResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(creditInquiryResponse.getResponseDescription());
                response.setResponseDateTime(creditInquiryResponse.getResponseDateTime());
                response.setTransactionId(messageVO.getTransactionId());
                response.setComissionAmount(creditInquiryResponse.getComissionAmount());
                response.setAmount(messageVO.getTransactionAmount());
                response.setTotalAmount(creditInquiryResponse.getTotalAmount());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (creditInquiryResponse != null && StringUtils.isNotEmpty(creditInquiryResponse.getResponseCode())) {
                logger.info("[HOST] Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + creditInquiryResponse.getRrn());
                response.setResponseCode(creditInquiryResponse.getResponseCode());
                response.setResponseDescription(creditInquiryResponse.getResponseDescription());
                response.setRrn(creditInquiryResponse.getRrn());
                logModel.setResponseCode(creditInquiryResponse.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }

            StringBuilder stringText = new StringBuilder()
                    .append(response.getRrn())
                    .append(response.getResponseCode())
                    .append(response.getResponseDescription())
                    .append(response.getResponseDateTime());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] ****Optasia Credit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Optasia Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Optasia Credit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }

        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Optasia Credit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Optasia Credit Inquiry PAYMENT RESPONSE" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);

        updateTransactionInDB(logModel);
        return response;
    }

    public OptasiaCreditResponse optasiaCreditResponse(OptasiaCreditRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Optasia Credit Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        OptasiaCreditResponse response = new OptasiaCreditResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
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
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();

        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OptasiaCredit");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);
        try {
            logger.info("[HOST] Sent Optasia Credit Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cnicTo256(messageVO);
        } catch (Exception e) {
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

//below code comment disscuss with zulfiqar sir
//            if (e instanceof RemoteAccessException) {
//                if (!(e instanceof RemoteConnectFailureException)) {
//                    messageVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                }
//            }
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String stackTrace = sw.toString();
//            if (stackTrace.contains("status code = 503")) {
//                MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
//                middlewareMessageVO.setAccountNo1(messageVO.getMobileNo());
//                middlewareMessageVO.setAccountNo2(messageVO.getMobileNo());
//                middlewareMessageVO.setStan(messageVO.getReserved2());
//                middlewareMessageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//                middlewareMessageVO.setRequestTime(getRequestTime(messageVO.getDateTime()));
//                middlewareMessageVO.setTransactionAmount(messageVO.getTransactionAmount());
//                middlewareMessageVO.setProductId(Long.parseLong(messageVO.getProductID()));
//
//                try {
//                    this.sentWalletRequest(middlewareMessageVO);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    messageVO.setResponseCode("550");
//                    messageVO.setResponseCodeDescription("Host Not In Reach");
//                }
//            } else {
//                messageVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                messageVO.setResponseCodeDescription("Successful");
//
//            }

        }


        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().

                equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Optasia Credit Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            CreditRequest creditRequest = new CreditRequest();

            creditRequest.setUserName(request.getUserName());
            creditRequest.setPassword(request.getPassword());
            creditRequest.setMobileNumber(messageVO.getMobileNo());
            creditRequest.setDateTime(request.getDateTime());
            creditRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            creditRequest.setChannelId(request.getChannelId());
            creditRequest.setTerminalId(request.getTerminalId());
            creditRequest.setProductId(request.getProductId());
            creditRequest.setPin(request.getPin());
            creditRequest.setPinType(request.getPinType());
            creditRequest.setTransactionAmount(request.getTransactionAmount());
            creditRequest.setReserved1(request.getReserved1());
            creditRequest.setReserved2(request.getReserved2());
            creditRequest.setReserved3(request.getReserved3());
            creditRequest.setReserved4(request.getReserved4());
            creditRequest.setReserved5(request.getReserved5());
            creditRequest.setReserved6(request.getReserved6());
            creditRequest.setReserved7(request.getReserved7());
            creditRequest.setReserved8(request.getReserved8());
            creditRequest.setReserved9(request.getReserved9());
            creditRequest.setReserved10(request.getReserved10());
            logger.info("[HOST] Optasia Credit Payment Request Sent to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            CreditResponse creditResponse = new CreditResponse();
            creditResponse = this.creditResponse(creditRequest);

            if (creditResponse != null
                    && StringUtils.isNotEmpty(creditResponse.getResponseCode())
                    && creditResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                response.setRrn(creditResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(creditResponse.getResponseDescription());
                response.setResponseDateTime(creditResponse.getResponseDateTime());
                response.setTransactionId(creditResponse.getTransactionId());
                response.setComissionAmount(creditResponse.getComissionAmount());
                response.setTransactionAmount(creditResponse.getTransactionAmount());
                response.setTotalTransactionAmount(creditResponse.getTotalTransactionAmount());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (creditResponse != null && StringUtils.isNotEmpty(creditResponse.getResponseCode())) {
                logger.info("[HOST] Credit Payment Request Unsuccessful from Micro Bank RRN: " + creditResponse.getRrn());
                response.setResponseCode(creditResponse.getResponseCode());
                response.setResponseDescription(creditResponse.getResponseDescription());
                response.setRrn(creditResponse.getRrn());
                logModel.setResponseCode(creditResponse.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Credit Payment Request Unsuccessful from Micro Bank RRN: " + creditResponse.getRrn());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }

            StringBuilder stringText = new StringBuilder()
                    .append(response.getRrn())
                    .append(response.getResponseCode())
                    .append(response.getResponseDescription())
                    .append(response.getResponseDateTime());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] ****Credit Payment PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Optasia Credit  Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Optasia Credit Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }

        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Optasia Credit PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Optasia Credit PAYMENT RESPONSE" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);

        updateTransactionInDB(logModel);
        return response;
    }

    public OptasiaDebitInquiryResponse optasiaDebitInquiryResponse(OptasiaDebitInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Optasia Debit Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        OptasiaDebitInquiryResponse response = new OptasiaDebitInquiryResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
        messageVO.setMobilePin(request.getPin());
        messageVO.setPinType(request.getPinType());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OptasiaDebitInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Optasia Debit Inquiry Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cnicTo256(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Optasia Debit Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            DebitInquiryRequest debitInquiryRequest = new DebitInquiryRequest();

            debitInquiryRequest.setUserName(request.getUserName());
            debitInquiryRequest.setPassword(request.getPassword());
            debitInquiryRequest.setMobileNumber(messageVO.getMobileNo());
            debitInquiryRequest.setDateTime(request.getDateTime());
            debitInquiryRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            debitInquiryRequest.setChannelId(request.getChannelId());
            debitInquiryRequest.setTerminalId(request.getTerminalId());
            debitInquiryRequest.setProductId(request.getProductId());
            debitInquiryRequest.setPinType(request.getPinType());
            debitInquiryRequest.setTransactionAmount(request.getTransactionAmount());
            debitInquiryRequest.setReserved1(request.getReserved1());
            debitInquiryRequest.setReserved2(request.getReserved2());
            debitInquiryRequest.setReserved3(request.getReserved3());
            debitInquiryRequest.setReserved4(request.getReserved4());
            debitInquiryRequest.setReserved5(request.getReserved5());
            debitInquiryRequest.setReserved6(request.getReserved6());
            debitInquiryRequest.setReserved7(request.getReserved7());
            debitInquiryRequest.setReserved8(request.getReserved8());
            debitInquiryRequest.setReserved9(request.getReserved9());
            debitInquiryRequest.setReserved10(request.getReserved10());
            logger.info("[HOST] Optasia Debit Inquiry Payment Request Sent to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            DebitInquiryResponse debitInquiryResponse = new DebitInquiryResponse();
            debitInquiryResponse = this.debitInquiryResponse(debitInquiryRequest);

            if (debitInquiryResponse != null
                    && StringUtils.isNotEmpty(debitInquiryResponse.getResponseCode())
                    && debitInquiryResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                response.setRrn(debitInquiryResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(debitInquiryResponse.getResponseDescription());
                response.setResponseDateTime(debitInquiryResponse.getResponseDateTime());
                response.setTransactionId(messageVO.getTransactionId());
                response.setComissionAmount(debitInquiryResponse.getComissionAmount());
                response.setAmount(messageVO.getTransactionAmount());
                response.setTotalAmount(debitInquiryResponse.getTotalAmount());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (debitInquiryResponse != null && StringUtils.isNotEmpty(debitInquiryResponse.getResponseCode())) {
                logger.info("[HOST] Debit Inquiry Request Unsuccessful from Micro Bank RRN: " + debitInquiryResponse.getRrn());
                response.setResponseCode(debitInquiryResponse.getResponseCode());
                response.setResponseDescription(debitInquiryResponse.getResponseDescription());
                response.setRrn(debitInquiryResponse.getRrn());
                logModel.setResponseCode(debitInquiryResponse.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Debit Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }

            StringBuilder stringText = new StringBuilder()
                    .append(response.getRrn())
                    .append(response.getResponseCode())
                    .append(response.getResponseDescription())
                    .append(response.getResponseDateTime());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] ****Optasia Debit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Optasia Debit Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Optasia Debit Inquiry Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());

        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Optasia Debit Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Optasia Debit Inquiry PAYMENT RESPONSE" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public OptasiaDebitResponse optasiaDebitResponse(OptasiaDebitRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Optasia Debit Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        OptasiaDebitResponse response = new OptasiaDebitResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setProductID(request.getProductId());
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
        messageVO.setTransactionAmount(request.getTransactionAmount());
        messageVO.setReserved1(request.getPinType());
        messageVO.setReserved2(request.getReserved2());
        messageVO.setReserved3(request.getReserved3());
        messageVO.setReserved4(request.getReserved4());
        messageVO.setReserved5(request.getReserved5());
        messageVO.setReserved6(request.getReserved6());
        messageVO.setReserved7(request.getReserved7());
        messageVO.setReserved8(request.getReserved8());
        messageVO.setReserved9(request.getReserved9());
        messageVO.setReserved10(request.getReserved10());


        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        // messageVO.setReserved1("1");

//        messageVO.setReserved1(request.getReserved1());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OptasiaDebit");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Optasia Debit Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cnicTo256(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//
//            if (e instanceof RemoteAccessException) {
//                if (!(e instanceof RemoteConnectFailureException)) {
//
//                    try {
//                        MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
//                        middlewareMessageVO.setAccountNo1(messageVO.getMobileNo());
//                        middlewareMessageVO.setAccountNo2(messageVO.getMobileNo());
//                        middlewareMessageVO.setStan(messageVO.getReserved2());
//                        middlewareMessageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//                        middlewareMessageVO.setRequestTime(getRequestTime(messageVO.getDateTime()));
//                        middlewareMessageVO.setDateTime(messageVO.getDateTime());
//                        middlewareMessageVO.setTransactionAmount(messageVO.getTransactionAmount());
//                        middlewareMessageVO.setProductId(Long.parseLong(messageVO.getProductID()));
//                        this.sentDebitPaymentRequest(middlewareMessageVO);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        messageVO.setResponseCode("550");
//                        messageVO.setResponseCodeDescription("Host Not In Reach");
//                    }
//
//                    messageVO.setResponseCode(" ");
//                    messageVO.setResponseCodeDescription("");
//                    messageVO.setRetrievalReferenceNumber("");
//                }
//            }
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String stackTrace = sw.toString();
//            if (stackTrace.contains("status code = 503")) {
//                messageVO.setResponseCode("550");
//                messageVO.setResponseCodeDescription("Host Not In Reach");
//            }
        }


        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Optasia Debit Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

//            if (messageVO.getProductID().equals("10245364")){
//                PaymentReversalRequest reversalRequest=new PaymentReversalRequest();
//                String requestTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                String stan = String.valueOf((100000 + new Random().nextInt(900000)));
//                reversalRequest.setTransactionCode(request.getRrn());
//                reversalRequest.setRrn(requestTime+stan);
//                reversalRequest.setDateTime(request.getDateTime());
//                reversalRequest.setUserName(request.getUserName());
//                reversalRequest.setPassword(request.getPassword());
//                reversalRequest.setChannelId(request.getChannelId());
//                this.paymentReversal(reversalRequest);
//            }
            DebitRequest debitRequest = new DebitRequest();

            debitRequest.setUserName(request.getUserName());
            debitRequest.setPassword(request.getPassword());
            debitRequest.setMobileNumber(messageVO.getMobileNo());
            debitRequest.setDateTime(request.getDateTime());
            debitRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            debitRequest.setChannelId(request.getChannelId());
            debitRequest.setTerminalId(request.getTerminalId());
            debitRequest.setProductId(request.getProductId());
            debitRequest.setPin(request.getPin());
            debitRequest.setPinType(request.getPinType());
            debitRequest.setTransactionAmount(request.getTransactionAmount());
            debitRequest.setReserved1(request.getReserved1());
            debitRequest.setReserved2(request.getReserved2());
            debitRequest.setReserved3(request.getReserved3());
            debitRequest.setReserved4(request.getReserved4());
            debitRequest.setReserved5(request.getReserved5());
            debitRequest.setReserved6(request.getReserved6());
            debitRequest.setReserved7(request.getReserved7());
            debitRequest.setReserved8(request.getReserved8());
            debitRequest.setReserved9(request.getReserved9());
            debitRequest.setReserved10(request.getReserved10());
            logger.info("[HOST] Optasia Debit Payment Request Sent to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            DebitResponse debitResponse = new DebitResponse();
            debitResponse = this.debitResponse(debitRequest);

            if (debitResponse != null
                    && StringUtils.isNotEmpty(debitResponse.getResponseCode())
                    && debitResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                response.setRrn(debitResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(debitResponse.getResponseDescription());
                response.setResponseDateTime(debitResponse.getResponseDateTime());
                response.setTransactionId(debitResponse.getTransactionId());
                response.setComissionAmount(debitResponse.getComissionAmount());
                response.setTransactionAmount(debitResponse.getTransactionAmount());
                response.setTotalTransactionAmount(debitResponse.getTotalTransactionAmount());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(debitResponse.getResponseCode())) {
                logger.info("[HOST] Debit Payment Request Unsuccessful from Micro Bank RRN: " + debitResponse.getRrn());
                response.setResponseCode(debitResponse.getResponseCode());
                response.setResponseDescription(debitResponse.getResponseDescription());
                response.setRrn(debitResponse.getRrn());
                logModel.setResponseCode(debitResponse.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Debit Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }

            StringBuilder stringText = new StringBuilder()
                    .append(response.getRrn())
                    .append(response.getResponseCode())
                    .append(response.getResponseDescription())
                    .append(response.getResponseDateTime());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] ****Optasia Debit Payment PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Optasia Debit Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Optasiaa Debit Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getRrn())
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getResponseDateTime());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Optasia Debit PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Optasia Debit PAYMENT RESPONSE" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public TransactionStatusResponse transactionStatusResponse(TransactionStatusRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Transaction Status Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        TransactionStatusResponse response = new TransactionStatusResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setThirdPartyTransactionId(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("TransactionStatus");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Transaction Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.transactionStatus(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Transaction Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());
            response.setTransactionAmount(messageVO.getTransactionAmount());
            response.setTotalAmount(messageVO.getTotalAmount());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Transaction Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Transaction Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Transaction Status Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Transaction Status Response" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public ProfileStatusResponse profileStatusResponse(ProfileStatusRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Profile Status Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        ProfileStatusResponse response = new ProfileStatusResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("ProfileStatus");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Profile Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.profileStatus(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST]  Profile Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setWalletType(messageVO.getWalletType());
            response.setWalletStatus(messageVO.getWalletStatus());
            response.setTaxRegime(messageVO.getTaxRegime());
            response.setLienStatus(messageVO.getLienStatus());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST]  Profile Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Profile Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Profile Status Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Profile Status Response" + responseXml);

        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public LienStatusResponse lienStatusResponse(LienStatusRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Lien Status Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LienStatusResponse response = new LienStatusResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LienStatus");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Lien Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.lienStatus(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Lien Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Lien Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Lien Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Lien Status Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Lien Status Response" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);

        return response;
    }

    public OfferListForCommodityResponse initiateLoanResponse(OfferListForCommodityRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Initiate Loan Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        OfferListForCommodityResponse response = new OfferListForCommodityResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
//        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setIdentityType(request.getIdentityType());
        messageVO.setIdentityValue(request.getIdentityValue());
        messageVO.setOrigSource(request.getOrigSource());
        messageVO.setCommodityType(request.getCommodityType());
        messageVO.setSourceRequestId(request.getSourceRequestId());
        messageVO.setOfferName(request.getOfferName());
        messageVO.setAmount(request.getAmount());
        messageVO.setFed(request.getFed());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("InitiateLoan");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.offerListForCommodity();
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Initiate Loan Request: " + responseXml);
        } else {
            // Call i8

            try {
                logger.info("[HOST] Sent Initiate Loan Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.initiateLoan(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Initiate Loan Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setIdentityValue(messageVO.getIdentityValue());
                response.setIdentityType(messageVO.getIdentityType());
                response.setOrigSource(messageVO.getOrigSource());
                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
                response.setEligibilityStatusList(messageVO.getEligibilityStatusList());
                response.setLoanOffersByLoanProductGroupList(messageVO.getLoanOffersByLoanProductGroupList());
                response.setOutstandingStatusList(messageVO.getOutstandingStatusList());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Initiate Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Initiate Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Initiate Loan Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Initiate Loan Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public LoanOfferResponse selectLoanResponse(LoanOfferRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Select Loan Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoanOfferResponse response = new LoanOfferResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setSourceRequestId(request.getSourceRequestId());
        messageVO.setOfferName(request.getOfferName());
        messageVO.setAmount(request.getAmount());
        messageVO.setExternalLoanId(request.getExternalLoanId());
        messageVO.setMerchantId(request.getMerchantId());
        messageVO.setFed(request.getFed());
        messageVO.setLoanPurpose(request.getLoanPurpose());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("SelectLoan");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.loanOffer();
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Select Loan Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Select Loan Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.selectLoan(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Select Loan Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setCode(messageVO.getCode());
                response.setMessage(messageVO.getMessage());
                response.setIdentityValue(messageVO.getIdentityValue());
                response.setIdentityType(messageVO.getIdentityType());
                response.setOrigSource(messageVO.getOrigSource());
                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
                response.setSourceRequestId(messageVO.getSourceRequestId());
                response.setExternalLoanId(messageVO.getExternalLoanId());
                response.setAdvanceOfferId(messageVO.getAdvanceOfferId());
                response.setOfferName(messageVO.getOfferName());
                response.setAmount(messageVO.getAmount());
                response.setProcessingFee(messageVO.getProcessingFee());
                response.setTotalAmount(messageVO.getTotalAmount());
                response.setTransactionId(messageVO.getTransactionId());


                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Select Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Select Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Select Loan Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Select Loan Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }

        return response;
    }

    public ProjectionResponse selectLoanOfferResponse(ProjectionRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Loan Offer Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        ProjectionResponse response = new ProjectionResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setIdentityType(request.getIdentityType());
        messageVO.setOrigSource(request.getOrigSource());
        messageVO.setIdentityValue(request.getIdentityValue());
        messageVO.setOfferName(request.getOfferName());
        messageVO.setLoanAmount(request.getLoanAmount());
        messageVO.setUpToPeriod(request.getUpToPeriod());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoanOffer");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.projection();

            String responseXml = JSONUtil.getJSON(response);

            logger.info("[HOST] Mock Response of Loan Offer Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Loan Offer Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.selectLoanOffer(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Loan Offer Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setIdentityValue(messageVO.getIdentityValue());
                response.setIdentityType(messageVO.getIdentityType());
                response.setOrigSource(messageVO.getOrigSource());
                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
                response.setLoanOffersList(messageVO.getLoanOffersList());
                response.setPeriodsProjectionList(messageVO.getPeriodsProjectionList());


                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Loan Offer Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Loan Offer Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Loan Offer Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Loan Offer Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public LoanPaymentResponse loanPaymentResponse(LoanPaymentRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Loan Payment Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoanPaymentResponse response = new LoanPaymentResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setSourceRequestId(request.getSourceRequestId());
        messageVO.setAmount(request.getAmount());
        messageVO.setCurrencyCode(request.getCurrencyCode());
        messageVO.setReason(request.getReason());
        messageVO.setIdentityType(request.getIdentityType());
        messageVO.setOrigSource(request.getOrigSource());
        messageVO.setIdentityValue(request.getIdentityValue());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoanPayment");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.payment();

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Loan Payment Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Loan Payment Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.payLoan(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Loan Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setCode(messageVO.getCode());
                response.setMessage(messageVO.getMessage());
                response.setIdentityValue(messageVO.getIdentityValue());
                response.setIdentityType(messageVO.getIdentityType());
                response.setOrigSource(messageVO.getOrigSource());
                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
                response.setSourceRequestId(messageVO.getSourceRequestId());
                response.setTransactionId(messageVO.getTransactionId());
                response.setAmount(messageVO.getAmount());
                response.setProcessingFee(messageVO.getProcessingFee());
                response.setTotalAmount(messageVO.getTotalAmount());


                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Loan Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Loan Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Loan Payment Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Loan Payment Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public LoanCallBackResponse loanCallBackResponse(LoanCallBackRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Loan Call Back Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoanCallBackResponse response = new LoanCallBackResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setLoanEvent(request.getLoanEvent());
        messageVO.setLoanEventStatus(request.getLoanEventStatus());
        messageVO.setOrigSource(request.getOrigSource());
        messageVO.setInternalLoanId(request.getInternalLoanId());
        messageVO.setThirdPartyTransactionId(request.getThirdPartyTransactionId());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoanCallBack");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.callBack();

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Call Back Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Loan Call Back Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.loanCallBack(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Loan Call Back Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Loan Call Back Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Loan Call Back Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Loan Call Back Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Loan Call Back Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public LoansResponse outstandingResponse(LoansRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Outstanding Loan Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoansResponse response = new LoansResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setIdentityType(request.getIdentityType());
        messageVO.setOrigSource(request.getOrigSource());
        messageVO.setIdentityValue(request.getIdentityValue());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("Outstanding");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.loans();
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Outstanding Loan Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Outstanding Loan Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.outstandingLoanStatus(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Outstanding Loan Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setIdentityValue(messageVO.getIdentityValue());
                response.setIdentityType(messageVO.getIdentityType());
                response.setOrigSource(messageVO.getOrigSource());
                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
                response.setLoansPerStateList(messageVO.getLoansPerStates());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Outstanding Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Outstanding Loan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Outstanding Loan Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Outstanding Loan  Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }


//    public OutstandingResponse customerOutstandingLoanStatus(OutstandingRequest request) {
//
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Customer Outstanding Loan Status Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        OutstandingResponse response = new OutstandingResponse();
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setShaCnic(request.getCustomerId());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setIdentityType(request.getIdentityType());
//        messageVO.setOrigSource(request.getOrigSource());
//        messageVO.setIdentityValue(request.getIdentityValue());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//        messageVO.setReserved6(request.getReserved6());
//        messageVO.setReserved7(request.getReserved7());
//        messageVO.setReserved8(request.getReserved8());
//        messageVO.setReserved9(request.getReserved9());
//        messageVO.setReserved10(request.getReserved10());
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("CustomerOutstandingLoanStatus");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request
//        String requestXml = JSONUtil.getJSON(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
//            response = optasiaMock.outstanding();
//
//            String responseXml = JSONUtil.getJSON(response);
//            logger.info("[HOST] Mock Response of Loan Outstanding Request: " + responseXml);
//        } else {
//            // Call i8
//            try {
//                logger.info("[HOST] Sent Customer Outstanding Loan Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                messageVO = switchController.outstandingLoanStatus(messageVO);
//            } catch (Exception e) {
//
//                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//
//            }
//
//            // Set Response from i8
//            if (messageVO != null
//                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//                logger.info("[HOST] Customer Outstanding Loan Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setRrn(messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                response.setResponseDateTime(messageVO.getDateTime());
//                response.setIdentityValue(messageVO.getIdentityValue());
//                response.setIdentityType(messageVO.getIdentityType());
//                response.setOrigSource(messageVO.getOrigSource());
//                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
//                response.setCurrencyCode(messageVO.getCurrencyCode());
//                response.setTotalGross(messageVO.getTotalGross());
//                response.setTotalPrincipal(messageVO.getTotalPrincipal());
//                response.setTotalSetupFees(messageVO.getTotalSetupFees());
//                response.setTotalInterest(messageVO.getTotalInterest());
//                response.setTotalInterestVAT(messageVO.getTotalInterestVAT());
//                response.setTotalCharges(messageVO.getTotalCharges());
//                response.setTotalChargesVAT(messageVO.getTotalChargesVAT());
//                response.setTotalPendingLoans(messageVO.getTotalPendingLoans());
//                response.setTotalPendingRecoveries(messageVO.getTotalPendingRecoveries());
//
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//                logger.info("[HOST] Customer Outstanding Loan Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(messageVO.getResponseCode());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//            } else {
//                logger.info("[HOST] Customer Outstanding Loan Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//                response.setResponseDescription("Host Not In Reach");
//                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//
//                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//            }
//            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            response.setHashData(sha256hex);
//
//            long endTime = new Date().getTime(); // end time
//            long difference = endTime - startTime; // check different
//            logger.debug("[HOST] **** Customer Outstanding Loan Status Request PROCESSED IN ****: " + difference + " milliseconds");
//
//            //preparing request
//            String responseXml = JSONUtil.getJSON(response);
//            //Setting in logModel
//            logModel.setPduResponseHEX(responseXml);
//            logModel.setProcessedTime(difference);
//            updateTransactionInDB(logModel);
//        }
//        return response;
//    }

//    public LoanStatusResponse loanStatusResponse(LoanStatusRequest request) {
//
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Loan Status Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        LoanStatusResponse response = new LoanStatusResponse();
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setShaCnic(request.getCustomerId());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setIdentityType(request.getIdentityType());
//        messageVO.setOrigSource(request.getOrigSource());
//        messageVO.setIdentityValue(request.getIdentityValue());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//        messageVO.setReserved6(request.getReserved6());
//        messageVO.setReserved7(request.getReserved7());
//        messageVO.setReserved8(request.getReserved8());
//        messageVO.setReserved9(request.getReserved9());
//        messageVO.setReserved10(request.getReserved10());
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("LoanStatus");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request
//        String requestXml = JSONUtil.getJSON(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
//            response = optasiaMock.status();
//
//            String responseXml = JSONUtil.getJSON(response);
//            logger.info("[HOST] Mock Response of Loan Status Request: " + responseXml);
//        } else {
//            // Call i8
//            try {
//                logger.info("[HOST] Sent Loan Status Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                messageVO = switchController.loanStatus(messageVO);
//            } catch (Exception e) {
//
//                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//
//            }
//
//            // Set Response from i8
//            if (messageVO != null
//                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//                logger.info("[HOST] Loan Status Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setRrn(messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                response.setResponseDateTime(messageVO.getDateTime());
//                response.setIdentityValue(messageVO.getIdentityValue());
//                response.setIdentityType(messageVO.getIdentityType());
//                response.setOrigSource(messageVO.getOrigSource());
//                response.setReceivedTimestamp(messageVO.getReceivedTimestamp());
//                response.setInternalLoanId(messageVO.getInternalLoanId());
//                response.setExternalLoanId(messageVO.getExternalLoanId());
//                response.setLoanState(messageVO.getLoanState());
//                response.setLoanTimestamp(messageVO.getLoanTimestamp());
//                response.setLoanReason(messageVO.getLoanReason());
//                response.setOfferName(messageVO.getOfferName());
//                response.setCommodityType(messageVO.getCommodityType());
//                response.setCurrencyCode(messageVO.getCurrencyCode());
//                response.setPrincipalAmount(messageVO.getPrincipalAmount());
//                response.setSetupFees(messageVO.getSetupFees());
//                response.setLoanPlanId(messageVO.getLoanPlanId());
//                response.setLoanPlanName(messageVO.getLoanPlanName());
//                response.setLoanProductGroup(messageVO.getLoanProductGroup());
//                response.setMaturityDuration(messageVO.getMaturityDuration());
//                response.setRepaymentsCount(messageVO.getRepaymentsCount());
//                response.setGross(messageVO.getGross());
//                response.setPrincipal(messageVO.getPrincipal());
//                response.setInterest(messageVO.getInterest());
//                response.setInterestVAT(messageVO.getInterestVAT());
//                response.setCharges(messageVO.getCharges());
//                response.setChargesVAT(messageVO.getChargesVAT());
//                response.setTotalGross(messageVO.getTotalGross());
//                response.setTotalPrincipal(messageVO.getTotalPrincipal());
//                response.setTotalSetupFees(messageVO.getTotalSetupFees());
//                response.setTotalInterest(messageVO.getTotalInterest());
//                response.setTotalInterestVAT(messageVO.getTotalInterestVAT());
//                response.setTotalCharges(messageVO.getTotalCharges());
//                response.setTotalChargesVAT(messageVO.getTotalChargesVAT());
//                response.setTotalPendingRecoveries(messageVO.getTotalPendingRecoveries());
//                response.setCurrentPeriod(messageVO.getCurrentPeriod());
//                response.setDaysLeftInPeriod(messageVO.getDaysLeftInPeriod());
//                response.setNextPeriod(messageVO.getNextPeriod());
//
//
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//                logger.info("[HOST] Loan Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(messageVO.getResponseCode());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//            } else {
//                logger.info("[HOST] Loan Status Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//                response.setResponseDescription("Host Not In Reach");
//                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//
//                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//            }
//            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            response.setHashData(sha256hex);
//
//            long endTime = new Date().getTime(); // end time
//            long difference = endTime - startTime; // check different
//            logger.debug("[HOST] **** Loan Status Request PROCESSED IN ****: " + difference + " milliseconds");
//
//            //preparing request
//            String responseXml = JSONUtil.getJSON(response);
//            //Setting in logModel
//            logModel.setPduResponseHEX(responseXml);
//            logModel.setProcessedTime(difference);
//            updateTransactionInDB(logModel);
//        }
//        return response;
//    }

    public LoansHistoryResponse loansHistoryResponse(LoansHistoryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Loan History Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoansHistoryResponse response = new LoansHistoryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());

        messageVO.setFromDate(request.getFromDate());
        messageVO.setToDate(request.getToDate());

//        Date fromDate=new SimpleDateFormat("ddMMyyyy").parse(String.valueOf(request.getFromDate()));
//        messageVO.setFromDate(fromDate);
//        Date toDate=new SimpleDateFormat("ddMMyyyy").parse(String.valueOf(request.getToDate()));
//        messageVO.setToDate(toDate);

        messageVO.setReason(request.getReason());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoanHistory");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.loansHistoryResponse();

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Loan History Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Loan History Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.loanHistory(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Loan History Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setHistoryList(messageVO.getHistoryList());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Loan History Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Loan History Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Loan History Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Loan History Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public LoanPlanResponse loanPlanResponse(LoansPlanRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Loan Plan Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoanPlanResponse response = new LoanPlanResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setFromDate(request.getFromDate());
        messageVO.setToDate(request.getToDate());
        messageVO.setAmount(request.getAmount());
        messageVO.setReason(request.getReason());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("LoanPlan");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.loansPlanResponse();

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Loan Plan Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Loan Plan  Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = response.repaymentPlan(messageVO);
                String repaymentPlan = JSONUtil.getJSON(messageVO);
                logger.info("[HOST] Loan Plan Request Successful Response: " + repaymentPlan);

            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//                logger.info("[HOST] Loan Plan Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setDueDatePlans(messageVO.getDueDatePlanList());
                response.setLoanAmountList(messageVO.getLoanAmountList());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Loan Plan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Loan Plan Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Loan Plan Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Loan Plan Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public TransactionActiveResponse transactionActiveResponse(TransactionActiveRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Transaction Active Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        TransactionActiveResponse response = new TransactionActiveResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setFromDate(request.getFromDate());
        messageVO.setToDate(request.getToDate());
        messageVO.setReason(request.getReason());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("TransactionActive");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
            response = optasiaMock.transactionActiveResponse();

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] Mock Response of Transaction Active Request: " + responseXml);
        } else {
            // Call i8
            try {
                logger.info("[HOST] Sent Transaction Active Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                messageVO = switchController.transactionActive(messageVO);
            } catch (Exception e) {

                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

            }

            // Set Response from i8
            if (messageVO != null
                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                logger.info("[HOST] Transaction Active Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                response.setStatus(messageVO.getStatus());
                response.setIsStatus(messageVO.getStatusFlag());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
                logger.info("[HOST] Transaction Active Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Transaction Active Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Transaction Active Request PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request
            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] ****Transaction Active Response" + responseXml);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
        }
        return response;
    }

    public OptasiaSmsGenerationResponse optasiaSmsGenerationResponse(OptasiaSmsGenerationRequest request) {

        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Optasia SMS Generation Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        OptasiaSmsGenerationResponse response = new OptasiaSmsGenerationResponse();


        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setShaCnic(request.getCustomerId());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setMessage(request.getMessage());
        messageVO.setReserved1(request.getReserved1());
        messageVO.setReserved2(request.getReserved2());

        /*This is temporary solution to enable talotalk on behalf of Attique Butt.
        Should be reverted once otp optional implemented
        on APIGEE End*/
        //messageVO.setReserved1("1");

        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("OptasiaSMSGeneration");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Optasia Sent SMS Generation Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.cnicTo256(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

            SmsGenerationRequest smsGenerationRequest = new SmsGenerationRequest();
            smsGenerationRequest.setUserName(request.getUserName());
            smsGenerationRequest.setPassword(request.getPassword());
            smsGenerationRequest.setMobileNumber(messageVO.getMobileNo());
            smsGenerationRequest.setDateTime(request.getDateTime());
            smsGenerationRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            smsGenerationRequest.setChannelId(request.getChannelId());
            smsGenerationRequest.setTerminalId(request.getTerminalId());
            smsGenerationRequest.setMessage(request.getMessage());
            smsGenerationRequest.setReserved1(request.getReserved1());
            smsGenerationRequest.setReserved2(request.getReserved2());
            SmsGenerationResponse smsGenerationResponse = new SmsGenerationResponse();

            smsGenerationResponse = this.smsGeneration(smsGenerationRequest);

            if (smsGenerationResponse != null
                    && StringUtils.isNotEmpty(smsGenerationResponse.getResponseCode())
                    && smsGenerationResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                logger.info("[HOST] Optasia SMS Generation Request Successful from Micro Bank RRN: " + smsGenerationResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(smsGenerationResponse.getResponseDescription());
                response.setResponseDateTime(smsGenerationResponse.getResponseDateTime());
                response.setRrn(smsGenerationResponse.getRrn());

                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (smsGenerationResponse != null && StringUtils.isNotEmpty(smsGenerationResponse.getResponseCode())) {
                logger.info("[HOST] Optasia SMS Generation Request Unsuccessful from Micro Bank RRN: " + smsGenerationResponse.getRrn());
                response.setResponseCode(messageVO.getResponseCode());
                response.setResponseDescription(messageVO.getResponseCodeDescription());
                logModel.setResponseCode(messageVO.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Optasia SMS Generation Request Unsuccessful from Micro Bank RRN: " + smsGenerationResponse.getRrn());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }
            StringBuffer stringText = new StringBuffer(smsGenerationResponse.getResponseCode() + smsGenerationResponse.getResponseDescription());
            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] **** Optasia SMS Generation REQUEST PROCESSED IN ****: " + difference + " milliseconds");

            //preparing request XML
            String responseXml = JSONUtil.getJSON(response);
            //Setting in logModel
            logModel.setPduResponseHEX(responseXml);
            logModel.setProcessedTime(difference);
            updateTransactionInDB(logModel);
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Optasia SMS Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Optasia SMS Generation Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Optasia SMS Generation REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Optasia SMS Generation Response" + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
        return response;
    }

    public SimpleAccountOpeningResponse simpleAccountOpeningResponse(SimpleAccountOpeningRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Simple Account Opening Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        SimpleAccountOpeningResponse response = new SimpleAccountOpeningResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnic());
        messageVO.setCnicIssuanceDate(request.getCnicIssuanceDate());
        messageVO.setCnicExpiry(request.getCnicExpiryDate());
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
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
        logModel.setTransactionCode("SimpleAccountOpening");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
//            response = optasiaMock.simpleAccountOpeningResponse();
//
//            String responseXml = JSONUtil.getJSON(response);
//            logger.info("[HOST] Mock Response of Simple Account Opening Request: " + responseXml);
//        } else {
        // Call i8
        try {
            logger.info("[HOST] Sent Simple Account Opening Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.simpleAccountOpening(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Simple Account Opening Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());


            AccountOpeningRequest accountOpeningRequest = new AccountOpeningRequest();

            accountOpeningRequest.setUserName(request.getUserName());
            accountOpeningRequest.setPassword(request.getPassword());
            accountOpeningRequest.setCnic(request.getCnic());
            accountOpeningRequest.setDateTime(request.getDateTime());
            accountOpeningRequest.setRrn(messageVO.getRetrievalReferenceNumber());
            accountOpeningRequest.setMobileNumber(request.getMobileNumber());
            accountOpeningRequest.setConsumerName(StringUtils.trim(messageVO.getConsumerName()));
            accountOpeningRequest.setAccountTitle(StringUtils.trim(messageVO.getAccountTitle()));
            accountOpeningRequest.setBirthPlace(messageVO.getBirthPlace());
            accountOpeningRequest.setPresentAddress(messageVO.getPresentAddress());
            accountOpeningRequest.setCnicStatus(messageVO.getCnicStatus());
            accountOpeningRequest.setCnicExpiry(messageVO.getCnicExpiry());
            accountOpeningRequest.setDob(messageVO.getDateOfBirth());
            accountOpeningRequest.setFatherHusbandName(messageVO.getFatherHusbandName());
            accountOpeningRequest.setMotherMaiden(messageVO.getMotherMaiden());
            accountOpeningRequest.setGender(messageVO.getGender());
            accountOpeningRequest.setChannelId(messageVO.getChannelId());
            accountOpeningRequest.setAccountType(messageVO.getAccountType());
            accountOpeningRequest.setTrackingId(messageVO.getTrackingId());
            accountOpeningRequest.setCnicIssuanceDate(messageVO.getCnicIssuanceDate());
            accountOpeningRequest.setEmailAddress(messageVO.getEmailAddress());
            accountOpeningRequest.setMobileNetwork(messageVO.getCustomerMobileNetwork());
            accountOpeningRequest.setReserved(messageVO.getReserved1());
            accountOpeningRequest.setReserved2(messageVO.getReserved2());
            accountOpeningRequest.setReserved3(messageVO.getReserved3());

            AccountOpeningResponse accountOpeningResponse = new AccountOpeningResponse();
            logger.info("[HOST] Account Opening Request Sent to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            accountOpeningResponse = this.accountOpening(accountOpeningRequest);


            if (accountOpeningResponse != null
                    && StringUtils.isNotEmpty(accountOpeningResponse.getResponseCode())
                    && accountOpeningResponse.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

                logger.info("[HOST] Account Opening Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setRrn(accountOpeningResponse.getRrn());
                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
                response.setResponseDescription(accountOpeningResponse.getResponseDescription());
                response.setResponseDateTime(messageVO.getDateTime());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

            } else if (messageVO != null && StringUtils.isNotEmpty(accountOpeningResponse.getResponseCode())) {
                logger.info("[HOST] Account Opening Request Unsuccessful from Micro Bank RRN: " + accountOpeningResponse.getRrn());
                response.setResponseCode(accountOpeningResponse.getResponseCode());
                response.setResponseDescription(accountOpeningResponse.getResponseDescription());
                response.setRrn(accountOpeningResponse.getRrn());
                logModel.setResponseCode(accountOpeningResponse.getResponseCode());
                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
            } else {
                logger.info("[HOST] Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
                response.setResponseDescription("Host Not In Reach");
                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
            }

            StringBuilder stringText = new StringBuilder()
                    .append(response.getRrn())
                    .append(response.getResponseCode())
                    .append(response.getResponseDescription())
                    .append(response.getResponseDateTime());

            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
            response.setHashData(sha256hex);

            String responseXml = JSONUtil.getJSON(response);
            logger.info("[HOST] **** Account Opening Request **** " + responseXml);


            long endTime = new Date().getTime(); // end time
            long difference = endTime - startTime; // check different
            logger.debug("[HOST] ****Account Opening PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");
            return response;

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Simple Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Simple Account Opening Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Simple Account Opening Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] **** Simple Account Opening Request **** " + responseXml);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
//        }
        return response;
    }

//    public CustomerAnalyticsResponse customAnalyticsResponse(CustomerAnalyticsRequest request) {
//
//
//        long startTime = new Date().getTime(); // start time
//        WebServiceVO messageVO = new WebServiceVO();
//        String transactionKey = request.getDateTime() + request.getRrn();
//        messageVO.setRetrievalReferenceNumber(request.getRrn());
//        logger.info("[HOST] Customer Analytics Request Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());
//
//        transactionKey = request.getChannelId() + request.getRrn();
//
//        CustomerAnalyticsResponse response = new CustomerAnalyticsResponse();
//
//        messageVO.setUserName(request.getUserName());
//        messageVO.setCustomerPassword(request.getPassword());
//        messageVO.setShaCnic(request.getCustomerId());
//        messageVO.setDateTime(request.getDateTime());
//        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
//        messageVO.setChannelId(request.getChannelId());
//        messageVO.setTerminalId(request.getTerminalId());
//        messageVO.setName(request.getFullName());
//        messageVO.setDateOfBirth(request.getDateOfBirth());
//        messageVO.setPermanentCity(request.getCity());
//        messageVO.setLoanAmount(request.getLoanAmount());
//        messageVO.setGender(request.getGender());
//        messageVO.setPresentAddress(request.getCurrentAddress());
//        messageVO.setFatherHusbandName(request.getFatherHusbandName());
//        messageVO.setReserved1(request.getReserved1());
//        messageVO.setReserved2(request.getReserved2());
//        messageVO.setReserved3(request.getReserved3());
//        messageVO.setReserved4(request.getReserved4());
//        messageVO.setReserved5(request.getReserved5());
//        messageVO.setReserved6(request.getReserved6());
//        messageVO.setReserved7(request.getReserved7());
//        messageVO.setReserved8(request.getReserved8());
//        messageVO.setReserved9(request.getReserved9());
//        messageVO.setReserved10(request.getReserved10());
//
//        TransactionLogModel logModel = new TransactionLogModel();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//        Date txDateTime = new Date();
//        try {
//            txDateTime = dateFormat.parse(request.getDateTime());
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        logModel.setRetrievalRefNo(messageVO.getRetrievalReferenceNumber());
//        logModel.setTransactionDateTime(txDateTime);
//        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("CustomerAnalytics");
//        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
//        //preparing request
//        String requestXml = JSONUtil.getJSON(request);
//        //Setting in logModel
//        logModel.setPduRequestHEX(requestXml);
//
//        saveTransaction(logModel);
//
//        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock")) {
//            response = optasiaMock.analyticsResponse();
//
//            String responseXml = JSONUtil.getJSON(response);
//            logger.info("[HOST] Mock Response of Customer Analytics Request: " + responseXml);
//        } else {
//            // Call i8
//            try {
//                logger.info("[HOST] Sent Customer Analytics Request to Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                messageVO = switchController.customerAnalytics(messageVO);
//            } catch (Exception e) {
//
//                logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);
//
//            }
//
//            // Set Response from i8
//            if (messageVO != null
//                    && StringUtils.isNotEmpty(messageVO.getResponseCode())
//                    && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
//                logger.info("[HOST] Customer Analytics Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setRrn(messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                response.setResponseDateTime(messageVO.getDateTime());
//                response.setStatusCode(messageVO.getCode());
//                response.setMessageCode(messageVO.getMessageCode());
//                response.setMessage(messageVO.getMessage());
//                response.setReportDate(messageVO.getReportDate());
//                response.setReportTime(messageVO.getReportTime());
//                response.setName(messageVO.getName());
//                response.setCnic(messageVO.getCnicNo());
//                response.setCity(messageVO.getPresentCity());
//                response.setNoOfActiveAccounts(messageVO.getNoOfActiveAccounts());
//                response.setTotalOutstandingBalance(messageVO.getTotalOutstandingBalance());
//                response.setDob(messageVO.getDateOfBirth());
//                response.setPlus3024m(messageVO.getPlus3024m());
//                response.setPlus6024m(messageVO.getPlus6024m());
//                response.setPlus9024m(messageVO.getPlus9024m());
//                response.setPlus12024m(messageVO.getPlus12024m());
//                response.setPlus15024m(messageVO.getPlus15024m());
//                response.setPlus18024m(messageVO.getPlus18024m());
//                response.setDisclaimerText(messageVO.getDisclaimerText());
//                response.setRemarks(messageVO.getRemarks());
//
//
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//
//            } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
//                logger.info("[HOST] Customer Analytics Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//                response.setResponseCode(messageVO.getResponseCode());
//                response.setResponseDescription(messageVO.getResponseCodeDescription());
//                logModel.setResponseCode(messageVO.getResponseCode());
//                logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
//            } else {
//                logger.info("[HOST] Customer Analytics Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
//
//                response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//                response.setResponseDescription("Host Not In Reach");
//                logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
//
//                logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
//            }
//            StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
//            String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
//            response.setHashData(sha256hex);
//
//            long endTime = new Date().getTime(); // end time
//            long difference = endTime - startTime; // check different
//            logger.debug("[HOST] **** Customer Analytics Request PROCESSED IN ****: " + difference + " milliseconds");
//
//            //preparing request
//            String responseXml = JSONUtil.getJSON(response);
//            //Setting in logModel
//            logModel.setPduResponseHEX(responseXml);
//            logModel.setProcessedTime(difference);
//            updateTransactionInDB(logModel);
//        }
//        return response;
//    }

}
