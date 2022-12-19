package com.inov8.microbank.server.dao.ads.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AdsModel;
import com.inov8.microbank.server.dao.ads.AdsDAO;

public class AdsHibernateDAO extends BaseHibernateDAO<AdsModel, Long, AdsDAO> implements AdsDAO {
	/*
    public AdsModel findAdsById(long AdsId) throws FrameworkCheckedException {
		List<AdsModel> AdsModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AdsModel.class );
        detachedCriteria.add(Restrictions.eq("AdsId", AdsId));
        AdsModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
		return AdsModelList.get(0);
	}
	
	public AdsModel getAds(Long AdsId){
		String hql = "from AdsModel where AdsId = ?";
		List<AdsModel> list = this.getHibernateTemplate().find(hql,AdsId);
		return list.get(0);
	    
	  }
	*/
}
