package com.inov8.microbank.server.service.portal.supplierescalationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.supplierescalationmodule.EscToInov8ProductListViewModel;
import com.inov8.microbank.common.model.portal.supplierescalationmodule.EscToInov8ServiceListViewModel;
import com.inov8.microbank.server.dao.portal.supplierescalationmodule.EscToInov8ProductListViewDAO;
import com.inov8.microbank.server.dao.portal.supplierescalationmodule.EscToInov8ServiceListViewDAO;

public class SupplierEscalationManagerImpl implements SupplierEscalationManager{

	private EscToInov8ProductListViewDAO escToInov8ProductListViewDAO;
	private EscToInov8ServiceListViewDAO escToInov8ServiceListViewDAO;
	
	public void setEscToInov8ProductListViewDAO(
			EscToInov8ProductListViewDAO escToInov8ProductListViewDAO) {
		this.escToInov8ProductListViewDAO = escToInov8ProductListViewDAO;
	}
	
	public void setEscToInov8ServiceListViewDAO(
			EscToInov8ServiceListViewDAO escToInov8ServiceListViewDAO) {
		this.escToInov8ServiceListViewDAO = escToInov8ServiceListViewDAO;
	}

    
	/**
	 * Search Escalate to Inov8, Product
	 */
	public SearchBaseWrapper searchEscalateToInov8Product(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<EscToInov8ProductListViewModel>
        list = this.escToInov8ProductListViewDAO.findByExample( (EscToInov8ProductListViewModel)
                         searchBaseWrapper.getBasePersistableModel(),
                         searchBaseWrapper.getPagingHelperModel(),
                         searchBaseWrapper.getSortingOrderMap(),
                         searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	}

	/**
	 * Search Escalate to Inov8, Service
	 */
	public SearchBaseWrapper searchEscalateToInov8Service(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<EscToInov8ServiceListViewModel>
        list = this.escToInov8ServiceListViewDAO.findByExample( (EscToInov8ServiceListViewModel)
                         searchBaseWrapper.getBasePersistableModel(),
                         searchBaseWrapper.getPagingHelperModel(),
                         searchBaseWrapper.getSortingOrderMap(),
                         searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	}
	
}
