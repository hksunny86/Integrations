package com.inov8.microbank.server.service.applicationversionmodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.server.dao.applicationversionmodule.ApplicationVersionDAO;


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



public class ApplicationVersionManagerImpl implements ApplicationVersionManager
{

	private ApplicationVersionDAO applicationVersionDAO;
	
	public ApplicationVersionManagerImpl()
	{
	}

	public SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper)
	{
		AppVersionModel appVersionModel = this.applicationVersionDAO.findByPrimaryKey(searchBaseWrapper.
				getBasePersistableModel().
				getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(appVersionModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper)
	{
		AppVersionModel appVersionModel = this.applicationVersionDAO.findByPrimaryKey(baseWrapper.
				getBasePersistableModel().
				getPrimaryKey());
		baseWrapper.setBasePersistableModel(appVersionModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper)
	{
		CustomList<AppVersionModel>
		list = this.applicationVersionDAO.findByExample((AppVersionModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper)
	{
		AppVersionModel appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
		AppVersionModel newAppVersionModel = new AppVersionModel();
		newAppVersionModel.setAppVersionId(appVersionModel.getAppVersionId());
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int recordCount = this.applicationVersionDAO.countByExample(newAppVersionModel,exampleHolder);

		if (recordCount != 0 && appVersionModel.getPrimaryKey() != null)
		{
			appVersionModel = this.applicationVersionDAO.saveOrUpdate( (AppVersionModel) baseWrapper.
					getBasePersistableModel());
			baseWrapper.setBasePersistableModel(appVersionModel);
			return baseWrapper;
		}
		else
		{
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;
		}
	}

	public BaseWrapper createApplicationVersion(BaseWrapper baseWrapper)
	{
		int recordCount;
		Date nowDate = new Date();
		AppVersionModel newAppVersionModel = new AppVersionModel();
		AppVersionModel appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
		newAppVersionModel.setAppVersionNumber(appVersionModel.getAppVersionNumber());
		newAppVersionModel.setDeviceTypeId(appVersionModel.getDeviceTypeId());
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		recordCount = applicationVersionDAO.countByExample(newAppVersionModel,exampleHolder);

		//***Check if Record already exists

		appVersionModel.setCreatedOn(nowDate);
		appVersionModel.setUpdatedOn(nowDate);

		if (recordCount == 0)
		{
			baseWrapper.setBasePersistableModel(this.applicationVersionDAO.saveOrUpdate(appVersionModel));
			return baseWrapper;
		}
		else
		{
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;
		}
	}

	public void setApplicationVersionDAO(ApplicationVersionDAO applicationVersionDAO)
	{
		this.applicationVersionDAO = applicationVersionDAO;
	}

}
