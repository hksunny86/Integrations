package com.inov8.microbank.server.dao.portal.concernmodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ConcernModel;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernDAO;

public class ConcernHibernateDAO extends BaseHibernateDAO<ConcernModel, Long,ConcernDAO>
implements ConcernDAO{

	public String PRIMARY_CONCERN_RECORD = "FROM ConcernModel c WHERE c.concernCode = ? "+
	    " AND c.relationParentConcernIdConcernModel.concernId IS NULL";

	public String SECONDARY_CONCERN_RECORD = "FROM ConcernModel c WHERE c.concernCode = ? "+
    " AND c.relationParentConcernIdConcernModel.concernId IS NOT NULL";	
	
	public String ACTIVE_INDIRECT_PARTNER = "FROM ConcernModel c WHERE c.concernCode = ? "+
    " AND c.active = 1 AND c.relationParentConcernIdConcernModel.concernId IS NOT NULL";
	
	public ConcernModel findPrimaryConcernRecord(String concernCode){
		List<ConcernModel> list = this.getHibernateTemplate().find(PRIMARY_CONCERN_RECORD, concernCode);
		return list.get(0);
	}

	public List findSecondaryConcernRecords(String concernCode){
		List<ConcernModel> list = this.getHibernateTemplate().find(SECONDARY_CONCERN_RECORD, concernCode);
		return list;
	}	
	
	public ConcernModel findActiveIndirectPartner(String concernCode){
		List<ConcernModel> list = this.getHibernateTemplate().find(ACTIVE_INDIRECT_PARTNER, concernCode);
		ConcernModel concernModel = null;
		if(!list.isEmpty()){
			concernModel = list.get(0);
		}
		return concernModel;
	}	

}
