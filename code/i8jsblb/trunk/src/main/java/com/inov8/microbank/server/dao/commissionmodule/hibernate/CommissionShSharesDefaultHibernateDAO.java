package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import com.inov8.microbank.common.vo.rowmappers.ComissionShSharesDefaultRowMapper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesDefaultDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CommissionShSharesDefaultHibernateDAO
    extends BaseHibernateDAO<CommissionShSharesDefaultModel, Long, CommissionShSharesDefaultDAO>
    implements CommissionShSharesDefaultDAO
{
    private JdbcTemplate jdbcTemplate;
	
	
	/*public List<CommissionShSharesDefaultModel>  loadDefaultCommissionShSharesList(CommissionShSharesDefaultModel defaultSharesModel) throws FrameworkCheckedException {
		 DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CommissionShSharesModel.class, "shShare" );
		  
		  if(null != defaultSharesModel.getProductId()){
			  detachedCriteria.createAlias("shShare.relationProductIdProductModel", "pr");
			  detachedCriteria.createAlias("shShare.relationDeviceTypeIdDeviceTypeModel", "dt");
			  detachedCriteria.createAlias("shShare.relationSegmentIdSegmentModel", "se");
			  
			  detachedCriteria.add(Restrictions.or(
		     Restrictions.eq("pr.productId", defaultSharesModel.getProductId()), 
		     Restrictions.isNull("pr.productId")));
		  }
		  
	
		  
		  
		  List<CommissionShSharesDefaultModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
		 
		  return list;
	}*/

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<CommissionShSharesDefaultModel> commissionShSharesDefaultLoad(Long productId) {
        String sql="SELECT *" +
                "  FROM PRODUCT_SH_SHARES_DEFAULT " +
                " WHERE PRODUCT_ID = " + productId;
        return (List<CommissionShSharesDefaultModel>) jdbcTemplate.query(sql, new CommissionShSharesDefaultModel());
    }
}
