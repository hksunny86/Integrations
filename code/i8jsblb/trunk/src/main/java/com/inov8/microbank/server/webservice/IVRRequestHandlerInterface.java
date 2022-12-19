package com.inov8.microbank.server.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.inov8.microbank.server.webservice.bean.IvrResponseDTO;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;

@WebService(name="ivrRequestHandlerInterface" , targetNamespace = "http://service.microbank.inov8.com/")
public interface IVRRequestHandlerInterface {

	@WebMethod
	public IvrRequestDTO handleIVRRequest(@WebParam(name="ivrResponseDTO")IvrResponseDTO ivrResponseDTO);
	
}
