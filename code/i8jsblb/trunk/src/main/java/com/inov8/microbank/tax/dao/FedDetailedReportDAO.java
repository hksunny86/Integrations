package com.inov8.microbank.tax.dao;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by Attique on 8/18/2017.
 */
public interface FedDetailedReportDAO extends BaseDAO<FedDetailedReportModel, Long> {
    public List<FedDetailedReportModel> getFilteredData(SearchBaseWrapper baseWrapper) throws DataAccessException;

}
