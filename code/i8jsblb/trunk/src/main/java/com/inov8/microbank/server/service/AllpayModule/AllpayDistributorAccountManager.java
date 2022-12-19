package com.inov8.microbank.server.service.AllpayModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BankModel;

public interface AllpayDistributorAccountManager {
	 SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper) throws
     FrameworkCheckedException;
 public BankModel getOlaBankMadal(); 
 BaseWrapper loadAccount(BaseWrapper baseWrapper) throws
     FrameworkCheckedException;

 SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper) throws
     FrameworkCheckedException;

 ModelAndView updateAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException) throws
     Exception;

 ModelAndView createAccount(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws
     Exception;
Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;
}
