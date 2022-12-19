package com.inov8.microbank.server.dao.portal.escalateinov8.hibernate;

import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.escalateinov8module.EscalateInov8ListViewModel;
import com.inov8.microbank.server.dao.portal.escalateinov8.EscalateInov8ListViewDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class EscalateInov8ListViewHibernateDAOImpl
    extends BaseHibernateDAO<EscalateInov8ListViewModel, Long,EscalateInov8ListViewDAO>
    implements EscalateInov8ListViewDAO
{
	public static String HQL_BANK_APP_USER = "SELECT au.appUserId FROM AppUserModel au where au.relationBankUserIdBankUserModel.bankUserId IN(SELECT bankUserId FROM BankUserModel bu WHERE bu.relationBankIdBankModel.bankId=?)";
	public static String HQL_SUPPLIER_APP_USER = "SELECT au.appUserId FROM AppUserModel au WHERE au.relationSupplierUserIdSupplierUserModel.supplierUserId IN(SELECT su.supplierUserId FROM SupplierUserModel su WHERE su.relationSupplierIdSupplierModel.supplierId = ?) OR au.relationMnoUserIdMnoUserModel.mnoUserId IN(SELECT mu.mnoUserId FROM MnoUserModel mu WHERE mu.relationMnoIdMnoModel.mnoId IN(SELECT mn.mnoId FROM MnoModel mn WHERE mn.relationSupplierIdSupplierModel.supplierId = ?))"; 
		  
	
	public Long findAppUserByBankId(BaseWrapper wrapper)
	{
		Long bankId = new Long(wrapper.getObject("bankId").toString() );
		List list = this.getHibernateTemplate().find(HQL_BANK_APP_USER, bankId);
		if(list.isEmpty())
			return new Long(0);
		
		return new Long(list.get(0).toString());
	}

	public Long findAppUserBySupplierId(BaseWrapper wrapper)
	{
		Long supplierId = new Long(wrapper.getObject("supplierId").toString() );
		List list = this.getHibernateTemplate().find(HQL_SUPPLIER_APP_USER, new Long[]{supplierId, supplierId});
		if(list.isEmpty())
			return new Long(0);
		
		return new Long(list.get(0).toString());
	}
	
	
	
}

