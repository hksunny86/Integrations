/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.hsm.client;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.inov8.hsm.controller.NetworkInfoBean;
import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;
import com.inov8.hsm.pdu.PDUWrapper;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.parser.response.DecryptEncryptedPINResponseParser;
import com.inov8.hsm.pdu.parser.response.EncryptClearPINResponseParser;
import com.inov8.hsm.pdu.parser.response.GeneratePINBlockResponseParser;
import com.inov8.hsm.pdu.parser.response.GenerateRandomPINResponseParser;
import com.inov8.hsm.pdu.parser.response.GenerateVisaPVVResponseParser;
import com.inov8.hsm.pdu.parser.response.ValidatePINBlockWithPVVResponseParser;
import com.inov8.hsm.pdu.parser.response.VerifyAndGeneratePVVResponseParser;
import com.inov8.hsm.pdu.response.DecryptEncryptedPINResponse;
import com.inov8.hsm.pdu.response.EncryptClearPINResponse;
import com.inov8.hsm.pdu.response.GeneratePINBlockResponse;
import com.inov8.hsm.pdu.response.GenerateRandomPINResponse;
import com.inov8.hsm.pdu.response.GenerateVisaPVVResponse;
import com.inov8.hsm.pdu.response.ValidatePINBlockWithPVVResponse;
import com.inov8.hsm.pdu.response.VerifyAndGeneratePVVResponse;
import com.inov8.hsm.persistance.TransactionDAO;
import com.inov8.hsm.queue.HSMResponsePool;

@Component
public class HSMClientHandler extends IoHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(HSMClientHandler.class.getSimpleName());
	@Autowired
	private HSMResponsePool responsePool;
	@Autowired
	private TransactionDAO transactionDAO;
	@Autowired
	private NetworkInfoBean networkInfoBean;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void sessionOpened(IoSession session) {
		((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
		((SocketSessionConfig) session.getConfig()).setReuseAddress(true);

		networkInfoBean.setConnected(true);
	}

	@Override
	public void sessionClosed(IoSession session) {
		// Print out total number of bytes read from the remote peer.
		networkInfoBean.setConnected(false);
		logger.error("Total ReadBytes:" + session.getReadBytes() + " byte(s)");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
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

		String packetString = message.toString();
		logger.info("Received Message: " + packetString);
		
		BaseHeader baseHeader = HeaderParser.parseHeader(new StringBuilder(packetString));
		
		if(baseHeader.getCommand().equalsIgnoreCase("JB")){
			GenerateRandomPINResponse response = GenerateRandomPINResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("NH")){
			DecryptEncryptedPINResponse response = DecryptEncryptedPINResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("DH")){
			GenerateVisaPVVResponse response = GenerateVisaPVVResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("BB")){
			EncryptClearPINResponse response = EncryptClearPINResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("JH")){
			GeneratePINBlockResponse response = GeneratePINBlockResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("ED")){
			ValidatePINBlockWithPVVResponse response = ValidatePINBlockWithPVVResponseParser.parse(packetString);
			this.putInPool(response);
		}
		
		else if(baseHeader.getCommand().equalsIgnoreCase("CV")){
			VerifyAndGeneratePVVResponse response = VerifyAndGeneratePVVResponseParser.parse(packetString);
			this.putInPool(response);
		}

	}
	
	private void putInPool(BasePDU basePDU){
		PDUWrapper pduWrapper = new PDUWrapper();
		pduWrapper.setBasePDU(basePDU);
		pduWrapper.setPoolTimeIn(new Date());
		pduWrapper.setRRNKey(basePDU.getHeader().getUPID());
		responsePool.put(pduWrapper);
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
	}

}
