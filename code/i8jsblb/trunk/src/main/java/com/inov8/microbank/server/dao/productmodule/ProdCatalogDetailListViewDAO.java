package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;

import java.util.List;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public interface ProdCatalogDetailListViewDAO 
	extends BaseDAO<ProdCatalogDetailListViewModel, Long>
{
	public List<ProdCatalogDetailListViewModel> loadProductCatalogForVariableAndDiscrete(Long productCatalogId);
	public List<ProdCatalogDetailListViewModel> loadProductCatalogForBillPayment(Long productCatalogId);
	public List<ProdCatalogDetailListViewModel> loadCatalogProductsForRetailers(Long productCatalogId);
    public List<ProdCatalogDetailListViewModel> loadCatalogServiceForRetailers(Long productCatalogId);
    public List<ProdCatalogDetailListViewModel> loadCatalogServiceForCustomer(Long productCatalogId);
    public List<ProdCatalogDetailListViewModel> loadCatalogProductsForAllPayWeb(Long productCatalogId);
	public List<ProdCatalogDetailListViewModel> loadCatalogProductForRetailerBulkBillPayment(Long productCatalogId);
	public boolean isProductExistInCatalog(Long productCatalogId,long productId);
}