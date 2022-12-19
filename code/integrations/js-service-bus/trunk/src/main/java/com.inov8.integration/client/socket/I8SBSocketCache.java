package com.inov8.integration.client.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Component
public class I8SBSocketCache {

    private static Logger logger = LoggerFactory.getLogger(I8SBSocketCache.class.getSimpleName());
    private Map<String, String> responsePool = new ConcurrentHashMap<String, String>();
    private Map<String, I8SBSocketClient> socketClientPool = new ConcurrentHashMap<String, I8SBSocketClient>();

    public void putResponse(String rrn, String response) {
        if (response != null) {
            responsePool.put(rrn, response);
            logger.debug("Response added in response pool for RRN: " + rrn);
            logger.debug("Response pool size: " + responsePool.size());
        }
    }

    public String getResponse(String rrn) {
        String response;
        if (rrn != null) {
            response = this.responsePool.get(rrn);
        } else {
            logger.error("RRN key does not exist in response pool map");
            throw new IllegalArgumentException("Invalid argument provided");
        }
        return response;
    }

    public String checkResponsePool(String rrn, int responseTimeOut, int retryTimeInSeconds) throws TimeoutException, InterruptedException {
        Long startTime = System.currentTimeMillis();
        while (true) {
            long elapsedTime = System.currentTimeMillis() - startTime.longValue();
            if (elapsedTime >= responseTimeOut * 1000) {
                logger.info("Request timeout error for rrn " + rrn);
                throw new TimeoutException("Response timeout occurred");
            }

            String response = getResponse(rrn);
            if (response != null) {
                logger.info("Response found in pool for RRN: " + rrn);
                this.removeResponse(rrn);
                return response;
            }

            Thread.sleep(retryTimeInSeconds * 1000);
        }
    }

    public boolean removeResponse(String rrn) {
        boolean status = false;
        String response;
        if (rrn != null) {
            response = this.responsePool.remove(rrn);
            if (response != null) {
                status = true;
                logger.info("Removing response from pool for RRN: " + rrn);
            }
        }
        return status;
    }

    public void putSocketClient(String channelId, I8SBSocketClient i8SBSocketClient) {
        socketClientPool.put(channelId, i8SBSocketClient);
        logger.debug("Socket client added in pool for: " + channelId);
        logger.debug("Socket client pool size: " + socketClientPool.size());
    }

    public I8SBSocketClient getSocketClient(String integrationType) {
        return this.socketClientPool.get(integrationType);
    }
}