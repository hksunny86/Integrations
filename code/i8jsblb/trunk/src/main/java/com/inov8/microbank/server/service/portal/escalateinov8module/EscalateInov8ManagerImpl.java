package com.inov8.microbank.server.service.portal.escalateinov8module;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.PostLoadHibernateCallback;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.portal.escalateinov8module.EscalateInov8ListViewModel;
import com.inov8.microbank.common.model.portal.escalateinov8module.I8EscalateModel;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.server.dao.portal.escalateinov8.EscalateInov8DAO;
import com.inov8.microbank.server.dao.portal.escalateinov8.EscalateInov8ListViewDAO;
import com.inov8.microbank.server.dao.portal.issuemodule.IssueDAO;
import com.inov8.microbank.server.dao.portal.issuemodule.hibernate.IssueHibernateDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailDAO;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;

public class EscalateInov8ManagerImpl implements EscalateInov8Manager {

	private EscalateInov8ListViewDAO escalateInov8ListViewDAO;
	private EscalateInov8DAO escalateInov8DAO;
	private IssueDAO issueDAO;
	private TransactionDetailDAO transactionDetailDAO;
	private IssueManager issueManager;

	public BaseWrapper makeResolveDispute(BaseWrapper baseWrapper) throws FrameworkCheckedException 
    {
	  I8EscalateModel i8EscalateModel  = (I8EscalateModel)baseWrapper.getObject("i8EscalateModel");
	  
		// getting issue model from issueId
		IssueHibernateDAO issueHibernateDAO = (IssueHibernateDAO)issueDAO;
		EscalateToInov8HibernateCallback chargCallback = new EscalateToInov8HibernateCallback();
		IssueModel issueModel = issueHibernateDAO.findByPrimaryKey(i8EscalateModel.getIssueId(), chargCallback);
		
		Long transactionId = issueModel.getTransactionId();
		
		//TransactionDetailModel transactionDetailModel = chargCallback.getTransactionDetailModel();
	
	  //create issueModel for creating new issue	
	  IssueModel newIssueModel = null;
	  //AppUserModel appUserModel = null;
	  
		if (IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.equals(issueModel.getIssueTypeStatusId())) {
			
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject("supplierId", chargCallback.getSupplierModel().getSupplierId());
			Long appUserId = escalateInov8ListViewDAO.findAppUserBySupplierId(baseWrapper);

			//if there is no user then generate error message
			if(appUserId == null || appUserId.longValue() == 0 ){
				baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("error", "true");
				return baseWrapper; 
			}
			
			//close issue and setting updated by field
			issueModel.setIssueTypeStatusId(IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFM);
			issueModel.setComments("Issue forwarded as a customer dispute.");
			baseWrapper.setBasePersistableModel(issueModel);

			//calling issueManger for update issue
			issueManager.createOrUpdateIssue(baseWrapper,false);
			
			//create issueModel for create new issue
			newIssueModel = new IssueModel();
			newIssueModel.setIssueTypeStatusId(IssueTypeStatusConstantsInterface.DISPUTE_NEW );
			newIssueModel.setComments(i8EscalateModel.getIssueDetail());
			newIssueModel.setUpdatedBy(appUserId);
			newIssueModel.setCreatedBy(appUserId);
			newIssueModel.setTransactionId(transactionId);
			
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(newIssueModel);
			
			//calling issueManger for create issue
			baseWrapper = issueManager.createOrUpdateIssue(baseWrapper,true);
		} else {

			//create new issue for dispute navigation path
			EscalateToInov8DisHibernateCallback dispCall = new EscalateToInov8DisHibernateCallback();
			issueModel = issueHibernateDAO.findByPrimaryKey(i8EscalateModel.getIssueId(), dispCall);
			
			//getting bank app user
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject("bankId", dispCall.getBankModel().getBankId());
			Long bankUserId = escalateInov8ListViewDAO.findAppUserByBankId(baseWrapper);

			//if there is no user then generate error message
			if(bankUserId == null || bankUserId.longValue() == 0 ){
				baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("error", "true");
				return baseWrapper; 
			}

			//close issue and setting updated by field
			issueModel.setIssueTypeStatusId(IssueTypeStatusConstantsInterface.INOV8_DISPUTE_INVALID);
			issueModel.setComments("Issue forwarded as a charge back.");
			//updating current issue
			baseWrapper.setBasePersistableModel(issueModel);

//			calling issueManger for update issue
			issueManager.createOrUpdateIssue(baseWrapper,false);
			
			//creating new issue
			newIssueModel = new IssueModel();
			newIssueModel.setIssueTypeStatusId(IssueTypeStatusConstantsInterface.CHARGEBACK_NEW );
			newIssueModel.setComments(i8EscalateModel.getIssueDetail());
			newIssueModel.setUpdatedBy(bankUserId);
			newIssueModel.setCreatedBy(bankUserId);
			newIssueModel.setTransactionId(transactionId);

			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(newIssueModel);
			
			//calling issueManger for create issue
			baseWrapper = issueManager.createOrUpdateIssue(baseWrapper,true);
		}
	  
	  return baseWrapper;
 }

	public BaseWrapper retrieveI8EscalateForm(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		I8EscalateModel i8EscalateModel = new I8EscalateModel();

		Long issueId = new Long(baseWrapper.getObject("issueId").toString());

		// getting issue model from issueId
		IssueHibernateDAO dao = (IssueHibernateDAO)issueDAO;
		EscalateToInov8HibernateCallback callback = new EscalateToInov8HibernateCallback();

		IssueModel issueModel = dao.findByPrimaryKey(issueId, callback);

		Long transactionId = issueModel.getTransactionId();
				
		if (IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.equals(issueModel.getIssueTypeStatusId())) {
			
			i8EscalateModel.setEscalateToPartner( callback.getSupplierModel().getName() );

		} else {
			EscalateToInov8DisHibernateCallback dispCallback = new EscalateToInov8DisHibernateCallback();
			issueModel = dao.findByPrimaryKey(issueId, dispCallback);
			
			i8EscalateModel.setEscalateToPartner(dispCallback.getBankModel().getName());
		}

		// setting transaction id and issue id
		i8EscalateModel.setTransactionId(transactionId);
		i8EscalateModel.setIssueId(issueModel.getIssueId());
		baseWrapper.putObject("i8EscalateModel", i8EscalateModel);

		return baseWrapper;
	}

	public SearchBaseWrapper searchEscalateInov8Status(
			SearchBaseWrapper searchBaseWrapper)

	{
		CustomList<EscalateInov8ListViewModel> list = this.escalateInov8ListViewDAO
				.findByExample((EscalateInov8ListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap(), searchBaseWrapper
						.getDateRangeHolderModel());

		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;

	}
	/*		

	public BaseWrapper updateStatusByInov8(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		IssueModel tempIssueModel = (IssueModel) baseWrapper
				.getBasePersistableModel();
		IssueModel issueModel = (IssueModel) this.escalateInov8DAO
				.findByPrimaryKey(tempIssueModel.getIssueId());

		if (issueModel == null)
			throw new FrameworkCheckedException("Issue is null");

		issueModel.setUpdatedOn(tempIssueModel.getUpdatedOn());
		issueModel.setUpdatedBy(tempIssueModel.getUpdatedBy());
		issueModel.setIssueTypeStatusId(tempIssueModel.getIssueTypeStatusId());

		issueModel = this.escalateInov8DAO.saveOrUpdate(issueModel);
		baseWrapper.setBasePersistableModel(issueModel);
		return baseWrapper;
		
	}
		*/

	public void setEscalateInov8ListViewDAO(
			EscalateInov8ListViewDAO escalateInov8ListViewDAO) {
		this.escalateInov8ListViewDAO = escalateInov8ListViewDAO;
	}

	public void setEscalateInov8DAO(EscalateInov8DAO escalateInov8DAO) {
		this.escalateInov8DAO = escalateInov8DAO;
	}

	/**
	 * @param issueDAO
	 *            the issueDAO to set
	 */
	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	/**
	 * @param transactionDetailDAO
	 *            the transactionDetailDAO to set
	 */
	public void setTransactionDetailDAO(
			TransactionDetailDAO transactionDetailDAO) {
		this.transactionDetailDAO = transactionDetailDAO;
	}

	// public BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper)
	// throws
	// FrameworkCheckedException
	// {
	// //IssueModel issueModel = new IssueModel();
	// TransactionModel transactionModel = (TransactionModel)
	// baseWrapper.getBasePersistableModel();
	//
	// transactionModel = this.transactionDAO.saveOrUpdate( (
	// TransactionModel) baseWrapper.getBasePersistableModel());
	// baseWrapper.setBasePersistableModel(transactionModel);
	// return baseWrapper;
	//
	// }
	
	private class EscalateToInov8HibernateCallback implements PostLoadHibernateCallback
	{
		private SupplierModel supplierModel = null;
		private TransactionDetailModel transactionDetailModel = null;
		private ProductModel productModel = null; 
		
		public EscalateToInov8HibernateCallback ()
		{
		}

		public <T extends BasePersistableModel> T postLoad(T issueModel, Session session)
		{
			IssueModel model = (IssueModel)issueModel;
			// get the transaction detail
			List<TransactionDetailModel> tDetails;
			try 
			{
				tDetails = new ArrayList<TransactionDetailModel>(model.getTransactionIdTransactionModel().getTransactionIdTransactionDetailModelList());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new RuntimeException("Exception => ", e);
			}
			//Assuming that there will be only one detail against a transaction
			this.transactionDetailModel = tDetails.get(0);
			this.productModel = this.transactionDetailModel.getProductIdProductModel();
			this.supplierModel = this.productModel.getSupplierIdSupplierModel();
			return issueModel;
		}
		
		
		/**
		 * @return the supplierModel
		 */
		public SupplierModel getSupplierModel() {
			return supplierModel;
		}


		/**
		 * @return the productModel
		 */
		public ProductModel getProductModel() {
			return productModel;
		}


		/**
		 * @return the transactionDetailModel
		 */
		public TransactionDetailModel getTransactionDetailModel() {
			return transactionDetailModel;
		}
		
	}
	
	private class EscalateToInov8DisHibernateCallback implements PostLoadHibernateCallback
	{
		private TransactionModel transactionModel = null;
		private SmartMoneyAccountModel smartMoneyAccountModel = null;
		private BankModel bankModel = null; 
		
		public EscalateToInov8DisHibernateCallback ()
		{
		}

		public <T extends BasePersistableModel> T postLoad(T issueModel, Session session)
		{
			IssueModel model = (IssueModel)issueModel;
			
			//getting transaction model
			this.transactionModel = model.getTransactionIdTransactionModel(); 
			this.smartMoneyAccountModel = transactionModel.getSmartMoneyAccountIdSmartMoneyAccountModel();
			this.bankModel = smartMoneyAccountModel.getBankIdBankModel();
			
			return issueModel;
		}

		/**
		 * @return the bankModel
		 */
		public BankModel getBankModel() {
			return bankModel;
		}

		/**
		 * @param bankModel the bankModel to set
		 */
		public void setBankModel(BankModel bankModel) {
			this.bankModel = bankModel;
		}

		/**
		 * @return the smartMoneyAccountModel
		 */
		public SmartMoneyAccountModel getSmartMoneyAccountModel() {
			return smartMoneyAccountModel;
		}

		/**
		 * @param smartMoneyAccountModel the smartMoneyAccountModel to set
		 */
		public void setSmartMoneyAccountModel(
				SmartMoneyAccountModel smartMoneyAccountModel) {
			this.smartMoneyAccountModel = smartMoneyAccountModel;
		}

		/**
		 * @return the transactionModel
		 */
		public TransactionModel getTransactionModel() {
			return transactionModel;
		}

		/**
		 * @param transactionModel the transactionModel to set
		 */
		public void setTransactionModel(TransactionModel transactionModel) {
			this.transactionModel = transactionModel;
		}	
	
	}
	/**
	 * @param issueManager the issueManager to set
	 */
	public void setIssueManager(IssueManager issueManager) {
		this.issueManager = issueManager;
	}

}
