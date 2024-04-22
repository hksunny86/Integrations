package com.inov8.integration.smt.service;

// @Created On 4/17/2024 : Wednesday
// @Created By muhammad.aqeel

import com.inov8.integration.l2Account.pdu.request.L2AccountRequest;
import com.inov8.integration.l2Account.pdu.response.L2AccountResponse;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.response.AccountInfoResponse;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.smt.pdu.request.AccountInfoSMTRequest;
import com.inov8.integration.smt.pdu.response.AccountInfoSMTResponse;
import com.inov8.integration.webservice.controller.L2AccountSwitchController;
import com.inov8.integration.webservice.controller.SMTSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class SMTService {
    private static Logger logger = LoggerFactory.getLogger(SMTService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("smt.i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));
    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    @Autowired
    TransactionDAO transactionDAO;
    SMTSwitchController smtSwitchController;

    public SMTService() {
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

    private void buildSwitch() {
        try {
            if (smtSwitchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(SMTSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                smtSwitchController = (SMTSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }
    public AccountInfoSMTResponse accountInfoSMTResponse(AccountInfoSMTRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Account Info SMT Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        transactionKey = request.getChannelId() + request.getRrn();

        AccountInfoSMTResponse response = new AccountInfoSMTResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
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
        logModel.setTransactionCode("AccountInfoSMT");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Account Info SMT Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = smtSwitchController.accountInfoSmt(messageVO);
        } catch (Exception e) {
            if (e instanceof RemoteAccessException) {
                if (!(e instanceof RemoteConnectFailureException)) {

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    int statusCode = stackTrace.indexOf("status code");
                    if (statusCode == -1){
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
            logger.info("[HOST] Account Info SMT Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setAccountTitle(messageVO.getAccountTitle());
            response.setAccountNumber(messageVO.getAccountNo1());
            response.setRegistrationTypeCode(messageVO.getRegistrationTypeCode());
            response.setAccountLevelCode(messageVO.getAccountLevelCode());
            response.setAccountStatusCode(messageVO.getAccountStatusCode());
            response.setAccountTypeCode(messageVO.getAccountTypeCode());
            response.setAccountNatureCode(messageVO.getAccountNatureCode());
            response.setCurrencyCode(messageVO.getCurrencyCode());
            response.setCnic(messageVO.getCnicNo());
            response.setName(messageVO.getName());
            response.setSegment(messageVO.getSegmentName());
            response.setEmail(messageVO.getEmailAddress());
            response.setChannel(messageVO.getChannelId());
            response.setGlCodeCombination("");
            response.setDateOfBirth(messageVO.getDateOfBirth());
            response.setGender(messageVO.getGender());
            response.setAddress(messageVO.getPresentAddress());
            response.setCountry(messageVO.getCountry());
            response.setCity(messageVO.getCity());
            response.setProvinceState(messageVO.getProvinceState());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Account Info SMT Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Account Info SMT Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] **** Account Info SMT Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        logger.info("[HOST] ****Account Info SMT Response" + Objects.requireNonNull(responseXml).replaceAll(System.getProperty("line.separator"), " "));
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        //updateTransactionInDB(logModel);
//        }
        return response;
    }
}
