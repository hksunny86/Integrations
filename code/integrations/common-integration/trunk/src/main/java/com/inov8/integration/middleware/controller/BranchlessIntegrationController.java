package com.inov8.integration.middleware.controller;


import com.inov8.integration.vo.BranchlessIntegrationVO;

public interface BranchlessIntegrationController {

    public BranchlessIntegrationVO initiateOTP(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO withdrawalTrans(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO depositTrans(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO reversalTrans(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO titleFetch(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO transactionInquiry(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO withdrawalPinlessTrans(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO cardInquiry(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO cardTagging(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO checkIn(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO checkOut(BranchlessIntegrationVO integrationVO);

    public BranchlessIntegrationVO linkCustomerAccount(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO verifyCustomer(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO paymentInfo(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO paymentRequest(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO paymentReversal(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO initiateBalanceInquiry(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO balanceInquiry(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO registrationInquiry(BranchlessIntegrationVO integrationVO);
    public BranchlessIntegrationVO accountRegistration(BranchlessIntegrationVO integrationVO);

}
