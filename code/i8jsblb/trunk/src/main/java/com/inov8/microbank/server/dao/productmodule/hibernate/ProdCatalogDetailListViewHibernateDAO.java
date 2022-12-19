package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.server.dao.productmodule.ProdCatalogDetailListViewDAO;



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


public class ProdCatalogDetailListViewHibernateDAO 
	extends BaseHibernateDAO<ProdCatalogDetailListViewModel, Long, ProdCatalogDetailListViewDAO>
	implements ProdCatalogDetailListViewDAO
{
	public List<ProdCatalogDetailListViewModel> loadProductCatalogForVariableAndDiscrete(Long productCatalogId)
	{	
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null)
		{
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId in(1,2) order by pcdlvm.mnoId asc, pcdlvm.supplierName asc, length(pcdlvm.productName) asc, pcdlvm.productName asc ");
		}
		return list;
	}
	
	public List<ProdCatalogDetailListViewModel> loadProductCatalogForBillPayment(Long productCatalogId)
	{	
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null)
		{
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId = 3 order by pcdlvm.mnoId,pcdlvm.productName asc ");
		}
		return list;
	}
	
	public List<ProdCatalogDetailListViewModel> loadCatalogProductsForRetailers(Long productCatalogId)
	{	
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null)
		{
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId in(1,2) order by pcdlvm.sequenceNo asc, pcdlvm.productName asc ");
		}
		return list;
	}
	//**************************************************************************************************************************
	//=========================================
	// Checking Product Catalog
	//=========================================
	public boolean isProductExistInCatalog(Long productCatalogId , long productId){
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null){
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm " +
					"WHERE pcdlvm.productCatalogId = '"+productCatalogId+"'   and  pcdlvm.productId =  '"+productId+"'     ");
		}
		if(list.size() > 0){
			return true;
		}
		return false;
	}
	//***************************************************************************************************************************
	
	public List<ProdCatalogDetailListViewModel> loadCatalogProductsForAllPayWeb(Long productCatalogId)
	{	
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null)
		{
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId in(1,2,3) order by pcdlvm.sequenceNo asc ");
		}
		return list;
	}
	
	public List<ProdCatalogDetailListViewModel> loadCatalogServiceForRetailers(Long productCatalogId)
	{	
		List<ProdCatalogDetailListViewModel> list = null;
		if(productCatalogId != null)
		{
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId in(1,2,3,4,7,8) order by pcdlvm.sequenceNo asc, pcdlvm.productName asc ");
		}
		return list;
	}

    public List<ProdCatalogDetailListViewModel> loadCatalogServiceForCustomer(Long productCatalogId){
        List<ProdCatalogDetailListViewModel> list = null;
        if(productCatalogId != null){
            list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel pcdlvm WHERE pcdlvm.productCatalogId = "+productCatalogId+" and pcdlvm.serviceTypeId in(1,2,3,4,7,8) order by pcdlvm.sequenceForConsumerApp asc, pcdlvm.productName desc ");
        }
        return list;
    }

    public List<ProdCatalogDetailListViewModel> loadCatalogProductForRetailerBulkBillPayment(Long productCatalogId) {
		
		List<ProdCatalogDetailListViewModel> list = null;
		 
		if(productCatalogId != null) {
			list = this.getHibernateTemplate().find("from ProdCatalogDetailListViewModel catalog WHERE catalog.productCatalogId = "+productCatalogId+" and catalog.serviceTypeId not in (7) order by catalog.productName asc ");
		}
		
		return list;
	}
	
}
