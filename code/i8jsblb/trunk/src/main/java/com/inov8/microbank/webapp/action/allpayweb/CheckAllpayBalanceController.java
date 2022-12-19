package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.webapp.action.allpayweb.formbean.AgentWebFormBean;
import com.inov8.microbank.webapp.action.allpayweb.utils.ParameterMapper;

public class CheckAllpayBalanceController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private CommandManager commandManager;
	
	public CheckAllpayBalanceController () {
		
		setCommandName(AgentWebFormBean.CLASS_NAME);
	    setCommandClass(AgentWebFormBean.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		AgentWebFormBean agentWebFormBean = new AgentWebFormBean();
		agentWebFormBean.setDeviceTypeId(Long.valueOf(8));
		
		return agentWebFormBean;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {

		logger.info("[CheckBalanceAllpayBalanveController.loadReferenceData] Showing Pin Code screen for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
//		request.setAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.inov8.framework.webapp.action.AdvanceFormController#onCreate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object formBean, BindException exception) throws Exception {	
				
		ModelAndView modelAndView = new ModelAndView();
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
		//requestWrapper.addParameter("ACCTYPE", "1");
		
		String nextView = getSuccessView();
		
		if(!isTokenValid(request)) {	
			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		

		ParameterMapper.mapParameters(formBean, requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		//For Using Verify Pin Command I am setting retry-count = 0, 
		//In the below chunk I am managing retry count separately.
		/*String pin = requestWrapper.getParameter("bankPin");
		// New Wrapper for Pin Command
    	BaseWrapper pinWrapper = new BaseWrapperImpl();	
    	pinWrapper.putObject("ENCT", EncryptionUtil.ENCRYPTION_TYPE_AES);
    	pinWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
    	pinWrapper.putObject("DTID", DeviceTypeConstantsInterface.ALLPAY_WEB);
    	pinWrapper.putObject("PIN_RETRY_COUNT", 0);
    	//pinWrapper.putObject("ISWEBSERVICE", "1");
*/		
		
    	
		
		
		
		requestWrapper.addParameter("PIN_RETRY_COUNT", "0");
		requestWrapper.addParameter("ENCT", "1");
		
		String pin = requestWrapper.getParameter("bankPin");
		requestWrapper.addParameter("KEY_PIN", pin);
		
		
		logger.info("[CheckAllPayBalanceController.onCreate] Start Check Balance of Logged in AppUserID:" + appUserModel.getAppUserId());		
		String pinXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);	
		//String pinXML = this.commandManager.executeCommand(pinWrapper,CommandFieldConstants.CMD_VERIFY_PIN);
		boolean pinXmlHasError = MfsWebUtil.isErrorXML(pinXML);
		System.out.println("Request Wrapper After :  "+requestWrapper);
		ThreadLocalAppUser.setAppUserModel(appUserModel);	
		//String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
		
		//boolean hasError = MfsWebUtil.isErrorXML(responseXML);
		
		//mfsWebResponseDataPopulator.populateMessage(requestWrapper, responseXML);	
		mfsWebResponseDataPopulator.populateMessage(requestWrapper, pinXML);	

		if(/*hasError && */pinXmlHasError) {
			
			modelAndView.addObject(super.getCommand(requestWrapper));
			
			//mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, pinXML);
			
			Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
			
			if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
				
				nextView = getFormView();
				
			} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

				nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
			}
		}
		else{
			String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
			
		}
		
		modelAndView.setViewName(nextView);

		return modelAndView;
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

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}	
	
	
	
	
}