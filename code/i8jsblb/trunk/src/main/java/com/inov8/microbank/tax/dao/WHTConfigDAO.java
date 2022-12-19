package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.WHTConfigModel;

import java.util.List;

public interface WHTConfigDAO extends BaseDAO<WHTConfigModel, Long> {
    public List<WHTConfigModel> loadAllActiveWHTConfigModels();

}
