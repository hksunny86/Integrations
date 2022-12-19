package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

/**
 *
 * @author Jawwad Farooq
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.SwitchFinderModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.switchmodule.SwitchListViewModel;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.dao.switchmodule.SwitchFinderDAO;
import com.inov8.microbank.server.dao.switchmodule.SwitchListViewDAO;
import com.inov8.microbank.server.dao.switchmodule.SwitchModuleDAO;

public class SwitchModuleManagerImpl
    implements SwitchModuleManager
{
  private SwitchModuleDAO switchDAO;
  private SwitchFinderDAO switchFinderDAO;
  private SwitchListViewDAO switchListViewDAO;
  protected final Log logger = LogFactory.getLog(getClass());
  public SwitchModuleManagerImpl()
  {
  }

  public SwitchWrapper getSwitchClassPath(SwitchWrapper switchWrapper) throws
      FrameworkCheckedException
  {
    SwitchFinderModel switchFinderModel = new SwitchFinderModel() ;

    switchFinderModel.setBankId( switchWrapper.getBankId() );
    switchFinderModel.setPaymentModeId( switchWrapper.getPaymentModeId() );
    
    if(null != switchWrapper.getPaymentModeId() && switchWrapper.getPaymentModeId() == 6L)
    {
    	switchWrapper.setSwitchSwitchModel(switchDAO.findByPrimaryKey(SwitchConstants.CORE_BANKING_SWITCH));
    }
    else if(null != switchWrapper.getPaymentModeId() && (switchWrapper.getPaymentModeId() == 3L || switchWrapper.getPaymentModeId() == 7L))
    {
    	switchWrapper.setSwitchSwitchModel(switchDAO.findByPrimaryKey(SwitchConstants.OLA_SWITCH));
    }
    else
    {
    	logger.error("Invalid Payment Mode Supplied. Please Check .. The Payment Mode was "+switchWrapper.getPaymentModeId());
    	throw new WorkFlowException("Invalid Payment Mode Supplied. Please Check");
    }

    /*
    List switchFinderList = this.switchFinderDAO.findByExample( switchFinderModel ).getResultsetList();

    if( switchFinderList != null && switchFinderList.size() > 0 )
    {
      switchWrapper.setSwitchSwitchModel(switchDAO.findByPrimaryKey(
        ((SwitchFinderModel)switchFinderList.get(0)).getSwitchId() ));
    }
    else
    {
    	logger.error("*******Either Switch Finder List was Empty or it had size zero. The parameters passed were "+switchWrapper.getBankId()+" "+switchWrapper.getPaymentModeId());
    }
    */

    return switchWrapper;
  }
  
	public BaseWrapper createOrUpdateSwitch(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		SwitchModel newSwitchModel = new SwitchModel();
		
		SwitchModel switchModel = (SwitchModel) baseWrapper.getBasePersistableModel();
	    
		newSwitchModel.setName(switchModel.getName());
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		
	    int recordCount = switchDAO.countByExample(newSwitchModel,exampleHolder);
	    //***Check if name already exists
	     if (recordCount == 0 || switchModel.getPrimaryKey() != null)
	     {
	    	 switchModel = this.switchDAO.saveOrUpdate( (
	    			 SwitchModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(switchModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       baseWrapper.setBasePersistableModel(null);
	       return baseWrapper;
	     }
	}

	public BaseWrapper loadSwitch(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		SwitchModel switchModel = (SwitchModel)this.switchDAO.
	    findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
	    baseWrapper.setBasePersistableModel(switchModel);
	    
	    return baseWrapper;
	}
	
	public SearchBaseWrapper searchSwitch(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<SwitchListViewModel>
        list = this.switchListViewDAO.findByExample( (SwitchListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}


  public void setSwitchDAO(SwitchModuleDAO switchModuleDAO)
  {
    this.switchDAO = switchModuleDAO;
  }

  public void setSwitchFinderDAO(SwitchFinderDAO switchFinderDAO)
  {
    this.switchFinderDAO = switchFinderDAO;
  }

public void setSwitchListViewDAO(SwitchListViewDAO switchListViewDAO) {
	this.switchListViewDAO = switchListViewDAO;
}

}
