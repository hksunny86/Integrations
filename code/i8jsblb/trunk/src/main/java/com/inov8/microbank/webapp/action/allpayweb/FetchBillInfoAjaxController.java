package com.inov8.microbank.webapp.action.allpayweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class FetchBillInfoAjaxController extends AjaxController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);		
		
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_INFO, Boolean.FALSE);
		
		return responseXml.toString();
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
}
