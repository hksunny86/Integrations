package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.creditmodule.CreditInfoListViewModel;
import com.inov8.microbank.server.dao.creditmodule.CreditInfoListViewDAO;

public class CreditInfoManagerImpl implements CreditInfoManager
{
	
	private CreditInfoListViewDAO creditInfoDAO;

	

	public SearchBaseWrapper searchDistributorOrRetailer(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		
		CreditInfoListViewModel creditInfoModel = (CreditInfoListViewModel)searchBaseWrapper.getBasePersistableModel();
		CustomList<CreditInfoListViewModel> list =  creditInfoDAO.findByExample(creditInfoModel,searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}



	public void setCreditInfoDAO(CreditInfoListViewDAO creditInfoDAO)
	{
		this.creditInfoDAO = creditInfoDAO;
	}

}
