package com.inov8.microbank.server.service.portal.partnergroupmodule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.AppUserPartnerViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.GroupPermissionViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupListViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupPermissionListViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.AppUserPartnerViewDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.GroupPermissionViewDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerGroupDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerGroupListViewDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerPermissionViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class PartnerGroupManagerImpl implements PartnerGroupManager{
	private PartnerGroupListViewDAO partnerGroupListViewDAO;
	private PartnerGroupDAO partnerGroupDAO;
	private GroupPermissionViewDAO groupPermissionViewDAO;
	private PartnerPermissionViewDAO partnerPermissionViewDAO;
	private AppUserPartnerViewDAO appUserPartnerViewDAO;
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	private MessageSource messageSource;
	private ActionLogManager actionLogManager;
	private SessionRegistry sessionRegistry;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public AppUserPartnerViewDAO getAppUserPartnerViewDAO() {
		return appUserPartnerViewDAO;
	}

	public AppUserPartnerGroupDAO getAppUserPartnerGroupDAO() {
		return appUserPartnerGroupDAO;
	}

	public BaseWrapper createPartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		PartnerGroupModel partnerGroupModel = (PartnerGroupModel)baseWrapper.getBasePersistableModel();				
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		baseWrapper.setBasePersistableModel(this.partnerGroupDAO.saveOrUpdate(partnerGroupModel));		

		actionLogModel.setCustomField1(partnerGroupModel.getPartnerGroupId().toString());
		actionLogModel.setCustomField11(partnerGroupModel.getName());
		this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;
	}

	public SearchBaseWrapper loadPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		searchBaseWrapper.setBasePersistableModel(this.partnerGroupListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey()));
		
		return searchBaseWrapper;
	}
	
	public BaseWrapper loadPartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		baseWrapper.setBasePersistableModel(this.partnerGroupDAO
				.findByPrimaryKey(baseWrapper.getBasePersistableModel()
						.getPrimaryKey()));
		
		return baseWrapper;
	}

	public BaseWrapper loadPartnerGroupView(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel)baseWrapper.getBasePersistableModel();
		Long partnerId = partnerGroupPermissionListViewModel.getPartnerId();
		Long partnerGroupId = partnerGroupPermissionListViewModel.getPartnerGroupId();
		
		if(partnerGroupId != null && partnerGroupId > 0 ) // update case.
		{
			PartnerGroupListViewModel partnerGroupListViewModel =this.partnerGroupListViewDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel()
					.getPrimaryKey());
					
			try
			{
				BeanUtils.copyProperties(partnerGroupPermissionListViewModel, partnerGroupListViewModel);
			}catch(Exception e)
			{
				System.out.println("Execption");
			}
		}
		
		List<UserPermissionWrapper> userPermissionList = partnerGroupPermissionListViewModel.getUserPermissionList();
		
		if(partnerGroupId != null && partnerGroupId > 0 ) // update case.
		{
			GroupPermissionViewModel groupPermissionViewModel = new GroupPermissionViewModel();
			groupPermissionViewModel.setPartnerGroupId(partnerGroupId);
			groupPermissionViewModel.setIsDefault(false);

			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
            sortingOrderMap.put("userPermissionSectionId", SortingOrder.ASC);
            sortingOrderMap.put("sequenceNo", SortingOrder.ASC);

			CustomList<GroupPermissionViewModel> list = this.groupPermissionViewDAO
			.findByExample(groupPermissionViewModel, null, sortingOrderMap);
			
			Iterator<GroupPermissionViewModel> gpItr = list.getResultsetList().iterator();
			while(gpItr.hasNext())
			{
				GroupPermissionViewModel gpModel = (GroupPermissionViewModel)gpItr.next();
				
				UserPermissionWrapper userPermissionWrapper = new UserPermissionWrapper(gpModel.getUserPermissionId(), gpModel.getName());
				userPermissionWrapper.setUserPermissionSectionId( gpModel.getUserPermissionSectionId() );
                userPermissionWrapper.setUserPermissionSectionName( gpModel.getUserPermissionSectionName() );
                userPermissionWrapper.setSequenceNo( gpModel.getSequenceNo() );

				userPermissionWrapper.setPermissionShortName(gpModel.getShortName());
				
				userPermissionWrapper.setCreateAllowed(gpModel.getCreateAllowed());
				userPermissionWrapper.setCreateAvailable(gpModel.getCreateAvailable());
				
				userPermissionWrapper.setUpdateAllowed(gpModel.getUpdateAllowed());
				userPermissionWrapper.setUpdateAvailable(gpModel.getUpdateAvailable());
				
				userPermissionWrapper.setDeleteAllowed(gpModel.getDeleteAllowed());
				userPermissionWrapper.setDeleteAvailable(gpModel.getDeleteAvailable());
				
				userPermissionWrapper.setReadAllowed(gpModel.getReadAllowed());
				userPermissionWrapper.setReadAvailable(gpModel.getReadAvailable());
				
				userPermissionList.add(userPermissionWrapper);
			}
			
		}else if(partnerId != null && partnerId > 0 ) // create case.
		{
			PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
			partnerPermissionViewModel.setPartnerId(partnerId);
			partnerPermissionViewModel.setIsDefault(false);

			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
            sortingOrderMap.put("userPermissionSectionId", SortingOrder.ASC);
            sortingOrderMap.put("sequenceNo", SortingOrder.ASC);

			CustomList<PartnerPermissionViewModel> list = this.partnerPermissionViewDAO
			.findByExample(partnerPermissionViewModel, null, sortingOrderMap);
			
			Iterator<PartnerPermissionViewModel> ppItr = list.getResultsetList().iterator();
			while(ppItr.hasNext())
			{
				PartnerPermissionViewModel ppModel = (PartnerPermissionViewModel)ppItr.next();
				
				UserPermissionWrapper userPermissionWrapper = new UserPermissionWrapper(ppModel.getUserPermissionId(), ppModel.getName());
				userPermissionWrapper.setUserPermissionSectionId( ppModel.getUserPermissionSectionId() );
				userPermissionWrapper.setUserPermissionSectionName( ppModel.getUserPermissionSectionName() );
				userPermissionWrapper.setSequenceNo( ppModel.getSequenceNo() );
				userPermissionWrapper.setPermissionShortName(ppModel.getShortName());
				userPermissionWrapper.setCreateAvailable(ppModel.getCreateAvailable());								
				userPermissionWrapper.setUpdateAvailable(ppModel.getUpdateAvailable());								
				userPermissionWrapper.setDeleteAvailable(ppModel.getDeleteAvailable());								
				userPermissionWrapper.setReadAvailable(ppModel.getReadAvailable());
				
				userPermissionList.add(userPermissionWrapper);
			}
		}
		
		baseWrapper.setBasePersistableModel(partnerGroupPermissionListViewModel);
		
		return baseWrapper;
	}

	public SearchBaseWrapper searchPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<PartnerGroupListViewModel> list = this.partnerGroupListViewDAO
		.findByExample((PartnerGroupListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	@Override
	public SearchBaseWrapper getAllPartnerGroups() throws FrameworkCheckedException {
		
		PartnerGroupModel partnerGroupModel=new PartnerGroupModel();
		partnerGroupModel.setActive(Boolean.TRUE);
		
		CustomList<PartnerGroupModel> list = this.partnerGroupDAO.findByExample(partnerGroupModel);
		
		SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}


	public BaseWrapper updatePartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		PartnerGroupModel partnerGroupModel = (PartnerGroupModel)baseWrapper.getBasePersistableModel();
		if(!partnerGroupModel.getActive())
		{
			AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
			appUserPartnerGroupModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
			
			ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
			exampleConfigHolder.setEnableLike(false);
			
			Integer appUserPartnerGroupCount = appUserPartnerGroupDAO.countByExample(appUserPartnerGroupModel,exampleConfigHolder);
			
			if(appUserPartnerGroupCount == null || appUserPartnerGroupCount.intValue() > 0)
			{
				throw new FrameworkCheckedException(this.messageSource.getMessage("partnerGroupModule.partnerGroupCantBeDeactive", null, null));
			}
		}
		
		baseWrapper.setBasePersistableModel(this.partnerGroupDAO.saveOrUpdate(partnerGroupModel));
		
		
		
		//Expire Current Sessions Associated with this group rights
				AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
				appUserPartnerGroupModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
				CustomList<AppUserPartnerGroupModel>appUserPartnerGroupModelList = appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
				if(null!=appUserPartnerGroupModelList&&null!=appUserPartnerGroupModelList.getResultsetList() &&appUserPartnerGroupModelList.getResultsetList().size()>0)
				{
					Set<AppUserPartnerGroupModel>appUserPartnerGroupModelSet = new HashSet<>();
					appUserPartnerGroupModelSet.addAll(appUserPartnerGroupModelList.getResultsetList());
					List<AppUserPartnerGroupModel> list = new ArrayList<>();
					list.addAll(appUserPartnerGroupModelSet);
					
					for (AppUserPartnerGroupModel model : list) {
						
						Object principal = model.getAppUserIdAppUserModel(); 
						
				        String name = model.getAppUserIdAppUserModel().getUsername();
					
				        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
				   for (SessionInformation si : sessions) {
					AppUserModel pr = (AppUserModel) si.getPrincipal();

						if (!si.isExpired() &&pr.getUsername().equalsIgnoreCase(name)) {
							si.expireNow(); 
				            }
				        }
				
					}
				}		
				//End Expire Current Sessions Associated with this group rights  
				
		
		actionLogModel.setCustomField1(partnerGroupModel.getPartnerGroupId().toString());
		actionLogModel.setCustomField11(partnerGroupModel.getName());
		this.actionLogManager.completeActionLog(actionLogModel);
		
		return baseWrapper;
	}
	
	public List<PartnerGroupModel> getPartnerGroups(PartnerGroupModel partnerGroupModel,Boolean admin)throws FrameworkCheckedException
	{
		List<PartnerGroupModel> partnerGroupModelListView = null;
			List list = this.partnerGroupDAO.getPartnerGroups(partnerGroupModel.getPartnerId(),admin);
			if(list!=null && list.size()>0)
			{
			    partnerGroupModelListView = new ArrayList<PartnerGroupModel>();

				PartnerGroupModel partnerGroupModelforList = null;
				for (int count = 0; count < list.size(); count++) {
					partnerGroupModelforList = new PartnerGroupModel();

					Object obj[] = (Object[]) list.get(count);
					partnerGroupModelforList.setPrimaryKey((Long) obj[0]);
					partnerGroupModelforList.setName((String) obj[1]);

					partnerGroupModelListView.add(partnerGroupModelforList);

				}
			}
			return partnerGroupModelListView;
	}

	public BaseWrapper activateDeactivatePartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.USER_GROUP_ACT_DEACT_USECASE_ID);
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		PartnerGroupModel partnerGroupModel  =(PartnerGroupModel)baseWrapper.getBasePersistableModel();
		baseWrapper=this.loadPartnerGroup(baseWrapper);
		partnerGroupModel  =(PartnerGroupModel)baseWrapper.getBasePersistableModel();
		if(partnerGroupModel.getActive())
		{
			AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
			appUserPartnerGroupModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
			
			ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
			exampleConfigHolder.setEnableLike(false);
			
			Integer appUserPartnerGroupCount = appUserPartnerGroupDAO.countByExample(appUserPartnerGroupModel,exampleConfigHolder);
			
			if(appUserPartnerGroupCount == null || appUserPartnerGroupCount.intValue() > 0)
			{
				throw new FrameworkCheckedException(this.messageSource.getMessage("partnerGroupModule.partnerGroupCantBeDeactive", null, null));
			}
			
			partnerGroupModel.setActive(false);
			baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.FALSE);			
			
		}
		else
		{
			partnerGroupModel.setActive(true);
			baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.TRUE);
			
		}
		this.partnerGroupDAO.saveOrUpdate(partnerGroupModel);
		
		actionLogModel.setCustomField1(partnerGroupModel.getPartnerGroupId().toString());
		actionLogModel.setCustomField11(partnerGroupModel.getName());
		this.actionLogManager.completeActionLog(actionLogModel);
		
		return baseWrapper;
	}


	public PartnerGroupListViewDAO getPartnerGroupListViewDAO() {
		return partnerGroupListViewDAO;
	}

	public void setPartnerGroupListViewDAO(
			PartnerGroupListViewDAO partnerGroupListViewDAO) {
		this.partnerGroupListViewDAO = partnerGroupListViewDAO;
	}

	public PartnerGroupDAO getPartnerGroupDAO() {
		return partnerGroupDAO;
	}

	public void setPartnerGroupDAO(PartnerGroupDAO partnerGroupDAO) {
		this.partnerGroupDAO = partnerGroupDAO;
	}

	public GroupPermissionViewDAO getGroupPermissionViewDAO() {
		return groupPermissionViewDAO;
	}

	public void setGroupPermissionViewDAO(
			GroupPermissionViewDAO groupPermissionViewDAO) {
		this.groupPermissionViewDAO = groupPermissionViewDAO;
	}

	public PartnerPermissionViewDAO getPartnerPermissionViewDAO() {
		return partnerPermissionViewDAO;
	}

	public void setPartnerPermissionViewDAO(
			PartnerPermissionViewDAO partnerPermissionViewDAO) {
		this.partnerPermissionViewDAO = partnerPermissionViewDAO;
	}

	public List<UserPermissionWrapper> loadPartnerPermission(Long partnerId)
			throws FrameworkCheckedException {
		
		PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
		partnerPermissionViewModel.setPartnerId(partnerId);
		partnerPermissionViewModel.setIsDefault(false);

		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("userPermissionSectionId", SortingOrder.ASC);
        sortingOrderMap.put("sequenceNo", SortingOrder.ASC);

		CustomList<PartnerPermissionViewModel> list = this.partnerPermissionViewDAO
		.findByExample(partnerPermissionViewModel, null, sortingOrderMap);
		
		List<UserPermissionWrapper> userPermissionList = new ArrayList<UserPermissionWrapper>();
		Iterator<PartnerPermissionViewModel> ppItr = list.getResultsetList().iterator();
		
		while(ppItr.hasNext())
		{
			PartnerPermissionViewModel ppModel = (PartnerPermissionViewModel)ppItr.next();
			
			UserPermissionWrapper userPermissionWrapper = new UserPermissionWrapper(ppModel.getUserPermissionId(), ppModel.getName());
			userPermissionWrapper.setUserPermissionSectionId( ppModel.getUserPermissionSectionId() );
			userPermissionWrapper.setUserPermissionSectionName( ppModel.getUserPermissionSectionName() );
			userPermissionWrapper.setSequenceNo( ppModel.getSequenceNo() );
			userPermissionWrapper.setPermissionShortName(ppModel.getShortName());
			userPermissionWrapper.setCreateAvailable(ppModel.getCreateAvailable());								
			userPermissionWrapper.setUpdateAvailable(ppModel.getUpdateAvailable());								
			userPermissionWrapper.setDeleteAvailable(ppModel.getDeleteAvailable());								
			userPermissionWrapper.setReadAvailable(ppModel.getReadAvailable());
			
			userPermissionList.add(userPermissionWrapper);
		}
		return userPermissionList;
	}

	public void setAppUserPartnerViewDAO(AppUserPartnerViewDAO appUserPartnerViewDAO) {
		this.appUserPartnerViewDAO = appUserPartnerViewDAO;
	}

	public BaseWrapper loadAppUserPartner(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AppUserPartnerViewModel appUserPartnerViewModel = (AppUserPartnerViewModel)baseWrapper.getBasePersistableModel();
		CustomList<AppUserPartnerViewModel> list = this.appUserPartnerViewDAO
		.findByExample(appUserPartnerViewModel);
		baseWrapper.setBasePersistableModel(list.getResultsetList().get(0));
		return baseWrapper;
	}

	public SearchBaseWrapper searchDefaultPartnerPermission(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		PartnerPermissionViewModel partnerPermissionViewModel = (PartnerPermissionViewModel)searchBaseWrapper.getBasePersistableModel();		
		CustomList<PartnerPermissionViewModel> list = this.partnerPermissionViewDAO
		.findByExample(partnerPermissionViewModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	@Override
	public void validatePartnerGroup(PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel) throws FrameworkCheckedException{
		
		if (null != partnerGroupPermissionListViewModel.getName() 
				&& !this.isPartnerGroupNameUnique(partnerGroupPermissionListViewModel.getName(),partnerGroupPermissionListViewModel.getPartnerGroupId())){
			
			throw new FrameworkCheckedException("DataIntegrityViolationException");
		}
	}

	private boolean isPartnerGroupNameUnique(String name, Long partnerGroupId){
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		partnerGroupModel.setName(name);
		partnerGroupModel.setPartnerGroupId(partnerGroupId);
		return this.partnerGroupDAO.isNameUnique(partnerGroupModel);	
	}

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}	
	
	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

}
