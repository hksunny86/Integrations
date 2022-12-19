package com.inov8.microbank.server.service.postedtransactionmodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.enums.TransactionCodeEnum;
import com.inov8.integration.enums.TransactionStatus;
import com.inov8.integration.pdu.parser.request.BillPaymentMarkingAdviceRequestParser;
import com.inov8.integration.pdu.parser.request.HeaderRequestParser;
import com.inov8.integration.pdu.parser.request.OpenAccountBIRequestParser;
import com.inov8.integration.pdu.parser.request.OpenAccountFTRequestParser;
import com.inov8.integration.pdu.request.BillPaymentMarkingAdvice;
import com.inov8.integration.pdu.request.OpenAccountBalanceInquiry;
import com.inov8.integration.pdu.request.OpenAccountFT;
import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.util.FormatUtils;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.TransactionLogModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.TransactionLogDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;


public class PostedTransactionsManagerImpl implements PostedTransactionsManager {
	
	private TransactionLogDAO transactionLogDAO;
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private ActionLogManager actionLogManager;
	
	public PostedTransactionsManagerImpl() {
	}
	
	private void populateTransactionDetails(TransactionLogModel trxModel){
		try {
			trxModel.setDisplayRRN(trxModel.getRrn().substring(8));
			if(trxModel.getStatusId() != null){
				if(trxModel.getStatusId().intValue() == TransactionStatus.REVERSED.getValue().intValue()){
					trxModel.setDisplayStatus("Reversed");
				}else if(trxModel.getStatusId().intValue() == TransactionStatus.EXTERNALLY_RESOLVED.getValue().intValue()){
					trxModel.setDisplayStatus("Externally Resolved");
				}else if(trxModel.getStatusId().intValue() == TransactionStatus.RECTIFICATION_NOT_REQUIRED.getValue().intValue()){
					trxModel.setDisplayStatus("Rectification Not Required");
				}else if(trxModel.getStatusId().intValue() == TransactionStatus.REVERSAL_UNDELIVERED.getValue().intValue()){
					trxModel.setDisplayStatus("Auto Reversal Failed");
				}else if(trxModel.getStatusId().intValue() == TransactionStatus.TIMEOUT.getValue().intValue()
						&& trxModel.getRetryCount() > 0){
					trxModel.setDisplayStatus("Auto Reversal In-progress");
				}
				
			}
			trxModel.setRequestPDU(trxModel.getRequestPDU().substring(2));
			
			PhoenixRequestHeader header = HeaderRequestParser.parseHeader(trxModel.getRequestPDU().getBytes());
			
			String trxCode = trxModel.getTransactionType();
			if (trxCode.equals(TransactionCodeEnum.BILL_PAYMENT_ADVICE.getValue())) {
				trxModel.setDisplayTransactionType("Bill Payment");
				BillPaymentMarkingAdvice bpma = BillPaymentMarkingAdviceRequestParser.parse(trxModel.getRequestPDU().getBytes(), header);
				String amount = bpma.getAmountPaid().getValue();
				trxModel.setAmount(FormatUtils.parsePhoenixAmount(amount));
				trxModel.setFromAccount(bpma.getFromAccountNumber().getValue());
			} else if (trxCode.equals(TransactionCodeEnum.ACCOUNT_OPEN_FUND_TRANSFER.getValue())) {
				trxModel.setDisplayTransactionType("Fund Transfer");
				OpenAccountFT ft = OpenAccountFTRequestParser.parse(trxModel.getRequestPDU().getBytes(), header);
				String amount = ft.getTransactionAmount().getValue();
				trxModel.setAmount(FormatUtils.parsePhoenixAmount(amount));
				trxModel.setFromAccount(ft.getFromAccountNumber().getValue());
				trxModel.setToAccount(ft.getToAccountNumber().getValue());
			} else if (trxCode.equals(TransactionCodeEnum.OPEN_ACCOUNT_BALANCE_INQUIRY.getValue())) {
				trxModel.setDisplayTransactionType("Balance Inquiry");
				OpenAccountBalanceInquiry biRequest = OpenAccountBIRequestParser.parse(trxModel.getRequestPDU().getBytes(), null);
				trxModel.setFromAccount(biRequest.getAccountNumber().getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public SearchBaseWrapper searchPostedTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		CustomList<TransactionLogModel> customList = transactionLogDAO.searchPostedTransactions(searchBaseWrapper);
		searchBaseWrapper.setCustomList(customList);
		
		List<TransactionLogModel> transList = null;
		if( customList != null){
        	transList = customList.getResultsetList();
        }

		if (transList != null && transList.size() > 0) {
			for (TransactionLogModel pTrx : transList) {
				this.populateTransactionDetails(pTrx);
			}
		}
		
		return searchBaseWrapper;
	}
	
	public Boolean resetPostedTransaction(Long transactionLogId, String resolutionType){
		Boolean result = Boolean.FALSE;
		if(StringUtil.isNullOrEmpty(resolutionType) 
				|| !(resolutionType.equals("FT") || resolutionType.equals("RNR") || resolutionType.equals("ER"))){
			
			return result;
		}
		
		ActionLogModel actionLogModel = new ActionLogModel();
		try{
			TransactionLogModel model = transactionLogDAO.findByPrimaryKey(transactionLogId);
			if(model!=null){
				if(resolutionType.equals("FT")){
					actionLogModel = this.logActionStart(PortalConstants.REVERSE_FT_USECASE_ID);
					PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO();
					phoenixIntegrationMessageVO.setRetrievalReferenceNumber(model.getRrn());
					phoenixIntegrationMessageVO.setChannelId("MR0001");
					
					SwitchWrapper switchWrapper = new SwitchWrapperImpl();
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					switchWrapper = phoenixFinancialInstitution.reverseFundTransfer(switchWrapper);
					this.logActionEnd(actionLogModel,model.getRrn());
					result = Boolean.TRUE;
				}else if(resolutionType.equals("RNR")){
					actionLogModel = this.logActionStart(PortalConstants.NO_RECTIFICATION_USECASE_ID);
					model.setStatusId(new Long(TransactionStatus.RECTIFICATION_NOT_REQUIRED.getValue()));
					transactionLogDAO.saveOrUpdate(model);
					this.logActionEnd(actionLogModel,model.getRrn());
					result = Boolean.TRUE;
				}else if(resolutionType.equals("ER")){
					actionLogModel = this.logActionStart(PortalConstants.EXTERNALLY_RECTIFIED_USECASE_ID);
					model.setStatusId(new Long(TransactionStatus.EXTERNALLY_RESOLVED.getValue()));
					transactionLogDAO.saveOrUpdate(model);
					this.logActionEnd(actionLogModel,model.getRrn());
					result = Boolean.TRUE;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

	public TransactionLogModel loadTransactionDetail(Long transactionLogId) throws FrameworkCheckedException{
		TransactionLogModel transactionLogModel = transactionLogDAO.findByPrimaryKey(transactionLogId);
		this.populateTransactionDetails(transactionLogModel);
		
		TransactionLogModel exampleModel = new TransactionLogModel();
		exampleModel.setParentTransactionId(transactionLogId);
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>(); 
        sortingOrderMap.put("createdOn", SortingOrder.ASC);
        ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
		configModel.setMatchMode(MatchMode.EXACT);
		CustomList<TransactionLogModel> list = this.transactionLogDAO.findByExample(exampleModel,null,sortingOrderMap,configModel);
		List<TransactionLogModel> populatedTrxList = null;
		if( list != null){
			populatedTrxList = list.getResultsetList();
        }
		if (populatedTrxList != null && populatedTrxList.size() > 0) {
			for (TransactionLogModel pTrx : populatedTrxList) {
				this.populateTransactionDetails(pTrx);
			}
		}
		
		transactionLogModel.setReversalTransactionList(populatedTrxList);
		
		return transactionLogModel;
	}

	private ActionLogModel logActionStart(Long useCaseId) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(PortalConstants.ACTION_UPDATE);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel.setUsecaseId(useCaseId);

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		return (ActionLogModel)baseWrapper.getBasePersistableModel();
	}

	private ActionLogModel logActionEnd( ActionLogModel actionLogModel, String rrn ) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setCustomField1(rrn);
		
		baseWrapper.setBasePersistableModel(actionLogModel);
		baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		return (ActionLogModel)baseWrapper.getBasePersistableModel();
	}
	
	public void setTransactionLogDAO(TransactionLogDAO transactionLogDAO) {
		this.transactionLogDAO = transactionLogDAO;
	}
	
	public void setPhoenixFinancialInstitution(	AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
}
