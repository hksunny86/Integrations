package com.inov8.hsm.client;

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
import org.springframework.stereotype.Component;

import com.inov8.hsm.client.codecs.HSMCodecFactory;
import com.inov8.hsm.controller.NetworkInfoBean;
import com.inov8.hsm.util.ConfigReader;

@Component
public class HSMClient {
	private static Logger logger = LoggerFactory.getLogger(HSMClient.class.getSimpleName());
	private IoSession session = null;
	private NioSocketConnector connector;

	@Autowired
	private HSMClientHandler middlewareClientHandler;

	@Autowired
	private NetworkInfoBean networkInfoBean;
	private long writeTimeoutSec = 60L;
	private int readTimeoutSec = 60;
	private Object mutex = new Object();

	private String REMOTE_IP = null;
	private int REMOTE_PORT;
	private int RETRY_DELAY;

	public void loadApplicationConfig() {
		REMOTE_IP = ConfigReader.getInstance().getProperty("hsm-ip", "127.0.0.1", true);
		REMOTE_PORT = Integer.parseInt(ConfigReader.getInstance().getProperty("hsm-port", "2020", false));
		RETRY_DELAY = Integer.parseInt(ConfigReader.getInstance().getProperty("retry.delay", "1000", false));
	}

	private SocketAddress remoteSocket;

	// @Autowired
	public HSMClient() {

	}

	@PostConstruct
	public void init() {
		try {

			logger.info("BOOTING PAYSHIELD HSM INTEGRATION MODULE >>>");
			loadApplicationConfig();

			int retryCount = 0;
			while (!ping(this.REMOTE_IP, this.REMOTE_PORT)) {
				retryCount++;
				logger.debug(REMOTE_IP + ":" + REMOTE_PORT);
				logger.debug("PAYSHIELD IS NOT ACCESSIBLE. RETRYING " + retryCount);
				Thread.sleep(RETRY_DELAY);
			}

			this.remoteSocket = new InetSocketAddress(this.REMOTE_IP, this.REMOTE_PORT);
			this.connector = new NioSocketConnector();

			DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
			LoggingFilter loggingFilter = new LoggingFilter();
			chain.addLast("logging", loggingFilter);

			this.connector.setConnectTimeoutMillis(300000L);
			this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HSMCodecFactory()));

			chain.addLast("executor2", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_RECEIVED }));
			chain.addLast("executor3", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_SENT }));
			chain.addLast("executor5", new ExecutorFilter(new IoEventType[] { IoEventType.WRITE }));
			chain.addLast("executor6", new ExecutorFilter(new IoEventType[] { IoEventType.EXCEPTION_CAUGHT }));

			this.connector.getFilterChain().addLast("reconnect",
					new HSMFilter(RETRY_DELAY, this.connector, this.middlewareClientHandler, this.remoteSocket, null, this, networkInfoBean));

			this.connector.setHandler(this.middlewareClientHandler);

			synchronized (this.mutex) {
				this.networkInfoBean.setConnected(true);
				this.networkInfoBean.setConnecting(true);
			}

			ConnectFuture connectFuture = this.connector.connect(remoteSocket);

			connectFuture.awaitUninterruptibly();
			if (connectFuture.isConnected()) {
				logger.info("CONNECTED TO PAYSHIELD HSM...");
				this.session = connectFuture.getSession();
				this.networkInfoBean.setIoSession(session);
				this.networkInfoBean.setConnected(true);
				this.networkInfoBean.setConnecting(false);
				logger.debug("SYSTEM IS ONLINE & AUTHORIZED FOR TRANSACTIONS");
				logger.debug("LOCAL SOCKET INFO: " + this.session.getLocalAddress().toString());
				logger.debug("REMOTE SOCKET INFO: " + this.session.getRemoteAddress().toString());
			} else {
				logger.error(">>>> Error: Failed to connect to the PayShield HSM...", connectFuture.getException());
			}
		} catch (Exception e) {
			logger.error("Exception",e);
			logger.error("EXCEPTION OCCURED", e);
		}
	}

	public void sendMessage(Object pdu) {
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
