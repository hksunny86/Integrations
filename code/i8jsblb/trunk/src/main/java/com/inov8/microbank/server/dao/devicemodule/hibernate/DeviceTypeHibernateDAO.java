package com.inov8.microbank.server.dao.devicemodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.server.dao.devicemodule.DeviceTypeDAO;

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
public class DeviceTypeHibernateDAO
    extends BaseHibernateDAO<DeviceTypeModel, Long, DeviceTypeDAO>
    implements DeviceTypeDAO

{

    @SuppressWarnings( "unchecked" )
    @Override
    public List<DeviceTypeModel> searchDeviceTypes( Long... deviceTypes )
    {
        List<DeviceTypeModel> deviceTypeModelList = null;
        String hql = "from DeviceTypeModel model where model.deviceTypeId IN (:deviceTypeIdsParam)";
        String paramName = "deviceTypeIdsParam";
        deviceTypeModelList = getHibernateTemplate().findByNamedParam( hql, paramName, deviceTypes );
        return deviceTypeModelList;
    }

}
