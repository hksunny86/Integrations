package com.inov8.integration.client.socket;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * A simple auto reconnection filter. Retry to reconnect a closed session until
 * it's connected. A delay between the reconnection is available as parameter.
 */
public class I8SBSocketReconnectionFilter extends IoFilterAdapter {
    private static Logger logger = LoggerFactory.getLogger(I8SBSocketReconnectionFilter.class.getSimpleName());

    private I8SBSocketClient i8SBSocketClient;

    public I8SBSocketReconnectionFilter(I8SBSocketClient i8SBSocketClient) {
        this.i8SBSocketClient = i8SBSocketClient;
    }

    public void sessionClosed(NextFilter nextFilter, IoSession session) {
        // fire a connection retrying Thread
        logger.info("Session closed for: " + i8SBSocketClient.getI8SBSocketConnectionPropertiesVO().getChannelID());
        logger.info("Creating session again for: " + i8SBSocketClient.getI8SBSocketConnectionPropertiesVO().getChannelID());
        nextFilter.sessionClosed(session);
        i8SBSocketClient.connectSocket();
    }
}
