package com.inov8.integration.host;

import java.net.SocketAddress;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.inov8.integration.middleware.pdu.response.*;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.parser.ISO8583MessageParser;
import com.inov8.integration.middleware.util.ConfigReader;

@Component
@SuppressWarnings("unused")
public class HostTransactionProcessor extends IoHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(HostTransactionProcessor.class.getSimpleName());
    private static Map<String, String> acl = new HashMap<String, String>();

    @Autowired
    private HostTransactionResponseBuilder hostResponseBuilder = null;

    public HostTransactionProcessor() {

        String allowedClients = ConfigReader.getInstance().getProperty("acl", "");
        String[] clients = StringUtils.split(allowedClients, ";");

        for (String client : clients) {
            acl.put(client, client);
        }

        acl.put("127.0.0.1", "all");
        acl.put("10.0.5.187", "JS Bank");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("Exception occured at server.");
        logger.error("Exception", cause);
    }

    private void msgProcessor(IoSession session, Object message) throws InterruptedException {
        // super.messageReceived(session, message);
        logger.debug("Message Received at Middleware Mock Server");
        logger.debug("Message: " + message);

        byte[] packet = (byte[]) message;
        BasePDU pdu = ISO8583MessageParser.parse(packet);

        if (pdu == null) {
            logger.error("<<<<< PDU IS NOT RECOGNIZED >>>>>");
            logger.error("<<<<< DISCONNECTING CLIENT " + session.getRemoteAddress().toString() + " >>>>>");
            session.close(true);
            return;
        }

        BasePDU resp = null;
        String messageType = pdu.getHeader().getMessageType();

        resp = processHostTransactions(pdu, messageType);

        logger.info("######## PDU Sending Response Back ###");
        if (resp != null) {
            session.write(resp);
        } else {
            // No Response to send
        }

    }

    public BasePDU processHostTransactions(BasePDU pdu, String messageType) {
        BasePDU resp = null;
        // Host Transaction
        if (messageType.equals(MessageTypeEnum.MT_0200.getValue()) && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
            String procCode = pdu.getProcessingCode();
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_TITLE_FETCH_CODE.getValue())
                    || procCode.startsWith(TransactionCodeEnum.JS_TITLE_FETCH.getValue())
            ) {
                BBTitleFetchResponse titleFetchResponse = hostResponseBuilder.titleFetchBBHandler(pdu);

                titleFetchResponse.build();
                resp = titleFetchResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_CASH_WITH_DRAWAL_CODE.getValue())) {
                CashWithDrawalResponse cashWithDrawlResponse = hostResponseBuilder.cashWithDrawalHandler(pdu);
                cashWithDrawlResponse.build();
                resp = cashWithDrawlResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_MINI_STATEMENT_CODE.getValue())) {
                MiniStatementResponse miniStatementResponse = hostResponseBuilder.miniStatementHandler(pdu);
                miniStatementResponse.build();
                resp = miniStatementResponse;

            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_BALANCE_INQUIRY_CODE.getValue())) {
                BalanceInquiryResponse balanceInquiryResponse = hostResponseBuilder.balanceInquiryResponseHandler(pdu);
                balanceInquiryResponse.build();
                resp = balanceInquiryResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_POS_TRANSACTION_CODE.getValue())) {
                PosTransactionResponse posTransactionResponse = hostResponseBuilder.posTransactionHandler(pdu);
                posTransactionResponse.build();
                resp = posTransactionResponse;

            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_POS_REVERSAL_CODE.getValue())) {
                POSRefundResponse posTransactionResponse = hostResponseBuilder.posRefundHandler(pdu);
                posTransactionResponse.build();
                resp = posTransactionResponse;

            }
        } else if (messageType.equals(MessageTypeEnum.MT_0220.getValue())
                && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
            String procCode = pdu.getProcessingCode();
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_ACCOUNT_FT_ADVICE_CODE.getValue())) {

                BBFundTransferAdviceResponse adviceResponse = hostResponseBuilder.funTransferBBAdviceHandler(pdu);

                adviceResponse.build();
                resp = adviceResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_CORE_TO_WALLET_ADVICE.getValue())) {

                CoreToWalletResponse coretoWalletadviceResponse = hostResponseBuilder.coreToWalletAdviceHandler(pdu);

                coretoWalletadviceResponse.build();
                resp = coretoWalletadviceResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_IBFT_WALLET_CREDIT_ADVICE_CODE.getValue())) {

                BBFundTransferAdviceResponse adviceResponse = hostResponseBuilder.funTransferBBAdviceHandler(pdu);

                adviceResponse.build();
                resp = adviceResponse;

//				BBWalletIBFTAdviceResponse adviceResponse = hostResponseBuilder.walletIBFTBBAdviceHandler(pdu);
//
//				adviceResponse.build();
//				resp = adviceResponse;
            }
        } else if (messageType.equals(MessageTypeEnum.MT_0420.getValue()) && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
            String procCode = pdu.getProcessingCode();
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_CASH_WITH_DRAWAL_CODE.getValue())) {
                CashWithDrawalReversalResponse cashWithDrawalReversalResponse = hostResponseBuilder.cashWithDrawalReversalHandler(pdu);
                cashWithDrawalReversalResponse.build();
                resp = cashWithDrawalReversalResponse;
            }
            if (procCode.startsWith(TransactionCodeEnum.JS_BB_POS_TRANSACTION_CODE.getValue())) {
                PosReverseTransactionResponse posRefundResponse = hostResponseBuilder.posReverseTransactionHandler(pdu);
                posRefundResponse.build();
                resp = posRefundResponse;
            }

        } else if (messageType.equals(MessageTypeEnum.MT_0800.getValue())
                && StringUtils.isNotEmpty(pdu.getNetworkManagementCode())) {
            String networkManagementCode = pdu.getNetworkManagementCode();

            // SAF
            if (networkManagementCode.equals("060")) {

                // Sign on
            } else if (networkManagementCode.equals("061")) {

                // Sing off
            } else if (networkManagementCode.equals("062")) {

                // network sign-off, begin stand-in processing
            } else if (networkManagementCode.equals("065")) {

                // 001 is log on
            } else if (networkManagementCode.equals("001") || networkManagementCode.equals("270")
                    || networkManagementCode.equals("003")) {// echo
                logger.debug("NETWORK MANAGEMENT: ECHO MESSAGE RECIEVED");
                EchoResponse echoResponse = new EchoResponse();
                echoResponse.setTransactionDate(pdu.getTransactionDate());
                echoResponse.setStan(pdu.getStan());
                echoResponse.setNetworkManagementCode(networkManagementCode);
                echoResponse.setResponseCode("00");
                echoResponse.build();
                resp = echoResponse;
            }
        }
        return resp;
    }

    private void logHostRequest(ISOMsg msg) {
        try {
            BitSet bset = (BitSet) msg.getValue(-1);
            logger.debug("BitMap : " + org.jpos.iso.ISOUtil.bitSet2String(bset));

            System.out.println("Message (HEX String)");
            System.out.println(ISOUtil.hexString(msg.pack()));

            System.out.println("Message (HEX Dump");
            System.out.println(ISOUtil.hexdump(msg.pack()));
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    private void logHostResponse(ISOMsg msg) {
        try {
            BitSet bset = (BitSet) msg.getValue(-1);
            logger.debug("BitMap : " + org.jpos.iso.ISOUtil.bitSet2String(bset));

            System.out.println("Message (HEX String)");
            System.out.println(ISOUtil.hexString(msg.pack()));

            System.out.println("Message (HEX Dump");
            System.out.println(ISOUtil.hexdump(msg.pack()));
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception {

        ExecutorService es = Executors.newCachedThreadPool();
        final Future<Object> future = es.submit(new Callable<Object>() {
            public Object call() throws Exception {
                msgProcessor(session, message);
                return null;
            }
        });
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        logger.debug("Sending Message: " + message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        logger.debug("Session being closed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // super.sessionCreated(session);
        SocketAddress remoteClient = session.getRemoteAddress();
        String address = remoteClient.toString();
        address = StringUtils.remove(address, "/");
        address = address.split(":")[0];
        boolean production = Boolean.valueOf(ConfigReader.getInstance().getProperty("production", "false"));
        if (production) {
            if (!acl.containsKey(address)) {
                logger.debug("ACL ERROR: Session forcefuly closed from " + address);
                session.close(true);
            } else {
                logger.debug("Session being created for Host Server");
                logger.debug("Connected: " + address);
            }
        } else {
            logger.debug("Session being created for Host Server");
            logger.debug("Connected: " + address);
        }

    }

    @Override
    /**
     * WHEN EVER SESSION (READING, WRITING) IS IDLE, SEND ECHOTEST MESSAGE TO ENSURE
     * THE CONNECTIVITY WITH
     */
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.debug("Idle Session detected. Sending Echo Message from server.");
        // EchoTestRequest request = new EchoTestRequest();
        // request.getHeader().getMessageType().setValue(MessageTypeEnum.MT_0800.getValue());
        // request.getHeader().getTransactionCode().setValue(TransactionCodeEnum.ECHOTEST.getValue());
        // request.getHeader().getTransmissionDateTime().setValue(FieldUtil.buildTransmissionDateTime());
        // request.getHeader().getRetrievalRefNumber().setValue(FieldUtil.buildRRN());
        // request.assemblePDU();
        // session.write(request);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        session.getConfig().setBothIdleTime(3600);
    }

}
