package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;
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

import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.util.*;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ManualAdjustmentRefDataModel;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class ManualAdjustmentAuthorizationController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( ManualAdjustmentAuthorizationController.class );
	private ManualAdjustmentManager manualAdjustmentManager;
    private ReferenceDataManager referenceDataManager;
		
	public ManualAdjustmentAuthorizationController() {
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
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()) 
						//|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue()))
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
			ManualAdjustmentRefDataModel manualAdjustmentRefDataModel = (ManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("transactionCodeId",manualAdjustmentRefDataModel.getTransactionCodeId());
			request.setAttribute("adjustmentType",manualAdjustmentRefDataModel.getAdjustmentType());
			request.setAttribute("fromACNo",manualAdjustmentRefDataModel.getFromACNo());
			BBAccountsViewModel model = new BBAccountsViewModel();
			model.setAccountNumber(EncryptionUtil.encryptWithDES(manualAdjustmentRefDataModel.getToACNo()));
			model = manualAdjustmentManager.getBBAccountsViewModel(model);
			String toAcNick = null;
			if(model != null)
				toAcNick = model.getTitle();
			else
				toAcNick = manualAdjustmentRefDataModel.getToACNick();
			model = new BBAccountsViewModel();
			model.setAccountNumber(EncryptionUtil.encryptWithDES(manualAdjustmentRefDataModel.getFromACNo()));
			model = manualAdjustmentManager.getBBAccountsViewModel(model);
			String fromAcNick = null;
			if(model != null)
				fromAcNick = model.getTitle();
			else
				fromAcNick = manualAdjustmentRefDataModel.getFromACNick();
			request.setAttribute("fromACNick",fromAcNick);
			request.setAttribute("toACNo",manualAdjustmentRefDataModel.getToACNo());
			request.setAttribute("toACNick",toAcNick);
			request.setAttribute("amount",manualAdjustmentRefDataModel.getAmount());
			request.setAttribute("comments",manualAdjustmentRefDataModel.getComments());

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
			
			if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				
				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
				if(nextAuthorizationLevel<1){
					
					XStream xstream = new XStream();
					ManualAdjustmentRefDataModel manualAdjustmentRefDataModel = (ManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
					ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
					manualAdjustmentModel.setTransactionCodeId(manualAdjustmentRefDataModel.getTransactionCodeId());
					manualAdjustmentModel.setAdjustmentType(manualAdjustmentRefDataModel.getAdjustmentType());
					manualAdjustmentModel.setFromACNo(manualAdjustmentRefDataModel.getFromACNo());
					manualAdjustmentModel.setFromACNick(manualAdjustmentRefDataModel.getFromACNick());
					manualAdjustmentModel.setToACNo(manualAdjustmentRefDataModel.getToACNo());
					manualAdjustmentModel.setToACNick(manualAdjustmentRefDataModel.getToACNick());
					manualAdjustmentModel.setAmount(manualAdjustmentRefDataModel.getAmount());
					manualAdjustmentModel.setComments(manualAdjustmentRefDataModel.getComments());
					manualAdjustmentModel.setCreatedBy(actionAuthorizationModel.getCreatedById());
					manualAdjustmentModel.setCreatedOn(actionAuthorizationModel.getCreatedOn());
					manualAdjustmentModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());

					if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) {
						this.manualAdjustmentManager.makeOlaToOlaTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
					}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)){
						this.manualAdjustmentManager.makeBBToCoreTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
					}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.CORE_TO_BB)){
						this.manualAdjustmentManager.makeCoreToBBTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
//					}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.ORACLE_FINANCIAL_SETTLEMENT)){
//						this.manualAdjustmentManager.makeOracleFinancialSettlement(manualAdjustmentModel, model.getActionAuthorizationId());
					}

					String msg = this.getText("manualadjustment.add.success", req.getLocale());
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
		}
		catch(WorkFlowException wfe){
			wfe.printStackTrace();
			this.showForm(req, resp, errors);
			req.setAttribute("message", super.getText("manualadjustment.add.failure.lowbalance", req.getLocale()));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			String msg = null;


			if(null!=fce.getMessage() && fce.getMessage().contains("Same BB Accounts Type"))
				msg = "Manual Correction/Adjustment for Customer to Customer BLB A/c or Agent to Agent BLB A/c is not Allowed";
			else if(fce.getMessage().contains("You are not authorized to update action status."))
				msg = "You are not authorized to update action status.";
			else if(fce.getMessage().contains("Invalid status marked"))
				msg = "Invalid status marked";
//			else if(fce.getMessage().equals("8062"))
//			{
//				msg = "Per Day limit of Sender exceeded.";
//
//			}
//			else if(fce.getMessage().equals("8064"))
//			{
//				msg = "Per Month limit of Sender exceeded.";
//
//			}
//			else if(fce.getMessage().equals("8061"))
//			{
//				msg = "Per Day limit of Recipient exceeded.";
//
//			}
//			else if(fce.getMessage().equals("8063"))
//			{
//				msg = "Per Month limit of Recipient exceeded.";
//
//			}
			else
				msg = super.getText("manualadjustment.add.failure", req.getLocale());
			
			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);

			return super.showForm(req, resp, errors);
		
		}catch(Exception ex){
			String msg = super.getText("manualadjustment.add.failure", req.getLocale());
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
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());			
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			XStream xstream = new XStream();
			ManualAdjustmentRefDataModel manualAdjustmentRefDataModel = (ManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
			ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
			manualAdjustmentModel.setTransactionCodeId(manualAdjustmentRefDataModel.getTransactionCodeId());
			manualAdjustmentModel.setAdjustmentType(manualAdjustmentRefDataModel.getAdjustmentType());
			manualAdjustmentModel.setFromACNo(manualAdjustmentRefDataModel.getFromACNo());
			manualAdjustmentModel.setFromACNick(manualAdjustmentRefDataModel.getFromACNick());
			manualAdjustmentModel.setToACNo(manualAdjustmentRefDataModel.getToACNo());
			manualAdjustmentModel.setToACNick(manualAdjustmentRefDataModel.getToACNick());
			manualAdjustmentModel.setAmount(manualAdjustmentRefDataModel.getAmount());
			manualAdjustmentModel.setComments(manualAdjustmentRefDataModel.getComments());
			manualAdjustmentModel.setCreatedBy(actionAuthorizationModel.getCreatedById());
			manualAdjustmentModel.setCreatedOn(actionAuthorizationModel.getCreatedOn());
			manualAdjustmentModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
			
			if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) {
				this.manualAdjustmentManager.makeOlaToOlaTransfer(manualAdjustmentModel, actionAuthorizationModel.getActionAuthorizationId());
			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)){
				this.manualAdjustmentManager.makeBBToCoreTransfer(manualAdjustmentModel, actionAuthorizationModel.getActionAuthorizationId());
			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.CORE_TO_BB)){
				this.manualAdjustmentManager.makeCoreToBBTransfer(manualAdjustmentModel, actionAuthorizationModel.getActionAuthorizationId());
//			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.ORACLE_FINANCIAL_SETTLEMENT)){
//				this.manualAdjustmentManager.makeOracleFinancialSettlement(manualAdjustmentModel, actionAuthorizationModel.getActionAuthorizationId());
			}

			String msg = this.getText("manualadjustment.add.success", req.getLocale());
			this.saveMessage(req, msg);
			
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

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

}
