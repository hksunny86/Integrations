package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionRateDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesRuleModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.LabelValueBean;

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

public interface ProductDAO
    extends BaseDAO<ProductModel, Long>
{
	List<ProductModel> loadProductsWithNoStakeholderShares()  throws FrameworkCheckedException;

    List<ProductModel> loadProductList(Long productCatalogId)  throws FrameworkCheckedException;

    List<ProductModel> loadProductsByIds(String propertyToSortBy, SortingOrder sortingOrder, Long... productIds);
	void saveUpdateCommissionRateDefault(CommissionRateDefaultModel commissionRateDefaultModel) throws FrameworkCheckedException;
	void saveUpdateCommissionShSharesDefault(Collection<CommissionShSharesDefaultModel> commissionShSharesDefaultModels)  throws FrameworkCheckedException;
	CommissionShSharesDefaultModel getDefaultSharesRateById(Long Id);
	void saveUpdateCommissionShSharesRuleModel(List<CommissionShSharesRuleModel> commissionShSharesRuleModelList)
			throws FrameworkCheckedException;
	void deleteCommissionShSharesDefault(Collection<CommissionShSharesDefaultModel> commissionShSharesDefaultModels)
			throws FrameworkCheckedException;
	void deleteCommissionShSharesRules(Collection<CommissionShSharesRuleModel> commissionShSharesRuleModels) throws FrameworkCheckedException;
	boolean isProductNameUnique(ProductModel productModel)throws FrameworkCheckedException;
	ProductModel loadProductByProductId(Long productId);
	List<ProductModel> loadProductListByService(Long[] serviceIdList)  throws FrameworkCheckedException;
	List<ProductModel> loadProductsByCategoryId(Long categoryId)  throws FrameworkCheckedException;

	boolean isProductExist(String productCode);

	ProductModel loadProductByProductName(String productCode) throws FrameworkCheckedException;

	List<ProductModel>  loadProductModelListByProductIdAndServiceId(Long[] productIds , Long serviceId) throws FrameworkCheckedException;

	void setUpdatedProductNameInTxDetailMaster(String productName,Long productId) throws FrameworkCheckedException;

	List<ProductModel> loadProductModelListByProductIds(Long[] productIds)
			throws FrameworkCheckedException;

	List<ProductModel> searchAgentProducts() throws FrameworkCheckedException;

    ProductModel loadProductByProductCode(String productCode,Long appUserTypeId) throws FrameworkCheckedException;

    List<LabelValueBean> getProductLabelsByReferencedClass(Class clazz, Long pk);
}
