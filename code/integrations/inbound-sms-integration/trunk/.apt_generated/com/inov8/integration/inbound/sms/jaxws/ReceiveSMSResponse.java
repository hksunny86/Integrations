
package com.inov8.integration.inbound.sms.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "receiveSMSResponse", namespace = "http://sms.inbound.integration.inov8.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveSMSResponse", namespace = "http://sms.inbound.integration.inov8.com/")
public class ReceiveSMSResponse {

    @XmlElement(name = "return", namespace = "")
    private com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean _return;

    /**
     * 
     * @return
     *     returns InboundSMSServiceBean
     */
    public com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean _return) {
        this._return = _return;
    }

}
