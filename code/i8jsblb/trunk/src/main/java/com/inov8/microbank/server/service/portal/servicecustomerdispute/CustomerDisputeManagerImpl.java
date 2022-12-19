package com.inov8.microbank.server.service.portal.servicecustomerdispute;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.CustDispTranBlPmListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoSupplierListViewDAO;
import com.inov8.microbank.server.dao.portal.sales.SalesDAO;
import com.inov8.microbank.server.dao.portal.servicecustomerdispute.CustDisputeTransListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;



public class CustomerDisputeManagerImpl implements CustomerDisputeManager
{
	private CustDisputeTransListViewDAO custDisputeTransListViewDAO;
	private MnoSupplierListViewDAO mnoSupplierListViewDAO;
	private SalesDAO salesDAO;
	private IssueManager issueManager;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private AppUserDAO appUserDAO;
	
	
	public SearchBaseWrapper searchCustomerDisputeTransactionForBillPayment(SearchBaseWrapper searchBaseWrapper)
	{
		CustomList<CustDispTranBlPmListViewModel>
		list = this.custDisputeTransListViewDAO.findByExample( (CustDispTranBlPmListViewModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
				searchBaseWrapper.setCustomList(list);
		
		return searchBaseWrapper;
	}

	public BaseWrapper createIssue(BaseWrapper baseWrapper) throws
	FrameworkCheckedException
	{
		IssueModel issueModel = (IssueModel) baseWrapper.getBasePersistableModel();
		issueModel.setIssueCode(issueManager.getUniqueIssueCode());
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

	
    public BaseWrapper isValidMfsId(BaseWrapper baseWrapper) throws
    FrameworkCheckedException{
    	   	
    	
    	String issueMfsId = (String)baseWrapper.getObject("issueMfsId");
    	
 	   ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
	   exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
    	
       UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
       userDeviceAccountsModel.setUserId(issueMfsId);
       userDeviceAccountsModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );
    	
    	CustomList <UserDeviceAccountsModel>cList = userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, exampleConfigHolderModel);
    	List <UserDeviceAccountsModel>list = cList.getResultsetList();
    	
    	if(list.isEmpty()){
    		baseWrapper.putObject("errorCode", "1"); //not found
    		return baseWrapper; 
    	}
    	
    	AppUserModel appUserModel = appUserDAO.findByPrimaryKey(list.get(0).getAppUserId());
    	
    	Long appUserTypeId  = appUserModel.getAppUserTypeId();
    	
    	if(!(appUserTypeId.longValue() == UserTypeConstantsInterface.CUSTOMER|| 
    			appUserTypeId.longValue() == UserTypeConstantsInterface.RETAILER)		
    	){
    		baseWrapper.putObject("errorCode", "2"); //not A VALID MWallet ID ... MWallet id must be of customer or retailer
    		return baseWrapper; 
    	}
    	
    	return baseWrapper;
    }
	
	
	public void setMnoSupplierListViewDAO(MnoSupplierListViewDAO mnoSupplierListViewDAO)
	{
		this.mnoSupplierListViewDAO = mnoSupplierListViewDAO;
	}

	public void setCustDisputeTransListViewDAO(CustDisputeTransListViewDAO custDisputeTransListViewDAO)
	{
		this.custDisputeTransListViewDAO = custDisputeTransListViewDAO;
	}
	
	public void setSalesDAO(SalesDAO salesDAO)
	{
		this.salesDAO = salesDAO;
	}

	public void setIssueManager(IssueManager issueManager)
	{
		this.issueManager = issueManager;
	}

	/**
	 * @param userDeviceAccountsDAO the userDeviceAccountsDAO to set
	 */
	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	/**
	 * @param appUserDAO the appUserDAO to set
	 */
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

}
