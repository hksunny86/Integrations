package com.inov8.integration.test;

import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.vo.NadraIntegrationVO;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

public class NadraIntegrationWebTest {

    private Logger LOG = LoggerFactory.getLogger(NadraIntegrationWebTest.class);

    private NadraIntegrationController controller;

    private static String FRANCHISE_ID = ConfigReader.getInstance().getProperty("nadra.franchize.id", "");


    @Before
    public void init() {

        try {
            getFromProxy("10.0.1.40:7070");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fingerprintVerificationTest() throws IOException {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3450254875112");
        messageVO.setContactNo("03454677743");
        messageVO.setFingerIndex("6");

//        String template = Base64Utils.encodeToString(IOUtils.toByteArray(new FileReader("d:/biometric/35202.jpg")));
//        String template = IOUtils.toString(new FileReader("a:/biometric/Right_Thumb_ISO.txt"));
//        String template = FileCopyUtils.copyToString(new FileReader("classpath:Right_Thumb_Raw_Image.txt"));
        String template = "33313141333235463038303238303238303238303238303238303238303238303238303238303238303238303238303238303238303238303238303230303030303030303030303030303030354642394331444334363734304444354634303936354436464242314235413342383630374236383432443831333133394339373832414141363739383336323536383939323038384533363236374433463733333238443042373639324341303137363833423137393736373346313644343634383530423531373944344242353235314537463531303233354637353636424431364435353838374330423232433144413237323644413436323932323432384536333233384139364135323333324532414432363942303837313235324242343035333345413339303138314143323443324341314133463136373332314344363033313943344531363230413138423432323534304143313934304445414333353838393545443633384139373644334431324337324436333243354536443241423931353241344538384333364231454236354537324131373938363834324332383143383230323436323239333731303139383434354134313336363431414534303236333542373639413630303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030";
        messageVO.setFingerTemplate(template);
        messageVO.setTemplateType("ISO_1974_2");
        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
        messageVO.setAreaName("punjab");
        try {
            messageVO = controller.fingerPrintVerification(messageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void secretIdentityDemographicsData() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3555699001111");
        messageVO.setContactNo("03454677743");
        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
//        messageVO.setAreaName("punjab");
//        messageVO = controller.getSecretIdentityDemographicsData(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    @Test
    public void manualVerificationDataTest() throws Exception {
        LOG.info("START: Secret Identity Demographics Data Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3520227440669");
        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
        messageVO.setVerificationResult("successful");
//        messageVO = controller.getManualVerificationResult(messageVO);
        LOG.info("END:Secret Identity Demographics Data Test");
    }


    @Test
    public void lastVerificationResult() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3520227440669");
        messageVO.setServiceProviderTransactionId("1905832115160757090");
        messageVO = controller.getLastVerificationResult(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    @Test
    public void submitMobileBankAccountDetails() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3520227440669");
        messageVO.setSessionId("1905100000000054452");
        messageVO.setAccountNumber("03454677743");
        messageVO.setAccountLevel("level 0");
        messageVO = controller.submitMobileBankAccountDetail(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    @Test
    public void fingerprintOTCVerificationSendTest() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
        messageVO.setCitizenNumber("3410344692665");
        messageVO.setContactNo("03454544454");
        messageVO.setFingerIndex("1");
//        String template = IOUtils.toString(new FileReader("a:/biometric/Right_Thumb_ISO.txt"));
//        messageVO.setFingerTemplate(template);
        messageVO.setTemplateType("ANSI");
//        messageVO.setRemittanceType("IBFT");
//        messageVO.setSecondaryCitizenNumber("3740578347033");
//        messageVO.setSecondaryContactNo("03454677743");
//        messageVO.setAreaName("punjab");

//        messageVO = controller.otcFingerPrintVerification(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }

    @Test
    public void fingerprintOTCVerificationReceiveTest() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
        messageVO.setCitizenNumber("3555699001111");
        messageVO.setContactNo("03454544454");
        messageVO.setFingerIndex("1");
//        String template = IOUtils.toString(new FileReader("a:/biometric/Right_Thumb_ISO.txt"));
        String template = "VGVzdA==";
        messageVO.setFingerTemplate(template);
        messageVO.setTemplateType("ANSI");
//        messageVO.setRemittanceType("TRANSFER_SEND");
//        messageVO.setSecondaryCitizenNumber("3520227440669");
//        messageVO.setSecondaryContactNo("03454677743");
//        messageVO.setAreaName("punjab");

//        messageVO = controller.otcFingerPrintVerification(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    @Test
    public void secretIdentityDemographicsDataOTC() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3450254308771");
//        messageVO.setSecondaryCitizenNumber("3520227440669");
//        messageVO.setSecondaryContactNo("03414937631");
//        messageVO.setRemittanceType("MONENY_TRANSFER_RECEIVE");
//        messageVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
//        messageVO.setAreaName("punjab");
//        messageVO = controller.getOtcSecretIdentityDemographicsData(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    @Test
    public void lastVerificationResultOTC() throws Exception {
        LOG.info("START: Finger Print Verification Test");
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber("3520227440669");
        messageVO.setServiceProviderTransactionId("1905685820978510634");
//        messageVO.setSecondaryCitizenNumber("3410344692665");
//        messageVO.setSecondaryContactNo("03414937631");
//        messageVO.setRemittanceType("MONENY_TRANSFER_RECEIVE");
//        messageVO = controller.getOtcLastVerificationResult(messageVO);
        LOG.info("END:Finger Print Verification Test");
    }


    private static long getRandomNumberInRange() {

        long LOWER_RANGE = 100000000000000L; //assign lower range value
        long UPPER_RANGE = 999999999999999L; //assign upper range value
        Random random = new Random();

        return LOWER_RANGE + (long) (random.nextDouble() * (UPPER_RANGE - LOWER_RANGE));

    }

    public RestTemplate restTemplate = new RestTemplate();

    public void getFromProxy(String server) throws Exception {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(NadraIntegrationController.class);
        httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/nadra-biometric-integration/ws/api");
        httpInvokerProxyFactoryBean.afterPropertiesSet();
        controller = (NadraIntegrationController) httpInvokerProxyFactoryBean.getObject();
    }
}
