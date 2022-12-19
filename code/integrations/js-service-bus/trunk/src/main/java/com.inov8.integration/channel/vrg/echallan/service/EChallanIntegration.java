package com.inov8.integration.channel.vrg.echallan.service;




import com.inov8.integration.channel.vrg.echallan.request.BillInquiryRequest;
import com.inov8.integration.channel.vrg.echallan.request.BillPaymentRequest;
import com.inov8.integration.channel.vrg.echallan.response.BillInquiryResponse;
import com.inov8.integration.channel.vrg.echallan.response.BillPaymentResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceClient;

@WebService(targetNamespace = "http://process.soap.echallan.vrg.com/", name = "ProcessEchallan")
public interface EChallanIntegration {

    @WebMethod(operationName = "BillInquiry")
    @Action(input = "http://process.soap.echallan.vrg.com/ProcessEchallan/BillInquiryRequest", output = "http://process.soap.echallan.vrg.com/ProcessEchallan/BillInquiryResponse")
    @RequestWrapper(localName = "BillInquiry", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.BillInquiry")
    @ResponseWrapper(localName = "BillInquiryResponse", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.BillInquiryResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String billInquiry(
            @WebParam(name = "Username", targetNamespace = "")
                    java.lang.String username,
            @WebParam(name = "Password", targetNamespace = "")
                    java.lang.String password,
            @WebParam(name = "Consumer_Number", targetNamespace = "")
                    java.lang.String consumerNumber,
            @WebParam(name = "Bank_Mnemonic", targetNamespace = "")
                    java.lang.String bankMnemonic,
            @WebParam(name = "Reserved", targetNamespace = "")
                    java.lang.String reserved
    );

    @WebMethod(operationName = "BillPayment")
    @Action(input = "http://process.soap.echallan.vrg.com/ProcessEchallan/BillPaymentRequest", output = "http://process.soap.echallan.vrg.com/ProcessEchallan/BillPaymentResponse")
    @RequestWrapper(localName = "BillPayment", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.BillPayment")
    @ResponseWrapper(localName = "BillPaymentResponse", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.BillPaymentResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String billPayment(
            @WebParam(name = "Username", targetNamespace = "")
                    java.lang.String username,
            @WebParam(name = "Password", targetNamespace = "")
                    java.lang.String password,
            @WebParam(name = "Consumer_Number", targetNamespace = "")
                    java.lang.String consumerNumber,
            @WebParam(name = "Tran_Auth_Id", targetNamespace = "")
                    java.lang.String tranAuthId,
            @WebParam(name = "Transaction_Amount", targetNamespace = "")
                    java.lang.String transactionAmount,
            @WebParam(name = "Tran_Date", targetNamespace = "")
                    java.lang.String tranDate,
            @WebParam(name = "Tran_Time", targetNamespace = "")
                    java.lang.String tranTime,
            @WebParam(name = "Bank_Mnemonic", targetNamespace = "")
                    java.lang.String bankMnemonic,
            @WebParam(name = "Reserved", targetNamespace = "")
                    java.lang.String reserved
    );

    @WebMethod(operationName = "Echo_Transaction")
    @Action(input = "http://process.soap.echallan.vrg.com/ProcessEchallan/Echo_TransactionRequest", output = "http://process.soap.echallan.vrg.com/ProcessEchallan/Echo_TransactionResponse")
    @RequestWrapper(localName = "Echo_Transaction", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.EchoTransaction")
    @ResponseWrapper(localName = "Echo_TransactionResponse", targetNamespace = "http://process.soap.echallan.vrg.com/", className = "com.inov8.integration.middleware.EchoTransactionResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String echoTransaction(
            @WebParam(name = "Username", targetNamespace = "")
                    java.lang.String username,
            @WebParam(name = "Password", targetNamespace = "")
                    java.lang.String password,
            @WebParam(name = "Ping", targetNamespace = "")
                    java.lang.String ping
    );
}
