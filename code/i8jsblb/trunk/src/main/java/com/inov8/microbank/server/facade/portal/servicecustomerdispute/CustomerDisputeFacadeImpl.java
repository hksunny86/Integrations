package com.inov8.microbank.server.facade.portal.servicecustomerdispute;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.servicecustomerdispute.CustomerDisputeManager;

public class CustomerDisputeFacadeImpl implements CustomerDisputeFacade {
	private CustomerDisputeManager customerDisputeManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeManager.createIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public SearchBaseWrapper searchCustomerDisputeTransactionForBillPayment(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeManager
					.searchCustomerDisputeTransactionForBillPayment(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper searchMNOSupplier(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return customerDisputeManager
					.searchMNOSupplier(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

   public BaseWrapper isValidMfsId(BaseWrapper
	           baseWrapper) throws FrameworkCheckedException {
	       try
	       {
	         return customerDisputeManager.isValidMfsId(baseWrapper);
	       }
	       catch (Exception ex)
	       {
	         throw this.frameworkExceptionTranslator.translate(ex,
	             FrameworkExceptionTranslator.
	             FIND_BY_PRIMARY_KEY_ACTION);
	       }
	   }
	
	
	
	public void setCustomerDisputeManager(
			CustomerDisputeManager customerDisputeManager) {
		this.customerDisputeManager = customerDisputeManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
