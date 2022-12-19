package com.inov8.microbank.server.dao.devicemodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
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
public interface DeviceTypeDAO
    extends BaseDAO<DeviceTypeModel, Long>
{
    List<DeviceTypeModel> searchDeviceTypes(Long... deviceTypes);
}
