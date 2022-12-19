package com.inov8.microbank.webapp.action.productmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
import com.inov8.microbank.server.service.productmodule.ProductManager;

	

	public class ProductSearchFormController extends BaseFormSearchController

	    
	{
	  private ProductManager productManager;
	  private ReferenceDataManager referenceDataManager;
	 

	  public ProductSearchFormController()
	  {
	   
	    super.setCommandName("productListViewModel");
		super.setCommandClass(ProductListViewModel.class);
	  }

	  public void setProductManager(ProductManager productManager)
	  {
	    this.productManager = productManager;
	  }

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }

	  @Override
	  

	  protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
		{
			Map<String,Object> referenceDataMap = new HashMap<String, Object>();

		    if (log.isDebugEnabled())
		    {
		      log.debug("Inside reference data");
		    }

			/**
			 * code fragment to load reference data for SupplierModel
			 */
			String strSupplierId = httpServletRequest.getParameter("supplierId");

			SupplierModel supplierModel = new SupplierModel();
			supplierModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					supplierModel, "name", SortingOrder.ASC);

			try
			{		
			referenceDataManager.getReferenceData(referenceDataWrapper);

			List<SupplierModel> supplierModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				supplierModelList = referenceDataWrapper.getReferenceDataList();
			}

			ProductModel productModel = null;
			List<ProductModel> productModelList = null;

			if (null != strSupplierId && !"".equals(strSupplierId))
			{
					Long supplierId = Long.parseLong(strSupplierId);
					productModel = new ProductModel();
					productModel.setSupplierId(supplierId);
					productModel.setActive(true);
					referenceDataWrapper = new ReferenceDataWrapperImpl(
							productModel, "name", SortingOrder.ASC);
					referenceDataManager.getReferenceData(referenceDataWrapper);
					if (referenceDataWrapper.getReferenceDataList() != null)
					{
						productModelList = referenceDataWrapper.getReferenceDataList();
					}
			}
			referenceDataMap.put("supplierModelList", supplierModelList);
			referenceDataMap.put("productModelList", productModelList);		
			
			}
			catch (Exception ex)
			{
				log.error("Error getting product data in loadReferenceData", ex);
				ex.printStackTrace();
			}
		    
		    return referenceDataMap;	
		}

	  @Override
	  
	  
	  protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
				HttpServletResponse httpServletResponse,
				Object model,
				PagingHelperModel pagingHelperModel,
				LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception{

			ModelAndView modelAndView = new ModelAndView(getSuccessView());
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			
			List resultList = new ArrayList();
			
			
				
				ProductListViewModel productListViewModel = (ProductListViewModel) model;
				
				
				if(sortingOrderMap.isEmpty()){
		        	sortingOrderMap.put("serviceName", SortingOrder.ASC );
		        }
							
				searchBaseWrapper.setBasePersistableModel(productListViewModel);
				searchBaseWrapper.setPagingHelperModel(pagingHelperModel);       
				searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

				searchBaseWrapper = this.productManager.searchProduct(searchBaseWrapper);
									
	            if ( searchBaseWrapper.getCustomList() != null
	                 && searchBaseWrapper.getCustomList().getResultsetList() != null )
	            {
	                resultList = searchBaseWrapper.getCustomList().getResultsetList();
	            }
		        else
		        {
		            pagingHelperModel.setTotalRecordsCount( 0 );
		        }
			
			modelAndView.addObject("productListViewModelList", resultList);
			
			return modelAndView;
		}

	}


