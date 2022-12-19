package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.AllPayIntegrationVO;
import java.io.Serializable;

public abstract interface AllPayIntegrationController extends Serializable
{
    public AllPayIntegrationVO getProducts(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;

    public AllPayIntegrationVO getProductByID(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;

    public AllPayIntegrationVO purchaseProduct(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;

    public AllPayIntegrationVO purchaseReverse(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;

    public AllPayIntegrationVO productPurchaseInquiry(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;

    public AllPayIntegrationVO resendProductPurchaseSMS(AllPayIntegrationVO paramAllPayIntegrationVO) throws Exception;
}
