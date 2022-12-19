package com.inov8.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zeeshan Ahmad on 4/26/2016.
 */
public class JSNadraWebTest {

    private static final Logger log = LoggerFactory.getLogger(JSNadraWebTest.class);

//    private JsNadraController controller;
//
//    @Before
//    public void setup() {
//
//        try {
//            getFromProxy("127.0.0.1:9090");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void TestBeneficiaryRegistration(){
//        JsNadraBiometricVo integrationVO= new JsNadraBiometricVo();
//
//        FingerPrintVerificationResponse registration1 = new FingerPrintVerificationResponse();
//        registration1.setName("Muddasir Hanif");
//        registration1.setMobileNo("03336775543");
//
//        registration1.setCnicNo("3410171565222");
//        registration1.setCardNo("896701208818555");
//        registration1.setRegisterationDateTime("12/14/2015 04:38:58 PM");
//        registration1.setAgentId("2");
//        registration1.setAgentName("Asif");
//        registration1.setAgentContactNo("03006788854");
//        registration1.setCurrentBalance("0.00");
//        registration1.setSegmentType("10");
//
//        List<FingerPrintVerificationResponse> list = new ArrayList<>();
//        list.add(registration1);
//
//
//        integrationVO.setBiometricVerificationResponseList(list);
//
//        for (int i = 0; i < 100; i++) {
//            integrationVO = controller.beneficiaryRegistration(integrationVO);
//                    log.info(integrationVO.getResponseCode());
//
//        }
//
//
//
//    }
//
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
//        integrationVO = controller.transaction(integrationVO);
//
//        log.info(integrationVO.toString());
//    }
//
//    public void getFromProxy(String server) throws Exception {
//        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
//        httpInvokerProxyFactoryBean.setServiceInterface(JsNadraController.class);
//        httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/pitb-integration/ws/api");
//        httpInvokerProxyFactoryBean.afterPropertiesSet();
//        controller = (JsNadraController) httpInvokerProxyFactoryBean.getObject();
//    }
}
