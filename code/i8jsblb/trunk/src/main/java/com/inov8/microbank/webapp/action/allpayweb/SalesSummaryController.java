package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class SalesSummaryController extends AdvanceFormController{
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	
	public SalesSummaryController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

//		System.out.println("asfs");
		
		if(null != request)
		{
			request.setAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB);
		}
		
		String responseXML = mfsWebController.handleRequest(request, CommandFieldConstants.CMD_ALLPAY_SALES_SUMMARY);
		
		if( MfsWebUtil.isErrorXML(responseXML) )
		{
			mfsWebResponseDataPopulator.populateErrorMessages(request, responseXML);			
		}
		mfsWebResponseDataPopulator.populateAllPaySalesSummaryData(request, responseXML);
		
		return new Object ();	

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {	
		return new ModelAndView (this.getSuccessView());
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
