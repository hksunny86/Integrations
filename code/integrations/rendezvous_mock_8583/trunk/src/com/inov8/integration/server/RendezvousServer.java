package com.inov8.integration.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.server.codecs.MockCodecFactory;

@Component("RendezvousServer")
public class RendezvousServer {
	private static Logger logger = LoggerFactory.getLogger(RendezvousServer.class);

	private final Integer PORT = Integer.parseInt(ConfigReader.getInstance().getProperty("mock-port", "2020"));
	private IoAcceptor acceptor;
	@Autowired
	private RendezvousServerTransactionProcessor processor;

	public RendezvousServer() {
	}

	public static void main(String[] args) {
		RendezvousServer mockServer = new RendezvousServer();
		System.out.println("MOCK GREETS YOU! Hahahaha");

	}

	@PostConstruct
	public void start() {
		this.init();
		this.startup();
	}

	public void init() {
		logger.info("Initiating Integration Module Mock Server");
		acceptor = new NioSocketAcceptor();

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		LoggingFilter loggingFilter = new LoggingFilter();
		chain.addLast("logger", loggingFilter);
		chain.addLast("executor2", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_RECEIVED }));
		chain.addLast("executor3", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_SENT }));
		chain.addLast("executor5", new ExecutorFilter(new IoEventType[] { IoEventType.WRITE }));
		chain.addLast("executor6", new ExecutorFilter(new IoEventType[] { IoEventType.EXCEPTION_CAUGHT }));

		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MockCodecFactory()));

		acceptor.setHandler(processor);

		acceptor.getSessionConfig().setReadBufferSize(2048);
		//acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3000);
		
		

	}

	public void startup() {
		logger.info("Starting up Mock Server");
		try {
			// Starting the Server
			acceptor.bind(new InetSocketAddress(PORT));
			
			logger.info("Server started and listing at " + acceptor.getLocalAddress());
		} catch (IOException e) {
			logger.error("Exception",e);
			logger.error(e.getMessage());
		}
	}
}