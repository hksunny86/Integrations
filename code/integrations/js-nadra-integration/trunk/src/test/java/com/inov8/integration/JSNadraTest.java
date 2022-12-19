package com.inov8.integration;
import com.inov8.integration.middleware.pdu.response.FingerPrintVerificationResponse;
import com.inov8.integration.vo.JsNadraBiometricVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JSNadraTest {

    private static final Logger log = LoggerFactory.getLogger(JSNadraTest.class);

//    @Autowired
//    private JsNadraController pitbController;

    @Before
    public void setup() {

    }


    @Test
    public void TestBeneficiaryRegistration(){
        JsNadraBiometricVo integrationVO= new JsNadraBiometricVo();

        FingerPrintVerificationResponse registration1 = new FingerPrintVerificationResponse();
//        registration1.setName("Muddasir khalid");
//        registration1.setMobileNo("03414937632");
//
//        registration1.setCnicNo("3450254875441");
//        registration1.setCardNo("5389672000000450");
//        registration1.setRegisterationDateTime("12/14/2015 04:38:58 PM");
//        registration1.setAgentId("2");
//        registration1.setAgentName("Telenor System");
//        registration1.setAgentContactNo("03400000000");
//        registration1.setCurrentBalance("0.00");
//        registration1.setSegmentType("10");
//
//        List<FingerPrintVerificationResponse> list = new ArrayList<>();
//        list.add(registration1);
//
//
//        integrationVO.setBiometricVerificationResponseList(list);

//        integrationVO = pitbController.beneficiaryRegistration(integrationVO);

        log.info(integrationVO.toString());
    }

//    @Test
//    public void TestTransaction(){
//        JsNadraBiometricVo integrationVO= new JsNadraBiometricVo();
//
//        Transaction transaction1 = new Transaction();
//
//        transaction1.setTransactionId("10000198");
//        transaction1.setTransactionDateTime("12/14/2015 06:27:00 PM");
//        transaction1.setAccountType("L0");
//        transaction1.setCnicNo("3410171565222");
//        transaction1.setCardNo("896701208818555");
//        transaction1.setMobileNo("03336775543");
//        transaction1.setDebit("200.00");
//        transaction1.setCredit("0.00");
//        transaction1.setBalance("14050.00");
//        transaction1.setChannelType("AGENT");
//        transaction1.setChannelId("1");
//        transaction1.setChannelName("Hameed");
////        transaction1.setSegmentType("10");
//
//
//        List<Transaction> list = new ArrayList<>();
//        list.add(transaction1);
//        integrationVO.setTransactionList(list);
//
//        integrationVO = pitbController.transaction(integrationVO);
//
//        log.info(integrationVO.toString());
//    }
//    @Test
//    public void TestPermanentCardBlock(){
//        JsNadraBiometricVo integrationVO= new JsNadraBiometricVo();
//
//        PermanentCardBlock permanentCardBlock = new PermanentCardBlock();
//        permanentCardBlock.setName("Muddasir Hanif");
//        permanentCardBlock.setMobileNo("03336775543");
//
//        permanentCardBlock.setCnicNo("3410171565222");
//        permanentCardBlock.setCardNo("896701208818555");
//        permanentCardBlock.setBlockingDateTime("12/14/2015 04:38:58 PM");
//        permanentCardBlock.setSegmentType("10");
//
//        List<PermanentCardBlock> list = new ArrayList<>();
//        list.add(permanentCardBlock);
//
//
//        integrationVO.setPermanentCardBlockList(list);
//
//        integrationVO = pitbController.permanentCardBlock(integrationVO);
//
//        log.info(integrationVO.toString());
//    }
}
