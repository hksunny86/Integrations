package com.inov8.microbank.server.facade.portal.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.sales.SalesManager;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */
public class SalesFacadeImpl implements SalesFacade {

	private SalesManager salesManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchSales(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return salesManager.searchSales(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return salesManager.createOrUpdateTransaction(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BaseWrapper loadTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return salesManager.loadTransaction(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public SearchBaseWrapper getSupplierProcessingStatus(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return salesManager.getSupplierProcessingStatus(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper updateTransactionSupStatus(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return salesManager.updateTransactionSupStatus(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public void setSalesManager(SalesManager salesManager) {
		this.salesManager = salesManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
