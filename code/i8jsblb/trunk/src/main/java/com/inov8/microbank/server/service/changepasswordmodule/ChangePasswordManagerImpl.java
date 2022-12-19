package com.inov8.microbank.server.service.changepasswordmodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPasswordHistoryModel;
import com.inov8.microbank.common.model.ChangePasswordListViewFormModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class ChangePasswordManagerImpl implements ChangePasswordManager {
	
	private AppUserManager appUserManager;

	public BaseWrapper loadAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 AppUserModel appUserModel= appUserManager.getUser(String
					.valueOf(UserUtils.getCurrentUser().getAppUserId()));
		 
		 appUserModel.setPasswordChangeRequired(false);
		 appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		 appUserModel.setUpdatedOn(new Date());
		 
		 baseWrapper.setBasePersistableModel(appUserModel);
		return baseWrapper;
	}

	public BaseWrapper savePassword(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	
		this.loadAppUser(baseWrapper);
		this.validatePassword(baseWrapper);
		if (null!=baseWrapper)
		{
		appUserManager.saveOrUpdateAppUser(baseWrapper);
		
		 //now save this password in history table
        AppUserPasswordHistoryModel passwordHistory = new AppUserPasswordHistoryModel();
        AppUserModel user = (AppUserModel)baseWrapper.getBasePersistableModel();
        passwordHistory.setAppUserIdAppUserModel(user);
        passwordHistory.setCreatedByAppUserModel(user);
        passwordHistory.setUpdatedByAppUserModel(user);
        passwordHistory.setCreatedOn(new Date());
        passwordHistory.setUpdatedOn(new Date());
        passwordHistory.setPassword(user.getPassword());
//        passwordHistory.setVersionNo(1);
        appUserManager.saveAppUserPasswordHistory(passwordHistory);
		
		}
		
		
		return baseWrapper;
	}

	public BaseWrapper validatePassword(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AppUserModel appUserModel =(AppUserModel)baseWrapper.getBasePersistableModel();
		ChangePasswordListViewFormModel changePasswordListViewFormModel =(ChangePasswordListViewFormModel)
			baseWrapper.getObject("changePasswordListViewFormModel");
		
		if (!appUserModel.getPassword().equals(EncoderUtils.encodeToSha(changePasswordListViewFormModel.getOldPassword())))
		{
			throw new FrameworkCheckedException("IncorrectOldPasswordException");
			
		}
		
		
		
		else if (!changePasswordListViewFormModel.getNewPassword().equals(changePasswordListViewFormModel.getConfirmPassword()))
		{
			
			throw new FrameworkCheckedException("NewandConfirmPasswordException");
		}
		
		else if (appUserModel.getPassword().equals(EncoderUtils.encodeToSha(changePasswordListViewFormModel.getNewPassword())))
		{
			return null;
			
		}
		
		 
		appUserModel.setPassword(EncoderUtils.encodeToSha(changePasswordListViewFormModel.getConfirmPassword()));
		baseWrapper.setBasePersistableModel(appUserModel);
		return baseWrapper;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
