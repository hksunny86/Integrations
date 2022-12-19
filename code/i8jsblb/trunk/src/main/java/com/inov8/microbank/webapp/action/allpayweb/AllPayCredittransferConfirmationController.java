package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.AllPayWebUtil;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class AllPayCredittransferConfirmationController extends AdvanceFormController{
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	
	public AllPayCredittransferConfirmationController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
//		System.out.println("hello");
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception 
	{
		
		String responseXML = "";
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		if(null != appUserModel && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.DISTRIBUTOR.longValue())
		{
			responseXML = mfsWebController.handleRequest(request, CommandFieldConstants.CMD_D2D_TRANS,false);
		}
		else
		{
			responseXML = mfsWebController.handleRequest(request, CommandFieldConstants.CMD_R2R_TRANS,false);
		}
		
		if( AllPayWebUtil.isErrorXML(responseXML) )
		{
			mfsWebResponseDataPopulator.populateErrorMessages(request, responseXML);
			return new ModelAndView(this.getFormView());
		}				
		else
		{
			mfsWebResponseDataPopulator.populateAllPayTransactionSummary(request, responseXML);
			return new ModelAndView(this.getSuccessView());
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
	public void setMfsWebResponseDataPopulator(
			MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}

	

}
