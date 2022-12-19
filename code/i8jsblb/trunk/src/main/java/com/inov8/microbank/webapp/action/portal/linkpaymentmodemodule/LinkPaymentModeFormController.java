package com.inov8.microbank.webapp.action.portal.linkpaymentmodemodule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AccountTypeModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CardTypeModel;
import com.inov8.microbank.common.model.CurrencyCodeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;


public class LinkPaymentModeFormController extends AdvanceFormController

{
	private ReferenceDataManager	referenceDataManager;

	private LinkPaymentModeManager	linkPaymentModeManager;
	private FinancialIntegrationManager financialIntegrationManager;	


	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{

		super.initBinder(request, binder);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

	public LinkPaymentModeFormController()
	{
		setCommandName("linkPaymentModeModel");
		setCommandClass(LinkPaymentModeModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception
	{   
		// TODO Auto-generated method stub
		Long bankId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId();
		LinkPaymentModeModel linkPaymentModeModel = new LinkPaymentModeModel();
		
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
		request.setAttribute("veriflyRequired", veriflyRequired);
		if(!veriflyRequired){
			linkPaymentModeModel.setName(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName());
		}
		
		boolean veriflyLite = abstractFinancialInstitution.isVeriflyLite();
		request.setAttribute("veriflyLite", veriflyLite);
//		request.setAttribute("bank", veriflyLite); // verifly lite mean bank Bank
		
		UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId();
		Long finIntegrationId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getFinancialIntegrationId();
		if (finIntegrationId == 3)
		{
			request.setAttribute("bank", veriflyLite); // verifly lite mean bank Bank
		}
		else
		{
			request.setAttribute("bank", false); // every other bank
		}
		String eappUserId = ServletRequestUtils.getStringParameter(request, PortalConstants.KEY_APP_USER_ID);
		Long appUserId = null;
		if( null != eappUserId ){
			
			appUserId = new Long(EncryptionUtil.decryptWithDES(eappUserId));
			
            BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
			userInfoListViewModel.setAppUserId(appUserId);
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByPrimaryKey(baseWrapper);
			userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
			
			linkPaymentModeModel.setMfsId(userInfoListViewModel.getUserId());
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			linkPaymentModeModel.setNic(userInfoListViewModel.getNic());
			//request.setAttribute(PortalConstants.KEY_APP_USER_ID, eappUserId);
			linkPaymentModeModel.setAppUserId(userInfoListViewModel.getAppUserId());
		}
		
		
		
		return linkPaymentModeModel;
		//return null;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception
	{
		// TODO Auto-generated method stub
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);		
		boolean veriflyLite = abstractFinancialInstitution.isVeriflyLite();
		request.setAttribute("veriflyLite", veriflyLite);
		Long finIntegrationId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getFinancialIntegrationId();
		if (finIntegrationId == 3)
		{
			request.setAttribute("bank", veriflyLite); // verifly lite mean bank Bank
		}
		else
		{
			request.setAttribute("bank", false); // every other bank
		}

		PaymentModeModel paymentModeModel = new PaymentModeModel();

		//paymentModeModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(paymentModeModel, "name",
				SortingOrder.ASC);

		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex1)
		{
			ex1.getStackTrace();
		}
		List<PaymentModeModel> paymentModeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			paymentModeModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("PaymentModeModelList", paymentModeModelList);

		CardTypeModel cardTypeModel = new CardTypeModel();

		cardTypeModel.setName(new String());
		referenceDataWrapper = new ReferenceDataWrapperImpl(cardTypeModel, "name", SortingOrder.ASC);

		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex1)
		{
			ex1.getStackTrace();
		}
		List<CardTypeModel> cardTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			cardTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("CardTypeModelList", cardTypeModelList);
        
		
		CurrencyCodeModel currencyCodeModel = new CurrencyCodeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(currencyCodeModel, "name", SortingOrder.ASC);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			ex.getStackTrace();
		}
		List<CurrencyCodeModel> currencyCodeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			currencyCodeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("currencyCodeModelList", currencyCodeModelList);

		AccountTypeModel accountTypeModel = new AccountTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(accountTypeModel, "name", SortingOrder.ASC);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			ex.getStackTrace();
		}
		List<AccountTypeModel> accountTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			accountTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("accountTypeModelList", accountTypeModelList);

		
		return referenceDataMap;

	}

	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception
	{
		// TODO Auto-generated method stub

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		LinkPaymentModeModel linkPaymentModeModel = null;
		
		try
		{

			//int defAccountFlag = 0;
			long useCaseId = 0;
			long actionId = 0;
			linkPaymentModeModel = (LinkPaymentModeModel) arg2;

			if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null)
			{
				useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
				System.out.println("usecase id " + useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

			}
			if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null)
			{
				actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
				System.out.println("action id " + actionId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			}

			baseWrapper.putObject("linkPaymentModeModel", linkPaymentModeModel);

			baseWrapper = linkPaymentModeManager.createLinkPaymentMode(baseWrapper);

			if (baseWrapper != null)
			{

				String sucessMessage = "", failureMessage = "";
				if (baseWrapper.getObject("ErrorMessage") != null)
				{

					failureMessage = (String) baseWrapper.getObject("ErrorMessage"); // verifly   message
					super.saveMessage(request, failureMessage);
					// as the error occure so returnig to the previous form, with failure message
					UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
					userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
					baseWrapper.setBasePersistableModel(userInfoListViewModel);
					baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
					userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
					request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
					request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
					request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
					request.setAttribute("txtNic", userInfoListViewModel.getNic());
					
					return super.showForm(request, arg1, arg3);

				}
				else
				{
					sucessMessage = getText("linkpaymentmodemodule.paymend.mode.linked.success", request.getLocale());
				}
				
				//if appuserid exist mean we need to navigate to mnonewmfsaccount page
				String eappUserId = ServletRequestUtils.getStringParameter(request, "eappUserId"); 
				request.setAttribute(PortalConstants.KEY_APP_USER_ID, eappUserId);
				String returnView = "";
				if(eappUserId != null && eappUserId.trim().length() > 0 ){
					returnView = "redirect:p_mnonewmfsaccountform.html?actionId=3&appUserId="+eappUserId;
				}else{
					returnView = this.getSuccessView();
				}
				
				this.saveMessage(request, sucessMessage);
				ModelAndView modelAndView = new ModelAndView(returnView);
				return modelAndView;
			}

			else
			{
				UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
				userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
				baseWrapper.setBasePersistableModel(userInfoListViewModel);
				baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
				userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
				request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
				request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
				request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
				request.setAttribute("txtNic", userInfoListViewModel.getNic());
				this.saveMessage(request, getText("linkpaymentmodemodule.mfsid.exist.failure", request.getLocale()));
				return super.showForm(request, arg1, arg3);
			}

		}
		catch (FrameworkCheckedException ex)
		{
			String errorMessage = ex.getMessage();
			if (baseWrapper.getObject("ErrMessage") != null)
			{
				errorMessage = (String) baseWrapper.getObject("ErrMessage"); // Customer does not exits
			}
			if(ex.getMessage() == "implementationNotSupportedException"){
				errorMessage = getText("linkpaymentmodemodule.featureNotSupported", request.getLocale());
			}
			if(ex.getMessage() == "linkPaymentMode.customerprofiledoesnotexist"){
				errorMessage = getText("linkPaymentMode.customerprofiledoesnotexist", request.getLocale());
			}
			
			if(ex.getMessage() == WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED){
				errorMessage = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
			}

			if(ex.getMessage() == "This Payment Mode is already linked."){
				errorMessage = "This Payment Mode is already linked.";
			}

			
			else
			{
				if(errorMessage.equalsIgnoreCase(""))
				{	
					errorMessage = "Smart Money Account Could Not Be Saved";
				}
			}

			
			
			UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
			userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request, errorMessage);
			ex.printStackTrace();
			
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			BankModel bankModel = new BankModel();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
			baseWrapperBank.setBasePersistableModel(bankModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
			request.setAttribute("veriflyRequired", veriflyRequired);
			if(!veriflyRequired &&  PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode()) ){
				linkPaymentModeModel.setName(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName());
			}
			
			return super.showForm(request, arg1, arg3);
		}
		catch (Exception ex)
		{
			UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
			userInfoListViewModel = (UserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			ex.printStackTrace();
			
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			BankModel bankModel = new BankModel();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
			baseWrapperBank.setBasePersistableModel(bankModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
			request.setAttribute("veriflyRequired", veriflyRequired);
			if(!veriflyRequired &&  PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode()) ){
				linkPaymentModeModel.setName(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName());
			}
			
			return super.showForm(request, arg1, arg3);
		}

		//return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setLinkPaymentModeManager(LinkPaymentModeManager linkPaymentModeManager)
	{
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

}
