package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class P2PDetailFormController extends AdvanceAuthorizationFormController {

	private TransactionDetailMasterManager	transactionDetailMasterManager;

	private TransactionModuleManager		transactionModuleManager;
	private ReferenceDataManager			referenceDataManager;
	private FinancialIntegrationManager		financialIntegrationManager;
	private MfsAccountManager mfsAccountManager;
	private AppUserManager appUserManager;

	private P2PDetailModel p2pModel;

	public P2PDetailFormController() {
		setCommandName("p2pDetailModel");
		setCommandClass(P2PDetailModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req)
			throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		String transactionCode = ServletRequestUtils.getStringParameter(req,
				"transactionCode");
		CustomList<P2PDetailModel> list = null;
		P2PDetailModel model = new P2PDetailModel();
		if (!GenericValidator.isBlankOrNull(transactionCode)) {
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			model.setTransactionCode(transactionCode);
			wrapper.setBasePersistableModel(model);
			wrapper = this.transactionModuleManager
					.loadP2PUpdateHistory(wrapper);
			list = wrapper.getCustomList();
			referenceDataMap.put("p2pDetailModelList", list.getResultsetList());
			return referenceDataMap;
		}
		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req)
			throws Exception {
		String transactionCode = ServletRequestUtils.getStringParameter(req,"transactionCode");
		String agent1Name = ServletRequestUtils.getStringParameter(req,"agent1Name");
		
		
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		
		
		/// Added for Resubmit Authorization Request 
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
				throw new FrameworkCheckedException("illegal operation performed");
			}
			
			XStream xstream = new XStream();
			P2PDetailModel p2pDetailModel = (P2PDetailModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());		
			return p2pDetailModel;
		}
		///End Added for Resubmit Authorization Request
		
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model.setTransactionCode(transactionCode);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(model);
		baseWrapper = transactionDetailMasterManager
				.loadTransactionDetailMasterModel(baseWrapper);
		model = (TransactionDetailMasterModel) baseWrapper
				.getBasePersistableModel();

		P2PDetailModel p2pDetailModel = getP2PModel(model);
		/*p2pDetailModel.setTransactionCode(model.getTransactionCode());
		p2pDetailModel.setTransactionAmount(model.getTransactionAmount());
		p2pDetailModel.setCreatedOn(model.getCreatedOn());
		p2pDetailModel.setSenderMobile(model.getSaleMobileNo());
		p2pDetailModel.setSenderCNIC(model.getSenderCnic());
		p2pDetailModel.setRecipientMobile(model.getRecipientMobileNo());
		p2pDetailModel.setRecipientCNIC(model.getRecipientCnic());
		p2pDetailModel.setSupProcessingStatusId(model
				.getSupProcessingStatusId());
		p2pDetailModel.setProcessingStatusName(model.getProcessingStatusName());
		
		p2pDetailModel.setAgent1Id(model.getAgent1Id());
		p2pDetailModel.setAgent1MobileNo(model.getAgent1MobileNo());*/
		p2pDetailModel.setAgent1Name(agent1Name);

		return p2pDetailModel;
	}

	private P2PDetailModel getP2PModel(TransactionDetailMasterModel model)
	{
		P2PDetailModel p2pDetailModel = new P2PDetailModel();
		p2pDetailModel.setTransactionCode(model.getTransactionCode());
		p2pDetailModel.setTransactionAmount(model.getTransactionAmount());
		p2pDetailModel.setCreatedOn(model.getCreatedOn());
		p2pDetailModel.setSenderMobile(model.getSaleMobileNo());
		p2pDetailModel.setSenderCNIC(model.getSenderCnic());
		p2pDetailModel.setRecipientMobile(model.getRecipientMobileNo());
		p2pDetailModel.setRecipientCNIC(model.getRecipientCnic());
		p2pDetailModel.setSupProcessingStatusId(model
				.getSupProcessingStatusId());
		p2pDetailModel.setProcessingStatusName(model.getProcessingStatusName());

		p2pDetailModel.setAgent1Id(model.getAgent1Id());
		p2pDetailModel.setAgent1MobileNo(model.getAgent1MobileNo());

		return p2pDetailModel;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req,
			HttpServletResponse res, Object model, BindException errors)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView(new RedirectView(
				"home.html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req,
			HttpServletResponse res, Object obj, BindException errors)
			throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		P2PDetailModel model = new P2PDetailModel();
		try {
			model = (P2PDetailModel) obj;
			p2pModel = new P2PDetailModel();
			p2pModel = model;
			String errorMessage = validateInput(model,req);
			if(!StringUtil.isNullOrEmpty(errorMessage)){
				super.saveMessage(req, errorMessage);
				return super.showForm(req, res, errors);
			}
/*			baseWrapper.setBasePersistableModel(model);
			populateAuthenticationParams(baseWrapper, req, model);
			baseWrapper = this.transactionFacade.updateP2PTxDetailsWithAuthorization(baseWrapper);*/
			
			// Creating/Updating Walk In Customer before Limit check 
			this.transactionModuleManager.makeWalkinCustomer(model.getSenderCNIC(), model.getSenderMobile());
			this.transactionModuleManager.makeWalkinCustomer(model.getRecipientCNIC(), model.getRecipientMobile());

			if(model.isSenderCNICChanged()){
				this.limitCheck(model, false, req.getLocale());
			}
			
			if(model.isRecipientCNICChanged()){
				this.limitCheck(model, true, req.getLocale());
			}
	
			baseWrapper.putObject(P2PDetailModel.class.getSimpleName(), model);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID));
			TransactionDetailMasterModel oldTDMModel = loadTransactionDetailMasterModel(model.getTransactionCode());
			String trxData = this.prepareTRXData(oldTDMModel);
			baseWrapper.putObject("trxData",trxData);
			baseWrapper = this.transactionModuleManager.updateP2PTxDetails(baseWrapper);

			String msg = super.getText("updatep2p.add.success", req.getLocale());
			this.saveMessage(req, msg);
		
		} catch (FrameworkCheckedException exception) {
			exception.printStackTrace();
			super.saveMessage(req, super.getText("updatep2p.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		} catch (WorkFlowException wfe){
			String message = super.getText("updatep2p.add.failure", req.getLocale());
			if(StringUtil.isFailureReasonId(wfe.getMessage())){
				message = super.getText(wfe.getMessage() , req.getLocale());
			}
			super.saveMessage(req, message);
			return super.showForm(req, res, errors);
		}
		catch (Exception exception) {
			exception.printStackTrace();
			super.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_updatep2ptxreport.html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest req,
			HttpServletResponse res, Object command, BindException errors)
			throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		P2PDetailModel model = (P2PDetailModel) command;
		p2pModel = new P2PDetailModel();
		p2pModel = model;

		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(req,
				"resubmitRequest", false);
		Long usecaseId = ServletRequestUtils.getLongParameter(req, "usecaseId");

		Long actionAuthorizationId = null;
		if (resubmitRequest)
			actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"actionAuthorizationId");
		try {
			String errorMessage = validateInput(model,req);
			if(!StringUtil.isNullOrEmpty(errorMessage)){
				super.saveMessage(req, errorMessage);
				return super.showForm(req, res, errors);
			}
			
			// Creating/Updating Walk In Customer before Limit check 
			this.transactionModuleManager.makeWalkinCustomer(model.getSenderCNIC(), model.getSenderMobile());
			this.transactionModuleManager.makeWalkinCustomer(model.getRecipientCNIC(), model.getRecipientMobile());

			if(model.isSenderCNICChanged()){
				this.limitCheck(model, false, req.getLocale());
			}
			
			if(model.isRecipientCNICChanged()){
				this.limitCheck(model, true, req.getLocale());
			}

			XStream xstream = new XStream();
			String refDataModelString = xstream.toXML(model);

			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade
					.getNextAuthorizationLevel(usecaseId, new Long(0));
			
			
			TransactionDetailMasterModel oldTDMModel = loadTransactionDetailMasterModel(model.getTransactionCode());
			P2PDetailModel oldP2PDetailModel = CommonUtils.prepareP2PDetailModel(oldTDMModel, model);
			String oldRefDataModelString = xstream.toXML(oldP2PDetailModel);

			if (nextAuthorizationLevel.intValue() < 1) {

				baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(P2PDetailModel.class.getSimpleName(),
						model);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,
						PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(
						PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID));
				baseWrapper = this.transactionModuleManager
						.updateP2PTxDetails(baseWrapper);
				model = (P2PDetailModel) baseWrapper
						.getObject(P2PDetailModel.class.getSimpleName());

				String msg = super.getText("updatep2p.add.success",
						req.getLocale());
				this.saveMessage(req, msg);

				actionAuthorizationId = performActionWithAllIntimationLevels(
						nextAuthorizationLevel, "", refDataModelString,oldRefDataModelString,
						usecaseModel, actionAuthorizationId, req);
				this.saveMessage(
						req,
						"Action is authorized successfully. Changes are saved against refernce Action ID : "
								+ actionAuthorizationId);
			} else {

				actionAuthorizationId = createAuthorizationRequest(
						model.getTransactionCode(), nextAuthorizationLevel, "",
						refDataModelString,oldRefDataModelString,usecaseModel.getUsecaseId(),
						model.getTransactionCode(), resubmitRequest,
						actionAuthorizationId, req);
				this.saveMessage(req,
						"Action is pending for approval against reference Action ID : "
								+ actionAuthorizationId);
			}
		} catch (FrameworkCheckedException fce) {
			logger.error(fce.getMessage(),fce);
			String msg = fce.getMessage();

			if (msg.contains("Action authorization request already exist")){
				super.saveMessage(req, msg);
				return super.showForm(req, res, errors);
			}
			
			super.saveMessage(req,super.getText("updatep2p.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		} catch (WorkFlowException wfe){
			String message = super.getText("updatep2p.add.failure", req.getLocale());
			if(StringUtil.isFailureReasonId(wfe.getMessage())){
				message = super.getText(wfe.getMessage() , req.getLocale());
			}
			super.saveMessage(req, message);
			return super.showForm(req, res, errors);
		}catch (Exception fce) {
			logger.error(fce.getMessage(),fce);
			super.saveMessage(req,MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		} 
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_updatep2ptxreport.html"));
		return modelAndView;
	}

	private String prepareTRXData(TransactionDetailMasterModel model)
	{
		P2PDetailModel oldP2PModel = this.getP2PModel(model);
		String trxData = model.getTransactionCode() + "|";
		if(p2pModel.isSenderCNICChanged())
			trxData += "Update Sender CNIC |" + oldP2PModel.getSenderCNIC() + "|" + p2pModel.getSenderCNIC() + "|";
		else
			trxData += "|||";

		if(p2pModel.isSenderMobileChanged())
			trxData += "Update Sender Mobile |" + oldP2PModel.getSenderMobile() + "|" + p2pModel.getSenderMobile() + "|";
		else
			trxData += "|||";

		if(p2pModel.isRecipientCNICChanged())
			trxData += "Update Recipient CNIC |" + oldP2PModel.getRecipientCNIC() + "|" + p2pModel.getRecipientCNIC() + "|";
		else
			trxData += "|||";

		if(p2pModel.isRecipientMobileChanged())
			trxData += "Update Recipient Mobile |" + oldP2PModel.getRecipientMobile() + "|" + p2pModel.getRecipientMobile() + "|";
		else
			trxData += "|||";
		return trxData;
	}

	private void prepareActinAuthHistoryModel(Long nextAuthorizationLevel,ActionAuthorizationModel actionAuthorizationModel,
																		 Long usecaseId,Long escalationLevel,Long actionStatusId,BaseWrapper baseWrapper,
																		 HttpServletRequest request) throws FrameworkCheckedException
	{
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel
				.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel
				.setActionStatusId(actionStatusId);
		actionAuthorizationHistoryModel.setIntimatedOn(new Date());
		actionAuthorizationHistoryModel.setEscalationLevel(escalationLevel);

		// Setting Checker Names
		UsecaseLevelModel usecaseLevelModel = usecaseFacade
				.findUsecaseLevel(usecaseId, escalationLevel);
		List<LevelCheckerModel> checkerList = usecaseFacade
				.getLevelCheckerModelList(usecaseLevelModel
						.getUsecaseLevelId());
		StringBuilder usernames = new StringBuilder();
		StringBuilder emailRecipients = new StringBuilder();
		for (LevelCheckerModel levelCheckerModel : checkerList) {
			usernames
					.append(levelCheckerModel
							.getRelationCheckerIdAppUserModel()
							.getUsername()
							+ ",");
			if ((null != (levelCheckerModel
					.getRelationCheckerIdAppUserModel().getEmail()))
					&& (!levelCheckerModel
					.getRelationCheckerIdAppUserModel().getEmail()
					.isEmpty())) {
				if (GenericValidator.isEmail(levelCheckerModel
						.getRelationCheckerIdAppUserModel().getEmail()))
					emailRecipients.append(levelCheckerModel
							.getRelationCheckerIdAppUserModel().getEmail()
							+ ";");
			}

		}
		usernames.deleteCharAt(usernames.lastIndexOf(","));
		actionAuthorizationHistoryModel
				.setIntimatedTo(usernames.toString());

		baseWrapper
				.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager
				.saveOrUpdate(baseWrapper);

		// Sending Email Notification to all checkers

		sendNotificationEmail(emailRecipients.toString(),
				actionAuthorizationModel, request);

	}

	protected Long createAuthorizationRequest(String transactionCode,
			Long nextAuthorizationLevel, String comments,
			String refDataModelString,String oldRefDataModelString,Long usecaseId, String referenceId,
			boolean resubmitRequest, Long actionAuthorizationId,
			HttpServletRequest request) throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model.setTransactionCode(transactionCode);
		baseWrapper.setBasePersistableModel(model);
		baseWrapper = transactionDetailMasterManager
				.loadTransactionDetailMasterModel(baseWrapper);
		model = (TransactionDetailMasterModel) baseWrapper
				.getBasePersistableModel();
		String trxData = this.prepareTRXData(model);
//		model.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
//		model.setProcessingStatusName(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH_NAME);
		
		//This flag makes P2P transaction blocked for leg II, until approved by checker
		model.setUpdateP2PFlag(true);
		transactionDetailMasterManager.saveTransactionDetailMaster(model);

/*		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionId(model.getTransactionId());
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		searchBaseWrapper = transactionModuleManager
				.loadTransaction(searchBaseWrapper);
		transactionModel = (TransactionModel) searchBaseWrapper
				.getBasePersistableModel();
		transactionModel
				.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		transactionModuleManager.updateTransaction(searchBaseWrapper);
*/
		baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseId);
		List<ActionAuthorizationModel> existingRequest;

		ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
		if (null != actionAuthorizationId) {
			actionAuthorizationModel
					.setActionAuthorizationId(actionAuthorizationId);
		}
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		actionAuthorizationModel
				.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		actionAuthorizationModel.setUsecaseId(usecaseId);
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setOldReferenceData(oldRefDataModelString);
		actionAuthorizationModel.setReferenceId(referenceId);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		existingRequest = actionAuthorizationFacade.checkExistingRequest(
				actionAuthorizationModel).getResultsetList();

		if (!existingRequest.isEmpty() && !resubmitRequest)
		{
			/*actionLogModel.setOutputXml("Action authorization request already exist with  Action ID : " + existingRequest.get(0).getActionAuthorizationId().toString());
			actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);*/
			throw new FrameworkCheckedException(
					"Action authorization request already exist with  Action ID : "
							+ existingRequest.get(0).getActionAuthorizationId()
							.toString());
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setTrxData(trxData);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);

		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper
				.getBasePersistableModel();

		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationModel);
		modelWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());

		if(nextAuthorizationLevel >1)
		{
			for(long i=1;i<nextAuthorizationLevel;i++)
			{
				this.prepareActinAuthHistoryModel(nextAuthorizationLevel,actionAuthorizationModel,usecaseId,i,ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL,baseWrapper,request);
			}
		}
		else {
			this.prepareActinAuthHistoryModel(nextAuthorizationLevel,actionAuthorizationModel,usecaseId,1L,ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL,baseWrapper,request);
		}
		///Send Email to Current Level Checkers

		if (nextAuthorizationLevel > 0) {

			UsecaseLevelModel usecaseLevelModel = usecaseFacade
					.findUsecaseLevel(usecaseId, nextAuthorizationLevel);
			List<LevelCheckerModel> checkerList = usecaseFacade
					.getLevelCheckerModelList(usecaseLevelModel
							.getUsecaseLevelId());
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				if ((null != (levelCheckerModel
						.getRelationCheckerIdAppUserModel().getEmail()))
						&& (!levelCheckerModel
								.getRelationCheckerIdAppUserModel().getEmail()
								.isEmpty())) {
					if (GenericValidator.isEmail(levelCheckerModel
							.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel
								.getRelationCheckerIdAppUserModel().getEmail()
								+ ";");
				}
			}
			sendNotificationEmail(emailRecipients.toString(),
					actionAuthorizationModel, request);
		}
		///End Send Notification Email to Current Level Checkers

		actionLogModel.setOutputXml("Action is pending for approval against reference Action ID : " + actionAuthorizationModel.getActionAuthorizationId().toString());
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);
		return actionAuthorizationModel.getActionAuthorizationId();
	}

	private void limitCheck(P2PDetailModel model,boolean isCredit,Locale locale) throws WorkFlowException {
		try {
			
			WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			BankModel bankModel = mfsAccountManager.getOlaBankMadal();
			smartMoneyAccountModel.setBankIdBankModel(bankModel);
			wrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setBankId(bankModel.getBankId()); // OLA bank id
			switchWrapper.setWorkFlowWrapper(wrapper);

			OLAVO olavo = new OLAVO();
			olavo.setBalance(model.getTransactionAmount());
			
			if(isCredit){
				olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(model.getRecipientCNIC()));//cnic
				olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);//recepient credit
				logger.info("[P2PDetailFormController.limitCheck()] Going to verify Recipient Walkin Customer Throughput Limits for CNIC:"+ model.getRecipientCNIC());
			}else{
				olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(model.getSenderCNIC()));//cnic
				olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);// sender debit
				logger.info("[P2PDetailFormController.limitCheck()] Going to verify Sender Walkin Customer Throughput Limits for CNIC:"+ model.getSenderCNIC());
			}

			olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
			olavo.setTransactionDateTime(new Date());

			switchWrapper.setOlavo(olavo);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(bankModel);

			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

			switchWrapper = abstractFinancialInstitution.verifyWalkinCustomerThroughputLimits(switchWrapper);

		}catch(NullPointerException npe){
			logger.info("[P2PDetailFormController.limitCheck()] NullPointerException:" + " Details: ",npe);
			throw new WorkFlowException(super.getText("updatep2p.add.failure", locale));
		}catch(WorkFlowException wex){
			logger.info("[P2PDetailFormController.limitCheck()] WorkFlowException:"+wex.getMessage() + " Details: ",wex);
			throw wex;
		} catch (Exception e) {
			logger.info("[P2PDetailFormController.limitCheck()] Exception in verifying Walkin Customer Throughput Limits:" + " Message: " + e.getMessage(),e);
			throw new WorkFlowException(super.getText("updatep2p.add.failure", locale));
		}

	}

	private String validateInput(P2PDetailModel model, HttpServletRequest req){
		String errorMessage = "";
		try{
			if(StringUtil.isNullOrEmpty(model.getSenderMobile())){
				errorMessage = "Sender Mobile No is required";
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
			if(StringUtil.isNullOrEmpty(model.getSenderCNIC())){
				errorMessage = "Sender CNIC is required";
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
			if(StringUtil.isNullOrEmpty(model.getRecipientMobile())){
				errorMessage = "Receiver Mobile No is required";
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
			if(StringUtil.isNullOrEmpty(model.getRecipientCNIC())){
				errorMessage = "Receiver CNIC is required";
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
			if(model.getSenderMobile().equals(model.getRecipientMobile())){
				errorMessage = super.getText("MONEYTRANSFER.SENDER_REC_SAME_MOB", req.getLocale());
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
	
			if(model.getSenderCNIC().equals(model.getRecipientCNIC())){
				errorMessage = super.getText("MONEYTRANSFER.SENDER_REC_SAME_NIC", req.getLocale());
				logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
				return errorMessage;
			}
	
			if(model.isSenderMobileChanged()){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("mobileNo", model.getSenderMobile());
				Boolean isUnique = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUnique){
					errorMessage = super.getText("MONEYTRANSFER.SENDER_MOB_ALREADY_REG", req.getLocale());
					logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
					return errorMessage;
				}

			}
			if(model.isRecipientMobileChanged()){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("mobileNo", model.getRecipientMobile());
				Boolean isUnique = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUnique){
					errorMessage = super.getText("MONEYTRANSFER.REC_MOB_ALREAY_REG", req.getLocale());
					logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
					return errorMessage;
				}

			}
			if(model.isSenderCNICChanged()){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("cnic", model.getSenderCNIC());
				Boolean isUnique = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUnique){
					errorMessage = super.getText("MONEYTRANSFER.SENDER_NIC_ALREADY_REG", req.getLocale());
					logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
					return errorMessage;
				}

			}
			if(model.isRecipientCNICChanged()){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("cnic", model.getRecipientCNIC());
				Boolean isUnique = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUnique){
					errorMessage = super.getText("MONEYTRANSFER.REC_NIC_ALREADY_REG", req.getLocale());
					logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
					return errorMessage;
				}

			}







			/*if(model.isRecipientCNICChanged() || model.isRecipientMobileChanged() || model.isSenderMobileChanged() || model.isSenderCNICChanged()){

				BaseWrapper baseWrapper = new BaseWrapperImpl();
				TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
				tdm.setTransactionCode(model.getTransactionCode());
				baseWrapper.setBasePersistableModel(tdm);
				baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
				tdm = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();

				if(tdm.getProductId().longValue() != AllPayWebConstant.BULK_PAYMENT.getIntValue()){

					Boolean isSenderPairValid = appUserManager.isP2PMobileCNICPairValid(tdm.getSenderCnic(), tdm.getSaleMobileNo(), model.getSenderCNIC(), model.getSenderMobile());

					if(!isSenderPairValid)
					{
						errorMessage = "Invalid Sender's CNIC/Mobile No. provided for update";
						logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
						return errorMessage;
					}

				}

				Boolean isRecipientPairValid = appUserManager.isP2PMobileCNICPairValid(tdm.getRecipientCnic(), tdm.getRecipientMobileNo(), model.getRecipientCNIC(), model.getRecipientMobile());

				if(!isRecipientPairValid)
				{
					errorMessage = "Invalid Recipient's CNIC/Mobile No. provided for update";
					logger.error("Error in P2PDetailFormController.onUpdate() : " + errorMessage);
					return errorMessage;
				}

			}*/
		
		}catch(Exception ex){
			logger.error("Exception occurred while validating UpdateP2P... ",ex);
		}
		
		return errorMessage;
	}
	
	private TransactionDetailMasterModel loadTransactionDetailMasterModel(String transactionCode) throws FrameworkCheckedException{
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model.setTransactionCode(transactionCode);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(model);
		baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
		return model = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
	}
	
	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		if (transactionDetailMasterManager != null) {
			this.transactionDetailMasterManager = transactionDetailMasterManager;
		}
	}

	public void setTransactionModuleManager(
			TransactionModuleManager transactionModuleManager) {
		if (transactionModuleManager != null) {
			this.transactionModuleManager = transactionModuleManager;
		}
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		if (financialIntegrationManager != null) {
			this.financialIntegrationManager = financialIntegrationManager;
		}
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		if (mfsAccountManager != null) {
			this.mfsAccountManager = mfsAccountManager;
		}
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}