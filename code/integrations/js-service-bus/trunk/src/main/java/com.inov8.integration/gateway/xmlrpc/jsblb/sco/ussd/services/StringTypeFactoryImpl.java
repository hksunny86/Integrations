package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.SAXException;

public class StringTypeFactoryImpl extends TypeFactoryImpl {

    public StringTypeFactoryImpl(XmlRpcController pController) {
        super(pController);
    }

    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
        if(pObject instanceof String) {
            return new CustomStringSerializer();
        } else {
            return super.getSerializer(pConfig, pObject);
        }
    }
}
