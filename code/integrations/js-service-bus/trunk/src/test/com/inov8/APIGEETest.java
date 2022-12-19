package com.inov8;

import com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut.BalanceInquiryRequest;
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
public class APIGEETest {

    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void sendPayMTCNRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_PayMTCN);

        requestVO.setMobilePhone("03226547564");
        requestVO.setAddress("221B Baker Street");
        requestVO.setCNIC("4210177764357");
        requestVO.setFirstName("Sherlock");
        requestVO.setLastName("Sherlock");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void balanceInquiryRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountBalanceInquiry);
        requestVO.setTransactionId("111");
        requestVO.setTransmissionDateAndTime("05/29/201505:50AM");
        requestVO.setCNIC("4210177764357");
        requestVO.setProductCode("115456");
        requestVO.setSenderMobile("03256325562");
        requestVO.setAgentId("03462225455");
        requestVO.setTerminalID("ksfsfd");
        requestVO.setAccessToken("kdjaskjfcahskld");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendCashWithdrawlRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_CashWithdrawal);
        requestVO.setTransactionId("111");
        requestVO.setTransmissionDateAndTime("05/29/201505:50AM");
        requestVO.setTransactionAmount("564454");
        requestVO.setConsumerNumber("03251554546");
        requestVO.setSenderMobile("032154584545");
        requestVO.setCNIC("4210177764357");
        requestVO.setProductCode("115456");
        requestVO.setSenderMobile("03256325562");
        requestVO.setAgentId("03462225455");
        requestVO.setSessionId("11111");
        requestVO.setTerminalID("ksfsfd");
        requestVO.setMobilePhone("0326545548");
        requestVO.setFingerTemplete("/Rk1SACAyMAAAAAEUAAABQAHgAMUAxQEAAABkKUBPAF2CAECMAHB0AEBtAI56AICaAI7mAEAqAI+QAEBLAJH6AEAwAJn9AIA/AJqGAIB6ALhuAEAXALoQAEB2AL7mAEB/ANpmAIASANuWAIDrAPBgAEB6APVrAEAvAPuRAEAXAREWAEB9ARPeAEBCARrwAIBuASBmAIDIASXXAEDwASXeAEAOASqsAIBMASxzAIBOATrYAED+AUFeAIA2AUe6AIAfAVK3AICdAVfUAIClAWVVAIAqAXHEAICKAXfQAECgAXmgAEDhAYnlAICVAYtXAEAZAZPRAICuAZVeAEAjAZlOAEDOAa5qAIAsAbXdAEA6AbZbAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==");
        requestVO.setFingerIndex("1");
        requestVO.setAreaName("SINDH");
        requestVO.setTempeleteType("ISO_19794_2");
        requestVO.setAccessToken("kdjaskjfcahskld");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendCashWithdrawlReversal() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_CashWithdrawalReversal);
        requestVO.setConsumerNumber("03251554546");
        requestVO.setTransactionAmount("200");
        requestVO.setTransactionId("111");
        requestVO.setTransmissionDateAndTime("05/29/201505:50AM");
        requestVO.setSenderMobile("03214642841");
        requestVO.setAgentId("03462225455");
        requestVO.setSessionId("126585552");
        requestVO.setCNIC("3520243953533");
        requestVO.setProductCode("115456");
        requestVO.setTerminalID("ksfsfd");
        requestVO.setAccessToken("kdjaskjfcahskld");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendTitleFetchRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_EOBI_TitleFetch);
        requestVO.setAmount("100");
        requestVO.setConsumerNumber("03200460403");
        requestVO.setSenderMobile("03232024067");
        requestVO.setConsumerNumber("03200460403");
        requestVO.setAccessToken("kdjaskjfcahskld");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());

    }

    @Test
    public void sendMoneyTransferresponse() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_EOBI_CashWithdrawal);
        requestVO.setAmount("100");
        requestVO.setConsumerNumber("03200460403");
        requestVO.setConsumerNumber("03200460403");
        requestVO.setSessionId("126585552");
        requestVO.setTransactionId("111");
        requestVO.setOtp("625325");
        requestVO.setAccessToken("kdjaskjfcahskld");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
    @Test
    public void sendAtmPinGenerationResponse(){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_ATM_PIN_GENERATION);

        requestVO.setRRN("123456789");
        requestVO.setTransmissionDateAndTime("1022124037");
        requestVO.setSTAN("511004");
        requestVO.setCardNumber("4374590001670702");
        requestVO.setConsumerNumber("4220188556725");
        requestVO.setNewMPin("123456789");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();

        System.out.println(responseVO.getResponseCode());
    }

    @Test
    public void sendCardDetails(){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_APIGEE);
        requestVO.setRequestType(I8SBConstants.RequestType_GET_CARD_DETAILS);
        requestVO.setRRN("123456789");
        requestVO.setMTI("0200");
        requestVO.setProcessingCode("734000");
        requestVO.setTransmissionDateAndTime("0101120302");
        requestVO.setSystemTraceAuditNumber("060000");
        requestVO.setTimeLocalTransaction("120302");
        requestVO.setDateLocalTransaction("0101");
        requestVO.setMerchantType("0069");
        requestVO.setCardNumber("6037331771019912015");
        requestVO.setCardType("DC");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();

        System.out.println(responseVO.getResponseCode());
    }
}
