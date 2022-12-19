package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.tax.dao.WHTConfigDAO;
import com.inov8.microbank.tax.model.WHTConfigModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class WHTConfigHibernateDAO extends 
    BaseHibernateDAO<WHTConfigModel, Long, WHTConfigDAO>
    implements WHTConfigDAO
{

    public List<WHTConfigModel> loadAllActiveWHTConfigModels()
    {
        Session session=this.getSession();
        Criteria criteria=session.createCriteria(WHTConfigModel.class);
        Criterion isActive= Restrictions.eq("active",true);
        criteria.add(isActive);
        return criteria.list();
    }
}
