package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TickerModel;
import com.inov8.microbank.server.dao.mfsmodule.TickerDAO;

public class TickerHibernateDAO 
	extends BaseHibernateDAO<TickerModel, Long, TickerDAO>
	implements TickerDAO
{

	public BaseWrapper loadDefaultTicker()
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		DetachedCriteria nullCriteria = this.createDetachedCriteria(
	            Restrictions.isNull("relationAppUserIdAppUserModel"));
		List list = this.getHibernateTemplate().findByCriteria(nullCriteria);
		if(null != list && list.size() > 0)
		{			
			TickerModel defaultTickerModel = (TickerModel)list.get(0);
			baseWrapper.setBasePersistableModel(defaultTickerModel);
		}
		return baseWrapper;
	}

}
