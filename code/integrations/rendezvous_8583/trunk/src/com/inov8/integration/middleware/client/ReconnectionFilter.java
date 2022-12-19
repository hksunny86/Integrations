package com.inov8.integration.middleware.client;

import java.net.SocketAddress;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.controller.NetworkInfoBean;

/**
 * A simple auto reconnection filter. Retry to reconnect a closed session until
 * it's connected. A delay between the reconnection is available as parameter.
 * 
 */
public class ReconnectionFilter extends IoFilterAdapter {
	private static Logger logger = LoggerFactory.getLogger(ReconnectionFilter.class.getSimpleName());
	private int reconnectDelay;
	private IoConnector connector;
	private IoHandler handler;
	private SocketAddress remoteAddress;
	private SocketAddress localAddress;
	private MiddlewareClient manager;
	private NetworkInfoBean info;
	
	/**
	 * @param delay
	 *            delay in mili-seconds between 2 reconnections
	 * @param connector
	 *            the IoConnector for reconnect
	 * @param protocol
	 *            handler provided to use for the connection
	 */
	public ReconnectionFilter(int delay, IoConnector connector, IoHandler handler, SocketAddress remoteAddress, SocketAddress localAddress,
			MiddlewareClient manager, NetworkInfoBean info) {
		this.reconnectDelay = delay;
		this.connector = connector;
		this.handler = handler;
		this.remoteAddress = remoteAddress;
		this.localAddress = localAddress;
		this.manager = manager;
		this.info = info;
	}


	public void sessionClosed(NextFilter nextFilter, IoSession session) {
		// fire a connection retrying Thread
		logger.debug("Socket Connection Terminated By Server");
		nextFilter.sessionClosed(session);
		
		manager.connectSocket();
		
		/*ConnectionThread connectionThread = new ConnectionThread();
		
		synchronized (this) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
		connectionThread.start();*/
	}

	/* private class ConnectionThread extends Thread {
		private ConnectionThread() {
			super();
		}

		public void run() {
			for (int i=1;;i++) {
				logger.debug("Firing TCP/SOCKET Connection Retry => "+i);
				ConnectFuture future = connector.connect(remoteAddress);
//				connector.setHandler(handler);
				future.awaitUninterruptibly();
				if (future.isConnected()) {
					manager.setSession(future.getSession());
					info.setConnected(true);
					info.setIoSession(future.getSession()); 
					return;
				}
				synchronized (this) {
					try {
						sleep(reconnectDelay);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}*/
	
}
