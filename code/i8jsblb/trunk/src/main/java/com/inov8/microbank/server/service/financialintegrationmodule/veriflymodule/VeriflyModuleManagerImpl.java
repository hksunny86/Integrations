package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

/**
 *
 * @author Jawwad Farooq
 *
 */

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.common.model.veriflymodule.VeriflyFormListViewModel;
import com.inov8.microbank.server.dao.veriflymodule.VeriflyDAO;
import com.inov8.microbank.server.dao.veriflymodule.VeriflyFormListViewDAO;

public class VeriflyModuleManagerImpl
    implements VeriflyModuleManager
{
  private VeriflyDAO veriflyDAO;  
  private VeriflyFormListViewDAO veriflyFormListViewDAO;

  public VeriflyModuleManagerImpl()
  {
  }

	public BaseWrapper createOrUpdateVerifly(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		VeriflyModel newVeriflyModel = new VeriflyModel();
		
		VeriflyModel veriflyModel = (VeriflyModel) baseWrapper.getBasePersistableModel();
	    
		newVeriflyModel.setName(veriflyModel.getName());
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
	    int recordCount = veriflyDAO.countByExample(newVeriflyModel,exampleHolder);
	    //***Check if name already exists
	     if (recordCount == 0 || veriflyModel.getPrimaryKey() != null)
	     {
	    	 veriflyModel = this.veriflyDAO.saveOrUpdate( (
	    			 VeriflyModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(veriflyModel);
	       return baseWrapper;

	     }
	    
	       //set baseWrapper to null if record exists
	    	 throw new FrameworkCheckedException("VeriflyNameUniqueException");
	    	// baseWrapper.setBasePersistableModel(null);
	       
	    
	    
	}

	public BaseWrapper loadVerifly(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		VeriflyModel veriflyModel = (VeriflyModel)this.veriflyDAO.
	    findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
	    baseWrapper.setBasePersistableModel(veriflyModel);
	    
	    return baseWrapper;
	}
	
	public SearchBaseWrapper searchVerifly(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<VeriflyFormListViewModel>
        list = this.veriflyFormListViewDAO.findByExample( (VeriflyFormListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}

	public void setVeriflyDAO(VeriflyDAO veriflyDAO)
	{
		this.veriflyDAO = veriflyDAO;
	}

	public void setVeriflyFormListViewDAO(VeriflyFormListViewDAO veriflyFormListViewDAO)
	{
		this.veriflyFormListViewDAO = veriflyFormListViewDAO;
	}



}
