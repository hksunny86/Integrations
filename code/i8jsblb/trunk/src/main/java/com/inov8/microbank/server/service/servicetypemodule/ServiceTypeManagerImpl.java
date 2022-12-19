package com.inov8.microbank.server.service.servicetypemodule;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.model.servicemodule.ServiceTypeListViewModel;
import com.inov8.microbank.server.dao.servicetypemodule.ServiceTypeDAO;
import com.inov8.microbank.server.dao.servicetypemodule.ServiceTypeListViewDAO;

public class ServiceTypeManagerImpl implements ServiceTypeManager {

	 private ServiceTypeDAO serviceTypeDAO; 
	 private ServiceTypeListViewDAO serviceTypeListViewDAO;
	
	 
	 public SearchBaseWrapper searchServiceType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		 CustomList<ServiceTypeListViewModel> list = this.serviceTypeListViewDAO
			.findByExample((ServiceTypeListViewModel) searchBaseWrapper
					.getBasePersistableModel(), searchBaseWrapper
					.getPagingHelperModel(), searchBaseWrapper
					.getSortingOrderMap());
						searchBaseWrapper.setCustomList(list);
					
			return searchBaseWrapper;
	}

	
	public BaseWrapper loadServiceType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		ServiceTypeModel serviceTypeModel=this.serviceTypeDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey()); 
		baseWrapper.setBasePersistableModel(serviceTypeModel);
		
		return baseWrapper;
	}

	
	public BaseWrapper updateServiceType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		ServiceTypeModel  serviceTypeModel=(ServiceTypeModel)baseWrapper.getBasePersistableModel();
		ServiceTypeModel newServiceTypeModel = new ServiceTypeModel();
	    
		newServiceTypeModel.setName( serviceTypeModel.getName() ) ;
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int recordCount = serviceTypeDAO.countByExample(newServiceTypeModel, exampleHolder);
		
//		***Check if name already exists	    
	     if (recordCount == 0 || serviceTypeModel.getPrimaryKey() != null)
	     {
	    	 serviceTypeModel = this.serviceTypeDAO.saveOrUpdate( (
	    			 ServiceTypeModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(serviceTypeModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       baseWrapper.setBasePersistableModel(null);
	       return baseWrapper;
	     }
	}


	public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
		this.serviceTypeDAO = serviceTypeDAO;
	}


	public void setServiceTypeListViewDAO(
			ServiceTypeListViewDAO serviceTypeListViewDAO) {
		this.serviceTypeListViewDAO = serviceTypeListViewDAO;
	}



}
