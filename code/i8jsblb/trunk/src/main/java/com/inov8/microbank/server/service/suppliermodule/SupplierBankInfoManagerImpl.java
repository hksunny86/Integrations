package com.inov8.microbank.server.service.suppliermodule;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierBankInfoListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.suppliermodule.SupplierBankInfoDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierBankInfoListViewDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class SupplierBankInfoManagerImpl implements SupplierBankInfoManager
{
  private SupplierBankInfoDAO supplierBankInfoDAO;
  private SupplierBankInfoListViewDAO supplierBankInfoListViewDAO ;

  public void setSupplierBankInfoListViewDAO(
		SupplierBankInfoListViewDAO supplierBankInfoListViewDAO) {
	this.supplierBankInfoListViewDAO = supplierBankInfoListViewDAO;
}

public BaseWrapper loadSupplierBankInfo(BaseWrapper baseWrapper)
  {
    List supplierBankInfoModelList = this.supplierBankInfoDAO.findByExample((SupplierBankInfoModel)baseWrapper.
        getBasePersistableModel()).getResultsetList() ;
    if( supplierBankInfoModelList.size() > 0 )
      baseWrapper.setBasePersistableModel((SupplierBankInfoModel)supplierBankInfoModelList.get(0));
    else
      baseWrapper.setBasePersistableModel(null) ;

    return baseWrapper;
  }

  public void setSupplierBankInfoDAO(SupplierBankInfoDAO supplierBankInfoDAO)
  {
    this.supplierBankInfoDAO = supplierBankInfoDAO;
  }

public BaseWrapper createSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

	SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
	SupplierBankInfoModel theNewSupplierBankInfoModel = new SupplierBankInfoModel();
	SupplierBankInfoListViewModel supplierBankInfoListViewModel = (SupplierBankInfoListViewModel) baseWrapper.
	        getBasePersistableModel();
	
	supplierBankInfoModel.setName(supplierBankInfoListViewModel.getName());
	 ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
	    int recordCount = supplierBankInfoDAO.countByExample(supplierBankInfoModel, exampleHolder);
	    //***Check if name already exists
	     if (recordCount == 0 )
	     {
	    
	    	 
	    	 	
	    	 //theNewSupplierBankInfoModel.setMerchantCategory(supplierBankInfoListViewModel.getMerchantCategory());
	    	 theNewSupplierBankInfoModel.setAccountNo(supplierBankInfoListViewModel.getAccountNo());
	    	 theNewSupplierBankInfoModel.setBankId(supplierBankInfoListViewModel.getBankId());
	    	 theNewSupplierBankInfoModel.setName(supplierBankInfoListViewModel.getName());
	    	 theNewSupplierBankInfoModel.setPaymentModeId(supplierBankInfoListViewModel.getPaymentModeId());
	    	 theNewSupplierBankInfoModel.setSupplierId(supplierBankInfoListViewModel.getSupplierId());
	    	 theNewSupplierBankInfoModel.setActive(supplierBankInfoListViewModel.getActive()==null?false:supplierBankInfoListViewModel.getActive());
		   	theNewSupplierBankInfoModel.setCreatedBy(UserUtils.getCurrentUser()
					.getAppUserId());
		   	theNewSupplierBankInfoModel.setCreatedOn(new Date());
		   	theNewSupplierBankInfoModel.setUpdatedBy(UserUtils.getCurrentUser()
					.getAppUserId());
		   	theNewSupplierBankInfoModel.setUpdatedOn(new Date());
			
			baseWrapper.setBasePersistableModel(theNewSupplierBankInfoModel);
			theNewSupplierBankInfoModel = this.supplierBankInfoDAO.saveOrUpdate( (SupplierBankInfoModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(theNewSupplierBankInfoModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	 			throw new FrameworkCheckedException("NameUniqueException");
	     }


}

public BaseWrapper updateSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

	SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
	SupplierBankInfoListViewModel supplierBankInfoListViewModel = (SupplierBankInfoListViewModel) baseWrapper.
	        getBasePersistableModel();
	
	supplierBankInfoModel.setName(supplierBankInfoListViewModel.getName());
	    int recordCount = supplierBankInfoDAO.countByExample(supplierBankInfoModel);
	    //***Check if name already exists
	     if (recordCount != 0 || supplierBankInfoListViewModel.getSupplierBankInfoId() != null)
	     {
	    
	   	 
	   		 
	   		supplierBankInfoModel=this.supplierBankInfoDAO.findByPrimaryKey(supplierBankInfoListViewModel.getSupplierBankInfoId());
			
	   		//supplierBankInfoModel.setMerchantCategory(supplierBankInfoListViewModel.getMerchantCategory());
	   		supplierBankInfoModel.setAccountNo(supplierBankInfoListViewModel.getAccountNo());
	   		supplierBankInfoModel.setBankId(supplierBankInfoListViewModel.getBankId());
	   		supplierBankInfoModel.setName(supplierBankInfoListViewModel.getName());
	   		supplierBankInfoModel.setPaymentModeId(supplierBankInfoListViewModel.getPaymentModeId());
	   		supplierBankInfoModel.setSupplierId(supplierBankInfoListViewModel.getSupplierId());
	   		supplierBankInfoModel.setActive(supplierBankInfoListViewModel.getActive()==null?false:supplierBankInfoListViewModel.getActive());
	   		supplierBankInfoModel.setUpdatedOn(new Date());
	   		supplierBankInfoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	   		
		
		baseWrapper.setBasePersistableModel(supplierBankInfoModel);
	    	 supplierBankInfoModel = this.supplierBankInfoDAO.saveOrUpdate( (SupplierBankInfoModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(supplierBankInfoModel);
	     
	       return baseWrapper;

	     }
	     throw new FrameworkCheckedException("NameUniqueException");
}


public SearchBaseWrapper searchSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	CustomList<SupplierBankInfoListViewModel>
    list = this.supplierBankInfoListViewDAO.findByExample( (SupplierBankInfoListViewModel)
    searchBaseWrapper.getBasePersistableModel(),
    searchBaseWrapper.getPagingHelperModel(),
    searchBaseWrapper.getSortingOrderMap());
searchBaseWrapper.setCustomList(list);
return searchBaseWrapper;
}




public SearchBaseWrapper loadSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	
	
	SupplierBankInfoListViewModel supplierBankInfoListViewModel = this.supplierBankInfoListViewDAO
	.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
			.getPrimaryKey());
searchBaseWrapper.setBasePersistableModel(supplierBankInfoListViewModel);
return searchBaseWrapper;

}


	public SupplierBankInfoModel getSupplierBankInfoModel(SupplierBankInfoModel example) throws FrameworkCheckedException {

		SupplierBankInfoModel supplierBankInfoModel = null;
		
		ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);		
		
		CustomList<SupplierBankInfoModel> customList =  supplierBankInfoDAO.findByExample(example, null, null, exampleConfigHolderModel, null);
		
		if(customList != null && customList.getResultsetList() != null && customList.getResultsetList().size() > 0) {
			
			supplierBankInfoModel = (SupplierBankInfoModel) customList.getResultsetList().get(0);
		}
		
		return supplierBankInfoModel;
	}

}
