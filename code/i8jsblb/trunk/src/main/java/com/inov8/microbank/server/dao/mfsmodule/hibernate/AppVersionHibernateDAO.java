package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.app.vo.AppVO;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.common.util.AppConstants;
import com.inov8.microbank.server.dao.mfsmodule.AppVersionDAO;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AppVersionHibernateDAO 
	extends BaseHibernateDAO<AppVersionModel, Long, AppVersionDAO>
	implements AppVersionDAO
{

	@Override
	public AppVO loadLatestAppVersion(AppVO appVO) {

		StringBuilder  queryStr = new StringBuilder();
		queryStr.append("select a.appId as appId, a.appName as appName, a.appUserTypeModel.appUserTypeId as appUserTypeId, ai.url as url, ai.osType as osType, " +
				"av.appVersionNumber as appVersion, av.fromCompatibleVersion as fromCompatibleVersion, av.toCompatibleVersion as toCompatibleVersion ");
		queryStr.append("from AppVersionModel av, AppModel a, AppInfoModel ai ");
		queryStr.append("where av.appModel.appId = a.appId ");
		queryStr.append("and a.appId = ai.appModel.appId ");
		queryStr.append("and a.active = 1 ");
		queryStr.append("and av.active = 1 ");
		queryStr.append("and av.blackListed = 0 ");
		queryStr.append("and a.appId =:appId ");
		queryStr.append("and ai.osType =:osType ");
		queryStr.append(" order by av.releaseDate desc");

		try {
			Query query = getSession().createQuery(queryStr.toString());
			query.setParameter("appId", appVO.getAppId());
			query.setParameter("osType", appVO.getOsType());

			query.setResultTransformer(Transformers.aliasToBean(AppVO.class));
			List<AppVO> resultList = query.list();

			if(!CollectionUtils.isEmpty(resultList)) {
				return resultList.get(0);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return appVO;
	}

}
