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
public class KMBLTest {
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
    public void balanceInquiryTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_KMBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_OTHER);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_KMBL_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountBalanceInquiry);

        requestVO.setAccountId1(encrypt("123455678912"));
        requestVO.setMerchantQRCode("39349");
        requestVO.setCNIC("3310274937801");
        requestVO.setMobilePhone("03143136925");
        requestVO.setTerminalID("EJ389");
        requestVO.setAmount("500");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void cashDepositTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_KMBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_OTHER);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_KMBL_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CashDeposit);

        requestVO.setTransactionId("3889490943");
        requestVO.setTransmissionDateAndTime("20180531 10:18:00");
        requestVO.setAccountId1(EncryptionUtil.encrypt("2025297108"));
        requestVO.setAmount("500");
        requestVO.setMerchantQRCode("39349");
        requestVO.setCNIC("3310274937801");
        requestVO.setTerminalID("EJ389");
        requestVO.setMobilePhone("03143136925");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void cashWithdrawalTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_KMBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_OTHER);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_KMBL_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CashDeposit);

        requestVO.setPasscode(EncryptionUtil.encrypt("232081)"));
        requestVO.setTransactionId("");
        requestVO.setTransmissionDateAndTime("20180531 10:18:00");
        requestVO.setAccountId1(EncryptionUtil.encrypt("2025297108"));
        requestVO.setAmount("500");
        requestVO.setMerchantQRCode("39349");
        requestVO.setCNIC("3310274937801");
        requestVO.setTerminalID("EJ389");
        requestVO.setMobilePhone("03143136925");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void transactionInquiryTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_KMBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_OTHER);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_KMBL_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_TransactionInquiry);

        requestVO.setTxnRefNo("232081");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void transactionReversalTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_KMBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_OTHER);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_KMBL_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_TransactionReversal);

        requestVO.setOriginalTxnID("232081");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}