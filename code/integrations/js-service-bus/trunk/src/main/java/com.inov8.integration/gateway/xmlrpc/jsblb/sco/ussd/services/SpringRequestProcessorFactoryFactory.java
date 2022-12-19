package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.StatelessProcessorFactoryFactory;

import java.util.HashMap;
import java.util.Map;


public class SpringRequestProcessorFactoryFactory extends
        StatelessProcessorFactoryFactory implements
        RequestProcessorFactoryFactory {

    Map<Class<? extends RPCHandler>,RPCHandler> classHandlerMappings;

    protected void init(Map<String, RPCHandler> handlerMappings) {
        classHandlerMappings = new HashMap<Class<? extends RPCHandler>, RPCHandler>();
        for (String key : handlerMappings.keySet()) {
            RPCHandler handler = handlerMappings.get(key);
            Class<? extends RPCHandler> clazz = handler.getClass();
            classHandlerMappings.put(clazz, handler);
        }
    }

    public RequestProcessorFactory getRequestProcessorFactory(@SuppressWarnings("rawtypes") Class clazz)
            throws XmlRpcException {
        final RPCHandler handler = classHandlerMappings.get(clazz);
        return new RequestProcessorFactory() {
            public Object getRequestProcessor(XmlRpcRequest pRequest)
                    throws XmlRpcException {
                return handler;
            }
        };
    }
}