/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.controller.NetworkInfoBean;
import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.PDUWrapper;
import com.inov8.integration.middleware.pdu.parser.ISO8583MessageParser;
import com.inov8.integration.middleware.pdu.request.EchoRequest;
import com.inov8.integration.middleware.pdu.response.EchoResponse;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.queue.TransactionResponsePool;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;

@Component
public class MiddlewareClientHandler extends IoHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(MiddlewareClientHandler.class.getSimpleName());
	@Autowired
	private TransactionResponsePool responsePool;
	@Autowired
	private TransactionDAO transactionDAO;
	@Autowired
	private NetworkInfoBean networkInfoBean;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private String REMOTE_IP = ConfigReader.getInstance().getProperty("middleware-ip", "127.0.0.1", true);
	private int REMOTE_PORT = Integer.parseInt(ConfigReader.getInstance().getProperty("middleware-port", "2020", false));
	private int IDDLE_TIME = Integer.parseInt(ConfigReader.getInstance().getProperty("idletime", "60", false));
	
	@Override
	public void sessionOpened(IoSession session) {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDDLE_TIME);
		((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
		((SocketSessionConfig) session.getConfig()).setReuseAddress(true);
		session.getConfig().setUseReadOperation(true);

		networkInfoBean.setConnected(true);
	}

	@Override
	public void sessionClosed(IoSession session) {
		// Print out total number of bytes read from the remote peer.
		networkInfoBean.setAuthenticated(false);
		networkInfoBean.setConnected(false);
		logger.error("Total ReadBytes:" + session.getReadBytes() + " byte(s)");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		logger.debug("Last read time => " + session.getLastReadTime());
		logger.debug("Current time => " + System.currentTimeMillis());

		// Send Echo if session is idle
		EchoRequest request = new EchoRequest();
		String stan = transactionDAO.getNextRRNSequence();
		request.setStan(stan);
		request.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		request.setNetworkManagementCode("270");

		request.build();
		session.write(request);

		long timeout = IDDLE_TIME * 4 * 1000 + 1000;
		
		// Forcelly Disconnect after timeout * 3 seconds of Reader Idle
		if (System.currentTimeMillis() - session.getLastReadTime() > timeout) {

			logger.debug("Reader IDLE SESSION DETECTED More than " + IDDLE_TIME * 4 +" seconds => Checking Active Network Connection ");
			logger.debug("IDLE SESSION DETECTED => Session closing ");
			session.close(true);
		}
	}

	@Override
	public void messageReceived(final IoSession session, final Object message) {
		executorService.submit(new Callable<Object>() {
			public Object call() throws Exception {
				processMessageRecieve(session, message);
				return null;
			}
		});
	}

	/**
	 * @param session
	 * @param message
	 */
	@Async
	private void processMessageRecieve(IoSession session, Object message) {

		byte[] packet = (byte[]) message;
		BasePDU basePDU = ISO8583MessageParser.parse(packet);

		if (basePDU != null) {
			if (basePDU.getHeader().getMessageType().equals("0800")) {
				EchoResponse response = new EchoResponse();

				String stan = transactionDAO.getNextRRNSequence();
				response.setStan(stan);
				response.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
				response.setNetworkManagementCode("270");
				response.setResponseCode("00");
				response.build();
				session.write(response);
			} else if (basePDU.getHeader().getMessageType().equals("0810")) {
				// Do Nothing
			} else {

				basePDU.setRawPdu(packet);
				PDUWrapper pduWrapper = new PDUWrapper();
				pduWrapper.setBasePDU(basePDU);
				String stan = basePDU.getStan();

				String transactionKey = this.getVersionClass(basePDU.getHeader().getMessageType()) + basePDU.getTransactionDate() + stan;
				pduWrapper.setRRNKey(transactionKey);
				pduWrapper.setPoolTimeIn(new Date());

				this.responsePool.put(pduWrapper);
				updateTransactionInDB(basePDU, transactionKey);
			}
		}
	}

	private void updateTransactionInDB(BasePDU basePDU, String rrn) {
		TransactionLogModel trx = new TransactionLogModel();// this.transactionDAO.select(rrnKey);
		trx.setStatus(TransactionStatus.RECEIVED.getValue().longValue());
		trx.setResponseCode(basePDU.getResponseCode());
		String responseString;
		try {
			responseString = new String(basePDU.getRawPdu(), "UTF-8");
			trx.setPduResponseString(responseString);
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception", e);
		}
		trx.setPduResponseHEX(Hex.encodeHexString(basePDU.getRawPdu()));
		trx.setProcessedTime(0L);
		trx.setRetrievalRefNo(rrn);
		this.transactionDAO.update(trx);
		logger.debug("RRN Recieved and Inserted in Transaction Pool: " + basePDU.getRrn());

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error("**** EXCEPTION ****", cause);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		logger.debug("********** TCP SOCKET CONNECTED *********");
		logger.debug("DESTINATION SERVER >>> " + session.getRemoteAddress().toString());

		// Forcelly Disconnect after 15 seconds of Reader Idle
		logger.debug("Last read time => " + session.getLastReadTime());
		logger.debug("Current time =>  " + System.currentTimeMillis());
		if (System.currentTimeMillis() - session.getLastReadTime() > 15000L) {

			logger.debug("Reader IDLE SESSION DETECTED More than 15 seconds => Session closing ");
			boolean isConnected = ping(REMOTE_IP, REMOTE_PORT);
			if (!isConnected) {
				networkInfoBean.setConnected(false);
				logger.debug("Reader IDLE SESSION DETECTED More than 15 seconds => Session closing ");
				session.close(true);
			} else {
				logger.debug("Connection Succesful to Remote Server");
			}

		}
	}

	private boolean ping(String ip, int port) {

		boolean ping = true;
		Socket requestSocket = null;
		try {
			requestSocket = new Socket(ip, port);
			logger.debug("Telnet Successful: " + requestSocket.getRemoteSocketAddress());
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException ", e);
			ping = false;
		} catch (IOException e) {
			ping = false;
			logger.error("IOException ", e);
		} finally {
			try {
				if (requestSocket != null && requestSocket.isConnected()) {
					requestSocket.close();
				}
			} catch (Exception e2) {
				logger.error("Exception While Closing Socket", e2);
			}
		}
		return ping;
	}
	public String getVersionClass(String str)
	{
		return str.substring(0, 2);
	}

}
