package com.inov8.microbank.updatecustomername.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.model.BlacklistedCnicsModel;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.updatecustomername.dao.UpdateCustomerNameDAO;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UpdateCustomerNameHibernateDAO extends BaseHibernateDAO<UpdateCustomerNameModel,Long, UpdateCustomerNameDAO>
        implements UpdateCustomerNameDAO  {
    @Override
    public List<UpdateCustomerNameModel> getCustomerNames() {
        DetachedCriteria criteria = DetachedCriteria.forClass(UpdateCustomerNameModel.class);
        List<UpdateCustomerNameModel> list = this.getHibernateTemplate().findByCriteria(criteria);
        return list;

    }

    @Override
    public UpdateCustomerNameModel getCustomer(String cnic) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UpdateCustomerNameModel.class);
        criteria.add(Restrictions.eq("cnic",cnic));
        List<UpdateCustomerNameModel> list = this.getHibernateTemplate().findByCriteria(criteria);
        return list.get(0);
    }

    @Override
    public UpdateCustomerNameModel getCustomerNameAndNameUpdate(String cnic, Boolean accupdate) {
        UpdateCustomerNameModel updateCustomerNameModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(UpdateCustomerNameModel.class);
        criteria.add(Restrictions.eq("cnic",cnic));
        criteria.add(Restrictions.eq("updated",accupdate));
        List<UpdateCustomerNameModel> list = this.getHibernateTemplate().findByCriteria(criteria);
        if(list != null && !list.isEmpty()) {

            updateCustomerNameModel = list.get(0);
        }
        return updateCustomerNameModel;
    }
}
