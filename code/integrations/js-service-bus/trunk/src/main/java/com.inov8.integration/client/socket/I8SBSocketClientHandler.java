package com.inov8.integration.client.socket;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class I8SBSocketClientHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(I8SBSocketClientHandler.class.getSimpleName());

    private I8SBSocketCache i8SBSocketCache;
    private I8SBSocketClientInfo i8SBSocketClientInfo;
    private I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void setI8SBSocketCache(I8SBSocketCache i8SBSocketCache) {
        this.i8SBSocketCache = i8SBSocketCache;
    }

    @Override
    public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3600);
        ((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
        ((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
        ((SocketSessionConfig) session.getConfig()).setReuseAddress(true);

        logger.info("Session opened for " + i8SBSocketConnectionPropertiesVO.getChannelID());
    }

    @Override
    public void sessionClosed(IoSession session) {
        logger.info("Session closed in socket client handler for " + i8SBSocketConnectionPropertiesVO.getChannelID());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("Session idle for " + i8SBSocketConnectionPropertiesVO.getChannelID());
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) {
        executorService.submit(new Callable<Object>() {
            public Object call() throws Exception {
                processMessageReceive(session, message);
                return null;
            }
        });
    }

    @Async
    private void processMessageReceive(IoSession session, Object message) {
        String response = (String) message;
        String rrn = "";
        logger.info("Message received from " + i8SBSocketConnectionPropertiesVO.getChannelID());
        // Compose RRN based on channel id
        if (response != null && response.length() > 0) {
            if (i8SBSocketConnectionPropertiesVO.getChannelID().equals(I8SBConstants.I8SB_Channel_ID_RDV_MB)) {
                String[] responseArr = response.split(Pattern.quote(I8SBConstants.PIPE_DELIMITER), -1);
                if (responseArr.length > 4) {
                    // Create rrn number - In this case TransactionID at index 3 is RRN
                    rrn = responseArr[3];
                }
            }
        }
        if (rrn.length() > 0) {
            this.i8SBSocketCache.putResponse(rrn, response);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("Exception occurred ", cause);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        logger.info("Session created for " + i8SBSocketConnectionPropertiesVO.getChannelID());
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
}
