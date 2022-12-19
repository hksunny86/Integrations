package com.inov8.microbank.server.facade.portal.linkpaymentmodemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;

public class LinkPaymentModeFacadeImpl implements LinkPaymentModeFacade {
	private LinkPaymentModeManager linkPaymentModeManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createLinkPaymentMode(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return linkPaymentModeManager.createLinkPaymentMode(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public SearchBaseWrapper loadLinkPaymentMode(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public BaseWrapper loadLinkPaymentMode(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchBaseWrapper searchLinkPaymentMode(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public BaseWrapper updateLinkPaymentMode(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return linkPaymentModeManager.searchAccounts(searchBaseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BaseWrapper changeAccountNick(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return linkPaymentModeManager.changeAccountNick(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BaseWrapper loadUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return linkPaymentModeManager.loadUserInfoListViewByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper loadUserInfoListViewByMfsId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return linkPaymentModeManager.loadUserInfoListViewByMfsId(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper getSmartMoneyAccountInfo(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return linkPaymentModeManager.getSmartMoneyAccountInfo(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}
	
	public  boolean isFirstSmartMoneyAccount(Long customerId,Long custType){
		try {
			return linkPaymentModeManager.isFirstSmartMoneyAccount(customerId,custType);
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}
		
		return false;
	}

	public boolean isFirstAccountOtherThanOla(String allpayId) {
		
			return linkPaymentModeManager.isFirstAccountOtherThanOla(allpayId);
		
	}

	public BaseWrapper loadUserInfoListViewByAllPayId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return linkPaymentModeManager.loadUserInfoListViewByAllPayId(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
		
	public BaseWrapper loadAllPayUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return linkPaymentModeManager.loadAllPayUserInfoListViewByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper createLinkPaymentModeForBulk(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}
		
	@Override
	public BaseWrapper createLinkPaymentModeForL3(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return linkPaymentModeManager.createLinkPaymentModeForL3(baseWrapper);
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}
}
