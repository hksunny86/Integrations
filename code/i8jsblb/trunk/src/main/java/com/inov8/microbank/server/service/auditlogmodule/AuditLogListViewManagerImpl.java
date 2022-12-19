package com.inov8.microbank.server.service.auditlogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.auditlogmodule.AuditLogListViewModel;
import com.inov8.microbank.server.dao.auditlogmodule.AuditLogListViewDAO;
import com.inov8.microbank.server.dao.failurelogmodule.AuditLogDAO;

public class AuditLogListViewManagerImpl implements AuditLogListViewManager {

	private AuditLogListViewDAO auditLogListViewDAO;
	private AuditLogDAO auditLogDAO;
	
	public BaseWrapper loadAuditLog(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		AuditLogModel auditLogModel = (AuditLogModel)baseWrapper.getBasePersistableModel();
		auditLogModel = this.auditLogDAO.findByPrimaryKey(auditLogModel.getAuditLogId());
		baseWrapper.setBasePersistableModel(auditLogModel);
		return baseWrapper;
	}
	
	public SearchBaseWrapper searchAuditLogListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<AuditLogListViewModel> list = this.auditLogListViewDAO
		.findByExample((AuditLogListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
		
		
		return searchBaseWrapper;
	}

	public void setAuditLogListViewDAO(AuditLogListViewDAO auditLogListViewDAO) {
		this.auditLogListViewDAO = auditLogListViewDAO;
	}

	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}

}
