
package com.inov8.integration.inbound.sms.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "receiveSMS", namespace = "http://sms.inbound.integration.inov8.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveSMS", namespace = "http://sms.inbound.integration.inov8.com/")
public class ReceiveSMS {

    @XmlElement(name = "arg0", namespace = "")
    private com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean arg0;

    /**
     * 
     * @return
     *     returns InboundSMSServiceBean
     */
    public com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean arg0) {
        this.arg0 = arg0;
    }

}
