package com.inov8.microbank.server.facade.allpaymodule.reports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayReportsFacadeImpl implements AllPayReportsFacade {

	private AllPayReportsManager allPayReportsManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchRetailerTransactions(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return allPayReportsManager
				.searchRetailerTransactions(searchBaseWrapper);
	}

	public SearchBaseWrapper searchRetailerBillSummaries(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return allPayReportsManager
		.searchRetailerBillSummaries(searchBaseWrapper);
	}

	public AllPayReportsManager getAllPayReportsManager() {
		return allPayReportsManager;
	}

	public void setAllPayReportsManager(
			AllPayReportsManager allPayReportsManager) {
		this.allPayReportsManager = allPayReportsManager;
	}

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public SearchBaseWrapper searchRetailerHeadTransactions(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return allPayReportsManager
		.searchRetailerHeadTransactions(searchBaseWrapper);
	}

	public SearchBaseWrapper searchDistributorHeadTransactions(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return allPayReportsManager
		.searchDistributorHeadTransactions(searchBaseWrapper);
	}

	public SearchBaseWrapper searchDistHeadSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return allPayReportsManager
		.searchDistHeadSummary(searchBaseWrapper);
	}

	public SearchBaseWrapper searchRegionalHeadSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return allPayReportsManager
		.searchRegionalHeadSummary(searchBaseWrapper);
	}

	public SearchBaseWrapper searchRetSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return allPayReportsManager
		.searchRetSummary(searchBaseWrapper);
	}

}
