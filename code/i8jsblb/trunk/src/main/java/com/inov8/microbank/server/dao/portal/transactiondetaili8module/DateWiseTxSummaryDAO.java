package com.inov8.microbank.server.dao.portal.transactiondetaili8module;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.DateWiseTxSummaryModel;

public interface DateWiseTxSummaryDAO
extends BaseDAO<DateWiseTxSummaryModel, Long> {
    List<DateWiseTxSummaryModel> loadDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws DataAccessException;
}
