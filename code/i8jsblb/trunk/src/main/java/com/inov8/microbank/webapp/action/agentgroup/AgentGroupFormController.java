package com.inov8.microbank.webapp.action.agentgroup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class AgentGroupFormController extends AdvanceAuthorizationFormController {

	@Autowired
	MessageSource messageSource;

	@Autowired
	AgentTaggingManager agentTaggingManager;

	AppUserManager appUserManager;
	@Autowired
	private HandlerManager handlerManager;
	private RetailerContactManager retailerContactManager;
	

	public AgentGroupFormController() {

		setCommandName("agentGroupVOModel");
	    setCommandClass(AgentGroupVOModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {

		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReSubmit",false);
		String groupId = httpServletRequest.getParameter("groupId");
		
		AgentGroupVOModel agentGroupVOModel = new AgentGroupVOModel();
		agentGroupVOModel.setUsecaseId(PortalConstants.CREATE_AGENT_GUOUP_TAGGING_USECASE_ID);
		
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(httpServletRequest,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
				throw new FrameworkCheckedException("illegal operation performed");
			}
			
			XStream xstream = new XStream();
			agentGroupVOModel = (AgentGroupVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			agentGroupVOModel.setGroupIdentity(-1L);
			if(actionAuthorizationModel.getUsecaseId().longValue()==PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID){
				agentGroupVOModel.setGroupIdentity(agentGroupVOModel.getGroupId());
			}
			if(StringUtil.isNullOrEmpty(agentGroupVOModel.getChildString())){
				agentGroupVOModel.setChildString("");
			}
			
//			return agentGroupVOModel;
		}
		///End Added for Resubmit Authorization Request
		
		if(!isReSubmit && !StringUtil.isNullOrEmpty(groupId)) {
			
			AgentTaggingViewModel agentTaggingViewModel = agentTaggingManager.loadAgentTaggingViewModel(Long.valueOf(groupId));
			agentGroupVOModel = new AgentGroupVOModel();
			agentGroupVOModel.setGroupId(Long.valueOf(groupId));
			agentGroupVOModel.setGroupIdentity(Long.valueOf(groupId));
			agentGroupVOModel.setAgentName(agentTaggingViewModel.getAgentName());
			agentGroupVOModel.setAppUserId(agentTaggingViewModel.getAppUserId());
			agentGroupVOModel.setBusinessName(agentTaggingViewModel.getBusinessName());
			agentGroupVOModel.setCnic(agentTaggingViewModel.getCnic());
			agentGroupVOModel.setGroupTitle(agentTaggingViewModel.getGroupTitle());
			agentGroupVOModel.setMobileNumber(agentTaggingViewModel.getMobileNumber());
			agentGroupVOModel.setParrentId(agentTaggingViewModel.getParrentId());
			agentGroupVOModel.setStatus(agentTaggingViewModel.getStatus());	
			agentGroupVOModel.setUsecaseId(PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID);
			
			String childString = agentTaggingManager.getAgentTaggingChildrenList(new AgentTaggingChildrenModel(null, agentTaggingViewModel.getPk(), null));
			
			agentGroupVOModel.setChildString(childString.trim());
		}
		
		return agentGroupVOModel;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		
		return new java.util.HashMap();
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse HttpServletResponse, Object command, BindException exception) throws Exception {
		
		return onUpdate(httpServletRequest, HttpServletResponse, command, exception);
	}
	
	
	public Boolean isValidGroup(AgentGroupVOModel agentGroupVOModel, HttpServletRequest httpServletRequest) {
				
		Boolean isValidGroup = Boolean.TRUE;
		
		AgentTaggingModel _agentTaggingModel = new AgentTaggingModel();
		_agentTaggingModel.setGroupTitle(agentGroupVOModel.getGroupTitle());
		List<AgentTaggingModel> list = agentTaggingManager.getAgentTaggingModel(_agentTaggingModel);			
		
		if(list.size() > 0 && agentGroupVOModel.getGroupId() == null) {
			
			super.saveMessage(httpServletRequest, "Agent Group "+agentGroupVOModel.getGroupTitle()+" already exists");
			
			isValidGroup = Boolean.FALSE;
			return isValidGroup;
		}
		
		if(list.size() > 0 && agentGroupVOModel.getGroupId() != null) {
			
			for(AgentTaggingModel model : list) {
				
				if(model.getAgentTaggingId().longValue() != agentGroupVOModel.getGroupId().longValue()) {

					isValidGroup = Boolean.FALSE;
					super.saveMessage(httpServletRequest, "Agent Group "+agentGroupVOModel.getGroupTitle()+" already exists");
					break;
				}
			}
			
			return isValidGroup;
		}	
		
		return isValidGroup;
	}	
	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse HttpServletResponse, Object command, BindException exception) throws Exception {
		
		if(1==1) {
			//return onAuthorization(httpServletRequest, HttpServletResponse, command, exception);
		}
		
		AgentGroupVOModel agentGroupVOModel = (AgentGroupVOModel) command;
		try{
			Boolean isValid = this.isValidGroup(agentGroupVOModel, httpServletRequest);
			
			if(isValid && agentGroupVOModel.getGroupId() != null) {
				
				isValid = checkIfAgentTransferRuleExists(agentGroupVOModel, httpServletRequest);
			}
			
			if(!isValid) {
				
				String childString = getAgentTaggingChildrenList(httpServletRequest);
				agentGroupVOModel.setChildString(childString.trim());
				agentGroupVOModel.setGroupIdentity(0L);
				
				return new ModelAndView(this.getFormView(), "agentGroupVOModel", agentGroupVOModel);			
			}
			
			String msg = "";
			
			AgentTaggingModel agentTaggingModel = agentTaggingManager.createAgentTaggingModel(agentGroupVOModel);
			
			boolean isTitleUnique = agentTaggingManager.isTitleUnique(agentTaggingModel.getAgentTaggingId(), agentTaggingModel.getGroupTitle());
			if(!isTitleUnique){
				throw new FrameworkCheckedException("GroupTitleAlreadyExists");
			}

			
			if(agentGroupVOModel.getGroupId() != null) {
				
				agentTaggingManager.updateAgentTaggingModel(agentTaggingModel, httpServletRequest);
				msg = "Agent Group Tagging Updated Successfully";
				
			} else {
				
				agentTaggingManager.saveAgent(agentTaggingModel, httpServletRequest);
				msg = "Agent Group Tagging Created Successfully";					
			}
			
			this.saveMessage(httpServletRequest, msg);
	
			
		} catch (FrameworkCheckedException ex) {
			logger.error(ex.getMessage(), ex);
			if(ex.getMessage() != null && ex.getMessage().equals("GroupTitleAlreadyExists")){
				this.saveMessage(httpServletRequest, "Group title already exists.");
			} else {
				this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
			}
		} catch (Exception ex) {	
			logger.error(ex.getMessage(), ex);
			this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
		}
		
		return new ModelAndView("redirect:p_searchAgentGroup.html");
	}
	
	
	/**
	 * @param agentGroupVOModel
	 * @param httpServletRequest
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private Boolean checkIfAgentTransferRuleExists(AgentGroupVOModel agentGroupVOModel, HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
	
		if(!agentGroupVOModel.getStatus()) {
	
			if(this.agentTaggingManager.checkIfAgentTransferRuleExists(agentGroupVOModel.getGroupId())) {
	
				super.saveMessage(httpServletRequest, "Agent Transfer Rules found against this group.\nBefore deactivate Agent Transfer Rules against this group must be removed.");
				return Boolean.FALSE;	
			}
		}
		
		return Boolean.TRUE;
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest httpServletRequest, HttpServletResponse HttpServletResponse, Object command, BindException exception) throws Exception {

		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(httpServletRequest, "resubmitRequest",false);
		Long actionAuthorizationId = null;
		if(resubmitRequest){
			actionAuthorizationId=ServletRequestUtils.getLongParameter(httpServletRequest, "actionAuthorizationId");
		}
		
		AgentGroupVOModel agentGroupVOModel = (AgentGroupVOModel) command;
		
		String childString = getAgentTaggingChildrenList(httpServletRequest);

		Boolean isValid = this.isValidGroup(agentGroupVOModel, httpServletRequest);
				
		if(isValid && agentGroupVOModel.getGroupId() != null) {
			
			isValid = checkIfAgentTransferRuleExists(agentGroupVOModel, httpServletRequest);
		}	
		
		if(!isValid) {
						
			agentGroupVOModel.setChildString(childString.trim());
			agentGroupVOModel.setGroupIdentity(0L);
			
			return new ModelAndView(this.getFormView(), "agentGroupVOModel", agentGroupVOModel);			
		}
		
		
		agentGroupVOModel.setChildString(childString.trim());
		
		String enAppUserId = ServletRequestUtils.getStringParameter(httpServletRequest, "appUserId"); 
		
		try {
										
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(agentGroupVOModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(agentGroupVOModel.getUsecaseId());
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(agentGroupVOModel.getUsecaseId(), new Long(0));
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			if(nextAuthorizationLevel.intValue() < 1 ) {
				
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, agentGroupVOModel.getUsecaseId());
				
				if(agentGroupVOModel.getUsecaseId() != null && agentGroupVOModel.getUsecaseId() == PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID) {
					
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				
				} else {
					
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				}
								
				AgentTaggingModel agentTaggingModel = agentTaggingManager.createAgentTaggingModel(agentGroupVOModel);
				
				boolean isTitleUnique = agentTaggingManager.isTitleUnique(agentTaggingModel.getAgentTaggingId(), agentTaggingModel.getGroupTitle());
				if(!isTitleUnique){
					throw new FrameworkCheckedException("GroupTitleAlreadyExists");
				}
				
				if(agentGroupVOModel.getGroupId() != null) {
					
					agentTaggingManager.updateAgentTaggingModel(agentTaggingModel, httpServletRequest);
					
				} else {
					
					agentTaggingManager.saveAgent(agentTaggingModel, httpServletRequest);					
				}				
				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString, "", usecaseModel, actionAuthorizationId, httpServletRequest);
				this.saveMessage(httpServletRequest, "Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			
			} else {
				
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, "", refDataModelString, "", usecaseModel.getUsecaseId(),agentGroupVOModel.getParrentId(), resubmitRequest, actionAuthorizationId, httpServletRequest);
				this.saveMessage(httpServletRequest,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		
		} catch (FrameworkCheckedException ex) {
			logger.error(ex.getMessage(), ex);
			if(ex.getMessage() == null){
				this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
			}else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage())) {
				this.saveMessage(httpServletRequest, super.getText("userManagementModule.userIdUnique", httpServletRequest.getLocale()));
			} else if(ex.getMessage() != null && ex.getMessage().equals("GroupTitleAlreadyExists")){
				this.saveMessage(httpServletRequest, "Group title already exists.");
			}else{
				this.saveMessage(httpServletRequest, ex.getMessage());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
		}
		
		return new ModelAndView("redirect:p_searchAgentGroup.html");
	}

	
	private String getAgentTaggingChildrenList(HttpServletRequest httpServletRequest) throws FrameworkCheckedException {

		List<AgentTaggingChildrenModel> agentTaggingChildrenList = agentTaggingManager.getAgentTaggingChildrenList(httpServletRequest, new AgentTaggingModel());
		
		StringBuilder xml = new StringBuilder("");
		
		for(AgentTaggingChildrenModel _agentTaggingChildrenModel : agentTaggingChildrenList) {
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(new AppUserModel(_agentTaggingChildrenModel.getAppUserModelId()));
			
			this.appUserManager.loadAppUser(searchBaseWrapper);
			
			AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();

			String errorString = null;
			String businessName = "";
			
			if(appUserModel != null && 
					(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()
					|| appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue())) {
	

				logger.info("RegistrationStateId : "+appUserModel.getRegistrationStateId());
				logger.info("AccountStateId : "+appUserModel.getAccountStateId());
				
				Long registrationStateId = -1L;
				
				if(appUserModel.getRegistrationStateId() != null) {
					registrationStateId = appUserModel.getRegistrationStateId();
				} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
					registrationStateId = RegistrationStateConstantsInterface.VERIFIED;
				}
				
				if(errorString == null) {
					
					Long retailerContactId = appUserModel.getRetailerContactId();
					
					if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
						
						SearchBaseWrapper _searchBaseWrapper = new SearchBaseWrapperImpl();
						_searchBaseWrapper.setBasePersistableModel(new HandlerModel(appUserModel.getHandlerId()));
						handlerManager.loadHandler(_searchBaseWrapper);
						HandlerModel HandlerModel = ((HandlerModel) _searchBaseWrapper.getBasePersistableModel());
						retailerContactId = HandlerModel.getRetailerContactId();
					}
					
					if(retailerContactId != null) {
						
						BaseWrapper _baseWrapper = new BaseWrapperImpl();
						_baseWrapper.setBasePersistableModel(new RetailerContactModel(retailerContactId));
						
						retailerContactManager.loadRetailerContact(_baseWrapper);
						
						RetailerContactModel retailerContactModel = (RetailerContactModel) _baseWrapper.getBasePersistableModel();
						
						if(retailerContactModel.getBusinessName() != null) {
							
							businessName = retailerContactModel.getBusinessName(); 
						}
					}
					
					xml.append(appUserModel.getFirstName());
					xml.append(" ");
					xml.append(appUserModel.getLastName());
					xml.append(";");
					xml.append(businessName);
					xml.append(";");
					xml.append(appUserModel.getMobileNo());
					xml.append(";");
					xml.append(appUserModel.getNic());
					xml.append(";");
					xml.append(appUserModel.getAppUserId());
					xml.append(";");
					xml.append(_agentTaggingChildrenModel.getUserId());
					xml.append(";");
					xml.append("");
					xml.append("|");				
				}	
			} 			
		}
		
		return xml.toString();
	}	
	
	
	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}	
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}
}