package com.inov8.microbank.server.service.transactionmodule;

import java.sql.Timestamp;
import java.util.*;

import com.inov8.microbank.common.model.productmodule.paymentservice.BookMeLog;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeLogDAO;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LedgerModel;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.P2PDetailModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionSummaryModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.wrapper.transaction.TransactionWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.p2pupdatemodule.P2PDetailDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionSummaryDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description: The pupose of this class is to Insert and update the the record
 * in Transaction and Transaction Detail
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: inov8 Limited
 * </p>
 *
 * @author Syed Ahmad Bilal
 * @version 1.0
 */
public class TransactionManagerImpl implements TransactionModuleManager
{
	// private TransactionCodeGenerator transactionCodeGenerator;
	private TransactionCodeDAO transactionCodeDAO;
	private TransactionDAO transactionDAO;
	private TransactionDetailDAO transactionDetailDAO;
	private MiniTransactionDAO miniTransactionDAO;
	private TransactionSummaryDAO transactionSummaryDAO;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private AccountManager accountManager;
	private LedgerManager ledgerManager;
	private MfsAccountManager mfsAccountManager;
	private P2PDetailDAO p2pDetailDAO;
	private SmsSender smsSender;
	private ActionLogManager actionLogManager;
	private BookMeLogDAO bookMeLogDAO;

	protected final transient Log logger = LogFactory.getLog(TransactionManagerImpl.class);

	public TransactionManagerImpl()
	{
	}

	/**
	 * This method is used for generating the transaction code. It first Saves a
	 * new record in Transaction code table and then returns the newly saved
	 * record in respective model .i.e. Transaction Code Model.
	 *
	 * @param workFlowWrapper
	 *            WorkFlowWrapper
	 * @return TransactionWrapperImpl
	 */
	public WorkFlowWrapper generateTransactionCodeRequiresNewTransaction(WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside generateTransactionCodeRequiresNewTransaction method of TransactionManagerImpl...");
		}
		TransactionWrapperImpl transactionWrapper = new TransactionWrapperImpl();
		TransactionCodeGenerator transactionCodeGenerator = new TransactionCodeGeneratorImpl();
		String transactionCode = "" ;

		// Condition added for generating different transaction codes for AllPay
		// @author Jawwad Farooq
//		if( (null != workFlowWrapper && null != workFlowWrapper.getDeviceTypeModel()) && (workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.ALL_PAY  || workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.ALLPAY_WEB || workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD))
		transactionCode = transactionCodeGenerator.getAllPayTransactionCode();
//		else
//			transactionCode = transactionCodeGenerator.getTransactionCode();


		TransactionCodeModel transactionCodeModel = this.saveTransactionCodeModel(transactionCode, workFlowWrapper);
		// transactionWrapper.setTransactionCodeModel(transactionCodeModel);
		workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending generateTransactionCodeRequiresNewTransaction method of TransactionManagerImpl...");
		}
		return workFlowWrapper;
	}

	/**
	 * This mthod is used for the saving a new transaction.This method also
	 * saves the input information i.e. WorkFlow wrapper in form of XML String.
	 *
	 * @param transactionCode
	 *            String
	 * @param workFlowWrapper
	 *            WorkFlowWrapper
	 * @return TransactionCodeModel
	 */

	private TransactionCodeModel saveTransactionCodeModel(String transactionCode, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside saveTransactionCodeModel method of TransactionManagerImpl...");
		}
		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
		AppUserModel appuserModel = UserUtils.getCurrentUser();
		XStream xstream = new XStream(new PureJavaReflectionProvider());
		// String workFlowWrapperString = xstream.toXML(workFlowWrapper);
		// transactionCodeModel.setInputParam(workFlowWrapperString);
		transactionCodeModel.setCode(transactionCode);
		transactionCodeModel.setCreatedBy(appuserModel.getPrimaryKey());
		transactionCodeModel.setUpdatedBy(appuserModel.getPrimaryKey());

		Date nowDate = new Date();
		Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
		transactionCodeModel.setCreatedOn(nowDate);
		transactionCodeModel.setUpdatedOn(nowDate);
		transactionCodeModel.setStartTime(nowTimestamp);
		transactionCodeModel.setEndTime(nowTimestamp);
		transactionCodeModel.setStatus(1);
		transactionCodeModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

		transactionCodeModel = this.transactionCodeDAO.saveOrUpdate(transactionCodeModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending saveTransactionCodeModel method of TransactionManagerImpl...");
		}
		return transactionCodeModel;
	}

	public WorkFlowWrapper generateTransactionObject(WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside generateTransactionObject method of TransactionManagerImpl...");
		}
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionCodeIdTransactionCodeModel(workFlowWrapper.getTransactionCodeModel());

		// Sets the default value for Supplier Processing Status

		transactionModel.setSupProcessingStatusId(4L);

		workFlowWrapper.setTransactionModel(transactionModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending generateTransactionObject method of TransactionManagerImpl...");
		}
		return workFlowWrapper;
	}

	/**
	 * This method is used for saving the transaction.
	 *
	 * @param transactionWrapper
	 *            TransactionWrapper
	 */
	public void saveTransaction(WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside saveTransaction method of TransactionManagerImpl...");
		}
		AppUserModel appuserModel = UserUtils.getCurrentUser();
		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		Date nowDate = new Date();

		transactionModel.setCreatedBy(appuserModel.getPrimaryKey());
		transactionModel.setCreatedOn(nowDate);
		transactionModel.setUpdatedBy(appuserModel.getPrimaryKey());
		transactionModel.setUpdatedOn(nowDate);

		transactionModel = this.transactionDAO.saveOrUpdate(transactionModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending saveTransaction method of TransactionManagerImpl...");
		}

		workFlowWrapper.setTransactionModel(transactionModel);
	}

	public BaseWrapper updateTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		TransactionModel transactionModel = (TransactionModel) baseWrapper.getBasePersistableModel();

		transactionModel.setUpdatedOn(new Date());
		transactionModel.setUpdatedBy(UserUtils.getCurrentUser().getPrimaryKey());

		transactionModel = this.transactionDAO.saveOrUpdate(transactionModel);
		baseWrapper.setBasePersistableModel(transactionModel);
		return baseWrapper;

	}

	public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		TransactionModel model = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
		model = transactionDAO.findByPrimaryKey(model.getTransactionId());
		searchBaseWrapper.setBasePersistableModel(model);
		return searchBaseWrapper;
	}

	public void updateTransactionCode(WorkFlowWrapper workFlowWrapper)
	{
		transactionCodeDAO.saveOrUpdate(workFlowWrapper.getTransactionCodeModel());
	}

	public BaseWrapper updateTransactionCode(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		TransactionCodeModel model = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
		model = transactionCodeDAO.saveOrUpdate(model);
		baseWrapper.setBasePersistableModel(model);
		return baseWrapper;
	}

	public BaseWrapper loadTransactionCode(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		TransactionCodeModel model = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
		model = transactionCodeDAO.findByPrimaryKey(model.getPrimaryKey());
		baseWrapper.setBasePersistableModel(model);
		return baseWrapper;
	}

	public BaseWrapper loadTransactionCodeByCode(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		TransactionCodeModel model = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
		CustomList<TransactionCodeModel> list = transactionCodeDAO.findByExample(model, null);
		if(null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0)
		{
			model = list.getResultsetList().get(0);
		}
		else
		{
			throw new FrameworkCheckedException("Transaction Code Not found.");
		}
		baseWrapper.setBasePersistableModel(model);
		return baseWrapper;
	}

	public void setTransactionDAO(TransactionDAO transactionDAO)
	{
		this.transactionDAO = transactionDAO;
	}

	public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO)
	{
		this.transactionCodeDAO = transactionCodeDAO;
	}

	public void setTransactionDetailDAO(TransactionDetailDAO transactionDetailDAO)
	{
		this.transactionDetailDAO = transactionDetailDAO;
	}

	public SearchBaseWrapper loadTransactionByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside loadTransactionByTransactionCode method of TransactionManagerImpl...");
		}
		TransactionCodeModel model = (TransactionCodeModel) searchBaseWrapper.getBasePersistableModel();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionCodeId(model.getTransactionCodeId());
		CustomList list = transactionDAO.findByExample(transactionModel);
		if (null != list && null != list.getResultsetList())
		{
			transactionModel = (TransactionModel) list.getResultsetList().get(0);
			searchBaseWrapper.setBasePersistableModel(transactionModel);
		}
		else
		{
			searchBaseWrapper.setBasePersistableModel(null);
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending loadTransactionByTransactionCode method of TransactionManagerImpl...");
		}
		return searchBaseWrapper;

	}

	public BaseWrapper failTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		TransactionCodeModel txCodeModel = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(txCodeModel);
		searchBaseWrapper = this.loadTransactionByTransactionCode(searchBaseWrapper);
		if (null != searchBaseWrapper.getBasePersistableModel())
		{
			TransactionModel txModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
			txModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			txModel = this.transactionDAO.saveOrUpdate(txModel);
			baseWrapper.setBasePersistableModel(txModel);
		}
		else
		{
			baseWrapper.setBasePersistableModel(null);
			throw new FrameworkCheckedException("Transaction against this Transaction Code not found.");

		}
		return baseWrapper;

	}

	public BaseWrapper saveTransactionModel(BaseWrapper wrapper) throws FrameworkCheckedException
	{
		TransactionModel transactionModel = (TransactionModel) wrapper.getBasePersistableModel();
		transactionModel = this.transactionDAO.saveOrUpdate(transactionModel);
		return wrapper;

	}

	public void transactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside transactionRequiresNewTransaction method of TransactionManagerImpl...");
		}
		AppUserModel appuserModel = UserUtils.getCurrentUser();
		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		Date nowDate = new Date();

		transactionModel.setCreatedBy(appuserModel.getPrimaryKey());
		transactionModel.setUpdatedBy(appuserModel.getPrimaryKey());
		transactionModel.setCreatedOn(nowDate);
		transactionModel.setUpdatedOn(nowDate);

		transactionModel = this.transactionDAO.saveOrUpdate(transactionModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending transactionRequiresNewTransaction method of TransactionManagerImpl...");
		}

		workFlowWrapper.setTransactionModel(transactionModel);

	}

	@Override
	public void bookMeTransactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside transactionRequiresNewTransaction method of TransactionManagerImpl...");
		}
		BookMeLog bookMeLog = (BookMeLog) workFlowWrapper.getBasePersistableModel();
		this.bookMeLogDAO.saveOrUpdate(bookMeLog);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommonCommandManagerImpl.updateAppUser");
		}

	}

	public BaseWrapper saveTransactionSummaryModel(BaseWrapper wrapper) throws FrameworkCheckedException
	{
		TransactionSummaryModel transactionSummaryModel = (TransactionSummaryModel) wrapper.getBasePersistableModel();
		transactionSummaryModel = this.transactionSummaryDAO.saveOrUpdate(transactionSummaryModel);
		return wrapper;

	}

	public List getTransactionsByCriteria(Long distributorId, Long productId, Boolean isSettled, Boolean isPosted) {

		return transactionDAO.getTransactionsByCriteria(distributorId, productId, isSettled, isPosted);
	}


	public void saveUpdateAll(Collection<TransactionModel> collection) {

		transactionDAO.saveOrUpdateCollection(collection);
	}


	public List<Object[]> getDonationTransactionList(Long trnsactionType, Long supProcessingStatusId, Long serviceId) throws FrameworkCheckedException {

		return transactionDAO.getDonationTransactionList(trnsactionType, supProcessingStatusId, serviceId);
	}


	public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionId) {

		return transactionDAO.updateTransactionProcessingStatus(transactionProcessingStatus, transactionId);
	}


	public void setTransactionSummaryDAO(TransactionSummaryDAO transactionSummaryDAO) {
		this.transactionSummaryDAO = transactionSummaryDAO;
	}

	public BaseWrapper saveMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel)baseWrapper.getBasePersistableModel();

		miniTransactionModel.setCreatedBy( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
		miniTransactionModel.setUpdatedBy( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
		miniTransactionModel.setCreatedOn(new Date()) ;
		miniTransactionModel.setUpdatedOn(new Date()) ;

		baseWrapper.setBasePersistableModel(this.miniTransactionDAO.saveOrUpdate(miniTransactionModel));

		return baseWrapper;
	}

	/*@Override
	public BaseWrapper updateP2PTxDetailsActionAuth(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		P2PDetailModel p2pDetailModel = (P2PDetailModel) baseWrapper.getObject(P2PDetailModel.class.getSimpleName());
		baseWrapper = updateP2PTxDetails(baseWrapper);
		BaseWrapper wrapper= new BaseWrapperImpl();
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
		model.setTransactionCode(p2pDetailModel.getTransactionCode());
		wrapper.setBasePersistableModel(model);
		wrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(wrapper);
		model = (TransactionDetailMasterModel) wrapper.getBasePersistableModel();
		model.setSupProcessingStatusId(p2pDetailModel.getSupProcessingStatusId());
		model.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(p2pDetailModel.getSupProcessingStatusId()));
		transactionDetailMasterManager.saveTransactionDetailMaster(model);

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionId(model.getTransactionId());
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		searchBaseWrapper = loadTransaction(searchBaseWrapper);
		transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
		transactionModel.setSupProcessingStatusId(p2pDetailModel.getSupProcessingStatusId());
		searchBaseWrapper.setBasePersistableModel(transactionModel);
		updateTransaction(searchBaseWrapper);


		return baseWrapper;
	}*/


	@Override
	public BaseWrapper updateP2PTxDetails(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		P2PDetailModel p2pDetailModel = (P2PDetailModel) baseWrapper.getObject(P2PDetailModel.class.getSimpleName());

		try {
			BaseWrapper modelWrapper = new BaseWrapperImpl();
			modelWrapper.setBasePersistableModel(p2pDetailModel);
			modelWrapper.putObject(PortalConstants.KEY_USECASE_ID,(Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
			//ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
			Long actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
			ActionLogModel actionLogModel = null;
			if(actionAuthId == null)
			{
				actionLogModel = new ActionLogModel();
				actionLogModel.setTrxData((String)baseWrapper.getObject("trxData"));
				actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);
			}
			else{
				actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthId);
			}
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			actionLogModel.setCustomField11(p2pDetailModel.getTransactionCode());

			/*Updating TransactionDetailMaster*/
			TransactionDetailMasterModel tempTxDetailMasterModel = new TransactionDetailMasterModel();
			tempTxDetailMasterModel.setTransactionCode(p2pDetailModel.getTransactionCode());
			baseWrapper.setBasePersistableModel(tempTxDetailMasterModel);
			baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
			tempTxDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();

			// this is to be used for history management
			baseWrapper.putObject("oldTransactionDetailMasterModel", tempTxDetailMasterModel.clone());

			P2PDetailModel tempp2pDetailModel = new P2PDetailModel();
			tempp2pDetailModel.setTransactionCode(p2pDetailModel.getTransactionCode());
			CustomList<P2PDetailModel> tempCustomList = p2pDetailDAO.findByExample(tempp2pDetailModel);
			if(null==tempCustomList.getResultsetList() || tempCustomList.getResultsetList().size()<1){
				tempp2pDetailModel.setTransactionCode(tempTxDetailMasterModel.getTransactionCode());
				tempp2pDetailModel.setSenderMobile(tempTxDetailMasterModel.getSaleMobileNo());
				tempp2pDetailModel.setSenderCNIC(tempTxDetailMasterModel.getSenderCnic());
				tempp2pDetailModel.setRecipientMobile(tempTxDetailMasterModel.getRecipientMobileNo());
				tempp2pDetailModel.setRecipientCNIC(tempTxDetailMasterModel.getRecipientCnic());
				tempp2pDetailModel.setCreatedOn(tempTxDetailMasterModel.getUpdatedOn());
				p2pDetailDAO.saveOrUpdate(tempp2pDetailModel);
			}
			p2pDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			p2pDetailModel.setCreatedOn(new Date());
			p2pDetailDAO.saveOrUpdate(p2pDetailModel);

			modelWrapper.setBasePersistableModel(p2pDetailModel);

			String oldSenderCNIC = tempTxDetailMasterModel.getSenderCnic();
			String oldRecipientCNIC = tempTxDetailMasterModel.getRecipientCnic();

			/*Updating TransactionDetail*/
			TransactionDetailModel tempTxDetailModel = new TransactionDetailModel();
			tempTxDetailModel.setTransactionId(tempTxDetailMasterModel.getTransactionId());
			CustomList<TransactionDetailModel> txDetailList = transactionDetailDAO.findByExample(tempTxDetailModel);
			tempTxDetailModel = txDetailList.getResultsetList().get(0);

			String brandName = MessageUtil.getMessage("jsbl.brandName");

			if(p2pDetailModel.isSenderCNICChanged()){
				tempTxDetailMasterModel.setSenderCnic(p2pDetailModel.getSenderCNIC());
				tempTxDetailModel.setCustomField7(p2pDetailModel.getSenderCNIC());

				Object[] args = {brandName,p2pDetailModel.getSenderCNIC()};
				String smsText = MessageUtil.getMessage("updatep2p.sender.cnic", args);
				this.sendSMSForUpdateP2P(p2pDetailModel.getSenderMobile(), smsText);

			}
			if(p2pDetailModel.isSenderMobileChanged()){
				tempTxDetailMasterModel.setSaleMobileNo(p2pDetailModel.getSenderMobile());
				tempTxDetailModel.setCustomField6(p2pDetailModel.getSenderMobile());

				//Expiring old mini transaction rows
				this.modifyPINSentMiniTransToExpired(tempTxDetailMasterModel.getTransactionCodeId());

				//generate 5 digit PIN and save it in MiniTransactionModel and send SMS to Sender
				MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
				String originalPin = "";
				String encryptedPin = "";
				originalPin = CommonUtils.generateOneTimePin(5);//RandomUtils.generateRandom(4, false, true);
				encryptedPin = EncoderUtils.encodeToSha(originalPin);
				//this will be only cash to cash
				miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)) ;

				miniTransactionModel.setMobileNo(p2pDetailModel.getSenderMobile());
				miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT) ;
				miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
				miniTransactionModel.setOneTimePin(encryptedPin);
				miniTransactionModel.setTAMT(p2pDetailModel.getTransactionAmount()) ;
				miniTransactionModel.setTransactionCodeId(tempTxDetailMasterModel.getTransactionCodeId());
				miniTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				miniTransactionModel.setCreatedOn(new Date());
				miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				miniTransactionModel.setUpdatedOn(new Date());
				miniTransactionModel.setTimeDate(new Date());
				miniTransactionModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());

				Object[] args = {
						brandName,
						p2pDetailModel.getSenderMobile(),
						p2pDetailModel.getTransactionCode(),
						originalPin};

				String smsText = MessageUtil.getMessage("updatep2p.sender.mobile", args);

				miniTransactionModel.setSmsText( p2pDetailModel.getSenderMobile() + " " + p2pDetailModel.getTransactionAmount() ) ;
				this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);

				this.sendSMSForUpdateP2P(p2pDetailModel.getSenderMobile(), smsText);

			}
			if(p2pDetailModel.isRecipientCNICChanged()){
				tempTxDetailMasterModel.setRecipientCnic(p2pDetailModel.getRecipientCNIC());
				tempTxDetailModel.setCustomField9(p2pDetailModel.getRecipientCNIC());

				Object[] args = {brandName,p2pDetailModel.getRecipientCNIC()};
				String smsText = MessageUtil.getMessage("updatep2p.recipient.cnic", args);
				this.sendSMSForUpdateP2P(p2pDetailModel.getRecipientMobile(), smsText);
			}
			if(p2pDetailModel.isRecipientMobileChanged()){
				tempTxDetailMasterModel.setRecipientMobileNo(p2pDetailModel.getRecipientMobile());
				tempTxDetailModel.setCustomField5(p2pDetailModel.getRecipientMobile());

				Object[] args = {brandName,p2pDetailModel.getRecipientMobile()};
				String smsText = MessageUtil.getMessage("updatep2p.recipient.mobile", args);
				this.sendSMSForUpdateP2P(p2pDetailModel.getRecipientMobile(), smsText);
			}

			//make P2P transaction ready for leg II
			tempTxDetailMasterModel.setUpdateP2PFlag(false);

			transactionDetailMasterManager.saveTransactionDetailMaster(tempTxDetailMasterModel);
			transactionDetailDAO.saveOrUpdate(tempTxDetailModel);


			if (p2pDetailModel.isSenderCNICChanged()) {
				/*Updating Sender LedgerModel*/
				AccountModel oldSenderAccountModel = new AccountModel();
				oldSenderAccountModel = accountManager.getAccountModelByCNIC(oldSenderCNIC+"-W");
				if(oldSenderAccountModel==null){
					oldSenderAccountModel = accountManager.getAccountModelByCNIC(p2pDetailModel.getSenderCNIC()+"-W");
				}
				AccountModel newSenderAccountModel = new AccountModel();
				newSenderAccountModel = accountManager.getAccountModelByCNIC(p2pDetailModel.getSenderCNIC()+"-W");
				LedgerModel senderLedgerModel = new LedgerModel();
				senderLedgerModel.setAccountId(oldSenderAccountModel.getAccountId());
				senderLedgerModel.setMicrobankTransactionCode(p2pDetailModel.getTransactionCode());
				baseWrapper.setBasePersistableModel(senderLedgerModel);
				baseWrapper = ledgerManager.loadLedgerEntry(baseWrapper);
				senderLedgerModel = (LedgerModel) baseWrapper.getBasePersistableModel();
				senderLedgerModel.setAccountId(newSenderAccountModel.getAccountId());
				baseWrapper.setBasePersistableModel(senderLedgerModel);
				ledgerManager.saveLedgerEntry(baseWrapper);
			}

			if (p2pDetailModel.isRecipientCNICChanged()) {
				/*Updating Recepient LedgerModel*/
				AccountModel oldRecepientAccountModel = new AccountModel();
				oldRecepientAccountModel = accountManager.getAccountModelByCNIC(oldRecipientCNIC+"-W");
				logger.info("[TransactionManagerImpl.updatep2pTxDetails]: Account loaded against CNIC "+oldRecipientCNIC+" with Account id "+oldRecepientAccountModel.getAccountId());
				/*if(oldRecepientAccountModel==null){
					oldRecepientAccountModel = accountManager.getAccountModelByCNIC(p2pDetailModel.getRecipientCNIC()+"-W");
					logger.info("[TransactionManagerImpl.updatep2pTxDetails]: Account loaded against new CNIC "+p2pDetailModel.getRecipientCNIC()+" with Account id "+oldRecepientAccountModel.getAccountId());
				}*/
				AccountModel newRecepientAccountModel = new AccountModel();
				newRecepientAccountModel = accountManager.getAccountModelByCNIC(p2pDetailModel.getRecipientCNIC()+"-W");
				logger.info("[TransactionManagerImpl.updatep2pTxDetails]: Account loaded against new CNIC "+p2pDetailModel.getRecipientCNIC()+" with new Recepient Account id "+newRecepientAccountModel.getAccountId());
				LedgerModel recepientLedgerModel = new LedgerModel();
				recepientLedgerModel.setToAccountId(oldRecepientAccountModel.getAccountId());
				recepientLedgerModel.setMicrobankTransactionCode(p2pDetailModel.getTransactionCode());
				logger.info("[TransactionManagerImpl.updatep2pTxDetails]: Ledger loaded against Sender Account "+recepientLedgerModel.getAccountId()+" and Recepient Account id "+recepientLedgerModel.getToAccountId());
				baseWrapper.setBasePersistableModel(recepientLedgerModel);
				baseWrapper = ledgerManager.loadLedgerEntry(baseWrapper);
				recepientLedgerModel = (LedgerModel) baseWrapper.getBasePersistableModel();
				logger.info("[TransactionManagerImpl.updatep2pTxDetails]: Going to change recepientledgermodel.toaccountid ");
				recepientLedgerModel.setToAccountId(newRecepientAccountModel.getAccountId());
				baseWrapper.setBasePersistableModel(recepientLedgerModel);
				ledgerManager.saveLedgerEntry(baseWrapper);
			}

			//this.actionLogManager.completeActionLog(actionLogModel);
			if(actionAuthId == null && actionLogModel.getActionStatusId() == 1L && actionLogModel != null)
				actionLogModel.setActionStatusId(2L);
			if(actionLogModel != null)
				actionLogManager.completeActionLog(actionLogModel);

		}catch (FrameworkCheckedException fce){
			throw new FrameworkCheckedException(fce.getLocalizedMessage());
		}catch (Exception e){
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getLocalizedMessage());
		}

		return baseWrapper;
	}

	@Override
	public void makeWalkinCustomer(String walkInCNIC, String walkInMobileNumber) throws FrameworkCheckedException {

		Boolean isWalkinCustomerExists = null;

		try {
			AppUserModel createdUpdatedByAppUserModel = UserUtils.getCurrentUser();
			ThreadLocalAppUser.setAppUserModel(createdUpdatedByAppUserModel);
			isWalkinCustomerExists = getMfsAccountManager().updateWalkinCustomerIfExists(walkInCNIC, walkInMobileNumber, createdUpdatedByAppUserModel.getMobileNo());

			if (isWalkinCustomerExists == false) {//need to create new walkin customer account.
				getMfsAccountManager().createWalkinCustomerAccount(walkInCNIC, walkInMobileNumber, createdUpdatedByAppUserModel.getMobileNo());
			}

		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkCheckedException(e.getLocalizedMessage());
		}

	}

	@Override
	public WorkFlowWrapper createTransactionModel(WorkFlowWrapper wrapper) throws FrameworkCheckedException {

		ProductModel productModel = wrapper.getProductModel();
		Date now = new Date();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(wrapper.getTransactionAmount());
		transactionModel.setTotalAmount(wrapper.getTransactionAmount());
		transactionModel.setTotalCommissionAmount(0.0);
		transactionModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
		transactionModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
		transactionModel.setPaymentModeId(wrapper.getPaymentModeModel().getPaymentModeId());
		transactionModel.setCustomerId(null);
		transactionModel.setTransactionTypeId(wrapper.getTransactionTypeModel().getTransactionTypeId());
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		transactionModel.setConfirmationMessage("-");
		transactionModel.setNotificationMobileNo("");
		transactionModel.setIssue(false);
		transactionModel.setDiscountAmount(0.0);
		transactionModel.setCreatedOn(now);
		transactionModel.setUpdatedOn(now);
		transactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		transactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		transactionModel.setCreatedOn(now);
		transactionModel.setUpdatedOn(now);
		transactionModel.setBankAccountNo((String)wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_SENDER));

		transactionModel.setProcessingSwitchId(6l);
		transactionModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);

		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setTransactionId(transactionModel.getTransactionId());
		transactionDetailModel.setActualBillableAmount(wrapper.getTransactionAmount());
		transactionDetailModel.setSettled(true);
		transactionDetailModel.setProductId(productModel.getProductId());
		transactionDetailModel.setCustomField2((String)wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_RECIPIENT));
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);

		wrapper.setTransactionModel(transactionModel);
		wrapper.setTransactionDetailModel(transactionDetailModel);

		saveTransaction(wrapper);

		TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel(true);
		txDetailMasterModel.setCreatedOn(now);
		txDetailMasterModel.setUpdatedOn(now);
		txDetailMasterModel.setTransactionId(transactionModel.getTransactionId());
		txDetailMasterModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
		txDetailMasterModel.setTransactionCode(wrapper.getTransactionCodeModel().getCode());
		txDetailMasterModel.setTotalAmount(wrapper.getTransactionAmount());
		txDetailMasterModel.setTransactionAmount(wrapper.getTransactionAmount());
		txDetailMasterModel.setProductId(productModel.getProductId());
		txDetailMasterModel.setProductName(productModel.getName());
		txDetailMasterModel.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
		txDetailMasterModel.setSupplierId(productModel.getSupplierId());
		txDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.COMPLETE_NAME);
		txDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		txDetailMasterModel.setPaymentModeId(wrapper.getPaymentModeModel().getPaymentModeId());
		txDetailMasterModel.setPaymentMode(PaymentModeConstantsInterface.paymentModeConstantsMap.get(wrapper.getPaymentModeModel().getPaymentModeId()));

		//sender account
		txDetailMasterModel.setBankAccountNo(wrapper.getTransactionModel().getBankAccountNo());
		txDetailMasterModel.setBankAccountNoLastFive(StringUtil.getLastFiveDigitsFromAccountNo(wrapper.getTransactionModel().getBankAccountNo()));

		//recipient account
		txDetailMasterModel.setRecipientAccountNo(wrapper.getTransactionDetailModel().getCustomField2());


		wrapper.setTransactionDetailMasterModel(txDetailMasterModel);

		transactionDetailMasterManager.saveTransactionDetailMaster(txDetailMasterModel);

		return wrapper;
	}

	private void sendSMSForUpdateP2P(String mobileNo,String smsText){
		try {
			SmsMessage message = new SmsMessage( mobileNo, smsText);
			smsSender.send( message );
		}catch( Exception e ){
			logger.error( "Unable to send UpdateP2P sms to: " + mobileNo + " Reason: " +e.getMessage(), e );
		}
	}

	@Override
	public SearchBaseWrapper loadP2PUpdateHistory(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException {
		P2PDetailModel p2pDetailModel = (P2PDetailModel) wrapper.getBasePersistableModel();
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		sortingOrderMap.put("createdOn", SortingOrder.DESC);
		wrapper.setSortingOrderMap(sortingOrderMap);
		CustomList<P2PDetailModel> customList = p2pDetailDAO.findByExample(p2pDetailModel,wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap());
		wrapper.setCustomList( customList );
		return wrapper;
	}

	private void modifyPINSentMiniTransToExpired(Long trxCodeId) throws FrameworkCheckedException{
		List<MiniTransactionModel> miniTransactionModelList = this.miniTransactionDAO.LoadMiniTransactionModelByPK(trxCodeId);
		Iterator<MiniTransactionModel> ite = miniTransactionModelList.iterator() ;
		while( ite.hasNext() ){
			MiniTransactionModel miniTransactionModel  = ite.next() ;
			miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.EXPIRED ) ;
			miniTransactionModel.setUpdatedOn(new Date());
			miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
		}
	}

	public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
		this.miniTransactionDAO = miniTransactionDAO;
	}
	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		if (accountManager != null) {
			this.accountManager = accountManager;
		}
	}

	public void setLedgerManager(LedgerManager ledgerManager) {
		if (ledgerManager != null) {
			this.ledgerManager = ledgerManager;
		}
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		if (mfsAccountManager != null) {
			this.mfsAccountManager = mfsAccountManager;
		}
	}

	private MfsAccountManager getMfsAccountManager()
	{
		ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		MfsAccountManager mfsAccountManager = (MfsAccountManager) webApplicationContext.getBean("mfsAccountManager");
		return mfsAccountManager;
	}

	public void setP2pDetailDAO(P2PDetailDAO p2pDetailDAO) {
		if (p2pDetailDAO != null) {
			this.p2pDetailDAO = p2pDetailDAO;
		}
	}

	public void setSmsSender(SmsSender smsSender) {
		if (smsSender != null) {
			this.smsSender = smsSender;
		}
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	@Override
	public MiniTransactionModel loadMiniTransactionModelByTransactionCode(String transactionCode) {
		return transactionDAO.loadMiniTransactionByTransactionCode(transactionCode);
	}

	@Override
	public MiniTransactionModel updateMiniTransactionRequiresNewTransaction(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
		miniTransactionModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		miniTransactionModel.setUpdatedOn(new Date());

		return this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
	}

	@Override
	public BaseWrapper loadAndLockMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		try{
			MiniTransactionModel miniTransactionModel = (MiniTransactionModel)baseWrapper.getBasePersistableModel();
			logger.info("[TransactionManagerImpl.loadAndLockMiniTransaction] Attempting to Lock Minitransaction. miniTransactionId: " + miniTransactionModel.getMiniTransactionId() + " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			baseWrapper = this.miniTransactionDAO.loadAndLockMinitransaction(baseWrapper);
			logger.info("[TransactionManagerImpl.loadAndLockMiniTransaction] Successfully acquired Lock on Minitransaction. miniTransactionId: " + miniTransactionModel.getMiniTransactionId() + " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		}catch(Exception ex){
			logger.error("[TransactionManagerImpl.loadAndLockMiniTransaction] Failed to Lock Minitransaction. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			ex.printStackTrace();
			throw new FrameworkCheckedException(ex.getMessage());
		}
		return baseWrapper;
	}

	@Override
	public BaseWrapper updateMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel)baseWrapper.getBasePersistableModel();
		miniTransactionModel.setUpdatedBy( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
		miniTransactionModel.setUpdatedOn(new Date()) ;
		baseWrapper.setBasePersistableModel(this.miniTransactionDAO.saveOrUpdate(miniTransactionModel));
		return baseWrapper;
	}

	public void setBookMeLogDAO(BookMeLogDAO bookMeLogDAO) {
		this.bookMeLogDAO = bookMeLogDAO;
	}


}