package com.inov8.integration.middleware.nadra.biometric;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2019-12-19T12:55:54.425+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebService(targetNamespace = "http://NADRA.Biometric.Verification", name = "IBioVeriSysMobileBankAccount")
@XmlSeeAlso({ObjectFactory.class})
public interface IBioVeriSysMobileBankAccount {

    @WebMethod(operationName = "VerifyFingerPrints", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyFingerPrints")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyFingerPrints", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyFingerPrintsResponse")
    @RequestWrapper(localName = "VerifyFingerPrints", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.VerifyFingerPrints")
    @ResponseWrapper(localName = "VerifyFingerPrintsResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.VerifyFingerPrintsResponse")
    @WebResult(name = "VerifyFingerPrintsResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String verifyFingerPrints(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "GetCitizenIdentityDemographicsData", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenIdentityDemographicsData")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenIdentityDemographicsData", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenIdentityDemographicsDataResponse")
    @RequestWrapper(localName = "GetCitizenIdentityDemographicsData", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetCitizenIdentityDemographicsData")
    @ResponseWrapper(localName = "GetCitizenIdentityDemographicsDataResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetCitizenIdentityDemographicsDataResponse")
    @WebResult(name = "GetCitizenIdentityDemographicsDataResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String getCitizenIdentityDemographicsData(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "TestService", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/TestService")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/TestService", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/TestServiceResponse")
    @RequestWrapper(localName = "TestService", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.TestService")
    @ResponseWrapper(localName = "TestServiceResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.TestServiceResponse")
    @WebResult(name = "TestServiceResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String testService(
        @WebParam(name = "type", targetNamespace = "http://NADRA.Biometric.Verification")
        com.inov8.integration.middleware.nadra.biometric.TemplateType type,
        @WebParam(name = "tempType", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String tempType
    );

    @WebMethod(operationName = "GetCitizenData", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenData")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenData", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetCitizenDataResponse")
    @RequestWrapper(localName = "GetCitizenData", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetCitizenData")
    @ResponseWrapper(localName = "GetCitizenDataResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetCitizenDataResponse")
    @WebResult(name = "GetCitizenDataResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String getCitizenData(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "VerifyPhotograph", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyPhotograph")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyPhotograph", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/VerifyPhotographResponse")
    @RequestWrapper(localName = "VerifyPhotograph", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.VerifyPhotograph")
    @ResponseWrapper(localName = "VerifyPhotographResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.VerifyPhotographResponse")
    @WebResult(name = "VerifyPhotographResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String verifyPhotograph(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "GetLastVerificationResults", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetLastVerificationResults")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetLastVerificationResults", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/GetLastVerificationResultsResponse")
    @RequestWrapper(localName = "GetLastVerificationResults", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetLastVerificationResults")
    @ResponseWrapper(localName = "GetLastVerificationResultsResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.GetLastVerificationResultsResponse")
    @WebResult(name = "GetLastVerificationResultsResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String getLastVerificationResults(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "SubmitBankAccountDetails", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitBankAccountDetails")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitBankAccountDetails", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitBankAccountDetailsResponse")
    @RequestWrapper(localName = "SubmitBankAccountDetails", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.SubmitBankAccountDetails")
    @ResponseWrapper(localName = "SubmitBankAccountDetailsResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.SubmitBankAccountDetailsResponse")
    @WebResult(name = "SubmitBankAccountDetailsResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String submitBankAccountDetails(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );

    @WebMethod(operationName = "SubmitManualVerificationResults", action = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitManualVerificationResults")
    @Action(input = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitManualVerificationResults", output = "http://NADRA.Biometric.Verification/IBioVeriSysMobileBankAccount/SubmitManualVerificationResultsResponse")
    @RequestWrapper(localName = "SubmitManualVerificationResults", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.SubmitManualVerificationResults")
    @ResponseWrapper(localName = "SubmitManualVerificationResultsResponse", targetNamespace = "http://NADRA.Biometric.Verification", className = "com.inov8.integration.middleware.nadra.biometric.SubmitManualVerificationResultsResponse")
    @WebResult(name = "SubmitManualVerificationResultsResult", targetNamespace = "http://NADRA.Biometric.Verification")
    public java.lang.String submitManualVerificationResults(
        @WebParam(name = "franchizeID", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String franchizeID,
        @WebParam(name = "xml_request_data", targetNamespace = "http://NADRA.Biometric.Verification")
        java.lang.String xmlRequestData
    );
}
