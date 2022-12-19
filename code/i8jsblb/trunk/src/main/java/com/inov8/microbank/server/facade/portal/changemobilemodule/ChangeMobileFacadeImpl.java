package com.inov8.microbank.server.facade.portal.changemobilemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.changemobilemodule.ChangeMobileManager;

public class ChangeMobileFacadeImpl implements ChangeMobileFacade {

	private ChangeMobileManager changeMobileManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper loadChangeMobile(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.changeMobileManager.loadChangeMobile(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper loadChangeMobile(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.changeMobileManager.loadChangeMobile(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}		
	}

	public SearchBaseWrapper searchChangeMobile(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.changeMobileManager.searchChangeMobile(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createChangeMobile(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BaseWrapper updateChangeMobile(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.changeMobileManager
					.updateChangeMobile(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	public void setChangeMobileManager(ChangeMobileManager changeMobileManager) {
		this.changeMobileManager = changeMobileManager;
	}

}
