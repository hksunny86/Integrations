package com.inov8;

import com.inov8.integration.config.PropertyReader;
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
public class EsbTest {
    private static Logger logger = LoggerFactory.getLogger(EsbTest.class.getSimpleName());

    @Autowired
    I8SBSwitchController switchController;

    @Test
    public void sendEsbBillInquiry() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ESB);
        requestVO.setRequestType(I8SBConstants.RequestType_ESB_BILL_INQUIRRY);

        requestVO.setProcessingCode("RCDPbillInquiry");
        requestVO.setDateTime("20230915201527");
        requestVO.setTraceNo("500202");
        requestVO.setMerchantType("0088");
        requestVO.setUserName("BLP.JS");
        requestVO.setPassword("Rcdp@(123)");
        requestVO.setConsumerNumber("910000005");
        requestVO.setBankMnemonic("RCDP");
        requestVO.setReserved1("");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendEsbBillPayment() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ESB);
        requestVO.setRequestType(I8SBConstants.RequestType_ESB_BILL_PAYMENT);

        requestVO.setProcessingCode("RCDPbillInquiry");
        requestVO.setDateTime("20230915201527");
        requestVO.setTraceNo("500202");
        requestVO.setMerchantType("0088");
        requestVO.setUserName("BLP.JS");
        requestVO.setPassword("Rcdp@(123)");
        requestVO.setConsumerNumber("910000005");
        requestVO.setTransactionId("910000005");
        requestVO.setTransactionAmount("5000");
        requestVO.setTransactionDate("20211108");
        requestVO.setTransmissionDateAndTime("1140");
        requestVO.setBankMnemonic("RCDP");
        requestVO.setReserved1("");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
