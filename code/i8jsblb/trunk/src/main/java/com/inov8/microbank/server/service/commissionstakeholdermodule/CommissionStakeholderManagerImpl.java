package com.inov8.microbank.server.service.commissionstakeholdermodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.commissionmodule.CommShAcctsListViewModel;
import com.inov8.microbank.common.model.commissionmodule.CommStakeholderListViewModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesDefaultDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesViewDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionstakeholdermodule.CommissionStakeholderAccountsListViewDAO;
import com.inov8.microbank.server.dao.commissionstakeholdermodule.CommissionStakeholderListViewDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionStakeholderManagerImpl implements CommissionStakeholderManager 
{

	private CommissionStakeholderListViewDAO commissionStakeholderListViewDAO;
	private CommissionStakeholderDAO commissionStakeholderDAO;
	private CommissionStakeholderAccountsListViewDAO commissionStakeholderAccountsListViewDAO;
	private StakeholderBankInfoDAO stakeholderBankInfoDAO;
	
	private CommissionShSharesViewDAO commissionShSharesViewDAO;
	private CommissionShSharesDAO commissionShSharesDAO;
	private ActionLogManager actionLogManager;
	private CommissionShSharesDefaultDAO commissionShSharesDefaultDAO;
	
	public CommissionStakeholderManagerImpl()
	{
	
	}

	@Override
	public SearchBaseWrapper loadCommissionStakeholder(SearchBaseWrapper searchBaseWrapper)
	{
		CommissionStakeholderModel commissionStakeholderModel = this.commissionStakeholderDAO.findByPrimaryKey(searchBaseWrapper.
		        getBasePersistableModel().getPrimaryKey());
		    searchBaseWrapper.setBasePersistableModel(commissionStakeholderModel);
		    return searchBaseWrapper;
	}

	@Override
	public List<CommissionShSharesViewModel> loadCommissionStakeholderSharesViewList(SearchBaseWrapper wrapper)
	{
	    List<CommissionShSharesViewModel> list =  null;
		CommissionShSharesViewModel commissionShSharesViewModel = (CommissionShSharesViewModel)wrapper.getBasePersistableModel();
		PagingHelperModel pagingHelperModel = wrapper.getPagingHelperModel();

		Long primaryKey = commissionShSharesViewModel.getPrimaryKey();
		if( primaryKey != null && primaryKey > 0 )
		{
		    CommissionShSharesViewModel commShSharesViewModel = commissionShSharesViewDAO.findByPrimaryKey( primaryKey );
		    list = new ArrayList<>( 1 );
		    if( commShSharesViewModel != null )
		    {
		        list.add( commShSharesViewModel );
		    }
		    pagingHelperModel.setTotalRecordsCount( list.size() );
		}
		else
		{
		    CustomList<CommissionShSharesViewModel> customList =  commissionShSharesViewDAO.findByExample(commissionShSharesViewModel, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap());
		    wrapper.setCustomList( customList );
		    if( customList != null )
		    {
		        list = customList.getResultsetList();
		    }
		}
		return list;
	}
	
	@Override
	public List<CommissionShSharesModel> loadCommissionShSharesList(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
	    List<CommissionShSharesModel> list =  null;
		CommissionShSharesModel commissionShSharesModel = (CommissionShSharesModel)wrapper.getBasePersistableModel();

		CustomList<CommissionShSharesModel> customList =  commissionShSharesDAO.findByExample(commissionShSharesModel);
		wrapper.setCustomList( customList );
		if( customList != null )
		{
			list = customList.getResultsetList();
		}
		
		return list;
	}

	@Override
	public List<CommissionShSharesDefaultModel> loadDefaultCommissionShSharesList(Long productId) throws FrameworkCheckedException
	{
	    /*List<CommissionShSharesDefaultModel> list =  null;
	    CommissionShSharesDefaultModel defaultShSharesModel = new CommissionShSharesDefaultModel();
	    defaultShSharesModel.setProductId(productId);
	    
		CustomList<CommissionShSharesDefaultModel> customList =  commissionShSharesDefaultDAO.findByExample(defaultShSharesModel);
		if( customList != null )
		{
			list = customList.getResultsetList();
		}*/
		List<CommissionShSharesDefaultModel> defaultList = commissionShSharesDefaultDAO.commissionShSharesDefaultLoad(productId);
		return defaultList;
	}

	@Override
	public List<CommissionStakeholderModel> loadCommissionStakeholdersList(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
	    /*List<CommissionStakeholderModel> list =  null;
	    CommissionStakeholderModel commissionShSharesModel = (CommissionStakeholderModel)wrapper.getBasePersistableModel();

		CustomList<CommissionStakeholderModel> customList =  commissionStakeholderDAO.findByExample(commissionShSharesModel);
		wrapper.setCustomList( customList );
		if( customList != null )
		{
			list = customList.getResultsetList();
		}
		return list;*/
	    return commissionStakeholderDAO.findAllStakeHolders();
	}

	@Override
	public SearchBaseWrapper searchCommissionStakeholder(SearchBaseWrapper searchBaseWrapper)
	{
		CustomList<CommStakeholderListViewModel>
		list = this.commissionStakeholderListViewDAO.findByExample( (CommStakeholderListViewModel)
				searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(),
				searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	@Override
	public BaseWrapper updateCommissionStakeholder(BaseWrapper baseWrapper)
	{
		CommissionStakeholderModel commissionStakeholderModel = (CommissionStakeholderModel) baseWrapper.getBasePersistableModel();
		CommissionStakeholderModel newCommissionStakeholderModel = new CommissionStakeholderModel();
	    newCommissionStakeholderModel.setCommissionStakeholderId(commissionStakeholderModel.getCommissionStakeholderId());

	    int recordCount = commissionStakeholderDAO.countByExample(newCommissionStakeholderModel);

	    if (recordCount != 0 && commissionStakeholderModel.getPrimaryKey() != null)
	    {
	    	commissionStakeholderModel = this.commissionStakeholderDAO.saveOrUpdate( (CommissionStakeholderModel) baseWrapper.
	                                            getBasePersistableModel());
	      baseWrapper.setBasePersistableModel(commissionStakeholderModel);
	      return baseWrapper;
	    }
	    else
	    {
	      baseWrapper.setBasePersistableModel(null);
	      return baseWrapper;
	    }	
	}

	@Override
	public BaseWrapper createCommissionStakeholder(BaseWrapper baseWrapper)
	{
		int recordCount;
		Date nowDate = new Date();
		CommissionStakeholderModel newCommissionStakeholderModel = new CommissionStakeholderModel();
				
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		CommissionStakeholderModel commissionStakeholderModel = (CommissionStakeholderModel) baseWrapper.getBasePersistableModel();
		
		// Checking stakeholder of partners
		CommissionStakeholderModel tempCommissionStakeholderModel = new CommissionStakeholderModel();
		tempCommissionStakeholderModel.setOperatorId(commissionStakeholderModel.getOperatorId());
		tempCommissionStakeholderModel.setBankId(commissionStakeholderModel.getBankId());
		
		recordCount = commissionStakeholderDAO.countByExample(tempCommissionStakeholderModel);
		if(recordCount != 0){
			baseWrapper.setBasePersistableModel(new CommissionStakeholderModel());
			return baseWrapper;
		}
		
		newCommissionStakeholderModel.setName(commissionStakeholderModel.getName());
		
		recordCount = commissionStakeholderDAO.countByExample(newCommissionStakeholderModel,exampleHolder);
		commissionStakeholderModel.setCreatedOn(nowDate);
		commissionStakeholderModel.setUpdatedOn(nowDate);

		if (recordCount == 0)
		{
			baseWrapper.setBasePersistableModel(this.commissionStakeholderDAO.saveOrUpdate(commissionStakeholderModel));
			return baseWrapper;
		}
		else
		{
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;
		}	
	}
	
	public boolean saveCommissionShShares(List<CommissionShSharesModel> commissionShSharesList) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.COMM_SH_SHARES_CREATE_UPDATE_USECASE_ID ) );

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		Collection<CommissionShSharesModel> updatedList = null;
		try{
			updatedList = this.commissionShSharesDAO.saveOrUpdateCollection(commissionShSharesList);
		
			/*if( ! CollectionUtils.isEmpty(updatedList)){//causing exception in action log report as custom field 1 expects an integer.therefore removed.
				String ids = generateIdsStringFromModelList(updatedList);
				actionLogModel.setCustomField1(ids);
			}*/
		}catch(Exception ex){
			throw ex;
		}finally{
			
			this.completeActionLogRequiresNewTransaction(actionLogModel);
			
		}
		
		return updatedList.size() > 0;
	}
	
	private ActionLogModel completeActionLogRequiresNewTransaction(ActionLogModel actionLogModel) throws FrameworkCheckedException{
		return this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
	}

	private String generateIdsStringFromModelList(Collection<CommissionShSharesModel> updatedList){
		List<Long> ids = new ArrayList<Long>();
		for(CommissionShSharesModel model: updatedList){
			ids.add(model.getPrimaryKey());
		}
		
		String idsString = StringUtil.getCommaSeparatedStringFromLongList(ids);
		return idsString;
	}
	
	public boolean removeCommissionShSharesByStakeholderIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean removed = false;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.COMM_SH_SHARES_CREATE_DELETE_USECASE_ID ) );

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		removed = commissionShSharesDAO.removeCommissionShSharesByStakeholderIds(removeSharesList);
		
		//Hotfix as per instructions by Khawar Baig
		//actionLogModel.setCustomField1(StringUtil.getCommaSeparatedStringFromLongList(removeSharesList));
		this.actionLogManager.completeActionLog(actionLogModel);
		
		return removed;
	}

	public boolean removeCommissionShSharesByShShareIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean removed = false;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.COMM_SH_SHARES_CREATE_DELETE_USECASE_ID ) );

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		removed = commissionShSharesDAO.removeCommissionShSharesByShShareIds(removeSharesList);
		
		//Hotfix as per instructions by Khawar Baig
		//actionLogModel.setCustomField1(StringUtil.getCommaSeparatedStringFromLongList(removeSharesList));
		 this.actionLogManager.completeActionLog(actionLogModel);
		
		return removed;
	}
	
	public List<CommissionShSharesModel>  loadCommissionShSharesList(CommissionShSharesModel vo) throws FrameworkCheckedException {
		
		List<CommissionShSharesModel> stakeholderSharesList = commissionShSharesDAO.loadCommissionShSharesList(vo);
		return stakeholderSharesList;
	}

	public void setCommissionStakeholderDAO(CommissionStakeholderDAO commissionStakeholderDAO)
	{
		this.commissionStakeholderDAO = commissionStakeholderDAO;
	}
		
	public void setCommissionStakeholderListViewDAO(CommissionStakeholderListViewDAO commissionStakeholderListViewDAO)
	{
		this.commissionStakeholderListViewDAO = commissionStakeholderListViewDAO;
	}

	@Override
	public SearchBaseWrapper searchCommissionStakeholderAccounts(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		CommShAcctsListViewModel commShAcctsListViewModel = (CommShAcctsListViewModel)searchBaseWrapper.getBasePersistableModel();
		CustomList<CommShAcctsListViewModel> list =  commissionStakeholderAccountsListViewDAO.findByExample(commShAcctsListViewModel,searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
		
	}
	@Override
	public SearchBaseWrapper searchCommissionStakeholderAccountsByCriteria(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		CommShAcctsListViewModel commShAcctsListViewModel = (CommShAcctsListViewModel)searchBaseWrapper.getBasePersistableModel();
		CustomList<CommShAcctsListViewModel> list =  commissionStakeholderAccountsListViewDAO.searchCommissionStakeholderAccountsByCriteria(commShAcctsListViewModel,searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
		
	}

	public void setCommissionStakeholderAccountsListViewDAO(
			CommissionStakeholderAccountsListViewDAO commissionStakeholderAccountsListViewDAO) {
		this.commissionStakeholderAccountsListViewDAO = commissionStakeholderAccountsListViewDAO;
	}

	@Override
	public BaseWrapper createCommissionStakeholderAccounts(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel) baseWrapper.getBasePersistableModel();
		baseWrapper.setBasePersistableModel(stakeholderBankInfoDAO.saveOrUpdate(stakeholderBankInfoModel));
		return baseWrapper;
	}
	
	@Override
	public BaseWrapper saveCommissionStakeholderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		CommissionStakeholderModel commissionStakeholderModel = (CommissionStakeholderModel) baseWrapper.getBasePersistableModel();
		baseWrapper.setBasePersistableModel(commissionStakeholderDAO.saveOrUpdate(commissionStakeholderModel));
		return baseWrapper;
	}

	public void setStakeholderBankInfoDAO(
			StakeholderBankInfoDAO stakeholderBankInfoDAO) {
		this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
	}

	public void setCommissionShSharesViewDAO(
			CommissionShSharesViewDAO commissionShSharesViewDAO) {
		this.commissionShSharesViewDAO = commissionShSharesViewDAO;
	}

	public void setCommissionShSharesDAO(CommissionShSharesDAO commissionShSharesDAO) {
		this.commissionShSharesDAO = commissionShSharesDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setCommissionShSharesDefaultDAO(
			CommissionShSharesDefaultDAO commissionShSharesDefaultDAO) {
		this.commissionShSharesDefaultDAO = commissionShSharesDefaultDAO;
	}

}
