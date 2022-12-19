package com.inov8.integration.test;


import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class RemoteChecking {
    public static void main(String a[]){

        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl("http://10.0.1.9:8080/nadra-biometric-integration/ws/api");
        factoryBean.setServiceInterface(NadraIntegrationController.class);
        factoryBean.afterPropertiesSet();

        NadraIntegrationController controller = (NadraIntegrationController) factoryBean.getObject();

        NadraIntegrationVO integrationVO = new NadraIntegrationVO();

        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setFranchiseeID("1258");
        messageVO.setUserName("100");
        messageVO.setPassword("Password");
        messageVO.setCitizenNumber("3520178325202");
        messageVO.setContactNo("03124090386");
        messageVO.setFingerIndex("1");
        messageVO.setFingerTemplate("VGVzdA==");
        messageVO.setTemplateType("RAW_IMAGE");
        messageVO.setServiceProviderTransactionId("44888");
        messageVO.setAreaName("lahore");
        try {
            messageVO = controller.fingerPrintVerification(messageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("");

    }
}
