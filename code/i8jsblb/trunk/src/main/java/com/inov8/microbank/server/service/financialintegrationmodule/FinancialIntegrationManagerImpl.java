package com.inov8.microbank.server.service.financialintegrationmodule;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.financialintegrationmodule.FinancialIntgListViewModel;

public class FinancialIntegrationManagerImpl implements FinancialIntegrationManager, ApplicationContextAware
{
	private GenericDao genericDao;
	private ApplicationContext applicationContext;
	
	
	public AbstractFinancialInstitution loadFinancialInstitution(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AbstractFinancialInstitution abstractFinancialInstitution = null;
		FinancialIntgListViewModel financialIntgListViewModel = new FinancialIntgListViewModel();
		
		if(baseWrapper.getBasePersistableModel() instanceof BankModel)
		{
			BankModel bankModel = (BankModel)baseWrapper.getBasePersistableModel();
			if(bankModel != null && bankModel.getBankId() != null)
			{				
				bankModel = this.genericDao.getEntityByPrimaryKey(bankModel);
				if(bankModel != null && bankModel.getFinancialIntegrationIdFinancialIntegrationModel() != null)
				{
					abstractFinancialInstitution = loadFinancialInstitutionByClassName(bankModel.getFinancialIntegrationIdFinancialIntegrationModel().getClassName());
				}
			}
		}
		else if(baseWrapper.getBasePersistableModel() instanceof SmartMoneyAccountModel)
		{
			SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();	
			if(smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null)
			{
				financialIntgListViewModel.setSmartMoneyAccountId(smartMoneyAccountModel.getSmartMoneyAccountId());		
				financialIntgListViewModel = this.genericDao.getEntityByPrimaryKey(financialIntgListViewModel);
				if(financialIntgListViewModel != null && financialIntgListViewModel.getClassName() != null)
				{
					abstractFinancialInstitution = loadFinancialInstitutionByClassName(financialIntgListViewModel.getClassName());
				}
			}
		}
		else
		{
			throw new FrameworkCheckedException("Invalid Mechanism for Financial Institution Loading...");
		}
		
		if(abstractFinancialInstitution == null)
		{
			throw new FrameworkCheckedException("Financial Institution Doesn't Exist...");
		}

		return abstractFinancialInstitution;
	}
	
	
	public AbstractFinancialInstitution loadFinancialInstitutionByClassName(String className) throws FrameworkCheckedException
	{
		AbstractFinancialInstitution abstractFinancialInstitution;
		if(null != className)
		{
			abstractFinancialInstitution = (AbstractFinancialInstitution)applicationContext.getBean(className);
		}
		else
		{
			throw new FrameworkCheckedException("Financial Institution Doesn't Exist...");
		}	
		return abstractFinancialInstitution;
	}
	

	public void setGenericDao(GenericDao genericDao)
	{
		this.genericDao = genericDao;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}