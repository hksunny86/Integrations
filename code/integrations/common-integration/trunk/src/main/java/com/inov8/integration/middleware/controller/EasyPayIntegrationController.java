package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.EasyPayIntegrationVO;
import com.inov8.integration.vo.MoneySendIntegrationVO;
import com.inov8.integration.vo.VirtualCardIntegrationVO;

import java.io.Serializable;

public interface EasyPayIntegrationController extends Serializable {

    EasyPayIntegrationVO initiateTransaction(EasyPayIntegrationVO integrationVO) throws Exception;

    EasyPayIntegrationVO inquireTransaction(EasyPayIntegrationVO integrationVO) throws Exception;

    EasyPayIntegrationVO instantPaymentNotification(EasyPayIntegrationVO integrationVO) throws Exception;

    EasyPayIntegrationVO getTagParingDetail(EasyPayIntegrationVO easyPayIntegrationVO) throws Exception;

    EasyPayIntegrationVO saveNFCPayment(EasyPayIntegrationVO easyPayIntegrationVO) throws Exception;

    EasyPayIntegrationVO getNFCPaymentDetail(EasyPayIntegrationVO easyPayIntegrationVO) throws  Exception;

    EasyPayIntegrationVO paymentReversal(EasyPayIntegrationVO easyPayIntegrationVO) throws Exception;

    EasyPayIntegrationVO getMerchantSummary(EasyPayIntegrationVO middlewareVO) throws Exception;

    EasyPayIntegrationVO getMerchantInfo(EasyPayIntegrationVO middlewareVO) throws Exception;

    EasyPayIntegrationVO retailPayment(EasyPayIntegrationVO middlewareVO) throws Exception;

    MoneySendIntegrationVO externalTrxNotification(MoneySendIntegrationVO moneySendIntegrationVO) throws Exception;

    VirtualCardIntegrationVO onlinePayment(VirtualCardIntegrationVO integrationVO) throws Exception;

}
