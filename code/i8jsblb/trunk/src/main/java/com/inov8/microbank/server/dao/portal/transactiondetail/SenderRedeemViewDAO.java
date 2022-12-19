package com.inov8.microbank.server.dao.portal.transactiondetail;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;

public interface SenderRedeemViewDAO extends BaseDAO<SenderRedeemViewModel, Long>{
	List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException;
}
