package com.inov8.integration.middleware.controller.hostController;

import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "JsIntegration", targetNamespace = "http://tempuri.org/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface JsIntegration {
    public ChequeBookResponse chequeBook(@WebParam(name = "chequeBookRequest") ChequeBookRequest request);


}
