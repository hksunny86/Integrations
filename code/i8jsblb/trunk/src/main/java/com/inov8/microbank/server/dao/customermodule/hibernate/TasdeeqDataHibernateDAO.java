package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.TasdeeqDataModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.customermodule.TasdeeqDataDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class TasdeeqDataHibernateDAO  extends
        BaseHibernateDAO<TasdeeqDataModel, Long, TasdeeqDataDAO>
        implements
        TasdeeqDataDAO{
    @Override
    public TasdeeqDataModel loadTasdeeqDataByMobile(String mobileNo) {
        TasdeeqDataModel tasdeeqDataModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(TasdeeqDataModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.eq("validStatus", "1"));
//		criteria.add(expressionCriterion);
        List<TasdeeqDataModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {
            tasdeeqDataModel = list.get(0);
        }
        return tasdeeqDataModel;
    }
}
