package com.inov8.microbank.server.service.servicemodule;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.productmodule.ServiceListViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.dao.servicemodule.ServiceDAO;
import com.inov8.microbank.server.dao.servicemodule.ServiceListViewDAO;



public class ServiceManagerImpl implements ServiceManager
{
	ServiceDAO serviceDAO ;
	ServiceListViewDAO serviceListViewDAO;

	public BaseWrapper createOrUpdateService(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		ServiceModel newServiceModel = new ServiceModel();
		
		ServiceModel serviceModel = (ServiceModel) baseWrapper.getBasePersistableModel();
	    
	    newServiceModel.setName(serviceModel.getName());
	    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
	    int recordCount = serviceDAO.countByExample(newServiceModel, exampleHolder);
	    //***Check if name already exists
	     if (recordCount == 0 || serviceModel.getPrimaryKey() != null)
	     {
	    	 serviceModel = this.serviceDAO.saveOrUpdate( (
	    			 ServiceModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(serviceModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       baseWrapper.setBasePersistableModel(null);
	       return baseWrapper;
	     }
	}

	public BaseWrapper loadService(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
	    ServiceModel serviceModel = (ServiceModel)this.serviceDAO.
	    findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
	    baseWrapper.setBasePersistableModel(serviceModel);
	    
	    return baseWrapper;
	}
	
	public SearchBaseWrapper searchService(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<ServiceListViewModel>
        list = this.serviceListViewDAO.findByExample( (ServiceListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}
	
	@Override
	public List<LabelValueBean> getServiceLabels(Long... pk) {
		return serviceDAO.getServiceLabels(pk);
	}
	

	public void setServiceDAO(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	public void setServiceListViewDAO(ServiceListViewDAO serviceListViewDAO) {
		this.serviceListViewDAO = serviceListViewDAO;
	}

}
