package com.inov8.microbank.server.service.devicemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.DeviceTypeCommandModel;
import com.inov8.microbank.server.dao.devicemodule.DeviceTypeCommandDAO;

public class DeviceTypeCommandManagerImpl implements DeviceTypeCommandManager {
	
	private DeviceTypeCommandDAO deviceTypeCommandDAO;

	public CustomList loadDeviceTypeCommand(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		CustomList<DeviceTypeCommandModel> list = deviceTypeCommandDAO.findByExample
		((DeviceTypeCommandModel)baseWrapper.getBasePersistableModel());
		
		return list
		;
		
	}

	public void setDeviceTypeCommandDAO(DeviceTypeCommandDAO deviceTypeCommandDAO) {
		this.deviceTypeCommandDAO = deviceTypeCommandDAO;
	}

}
