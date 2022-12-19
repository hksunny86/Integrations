package com.inov8.microbank.server.facade.portal.forgotveriflypinmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.server.service.portal.forgotveriflypinmodule.ForgotVeriflyPinManager;

public class ForgotVeriflyPinFacadeImpl implements ForgotVeriflyPinFacade {
	
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private ForgotVeriflyPinManager forgotVeriflyPinManager;
	
	public void setForgotVeriflyPinManager(
			ForgotVeriflyPinManager forgotVeriflyPinManager) {
		this.forgotVeriflyPinManager = forgotVeriflyPinManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public SearchBaseWrapper searchForgotVeriflyPin(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
	       try {
	    	   return forgotVeriflyPinManager.searchForgotVeriflyPin(searchBaseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.
	                    FIND_BY_PRIMARY_KEY_ACTION);
	        }
	}
	

	public BaseWrapper changeVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
	       try {
	    	   return forgotVeriflyPinManager.changeVeriflyPin(baseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.UPDATE_ACTION);
	        }
	}

	public BaseWrapper changeAllPayVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 try {
	    	   return forgotVeriflyPinManager.changeAllPayVeriflyPin(baseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.UPDATE_ACTION);
	        }
	}

	public AppUserModel isRetailerOrDistributor(String appUserId) {
		return this.isRetailerOrDistributor(appUserId);
	}
	

}
