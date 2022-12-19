package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class MainMenuController extends AdvanceFormController{
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	
	public MainMenuController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		String responseXML = mfsWebController.handleRequest(request, CommandFieldConstants.CMD_GET_LATEST_CATALOG);
		mfsWebResponseDataPopulator.populateAllPayServicesAndProducts(request, responseXML);
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
//		AllPayWebResponseDataPopulator.setMainMenuData(request);
		
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return null;
	}

	private boolean  validCredentials(String username, String password) {		
		if (username.trim().length()!= 12){
			return false;
		}
		if (username.charAt(0) == '-'){
			return false;
		}
		if (username.indexOf('.') != -1){
			return false;
		}
		try{
			Long.valueOf(username);
//			Long.valueOf(password);
		}catch(NumberFormatException nfe){
			return false;
		}
		
		
		return true;
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
