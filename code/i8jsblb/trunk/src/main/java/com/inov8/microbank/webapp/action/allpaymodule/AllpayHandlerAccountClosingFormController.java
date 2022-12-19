package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountClosureFacade;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;


public class AllpayHandlerAccountClosingFormController extends AdvanceFormController
{
	private AppUserManager appUserManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private SmsSender smsSender;
	private MfsAccountClosureFacade mfsAccountClosureFacade;

	public AllpayHandlerAccountClosingFormController()
	{
		setCommandName("appUserModel");
	    setCommandClass(AppUserModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		String mfsId = ServletRequestUtils.getStringParameter(req, "retailerId");
		req.setAttribute("mfsId",mfsId);
		  
	    AppUserModel appUserModel = new AppUserModel();		  
	    appUserModel.setAppUserId(Long.valueOf(appUserId)); 
	    
	    if(appUserId != null) {         
	          appUserModel= this.appUserManager.getUser(appUserId);  
	    }
	    
	    return appUserModel;	    
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest req) throws Exception {
		return null;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception 
	{
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		
		ModelAndView modelAndView = null;
		
		try {         	
			    
			String retailerId = ServletRequestUtils.getStringParameter(req, "customerId");
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject("retailerId",retailerId);
		      
		    AppUserModel appUserModel = (AppUserModel) obj;
		    baseWrapper.setBasePersistableModel(appUserModel);
		    baseWrapper = mfsAccountClosureFacade.makeHandlerAccountClosed(baseWrapper);
		    appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			
		    //Sending SMS on Successfull Account Closing
			if(!appUserModel.getAccountClosedSettled()) {	    	
			   	logger.info("[AllpayRetailerAccountClosingFormController.onUpdate] Sending SMS to handler after closing successfully. MobileNo:" + appUserModel.getMobileNo());
			    String messageString = MessageUtil.getMessage("smsCommand.close_handler");
			    SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),messageString);
			
			    try {
					smsSender.send(smsMessage);
			    } catch (Exception e) {
					e.printStackTrace();
					logger.error("[AllpayRetailerAccountClosingFormController.onUpdate] Exception while sending SMS to Mobile No:" + appUserModel.getMobileNo());
				}
			}
			   
			Map<String, String> map = new HashMap<String, String>();
			map.put("status","success");
			modelAndView = new ModelAndView(this.getSuccessView()+"?status=success&appUserId="+appUserModel.getAppUserId(),map);
		    
		} catch (Exception ex) {
			
		    	if("AccLinked".equals(ex.getMessage()))
			    	req.setAttribute("message", "Please delete payment mode first to close the account.");
		    	else if("ChildExists".equals(ex.getMessage()))
		    		req.setAttribute("message", "Agent has some Sub Agents accociated with it, so account cannot be closed.");	
		    	else if("TrnsPend".equals(ex.getMessage()))
		    		req.setAttribute("message", "Transaction is pending against account, so account cannot be closed.");	
		    	else
		    		req.setAttribute("message", "Account Cannot be closed or settled, Kindly consult administrator for details.");
		    	req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
		
		    	return super.showForm(req, res, errors);
		    
		} finally {
				ThreadLocalAppUser.remove();
				ThreadLocalActionLog.remove();
			}
			
		    return modelAndView;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
	public void setMfsAccountClosureFacade(MfsAccountClosureFacade mfsAccountClosureFacade) {
		this.mfsAccountClosureFacade = mfsAccountClosureFacade;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
}