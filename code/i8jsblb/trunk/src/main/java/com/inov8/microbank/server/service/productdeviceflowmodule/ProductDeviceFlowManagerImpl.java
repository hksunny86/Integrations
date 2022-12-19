package com.inov8.microbank.server.service.productdeviceflowmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductDeviceFlowModel;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.server.dao.productdeviceflowmodule.ProductDeviceFlowDAO;
import com.inov8.microbank.server.dao.productdeviceflowmodule.ProductDeviceFlowListViewDAO;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;

public class ProductDeviceFlowManagerImpl implements ProductDeviceFlowManager{
    private ProductDeviceFlowDAO productDeviceFlowDAO;
    private ProductDeviceFlowListViewDAO productDeviceFlowListViewDAO;
    private ProductCatalogManager productCatalogManager;
    
	/**
     * Search productDeviceFlowListView
     */
    public SearchBaseWrapper searchProductDeviceFlowListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<ProductDeviceFlowListViewModel>
        list = this.productDeviceFlowListViewDAO.findByExample( (ProductDeviceFlowListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
	}
	/**
     * Load productDeviceFlow by primarykey
     */
    public BaseWrapper loadProductDeviceFlowByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)baseWrapper.getBasePersistableModel();
	    productDeviceFlowModel = this.productDeviceFlowDAO.findByPrimaryKey(productDeviceFlowModel.getProductDeviceFlowId());
		baseWrapper.setBasePersistableModel(productDeviceFlowModel);
	    return baseWrapper;
	}
	/**
	 * Create the product device flow entry
	 */
	public BaseWrapper createProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)baseWrapper.getBasePersistableModel();
		int recordCount =this.isproductDeviceFlow(productDeviceFlowModel);
		if (recordCount != 0)
	      {
	    	  throw new FrameworkCheckedException("Recordsalreadyexist");
	    	  
	      }
		this.productDeviceFlowDAO.saveOrUpdate(productDeviceFlowModel);
		this.productCatalogManager.updateDefaultCatalogsVersion(baseWrapper);
		return baseWrapper;
	}
    
	/**
	 * Update the product device flow entry
	 */
	public BaseWrapper updateProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)baseWrapper.getBasePersistableModel();
		this.productDeviceFlowDAO.saveOrUpdate(productDeviceFlowModel);
		this.productCatalogManager.updateDefaultCatalogsVersion(baseWrapper);
		return baseWrapper;
	}
	
	public int  isproductDeviceFlow(ProductDeviceFlowModel productDeviceFlowModel) throws FrameworkCheckedException
	{
		return  productDeviceFlowDAO.countByExample(productDeviceFlowModel);
		
	}
    
	/**
	 * Setters
	 */
	
	public void setProductDeviceFlowDAO(ProductDeviceFlowDAO productDeviceFlowDAO) {
		this.productDeviceFlowDAO = productDeviceFlowDAO;
	}
	public void setProductDeviceFlowListViewDAO(
			ProductDeviceFlowListViewDAO productDeviceFlowListViewDAO) {
		this.productDeviceFlowListViewDAO = productDeviceFlowListViewDAO;
	}
	public void setProductCatalogManager(ProductCatalogManager productCatalogManager)
	{
		this.productCatalogManager = productCatalogManager;
	}
    
}
