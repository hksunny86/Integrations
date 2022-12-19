package com.inov8.microbank.server.facade.portal.escalateinov8;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.escalateinov8module.EscalateInov8Manager;

public class EscalateInov8FacadeImpl implements EscalateInov8Facade {
	private EscalateInov8Manager escalateInov8Manager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchEscalateInov8Status(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		try {
			return escalateInov8Manager
					.searchEscalateInov8Status(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setEscalateInov8Manager(
			EscalateInov8Manager escalateInov8Manager) {
		this.escalateInov8Manager = escalateInov8Manager;
	}

	public BaseWrapper retrieveI8EscalateForm(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return escalateInov8Manager.retrieveI8EscalateForm(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper makeResolveDispute(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.escalateInov8Manager.makeResolveDispute(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}

	}

}
