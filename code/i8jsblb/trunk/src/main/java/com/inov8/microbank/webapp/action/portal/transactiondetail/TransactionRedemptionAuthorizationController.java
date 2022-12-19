package com.inov8.microbank.webapp.action.portal.transactiondetail;
/*
 * Author : Hassan Javaid
 * Date   : 23-09-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.P2PDetailModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.facade.OrphanA2PReversalFacade;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class TransactionRedemptionAuthorizationController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( TransactionRedemptionAuthorizationController.class );
    private ReferenceDataManager referenceDataManager;
    private TransactionModuleManager transactionModuleManager;
    private TransactionDetailMasterManager transactionDetailMasterManager;
	private TransactionReversalFacade transactionReversalFacade;
	private OrphanA2PReversalFacade orphanA2PReversalFacade;
		
	public TransactionRedemptionAuthorizationController() {
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
		
		if (escalateRequest || resolveRequest) {
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			XStream xstream = new XStream();
			TransactionReversalVo transactionReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("transactionCode",transactionReversalVo.getTransactionCode() );
			request.setAttribute("comments",transactionReversalVo.getComments());
			request.setAttribute("isFullReversal",transactionReversalVo.getRedemptionType() == 1 ? true : false);
			
			/*P2PDetailModel oldP2pDetailModel = (P2PDetailModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());

			request.setAttribute("oldSenderCNIC",oldP2pDetailModel.getSenderCNIC());
			request.setAttribute("oldSenderMobile",oldP2pDetailModel.getSenderMobile());
			request.setAttribute("oldRecipientCNIC",oldP2pDetailModel.getRecipientCNIC());
			request.setAttribute("oldRecipientMobile",oldP2pDetailModel.getRecipientMobile());*/
			
			
			return actionAuthorizationModel;
		}
		else 
			return new ActionAuthorizationModel();
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception {
		return null;		
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception {
		return null;
	}
	

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		return null;
	}
	
	@Override
	protected ModelAndView onEscalate(HttpServletRequest req,HttpServletResponse resp, Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
			boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
			long currentUserId= UserUtils.getCurrentUser().getAppUserId();
				
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				
				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.TRANS_REDEMPTION_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
				if(nextAuthorizationLevel<1){
					
					XStream xstream = new XStream();
					TransactionReversalVo transactionReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

					baseWrapper = new BaseWrapperImpl();
					baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
					baseWrapper.putObject(TransactionReversalVo.class.getSimpleName(), transactionReversalVo);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.TRANS_REDEMPTION_USECASE_ID));

					redeemTransaction(transactionReversalVo);
					
					String msg = this.getText("createredemption.success", req.getLocale());
					this.saveMessage(req, msg);
					
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
				
				XStream xstream = new XStream();
				TransactionReversalVo transactionReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

//				restoreTransactionProcessingStatus(p2pDetailModel);
				
				actionDeniedOrCancelled(actionAuthorizationModel, model,req);
			
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue() 
					&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
					|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){
				
				if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				
				XStream xstream = new XStream();
				TransactionReversalVo transactionReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

//				restoreTransactionProcessingStatus(p2pDetailModel);
				
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
			req.setAttribute("message", super.getText("manualadjustment.add.failure.lowbalance", req.getLocale()));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			String msg = null;
			
			if(null!=fce.getMessage()){
				msg = fce.getMessage();
			}else {
				msg = super.getText("updatep2p.add.failure", req.getLocale());
			}
			
			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(Exception fce){
			fce.printStackTrace();
			req.setAttribute("message", MessageUtil.getMessage("6075"));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}
		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(req, resp, errors);
	    return modelAndView; 
	}
	
	
	private void redeemTransaction(TransactionReversalVo txReversalVo) {
		boolean isReversed = false;
        try{
        	logger.info("Going to call updateTransactionReversed for TransactionCode:"+txReversalVo.getTransactionCode() + ". Product ID: " + txReversalVo.getProductId());
        	if ((txReversalVo.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH)   || (txReversalVo.getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI)) {
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
	}
	
	@Override
	protected ModelAndView onResolve(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {
		
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());			
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			XStream xstream = new XStream();
			TransactionReversalVo transactionReversalVo = (TransactionReversalVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

			baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			baseWrapper.putObject(TransactionReversalVo.class.getSimpleName(), transactionReversalVo);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.TRANS_REDEMPTION_USECASE_ID));
			
			redeemTransaction(transactionReversalVo);
			
			String msg = this.getText("createredemption.success", req.getLocale());
			this.saveMessage(req, msg);
			
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel, req);
			
		}
		catch (FrameworkCheckedException ex)
		{			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			req.setAttribute("message", ex.getMessage());
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(req, resp, errors);
		}
		catch (Exception ex)
		{			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			req.setAttribute("message", MessageUtil.getMessage("6075"));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(req, resp, errors);
		}
		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(req, resp, errors);	
		return modelAndView;	
	}
	
/*	private void restoreTransactionProcessingStatus(P2PDetailModel p2pDetailModel) throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model.setTransactionCode(p2pDetailModel.getTransactionCode());
		baseWrapper.setBasePersistableModel(model);
		baseWrapper = transactionDetailMasterManager
				.loadTransactionDetailMasterModel(baseWrapper);
		model = (TransactionDetailMasterModel) baseWrapper
				.getBasePersistableModel();
//		model.setSupProcessingStatusId(p2pDetailModel.getSupProcessingStatusId());
//		model.setProcessingStatusName(p2pDetailModel.getProcessingStatusName());
		
		//make P2P transaction ready for leg II
		model.setUpdateP2PFlag(false);
		transactionDetailMasterManager.saveTransactionDetailMaster(model);

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionId(model.getTransactionId());
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		searchBaseWrapper = transactionModuleManager
				.loadTransaction(searchBaseWrapper);
		transactionModel = (TransactionModel) searchBaseWrapper
				.getBasePersistableModel();
		transactionModel
				.setSupProcessingStatusId(p2pDetailModel.getSupProcessingStatusId());
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		transactionModuleManager.updateTransaction(searchBaseWrapper);
		
	}*/

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionModuleManager(
			TransactionModuleManager transactionModuleManager) {
		if (transactionModuleManager != null) {
			this.transactionModuleManager = transactionModuleManager;
		}
	}

	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		if (transactionDetailMasterManager != null) {
			this.transactionDetailMasterManager = transactionDetailMasterManager;
		}
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

}
