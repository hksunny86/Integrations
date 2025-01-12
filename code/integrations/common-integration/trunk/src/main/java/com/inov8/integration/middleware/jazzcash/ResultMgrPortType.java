package com.inov8.integration.middleware.jazzcash;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2017-05-24T12:39:34.738+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebService(targetNamespace = "http://cps.huawei.com/cpsinterface/result", name = "ResultMgrPortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ResultMgrPortType {

    @Oneway
    @WebMethod(operationName = "GenericAPIResult", action = "GenericAPIResult")
    public void genericAPIResult(
        @WebParam(partName = "ResultMsg", name = "ResultMsg", targetNamespace = "http://cps.huawei.com/cpsinterface/result")
        java.lang.String resultMsg
    );
}
