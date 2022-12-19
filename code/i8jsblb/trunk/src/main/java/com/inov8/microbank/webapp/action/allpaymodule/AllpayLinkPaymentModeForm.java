package com.inov8.microbank.webapp.action.allpaymodule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.inov8.microbank.common.model.AccountTypeModel;
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CardTypeModel;
import com.inov8.microbank.common.model.CurrencyCodeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;


public class AllpayLinkPaymentModeForm extends AdvanceAuthorizationFormController

{
	private static final Logger LOGGER = Logger.getLogger( AllpayLinkPaymentModeForm.class );
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

	public AllpayLinkPaymentModeForm()
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
//		request.setAttribute("bank", veriflyLite); // verifly lite mean Bank
		
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
		//request.getParameter(PortalConstants.KEY_APP_USER_ID)
		Long appUserId = null;
//		if( null != eappUserId ){
//			
//			appUserId = new Long(EncryptionUtil.decryptWithDES(eappUserId));
//			
//            BaseWrapper baseWrapper = new BaseWrapperImpl();
//            AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
//			userInfoListViewModel.setAppUserId(appUserId);
//			baseWrapper.setBasePersistableModel(userInfoListViewModel);
//			baseWrapper = this.linkPaymentModeManager.loadAllPayUserInfoListViewByPrimaryKey(baseWrapper);
//			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
//			
//			linkPaymentModeModel.setMfsID(userInfoListViewModel.getUserId());
//			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
//			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
//			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
//			request.setAttribute("txtNic", userInfoListViewModel.getNic());
//			linkPaymentModeModel.setNic(userInfoListViewModel.getNic());
//			//request.setAttribute(PortalConstants.KEY_APP_USER_ID, eappUserId);
//			linkPaymentModeModel.setAppUserId(userInfoListViewModel.getAppUserId());
//		}
		
		
		
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
			
			ArrayList<PaymentModeModel> toBeRemovedList = new ArrayList<PaymentModeModel>();
			
			PaymentModeModel pM = null;
			// To remove Agent Entry
			for(int i=0; i<paymentModeModelList.size() ; i++){
				pM = paymentModeModelList.get(i);
				if(pM.getName().contains("BB") == true){
					toBeRemovedList.add(paymentModeModelList.get(i));
				}
			}
			
			for(int j=0; j<toBeRemovedList.size(); j++){
				paymentModeModelList.remove(toBeRemovedList.get(j));
			}
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
//			check is an account other than OLA is already linked 
			if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(linkPaymentModeModel.getMfsId())){
			String messageString = MessageUtil.getMessage(
					"customer.allpayAccountLinkingError");
			super.saveMessage(request, messageString);
			AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			request.setAttribute(PortalConstants.KEY_APP_USER_ID, null);
			LinkPaymentModeModel tempLink = (LinkPaymentModeModel)this.loadFormBackingObject(request);
			if (tempLink.getName() != null ){
				linkPaymentModeModel.setName(tempLink.getName());
			}
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			request.setAttribute("bankNameBank", linkPaymentModeModel.getName());
			return super.showForm(request, arg1, arg3);
			}
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
					AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
					userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
					baseWrapper.setBasePersistableModel(userInfoListViewModel);
					baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
					userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
					request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
					request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
					request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
					request.setAttribute("txtNic", userInfoListViewModel.getNic());
					//UserUtils.getCurrentUser().getappuse
					
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
				AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
				userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
				baseWrapper.setBasePersistableModel(userInfoListViewModel);
				baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
				userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
				request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
				request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
				request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
				request.setAttribute("txtNic", userInfoListViewModel.getNic());
				this.saveMessage(request, getText("linkpaymentmodemodule.allpayid.exist.failure", request.getLocale()));
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
			
			if(ex != null && ex.getMessage() != null) {
				
				if(ex.getMessage().equals("implementationNotSupportedException")){
					errorMessage = getText("linkpaymentmodemodule.featureNotSupported", request.getLocale());
				}
				
				if(ex.getMessage().equals("linkPaymentMode.customerprofiledoesnotexist")){
					errorMessage = getText("linkPaymentMode.customerprofiledoesnotexist", request.getLocale());
				}
				
				if(ex.getMessage().equals(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED)){
					errorMessage = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
				}

				if(ex.getMessage().equals("This Payment Mode is already linked.")){
					errorMessage = "This Payment Mode is already linked.";
				}				
			}
			
			if (errorMessage != null && errorMessage.equalsIgnoreCase("Service unavailable due to technical difficulties, please try again or contact service provider.")) {
				
			} else if(errorMessage != null && errorMessage.equalsIgnoreCase("")) {
				
				errorMessage = "Smart Money Account Could Not Be Saved";
			}

			
			
			AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request, errorMessage);
			//ex.printStackTrace();
			
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
			AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request,MessageUtil.getMessage("6075"));

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
	protected ModelAndView onAuthorization(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ModelAndView modelAndView = null;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		LinkPaymentModeModel linkPaymentModeModel =(LinkPaymentModeModel) command;
		try
		{	
								
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(linkPaymentModeModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(PortalConstants.LINK_PAYMENT_MODE_USECASE_ID);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.LINK_PAYMENT_MODE_USECASE_ID,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
				
				long useCaseId = 0;
				long actionId = 0;
				
//				check is an account other than OLA is already linked 
				if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(linkPaymentModeModel.getMfsId())){
					String messageString = MessageUtil.getMessage(
							"customer.allpayAccountLinkingError");
					super.saveMessage(request, messageString);
					AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
					userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
					baseWrapper.setBasePersistableModel(userInfoListViewModel);
					request.setAttribute(PortalConstants.KEY_APP_USER_ID, null);
					LinkPaymentModeModel tempLink = (LinkPaymentModeModel)this.loadFormBackingObject(request);
					if (tempLink.getName() != null ){
						linkPaymentModeModel.setName(tempLink.getName());
					}
					baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
					userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
					request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
					request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
					request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
					request.setAttribute("txtNic", userInfoListViewModel.getNic());
					request.setAttribute("bankNameBank", linkPaymentModeModel.getName());
					return super.showForm(request, response, errors);
				}
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
						AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
						userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
						baseWrapper.setBasePersistableModel(userInfoListViewModel);
						baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
						userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
						request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
						request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
						request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
						request.setAttribute("txtNic", userInfoListViewModel.getNic());
						//UserUtils.getCurrentUser().getappuse
						
						return super.showForm(request, response, errors);

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
					
					//this.saveMessage(request, sucessMessage);
					modelAndView = new ModelAndView(returnView);
					//return modelAndView;
				}

				else
				{
					AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
					userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
					baseWrapper.setBasePersistableModel(userInfoListViewModel);
					baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
					userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
					request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
					request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
					request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
					request.setAttribute("txtNic", userInfoListViewModel.getNic());
					return super.showForm(request, response, errors);
				}
					
				/////////////////////////
				Long actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,null, refDataModelString,null,usecaseModel,null,request);
				this.saveMessage(request,getText("actionAuthorization.actionAuthorized", actionAuthorizationId.toString(), request.getLocale()));
				
			}
			else
			{									
				Long actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,null, refDataModelString,null,usecaseModel.getUsecaseId(),linkPaymentModeModel.getMfsId(),false,null,request);
				this.saveMessage(request,getText("actionAuthorization.actionPending", actionAuthorizationId.toString(), request.getLocale()));
				modelAndView= new ModelAndView(getSuccessView());
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured : " +ex.getMessage());
			  	
	    	String errorMessage = ex.getMessage();
			
			if (baseWrapper.getObject("ErrMessage") != null)
			{
				errorMessage = (String) baseWrapper.getObject("ErrMessage"); 
			}
			
			if(ex != null && ex.getMessage() != null) {
				
				if(ex.getMessage().equals("implementationNotSupportedException")){
					errorMessage = getText("linkpaymentmodemodule.featureNotSupported", request.getLocale());
				}
				
				if(ex.getMessage().equals("linkPaymentMode.customerprofiledoesnotexist")){
					errorMessage = getText("linkPaymentMode.customerprofiledoesnotexist", request.getLocale());
				}
				
				if(ex.getMessage().equals(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED)){
					errorMessage = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
				}

				if(ex.getMessage().equals("This Payment Mode is already linked.")){
					errorMessage = "This Payment Mode is already linked.";
				}
				if(ex.getMessage().contains("already exist"))
					errorMessage = ex.getMessage();
				
			}
			
			if (errorMessage != null && errorMessage.equalsIgnoreCase("Service unavailable due to technical difficulties, please try again or contact service provider.")) {
				
			} else if(errorMessage != null && errorMessage.equalsIgnoreCase("")) {
				
				errorMessage = "Smart Money Account Could Not Be Saved";
			}

			
			
			AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request, errorMessage);
			//ex.printStackTrace();
			
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
			
			return super.showForm(request, response, errors);
	    	
	    	
		}
		catch (Exception ex)
		{	
			LOGGER.error("Exception occured : " +ex.getMessage());
			  	
	    	
			
			
			AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
			userInfoListViewModel.setUserId(linkPaymentModeModel.getMfsId());
			baseWrapper.setBasePersistableModel(userInfoListViewModel);
			baseWrapper = this.linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
			userInfoListViewModel = (AllpayUserInfoListViewModel)baseWrapper.getBasePersistableModel();
			request.setAttribute("txtFirstName", userInfoListViewModel.getFirstName());
			request.setAttribute("txtLastName", userInfoListViewModel.getLastName());
			request.setAttribute("txtMobileNo", userInfoListViewModel.getMobileNo());
			request.setAttribute("txtNic", userInfoListViewModel.getNic());
			
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			//ex.printStackTrace();
			
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
			
			return super.showForm(request, response, errors);
		}
		return modelAndView;
	}
	@Override
	protected ModelAndView onEscalate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onResolve(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
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
