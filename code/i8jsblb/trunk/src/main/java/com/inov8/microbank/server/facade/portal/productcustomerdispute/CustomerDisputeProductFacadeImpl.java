package com.inov8.microbank.server.facade.portal.productcustomerdispute;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.productcustomerdispute.CustomerDisputeProductManager;

public class CustomerDisputeProductFacadeImpl implements
		CustomerDisputeProductFacade {

	private CustomerDisputeProductManager customerDisputeProductManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeProductManager.createIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public SearchBaseWrapper searchCustomerDisputeTransactionForVariableDiscrete(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeProductManager
					.searchCustomerDisputeTransactionForVariableDiscrete(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper searchMNOSupplier(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeProductManager
					.searchMNOSupplier(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setCustomerDisputeProductManager(
			CustomerDisputeProductManager customerDisputeProductManager) {
		this.customerDisputeProductManager = customerDisputeProductManager;
	}

}
