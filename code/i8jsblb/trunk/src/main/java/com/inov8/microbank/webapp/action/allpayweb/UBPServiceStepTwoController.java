package com.inov8.microbank.webapp.action.allpayweb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class UBPServiceStepTwoController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator; 
	
	public UBPServiceStepTwoController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {

		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {	

		String nextView = getSuccessView();
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		requestWrapper.toString();
		
//		String resultData = null;
//		Map<String, String> responseData = null;
//
//		
//		if(!isTokenValid(request)) {			
//			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
//		}
//		
//		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);		
//		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
//		ThreadLocalAppUser.setAppUserModel(appUserModel);		
//		
//		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
//		
//		if(MfsWebUtil.isErrorXML(responseXml)) {
//			
//			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
//			
//			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
//			
//			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
//			
//			if(AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {
//				
//				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
//				
//				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
//					
//					nextView = "allpay-web/billpaymentconfirmation";
//					this.createParameters(requestWrapper);
//					
//				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {
//
//					nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
//				}				
//			}			
//			
//		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
//			
//			resultData = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg)");
//			responseData = getResultData(resultData, requestWrapper);
//		}
		
		return new ModelAndView (nextView, "responseData", null);
	}	
	

	/**
	 * @param request
	 */
	private void createParameters(HttpServletRequest request) {
		
		request.setAttribute("BDATE", request.getParameter("BDATE"));
		request.setAttribute("LBDATEF", request.getParameter("LBDATEF"));
		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
		request.setAttribute("BPAID", request.getParameter("BPAID"));
		request.setAttribute("CDCUSTMOB", request.getParameter("CDCUSTMOB"));

		request.setAttribute("BAMT", request.getParameter("BAMT"));
		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
		request.setAttribute("TPAMF", request.getParameter("TPAMF"));
		request.setAttribute("CAMTF", request.getParameter("CAMTF"));
		request.setAttribute("TAMT", request.getParameter("TAMT"));
		request.setAttribute("RPNAME", request.getParameter("RPNAME"));
		request.setAttribute("MOBN", request.getParameter("MOBN"));
		request.setAttribute("TPAM", request.getParameter("TPAM"));
		request.setAttribute("CAMT", request.getParameter("CAMT"));

		request.setAttribute("PNAME", request.getParameter("PNAME"));
		request.setAttribute("MOBN", request.getParameter("MOBN"));
		request.setAttribute("TXAMF", request.getParameter("TXAMF"));
		request.setAttribute("TXAM", request.getParameter("TXAM"));
		request.setAttribute("PID", request.getParameter("PID"));
		request.setAttribute("SID", request.getParameter("SID"));
		request.setAttribute("UID", request.getParameter("UID"));
		request.setAttribute("billServiceLabel", request.getParameter("billServiceLabel"));
		request.setAttribute("PNAME", request.getParameter("PNAME"));
		request.setAttribute("dfid", request.getParameter("dfid"));
		request.setAttribute("PID", request.getParameter("PID"));
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		request.setAttribute("SID", request.getParameter("SID"));		
	} 
	
	/**
	 * @param response
	 * @param request
	 * @return
	 */
	private Map<String, String> getResultData(String response, HttpServletRequest request) {
		
		Map<String, String> responseData = new HashMap<String, String>();
		
		response = response.replaceAll("\n", ";");
		
		String[] lines = response.split(";");
		
		int i = 0;
		
		for(String line : lines) {
			
			if(i != 4 && i != 0) {
			
				String[] KV = line.split(":");
				
				if(i == 1) {				
					responseData.put("Consumer Number", KV[1]);
				} else {
					responseData.put(KV[0], KV[1]);
				}
			} else if(i == 4) {
				responseData.put("Dated", line);				
			} else if(i == 0) {
				request.setAttribute("Heading", line);	
			}
			i++;
		}	
		
		request.setAttribute("TRXID", responseData.get("Tx ID"));
		request.setAttribute("PID", request.getParameter("PID"));
		logger.info(request.getAttribute("TRXID"));		
		
		return responseData;
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
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
