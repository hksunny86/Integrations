package com.inov8.microbank.webapp.action.portal.transactionreversal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AccountCreditQueueLogModel;
import com.inov8.microbank.common.util.CreditAccountQueueSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.TransactionTypeConstants;

public class RetryCreditAccountAjaxRequestController extends AjaxController {

	/**
	 * @author kashif
	 */
	
	@Autowired
	private TransactionReversalFacade transactionReversalFacade;
	@Autowired
	private CreditAccountQueueSender creditAccountQueueSender;


	@Override
	public String getResponseContent(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		AccountCreditQueueLogModel accountCreditQueueLogModel = new AccountCreditQueueLogModel();
		
		String _accountCreditQueueLogId = (String) request.getParameter(accountCreditQueueLogModel.getPrimaryKeyFieldName());
		
		if(!StringUtil.isNullOrEmpty(_accountCreditQueueLogId)) {
						
			accountCreditQueueLogModel.setPrimaryKey(Long.valueOf(_accountCreditQueueLogId));
			
			try {
				
				accountCreditQueueLogModel = transactionReversalFacade.findAccountCreditQueueLogModel(accountCreditQueueLogModel);
				
				accountCreditQueueLogModel.setStatus(accountCreditQueueLogModel.PROCESSING);
				transactionReversalFacade.createSaveAccountCreditQueueLogModel(accountCreditQueueLogModel);

				OLAVO olaVO = this.prepareOLAVO(accountCreditQueueLogModel);

				pushCreditQueue(olaVO);
				
				ajaxXmlBuilder.addItem("mesg", "Advice has been pushed to SAF");
			} 
			catch (FrameworkCheckedException ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().equals("Already pushed to SAF, you cannot retry this advice.")){
					ajaxXmlBuilder.addItem("mesg",ex.getMessage());
				}else{
					ajaxXmlBuilder.addItem("mesg",
							"Some error has occured while pushing to SAF");
				}
			}
		}
		
		return ajaxXmlBuilder.toString();
	}

	
	private OLAVO prepareOLAVO(AccountCreditQueueLogModel accountCreditQueueLogModel) {
		
		OLAVO olaVO = new OLAVO();
		
		olaVO.setAccountCreditQueueLogId(accountCreditQueueLogModel.getAccountCreditQueueLogId());
		olaVO.setStatusName(accountCreditQueueLogModel.getStatus());		
		olaVO.setAuthCode(accountCreditQueueLogModel.getAuthCode());
		olaVO.setBalance(accountCreditQueueLogModel.getBalance());
		olaVO.setCustomerAccountTypeId(accountCreditQueueLogModel.getCustomerAccountTypeId());
		olaVO.setAddress(accountCreditQueueLogModel.getDescription());
		
		if(accountCreditQueueLogModel.getTransactionTypeId() == TransactionTypeConstants.CREDIT){
			olaVO.setIsCreditPush(Boolean.TRUE);
			olaVO.setToBalanceAfterTransaction(accountCreditQueueLogModel.getBalanceAfterTransaction());
		}else{
			olaVO.setIsDebitPush(Boolean.TRUE);
			olaVO.setFromBalanceAfterTransaction(accountCreditQueueLogModel.getBalanceAfterTransaction());
		}
		
		olaVO.setLedgerId(accountCreditQueueLogModel.getLedgerId());
		olaVO.setReasonId(accountCreditQueueLogModel.getReasonId());
		olaVO.setResponseCode(accountCreditQueueLogModel.getResponseCode());
		olaVO.setTransactionDateTime(accountCreditQueueLogModel.getTransactionDateTime());
		olaVO.setTransactionTypeId(String.valueOf(accountCreditQueueLogModel.getTransactionTypeId()));
		olaVO.setPayingAccNo(accountCreditQueueLogModel.getToBankAccountNumber());
		olaVO.setReceivingAccountId(accountCreditQueueLogModel.getReceivingAccountId());
		olaVO.setMicrobankTransactionCode(accountCreditQueueLogModel.getTransactionCode());
		olaVO.setStatusName(AccountCreditQueueLogModel.PROCESSING);
		olaVO.setIsViaQueue(Boolean.TRUE);

		return olaVO;
	}
	
	/**
	 * @param olaVO
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private OLAVO pushCreditQueue(OLAVO olaVO) throws FrameworkCheckedException {

//		olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString()) ;
//		olaVO.setIsCreditPush(Boolean.TRUE);
		
		try {
			logger.info("Sending in Queue ACID : "+olaVO.getAccountId());
			creditAccountQueueSender.send(olaVO);
		
		} catch (Exception e) {

			logger.error(e);
			throw new FrameworkCheckedException("Failed to queue Credit Account Id: "+olaVO.getReceivingAccountId());
		}
		
		return olaVO;
	}	
	
	public void setTransactionReversalFacade(TransactionReversalFacade transactionReversalFacade) {
		this.transactionReversalFacade = transactionReversalFacade;
	}
	
	public void setCreditAccountQueueSender(CreditAccountQueueSender creditAccountQueueSender) {
		this.creditAccountQueueSender = creditAccountQueueSender;
	}
}