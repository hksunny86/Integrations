package com.inov8.microbank.webapp.action.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class FetchTitleController extends AjaxController {
	

	private static Log logger = LogFactory.getLog(FetchTitleController.class);
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		String name = "null";
		String errMsg = null;
		
		String acType = ServletRequestUtils.getRequiredStringParameter(request, "type");
		if(acType.equals("1")){
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();			
			switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			CustomerAccount custAcct = new CustomerAccount();
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setCustomerAccount(custAcct);
			logger.info("Core bank A/C No"+ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
			switchWrapper.getCustomerAccount().setNumber(ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
			switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			try {
				phoenixFinancialInstitution.titleFetch(switchWrapper);	
				name = switchWrapper.getCustomerAccount().getTitleOfTheAccount();
				name = StringEscapeUtils.escapeXml(name);
			} catch(Exception ex) {
				errMsg = ex.getMessage();
				ex.printStackTrace();
			}
		}else if(acType.equals("2")){
			String mobileNo = ServletRequestUtils.getRequiredStringParameter(request, "accountNo");
			logger.info("BB A/C No"+mobileNo);
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setMobileNo(mobileNo);
			appUserModel = smartMoneyAccountManager.getAppUserModel(appUserModel);
			if(appUserModel != null){
				name = appUserModel.getFirstName()+" "+appUserModel.getLastName();					
			}else{
				errMsg = "Account does not exist against provided account number";
			}
		}
		
		if(name !=null && name.equalsIgnoreCase("X"))
		{
			name="null";
			errMsg="Account does not exist against provided account number";
		}
		ajaxXmlBuilder.addItem("name", name);
		ajaxXmlBuilder.addItem("errMsg", errMsg);
		return ajaxXmlBuilder.toString();
	}

	public void setPhoenixFinancialInstitution(	AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
}
