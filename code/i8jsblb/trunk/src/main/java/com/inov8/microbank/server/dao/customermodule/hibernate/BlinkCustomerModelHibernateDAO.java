package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class BlinkCustomerModelHibernateDAO extends
        BaseHibernateDAO<BlinkCustomerModel, Long, BlinkCustomerModelDAO>
        implements BlinkCustomerModelDAO{
    @Override
    public BlinkCustomerModel loadBlinkCustomerModelByMobileAndAccUpdate(String mobileNo, Long accUpdate) {
        BlinkCustomerModel blinkCustomerModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerModel.class);
        criteria.add( Restrictions.eq("mobileNo", mobileNo));
        criteria.add( Restrictions.eq("accUpdate", accUpdate));
        List<BlinkCustomerModel> list = getHibernateTemplate().findByCriteria(criteria);


        if(list != null && !list.isEmpty()) {

            blinkCustomerModel = list.get(0);
        }
        return blinkCustomerModel;
    }

    @Override
    public BlinkCustomerModel loadBlinkCustomerModelByBlinkCustomerId(Long accType) {
        BlinkCustomerModel blinkCustomerModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerModel.class);
        criteria.add( Restrictions.eq("blinkCustomerId", accType));
        List<BlinkCustomerModel> list = getHibernateTemplate().findByCriteria(criteria);


        if(list != null && !list.isEmpty()) {

            blinkCustomerModel = list.get(0);
        }
        return blinkCustomerModel;
    }

    @Override
    public List<BlinkCustomerModel> getAllData() throws FrameworkCheckedException {
        String hql="from BlinkCustomerModel";
        List<BlinkCustomerModel> data = null ;
        try {
            data = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getHibernateTemplate().find(hql);
        return data;
    }
}
