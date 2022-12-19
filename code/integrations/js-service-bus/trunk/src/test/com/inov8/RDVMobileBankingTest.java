package com.inov8;

//import com.inov8.integration.channel.rdv.mb.enums.RDVMB_TransactionCodeEnum;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.util.EncryptionUtil;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RDVMobileBankingTest {

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
    public void LDAPRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_LDAPRequest);

        requestVO.setActivityFlag("S");
//        requestVO.setUserId("admin");
        requestVO.setPassword("inov8@com");
        requestVO.setEmail("inov8@com");
        requestVO.setUserName("gulfam");
        requestVO.setDomainName("testdomain.com");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sendSms() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_SendSMS);

        requestVO.setMobilePhone("03216906143");
        requestVO.setSmsText(EncryptionUtil.encrypt("Dear Customer this is test message"));
        requestVO.setSmsTransactionNature("N");
        requestVO.setSmsTransactionType("FPIN");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void PanPinVerification() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CustomerValidation);

        requestVO.setPAN(EncryptionUtil.encrypt("0987654321123456"));
        requestVO.setPinData(encrypt("1111"));
        requestVO.setCNIC("3540123329249");
        requestVO.setDateOfBirth(DateTime.now().toString());

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void CNICValidationTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CNICValidation);

        requestVO.setCNIC("3562478642547");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Ignore
    public void getPasswordTest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        //requestVO.setRequestType(I8SBConstants.RequestType_GetPassword);

        requestVO.setUserId("3456202569887");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getBalanceInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountBalanceInquiry);

        requestVO.setUserId("12345698745632");
        requestVO.setCNIC("12345698745632");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");
        requestVO.setAccountNumber(EncryptionUtil.encrypt("123456789"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getTitleFetch() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_TitleFetch);

        requestVO.setUserId("12345698745632");
        requestVO.setCNIC("12345698745632");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");
        requestVO.setAccountId2("12345698745632");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getFundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_FundsTransfer);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");
        requestVO.setAccountId1(EncryptionUtil.encrypt("123456789"));
        requestVO.setAccountId2(EncryptionUtil.encrypt("1234567891"));
        requestVO.setAmount("500");
        requestVO.setFPIN(EncryptionUtil.encrypt("1234"));
        requestVO.setTransactionFees("100");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ibftFundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_InterBankFundTransfer);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("12345698745632");
        requestVO.setBankImd("some");
        requestVO.setBankName("some");
        requestVO.setBranchName("some");
        requestVO.setAccountId2("12345678903210");
        requestVO.setAccountTitle("Title");
        requestVO.setAmount("500");
        requestVO.setFPIN("1234");
        requestVO.setTransactionFees("100");
        requestVO.setTransferPurpose("IBFT");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getFeeTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_FeeTransfer);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");
        requestVO.setAccountId1("123456789");
        requestVO.setAmount("500");
        requestVO.setMPIN("1234");
        requestVO.setTransactionFees("100");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }


    @Test
    public void debitCardList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_DebitCardList);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3562478642547");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void creditCardList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CreditCardList);

        requestVO.setUserId("saadsarwar1");
        requestVO.setCNIC("3520154140846");
        requestVO.setRelationshipNumber("474934");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword(EncryptionUtil.encrypt("222555888"));


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void prepaidCardList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardList);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890124");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void checkingAccountSummary() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CheckingAccountSummary);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3840171236695");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void LoanAccountSummary() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_LoanAccountSummary);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234569874562");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void TDRAccountSummary() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_TDRAccountSummary);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234569874562");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void LoanAccountDetail() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_LoanAccountStatement);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("some");
        requestVO.setProductCode("some");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void TDRSStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_TDRAccountStatement);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("some");
        requestVO.setProductCode("some");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void accountFullStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_AccountFullStatement);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1(EncryptionUtil.encrypt("22222222"));
        requestVO.setFromDate("20/02/2018");
        requestVO.setToDate("20-02-2018");
        requestVO.setFromAmmount("200");
        requestVO.setToAmmount("200");
        requestVO.setRequestingTranCode("2323");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void UBPSCustomerRegistration() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_UBPSCustomerRegistration);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setCategoryCode("some");
        requestVO.setCompanyCode("some");
        requestVO.setConsumerNumber("some");
        requestVO.setConsumerNickName("some");
        requestVO.setFPIN("some");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void UBPSCustomerUnRegistration() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_UBPSCustomerUnRegistration);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setCategoryCode("some");
        requestVO.setCompanyCode("some");
        requestVO.setConsumerNumber("some");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void CCBillInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CreditCardBillInquiry);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setPAN(EncryptionUtil.encrypt("4444987654321012"));

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void billInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_BillInquiry);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setCategoryCode("some");
        requestVO.setCompanyCode("some");
        requestVO.setCompanyName("some");
        requestVO.setConsumerNumber("1234567890123992");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void prepaidCardRecharge() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardRecharge);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("12345698745631");
        requestVO.setAccountId2("12345698745632");
        requestVO.setAmount("500");
        requestVO.setFPIN("some");
        requestVO.setTransactionFees("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getCCPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CreditCardPayment);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setCategoryCode("some");
        requestVO.setCompanyCode("some");
        requestVO.setConsumerNumber("1234567890123992");
        requestVO.setAccountId1("12345698745631");
        requestVO.setAmount("500");
        requestVO.setFPIN("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }


    @Test
    public void getBillPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_BillPayment);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setCategoryCode("some");
        requestVO.setCompanyCode("some");
        requestVO.setCompanyName("some");
        requestVO.setConsumerNumber("1234567890123992");
        requestVO.setAccountId1("123456789");
        requestVO.setAmount("500");
        requestVO.setFPIN("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }


    @Test
    public void generateFPIN() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_GenerateFPIN);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setFPINExpiry("some");
        requestVO.setFPINExpiryCheck("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void cardDetails() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_DebitCardDetails);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setPAN("12369874521456");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void cardDetailsPPC() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardDetails);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setPAN("12369874521456");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getCCMiniStatement() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CreditCardMiniStatement);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO.setPAN("1234567890123456");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getPPCBeneficiaryAccountList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardBeneficiaryAccountList);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234569874561");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getBeneficiaryAccountList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_BeneficiaryAccountList);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3840171236695");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getIBFTBeneficiaryAccountList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_IBFTBeneficiaryAccountList);


        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ppcTitleFetch() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardTitleFetch);


        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO.setPAN("1234567890123456");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void beneficiaryMaintenance() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_BeneficiaryMaintenance);


        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890124");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO.setActivityFlag("A");
        requestVO.setAccountId1("4444567890123459");
        requestVO.setAccountTitle("some");
        requestVO.setBeneficiaryAccountTitle("ss");
        requestVO.setFPIN("1234567890123992");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ibftBeneficiaryMaintenance() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_IBFTBeneficiaryMaintenance);


        requestVO.setUserId("1234567890123993");
        requestVO.setCNIC("3456202569886");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO.setActivityFlag("D");
        requestVO.setAccountId1("12345678901");
        requestVO.setAccountTitle("some");
        requestVO.setBeneficiaryAccountTitle("JHH");
        requestVO.setBankImd("some");
        requestVO.setBankName("some");
        requestVO.setFPIN("1234567890123992");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void ppcBeneficiaryMaintenance() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_PrepaidCardBeneficiaryMaintenance);


        requestVO.setUserId("1234567890123993");
        requestVO.setCNIC("1234569874561");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");


        requestVO.setActivityFlag("A");
        requestVO.setAccountId1("4444567890123455");
        requestVO.setAccountTitle("some");
        requestVO.setBeneficiaryAccountTitle("JHH");
        requestVO.setFPIN("1234567890123992");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getRegisteredConsumer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_GetRegisteredConsumers);


        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getMyAccounts() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_GetMyAccounts);


        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3562478642547");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sIBillPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_SIBillPayment);

        requestVO.setUserId("khokhar2017");
        requestVO.setCNIC("3520154140846");
        requestVO.setRelationshipNumber("658");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("");

        requestVO.setCategoryCode("000");
        requestVO.setCompanyCode("LESCO001");
        requestVO.setCompanyName("LESCO001");
        requestVO.setConsumerNumber("123000123");
        requestVO.setAccountId1(EncryptionUtil.encrypt("12345678909876"));
        requestVO.setAmount("5000");
        requestVO.setFPIN(EncryptionUtil.encrypt("1234"));
        requestVO.setStartDate("22/02/2018");
        requestVO.setFrequency("1");

        requestVO.setTotalNumberOfExecution("2");
        requestVO.setSIID("7450");

        requestVO.setActivityFlag("D");

        requestVO.setSITranCode("1033");
        requestVO.setTransactionFees("0");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sIFundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_SIFundsTransfer);

        requestVO.setUserId("khokhar2017");
        requestVO.setCNIC("3520154140846");
        requestVO.setRelationshipNumber("658");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1(EncryptionUtil.encrypt("12345698745631"));
        requestVO.setAccountId2(EncryptionUtil.encrypt("12345678903210"));
        requestVO.setAmount("500");
        requestVO.setFPIN(EncryptionUtil.encrypt("1234"));
        requestVO.setTransactionFees("100");
        requestVO.setStartDate("some");
        requestVO.setFrequency("some");
        requestVO.setTotalNumberOfExecution("2");
        requestVO.setSIID("11");

        requestVO.setActivityFlag("A");

//        requestVO.setSITranCode(RDVMB_TransactionCodeEnum.SIFundsTransfer.getValue());

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void sIIbftFundTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_SIIBFT);

        requestVO.setUserId("khokhar2017");
        requestVO.setCNIC("3520154140846");
        requestVO.setRelationshipNumber("658");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("12345698745631");
        requestVO.setBankImd("some");
        requestVO.setAccountId2("12345678903210");
        requestVO.setAmount("500");
        requestVO.setFPIN("1234");
        requestVO.setTransactionFees("100");
        requestVO.setStartDate("some");
        requestVO.setFrequency("some");
        requestVO.setTotalNumberOfExecution("2");
        requestVO.setSIID("12");

        requestVO.setActivityFlag("D");

//        requestVO.setSITranCode(RDVMB_TransactionCodeEnum.SIIBFT.getValue());


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }


    @Test
    public void sIList() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_SIList);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("3520154140846");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1(EncryptionUtil.encrypt("12345678909876"));


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void cardLessWithDrawl() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_CardLessWithdrawalRequest);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("12345698745631");
        requestVO.setAmount("50");
        requestVO.setFPIN("some");
        requestVO.setTransactionFees("0.2");
        requestVO.setBeneficiaryCNIC("1234567890123991");
        requestVO.setBeneficiaryMobile("5554545");
        requestVO.setTxnRefNo("some");
        requestVO.setSenderMobile("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void mVisaPushPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_mVisaPushPayment);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setPAN(encrypt("12345698745631"));
        requestVO.setAmount("500");
        requestVO.setTxnCurrency("PKR");
        requestVO.setMerchantName("some");
        requestVO.setMerchantCity("Lahore");
        requestVO.setMerchantCountry("Pakistan");
        requestVO.setMerchantReferenceNum("some");
        requestVO.setMerchantPurchaseType("some");
        requestVO.setAcquirerCountryISO("some");
        requestVO.setAcquirerBIN("some");
        requestVO.setVisaBusinessAppId("some");
        requestVO.setVisaIdCode("some");
        requestVO.setVisaIdName("some");
        requestVO.setMerchantId("222");
        requestVO.setVisaFeeProgramIndicator("some");
        requestVO.setFPIN(encrypt("1245"));


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void qrMerchantTransfer() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_QRMerchantTransfer);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setAccountId1("12345698745631");
        requestVO.setAccountId1Type("12345698745631");
        requestVO.setAccountId2("12345698745631");
        requestVO.setAmount("500");
        requestVO.setFPIN("some");
        requestVO.setTransactionFees("some");
        requestVO.setMerchantId("Lahore");
        requestVO.setMerchantName("some");
        requestVO.setMerchantQRCode("some");
        requestVO.setAdditionalDetails("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void merchantSummaryRequest() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_FONEPAY);
        requestVO.setRequestType(I8SBConstants.RequestType_MPASS_MerchantDetail);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setMerchantQRCode("some");
        requestVO.setTransmissionDateAndTime("some");
        requestVO.setMerchantId("some");
        requestVO.setBankId("some");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void mPassPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_FONEPAY);
        requestVO.setRequestType(I8SBConstants.RequestType_MPASS_Payment);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setMobilePhone("some");
        requestVO.setEmail("some");
        requestVO.setTransactionAmount("some");
        requestVO.setMerchantId("some");
        requestVO.setTransmissionDateAndTime("some");
        requestVO.setRRN("some");
        requestVO.setBankId("some");



        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void qRTransferReversal() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
        requestVO.setRequestType(I8SBConstants.RequestType_QRTransferReversal);

        requestVO.setUserId("1234567890123992");
        requestVO.setCNIC("1234567890123992");
        requestVO.setRelationshipNumber("555555555");
        requestVO.setPasswordBitmap("23564");
        requestVO.setPassword("222555888");

        requestVO.setOriginalTxnDateTime("some");
        requestVO.setOriginalTxnID("some");
        requestVO.setOriginalSTAN("some");
        int count = 1;

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
}
