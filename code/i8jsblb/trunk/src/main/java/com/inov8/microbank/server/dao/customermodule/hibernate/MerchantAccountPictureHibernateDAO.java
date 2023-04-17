package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MerchantAccountModel;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerPictureDAO;
import com.inov8.microbank.server.dao.customermodule.MerchantAccountModelDAO;
import com.inov8.microbank.server.dao.customermodule.MerchantAccountPictureDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MerchantAccountPictureHibernateDAO extends
        BaseHibernateDAO<MerchantAccountPictureModel, Long, MerchantAccountPictureDAO>
        implements MerchantAccountPictureDAO {

    @Override
    public MerchantAccountPictureModel getMerchantAccountPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        MerchantAccountPictureModel customerPictureModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(MerchantAccountPictureModel.class);

        criteria.add( Restrictions.eq("relationPictureTypeModel.pictureTypeId", pictureTypeId) );
        criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
        //criteria.add( Restrictions.ne("discrepant", Boolean.TRUE));
        List<MerchantAccountPictureModel> list = getHibernateTemplate().findByCriteria(criteria);

        if(list != null && !list.isEmpty()) {

            customerPictureModel = list.get(0);
        }

        return customerPictureModel;

    }
}
