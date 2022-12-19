package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ReferenceDataController extends AjaxController
{
	private ReferenceDataManager referenceDataManager;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	public ReferenceDataController()
	{
	}

	@SuppressWarnings("unchecked")
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response)
	{

		/* This parameter specifies that what kind of reference data is required 
		 * Please refer to com.inov8.microbank.common.util.PortalConstants.REF_DATA_REQUEST_PARAM for parameter name
		 * and com.inov8.microbank.common.util.PortalConstants.REF_DATA_**** for the possible values
		 * 
		 * */
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of getResponseContent of TransactionDetailSearchController");
			
		}
		
		try
		{
			/*
			 * Product Status can be "active", "inactive", "all"
			 */
			String productStatus	=	ServletRequestUtils.getStringParameter(request, "productStatus", "active");
			
			ProductModel productModel = new ProductModel();
			if(productStatus.equalsIgnoreCase("active"))
			{
				productModel.setActive(true);
			}
			else if(productStatus.equalsIgnoreCase("inactive"))
			{
				productModel.setActive(false);
			}
			else if(productStatus.equalsIgnoreCase("all"))
			{
				productModel.setActive(null);
			}
			if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
				productModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
			List<ProductModel> productModelList = null;
			List<ProductModel> filteredProductModelList = new ArrayList<ProductModel>();
			
			Long requestType = ServletRequestUtils.getRequiredLongParameter(request, PortalConstants.REF_DATA_REQUEST_PARAM);
			if(PortalConstants.REF_DATA_SUPPLIER.equals(requestType))
			{
				productModel.setSupplierId(ServletRequestUtils.getRequiredLongParameter(request, "supplierId"));
			}
			else
				if(PortalConstants.REF_DATA_SERVICE.equals(requestType))
				{
					productModel.setServiceId(ServletRequestUtils.getRequiredLongParameter(request, "serviceId"));
				}
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					productModel, "name", SortingOrder.ASC);

			referenceDataManager.getReferenceData(referenceDataWrapper);

			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				productModelList = referenceDataWrapper.getReferenceDataList();
				
				// ******* Excluding "Commission Settlement" *******
				for(ProductModel pModel: productModelList){
					if(pModel.getProductId().longValue() != 2510734L){
						filteredProductModelList.add(pModel);
					}
				}
			}
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			ajaxXmlBuilder.addItemAsCData("---All---", "");
			
			if(logger.isDebugEnabled())
			{
				logger.debug("end of getResponseContent of TransactionDetailSearchController");
				
			}
			return ajaxXmlBuilder.addItems(filteredProductModelList, "name", "productId").toString();
			
			

		}
//		catch (ServletRequestBindingException rEx)
//		{
//			rEx.printStackTrace();
//		}
//		catch (FrameworkCheckedException ex)
//		{
//			ex.printStackTrace();
//
//		}
		catch (Exception e)
		{
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData("---All---", "").toString();
			return result;
		}

	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}
}
