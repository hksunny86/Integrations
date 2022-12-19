package com.inov8.microbank.server.dao.appuserpartnergroupmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;

public class AppUserPartnerGroupHibernateDAO extends BaseHibernateDAO<AppUserPartnerGroupModel,Long,AppUserPartnerGroupDAO>
implements AppUserPartnerGroupDAO {
	
	public AppUserPartnerGroupModel findByAppUserId(long appUserId) throws FrameworkCheckedException
	{
		AppUserPartnerGroupModel appUserPartnerGroupModel = null;
		List<AppUserPartnerGroupModel> list = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AppUserPartnerGroupModel.class );
        detachedCriteria.add( Restrictions.eq( "relationAppUserIdAppUserModel.appUserId", appUserId) );
        list = getHibernateTemplate().findByCriteria(detachedCriteria );
        if(list != null && list.size() > 0)
        {
        	appUserPartnerGroupModel = list.get(0);
        }
		return appUserPartnerGroupModel;
	}
}
