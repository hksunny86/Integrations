
package com.inov8.microbank.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IVRRequestHandlerDelegate", targetNamespace = "http://service.microbank.inov8.com/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
//@XmlSeeAlso({
//    com.inov8.microbank.server.ObjectFactory.class,
//    com.inov8.microbank.service.ObjectFactory.class
//})
public interface IVRRequestHandlerDelegate {


    /**
     * 
     * @param ivrResponseDTO
     * @return
     *     returns com.inov8.microbank.server.IvrRequestDTO
     */
    @WebMethod
    @WebResult(name = "handleIVRRequestResponse", targetNamespace = "http://service.microbank.inov8.com/", partName = "handleIVRRequestResponse")
    public IvrRequestDTO handleIVRRequest(
        @WebParam(name = "ivrResponseDTO", targetNamespace = "http://service.microbank.inov8.com/", partName = "ivrResponseDTO")
        IvrResponseDTO ivrResponseDTO);

    /**
     * 
     * @param ivrKeyInRequest
     * @return
     *     returns com.inov8.microbank.service.IvrKeyInResponse
     */
    @WebMethod(exclude=true)
    @WebResult(name = "processKeyInResponse", targetNamespace = "http://service.microbank.inov8.com/", partName = "processKeyInResponse")
    public IvrKeyInResponse processKeyIn(
        @WebParam(name = "ivrKeyInRequest", targetNamespace = "http://service.microbank.inov8.com/", partName = "ivrKeyInRequest")
        IvrKeyInRequest ivrKeyInRequest);

}