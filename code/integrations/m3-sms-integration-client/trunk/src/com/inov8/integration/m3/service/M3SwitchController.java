package com.inov8.integration.m3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.PhoenixTransactionVO;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

@Controller
public class M3SwitchController implements SwitchController{
	
	@Autowired
	M3SmsService m3SmsService;

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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
	public IntegrationMessageVO transaction(IntegrationMessageVO messageVO)
			throws Exception {
		return m3SmsService.sendSms((PhoenixIntegrationMessageVO)messageVO);
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

	public M3SmsService getM3SmsService() {
		return m3SmsService;
	}

	public void setM3SmsService(M3SmsService m3SmsService) {
		this.m3SmsService = m3SmsService;
	}

}
