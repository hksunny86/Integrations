package com.inov8.microbank.server.dao.addressmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;

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


public class AddressHibernateDAO
    extends BaseHibernateDAO<AddressModel, Long, AddressDAO>
    implements AddressDAO
{
	public AddressModel findAddressById(long addressId) throws FrameworkCheckedException
	{
		List<AddressModel> addressModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AddressModel.class );
        detachedCriteria.add(Restrictions.eq("addressId", addressId));
        addressModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
		return addressModelList.get(0);
	}
	
	public AddressModel getAddress(Long addressId)
	  {
		String hql = "from AddressModel where addressId = ?";
		List<AddressModel> list = this.getHibernateTemplate().find(hql,addressId);
		return list.get(0);
	    
	  }
	
}
