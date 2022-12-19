package com.inov8.integration.channel.fonepay.service;

import com.inov8.integration.channel.fonepay.request.MPassPaymentRequest;
import com.inov8.integration.channel.fonepay.request.MerchantSummaryRequest;
import com.inov8.integration.channel.fonepay.response.MPassPaymentResponse;
import com.inov8.integration.channel.fonepay.response.MerchantSummaryResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * This class was generated by Apache CXF 3.1.8
 * 2017-11-08T13:38:29.171+05:00
 * Generated source version: 3.1.8
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "FonePayIntegration")
public interface FonePayIntegration {


    @WebMethod
    MPassPaymentResponse mPassPayment(
            @WebParam(name = "mpassPaymentRequest")
                    MPassPaymentRequest mpassPaymentRequest
    );

    @WebMethod
    MerchantSummaryResponse merchantDetail(
            @WebParam(name = "merchantSummaryRequest")
                    MerchantSummaryRequest merchantSummaryRequest



    );
}
