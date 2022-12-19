/**
 * 
 */
package com.inov8.microbank.server.dao.productmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.productmodule.CategoryModel;

/**
 * @author Rashid Mahmood
 * @since  December 2013
 */
public interface CategoryDAO extends BaseDAO<CategoryModel, Long> 
{
	public List<CategoryModel> findParentCategories(Long menuLevelId, Long ultimateParentCategoryId ) throws FrameworkCheckedException;
	public List<CategoryModel> findUltimateParentCategories() throws FrameworkCheckedException;
	public List<CategoryModel> findCategoryByName(String name) throws FrameworkCheckedException;
	public List<CategoryModel> findCategoryByMenuLevelId(Long menuLevelId) throws FrameworkCheckedException;
	public List<CategoryModel> findAllCategories() throws FrameworkCheckedException;
}
