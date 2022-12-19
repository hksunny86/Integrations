package com.inov8.microbank.server.service.integration.dispenser;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.RetailPaymentVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class RetailPaymentDispenser extends BillPaymentProductDispenser
{

	private ProductIntgModuleInfoManager productIntgModuleInfoManager;
	private CommissionManager commissionManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private SettlementManager settlementManager;
	private ProductManager productManager;
	private GenericDao genericDAO;
	private ProductUnitManager productUnitManager;
	private FailureLogManager failureLogManager;
	private AppUserManager appUserManager;
	private ShipmentManager shipmentManager;
	private ApplicationContext ctx;
	private AppUserDAO appUserDao;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private AbstractFinancialInstitution olaFinancialInst ;
	private AbstractFinancialInstitution phoenixFinancialInst;


	private final Log logger = LogFactory.getLog(this.getClass());

	public RetailPaymentDispenser(CommissionManager commissionManager,
			SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
			ProductManager productManager, AppUserManager appUserManager,
			ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO,ApplicationContext ctx)
	{
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO;
		this.ctx = ctx ;


		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));		
		this.appUserDao = (AppUserDAO) ctx.getBean("appUserDAO");
		this.smartMoneyAccountDAO = (SmartMoneyAccountDAO) ctx.getBean("smartMoneyAccountDAO") ;
		this.olaFinancialInst = (OLAVeriflyFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl") ;
		this.phoenixFinancialInst = (PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
		
		try{//Added by mudassir
			this.customerManager = (CustTransManager) ctx.getBean("custTransManager");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * doSale
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doSale of RetailPaymentDispenser");
		}
		
		try
		{
			RetailPaymentVO p2PVO = (RetailPaymentVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setRetailerContactId(p2PVO.getRetailerId()) ;
			sma.setDeleted(false);			
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, null);

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
								AppUserModel appUser = new AppUserModel();
				AppUserModel appUserTemp = ThreadLocalAppUser.getAppUserModel() ;
				appUser.setAppUserId(p2PVO.getAppUserId()) ;
//				appUser.setCustomerId(p2PVO.getCustomerId());
//				ThreadLocalAppUser.setAppUserModel(appUser) ; Might have to uncomment it...be very careful
				
				p2PVO.setTransactionDateTime(new Date());
				CustomerModel cModel = new CustomerModel();
				cModel.setCustomerId(workFlowWrapper.getCustomerAppUserModel().getCustomerId());
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(cModel);
				baseWrapper =  customerManager.loadCustomer(baseWrapper);//Added by mudassir
				cModel = (CustomerModel)baseWrapper.getBasePersistableModel();
				p2PVO.setCustomerAccountTypeId(cModel.getCustomerAccountTypeId()); // maqsood Shahzad -- need to set this dynamically
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				
				switchWrapper.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
				
				switchWrapper.setBasePersistableModel(sma) ;
				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				workFlowWrapper.setProductVO(p2PVO);
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				
				switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				
				if(null != sma.getPaymentModeId() && sma.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue())
				{
					switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
					logger.info("[RetailPaymentDispenser.doSale] Going to Debit/Credit Phoenix Accounts. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
								" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));

					
				//Maqsood -- reversed the order of Balance Check and FT in case of Cash Withdrawal Transaction	
					SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
					newSwitchWrapper.setBasePersistableModel(sma);
					newSwitchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
					logger.info("[RetailPaymentDispenser.doSale] Going to Check Agent Balance. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));

					newSwitchWrapper = phoenixFinancialInst.checkBalanceWithoutPin(newSwitchWrapper);

					switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
					logger.info("[RetailPaymentDispenser.doSale] Going to Debit/Credit Phoenix Accounts. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));
					switchWrapper = phoenixFinancialInst.debitCreditAccount(switchWrapper);
					p2PVO.setBalance(newSwitchWrapper.getBalance() - (switchWrapper.getTransactionAmount()));
					
					
					
					
				}
				else
				{
					logger.info("[RetailPaymentDispenser.doSale] Going to Credit OLA Account. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
								" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));
					switchWrapper = olaFinancialInst.debit(switchWrapper) ;
					
					
				}
				
				
//				if ( workFlowWrapper.getProductModel().getProductId() == 50006 )
//				{
//					
//					
//					TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
//					
//					
//					
//					switchWrapper = olaFinancialInst.checkBalance(switchWrapper) ;
//					p2PVO.setBalance(switchWrapper.getBalance() + p2PVO.getAmount());
//					
					switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
//					
//					
//					
//				}
				
				
				
				ThreadLocalAppUser.setAppUserModel(appUserTemp) ;
				workFlowWrapper.setSwitchWrapper(switchWrapper);
			}
		}		
		catch (Exception e)
		{			
			logger.error("[RetailPaymentDispenser.doSale] Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". TransactionID:" + " TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()) + " Exception Msg:" + e.getMessage());
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of RetailPaymentDispenser");
		}

		return workFlowWrapper;
	}


	/**
	 * getBillInfo
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getBillInfo of RetailPaymentDispenser");
		}
		
		RetailPaymentVO p2PVO = (RetailPaymentVO) workFlowWrapper.getProductVO();
		String mobileNo = p2PVO.getMobileNo() ;
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		AppUserModel appUser = new AppUserModel() ;
		appUser.setMobileNo(mobileNo);
		appUser.setAppUserTypeId( 3l );
		CustomList<AppUserModel> appUserList = this.appUserDao.findByExample(appUser, null, null, exampleHolder);

		if( ThreadLocalAppUser.getAppUserModel().getMobileNo().equals( p2PVO.getMobileNo() ) && null != workFlowWrapper.getProductModel() && workFlowWrapper.getProductModel().getProductId() != 50006  )
		{
			throw new FrameworkCheckedException( "Own account transfer is not allowed." ) ;
		}

		
		if (null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0) 
		{
			appUser = ((AppUserModel)appUserList.getResultsetList().get(0)) ;
			
			p2PVO.setCustomerName( appUser.getFirstName() + " " + appUser.getLastName() ) ;
			p2PVO.setRetailerId( appUser.getRetailerContactId() ) ;
			p2PVO.setAppUserId( appUser.getAppUserId() ) ;
			
			try
			{
				p2PVO.setMfsId( ((UserDeviceAccountsModel)((List)appUser.getAppUserIdUserDeviceAccountsModelList()).get(0)).getUserId() ) ;
			}
			catch (Exception e)
			{
				logger.error("[RetailPaymentDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + e.getMessage());
				
			}
			
			
			RetailerModel retailerModel = new RetailerModel();
			
			retailerModel.setRetailerId( appUser.getRelationRetailerContactIdRetailerContactModel().getRetailerId() ) ;
			
			List<RetailerModel> list = this.genericDAO.findEntityByExample(retailerModel, null);
			if(list != null && list.size() > 0)
			{
				p2PVO.setRetailerName( ((RetailerModel)list.get(0)).getName() ) ;
			}
			
			
		}
		else
		{
			if ( workFlowWrapper.getProductModel().getProductId() == 50006 )
			{
				logger.error("[RetailPaymentDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							 ". Exception: Invalid mobile number or branchless banking account. Please enter the correct number or call helpline for more information.");
				throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. Please enter the correct number or call helpline for more information." ) ;
			}
			
			logger.error("[RetailPaymentDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
					 ". Exception: Invalid mobile number or branchless banking account. No such retailer/account exists for the entered number. Please enter the correct number or call helpline for more information.");
		
			throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. No such retailer/account exists for the entered number. Please enter the correct number or call helpline for more information." ) ;
		}
		
//		if ( workFlowWrapper.getProductModel().getProductId() == 50006 )
		{
			
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setRetailerContactId(p2PVO.getRetailerId()) ;
			sma.setDeleted(false);			
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, null);

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				try
				{
					sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
					
					appUser = new AppUserModel();
					AppUserModel appUserTemp = ThreadLocalAppUser.getAppUserModel() ;
					appUser.setAppUserId(p2PVO.getAppUserId()) ;
					SwitchWrapper switchWrapper = new SwitchWrapperImpl();
					switchWrapper.setBasePersistableModel(sma) ;
					workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
					switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
					if(null != workFlowWrapper.getAccountInfoModel() && null != workFlowWrapper.getAccountInfoModel().getOldPin() &&
							!"".equals(workFlowWrapper.getAccountInfoModel().getOldPin()))
					{
						switchWrapper.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
					}
					TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
					if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER.longValue())
					{

					ThreadLocalAppUser.setAppUserModel(appUser) ;

					
					
					// Checking the balance of the agent in case of CW
					
					
					
					
					
					
					switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
					switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
					switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserTemp.getNic());
//					TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
					
					if(null != sma.getPaymentModeId() && sma.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue())
					{
//						switchWrapper = phoenixFinancialInst.checkBalance(switchWrapper);
					}
					else
					{
						switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
						logger.info("[RetailPaymentDispenser.getBillInfo] Going to Check balance for SmartMoneyAccountID:" + ((SmartMoneyAccountModel)switchWrapper.getBasePersistableModel()).getSmartMoneyAccountId() + " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
						switchWrapper = olaFinancialInst.checkBalance(switchWrapper) ;
					}
					
					
					
					
					
					
//						switchWrapper = olaFinancialInst.checkBalance(switchWrapper) ; //commented by Maqsood due to moving to Phoenix
						p2PVO.setBalance(switchWrapper.getBalance() + p2PVO.getAmount());
					}
					switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
					ThreadLocalAppUser.setAppUserModel(appUserTemp) ;
				}
				catch (Exception e)
				{
					logger.error("[RetailPaymentDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + e.getMessage());
					throw new FrameworkCheckedException( "Your request cannot be processed at the moment. Please try again later." ) ;
				}
			}
			
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getBillInfo of RetailPaymentDispenser");
		}
		return workFlowWrapper;
	}

	/**
	 * rollback
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		return null;
	}

	/**
	 * verify
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		return workFlowWrapper;
	}

	
	
	


}
