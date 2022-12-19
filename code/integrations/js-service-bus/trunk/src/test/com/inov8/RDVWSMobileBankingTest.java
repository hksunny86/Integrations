package com.inov8;

import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.EncryptionUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 5/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RDVWSMobileBankingTest {
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
    public void voucherCode365() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GetVoucherCode);

        requestVO.setPAN(encrypt("5245210154001258"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void customerRegistration() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_RegisterUser);


        requestVO.setEmail("muhammadusman55@gmail.com");
        requestVO.setNewPassword(encrypt("usman123"));
        requestVO.setUserName("usman151292");
        requestVO.setMobilePhone("03143136925");
        requestVO.setCNIC("4200051227219");
        requestVO.setFPIN(encrypt("1111"));
        requestVO.setPasscode(encrypt("1234"));
        requestVO.setPAN(encrypt("5245210154001258"));
        requestVO.setAccountId1(encrypt("01540101828183"));
        requestVO.setAuthToken("12345");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void authenticateCustomer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AuthenticateCustomer);

        requestVO.setCNIC("4200051227219");
        requestVO.setPAN(EncryptionUtil.encrypt("5245210154001258"));
        requestVO.setAccountId1(EncryptionUtil.encrypt("01540101828183"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void validateUsername() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_ValidateUsername);

        requestVO.setUserName("mwajahat");
        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void loginTransaction() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_Login);


        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan222"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void validatePANPIN() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_PanPinVerification);

        requestVO.setCarrier("1");
        requestVO.setFPIN( encrypt("1234"));
        requestVO.setPAN(encrypt("5245210154001258"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billPaymentBeneficiaryList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GetRegisteredConsumers);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan111"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ftBeneficiaryList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_BeneficiaryAccountList);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setPassword(encrypt("8E506557D9F1E4CF"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFTBeneficiaryList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_IBFTBeneficiaryAccountList);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan111"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFTFTBeneficiaryAddition() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_BeneficiaryMaintenance);

        requestVO.setCNIC("3520286656523");
        requestVO.setUserName("jahanze1");
        requestVO.setConsumerNickName("anss");
        requestVO.setPassword(encrypt("asdf1234"));
        requestVO.setActivityFlag("A");
        requestVO.setBankName("JS Bank");
        requestVO.setBranchName("DHA Phase IV Branch Lahore");
        requestVO.setBankImd("627873");
        requestVO.setBeneficiaryAccountTitle("Salman Zafar");
        requestVO.setAccountId2(encrypt("02080102242676"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFTFTBeneficiaryDeletion() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_BeneficiaryMaintenance);

        requestVO.setCNIC("3520286656523");
        requestVO.setUserName("jahanze1");
        requestVO.setPassword(encrypt("asdf1234"));
        requestVO.setActivityFlag("D");
        requestVO.setBankName("JS Bank");
        requestVO.setBranchName("DHA Phase IV Branch Lahore");
        requestVO.setBankImd("627873");
        requestVO.setBeneficiaryAccountTitle("Salman Zafar");
        requestVO.setAccountId2(encrypt("02080102242676"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void AddEform() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AddEForm);


        requestVO.setEmail("farantariq@gmail.com");
        requestVO.setNewPassword("g4yFPpUKU5gjscC9LIh1VQ==");
        requestVO.setAccountId1("2w9HUXL5F0YGLUq3JX7Mng==");
        requestVO.setDateOfBirth("04-07-1994");
        requestVO.setMobilePhone("03087877456");
        requestVO.setUserName("Faran");
        requestVO.setPAN("6uirEh/dSKEJ7T7EB2TTLA==");
        requestVO.setCNIC("3650299538509");
        requestVO.setTelephoneNumberHome("0404987632");
        requestVO.setRequestType("AddEForm");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);

        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billBeneficiaryAddition() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_UBPSCustomerRegistration);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setPassword(encrypt("8E506557D9F1E4CF"));
        requestVO.setActivityFlag("1");
        requestVO.setFirstName("KE HOME");
        requestVO.setCategoryCode("UT");
        requestVO.setCompanyCode("55");
        requestVO.setConsumerNickName("faran");
        requestVO.setConsumerNumber(encrypt("01371110001300"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billBeneficiaryDeletion() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_UBPSCustomerUnRegistration);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setPassword(encrypt("8E506557D9F1E4CF"));
        requestVO.setActivityFlag("0");
        requestVO.setFirstName("KE HOME");
        requestVO.setCategoryCode("UT");
        requestVO.setCompanyCode("55");
        requestVO.setFirstName("wajahat");
        requestVO.setConsumerNickName("wajahat");
        requestVO.setAccountNumber(encrypt("01371110001300"));
        requestVO.setConsumerNumber("030876541");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_BillInquiry);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan222"));
        requestVO.setConsumerNumber("00141000");
        requestVO.setCompanyCode("69");
        requestVO.setCategoryCode("UT");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_BillPayment);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setCategoryCode("2");
        requestVO.setCompanyCode("63");
        requestVO.setConsumerNumber("1700416100034");
        requestVO.setAmount("500");
        requestVO.setAccountId1(encrypt("01190101242647"));
        requestVO.setGLAccount("01190101242648");
        requestVO.setNarrative("Utility Bill Pay");
        requestVO.setChannelID("213");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void FTTitleFetch() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_TitleFetch);

        requestVO.setCNIC("4200051227219");
        requestVO.setBankImd("627873");
        requestVO.setAccountId2(encrypt("01040101663666"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void fundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_FundsTransfer);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setAccountId2(encrypt("01040101663666"));
        requestVO.setAmount("50");
        requestVO.setAccountId1(encrypt("01540102016409"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

//    @Test
//    public void fundTransferReversal() {
//        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//
//        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
//        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
//        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
//        requestVO.setRequestType(I8SBConstants.RequestType_FundsTransferReversal);
//
//        requestVO.setCNIC("4200051227219");
//        requestVO.setUserName("wajahat249");
//        requestVO.setNewPassword("8E506557D9F1E4CF");
//        requestVO.setSTAN("466047");
//        requestVO.setAccountId2("01040101663666");
//        requestVO.setAmount("200");
//        requestVO.setAccountId1("01540102016409");
//
//        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
//        assertNotNull(requestVO);
//        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
//        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
//    }

    @Test
    public void cardActivationDeActivation() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CardActivationDeactivation);

        requestVO.setCNIC("4200051227219");
        requestVO.setCardStatusFlag("001"); //002 for deactivation
        requestVO.setPAN(encrypt("4649510111057409"));


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void miniStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CoreAccountMiniStatement);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan123"));
        requestVO.setAccountId1(encrypt("01540101828183"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void fullStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountFullStatement);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan123"));
        requestVO.setAccountId1(encrypt("01540101828183"));
        requestVO.setFromDate("20171221");
        requestVO.setToDate("20180321");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void CustomerAccountInfoList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GetMyAccounts);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan111"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void CustomerInformation() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CustomerInformation);

        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan111"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ForgetUserNamePassword() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_ForgetCredentials);

        requestVO.setCNIC("4200051227219");
        requestVO.setFPIN(encrypt("1766"));
        requestVO.setPAN(encrypt("4375250143000651"));
        requestVO.setPasscode(encrypt("1234"));
        requestVO.setOldPassword(encrypt("1234"));
        requestVO.setNewPassword(encrypt("1234"));
        requestVO.setAuthToken("12345");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void changePassword() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_ChangePassword);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setNewPassword(encrypt("rehan111"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFTBankList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GetBankList);

        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFTTitleFetch() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_IBFTTitleFetch);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan111"));
        requestVO.setAmount("500");
        requestVO.setBankImd("600648");
        requestVO.setAccountId1(encrypt("04050102928247"));
        requestVO.setAccountId2(encrypt("01010100625466"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IBFT() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_InterBankFundTransfer);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("rehan169");
        requestVO.setPassword(encrypt("rehan222"));
        requestVO.setBranchName("LANDHI KARACHI");
        requestVO.setBankImd("100196");
        requestVO.setBeneficiaryAccountTitle("TITLE NOT AVAILABLE");
        requestVO.setAccountId2(encrypt("00020000011005325"));
        requestVO.setAmount("500");
        requestVO.setAccountId1(encrypt("01540101828183"));
        requestVO.setNarrative("IBFT Transfer");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void cardDetails() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_DebitCardDetails);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setPassword(encrypt("8E506557D9F1E4CF"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void balanceInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountBalanceInquiry);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("ghousahmed");
        requestVO.setPassword(encrypt("528B37B9FC149664"));
        requestVO.setAccountId1(encrypt("01540101828183"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void UBPSCompanyList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_UBPSCompaniesList);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void cardShoppingStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_CardShoppingStatement);

        requestVO.setCNIC("4200051227219");
        requestVO.setUserName("wajahat249");
        requestVO.setPassword(encrypt("8E506557D9F1E4CF"));
        requestVO.setPAN(encrypt("4649510154010745"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getBranchList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GetBranchList);

        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sendSMS() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_SendSMS);

        requestVO.setMobilePhone("923352255329");
        requestVO.setAccountId2(encrypt("4649520154001735"));
        requestVO.setSmsText(encrypt("Test Inov8"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sendEmail() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_Email);

        requestVO.setAccountId2(encrypt("4649520154001735"));
        requestVO.setEmail("muhammadusman55@gmail.com");
        requestVO.setEmailSubject("testing");
        requestVO.setEmailText("testing meezan email serivces");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void IVRGenerateOTP() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_IVR_Passcode);


        requestVO.setCarrier("1");
        requestVO.setAccountId1(encrypt("01540101828183"));
        requestVO.setCNIC("4200051227219");
        requestVO.setMobilePhone("03087877452");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());


    }

    @Test
    public void generateOTP() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_GenerateOTP);

        requestVO.setCarrier("6");
        requestVO.setMobilePhone("03087877235");
        requestVO.setCNIC("4200051227219");
        requestVO.setAccountId1(encrypt("0qBtOkFrBfRRHDriqaQeBw=="));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void authenticateOTP() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_AuthenticateOTP);

        //requestVO.setCNIC("4200051227219");
        requestVO.setPasscode(encrypt("1234"));
        requestVO.setAuthToken("12345");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
    @Test
    public void SIExecution() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_SI_EXECUTION);

        requestVO.setSIID("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void SIView() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_SIList);

        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void SIZakaat() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_ZAKAAT_FT_SI);

        requestVO.setTitleFetchStan("27219");
        requestVO.setFromAccountIBAN("PK4200051227219");
        requestVO.setToAccountNumber("004200051227219");
        requestVO.setFromAccountNumber("004200051227219");
        requestVO.setsIFrequency("219");
        requestVO.setsIStartDate("12032019");
        requestVO.setsIEndDate("13092019");
        requestVO.setsIEndOccurance("12");
        requestVO.setsIFrequencyDay("123");
        requestVO.setReminder("qwer");
        requestVO.setToAccountTitle("Bank");
        requestVO.setToBankIMD("1234");
        requestVO.setAmount1("323444");
        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
    @Test
    public void SIFundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_SIFundsTransfer);

        requestVO.setTitleFetchStan("27219");
        requestVO.setFromAccountIBAN("PK4200051227219");
        requestVO.setToAccountNumber("004200051227219");
        requestVO.setFromAccountIBAN("004200051227219");
        requestVO.setToAccountNumber("004200051227219");
        requestVO.setAmountTransaction("004200051227219");
        requestVO.setFromAccountNumber("004200051227219");
        requestVO.setsIFrequency("219");
        requestVO.setsIStartDate("12032019");
        requestVO.setsIEndDate("13092019");
        requestVO.setsIEndOccurance("12");
        requestVO.setsIFrequencyDay("123");
        requestVO.setReminder("qwer");
        requestVO.setCNIC("4200051227219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
    @Test
    public void SIDelete() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_MBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_WS);
        requestVO.setRequestType(I8SBConstants.RequestType_DELETE_SI);

        requestVO.setSIID("7219");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
}
