package com.inov8.integration.middleware.bop.avenza;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2016-12-21T23:43:31.851-08:00
 * Generated source version: 3.1.6
 * 
 */
@WebService(targetNamespace = "http://BOP_MobileApp.org", name = "ServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ServiceSoap {

    @WebMethod(operationName = "EmailAccountStatement", action = "http://BOP_MobileApp.org/EmailAccountStatement")
    @WebResult(name = "EmailAccountStatementResponse", targetNamespace = "http://BOP_MobileApp.org", partName = "parameters")
    public EmailAccountStatementResponse emailAccountStatement(
        @WebParam(partName = "parameters", name = "EmailAccountStatementRequest", targetNamespace = "http://BOP_MobileApp.org")
        EmailAccountStatementRequest parameters
    );

    @WebMethod(operationName = "CustomerEmailUpdate", action = "http://BOP_MobileApp.org/CustomerEmailUpdate")
    @WebResult(name = "CustomerEmailUpdateResponse", targetNamespace = "http://BOP_MobileApp.org", partName = "parameters")
    public CustomerEmailUpdateResponse customerEmailUpdate(
        @WebParam(partName = "parameters", name = "CustomerEmailUpdateRequest", targetNamespace = "http://BOP_MobileApp.org")
        CustomerEmailUpdateRequest parameters
    );

    @WebMethod(operationName = "CustomerEmailVerification", action = "http://BOP_MobileApp.org/CustomerEmailVerification")
    @WebResult(name = "CustomerEmailVerificationResponse", targetNamespace = "http://BOP_MobileApp.org", partName = "parameters")
    public CustomerEmailVerificationResponse customerEmailVerification(
        @WebParam(partName = "parameters", name = "CustomerEmailVerificationRequest", targetNamespace = "http://BOP_MobileApp.org")
        CustomerEmailVerificationRequest parameters
    );

    @WebMethod(operationName = "ViewAccountStatement", action = "http://BOP_MobileApp.org/ViewAccountStatement")
    @WebResult(name = "ViewAccountStatementResponse", targetNamespace = "http://BOP_MobileApp.org", partName = "parameters")
    public ViewAccountStatementResponse viewAccountStatement(
        @WebParam(partName = "parameters", name = "ViewAccountStatementRequest", targetNamespace = "http://BOP_MobileApp.org")
        ViewAccountStatementRequest parameters
    );
}
