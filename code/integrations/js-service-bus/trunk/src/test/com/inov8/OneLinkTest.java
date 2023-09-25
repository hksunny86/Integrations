package com.inov8;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.EncryptionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class OneLinkTest {
    private static Logger logger = LoggerFactory.getLogger(OneLinkTest.class.getSimpleName());

    @Autowired
    I8SBSwitchController switchController;

    @Test
    public void sendOneLinkIbftTitleFetch() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ONE_LINK);
        requestVO.setRequestType(I8SBConstants.RequestType_ONE_LINK_IBFT_TITLE_FETCH);

        requestVO.setPAN("");
        requestVO.setTransactionAmount("100");
        requestVO.setTransactionDateTime("20230915201527");
        requestVO.setMerchantType("0088");
        requestVO.setPointOfEntry("");
        requestVO.setNetworkIdentifier("");
        requestVO.setCardAcceptorTerminalId("");
        requestVO.setCardAcceptorIdentificationCode("");
        requestVO.setCardAcceptorNameAndLocation("");
        requestVO.setTransferPurpose("BANK");
        requestVO.setCurrencyCode("PKR");
        requestVO.setAccountId1(EncryptionUtil.encrypt("03051234567"));
        requestVO.setAccountId2(EncryptionUtil.encrypt("03332345687"));
        requestVO.setToBankIMD("");
        requestVO.setSTAN("202322090426");
        requestVO.setRRN("092220230426");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendOneLinkIbftAdvice() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_ONE_LINK);
        requestVO.setRequestType(I8SBConstants.RequestType_ONE_LINK_IBFT_ADVICE);

        requestVO.setPAN("");
        requestVO.setTransactionAmount("100");
        requestVO.setTransactionDateTime("20230915201527");
        requestVO.setMerchantType("0088");
        requestVO.setPointOfEntry("");
        requestVO.setNetworkIdentifier("");
        requestVO.setCardAcceptorTerminalId("");
        requestVO.setCardAcceptorIdentificationCode("");
        requestVO.setCardAcceptorNameAndLocation("");
        requestVO.setTransferPurpose("BANK");
        requestVO.setCurrencyCode("PKR");
        requestVO.setAccountId1(EncryptionUtil.encrypt("03051234567"));
        requestVO.setAccountId2(EncryptionUtil.encrypt("03332345687"));
        requestVO.setToBankIMD("");
        requestVO.setSTAN("202322090426");
        requestVO.setRRN("092220230426");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
