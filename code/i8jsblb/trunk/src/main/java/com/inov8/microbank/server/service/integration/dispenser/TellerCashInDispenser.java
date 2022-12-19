package com.inov8.microbank.server.service.integration.dispenser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.verifly.common.model.AccountInfoModel;

/**
 * @author kashif bashir
 */
public class TellerCashInDispenser extends BillPaymentProductDispenser {

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
	private ApplicationContext applicationContext;
	private AbstractFinancialInstitution olaFinancialInstitution ;
	private AbstractFinancialInstitution phoenixFinancialInst ;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;

	private final Log logger = LogFactory.getLog(this.getClass());

	public TellerCashInDispenser(CommissionManager commissionManager, SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager, 
			ProductManager productManager, AppUserManager appUserManager, ProductUnitManager productUnitManager, ShipmentManager shipmentManager,
								 GenericDao genericDAO, ApplicationContext applicationContext) {
		
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO;
		this.appUserDao = (AppUserDAO) applicationContext.getBean("appUserDAO");
		this.applicationContext = applicationContext ;

		this.smartMoneyAccountDAO = (SmartMoneyAccountDAO) applicationContext.getBean("smartMoneyAccountDAO") ;
		this.olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) applicationContext.getBean("com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl") ;
		this.phoenixFinancialInstitution = (PhoenixFinancialInstitutionImpl) applicationContext.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
		phoenixFinancialInst = phoenixFinancialInstitution;
		super.setAuditLogModule((FailureLogManager) applicationContext.getBean("failureLogManager"));		
	}


	@Override
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws Exception {

		if(logger.isDebugEnabled()) {
			logger.debug("Inside doSale of TransferInDispenser");
		}
		
		SwitchWrapper switchWrapper = workFlowWrapper.getOLASwitchWrapper();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		
		try {
			
			if(ThreadLocalActionLog.getActionLogId() == null) {
//				ThreadLocalActionLog.setActionLogId(this.logActionLogModel());
			}
			
			olaFinancialInstitution.transferFunds(switchWrapper);
			
		} catch(Exception ex) {

			ex.printStackTrace();
			logger.error(ex.getLocalizedMessage());
			
			if(ex instanceof CommandException) 
				throw new WorkFlowException(ex.getLocalizedMessage());
			else if (ex instanceof WorkFlowException)
				throw (WorkFlowException) ex;
			else if (ex instanceof IOException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else if (ex instanceof RemoteAccessException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		}
		
		String accountNumber = StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*");
		
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(accountNumber);
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionIdTransactionDetailModelList());
		
		if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {

			transactionDetailModelList.get(0).setCustomField2(accountNumber);
		}
		
		workFlowWrapper.setOLASwitchWrapper(switchWrapper);

		return workFlowWrapper;
	}

	
	public AccountInfoModel getAccountInfoModelBySmartMoneyAccount(SmartMoneyAccountModel smartMoneyAccountModel, Long customerId, Long trxCodeId) throws Exception {
		
		return this.olaFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(smartMoneyAccountModel, customerId, trxCodeId);
	}
	
	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		return null;
	}

	@Override
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		return null;
	}
	
	@Override
	public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		return workFlowWrapper;
	}
	
	ActionLogManager actionLogManager = null;
	
	protected Long logActionLogModel() {
		
		if(actionLogManager == null) {
			actionLogManager = (ActionLogManager) this.applicationContext.getBean("actionLogManager");
		}
		
		Long actionLogId = null;
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
	
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		
		try {
		
			baseWrapperActionLog = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
			
		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {
			
			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();
			
			actionLogId = actionLogModel.getActionLogId();
		}
		
		return actionLogId;
	}
}