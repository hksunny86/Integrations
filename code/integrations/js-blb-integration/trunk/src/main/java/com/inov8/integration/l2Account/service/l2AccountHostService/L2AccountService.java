package com.inov8.integration.l2Account.service.l2AccountHostService;

import com.inov8.integration.l2Account.pdu.request.L2AccountFieldsRequest;
import com.inov8.integration.l2Account.pdu.request.L2AccountRequest;
import com.inov8.integration.l2Account.pdu.response.L2AccountFieldsResponse;
import com.inov8.integration.l2Account.pdu.response.L2AccountResponse;
import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.webservice.controller.L2AccountSwitchController;
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
public class L2AccountService {
    private static Logger logger = LoggerFactory.getLogger(L2AccountService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("l2Account.i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));

    @Autowired
    TransactionDAO transactionDAO;
    private L2AccountSwitchController l2AccountSwitchController = null;


    public L2AccountService() {
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
            if (l2AccountSwitchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(L2AccountSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                l2AccountSwitchController = (L2AccountSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }

    public L2AccountResponse l2AccountResponse(L2AccountRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Level 2 Accounts Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        L2AccountResponse response = new L2AccountResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        logModel.setTransactionCode("L2Accounts");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Level 2 Accounts Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = l2AccountSwitchController.l2Account(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Level 2 Accounts Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setL2AccountList(messageVO.getL2AccountList());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Level 2 Accounts Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] L2 Accounts Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Level 2 Accounts Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
//        }
        return response;
    }

    public L2AccountFieldsResponse l2AccountFieldsResponse(L2AccountFieldsRequest request) {


        long startTime = new Date().getTime(); // start time
        WebServiceVO messageVO = new WebServiceVO();
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Level 2 Account Fields Starting Processing Request RRN: " + messageVO.getRetrievalReferenceNumber());

        L2AccountFieldsResponse response = new L2AccountFieldsResponse();

        messageVO.setUserName(request.getUserName());
        messageVO.setCustomerPassword(request.getPassword());
        messageVO.setMobileNo(request.getMobileNumber());
        messageVO.setDateTime(request.getDateTime());
        messageVO.setRetrievalReferenceNumber(request.getRrn());
        messageVO.setChannelId(request.getChannelId());
        messageVO.setTerminalId(request.getTerminalId());
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
        logModel.setTransactionCode("L2AccountFields");
        logModel.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
        //preparing request
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Level 2 Account Fields Request to Micro Bank " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + messageVO.getRetrievalReferenceNumber());

            messageVO = l2AccountSwitchController.l2AccountFields(messageVO);
        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + messageVO.getRetrievalReferenceNumber(), e);

        }

        // Set Response from i8
        if (messageVO != null
                && StringUtils.isNotEmpty(messageVO.getResponseCode())
                && messageVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Level 2 Account Fields Request Successful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());

            response.setRrn(messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            response.setResponseDateTime(messageVO.getDateTime());
            response.setL2AccountFieldsList(messageVO.getL2AccountFieldsList());

            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());

        } else if (messageVO != null && StringUtils.isNotEmpty(messageVO.getResponseCode())) {
            logger.info("[HOST] Level 2 Account Fields Request Unsuccessful from Micro Bank RRN: " + messageVO.getRetrievalReferenceNumber());
            response.setResponseCode(messageVO.getResponseCode());
            response.setResponseDescription(messageVO.getResponseCodeDescription());
            logModel.setResponseCode(messageVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] L2 Account Fields Request Unsuccessful from Micro Bank RRN: " + Objects.requireNonNull(messageVO).getRetrievalReferenceNumber());

            response.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());
            response.setResponseDescription("Host Not In Reach");
            logModel.setResponseCode(ResponseCodeEnum.HOST_NOT_PROCESSING.getValue());

            logModel.setStatus(TransactionStatus.REJECTED.getValue().longValue());
        }
        String sha256hex = DigestUtils.sha256Hex(response.getResponseCode() + response.getResponseDescription());
        response.setHashData(sha256hex);

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("[HOST] **** Level 2 Account Fields Request PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
        updateTransactionInDB(logModel);
//        }
        return response;
    }


}
