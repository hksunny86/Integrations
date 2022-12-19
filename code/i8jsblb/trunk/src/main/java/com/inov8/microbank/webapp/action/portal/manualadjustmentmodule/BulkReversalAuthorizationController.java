package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkReversalRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.server.dao.portal.manualadjustmentmodule.BulkReversalDAO;
import com.inov8.microbank.server.facade.manualadjustmentmodule.ManualReversalFacadeImpl;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.TransactionTypeConstants;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.owasp.esapi.User;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BulkReversalAuthorizationController extends AdvanceAuthorizationFormController {

	private static final Logger LOGGER = Logger.getLogger( ManualAdjustmentAuthorizationController.class );
//	private ManualAdjustmentManager manualAdjustmentManager;
	private ManualReversalManager manualReversalManager;
	private ReferenceDataManager referenceDataManager;
	private PortalOlaFacade portalOlaFacade;
	private ManualReversalManager manualReversalFacade;
	private BulkReversalDAO bulkReversalDAO;

	public BulkReversalAuthorizationController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

		if (escalateRequest || resolveRequest){
			ActionStatusModel actionStatusModel = new ActionStatusModel();
			ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			List<ActionStatusModel> actionStatusModelList;
			actionStatusModelList=refDataWrapper.getReferenceDataList();
			List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

			for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
				if(((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
						||(actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()))
						&& escalateRequest )
					tempActionStatusModelList.add(actionStatusModel2);
				else if((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
					tempActionStatusModelList.add(actionStatusModel2);
			}
			referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);

			////// Action Authorization history////
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

			List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

			refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );

			actionAuthorizationHistoryModelList=refDataWrapper.getReferenceDataList();

			referenceDataMap.put( "actionAuthorizationHistoryModelList",actionAuthorizationHistoryModelList );

			if(actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getCreatedById().longValue()== UserUtils.getCurrentUser().getAppUserId()){
				boolean isAssignedBack=false;
				isAssignedBack=true;
				request.setAttribute( "isAssignedBack",isAssignedBack );
			}
		}
		return referenceDataMap;
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

		if (escalateRequest || resolveRequest){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

			XStream xstream = new XStream();
//			BulkManualAdjustmentRefDataModel manualAdjustmentRefDataModel = (BulkManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			BulkReversalVOModel bulkReversalVOModel = (BulkReversalVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkReversalVOModel.getBatchId();
			BulkAutoReversalModel bulkAutoReversalModel = new BulkAutoReversalModel();
			bulkAutoReversalModel.setBatchId(batchId);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(bulkAutoReversalModel);

			List<BulkAutoReversalModel> resultList = manualReversalManager.loadBulkReversalModelList(searchBaseWrapper);

			Double totalAmount = 0d;
			for (BulkAutoReversalModel model : resultList) {
//				totalAmount += model.getAmount();
			}
			request.setAttribute("bulkAutoReversalModelList",resultList);
			request.setAttribute("totalAmount",totalAmount);
			request.setAttribute("trxCount",resultList.size());
			/*request.setAttribute("transactionCodeId",manualAdjustmentRefDataModel.getTransactionCodeId());
			request.setAttribute("adjustmentType",manualAdjustmentRefDataModel.getAdjustmentType());
			request.setAttribute("fromACNo",manualAdjustmentRefDataModel.getFromACNo());
			request.setAttribute("fromACNick",manualAdjustmentRefDataModel.getFromACNick());
			request.setAttribute("toACNo",manualAdjustmentRefDataModel.getToACNo());
			request.setAttribute("toACNick",manualAdjustmentRefDataModel.getToACNick());
			request.setAttribute("amount",manualAdjustmentRefDataModel.getAmount());
			request.setAttribute("comments",manualAdjustmentRefDataModel.getComments());*/

			return actionAuthorizationModel;
		}
		else
			return new ActionAuthorizationModel();
	}



	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
									BindException errors) throws Exception
	{
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
									BindException errors) throws Exception
	{
		return null;
	}


	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		return null;
	}

	private boolean validateManualReversalForm(ManualReversalVO manualReversalVO){

		Integer adjustmentType = manualReversalVO.getAdjustmentType();
		if(adjustmentType == null || adjustmentType.intValue() == 0){
			LOGGER.error("Adjustment Type empty");
			return false;
		}

		if(!StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode())){
			TransactionDetailMasterModel tdmModel = null;
			try {
				tdmModel = this.manualReversalFacade.getTransactionDetailMasterModel(manualReversalVO.getTransactionCode());
			} catch (FrameworkCheckedException e) {
				LOGGER.error("Exception while loading TransactionDetailMasterModel for trx ID:"+manualReversalVO.getTransactionCode());
				return false;
			}
			if(tdmModel == null){
				LOGGER.error("Invalid Trx ID provided:"+manualReversalVO.getTransactionCode());
				return false;
			}

			if(SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == tdmModel.getSupProcessingStatusId() && adjustmentType.intValue() == 1){
				LOGGER.error("Transaction status is already Reverse Completed for trx ID:"+manualReversalVO.getTransactionCode());
				manualReversalVO.setIsReversed("1");
				manualReversalVO.setComments("Transaction status is already Reverse Completed");
				return false;
			}

			// 08-MAR-2017 check commented on request of JSBL
			/*if(adjustmentType.intValue() == 1 &&
					(tdmModel.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.BULK_PAYMENT
							) && (tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.COMPLETED ||
							tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.REVERSE_COMPLETED)){

				logger.error("Transaction reversal is not allowed for " + tdmModel.getProductName() +  "  with status completed / reversed-completed");
				super.saveMessage(req,"Transaction reversal is not allowed for " + tdmModel.getProductName() + " with status completed / reversed-completed ");
				return false;
			}*/

		}


		List<BbStatementAllViewModel> entryList = manualReversalVO.getFundTransferEntryList();
		if(entryList==null || entryList.size() == 0){
			LOGGER.error("Entry list is empty");
			return false;
		}

		double totalDebit = 0;
		double totalCredit = 0;
		double fmtAmount = 0;

		for(BbStatementAllViewModel entry: entryList){
			BBAccountsViewModel model = new BBAccountsViewModel();
			if(StringUtil.isNullOrEmpty(entry.getAccountNumber())){
				LOGGER.error("Account Number is empty");
				return false;
			}else{
				try {
					model.setAccountNumber(com.inov8.ola.util.EncryptionUtil.encryptWithDES(entry.getAccountNumber()));
					model = manualReversalManager.getBBAccountsViewModel(model);
					if(model !=null){
						if(model.getTitle() == null || model.getTitle().equals("null") || model.getTitle().trim().equals("")){
							LOGGER.error("Account does not exist against the  account number : " + entry.getAccountNumber());
							manualReversalVO.setComments("Account does not exist against the  account number");
							return false;
						}else if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
							LOGGER.info("Settlement Acc Type loaded against accNumber:"+entry.getAccountNumber()+" ... so SKIPPING account status/active check");
						}else{
							if( model.getIsActive()== null || !model.getIsActive() ){
								LOGGER.error("Account is not active against the account number : " + entry.getAccountNumber());
								manualReversalVO.setComments("Account is not active against the account number");
								return false;
							}
							if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
								LOGGER.error("Account Status is not active against the account number : " + entry.getAccountNumber() );
								manualReversalVO.setComments("Account Status is not active against the account number");
								return false;
							}
							if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
								LOGGER.error("Account is CLOSED against the account number : " + entry.getAccountNumber());
								manualReversalVO.setComments("Account is CLOSED against the account number");
								return false;
							}
						}

					}

					else{
						LOGGER.error(entry.getAccountNumber() + "Account Number does not exist");
						manualReversalVO.setComments("Account Number does not exist");
						return false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(StringUtil.isNullOrEmpty(entry.getAmountStr())){
				LOGGER.error("Amount is empty");
				return false;
			}else{
				try{
					entry.setAmount(parseAmount(entry.getAmountStr()));
				}catch(NumberFormatException e){
					LOGGER.error("Manual Reversal: Invalid Amount:"+entry.getAmountStr(),e);
					return false;
				}
				fmtAmount = formatAmount(entry.getAmount());
				entry.setAmount(fmtAmount);
				if(fmtAmount < 0.01D){
					LOGGER.error("Manual Reversal: Amount cannot be less than 0.01");
					return false;
				}
				if(fmtAmount > 999999999999.99D){
					LOGGER.error("Manual Reversal: Amount cannot be greater than 999999999999.99");
					return false;
				}
			}
			if(entry.getTransactionType() == null){
				LOGGER.error("Transaction type is empty");
				return false;
			}

			if(entry.getTransactionType().longValue() == TransactionTypeConstants.DEBIT){
				totalDebit += fmtAmount;
			}else{
				totalCredit += fmtAmount;
			}
		}

		if(totalDebit == 0){
			LOGGER.error("Total debit amount is 0");
			return false;
		}else if(totalCredit == 0){
			LOGGER.error("Total credit amount is 0");
			return false;
		}else if(formatAmount(totalDebit).longValue() != formatAmount(totalCredit).longValue()){
			LOGGER.error("Total debit and credit amount is not equal... Total Debit:"+totalDebit+" Total Credit:"+totalCredit);
			return false;
		}

		manualReversalVO.setTotalAmount(totalDebit);

		return true;
	}
	private Double parseAmount(String amountStr){
		if(StringUtil.isNullOrEmpty(amountStr)){
			return 0.0D;
		}else{
			return Double.parseDouble(amountStr);
		}
	}

	private Double formatAmount(Double amount){
		if(amount == null){
			return 0.0D;
		}else{
			String amt = Formatter.formatDouble(amount);
			return Double.parseDouble(amt);
		}
	}

	@Override
	protected ModelAndView onEscalate(HttpServletRequest req,HttpServletResponse resp, Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command; 
		String partnersAssociation[] = req.getParameterValues("checkedList");
		try{
			XStream xstream = new XStream();

			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
			boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
			long currentUserId= UserUtils.getCurrentUser().getAppUserId();
			
			BulkReversalVOModel bulkReversalVOModel = (BulkReversalVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkReversalVOModel.getBatchId();

			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}

				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.BULK_AUTO_REVERSAL_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
				if(nextAuthorizationLevel<1){
					//Check This bulkAdjustmentId//
					manualReversalManager.updateIsApprovedForBatch(batchId , partnersAssociation );
					List<BulkAutoReversalModel> bulkAutoReversalModelList = loadBulkReversalModelList(batchId);
					if (CollectionUtils.isNotEmpty(bulkAutoReversalModelList)) {
						for (BulkAutoReversalModel bulkModel : bulkAutoReversalModelList) {
							try{
								BulkReversalRefDataModel bulkReversalRefDataModel = convertBulkReversalToRefDataModel(bulkModel, actionAuthorizationModel);
								ManualReversalVO manualReversalVO = new ManualReversalVO();
								manualReversalVO.setTransactionCode(bulkReversalRefDataModel.getTrxnId());
								manualReversalVO.setAdjustmentType(1);
								manualReversalVO.setInitiatorAppUserId(UserUtils.getCurrentUser().getAppUserId());
								Integer adjustmentType = 1;
								boolean isReversal = (adjustmentType != null && adjustmentType == 1) ? true : false;
								BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
								bbStatementAllViewModel.setTransactionCode(bulkReversalRefDataModel.getTrxnId());
								LinkedHashMap<String,SortingOrder> sortingMap = new LinkedHashMap<String,SortingOrder>();
								sortingMap.put("category", SortingOrder.ASC);
								sortingMap.put("ledgerId", SortingOrder.ASC);
								SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
								sbWrapper.setSortingOrderMap(sortingMap);
								sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
								List<BbStatementAllViewModel>  settlementBBStatementList =  portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
								if(null!=settlementBBStatementList && settlementBBStatementList.size()>0){
									for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
										modelInList.setAccountNumber(EncryptionUtil.decryptAccountNo( modelInList.getAccountNumber()));
										boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
										if(isDebit){
											modelInList.setAmountStr(isReversal ? com.inov8.microbank.common.util.Formatter.formatDouble(modelInList.getDebitAmount()) : "");
											modelInList.setCreditAmount(null);
											modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
										}else{
											modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
											modelInList.setDebitAmount(null);
											modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
										}
									}
								}
								manualReversalVO.setFundTransferEntryList(settlementBBStatementList);
								boolean validReversal = this.validateManualReversalForm(manualReversalVO);
								if(!validReversal){
									bulkModel.setErrorDescription(manualReversalVO.getComments());
								}
								if (validReversal) {
									manualReversalManager.makeReversal(manualReversalVO);
									bulkModel.setIsProcessed(true);
								}
								bulkReversalDAO.saveOrUpdate(bulkModel);
							}catch(Exception ex){
								logger.error("Failed to pushBulkReversalToQueue for TrnxId:" + bulkModel.getTrxnId() + " Exception:" + ex.getMessage(), ex);
							}

						}
					}

					String msg = this.getText("bulkreversal.add.success", req.getLocale());
					String newMsg = msg + " against Batch ID : "+batchId;
					this.saveMessage(req, newMsg);

					if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()){
						approvedWithIntimationLevelsNext(actionAuthorizationModel,model, usecaseModel,req);
					}
					else
					{
						approvedAtMaxLevel(actionAuthorizationModel, model);
					}
				}else{
					escalateToNextLevel(actionAuthorizationModel,model, nextAuthorizationLevel, usecaseModel.getUsecaseId(),req);
				}

			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				actionDeniedOrCancelled(actionAuthorizationModel, model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
					&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
					|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){

				if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				actionDeniedOrCancelled(actionAuthorizationModel,model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				requestAssignedBack(actionAuthorizationModel,model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){

				if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
			}else{
				throw new FrameworkCheckedException("Invalid status marked");
			}
		}catch(WorkFlowException wfe){
			wfe.printStackTrace();
			this.showForm(req, resp, errors);
			req.setAttribute("message", super.getText("bulkreversal.add.failure.lowbalance", req.getLocale()));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			String msg = null;
			if(null!=fce.getMessage() && fce.getMessage().contains("Same BB Accounts Type"))
				msg = "Bulk Reversal for Customer to Customer BLB A/c or Agent to Agent BLB A/c is not Allowed";
			else if(fce.getMessage().contains("You are not authorized to update action status."))
				msg = "You are not authorized to update action status.";
			else if(fce.getMessage().contains("Invalid status marked"))
				msg = "Invalid status marked";
			else
				msg = super.getText("bulkreversal.add.failure", req.getLocale());

			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);

		}catch(Exception ex){
			ex.printStackTrace();
			String msg = super.getText("bulkreversal.add.failure", req.getLocale());
			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}

		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
		modelAndView = super.showForm(req, resp, errors);
		return modelAndView;
	}


	@Override
	protected ModelAndView onResolve(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {

		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		String partnersAssociation[] = req.getParameterValues("checkedList"); 
		try{
			XStream xstream = new XStream();

			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			BulkReversalVOModel bulkReversalVOModel = (BulkReversalVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkReversalVOModel.getBatchId();

			manualReversalManager.updateIsApprovedForBatch(batchId , partnersAssociation);
			List<BulkAutoReversalModel> bulkAutoReversalModelList = loadBulkReversalModelList(batchId);
			if (CollectionUtils.isNotEmpty(bulkAutoReversalModelList)) {
				
			//	manualReversalManager.updateIsApprovedForBatch(batchId , partnersAssociation);
				
				for (BulkAutoReversalModel bulkModel : bulkAutoReversalModelList) {
					try{
						
						BulkReversalRefDataModel bulkReversalRefDataModel = convertBulkReversalToRefDataModel(bulkModel, actionAuthorizationModel);
						manualReversalManager.pushBulkReversalToQueue(bulkReversalRefDataModel);
					}catch(Exception ex){

						logger.error("Failed to pushBulkManualAdjustmentToQueue for TrnxId:" + bulkModel.getTrxnId() + " Exception:" + ex.getMessage(), ex);
					}
				}
			}

			String msg = this.getText("manualadjustment.add.success", req.getLocale());
			String newMsg=msg+" against Batch ID : "+batchId;
			this.saveMessage(req, newMsg);

			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel, req);

		}
		catch (Exception ex)
		{
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			req.setAttribute("message", ex.getMessage());
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}
		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
		modelAndView = super.showForm(req, resp, errors);
		return modelAndView;
	}

	private List<BulkAutoReversalModel> loadBulkReversalModelList(Long batchID) throws Exception{
		BulkAutoReversalModel bulkAutoReversalModel = new BulkAutoReversalModel();
		bulkAutoReversalModel.setBatchId(batchID);
//		bulkAutoReversalModel.setIsSkipped(false);
		bulkAutoReversalModel.setIsProcessed(Boolean.FALSE);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(bulkAutoReversalModel);

		return manualReversalManager.loadBulkReversalModelList(searchBaseWrapper);
	}

	private BulkReversalRefDataModel convertBulkReversalToRefDataModel(BulkAutoReversalModel bulkAutoReversalModel, ActionAuthorizationModel actionAuthorizationModel){

		BulkReversalRefDataModel bulkReversalRefDataModel = new BulkReversalRefDataModel();

//		bulkReversalRefDataModel.setBulkAdjustmentId(bulkAutoReversalModel.getBulkAdjustmentId());
		bulkReversalRefDataModel.setTrxnId(bulkAutoReversalModel.getTrxnId());
//		bulkReversalRefDataModel.setAdjustmentType(bulkAutoReversalModel.getAdjustmentType());
//		bulkReversalRefDataModel.setFromAccount(bulkAutoReversalModel.getFromAccount());
//		bulkReversalRefDataModel.setFromAccountTitle(bulkAutoReversalModel.getFromAccountTitle());
//		bulkReversalRefDataModel.setToAccount(bulkAutoReversalModel.getToAccount());
//		bulkReversalRefDataModel.setToAccountTitle(bulkAutoReversalModel.getToAccountTitle());
//		bulkReversalRefDataModel.setAmount(bulkAutoReversalModel.getAmount());
//		bulkReversalRefDataModel.setComments(bulkAutoReversalModel.getDescription());
		bulkReversalRefDataModel.setCreatedBy(actionAuthorizationModel.getCreatedById());
		bulkReversalRefDataModel.setCreatedOn(actionAuthorizationModel.getCreatedOn());
		bulkReversalRefDataModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
		bulkReversalRefDataModel.setBatchID(bulkAutoReversalModel.getBatchId());

		return bulkReversalRefDataModel;
	}

	/*private void pushBulkManualAdjustmentBatchToQueue(BulkManualAdjustmentRefDataModel refDataModel) throws Exception{
		Long batchId = refDataModel.getBatchID();
		BulkManualAdjustmentModel bulkManualAdjustmentModel = new BulkManualAdjustmentModel();
		bulkManualAdjustmentModel.setBatchId(batchId);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(bulkManualAdjustmentModel);

		List<BulkManualAdjustmentModel> resultList = manualReversalManager.loadBulkManualAdjustmentModelList(searchBaseWrapper);
		if (CollectionUtils.isNotEmpty(resultList)){
			for (BulkManualAdjustmentModel model : resultList) {

				ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
				manualAdjustmentModel.setTransactionCodeId(model.getTrxnId());
				manualAdjustmentModel.setAdjustmentType(model.getAdjustmentType());
				manualAdjustmentModel.setFromACNo(model.getFromAccount());
//				manualAdjustmentModel.setFromACNick(model.getFromAccount);//TODO
				manualAdjustmentModel.setToACNo(model.getToAccount());
//				manualAdjustmentModel.setToACNick(model.getToACNick());
				manualAdjustmentModel.setAmount(model.getAmount());//TODO
				manualAdjustmentModel.setComments(model.getDescription());
				manualAdjustmentModel.setCreatedBy(model.getCreatedBy());
				manualAdjustmentModel.setCreatedOn(model.getCreatedOn());
				manualAdjustmentModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());

				manualReversalManager.pushBulkManualAdjustmentToQueue(model);

			}
		}
	}*/

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

//	public void setManualAdjustmentManager(manualReversalManager manualReversalManager) {
//		this.manualReversalManager = manualReversalManager;
//	}

	public void setManualReversalManager(ManualReversalManager manualReversalManager) {
		this.manualReversalManager = manualReversalManager;
	}

	public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
		this.portalOlaFacade = portalOlaFacade;
	}

	public void setManualReversalFacade(ManualReversalManager manualReversalFacade) {
		this.manualReversalFacade = manualReversalFacade;
	}

	public void setBulkReversalDAO(BulkReversalDAO bulkReversalDAO) {
		this.bulkReversalDAO = bulkReversalDAO;
	}
}
