package com.inov8.microbank.server.service.integration.dispenser;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
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

public class CustomerRetailPaymentDispenser extends BillPaymentProductDispenser
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

	public CustomerRetailPaymentDispenser(CommissionManager commissionManager,
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
			logger.debug("Inside doSale of CustomerRetailPaymentDispenser");
		}
		
		AppUserModel appUserTemp = ThreadLocalAppUser.getAppUserModel() ;
		
		try
		{
			RetailPaymentVO p2PVO = (RetailPaymentVO) workFlowWrapper.getProductVO();
			//now populated in transaction.preprocess()
			/*AppUserModel agentAppUser = new AppUserModel();
			agentAppUser.setMobileNo(p2PVO.getReceiverMobile());
			
	        CustomList<AppUserModel> appUsersList = appUserDao.findByExample(agentAppUser,null,null,exampleHolder);
	        if (null != appUsersList && null != appUsersList.getResultsetList() && appUsersList.getResultsetList().size() > 0) {
	        	agentAppUser = (AppUserModel)appUsersList.getResultsetList().get(0);
	        	workFlowWrapper.setReceiverAppUserModel(agentAppUser);
			}else{
				throw new FrameworkCheckedException("Recipient Agent not valid. Please try again later.");
			}*/
	        
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	        exampleHolder.setMatchMode(MatchMode.EXACT);
	        
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setRetailerContactId(workFlowWrapper.getReceiverAppUserModel().getRetailerContactId()) ;
			sma.setDeleted(false);			
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, exampleHolder);

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
				
				p2PVO.setTransactionDateTime(new Date());
				p2PVO.setCustomerAccountTypeId(workFlowWrapper.getCustomerModel().getCustomerAccountTypeId());
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				switchWrapper.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
				switchWrapper.setBasePersistableModel(sma) ;
				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				workFlowWrapper.setRecipientSmartMoneyAccountModel(sma);
				workFlowWrapper.setProductVO(p2PVO);
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				
				switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
				logger.info("[CustomerRetailPaymentDispenser.doSale] Going to Debit/Credit Phoenix Accounts. Logged in AppUserID:" + appUserTemp.getAppUserId() + 
							" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));

				
				SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
				newSwitchWrapper.setBasePersistableModel(sma);
				newSwitchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				logger.info("[CustomerRetailPaymentDispenser.doSale] Going to Check Agent Balance. Logged in AppUserID:" + appUserTemp.getAppUserId() + 
						" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));

				//send agent appuser to phoenix FT/Check balance in threadlocal.
				workFlowWrapper.setIsCRetailPayment(true);
//				ThreadLocalAppUser.setAppUserModel(workFlowWrapper.getReceiverAppUserModel());
				newSwitchWrapper = phoenixFinancialInst.checkBalanceWithoutPin(newSwitchWrapper);

				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				logger.info("[CustomerRetailPaymentDispenser.doSale] Going to Debit/Credit Phoenix Accounts. Logged in AppUserID:" + appUserTemp.getAppUserId() + 
						" TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()));
				switchWrapper = phoenixFinancialInst.debitCreditAccount(switchWrapper);
				p2PVO.setRecipientAccountBalance(newSwitchWrapper.getBalance() + (switchWrapper.getTransactionAmount()));
				
				//replace agent app user with customer appuser again
//				ThreadLocalAppUser.setAppUserModel(appUserTemp);
				
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				
				workFlowWrapper.setSwitchWrapper(switchWrapper);
			}
		}		
		catch (Exception e)
		{			
			logger.error("[CustomerRetailPaymentDispenser.doSale] Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " TransactionID:" + (workFlowWrapper.getTransactionCodeModel() == null ? " is NULl" : workFlowWrapper.getTransactionCodeModel().getCode()) + " Exception Msg:" + e.getMessage());
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}finally{
//			ThreadLocalAppUser.setAppUserModel(appUserTemp) ;//Need to do this to avoid scenario when exception occures after setting agent as threadlocal and the agent needs to replace with customer again 
			workFlowWrapper.setIsCRetailPayment(false);
			
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of CustomerRetailPaymentDispenser");
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
			logger.debug("Inside getBillInfo of CustomerRetailPaymentDispenser");
		}
		
		RetailPaymentVO p2PVO = (RetailPaymentVO) workFlowWrapper.getProductVO();

		if( ThreadLocalAppUser.getAppUserModel().getMobileNo().equals( p2PVO.getReceiverMobile()))
		{
			throw new FrameworkCheckedException( "Own account transfer is not allowed." ) ;
		}

		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getBillInfo of CustomerRetailPaymentDispenser");
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
