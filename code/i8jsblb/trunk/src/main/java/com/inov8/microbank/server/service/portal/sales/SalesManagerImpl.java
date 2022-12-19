package com.inov8.microbank.server.service.portal.sales;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.hibernate.GenericHibernateDao;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.salesmodule.SalesListViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.sales.SalesDAO;
import com.inov8.microbank.server.dao.portal.sales.SalesListViewDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */
public class SalesManagerImpl implements SalesManager {

	private SalesListViewDAO salesListViewDAO;
	private SalesDAO salesDAO;
	private TransactionDAO transactionDAO;
	private GenericHibernateDao genericHibernateDAO;
	private ActionLogManager actionLogManager;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	
	public SearchBaseWrapper searchSales(SearchBaseWrapper searchBaseWrapper)

	{
		CustomList<SalesListViewModel> list = this.salesListViewDAO
				.findByExample((SalesListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap(), searchBaseWrapper
						.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;

	}

	public BaseWrapper loadTransaction(BaseWrapper baseWrapper) {
		TransactionModel transactionModel = (TransactionModel) this.transactionDAO
				.findByPrimaryKey((baseWrapper.getBasePersistableModel())
						.getPrimaryKey());
		baseWrapper.setBasePersistableModel(transactionModel);
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// IssueModel issueModel = new IssueModel();
		TransactionModel transactionModel = (TransactionModel) baseWrapper
				.getBasePersistableModel();

		transactionModel = this.transactionDAO
				.saveOrUpdate((TransactionModel) baseWrapper
						.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(transactionModel);
		return baseWrapper;

	}

	public SearchBaseWrapper getSupplierProcessingStatus(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
		// CriteriaConfiguration criteriaConfiguration = new
		// CriteriaConfiguration();

		// List <SupplierProcessingStatusModel>supplierProcessingStatusModelList
		// =
		// genericHibernateDAO.findEntityByExample(supplierProcessingStatusModel,
		// criteriaConfiguration);

		List<SupplierProcessingStatusModel> supplierProcessingStatusModelList = genericHibernateDAO
				.findByHQL("from SupplierProcessingStatusModel a order by a.name");

		CustomList<SupplierProcessingStatusModel> customList = new CustomList<SupplierProcessingStatusModel>(
				supplierProcessingStatusModelList);
		searchBaseWrapper.setCustomList(customList);

		return searchBaseWrapper;
	}

	public BaseWrapper updateTransactionSupStatus(BaseWrapper baseWrapper) throws
 FrameworkCheckedException{
	 
	 TransactionModel transactionModel = (TransactionModel)baseWrapper.getBasePersistableModel();
	 Long supProcessingStatusId = transactionModel.getSupProcessingStatusId();
	 
	 // getting fresh data of transaction table
	 transactionModel = transactionDAO.findByPrimaryKey(transactionModel.getTransactionId());
	 // saving old supProcessing StatusId to save in log table
	 Long oldSupProcessingStatusId = transactionModel.getSupProcessingStatusId();
	 
	 String transactionCode = "";
	 // transactionCode Id to save in custom field 4
	 if(transactionModel.getTransactionCodeId() != null){
		 transactionCode  = transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();	 
	 }
	 
	 // log activities
	 ActionLogModel actionLogModel = new ActionLogModel();

	 actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
	 actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
	 actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
	 actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
	 actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
	 actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
	 actionLogModel.setCustomField3(oldSupProcessingStatusId+","+supProcessingStatusId);
	 actionLogModel.setCustomField4(transactionCode);
	 actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
	 actionLogModel = logAction(actionLogModel); 
	 
	 
	 // update transaction table data
	 transactionModel.setSupProcessingStatusId(supProcessingStatusId);
	 transactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	 transactionModel.setUpdatedOn(new Date());
	 transactionDAO.saveOrUpdate(transactionModel);
	 
	 //getting appUserId from MfsId
	 UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
	 userDeviceAccountsModel.setUserId(transactionModel.getMfsId());
	 CustomList <UserDeviceAccountsModel>customeUserDeviceList = userDeviceAccountsDAO.findByExample(userDeviceAccountsModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
	 List <UserDeviceAccountsModel>list = customeUserDeviceList.getResultsetList();
	 userDeviceAccountsModel = list.get(0);
	 
	//end log activities 
	actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
	actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
	if(userDeviceAccountsModel != null){
		String appUserId = userDeviceAccountsModel.getAppUserId()==null?"":userDeviceAccountsModel.getAppUserId().toString();	
		actionLogModel.setCustomField1(appUserId);	
	}	
	actionLogModel = logAction(actionLogModel);
	 
    baseWrapper.putObject("success", "true");
	 
	 return baseWrapper;
 }

	/**
	 * Method Logs the action performed in the action log table
	 */
	private ActionLogModel logAction(ActionLogModel actionLogModel)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (null == actionLogModel.getActionLogId()) {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		} else {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}

	public void setSalesListViewDAO(SalesListViewDAO salesListViewDAO) {
		this.salesListViewDAO = salesListViewDAO;
	}

	public void setSalesDAO(SalesDAO salesDAO) {
		this.salesDAO = salesDAO;
	}

	public void setTransactionDAO(TransactionDAO transactionDAO) {
		this.transactionDAO = transactionDAO;
	}

	/**
	 * @param genericHibernateDAO
	 *            the genericHibernateDAO to set
	 */
	public void setGenericHibernateDAO(GenericHibernateDao genericHibernateDAO) {
		this.genericHibernateDAO = genericHibernateDAO;
	}

	/**
	 * @param actionLogManager
	 *            the actionLogManager to set
	 */
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	/**
	 * @param userDeviceAccountsDAO the userDeviceAccountsDAO to set
	 */
	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

}
