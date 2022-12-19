package com.inov8.export.dao;

import com.inov8.framework.common.model.ExportInfoModel;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Created by NaseerUl on 9/6/2016.
 */
public interface OracleJdbcDao
{
    SqlRowSet queryForRowSet(ExportInfoModel exportInfoModel) throws Exception;

    SqlRowSet queryForTotalsRowSet(ExportInfoModel exportInfoModel);
}
