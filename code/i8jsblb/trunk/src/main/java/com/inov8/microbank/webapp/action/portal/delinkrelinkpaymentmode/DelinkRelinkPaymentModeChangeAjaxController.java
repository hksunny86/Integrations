/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.delinkrelinkpaymentmode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * Project Name: microbank
 * 
 * @author Imran Sarwar Creation Date: Jan 19, 2007 Creation Time: 8:42:08 PM
 *         Description:
 */
public class DelinkRelinkPaymentModeChangeAjaxController extends AjaxController {

	private DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager;

	private FinancialIntegrationManager financialIntegrationManager;

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		BankModel bankModel = new BankModel();
		Boolean isBank = false;
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		if (abstractFinancialInstitution.isVeriflyLite()) {
			isBank = true;
		}
		Boolean isRelink = false;
		String isDeleted = "";
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try {
			Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "smAcId")));
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "apUsrId")));
			// getting log information from the request
			Long useCaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);

			// putting log information into wrapper for further used

			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_SMART_MONEY_ACC_ID, smartMoneyAccountId);
			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_APP_USER_ID, appUserId);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);

			isDeleted = ServletRequestUtils.getStringParameter(request, "isDeleted");
			boolean isRetailerOrDistributor = this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(appUserId);
			if (isDeleted != null && isDeleted.equals("true")) {
				if (isRetailerOrDistributor) {
					logger.info("Executing delete for allpay");
					baseWrapper = this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
				} else {
					logger.info("Executing delete for MFS");
					baseWrapper = this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
				}
				ajaxXmlBuilder.addItem("message", getMessage(request, "smartMoneyAccountModel.delete.success", request.getLocale()));
				ajaxXmlBuilder.addItem("divsToDisable", ServletRequestUtils.getStringParameter(request, "apUsrId") + ServletRequestUtils.getStringParameter(request, "smAcId"));
			} else {

				String btnName = "";
				String status = "De-Linked";
				// getting parameters from request

				btnName = EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "btn"));
				// aali
				if (isRetailerOrDistributor) {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
				} else {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
				}
				isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);
				String moreThanTwoAccounts = (String) baseWrapper.getObject("moreThanTwoAccounts");
				
					if (isRelink.booleanValue()) // relink scenerio
					{
						status = "Re-Linked";
						ajaxXmlBuilder.addItem(btnName, "De-Link");
					} else // delink scenario
					{
						status = "De-Linked";
						ajaxXmlBuilder.addItem(btnName, "Re-Link");
					}
				
				if ("y".equals(moreThanTwoAccounts)) {
					ajaxXmlBuilder.addItem("message", getMessage(request, "customer.allpayAccountLinkingError"));
				} else {
					ajaxXmlBuilder.addItem("message", getMessage(request, "smartMoneyAccountModel.success", new String[] { status }));
				}

			}

		}

		catch (Exception e) {

			e.printStackTrace();
			String errorMessage = "";
			isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);

			if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG)) {
				errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;

			}

			else if (e.getMessage().equalsIgnoreCase("Invalid Identifier..."))

			{
				errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;

			}

			else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)) {

				errorMessage = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;
			}

			else {

				if (isDeleted != null && isDeleted.equals("true")) {

					if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_DEACT_CHANNEL_REQ_FAILED))

					{
						errorMessage = "Record could not be deleted. Please contact administrator.";

					}

				} else {

					if (isRelink.booleanValue()) // delink scenario but
													// exception occurred
					{
						if (isBank) {

							errorMessage = "Account could not be relinked. Please try again.";
						} else {
							errorMessage = "Payment Mode could not be Re-Linked";
						}
					}

					else {
						if (isBank) {

							errorMessage = "Account could not be delinked. Please try again.";
						} else {

							errorMessage = "Payment Mode could not be De-Linked";

						}
					}
				}

			}

			if (isDeleted != null && isDeleted.equals("true")) {

				/*
				 * if (errorMessage.equalsIgnoreCase(""))
				 *  { errorMessage="Record could not be deleted. Please contact
				 * administrator.";
				 *  }
				 */
			} else {
				String btnName = "";
				String status = "";
				// getting parameters from request

				btnName = EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "btn"));
				if (isRelink.booleanValue()) // relink case
				{
					status = "De-Linked";
					ajaxXmlBuilder.addItem(btnName, "Re-Link");
				} else // Delink scenario
				{
					status = "Re-Linked";
					ajaxXmlBuilder.addItem(btnName, "De-Link");

				}

			}

			if (errorMessage.equalsIgnoreCase("")) {
				errorMessage = e.getMessage();
			}

			if (e.getMessage() == null) {
				errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;
			}

			ajaxXmlBuilder.addItem("message", errorMessage);

		}
		return ajaxXmlBuilder.toString();
	}

	public void setDelinkRelinkPaymentModeManager(DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager) {
		this.delinkRelinkPaymentModeManager = delinkRelinkPaymentModeManager;
	}

}
