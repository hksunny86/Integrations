package com.inov8.microbank.server.service.switchmodule.iris;

import java.util.List;

import com.inov8.integration.vo.AccessIntegrationVO;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.PhoenixTransactionVO;


public interface SwitchController {
    IntegrationMessageVO statementRequest(IntegrationMessageVO requestVO) throws RuntimeException;

    IntegrationMessageVO chequeBookRequest(IntegrationMessageVO requestVO) throws RuntimeException;

    public IntegrationMessageVO checkBalanceForCreditCard(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO transaction(IntegrationMessageVO integrationMessageVO) throws Exception;

    //	public IntegrationMessageVO transactionForCreditCard(IntegrationMessageVO integrationMessageVO) throws Exception;
    public IntegrationMessageVO creditCardtransaction(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO customerAccountRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO activateAccount(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO verifyAccount(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO getMiniStatement(IntegrationMessageVO integrationMessageVO) throws Exception;

    //	public IntegrationMessageVO getMiniStatementForCreditCard(IntegrationMessageVO integrationMessageVO) throws Exception;
    public IntegrationMessageVO generateMPIN(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO changeMPIN(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO extendedCustomerProfileInquiry(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO rollback(IntegrationMessageVO integrationMessageVO) throws Exception;


    public IntegrationMessageVO ownAccountFundTransfer(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO thirdPartyFundTransfer(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO interBankFundTransfer(IntegrationMessageVO integrationMessageVO) throws Exception;

    //ASKARI PHOENIX INTEGRATION
    public IntegrationMessageVO checkBalance(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO billInquiry(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO titleFetch(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO billPaymentAdvice(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO billPayment(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO openAccountFundTransfer(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO openAccountFTNarration(IntegrationMessageVO integrationMessageVO);

    public List<PhoenixTransactionVO> getPhoenixTransactions(PhoenixTransactionVO phoenixTransactionVO);

    public IntegrationMessageVO transactionStatus(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO reverseFundTransfer(IntegrationMessageVO integrationMessageVO);

    //	public IntegrationMessageVO getNadraBillCompanies(IntegrationMessageVO integrationMessageVO)throws RuntimeException;
    public IntegrationMessageVO viewBill(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO payBill(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO verifyApplicantbyBiometrics(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO markBOPPayment(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO creditAdvice(IntegrationMessageVO integrationMessageVO) throws RuntimeException;


    public IntegrationMessageVO openAccountFundTransferAdvice(IntegrationMessageVO integrationMessageVO);

    public IntegrationMessageVO interBankFundTransferTitleFetch(IntegrationMessageVO integrationMessageVO) throws Exception;

    public IntegrationMessageVO generateATMPIN(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO changeATMPIN(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO incomingInterBankFundTransfer(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO walletToWalletFundTransfer(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO walletCashWithdrawal(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;
    public IntegrationMessageVO walletRetailPayment(IntegrationMessageVO paramIntegrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO reverseTransaction(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO cardStatusUpdate(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO cardStatusInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO validateATMPIN(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO validateInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO generateInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO changeInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO cardRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO debitCardInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO extendedCardInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO changeChannelDelivery(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO customerExtendedProfile(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO accountAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO relationshipAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO profileAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO accountRelationshipAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO cnicToPanRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO panToAccountRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException;
    public IntegrationMessageVO customerAccountLink(IntegrationMessageVO integrationMessageVO) throws RuntimeException;
    public IntegrationMessageVO onlineRetailTransaction(IntegrationMessageVO integrationMessageVO) throws RuntimeException;
    public IntegrationMessageVO onlineRetailTransactionReversal(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO subscriptionFeeDeduction(IntegrationMessageVO integrationMessageVO) throws RuntimeException;
    public IntegrationMessageVO otpVerification(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

    public IntegrationMessageVO cardAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException;

}