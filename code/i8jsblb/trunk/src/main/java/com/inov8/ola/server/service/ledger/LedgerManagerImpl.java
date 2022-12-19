package com.inov8.ola.server.service.ledger;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.common.model.LedgerModel;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.util.TransactionTypeConstants;
import org.hibernate.criterion.MatchMode;


public class LedgerManagerImpl implements LedgerManager 
{

	private  LedgerDAO  ledgerDAO;
	
	
	public BaseWrapper saveLedgerEntry(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		LedgerModel ledgerModel = (LedgerModel)baseWrapper.getBasePersistableModel() ;		
		ledgerModel = this.ledgerDAO.saveOrUpdate(ledgerModel) ;
		
		baseWrapper.setBasePersistableModel(ledgerModel) ;		
				
		return baseWrapper;	
	}
	
	public BaseWrapper loadLedgerEntry(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		LedgerModel ledgerModel = (LedgerModel)baseWrapper.getBasePersistableModel() ;		
		List<LedgerModel> list = this.ledgerDAO.findByExample(ledgerModel,null).getResultsetList() ;
		
		if( list.size() > 0 )
		{
			baseWrapper.setBasePersistableModel(list.get(0)) ;
		}
		else			
			baseWrapper.setBasePersistableModel(null) ;		
				
		return baseWrapper;	
	}

	public BaseWrapper loadLedgerModelByPK(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		LedgerModel ledgerModel = (LedgerModel)baseWrapper.getBasePersistableModel() ;
		ledgerModel = this.ledgerDAO.findByPrimaryKey(ledgerModel.getPrimaryKey()) ;
		baseWrapper.setBasePersistableModel(ledgerModel) ;		
				
		return baseWrapper;	
	}

	@Override
	public Date getLastTrxDate(Long accountId, Long transactionTypeId, Long handlerId) throws Exception {
		return ledgerDAO.getLastTrxDate(accountId,transactionTypeId,handlerId);
	}

	@Override
	public Double getConsumedBalanceByDateRangeForDormancy(Long accountId, Long transactionTypeId, Date startDate, Date endDate) throws Exception {
		Double consumedBalance = null;
		try{
			return this.ledgerDAO.getConsumedBalanceByDateRangeForDormancy(accountId, transactionTypeId, startDate,endDate);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return consumedBalance;
	}

	@Override
	public List<LedgerModel> loadLedgerEntries(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		LedgerModel ledgerModel = (LedgerModel)baseWrapper.getBasePersistableModel() ;
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setIgnoreCase(Boolean.FALSE);

		List<LedgerModel> list = this.ledgerDAO.findByExample(ledgerModel,null,null,exampleHolder,null).getResultsetList() ;


		return list;
	}

	public Double getDailyConsumedBalance(Long accountId,Long transactionTypeId,Date date){
		Double consumedBalance = null;
		try{
			return this.ledgerDAO.getDailyConsumedBalance(accountId, transactionTypeId, date,null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return consumedBalance;
	}
	
	public Double getConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,Date startDate,Date endDate){
		//yearly limit is bypassed in getConsumedBalanceByDateRange
		//for reporting new method implemented getConsumedBalanceByDateRangeReports for getting consumed balance greater than 1 month
		//for hot fix, portal yearly limit check also skip 11.01 pm 6 Oct 2021
		Double consumedBalance = null;
		try{
			//return this.ledgerDAO.getConsumedBalanceByDateRangeReports(accountId,transactionTypeId,startDate,endDate,null);
			return this.ledgerDAO.getConsumedBalanceByDateRange(accountId, transactionTypeId, startDate,endDate,null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return consumedBalance;
	}
	
	public void deleteLedgerEntry(BaseWrapper baseWrapper){
		try{
			LedgerModel ledgerModel = (LedgerModel) baseWrapper.getBasePersistableModel();
			this.ledgerDAO.deleteByPrimaryKey(ledgerModel.getPrimaryKey());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void setLedgerDAO(LedgerDAO ledgerDAO)
	{
		this.ledgerDAO = ledgerDAO;
	}

	public List<com.inov8.ola.integration.vo.LedgerModel> getLedgerModelByDateRangeAndAccountId(Long accountId,Date fromDate,Date toDate) throws Exception {
		List<com.inov8.ola.integration.vo.LedgerModel> ledgerModels = null;
		try{
			List<LedgerModel> olaLedgerModels= this.ledgerDAO.getLedgerModelByDateRangeAndAccountID(fromDate, toDate,accountId);
			if(olaLedgerModels != null && olaLedgerModels.size() > 0){
				ledgerModels = new ArrayList<com.inov8.ola.integration.vo.LedgerModel>();
				for(LedgerModel model : olaLedgerModels){
					com.inov8.ola.integration.vo.LedgerModel voLedgerModel = new com.inov8.ola.integration.vo.LedgerModel();
					voLedgerModel.setCustomerBalanceAfterTx(model.getBalanceAfterTransaction());
					Long reasonId = model.getReasonId().longValue();
					if(model.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
						if(TransactionTypeConstants.REVERSAL_REASON.longValue() == reasonId)
						{
							continue;
						}
						voLedgerModel.setCustomerBalanceBeforeTx(model.getBalanceAfterTransaction() - model.getTransactionAmount());
						voLedgerModel.setCreditAmount(model.getTransactionAmount());
					}else if(model.getTransactionTypeId().equals(TransactionTypeConstants.DEBIT)){
						if(TransactionTypeConstants.REVERSAL_REASON.longValue() == reasonId)
						{
							continue;
						}
						voLedgerModel.setCustomerBalanceBeforeTx(model.getBalanceAfterTransaction() + model.getTransactionAmount());
						voLedgerModel.setDebitAmount(model.getTransactionAmount());
					}
					else
					{
						continue;
					}
					voLedgerModel.setTransactionTypeId( model.getTransactionTypeId() );
					voLedgerModel.setReasonId(reasonId);
					voLedgerModel.setMicrobankTransactionCode(model.getMicrobankTransactionCode());
					voLedgerModel.setTransactionDateTime(model.getTransactionTime());
					ledgerModels.add(voLedgerModel);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ledgerModels;
	}
	
	
	public List<com.inov8.ola.integration.vo.LedgerModel> getLedgerModelByAccountId(Long accountId, Integer noOfTransactions) throws Exception {
		List<com.inov8.ola.integration.vo.LedgerModel> ledgerModels = null;
		try{
			List<LedgerModel> olaLedgerModels= this.ledgerDAO.getLedgerModelByAccountID(accountId,noOfTransactions);
			if(olaLedgerModels != null && olaLedgerModels.size() > 0){
				ledgerModels = new ArrayList<com.inov8.ola.integration.vo.LedgerModel>();
				for(LedgerModel model : olaLedgerModels){
					com.inov8.ola.integration.vo.LedgerModel voLedgerModel = new com.inov8.ola.integration.vo.LedgerModel();
					voLedgerModel.setCustomerBalanceAfterTx(model.getBalanceAfterTransaction());
					if(model.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
						if(model.getReasonId().longValue() == TransactionTypeConstants.REVERSAL_REASON.longValue())
						{
							break;
						}
//						voLedgerModel.setCustomerBalanceBeforeTx(model.getBalanceAfterTransaction()-model.getTransactionAmount());
						voLedgerModel.setCreditAmount(model.getTransactionAmount());
					}else if(model.getTransactionTypeId().equals(TransactionTypeConstants.DEBIT)){
//						voLedgerModel.setCustomerBalanceBeforeTx(model.getBalanceAfterTransaction()-model.getTransactionAmount());
						if(model.getReasonId().longValue() == TransactionTypeConstants.REVERSAL_REASON.longValue())
						{
							break;
						}
						voLedgerModel.setDebitAmount(model.getTransactionAmount());
					}
					else
					{
						break;
					}
					voLedgerModel.setMicrobankTransactionCode(model.getMicrobankTransactionCode());
					voLedgerModel.setTransactionDateTime(model.getTransactionTime());
					ledgerModels.add(voLedgerModel);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ledgerModels;
	}

	
	
	
	

	 
	
	
	
	

}


