package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.SafRepoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;
import com.inov8.microbank.common.util.CreditAccountQueueSender;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.SafRepoDao;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.util.EncryptionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class CreditAccountQueingPreProcessor {

	private SafRepoDao safRepoDao;
	private WalletSafRepoDAO walletSafRepoDAO;
	private CreditAccountQueueSender creditAccountQueueSender;
	private AccountDAO accountDAO;
	private AccountManager	accountManager;

	private static final Log logger = LogFactory.getLog(CreditAccountQueingPreProcessor.class);
	
	
	public CreditAccountQueingPreProcessor(){}
	
	public void loadAndForwardAccountToQueue(String transactionCode) throws InterruptedException{
		if(StringUtil.isNullOrEmpty(transactionCode)){
			logger.error("[CreditAccountQueingPreProcessor.loadAndForwardAccountToQueue] TransactionCode supplied is Null");
			return;
		}
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
		workFlowWrapper.getTransactionCodeModel().setCode(transactionCode);
		this.startProcessing(workFlowWrapper);
		
	}

	public void startProcessing(WorkFlowWrapper wrapper){
		new QueingThread(wrapper).start();
	}
	
	private AccountModel loadAccountNumberById(Long accountId) throws FrameworkCheckedException{
		AccountModel acModel = accountDAO.findByPrimaryKey(accountId);
		return acModel;
	}

	public void setWalletSafRepoDAO(WalletSafRepoDAO walletSafRepoDAO) {
		this.walletSafRepoDAO = walletSafRepoDAO;
	}

	class QueingThread extends Thread{

		WorkFlowWrapper workFlowWrapper;
		
		QueingThread(WorkFlowWrapper workFlowWrapper){
			this.workFlowWrapper = workFlowWrapper;
		}
		
		@Override
		public void run() {
			synchronized (this) {
				OLAVO olaVO = null;
				AccountModel accountModel = null;
				try {
					Long productId = null;
					if (workFlowWrapper != null && workFlowWrapper.getProductModel() != null && workFlowWrapper.getProductModel().getProductId() != null)
						productId = workFlowWrapper.getProductModel().getProductId();
					if (productId != null && (productId.equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE) || productId.equals(ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE)))
						Thread.sleep(5000);
					SafRepoModel safRepoModel = new SafRepoModel();
					safRepoModel.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
					safRepoModel.setTransactionStatus(PortalConstants.SAF_STATUS_INITIATED);
					logger.info("[CreditAccountQueingPreProcessor] Loading transactions against trx_code: " + safRepoModel.getTransactionCode());
					CustomList<SafRepoModel> customList = safRepoDao.findByExample(safRepoModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
					List<SafRepoModel> safRepoModels = customList.getResultsetList();

					WalletSafRepoModel walletSafRepoModel = new WalletSafRepoModel();
					walletSafRepoModel.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
//					walletSafRepoModel.setTransactionStatus(PortalConstants.SAF_STATUS_INITIATED);
					logger.info("[CreditAccountQueingPreProcessor] Loading transactions against trx_code: " + walletSafRepoModel.getTransactionCode());
					CustomList<WalletSafRepoModel> walletCustomList = walletSafRepoDAO.findByExample(walletSafRepoModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
					List<WalletSafRepoModel> walletSafRepoModels = walletCustomList.getResultsetList();

					if (safRepoModels != null && safRepoModels.size() > 0) {
						for (SafRepoModel model : safRepoModels) {
							olaVO = new OLAVO();

							accountModel = loadAccountNumberById(model.getAccountId());
							olaVO.setPayingAccNo(EncryptionUtil.decryptAccountNo(accountModel.getAccountNumber()));

							olaVO.setTransactionTypeId(model.getTransactionTypeIdTransactionTypeModel().getTransactionTypeId().toString());
							olaVO.setTransactionDateTime(model.getTransactionTime());
							olaVO.setBalance(model.getTransactionAmount());
							olaVO.setMicrobankTransactionCode(model.getTransactionCode());
							olaVO.setReceivingAccountId(model.getAccountId());
							if (model.getProductId() != null)
								olaVO.setProductId(model.getProductId());

							if (model.getCategory() != null)
								olaVO.setCategory(model.getCategory());

							olaVO.setLedgerId(model.getLedgerId());
							olaVO.setIsViaQueue(true);
							olaVO.setCustomerAccountTypeId(model.getCustomerAccountTypeId());

							if (model.getReasonIdReasonModel() != null && model.getReasonIdReasonModel().getReasonId() != null)
								olaVO.setReasonId(model.getReasonIdReasonModel().getReasonId());

							try {
								accountManager.makeOLACreditDebitViaQueue(olaVO);
							} catch (Exception ee) {
								logger.error("Failed to process OLA Credit/Debit via Queue for LedgerId:" + olaVO.getLedgerId());
								ee.printStackTrace();
							}
						}
					}

					if (walletSafRepoModels != null && walletSafRepoModels.size() > 0) {
						for (WalletSafRepoModel model : walletSafRepoModels) {
							olaVO = new OLAVO();

							accountModel = loadAccountNumberById(model.getAccountId());
							olaVO.setPayingAccNo(EncryptionUtil.decryptAccountNo(accountModel.getAccountNumber()));

							olaVO.setTransactionTypeId(model.getTransactionTypeIdTransactionTypeModel().getTransactionTypeId().toString());
							olaVO.setTransactionDateTime(model.getTransactionTime());
							olaVO.setBalance(model.getTransactionAmount());
							olaVO.setMicrobankTransactionCode(model.getTransactionCode());
							olaVO.setReceivingAccountId(model.getAccountId());
							if (model.getProductId() != null)
								olaVO.setProductId(model.getProductId());

							if (model.getCategory() != null)
								olaVO.setCategory(model.getCategory());

							olaVO.setLedgerId(model.getLedgerId());
							olaVO.setIsViaQueue(true);
							olaVO.setCustomerAccountTypeId(model.getCustomerAccountTypeId());

							if (model.getReasonIdReasonModel() != null && model.getReasonIdReasonModel().getReasonId() != null)
								olaVO.setReasonId(model.getReasonIdReasonModel().getReasonId());

							try {
								accountManager.makeWalletOLACreditDebitViaQueue(olaVO);
							} catch (Exception ee) {
								logger.error("Failed to process OLA Credit/Debit via Queue for LedgerId:" + olaVO.getLedgerId());
								ee.printStackTrace();
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
		}
	}
	
	public SafRepoDao getSafRepoDao() {
		return safRepoDao;
	}

	public void setSafRepoDao(SafRepoDao safRepoDao) {
		this.safRepoDao = safRepoDao;
	}

	public CreditAccountQueueSender getCreditAccountQueueSender() {
		return creditAccountQueueSender;
	}

	public void setCreditAccountQueueSender(
			CreditAccountQueueSender creditAccountQueueSender) {
		this.creditAccountQueueSender = creditAccountQueueSender;
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}
	
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
}
