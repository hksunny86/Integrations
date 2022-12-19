package com.inov8.microbank.server.dao.mfsmodule;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */



public interface UserDeviceAccountsDAO extends BaseDAO<UserDeviceAccountsModel, Long> 
{
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserId(long appUserId) throws FrameworkCheckedException;
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserIdAndDeviceTypeId(long appUserId,Long deviceTypeId) throws FrameworkCheckedException;
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserId(AppUserModel appUserModel) throws FrameworkCheckedException;
	public UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException;
	UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(Long deviceTypeId, Long appUserId);
	UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeIds( Long appUserId, ArrayList<Long> deviceTypeIds);
}
