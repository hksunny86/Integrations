package com.inov8.integration.middleware.novatti;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2015-11-04T11:58:24.686+05:00
 * Generated source version: 3.1.3
 * 
 */
@WebService(targetNamespace = "http://soap.api.novatti.com/service", name = "ReportsServiceIF")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ReportsServiceIF {

    @WebMethod(operationName = "GetTransactionInfo")
    @WebResult(name = "SoapTransactionInfoResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "TransactionInfoResponse")
    public SoapTransactionInfoResponse getTransactionInfo(
            @WebParam(partName = "TransactionInfoRequest", name = "SoapTransactionInfoRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapTransactionInfoRequest transactionInfoRequest
    );
}
