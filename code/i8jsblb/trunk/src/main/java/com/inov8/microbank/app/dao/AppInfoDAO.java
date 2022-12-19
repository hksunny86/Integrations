package com.inov8.microbank.app.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.app.vo.AppInfoVO;

import java.util.List;

public interface AppInfoDAO extends BaseDAO<AppInfoModel, Long>
{
    List<AppInfoVO> loadAppInfoList(Long appUserTypeId);
}
