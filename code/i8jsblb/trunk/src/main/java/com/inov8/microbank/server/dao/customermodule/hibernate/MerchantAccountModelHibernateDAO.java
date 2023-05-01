package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.MerchantAccountModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.MerchantAccountModelDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MerchantAccountModelHibernateDAO extends
        BaseHibernateDAO<MerchantAccountModel, Long, MerchantAccountModelDAO>
        implements MerchantAccountModelDAO{


    @Override
    public MerchantAccountModel loadMerchantCustomerModelByMobileAndAccUpdate(String mobileNo, Long accUpdate) {
        MerchantAccountModel merchantAccountModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(MerchantAccountModel.class);
        criteria.add( Restrictions.eq("mobileNo", mobileNo));
        criteria.add( Restrictions.eq("accUpdate", accUpdate));
        List<MerchantAccountModel> list = getHibernateTemplate().findByCriteria(criteria);


        if(list != null && !list.isEmpty()) {

            merchantAccountModel = list.get(0);
        }
        return merchantAccountModel;
    }

    @Override
    public MerchantAccountModel loadMerchantModelByBlinkCustomerId(Long accType) {
        MerchantAccountModel blinkCustomerModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(MerchantAccountModel.class);
        criteria.add( Restrictions.eq("merchanAccountId", accType));
        List<MerchantAccountModel> list = getHibernateTemplate().findByCriteria(criteria);


        if(list != null && !list.isEmpty()) {

            blinkCustomerModel = list.get(0);
        }
        return blinkCustomerModel;
    }
}
