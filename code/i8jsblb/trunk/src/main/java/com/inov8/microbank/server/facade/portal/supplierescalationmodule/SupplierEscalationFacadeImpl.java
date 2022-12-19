package com.inov8.microbank.server.facade.portal.supplierescalationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.supplierescalationmodule.SupplierEscalationManager;

public class SupplierEscalationFacadeImpl implements SupplierEscalationFacade {

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	private SupplierEscalationManager supplierEscalationManager;

	public SearchBaseWrapper searchEscalateToInov8Product(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return supplierEscalationManager
					.searchEscalateToInov8Product(searchBaseWrapper);
		} catch (Exception exp) {
			throw this.frameworkExceptionTranslator.translate(exp,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchEscalateToInov8Service(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return supplierEscalationManager
					.searchEscalateToInov8Service(searchBaseWrapper);
		} catch (Exception exp) {
			throw this.frameworkExceptionTranslator.translate(exp,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setSupplierEscalationManager(
			SupplierEscalationManager supplierEscalationManager) {
		this.supplierEscalationManager = supplierEscalationManager;
	}

}
