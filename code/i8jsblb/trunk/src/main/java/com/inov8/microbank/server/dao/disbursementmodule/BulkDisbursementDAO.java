package com.inov8.microbank.server.dao.disbursementmodule;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.LabelValueBean;

public interface BulkDisbursementDAO extends BaseDAO<BulkDisbursementsModel, Long> {

	List<BulkDisbursementsModel> findDueDisbursement(Long productId, Integer[] types, Date end, Boolean posted, Boolean settled);
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException;
	List<BulkDisbursementsModel> findBulkDisbursement(Long productId, Integer type, Date end, Boolean posted, Boolean settled);
	Double findTotalDueDisbursement(Long productId, Integer value, Date end, Boolean posted, Boolean settled);
	void update(BulkDisbursementsModel model);
	public void saveOrUpdateList(List<BulkDisbursementsModel> bulkDisbursementsModelList);
}
