package com.inov8.microbank.common.util;

import org.apache.log4j.Logger;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;

/**
 * Created by AtieqRe on 4/7/2017.
 */
public class HttpInvokerUtil {
    private static final Logger logger = Logger.getLogger(HttpInvokerUtil.class);

    /**
     * Initialize and return {@link HttpInvokerProxyFactoryBean} of given Type with provided URL.
     * it also set up {@link SimpleHttpInvokerRequestExecutor} object with configured Connection & Read time outs.
     * @param clazz given class [must be some an interface]
     * @param url URL to be connected to
     * @return <T> Object of class provided in first argument
     */
    public static <T> T getHttpInvokerFactoryBean(Class<T> clazz, String url) {

        try {
            int connectionTimeOut = MessageUtil.getRemotingConnectionTimeout();
            int readTimeOut = MessageUtil.getRemotingReadTimeout();

            SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
            executor.setConnectTimeout(connectionTimeOut * 1000);
            executor.setReadTimeout(readTimeOut * 1000);

            HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
            factoryBean.setServiceInterface(clazz);
            factoryBean.setServiceUrl(url);
            factoryBean.afterPropertiesSet();
            factoryBean.setHttpInvokerRequestExecutor(executor);

            return (T)factoryBean.getObject();
        }

        catch (Exception e) {
            logger.error("Exception on initializing HttpInvokerProxyFactoryBean for Class : " + clazz + " Message : " + e.getMessage() ,e);

            return null;
        }
    }
}
