package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class TillBalanceRequiredController extends AdvanceFormController {

	private final Logger logger = Logger.getLogger(TillBalanceRequiredController.class);
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 	
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

	public TillBalanceRequiredController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		requestWrapper.setAttribute("FetchTransactionListViewModel", requestWrapper.getAttribute("FetchTransactionListViewModel"));
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.setAttribute("FetchTransactionListViewModel", requestWrapper.getAttribute("FetchTransactionListViewModel"));
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
	
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);		
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		ThreadLocalAppUser.setAppUserModel(appUserModel);			

		requestWrapper.addParameter(CommandFieldConstants.KEY_MOB_NO, appUserModel.getMobileNo());
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID.toString(), DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
		
		String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_SAVE_TILL_BALANCE);
		
		boolean hasError = MfsWebUtil.isErrorXML(responseXML);
		
		mfsWebResponseDataPopulator.populateMessage(requestWrapper, responseXML);
		setTransactionInfo(requestWrapper);	

		if(hasError) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
//			allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue());
		}		
		
		return new ModelAndView( getSuccessView() );
	}
	
	
	private void setTransactionInfo(AllPayRequestWrapper requestWrapper) {

		String tranAmount = requestWrapper.getParameter("tranAmount");
		String productName = requestWrapper.getParameter("productName");
		String transactionDateTime = requestWrapper.getParameter("transactionDateTime");
		
		FetchTransactionListViewModel model = new FetchTransactionListViewModel();

		if(AllPayWebResponseDataPopulator.isValidString(tranAmount)) {
			model.setTranAmount(Double.valueOf(requestWrapper.getParameter("tranAmount")));			
		}
		
		if(AllPayWebResponseDataPopulator.isValidString(productName)) {
			model.setProductName(requestWrapper.getParameter("productName"));		
		}
		
		if(AllPayWebResponseDataPopulator.isValidString(transactionDateTime)) {
			model.setTransactionDateTime(requestWrapper.getParameter("transactionDateTime"));		
		}

		requestWrapper.addParameter("productName", requestWrapper.getParameter("productName"));
		requestWrapper.addParameter("tranAmount", requestWrapper.getParameter("tranAmount"));
		requestWrapper.addParameter("transactionDateTime", requestWrapper.getParameter("transactionDateTime"));
		
		requestWrapper.setAttribute("FetchTransactionListViewModel", model);		
	}
	
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
		return mfsWebResponseDataPopulator;
	}
	public void setMfsWebResponseDataPopulator(
			MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}
}