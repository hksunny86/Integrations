package com.inov8.microbank.server.facade.portal.paymentmodesmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.paymentmodesmodule.ViewPaymentModeManager;

public class ViewPaymentModeFacadeImp implements ViewPaymentModeFacade {

	private ViewPaymentModeManager viewPaymentModesManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper viewAvailablePaymentModeList(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return viewPaymentModesManager
					.viewAvailablePaymentModeList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}		 
	}

	public void setViewPaymentModesManager(
			ViewPaymentModeManager viewPaymentModesManager) {
		this.viewPaymentModesManager = viewPaymentModesManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
