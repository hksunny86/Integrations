package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services;

import org.apache.xmlrpc.serializer.StringSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CustomStringSerializer extends StringSerializer
{

    public void write(ContentHandler pHandler, Object pObject)
            throws SAXException {
        // Write <string> tag explicitly
        write(pHandler, "string", pObject.toString());
    }
}
