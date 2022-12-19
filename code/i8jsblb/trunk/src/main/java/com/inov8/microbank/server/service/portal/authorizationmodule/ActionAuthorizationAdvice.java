package com.inov8.microbank.server.service.portal.authorizationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.UserUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.ServletRequestUtils;

import java.lang.reflect.Array;

@Aspect
public class ActionAuthorizationAdvice {
	
	private ActionAuthorizationManager actionAuthorizationManager;

	public ActionAuthorizationAdvice() {
	}

	@Around("execution(* *WithAuthorization(..)) && within(com.inov8..*.service..*)")
	public BaseWrapper doAuthorization(ProceedingJoinPoint proceedingJoinPoint) throws FrameworkCheckedException{

	Object[] agrs = proceedingJoinPoint.getArgs();
	BaseWrapper baseWrapper = (BaseWrapper) Array.get(agrs, 0) ;
	
	if (!actionAuthorizationManager.performAuthorization(baseWrapper))
		return baseWrapper;
	try 
	{
		 baseWrapper = (BaseWrapper) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
	} catch (Throwable e) {
		e.printStackTrace();
		throw new FrameworkCheckedException(e.getMessage(),e);
	}
	return baseWrapper;
	}

	public void setActionAuthorizationManager(
			ActionAuthorizationManager actionAuthorizationManager) {
		this.actionAuthorizationManager = actionAuthorizationManager;
	}
}
