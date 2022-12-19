package com.inov8.microbank.webapp.action.inventorymodule;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;

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

public class ProductUnitFormController extends AdvanceFormController
{

	private ShipmentManager shipmentManager;
	private ProductUnitManager productUnitManager;

	public ProductUnitFormController()
	{
		setCommandName("productUnitModel");
		setCommandClass(ProductUnitModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws FrameworkCheckedException
	{

		return null;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{

		Long productId = ServletRequestUtils.getLongParameter(httpServletRequest, "productId");
		Long shipmentId = ServletRequestUtils.getLongParameter(httpServletRequest, "shipmentId");
		long theDate = new Date().getTime();
		if (null != shipmentId && null != productId)
		{
			if (log.isDebugEnabled())
			{
				log.debug("productId and shipmentId is not null....preparing object ");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ProductUnitModel productUnitModel = new ProductUnitModel();
			productUnitModel.setUpdatedOn(new Date(theDate));
			productUnitModel.setCreatedOn(new Date(theDate));
			productUnitModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			productUnitModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			productUnitModel.setShipmentId(shipmentId);
			productUnitModel.setProductId(productId);
			productUnitModel.setSold(false);
			productUnitModel.setActive(true);
			return productUnitModel;
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("either productId or shipmentId is null");
			}

			ProductUnitModel model = new ProductUnitModel();
			return model;
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception
	{
		return this.createOrUpdate(httpServletRequest, httpServletResponse, object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception
	{
		return null;
	}

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception
	{
		
		try
		{   
			Long productID = ServletRequestUtils.getLongParameter(request, "productId");
			productUnitManager.isPriceUnitEmpty(productID);
			if(((ProductUnitModel) command).getActive() == null)
			    ((ProductUnitModel) command).setActive(false);
			((ProductUnitModel) command).setCreatedOn(new Date());
			((ProductUnitModel) command).setUpdatedOn(new Date());
			((ProductUnitModel) command).setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			((ProductUnitModel) command).setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel((ProductUnitModel) command);
			baseWrapper = this.productUnitManager.createOrUpdateProductUnit(baseWrapper);
			//***Check if record already exists.
			if (null != baseWrapper.getBasePersistableModel())
			{ //if not found
				

				if (null != (ServletRequestUtils.getStringParameter(request, "_saveanother")))
				{

					Long productId = ServletRequestUtils.getLongParameter(request, "productId");
					Long shipmentId = ServletRequestUtils.getLongParameter(request, "shipmentId");
					this.saveMessage(request, "Record saved successfully.");
					String strview = "productunitform.html?productId=" + productId + "&shipmentId="
							+ shipmentId;
					RedirectView redirectView = new RedirectView(strview);
					ModelAndView modelAndView = new ModelAndView(redirectView);
					return modelAndView;
				}
				this.saveMessage(request, "Record saved successfully.");
				RedirectView redirectView = new RedirectView("shipmentmanagement.html");
				ModelAndView modelAndView = new ModelAndView(redirectView);
				return modelAndView;
			}
			else
			{
				this.saveMessage(request, "PIN already exists.");
				return super.showForm(request, response, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			
			if( ex.getMessage().equalsIgnoreCase("Priceunitempty") )
			{
				super.saveMessage(request, "Unit Price for the product should be defined before adding a product unit.");
				return super.showForm(request, response, errors);				
			}			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}

	}

	public void setShipmentManager(ShipmentManager shipmentManager)
	{
		this.shipmentManager = shipmentManager;
	}

	public void setProductUnitManager(ProductUnitManager productUnitManager)
	{
		this.productUnitManager = productUnitManager;
	}

}
