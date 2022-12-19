package com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AgentSegmentRestrictionHibernateDAO extends BaseHibernateDAO<AgentSegmentRestriction, Long, AgentSegmentRestrictionDAO> implements AgentSegmentRestrictionDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean checkAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AgentSegmentRestriction agentSegmentRestriction = new AgentSegmentRestriction();
        agentSegmentRestriction = (AgentSegmentRestriction) baseWrapper.getBasePersistableModel();
        DetachedCriteria criteria = DetachedCriteria.forClass(AgentSegmentRestriction.class);
        Criterion criterionOne = Restrictions.eq("agentID", agentSegmentRestriction.getAgentID());
        Criterion criterionTwo = Restrictions.eq("segmentId", agentSegmentRestriction.getSegmentId());
        Criterion criterionThree = Restrictions.eq("productId", agentSegmentRestriction.getProductId());
        Criterion criterionFour = Restrictions.eq("isActive", agentSegmentRestriction.getIsActive());
        criteria.add(criterionOne);
        criteria.add(criterionTwo);
        criteria.add(criterionThree);
        criteria.add(criterionFour);
        List<AgentSegmentRestriction> list = getHibernateTemplate().findByCriteria(criteria);
        AgentSegmentRestriction appUserModel = null;
        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);

        }
        if (appUserModel != null) {
            return true;
        } else {
            return false;
        }

    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
