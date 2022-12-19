package com.inov8.ola.server.facade;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.ola.integration.vo.LedgerModel;
import com.inov8.ola.server.service.ledger.LedgerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LedgerFacadeImpl implements LedgerFacade
{
		
	private LedgerManager ledgerManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private static final Log logger = LogFactory.getLog(LedgerFacadeImpl.class);


	public BaseWrapper saveLedgerEntry(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.ledgerManager.saveLedgerEntry(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	      throw ex;
	    }
	    
	    return baseWrapper;
	}
	
	public BaseWrapper loadLedgerEntry(BaseWrapper baseWrapper) throws Exception
	{
		try
	    {
			baseWrapper = this.ledgerManager.loadLedgerEntry(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	      throw ex;
	    }	    
	    return baseWrapper;
	}
	
	public BaseWrapper loadLedgerModelByPK(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		try
	    {
			baseWrapper = this.ledgerManager.loadLedgerModelByPK(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	      throw ex;
	    }	    
	    return baseWrapper;
	}

	@Override
	public Date getLastTrxDate(Long accountId, Long transactionTypeId, Long handlerId) throws Exception {
		return ledgerManager.getLastTrxDate(accountId,transactionTypeId,handlerId);
	}

	@Override
	public Double getConsumedBalanceByDateRangeForDormancy(Long accountId, Long transactionTypeId, Date startDate, Date endDate) throws Exception {
		return ledgerManager.getConsumedBalanceByDateRangeForDormancy(accountId,transactionTypeId,startDate,endDate);
	}

	@Override
	public List<com.inov8.integration.common.model.LedgerModel> loadLedgerEntries(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		List<com.inov8.integration.common.model.LedgerModel> ledgerList = null;
		try
		{
			ledgerList = this.ledgerManager.loadLedgerEntries(baseWrapper);
		}
		catch (Exception ex)
		{
			logger.error(ex.getClass() + " : " + ex.getMessage(), ex);
			throw ex;
		}
		return ledgerList;
	}

	public Double getDailyConsumedBalance(Long accountId,Long transactionTypeId,Date date) throws Exception{
		Double consumedBalance = null;
		try{
			consumedBalance = this.ledgerManager.getDailyConsumedBalance(accountId, transactionTypeId, date);
		}catch (Exception ex)
	    {
		      ex.printStackTrace();
		      throw ex;
		    }
		return consumedBalance;
	}
	public Double getConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,Date startDate,Date endDate) throws Exception{
		Double consumedBalance = null;
		try{
			consumedBalance = this.ledgerManager.getConsumedBalanceByDateRange(accountId, transactionTypeId, startDate,endDate);
		}catch (Exception ex)
	    {
		      ex.printStackTrace();
		      throw ex;
		    }
		return consumedBalance;
	}
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	
	public void setLedgerManager(LedgerManager ledgerManager)
	{
		this.ledgerManager = ledgerManager;
	}

	public List<LedgerModel> getLedgerModelByDateRangeAndAccountId(Long accountId,Date fromDate,Date toDate) throws Exception {
		
		List<LedgerModel> ledgerModels = null;
		try{
			ledgerModels = this.ledgerManager.getLedgerModelByDateRangeAndAccountId(accountId,fromDate, toDate);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return ledgerModels;
	}

	public List<LedgerModel> getLedgerModelByAccountId(Long accountId,Integer noOfTransactions) throws Exception {
		
		List<LedgerModel> ledgerModels = null;
		try{
			ledgerModels = this.ledgerManager.getLedgerModelByAccountId(accountId,noOfTransactions);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return ledgerModels;
	}

	public void deleteLedgerEntry(BaseWrapper baseWrapper) throws Exception {
		// TODO Auto-generated method stub
		try{
			this.ledgerManager.deleteLedgerEntry(baseWrapper);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}


  
  
  

}

