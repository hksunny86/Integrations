package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.tax.model.WHTConfigModel;

import java.util.List;

public interface FilerRateConfigDAO extends BaseDAO<FilerRateConfigModel, Long> {
    public FilerRateConfigModel loadFilerRateConfigModelByFiler(Long filer);
    public List<FilerRateConfigModel> loadFilerRateConfigModelByNonFiler();

}
