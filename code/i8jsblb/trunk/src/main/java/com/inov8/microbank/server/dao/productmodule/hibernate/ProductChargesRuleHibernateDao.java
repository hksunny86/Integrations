/**
 * 
 */
package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import org.apache.commons.collections.CollectionUtils;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductChargesRuleModel;
import com.inov8.microbank.server.dao.productmodule.ProductChargesRuleDao;

/**
 * @author NaseerUl
 *
 */
public class ProductChargesRuleHibernateDao extends BaseHibernateDAO<ProductChargesRuleModel, Long, BaseDAO<ProductChargesRuleModel,Long>> implements ProductChargesRuleDao
{
	@Override
	public void deleteByProductId(Long productId)
	{
		String hql = "delete from ProductChargesRuleModel model where model.productModel.productId=?";
		getHibernateTemplate().bulkUpdate(hql,productId);
	}
	
	@Override
	public void updateAndSaveProductChargesRules(List<ProductChargesRuleModel> existingList, List<ProductChargesRuleModel> newList){
		
		if(CollectionUtils.isNotEmpty(existingList)){
			saveOrUpdateCollection(existingList);
		}
		
		// without following flush, DB trigger execution order is disturbed
		getHibernateTemplate().flush();
		
		if(CollectionUtils.isNotEmpty(newList)){
			saveOrUpdateCollection(newList);
		}
		
	}

	@Override
	public List<ProductChargesRuleModel> loadProductChargesRulesByServiceOpId(String hql, Object[] parameterList) throws FrameworkCheckedException {
		List<ProductChargesRuleModel> list = getHibernateTemplate().find(hql,parameterList);
		return list;
	}

}
