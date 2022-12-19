package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.common.model.productmodule.ProductCatalogListViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.productmodule.ProdCatalogDetailListViewDAO;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDAO;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDetailDAO;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogListViewDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
 * @author Asad Hayat
 * @version 1.0
 */
public class ProductCatalogManagerImpl
    implements ProductCatalogManager
{
  private ProductCatalogDAO productCatalogDAO;
  private ProductCatalogDetailDAO productCatalogDetailDAO;
  private ProductCatalogListViewDAO productCatalogListViewDAO;
  private ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO;

  @Autowired
  private UserDeviceAccountsDAO	userDeviceAccountsDAO;
  
  @Autowired
  private RetailerContactDAO	retailerContactDAO;  
  
  public void setProductCatalogListViewzDAO(ProductCatalogListViewDAO
                                            productCatalogListViewDAO)
  {
    this.productCatalogListViewDAO = productCatalogListViewDAO;
  }

  public void setProductCatalogDetailDAO(ProductCatalogDetailDAO
                                         productCatalogDetailDAO)
  {
    this.productCatalogDetailDAO = productCatalogDetailDAO;
  }

  public void setProductCatalogDAO(ProductCatalogDAO productCatalogDAO)
  {
    this.productCatalogDAO = productCatalogDAO;
  }

  public void setProductCatalogListViewDAO(ProductCatalogListViewDAO
                                           productCatalogListViewDAO)
  {
    this.productCatalogListViewDAO = productCatalogListViewDAO;
  }

  public BaseWrapper deactivateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    ProductCatalogModel model = (ProductCatalogModel) baseWrapper.
        getBasePersistableModel();

    try
    {

      ProductCatalogDetailModel detailModel = new ProductCatalogDetailModel();
      detailModel.setProductCatalogId(model.getCreatedBy());
      ProductCatalogModel savedModel = this.productCatalogDAO.saveOrUpdate( (
          ProductCatalogModel) baseWrapper.getBasePersistableModel());
      baseWrapper.setBasePersistableModel(savedModel);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchCatalog(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {

    CustomList<ProductCatalogListViewModel>
        list = this.productCatalogListViewDAO.findByExample( (
            ProductCatalogListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());

    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  /**
   * This is meant specifically for the deactivate case
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   */


  public BaseWrapper loadCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    ProductCatalogModel productCatalogModel = this.productCatalogDAO.
        findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(productCatalogModel);
    return baseWrapper;

  }

  public SearchBaseWrapper loadCatalog(SearchBaseWrapper searchBaseWrapper)
  {
    ProductCatalogModel productCatalogModel = this.productCatalogDAO.
        findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(productCatalogModel);
    return searchBaseWrapper;
  }

  public BaseWrapper loadCatalogProducts(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    ProductCatalogDetailModel productCatalogDetailModel = (
        ProductCatalogDetailModel) baseWrapper.getBasePersistableModel();
    baseWrapper.setBasePersistableModel(this.productCatalogDAO.loadCatalog(
        productCatalogDetailModel.getProductCatalogId()));
    return baseWrapper;
  }

  public SearchBaseWrapper loadAllCatalogs() throws
      FrameworkCheckedException
  {
    LinkedHashMap<String, SortingOrder>
        sortingOrderMap = new LinkedHashMap<String, SortingOrder> ();
    sortingOrderMap.put("name", SortingOrder.ASC);

    CustomList<ProductCatalogModel>
        list = this.productCatalogDAO.findAll(sortingOrderMap);

    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;

  }

  public BaseWrapper includeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    try
    {
      //Step1:  Get the id of the catalog named 'ALL'

      ProductCatalogModel catalogModel = this.productCatalogDAO.
          loadCatalogAll();

      if (catalogModel == null)
      {
        throw new FrameworkCheckedException("Catalog named 'ALL' not found.");
      }

      //Step2: Get product id.
      ProductModel prodModel = (ProductModel) baseWrapper.
          getBasePersistableModel();

      //Step3: Prepare ProductCatalogDetailModel

      ProductCatalogDetailModel detailCatalogModel = new
          ProductCatalogDetailModel();
      detailCatalogModel.setProductCatalogId(catalogModel.getPrimaryKey()
          );
      detailCatalogModel.setProductId(prodModel.getPrimaryKey());

      //Step4:  Make sure any record with same prodId and catalogId doesn't exist already
      CustomList<ProductCatalogDetailModel> alreadySavedCatalogDetailRecords = this.
          productCatalogDetailDAO.findByExample(detailCatalogModel);
      if (alreadySavedCatalogDetailRecords.getResultsetList().size() == 0)
      {
        this.productCatalogDetailDAO.saveOrUpdate(detailCatalogModel);
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new FrameworkCheckedException(
          "Product can not be added to catalog named ALL", ex);

    }
    return baseWrapper;

  }

  public BaseWrapper excludeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      //Step1:  Get the id of the catalog named 'ALL'
      ProductCatalogModel catalogModel = this.productCatalogDAO.
          loadCatalogAll();

      if (catalogModel == null)
      {
        throw new FrameworkCheckedException("Catalog named 'ALL' not found.");
      }

      //Step2: Get product id.
      ProductModel prodModel = (ProductModel) baseWrapper.
          getBasePersistableModel();

      //Step3: Prepare ProductCatalogDetailModel

      ProductCatalogDetailModel detailCatalogModel = new
          ProductCatalogDetailModel();
      detailCatalogModel.setProductCatalogId(catalogModel.getPrimaryKey()
          );
      detailCatalogModel.setProductId(prodModel.getPrimaryKey());

      //Step4:  Make sure any record with same prodId and catalogId doesn't exist already
      CustomList<ProductCatalogDetailModel> alreadySavedCatalogDetailRecords = this.
          productCatalogDetailDAO.findByExample(detailCatalogModel);
      if (alreadySavedCatalogDetailRecords.getResultsetList().size() > 0)
      {
        this.productCatalogDetailDAO.delete(alreadySavedCatalogDetailRecords.
                                            getResultsetList().get(0));
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new FrameworkCheckedException(
          "Product can not be deleted from catalog named ALL", ex);

    }
    return baseWrapper;

  }

//  public BaseWrapper createOrUpdateCatalog(BaseWrapper baseWrapper) throws
//      FrameworkCheckedException
//  {
//
//    ProductCatalogModel model = (ProductCatalogModel) baseWrapper.
//        getBasePersistableModel();
//
//    try
//    {
//
//      ProductCatalogDetailModel detailModel = new ProductCatalogDetailModel();
//      detailModel.setProductCatalogId(model.getProductCatalogId());
//      int count = this.productCatalogDetailDAO.countByExample(detailModel);
//
//      if (count > 0)
//      {
//        this.productCatalogDetailDAO.delete(model.getProductCatalogId());
//      }
//
//      ProductCatalogModel savedModel = this.productCatalogDAO.saveOrUpdate( (
//          ProductCatalogModel) baseWrapper.getBasePersistableModel());
//      baseWrapper.setBasePersistableModel(savedModel);
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
//    return baseWrapper;
//  }
  public BaseWrapper createCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    ProductCatalogModel model = (ProductCatalogModel) baseWrapper.
        getBasePersistableModel();
    

      ProductCatalogDetailModel detailModel = new ProductCatalogDetailModel();
      ProductCatalogModel uniqueModel = new ProductCatalogModel();
      detailModel.setProductCatalogId(model.getProductCatalogId());
      
      int count = this.productCatalogDetailDAO.countByExample(detailModel);

      if (count > 0)
      {
        this.productCatalogDetailDAO.delete(model.getProductCatalogId());
      }

      
      if (null==model.getProductCatalogId())
      {
    	  
    	  //int recordcount = this.productCatalogDAO.countByExample(model);
    	  
    	  uniqueModel.setName(model.getName());
    	  if (!isProductCatalogId(uniqueModel))
          {
    		  throw new FrameworkCheckedException("ProductCatalogUniqueException");
          }
      }
      
      ProductCatalogModel savedModel = this.productCatalogDAO.saveOrUpdate( (
          ProductCatalogModel) baseWrapper.getBasePersistableModel());
      baseWrapper.setBasePersistableModel(savedModel);
    
    
    return baseWrapper;
  }
  
  /**
   * Code added by Jawwad Farooq
   */
  public void updateDefaultCatalogsVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)baseWrapper.getBasePersistableModel();
		ProdCatalogDetailListViewModel prodCatalogDetailListView = new ProdCatalogDetailListViewModel();
		
		prodCatalogDetailListView.setProductId(productDeviceFlowModel.getProductId());
		
		List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.findByExample(prodCatalogDetailListView, null).getResultsetList() ;
		Iterator<ProdCatalogDetailListViewModel> iterator = list.iterator();
		
		while( iterator.hasNext() )
		{
			prodCatalogDetailListView = iterator.next() ;
			
			ProductCatalogModel productCatalogModel = new ProductCatalogModel();
			productCatalogModel.setPrimaryKey(prodCatalogDetailListView.getProductCatalogId());
			productCatalogModel = this.productCatalogDAO.findByPrimaryKey(productCatalogModel.getPrimaryKey()) ;
			productCatalogModel.setUpdatedOn(new Date());
			this.productCatalogDAO.saveOrUpdate(productCatalogModel);
			
		}
		
		
//		productCatalogModel.setName(CommandFieldConstants.KEY_DEFAULT_CATALOG_VERSION_NAME);
//		ExampleConfigHolderModel exampleCriteria = new ExampleConfigHolderModel();
//		exampleCriteria.setMatchMode(MatchMode.EXACT);
//		
//		List<ProductCatalogModel> list = this.productCatalogDAO.findByExample(productCatalogModel, null, null , exampleCriteria).getResultsetList() ;
//		
//		if( list != null && list.size() > 0 )
//		{
//			productCatalogModel = list.get(0);
//			productCatalogModel.setUpdatedOn(new Date());
//			this.productCatalogDAO.saveOrUpdate(productCatalogModel);
//			
//		}
//		
//		productCatalogModel = new ProductCatalogModel();
//		
//		productCatalogModel.setName(CommandFieldConstants.KEY_DEFAULT_ALLPAY_CATALOG_VERSION_NAME);
//		list = this.productCatalogDAO.findByExample(productCatalogModel, null).getResultsetList() ;
//		
//		if( list != null && list.size() > 0 )
//		{
//			productCatalogModel = list.get(0);
//			productCatalogModel.setUpdatedOn(new Date());
//			this.productCatalogDAO.saveOrUpdate(productCatalogModel);
//			
//		}
	}


  public BaseWrapper updateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    ProductCatalogModel model = (ProductCatalogModel) baseWrapper.
        getBasePersistableModel();
    
    

      ProductCatalogDetailModel detailModel = new ProductCatalogDetailModel();
      detailModel.setProductCatalogId(model.getProductCatalogId());
      int count = this.productCatalogDetailDAO.countByExample(detailModel);

      if (count > 0)
      {
        this.productCatalogDetailDAO.delete(model.getProductCatalogId());
      }

      ProductCatalogModel savedModel = this.productCatalogDAO.saveOrUpdate( (
          ProductCatalogModel) baseWrapper.getBasePersistableModel());
      baseWrapper.setBasePersistableModel(savedModel);
    

    return baseWrapper;
  }

  public BaseWrapper updateCatalogActivateDeActivate(BaseWrapper baseWrapper) throws
  FrameworkCheckedException
{

  
  ProductCatalogModel savedModel = this.productCatalogDAO.saveOrUpdate( (
      ProductCatalogModel) baseWrapper.getBasePersistableModel());
  baseWrapper.setBasePersistableModel(savedModel);


return baseWrapper;
}

  
  private boolean isProductCatalogId(ProductCatalogModel productCatalogModel)
	{
	  
	  	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.productCatalogDAO.countByExample(productCatalogModel,exampleHolder);
		
		if (count == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean isAssociatedWithAgentOrHandler(Long productCatalogId)
			throws FrameworkCheckedException {

		boolean isAssociated = false;
		int agentCount = 0;
		int handlerCount = 0;
		
		RetailerContactModel agentModel = new RetailerContactModel();// turab
		agentModel.setProductCatalogId(productCatalogId);
		
		agentCount = retailerContactDAO.countByExample(agentModel,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setProdCatalogId(productCatalogId);

		handlerCount = userDeviceAccountsDAO.countByExample(userDeviceAccountsModel,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

		if (agentCount > 0 || handlerCount > 0) {
			isAssociated = true;
		}
		return isAssociated;
	}

    //*********************************************************************************************
    @Override
    public boolean isProductExistInCatalog(Long productCatalogId ,long productId){
        return prodCatalogDetailListViewDAO.isProductExistInCatalog(productCatalogId , productId);
    }
    //*********************************************************************************************

	public void setProdCatalogDetailListViewDAO(ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO)
	{
		this.prodCatalogDetailListViewDAO = prodCatalogDetailListViewDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}


}
