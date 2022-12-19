package com.inov8;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.EncryptionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AppInSnapTest {
    @Autowired
    private I8SBSwitchController switchController;
    String key = new String("682ede816988e58fb6d057d9d85605e0");

    private String encrypt(String input) {
        return EncryptionUtil.encryptWithAES(key, input);
    }

    private String decrypt(String input) {
        return EncryptionUtil.decryptWithAES(key, input);
    }

    @Test
    public void customerDataSet() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APPINSNAP);
        requestVO.setRequestType(I8SBConstants.RequestType_AppInSnap_CustomerDataSet);
        requestVO.setTransactionId("111");
        requestVO.setTransmissionDateAndTime("05/29/201505:50AM");
        requestVO.setSenderCnic("4210177764357");
        requestVO.setSenderCity("lahore");
        requestVO.setTransactionAmount("5000");
        requestVO.setSenderMobile("03256325562");
        requestVO.setRecieverCity("karachi");
        requestVO.setRecieverCnic("3520278888833");
        requestVO.setRecieverMobileNo("03212580158");
        requestVO.setReserved1("");
        requestVO.setReserved2("");
        requestVO.setReserved3("");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
