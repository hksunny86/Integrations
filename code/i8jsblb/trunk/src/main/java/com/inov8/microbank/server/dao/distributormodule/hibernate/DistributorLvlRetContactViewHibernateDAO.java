package com.inov8.microbank.server.dao.distributormodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorLvlRetContactViewDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 
 * @author Hassan Javaid
 * @version 1.0
 */
public class DistributorLvlRetContactViewHibernateDAO
    extends BaseHibernateDAO<DistributorLvlRetContactViewModel, Long,DistributorLvlRetContactViewDAO>
    implements DistributorLvlRetContactViewDAO
{
	public SearchBaseWrapper getParentAgentsBydistributorLevelId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	
		Long retailerId = (Long) searchBaseWrapper.getObject("retailerId");
		Long distributorId = (Long) searchBaseWrapper.getObject("distributorId");


		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

		java.sql.CallableStatement cstmt = null;

		try {

			cstmt = connection.prepareCall("{ call DYNAMIC_PARAMETERS.SET_RETAILER (?)}");

				cstmt.setLong(1,retailerId);

			cstmt.execute();
			cstmt = connection.prepareCall("{ call DYNAMIC_PARAMETERS.SET_DISTRIBUTOR (?)}");

			cstmt.setLong(1,distributorId);
			cstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}


		CustomList<DistributorLvlRetContactViewModel> list = this.findByExample((DistributorLvlRetContactViewModel)
			    searchBaseWrapper.
			    getBasePersistableModel(),
			    searchBaseWrapper.
			    getPagingHelperModel(),
			    searchBaseWrapper.
			    getSortingOrderMap());
			searchBaseWrapper.setCustomList(list);	
		return searchBaseWrapper;	
	}

	@Override
	public List<DistributorLvlRetContactViewModel> getParentAgentsByDistributorLevelId(Long retailerId,Long distributorLevelId,Long distributorId) throws FrameworkCheckedException {

		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

		java.sql.CallableStatement callableStatement = null;

		try {
			callableStatement = connection.prepareCall("{ call DYNAMIC_PARAMETERS.SET_RETAILER (?)}");
			callableStatement.setLong(1,retailerId);
			callableStatement.execute();
			callableStatement = connection.prepareCall("{ call DYNAMIC_PARAMETERS.SET_DISTRIBUTOR (?)}");
			callableStatement.setLong(1,distributorLevelId);
			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		DistributorLvlRetContactViewModel model = new DistributorLvlRetContactViewModel();
		CustomList<DistributorLvlRetContactViewModel> customList = this.findByExample(model);
		if(customList != null && customList.getResultsetList().size() > 0)
			return customList.getResultsetList();
		return null;
	}
}
