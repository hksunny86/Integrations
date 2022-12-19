package com.inov8.microbank.webapp.action.portal.linkpaymentmodemodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.AppRoleConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.accounttypemodule.AccountTypeManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import com.inov8.verifly.common.model.AccountInfoModel;

public class FetchAccountInfoAjaxController extends AjaxController{
    /**
     * This Controller Fetch the Account Info for bank, used in Link Payment Mode Screen
     */
	private FinancialIntegrationManager financialIntegrationManager;
	private AccountTypeManager accountTypeManager ;
	private LinkPaymentModeManager linkPaymentModeManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
		
	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		String actionType = ServletRequestUtils.getStringParameter(request, "actionType");
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		if ("1".equals(actionType)) {
			try
			{
			BankModel bankModel = new BankModel();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
			baseWrapper.setBasePersistableModel(bankModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
	        
			String nic = ServletRequestUtils.getStringParameter(request, "nic");
			String accountType = ServletRequestUtils.getStringParameter(request, "accountType");
			String currencyCode = ServletRequestUtils.getStringParameter(request, "currencyCode");
			String accountNum = ServletRequestUtils.getStringParameter(request, "accountNum");
			String mfsId = ServletRequestUtils.getStringParameter(request, "mfsId");
			String cardNo = ServletRequestUtils.getStringParameter(request, "cardNum");
			String expirayDate = ServletRequestUtils.getStringParameter(request, "expirayDate");
			
	        String title = null;
			//CustomerAccount customerAccount = new CustomerAccount();
	        
	        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setUserId(mfsId);
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapperUserDevice = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapperUserDevice);
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice.getBasePersistableModel();
			if (!userDeviceAccountsModel.getAccountEnabled())
			{
			
				throw new FrameworkCheckedException("Account is disabled.");
			}
			boolean isFirstSMA = true;
			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null){
				linkPaymentModeManager.isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId(),AppRoleConstantsInterface.RETAILER);
			}else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null){
				linkPaymentModeManager.isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId(),AppRoleConstantsInterface.DISTRIBUTOR);
			}else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null ){
				linkPaymentModeManager.isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId(),AppRoleConstantsInterface.CUSTOMER);
			}
			
	        if(abstractFinancialInstitution.isVeriflyLite() && isFirstSMA)
	        {
	        	
	        	if(abstractFinancialInstitution.isIVRChannelActive(accountNum, accountType, currencyCode, nic)){
					
	        		if(accountType.equalsIgnoreCase("5"))
	        		{
	        			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	        			AccountInfoModel accountInfoModel = new AccountInfoModel();
	        			
	        			accountInfoModel.setCardNo(cardNo);
	        			accountInfoModel.setCardExpiryDate(expirayDate);
	        			
	        			switchWrapper.setAccountInfoModel(accountInfoModel);
	        			
	        			switchWrapper.setBankId(bankModel.getBankId());
	        			switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
	        			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
	        			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC,nic);
	        			switchWrapper.getWorkFlowWrapper().setDeviceTypeModel(new DeviceTypeModel());
	        			switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
	        			abstractFinancialInstitution.checkBalanceForAccountVerification(switchWrapper);
	        		}
	        		else
	        		{
	        		title = abstractFinancialInstitution.getAccountTitle(accountNum, accountType, currencyCode, nic);
	        		}
				}
				else{
					
					throw new Exception("Customerprofiledoesnotexist.");
					
				}
	        }
	        else
	        {
	        	if(accountType.equalsIgnoreCase("5"))
        		{
        			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        			AccountInfoModel accountInfoModel = new AccountInfoModel();
        			
        			accountInfoModel.setCardNo(cardNo);
        			accountInfoModel.setCardExpiryDate(expirayDate);
        			
        			switchWrapper.setAccountInfoModel(accountInfoModel);
        			
        			switchWrapper.setBankId(bankModel.getBankId());
        			switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
        			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
        			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC,nic);
        			switchWrapper.getWorkFlowWrapper().setDeviceTypeModel(new DeviceTypeModel());
        			switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
        			abstractFinancialInstitution.checkBalanceForAccountVerification(switchWrapper);
        		}
        		else
        		{
        			title = abstractFinancialInstitution.getAccountTitle(accountNum, accountType, currencyCode, nic);
        		}
	        
				
	        }
	        
		        if(accountType.equalsIgnoreCase("5"))
		        {
		        	title = "Account has been verified.";
		        	
		        }
		        else
		        {
				
		            if(title!=null && title!="")
			        {
		        	title = "Account Title: " +title;
		        	
			        }
			        else
			        {
			        	throw new FrameworkCheckedException("Your request cannot be processed at the moment. Please try again later.");
			        }
				
		        }
		    
		        ajaxXmlBuilder.addItem("message", title);
		        
		        	
			}
			
			catch (Exception e )
			{
				
				e.printStackTrace();
				if (e.getMessage().equalsIgnoreCase("Customerprofiledoesnotexist."))
				{
					throw new FrameworkCheckedException("Customer profile does not exist.");
					
				}
				
				
				if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_PROF_NOT_EXIST))
				{
					throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_PROF_NOT_EXIST);
					
				}
				
				else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_ACC_NOT_EXIST))
				{
					throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_ACC_NOT_EXIST);
					
				}
 
					
				
				
				else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_INVALID_CUSTOMER_PROFILE))
				{
					throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_CUSTOMER_PROFILE);
					
				}
				
				else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_INVALID_IVR_RESP))
				{
					throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_IVR_RESP);
					
				}
				
				else if (e.getMessage().equalsIgnoreCase("Account is disabled."))
				{
					throw new FrameworkCheckedException("Account is disabled.");
					
				}
				
				
				
				
				
				else
				{
				
					throw new FrameworkCheckedException("Your request cannot be processed at the moment. Please try again later.");
				}
			}
		}
		
		else if ("2".equals(actionType))
		{
			String accountType = ServletRequestUtils.getStringParameter(request, "accountType");
			List<PaymentModeModel> paymentModeModelList =accountTypeManager.searchPaymentModeModel(Long.valueOf(accountType));
			if (paymentModeModelList.size() == 0)
				ajaxXmlBuilder.addItemAsCData("", "");
			ajaxXmlBuilder.addItems(paymentModeModelList, "name",
					"paymentModeId").toString();
 		}
		else {
			ajaxXmlBuilder.addItemAsCData("", "");
		}
		return ajaxXmlBuilder.toString();
	}
	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setAccountTypeManager(AccountTypeManager accountTypeManager) {
		this.accountTypeManager = accountTypeManager;
	}
	
	

}
