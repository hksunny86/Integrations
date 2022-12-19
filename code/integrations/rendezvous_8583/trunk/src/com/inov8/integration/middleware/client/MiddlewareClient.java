package com.inov8.integration.middleware.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.client.codecs.MiddlewareCodecFactory;
import com.inov8.integration.middleware.controller.NetworkInfoBean;
import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.request.EchoRequest;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;

@Component
public class MiddlewareClient {
	private static Logger logger = LoggerFactory.getLogger(MiddlewareClient.class.getSimpleName());
	private IoSession session = null;
	private NioSocketConnector connector;

	@Autowired
	private MiddlewareClientHandler middlewareClientHandler;

	@Autowired
	private NetworkInfoBean networkInfoBean;
	private long writeTimeoutSec = 60L;
	private int readTimeoutSec = 60;
	private Object mutex = new Object();

	private String REMOTE_IP = null;
	private int REMOTE_PORT;
	private int RETRY_DELAY;
	
	@Autowired
	private TransactionDAO transactionDAO;

	public void loadApplicationConfig() {
		REMOTE_IP = ConfigReader.getInstance().getProperty("middleware-ip", "127.0.0.1", true);
		REMOTE_PORT = Integer.parseInt(ConfigReader.getInstance().getProperty("middleware-port", "2020", false));
		RETRY_DELAY = Integer.parseInt(ConfigReader.getInstance().getProperty("retry.delay", "1000", false));
	}

	private SocketAddress remoteSocket;

	// @Autowired
	public MiddlewareClient() {

	}

	@PostConstruct
	public void init() {
		try {

			logger.info("BOOTING MIDDLEWARE INTEGRATION MODULE >>>");
			loadApplicationConfig();			

			this.remoteSocket = new InetSocketAddress(this.REMOTE_IP, this.REMOTE_PORT);
			this.connector = new NioSocketConnector();

			DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
			LoggingFilter loggingFilter = new LoggingFilter();
			chain.addLast("logging", loggingFilter);

			this.connector.setConnectTimeoutMillis(300000L);
			this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MiddlewareCodecFactory()));

			chain.addLast("executor2", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_RECEIVED }));
			chain.addLast("executor3", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_SENT }));
			chain.addLast("executor5", new ExecutorFilter(new IoEventType[] { IoEventType.WRITE }));
			chain.addLast("executor6", new ExecutorFilter(new IoEventType[] { IoEventType.EXCEPTION_CAUGHT }));

			this.connector.getFilterChain().addLast("reconnect",
					new ReconnectionFilter(RETRY_DELAY, this.connector, this.middlewareClientHandler, this.remoteSocket, null, this, networkInfoBean));

			this.connector.setHandler(this.middlewareClientHandler);			
			
			connectSocket();
			
		} catch (Exception e) {
			logger.error("Exception",e);
			logger.error("EXCEPTION OCCURED", e);
		}
	}
	
	public void connectSocket() {

		logger.info("Connecting socket..");
		try {
			
			int retryCount = 0;
			while (!ping(this.REMOTE_IP, this.REMOTE_PORT)) {
				retryCount++;
				logger.debug(REMOTE_IP + ":" + REMOTE_PORT);
				logger.debug("EXTERNAL SYSTEM IS NOT ACCESSIBLE. RETRYING " + retryCount);
				Thread.sleep(RETRY_DELAY);
			}
			
            synchronized (this.mutex) {
                this.networkInfoBean.setConnected(false);
                this.networkInfoBean.setConnecting(true);
                this.networkInfoBean.setAuthenticated(false);
            }

			while (true) {
                ConnectFuture connectFuture = this.connector.connect(remoteSocket);
                connectFuture.awaitUninterruptibly();
				if (connectFuture.isConnected()) {
					logger.info("Socket connected..");
					this.session = connectFuture.getSession();
					this.networkInfoBean.setIoSession(session);
					this.networkInfoBean.setConnected(true);
					this.networkInfoBean.setConnecting(false);
					logger.debug("SYSTEM IS ONLINE & AUTHORIZED FOR TRANSACTIONS");
					logger.debug("LOCAL SOCKET INFO: " + this.session.getLocalAddress().toString());
					logger.debug("REMOTE SOCKET INFO: " + this.session.getRemoteAddress().toString());
					break;
				} else {
					logger.info("Failed to connect with external system socket..");
				}

				Thread.sleep(RETRY_DELAY);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendMessage(BasePDU pdu) {
		this.session.write(pdu);
	}

	public IoSession getSession() {
		return this.session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public NioSocketConnector getConnector() {
		return this.connector;
	}

	public void setConnector(NioSocketConnector connector) {
		this.connector = connector;
	}

	public long getWriteTimeoutSec() {
		return this.writeTimeoutSec;
	}

	public void setWriteTimeoutSec(long writeTimeoutSec) {
		this.writeTimeoutSec = writeTimeoutSec;
	}

	public int getReadTimeoutSec() {
		return this.readTimeoutSec;
	}

	public void setReadTimeoutSec(int readTimeoutSec) {
		this.readTimeoutSec = readTimeoutSec;
	}

	public Object getMutex() {
		return this.mutex;
	}

	public void setMutex(Object mutex) {
		this.mutex = mutex;
	}
	
	@Scheduled(initialDelay = 5000, fixedRateString = "${heartbeat}")
	public void heartBeat(){
		// Send Echo if session is idle
		if(networkInfoBean.isConnected()){
			EchoRequest request = new EchoRequest();
			String stan = transactionDAO.getNextRRNSequence();
			request.setStan(stan);
			request.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
			request.setNetworkManagementCode("270");

			request.build();
			logger.debug("Send Heart Beat");
			sendMessage(request);
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
				if (requestSocket != null) {
					requestSocket.close();
				}
			} catch (Exception e2) {
				logger.error("Exception While Closing Socket", e2);
			}
		}
		return ping;
	}
}
