package com.inov8.microbank.server.dao.suppliermodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.server.dao.suppliermodule.SupplierDAO;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SupplierHibernateDAO
    extends BaseHibernateDAO<SupplierModel, Long, SupplierDAO>
    implements SupplierDAO
{
  public SupplierHibernateDAO()
  {
  }
  
  public List getServicesAgainstSupplier(Long supplierId)
  {
	  
	  String hql = "from ServiceModel sm where sm.relationServiceTypeIdServiceTypeModel = 3";
	  List list =  this.getHibernateTemplate().find(hql);
	  return list;
	  
	  
	  
  }

}
