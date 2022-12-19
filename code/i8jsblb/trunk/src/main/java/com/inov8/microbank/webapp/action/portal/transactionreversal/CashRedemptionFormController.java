package com.inov8.microbank.webapp.action.portal.transactionreversal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.ActionLogModel;
import org.apache.commons.validator.GenericValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.P2PDetailModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.TransactionReversalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.facade.OrphanA2PReversalFacade;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;


public class CashRedemptionFormController extends AdvanceAuthorizationFormController
{
	private TransactionReversalFacade transactionReversalFacade;
	private OrphanA2PReversalFacade orphanA2PReversalFacade;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

    public CashRedemptionFormController()
    {
        setCommandName( TransactionReversalConstants.COMMAND_NAME );
        setCommandClass( TransactionReversalVo.class );
    }

    @Override
    protected TransactionReversalVo loadFormBackingObject( HttpServletRequest request ) throws Exception
    {
    	boolean isReSubmit = ServletRequestUtils.getBooleanParameter(request, "isReSubmit",false);
		
		
		/// Added for Resubmit Authorization Request 
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
				throw new FrameworkCheckedException("illegal operation performed");
			}
			
			XStream xstream = new XStream();
			TransactionReversalVo txReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());		
			return txReversalVo;
		}
        TransactionReversalVo txReversalVo = new TransactionReversalVo();
        txReversalVo.setTransactionCode( ServletRequestUtils.getStringParameter( request,  TransactionReversalConstants.TRANSACTION_CODE ) );
        txReversalVo.setTransactionId( ServletRequestUtils.getLongParameter( request, "transactionId" ) );
        txReversalVo.setProductId(ServletRequestUtils.getLongParameter(request, "productId"));
        txReversalVo.setIsFullReversal(ServletRequestUtils.getBooleanParameter(request, "isFullReversal"));
        txReversalVo.setTransactionCodeId( ServletRequestUtils.getLongParameter( request, TransactionReversalConstants.TRANSACTION_CODE_ID ) );
        txReversalVo.setBtnName( ServletRequestUtils.getStringParameter( request, "btnName" ) );
        return txReversalVo;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        TransactionReversalVo txReversalVo = (TransactionReversalVo) command;
        boolean isReversed = false;
        try{
        	logger.info("Going to call updateTransactionReversed for TransactionCode:"+txReversalVo.getTransactionCode() + ". Product ID: " + txReversalVo.getProductId());
        	if ((txReversalVo.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH) || (txReversalVo.getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI)) {
        		WorkFlowWrapper wrapper = orphanA2PReversalFacade.makeAccountToCashReversal(txReversalVo);
        		//send sms here using commoncommandmanager.sendsms()
        		getCommonCommandManager().sendSMS(wrapper);
			}else {
				
	        	WorkFlowWrapper wrapper = transactionReversalFacade.updateTransactionRedeemed(txReversalVo);
	        	getCommonCommandManager().sendSMS(wrapper);
			}
            
        	isReversed = true;
        	
        	logger.info("After updateTransactionRedeemd for trx ID:"+txReversalVo.getTransactionCode() );
			
        }catch(Exception ex){
        	logger.error("Exception at updateTransactionRedeemd...", ex);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put( TransactionReversalConstants.IS_REDEEMED, isReversed );
        modelMap.put( TransactionReversalConstants.COMMAND_NAME, txReversalVo );
        modelMap.put("isMakerCheckerEnabled", false);
        return new ModelAndView( getSuccessView(), modelMap );
    }
    
    @Override
	protected ModelAndView onAuthorization(HttpServletRequest req,
			HttpServletResponse res, Object command, BindException errors)
			throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TransactionReversalVo txReversalVo = (TransactionReversalVo) command;
        
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(req,
				"resubmitRequest", false);
		Long usecaseId = ServletRequestUtils.getLongParameter(req, "usecaseId");

		Long actionAuthorizationId = null;
		if (resubmitRequest)
			actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"actionAuthorizationId");
		try {
			
			XStream xstream = new XStream();
			String refDataModelString = xstream.toXML(txReversalVo);
			String oldRefDataModelString = "";
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade
					.getNextAuthorizationLevel(usecaseId, new Long(0));
			
			
			if (nextAuthorizationLevel.intValue() < 1) {

				baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(TransactionReversalVo.class.getSimpleName(),
						txReversalVo);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,
						PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(
						PortalConstants.TRANS_REDEMPTION_USECASE_ID));

				actionAuthorizationId = performActionWithAllIntimationLevels(
						nextAuthorizationLevel, "", refDataModelString,oldRefDataModelString,
						usecaseModel, actionAuthorizationId, req);
//				this.saveMessage(
//						req,
//						"Action is authorized successfully. Changes are saved against refernce Action ID : "
//								+ actionAuthorizationId);
			} else {

				actionAuthorizationId = createAuthorizationRequest(
						txReversalVo.getTransactionCode(), nextAuthorizationLevel, "",
						refDataModelString,oldRefDataModelString,usecaseModel.getUsecaseId(),
						txReversalVo.getTransactionCode(), resubmitRequest,
						actionAuthorizationId, req);
//				this.saveMessage(req,
//						"Action is pending for approval against reference Action ID : "
//								+ actionAuthorizationId);
			}
		} catch (FrameworkCheckedException fce) {
			logger.error(fce.getMessage(),fce);
			String msg = fce.getMessage();

			if (msg.contains("Action authorization request already exist")){
				super.saveMessage(req, msg);
				return super.showForm(req, res, errors);
			}
			
			super.saveMessage(req,super.getText("createredemption.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		} catch (WorkFlowException wfe){
			String message = super.getText("createredemption.failure", req.getLocale());
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

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("authorizId", actionAuthorizationId);
        modelMap.put( TransactionReversalConstants.IS_REDEEMED, true );
        modelMap.put( TransactionReversalConstants.COMMAND_NAME, txReversalVo );
        modelMap.put("isMakerCheckerEnabled", true);

		ModelAndView modelAndView = new ModelAndView(getSuccessView(), modelMap);
		return modelAndView;
	}

	protected Long createAuthorizationRequest(String transactionCode, Long nextAuthorizationLevel, String comments, String refDataModelString,
			String oldRefDataModelString,Long usecaseId, String referenceId, boolean resubmitRequest, Long actionAuthorizationId, HttpServletRequest request) throws FrameworkCheckedException {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
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

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setTrxData(transactionCode + "|||||||||||||");
		actionLogModel.setCustomField11(referenceId);
		actionLogModel.setUsecaseId(usecaseId);
		existingRequest = actionAuthorizationFacade.checkExistingRequest(
				actionAuthorizationModel).getResultsetList();

		if (!existingRequest.isEmpty() && !resubmitRequest)
			throw new FrameworkCheckedException(
					"Action authorization request already exist with  Action ID : "
							+ existingRequest.get(0).getActionAuthorizationId()
									.toString());
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);

		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);

		Boolean isAuthHistory = Boolean.FALSE;

		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper
				.getBasePersistableModel();

		for (long i = 1; i < nextAuthorizationLevel; i++) {
			isAuthHistory = Boolean.TRUE;
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel
					.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel
					.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			actionAuthorizationHistoryModel.setEscalationLevel(i);

			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel = usecaseFacade
					.findUsecaseLevel(usecaseId, i);
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

		if(!isAuthHistory)
		{
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			actionAuthorizationHistoryModel.setEscalationLevel(1L);
			actionAuthorizationHistoryModel.setCheckerComments(comments);
			actionAuthorizationHistoryModel.setStartTime(new Timestamp(new Date().getTime()));
			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,1L);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}

			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());

			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
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

		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionLogModel.setOutputXml("Action is pending for approval against reference Action ID : "+actionAuthorizationModel.getActionAuthorizationId().toString());
		actionLogModel.setActionAuthorizationId(actionAuthorizationModel.getActionAuthorizationId());
		modelWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);
		return actionAuthorizationModel.getActionAuthorizationId();
	}

	public void setCreditAccountQueingPreProcessor(
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

	public void setOrphanA2PReversalFacade(OrphanA2PReversalFacade orphanA2PReversalFacade) {
		this.orphanA2PReversalFacade = orphanA2PReversalFacade;
	}

	private CommonCommandManager getCommonCommandManager() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
	}

    public void setTransactionReversalFacade( TransactionReversalFacade transactionReversalFacade )
    {
        this.transactionReversalFacade = transactionReversalFacade;
    }

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView(new RedirectView("home.html"));
		return modelAndView;
	}


}
