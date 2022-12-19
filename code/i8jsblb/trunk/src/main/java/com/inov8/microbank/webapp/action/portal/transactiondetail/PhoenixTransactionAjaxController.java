package com.inov8.microbank.webapp.action.portal.transactiondetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionsManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class PhoenixTransactionAjaxController extends AjaxController{

	private PostedTransactionsManager postedTransactionsManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		Long trxLogId = null;
		boolean result = false;
		String transactionLogId = ServletRequestUtils.getStringParameter(request, "trxLogId");
		String rrn = ServletRequestUtils.getStringParameter(request, "rrn");
		String resType = ServletRequestUtils.getStringParameter(request, "resType");
		
		if (null != transactionLogId && transactionLogId.trim().length() > 0 ){
			trxLogId = new Long(transactionLogId);
			result = postedTransactionsManager.resetPostedTransaction(trxLogId,resType);
		}
		
		if(result){
			String msgKey = "";
			if(resType.equals("FT")){
				msgKey = "postedtransaction.reversal.success";
			}else if(resType.equals("RNR")){
				msgKey = "postedtransaction.rnr.success";
			}else if(resType.equals("ER")){
				msgKey = "postedtransaction.er.success";
			}
			ajaxXmlBuilder.addItem("message", getMessage(request, msgKey, new String[] { rrn }));
			ajaxXmlBuilder.addItem("currentBtn", transactionLogId);
		}else{
			ajaxXmlBuilder.addItem("message", getMessage(request, "postedtransaction.reversal.failure"));
		}
		
		return ajaxXmlBuilder.toString();
	}

	public void setPostedTransactionsManager(PostedTransactionsManager postedTransactionsManager) {
		this.postedTransactionsManager = postedTransactionsManager;
	}

}
