package com.inov8.microbank.server.dao.allpaymodule.hibernate;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AllpayCommissionTransactionModel;
import com.inov8.microbank.server.dao.allpaymodule.AllPayCommissionTransactionDAO;

public class AllPayCommissionTransactionHibernateDAO extends BaseHibernateDAO<AllpayCommissionTransactionModel, Long, AllPayCommissionTransactionDAO> implements
		AllPayCommissionTransactionDAO
{

}
