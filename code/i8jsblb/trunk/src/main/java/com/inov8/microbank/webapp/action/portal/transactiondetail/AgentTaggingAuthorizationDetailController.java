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
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.AppUserMobileHistoryModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class AgentTaggingAuthorizationDetailController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( UpdateCnicDetailAuthorizationDetailController.class );
    private ReferenceDataManager referenceDataManager;
    private AppUserManager appUserManager;
	private AgentTaggingManager agentTaggingManager;

	public AgentTaggingAuthorizationDetailController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
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
			
			AgentGroupVOModel agentGroupVOModel = (AgentGroupVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

			String childString = agentGroupVOModel.getChildString();
			
			logger.info("\nchildString : "+childString);
			
			if(!StringUtil.isNullOrEmpty(childString)) {
				
				childString = childString.replace("|", ":");
				String[] rows = childString.split(":");

				for(String row : rows) {
					
					String[] cols = row.split(";");

					AgentGroupVOModel _agentGroupVOModel = new AgentGroupVOModel();
					_agentGroupVOModel.setAgentName(cols[0]);					
					_agentGroupVOModel.setBusinessName(cols[1]);
					_agentGroupVOModel.setMobileNumber(cols[2]);
					_agentGroupVOModel.setCnic(cols[3]);
					_agentGroupVOModel.setAppUserId(Long.valueOf(cols[4]));
					_agentGroupVOModel.setParrentId(cols[5]);
						
					agentGroupVOModel.add(_agentGroupVOModel);
				}				
			}
			
			request.setAttribute("agentGroupVOModel", agentGroupVOModel);
			
			return actionAuthorizationModel;
		}
		else 
			return new ActionAuthorizationModel();
	}

	
	public static void main(String sp[]) {
		
		
		String childString = "das day;frwqerqwereqw;06265465468;5657655444654;94648;1002797;|saddam hadler handler;handler12345;03978435435;9889743435135;94628;1002793;|asjkfgaskjfg fsdfasffasfdas;handler13213;03976834134;4684138413513;94626;1002791;|ddd dsdfs;usernamehandler1;03998878945;2333322222222;94898;1002801;|sfsssf sdf;usernamehandler12;03956858555;2342342323423;94914;1002823;|SADDAM SADDAM;1122;03968413513;2346354135346;93338;1002661;|nida nida;ns;03458488878;8726348768236;86446;1002277;|";
		childString = childString.replace("|", ":");
		String[] rows = childString.split(":");

		int i=0;
		
		for(String row : rows) {
			
			String[] cols = row.split(";");

			System.out.print(++i);
			
			for(String col : cols) {
				
				System.out.println(col);
			}
			
			System.out.println("\n\n");
		}
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

				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
				
				if(nextAuthorizationLevel<1){
					
					XStream xstream = new XStream();
					AgentGroupVOModel agentGroupVOModel = (AgentGroupVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
					AgentTaggingModel agentTaggingModel = this.agentTaggingManager.createAgentTaggingModel(agentGroupVOModel);
					
					boolean isTitleUnique = agentTaggingManager.isTitleUnique(agentTaggingModel.getAgentTaggingId(), agentTaggingModel.getGroupTitle());
					if(!isTitleUnique){
						throw new FrameworkCheckedException("GroupTitleAlreadyExists");
					}

					String msg = "";
					
					if(agentGroupVOModel.getGroupId() != null) {
						
						String childString = agentGroupVOModel.getChildString();
						
						if(StringUtil.isNullOrEmpty(childString)) {
							childString = null;
						}
						
						agentTaggingManager.updateAgentTaggingModel(agentTaggingModel, childString);
						msg = "Agent Group Tagging Updated Successfully";
						
					} else {
						
						String childString = agentGroupVOModel.getChildString();
						
						if(StringUtil.isNullOrEmpty(childString)) {
							childString = null;
						}
						
						this.agentTaggingManager.saveAgent(agentTaggingModel, childString);
						msg = "Agent Group Tagging Created Successfully";						
					}					
						
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
				if(fce.getMessage().equals("GroupTitleAlreadyExists")){
					msg = "Group title already exists.";
				}else{
					msg = fce.getMessage();
				}
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
			
			AgentGroupVOModel agentGroupVOModel = (AgentGroupVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			////////////////////////////////////////////////////
			AgentTaggingModel agentTaggingModel = this.agentTaggingManager.createAgentTaggingModel(agentGroupVOModel);
			
			boolean isTitleUnique = agentTaggingManager.isTitleUnique(agentTaggingModel.getAgentTaggingId(), agentTaggingModel.getGroupTitle());
			if(!isTitleUnique){
				throw new FrameworkCheckedException("GroupTitleAlreadyExists");
			}

			String msg = "";
			if(agentGroupVOModel.getGroupId() != null) {
				String childString = agentGroupVOModel.getChildString();
				if(StringUtil.isNullOrEmpty(childString)) {
					childString = null;
				}
				agentTaggingManager.updateAgentTaggingModel(agentTaggingModel, childString);
				msg = "Agent Group Tagging Updated Successfully";
			} else {
				String childString = agentGroupVOModel.getChildString();
				if(StringUtil.isNullOrEmpty(childString)) {
					childString = null;
				}
				this.agentTaggingManager.saveAgent(agentTaggingModel, childString);
				msg = "Agent Group Tagging Created Successfully";						
			}					
			this.saveMessage(req, msg);
			////////////////////////////////////////////////////////
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel, req);
			
		}
		catch (FrameworkCheckedException ex)
		{			
			if(ex.getMessage() != null && ex.getMessage().equals("GroupTitleAlreadyExists")){
				req.setAttribute("message", "Group title already exists.");
			}else{
				req.setAttribute("message", ex.getMessage());
			}

			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
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
    
	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}
}