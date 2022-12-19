package com.inov8.microbank.server.service.customermodule;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.common.model.customermodule.CustTransListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.customermodule.CustTransListViewDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerRemitterDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class CustTransManagerImpl
    implements CustTransManager
{
  private CustTransListViewDAO custTransListViewDAO;
  private CustomerDAO customerDAO;
  private AppUserDAO appUserDAO;
  private CustomerRemitterDAO customerRemitterDAO;

  public SearchBaseWrapper loadCustomer(SearchBaseWrapper searchBaseWrapper)
  {
    CustomerModel customerModel = (CustomerModel)this.customerDAO.findByPrimaryKey( (
        searchBaseWrapper.getBasePersistableModel()).getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(customerModel);
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchCustomer(SearchBaseWrapper searchBaseWrapper)
  {
	  CustomList<CustomerModel>
      list = this.customerDAO.findByExample( (CustomerModel)
      searchBaseWrapper.getBasePersistableModel(),
      searchBaseWrapper.getPagingHelperModel());
	  searchBaseWrapper.setCustomList(list);
	  return searchBaseWrapper;
  }

  public BaseWrapper loadCustomer(BaseWrapper baseWrapper)
  {
	  CustomerModel customerModel = (CustomerModel)this.
        customerDAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).
                                    getPrimaryKey());
    baseWrapper.setBasePersistableModel(customerModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchCustTrans(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<CustTransListViewModel>
        list = this.custTransListViewDAO.findByExample( (CustTransListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper searchCustomerByMobile(BaseWrapper wrapper) throws FrameworkCheckedException
  {
    CustomList<AppUserModel> list = appUserDAO.findByExample((AppUserModel)wrapper.getBasePersistableModel());
    if(null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0)
    {
      AppUserModel appUserModel = list.getResultsetList().get(0);
      wrapper.setBasePersistableModel(appUserModel);
    }
    else
    {
      wrapper.setBasePersistableModel(new AppUserModel());
    }
    return wrapper;
  }
  
  @Override
public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper) throws FrameworkCheckedException
  {
	  CustomerModel customerModel = (CustomerModel) wrapper.getBasePersistableModel();
	  CustomList<CustomerModel> list = customerDAO.findByExample(customerModel);
	  if(null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0){
		  customerModel = list.getResultsetList().get(0);
		  AppUserModel appUserModel = new AppUserModel();
		  appUserModel.setCustomerId(customerModel.getCustomerId());
		  CustomList<AppUserModel> appUserList = appUserDAO.findByExample(appUserModel);
		  if(null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0)
		    {
		      appUserModel = appUserList.getResultsetList().get(0);
		      wrapper.setBasePersistableModel(appUserModel);
		    }
		    else
		    {
		      wrapper.setBasePersistableModel(new AppUserModel());
		    }
	  }else
	  {
		  wrapper.setBasePersistableModel(new AppUserModel());
	  }
    
    return wrapper;
  }


  public void setCustTransListViewDAO(CustTransListViewDAO custTransListViewDAO)
  {
    this.custTransListViewDAO = custTransListViewDAO;
  }

  public void setCustomerDAO(CustomerDAO customerDAO)
  {
    this.customerDAO = customerDAO;
  }

  public void setAppUserDAO(AppUserDAO appUserDAO)
  {
    this.appUserDAO = appUserDAO;
  }
  
  

  public void setCustomerRemitterDAO(CustomerRemitterDAO customerRemitterDAO) {
	this.customerRemitterDAO = customerRemitterDAO;
}

public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)
  {

    CustomerModel customerModel = (CustomerModel) baseWrapper.
        getBasePersistableModel();
    /**
     * @todo Uncomment the following line with modified code for getting mobile number
     */
    // int recordCount = getRecordCount(customerModel.getMobileNo());

    int recordCount = 0 ;
    //***Check if customer already exists
     if (recordCount == 0 || customerModel.getPrimaryKey() != null)
     {

       customerModel = this.customerDAO.saveOrUpdate( (
           CustomerModel) baseWrapper.getBasePersistableModel());
       baseWrapper.setBasePersistableModel(customerModel);
       return baseWrapper;

     }
     else
     {

       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }

  }

  private int getRecordCount(String mobileNumber)
  {
    int recordCount;
    CustomerModel customerModel = new CustomerModel();
    /**
     * @todo Uncomment the following line with modified code for getting mobile number
     */
//    customerModel.setMobileNo(mobileNumber);
    return recordCount = customerDAO.countByExample(customerModel);
  }


  public WorkFlowWrapper saveCustomerAndUser(WorkFlowWrapper wrapper)
  {
    CustomerModel customerModel = wrapper.getCustomerModel();

    CustomerModel tempCustomerModel = new CustomerModel();
    /**
     * @todo Uncomment the following line with modified code for getting mobile number
     */
//    tempCustomerModel.setMobileNo(customerModel.getMobileNo());
//    int recordCount = getRecordCount(customerModel.getMobileNo());
    int recordCount = 0 ;

    //***Check if customer already exists
     if (recordCount == 0 || customerModel.getPrimaryKey() != null)
     {
       customerModel = this.setNonRegisteredCustomerData(customerModel);
       customerModel = this.customerDAO.saveOrUpdate(customerModel);
       /**
        * @todo Uncomment the following line with modified code for getting mobile number
        */
//       wrapper.getTransactionModel().setCustomerMobileNo(customerModel.getMobileNo());
       wrapper.getTransactionModel().setCustomerIdCustomerModel(customerModel);
       wrapper.setBasePersistableModel(customerModel);
       this.createAppUserForCustomer(wrapper);
     }
     else
     {

       List<CustomerModel>
           customerModelList = customerDAO.findByExample(tempCustomerModel).
           getResultsetList();
       if (null != customerModelList && customerModelList.size() > 0)
       {
         wrapper.getTransactionModel().setCustomerIdCustomerModel(
             customerModelList.get(0));

       }
     }
    return wrapper;

  }

  private CustomerModel setNonRegisteredCustomerData(CustomerModel
      customerModel)
  {
    customerModel.setRegister(false);
    /**
     * @todo Uncomment the following line with modified code for setting first and last name
     */
//    customerModel.setFirstName("UNREGISTERED");
//    customerModel.setLastName("UNREGISTERED");
    customerModel.setReferringName1("Test");
    customerModel.setReferringName2("Test");
    customerModel.setReferringNic1("1234567890");
    customerModel.setReferringNic2("0987654321");
    customerModel.setUpdatedOn(new Date());
    customerModel.setCreatedOn(new Date());
    customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    customerModel.setRegister(false);
    return customerModel;

  }

  public BaseWrapper createAppUserForCustomer(BaseWrapper wrapper)
  {
    AppUserModel appUserModel = new AppUserModel();
    CustomerModel customerModel = (CustomerModel)
        wrapper.getBasePersistableModel();

    /**
     * @todo Uncomment the following line with modified code for getting mobile number
     */
//    appUserModel.setMobileNo(customerModel.getMobileNo());
    appUserModel.setCreatedOn(new Date());
    appUserModel.setUpdatedOn(new Date());
    appUserModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
    /**
     * @todo Uncomment the following line with modified code for getting mobile number
     */
//    appUserModel.setUsername(customerModel.getMobileNo());
    appUserModel.setPassword(RandomUtils.generateRandom(10, true, true));
    appUserModel.setCustomerId(customerModel.getCustomerId());
    appUserModel.setAccountEnabled(false);
    appUserModel.setAccountExpired(false);
    appUserModel.setAccountLocked(false);
    appUserModel.setCredentialsExpired(true);
    appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);
    return wrapper;

  }
  
  
  @Override
  public void saveOrUpdateCustomerRemitter(List<CustomerRemitterModel> customerRemitterModelList){
      this.customerRemitterDAO.saveOrUpdateCollection(customerRemitterModelList);
  }

}
