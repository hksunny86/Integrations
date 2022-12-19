package com.inov8.microbank.server.service.integration.dispenser;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
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

public class CashInProductDispenser extends BillPaymentProductDispenser
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

	public CashInProductDispenser(CommissionManager commissionManager,
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
		
		logger.info("Something went wrong with Cash Deposit if you are viewing this in logs... Dispenser not needed to be called");
		
		
		/*try
		{
			P2PVO p2PVO = (P2PVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setCustomerId(p2PVO.getCustomerId()) ;
			ExampleConfigHolderModel example = new ExampleConfigHolderModel();
			example.setEnableLike(Boolean.FALSE);
//			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, example);
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.loadCustomerSmartMoneyAccountByHQL(sma);
			

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
				
				// Change 1 Start
				// Date: 07-11-12
				// Change Owner: Rashid Mahmood
				// Description: Following 4 lines are commented and one line is written.
				
//				AppUserModel appUser = new AppUserModel();
//				AppUserModel appUserTemp = ThreadLocalAppUser.getAppUserModel();
//				appUser.setCustomerId(p2PVO.getCustomerId());
//				ThreadLocalAppUser.setAppUserModel(appUser) ;
				
				ThreadLocalAppUser.getAppUserModel().setCustomerId(p2PVO.getCustomerId());
				
				// Change 1 End				
				
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				
				switchWrapper.setBasePersistableModel(sma) ;
				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+sma.getSmartMoneyAccountId()); //set customer's smart money account ID
				TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				if(null != sma.getPaymentModeId() && sma.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue())
				{
				
					switchWrapper = phoenixFinancialInst.debitWithoutPin(switchWrapper) ;
					switchWrapper = phoenixFinancialInst.checkBalanceWithoutPin(switchWrapper) ;
				}
				else
				{
					if(workFlowWrapper.getProductModel().getProductId() == 50002L){
						switchWrapper.getWorkFlowWrapper().setCashDeposit(true);
					}
					
					logger.info("CashInProductDispenser.doSale] Going to Credit OLA Customer Account." + 
									" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
									" Trx ID:" + workFlowWrapper.getTransactionCodeModel().getCode());
					
					switchWrapper = olaFinancialInst.debitWithoutPin(switchWrapper) ;
					switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
					switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());
					workFlowWrapper.setOLASwitchWrapper(switchWrapper); // setting the switchWrapper for rollback
					SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
					switchWrapperTemp.setBasePersistableModel(sma);
					switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
					
					logger.info("CashInProductDispenser.doSale] Checking Balance of Customer Account in OLA." + 
							" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" Trx ID:" + workFlowWrapper.getTransactionCodeModel().getCode());
			
					switchWrapper = olaFinancialInst.checkBalanceWithoutPin(switchWrapperTemp) ;
					
				}
				
				p2PVO.setBalance(switchWrapper.getBalance() + p2PVO.getAmount());
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				
				// Change 2 Start
				// Date: 07-11-12
				// Change Owner: Rashid Mahmood
				// Description: Due to upper change 1 and to keep the previous state following 1 line is commented and one line is written.
				
				//	ThreadLocalAppUser.setAppUserModel(appUserTemp) ;
				ThreadLocalAppUser.getAppUserModel().setCustomerId(null);
				// Change 2 End. 
				
				
				
			}
		}		
		catch (Exception e)
		{
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
		}*/

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
		
		logger.debug("Inside getBillInfo of cashinproductdispenser");
		
		CashInVO productVO = (CashInVO) workFlowWrapper.getProductVO();
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(productVO.getCustomerMobileNo());
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		
		
		appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
		AppUserModel userModel = this.appUserDao.loadAppUserByQuery(appUserModel.getMobileNo(), appUserModel.getAppUserTypeId());//   findByExample(appUserModel, null, null, exampleHolder);

		if (null != userModel) 
		{
			appUserModel = userModel;
			productVO.setCustomerAppUserId(appUserModel.getAppUserId());
			productVO.setCustomerId(appUserModel.getCustomerId());
			productVO.setCustomerName(appUserModel.getFirstName()+ " " + appUserModel.getLastName());
			productVO.setCustomerCNIC(appUserModel.getNic());
			
			logger.info("[CashInProductDispenser.getBillInfo] Populating VO. LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" ProductID: "+ (workFlowWrapper.getProductModel() == null ? " NULL" : workFlowWrapper.getProductModel().getProductId()) + 
							" Trx ID: "+ (workFlowWrapper.getTransactionCodeModel() == null ? " NULL" : workFlowWrapper.getTransactionCodeModel().getCode()) + 
							" productVO.getCustomerAppUserId()->" + productVO.getCustomerAppUserId() + " productVO.getCustomerId()->" + productVO.getCustomerId() + 
							" productVO.getCustomerName()->" + productVO.getCustomerName());
		}
		else
		{
			throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. Please enter the correct number." ) ;
		}
//		productVO.setMfsId( ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() ) ;
		
		try
		{
			productVO.setCustomerMFSID(((UserDeviceAccountsModel)((List)appUserModel.getAppUserIdUserDeviceAccountsModelList()).get(0)).getUserId() ) ;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		workFlowWrapper.setProductVO((ProductVO) productVO);

		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getBillInfo of cashinproductdispenser");
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
