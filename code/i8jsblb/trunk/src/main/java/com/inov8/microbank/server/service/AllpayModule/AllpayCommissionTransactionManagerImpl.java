package com.inov8.microbank.server.service.AllpayModule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AllpayCommissionTransactionModel;
import com.inov8.microbank.server.dao.allpaymodule.AllPayCommissionTransactionDAO;

public class AllpayCommissionTransactionManagerImpl implements AllpayCommissionTransactionManager
{

	private AllPayCommissionTransactionDAO allPayCommissionTransactionDAO;

	public BaseWrapper loadCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AllpayCommissionTransactionModel allpayCommissionTransactionModel;

		allpayCommissionTransactionModel = this.allPayCommissionTransactionDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(allpayCommissionTransactionModel);

		return baseWrapper;
	}

	public BaseWrapper updateCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AllpayCommissionTransactionModel allpayCommissionTransactionModel = (AllpayCommissionTransactionModel) baseWrapper.getBasePersistableModel();

		allpayCommissionTransactionModel = this.allPayCommissionTransactionDAO.saveOrUpdate((AllpayCommissionTransactionModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(allpayCommissionTransactionModel);

		return baseWrapper;
	}

	public void setAllPayCommissionTransactionDAO(AllPayCommissionTransactionDAO allPayCommissionTransactionDAO)
	{
		this.allPayCommissionTransactionDAO = allPayCommissionTransactionDAO;
	}

}
