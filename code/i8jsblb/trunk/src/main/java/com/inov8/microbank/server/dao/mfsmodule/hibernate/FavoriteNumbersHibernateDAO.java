package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.FavoriteNumbersModel;
import com.inov8.microbank.server.dao.mfsmodule.FavoriteNumbersDAO;

public class FavoriteNumbersHibernateDAO 
	extends BaseHibernateDAO<FavoriteNumbersModel, Long, FavoriteNumbersDAO>
	implements FavoriteNumbersDAO
{
	public void deteleFavoriteNumbers(Long appUserId)
	{
		this.getHibernateTemplate().bulkUpdate("DELETE FROM FavoriteNumbersModel fv WHERE fv.relationAppUserIdAppUserModel.appUserId = "+appUserId);
	}
	
	public Long getLatestSequenceNumber(Long appUserId)
	{
		List fnm = this.getHibernateTemplate().find("select max(fv.sequenceNumber)from FavoriteNumbersModel fv where fv.relationAppUserIdAppUserModel.appUserId="+appUserId);
		if(null == fnm || fnm.isEmpty() || fnm.get(0) == null)
		{
			return 0L;
		}
		else
		{
			Long seqNum = (Long) fnm.get(0);
			return seqNum;
		}
	}
}
