package com.inov8.microbank.demographics.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.demographics.model.DemographicsModel;

import java.util.List;

public interface DemographicsDAO extends BaseDAO<DemographicsModel, Long> {
    DemographicsModel loadDemographicsModel(String mobileNo);

    public DemographicsModel loadDemographicsModel(long appUserId);

    public List<DemographicsModel> loadDistinctOS() throws FrameworkCheckedException;

    public List<String> loadDistinctOSVersion(String os) throws FrameworkCheckedException;

    public List<String> loadDistinctVendor(String os) throws FrameworkCheckedException;

    public DemographicsModel loadDeviceKeyByAppUserId(Long appUserId) throws FrameworkCheckedException;
}
