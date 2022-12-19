package com.inov8.microbank.webapp.action.portal.ibft;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.MiddlewareRetryAdviceModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class IBFTRetryAdviceAjaxController extends AjaxController {

	/**
	 * @author Omar Butt
	 */
	
	@Autowired
	private TransactionReversalFacade transactionReversalFacade;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		String param = (String)request.getParameter("ibftRetryAdviceId");
		
		if(!StringUtil.isNullOrEmpty(param) && StringUtil.isNumeric(param)) {
			
			Long ibftRetryAdviceId = Long.parseLong(param);
			
			try{
				transactionReversalFacade.makeIBFTRetryAdvice(ibftRetryAdviceId);
				ajaxXmlBuilder.addItem("mesg", "IBFT Credit Advice has been pushed to SAF");
			
			}catch (FrameworkCheckedException ex) {
				ex.printStackTrace();
				if(ex.getMessage().equals("Already pushed to SAF, you cannot retry this advice.") 
						|| ex.getMessage().equals("Already Successful, you cannot retry this advice.")){

					ajaxXmlBuilder.addItem("mesg",ex.getMessage());
				}else{
					ajaxXmlBuilder.addItem("mesg","Some error has occured while pushing to SAF");
				}
			}
		}
		
		return ajaxXmlBuilder.toString();
	}

	public void setTransactionReversalFacade(TransactionReversalFacade transactionReversalFacade) {
		this.transactionReversalFacade = transactionReversalFacade;
	}
}