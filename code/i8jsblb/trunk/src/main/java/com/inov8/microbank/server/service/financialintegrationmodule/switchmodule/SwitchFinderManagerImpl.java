package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

/**
 *
 * @author Jawwad Farooq
 *
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SwitchFinderModel;
import com.inov8.microbank.common.model.switchmodule.SwitchFinderListViewModel;
import com.inov8.microbank.server.dao.switchmodule.SwitchFinderDAO;
import com.inov8.microbank.server.dao.switchmodule.SwitchFinderListViewDAO;

public class SwitchFinderManagerImpl
    implements SwitchFinderManager
{

  private SwitchFinderDAO switchFinderDAO;
  private SwitchFinderListViewDAO switchFinderListViewDAO;
  

  public SwitchFinderManagerImpl()
  {
  }
  
	public BaseWrapper createOrUpdateSwitchFinder(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		SwitchFinderModel newSwitchFinderModel = new SwitchFinderModel();		
		SwitchFinderModel switchFinderModel = (SwitchFinderModel) baseWrapper.getBasePersistableModel();
	    
		newSwitchFinderModel.setBankId(switchFinderModel.getBankId());
		newSwitchFinderModel.setPaymentModeId(switchFinderModel.getPaymentModeId());
		newSwitchFinderModel.setSwitchId(switchFinderModel.getSwitchId());
		
	    int recordCount = switchFinderDAO.countByExample(newSwitchFinderModel);
	    //***Check if name already exists	    
	     if (recordCount == 0 || switchFinderModel.getPrimaryKey() != null)
	     {
	    	 switchFinderModel = this.switchFinderDAO.saveOrUpdate( (
	    			 SwitchFinderModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(switchFinderModel);
	       return baseWrapper;

	     }
	     
	     
	       //set baseWrapper to null if record exists
	    	 throw new FrameworkCheckedException("SwitchFinderUniqueException");
	       //baseWrapper.setBasePersistableModel(null);
	       //return baseWrapper;
	     
	}

	public BaseWrapper loadSwitchFinder(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		SwitchFinderModel switchFinderModel = (SwitchFinderModel)this.switchFinderDAO.
	    findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
	    baseWrapper.setBasePersistableModel(switchFinderModel);
	    
	    return baseWrapper;
	}
	
	public SearchBaseWrapper searchSwitchFinder(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<SwitchFinderListViewModel>
        list = this.switchFinderListViewDAO.findByExample( (SwitchFinderListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper; 
		
	}




  public void setSwitchFinderDAO(SwitchFinderDAO switchFinderDAO)
  {
    this.switchFinderDAO = switchFinderDAO;
  }

public void setSwitchFinderListViewDAO(
		SwitchFinderListViewDAO switchFinderListViewDAO) {
	this.switchFinderListViewDAO = switchFinderListViewDAO;
}

}
