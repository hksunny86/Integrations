package com.inov8.microbank.webapp.action.retailermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class FetchTitleDataByAccNoController extends AjaxController {

	private static Log logger = LogFactory.getLog(FetchTitleDataByAccNoController.class);
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
		switchWrapper.setPaymentModeId(6L);

		CustomerAccount custAcct = new CustomerAccount();

		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setCustomerAccount(custAcct);
		switchWrapper.getCustomerAccount().setNumber(ServletRequestUtils.getRequiredStringParameter(request,"accountNo"));
		switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		String name = "nill";
		
		try {
			//ThreadLocalAppUser.setAppUserModel(appUserModel);
			phoenixFinancialInstitution.titleFetch(switchWrapper);
			name = switchWrapper.getCustomerAccount().getTitleOfTheAccount();
			name = StringEscapeUtils.escapeXml(name);
			ajaxXmlBuilder.addItem("name", name);
		} catch (Exception ex) {
			ex.printStackTrace();
			ajaxXmlBuilder.addItem("name", name);
		}		return ajaxXmlBuilder.toString();
	}
	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
}
