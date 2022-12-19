/**
 * 
 */
package com.inov8.microbank.server.dao.portal.citymodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author NaseerUl  
 *
 */
public class CityHibernateDAO extends BaseHibernateDAO<CityModel, Long, CityDAO> implements CityDAO
{
    @Override
    public CityModel findAddressById(long cityId) throws FrameworkCheckedException {
        List<CityModel> cityModelList = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CityModel.class );
        detachedCriteria.add(Restrictions.eq("cityId", cityId));
        cityModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
        return cityModelList.get(0);
    }
}
