package com.inov8.microbank.server.dao.addressmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Usman Ashraf
 * @version 1.0
 */


public class CustomerAddressesHibernateDAO
    extends BaseHibernateDAO<CustomerAddressesModel, Long, CustomerAddressesDAO>
    implements CustomerAddressesDAO
{
    @Override
    public CustomerAddressesModel findAddressByCustomerIdAndAddressType(Long customerId, Long addressType) throws FrameworkCheckedException {
        CustomerAddressesModel customerAddressesModel = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomerAddressesModel.class);
        detachedCriteria.add(Restrictions.eq("relationCustomerIdCustomerModel.customerId",customerId));
        detachedCriteria.add(Restrictions.eq("relationAddressTypeIdAddressTypeModel.addressTypeId",addressType));
        List<CustomerAddressesModel> customerAddressesModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(customerAddressesModelList.size() > 0 )
            customerAddressesModel = customerAddressesModelList.get(0);
        return customerAddressesModel;
    }
}
