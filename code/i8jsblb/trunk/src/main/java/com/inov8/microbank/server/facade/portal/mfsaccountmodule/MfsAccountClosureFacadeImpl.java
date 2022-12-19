package com.inov8.microbank.server.facade.portal.mfsaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountClosureManager;

public class MfsAccountClosureFacadeImpl implements MfsAccountClosureFacade{

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private MfsAccountClosureManager mfsAccountClosureManager;
	
	public BaseWrapper makeCustomerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			return mfsAccountClosureManager.makeCustomerAccountClosed(baseWrapper);
		}catch(Exception exp){
			if(((FrameworkCheckedException) exp).getErrorCode()==151){
				throw new FrameworkCheckedException("Balance not Zero",null,151,null);
			}
			else
				throw this.frameworkExceptionTranslator.translate(exp,
			          FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	
	public BaseWrapper makeAgentAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			return mfsAccountClosureManager.makeAgentAccountClosed(baseWrapper);
		}catch(Exception exp){
			throw this.frameworkExceptionTranslator.translate(exp,
			          FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setMfsAccountClosureManager(MfsAccountClosureManager mfsAccountClosureManager) {
		this.mfsAccountClosureManager = mfsAccountClosureManager;
	}

	@Override
	public BaseWrapper makeHandlerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		return this.mfsAccountClosureManager.makeHandlerAccountClosed(baseWrapper);
	}

	@Override
	public Double checkAgentBalance(AppUserModel appUserModel)
			throws WorkFlowException, FrameworkCheckedException, Exception {
		
		return this.mfsAccountClosureManager.checkAgentBalance(appUserModel);
	}
	
}
