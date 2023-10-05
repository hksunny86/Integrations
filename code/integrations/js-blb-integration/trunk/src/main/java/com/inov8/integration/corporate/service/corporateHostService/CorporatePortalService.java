package com.inov8.integration.corporate.service.corporateHostService;

import com.inov8.integration.corporate.pdu.request.*;
import com.inov8.integration.corporate.pdu.response.*;
import com.inov8.integration.middleware.constants.CashInResponseEnum;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.request.TitleFetchRequest;
import com.inov8.integration.middleware.pdu.response.TitleFetchResponse;
import com.inov8.integration.middleware.util.*;
import com.inov8.integration.webservice.controller.CorporatePortalSwitchController;
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

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class CorporatePortalService {
    private static Logger logger = LoggerFactory.getLogger(CorporatePortalService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("corporate.i8-path", ""));
    private static String CORPORATE_I8_PATH = (ConfigReader.getInstance().getProperty("i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));

    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    @Autowired
    TransactionDAO transactionDAO;
    private CorporatePortalSwitchController corporatePortalSwitchController = null;
    private WebServiceSwitchController switchController = null;


    public CorporatePortalService() {
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
            if (corporatePortalSwitchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(CorporatePortalSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                corporatePortalSwitchController = (CorporatePortalSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
            if (switchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(WebServiceSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + CORPORATE_I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                switchController = (WebServiceSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    public LoginResponse loginResponse(LoginRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Corporate Portal Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        LoginResponse response = new LoginResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPassword());
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
        logModel.setTransactionCode("CorporateLogin");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Corporate Portal Login Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.login(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Corporate Portal Login Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Corporate Portal Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Corporate Portal Login Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Corporate Portal Login Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public AccountStateInquiryResponse accountStateInquiryResponse(AccountStateInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Account State Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountStateInquiryResponse response = new AccountStateInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnicNumber());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setSegmentCode(request.getSegmentId());
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
        logModel.setTransactionCode("AccountStateInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account State Inquiry Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.accountBlockUnblockInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account State Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCnic(messageVO.getCnicNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setAccountLevel(messageVO.getAccountLevel());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountStatus(messageVO.getAccountStatus());
            response.setCustomerMobile(messageVO.getMobileNo());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account State Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account State Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Account State Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public AccountStateResponse accountStateResponse(AccountStateRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Account State Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountStateResponse response = new AccountStateResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setCnicNo(request.getCnicNumber());
        messageVO.setAccountStatus(request.getAccountStatus());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setOtpPin(request.getOtp());
        messageVO.setMessage(request.getComments());
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
        logModel.setTransactionCode("AccountState");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account State Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.accountBlockUnblock(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account State Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account State Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account State Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Account State Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public MpinResetInquiryResponse mpinResetInquiryResponse(MpinResetInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getChannelId() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] MPIN Reset Inquiry State Starting Processing Request RRN: " + webServiceVO.getRetrievalReferenceNumber());

        MpinResetInquiryResponse response = new MpinResetInquiryResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setMobileNo(request.getMobileNo());
        webServiceVO.setPortalId(request.getPortalId());
        webServiceVO.setPortalPassword(request.getPassword());
        webServiceVO.setSegmentCode(request.getSegmentId());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
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
        logModel.setTransactionCode("MpinResetInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent MPIN Reset Inquiry Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
            webServiceVO = corporatePortalSwitchController.mpinResetInquiry(webServiceVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] MPIN Reset Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());
            response.setResponseDateTime(webServiceVO.getDateTime());
            response.setCnic(webServiceVO.getCnicNo());
            response.setCustomerName(webServiceVO.getConsumerName());
            response.setAccountLevel(webServiceVO.getAccountLevel());
            response.setAccountTitle(webServiceVO.getAccountTitle());
            response.setCustomerMobile(webServiceVO.getMobileNo());

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] MPIN Reset Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] MPIN Reset Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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

        long endTime = new Date().getTime();    // end time
        long difference = endTime - startTime;  // check different
        logger.debug("[HOST] ****MPIN RESET INQUIRY REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);

        return response;
    }

    public MpinResetResponse mpinResetResponse(MpinResetRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getChannelId() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] MPIN Reset State Starting Processing Request RRN: " + webServiceVO.getRetrievalReferenceNumber());

        MpinResetResponse response = new MpinResetResponse();

        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
        webServiceVO.setOldMpin(EncryptionUtil.encrypt(request.getOldMpin()));
//        try {
//            String text;
//            String oldMpin;
//            text = request.getOldMpin();
//            oldMpin = text.replaceAll("\\r|\\n", "");
//            String oldPin = (RSAEncryption.decrypt(oldMpin, loginPrivateKey));
//            webServiceVO.setOldMpin(EncryptionUtil.encrypt(oldPin));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        webServiceVO.setMobilePin(EncryptionUtil.encrypt(request.getNewMpin()));
//        try {
//            String text;
//            String newPin;
//            text = request.getNewMpin();
//            newPin = text.replaceAll("\\r|\\n", "");
//            String newMpin = (RSAEncryption.decrypt(newPin, loginPrivateKey));
//            webServiceVO.setMobilePin(EncryptionUtil.encrypt(newMpin));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(request.getConfirmMpin()));
//        try {
//            String text;
//            String confirmMPin;
//            text = request.getConfirmMpin();
//            confirmMPin = text.replaceAll("\\r|\\n", "");
//            String confirmPin = (RSAEncryption.decrypt(confirmMPin, loginPrivateKey));
//            webServiceVO.setConfirmMpin(EncryptionUtil.encrypt(confirmPin));
//        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        webServiceVO.setPortalId(request.getPortalId());
        webServiceVO.setPortalPassword(request.getPassword());
        webServiceVO.setMobileNo(request.getMobileNo());
        webServiceVO.setOtpPin(request.getOtp());
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
        logModel.setTransactionCode("MpinReset");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent MPIN Reset Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());
            webServiceVO = corporatePortalSwitchController.mpinReset(webServiceVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] MPIN Reset Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            response.setRrn(webServiceVO.getRetrievalReferenceNumber());

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] MPIN Reset Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] MPIN Reset Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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

        long endTime = new Date().getTime();    // end time
        long difference = endTime - startTime;  // check different
        logger.debug("[HOST] ****MPIN RESET REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);

        return response;
    }

    public AccountStatementInquiryResponse accountStatementInquiryResponse(AccountStatementInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Account Statement Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountStatementInquiryResponse response = new AccountStatementInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setSegmentCode(request.getSegmentId());
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
        logModel.setTransactionCode("AccountStatementInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account Statement Inquiry Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.customerAccountStatementInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Statement Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCnic(messageVO.getCnicNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setAccountLevel(messageVO.getAccountLevel());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setCustomerMobile(messageVO.getMobileNo());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Statement Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Statement Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Account Statement Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public AccountStatementResponse accountStatementResponse(AccountStatementRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Account Statement Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountStatementResponse response = new AccountStatementResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setOtpPin(request.getOtp());
        messageVO.setFromDate(request.getFromDate());
        messageVO.setToDate(request.getToDate());
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
        logModel.setTransactionCode("AccountStatement");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account Statement Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.customerAccountStatement(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Account Statement Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setAccountStatementList(messageVO.getAccountStatementList());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Statement Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Statement Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Account Statement Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public DeviceVerificationInquiryResponse deviceVerificationInquiryResponse(DeviceVerificationInquiryRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Device Verification Inquiry Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        DeviceVerificationInquiryResponse response = new DeviceVerificationInquiryResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setSegmentCode(request.getSegmentId());
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
        logModel.setTransactionCode("DeviceVerificationInquiry");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Device Verification Inquiry Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.deviceVerificationInquiry(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Device Verification Inquiry Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setCnic(messageVO.getCnicNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setAccountLevel(messageVO.getAccountLevel());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setCustomerMobile(messageVO.getMobileNo());
            response.setCustomerDeviceVerificationList(messageVO.getCustomerDeviceVerificationList());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Device Verification Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Device Verification Inquiry Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Device Verification Inquiry Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public DeviceVerificationResponse deviceVerificationResponse(DeviceVerificationRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Device Verification Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        DeviceVerificationResponse response = new DeviceVerificationResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setOtpPin(request.getOtp());
        messageVO.setId(request.getId());
        messageVO.setDeviceName(request.getDeviceName());
        messageVO.setApprovalStatus(request.getApprovalStatus());
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
        logModel.setTransactionCode("DeviceVerification");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Device Verification Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.deviceVerification(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Device Verification Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Device Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Device Verification Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Device Verification Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public CorporateTitleFetchResponse corporateTitleFetchResponse(CorporateTitleFetchRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Title Fetch Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CorporateTitleFetchResponse response = new CorporateTitleFetchResponse();

        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
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
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Title Fetch Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
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
            response.setResponseDateTime(messageVO.getDateTime());
            response.setBalance(messageVO.getBalance());
            response.setRemainingCreditLimit(messageVO.getRemainingCreditLimit());
            response.setRemainingDebitLimit(messageVO.getRemainingDebitLimit());
            response.setConsumedVelocity(messageVO.getConsumedVelocity());
            response.setCustomerMobile(messageVO.getMobileNo());
            response.setCnic(messageVO.getCnicNo());
            response.setCustomerName(messageVO.getConsumerName());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountLevel(messageVO.getAccountLevel());
//            if (StringUtils.isNotEmpty(messageVO.getResponseContentXML()))
//                XMLUtil.populateFromResponse(response, messageVO.getResponseContentXML(), CashInResponseEnum.values());

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
                .append(response.getResponseDateTime())
                .append(response.getCnic())
                .append(response.getCustomerName())
                .append(response.getAccountTitle())
                .append(response.getBalance())
                .append(response.getRemainingCreditLimit())
                .append(response.getRemainingDebitLimit())
                .append(response.getConsumedVelocity());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Title Fetch REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);

        return response;
    }

    public CoreToGlAccountResponse coreToGlAccountResponse(CoreToGlAccountRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Core To GL Account Request RRN: " + messageVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        CoreToGlAccountResponse response = new CoreToGlAccountResponse();

        messageVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setCoreAccountId(request.getCoreAccountNo());
        messageVO.setGLAccountNo1(request.getGlAccountNo());
        messageVO.setProductID(request.getProductId());
        messageVO.setTransactionAmount(request.getTransactionAmount());
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
        logModel.setTransactionCode("CoreToGlAccount");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request XML
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Core To GL Account Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());
            messageVO = corporatePortalSwitchController.coreToGl(messageVO);

        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }
        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Core To GL Account Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setTransactionId(messageVO.getTransactionId());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Core To GL Account Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Core To GL Account Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuilder stringText = new StringBuilder()
                .append(response.getResponseCode())
                .append(response.getResponseDescription())
                .append(response.getRrn())
                .append(response.getResponseDateTime())
                .append(response.getTransactionId());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] ****Core To GL Account REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);

        return response;
    }

    public RegenerateLoginPinResponse regenerateLoginPinResponse(RegenerateLoginPinRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Regenerate Login Pin Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        RegenerateLoginPinResponse response = new RegenerateLoginPinResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setSegmentCode(request.getSegmentId());
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
        logModel.setTransactionCode("RegenerateLoginPin");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Regenerate Login Pin Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.regenerateLoginPin(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Regenerate Login Pin Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Regenerate Login Pin Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Regenerate Login Pin Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Regenerate Login Pin Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

    public ChangePinResponse changePinResponse(ChangePinRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Change Financial Pin Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        ChangePinResponse response = new ChangePinResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNo());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
        messageVO.setPortalId(request.getPortalId());
        messageVO.setPortalPassword(request.getPortalPassword());
        messageVO.setSegmentCode(request.getSegmentId());
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
        logModel.setTransactionCode("ChangeFinancialPin");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        //saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Change Financial Pin Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = corporatePortalSwitchController.changePin(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Change Financial Pin Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Change Financial Pin Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Change Financial Pin Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        StringBuffer stringText = new StringBuffer(response.getResponseCode() + response.getResponseDescription());
        String sha256hex = DigestUtils.sha256Hex(stringText.toString());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Change Financial Pin Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }

}
