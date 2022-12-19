package com.inov8.microbank.server.service.productmodule;

import java.util.Collection;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ProductModelVO;
import com.inov8.microbank.common.model.productmodule.ProductLimitRuleViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public interface ProductManager
{

  public SearchBaseWrapper loadProduct(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchProduct(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createOrUpdateProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper removeProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  BaseWrapper addOrUpdateProduct(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;

  public WorkFlowWrapper sellDiscreteProduct(WorkFlowWrapper wrapper) throws
      FrameworkCheckedException;

  public WorkFlowWrapper sellVariableProduct(WorkFlowWrapper wrapper) throws
      FrameworkCheckedException;
 
  public List<ProductModel> loadProductsWithNoStakeholderShares() throws FrameworkCheckedException;
  public List<ProductModel> loadProductsByIds(String propertyToSortBy, SortingOrder sortingOrder, Long... productIds) throws FrameworkCheckedException;
  public ProductLimitRuleViewModel getProductLimitRuleViewModel(ProductLimitRuleViewModel productLimitRuleViewModel) throws FrameworkCheckedException;

  public  void saveUpdateCommissionRateDefault(CommissionRateDefaultModel commissionRateDefaultModel)
		throws FrameworkCheckedException;

  public void saveUpdateCommissionShSharesDefaul(List <CommissionShSharesDefaultModel> commissionShSharesDefaultModels)
		throws FrameworkCheckedException;

  public List<ProductChargesRuleModel> searchProductChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

  public List<ProductThresholdChargesModel> searchProductThresholdChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

  public void saveOrUpdateAllProductChargesRules(BaseWrapper baseWrapper) throws FrameworkCheckedException;

  public void saveOrUpdateAllProductThresholdCharges(BaseWrapper baseWrapper) throws FrameworkCheckedException;

  public void saveUpdateCommissionShSharesRuleModels(List<CommissionShSharesRuleModel> commissionShSharesRuleModelList)
		throws FrameworkCheckedException;

	public BaseWrapper createOrUpdateProductLimitRule(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	
	public List<ProductLimitRuleModel> loadProductLimitRule(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public void markProductShSharesRuleDeleted(List<CommissionShSharesRuleModel> deleteList)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper updateProductCatalogVersion(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;

	public void deleteProductShSharesRule(Collection<CommissionShSharesRuleModel> commissionShSharesDefaultModels)
			throws FrameworkCheckedException;

	public boolean isProductNameUnique(ProductModel productModel) throws FrameworkCheckedException;
	
	List<ProductModel> loadProductListByService(Long[] serviceIdList)  throws FrameworkCheckedException;
	List<LabelValueBean> getProductLabelsByReferencedClass(Class clazz, Long pk) throws Exception;
	ProductModel loadProductByProductId(Long productId);

	ProductModel getProductModelFromVO(ProductModelVO productModelVO);
	
	SearchBaseWrapper searchProductModels(SearchBaseWrapper searchBaseWrapper);

}
