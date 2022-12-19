package com.inov8.integration;

import com.inov8.integration.middleware.controller.MiddlewareSwitchController;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Created by Administrator on 12/28/2017.
 */
public class RemoteTesting {
    public static void main(String a[]){

        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl("http://10.0.1.221:9090/js-blb-integration/ws/api");
//        factoryBean.setServiceUrl("http://127.0.0.1:9090/js-blb-integration/ws/api");
        factoryBean.setServiceInterface(WebServiceSwitchController.class);
        factoryBean.afterPropertiesSet();

        WebServiceSwitchController controller = (WebServiceSwitchController) factoryBean.getObject();

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
        messageVO = controller.billPaymentInquiry(messageVO);

        System.out.print("End Here ");


    }
}

