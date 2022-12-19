package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.MyCommissionEnum;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class MyCommissionController extends AdvanceFormController {

	private final Logger logger = Logger.getLogger(MyCommissionController.class);
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 	
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private final String PAGE_FROM = "allpay-web/myCommission";

	public MyCommissionController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {

		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {

		String nextPage = getSuccessView();
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());
				
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);			

		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID.toString(), DeviceTypeConstantsInterface.USSD.toString());
		
		Boolean pinVerified = Boolean.FALSE;
		Integer STATUS = AllPayWebResponseDataPopulator.VALID_BANK_PIN;
		String commissionOption = requestWrapper.getParameter(CommandFieldConstants.KEY_COMMISSION_OPTION);
		
		BANK_PIN_VERIFICATION : {
		
			allPayWebResponseDataPopulator.logActionLogModel();
			
			pinVerified = verifyPIN(requestWrapper);
			
			if(!pinVerified) {	
							
				STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
						
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
	
					nextPage = PAGE_FROM;
							
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {
	
					nextPage = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}
			}
		}

		if(pinVerified && STATUS == allPayWebResponseDataPopulator.VALID_BANK_PIN) {
			
			String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_MY_COMMISSION);
			
			boolean hasError = MfsWebUtil.isErrorXML(responseXML);			

			if(hasError) {
				
				mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
				
				nextPage = AllPayWebConstant.GENERIC_PAGE.getValue();
				
			} else {

				String DTODAY = MiniXMLUtil.getTagTextValue(responseXML, "/msg/params/param[@name='DTODAY']");
				String DYESTERDAY = MiniXMLUtil.getTagTextValue(responseXML, "/msg/params/param[@name='DYESTERDAY']");
				String CAMTF = MiniXMLUtil.getTagTextValue(responseXML, "/msg/params/param[@name='CAMTF']");
				
				String mesg = null;
				
				if(MyCommissionEnum.TODAY.getValue().equals(commissionOption) || MyCommissionEnum.YESTERDAY.getValue().equals(commissionOption)) {
					
					mesg = MessageUtil.getMessage("myCommission.notificationMessageSingleDate",new Object[]{DTODAY, CAMTF});
					
				} else {
					
					mesg = MessageUtil.getMessage("myCommission.notificationMessageDoubleDate",new Object[]{DTODAY, DYESTERDAY, CAMTF});
				}

				requestWrapper.setAttribute("mesg", mesg);
				requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Commission Notification");
				nextPage = AllPayWebConstant.GENERIC_PAGE.getValue();				
			}
		}
		
		return new ModelAndView(nextPage);
	}
	
	

	private Boolean verifyPIN(HttpServletRequest request) {

		Boolean pinVerified = Boolean.TRUE;

		String bankPin = request.getParameter(CommandFieldConstants.KEY_PIN);
		
		try {

			AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(request.getParameter(CommandFieldConstants.KEY_U_ID));

			allPayWebResponseDataPopulator.verifyPIN(appUserModel, MfsWebUtil.encryptPin(bankPin), null, null, Boolean.FALSE);
			
		} catch (FrameworkCheckedException e) {
			
			pinVerified = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
		return pinVerified;
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