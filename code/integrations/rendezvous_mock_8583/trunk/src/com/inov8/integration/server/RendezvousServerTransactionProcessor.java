package com.inov8.integration.server;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.parser.ISO8583MessageParser;
import com.inov8.integration.middleware.pdu.request.EchoRequest;
import com.inov8.integration.middleware.pdu.response.AccountBalanceInquiryResponse;
import com.inov8.integration.middleware.pdu.response.AcquirerReversalAdviceResponse;
import com.inov8.integration.middleware.pdu.response.BillInquiryResponse;
import com.inov8.integration.middleware.pdu.response.BillPaymentResponse;
import com.inov8.integration.middleware.pdu.response.EchoResponse;
import com.inov8.integration.middleware.pdu.response.FundTransferAdviceResponse;
import com.inov8.integration.middleware.pdu.response.FundTransferResponse;
import com.inov8.integration.middleware.pdu.response.TitleFetchResponse;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FieldUtil;

@Component
public class RendezvousServerTransactionProcessor extends IoHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(RendezvousServerTransactionProcessor.class.getSimpleName());
	private RendezvousConnectionManager manager;
	@Autowired
	private RendezvousTransactionResponseBuilder mockResponseBuilder;

	public RendezvousServerTransactionProcessor() {
//		acl.put("127.0.0.1", "all");
//		acl.put("172.29.12.152", "all");
		
		// manager = MockJDBCConnectionManager.instance();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error("Exception occured at server.");
		logger.error("Exception",cause);
	}

	private void msgProcessor(IoSession session, Object message) throws InterruptedException {
		// super.messageReceived(session, message);
		logger.debug("Message Received at Middleware Mock Server");
		logger.debug("Message: " + message);

		byte[] packet = (byte[]) message;
		BasePDU pdu = ISO8583MessageParser.parse(packet);

		if (pdu == null) {
			logger.error("######## PDU is not recognized ###");
		} else {
			logger.info("######## PDU Sending Response Back ###");
			// session.write(pdu);
		}

		try {
			BasePDU transactionPdu = processIntegrationTransactions(pdu);
			if (transactionPdu != null) {
				session.write(transactionPdu);
			} else {
				// No Response to send
			}
		} catch (Exception e) {
			logger.error("Erro", e);
		}
		System.out.println();

	}

	public BasePDU processIntegrationTransactions(BasePDU pdu) {
		BasePDU transactionPdu = null;
		String messageType = pdu.getHeader().getMessageType();
		String procCode = pdu.getProcessingCode();
		if (messageType.equals("0200") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
			if (procCode.equals(TransactionCodeEnum.JS_ACCOUNT_BALANCE_INQUIRY.getValue())) {
				AccountBalanceInquiryResponse balanceInquiryResponse = mockResponseBuilder.balanceInquiryHandler(pdu);
				balanceInquiryResponse.build();
				transactionPdu = balanceInquiryResponse;
			} else if (procCode.equals(TransactionCodeEnum.JS_BILL_INQUIRY.getValue())) {
				BillInquiryResponse billInquiryResponse = mockResponseBuilder.billInquiryHandler(pdu);
				billInquiryResponse.build();
				transactionPdu = billInquiryResponse;
			} else if (procCode.equals(TransactionCodeEnum.JS_TITLE_FETCH.getValue())) {
				TitleFetchResponse titleFetchResponse = mockResponseBuilder.titleFetchHandler(pdu);
				if(titleFetchResponse != null){
					titleFetchResponse.build();
				}
				transactionPdu = titleFetchResponse;
			} else if (procCode.equals(TransactionCodeEnum.JS_FUND_TRANSFER.getValue())) {
				FundTransferResponse fundTransferResponse = mockResponseBuilder.fundTransferHandler(pdu);
				if(fundTransferResponse != null){
					fundTransferResponse.build();
				}
				transactionPdu = fundTransferResponse;
			}

		} else if (messageType.equals("0220") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
			if (procCode.equals(TransactionCodeEnum.JS_FUND_TRANSFER_ADVICE.getValue())) {
				FundTransferAdviceResponse fundTransferAdviceResponse = mockResponseBuilder.fundTransferAdviceHandler(pdu);
				if(fundTransferAdviceResponse != null){
					fundTransferAdviceResponse.build();
				}
				transactionPdu = fundTransferAdviceResponse;
			} else if (procCode.equals(TransactionCodeEnum.JS_BILL_PAYMENT.getValue())) {
				BillPaymentResponse billPaymentResponse = mockResponseBuilder.billPaymentHandler(pdu);
				if(billPaymentResponse != null){
					billPaymentResponse.build();
				}
				transactionPdu = billPaymentResponse;
			}
		} else if (messageType.equals("0420") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
			AcquirerReversalAdviceResponse acquirerReversalAdviceResponse = mockResponseBuilder.acquirerReversalAdviceHandler(pdu);
			if(acquirerReversalAdviceResponse != null){
				acquirerReversalAdviceResponse.build();
			}
			transactionPdu = acquirerReversalAdviceResponse;
		
		} else if (messageType.equals("0800")) {
			EchoResponse echoResponse = new EchoResponse();
			echoResponse.setStan(pdu.getStan());
			echoResponse.setTransactionDate(pdu.getTransactionDate());
			echoResponse.setNetworkManagementCode(pdu.getNetworkManagementCode());
			echoResponse.setResponseCode("00");
			
			echoResponse.build();
			transactionPdu = echoResponse;
			
			try{
//				Thread.sleep(10000);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return transactionPdu;
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

	private Map<String, String> acl = new HashMap<String, String>();

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// super.sessionCreated(session);
		SocketAddress remoteClient = session.getRemoteAddress();
		String address = remoteClient.toString();
		address = StringUtils.remove(address, "/");
		address = address.split(":")[0];
//		if (!acl.containsKey(address)) {
//			logger.debug("ACL ERROR: Session forcefuly closed from " + address);
//			session.close(true);
//		} else {
			logger.debug("Session being created for Host Server");
			logger.debug("Connected: " + address);
//		}
	}

	@Override
	/**
	 * WHEN EVER SESSION (READING, WRITING) IS IDLE, SEND ECHOTEST MESSAGE
	 * TO ENSURE THE CONNECTIVITY WITH
	 */
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.debug("Idle Session detected. Sending Echo Message from server.");
		// Send Echo if session is idle
		EchoRequest request = new EchoRequest();
		request.setStan("123456");
		request.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		request.setNetworkManagementCode("270");

//		request.build();
//		session.write(request);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
		((SocketSessionConfig) session.getConfig()).setReuseAddress(true);
		session.getConfig().setUseReadOperation(true);
	}

	public void prepareResponseHeader(BaseHeader requestHeader, BaseHeader responseHeader) {
		// responseHeader.setMessageType(MessageTypeEnum.MT_0210.getValue());
		// responseHeader.setPan(requestHeader.getPan());
		// responseHeader.setTransCode(requestHeader.getTransCode());
		// responseHeader.setTransmissionDateTime(requestHeader.getTransmissionDateTime());
	}
}
