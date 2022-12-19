package com.inov8.microbank.common.util;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public ObjectFactory() {
    }

    public CommandResponseXML createCommandResponseXML() {
        return new CommandResponseXML();
    }

    public CommandResponseXML.Param createCommandResponseXMLParam() {
        return new CommandResponseXML.Param();
    }

}
