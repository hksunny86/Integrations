package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class BillPaymentController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	
	public BillPaymentController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
		setReferenceData(request);
		
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		if(!validate(request, response, exception)) {
			
			modelAndView = showForm(request, response, exception);
		}
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);

		logger.info("[BillPaymentCommand.onCreate] AppUserID:" + appUserModel.getAppUserId() + " Consumer No:" + requestWrapper.getParameter(CommandFieldConstants.KEY_CSCD));
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_INFO,false);

		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(request, responseXml);
			modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		else if(!validate(request)) {
			
			modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
		} 

		setReferenceData(request);

		return modelAndView;
	}	
	

	private Boolean validate(HttpServletRequest request) {
		
		String billDateOverdue = (String) request.getAttribute( "BDATEOD" );
		
		Boolean validate = Boolean.TRUE;
			
		String bpaid = (String) request.getAttribute( "BPAID" );
			
		if( bpaid != null && !bpaid.isEmpty() && bpaid.equals( "1" )) {
			request.setAttribute("errors", "This bill is already paid.");
			validate = Boolean.FALSE;
		}
		
		return validate;
	}
	
	
	private Boolean validate(HttpServletRequest request, HttpServletResponse response, BindException exception) {

		Boolean flag = Boolean.TRUE;
		
		String deviceFlowId = request.getParameter("dfid");
		
		if( deviceFlowId.equalsIgnoreCase("1") )
		{			
		}		

		String consumerNo = request.getParameter("CSCD") ;
		consumerNo = consumerNo.trim();
		
		if( consumerNo == null || consumerNo.equals("") )
		{
			request.setAttribute("valErrors", "Consumer number is not provided.");
			flag = Boolean.FALSE;;
		}
		
		ValidationErrors validationErrors = new ValidationErrors(); 
		validationErrors = ValidatorWrapper.doIntegerWithoutTrim(consumerNo, validationErrors, "Consumer number");
		
		if( validationErrors.hasValidationErrors() )
		{
			request.setAttribute("valErrors", "Please enter a valid consumer number.");
			flag = Boolean.FALSE;;	
		}
		
		return flag;
	}
	
	private void setReferenceData(HttpServletRequest request) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		allPayWebResponseDataPopulator.setDefaultParams(requestWrapper);		
		
		Long productId = null;
		
		String productIdParam = request.getParameter("PID");
		
		if(productIdParam.contains(" ")) {
			productIdParam = productIdParam.replace(" ", "+");
 		}		
		
		if(!StringUtils.isEmpty(productIdParam)) {
			
			if(StringUtils.isNumeric(productIdParam)) {
			
				productId = Long.valueOf(productIdParam);
			
			} else {
			
				productIdParam = allPayWebResponseDataPopulator.decrypt(productIdParam);
				productId = Long.valueOf(productIdParam);
			}
		}
		
		ProductModel productModel = allPayWebResponseDataPopulator.getProductModel(productId);
		
		if(productModel != null) {

			requestWrapper.setAttribute("PID", String.valueOf(productModel.getProductId()));
			requestWrapper.setAttribute("PNAME", productModel.getName());
			
		} else {
			requestWrapper.setAttribute("errors", "No Product Found.");
		}
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

	public MfsWebManager getMfsWebController() {
		return mfsWebController;
	}
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
		return mfsWebResponseDataPopulator;
	}
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
}
