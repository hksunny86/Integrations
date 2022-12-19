package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.CategoryModel;
import com.inov8.microbank.server.dao.productmodule.CategoryDAO;

/**
 * @author Rashid Mahmood
 * @since December 2013
 */

public class CategoryHibernateDAO extends BaseHibernateDAO<CategoryModel, Long, CategoryDAO> implements CategoryDAO 
{
	public List<CategoryModel> findCategoryByName(String name) throws FrameworkCheckedException
	{
		List<CategoryModel> categoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CategoryModel.class );
        detachedCriteria.add( Restrictions.eq( "name", name));
        categoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return categoryModelList;
	}
	
	public List<CategoryModel> findCategoryByMenuLevelId(Long menuLevelId) throws FrameworkCheckedException
	{
		List<CategoryModel> categoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CategoryModel.class );
		detachedCriteria.add( Restrictions.eq( "menuLevelModel.menuLevelId", menuLevelId)).addOrder(Order.asc("categoryId"));
        categoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return categoryModelList;
	}
	
	public List<CategoryModel> findAllCategories() throws FrameworkCheckedException
	{
		List<CategoryModel> categoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CategoryModel.class );
		detachedCriteria.addOrder(Order.asc("categoryId"));
        categoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return categoryModelList;
	}
	
	
	public List<CategoryModel> findParentCategories(Long menuLevelId, Long ultimateParentCategoryId ) throws FrameworkCheckedException
	{
		List<CategoryModel> categoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CategoryModel.class );
        detachedCriteria.add( Restrictions.eq( "menuLevelModel.menuLevelId", menuLevelId));
        detachedCriteria.add( Restrictions.eq( "ultimateParentCategoryModel.categoryId", ultimateParentCategoryId));
        detachedCriteria.addOrder(Order.asc("name"));
        
        categoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return categoryModelList;
	}
	
	public List<CategoryModel> findUltimateParentCategories() throws FrameworkCheckedException
	{
		List<CategoryModel> categoryModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CategoryModel.class );
        detachedCriteria.add( Restrictions.isNull("ultimateParentCategoryModel.categoryId"));
        detachedCriteria.add( Restrictions.isNull("parentCategoryModel.categoryId"));
        detachedCriteria.addOrder(Order.asc("name"));
        
        categoryModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return categoryModelList;
	}
}
