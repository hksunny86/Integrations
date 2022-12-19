package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum;

public class RecepitController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private AgentWebManager agentWebManager;
		
	public RecepitController(){
		setCommandClass(Object.class);
		setCommandName ("object");
	}
	
	private PayCashWithDrawalController cashWithDrawalController = null;
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {	

		if(cashWithDrawalController == null) {		
			cashWithDrawalController = (PayCashWithDrawalController) super.getWebApplicationContext().getBean("payCashWithDrawalController");
		}

		String productId = request.getParameter(CommandFieldConstants.KEY_PROD_ID);
		String transactionCode = request.getParameter(CommandFieldConstants.KEY_TX_ID); 

		logger.debug(productId +" - "+ transactionCode);
		
		ProductModel productModel = this.agentWebManager.getProductModel(Long.valueOf(productId));
		
		if(UtilityCompanyEnum.contains(productId) || InternetCompanyEnum.contains(productId) || productModel.getServiceId().longValue() == 7L) {
			
			agentWebManager.updateBillPaymentRequestParameter(transactionCode, request);
			request.setAttribute(CommandFieldConstants.KEY_TX_CODE, "BILL_PRODUCT");
			
		} else if(String.valueOf(ReportTypeEnum.CASH_DEPOSIT.getProductId()).equals(productId)) {
			
			agentWebManager.updateCashDepositRequestParameter(transactionCode, request);
			request.setAttribute(CommandFieldConstants.KEY_TX_CODE, "TRANSACTION");
			
		} else {
			
			agentWebManager.initTransactionParams(request, Boolean.TRUE, agentWebManager.loadTransactionCodeByCode(transactionCode));
			request.setAttribute(CommandFieldConstants.KEY_TX_CODE, "TRANSACTION");
		}
		
		
		
		return new Object();
	}
/*
 insert into device_type_command values (222, 8, 30, 1,1,1,1,SYSDATE,SYSDATE);
 */
	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {	
		
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {
		


		return super.showForm(request, arg1, arg3);	
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {
		return onCreate(arg0, arg1, arg2, arg3);
	}

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}

	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
}
