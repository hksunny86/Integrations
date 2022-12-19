package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.dao.productmodule.ProductDAO;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class ProductHibernateDAO
		extends BaseHibernateDAO<ProductModel, Long, ProductDAO>
		implements ProductDAO
{

	private JdbcTemplate jdbcTemplate;
	public List<ProductModel> loadProductsWithNoStakeholderShares()  throws FrameworkCheckedException{
		String hql = "select p from ProductModel p where p.active = 1 and p.productId NOT IN ( select distinct(csm.relationProductIdProductModel.productId) from CommissionShSharesModel csm )";

		List<ProductModel> productList = this.getHibernateTemplate().find(hql, new Object[]{}) ;

		return productList;

	}


	@Override
	public List<ProductModel> loadProductList(Long productCatalogId)  throws FrameworkCheckedException{
		String hql = "select p " +
				"FROM ProductModel  p, ProductCatalogDetailModel pcd " +
				"where p.productId = pcd.relationProductIdProductModel.productId and p.active = 1 AND pcd.relationProductCatalogIdProductCatalogModel.productCatalogId = ?";
		List<ProductModel> list = this.getHibernateTemplate().find(hql,productCatalogId);

		return list;

	}



	@Override
	public List<ProductModel> loadProductsByCategoryId(Long categoryId)  throws FrameworkCheckedException{
		String hql = "select p " +
				"FROM ProductModel  p where p.active = 1 AND p.appUserTypeModel.appUserTypeId = 3 AND p.relationCategoryIdCategoryModel.categoryId = ?";
		List<ProductModel> list = this.getHibernateTemplate().find(hql,categoryId);
		return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductModel> loadProductsByIds(String propertyToSortBy, SortingOrder sortingOrder, Long... productIds)
	{
		List<ProductModel> productModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductModel.class);
		detachedCriteria.add(Restrictions.in("productId", productIds));

		if( SortingOrder.ASC == sortingOrder )
		{
			detachedCriteria.addOrder(Order.asc(propertyToSortBy));
		}
		else
		{
			detachedCriteria.addOrder( Order.desc(propertyToSortBy) );
		}

		productModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
		return productModelList;
	}
	
	@Override
	public void setUpdatedProductNameInTxDetailMaster(String productName, Long productId) throws FrameworkCheckedException{
		String hql = "update TransactionDetailMasterModel set productName = ? where productId = ?";
		getHibernateTemplate().bulkUpdate(hql,productName,productId);
	}  

	@Override
	public void saveUpdateCommissionRateDefault ( CommissionRateDefaultModel commissionRateDefaultModel )  throws FrameworkCheckedException{
		getHibernateTemplate().saveOrUpdate(commissionRateDefaultModel);
	}

	@Override
	public void saveUpdateCommissionShSharesDefault ( Collection<CommissionShSharesDefaultModel> commissionShSharesDefaultModels )  throws FrameworkCheckedException{
		getHibernateTemplate().saveOrUpdateAll(commissionShSharesDefaultModels);
	}

	@Override
	public CommissionShSharesDefaultModel getDefaultSharesRateById( Long Id ){
		return getHibernateTemplate().get(CommissionShSharesDefaultModel.class, Id);
	}

	@Override
	public void saveUpdateCommissionShSharesRuleModel(List<CommissionShSharesRuleModel> commissionShSharesRuleModelList) throws FrameworkCheckedException{
		getHibernateTemplate().saveOrUpdateAll(commissionShSharesRuleModelList);
	}

	@Override
	public void deleteCommissionShSharesDefault(Collection<CommissionShSharesDefaultModel> commissionShSharesDefaultModels){
		getHibernateTemplate().deleteAll(commissionShSharesDefaultModels);
	}

	@Override
	public void deleteCommissionShSharesRules(Collection<CommissionShSharesRuleModel> commissionShSharesRuleModels) throws FrameworkCheckedException{
		getHibernateTemplate().deleteAll(commissionShSharesRuleModels);
	}

	@Override
	public boolean isProductNameUnique(ProductModel productModel) throws FrameworkCheckedException{
		String hql = "from ProductModel where lower(name) = lower(?)";
		List<ProductModel> productModelList = getHibernateTemplate().find(hql, new Object[]{productModel.getName()});
		
		if(null!=productModel.getPrimaryKey()){
			if(productModelList.size()>1 ){
				return false;
			}
			else
				return !(productModelList.size() == 1 && productModelList.get(0).getPrimaryKey().longValue() != productModel.getPrimaryKey().longValue());
		}
		else{

			return productModelList.size() == 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProductModel loadProductByProductId(Long productId) {

		ProductModel productModel = this.getHibernateTemplate().get(ProductModel.class, productId) ;

		return productModel;
	}

	@Override
	public List<ProductModel>  loadProductModelListByProductIdAndServiceId(Long[] productIds, Long serviceId) throws FrameworkCheckedException {

		List<ProductModel> productList =null;

		String hql = "select productModel from ProductModel productModel where productModel.productId in (:productIds) Or productModel.relationServiceIdServiceModel.serviceId = :serviceId";

		String[] paramNames = {"productIds" ,"serviceId"};
		Object[] values = { productIds,serviceId };

		try {

			productList= getHibernateTemplate().findByNamedParam(hql, paramNames, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}
	
	@Override
	public List<ProductModel>  loadProductModelListByProductIds(Long[] productIds) throws FrameworkCheckedException {

		List<ProductModel> productList =null;

		String hql = "select productModel from ProductModel productModel where productModel.productId in (:productIds)";

		String[] paramNames = {"productIds"};
		Object[] values = { productIds };

		try {

			productList= getHibernateTemplate().findByNamedParam(hql, paramNames, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}

	public List<ProductModel> loadProductListByService(Long[] serviceIdList)  throws FrameworkCheckedException {

		List<ProductModel> productList = new ArrayList<ProductModel>(0);

		String hql = "select productModel from ProductModel productModel where productModel.relationServiceIdServiceModel.serviceId in (:serviceIdList)";

		String[] paramNames = {"serviceIdList"};
		Object[] values = { serviceIdList };

		try {

			List<ProductModel> _productList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			productList.addAll(_productList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}
	
	@Override
	public List<ProductModel> searchAgentProducts()  throws FrameworkCheckedException {

		List<ProductModel> productList = new ArrayList<ProductModel>(0);

		String hql = "select productModel from ProductModel productModel where productModel.active=true and (productModel.relationServiceIdServiceModel.serviceId in (:serviceIdList) or productModel.appUserTypeModel.appUserTypeId=3L) order by productModel.name asc";
		String[] paramNames = {"serviceIdList"};
		Object[] values = {13L};

		try {

			List<ProductModel> _productList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			productList.addAll(_productList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}


	@Override
	public boolean isProductExist(final String productCode)
	{
		StringBuilder sqlBuilder = new StringBuilder(200);
		sqlBuilder.append("select count(*) from PRODUCT where PRODUCT_CODE = ?");
		sqlBuilder.append(')');
		ResultSetExtractor<Boolean> countToBooleanExtractor = new CountToBooleanExtractor();
		Boolean exist = jdbcTemplate.query(sqlBuilder.toString(), new PreparedStatementSetter()
		{
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1,productCode);
			}
		},countToBooleanExtractor);
		return exist;
	}

	@Override
	public ProductModel loadProductByProductName(String productName) throws FrameworkCheckedException {


		String hql = "select productModel from ProductModel productModel where upper(productModel.name) =  :productName";

		String[] paramNames = {"productName"};
		Object[] values = { productName.toUpperCase() };

		try {

			List<ProductModel> _productList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			if(_productList != null && _productList.size() > 0){
				return _productList.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ProductModel loadProductByProductCode(String productCode, Long appUserTypeId) throws FrameworkCheckedException {


		String hql = "select productModel from ProductModel productModel where (upper(productModel.productCode) =  :productCode) AND (upper(productModel.appUserTypeModel.appUserTypeId)= :appUserTypeId) ";

		String[] paramNames = {"productCode","appUserTypeId"};
		Object[] values = { productCode.toUpperCase() ,appUserTypeId};

		try {

			List<ProductModel> _productList = getHibernateTemplate().findByNamedParam(hql, paramNames, values);
			if(_productList != null && _productList.size() > 0){
				return _productList.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override
	public List<LabelValueBean> getProductLabelsByReferencedClass(Class clazz, Long pk) {
		StringBuilder  queryStr = new StringBuilder();
		queryStr.append("select a.productId as id, a.name as label ");
		queryStr.append("from ProductModel a ");
		queryStr.append("where a.active = 1 ");

		if(ServiceModel.class == clazz) {
			queryStr.append("and a.relationServiceIdServiceModel.serviceId =:pk ");
		}

		else if(SupplierModel.class == clazz) {
			queryStr.append("and a.relationSupplierIdSupplierModel.supplierId =:pk ");
		}

		queryStr.append("order by a.name asc");

		try {
			Query query = getSession().createQuery(queryStr.toString());
			query.setParameter("pk", pk);

			query.setResultTransformer(Transformers.aliasToBean(LabelValueBean.class));
			return query.list();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	private final class CountToBooleanExtractor implements ResultSetExtractor<Boolean>
	{
		@Override
		public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException
		{
			int count = 0;
			if(resultSet.next())
			{
				count = resultSet.getInt(1);
			}
			return count > 0;
		}
	}

}
