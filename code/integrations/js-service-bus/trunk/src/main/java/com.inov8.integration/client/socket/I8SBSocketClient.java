package com.inov8.integration.client.socket;

import com.inov8.integration.client.socket.codecs.I8SBSocketCodecFactory;
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class I8SBSocketClient {
    private static Logger logger = LoggerFactory.getLogger(I8SBSocketClient.class.getSimpleName());
    private IoSession session = null;
    private NioSocketConnector connector;
    private I8SBSocketClientHandler i8SBSocketClientHandler = new I8SBSocketClientHandler();
    private I8SBSocketClientInfo i8SBSocketClientInfo = new I8SBSocketClientInfo();
    private I8SBSocketCache i8SBSocketCache;

    public void setI8SBSocketCache(I8SBSocketCache i8SBSocketCache) {
        this.i8SBSocketCache = i8SBSocketCache;
    }

    private long writeTimeoutSec = 60L;
    private int readTimeoutSec = 60;
    private Object mutex = new Object();
    private SocketAddress remoteSocket;
    private I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO;

    public void connect() {
        try {
            logger.info("Preparing socket connection for : " + i8SBSocketConnectionPropertiesVO.getChannelID());
            this.remoteSocket = new InetSocketAddress(i8SBSocketConnectionPropertiesVO.getIp(), i8SBSocketConnectionPropertiesVO.getPort());
            this.connector = new NioSocketConnector();

            DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
            LoggingFilter loggingFilter = new LoggingFilter();
            chain.addLast("logging", loggingFilter);

            this.connector.setConnectTimeoutMillis(300000L);
            this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new I8SBSocketCodecFactory()));

            chain.addLast("executor2", new ExecutorFilter(new IoEventType[]{IoEventType.MESSAGE_RECEIVED}));
            chain.addLast("executor3", new ExecutorFilter(new IoEventType[]{IoEventType.MESSAGE_SENT}));
            chain.addLast("executor5", new ExecutorFilter(new IoEventType[]{IoEventType.WRITE}));
            chain.addLast("executor6", new ExecutorFilter(new IoEventType[]{IoEventType.EXCEPTION_CAUGHT}));

            i8SBSocketClientHandler.setI8SBSocketConnectionPropertiesVO(i8SBSocketConnectionPropertiesVO);
            i8SBSocketClientHandler.setI8SBSocketClientInfo(i8SBSocketClientInfo);
            i8SBSocketClientHandler.setI8SBSocketCache(i8SBSocketCache);

            this.connector.getFilterChain().addLast("reconnect", new I8SBSocketReconnectionFilter(this));
            this.connector.setHandler(this.i8SBSocketClientHandler);
            connectSocket();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void connectSocket() {

        logger.info("Connecting socket: " + i8SBSocketConnectionPropertiesVO.getChannelID());
        try {
            int retryCount = 0;
            while (!ping(i8SBSocketConnectionPropertiesVO.getIp(), i8SBSocketConnectionPropertiesVO.getPort())) {
                retryCount++;
                logger.error("External system is not accessible. Retry " + retryCount);
                Thread.sleep(i8SBSocketConnectionPropertiesVO.getRetryTimeInSeconds() * 1000);
            }

            synchronized (this.mutex) {
                this.i8SBSocketClientInfo.setConnected(false);
                this.i8SBSocketClientInfo.setConnecting(true);
                //this.i8SBSocketClientInfo.setAuthenticated(false);
            }
            while (true) {
                ConnectFuture connectFuture = this.connector.connect(remoteSocket);
                connectFuture.awaitUninterruptibly();
                if (connectFuture.isConnected()) {
                    logger.info("Socket connected: " + i8SBSocketConnectionPropertiesVO.getChannelID());
                    this.session = connectFuture.getSession();
                    this.i8SBSocketClientInfo.setIoSession(session);
                    this.i8SBSocketClientInfo.setConnected(true);
                    this.i8SBSocketClientInfo.setConnecting(false);
                    break;
                } else {
                    logger.error("Error: Failed to connect to the external system ", connectFuture.getException());
                }

                Thread.sleep(i8SBSocketConnectionPropertiesVO.getRetryTimeInSeconds() * 1000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public I8SBSocketClientInfo getI8SBSocketClientInfo() {
        return i8SBSocketClientInfo;
    }

    public void setI8SBSocketClientInfo(I8SBSocketClientInfo i8SBSocketClientInfo) {
        this.i8SBSocketClientInfo = i8SBSocketClientInfo;
    }

    public I8SBSocketConnectionPropertiesVO getI8SBSocketConnectionPropertiesVO() {
        return i8SBSocketConnectionPropertiesVO;
    }

    public void setI8SBSocketConnectionPropertiesVO(I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO) {
        this.i8SBSocketConnectionPropertiesVO = i8SBSocketConnectionPropertiesVO;
    }

    public void sendMessage(String str) {
        this.session.write(str);
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
            logger.info("Telnet Successful: " + requestSocket.getRemoteSocketAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ping = false;
        } catch (IOException e) {
            ping = false;
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (requestSocket != null) {
                    requestSocket.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return ping;
    }
}
