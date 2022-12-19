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

/**
 * Created by Salman Khan on 7/2/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AMMATest {
    private static Logger logger = LoggerFactory.getLogger(AMMATest.class.getSimpleName());

    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void sendPmdcRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_AKBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_AMMA);
        requestVO.setRequestType(I8SBConstants.RequestType_AMMA_Update_Account);

        requestVO.setInstrumentDate("12032019");
        requestVO.setRequestXML("7564");
        requestVO.setBankImd("364");
        requestVO.setAccountType("3226547564");
        requestVO.setAccountNumber("3226547564");
        requestVO.setMobilePhone("3226547564");
        requestVO.setAccountTitle("3226547564");
        requestVO.setIsRegistered("0");
        requestVO.setIsEnabled("1");
        requestVO.setIsPinSet("1");
        requestVO.setIsClose("0");
        requestVO.setCNIC("4210177764357");
        requestVO.setReserved1("123121");
        requestVO.setReserved2("123121");
        requestVO.setReserved3("123121");
        requestVO.setReserved4("123121");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        logger.info("",responseVO.getResponseCode());
    }



}
