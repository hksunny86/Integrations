package com.inov8.integration.internationalRemittance.service;

import com.inov8.integration.internationalRemittance.pdu.request.CoreToWalletCreditRequest;
import com.inov8.integration.internationalRemittance.pdu.request.TitleFetchRequestV2;
import com.inov8.integration.internationalRemittance.pdu.response.CoreToWalletCreditResponse;
import com.inov8.integration.internationalRemittance.pdu.response.TitleFetchResponseV2;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.request.AdviceReversalRequest;
import com.inov8.integration.middleware.pdu.response.AdviceReversalResponse;
import com.inov8.integration.middleware.service.hostService.HostIntegrationService;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.XMLUtil;
import com.inov8.integration.webservice.controller.InternationalRemittanceController;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class InternationalRemittanceService {

    private static Logger logger = LoggerFactory.getLogger(HostIntegrationService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("internationalRemittance.i8-path", ""));

    private static String MB_I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String MB_I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String MB_I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String MB_I8_PATH = (ConfigReader.getInstance().getProperty("i8-path", ""));

    private static String OPTASIA_I8_SERVER = ConfigReader.getInstance().getProperty("optasia-i8-ip", "127.0.0.1");
    private static String OPTASIA_I8_PORT = (ConfigReader.getInstance().getProperty("optasia-i8-port", ""));
    private static String I8_QUEUE_SERVER = ConfigReader.getInstance().getProperty("i8-queue-ip", "127.0.0.1");
    private static String I8_QUEUE_PORT = (ConfigReader.getInstance().getProperty("i8-queue-port", ""));
    private static String OPTASIA_I8_PATH = (ConfigReader.getInstance().getProperty("i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));
    private static String publicKey = ConfigReader.getInstance().getProperty("publicKey", "");
    private static String privateKey = ConfigReader.getInstance().getProperty("privateKey", "");

    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    private InternationalRemittanceController remittanceController;
    private WebServiceSwitchController switchController = null;

    public InternationalRemittanceService() {
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

    private void buildSwitch() {
        try {
            if (remittanceController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(InternationalRemittanceController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                remittanceController = (InternationalRemittanceController) httpInvokerProxyFactoryBean.getObject();
            }
            if (switchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(WebServiceSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(MB_I8_SCHEME + "://" + MB_I8_SERVER + ":" + MB_I8_PORT + MB_I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                switchController = (WebServiceSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    public CoreToWalletCreditResponse CoreToWalletCredit(CoreToWalletCreditRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Core to Wallet Credit V2 Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CoreToWalletCreditResponse response = new CoreToWalletCreditResponse();

        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setAccountNo1(request.getAccountNo());
        webServiceVO.setDateTime(request.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
        webServiceVO.setMobilePin(request.getPin());
        webServiceVO.setProductID(request.getProductId());
        webServiceVO.setPinType(request.getPinType());
        webServiceVO.setStan(request.getStan());
        webServiceVO.setSourceBankImd(request.getBankIMD());
        webServiceVO.setTransactionAmount(request.getTransactionAmount());
        webServiceVO.setReserved1(request.getReserved1());
        webServiceVO.setReserved2(request.getReserved2());
        webServiceVO.setReserved3(request.getReserved3());
        webServiceVO.setReserved4(request.getReserved4());
        webServiceVO.setReserved5(request.getReserved5());
        webServiceVO.setReserved1(request.getReserved6());
        webServiceVO.setReserved2(request.getReserved7());
        webServiceVO.setReserved3(request.getReserved8());
        webServiceVO.setReserved4(request.getReserved9());
        webServiceVO.setReserved5(request.getReserved10());

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
        logModel.setTransactionCode("Core to Wallet Credit V2");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        // Call i8
        try {
            logger.info("[HOST] Sent Core to Wallet Credit V2 Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
            webServiceVO = remittanceController.CoreToWalletCredit(webServiceVO);

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
        // Set Response from i8
        if (webServiceVO != null
                && StringUtils.isNotEmpty(webServiceVO.getResponseCode())
                && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Core to Wallet Credit V2 Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setResponseDateTime(webServiceVO.getDateTime());
            response.setTransactionId(webServiceVO.getTransactionId());
            response.setComissionAmount(webServiceVO.getCommissionAmount());
            response.setTransactionAmount(webServiceVO.getTransactionAmount());
            response.setTotalTransactionAmount(webServiceVO.getTotalAmount());

            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Core to Wallet Credit V2 Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Core to Wallet Credit V2 Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] **** Core to Wallet Credit V2 REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);

        return response;
    }

    public TitleFetchResponseV2 TitleFetchResponseV2(TitleFetchRequestV2 request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Title fetch V2 Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        TitleFetchResponseV2 response = new TitleFetchResponseV2();

        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setAccountNo1(request.getAccountNo());
        webServiceVO.setDateTime(request.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
        webServiceVO.setPaymentMode(request.getPaymentMode());
        webServiceVO.setSegmentCode(request.getSegmentCode());
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
        logModel.setTransactionCode("Title fetch V2");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        // Call i8
        try {
            logger.info("[HOST] Sent Title Fetch Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
            webServiceVO = remittanceController.TitleFetchV2(webServiceVO);

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
        // Set Response from i8
        if (webServiceVO != null
                && StringUtils.isNotEmpty(webServiceVO.getResponseCode())
                && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Title Fetch V2 Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setDateTime(webServiceVO.getDateTime());
            response.setCnic(webServiceVO.getCnicNo());
            response.setCustomerName(webServiceVO.getCustomerName());
            response.setMobileNumber(webServiceVO.getMobileNo());
            response.setIban(webServiceVO.getBenificieryIban());
            response.setAccountLevel(webServiceVO.getAccountLevel());
            response.setAccountStatus(webServiceVO.getAccountStatus());
            response.setBalance(webServiceVO.getBalance());
            response.setAccounts(webServiceVO.getTitleFetchAccountResponse());

            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Title Fetch V2 Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Title Fetch V2 Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] ****Title Fetch V2 REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);

        return response;
    }

    public AdviceReversalResponse adviseReversal(AdviceReversalRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Advise Reversal Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();
        AdviceReversalResponse response = new AdviceReversalResponse();

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
        logModel.setTransactionCode("AdviseReversal");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);
        // Call i8
        try {
            logger.info("[HOST] Sent Advise Reversal Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = switchController.paymentReversal(messageVO);
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
            logger.info("[HOST] Advise Reversal Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Advise Reversal Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Advise Reversal Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
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
        logger.debug("[HOST] **** ADVISE REVERSAL REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = XMLUtil.convertToXML(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);
        return response;
    }


}
