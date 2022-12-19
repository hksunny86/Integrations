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
public class BopMBTest {
    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void accountStatementRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_BOP);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_BOPMB);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountStatment);

        requestVO.setCNIC("3460210637811");
        requestVO.setEmail("");
        requestVO.setFromDate("01-Jun-21");
        requestVO.setToDate("18-Jun-21");
        requestVO.setAccountNumber("6020222933800018");
        requestVO.setPAN("3460210637811");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(requestVO.toString());
        System.out.println(requestVO);
        System.out.println(responseVO.getResponseCode());
    }
}
