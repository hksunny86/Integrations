package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.changepasswordmodule.ChangePasswordManager;

public class ChangePasswordFacadeImpl implements ChangePasswordFacade {

	private ChangePasswordManager changePasswordManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setChangePasswordManager(ChangePasswordManager changePasswordManager) {
		this.changePasswordManager = changePasswordManager;
	}

	public BaseWrapper loadAppUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		 try
		    {
		      this.changePasswordManager.loadAppUser(baseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          this.frameworkExceptionTranslator.
		          FIND_BY_PRIMARY_KEY_ACTION);
		    }
		    return baseWrapper;
	}

	public BaseWrapper savePassword(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub]
		 try
		    {
		      this.changePasswordManager.savePassword(baseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          this.frameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		    }
		    return baseWrapper;

	}

	public BaseWrapper validatePassword(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try
	    {
	      this.changePasswordManager.validatePassword(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return baseWrapper;
	}

}
