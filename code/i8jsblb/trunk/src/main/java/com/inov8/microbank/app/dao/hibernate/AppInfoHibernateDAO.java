package com.inov8.microbank.app.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.app.dao.AppInfoDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.app.vo.AppInfoVO;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import java.util.List;

public class AppInfoHibernateDAO
	extends BaseHibernateDAO<AppInfoModel, Long, AppInfoDAO>
	implements AppInfoDAO {

	@Override
	public List<AppInfoVO> loadAppInfoList(Long appUserTypeId) {

		StringBuilder  queryStr = new StringBuilder();
		queryStr.append("select a.appId as appId, a.appName as appName, a.appUserTypeModel.appUserTypeId as appUserTypeId, ai.url as url, ai.osType as osType ");
		queryStr.append("from AppModel a, AppInfoModel ai ");
		queryStr.append("where a.appUserTypeModel.appUserTypeId =:appUserTypeId ");
		queryStr.append("and a.active = 1 ");
		queryStr.append("and a.appId = ai.appModel.appId ");
		queryStr.append(" order by ai.appInfoId asc");

		try {
			Query query = getSession().createQuery(queryStr.toString());
			query.setParameter("appUserTypeId", appUserTypeId);

			query.setResultTransformer(Transformers.aliasToBean(AppInfoVO.class));
			return query.list();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
