package com.inov8.microbank.server.service.devicemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DeviceTypeModel;

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
public interface DeviceTypeManager
{
  public BaseWrapper loadDeviceType(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public BaseWrapper createOrUpdateDeviceType(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
      
  public SearchBaseWrapper searchDeviceType(SearchBaseWrapper searchBaseWrapper)throws
			FrameworkCheckedException;
  
  List<DeviceTypeModel> searchDeviceTypes(Long... deviceTypes) throws FrameworkCheckedException;
}
