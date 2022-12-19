package com.inov8.integration.client.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by inov8 on 9/7/2017.
 */
public class I8SBSocketClientThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(I8SBSocketClientThread.class.getSimpleName());

    private I8SBSocketCache i8SBSocketCache;

    private I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO;
    I8SBSocketClient i8SBSocketClient;

    public void setI8SBSocketConnectionPropertiesVO(I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO) {
        this.i8SBSocketConnectionPropertiesVO = i8SBSocketConnectionPropertiesVO;
    }

    public void run () {
        logger.info("Executing socket connection thread for: " + this.getName());
        i8SBSocketClient = new I8SBSocketClient();
        i8SBSocketClient.setI8SBSocketConnectionPropertiesVO(i8SBSocketConnectionPropertiesVO);
        i8SBSocketClient.setI8SBSocketCache(i8SBSocketCache);
        i8SBSocketClient.connect();
        i8SBSocketCache.putSocketClient(i8SBSocketConnectionPropertiesVO.getChannelID(), i8SBSocketClient);
    }

    public void setI8SBSocketCache(I8SBSocketCache i8SBSocketCache) {
        this.i8SBSocketCache = i8SBSocketCache;
    }
}
