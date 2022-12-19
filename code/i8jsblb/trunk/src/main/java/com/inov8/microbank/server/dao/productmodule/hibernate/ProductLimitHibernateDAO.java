package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.ProductLimitRuleViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.productmodule.ProductLimitDAO;


public class ProductLimitHibernateDAO extends BaseHibernateDAO<ProductLimitRuleViewModel, Long, ProductLimitDAO> implements ProductLimitDAO {
	
	
	public List<ProductLimitRuleViewModel> getProductLimitRuleViewModel(ProductLimitRuleViewModel productLimitRuleViewModel) throws FrameworkCheckedException {

    	List parameterList = new ArrayList();
    	parameterList.add(Boolean.TRUE);
    	parameterList.add(productLimitRuleViewModel.getProductId());
    	
    	StringBuilder findHQL = new StringBuilder();

		findHQL.append(" from ");
		findHQL.append(productLimitRuleViewModel.getClass().getName());
    	findHQL.append(" where isActive = ? and productId = ?");
		
		if(productLimitRuleViewModel.getDeviceTypeId() != null) {
		
			findHQL.append(" and deviceTypeId = ?");
			parameterList.add(productLimitRuleViewModel.getDeviceTypeId());
		}
		
		if(productLimitRuleViewModel.getSegmentId() != null) {
		
			findHQL.append(" and segmentId = ?");
			parameterList.add(productLimitRuleViewModel.getSegmentId());
		}
		
		if(productLimitRuleViewModel.getDistributorId() != null) {
			findHQL.append(" and distributorId = ? ");
			parameterList.add(productLimitRuleViewModel.getDistributorId());
			
		}

		if(productLimitRuleViewModel.getHandlerAccountTypeId() != null) {
			findHQL.append(" and handlerAccountTypeId = ? ");
			parameterList.add(productLimitRuleViewModel.getHandlerAccountTypeId());
		}else{
			findHQL.append(" and handlerAccountTypeId is null ");
		}

		if(productLimitRuleViewModel.getMnoId() == null)
		{
			findHQL.append(" and (mnoId is null or mnoId = ? ) ");
			parameterList.add(50027L);
		}
		else
		{
			findHQL.append(" and mnoId = ? ");
			parameterList.add(50028L);
		}
    	Object[] parameters = parameterList.toArray();
		
		List<ProductLimitRuleViewModel> productLimitRuleViewModelList = getHibernateTemplate().find(findHQL.toString(), parameters);
		
		return productLimitRuleViewModelList;
	}
}
