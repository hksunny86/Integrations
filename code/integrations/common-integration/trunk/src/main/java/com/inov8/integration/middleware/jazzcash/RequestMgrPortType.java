package com.inov8.integration.middleware.jazzcash;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2017-05-24T12:41:25.380+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebService(targetNamespace = "http://cps.huawei.com/cpsinterface/request", name = "RequestMgrPortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface RequestMgrPortType {

    @WebResult(name = "ResponseMsg", targetNamespace = "http://cps.huawei.com/cpsinterface/request", partName = "ResponseMsg")
    @WebMethod(operationName = "GenericAPIRequest", action = "GenericAPIRequest")
    public java.lang.String genericAPIRequest(
        @WebParam(partName = "RequestMsg", name = "RequestMsg", targetNamespace = "http://cps.huawei.com/cpsinterface/request")
        java.lang.String requestMsg
    );
}