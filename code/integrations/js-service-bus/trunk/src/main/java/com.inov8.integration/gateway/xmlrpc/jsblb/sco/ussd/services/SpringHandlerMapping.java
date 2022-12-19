package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;


public class SpringHandlerMapping extends AbstractReflectiveHandlerMapping {

    private static Logger logger = LoggerFactory.getLogger(SpringHandlerMapping.class.getSimpleName());

    public void setHandlerMappings(Map<String, RPCHandler> handlerMappings)
            throws XmlRpcException {
        SpringRequestProcessorFactoryFactory ff = (SpringRequestProcessorFactoryFactory) getRequestProcessorFactoryFactory();
        Assert.notNull(ff, "RequestProcessorFactoryFactory must be set");
        ff.init(handlerMappings);
        for (String serviceName : handlerMappings.keySet()) {
            RPCHandler serviceBean = handlerMappings.get(serviceName);
            registerPublicMethods(serviceName, serviceBean.getClass());
        }
    }

    @Override
    public XmlRpcHandler getHandler(String pHandlerName) throws XmlRpcException {
        XmlRpcHandler result = null;

        try {
            result = super.getHandler("uSSDRequestHandler." + pHandlerName);
        } catch (Exception ex) {
            logger.info("Ignoring ALL exceptions so that default one will also get executed");
        }

        if (result == null) {
            if ((result = super.getHandler(pHandlerName)) == null) {
                throw new XmlRpcNoSuchHandlerException("No such handler: " + pHandlerName);
            }
        }
        return result;
    }
}
