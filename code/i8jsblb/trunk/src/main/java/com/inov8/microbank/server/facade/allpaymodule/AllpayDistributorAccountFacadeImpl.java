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
import com.inov8.microbank.server.service.AllpayModule.AllpayDistributorAccountManager;

public class AllpayDistributorAccountFacadeImpl implements
		AllpayDistributorAccountFacade {
	private AllpayDistributorAccountManager allpayDistributorAccountManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public ModelAndView createAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws FrameworkCheckedException {
		ModelAndView mav = null;
		try{
			mav =  allpayDistributorAccountManager.createAccount(httpServletRequest,httpServletResponse,object,bindException);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
		return mav;
	}

	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try{
		allpayDistributorAccountManager.loadAccount(searchBaseWrapper);
			} catch (Exception e) {
				throw this.frameworkExceptionTranslator.translate(e,
				          this.frameworkExceptionTranslator.FIND_ACTION);		
			}
		return searchBaseWrapper;
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try{
		 allpayDistributorAccountManager.loadAccount(baseWrapper);
			} catch (Exception e) {
				throw this.frameworkExceptionTranslator.translate(e,
				          this.frameworkExceptionTranslator.FIND_ACTION);		
			}
		 return baseWrapper;

	}

	public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try{
		allpayDistributorAccountManager.searchAccount(searchBaseWrapper);
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
			 mav = allpayDistributorAccountManager.updateAccount(httpServletRequest,httpServletResponse,object,bindException);
		} catch (Exception e) {
			throw this.frameworkExceptionTranslator.translate(e,
			          this.frameworkExceptionTranslator.UPDATE_ACTION);		
		}
		 return mav;
	}

	public AllpayDistributorAccountManager getAllpayDistributorAccountManager() {
		return allpayDistributorAccountManager;
	}

	public void setAllpayDistributorAccountManager(
			AllpayDistributorAccountManager allpayDistributorAccountManager) {
		this.allpayDistributorAccountManager = allpayDistributorAccountManager;
	}

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BankModel getOlaBankMadal() {
		return this.allpayDistributorAccountManager.getOlaBankMadal();
	}

	public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
		return this.allpayDistributorAccountManager.getAppUserPartnerGroupId(appUserId);
	}

}
