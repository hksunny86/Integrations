package com.inov8.microbank.server.dao.productmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductLimitModel;
import com.inov8.microbank.common.model.productmodule.ProductLimitRuleViewModel;


public interface ProductLimitDAO extends BaseDAO<ProductLimitRuleViewModel, Long> {

	public List<ProductLimitRuleViewModel> getProductLimitRuleViewModel(ProductLimitRuleViewModel productLimitRuleViewModel) throws FrameworkCheckedException;
}