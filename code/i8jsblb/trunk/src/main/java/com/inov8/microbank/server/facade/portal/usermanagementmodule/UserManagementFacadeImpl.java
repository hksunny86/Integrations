package com.inov8.microbank.server.facade.portal.usermanagementmodule;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class UserManagementFacadeImpl implements UserManagementFacade {

	private UserManagementManager userManagementManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createNewUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.createNewUser(baseWrapper);
		} catch (Exception ex) {
			if (ex instanceof ConstraintViolationException)
				throw new FrameworkCheckedException(
						"ConstraintViolationException", ex);
			if (ex instanceof DataIntegrityViolationException)
				throw new FrameworkCheckedException(
						"DataIntegrityViolationException", ex);
			else
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public SearchBaseWrapper searchUsers(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.userManagementManager.searchUsers(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.updateUser(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	public BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.searchAppUserByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createNewUserByAdmin(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.createNewUserByAdmin(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateUserByAdmin(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.updateUserByAdmin(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	/*public BaseWrapper changePasswordByAdmin(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.changePasswordByAdmin(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper changePasswordBySMS(BaseWrapper baseWrapper)
		throws FrameworkCheckedException {
		try {
			return userManagementManager.changePasswordBySMS(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}*/

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setUserManagementManager(
			UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}

	public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
		
		Long getAppUserPartnerGroupId;
		try
		    {
		       getAppUserPartnerGroupId = this.userManagementManager.getAppUserPartnerGroupId(appUserId);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          FrameworkExceptionTranslator.FIND_ACTION);
		    }
		    return getAppUserPartnerGroupId;

	}

	public BaseWrapper searchUserGroup(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return userManagementManager.searchUserGroup(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void validateUser(UserManagementModel userManagementModel)
			throws FrameworkCheckedException {
		try {
				userManagementManager.validateUser(userManagementModel);;
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}	
	}

	@Override
	public UserManagementModel getUserManagementModel(Long appUserId) throws FrameworkCheckedException {
		try 
		{
			return userManagementManager.getUserManagementModel(appUserId);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void saveAppUserPasswordHistory(AppUserModel appUserModel)
			throws FrameworkCheckedException {

		try 
		{
			this.userManagementManager.saveAppUserPasswordHistory(appUserModel);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.INSERT_ACTION);
	}
	}
	@Override
	public BaseWrapper changePasswordBySMSEmail(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try 
		{
			return this.userManagementManager.changePasswordBySMSEmail(baseWrapper);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	
	@Override
	public BaseWrapper changeAgentPasswordBySMSEmail(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try 
		{
			return this.userManagementManager.changeAgentPasswordBySMSEmail(baseWrapper);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	
	@Override
	public boolean isAppUserInPartnerGroup(Long appUserId, Long partnerGroupId)
			throws FrameworkCheckedException {
		try 
		{
			return this.userManagementManager.isAppUserInPartnerGroup(appUserId, partnerGroupId);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	@Override
	public AppUserModel getAppUserByRetailer(Long retailerContactId) throws FrameworkCheckedException
	{
		try 
		{
			return this.userManagementManager.getAppUserByRetailer(retailerContactId);
		} 
		catch (Exception ex) 
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public Boolean isTellerIdUnique(String tellerId, Long appUserId) {
		
		return userManagementManager.isTellerIdUnique(tellerId, appUserId);
	}

	@Override
	public Boolean isSalesHierarchyUser(Long bankUserId) {
		// TODO Auto-generated method stub
		return userManagementManager.isSalesHierarchyUser(bankUserId);
	}
}
