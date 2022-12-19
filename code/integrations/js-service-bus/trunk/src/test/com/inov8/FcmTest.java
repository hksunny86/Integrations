package com.inov8;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class FcmTest {

    @Autowired
    private I8SBSwitchController switchController;


    @Test
    public void sendPayMTCNRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_BOP);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_FCM);
        requestVO.setRequestType(I8SBConstants.RequestType_Notification);

        requestVO.setDeviceKey("cjAMJowGp6c:APA91bFeUjXpBGnMNFhc3gLd2q6II1ckyzdy43LvG7TMFK4_FQgO2SuYoxHTMh4KI5H4rQkuyov45fdWHETkC9ksa7vJ--8hjNWmKLzyA3EQ5u2VPDLlBbTesa_zIwrB8wyUFVZQh-10");
        requestVO.setMessage("request");
        requestVO.setNotificationTypeId("2");
        requestVO.setUserType("2");
        requestVO.setCpId("444");
        requestVO.setOsType("Android");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
