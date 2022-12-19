package com.inov8.microbank.webapp.action.allpayweb;

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
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class CreditCardPaymentStepOneController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private AgentWebManager agentWebManager;
	
	
	
	public CreditCardPaymentStepOneController (){
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
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);

		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_AGENT_CREDIT_CARD_INFO,false);
		
		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(request, responseXml);
			modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
			
		} else if(!validate(request)) {
			
			modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
			
		} else if(modelAndView.getViewName().equalsIgnoreCase(getFormView())) {

			setReferenceData(request);
		}
		
		String CCNO = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[2]");
		String BDDATE = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[3]");
		String BAMT = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[4]");
		String MAMTF = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[5]");
		String CCOB = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[6]");
		String TPAM = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[7]");
		
		requestWrapper.addParameter("CCNO", CCNO);
		requestWrapper.addParameter("BDDATE", BDDATE);		
		requestWrapper.addParameter("BAMT", BAMT);			
		requestWrapper.addParameter("MAMTF", MAMTF);			
		requestWrapper.addParameter("CCOB", CCOB);					
		requestWrapper.addParameter("TPAM", TPAM);		
		
		return modelAndView;
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
		Double rate = agentWebManager.getCommissionRate(productModel);

 		requestWrapper.setAttribute(CommandFieldConstants.KEY_CAMT, rate.toString());		
		
		if(productModel != null) {

			requestWrapper.setAttribute(AllPayWebConstant.PRODUCT_NAME.getValue(), productModel.getName());
			requestWrapper.setAttribute(AllPayWebConstant.PRODUCT_ID.getValue(), productModel.getProductId());
			requestWrapper.addParameter(AllPayWebConstant.PRODUCT_ID.getValue(), String.valueOf(productModel.getProductId()));
			requestWrapper.addParameter(AllPayWebConstant.PRODUCT_NAME.getValue(), productModel.getName());
			requestWrapper.addParameter("SID", requestWrapper.getParameter("SID"));	
			
		} else {
			requestWrapper.setAttribute("errors", "No Product Found.");
		}
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
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
}