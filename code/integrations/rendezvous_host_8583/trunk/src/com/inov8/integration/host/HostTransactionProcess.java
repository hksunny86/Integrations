package com.inov8.integration.host;

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

import com.inov8.integration.host.server.codecs.HostCodecFactory;
import com.inov8.integration.middleware.util.ConfigReader;

@Component
public class HostTransactionProcess {
	private static Logger logger = LoggerFactory.getLogger(HostTransactionProcess.class);

	private final Integer PORT = Integer.parseInt(ConfigReader.getInstance().getProperty("host-port", "2020"));
	private IoAcceptor acceptor;

	@Autowired
	private HostTransactionProcessor hostTransactionProcessor;

	public HostTransactionProcess() {
	}

	@PostConstruct
	public void start() {
		this.init();
		this.startup();
	}

	public void init() {
		logger.info("BOOTING UP HOST MODULE");
		acceptor = new NioSocketAcceptor();

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		LoggingFilter loggingFilter = new LoggingFilter();
		chain.addLast("logger", loggingFilter);
		chain.addLast("executor2", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_RECEIVED }));
		chain.addLast("executor3", new ExecutorFilter(new IoEventType[] { IoEventType.MESSAGE_SENT }));
		chain.addLast("executor5", new ExecutorFilter(new IoEventType[] { IoEventType.WRITE }));
		chain.addLast("executor6", new ExecutorFilter(new IoEventType[] { IoEventType.EXCEPTION_CAUGHT }));

		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HostCodecFactory()));

		acceptor.setHandler(this.hostTransactionProcessor);

		acceptor.getSessionConfig().setReadBufferSize(2048);
		// acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3000);

	}

	public void startup() {
		logger.info("STARTING HOST");
		try {
			// Starting the Server
			acceptor.bind(new InetSocketAddress(PORT));
			logger.info("HOST STARTED @ " + acceptor.getLocalAddress());
		} catch (IOException e) {
			logger.error("Exception", e);
			logger.error(e.getMessage());
		}
	}
}