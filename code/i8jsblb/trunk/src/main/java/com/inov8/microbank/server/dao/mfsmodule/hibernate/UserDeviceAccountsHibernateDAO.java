package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;


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


public class UserDeviceAccountsHibernateDAO 
	extends BaseHibernateDAO<UserDeviceAccountsModel, Long, UserDeviceAccountsDAO> implements UserDeviceAccountsDAO
{
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserId(long appUserId) throws FrameworkCheckedException
	{
		List<UserDeviceAccountsModel> userDeviceAccountsModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserDeviceAccountsModel.class );
        detachedCriteria.add(Restrictions.eq("relationAppUserIdAppUserModel.appUserId", appUserId));
        detachedCriteria.add(Restrictions.eq("relationDeviceTypeIdDeviceTypeModel.deviceTypeId", DeviceTypeConstantsInterface.ALL_PAY));
        userDeviceAccountsModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(userDeviceAccountsModelList != null && userDeviceAccountsModelList.size() > 0)
        {
        	return userDeviceAccountsModelList.get(0);	
        }
		return null;
	}

	@Override
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserIdAndDeviceTypeId(long appUserId, Long deviceTypeId) throws FrameworkCheckedException {
		List<UserDeviceAccountsModel> userDeviceAccountsModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserDeviceAccountsModel.class );
		detachedCriteria.add(Restrictions.eq("relationAppUserIdAppUserModel.appUserId", appUserId));
		detachedCriteria.add(Restrictions.eq("relationDeviceTypeIdDeviceTypeModel.deviceTypeId", deviceTypeId));
		userDeviceAccountsModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
		if(userDeviceAccountsModelList != null && userDeviceAccountsModelList.size() > 0)
		{
			return userDeviceAccountsModelList.get(0);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO#findUserDeviceAccountByAppUserId(com.inov8.microbank.common.model.AppUserModel)
	 */
	public UserDeviceAccountsModel findUserDeviceAccountByAppUserId(AppUserModel appUserModel) throws FrameworkCheckedException {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserDeviceAccountsModel.class);
        detachedCriteria.add(Restrictions.eq("relationAppUserIdAppUserModel.appUserId", appUserModel.getAppUserId()));
        
        List<UserDeviceAccountsModel> userDeviceAccountsModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
        
        if(userDeviceAccountsModelList != null && userDeviceAccountsModelList.size() > 0) {
        	return userDeviceAccountsModelList.get(0);	
        }
		return null;
	}
	
	@Override
	public UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException{

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserDeviceAccountsModel.class);
		detachedCriteria.add(Restrictions.eq("userId", userId));

		List<UserDeviceAccountsModel> userDeviceAccountsModelList = getHibernateTemplate().findByCriteria(detachedCriteria);

		if(userDeviceAccountsModelList != null && userDeviceAccountsModelList.size() > 0) {
			return userDeviceAccountsModelList.get(0);
		}
		return null;
	}
	public UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(Long deviceTypeId, Long appUserId){
		UserDeviceAccountsModel model = null;
		String hql = "from UserDeviceAccountsModel model where model.relationDeviceTypeIdDeviceTypeModel.deviceTypeId = :deviceTypeId and " +
				" model.relationAppUserIdAppUserModel.appUserId = :appUserId ";

		String[] paramNames = {"deviceTypeId", "appUserId"};
		Object[] values = { deviceTypeId, appUserId};

		try {
			List<UserDeviceAccountsModel> userDeviceList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			if(CollectionUtils.isNotEmpty(userDeviceList)){
				model = userDeviceList.get(0);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return model;
	}
	public UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeIds( Long appUserId ,ArrayList<Long> deviceTypeIds){
		UserDeviceAccountsModel model = null;
		String hql = "from UserDeviceAccountsModel model where model.relationDeviceTypeIdDeviceTypeModel.deviceTypeId IN (:deviceTypeIds) and " +
				" model.relationAppUserIdAppUserModel.appUserId = :appUserId ";

		String[] paramNames = {"deviceTypeIds", "appUserId"};
		Object[] values = { deviceTypeIds, appUserId};

		try {
			List<UserDeviceAccountsModel> userDeviceList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			if(CollectionUtils.isNotEmpty(userDeviceList)){
				model = userDeviceList.get(0);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return model;
	}
}
