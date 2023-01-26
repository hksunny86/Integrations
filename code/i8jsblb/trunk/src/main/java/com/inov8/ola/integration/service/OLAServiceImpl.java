package com.inov8.ola.integration.service;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.AccountsWithStatsListViewModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.ola.integration.vo.LedgerModel;
import com.inov8.ola.integration.vo.OLALedgerVO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.server.facade.LedgerFacade;
import com.inov8.ola.server.facade.LimitFacade;
import com.inov8.ola.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;



public class OLAServiceImpl implements OLAService
{
	
	private AccountFacade accountFacade;
	private LedgerFacade ledgerFacade;
	private LimitFacade limitFacade;
	protected static Log logger	= LogFactory.getLog(OLAServiceImpl.class);
	
	
	
	public Map<Long, String> getStatusCodes() throws Exception
	{		
		return null;
	}
	
	
	public HashMap<String,OLAVO> getAllAccountsWithStats(Date date) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccountsWithStats ------ ******* called ");
		}
		
		AccountModel accountModel = new AccountModel() ;
////		accountModel.setStatusId( StatusConstants.ACTIVE ) ;
//		
//		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
////		accountHolderModel.setFirstName("Hell");
//		accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
		searchBaseWrapper.setBasePersistableModel(accountModel) ;
		
		List<AccountsWithStatsListViewModel> list = this.accountFacade.getAllAccountsWithStats(date);
		
//		searchBaseWrapper = this.accountFacade.getAllAccounts(searchBaseWrapper) ;
		
		HashMap<String,OLAVO> olaVOList = this.prepareOLAVOHashMap( list ) ;
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccounts ------ ******* end ");
		}
		
		return olaVOList;
	}
	
	
	
	public HashMap<String,OLAVO> getAllAccountsStatsWithRange(Date startDate, Date endDate) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccountsStatsWithRange ------ ******* called ");
		}
		
		List<Object> list = this.accountFacade.getAccountStatsWithRange(startDate,endDate);
		
		
		
		
		HashMap<String,OLAVO> olaVOList = this.prepareOLAVOHashMapForRange( list ) ;
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccountsStatsWithRange ------ ******* end ");
		}
		
		return olaVOList;
	}

	
	
	
	
	public void init()
	{
		try
		{
			OLAVO ola = new OLAVO();
			ola.setPayingAccNo("0000000003");
			ola.setReceivingAccNo("0000000077");
			ola.setMicrobankTransactionCode("51397208");
			ola.setTransactionTypeId("1");
			ola.setReasonId(6l);
			ola.setBalance(1000d);
			
			this.creditTransfer(ola);
			
//			this.getAllAccountsWithStats(new Date()) ;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public OLAVO creditTransfer(OLAVO olaVO) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- creditTransfer ------ ******* called ");
		}
		
		try
		{
			olaVO.setResponseCode("");
			olaVO = this.accountFacade.makeTransferFunds(olaVO);
			
/*			if(!olaVO.getIsReversal()){
				// Default Scenario
				olaVO = this.accountFacade.creditTransferRequiresNewTransaction(olaVO) ;
			}else{
				// Orphan Transaction Reversal Scenario
				olaVO = this.accountFacade.makeCreditTransfer(olaVO) ;
			}
//			Ledger is now updated inside creditTransferRequiresNewTransaction()
//			olaVO = this.accountFacade.makeLedgerEntryRequiresNewTransaction(olaVO);
*/
		}catch (Exception e)
		{		
			e.printStackTrace();
			if( olaVO.getResponseCode().equals("") )
				olaVO.setResponseCode("06");
		}		
		
		return olaVO;
	}
	
	public OLAVO changeAccountStatus(OLAVO olaVO) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- changeAccountStatus ------ ******* called ");
		}
		
		AccountModel accountModel = this.generateAccountModel(olaVO) ;
		accountModel.setAccountId(olaVO.getAccountId());
		
		try
		{			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(accountModel) ;			
			accountModel = (AccountModel)this.accountFacade.loadAccountByPK(baseWrapper).getBasePersistableModel() ;
			
			if( accountModel == null  )
			{
				olaVO.setResponseCode("03");
			}
			else
			{
				AccountHolderModel accountHolderModel = accountModel.getAccountHolderIdAccountHolderModel() ;
				
				if( olaVO.getFirstName() != null && !olaVO.getFirstName().equals("") )
					accountHolderModel.setFirstName(olaVO.getFirstName());
				
				if( olaVO.getMiddleName() != null && !olaVO.getMiddleName().equals("") )
					accountHolderModel.setMiddleName(olaVO.getMiddleName());
				
				if( olaVO.getLastName() != null && !olaVO.getLastName().equals("") )
					accountHolderModel.setLastName(olaVO.getLastName());
				
				if( olaVO.getFatherName() != null && !olaVO.getFatherName().equals("") )
					accountHolderModel.setFatherName(olaVO.getFatherName());
				
				if( olaVO.getCnic() != null && !olaVO.getCnic().equals("") )
//					accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic()));
					accountHolderModel.setCnic(olaVO.getCnic());
				
				if( olaVO.getAddress() != null && !olaVO.getAddress().equals("") )
					accountHolderModel.setAddress(olaVO.getAddress());
				
				if( olaVO.getLandlineNumber() != null && !olaVO.getLandlineNumber().equals("") )
					accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
				
				if( olaVO.getMobileNumber() != null && !olaVO.getMobileNumber().equals("") )
					accountHolderModel.setMobileNumber(olaVO.getMobileNumber());	
				
				if( olaVO.getDob() != null && !olaVO.getDob().equals("") )
				{
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//					accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(olaVO.getDob())));
					accountHolderModel.setDob(dateFormat.format(olaVO.getDob()));
				}
				
				if( olaVO.getLandlineNumber() != null && !olaVO.getLandlineNumber().equals("") )
					accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
				
				if( olaVO.getStatusId() != null && olaVO.getStatusId() != 0 )			
					accountModel.setStatusId(olaVO.getStatusId()) ;
				
				try
				{
					baseWrapper.setBasePersistableModel(accountModel) ;			
					accountModel = (AccountModel)this.accountFacade.updateAccount(baseWrapper).getBasePersistableModel() ;
					
					olaVO.setResponseCode("00");
				}
				catch (Exception e)
				{				
						if( e instanceof DataIntegrityViolationException && e.getCause().getClass() == ConstraintViolationException.class)
						{
							ConstraintViolationException constExp = (ConstraintViolationException)e.getCause();
							if( constExp.getConstraintName().indexOf("UK_ACC_HLD_CNIC") > 0 )
							{
								olaVO.setResponseCode("07") ;
							}
						}
						else
							olaVO.setResponseCode("06");
						
						e.printStackTrace();
					}	
						
			}
		}
		catch (Exception e)
		{
			olaVO.setResponseCode("06");
			e.printStackTrace();
		}
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- changeAccountStatus ------ ******* end ");
		}
		
		return olaVO;
	}
	
	/*
	 * Updates Customer / Agent OLA Account Details
	 */
	public OLAVO changeAccountDetails(OLAVO olaVO) throws Exception{
		
		try
		{			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			
			AccountModel accountModel = new AccountModel() ;
			logger.info("OLAServiceImpl.changeAccountDetails() for NIC :: " + olaVO.getCnic() + " and Mobile # :: " + olaVO.getMobileNumber() );
			if( olaVO.getCnic() != null  ){
				AccountHolderModel accountHolderModel = new AccountHolderModel() ;
//				accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic())) ;
				accountHolderModel.setCnic(olaVO.getCnic()) ;
				accountHolderModel.setMobileNumber(olaVO.getMobileNumber());
				accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel) ;
// 				In case of customer, customer account type is updatable
//				accountModel.setCustomerAccountTypeId(olaVO.getCustomerAccountTypeId());

				baseWrapper.setBasePersistableModel(accountModel);
				
				/*baseWrapper = this.accountFacade.loadAccount(baseWrapper) ;
				accountModel = (AccountModel)baseWrapper.getBasePersistableModel();*/
				accountModel = accountFacade.getAccountModelByCNICAndMobile(olaVO.getCnic(),olaVO.getMobileNumber());
			}
			
			if( accountModel == null  ){
				olaVO.setResponseCode("03");
				return olaVO ;
			}else{
				AccountHolderModel accountHolderModel = accountModel.getAccountHolderIdAccountHolderModel() ;
				
				if( olaVO.getFirstName() != null && !olaVO.getFirstName().equals("") ){
					accountHolderModel.setFirstName(olaVO.getFirstName());
				}
				
//				if( olaVO.getMiddleName() != null && !olaVO.getMiddleName().equals("") ){
//					accountHolderModel.setMiddleName(olaVO.getMiddleName());
//				}
				
				if( olaVO.getLastName() != null && !olaVO.getLastName().equals("") ){
					accountHolderModel.setLastName(olaVO.getLastName());
				}
				
				// In case of customer, change the customer account type
				if( olaVO.getCustomerAccountTypeId() != null){
					OlaCustomerAccountTypeModel olaModel = new OlaCustomerAccountTypeModel();
					olaModel.setCustomerAccountTypeId(olaVO.getCustomerAccountTypeId());
					accountModel.setCustomerAccountTypeModel(olaModel) ;//TODO: used model instead of Long ID in merging process. might be erroneous.
				}

//				if( olaVO.getFatherName() != null && !olaVO.getFatherName().equals("") ){
//					accountHolderModel.setFatherName(olaVO.getFatherName());
//				}
				
//				if( olaVO.getCnic() != null && !olaVO.getCnic().equals("") ){
//					accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic()));
//				}
				
//				if( olaVO.getAddress() != null && !olaVO.getAddress().equals("") ){
//					accountHolderModel.setAddress(olaVO.getAddress());
//				}
				
//				if( olaVO.getLandlineNumber() != null && !olaVO.getLandlineNumber().equals("") ){
//					accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
//				}
//				if( olaVO.getMobileNumber() != null && !olaVO.getMobileNumber().equals("") )
//					accountHolderModel.setMobileNumber(olaVO.getMobileNumber());	
				
//				if( olaVO.getDob() != null && !olaVO.getDob().equals("") )
//				{
//					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//					accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(olaVO.getDob())));
//				}
				
//				if( olaVO.getLandlineNumber() != null && !olaVO.getLandlineNumber().equals("") ){
//					accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
//				}				
//				if( olaVO.getStatusId() != null && olaVO.getStatusId() != 0 )			
//					accountModel.setStatusId(olaVO.getStatusId()) ;
				
				try{
					baseWrapper.setBasePersistableModel(accountModel) ;			
					accountModel = (AccountModel)this.accountFacade.updateAccount(baseWrapper).getBasePersistableModel() ;
					
					olaVO.setResponseCode("00");
				}catch (Exception e){				
						if( e instanceof DataIntegrityViolationException && e.getCause().getClass() == ConstraintViolationException.class){
							ConstraintViolationException constExp = (ConstraintViolationException)e.getCause();
							if( constExp.getConstraintName().indexOf("UK_ACC_HLD_CNIC") > 0 ){
								olaVO.setResponseCode("07") ;
							}
						}else{
							olaVO.setResponseCode("06");
						}
						e.printStackTrace();
					}	
						
			}
		}
		catch (Exception e)
		{
			olaVO.setResponseCode("06");
			e.printStackTrace();
		}
		
		return olaVO;
	}

	public List<OLAVO> getAllAccounts( OLAVO olaVO ) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccounts ------ ******* called ");
		}
		
		AccountModel accountModel = this.prepareAccountModel(olaVO) ;
////		accountModel.setStatusId( StatusConstants.ACTIVE ) ;
//		
//		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
////		accountHolderModel.setFirstName("Hell");
//		accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
		searchBaseWrapper.setBasePersistableModel(accountModel) ;
		searchBaseWrapper.setSortingOrderMap(olaVO.getSortingOrderMap());
		searchBaseWrapper.putObject("OLA", olaVO) ;
		
		searchBaseWrapper = this.accountFacade.getAllAccounts(searchBaseWrapper) ;
		
		List<OLAVO> olaVOList = this.prepareOLAVOList( searchBaseWrapper.getCustomList().getResultsetList()  ) ;
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccounts ------ ******* end ");
		}
		
		return olaVOList;
	}
	
	public List<OLAVO> getAllAccounts( ) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccounts ------ ******* called ");
		}
		
		AccountModel accountModel = new AccountModel() ;
////		accountModel.setStatusId( StatusConstants.ACTIVE ) ;
//		
//		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
////		accountHolderModel.setFirstName("Hell");
//		accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
		searchBaseWrapper.setBasePersistableModel(accountModel) ;
		
		searchBaseWrapper = this.accountFacade.getAllAccounts(searchBaseWrapper) ;
		
		List<OLAVO> olaVOList = this.prepareOLAVOList( searchBaseWrapper.getCustomList().getResultsetList()  ) ;
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAllAccounts ------ ******* end ");
		}
		
		return olaVOList;
	}
	
	
	public OLAVO getAccountInfo(OLAVO olaVO) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAccountInfo ------ ******* called ");
		}
		
		try
		{
			AccountModel accountModel = new AccountModel() ;
			
			if( olaVO.getAccountId() != null  )
			{
				accountModel.setAccountId( olaVO.getAccountId()) ;
			
				BaseWrapper baseWrapper = new BaseWrapperImpl() ;
				baseWrapper.setBasePersistableModel(accountModel) ;
				
				baseWrapper = this.accountFacade.loadAccountByPK(baseWrapper) ;
				accountModel = (AccountModel)baseWrapper.getBasePersistableModel() ;
				
				
			}
			else if( olaVO.getCnic() != null  )
			{
				AccountHolderModel accountHolderModel = new AccountHolderModel() ;
//				accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic())) ;
				accountHolderModel.setCnic(olaVO.getCnic());
				accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel) ;
				
				BaseWrapper baseWrapper = new BaseWrapperImpl() ;
				baseWrapper.setBasePersistableModel(accountModel) ;
				
				baseWrapper = this.accountFacade.loadAccount(baseWrapper) ;
				accountModel = (AccountModel)baseWrapper.getBasePersistableModel() ;
			}
			else if( olaVO.getPayingAccNo() != null  )
			{
				accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(olaVO.getPayingAccNo()));
				logger.info("OLAServiceImpl.getAccountInf().accountNumber :: " + accountModel.getAccountNumber());
				BaseWrapper baseWrapper = new BaseWrapperImpl() ;
				baseWrapper.setBasePersistableModel(accountModel) ;
				
				baseWrapper = this.accountFacade.loadAccount(baseWrapper) ;
				accountModel = (AccountModel)baseWrapper.getBasePersistableModel() ;
				
			}
			
			if( accountModel == null  )
			{
				olaVO.setResponseCode("03");
				return olaVO ;
			}
			
			List<AccountModel> accountModelList = new ArrayList<AccountModel>();
			accountModelList.add(accountModel) ;
			
			List<OLAVO> olaVOList = this.prepareOLAVOList(accountModelList) ;
			
			olaVO = olaVOList.get(0) ;
			olaVO.setResponseCode("00");
		}
		catch (RuntimeException e)
		{	
			logger.error("[OLAServiceImpl.getAccountInfo] Exception occured. CNIC:" + olaVO.getCnic() + " \nException :: " + e);
			olaVO.setResponseCode("06");
		}
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAccountInfo ------ ******* end ");
		}
		
		return olaVO;
	}
	

	public OLAVO transaction(OLAVO olaVO) throws Exception
	{
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- transaction ------ ******* called ");
		}
		
		if( (olaVO.getPayingAccNo() == null || olaVO.getPayingAccNo().equals("")) && olaVO.getReasonId().longValue() != ReasonConstants.INOV8_COMMISSION )
		{
			olaVO.setResponseCode("06");
			return olaVO;
		}
		else if( olaVO.getReasonId().longValue() == ReasonConstants.INOV8_COMMISSION )
		{
			olaVO = this.accountFacade.makeTxFori8Commission(olaVO) ;
		}
		else if( olaVO.getReasonId().longValue() == ReasonConstants.BILL_PAYMENT )
		{
			olaVO = this.accountFacade.makeTxRequiresNewTransaction(olaVO) ;
		}
		else{
			
			if(olaVO.getRequiresNewTrx()){//when debit/credit account is performed in new transaction.(e.g. C2C sundry)
				
				olaVO = this.accountFacade.makeTxRequiresNewTransaction(olaVO) ;
			
			}else{
				
				olaVO = this.accountFacade.makeTx(olaVO) ;
			}
		}
		
		return olaVO;
	}


	public OLAVO checkBalance(OLAVO olaVO) throws Exception
	{
		
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- checkBalance ------ ******* called ");
		}
		
		AccountModel accountModel = this.generateAccountModel(olaVO) ;
		
		if( accountModel.getAccountNumber() == null || accountModel.getAccountNumber().equals("") )
		{
			olaVO.setResponseCode("06");
			return olaVO ;
		}
		
		try
		{			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(accountModel) ;			
			accountModel = (AccountModel)this.accountFacade.loadAccount(baseWrapper).getBasePersistableModel() ;
			
			if( accountModel == null  )
			{
				olaVO.setResponseCode("03");
				
			}
			else if( accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue() )
			{
				olaVO.setResponseCode("02");				
			}
			else
			{
				olaVO.setResponseCode("00");
//				Double balance = Double.parseDouble( EncryptionUtil.decryptPin(accountModel.getBalance()));
				Double balance = Double.parseDouble(accountModel.getBalance());
				balance = Double.valueOf(Formatter.formatDouble(balance));  // formatting to up to 2 decimal places
				olaVO.setBalance(balance) ;								
			}

		}
		catch (Exception e)
		{
			olaVO.setResponseCode("06");
			e.printStackTrace();
		}	
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- checkBalance ------ ******* end ");
		}
		
		return olaVO;
	}

	public OLAVO createAccount(OLAVO olaVO) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- createAccount ------ ******* called ");
		}
		
		AccountHolderModel accountHolderModel = this.generateAccountHolderModel(olaVO) ;
		
		
		try
		{	
//			 Sets the Account Number in OLAVO
			if( accountHolderModel.getAccountHolderIdAccountModelList() != null 
					&& ((List)accountHolderModel.getAccountHolderIdAccountModelList()).get(0) != null )
			{
				olaVO.setPayingAccNo( ((AccountModel)((List)accountHolderModel.getAccountHolderIdAccountModelList()).get(0)).getAccountNumber().toString() )  ;

				
				((AccountModel)((List)accountHolderModel.getAccountHolderIdAccountModelList()).get(0)).setAccountNumber( EncryptionUtil.encryptAccountNo( olaVO.getPayingAccNo( ) ))  ;
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(accountHolderModel) ;			
			baseWrapper = this.accountFacade.saveAccountHolder(baseWrapper) ;
			olaVO.setAccountHolderId(((AccountHolderModel)baseWrapper.getBasePersistableModel()).getAccountHolderId());
			
			
			olaVO.setResponseCode("00");
			
			

		}
		catch( ConstraintViolationException constExp )
		{
			constExp.printStackTrace();
			if( constExp.getConstraintName().indexOf("UK_ACC_HLD_CNIC") > 0 )
			{
				olaVO.setResponseCode("07") ;				
			}			
		}
		catch (Exception e)
		{
			if( e instanceof DataIntegrityViolationException && e.getCause().getClass() == ConstraintViolationException.class)
			{
				
				ConstraintViolationException constExp = (ConstraintViolationException)e.getCause();
				if( constExp.getConstraintName().indexOf("UK_ACC_HLD_CNIC") > 0 )
				{
					olaVO.setResponseCode("07") ;
				}
			}
			else
				olaVO.setResponseCode("06");
			
			e.printStackTrace();
		}	
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- createAccount ------ ******* end ");
		}
			
		return olaVO;
	}

	public OLAVO deleteAccount(OLAVO olaVO) throws Exception
	{
		olaVO.setResponseCode("00");
		olaVO.setAuthCode("12345");
		
		return olaVO;
	}

	public List<LedgerModel> getLegder(OLALedgerVO ledgerVO) throws Exception
	{
		List<LedgerModel> ledgerModelList  = null;
		OLAVO vo = new OLAVO();
		vo.setCnic(ledgerVO.getNIC());
		vo = getAccountInfo(vo);
		if(vo != null && vo.getAccountId() != null){
			ledgerModelList = ledgerFacade.getLedgerModelByDateRangeAndAccountId(vo.getAccountId(),ledgerVO.getFromDate(), ledgerVO.getToDate());
		}
//		LedgerModel ledgerModel = new LedgerModel() ;		
//		ledgerModel.setAmount(15D);
//        ledgerModel.setCustomerBalanceAfterTx(115D);
//		ledgerModel.setCustomerBalanceBeforeTx(100D);
//		ledgerModel.setMicrobankTransactionCode("I8KFKFKHFH") ;
////		
//		ledgerModelList = new ArrayList<LedgerModel>();
//		ledgerModelList.add(ledgerModel);
//		
		
		return ledgerModelList;
	}

	public List<LedgerModel> getLegderTransactions(OLALedgerVO ledgerVO,Integer noOfTransactions) throws Exception
	{
		List<LedgerModel> ledgerModelList  = null;
		OLAVO vo = new OLAVO();
		vo.setCnic(ledgerVO.getNIC());
		vo = getAccountInfo(vo);
		if(vo != null && vo.getAccountId() != null){
			ledgerModelList = ledgerFacade.getLedgerModelByAccountId(vo.getAccountId(),noOfTransactions);
		}
//		LedgerModel ledgerModel = new LedgerModel() ;		
//		ledgerModel.setAmount(15D);
//        ledgerModel.setCustomerBalanceAfterTx(115D);
//		ledgerModel.setCustomerBalanceBeforeTx(100D);
//		ledgerModel.setMicrobankTransactionCode("I8KFKFKHFH") ;
////		
//		ledgerModelList = new ArrayList<LedgerModel>();
//		ledgerModelList.add(ledgerModel);
//		
		
		return ledgerModelList;
	}
	
	public OLAVO reversal(OLAVO olaVO) throws Exception
	{
		logger.debug(" ******* ---- reversal ------ ******* called ");
		
		try
		{
			/*Added by mudassir - walkin customer account number loading*/
			boolean isWalkin = false;
			String cnic = olaVO.getCnic();
			if(null != cnic && StringUtil.isWalkinCustomerCNIC(cnic)){
				AccountModel accountModel = accountFacade.getAccountModelByCNIC(cnic);
				olaVO.setAccountId(accountModel.getAccountId());
				isWalkin = true;
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();
			baseWrapper.setBasePersistableModel(ledgerModel) ;
			
			if(olaVO.getLedgerId() != null){//in case when reversal is performed from customer bb statement 
				ledgerModel.setPrimaryKey(olaVO.getLedgerId());
				baseWrapper = this.ledgerFacade.loadLedgerModelByPK(baseWrapper) ;
			}else{
				ledgerModel.setAuthCode(olaVO.getAuthCode()) ;
				ledgerModel.setAccountId(olaVO.getAccountId()) ; // added by maqsood shahzad to get the account number other than the i8 account
				baseWrapper = this.ledgerFacade.loadLedgerEntry(baseWrapper) ;
			}
			
			ledgerModel = (com.inov8.integration.common.model.LedgerModel)baseWrapper.getBasePersistableModel();
			
			if( ledgerModel == null )
			{
				olaVO.setResponseCode("06") ;
				return olaVO;
			}
			
			olaVO.setBalance(ledgerModel.getTransactionAmount()) ;
			
						
				olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString()) ;
					
				//olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString()) ;
		
			
			AccountModel accountModel = new AccountModel() ;
			accountModel.setAccountId(ledgerModel.getAccountId()) ;
			
			baseWrapper = new BaseWrapperImpl() ;
			baseWrapper.setBasePersistableModel(accountModel) ;
			
			baseWrapper = this.accountFacade.loadAccountByPK(baseWrapper) ;
			
			accountModel = (AccountModel)baseWrapper.getBasePersistableModel() ;
			
			
			olaVO.setPayingAccNo( EncryptionUtil.decryptAccountNo(accountModel.getAccountNumber()) ) ;
			
			if(! isWalkin){
			
				olaVO = this.accountFacade.makeTx(olaVO) ;
				
			}else{
				olaVO.setResponseCode("00");
			}
			
			ledgerModel.setReasonId(ReasonConstants.REVERSAL);
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(ledgerModel);
			this.ledgerFacade.saveLedgerEntry(baseWrapper);
		}
		catch (Exception e)
		{			
			e.printStackTrace();
			olaVO.setResponseCode("06") ;
		}
		
		logger.debug(" ******* ---- reversal ------ ******* end ");
		
		return olaVO;
	}

	
	public void setAccountFacade(AccountFacade accountFacade)
	{
		this.accountFacade = accountFacade;
	}
	
	
	private AccountModel prepareAccountModel( OLAVO olaVO ) throws Exception
	{
		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
		AccountModel accountModel = new AccountModel() ;
		
		if( olaVO != null )
		{
			accountHolderModel.setFirstName(olaVO.getFirstName());
			accountHolderModel.setLastName(olaVO.getLastName());

			if( olaVO.getCnic() != null && !olaVO.getCnic().equals("") )
//				accountHolderModel.setCnic( EncryptionUtil.encryptPin( olaVO.getCnic() )) ;
				accountHolderModel.setCnic(olaVO.getCnic());
			
			accountHolderModel.setMobileNumber(olaVO.getMobileNumber());
		
			
			if( olaVO.getDob() != null  )
			{
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//				accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(olaVO.getDob())));
				accountHolderModel.setDob(dateFormat.format(olaVO.getDob()));
			}
			
			if( olaVO.getPayingAccNo() != null && !olaVO.getPayingAccNo().equals("") )
				accountModel.setAccountNumber( EncryptionUtil.encryptAccountNo(olaVO.getPayingAccNo() )) ;
			
			
			accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel) ;
		}
		return accountModel ;			
	}
	
	
//	private HashMap<String,AccountsWithStatsListViewModel> prepareOLAVOHashMap( List<AccountsWithStatsListViewModel> accountModelList ) throws Exception
//	{		
//		HashMap<String,AccountsWithStatsListViewModel> olaVOList = new HashMap<String,AccountsWithStatsListViewModel>() ;
//		
//		if( accountModelList != null && accountModelList.size() > 0 )
//		{
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//			
//			for( AccountsWithStatsListViewModel accountModel : accountModelList )
//			{
//				
//				accountModel.setCnic( EncryptionUtil.decryptPin(accountModel.getCnic())) ;
//				accountModel.setDateOfBirth(dateFormat.parse( EncryptionUtil.decryptPin(accountModel.getDob()))) ;
//				accountModel.setAccountNumber( EncryptionUtil.decryptPin(accountModel.getAccountNumber())) ;
//				accountModel.setBalance( (EncryptionUtil.decryptPin(accountModel.getBalance()) ));
//				
//				olaVOList.put(accountModel.getAccountNumber(), accountModel) ;
//			}
//		}
//			
//		return olaVOList ;			
//	}
	
	
	private HashMap<String,OLAVO> prepareOLAVOHashMap( List<AccountsWithStatsListViewModel> accountModelList ) throws Exception
	{		
		HashMap<String,OLAVO> olaVOList = new HashMap<String,OLAVO>() ;
		DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
		
		if( accountModelList != null && accountModelList.size() > 0 )
			for( AccountsWithStatsListViewModel accountModel : accountModelList )
			{
				OLAVO olaVo = new OLAVO() ;
//				if( accountModel.getAccountHolderIdAccountHolderModel() != null )
				{
					olaVo.setFirstName(accountModel.getFirstName()) ;
					olaVo.setLastName(accountModel.getLastName()) ;
					olaVo.setMiddleName(accountModel.getMiddleName()) ;
					olaVo.setFatherName(accountModel.getFatherName()) ;
					olaVo.setAddress(accountModel.getAddress()) ;
					
					olaVo.setMobileNumber(accountModel.getMobileNumber()) ;
					olaVo.setLandlineNumber(accountModel.getLandlineNumber()) ;
//					olaVo.setCnic( EncryptionUtil.decryptPin(accountModel.getCnic())) ;
					olaVo.setCnic(accountModel.getCnic()) ;
						
		//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//					olaVo.setDob(dateFormat.parse( EncryptionUtil.decryptPin(accountModel.getDob()))) ;
					olaVo.setDob(dateFormat.parse(accountModel.getDob()));
					olaVo.setPayingAccNo( EncryptionUtil.decryptAccountNo(accountModel.getAccountNumber())) ;
					olaVo.setStatusId(accountModel.getStatusId()) ;
						
					if( accountModel.getStatusId() == 1 )
						olaVo.setStatusName( "Active" ) ;
					else if( accountModel.getStatusId() == 2 )
						olaVo.setStatusName( "In-Active" ) ;
					else if( accountModel.getStatusId() == 3 )
						olaVo.setStatusName( "Blocked" ) ;
					
					olaVo.setAccountId(accountModel.getAccountId()) ;
//					olaVo.setBalance( Double.parseDouble(EncryptionUtil.decryptPin(accountModel.getBalance()) ));
					olaVo.setBalance( Double.parseDouble(accountModel.getBalance()));
					
					
					olaVo.setBalanceDisbursed( (accountModel.getBalanceDisbursed()) ) ;
					olaVo.setBalanceReceived( accountModel.getBalanceReceived() ) ;
//					olaVo.setEndDayBalance( (EncryptionUtil.decryptPin(accountModel.getEndDayBalance() ))) ;
					olaVo.setEndDayBalance(accountModel.getEndDayBalance());
//					olaVo.setStartDayBalance( (EncryptionUtil.decryptPin(accountModel.getStartDayBalance() ))) ;
					olaVo.setStartDayBalance(accountModel.getStartDayBalance()) ;
					
//					olaVo.setStatsDate( accountModel.getStatsDate() ) ;
					
//					try
//					{
//						System.out.println(Long.parseLong(EncryptionUtil.decryptPin(accountModel.getStartDayBalance())));
//					}
//					catch (RuntimeException e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					olaVo.setDailyAccountStatsId( accountModel.getDailyAccountStatsId()) ;
					
										
					olaVOList.put(olaVo.getPayingAccNo(), olaVo) ;
				}
			}
		
			
		return olaVOList ;			
	}

	private HashMap<String,OLAVO> prepareOLAVOHashMapForRange( List<Object> accountModelList ) throws Exception
	{		
		HashMap<String,OLAVO> olaVOList = new HashMap<String,OLAVO>() ;
		DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
		
		if( accountModelList != null && accountModelList.size() > 0 )
			for( Object accountModel : accountModelList )
			{				
				{
					OLAVO olaVo = new OLAVO() ;
	//				if( accountModel.getAccountHolderIdAccountHolderModel() != null )
					{
//						olaVo.setFirstName(accountModel.getFirstName()) ;
//						olaVo.setLastName(accountModel.getLastName()) ;
//						olaVo.setMiddleName(accountModel.getMiddleName()) ;
//						olaVo.setFatherName(accountModel.getFatherName()) ;
//						olaVo.setAddress(accountModel.getAddress()) ;
						
//						olaVo.setMobileNumber(accountModel.getMobileNumber()) ;
//						olaVo.setLandlineNumber(accountModel.getLandlineNumber()) ;
//						olaVo.setCnic( EncryptionUtil.decryptPin(accountModel.getCnic())) ;
							
			//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						
//						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//						olaVo.setDob(dateFormat.parse( EncryptionUtil.decryptPin(accountModel.getDob()))) ;
//						olaVo.setPayingAccNo( EncryptionUtil.decryptPin(((Object[])accountModel)[3].toString())) ;
						olaVo.setPayingAccNo(((Object[])accountModel)[3].toString());
//						olaVo.setStatusId(accountModel.getStatusId()) ;
							
//						if( accountModel.getStatusId() == 1 )
//							olaVo.setStatusName( "Active" ) ;
//						else if( accountModel.getStatusId() == 2 )
//							olaVo.setStatusName( "In-Active" ) ;
//						else if( accountModel.getStatusId() == 3 )
//							olaVo.setStatusName( "Blocked" ) ;
						
						olaVo.setAccountId((Long)((Object[])accountModel)[0]) ;
//						olaVo.setBalance( Double.parseDouble(EncryptionUtil.decryptPin( ((Object[])accountModel)[4].toString()) ) ) ;
						olaVo.setBalance( Double.parseDouble(((Object[])accountModel)[4].toString())) ;
						
						
//						olaVo.setBalanceDisbursed( (accountModel.getBalanceDisbursed()) ) ;
//						olaVo.setBalanceReceived( accountModel.getBalanceReceived() ) ;
						
						if( ((Object[])accountModel)[2] != null )
//							olaVo.setEndDayBalance( (EncryptionUtil.decryptPin(((Object[])accountModel)[2].toString() ))) ;
							olaVo.setEndDayBalance(((Object[])accountModel)[2].toString());

//						olaVo.setStartDayBalance( (EncryptionUtil.decryptPin(((Object[])accountModel)[1].toString() ))) ;
						olaVo.setStartDayBalance(((Object[])accountModel)[1].toString()) ;
//						olaVo.setDailyAccountStatsId( accountModel.getDailyAccountStatsId()) ;
											
						olaVOList.put(olaVo.getPayingAccNo(), olaVo) ;
					}
				}
				
				
			}
		
			
		return olaVOList ;			
	}

	
	private AccountHolderModel generateAccountHolderModel( OLAVO olaVO ) throws Exception
	{
		Date dateNow = new Date();
		
		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
		
		accountHolderModel.setFirstName(olaVO.getFirstName());
		accountHolderModel.setMiddleName(olaVO.getMiddleName());
		accountHolderModel.setLastName(olaVO.getLastName());
		accountHolderModel.setFatherName(olaVO.getFatherName());
//		accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic()));
		accountHolderModel.setCnic(olaVO.getCnic());
		accountHolderModel.setAddress(olaVO.getAddress());
		accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
		accountHolderModel.setMobileNumber(olaVO.getMobileNumber());
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//		accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(olaVO.getDob())) );
		accountHolderModel.setDob( dateFormat.format(olaVO.getDob()));
		accountHolderModel.setLandlineNumber(olaVO.getLandlineNumber());
		accountHolderModel.setCreatedOn(dateNow);
		accountHolderModel.setUpdatedOn(dateNow);
		
		AccountModel accountModel = new AccountModel() ;
		
		accountModel.setAccountNumber( AccountNumberGenerator.getAccountNumber()) ;
//		accountModel.setStatusId(StatusConstants.ACTIVE) ;
		accountModel.setCreatedOn(dateNow);
		accountModel.setUpdatedOn(dateNow);
		accountModel.setVersionNo(0);
		accountModel.setCustomerAccountTypeModel(new OlaCustomerAccountTypeModel(olaVO.getCustomerAccountTypeId()));
		
		accountModel.setStatusId(olaVO.getStatusId()) ;
		
		if( olaVO.getBalance() != null && olaVO.getBalance() > 0 )
//			accountModel.setBalance( EncryptionUtil.encryptPin(String.valueOf(olaVO.getBalance())) );
			accountModel.setBalance(String.valueOf(olaVO.getBalance()));
		else
//			accountModel.setBalance(EncryptionUtil.encryptPin("0"));
			accountModel.setBalance("0");
		
//		olaVO.setBalance( Double.parseDouble(EncryptionUtil.decryptPin(accountModel.getBalance()) ));
		olaVO.setBalance( Double.parseDouble(accountModel.getBalance()));
		
		accountHolderModel.addAccountHolderIdAccountModel(accountModel) ;
		
		return accountHolderModel ;			
	}
	
	private AccountModel generateAccountModel( OLAVO olaVO ) throws Exception
	{		
		AccountModel accountModel = new AccountModel() ;
		
		accountModel.setAccountNumber( EncryptionUtil.encryptAccountNo(olaVO.getPayingAccNo())) ;
			
		return accountModel ;			
	}
	
	private List<OLAVO> prepareOLAVOList( List<AccountModel> accountModelList ) throws Exception
	{		
		List<OLAVO> olaVOList = new ArrayList<OLAVO>() ;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		if( accountModelList != null && accountModelList.size() > 0 )
			for( AccountModel accountModel : accountModelList )
			{
				OLAVO olaVo = new OLAVO() ;
				if( accountModel.getAccountHolderIdAccountHolderModel() != null )
				{
					olaVo.setFirstName(accountModel.getAccountHolderIdAccountHolderModel().getFirstName()) ;
					olaVo.setLastName(accountModel.getAccountHolderIdAccountHolderModel().getLastName()) ;
					olaVo.setMiddleName(accountModel.getAccountHolderIdAccountHolderModel().getMiddleName()) ;
					olaVo.setFatherName(accountModel.getAccountHolderIdAccountHolderModel().getFatherName()) ;
					olaVo.setAddress(accountModel.getAccountHolderIdAccountHolderModel().getAddress()) ;
					
					try{
						logger.info("[OLAServiceImpl.prepareolAVOList] AccountHolderID:"+ accountModel.getAccountHolderIdAccountHolderModel().getAccountHolderId() + ".\nCNIC:" + accountModel.getAccountHolderIdAccountHolderModel().getCnic() + ".\nDOB:" + accountModel.getAccountHolderIdAccountHolderModel().getDob());
						
//						String balance = EncryptionUtil.decryptPin(accountModel.getBalance());
//						String cnic = EncryptionUtil.decryptPin(accountModel.getAccountHolderIdAccountHolderModel().getCnic());
//						String dob = EncryptionUtil.decryptPin(accountModel.getAccountHolderIdAccountHolderModel().getDob());
//						
						String balance = accountModel.getBalance();
						String cnic = accountModel.getAccountHolderIdAccountHolderModel().getCnic();
						String dob = accountModel.getAccountHolderIdAccountHolderModel().getDob();
						
						logger.info("[OLAServiceImpl.prepareolAVOList] AccountHolderID:"+ accountModel.getAccountHolderIdAccountHolderModel().getAccountHolderId() + ". CNIC:" + cnic + ". DOB:" + dob);
						
						olaVo.setBalance( Double.parseDouble(balance) );
						olaVo.setCnic( cnic ) ;
						olaVo.setDob( dateFormat.parse( dob ) ) ;
					}catch(ArrayIndexOutOfBoundsException arrExc){
						logger.error("$$$$$$$ Decryption Failed for account ID: "+accountModel.getAccountId() + " Exception in OlaServiceImpl :: " + arrExc);
					}
					olaVo.setLandlineNumber(accountModel.getAccountHolderIdAccountHolderModel().getLandlineNumber()) ;
					olaVo.setMobileNumber(accountModel.getAccountHolderIdAccountHolderModel().getMobileNumber()) ;
					olaVo.setPayingAccNo( EncryptionUtil.decryptAccountNo(accountModel.getAccountNumber())) ;
					olaVo.setStatusId(accountModel.getStatusId()) ;
								
					olaVo.setStatusName( accountModel.getStatusIdStatusModel().getStatus() ) ;
					olaVo.setAccountId(accountModel.getAccountId()) ;
						
					olaVo.setCustomerAccountTypeId(accountModel.getCustomerAccountTypeId());
					
					olaVOList.add(olaVo) ;
				}
			}
		
			
		return olaVOList ;			
	}
	public OLAVO verifyLimits(OLAVO olaVO)throws Exception{
		if(olaVO.getTransactionTypeId()!=null){
			if(olaVO.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT.toString())){
			return this.accountFacade.verifyCreditLimits(olaVO);
		}else if(olaVO.getTransactionTypeId().equals(TransactionTypeConstants.DEBIT.toString())){
			return this.accountFacade.verifyDebitLimits(olaVO);
		}else{
			return null;
			}
		}
		return null;
	}
	
	public OLAVO verifyWalkinCustomerThroughputLimits(OLAVO olaVO)throws Exception{
			return this.accountFacade.verifyWalkinCustomerThroughputLimits(olaVO);
	}

	public OLAVO saveWalkinCustomerLedgerEntry(OLAVO olaVO) throws Exception {
		/*if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- checkBalance ------ ******* called ");
		}
		
		try
		{	
			AccountHolderModel accountHolderModel = new AccountHolderModel();
			accountHolderModel.setCnic(olaVO.getCnic());
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(accountHolderModel) ;			
			accountHolderModel = (AccountHolderModel)this.accountFacade.loadAccountHolder(baseWrapper).getBasePersistableModel() ;
			
			
			AccountModel accountModel = new AccountModel();
			accountModel.setAccountHolderId(accountHolderModel.getaccounti)
			baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(accountModel) ;			
			accountModel = (AccountModel)this.accountFacade.loadAccount(baseWrapper).getBasePersistableModel() ;
			
			if( accountModel == null  )
			{
				olaVO.setResponseCode("03");
				
			}
			else if( accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue() )
			{
				olaVO.setResponseCode("02");				
			}
			else
			{
				olaVO.setResponseCode("00");
				olaVO.setBalance( Double.parseDouble( EncryptionUtil.decryptPin(accountModel.getBalance()))) ;								
			}

		}
		catch (Exception e)
		{
			olaVO.setResponseCode("06");
			e.printStackTrace();
		}	
		
		if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- checkBalance ------ ******* end ");
		}
		
		return olaVO;*/
		/*AccountHolderModel accountHolderModel = new AccountHolderModel();
		accountHolderModel.setCnic( EncryptionUtil.encryptPin(olaVO.getCnic()));
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();			
		baseWrapper.setBasePersistableModel(accountHolderModel) ;			
		accountHolderModel = (AccountHolderModel)this.accountFacade.loadAccountHolder(baseWrapper).getBasePersistableModel() ;
		*/
		return this.accountFacade.saveWalkinCustomerLedgerEntry(olaVO);
	}

	public OLAVO createLedgerRollbackEntries(OLAVO olavo) throws Exception{
		return accountFacade.makeLedgerRollbackEntriesRequiresNewTransaction(olavo);
	}
	
	public OLAVO rollbackWalkinCustomer(OLAVO olaVO) throws Exception{
		if (logger.isDebugEnabled()){
			logger.debug(" ******* ---- rollbackWalkinCustomer() ------ ******* called ");
		}
		
		try
		{
//			Commented as LedgerModel is now loaded on the basis of authCode 
//			String cnic = olaVO.getCnic();
//			if(null != cnic && StringUtil.isWalkinCustomerCNIC(cnic)){
//				AccountModel accountModel = accountFacade.getAccountModelByCNIC(cnic);
//				olaVO.setAccountId(accountModel.getAccountId());
//			}else{
//				olaVO.setResponseCode("06") ;
//				return olaVO;
//			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();
			ledgerModel.setAuthCode(olaVO.getAuthCode()) ;
//			ledgerModel.setAccountId(olaVO.getAccountId()) ; 
			baseWrapper.setBasePersistableModel(ledgerModel) ;
			
			baseWrapper = this.ledgerFacade.loadLedgerEntry(baseWrapper) ;
			ledgerModel = (com.inov8.integration.common.model.LedgerModel)baseWrapper.getBasePersistableModel();
			
			if( ledgerModel == null )
			{
				if (logger.isDebugEnabled()){
					logger.debug(" No ledger entry found for auth-code " + olaVO.getAuthCode() + ". Exiting OLA with Response code 06");
				}
				olaVO.setResponseCode("06") ;
				return olaVO;
			}
			
			olaVO.setBalance(ledgerModel.getTransactionAmount()) ;
			
			olaVO.setResponseCode("00");
			
			ledgerModel.setReasonId(olaVO.getReasonId());
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(ledgerModel);
			this.ledgerFacade.saveLedgerEntry(baseWrapper);
		}
		catch (Exception e)
		{			
			logger.error("Error in OLAServiceImpl.rollBackWalkInCustomer() :: " + e);
			olaVO.setResponseCode("06") ;
		}
		
		if (logger.isDebugEnabled()){
			logger.debug(" ******* ---- rollbackWalkinCustomer() ------ ******* end ");
		}
		
		return olaVO;
	}
	
	public OLAVO updateLedger(OLAVO olaVO) throws Exception{
		if (logger.isDebugEnabled()){
			logger.debug(" ******* ----- updateLedger() ------ ******* called ");
		}
		
		try
		{
			
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();
			ledgerModel.setAuthCode(olaVO.getAuthCode()) ;
//			ledgerModel.setAccountId(olaVO.getAccountId()) ; 
			baseWrapper.setBasePersistableModel(ledgerModel) ;
			
			baseWrapper = this.ledgerFacade.loadLedgerEntry(baseWrapper) ;
			ledgerModel = (com.inov8.integration.common.model.LedgerModel)baseWrapper.getBasePersistableModel();
			
			if( ledgerModel == null )
			{
				logger.error(" No ledger entry found for auth-code " + olaVO.getAuthCode() + ". Exiting OLA with Response code 06");
				olaVO.setResponseCode("06") ;
				return olaVO;
			}
			
//			No need to update TransactionAmount - so skipping
//			if(null != olaVO.getBalance()){
//				ledgerModel.setTransactionAmount(olaVO.getBalance());
//			}else{
//				olaVO.setBalance(ledgerModel.getTransactionAmount()) ;
//			}
			
			ledgerModel.setReasonId(olaVO.getReasonId());
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(ledgerModel);
			this.ledgerFacade.saveLedgerEntry(baseWrapper);
			olaVO.setResponseCode("00");
		}
		catch (Exception e)
		{			
			logger.error("Error in OLAServiceImpl.updateLedger() :: " + e);
			olaVO.setResponseCode("06") ;
		}
		
		if (logger.isDebugEnabled()){
			logger.debug(" ******* ------ updateLedger() ------ ******* end ");
		}
		
		return olaVO;
	}
	
	public void setLedgerFacade(LedgerFacade ledgerFacade)
	{
		this.ledgerFacade = ledgerFacade;		
	}
	public void setLimitFacade(LimitFacade limitFacade) {
		this.limitFacade = limitFacade;
	}

	public void check()
	{
		
		OLAVO olaVo = new OLAVO() ;
//		olaVo.setFirstName("iuo") ;
//		olaVo.setLastName("AA") ;
//		olaVo.setMiddleName("AA") ;
//		olaVo.setFatherName("AA") ;
//		olaVo.setAddress("AA") ;
//		
//		olaVo.setMobileNumber("03004445454") ;
//		olaVo.setLandlineNumber("231321231") ;
		olaVo.setCnic("1111111112222") ;
//		olaVo.setDob(new Date()) ;
//		
//		olaVo.setReasonId(1l);
//		olaVo.setStatusId(1l);
//		olaVo.setMicrobankTransactionCode("QTXHJFHFK") ;
		
//		olaVo.setBalance(100d);
//		olaVo.setPayingAccNo("0000000070");
//		olaVo.setTransactionTypeId("1");
//		olaVo.setAccountId(5053L);
		
		try
		{
//			olaVo = this.createAccount(olaVo) ;
			
//			List<OLAVO> l = this.getAllAccounts();
			
			
//			this.checkBalance(olaVo) ;
			
//			olaVo = this.transaction(olaVo) ;
//			olaVo = this.changeAccountStatus(olaVo) ;
//			olaVo = this.getAccountInfo(olaVo) ;
//			olaVo = this.createAccount(olaVo) ;
			
//			System.out.println( "Response code : " + olaVo.getResponseCode() );
//			System.out.println( "First Name : " + olaVo.getFirstName() );
//			System.out.println( "DOB : " + olaVo.getDob() );
//			System.out.println( "CNIC : " + olaVo.getCnic() );
			
			
			 
			
			
			//List<OLAVO> l = this.getAllAccounts(olaVo);
			
			
		}
		catch (Exception e)
		{			
			logger.error("Error in OLAServiceImpl.check() :: " + e);
		}
		
	}
	
	public static void main(String []a) throws Exception
	{
		
		
		
		
		
//		System.out.println( TransactionTypeConstants.CREDIT.toString() );
//		System.out.println( new Date().toString() );
		
		
		/*balance 0 = IBUH99Y96NLPQueYlh+neebrojXD5ntgBcyEuPH6OFVn3aHAi39sFKOchXrWAOWLwBGEgQF3olC4
		KemeAdn8I5ytZrkv9BvZwrR4Y5dHDzFBRIYrJzWSZNjN8rH55N9shOd6s0pkwnDF8oEBnsivkHqZ
		ZXxIcaMHwleAVPZ5TO4=*/
		
//		System.out.println( EncryptionUtil.encryptPin("500000") );
//		System.out.println( EncryptionUtil.encryptPin("5") );
//		System.out.println(  );
//		System.out.println( EncryptionUtil.encryptAccountNo("1") );
		/*String balance = "Ce2LdewMUX5F9+7JfHS7PT45KGR2waLaYH5mYpucIvVW3fgElfK2dKw6GR1MzoLsTF++WQrWalZ4"+
"bl1ypxO4Ul405nXcpckQ+/KfVDUuW91x5/9rxuHHUvC3mAhgGxCulkv4+W/k6vkcJl6/UkpTKxNM"+
"bPILGSLGbC2ATuEzuM0=";*/


		
//		System.out.println( EncryptionUtil.decryptPin("lN3Y6VsIf201YqmAkc9yMF3MkA5IDyJH6M/FQzPCEWi0jeUBrKeyL7TPCbCKaBnwSWOGALAPkdnd"
//+"ZydjJL1DLYWmiciecwrgG6lOdYG6OEo+UA/GzlnRdKOUmmkv8QdxiiQWUJgVRtji/i30iNqYnQml"
//+"mHwd7lB7s56SLragwn8=") );
//		
//		System.out.println( EncryptionUtil.decryptPin(" ") );
//		
//		DecimalFormat df2 = new DecimalFormat("###,###.00");
//		Double.parseDouble("4.9794194E7");
//        System.out.println(df2.format( Double.parseDouble("4.9794194E7") ));

		
	}


	@Override
	public OLAVO getAccountTitle(OLAVO olaVO) throws Exception {
		
		//if (logger.isDebugEnabled())
		{
			logger.debug(" ******* ---- getAccountTitle ------ ******* called ");
		}
		
		AccountModel accountModel = generateAccountModel(olaVO); 
		BaseWrapper baseWrapper = new BaseWrapperImpl();			
		baseWrapper.setBasePersistableModel(accountModel) ;		
		baseWrapper = accountFacade.loadAccount(baseWrapper);
		accountModel = (AccountModel)baseWrapper.getBasePersistableModel();
		
		if(accountModel != null){
			olaVO.setReceivingAccountId(accountModel.getAccountId());
			olaVO.setAccountId(accountModel.getAccountId());
			olaVO.setCustomerAccountTypeId(accountModel.getCustomerAccountTypeId());
			olaVO.setProductId(ProductConstantsInterface.IBFT);
			olaVO.setTransactionDateTime(new Date());
			try
			{			
				olaVO = this.accountFacade.verifyCreditLimits(olaVO);
				if(olaVO.getResponseCode().equalsIgnoreCase("00")){					
					olaVO.setFirstName(accountModel.getAccountHolderIdAccountHolderModel().getFirstName());
					olaVO.setLastName(accountModel.getAccountHolderIdAccountHolderModel().getLastName());
//					Double balance = Double.valueOf(EncryptionUtil.decryptPin(accountModel.getBalance()));
					Double balance = Double.valueOf(accountModel.getBalance());
					olaVO.setBalance(balance);
				}
			}
			catch (Exception e)
			{
				olaVO.setResponseCode("01");
				e.printStackTrace();
			}
		}

		logger.debug(" ******* ---- getAccountTitle ------ ******* end ");
		return olaVO;
	}

	@Override
	public OLAVO makeWalletTransactionReversal(OLAVO olavo) throws Exception {
		return null;
	}


}
