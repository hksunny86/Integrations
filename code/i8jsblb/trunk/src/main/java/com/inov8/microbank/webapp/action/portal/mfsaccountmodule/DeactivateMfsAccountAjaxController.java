/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Feb 2, 2007
 * Creation Time: 			12:55:47 PM
 * Description:				
 */
public class DeactivateMfsAccountAjaxController extends AjaxController
{

	private MfsAccountManager	mfsAccountManager;
	private ComplaintManager complaintManager;

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

		Long id = null;
		String appUserId = null;
		boolean isLockUnlock = false;
		Long usecaseId = null;
		Long actionId = null;
		String btnName = "";
		String status = "de-activated";
		String errorMessage = "";

		
//		try
//		{
			appUserId = ServletRequestUtils.getStringParameter(request, "appUsrId");
			usecaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);
			String lockUnlockVal = ServletRequestUtils.getStringParameter(request, "isLockUnlock");
			if(lockUnlockVal !=null && lockUnlockVal.equals("true")){
				isLockUnlock = true;
			}
			btnName = EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "btn"));

			if (null != appUserId && appUserId.trim().length() > 0 )
			{
				id = new Long(appUserId);
				
				if (logger.isDebugEnabled())
				{
					logger.debug("id is not null....retrieving object from DB and then updating it");
				}
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
				userDeviceAccountsModel.setAppUserId(id);
				baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, usecaseId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
				baseWrapper.putObject("isLockUnlock", new Boolean(isLockUnlock));

				String expMsg = "";
				
				try {	
					baseWrapper = this.mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);
					
//					
//					AppUserModel appUserModel = mfsAccountManager.getAppUserModelByPrimaryKey(id);
//					
//					if(appUserModel != null) {
//						
//						if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserModel.getAppUserTypeId().longValue()) {
//							complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_DEACTIVATE_CUSTOMER, id);
//							
//						} else if(UserTypeConstantsInterface.RETAILER.longValue() == appUserModel.getAppUserTypeId().longValue()) {
//							complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_DEACTIVATE_AGENT, id);							
//						}
//					}
					
				}catch(Exception e) {
					
					expMsg = e.getMessage();
					e.printStackTrace();
				}

				Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
				
				if(!isLockUnlock){
					if (isActive.booleanValue())
					{
						status = "re-activated";
						ajaxXmlBuilder.addItem(btnName, "Deactivate");
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_ACTIVATE, id);
					}
					else
					{
						status = "de-activated";
						ajaxXmlBuilder.addItem(btnName, "Reactivate");
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_DEACTIVATE, id);
					}
					errorMessage = getMessage(request, "mfsAccountModel.updatesuccess", new String[] { status });
				}else{
					if (isActive.booleanValue()){
						status = "unblocked";
						ajaxXmlBuilder.addItem(btnName, "Block");
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_UNBLOCK, id);
					}else{
						status = "blocked";
						ajaxXmlBuilder.addItem(btnName, "Unblock");
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_BLOCK, id);
					}
					errorMessage = getMessage(request, "mfsAccountModel.updatesuccess.lockunlock", new String[] { status });
				}
				
				ajaxXmlBuilder.addItem("message", errorMessage);

			}
			
			request.getSession().setAttribute("ajaxMessageToDisplay", errorMessage);
			
			return ajaxXmlBuilder.toString();
			
//		}
//		catch (Exception exp)
//		{
//			logger.error("Exception=>", exp);
//			if(isReqForActive.booleanValue())
//			{
//				activeValToReturn = true;
//				status = "re-activatation";
//				ajaxXmlBuilder.addItem(btnName, "De-Activate");				
//			}
//			else
//			{
//				ajaxXmlBuilder.addItem(btnName, "Re-Activate");				
//			}
//			messageToShow = getMessageSourceAccessor().getMessage("mfsAccountModel.updatefailure",
//					new String[] { status });
//		}

	}

	/**
	 * @param mfsAccountManager the mfsAccountManager to set
	 */
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager)
	{
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	} 
}
