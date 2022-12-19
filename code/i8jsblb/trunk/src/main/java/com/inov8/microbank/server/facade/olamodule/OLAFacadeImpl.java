package com.inov8.microbank.server.facade.olamodule;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.ola.integration.vo.OLAVO;

public class OLAFacadeImpl implements OLAFacade {

	private OLAManager olaManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public BaseWrapper createAccount(BaseWrapper baseWrapper,HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
		try
	    {
		olaManager.createAccount(baseWrapper,httpServletRequest);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
		return baseWrapper;
	}
	public BaseWrapper loadAccount(BaseWrapper baseWrapper,HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
		try {
			olaManager.loadAccount(baseWrapper, httpServletRequest);
		} catch (Exception e) {		
		
		}
		return baseWrapper;
	}
	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			olaManager.loadAccount(searchBaseWrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchBaseWrapper;
	}
	public  List<OLAVO> makeAccount(SearchBaseWrapper searchBaseWrapper,HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
		List<OLAVO> olaList = new ArrayList<OLAVO>(); 
		try {
			olaList =  olaManager.makeAccount(searchBaseWrapper,httpServletRequest);
			
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return olaList;
		
	}
	public BaseWrapper updateAccount(BaseWrapper baseWrapper,HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
		try {
			olaManager.updateAccount(baseWrapper,httpServletRequest);
		} catch (Exception e) {
			
			throw this.frameworkExceptionTranslator.translate(e,
			          this.frameworkExceptionTranslator.UPDATE_ACTION);
			
		}
		return baseWrapper;
	}
	
	public OLAVO makeTxForQueue(OLAVO olaVO) throws FrameworkCheckedException {
		try {
			olaVO =  olaManager.makeTxForQueue(olaVO);
			
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return olaVO;
		
	}	
	
	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
	public OLAManager getOlaManager() {
		return olaManager;
	}
	public void setOlaManager(OLAManager olaManager) {
		this.olaManager = olaManager;
	}

}
