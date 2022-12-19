/**
 * 
 */
package com.inov8.microbank.server.dao.portal.citymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.CityModel;

/**
 * @author NaseerUl  
 *
 */
public interface CityDAO extends BaseDAO<CityModel, Long>
{
    public CityModel findAddressById(long cityId) throws FrameworkCheckedException;

}
