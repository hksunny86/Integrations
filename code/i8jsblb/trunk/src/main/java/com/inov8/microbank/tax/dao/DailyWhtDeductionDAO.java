package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;

import java.util.Date;
import java.util.List;


public interface DailyWhtDeductionDAO extends BaseDAO<DailyWhtDeductionModel, Long>
{
    List<DailyWhtDeductionModel> loadUnsettledWhtDeductionList(Date toDate) throws Exception;
}
