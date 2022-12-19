package com.inov8.integration.middleware.service;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;


import com.inov8.integration.host.HostTransactionResponseBuilder;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.request.CashWithdrawalRequest;
import com.inov8.integration.middleware.pdu.response.BalanceInquiryResponse;
import com.inov8.integration.middleware.pdu.response.CashWithDrawalResponse;
import com.inov8.integration.middleware.pdu.response.MiniStatementResponse;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.middleware.util.MiniXMLUtil;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.vo.Transaction;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.stereotype.Service;

import com.inov8.integration.ibft.controller.HostTransactionController;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.vo.MiddlewareMessageVO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class HostIntegrationService {
    private static Logger logger = LoggerFactory.getLogger(HostIntegrationService.class.getSimpleName());
    private HostTransactionController hostTransactionController;
    private WebServiceSwitchController webServiceSwitchController;

    private static String I8_SCHEME = "http";
//            ConfigReader.getInstance().getProperty("i8-scheme", "http");
    private static String I8_SERVER = "10.220.2.25";
//        ConfigReader.getInstance().getProperty("i8-ip", "127.29.12.196");
    private static int I8_PORT = 8080;
//        Integer.parseInt(ConfigReader.getInstance().getProperty("i8-port", "8080"));

    static {
        disableSslVerification();
    }


    public HostIntegrationService() {
        buildSwtich();
    }

    public void titleFetchBBCommand(MiddlewareMessageVO vo) {
        buildSwtich();

        logger.info("I8 Title Fetch Request: " + vo.toString());

        MiddlewareMessageVO responseVO = (MiddlewareMessageVO) hostTransactionController.titleFetch(vo);

        if (responseVO != null) {
            logger.info("I8 Title Fetch Response: " + responseVO.toString());
            vo.setResponseCode(responseVO.getResponseCode());
            vo.setAccountBalance(responseVO.getAccountBalance());
            vo.setAccountTitle(responseVO.getAccountTitle());
        } else {
            logger.info("I8 Title Fetch No Response.");
        }
    }

    public void funTransferBBAdviceCommand(MiddlewareMessageVO vo) {
        buildSwtich();

        logger.info("I8 Fund Transfer BB Advice Request: " + vo.toString());

        MiddlewareMessageVO responseVO = (MiddlewareMessageVO) hostTransactionController.creditAdvice(vo);

        if (responseVO != null) {
            logger.info("I8 Fund Transfer BB Advice Response: " + responseVO.toString());
            vo.setResponseCode(responseVO.getResponseCode());
        } else {
            logger.info("I8 und Transfer BB Advice No Response.");
        }
    }

    public void coreToWalletAdviceCommand(MiddlewareMessageVO vo) {
        buildSwtich();

        logger.info("I8 Core To Wallet Advice Request: " + vo.toString());

        MiddlewareMessageVO responseVO = (MiddlewareMessageVO) hostTransactionController.coreToWalletAdvice(vo);

        if (responseVO != null) {
            logger.info("I8 Core To Wallet Advice Response: " + responseVO.toString());
            vo.setResponseCode(responseVO.getResponseCode());
        } else {
            logger.info("I8 Core To Wallet Advice No Response.");
        }
    }

    public void cashWithDrawalCommand(MiddlewareMessageVO vo) {
        buildSwtich();
        String BALANCE_FORMATED = null;
        logger.info("inter Bank cashWith Drawal Request send to microbank");
        MiddlewareMessageVO responseVO = (MiddlewareMessageVO) hostTransactionController.cashWithDrawal(vo);
        if (responseVO != null) {
            logger.info("I8 CashWithdrawal Response: " + responseVO.toString());
            vo.setResponseCode(responseVO.getResponseCode());
            if(vo.getResponseCode().equals("00")) {


                try {
                    BALANCE_FORMATED = MiniXMLUtil.getTagTextValue(responseVO.getResponseContentXML(), "//trans/trn/@BALF");
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }

                vo.setAccountBalance(BALANCE_FORMATED);
                logger.info("Balance Recieve from Microbank:" + BALANCE_FORMATED);
            }else{
                logger.info("Response code recieve  from microbank:"+vo.getResponseCode());
            }
        } else {
            logger.info("I8 fund Transfer BB Advice No Response.");
        }

    }

    public void MiniStatementCommand(WebServiceVO vo) {
        buildSwtich();
        logger.info("Microbank>>> Inter Bank MiniStatement Request");
        WebServiceVO responseVo = (WebServiceVO) webServiceSwitchController.miniStatement(vo);
        if (responseVo != null) {
            logger.info("I8 Ministatement Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
            vo.setTransactions(responseVo.getTransactions());
        } else {
            logger.info("I8 Ministatement No Response.");
        }
    }

    public void BalanceInquiryCommand(WebServiceVO vo) {
        buildSwtich();
        logger.info("MicroBank>>> inter Bank BalanceInquiry Request");
        WebServiceVO responseVo = (WebServiceVO) webServiceSwitchController.balanceInquiry(vo);
        if (responseVo != null) {
            logger.info("I8 Balance Inquiry Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
            vo.setBalance(responseVo.getBalance());
        } else {
            logger.info("I8 Balance Inquiry No Response.");
        }

    }

    public void cashWithDrawalReversalCommand(MiddlewareMessageVO vo) {
        buildSwtich();

        logger.info("MicroBank>>> Inter Bank CashWithDrawal Reversal Request");
        MiddlewareMessageVO responseVo = (MiddlewareMessageVO) hostTransactionController.cashWithDrawalReversal(vo);
        if (responseVo != null) {
            logger.info("I8 CashWithdrawal Reversal Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
        } else {
            logger.info("I8 CashWithDrawal Reversal No Response.");
        }

    }

    public void PosTransactionCommand(MiddlewareMessageVO vo) {
        buildSwtich();
        String BALANCE_FORMATED = null;
        logger.info("MicroBank>>> inter Bank PosTransaction Request");
        MiddlewareMessageVO responseVo = (MiddlewareMessageVO) hostTransactionController.posTransaction(vo);
        if (responseVo != null) {
            logger.info("I8 Pos Transaction Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
            if(vo.getResponseCode().equals("00")) {
                try {
                    BALANCE_FORMATED = MiniXMLUtil.getTagTextValue(responseVo.getResponseContentXML(), "//trans/trn/@BALF");
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
                vo.setAccountBalance(BALANCE_FORMATED);
            }else{
                logger.info("Response Recieve from microbank:"+vo.getResponseCode());
            }
        } else {
            logger.info("I8 POS Transaction  No Response.");
        }

    }

    public void PosRefundCommand(MiddlewareMessageVO vo) {
        buildSwtich();
        String BALANCE_FORMATED = null;
        logger.info("MicroBank>>> inter Bank PosRefundTransaction Request");
        MiddlewareMessageVO responseVo = (MiddlewareMessageVO) hostTransactionController.posRefundTransaction(vo);
        if (responseVo != null) {
            logger.info("I8 Pos Refund Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
            if(vo.getResponseCode().equals("00")) {

                try {
                    BALANCE_FORMATED = MiniXMLUtil.getTagTextValue(responseVo.getResponseContentXML(), "//trans/trn/@BALF");
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
                vo.setAccountBalance(BALANCE_FORMATED);
            }else {
                logger.info("Response Recieve from microbank:"+vo.getResponseCode());
            }
        } else {
            logger.info("I8 POS Refund Transaction  No Response.");
        }
    }


    public void PosReverseCommand(MiddlewareMessageVO vo) {
        buildSwtich();
        logger.info("MicroBank>>> inter Bank PosReverseTransaction Request");
        MiddlewareMessageVO responseVo = (MiddlewareMessageVO) hostTransactionController.cashWithDrawalReversal(vo);
        if (responseVo != null) {
            logger.info("I8 PosReverse Transaction Response: " + responseVo.toString());
            vo.setResponseCode(responseVo.getResponseCode());
        } else {
            logger.info("I8 PosReverse Transaction No Response.");
        }
    }


    public void walletIBFTBBAdviceCommand(MiddlewareMessageVO vo) {
        buildSwtich();

        logger.info("Microbank >>> Inter Bank Fund Transfer Request: " + vo.toString());

        MiddlewareMessageVO responseVO = (MiddlewareMessageVO) hostTransactionController.creditAdvice(vo);

        if (responseVO != null) {
            logger.info("Inter Bank Fund Transfer Response: " + responseVO.toString());
            vo.setResponseCode(responseVO.getResponseCode());
        } else {
            logger.info("Inter Bank Fund Transfer No Response.");
        }

    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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

    public void buildSwtich() {
        try {
            if (hostTransactionController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
                executor.setConnectTimeout(30000);
                executor.setReadTimeout(60000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(HostTransactionController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + "/i8Microbank/ws/ibft");
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                hostTransactionController = (HostTransactionController) httpInvokerProxyFactoryBean.getObject();
            }
            if (webServiceSwitchController == null) {

                SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
                executor.setConnectTimeout(30000);
                executor.setReadTimeout(60000);

                HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
                httpInvokerProxyFactoryBean.setServiceInterface(WebServiceSwitchController.class);
                httpInvokerProxyFactoryBean.setServiceUrl(I8_SCHEME + "://" + I8_SERVER + ":" + I8_PORT + "/i8Microbank/ws/fonepay");
                httpInvokerProxyFactoryBean.afterPropertiesSet();
                httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(executor);
                webServiceSwitchController = (WebServiceSwitchController) httpInvokerProxyFactoryBean.getObject();
            }
        } catch (Exception e) {
            logger.error("ERROR", e);
        }
    }








}