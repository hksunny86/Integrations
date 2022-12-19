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

/**
 * Created by Salman Khan on 7/2/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BopCashOutTest {

    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void accountRegistrationRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_BOPBLB);
        requestVO.setRequestType(I8SBConstants.RequestType_BOP_ProofOFLifeVerficationInquiry);

        requestVO.setMobileNumber("03226547564");
        requestVO.setAgentId("03226547564");
        requestVO.setDebitAccountNumber("221BBakerStreet");
        requestVO.setCNIC("4210177764357");
        requestVO.setSessionId("10");
        requestVO.setFingerIndex("01");
        requestVO.setFingerTemplete("aksdascklasc");
        requestVO.setSegmentId("01");
        requestVO.setTempeleteType("asd");
        requestVO.setRRN("1236543212345");
        requestVO.setTerminalID("blb");
        requestVO.setAgenCity("ma,nda");
        requestVO.setAgentLocation("mnadjknak");
        requestVO.setCardId("");
        requestVO.setMachineName("asd");
        requestVO.setIpAddress("asd");
        requestVO.setUdid("12");
        requestVO.setStatusFlag("12");
        requestVO.setTransactionType("01");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
