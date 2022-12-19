package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.TransactionLogModel;
import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionsManager;

public class PhoenixTransactionDetailController extends AdvanceFormController {

	private PostedTransactionsManager postedTransactionsManager;
	
	public PhoenixTransactionDetailController() {
		setCommandName("transactionLogModel");
		setCommandClass(TransactionLogModel.class);
	}

	@Override
	protected Map loadReferenceData( HttpServletRequest request ) throws Exception {
		return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {
		
		Long trxLogId = ServletRequestUtils.getLongParameter(request, "trxLogId");
		TransactionLogModel transactionLogModel = postedTransactionsManager.loadTransactionDetail(trxLogId);
		
		return transactionLogModel;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		return null;
	}

	public void setPostedTransactionsManager(
			PostedTransactionsManager postedTransactionsManager) {
		this.postedTransactionsManager = postedTransactionsManager;
	}

}
