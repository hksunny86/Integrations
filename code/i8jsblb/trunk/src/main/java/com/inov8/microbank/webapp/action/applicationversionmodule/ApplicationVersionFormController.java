package com.inov8.microbank.webapp.action.applicationversionmodule;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.applicationversionmodule.ApplicationVersionManager;

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


public class ApplicationVersionFormController
extends AdvanceFormController
{
	 private ReferenceDataManager referenceDataManager;
	private ApplicationVersionManager applicationVersionManager;
	
	private Long id;

	public ApplicationVersionFormController()
	{
		setCommandName("appVersionModel");
		setCommandClass(AppVersionModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
	{
		Map referenceDataMap = new HashMap();
		DeviceTypeModel deviceTypeModel = new DeviceTypeModel ();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				deviceTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(deviceTypeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(referenceDataWrapper);
	    }
	    catch (Exception e)
	    {

	    }
	    java.util.List<DeviceTypeModel> deviceTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	deviceTypeModelList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);
	    return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "appVersionId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			AppVersionModel appVersionModel = new AppVersionModel();
			appVersionModel.setAppVersionId(id);
			searchBaseWrapper.setBasePersistableModel(appVersionModel);
			searchBaseWrapper = this.applicationVersionManager.loadApplicationVersion(searchBaseWrapper);
			return (AppVersionModel) searchBaseWrapper.getBasePersistableModel();
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}
			return new AppVersionModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Object object, BindException errors) throws	Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			AppVersionModel appVersionModel = (AppVersionModel) object;
			appVersionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			appVersionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			appVersionModel.setUpdatedOn(new Date());
			appVersionModel.setCreatedOn(new Date());
			appVersionModel.setActive( appVersionModel.getActive() == null ? false : appVersionModel.getActive() ) ;
			appVersionModel.setBlackListed( appVersionModel.getBlackListed() == null ? false : appVersionModel.getBlackListed() ) ; 
			baseWrapper.setBasePersistableModel(appVersionModel);
			baseWrapper = this.applicationVersionManager.createApplicationVersion(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				this.saveMessage(httpServletRequest,"Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			}
			else
			{
				this.saveMessage(httpServletRequest, "Application Version already exists");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest,"Record could not be saved");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			throw ex;
		}
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Object object, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			Date nowDate = new Date();
			AppVersionModel appVersionModel = (AppVersionModel) object;
			
			 AppVersionModel tempAppVersionModel = new AppVersionModel();
			tempAppVersionModel.setAppVersionId(id);
			
			baseWrapper.setBasePersistableModel(tempAppVersionModel);
			baseWrapper = this.applicationVersionManager.loadApplicationVersion(baseWrapper);
			tempAppVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel(); 
			
			appVersionModel.setCreatedOn(tempAppVersionModel.getCreatedOn());
			appVersionModel.setCreatedBy(tempAppVersionModel.getCreatedBy());
			appVersionModel.setUpdatedOn(nowDate);
			appVersionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			appVersionModel.setActive( appVersionModel.getActive() == null ? false : appVersionModel.getActive() ) ;
			appVersionModel.setBlackListed( appVersionModel.getBlackListed() == null ? false : appVersionModel.getBlackListed() ) ;
			baseWrapper.setBasePersistableModel(appVersionModel);
			baseWrapper = this.applicationVersionManager.updateApplicationVersion(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				this.saveMessage(httpServletRequest,"Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			}
			else
			{
				this.saveMessage(httpServletRequest,"Record could not be updated");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest,"Record could not be updated");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			throw ex;
		}
	}

	public void setApplicationVersionManager(ApplicationVersionManager applicationVersionManager)
	{
		this.applicationVersionManager = applicationVersionManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
