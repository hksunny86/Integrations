package com.inov8.integration.channel.BOPBLB.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2020-10-27T20:17:16.440+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "ProcessEchallanSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface ProcessEchallanSoap {

    @WebMethod(operationName = "BillPayment", action = "http://tempuri.org/BillPayment")
    @RequestWrapper(localName = "BillPayment", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.BillPayment")
    @ResponseWrapper(localName = "BillPaymentResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.BillPaymentResponse")
    @WebResult(name = "BillPaymentResult", targetNamespace = "http://tempuri.org/")
    public java.lang.String billPayment(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "consumerNumber", targetNamespace = "http://tempuri.org/")
        java.lang.String consumerNumber,
        @WebParam(name = "authId", targetNamespace = "http://tempuri.org/")
        java.lang.String authId,
        @WebParam(name = "amount", targetNamespace = "http://tempuri.org/")
        java.lang.String amount,
        @WebParam(name = "tranDate", targetNamespace = "http://tempuri.org/")
        java.lang.String tranDate,
        @WebParam(name = "tranTime", targetNamespace = "http://tempuri.org/")
        java.lang.String tranTime,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "reserved", targetNamespace = "http://tempuri.org/")
        java.lang.String reserved
    );

    @WebMethod(operationName = "CashOutInquiry", action = "http://tempuri.org/CashOutInquiry")
    @RequestWrapper(localName = "CashOutInquiry", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashOutInquiry")
    @ResponseWrapper(localName = "CashOutInquiryResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashOutInquiryResponse")
    @WebResult(name = "CashOutInquiryResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.CashoutInquiryResponse2 cashOutInquiry(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "CNIC", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "Debit_Card_Number", targetNamespace = "http://tempuri.org/")
        java.lang.String debitCardNumber,
        @WebParam(name = "Mobile_No", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "Segment_ID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "Amount", targetNamespace = "http://tempuri.org/")
        java.lang.String amount,
        @WebParam(name = "Agent_Location", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "Agent_City", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "Terminal_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String terminalId,
        @WebParam(name = "Transaction_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String transactionId
    );

    @WebMethod(operationName = "CashOut", action = "http://tempuri.org/CashOut")
    @RequestWrapper(localName = "CashOut", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashOut")
    @ResponseWrapper(localName = "CashOutResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashOutResponse")
    @WebResult(name = "CashOutResult", targetNamespace = "http://tempuri.org/")
    public CashOutResponse2 cashOut(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "CNIC", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "Debit_Card_Number", targetNamespace = "http://tempuri.org/")
        java.lang.String debitCardNumber,
        @WebParam(name = "Mobile_No", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "Segment_ID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "Amount", targetNamespace = "http://tempuri.org/")
        java.lang.String amount,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "Trnsaction_ID", targetNamespace = "http://tempuri.org/")
        java.lang.String trnsactionID,
        @WebParam(name = "OTP", targetNamespace = "http://tempuri.org/")
        java.lang.String otp,
        @WebParam(name = "Finger_Index", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerIndex,
        @WebParam(name = "Finger_Template", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerTemplate,
        @WebParam(name = "Template_Type", targetNamespace = "http://tempuri.org/")
        java.lang.String templateType,
        @WebParam(name = "Agent_Location", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "Agent_City", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "Terminal_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String terminalId
    );

    @WebMethod(operationName = "AccountRegisteration", action = "http://tempuri.org/AccountRegisteration")
    @RequestWrapper(localName = "AccountRegisteration", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.AccountRegisteration")
    @ResponseWrapper(localName = "AccountRegisterationResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.AccountRegisterationResponse")
    @WebResult(name = "AccountRegisterationResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.AccountRegisterResponse accountRegisteration(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "CNIC", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "MobileNo", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "DebitCardNumber", targetNamespace = "http://tempuri.org/")
        java.lang.String debitCardNumber,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "SessionId", targetNamespace = "http://tempuri.org/")
        java.lang.String sessionId,
        @WebParam(name = "OTP", targetNamespace = "http://tempuri.org/")
        java.lang.String otp,
        @WebParam(name = "Finger_Index", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerIndex,
        @WebParam(name = "TransactionType", targetNamespace = "http://tempuri.org/")
        java.lang.String transactionType,
        @WebParam(name = "Finger_Template", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerTemplate,
        @WebParam(name = "SegmentID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "Template_Type", targetNamespace = "http://tempuri.org/")
        java.lang.String templateType,
        @WebParam(name = "Agent_Location", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "Agent_City", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "Terminal_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String terminalId
    );

    @WebMethod(operationName = "AccountRegisterationInquiry", action = "http://tempuri.org/AccountRegisterationInquiry")
    @RequestWrapper(localName = "AccountRegisterationInquiry", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.AccountRegisterationInquiry")
    @ResponseWrapper(localName = "AccountRegisterationInquiryResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.AccountRegisterationInquiryResponse")
    @WebResult(name = "AccountRegisterationInquiryResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.AccountRegisterInquiryResponse accountRegisterationInquiry(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "CNIC", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "MobileNo", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "DebitCardNumber", targetNamespace = "http://tempuri.org/")
        java.lang.String debitCardNumber,
        @WebParam(name = "TransactionType", targetNamespace = "http://tempuri.org/")
        java.lang.String transactionType,
        @WebParam(name = "SegmentID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "Agent_Location", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "Agent_City", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "Terminal_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String terminalId
    );

    @WebMethod(operationName = "BillInquiry", action = "http://tempuri.org/BillInquiry")
    @RequestWrapper(localName = "BillInquiry", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.BillInquiry")
    @ResponseWrapper(localName = "BillInquiryResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.BillInquiryResponse")
    @WebResult(name = "BillInquiryResult", targetNamespace = "http://tempuri.org/")
    public java.lang.String billInquiry(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "consumerNumber", targetNamespace = "http://tempuri.org/")
        java.lang.String consumerNumber,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "reserved", targetNamespace = "http://tempuri.org/")
        java.lang.String reserved
    );

    @WebMethod(operationName = "CashWithdrawReversal", action = "http://tempuri.org/CashWithdrawReversal")
    @RequestWrapper(localName = "CashWithdrawReversal", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashWithdrawReversal")
    @ResponseWrapper(localName = "CashWithdrawReversalResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.CashWithdrawReversalResponse")
    @WebResult(name = "CashWithdrawReversalResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.ReverselResponse cashWithdrawReversal(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "Mobile_No", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "Terminal_Id", targetNamespace = "http://tempuri.org/")
        java.lang.String terminalId,
        @WebParam(name = "Orignal_RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String orignalRRN,
        @WebParam(name = "Orignal_Transaction_Date_Time", targetNamespace = "http://tempuri.org/")
        java.lang.String orignalTransactionDateTime,
        @WebParam(name = "Transaction_Code", targetNamespace = "http://tempuri.org/")
        java.lang.String transactionCode
    );

    @WebMethod(operationName = "ProofOfLifeInquiry", action = "http://tempuri.org/ProofOfLifeInquiry")
    @RequestWrapper(localName = "ProofOfLifeInquiry", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.ProofOfLifeInquiry")
    @ResponseWrapper(localName = "ProofOfLifeInquiryResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.ProofOfLifeInquiryResponse")
    @WebResult(name = "ProofOfLifeInquiryResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.Proofoflifeinquiryresponse2 proofOfLifeInquiry(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "Mobile_No", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "Cnic", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "SegmentID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "AgentLocation", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "AgentCity", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "AgentID", targetNamespace = "http://tempuri.org/")
        java.lang.String agentID,
        @WebParam(name = "MachineName", targetNamespace = "http://tempuri.org/")
        java.lang.String machineName,
        @WebParam(name = "IP_Address", targetNamespace = "http://tempuri.org/")
        java.lang.String ipAddress,
        @WebParam(name = "MACAddress", targetNamespace = "http://tempuri.org/")
        java.lang.String macAddress,
        @WebParam(name = "UDID", targetNamespace = "http://tempuri.org/")
        java.lang.String udid,
        @WebParam(name = "Acknowledgedflag", targetNamespace = "http://tempuri.org/")
        java.lang.String acknowledgedflag
    );

    @WebMethod(operationName = "ProofOfLifeVerification", action = "http://tempuri.org/ProofOfLifeVerification")
    @RequestWrapper(localName = "ProofOfLifeVerification", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.ProofOfLifeVerification")
    @ResponseWrapper(localName = "ProofOfLifeVerificationResponse", targetNamespace = "http://tempuri.org/", className = "com.inov8.integration.channel.BOPBLB.client.ProofOfLifeVerificationResponse")
    @WebResult(name = "ProofOfLifeVerificationResult", targetNamespace = "http://tempuri.org/")
    public com.inov8.integration.channel.BOPBLB.client.Proofoflifeverificationresponse2 proofOfLifeVerification(
        @WebParam(name = "userName", targetNamespace = "http://tempuri.org/")
        java.lang.String userName,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        java.lang.String password,
        @WebParam(name = "bankMnemonic", targetNamespace = "http://tempuri.org/")
        java.lang.String bankMnemonic,
        @WebParam(name = "Mobile_No", targetNamespace = "http://tempuri.org/")
        java.lang.String mobileNo,
        @WebParam(name = "Cnic", targetNamespace = "http://tempuri.org/")
        java.lang.String cnic,
        @WebParam(name = "SegmentID", targetNamespace = "http://tempuri.org/")
        java.lang.String segmentID,
        @WebParam(name = "RRN", targetNamespace = "http://tempuri.org/")
        java.lang.String rrn,
        @WebParam(name = "FingerIndex", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerIndex,
        @WebParam(name = "FingerTemplate", targetNamespace = "http://tempuri.org/")
        java.lang.String fingerTemplate,
        @WebParam(name = "TemplateType", targetNamespace = "http://tempuri.org/")
        java.lang.String templateType,
        @WebParam(name = "SessionID", targetNamespace = "http://tempuri.org/")
        java.lang.String sessionID,
        @WebParam(name = "AgentLocation", targetNamespace = "http://tempuri.org/")
        java.lang.String agentLocation,
        @WebParam(name = "AgentCity", targetNamespace = "http://tempuri.org/")
        java.lang.String agentCity,
        @WebParam(name = "AgentID", targetNamespace = "http://tempuri.org/")
        java.lang.String agentID,
        @WebParam(name = "MachineName", targetNamespace = "http://tempuri.org/")
        java.lang.String machineName,
        @WebParam(name = "IP_Address", targetNamespace = "http://tempuri.org/")
        java.lang.String ipAddress,
        @WebParam(name = "MACAddress", targetNamespace = "http://tempuri.org/")
        java.lang.String macAddress,
        @WebParam(name = "UDID", targetNamespace = "http://tempuri.org/")
        java.lang.String udid,
        @WebParam(name = "AcknowledgmentFlag", targetNamespace = "http://tempuri.org/")
        java.lang.String acknowledgmentFlag
    );
}
