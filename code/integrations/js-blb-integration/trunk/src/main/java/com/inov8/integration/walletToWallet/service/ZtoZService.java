package com.inov8.integration.walletToWallet.service;

import com.inov8.integration.middleware.dao.TransactionDAO;
import com.inov8.integration.middleware.dao.TransactionLogModel;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;

import com.inov8.integration.middleware.util.*;
import com.inov8.integration.walletToWallet.pdu.request.ZToZPaymentInquiryRequest;
import com.inov8.integration.walletToWallet.pdu.request.ZToZPaymentRequest;
import com.inov8.integration.walletToWallet.pdu.response.ZToZPaymentInquiryResponse;
import com.inov8.integration.walletToWallet.pdu.response.ZToZPaymentResponse;
import com.inov8.integration.webservice.controller.ZtoZSwitchController;
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
public class ZtoZService {
    private static Logger logger = LoggerFactory.getLogger(ZtoZService.class.getSimpleName());
    private static String I8_SCHEME = ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = ConfigReader.getInstance().getProperty("i8-ip", "127.0.0.1");
    private static String I8_PORT = (ConfigReader.getInstance().getProperty("i8-port", ""));
    private static String I8_PATH = (ConfigReader.getInstance().getProperty("ztoz.i8-path", ""));
    private static int READ_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-read-timeout", "55"));
    private static int CONNECTION_TIME_OUT = Integer.parseInt(ConfigReader.getInstance().getProperty("i8-connection-timeout", "10"));

    private static String loginPrivateKey = ConfigReader.getInstance().getProperty("login.authentication.privateKey", "");

    @Autowired
    TransactionDAO transactionDAO;
    private ZtoZSwitchController ztoZSwitchController = null;

    public ZtoZService() {
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
            if (ztoZSwitchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();

                executor.setConnectTimeout(CONNECTION_TIME_OUT * 1000);
                executor.setReadTimeout(READ_TIME_OUT * 1000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(ZtoZSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH);
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                ztoZSwitchController = (ZtoZSwitchController) httpInvokerProxyFactoryBean.getObject();
            }

        } catch (Exception e) {
            logger.error("ERROR Building I8 Switch Controller", e);

        }
    }

    private void updateTransactionInDB(TransactionLogModel trx) {
        this.transactionDAO.update(trx);
        logger.debug("[HOST] Transaction updated with RRN: " + trx.getRetrievalRefNo());
    }


    public ZToZPaymentInquiryResponse zToZPaymentInquiryResponse(ZToZPaymentInquiryRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Z to Z Payment Inquiry Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        ZToZPaymentInquiryResponse response = new ZToZPaymentInquiryResponse();

        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
        webServiceVO.setMobileNo(request.getMobileNumber());
        webServiceVO.setDateTime(request.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
        webServiceVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        webServiceVO.setTotalAmount(request.getAmount());
        webServiceVO.setProductID(request.getProductId());
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
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Z to Z Payment Inquiry Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());

            webServiceVO = ztoZSwitchController.ztoZInquiry(webServiceVO);


        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Z to Z Payment Inquiry Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
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
            response.setReceiverAccountTitle(webServiceVO.getRecieverAccountTilte());

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Z to Z Payment Inquiry Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Z to Z Payment Inquiry Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] ****Z to Z Payment Inquiry REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);


        return response;
    }

    public ZToZPaymentResponse zToZPaymentResponse(ZToZPaymentRequest request) {
        long startTime = new Date().getTime(); // start time
        WebServiceVO webServiceVO = new WebServiceVO();
        String transactionKey = request.getDateTime() + request.getRrn();
        webServiceVO.setRetrievalReferenceNumber(request.getRrn());
        logger.info("[HOST] Starting Processing Z to Z Payment Request RRN: " + webServiceVO.getRetrievalReferenceNumber());
        transactionKey = request.getChannelId() + request.getRrn();

        ZToZPaymentResponse response = new ZToZPaymentResponse();

        webServiceVO.setUserName(request.getUserName());
        webServiceVO.setCustomerPassword(request.getPassword());
//        webServiceVO.setMobilePin(request.getMpin());
        webServiceVO.setMobileNo(request.getMobileNumber());
        webServiceVO.setDateTime(request.getDateTime());
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        webServiceVO.setChannelId(request.getChannelId());
        webServiceVO.setTerminalId(request.getTerminalId());
//        webServiceVO.setOtpPin(request.getOtp());
        try {
            if (request.getOtp() != null && !request.getOtp().equals("")) {
                String text = request.getOtp();
                String otp = text.replaceAll("\\r|\\n", "");
                webServiceVO.setOtpPin(RSAEncryption.decrypt(otp, loginPrivateKey));
            } else if (request.getMpin() != null && !request.getMpin().equals("")) {
                String text = request.getMpin();
                String pin = text.replaceAll("\\r|\\n", "");
                webServiceVO.setMobilePin(RSAEncryption.decrypt(pin, loginPrivateKey));
//                webServiceVO.setMobilePin("1234");
            }


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        webServiceVO.setMobilePin("1234");
        webServiceVO.setReceiverMobileNumber(request.getReceiverMobileNumber());
        webServiceVO.setTotalAmount(request.getAmount());
        webServiceVO.setProductID(request.getProductId());
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
        String requestXml = JSONUtil.getJSON(request);
        //Setting in logModel
        logModel.setPduRequestHEX(requestXml);

//        saveTransaction(logModel);

        // Call i8
        try {
            logger.info("[HOST] Sent Z to Z Payment Request to Micro Bank RRN: " + I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + I8_PATH + " against RRN: " + webServiceVO.getRetrievalReferenceNumber());

            webServiceVO = ztoZSwitchController.ztoZPayment(webServiceVO);

        } catch (Exception e) {

            logger.error("[HOST] Internal Error While Sending Request RRN: " + webServiceVO.getRetrievalReferenceNumber(), e);

        }

        if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode()) && webServiceVO.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("[HOST] Z to Z Payment Request Successful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
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

        } else if (webServiceVO != null && StringUtils.isNotEmpty(webServiceVO.getResponseCode())) {
            logger.info("[HOST] Z to Z Payment Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());
            response.setResponseCode(webServiceVO.getResponseCode());
            response.setResponseDescription(webServiceVO.getResponseCodeDescription());
            logModel.setResponseCode(webServiceVO.getResponseCode());
            logModel.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
        } else {
            logger.info("[HOST] Z to Z Payment Request Unsuccessful from Micro Bank RRN: " + webServiceVO.getRetrievalReferenceNumber());

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
        logger.debug("[HOST] ****Z to Z Payment REQUEST PROCESSED IN ****: " + difference + " milliseconds");

        //preparing request XML
        String responseXml = JSONUtil.getJSON(response);
        //Setting in logModel
        logModel.setPduResponseHEX(responseXml);
        logModel.setProcessedTime(difference);
//        updateTransactionInDB(logModel);


        return response;
    }

}
