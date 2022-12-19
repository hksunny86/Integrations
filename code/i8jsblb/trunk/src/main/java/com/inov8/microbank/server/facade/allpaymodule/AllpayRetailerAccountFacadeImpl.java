package com.inov8.microbank.server.facade.allpaymodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;

public class AllpayRetailerAccountFacadeImpl implements
		AllpayRetailerAccountManager {
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public ModelAndView createAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws FrameworkCheckedException {
		ModelAndView mav = null;
		try{
			mav =  allpayRetailerAccountManager.createAccount(httpServletRequest,httpServletResponse,object,bindException);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
		return mav;
	}

	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try{
		allpayRetailerAccountManager.searchAccount(searchBaseWrapper);
			} catch (Exception e) {
				throw this.frameworkExceptionTranslator.translate(e,
				          this.frameworkExceptionTranslator.FIND_ACTION);		
			}
		return searchBaseWrapper;
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try{
		 allpayRetailerAccountManager.loadAccount(baseWrapper);
			} catch (Exception e) {
				throw this.frameworkExceptionTranslator.translate(e,
				          this.frameworkExceptionTranslator.FIND_ACTION);		
			}
		 return baseWrapper;

	}

	public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try{
		allpayRetailerAccountManager.searchAccount(searchBaseWrapper);
			} catch (Exception e) {
				throw this.frameworkExceptionTranslator.translate(e,
				          this.frameworkExceptionTranslator.FIND_ACTION);		
			}
		return searchBaseWrapper;
		
	}

	public ModelAndView updateAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws FrameworkCheckedException {
		ModelAndView mav = null;
		 try {
			 mav = allpayRetailerAccountManager.updateAccount(httpServletRequest,httpServletResponse,object,bindException);
		} catch (Exception e) {
			throw this.frameworkExceptionTranslator.translate(e,
			          this.frameworkExceptionTranslator.UPDATE_ACTION);		
		}
		 return mav;
	}

	public AllpayRetailerAccountManager getAllpayRetailerAccountManager() {
		return allpayRetailerAccountManager;
	}

	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BankModel getOlaBankMadal() {
	return this.allpayRetailerAccountManager.getOlaBankMadal();
	}

	public UserDeviceAccountsModel getUserDeviceAccountsModel(String appUserId)
			throws FrameworkCheckedException {
		try{
			return	allpayRetailerAccountManager.getUserDeviceAccountsModel(appUserId);
		} catch (Exception e) {
			throw this.frameworkExceptionTranslator.translate(e,this.frameworkExceptionTranslator.FIND_ACTION);		
		}
	}

}
