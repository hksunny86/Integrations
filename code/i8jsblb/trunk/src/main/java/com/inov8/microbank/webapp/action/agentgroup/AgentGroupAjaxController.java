/**
 * 
 */
package com.inov8.microbank.webapp.action.agentgroup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * @author KashifBa
 *
 */
public class AgentGroupAjaxController extends AjaxController {

	@Autowired
	private UserDeviceAccountsManager userDeviceAccountsManager;
	@Autowired
	private AgentTaggingManager agentTaggingManager;	
	@Autowired
	private HandlerManager handlerManager;
	private RetailerContactManager retailerContactManager;
	private AppUserManager appUserManager;


	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String mfsAccountId = request.getParameter(PortalConstants.KEY_MFS_ACCOUNT_ID);
		String _isParentAgent = request.getParameter("isParentAgent");
		String strGroupId = request.getParameter("groupId");
		Long groupId = StringUtil.isNullOrEmpty(strGroupId)? null : Long.parseLong(strGroupId);
				
		StringBuilder xml = new StringBuilder("");
		
		Boolean isParentCall = Integer.parseInt(_isParentAgent) == 1 ? Boolean.TRUE : Boolean.FALSE;

		String errorString = null;
		StringBuilder groupTitle = new StringBuilder();
		
		if(isParentCall && isParentExist(mfsAccountId, groupTitle, groupId)) {
			
			errorString = mfsAccountId + " Parent Agent Already exist in Group '"+groupTitle.toString()+"'";
			
		} else if(!isParentCall && isChildExist(mfsAccountId, groupTitle, groupId)) {
			
			errorString = mfsAccountId + " Child Agent Already exist in Group '"+groupTitle.toString()+"'";
		}

		if(errorString == null) {
			
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setUserId(mfsAccountId);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			
			userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			
			AppUserModel appUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
			
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
				
				if(RegistrationStateConstantsInterface.VERIFIED.longValue() != registrationStateId.longValue()) {

					errorString = ("Agent/Handler "+mfsAccountId+" is not with approved state");	
				
				} else if(appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()) {

					errorString = ("Agent/Handler "+mfsAccountId+" account is closed");	
				} 
				
				if(errorString == null) {
					
					Long retailerContactId = null;
					
					if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
						
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(new HandlerModel(appUserModel.getHandlerId()));
						handlerManager.loadHandler(searchBaseWrapper);

						HandlerModel handlerModel = (HandlerModel) searchBaseWrapper.getBasePersistableModel();
						
						AppUserModel _appUserModel = this.appUserManager.findByRetailerContractId(handlerModel.getRetailerContactId());
						
						 if(_appUserModel.getAccountClosedUnsettled() || _appUserModel.getAccountClosedSettled()) {

							errorString = (mfsAccountId + " Handler's Parent Agent "+_appUserModel.getFirstName()+" "+_appUserModel.getLastName() +" Account is closed");
							
						 } else {
							 retailerContactId = handlerModel.getRetailerContactId();
						 }
						
						
					} else {
						retailerContactId = appUserModel.getRetailerContactId();
					}
					
					if(retailerContactId != null) {
						
						BaseWrapper _baseWrapper = new BaseWrapperImpl();
						_baseWrapper.setBasePersistableModel(new RetailerContactModel(retailerContactId));
						
						retailerContactManager.loadRetailerContact(_baseWrapper);
						
						RetailerContactModel retailerContactModel = (RetailerContactModel) _baseWrapper.getBasePersistableModel();
						
						if(retailerContactModel.getBusinessName() != null) {
							
							businessName = retailerContactModel.getBusinessName(); 
						}
						
						xml.append(appUserModel.getFirstName());
						xml.append(" ");
						xml.append(appUserModel.getLastName());
						xml.append(";");
						xml.append(appUserModel.getMobileNo());
						xml.append(";");
						xml.append(appUserModel.getNic());
						xml.append(";");
						xml.append(businessName);
						xml.append(";");
						xml.append(appUserModel.getAppUserId());
					}					
				}
	
			} else {
				
				errorString = ("No Agent/Handler Found agaist ID : "+mfsAccountId);	
			}
		}
		
		if(errorString != null) {
			
			xml.append("Error");
			xml.append("\n");
			xml.append(errorString);
		}
				
		
		return xml.toString();
	}


	private Boolean isChildExist(String mfsAccountId, StringBuilder groupTitle, Long agentTaggingId) {
		boolean alreadyExists = false;
		List<AgentTaggingChildrenModel> list = agentTaggingManager.loadAgentTaggingChildrenList(new AgentTaggingChildrenModel(null, null, mfsAccountId));

		if(!list.isEmpty()) {
			AgentTaggingChildrenModel childModel = list.get(0);
			if(agentTaggingId == null || agentTaggingId.longValue() != childModel.getAgentTaggingId()){
				alreadyExists = true;
				AgentTaggingModel agentTaggingModel = agentTaggingManager.getAgentTaggingModel(childModel.getAgentTaggingId());
				groupTitle.append(agentTaggingModel.getGroupTitle());
			}
		}
		return alreadyExists;
	}
	

	private Boolean isParentExist(String mfsAccountId, StringBuilder groupTitle, Long agentTaggingId) {
		boolean alreadyExists = false;
		List<AgentTaggingModel> list = agentTaggingManager.getAgentTaggingModel(new AgentTaggingModel(mfsAccountId));

		if(!list.isEmpty()) {
			AgentTaggingModel agentTaggingModel = list.get(0);
			if(agentTaggingId == null || agentTaggingId.longValue() != agentTaggingModel.getAgentTaggingId()){
				alreadyExists = true;
				groupTitle.append(agentTaggingModel.getGroupTitle());
			}
		}
		return alreadyExists;
	}
	
	
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	
	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}	
	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
}