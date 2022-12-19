package com.inov8.microbank.server.dao.portal.transactiondetail;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.CashTransactionViewModel;

public interface CashTransactionViewDAO extends BaseDAO<CashTransactionViewModel, Long>{
	List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException;
}
