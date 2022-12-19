package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.MonamiIntegrationVO;

public interface MonamiTransactionContrallerIF
{
    public MonamiIntegrationVO productPurchase(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO productRadeem(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO validateProductRadeem(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO makePurchaseRefund(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO makePurchaseReversal(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO getTransactionInfo(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;

    public MonamiIntegrationVO purchaseReprint(MonamiIntegrationVO paramMonamiIntegrationVO) throws Exception;
}
