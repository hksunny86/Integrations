package com.inov8.integration.client.socket;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inov8 on 9/7/2017.
 */
@Component
public class I8SBSocketConnectionInitializer {
    private static Logger logger = LoggerFactory.getLogger(I8SBSocketConnectionInitializer.class.getSimpleName());
    private List<I8SBSocketConnectionPropertiesVO> i8SBSocketConnectionPropertiesVOList = new ArrayList<I8SBSocketConnectionPropertiesVO>();
    @Autowired
    I8SBSocketCache i8SBSocketCache;

    @PostConstruct
    public void init() {
        try {
            populateConnectionPropertiesVOList();
            logger.info("Initializing mapped socket connections..");
            logger.info("Total mapped sockets are: " + i8SBSocketConnectionPropertiesVOList.size());
            if (i8SBSocketConnectionPropertiesVOList.size() > 0) {
                I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO;
                I8SBSocketClientThread i8SBSocketClientThread;
                for (int i = 0; i < i8SBSocketConnectionPropertiesVOList.size(); i++) {
                    i8SBSocketConnectionPropertiesVO = i8SBSocketConnectionPropertiesVOList.get(i);
                    i8SBSocketClientThread = new I8SBSocketClientThread();
                    i8SBSocketClientThread.setI8SBSocketCache(i8SBSocketCache);
                    i8SBSocketClientThread.setName(i8SBSocketConnectionPropertiesVO.getChannelID());
                    i8SBSocketClientThread.setI8SBSocketConnectionPropertiesVO(i8SBSocketConnectionPropertiesVO);
                    i8SBSocketClientThread.start();
                }
            } else {
                logger.info("No socket connection properties available.");
            }

        } catch (Exception e) {
            logger.error("Error occurred while initializing socket connection " + e);
        }
    }

    private void populateConnectionPropertiesVOList() {
        I8SBSocketConnectionPropertiesVO i8SBSocketConnectionPropertiesVO = new I8SBSocketConnectionPropertiesVO();

        if (PropertyReader.getProperty("rdv.mb.ip") != null && PropertyReader.getProperty("rdv.mb.ip").length() > 0) {
            i8SBSocketConnectionPropertiesVO.setChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
            i8SBSocketConnectionPropertiesVO.setIp(PropertyReader.getProperty("rdv.mb.ip"));
            i8SBSocketConnectionPropertiesVO.setPort(Integer.parseInt(PropertyReader.getProperty("rdv.mb.port")));
            i8SBSocketConnectionPropertiesVO.setRetryTimeInSeconds(Integer.parseInt(PropertyReader.getProperty("rdv.mb.retry")));
            i8SBSocketConnectionPropertiesVO.setResponseTimeOutInSeconds(Integer.parseInt(PropertyReader.getProperty("rdv.mb.timeout")));
            i8SBSocketConnectionPropertiesVOList.add(i8SBSocketConnectionPropertiesVO);
        }
    }
}
