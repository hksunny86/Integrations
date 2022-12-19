package com.inov8.microbank.demographics.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.demographics.model.DemographicsModel;

public interface DemographicsManager {
	DemographicsModel unlockDevice(String mobileNo, String udid) throws FrameworkCheckedException;

	DemographicsModel loadDemographics(Long appUserId) throws FrameworkCheckedException;
	DemographicsModel saveOrUpdate(DemographicsModel DemographicsModel) throws FrameworkCheckedException;
}
