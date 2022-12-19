package com.inov8.verifly.common.util;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.verifly.server.facade.VeriflyFacade;



public class TestVeriflyFacadeSingleton {

    private static VeriflyFacade instance = null;
    private TestVeriflyFacadeSingleton() {

    }


    public static  VeriflyFacade getInstance(String url) {
        if(null == instance)
        {
          //  String url = VeriflyConstants.VERIFLY_URL ;
            HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
            httpInvokerProxyFactoryBean.setServiceUrl(url);
            httpInvokerProxyFactoryBean.setServiceInterface(VeriflyFacade.class);
            try
            {
               httpInvokerProxyFactoryBean.afterPropertiesSet();
            }
            catch(Exception e){e.printStackTrace();}

            return (VeriflyFacade)httpInvokerProxyFactoryBean.getObject();
        }
        return instance;
    }

}
