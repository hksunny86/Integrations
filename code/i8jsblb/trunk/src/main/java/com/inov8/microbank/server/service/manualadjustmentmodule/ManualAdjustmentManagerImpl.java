package com.inov8.microbank.server.service.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.portal.bbaccountsview.BBAccountsViewDao;
import com.inov8.microbank.server.dao.portal.manualadjustmentmodule.BulkManualAdjustmentDAO;
import com.inov8.microbank.server.dao.portal.manualadjustmentmodule.ManualAdjustmentDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;
//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;

public class ManualAdjustmentManagerImpl implements ManualAdjustmentManager {

	private final Log logger = LogFactory.getLog(this.getClass());
	private ManualAdjustmentDAO manualAdjustmentDAO;
	private BBAccountsViewDao bBAccountsViewDao;
	private TransactionCodeDAO transactionCodeDAO;
	private ActionLogManager actionLogManager;
	private SettlementManager settlementManager;
	private StakeholderBankInfoManager stkholderBankInfoManager;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private SwitchController switchController;
	private TransactionModuleManager transactionModuleManager;
	private JmsProducer jmsProducer;
	private XmlMarshaller<BulkManualAdjustmentRefDataModel> bulkManualAdjustmentMarshaller;
	private BulkManualAdjustmentDAO bulkManualAdjustmentDAO;
	private AbstractFinancialInstitution phoenixFinancialInstitution;


	@Override
	public SearchBaseWrapper loadManualAdjustments(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<ManualAdjustmentModel>
				list = this.manualAdjustmentDAO.findByExample( (ManualAdjustmentModel)
						searchBaseWrapper.
								getBasePersistableModel(),
				searchBaseWrapper.
						getPagingHelperModel(),
				searchBaseWrapper.
						getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper loadBulkManualAdjustments(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<BulkManualAdjustmentModel>
				list = this.bulkManualAdjustmentDAO.findByExample( (BulkManualAdjustmentModel)
						searchBaseWrapper.
								getBasePersistableModel(),
				searchBaseWrapper.
						getPagingHelperModel(),
				searchBaseWrapper.
						getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null){
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	@Override
	public String fetchCoreAccountTitle(String accountNo) throws FrameworkCheckedException {

		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
		//AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		String name = "null";
		String errMsg = "null";
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		CustomerAccount custAcct = new CustomerAccount();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setCustomerAccount(custAcct);
		//logger.info("Core bank A/C No "+ ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
		//switchWrapper.getCustomerAccount().setNumber(ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
		switchWrapper.getCustomerAccount().setNumber(accountNo);
		switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		try {
			phoenixFinancialInstitution.titleFetch(switchWrapper);
			name = switchWrapper.getCustomerAccount().getTitleOfTheAccount();
			name = StringEscapeUtils.escapeXml(name);
			if(name == null || name.equals("null")){
				errMsg = "Account does not exist against provided account number";
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			errMsg = "Account does not exist against provided account number";
			ex.printStackTrace();
		}

		return name;
	}

	private boolean checkAlreadySaved(Long actionAuthorizationId){
		ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
		manualAdjustmentModel.setActionAuthorizationId(actionAuthorizationId);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.manualAdjustmentDAO.countByExample(manualAdjustmentModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		if (count > 0) {
			return true;
		}else{
			return false;
		}
	}

	private BaseWrapper saveManualAdjustmentRecord(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws FrameworkCheckedException {
		if(actionAuthorizationId != null && checkAlreadySaved(actionAuthorizationId) ){
			logger.error("[saveManualAdjustmentRecord] Record Already Exists against actionAuthorizationId:"+actionAuthorizationId);
			throw new FrameworkCheckedException("This action is already authorized.");
		}

		BaseWrapper wrapper = new BaseWrapperImpl();
		wrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationId);
		wrapper.setBasePersistableModel(manualAdjustmentModel);
		wrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		wrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID));


		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(wrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		manualAdjustmentModel = (ManualAdjustmentModel) wrapper.getBasePersistableModel();
		manualAdjustmentModel.setActionAuthorizationId(actionAuthorizationId);

		if(manualAdjustmentModel.getBulkAdjustmentId() != null && manualAdjustmentModel.getBulkAdjustmentId() > 0){
			manualAdjustmentModel.setAdjustmentCategory(2L);
		}else{
			manualAdjustmentModel.setAdjustmentCategory(1L);
		}

		manualAdjustmentModel = this.manualAdjustmentDAO.saveOrUpdate(manualAdjustmentModel) ;
		wrapper.setBasePersistableModel(manualAdjustmentModel);
		actionLogModel.setCustomField1(manualAdjustmentModel.getManualAdjustmentID().toString());
		actionLogModel.setCustomField11(manualAdjustmentModel.getTransactionCodeId());
		this.actionLogManager.completeActionLog(actionLogModel);
		return wrapper;
	}

	@Override
	public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException {
		BBAccountsViewModel bBAccountsViewModel = null;
		CustomList<BBAccountsViewModel> modelList = new CustomList<BBAccountsViewModel>();
		modelList = this.bBAccountsViewDao.findByExample(model,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		if(modelList.getResultsetList().size() > 0){
			bBAccountsViewModel = modelList.getResultsetList().get(0);
		}
		return bBAccountsViewModel;
	}

	public BBAccountsViewModel getBBAccountsViewModel(Long accountId) throws FrameworkCheckedException {

		BBAccountsViewModel bbAccountsViewModel = this.bBAccountsViewDao.findByPrimaryKey(accountId);

		return bbAccountsViewModel;
	}

	@Override
	public void makeOlaToOlaTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, WorkFlowException, FrameworkCheckedException {
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> creditList = new ArrayList<OLAInfo>();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>();

		WorkFlowWrapper workFlowWrapper = createTransaction(manualAdjustmentModel);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);

		OLAInfo debitFT = new OLAInfo();
		debitFT.setReasonId(ReasonConstants.MANUAL_ADJUSTMENT);
		debitFT.setMicrobankTransactionCode(manualAdjustmentModel.getTransactionCodeId());

		String fromAcNo = manualAdjustmentModel.getFromACNo();
		String fromAcNoEncrypted = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getFromACNo());
		BBAccountsViewModel fromBbACModel = new BBAccountsViewModel();
		fromBbACModel.setAccountNumber(fromAcNoEncrypted);
		fromBbACModel = getBBAccountsViewModel(fromBbACModel);
		this.checkClosedAccount(fromBbACModel);

		String toAcNo = manualAdjustmentModel.getToACNo().toString();
		String toAcNoEncrypted = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getToACNo().toString());
		BBAccountsViewModel toBbACModel = new BBAccountsViewModel();
		toBbACModel.setAccountNumber(toAcNoEncrypted);
		toBbACModel = getBBAccountsViewModel(toBbACModel);
		this.checkClosedAccount(toBbACModel);
		/* check removed as per JSBLMFS-BRD-0012 Transaction Redemption, Reversal and Manual Adjustment (13 Jan 2017)
		if(fromBbACModel.getAccountTypeId().longValue()!=3){
			if(toBbACModel.getAccountTypeId().longValue()!=3){
				if(fromBbACModel.getIsCustomerAccountType().equals(toBbACModel.getIsCustomerAccountType())){
					throw new FrameworkCheckedException("Same BB Accounts Type");
				}
			}
		}*/
		debitFT.setCustomerAccountTypeId(fromBbACModel.getAccountTypeId());
		debitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		debitFT.setPayingAccNo(manualAdjustmentModel.getFromACNo().toString());
		debitFT.setBalance(manualAdjustmentModel.getAmount());
		debitList.add(debitFT);

		OLAInfo creditFT = new OLAInfo();
		creditFT.setReasonId(ReasonConstants.MANUAL_ADJUSTMENT);
		creditFT.setMicrobankTransactionCode(manualAdjustmentModel.getTransactionCodeId());

		creditFT.setCustomerAccountTypeId(toBbACModel.getAccountTypeId());
		creditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		creditFT.setPayingAccNo(manualAdjustmentModel.getToACNo().toString());
		creditFT.setBalance(manualAdjustmentModel.getAmount());
		creditList.add(creditFT);

		olaVO.setCreditAccountList(creditList);
		olaVO.setDebitAccountList(debitList);
		olaVO.setCategory(TransactionConstantsInterface.MANUAL_ADJUSTMENT_CATEGORY_ID);

		switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());

		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

		/////////////////// Settlement Transactions ////
		Long fromAccountInfoId = null;
		Long toAccountInfoId = null;
		Boolean isAgent=null;

		TransactionDetailMasterModel txDetailMaster = workFlowWrapper.getTransactionDetailMasterModel();
		Long transactionId = txDetailMaster.getTransactionId();

		StakeholderBankInfoModel model = new StakeholderBankInfoModel();

		if(fromBbACModel.getAccountTypeId().longValue()==3){
			model.setAccountNo(fromAcNo);
			model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
			if(null!= model)
				fromAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
			isAgent=null;
			if(null!=fromAccountInfoId){
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,false);
			}else{
				throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + fromAcNo);
			}
		}
		else{
			fromAccountInfoId = settlementManager.getStakeholderBankInfoId(fromBbACModel.getAccountTypeId());
			if(fromBbACModel.getIsCustomerAccountType().equals(true)){
				isAgent=false;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,true);
			}
			else{
				isAgent=true;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,true);
			}
		}

		model = new StakeholderBankInfoModel();

		if(toBbACModel.getAccountTypeId().longValue()==3){
			model.setAccountNo(toAcNo);
			model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
			if(null!= model)
				toAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
			isAgent=null;
			if(null!=toAccountInfoId){
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}else{
				throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + toAcNo);
			}
		}
		else{
			toAccountInfoId = settlementManager.getStakeholderBankInfoId(toBbACModel.getAccountTypeId());
			if(toBbACModel.getIsCustomerAccountType().equals(true)){
				isAgent=false;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}
			else{
				isAgent=true;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}
		}

		this.saveManualAdjustmentRecord(manualAdjustmentModel, actionAuthorizationId);

		String fromToType = "bbToBb";

		if(manualAdjustmentModel.getBulkAdjustmentId() != null && manualAdjustmentModel.getBulkAdjustmentId() > 0){
			updaeBulkAdjustmentProcessed(manualAdjustmentModel.getBulkAdjustmentId(), txDetailMaster.getTransactionCode(), true, null, fromToType);
		}
	}

	@Override
	public void makeBBToCoreTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException{
		logger.info("Debit OLA Account : "+manualAdjustmentModel.getFromACNo());
		WorkFlowWrapper workFlowWrapper = debitOLAAccount(manualAdjustmentModel);

		BBAccountsViewModel iftBBAccountsViewModel = getBBAccountsViewModel(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_OLA_POOL_ACCOUNT_ID);
		String IFT_OLA_POOL_ACCOUNT = EncryptionUtil.decryptWithDES(iftBBAccountsViewModel.getAccountNumber());

		ManualAdjustmentModel _manualAdjustmentModel = (ManualAdjustmentModel) manualAdjustmentModel.clone();
		_manualAdjustmentModel.setToACNo(IFT_OLA_POOL_ACCOUNT);

		logger.info("Credit OLA Account : "+_manualAdjustmentModel.getToACNo());
		workFlowWrapper = this.creditOLAAccount(_manualAdjustmentModel);

		logger.info("Credit Core Account : "+manualAdjustmentModel.getToACNo());
		creditCoreBankAccount(manualAdjustmentModel.getFromACNo(), manualAdjustmentModel.getToACNo(), manualAdjustmentModel.getAmount(), workFlowWrapper);

		/////////////////// Settlement Transactions ////
		Long fromAccountInfoId = null;
		Boolean isAgent=null;

		String fromAcNo = manualAdjustmentModel.getFromACNo();
		String fromAcNoEncrypted = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getFromACNo());
		BBAccountsViewModel fromBbACModel = new BBAccountsViewModel();
		fromBbACModel.setAccountNumber(fromAcNoEncrypted);
		fromBbACModel = getBBAccountsViewModel(fromBbACModel);

		TransactionDetailMasterModel txDetailMaster = workFlowWrapper.getTransactionDetailMasterModel();
		Long transactionId = txDetailMaster.getTransactionId();
		StakeholderBankInfoModel model = new StakeholderBankInfoModel();

		prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),
				PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT, isAgent, true);

		if(fromBbACModel.getAccountTypeId().longValue()==3){
			model.setAccountNo(fromAcNo);
			model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
			if(null!= model)
				fromAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
			isAgent=null;
			if(null!=fromAccountInfoId){
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,false);
			}else{
				throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + fromAcNo);
			}

		}
		else{
			fromAccountInfoId = settlementManager.getStakeholderBankInfoId(fromBbACModel.getAccountTypeId());
			if(fromBbACModel.getIsCustomerAccountType().equals(true)){
				isAgent=false;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,true);
			}
			else{
				isAgent=true;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,isAgent,true);
			}
		}
		///////////////////End Settlement Transactions ////

		this.saveManualAdjustmentRecord(manualAdjustmentModel, actionAuthorizationId);

		/*if(manualAdjustmentModel.getBulkAdjustmentId() != null && manualAdjustmentModel.getBulkAdjustmentId() > 0){
			updaeBulkAdjustmentProcessed(manualAdjustmentModel.getBulkAdjustmentId(), txDetailMaster.getTransactionCode(), true);
		}*/

		String fromToType = "toAcc";
		String coreAccTitle = manualAdjustmentModel.getToACNick();

		if(manualAdjustmentModel.getBulkAdjustmentId() != null && manualAdjustmentModel.getBulkAdjustmentId() > 0){
			updaeBulkAdjustmentProcessed(manualAdjustmentModel.getBulkAdjustmentId(), txDetailMaster.getTransactionCode(), true, coreAccTitle, fromToType);
		}


	}

	@Override
	public void makeCoreToBBTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException{
		BBAccountsViewModel iftBBAccountsViewModel = getBBAccountsViewModel(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_OLA_POOL_ACCOUNT_ID);
		String IFT_OLA_POOL_ACCOUNT = EncryptionUtil.decryptWithDES(iftBBAccountsViewModel.getAccountNumber());

		ManualAdjustmentModel _manualAdjustmentModel = (ManualAdjustmentModel) manualAdjustmentModel.clone();
		_manualAdjustmentModel.setFromACNo(IFT_OLA_POOL_ACCOUNT);

		logger.info("Debit OLA Account : "+_manualAdjustmentModel.getFromACNo());
		WorkFlowWrapper workFlowWrapper = this.debitOLAAccount(_manualAdjustmentModel);

		manualAdjustmentModel.setTransactionCodeId(_manualAdjustmentModel.getTransactionCodeId());

		logger.info("Credit OLA Account : "+manualAdjustmentModel.getToACNo());
		workFlowWrapper = creditOLAAccount(manualAdjustmentModel);

		logger.info("Debit Core Account : "+manualAdjustmentModel.getFromACNo());
		debitCoreBankAccount(manualAdjustmentModel.getFromACNo(), manualAdjustmentModel.getToACNo(), manualAdjustmentModel.getAmount(), workFlowWrapper);

		/////////////////// Settlement Transactions ////
		Long toAccountInfoId = null;
		Boolean isAgent=null;

		String toAcNo = manualAdjustmentModel.getToACNo().toString();
		String toAcNoEncrypted = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getToACNo().toString());
		BBAccountsViewModel toBbACModel = new BBAccountsViewModel();
		toBbACModel.setAccountNumber(toAcNoEncrypted);
		toBbACModel = getBBAccountsViewModel(toBbACModel);

		TransactionDetailMasterModel txDetailMaster = workFlowWrapper.getTransactionDetailMasterModel();
		Long transactionId = txDetailMaster.getTransactionId();

//		StakeholderBankInfoModel iftStakeholderBankInfoModel =
//				this.stkholderBankInfoManager.loadStakeholderBankInfoModel(new StakeholderBankInfoModel(PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT));

		prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),
				PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, isAgent, false);

		StakeholderBankInfoModel model = new StakeholderBankInfoModel();

		if(toBbACModel.getAccountTypeId().longValue()==3){
			model.setAccountNo(toAcNo);
			model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
			if(null!= model)
				toAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
			isAgent=null;
			if(null!=toAccountInfoId){
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}else{
				throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + toAcNo);
			}
		}
		else{
			toAccountInfoId = settlementManager.getStakeholderBankInfoId(toBbACModel.getAccountTypeId());
			if(toBbACModel.getIsCustomerAccountType().equals(true)){
				isAgent=false;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}
			else{
				isAgent=true;
				prepareAndSaveSettlementTransaction(transactionId,txDetailMaster.getProductId(), manualAdjustmentModel.getAmount(),PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,isAgent,false);
			}
		}
		///////////////////End Settlement Transactions ////

		this.saveManualAdjustmentRecord(manualAdjustmentModel, actionAuthorizationId);

		String fromToType = "fromAcc";
		String coreAccTitle = manualAdjustmentModel.getFromACNick();

		if(manualAdjustmentModel.getBulkAdjustmentId() != null && manualAdjustmentModel.getBulkAdjustmentId() > 0){
			updaeBulkAdjustmentProcessed(manualAdjustmentModel.getBulkAdjustmentId(), txDetailMaster.getTransactionCode(), true, coreAccTitle, fromToType);
		}

	}

	@Override
	public void makeOracleFinancialSettlement(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException {
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();

		WorkFlowWrapper workFlowWrapper = createTransaction(manualAdjustmentModel);

		TransactionDetailMasterModel txDetailMaster = workFlowWrapper.getTransactionDetailMasterModel();

		settlementModel.setTransactionID(txDetailMaster.getTransactionId());
		settlementModel.setProductID(txDetailMaster.getProductId());
		settlementModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		settlementModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);

		StakeholderBankInfoModel model = new StakeholderBankInfoModel();
		model.setAccountNo(manualAdjustmentModel.getFromACNo());
		model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
		settlementModel.setFromBankInfoID(model.getStakeholderBankInfoId());

		model = new StakeholderBankInfoModel();
		model.setAccountNo(manualAdjustmentModel.getToACNo());
		model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
		settlementModel.setToBankInfoID(model.getStakeholderBankInfoId());
		settlementModel.setAmount(manualAdjustmentModel.getAmount());

		this.settlementManager.saveSettlementTransactionModel(settlementModel);

		this.saveManualAdjustmentRecord(manualAdjustmentModel, actionAuthorizationId);

	}

	private WorkFlowWrapper debitOLAAccount(ManualAdjustmentModel manualAdjustmentModel) throws Exception, FrameworkCheckedException {
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		WorkFlowWrapper workFlowWrapper = this.createTransaction(manualAdjustmentModel);
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>();

		OLAInfo debitFT = new OLAInfo();
		debitFT.setReasonId(ReasonConstants.MANUAL_ADJUSTMENT);
		debitFT.setMicrobankTransactionCode(manualAdjustmentModel.getTransactionCodeId());

		String acNo = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getFromACNo().toString());
		BBAccountsViewModel bbACModel = new BBAccountsViewModel();
		bbACModel.setAccountNumber(acNo);
		bbACModel = getBBAccountsViewModel(bbACModel);
		this.checkClosedAccount(bbACModel);

		debitFT.setCustomerAccountTypeId(bbACModel.getAccountTypeId());
		debitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		debitFT.setPayingAccNo(manualAdjustmentModel.getFromACNo().toString());
		debitFT.setBalance(manualAdjustmentModel.getAmount());
		debitList.add(debitFT);
		olaVO.setDebitAccountList(debitList);
		olaVO.setIsCreditOnly(Boolean.TRUE);
		olaVO.setCategory(TransactionConstantsInterface.MANUAL_ADJUSTMENT_CATEGORY_ID);

//		TransactionModel transactionModel = new TransactionModel();
//		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
//		transactionCodeModel.setCode(manualAdjustmentModel.getTransactionCodeId());
//		CustomList<TransactionCodeModel> txCodeList = new CustomList<TransactionCodeModel>();
//		txCodeList = transactionCodeDAO.findByExample(transactionCodeModel);
//		if(txCodeList.getResultsetList().size() > 0){
//			transactionCodeModel = txCodeList.getResultsetList().get(0);
//		}
//		switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
//		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
		switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());

		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		return workFlowWrapper;
	}

	private WorkFlowWrapper creditOLAAccount(ManualAdjustmentModel manualAdjustmentModel) throws Exception, FrameworkCheckedException {
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		WorkFlowWrapper workFlowWrapper = this.createTransaction(manualAdjustmentModel);
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> creditList = new ArrayList<OLAInfo>();

		OLAInfo creditFT = new OLAInfo();
		creditFT.setReasonId(ReasonConstants.MANUAL_ADJUSTMENT);
		creditFT.setMicrobankTransactionCode(manualAdjustmentModel.getTransactionCodeId());

		String acNo = EncryptionUtil.encryptWithDES(manualAdjustmentModel.getToACNo().toString());
		BBAccountsViewModel bbACModel = new BBAccountsViewModel();
		bbACModel.setAccountNumber(acNo);
		bbACModel = getBBAccountsViewModel(bbACModel);
		this.checkClosedAccount(bbACModel);

		creditFT.setCustomerAccountTypeId(bbACModel.getAccountTypeId());
		creditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		creditFT.setPayingAccNo(manualAdjustmentModel.getToACNo().toString());
		creditFT.setBalance(manualAdjustmentModel.getAmount());
		creditList.add(creditFT);
		olaVO.setCreditAccountList(creditList);
		olaVO.setIsCreditOnly(Boolean.TRUE);
		olaVO.setCategory(TransactionConstantsInterface.MANUAL_ADJUSTMENT_CATEGORY_ID);
//		TransactionModel transactionModel = new TransactionModel();
//		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
//		transactionCodeModel.setCode(manualAdjustmentModel.getTransactionCodeId());
//		CustomList<TransactionCodeModel> txCodeList = new CustomList<TransactionCodeModel>();
//		txCodeList = transactionCodeDAO.findByExample(transactionCodeModel);
//		if(txCodeList.getResultsetList().size() > 0){
//			transactionCodeModel = txCodeList.getResultsetList().get(0);
//		}
//		switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
//		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
		switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());

		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		return workFlowWrapper;
	}

	private void creditCoreBankAccount(String sourceAccount, String recipientAccount, Double amount, WorkFlowWrapper wrapper) throws FrameworkCheckedException{
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setFromAccountNo(sourceAccount);

		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountNo(recipientAccount);

		switchWrapper.setTransactionAmount(amount);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		try {
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			ThreadLocalAppUser.setAppUserModel(appUserModel);

			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

			switchWrapper = this.switchController.creditAccountAdvice(switchWrapper);

			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

		} catch (Exception e) {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: Exception at middleware while FT for Manual Adjustment.  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException("Fund transfer at RDV failed");
		}

		/*
		Credit is done in queue. Resp Code is null here now

		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

		if (StringUtils.isNotEmpty(responseCode) && responseCode.equals("00")) {
			logger.info("FT at RDV was successful.");
		} else {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: FT at middleware Failed. Response Code:" + responseCode + "  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			logger.error("*** FUND TRANSFER AT RDV FAILED  ***");
			throw new FrameworkCheckedException("Fund transfer at RDV failed");
		}*/

	}

	private void debitCoreBankAccount(String sourceAccount, String recipientAccount, Double amount, WorkFlowWrapper wrapper) throws FrameworkCheckedException{
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setFromAccountNo(sourceAccount);

		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountNo(recipientAccount);

		switchWrapper.setTransactionAmount(amount);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		try {
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			ThreadLocalAppUser.setAppUserModel(appUserModel);

			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

			MiddlewareMessageVO integrationMessageVO = new MiddlewareMessageVO();
			integrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
			integrationMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
			integrationMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
			integrationMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getTransactionAmount()));
			switchWrapper.setMiddlewareIntegrationMessageVO(integrationMessageVO);

			switchWrapper = this.switchController.debitAccount(switchWrapper);

			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

		} catch (Exception e) {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: Exception at middleware while FT for Manual Adjustment.  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException("Fund transfer at RDV failed");
		}

		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

		if (StringUtils.isNotEmpty(responseCode) && responseCode.equals("00")) {
			logger.info("FT at RDV was successful.");
		} else {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: FT at middleware Failed. Response Code:" + responseCode + "  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			logger.error("*** FUND TRANSFER AT RDV FAILED ***");
			throw new FrameworkCheckedException("Fund transfer at RDV failed");
		}

	}

	private void actionLogBeforeStart(ActionLogModel actionLogModel) {

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}

	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}
	private void prepareAndSaveSettlementTransaction(Long transactionId,Long productId,Double amount,Long fromAccountInfoId, Long toAccountInfoId, Boolean isAgent, boolean debitPoolAcc) throws Exception{
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(appUserModel.getAppUserId());
		settlementModel.setUpdatedBy(appUserModel.getAppUserId());
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);
		settlementModel.setFromBankInfoID(fromAccountInfoId);
		settlementModel.setToBankInfoID(toAccountInfoId);
		settlementModel.setAmount(amount);

		this.settlementManager.saveSettlementTransactionModel(settlementModel);

		if(isAgent != null){
			Long poolBankInfoId = null;

			settlementModel = new SettlementTransactionModel();
			if(isAgent){
				poolBankInfoId = PoolAccountConstantsInterface.AGENT_POOL_ACCOUNT_ID;
			}else{
				poolBankInfoId = PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID;
			}

			settlementModel.setTransactionID(transactionId);
			settlementModel.setProductID(productId);
			settlementModel.setCreatedBy(appUserModel.getAppUserId());
			settlementModel.setUpdatedBy(appUserModel.getAppUserId());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);
			settlementModel.setFromBankInfoID((debitPoolAcc)?poolBankInfoId:fromAccountInfoId);
			settlementModel.setToBankInfoID((!debitPoolAcc)?poolBankInfoId:toAccountInfoId);
			settlementModel.setAmount(amount);
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
		}
	}

	public void validateTransactionCode(String trxCode) throws FrameworkCheckedException{

		if(StringUtil.isNullOrEmpty(trxCode)){
			return;
		}

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setMatchMode(MatchMode.EXACT);
		exampleHolder.setIgnoreCase(false);

		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
		transactionCodeModel.setCode(trxCode);

		CustomList<TransactionCodeModel> txCodeList = transactionCodeDAO.findByExample(transactionCodeModel,null,null,exampleHolder);
		if(txCodeList == null || txCodeList.getResultsetList().size() == 0){
			throw new FrameworkCheckedException("INVALID_TRX_ID");
		}

	}

	private WorkFlowWrapper createTransaction(ManualAdjustmentModel maModel) throws FrameworkCheckedException {
		String transactionCode = maModel.getTransactionCodeId();
		WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();

		if(StringUtils.isEmpty(transactionCode)) {
			try{
				wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_SENDER, maModel.getFromACNo());
				wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_RECIPIENT, maModel.getToACNo());

				wrapper.setTransactionAmount(maModel.getAmount());

				ProductModel productModel = new ProductModel(ProductConstantsInterface.MANUAL_ADJUSTMENT, ProductConstantsInterface.MANUAL_ADJUSTMENT_NAME);
				productModel.setSupplierIdSupplierModel(new SupplierModel(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER, SupplierConstants.BRANCHLESS_BANKING_SUPPLIER_NAME));
				wrapper.setProductModel(productModel);
				wrapper.setDeviceTypeModel(new DeviceTypeModel(DeviceTypeConstantsInterface.WEB));
				wrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.MANUAL_ADJUSTMENT_TX));

				Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

				if(maModel.getAdjustmentType().longValue() == ManualAdjustmentTypeConstants.CORE_TO_BB.longValue()) {
					paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;
				}

				wrapper.setPaymentModeModel(new PaymentModeModel(paymentModeId));
				wrapper.setAppUserModel(UserUtils.getCurrentUser());
				wrapper.setUserDeviceAccountModel(new UserDeviceAccountsModel());
				//generate transaction code
				transactionModuleManager.generateTransactionCodeRequiresNewTransaction(wrapper);

				transactionModuleManager.createTransactionModel(wrapper);

				maModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getCode());

			}catch(Exception ex){
				logger.error("Unable to create Transaction, Reason:"+ex.getMessage(),ex);
				throw new FrameworkCheckedException("Unable to create Transaction");
			}

		}
		else {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
			txDetailMaster.setTransactionCode(transactionCode);
			baseWrapper.setBasePersistableModel(txDetailMaster);
			baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
			txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
			wrapper.setTransactionDetailMasterModel(txDetailMaster);

			wrapper.setProductModel(new ProductModel(txDetailMaster.getProductId(), txDetailMaster.getProductName()));

			TransactionCodeModel transactionCodeModel = new TransactionCodeModel(transactionCode);
			transactionCodeModel.setTransactionCodeId(txDetailMaster.getTransactionCodeId());
			wrapper.setTransactionCodeModel(transactionCodeModel);
			wrapper.setTransactionModel(new TransactionModel(txDetailMaster.getTransactionId()));
		}
		return wrapper;
	}

	private void checkClosedAccount(BBAccountsViewModel model) throws Exception{
		if(model != null ){
			if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
				logger.info(">checkClosedAccount - Passed - Settlement Acc Type");
			}else{
				String accNumber = EncryptionUtil.decryptWithDES(model.getAccountNumber());
				if( model.getIsActive()== null || !model.getIsActive() ){
					throw new FrameworkCheckedException("Account is not active against account number " + accNumber);
				}else if(model.getStatusId() == null || (model.getStatusId().longValue() != 1 && model.getStatusId().longValue() != 4)){
					throw new FrameworkCheckedException("Account Status is not active against account number " + accNumber);
				}else if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
					throw new FrameworkCheckedException("Account is CLOSED against account number " + accNumber);
				}
				logger.info(">checkClosedAccount - Passed");
			}
		}
	}

	public void createBulkAdjustments(List<BulkManualAdjustmentModel> dis) throws Exception {
		bulkManualAdjustmentDAO.saveOrUpdateCollection(dis);

		BulkManualAdjustmentModel bulkManualAdjustmentModel = new BulkManualAdjustmentModel();
		if(dis.size() > 0 && dis !=null){
			bulkManualAdjustmentModel = dis.get(0);
		}

		if(bulkManualAdjustmentModel.getIsApproved() == true){
			for (BulkManualAdjustmentModel bulkModel : dis) {
				BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel = new BulkManualAdjustmentRefDataModel();
				bulkManualAdjustmentRefDataModel.setBulkAdjustmentId(bulkModel.getBulkAdjustmentId());
				bulkManualAdjustmentRefDataModel.setTrxnId(bulkModel.getTrxnId());
				bulkManualAdjustmentRefDataModel.setAdjustmentType(bulkModel.getAdjustmentType());
				bulkManualAdjustmentRefDataModel.setFromAccount(bulkModel.getFromAccount());
				bulkManualAdjustmentRefDataModel.setFromAccountTitle(bulkModel.getFromAccountTitle());
				bulkManualAdjustmentRefDataModel.setToAccount(bulkModel.getToAccount());
				bulkManualAdjustmentRefDataModel.setToAccountTitle(bulkModel.getToAccountTitle());
				bulkManualAdjustmentRefDataModel.setAmount(bulkModel.getAmount());
				bulkManualAdjustmentRefDataModel.setComments(bulkModel.getDescription());
				bulkManualAdjustmentRefDataModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				bulkManualAdjustmentRefDataModel.setCreatedOn(new Date());
				//bulkManualAdjustmentRefDataModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
				//manualAdjustmentManager.pushBulkManualAdjustmentToQueue(bulkManualAdjustmentRefDataModel);

				String xml = bulkManualAdjustmentMarshaller.marshal(bulkManualAdjustmentRefDataModel);
				jmsProducer.produce(xml , DestinationConstants.BULK_MANUAL_ADJUSTMENT_DESTINATION);
			}
		}
	}

	public void pushBulkManualAdjustmentToQueue(BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel) throws Exception{
		String xml = bulkManualAdjustmentMarshaller.marshal(bulkManualAdjustmentRefDataModel);
		jmsProducer.produce(xml , DestinationConstants.BULK_MANUAL_ADJUSTMENT_DESTINATION);
	}


	public List<BulkManualAdjustmentModel> loadBulkManualAdjustmentModelList(SearchBaseWrapper searchBaseWrapper) throws Exception {
		BulkManualAdjustmentModel bulkManualAdjustmentModel = (BulkManualAdjustmentModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<BulkManualAdjustmentModel> customList = bulkManualAdjustmentDAO.findByExample(bulkManualAdjustmentModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		List<BulkManualAdjustmentModel> resultList = new ArrayList<>();
		if (customList != null && CollectionUtils.isNotEmpty(customList.getResultsetList())) {
			resultList = customList.getResultsetList();
		}

		return resultList;
	}

	public Boolean updateBulkAdjustmentErrorMessage(Long bulkManualAdjustmentId, String errorMessage) throws Exception{
		return bulkManualAdjustmentDAO.updateBulkManualAdjustment(bulkManualAdjustmentId, null, errorMessage, null, null, null, null);
	}

	public boolean updaeBulkAdjustmentProcessed(Long bulkManualAdjustmentId, String transactionCode, Boolean isProcessed, String coreAccTitle, String fromToType) throws Exception{
		return bulkManualAdjustmentDAO.updateBulkManualAdjustment(bulkManualAdjustmentId, transactionCode, null, isProcessed, null, coreAccTitle, fromToType);
	}

	public void saveBulkManualAdjustmentModel(BulkManualAdjustmentModel model) throws Exception{
		bulkManualAdjustmentDAO.saveOrUpdate(model);
	}

	public void updateIsApprovedForBatch(Long batchId,String bulkAdjustmentIds []) throws Exception{

		BulkManualAdjustmentModel model = new BulkManualAdjustmentModel();
		List<BulkManualAdjustmentModel> updatedBulkManualAdjustmentList = new ArrayList<>();
		model.setBatchId(batchId);
		CustomList<BulkManualAdjustmentModel> bulkManualAdjustmentModelList = (CustomList<BulkManualAdjustmentModel>) bulkManualAdjustmentDAO.findByExample(model, null);

		if(bulkAdjustmentIds != null && bulkAdjustmentIds.length > 0){
			HashSet<String> hs = new HashSet<String>(Arrays.asList(bulkAdjustmentIds));
			for(BulkManualAdjustmentModel bulkManualAdjustmentModel : bulkManualAdjustmentModelList.getResultsetList()){
				if(! hs.contains(bulkManualAdjustmentModel.getBulkAdjustmentId().toString())){
					bulkManualAdjustmentModel.setIsSkipped(true);
				}
				bulkManualAdjustmentModel.setIsApproved(true);
				bulkManualAdjustmentModel.setUpdatedOn(new Date());
				bulkManualAdjustmentModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

				updatedBulkManualAdjustmentList.add(bulkManualAdjustmentModel);

			}
			bulkManualAdjustmentDAO.saveOrUpdateCollection(updatedBulkManualAdjustmentList);
		}
		//bulkManualAdjustmentDAO.updateIsApprovedForBatch(batchId , bulkAdjustmentId);
	}

	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo){
		String errMsg = "";
		if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
			logger.info("[bulk]Settlement Acc Type loaded against accNumber:"+model.getAccountNumber()+" ... so SKIPPING account status/active check");
		}else{
			// Agent / Customer scenario
			if( model.getIsActive()== null || !model.getIsActive() ){
				errMsg = fromTo + "Account is not active against provided account number";
			}else if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
				errMsg = fromTo + "Account Status is not active against provided account number";
			}else if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
				errMsg = fromTo + "Account is CLOSED against provided account number";
			}
		}
		return errMsg;
	}

	@Override
	public TransactionCodeModel loadTransactionCodeModelByPrimaryKey(Long transactionCodeId) throws Exception {
		return transactionCodeDAO.findByPrimaryKey(transactionCodeId);
	}

	public void setManualAdjustmentDAO(ManualAdjustmentDAO manualAdjustmentDAO) {
		this.manualAdjustmentDAO = manualAdjustmentDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setBBAccountsViewDao(BBAccountsViewDao bBAccountsViewDao) {
		this.bBAccountsViewDao = bBAccountsViewDao;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO) {
		this.transactionCodeDAO = transactionCodeDAO;
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	public void setStkholderBankInfoManager(StakeholderBankInfoManager stkholderBankInfoManager) {
		this.stkholderBankInfoManager = stkholderBankInfoManager;
	}

	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager) {
		this.transactionModuleManager = transactionModuleManager;
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public void setBulkManualAdjustmentMarshaller(XmlMarshaller<BulkManualAdjustmentRefDataModel> bulkManualAdjustmentMarshaller) {
		this.bulkManualAdjustmentMarshaller = bulkManualAdjustmentMarshaller;
	}

	public void setBulkManualAdjustmentDAO(BulkManualAdjustmentDAO bulkManualAdjustmentDAO) {
		this.bulkManualAdjustmentDAO = bulkManualAdjustmentDAO;
	}

	public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

	public void setbBAccountsViewDao(BBAccountsViewDao bBAccountsViewDao) {
		this.bBAccountsViewDao = bBAccountsViewDao;
	}
}
