package com.inov8.integration.test;

import com.inov8.integration.middleware.controller.NadraIntegrationControllerImpl;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.vo.TransactionRequest;
import com.inov8.integration.vo.TransactionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by inov8 on 3/29/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class NadaraIntegrationTest {
    private Logger LOG = LoggerFactory.getLogger(NadaraIntegrationTest.class);
    @Autowired
    NadraIntegrationControllerImpl nadraIntegrationController;

    @Before
    public void init() {
    }

    @Test
    public void fingerprintVerificationTest() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("2222");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("3520255166837");
        messageVO.setContactNo("03214848568");
        messageVO.setFingerIndex("2");
        messageVO.setFingerTemplate("VGVzdA==");
        messageVO.setTemplateType("RAW_IMAGE");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO.setAreaName("lahore");
        try {
            messageVO = nadraIntegrationController.fingerPrintVerification(messageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void DirectfingerprintVerificationTest() {
        LOG.info("START: Finger Print Verification Test");
        TransactionRequest messageVO = new TransactionRequest();
        messageVO.setFranchiseeID("2222");
        messageVO.setUsername("user1");
        messageVO.setCitizenNumber("3520255166837");
        messageVO.setContactNumber("03214848568");
        messageVO.setFingerIndex("5");
        messageVO.setFingerTemplate("VGVzdA==");
        messageVO.setTemplateType("RAW_IMAGE");
        messageVO.setTransactionId("44888");
        messageVO.setAreaName("lahore");
        String json;
        json = JSONUtil.getJSON(messageVO);

        LOG.info("END:Json is :"+json);

        TransactionResponse messageVO1 = new TransactionResponse();
        messageVO1.setResponseCode("00");
        messageVO1.setMessage("successful");
        messageVO1.setSessionId(null);
        messageVO1.setCitizenNumber("45454545454545");
        messageVO1.setCitizenName("Gul");
        messageVO1.setPresentAddress("address");
        messageVO1.setBirthPlace("lahore");
        messageVO1.setCardExpired("1212");

        messageVO1.setDateOfBirth("23-12-2017");
        messageVO1.setFingerIndex("2");
        messageVO1.setReligion("Islam");
        messageVO1.setMotherName("Mother");
        messageVO1.setNativeLanguage("44888");
        messageVO1.setPhotograph(null);
        String json1;
        json1 = JSONUtil.getJSON(messageVO1);

        LOG.info("END:Response Json is :"+json1);
    }

    @Test
    public void secretIdentityDemographicsData() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("1257");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setSessionId("444445kijuhy");
        messageVO.setCitizenNumber("4565886967555");
        messageVO.setContactNo("8889999999");
//        messageVO.setFingerIndex(4);
//        messageVO.setFingerTemplate("kkkd999");
        messageVO.setTemplateType("Non");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO.setAreaName("lahore");
        messageVO = nadraIntegrationController.getSecretIdentityDemographicsData(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void manualVerificationDataTest() {
        LOG.info("START: Secret Identity Demographics Data Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("12587");
        messageVO.setSessionId("44444uytrewq");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("9520225166837");
        messageVO.setVerificationResult("llllllll");
        messageVO = nadraIntegrationController.getManualVerificationResult(messageVO);
        LOG.info("END:Secret Identity Demographics Data Test");
    }

    @Test
    public void lastVerificationResult() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("12587");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("9520225166837");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO = nadraIntegrationController.getLastVerificationResult(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void submitMobileBankAccountDetails() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("12587");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setSessionId("444445kijuhy");
        messageVO.setCitizenNumber("9520225166837");
        messageVO.setAccountNumber("4444444444");
        messageVO.setAccountLevel("58888888889");
        messageVO = nadraIntegrationController.submitMobileBankAccountDetail(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void fingerprintOTCVerificationTest() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
//        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("3950392020946");
        messageVO.setContactNo("8889999999");
        messageVO.setFingerIndex("5");
        messageVO.setFingerTemplate("kkkd999");
        messageVO.setTemplateType("Non");
        messageVO.setRemittanceAmount("2500");
        messageVO.setRemittanceType("non");
        messageVO.setSecondaryCitizenNumber("9996661117774");
        messageVO.setAccountNumber("12457896333");
        messageVO.setSecondaryContactNo("5555455");
        messageVO.setAreaName("lahore");

        messageVO = nadraIntegrationController.otcFingerPrintVerification(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void OtcSecretIdentityDemographicsData() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("8889");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setSessionId("444445kijuhy");
        messageVO.setCitizenNumber("2225554441110");
        messageVO.setContactNo("8889999999");
        messageVO.setFingerIndex("10");
        messageVO.setFingerTemplate("kkkd999");
        messageVO.setTemplateType("12");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO.setAreaName("lahore");
        messageVO = nadraIntegrationController.getOtcSecretIdentityDemographicsData(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void OtcManualVerificationDataTest() {
        LOG.info("START: Secret Identity Demographics Data Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("12587");
        messageVO.setSessionId("44444uytrewq");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("9520225166837");
        messageVO.setVerificationResult("llllllll");
        messageVO = nadraIntegrationController.getOtcManualVerificationResult(messageVO);
        LOG.info("END:Secret Identity Demographics Data Test");
    }

    @Test
    public void OtcLastVerificationResult() {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("12587");
        messageVO.setUserName("user1");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("9520225166837");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO = nadraIntegrationController.getOtcLastVerificationResult(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


}
