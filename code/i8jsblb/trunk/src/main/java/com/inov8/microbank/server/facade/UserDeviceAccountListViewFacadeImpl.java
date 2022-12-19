package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;


public class UserDeviceAccountListViewFacadeImpl implements UserDeviceAccountListViewFacade {

	
	private UserDeviceAccountListViewManager userDeviceAccountListViewManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	
	public SearchBaseWrapper searchUserDeviceAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      
			this.userDeviceAccountListViewManager.searchUserDeviceAccount(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}
	
	public BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.userDeviceAccountListViewManager.loadUserDeviceAccount(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}
	
	public BaseWrapper updateUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			baseWrapper = this.userDeviceAccountListViewManager.updateUserDeviceAccount(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.UPDATE_ACTION);
	    	
	    }
	    return baseWrapper;
	}
	
	public BaseWrapper createUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
			baseWrapper = this.userDeviceAccountListViewManager.createUserDeviceAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.UPDATE_ACTION);
	    }
	    return baseWrapper;
	}
	
	public void sendSMS( String mfsId, String randomPin, String mobileNo )
	{
		this.userDeviceAccountListViewManager.sendSMS(mfsId, randomPin, mobileNo);
	}

	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

	public BaseWrapper updateUserDeviceAccountStatus(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			baseWrapper = this.userDeviceAccountListViewManager.updateUserDeviceAccountStatus(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.UPDATE_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	@Override
	public UserDeviceAccountsModel findUserDeviceByAppUserId(Long appUserId) throws FrameworkCheckedException{
		try
	    {
	      
			return this.userDeviceAccountListViewManager.findUserDeviceByAppUserId(appUserId);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	}

	@Override
	public BaseWrapper changeUserDeviceAccountPin(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			baseWrapper = this.userDeviceAccountListViewManager.changeUserDeviceAccountPin(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.UPDATE_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	@Override
	public BaseWrapper changeUserDeviceAccountForgotPin(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
		{

			baseWrapper = this.userDeviceAccountListViewManager.changeUserDeviceAccountForgotPin(baseWrapper);


		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.UPDATE_ACTION);

		}
		return baseWrapper;
	}
	


}
