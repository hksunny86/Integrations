package com.inov8.microbank.server.facade.portal.mnosearchusermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.mnosearchusermodule.MnoSearchMfsUserManager;

public class MnoSearchMfsUserFacadeImpl implements MnoSearchMfsUserFacade{
	
	private MnoSearchMfsUserManager mnoSearchMfsUserManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public void setMnoSearchMfsUserManager(
			MnoSearchMfsUserManager mnoSearchMfsUserManager) {
		this.mnoSearchMfsUserManager = mnoSearchMfsUserManager;
	}

	public SearchBaseWrapper searchMfsUser(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.mnoSearchMfsUserManager.searchMfsUser(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
