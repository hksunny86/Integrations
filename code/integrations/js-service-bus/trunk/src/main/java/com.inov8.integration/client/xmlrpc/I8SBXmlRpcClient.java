package com.inov8.integration.client.xmlrpc;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by inov8 on 3/7/2018.
 */
//@Component
public class I8SBXmlRpcClient {
    private static Logger logger = LoggerFactory.getLogger(I8SBXmlRpcClient.class);

//    @Autowired
    private XmlRpcClient xmlRpcClient;

    public Map<String, Object> executeMethod(String methodName, Object[] params) throws XmlRpcException {
        return (Map<String, Object>) xmlRpcClient.execute(methodName, params);
    }
}
