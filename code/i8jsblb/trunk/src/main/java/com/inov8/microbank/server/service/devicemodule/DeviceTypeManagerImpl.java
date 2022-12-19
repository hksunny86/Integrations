package com.inov8.microbank.server.service.devicemodule;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.devicetypemodule.DeviceTypeListViewModel;
import com.inov8.microbank.server.dao.devicemodule.DeviceTypeDAO;
import com.inov8.microbank.server.dao.devicemodule.DeviceTypeListViewDAO;

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
public class DeviceTypeManagerImpl
    implements DeviceTypeManager
{
  private DeviceTypeDAO deviceTypeDAO;
  private DeviceTypeListViewDAO deviceTypeListViewDAO;

  public BaseWrapper loadDeviceType(BaseWrapper baseWrapper)
  {
    DeviceTypeModel deviceTypeModel = (DeviceTypeModel)this.deviceTypeDAO.findByPrimaryKey( (
        baseWrapper.getBasePersistableModel()).getPrimaryKey());
    baseWrapper.setBasePersistableModel(deviceTypeModel);
    return baseWrapper;
  }
  
  public SearchBaseWrapper searchDeviceType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<DeviceTypeListViewModel>
        list = this.deviceTypeListViewDAO.findByExample( (DeviceTypeListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}
  
  public BaseWrapper createOrUpdateDeviceType(BaseWrapper baseWrapper) 
  {
	  DeviceTypeModel newDeviceTypeModel = new DeviceTypeModel();
			
	  DeviceTypeModel deviceTypeModel = (DeviceTypeModel) baseWrapper.getBasePersistableModel();
		    
	  newDeviceTypeModel.setName(deviceTypeModel.getName());
	  ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
	  int recordCount = deviceTypeDAO.countByExample(newDeviceTypeModel,exampleHolder);
	  //***Check if name already exists
	  if (recordCount == 0 || deviceTypeModel.getPrimaryKey() != null)
	  {
		  deviceTypeModel = this.deviceTypeDAO.saveOrUpdate((DeviceTypeModel) baseWrapper.getBasePersistableModel());
		  baseWrapper.setBasePersistableModel(deviceTypeModel);
		  return baseWrapper;		  
	  }
	  else
	  {
		  //set baseWrapper to null if record exists
		  baseWrapper.setBasePersistableModel(null);
		  return baseWrapper;
	  }	  	  
  }


    @Override
    public List<DeviceTypeModel> searchDeviceTypes( Long... deviceTypes ) throws FrameworkCheckedException
    {
        return deviceTypeDAO.searchDeviceTypes( deviceTypes );
    }

  public void setDeviceTypeDAO(DeviceTypeDAO deviceTypeDAO)
  {
    this.deviceTypeDAO = deviceTypeDAO;
  }

public void setDeviceTypeListViewDAO(DeviceTypeListViewDAO deviceTypeListViewDAO) {
	this.deviceTypeListViewDAO = deviceTypeListViewDAO;
}

}
