package com.inov8.microbank.server.service.portal.productcustomerdispute;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.portal.productcustomerdisputemodule.CustDispTranVrDcListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.server.dao.mnomodule.MnoSupplierListViewDAO;
import com.inov8.microbank.server.dao.portal.productcustomerdispute.CustDisputeTransVrDcListViewDAO;
import com.inov8.microbank.server.dao.portal.sales.SalesDAO;


public class CustomerDisputeProductManagerImpl implements CustomerDisputeProductManager 
{
	private CustDisputeTransVrDcListViewDAO custDisputeTransVrDcListViewDAO;
	private MnoSupplierListViewDAO mnoSupplierListViewDAO;
	private SalesDAO salesDAO;
	
	public SearchBaseWrapper searchCustomerDisputeTransactionForVariableDiscrete(SearchBaseWrapper searchBaseWrapper)
	{
		CustomList<CustDispTranVrDcListViewModel>
		list = this.custDisputeTransVrDcListViewDAO.findByExample( (CustDispTranVrDcListViewModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	public BaseWrapper createIssue(BaseWrapper baseWrapper) throws
	FrameworkCheckedException
	{
		IssueModel issueModel = (IssueModel) baseWrapper.getBasePersistableModel();
		issueModel = this.salesDAO.saveOrUpdate( (
				IssueModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(issueModel);
		return baseWrapper;

	}
	
	public BaseWrapper searchMNOSupplier(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		MnoSupplierListViewModel mnoSupplierListViewModel = (MnoSupplierListViewModel)this.mnoSupplierListViewDAO.findByPrimaryKey( (
				baseWrapper.getBasePersistableModel()).getPrimaryKey());
		baseWrapper.setBasePersistableModel(mnoSupplierListViewModel);
		return baseWrapper;
	}

	public void setMnoSupplierListViewDAO(MnoSupplierListViewDAO mnoSupplierListViewDAO)
	{
		this.mnoSupplierListViewDAO = mnoSupplierListViewDAO;
	}

	public void setSalesDAO(SalesDAO salesDAO)
	{
		this.salesDAO = salesDAO;
	}

	public void setCustDisputeTransVrDcListViewDAO(CustDisputeTransVrDcListViewDAO custDisputeTransVrDcListViewDAO)
	{
		this.custDisputeTransVrDcListViewDAO = custDisputeTransVrDcListViewDAO;
	}
}
