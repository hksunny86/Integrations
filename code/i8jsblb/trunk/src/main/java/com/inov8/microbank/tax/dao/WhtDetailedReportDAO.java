package com.inov8.microbank.tax.dao;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by Attique on 8/22/2017.
 */
public interface WhtDetailedReportDAO extends BaseDAO<WhtDetailedReportModel, Long> {
    public List<WhtDetailedReportModel> getFilteredWhtData(SearchBaseWrapper baseWrapper) throws DataAccessException;
}
