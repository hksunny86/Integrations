package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

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
public class DeviceFacadeImpl implements DeviceFacade
{
  private DeviceTypeManager deviceTypeManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public BaseWrapper loadDeviceType(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.deviceTypeManager.loadDeviceType(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;

  }
  
  public BaseWrapper createOrUpdateDeviceType(BaseWrapper baseWrapper) throws
  FrameworkCheckedException
  {
	  try
	  {	
		  this.deviceTypeManager.createOrUpdateDeviceType(baseWrapper);
	  }
	  catch (Exception ex)
	  {
		  throw this.frameworkExceptionTranslator.translate(ex,
				  this.frameworkExceptionTranslator.FIND_ACTION);
	  }
	  return baseWrapper;

}
  
  public SearchBaseWrapper searchDeviceType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		try
		  {
		    this.deviceTypeManager.searchDeviceType(searchBaseWrapper);
		  }
		  catch (Exception ex)
		  {
		    throw this.frameworkExceptionTranslator.translate(ex,
		        this.frameworkExceptionTranslator.
		        FIND_BY_PRIMARY_KEY_ACTION);
		  }
		  return searchBaseWrapper;
	}

    @Override
    public List<DeviceTypeModel> searchDeviceTypes( Long... deviceTypes ) throws FrameworkCheckedException
    {
        List<DeviceTypeModel> deviceTypeModelList = null;

        try
        {
            deviceTypeModelList = deviceTypeManager.searchDeviceTypes( deviceTypes );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }

        return deviceTypeModelList;
    }

  public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager)
  {
    this.deviceTypeManager = deviceTypeManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

}
