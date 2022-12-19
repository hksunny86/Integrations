package com.inov8.microbank.server.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.inov8.microbank.server.webservice.bean.USSDInputDTO;
import com.inov8.microbank.server.webservice.bean.USSDOutputDTO;

@WebService(name="CalculatorPortType",
        targetNamespace = "http://service.microbank.inov8.com/")
public interface USSDRequestHandlerInterface {
	@WebMethod
	public USSDOutputDTO handleUSSDRequest(@WebParam(name="ussdInputDTO") USSDInputDTO ussdInputDTO);
	@WebMethod
	public boolean invalidateSession(int senderID);

}
