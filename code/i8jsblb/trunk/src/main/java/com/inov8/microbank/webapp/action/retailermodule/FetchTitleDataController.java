package com.inov8.microbank.webapp.action.retailermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class FetchTitleDataController extends AjaxController {
	

	private static Log logger = LogFactory.getLog(FetchTitleDataController.class);
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		
		switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
		switchWrapper.setPaymentModeId(6L);
		
		CustomerAccount custAcct = new CustomerAccount();
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setCustomerAccount(custAcct);
		switchWrapper.getCustomerAccount().setNumber(ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
		switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		String name = "null";

		String mobileNo = ServletRequestUtils.getRequiredStringParameter(request, "mobileNumber");
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		
		appUserModel = smartMoneyAccountManager.getAppUserModel(appUserModel);
		
		String errMsg = isAlreadyLinked(appUserModel);
		
		try {
			
			if(errMsg == null) {	
				
				ThreadLocalAppUser.setAppUserModel(appUserModel);
				
				phoenixFinancialInstitution.titleFetch(switchWrapper);
				
				name = switchWrapper.getCustomerAccount().getTitleOfTheAccount();
				name = StringEscapeUtils.escapeXml(name);
			
				if(name !=null && name.equalsIgnoreCase("X"))
				{
					name="null";
					errMsg="Account does not exist against provided account number";
				}
			}			
			
		} catch(Exception ex) {
			errMsg = ex.getMessage();
			ex.printStackTrace();
			
			if (request.getRequestedSessionId() != null
			        && !request.isRequestedSessionIdValid()) {
				errMsg = "Your Session is expired. Kindly login to proceed";
			}
			
		} finally {
		
			ajaxXmlBuilder.addItem("name", name);
			ajaxXmlBuilder.addItem("errMsg", errMsg);
		}
		
		return ajaxXmlBuilder.toString();
	}

	
	/**
	 * @param appUserModel
	 * @return
	 */
	private String isAlreadyLinked(AppUserModel appUserModel) {
		
		String message = null;		
		
		if(appUserModel != null) {
			
			RetailerContactModel retailerContactModel = appUserModel.getRelationRetailerContactIdRetailerContactModel();
			
			try {
				
				SmartMoneyAccountModel smaModel = new SmartMoneyAccountModel();
				smaModel.setDeleted(false);
				smaModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				smaModel.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(smaModel);
				
				int count = smartMoneyAccountManager.countSmartMoneyAccountModel(baseWrapper);

				if(count > 0) {
					message = "Payment Mode is Already Linked, Kindly Delete Existing Linked Account First.";
				}
				
			} catch (FrameworkCheckedException e) {
				message = e.getMessage();
				logger.error(e);
			}	
		}		
		
		return message;
	}
	


	public void setPhoenixFinancialInstitution(	AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
}
