package com.inov8.microbank.server.service.common;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.areamodule.AreaListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.common.AreaDAO;
import com.inov8.microbank.server.dao.common.AreaListViewDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/**
 * 
 * @author asad.hayyat
 *
 */


public class AreaManagerImpl implements AreaManager {

	private AreaDAO areaDAO;

	private AreaListViewDAO areaListViewDAO;

	private DistributorContactDAO distributorContactDAO;

	private ActionLogManager actionLogManager;

	public SearchBaseWrapper loadArea(SearchBaseWrapper searchBaseWrapper) {
		AreaModel areaModel = this.areaDAO.findByPrimaryKey(searchBaseWrapper
				.getBasePersistableModel().getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(areaModel);
		return searchBaseWrapper;
	}


public BaseWrapper loadArea(BaseWrapper baseWrapper) {
		AreaModel areaModel = this.areaDAO.findByPrimaryKey(baseWrapper
				.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(areaModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchArea(SearchBaseWrapper searchBaseWrapper) {
		CustomList<AreaListViewModel> list = this.areaListViewDAO
				.findByExample((AreaListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper createOrUpdateArea(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		String actionLogHandler = (String)baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);
		ActionLogModel actionLogModel = new ActionLogModel();
		
		AreaModel newAreaModel = new AreaModel();
		AreaModel areaModel = (AreaModel) baseWrapper.getBasePersistableModel();
		newAreaModel.setName(areaModel.getName());
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int recordCount = areaDAO.countByExample(newAreaModel,exampleHolder);
		// ***Check if name already exists
		if (recordCount == 0 || areaModel.getAreaId() != null) {
			if(actionLogHandler == null)
			{
				if(areaModel.getAreaId() == null){
					actionLogModel.setActionId(PortalConstants.ACTION_CREATE);
				}else{
					actionLogModel.setActionId(PortalConstants.ACTION_UPDATE);
				}
				actionLogModel.setUsecaseId(new Long(PortalConstants.AREA_USECASE_ID));
				actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
				actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
				actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
				actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
				actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
				actionLogModel = logAction(actionLogModel);
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}

			areaModel = this.areaDAO.saveOrUpdate((AreaModel) baseWrapper
					.getBasePersistableModel());

			if(actionLogHandler == null)
			{
				actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
				actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
				actionLogModel.setCustomField1(String.valueOf(areaModel.getAreaId()));
				actionLogModel.setCustomField11(areaModel.getRegionalHierarchyModel().getRegionalHierarchyName());
				actionLogModel = logAction(actionLogModel);
			}
			// If Level One Area found
			/*if (areaModel.getParentAreaId() == null
					&& areaModel.getUltimateParentAreaId() == null) {
				areaModel.setParentAreaId(areaModel.getAreaId());
				areaModel.setUltimateParentAreaId(areaModel.getAreaId());
				areaModel = this.areaDAO.saveOrUpdate(areaModel);
			}*/

			baseWrapper.setBasePersistableModel(areaModel);
			return baseWrapper;

		} else {
			// set baseWrapper to null if record exists
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;
		}
	}

	public boolean findDistributorContactByAreaId(BaseWrapper baseWrapper) {
		AreaModel areaModel = (AreaModel) baseWrapper.getBasePersistableModel();
		DistributorContactModel dcm = new DistributorContactModel();
		dcm.setAreaId(areaModel.getAreaId());
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int recordCount = distributorContactDAO.countByExample(dcm,exampleHolder);

		if (recordCount > 0)
			return true;
		else
			return false;
	}

	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;

	}

	public void setAreaListViewDAO(AreaListViewDAO areaListViewDAO) {
		this.areaListViewDAO = areaListViewDAO;
	}

	public void setDistributorContactDAO(
			DistributorContactDAO distributorContactDAO) {
		this.distributorContactDAO = distributorContactDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	private ActionLogModel logAction( ActionLogModel actionLogModel ) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if( null == actionLogModel.getActionLogId() ){
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		}else{
			baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel)baseWrapper.getBasePersistableModel();
	}
	
}
