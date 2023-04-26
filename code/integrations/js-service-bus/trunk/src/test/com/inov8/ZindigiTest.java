package com.inov8;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ZindigiTest {
    private static Logger logger = LoggerFactory.getLogger(ZindigiTest.class.getSimpleName());

    @Autowired
    I8SBSwitchController switchController;

    @Test
    public void sednP2MStatusUpdate() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ZINDIGI);
        requestVO.setRequestType(I8SBConstants.RequestType_ZINDIGI_P2M_STATUS_UPDATE);

       requestVO.setMobileNumber("03355913139");
       requestVO.setName("Approved");
       requestVO.setAddress("Approved");
       requestVO.setCnicFrontPic("Approved");
       requestVO.setCnicBackPic("Approved");
       requestVO.setCustomerPic("Approved");
       requestVO.setStatus("R");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
