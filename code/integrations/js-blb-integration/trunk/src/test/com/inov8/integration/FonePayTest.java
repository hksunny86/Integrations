package com.inov8.integration;

import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.service.ClientIntegrationService;
import com.inov8.integration.middleware.util.EncryptionUtil;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class FonePayTest {

    @Autowired
    private WebServiceSwitchController switchController;


    Logger LOGGER = LoggerFactory.getLogger(FonePayTest.class);

    String key = new String("682ede816988e58fb6d057d9d85605e0");

    private String encrypt(String input) {
        return EncryptionUtil.encryptWithAES(key, input);
    }

    private String decrypt(String input) {
        return EncryptionUtil.decryptWithAES(key, input);
    }



    @Test
    public void AccountVerify() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setCnicNo("3450254895771");
        messageVO.setDateTime("170713120823");
        messageVO.setMobileNo("03332221111");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setTransactionType("02");
        messageVO.setChannelId("0012");
        messageVO = switchController.verifyAccount(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void AccountOpening() {
        WebServiceVO messageVO = new WebServiceVO();

//        messageVO.setUserName("User");
//        messageVO.setCustomerPassword("123222");
        messageVO.setCnicNo("3454895771");
        messageVO.setDateTime("0825042525");
        messageVO.setMobileNo("0331111");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setConsumerName("کے اعداد و شمار کے اعداد و شمار");
        messageVO.setAccountTitle(" کے اعداد و شمار کے اعداد و شمار کے اعداد و شمار");
        messageVO.setBirthPlace("کے اعداد و شمار کے اعداد و شمار");
        messageVO.setPresentAddress("kk");
        messageVO.setCnicStatus("00");
        messageVO.setCnicExpiry("12");
        messageVO.setDateOfBirth("121290");
        messageVO.setFatherHusbandName("");
        messageVO.setMotherMaiden("");
        messageVO.setGender("");
        messageVO.setAccountType("02");
        messageVO.setChannelId("FONEPAY");
//        messageVO.setTrackingId("1112");
        messageVO = switchController.accountOpening(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void conventionalAccountOpening() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setCnicNo("3450254895771");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setMobileNo("03332221111");
        messageVO.setConsumerName("Consumer");
        messageVO.setCnicExpiry("1220");
        messageVO.setDateOfBirth("121290");
        messageVO.setCustomerPhoto("kjdflkfsldkjfsdif399933kjfsd");
        messageVO.setCnicFrontPhoto("kjdflkfsldkjfsdif399933kjfsd");
        messageVO.setCnicBackPhoto("kjdflkfsldkjfsdif399933kjfsd");
        messageVO.setSignaturePhoto("kjdflkfsldkjfsdif399933kjfsd");
        messageVO.setTermsPhoto("kjdflkfsldkjfsdif399933kjfsd");
        messageVO.setChannelId("122");
        messageVO.setAccountType("blb");
        messageVO = switchController.conventionalAccountOpening(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void PaymentInquiry() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setMobileNo("03332221111");
        messageVO.setDateTime("170713120823");
        messageVO.setTransactionAmount("200");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setTransactionType("02");
        messageVO.setChannelId("11122");
        messageVO = switchController.paymentInquiry(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }

    @Test
    public void PaymentRequest() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setAccountNo1("111112222558889");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setTransactionAmount("200");
        messageVO.setMicrobankTransactionCode("88848484");
        messageVO.setCharges("20");
        messageVO.setTransactionType("02");
        messageVO.setMobilePin("1234");
        messageVO.setTerminalId("44455");
        messageVO.setPaymentType("02");
        messageVO.setChannelId("0012");
        messageVO = switchController.paymentRequest(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void PaymentReversal() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setMicrobankTransactionCode("20");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("88888888");
        messageVO.setChannelId("112");
        messageVO = switchController.paymentReversal(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void OtpVerification() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setOtpPin("1234");
        messageVO.setMobileNo("03332221111");
        messageVO.setCnicNo("22255588888888");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setChannelId("02");
        messageVO = switchController.otpVerification(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void CardTagging() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setCardExpiry("111112222558889");
        messageVO.setCardNo("111112222558889");
        messageVO.setFirstName("Gulfam");
        messageVO.setLastName("Suleman");
        messageVO.setDateTime("170713120823");
        messageVO.setTransactionId("123654");
        messageVO.setMobileNo("03200544200");
        messageVO.setCnicNo("3450285478551");
        messageVO.setChannelId("1112");
        messageVO = switchController.cardTagging(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void accountLinkDeLink() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setTransactionType("02");
        messageVO.setMobileNo("03332221111");
        messageVO.setCnicNo("22255588888888");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setChannelId("25455");
        messageVO = switchController.accountLinkDelink(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
    @Test
    public void setCardStatus() {
        WebServiceVO messageVO = new WebServiceVO();

        messageVO.setUserName("User");
        messageVO.setCustomerPassword("123222");
        messageVO.setTransactionType("01");
        messageVO.setMobileNo("03332221111");
        messageVO.setCnicNo("22255588888888");
        messageVO.setDateTime("170713120823");
        messageVO.setRetrievalReferenceNumber("123654");
        messageVO.setCardNo("5555555555555555");
        messageVO.setChannelId("25455");
        messageVO = switchController.setCardStatus(messageVO);

        assertNotNull(messageVO);
        assertNotNull(messageVO.getResponseCode());
        assertEquals(ResponseCodeEnum.PROCESSED_OK.getValue(), messageVO.getResponseCode());

    }
}
