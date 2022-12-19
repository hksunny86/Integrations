package com.inov8.microbank.server.facade.reportsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.reportsmodule.LinkedAccountsReportManager;

public class LinkedAccountsReportFacadeImpl implements
		LinkedAccountsReportFacade {
	private LinkedAccountsReportManager linkedAccountsReportManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
	    try
	    {
	      this.linkedAccountsReportManager.searchAccounts(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	  }
	public LinkedAccountsReportManager getLinkedAccountsReportManager() {
		return linkedAccountsReportManager;
	}
	public void setLinkedAccountsReportManager(
			LinkedAccountsReportManager linkedAccountsReportManager) {
		this.linkedAccountsReportManager = linkedAccountsReportManager;
	}
	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
