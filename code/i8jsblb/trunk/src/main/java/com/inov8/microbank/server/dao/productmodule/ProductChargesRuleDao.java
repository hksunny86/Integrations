/**
 * 
 */
package com.inov8.microbank.server.dao.productmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductChargesRuleModel;

/**
 * @author NaseerUl
 *
 */
public interface ProductChargesRuleDao extends BaseDAO<ProductChargesRuleModel, Long>
{
	void deleteByProductId(Long productId);
	void updateAndSaveProductChargesRules(List<ProductChargesRuleModel> existingList, List<ProductChargesRuleModel> newList );

	List<ProductChargesRuleModel> loadProductChargesRulesByServiceOpId(String hql,Object[] parameterList) throws FrameworkCheckedException;
}
