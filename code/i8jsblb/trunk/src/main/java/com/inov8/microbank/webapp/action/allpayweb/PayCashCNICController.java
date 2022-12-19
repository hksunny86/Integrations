package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * 
 * @author Kashif Bashir
 * @since April, 2012
 * 
 */

public class PayCashCNICController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

	private final static Log logger = LogFactory.getLog(PayCashController.class);
	private final String PAGE_FROM = "allpay-web/payCashCNICStepOne";
	public final String CASH_WITHDRAW = "Cash Withdraw";
	public final String ACCOUNT_TO_CASH = "Account To Cash";
	public final String CASH_TO_CASH = "Cash Transfer";
	public final String BULK_PAYMENT_WITHDRAW = "Bulk Payment Withdrawal";
	public final String TRANSACTION_TYPE = "TRANSACTION_TYPE";

	private AgentWebManager agentWebManager;
	
	public PayCashCNICController() {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
		
		return new Object();
	}

	
	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {	
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);	
		
		return null;
	}	
	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
	
		return onCreate(request, response, model, exception);
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		logger.info("[PayCasyController.onCreate] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		
		
		String nextPage = getSuccessView();
		
		String KEY_WALKIN_RECEIVER_CNIC = request.getParameter(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC);
		
		TransactionDetailModel transactionDetailModel = this.getCommonCommandManager().loadTransactionDetailModel(KEY_WALKIN_RECEIVER_CNIC, ProductConstantsInterface.BULK_PAYMENT, SupplierProcessingStatusConstantsInterface.IN_PROGRESS);
			
		if(transactionDetailModel == null) {	
			
			nextPage = PAGE_FROM;				
			request.setAttribute("errors", "Transaction is either already claimed or expired.");	
		}
		
		return new ModelAndView(nextPage, "transactionDetailModel", transactionDetailModel);
	}
	
	
	private CommonCommandManager commonCommandManager = null;
	private CommonCommandManager getCommonCommandManager() {
		
		if(commonCommandManager == null) {
			   commonCommandManager = 
				   (CommonCommandManager) getWebApplicationContext().getBean("commonCommandManager");
		}
		
		return commonCommandManager;
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
