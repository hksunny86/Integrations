package com.inov8.integration.petroPocket.service;

import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.response.BalanceInquiryResponse;
import com.inov8.integration.middleware.util.*;
import com.inov8.integration.petroPocket.pdu.request.*;
import com.inov8.integration.petroPocket.pdu.response.*;
import com.inov8.integration.webservice.controller.PetroSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PetroPocketService {
    private static Logger logger = LoggerFactory.getLogger(PetroPocketService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("petro.i8-path", ""));
    private static String PETRO_I8_PATH = (ConfigReader.getInstance().getProperty("petro.i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));

    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    @Autowired
    TransactionDAO transactionDAO;

    private PetroSwitchController switchController = null;


    public PetroPocketService() {
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
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private TransactionLogModel saveTransaction(TransactionLogModel transaction) {
        try {
            this.transactionDAO.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[HOST] Transaction saved with RRN: " + transaction.getRetrievalRefNo());
        return transaction;
    }

    private void buildSwitch() {
        try {
            if (switchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(PetroSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + PETRO_I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                switchController = (PetroSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    public PetroInquiryResponse petroInquiryResponse(PetroInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Petro Inquiry  Payment  Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        PetroInquiryResponse response = new PetroInquiryResponse();


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
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Petro Inquiry Payment Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.petroInquiry(messageVO);
        } catch (Exception e) {
            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1) {
                        messageVO.setResponseCode("58");
                        messageVO.setResponseCodeDescription("Transaction Time Out");
                    }
                }
            }
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Petro Inquiry Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

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
            logger.info("[HOST] Petro Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Petro Inquiry Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
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
        logger.debug("[HOST] ****Petro Inquiry PAYMENT REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXML = JSONUtil.getJSON(response);

        //Setting in logModel
        logModel.setPduResponseHEX(responseXML);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
        return response;
    }

    public PetroPaymentResponse petroPaymentResponse(PetroPaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        String transactionKey = request.getDateTime() + request.getRrn();
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Petro Payment Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        PetroPaymentResponse response = new PetroPaymentResponse();


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
        logModel.setTransactionCode("PetroToWallet");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Petro Payment Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.petroPayment(messageVO);
        } catch (Exception e) {
            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1) {
                        messageVO.setResponseCode("58");
                        messageVO.setResponseCodeDescription("Transaction Time Out");
                    }
                }
            }
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Petro Payment Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

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
            logger.info("[HOST] Petro Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Petro Payment Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] ****Petro Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
        return response;
    }

    public PetroWalletToWalletInquiryResponse petroWalletToWalletInquiryResponse(PetroWalletToWalletInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Petro Wallet to Wallet Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        PetroWalletToWalletInquiryResponse response = new PetroWalletToWalletInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setMobileNo(request.getMobileNumber());
        webServiceVO.setDateTime(request.getDateTime());
        webServiceVO.setTotalAmount(request.getAmount());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
        webServiceVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        webServiceVO.setReserved1(request.getReserved1());
        webServiceVO.setReserved2(request.getReserved2());
        webServiceVO.setReserved3(request.getReserved3());
        webServiceVO.setReserved4(request.getReserved4());
        webServiceVO.setReserved5(request.getReserved5());
        TransactionLogModel logModel = new TransactionLogModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
        Date txDateTime = new Date();
        try {
            txDateTime = dateFormat.parse(request.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logModel.setRetrievalRefNo(webServiceVO.getRetrievalReferenceNumber());
        logModel.setTransactionDateTime(txDateTime);
        logModel.setChannelId(request.getChannelId());
//        logModel.setTransactionCode("CashOut");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = XMLUtil.convertToXML(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Wallet Payment Inquiry Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.petroWalletToWalletInquiry(webServiceVO);
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

            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1) {
                        webServiceVO.setResponseCode("58");
                        webServiceVO.setResponseCodeDescription("Transaction Time Out");
                    }
                }
            }
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
//        updateTransactionInDB(logModel);


        return response;
    }

    public PetroWalletToWalletPaymentResponse petroWalletToWalletPaymentResponse(PetroWalletToWalletPaymentRequest walletToWalletPaymentRequest) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = walletToWalletPaymentRequest.getDateTime() + walletToWalletPaymentRequest.getRrn();
        webServiceVO.setRetrievalReferenceNumber(walletToWalletPaymentRequest.getRrn());
        logger.info("[HOST] Starting Processing Petro Wallet to Wallet Payment Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = walletToWalletPaymentRequest.getChannelId() + walletToWalletPaymentRequest.getRrn();

        PetroWalletToWalletPaymentResponse response = new PetroWalletToWalletPaymentResponse();

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
//                webServiceVO.setMobilePin("1234");
            }


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        webServiceVO.setMobilePin("1234");
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

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Wallet To Wallet Payment Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
//            String input = "1";

//            if (input.equals("0")) {
            webServiceVO = switchController.petroWalletToWalletPayment(webServiceVO);
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

            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1) {
                        webServiceVO.setResponseCode("58");
                        webServiceVO.setResponseCodeDescription("Transaction Time Out");
                    }
                }
            }
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
            logger.info("[HOST] Petro Wallet to Wallet Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Petro Wallet to Wallet Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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
//        updateTransactionInDB(logModel);


        return response;
    }

    public PetroBalanceInquiryResponse petroBalanceInquiry(PetroBalanceInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Petro Balance Inquiry Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        PetroBalanceInquiryResponse response = new PetroBalanceInquiryResponse();


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
        logModel.setTransactionCode("PetroBalanceInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Petro Balance Inquiry Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.petroBalanceInquiry(messageVO);
        } catch (Exception e) {
            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1) {
                        messageVO.setResponseCode("58");
                        messageVO.setResponseCodeDescription("Transaction Time Out");
                    }
                }
            }
            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Petro Balance Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setBalance(messageVO.getBalance());
            response.setRegistrationTypeCode(messageVO.getRegistrationTypeCode());
            response.setAccountLevelCode(messageVO.getAccountLevelCode());
            response.setAccountStatusCode(messageVO.getAccountStatusCode());
            response.setAccountTypeCode(messageVO.getAccountTypeCode());
            response.setAccountNatureCode(messageVO.getAccountNatureCode());
            response.setCurrencyCode(messageVO.getCurrencyCode());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Petro Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Petro Balance Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription() + response.getBalance());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Petro BALANCE INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
        return response;
    }

}
