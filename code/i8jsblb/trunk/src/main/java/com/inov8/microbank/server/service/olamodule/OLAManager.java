package com.inov8.microbank.server.service.olamodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestBindingException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.ola.integration.vo.OLAVO;

public interface OLAManager {
	 SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper) throws
     FrameworkCheckedException;

 BaseWrapper loadAccount(BaseWrapper baseWrapper,HttpServletRequest httpServletRequest) throws
     FrameworkCheckedException;

 List<OLAVO> makeAccount(SearchBaseWrapper searchBaseWrapper,HttpServletRequest httpServletRequest) throws
     FrameworkCheckedException, ServletRequestBindingException;

 BaseWrapper updateAccount(BaseWrapper baseWrapper, HttpServletRequest httpServleRequest) throws
     FrameworkCheckedException, ServletRequestBindingException;

 BaseWrapper createAccount(BaseWrapper baseWrapper, HttpServletRequest httpServletRequest) throws
     FrameworkCheckedException, WorkFlowException, Exception;

 OLAVO makeTxForQueue(OLAVO olaVO) throws FrameworkCheckedException, WorkFlowException, Exception;
}
