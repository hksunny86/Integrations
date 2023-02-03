package com.inov8.integration.middleware.novatti;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2015-10-16T11:28:49.827+05:00
 * Generated source version: 3.1.3
 * 
 */
@WebService(targetNamespace = "http://soap.api.novatti.com/service", name = "PurchaseServiceIF")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PurchaseServiceIF {

    @WebMethod(operationName = "ValidateProductRedeem")
    @WebResult(name = "SoapValidateProductRedeemResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "ValidateProductRedeemResponse")
    public SoapValidateProductRedeemResponse validateProductRedeem(
            @WebParam(partName = "ValidateProductRedeemRequest", name = "SoapValidateProductRedeemRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapValidateProductRedeemRequest validateProductRedeemRequest
    );

    @WebMethod(operationName = "MakeProductPurchase")
    @WebResult(name = "SoapProductPurchaseResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "ProductPurchaseResponse")
    public SoapProductPurchaseResponse makeProductPurchase(
            @WebParam(partName = "ProductPurchaseRequest", name = "SoapProductPurchaseRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapProductPurchaseRequest productPurchaseRequest
    );

    @WebMethod(operationName = "RequestPinBatch")
    @WebResult(name = "SoapPinBatchResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PinBatchResponse")
    public SoapPinBatchResponse requestPinBatch(
            @WebParam(partName = "PinBatchRequest", name = "SoapPinBatchRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPinBatchRequest pinBatchRequest
    );

    @WebMethod(operationName = "MakeProductRedeem")
    @WebResult(name = "SoapMakeProductRedeemResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "MakeProductRedeemResponse")
    public SoapMakeProductRedeemResponse makeProductRedeem(
            @WebParam(partName = "MakeProductRedeemRequest", name = "SoapMakeProductRedeemRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapMakeProductRedeemRequest makeProductRedeemRequest
    );

    @WebMethod(operationName = "MakePurchaseReprint")
    @WebResult(name = "SoapPurchaseReprintResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PurchaseReprintResponse")
    public SoapPurchaseReprintResponse makePurchaseReprint(
            @WebParam(partName = "PurchaseReprintRequest", name = "SoapPurchaseReprintRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPurchaseReprintRequest purchaseReprintRequest
    );

    @WebMethod(operationName = "RequestPinBatchStatus")
    @WebResult(name = "SoapPinBatchStatusResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PinBatchStatusResponse")
    public SoapPinBatchStatusResponse requestPinBatchStatus(
            @WebParam(partName = "PinBatchStatusRequest", name = "SoapPinBatchStatusRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPinBatchStatusRequest pinBatchStatusRequest
    );

    @WebMethod(operationName = "ReceivePinBatch")
    @WebResult(name = "SoapPinBatchReceiveResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PinBatchReceiveResponse")
    public SoapPinBatchReceiveResponse receivePinBatch(
            @WebParam(partName = "PinBatchReceiveRequest", name = "SoapPinBatchReceiveRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPinBatchReceiveRequest pinBatchReceiveRequest
    );

    @WebMethod(operationName = "MakePurchaseRefund")
    @WebResult(name = "SoapPurchaseRefundResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PurchaseRefundResponse")
    public SoapPurchaseRefundResponse makePurchaseRefund(
            @WebParam(partName = "PurchaseRefundRequest", name = "SoapPurchaseRefundRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPurchaseRefundRequest purchaseRefundRequest
    );

    @WebMethod(operationName = "MakePurchaseReversal")
    @WebResult(name = "SoapPurchaseReversalResponse", targetNamespace = "http://soap.api.novatti.com/types", partName = "PurchaseReversalResponse")
    public SoapPurchaseReversalResponse makePurchaseReversal(
            @WebParam(partName = "PurchaseReversalRequest", name = "SoapPurchaseReversalRequest", targetNamespace = "http://soap.api.novatti.com/types")
            SoapPurchaseReversalRequest purchaseReversalRequest
    );
}