package com.inov8.microbank.server.dao.mfsmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.app.vo.AppVO;
import com.inov8.microbank.common.model.AppVersionModel;

public interface AppVersionDAO extends BaseDAO <AppVersionModel, Long> 
{
    public AppVO loadLatestAppVersion(AppVO appVO);

    }
