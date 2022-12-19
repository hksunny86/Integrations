package com.inov8.microbank.webapp.action.portal.transactiondetail;
/*
 * Author : Abu Turab Munir
 * Date   : 15-06-2015
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
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserMobileHistoryModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class UpdateCnicDetailAuthorizationDetailController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( UpdateCnicDetailAuthorizationDetailController.class );
    private ReferenceDataManager referenceDataManager;
    private AppUserManager		 appUserManager;
    private CustTransManager custTransManager;
    private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
		
	public UpdateCnicDetailAuthorizationDetailController() {
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
		
		if (escalateRequest || resolveRequest) {
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			XStream xstream = new XStream();
			AppUserMobileHistoryModel appUserMobileHistoryModel = (AppUserMobileHistoryModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			AppUserMobileHistoryModel oldAppUserMobileHistoryModel = (AppUserMobileHistoryModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());

			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserMobileHistoryModel.getAppUserId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = appUserManager.loadAppUser(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
			
			if(appUserModel.getAppUserTypeId().longValue() == 2L){//Customer
				//load user id from customer
				CustomerModel customerModel = new CustomerModel();
				customerModel.setCustomerId(appUserModel.getCustomerId());
				baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(customerModel);
				baseWrapper = custTransManager.loadCustomer(baseWrapper);
				customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
				
				request.setAttribute("name", customerModel.getName());
				
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				UserDeviceAccountListViewModel usd = new UserDeviceAccountListViewModel();
				usd.setAppUserId(appUserModel.getAppUserId());
				searchBaseWrapper.setBasePersistableModel(usd);
				searchBaseWrapper = userDeviceAccountListViewManager.searchUserDeviceAccount(searchBaseWrapper);
				usd = (UserDeviceAccountListViewModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
				request.setAttribute("userId", usd.getUserId());
				request.setAttribute("customerId", usd.getUserId());
			}else if(appUserModel.getAppUserTypeId().longValue() == 3L){//Agent
				//load user id from agent
				RetailerContactModel retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();
				
				request.setAttribute("name", retailerContactModel.getName());
				
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				UserDeviceAccountListViewModel usd = new UserDeviceAccountListViewModel();
				usd.setAppUserId(appUserModel.getAppUserId());
				searchBaseWrapper.setBasePersistableModel(usd);
				searchBaseWrapper = userDeviceAccountListViewManager.searchUserDeviceAccount(searchBaseWrapper);
				usd = (UserDeviceAccountListViewModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
				request.setAttribute("userId", usd.getUserId());
				request.setAttribute("agentId", usd.getUserId());
				
			}else if(appUserModel.getAppUserTypeId().longValue() == 12L){ //handler
				request.setAttribute("name", appUserModel.getFirstName() + " " + appUserModel.getLastName());
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				UserDeviceAccountListViewModel usd = new UserDeviceAccountListViewModel();
				usd.setAppUserId(appUserModel.getAppUserId());
				searchBaseWrapper.setBasePersistableModel(usd);
				searchBaseWrapper = userDeviceAccountListViewManager.searchUserDeviceAccount(searchBaseWrapper);
				usd = (UserDeviceAccountListViewModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
				request.setAttribute("userId", usd.getUserId());
				request.setAttribute("handlerId", usd.getUserId());
			}
			
			
			request.setAttribute("appUserId", EncryptionUtil.encryptWithDES(appUserModel.getAppUserId().toString()));
			request.setAttribute("mobileNo", appUserModel.getMobileNo());
			request.setAttribute("appUserType", appUserModel.getAppUserTypeIdAppUserTypeModel().getName());
			request.setAttribute("comments",appUserMobileHistoryModel.getDescription());
			request.setAttribute("previouscnic", oldAppUserMobileHistoryModel.getNic());
			request.setAttribute("newcnic", appUserMobileHistoryModel.getNic());
			
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
				
				Long usecaseId = null;
				if(model.getUsecaseName().indexOf("Customer") > 1){
					usecaseId = PortalConstants.UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID;
				}else if(model.getUsecaseName().indexOf("Agent") > 1){
					usecaseId = PortalConstants.UPDATE_AGENT_ID_DOC_NO_USECASE_ID;
				}else{
					usecaseId = PortalConstants.UPDATE_HANDLER_ID_DOC_NO_USECASE_ID;
				}
				
				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,actionAuthorizationModel.getEscalationLevel());
				if(nextAuthorizationLevel<1){
					
					XStream xstream = new XStream();
					AppUserMobileHistoryModel appUserMobileHistoryModel = (AppUserMobileHistoryModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

					baseWrapper = new BaseWrapperImpl();
					baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, appUserMobileHistoryModel.getUsecaseId());
					baseWrapper.putObject("cnic", appUserMobileHistoryModel.getMobileNo());
					baseWrapper.putObject("appUserId",appUserMobileHistoryModel.getAppUserId());
					baseWrapper.setBasePersistableModel(appUserMobileHistoryModel);
					baseWrapper = this.appUserManager.updateCNIC(baseWrapper);
						
					String msg = this.getText("cnichistory.update.success", req.getLocale());
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
		}catch(WorkFlowException wfe){
			wfe.printStackTrace();
			this.showForm(req, resp, errors);
			req.setAttribute("message", super.getText("cnichistory.update.failure", req.getLocale()));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			String msg = null;
			
			if(null!=fce.getMessage()){
				msg = fce.getMessage();
			}else {
				msg = super.getText("cnichistory.update.failure", req.getLocale());
			}
			
			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch (Exception ex)
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
	
	@Override
	protected ModelAndView onResolve(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {
		
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());			
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			XStream xstream = new XStream();
			AppUserMobileHistoryModel appUserMobileHistoryModel = (AppUserMobileHistoryModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, appUserMobileHistoryModel.getUsecaseId());
			baseWrapper.putObject("cnic", appUserMobileHistoryModel.getNic());
			baseWrapper.putObject("appUserId",appUserMobileHistoryModel.getAppUserId());
			baseWrapper.setBasePersistableModel(appUserMobileHistoryModel);
			baseWrapper = this.appUserManager.updateMobileNo(baseWrapper);
				
			String msg = this.getText("cnichistory.update.success", req.getLocale());
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

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setCustTransManager(CustTransManager custTransManager) {
		this.custTransManager = custTransManager;
	}

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}


}
