package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountClosureFacade;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;


public class AllpayRetailerAccountClosingFormController extends AdvanceFormController
{
	private AppUserManager appUserManager;
	private SmsSender smsSender;
	private MfsAccountClosureFacade mfsAccountClosureFacade;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private AccountManager accountManager;
	private AccountHolderManager accountHolderManager;


	public AllpayRetailerAccountClosingFormController()
	{
		setCommandName("appUserModel");
	    setCommandClass(AppUserModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{
		  String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		  String retailerId = ServletRequestUtils.getStringParameter(req, "retailerId");
		  
	      AppUserModel appUserModel = new AppUserModel();		  
	     
	      if(null != appUserId)
	      {         
	          appUserModel= this.appUserManager.getUser(appUserId);  
	      }
	    
	     if(null!=retailerId)
	     {
	    	 appUserModel.setRetailerContactId(Long.valueOf(retailerId));
	     } 
	     return appUserModel;	    
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest req) throws Exception
	{
		return null;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		try 
		    {         	
			    BaseWrapper baseWrapper = new BaseWrapperImpl();
				Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
				Long customerAccountTypeId = null;
			    String retailerId = ServletRequestUtils.getStringParameter(req, "customerId");
			    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			    baseWrapper.putObject("retailerId", retailerId);
		      
		      	AppUserModel appUserModel = (AppUserModel) obj;
//		      	baseWrapper.setBasePersistableModel(appUserModel);
				AppUserModel appUserModel1 = appUserManager.loadAppUser(appUserModel.getAppUserId());

				SmartMoneyAccountModel smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1);

				if(smartMoneyAccountModel == null )
				{
					SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
					sma.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel = smartMoneyAccountManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
				}

				AccountHolderModel accountHolderModel = null;
				customerAccountTypeId = CustomerAccountTypeConstants.RETAILER;
				AccountModel accountModel = this.accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel1.getNic(),customerAccountTypeId,statusId);
				if(accountModel != null)
				{
					if(accountModel.getAccountHolderId() != null)
					{
						BaseWrapper accountHolderWrapper = new BaseWrapperImpl();
						AccountHolderModel acHolderModel = new AccountHolderModel();
						acHolderModel.setAccountHolderId(accountModel.getAccountHolderId());
						accountHolderWrapper.setBasePersistableModel(acHolderModel);
						accountHolderWrapper = this.accountHolderManager.loadAccountHolder(accountHolderWrapper);

						accountHolderModel = (AccountHolderModel)accountHolderWrapper.getBasePersistableModel();
						accountHolderModel.setActive(Boolean.FALSE);
					}
					accountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);
				}

				baseWrapper.putObject("accountHolderModel",accountHolderModel);
				baseWrapper.putObject("accountModel",accountModel);
				baseWrapper.setBasePersistableModel(appUserModel);

				baseWrapper = mfsAccountClosureFacade.makeAgentAccountClosed(baseWrapper);
		      	
		      	appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			    //Sending SMS on Successfull Account Closing
			    if(!appUserModel.getAccountClosedSettled()){	    	
			    	logger.info("[AllpayRetailerAccountClosingFormController.onUpdate] Sending SMS to agent after closing successfully. MobileNo:" + appUserModel.getMobileNo());
				    String messageString = MessageUtil.getMessage("smsCommand.close_agent");
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
		    }
		   
		    catch (Exception ex)
		    {
		    	if("AccLinked".equals(ex.getMessage()))
			    	req.setAttribute("message", "Please delete payment mode first to close the account.");
		    	else if("ChildExists".equals(ex.getMessage()))
		    		req.setAttribute("message", "Agent has some Sub Agents accociated with it, so account cannot be closed.");	
		    	else if("TrnsPend".equals(ex.getMessage()))
		    		req.setAttribute("message", "Transaction is pending against account, so account cannot be closed.");
		    	else if("Balance not Zero".equals(ex.getMessage()))
			    	req.setAttribute("message", "Account can not be closed due to outstanding balance.");
		    	else
		    		req.setAttribute("message", "Account Cannot be closed or settled, Kindly consult administrator for details.");
		    	req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
		    	return super.showForm(req, res, errors);
		    }
			finally
			{
				ThreadLocalAppUser.remove();
				ThreadLocalActionLog.remove();
			}
			
		    return modelAndView;
	}
	
	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}
	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setMfsAccountClosureFacade(
			MfsAccountClosureFacade mfsAccountClosureFacade) {
		this.mfsAccountClosureFacade = mfsAccountClosureFacade;
	}
	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setAccountHolderManager(AccountHolderManager accountHolderManager) {
		this.accountHolderManager = accountHolderManager;
	}
}

