/**
 * 
 */
package com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AgentMerchantDetailModel;
import com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule.AgentMerchantDetailDAO;

/**
 * @author basit.mehr
 *
 */
public class AgentMerchantDetailHibernateDAO 
extends BaseHibernateDAO<AgentMerchantDetailModel, Long,AgentMerchantDetailDAO>
implements AgentMerchantDetailDAO
{
	@Override 
	public List<AgentMerchantDetailModel> getAgentMerchantDetailModel(AgentMerchantDetailModel agentMerchantDetailModel) {
		
		Session session = super.getSession();
		
		Criteria criteria = session.createCriteria(AgentMerchantDetailModel.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Criterion criterionInitialAppFormNo = Restrictions.eq("initialAppFormNo", agentMerchantDetailModel.getInitialAppFormNo());
		
		criteria.add(criterionInitialAppFormNo);
		
		List<AgentMerchantDetailModel> list = criteria.list();

		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		
		return list;		
	}
	
}
