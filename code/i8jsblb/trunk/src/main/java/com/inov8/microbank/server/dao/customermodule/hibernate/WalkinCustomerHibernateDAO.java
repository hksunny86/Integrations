package com.inov8.microbank.server.dao.customermodule.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.server.dao.customermodule.WalkinCustomerDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Kashif Bashir
 * @version 1.0
 *
 */

public class WalkinCustomerHibernateDAO extends BaseHibernateDAO<WalkinCustomerModel, Long, WalkinCustomerDAO> implements WalkinCustomerDAO {
	
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.customermodule.WalkinCustomerDAO#getWalkinCustomerByCNIC(java.lang.String)
	 */
	public WalkinCustomerModel getWalkinCustomerByCNIC(String cnic) {
		
		WalkinCustomerModel walkinCustomerModel = null;
		
		Criterion cnicCriterion = Restrictions.eq("cnic", cnic);
			
		CustomList<WalkinCustomerModel> customList = super.findByCriteria(cnicCriterion);
	
		if(customList != null && !customList.getResultsetList().isEmpty()) {
			
			walkinCustomerModel = customList.getResultsetList().get(0);
		}		
		
		return walkinCustomerModel;
	}
	
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.customermodule.WalkinCustomerDAO#getWalkinCustomerModel(com.inov8.microbank.common.model.WalkinCustomerModel)
	 */
	public WalkinCustomerModel getWalkinCustomerByMobile(String mobileNumber) {
		
		WalkinCustomerModel walkinCustomerModel = null;

		Criterion mobileCriterion = Restrictions.eq("mobileNumber", mobileNumber);
					
		CustomList<WalkinCustomerModel> customList = super.findByCriteria(mobileCriterion);
	
		if(customList != null && !customList.getResultsetList().isEmpty()) {
			
			walkinCustomerModel = customList.getResultsetList().get(0);
		}		
		
		return walkinCustomerModel;
	}
	@Override
	public WalkinCustomerModel getWalkinCustomerByPair(String mobile, String cnic) {

		WalkinCustomerModel walkinCustomerModel = null;

		Criterion cnicCriterion = Restrictions.and( Restrictions.eq("cnic", cnic),  Restrictions.eq("mobileNumber", mobile)) ;

		CustomList<WalkinCustomerModel> customList = super.findByCriteria(cnicCriterion);

		if(customList != null && !customList.getResultsetList().isEmpty()) {

			walkinCustomerModel = customList.getResultsetList().get(0);
		}

		return walkinCustomerModel;
	}
}
