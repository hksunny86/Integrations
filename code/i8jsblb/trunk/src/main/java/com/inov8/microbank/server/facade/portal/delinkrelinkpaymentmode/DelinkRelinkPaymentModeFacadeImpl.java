package com.inov8.microbank.server.facade.portal.delinkrelinkpaymentmode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeManager;


public class DelinkRelinkPaymentModeFacadeImpl implements DelinkRelinkPaymentModeFacade {
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager;

	public SearchBaseWrapper searchDelinkRelinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
	       try {
	    	   return delinkRelinkPaymentModeManager.searchDelinkRelinkPaymentMode(searchBaseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.
	                    FIND_BY_PRIMARY_KEY_ACTION);
	        }
	}

	public BaseWrapper updateDelinkRelinkVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
	       try {
	    	   return this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.UPDATE_ACTION);
	        }
	}	

	public BaseWrapper deleteAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
	       try {
	    	   return this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.UPDATE_ACTION);
	        }
	}		
	
	public void setDelinkRelinkPaymentModeManager(
			DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager) {
		this.delinkRelinkPaymentModeManager = delinkRelinkPaymentModeManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BaseWrapper updateAllPayDelinkRelinkVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {
//		 TODO Auto-generated method stub
	       try {
	    	   return this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
	        } catch (Exception ex) {
	            throw this.frameworkExceptionTranslator.translate(ex,
	                    FrameworkExceptionTranslator.UPDATE_ACTION);
	        }
	}

	public boolean isRetailerOrDistributor(Long appUserId) {
		return this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(appUserId);
	}

	public BaseWrapper allpayDeleteAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {
		return this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
	}

	

}
