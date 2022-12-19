package com.inov8.microbank.server.service.integration.dispenser;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
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
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class AccountToCashDispenser extends BillPaymentProductDispenser
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
	private AppUserDAO appUserDao;
	private ApplicationContext ctx;
	private AbstractFinancialInstitution olaFinancialInst ;
	private AbstractFinancialInstitution phoenixFinancialInst ;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;


	private final Log logger = LogFactory.getLog(this.getClass());

	public AccountToCashDispenser(CommissionManager commissionManager,
								  SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
								  ProductManager productManager, AppUserManager appUserManager,
								  ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO, ApplicationContext ctx)
	{
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO;
		this.appUserDao = (AppUserDAO) ctx.getBean("appUserDAO");
		this.ctx = ctx ;

		this.smartMoneyAccountDAO = (SmartMoneyAccountDAO) ctx.getBean("smartMoneyAccountDAO") ;
		this.olaFinancialInst = (OLAVeriflyFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl") ;
		this.phoenixFinancialInst = (PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));		
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
			logger.debug("Inside doSale of AccountToCashDispenser");
		}
		
		try
		{
			AccountToCashVO acctToCashVO = (AccountToCashVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setCustomerId(acctToCashVO.getCustomerId()) ;
						
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, null);

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
				
				// Change 1 Start
				// Date: 07-11-12
				// Change Owner: Rashid Mahmood
				// Description: Following 4 lines are commented and one line is written.
				
//				AppUserModel appUser = new AppUserModel();
//				AppUserModel appUserTemp = ThreadLocalAppUser.getAppUserModel() ;
//				appUser.setCustomerId(acctToCashVO.getCustomerId());
//				ThreadLocalAppUser.setAppUserModel(appUser) ;
				
				ThreadLocalAppUser.getAppUserModel().setCustomerId(acctToCashVO.getCustomerId());
				
				//Change 1 End		
				
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				
				switchWrapper.setBasePersistableModel(sma) ;
				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
//				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+sma.getSmartMoneyAccountId()); //set customer's smart money account ID
				TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;

				switchWrapper.setOlavo(new OLAVO());
				switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

				logger.info("[AccountToCashDispenser.doSale] Going to Credit A2C Sundry A/C. Sender SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
				
				//credit Acccount To Cash sundry account in OLA
				switchWrapper = olaFinancialInst.accountToCashTransaction(switchWrapper);
				workFlowWrapper.setOlaSwitchWrapper_2(switchWrapper);
				
				
				//Phoenix FT
				switchWrapper = new SwitchWrapperImpl();
				switchWrapper.setBasePersistableModel(sma) ;
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.setIsAccountToCashLeg2(false);
				
				switchWrapper = phoenixFinancialInst.debitCreditAccount(switchWrapper) ;
				
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				
				// Change 2 Start
				// Date: 07-11-12
				// Change Owner: Rashid Mahmood
				// Description: Due to upper change 1 and to keep the previous state following 1 line is commented and one line is written.
				
				//ThreadLocalAppUser.setAppUserModel(appUserTemp) ;
				ThreadLocalAppUser.getAppUserModel().setCustomerId(null);
				
				// Change 2 End. 
				
			}
		}		
		catch (Exception e)
		{

			logger.error("[AccountToCashDispenser.doSale] Exception occured. Sender SmartMoneyAccountId" + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception: " + e.getMessage());
			
			if(e instanceof CommandException)
			{
				throw new WorkFlowException(e.getMessage());
			}
//			e.printStackTrace();
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of cashinproductdispenser");
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
			logger.debug("Inside getBillInfo of cashinproductdispenser");
		}
		AccountToCashVO acctToCashVO = (AccountToCashVO) workFlowWrapper.getProductVO();
		/*
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(acctToCashVO.getConsumerNo());
		appUserModel.setAppUserTypeId(2l);
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		
		
		
		appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<AppUserModel> appUserList = this.appUserDao.findByExample(appUserModel, null, null, exampleHolder);

		if (null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0) 
		{
			appUserModel = ((AppUserModel)appUserList.getResultsetList().get(0)) ;
			acctToCashVO.setAppUserId(appUserModel.getAppUserId());
			acctToCashVO.setCustomerId(appUserModel.getCustomerId());
			
		}
		else
		{
			throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. Please enter the correct number." ) ;
		}
		*/
		try{
			//verify throughput limits for walkin customer
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
	
			OLAVO olavo = new OLAVO();
			olavo.setBalance(acctToCashVO.getTxAmount());
			olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(acctToCashVO.getRecipientWalkinCNIC()));
			olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);//Walk-in
			olavo.setTransactionDateTime(new Date());
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
			
			switchWrapper.setOlavo(olavo);
			logger.info("[AccountToCashDispenser.getBillInfo()] Going to verify Walkin Customer Throughput Limits for CNIC:" + acctToCashVO.getRecipientWalkinCNIC());
			switchWrapper = olaFinancialInst.verifyWalkinCustomerThroughputLimits(switchWrapper);
		
		}catch (Exception e)
		{
				
			logger.info("[AccountToCashDispenser.getBillInfo()] Exception in verifying Walkin Customer Throughput Limits for CNIC:" + acctToCashVO.getRecipientWalkinCNIC() + " Message: " + e.getMessage());
			throw new WorkFlowException(e.getMessage());
//			e.printStackTrace();
//			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}
		

//		try
//		{
//			acctToCashVO.setMfsId( ((UserDeviceAccountsModel)((List)appUserModel.getAppUserIdUserDeviceAccountsModelList()).get(0)).getUserId() ) ;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		workFlowWrapper.setProductVO((ProductVO) acctToCashVO);

		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getBillInfo of AccountToCashProductDispenser");
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
