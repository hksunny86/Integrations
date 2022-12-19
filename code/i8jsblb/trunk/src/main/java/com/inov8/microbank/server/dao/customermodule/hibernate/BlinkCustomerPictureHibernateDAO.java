package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerPictureDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class BlinkCustomerPictureHibernateDAO extends
        BaseHibernateDAO<BlinkCustomerPictureModel, Long, BlinkCustomerPictureDAO>
        implements BlinkCustomerPictureDAO {

    @Override
    public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId ) {

        BlinkCustomerPictureModel customerPictureModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerPictureModel.class);

        criteria.add( Restrictions.eq("relationPictureTypeModel.pictureTypeId", pictureTypeId) );
        criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
        //criteria.add( Restrictions.ne("discrepant", Boolean.TRUE));
        List<BlinkCustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);

        if(list != null && !list.isEmpty()) {

            customerPictureModel = list.get(0);
        }

        return customerPictureModel;
    }

}
