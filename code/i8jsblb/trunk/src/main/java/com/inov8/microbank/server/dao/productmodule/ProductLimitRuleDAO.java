package com.inov8.microbank.server.dao.productmodule;

import java.util.Collection;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductLimitRuleModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Muhammad Atif Hussain
 * @version 1.0
 */

public interface ProductLimitRuleDAO
    extends BaseDAO<ProductLimitRuleModel, Long>
{
	void deleteProductLimitRule(long productId)  throws FrameworkCheckedException;
	void createOrUpdateProductLimitRule(Collection<ProductLimitRuleModel> productLimitRuleModelList)  throws FrameworkCheckedException;
	Collection<ProductLimitRuleModel> loadProductLimitRules(long productId)  throws FrameworkCheckedException;

	List<ProductLimitRuleModel> loadProductLimitRulesByServiceOpId(String hql,Object[] parameterList) throws FrameworkCheckedException;
}
