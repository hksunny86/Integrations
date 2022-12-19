package com.inov8.microbank.disbursement.dao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsXmlVo;
import com.inov8.microbank.disbursement.vo.DisbursementVO;

public interface BulkDisbursementDAO extends BaseDAO<BulkDisbursementsModel, Long> {

	List<DisbursementVO> findDueDisbursement(Long serviceId, Date end, Boolean isCoreSumAccountNumber, Boolean posted, Boolean settled);
	List<DisbursementVO> findDueDisbursementForT24(Long serviceId, Date end, Boolean isCoreSumAccountNumber, Boolean posted, Boolean settled);
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException;
	List<BulkDisbursementsModel> findBulkDisbursement(Long productId, Integer type, Date end, Boolean posted, Boolean settled);
	Double findTotalDueDisbursement(Long productId, Integer value, Date end, Boolean posted, Boolean settled);

	void update(Long disbursementId, String txCode);

	void update(BulkDisbursementsModel model);
	public void saveOrUpdateList(List<BulkDisbursementsModel> bulkDisbursementsModelList);
	public void createOrUpdateBulkDisbursements(CopyOnWriteArrayList<String[]> recordList) throws FrameworkCheckedException;
	public List<BulkDisbursementsXmlVo> getBulkDisbursementsXmlVoByBatchNo(String batchNo);
	public Boolean updatePostedRecords(String batchNumber);
	public Boolean updatePostedRecordsForT24(String batchNumber);

	List<Object[]> loadDisbursementsForSMS(Long disbursementFileInfoId);
}
