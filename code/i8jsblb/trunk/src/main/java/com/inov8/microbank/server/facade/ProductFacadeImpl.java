package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ProductModelVO;
import com.inov8.microbank.common.model.productmodule.ProductLimitRuleViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgVOManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

import java.util.Collection;
import java.util.List;

public class ProductFacadeImpl
    implements ProductFacade
{
  private ProductManager productManager;
  private ProductCatalogManager productCatalogManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private ProductIntgModuleInfoManager productIntgModuleInfoManager;
  private ProductIntgVOManager productIntgVOManager;

  public ProductFacadeImpl()
  {
  }


  //******************************************************************************************
  //======================================================================
  // Methods for Product Catalog
  //======================================================================
    @Override
    public boolean isProductExistInCatalog(Long productCatalogId, long productId)
    {
        return productCatalogManager.isProductExistInCatalog(productCatalogId,productId);
    }
  //******************************************************************************************



  //======================================================================
  // Methods for Product
  //======================================================================

  public SearchBaseWrapper loadProduct(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productManager.loadProduct(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productManager.loadProduct(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper searchProduct(SearchBaseWrapper
                                         searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productManager.searchProduct(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }
  
  public void updateDefaultCatalogsVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.productCatalogManager.updateDefaultCatalogsVersion(baseWrapper);
		}

 		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
		}
	}

  public BaseWrapper createOrUpdateProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productManager.createOrUpdateProduct(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper removeProduct(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productManager.removeProduct(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.DELETE_ACTION);
    }
    return baseWrapper;
  }

  //*======================================================================
// Methods for Product catalogue
//======================================================================


   public BaseWrapper createCatalog(BaseWrapper baseWrapper) throws
       FrameworkCheckedException
   {
     BaseWrapper resultantBaseWrapper = null;
     try
     {
       resultantBaseWrapper = this.productCatalogManager.createCatalog(
           baseWrapper);
     }
     catch (Exception ex)
     {
       throw this.frameworkExceptionTranslator.translate(ex,
           this.frameworkExceptionTranslator.INSERT_ACTION);
     }
     return resultantBaseWrapper;

   }

  public BaseWrapper updateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    BaseWrapper resultantBaseWrapper = null;
    try
    {
      resultantBaseWrapper = this.productCatalogManager.updateCatalog(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return resultantBaseWrapper;

  }

  public BaseWrapper deactivateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    BaseWrapper resultantBaseWrapper = null;
    try
    {
      resultantBaseWrapper = this.productCatalogManager.deactivateCatalog(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return resultantBaseWrapper;

  }

  public SearchBaseWrapper searchCatalog(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    SearchBaseWrapper resultantSearchBasewrapper = null;
    try
    {

      resultantSearchBasewrapper = this.productCatalogManager.searchCatalog(
          searchBaseWrapper);

    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return resultantSearchBasewrapper;
  }

  public BaseWrapper loadCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {

      this.productCatalogManager.loadCatalog(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

  public SearchBaseWrapper loadCatalog(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productCatalogManager.loadCatalog(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadCatalogProducts(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productCatalogManager.loadCatalogProducts(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper loadAllCatalogs() throws FrameworkCheckedException
  {

    try
    {
      SearchBaseWrapper searchBaseWrapper = this.productCatalogManager.
          loadAllCatalogs();
      return searchBaseWrapper;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }

  }

  public BaseWrapper includeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.productCatalogManager.includeProductInCatalogAll(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }

  }

  public BaseWrapper excludeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.productCatalogManager.excludeProductInCatalogAll(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }

  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setProductCatalogManager(ProductCatalogManager
                                       productCatalogManager)
  {
    this.productCatalogManager = productCatalogManager;
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setProductIntgModuleInfoManager(ProductIntgModuleInfoManager
                                              productIntgModuleInfoManager)
  {
    this.productIntgModuleInfoManager = productIntgModuleInfoManager;
  }

  public void setProductIntgVOManager(ProductIntgVOManager productIntgVOManager)
  {
    this.productIntgVOManager = productIntgVOManager;
  }

  public WorkFlowWrapper sellDiscreteProduct(WorkFlowWrapper wrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.productManager.sellDiscreteProduct(wrapper);
    }
    catch(Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
  }

  public WorkFlowWrapper sellVariableProduct(WorkFlowWrapper wrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.productManager.sellVariableProduct(wrapper);
    }
    catch(Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
  }


  public SearchBaseWrapper loadProductIntgModuleInfo(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productIntgModuleInfoManager.loadProductIntgModuleInfo(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadProductIntgModuleInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productIntgModuleInfoManager.loadProductIntgModuleInfo(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper loadProductIntgVO(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productIntgVOManager.loadProductIntgVO(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

public BaseWrapper updateCatalogActivateDeActivate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	BaseWrapper resultantBaseWrapper = null;
    try
    {
      resultantBaseWrapper = this.productCatalogManager.updateCatalogActivateDeActivate(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return resultantBaseWrapper;
}

	@Override
	public List<ProductModel> loadProductsWithNoStakeholderShares() throws FrameworkCheckedException{
		try
		{
			List<ProductModel> productModelList = this.productManager.loadProductsWithNoStakeholderShares();
			return productModelList;
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	
	}

	@Override
	public List<ProductModel> loadProductsByIds(String propertyToSortBy, SortingOrder sortingOrder, Long... productIds) throws FrameworkCheckedException
	{
		List<ProductModel> productModelList = null;
		try
		{
			productModelList = this.productManager.loadProductsByIds(propertyToSortBy, sortingOrder, productIds);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator. FIND_ACTION);
		}
		return productModelList;
	}

	@Override
	public ProductLimitRuleViewModel getProductLimitRuleViewModel(ProductLimitRuleViewModel productLimitRuleViewModel) throws FrameworkCheckedException {

		return productManager.getProductLimitRuleViewModel(productLimitRuleViewModel);
	}

	@Override
	public void saveUpdateCommissionRateDefault(
			CommissionRateDefaultModel commissionRateDefaultModel)
			throws FrameworkCheckedException {
		this.productManager.saveUpdateCommissionRateDefault(commissionRateDefaultModel);
		
	}

	@Override
	public void saveUpdateCommissionShSharesDefaul(
			List<CommissionShSharesDefaultModel> commissionShSharesDefaultModels)
			throws FrameworkCheckedException {
		this.productManager.saveUpdateCommissionShSharesDefaul(commissionShSharesDefaultModels);
	}

	@Override
	public List<ProductChargesRuleModel> searchProductChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return productManager.searchProductChargesRules(searchBaseWrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

    @Override
    public List<ProductThresholdChargesModel> searchProductThresholdChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try
        {
            return productManager.searchProductThresholdChargesRules(searchBaseWrapper);
        }
        catch(Exception e)
        {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
	public void saveUpdateCommissionShSharesRuleModels(
			List<CommissionShSharesRuleModel> commissionShSharesRuleModelList)
			throws FrameworkCheckedException {
		this.productManager.saveUpdateCommissionShSharesRuleModels(commissionShSharesRuleModelList);
	}

	/**
	 * @author AtifHu
	 */
	@Override
	public BaseWrapper createOrUpdateProductLimitRule(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		try {
			return this.productManager.createOrUpdateProductLimitRule(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public void saveOrUpdateAllProductChargesRules(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			productManager.saveOrUpdateAllProductChargesRules(baseWrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

    @Override
    public void saveOrUpdateAllProductThresholdCharges(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try
        {
            productManager.saveOrUpdateAllProductThresholdCharges(baseWrapper);
        }
        catch(Exception e)
        {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
    }

    /**
	 * @author AtifHu
	 */
	@Override
	public List<ProductLimitRuleModel> loadProductLimitRule(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try 
		{
			return productManager.loadProductLimitRule(searchBaseWrapper);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}

	}

	@Override
	public void markProductShSharesRuleDeleted(
			List<CommissionShSharesRuleModel> deleteList)
			throws FrameworkCheckedException {
		try 
		{
			this.productManager.markProductShSharesRuleDeleted(deleteList);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(ex,FrameworkExceptionTranslator.DELETE_ACTION);
		}
	}
	
	
	@Override
	public BaseWrapper addOrUpdateProduct(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {	
		
		try 
		{
			return productManager.addOrUpdateProduct(baseWrapper);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(ex,FrameworkExceptionTranslator.DELETE_ACTION);
		}
	}


	@Override
	public SearchBaseWrapper updateProductCatalogVersion(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		try 
		{
			return this.productManager.updateProductCatalogVersion(searchBaseWrapper);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
		public boolean isAssociatedWithAgentOrHandler(Long productCatalogId)
				throws FrameworkCheckedException {
		try 
		{
			return this.productCatalogManager.isAssociatedWithAgentOrHandler(productCatalogId);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
		}

	@Override
	public void deleteProductShSharesRule(
			Collection<CommissionShSharesRuleModel> commissionShSharesDefaultModels)
			throws FrameworkCheckedException {
		try 
		{
			this.productManager.deleteProductShSharesRule(commissionShSharesDefaultModels);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.DELETE_ACTION);
		}
	}

	@Override
	public boolean isProductNameUnique(ProductModel productModel)
			throws FrameworkCheckedException {
		
		try 
		{
			return this.productManager.isProductNameUnique(productModel);
		}
		catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.DELETE_ACTION);
		}
		
	}
	
	@Override
	public List<ProductModel> loadProductListByService(Long[] serviceIdList) throws FrameworkCheckedException {
		
		return this.productManager.loadProductListByService(serviceIdList);
	}
	
    public List<LabelValueBean> getProductLabelsByReferencedClass(Class clazz, Long pk) throws Exception{
        return productManager.getProductLabelsByReferencedClass(clazz, pk);
      }

	@Override
	public ProductModel loadProductByProductId(Long productId) {

		return productManager.loadProductByProductId(productId);
	}


	@Override
	public ProductModel getProductModelFromVO(ProductModelVO productModelVO) {
		return productManager.getProductModelFromVO(productModelVO);
	}

	@Override
	public SearchBaseWrapper searchProductModels(SearchBaseWrapper searchBaseWrapper) {

		return productManager.searchProductModels(searchBaseWrapper);
	}

	
}
