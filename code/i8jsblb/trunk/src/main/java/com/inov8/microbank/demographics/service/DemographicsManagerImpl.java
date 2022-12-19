package com.inov8.microbank.demographics.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.demographics.dao.DemographicsDAO;
import com.inov8.microbank.demographics.model.DemographicsModel;

public class DemographicsManagerImpl implements DemographicsManager
{
	protected final Log logger = LogFactory.getLog(DemographicsManagerImpl.class);
	private DemographicsDAO demographicsDAO;

	@Override
	public DemographicsModel saveOrUpdate(DemographicsModel DemographicsModel) {
		return demographicsDAO.saveOrUpdate(DemographicsModel);
	}

	@Override
	public DemographicsModel unlockDevice(String mobileNo, String udid) throws FrameworkCheckedException {
		DemographicsModel demographicsModel = demographicsDAO.loadDemographicsModel(mobileNo);
		demographicsModel.setUdid(udid);
		demographicsModel.setUpdatedOn(new Date());
		demographicsModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		demographicsModel.setLocked(false);

		demographicsDAO.saveOrUpdate(demographicsModel);

		return demographicsModel;
	}

	public DemographicsModel loadDemographics(Long appUserId) throws FrameworkCheckedException {
		DemographicsModel demographicsModel = this.demographicsDAO.loadDemographicsModel(appUserId);

		return demographicsModel != null ? (DemographicsModel)demographicsModel.clone() : null;
	}

	public void setDemographicsDAO(DemographicsDAO demographicsDAO) {
		this.demographicsDAO = demographicsDAO;
	}
}
