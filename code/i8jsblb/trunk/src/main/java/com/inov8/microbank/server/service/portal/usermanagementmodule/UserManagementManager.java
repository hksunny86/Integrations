package com.inov8.microbank.server.service.portal.usermanagementmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;

public interface UserManagementManager {
    SearchBaseWrapper searchUsers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    BaseWrapper createNewUser(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper updateUser(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper createNewUserByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper updateUserByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException;
   /* BaseWrapper changePasswordByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper changePasswordBySMS(BaseWrapper baseWrapper) throws FrameworkCheckedException;*/
    BaseWrapper searchUserGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;
	void validateUser(UserManagementModel userManagementModel)throws FrameworkCheckedException;
	UserManagementModel getUserManagementModel(Long appUserId)throws FrameworkCheckedException;
	public void saveAppUserPasswordHistory(AppUserModel appUserModel) throws FrameworkCheckedException;
	/**
	 * Added By Atif Hussain
	 */
	BaseWrapper changePasswordBySMSEmail(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper changeAgentPasswordBySMSEmail(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public  boolean isAppUserInPartnerGroup(Long appUserId, Long partnerGroupId)throws FrameworkCheckedException;
	AppUserModel getAppUserByRetailer(Long retailerContactId) throws FrameworkCheckedException;
	public Boolean isTellerIdUnique(String tellerId, Long appUserId);
	public Boolean isSalesHierarchyUser(Long bankUserId);
}