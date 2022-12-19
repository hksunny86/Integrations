package com.inov8.microbank.server.service.integration.dispenser;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.P2PVO;
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

public class P2PDispenser extends BillPaymentProductDispenser
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


	private final Log logger = LogFactory.getLog(this.getClass());

	public P2PDispenser(CommissionManager commissionManager,
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
		/*if(logger.isDebugEnabled())
		{
			logger.debug("Inside doSale of P2PBillPaymentDispenser");
		}
		
		try
		{
			P2PVO p2PVO = (P2PVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
			sma.setCustomerId(p2PVO.getCustomerId()) ;
						
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
				
//				ThreadLocalAppUser.getAppUserModel().setCustomerId(p2PVO.getCustomerId());
				
				//Change 2 End		
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				
				switchWrapper.setBasePersistableModel(sma) ;
				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				if(logger.isDebugEnabled())
				{
					logger.debug("***About to Credit Recipient Account***");
				}
				switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
				switchWrapper.getWorkFlowWrapper().setP2PRecepient(true);
				switchWrapper = olaFinancialInst.debitWithoutPin(switchWrapper) ;
				SwitchWrapper recSwitchWrapper = new SwitchWrapperImpl();
				recSwitchWrapper.setOlavo(switchWrapper.getOlavo());
				recSwitchWrapper.setBasePersistableModel(sma) ;
				recSwitchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				recSwitchWrapper.setBankId(sma.getBankId());
				recSwitchWrapper.setPaymentModeId(sma.getPaymentModeId());
				switchWrapper = olaFinancialInst.checkBalanceWithoutPin(switchWrapper) ;
				p2PVO.setBalance(switchWrapper.getBalance() + p2PVO.getAmount());
				switchWrapper.getWorkFlowWrapper().setP2PRecepient(false);
				
				// Change 2 Start
				// Date: 07-11-12
				// Change Owner: Rashid Mahmood
				// Description: Due to upper change 1 and to keep the previous state following 1 line is commented and one line is written.
				
				//ThreadLocalAppUser.setAppUserModel(appUserTemp) ;
//				ThreadLocalAppUser.getAppUserModel().setCustomerId(null);
				
				// Change 2 End. 
				
				
				workFlowWrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId()+"");
				workFlowWrapper.getTransactionDetailModel().setCustomField1(sma.getSmartMoneyAccountId()+"");
				workFlowWrapper.setRecipientSwitchWrapper(recSwitchWrapper);
				workFlowWrapper.setRecipientSmartMoneyAccountModel(sma);
				
			}
			
		}
		catch(CommandException ce)
		{
			throw new CommandException(ce.getMessage() ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		catch (Exception e)
		{			
			e.printStackTrace();
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of P2PBillPaymentDispenser");
		}
*/
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
			logger.debug("Inside getBillInfo of P2P");
		}
		
		P2PVO p2PVO = (P2PVO) workFlowWrapper.getProductVO();
		String mobileNo = p2PVO.getMobileNo() ;
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		AppUserModel appUser = new AppUserModel() ;
		appUser.setMobileNo(mobileNo);
		appUser.setAppUserTypeId( 2l );
		CustomList<AppUserModel> appUserList = this.appUserDao.findByExample(appUser, null, null, exampleHolder);
		
		if( ThreadLocalAppUser.getAppUserModel().getMobileNo().equals( p2PVO.getMobileNo() ) )
		{
			logger.error("[P2PDispenser.getBillInfo] Own account transfer is not allowed. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			throw new CommandException( "Own account transfer is not allowed." ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, null ) ;
		}

		if (null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0) 
		{
			appUser = ((AppUserModel)appUserList.getResultsetList().get(0)) ;
			
			p2PVO.setCustomerName( appUser.getFirstName() + " " + appUser.getLastName() ) ;
			p2PVO.setCustomerId( appUser.getCustomerId() ) ;
			p2PVO.setAppUserId( appUser.getAppUserId() ) ;
			
			try
			{
				p2PVO.setMfsId( ((UserDeviceAccountsModel)((List)appUser.getAppUserIdUserDeviceAccountsModelList()).get(0)).getUserId() ) ;
			}
			catch (Exception e)
			{
				logger.error("[P2PDispenser.getBillInfo] Exception Occured for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Details:" + e.getMessage());
//				e.printStackTrace();
			}
			
		}
		else{
			logger.error("[P2PDispenser.getBillInfo] Invalid mobile number or branchless banking account. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. Please enter the correct number." ) ;
		}
		

		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getBillInfo of PtclBillPaymentDispenser");
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
