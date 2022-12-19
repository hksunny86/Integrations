package com.inov8.microbank.server.facade.portal.paymentsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.paymentsmodule.PaymentsManager;

public class PaymentsFacadeImpl implements PaymentsFacade {

	PaymentsManager paymentsManager;

	FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchPayments(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		try {
			return paymentsManager.searchPayments(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public void setPaymentsManager(PaymentsManager paymentsManager) {
		this.paymentsManager = paymentsManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
