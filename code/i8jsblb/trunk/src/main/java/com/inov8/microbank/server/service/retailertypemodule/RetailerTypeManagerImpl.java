package com.inov8.microbank.server.service.retailertypemodule;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.RetailerTypeModel;
import com.inov8.microbank.common.model.retailermodule.RetailerTypeListViewModel;
import com.inov8.microbank.server.dao.retailertypemodule.RetailerTypeDAO;
import com.inov8.microbank.server.dao.retailertypemodule.RetailerTypeListViewDAO;

public class RetailerTypeManagerImpl implements RetailerTypeManager {
    private RetailerTypeListViewDAO retailerTypeListViewDAO;
	private RetailerTypeDAO retailerTypeDAO; 
	
	public SearchBaseWrapper searchRetailerType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		CustomList<RetailerTypeListViewModel> list = this.retailerTypeListViewDAO
		.findByExample((RetailerTypeListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
				
		return searchBaseWrapper;
	}
	public BaseWrapper loadRetailerType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		RetailerTypeModel  retailerTypeModel=(RetailerTypeModel)this.retailerTypeDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(retailerTypeModel);	
		return baseWrapper;
	}

	
	public BaseWrapper updateRetailerType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		RetailerTypeModel  retailerTypeModel=(RetailerTypeModel)baseWrapper.getBasePersistableModel();
		RetailerTypeModel newRetailerTypeModel = new RetailerTypeModel();
	    
		newRetailerTypeModel.setName( retailerTypeModel.getName() ) ;
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int recordCount = retailerTypeDAO.countByExample(newRetailerTypeModel,exampleHolder);
		
//		***Check if name already exists	    
	     if (recordCount == 0 || retailerTypeModel.getPrimaryKey() != null)
	     {
	    	 retailerTypeModel = this.retailerTypeDAO.saveOrUpdate( (
	    			 RetailerTypeModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(retailerTypeModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       baseWrapper.setBasePersistableModel(null);
	       return baseWrapper;
	     }
	}
	
	
	public void setRetailerTypeDAO(RetailerTypeDAO retailerTypeDAO) {
		this.retailerTypeDAO = retailerTypeDAO;
	}
	
	public void setRetailerTypeListViewDAO(
			RetailerTypeListViewDAO retailerTypeListViewDAO) {
		this.retailerTypeListViewDAO = retailerTypeListViewDAO;
	}



}
