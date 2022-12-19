package com.inov8.microbank.webapp.action.portal.transactionreversal;

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

public class RetryAdviceAjaxController extends AjaxController {

	/**
	 * @author atifhu
	 */
	
	@Autowired
	private TransactionReversalFacade transactionReversalFacade;
	
	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		String param=(String)request.getParameter("middlewareRetryAdvRprtId");
		String rowAction = (String) request.getParameter("rowAction");
		boolean isSkipAction = ( ! StringUtil.isNullOrEmpty(rowAction) && rowAction.equals("skip") ) ? Boolean.TRUE : Boolean.FALSE;

		if(param!=null && !param.trim().equals(""))
		{
			Long	middlewareRetryAdvRprtId	=	Long.parseLong(param);
			
			MiddlewareRetryAdviceModel	model=new MiddlewareRetryAdviceModel();
			model.setMiddlewareRetryAdviceId(middlewareRetryAdvRprtId);
			
			BaseWrapper	baseWrapper=new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			try
			{
				if(isSkipAction){
					baseWrapper = transactionReversalFacade.loadRetryAdviceSummary(baseWrapper);
					transactionReversalFacade.makeCoreRetryAdviceSkipped(baseWrapper);
					ajaxXmlBuilder.addItem("mesg", "Advice has been Skipped");
				}else{
					baseWrapper = transactionReversalFacade.loadRetryAdviceSummary(baseWrapper);
					transactionReversalFacade.makeCoreRetryAdvice(baseWrapper);
					ajaxXmlBuilder.addItem("mesg", "Advice has been pushed to SAF");
				}
			} 
			catch (FrameworkCheckedException ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().equals("Already pushed to SAF, you cannot retry this advice.")){
					ajaxXmlBuilder.addItem("mesg",ex.getMessage());
				}else{
					ajaxXmlBuilder.addItem("mesg",
							"Some error has occured while pushing to SAF");
				}
			}
		}
		
		return ajaxXmlBuilder.toString();
	}

	public void setTransactionReversalFacade(
			TransactionReversalFacade transactionReversalFacade) {
		this.transactionReversalFacade = transactionReversalFacade;
	}
}