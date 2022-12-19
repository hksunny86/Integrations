package com.inov8.integration.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.PhoenixTransactionVO;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;

public class SmsSwitchController implements SwitchController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	private CommonCommandManager commonCommandManager;

	@Override
	public IntegrationMessageVO activateAccount(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO billInquiry(IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO billPayment(IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO billPaymentAdvice(IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO changeMPIN(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO checkBalance(IntegrationMessageVO arg0)
			throws Exception {
		logger.debug("In SmsSwitchController.checkBalance()");
		return null;
	}

	@Override
	public IntegrationMessageVO statementRequest(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO chequeBookRequest(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO checkBalanceForCreditCard(
			IntegrationMessageVO arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO creditCardtransaction(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO customerAccountRelationshipInquiry(
			IntegrationMessageVO arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO extendedCustomerProfileInquiry(
			IntegrationMessageVO arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO generateMPIN(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO getMiniStatement(IntegrationMessageVO arg0)
			throws Exception {
		logger.debug("In SmsSwitchController.getMiniStatement()");
		return null;
	}

	@Override
	public List<PhoenixTransactionVO> getPhoenixTransactions(
			PhoenixTransactionVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO interBankFundTransfer(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO openAccountFundTransfer(
			IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO openAccountFTNarration(IntegrationMessageVO integrationMessageVO) {
		return null;
	}

	@Override
	public IntegrationMessageVO ownAccountFundTransfer(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO payBill(IntegrationMessageVO arg0)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO reverseFundTransfer(IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO rollback(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO thirdPartyFundTransfer(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO titleFetch(IntegrationMessageVO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO transaction(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO transactionStatus(IntegrationMessageVO arg0)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO verifyAccount(IntegrationMessageVO arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO viewBill(IntegrationMessageVO arg0)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	public CommonCommandManager getCommonCommandManager() {
		return commonCommandManager;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}

	@Override
	public IntegrationMessageVO creditAdvice(IntegrationMessageVO arg0)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO openAccountFundTransferAdvice(IntegrationMessageVO integrationMessageVO) {
		return null;
	}

	@Override
	public IntegrationMessageVO interBankFundTransferTitleFetch(IntegrationMessageVO integrationMessageVO) throws Exception {
		return null;
	}

	@Override
	public IntegrationMessageVO generateATMPIN(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO changeATMPIN(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO incomingInterBankFundTransfer(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO walletToWalletFundTransfer(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO walletCashWithdrawal(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO walletRetailPayment(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO reverseTransaction(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO cardStatusUpdate(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO cardStatusInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO validateATMPIN(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO validateInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO generateInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO changeInternetPassword(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO cardRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO debitCardInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO extendedCardInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO changeChannelDelivery(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO customerExtendedProfile(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO accountAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO relationshipAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO profileAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO accountRelationshipAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO cnicToPanRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO panToAccountRelationshipInquiry(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO customerAccountLink(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO onlineRetailTransaction(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO onlineRetailTransactionReversal(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO subscriptionFeeDeduction(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO otpVerification(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO cardAddition(IntegrationMessageVO integrationMessageVO) throws RuntimeException {
		return null;
	}

	@Override
	public IntegrationMessageVO markBOPPayment(IntegrationMessageVO arg0)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMessageVO verifyApplicantbyBiometrics(
			IntegrationMessageVO arg0) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
