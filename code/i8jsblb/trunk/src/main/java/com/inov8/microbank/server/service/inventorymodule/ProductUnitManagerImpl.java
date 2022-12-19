package com.inov8.microbank.server.service.inventorymodule;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.inventorymodule.ProductUnitListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.inventorymodule.ShipmentBaseWrapper;
import com.inov8.microbank.server.dao.inventorymodule.ProductUnitDAO;
import com.inov8.microbank.server.dao.inventorymodule.ProductUnitListViewDAO;
import com.inov8.microbank.server.dao.inventorymodule.ShipmentDAO;
import com.inov8.microbank.server.dao.productmodule.ProductDAO;

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
public class ProductUnitManagerImpl implements ProductUnitManager

{

	private ProductUnitDAO productUnitDAO;
	private ProductUnitListViewDAO productUnitListViewDAO;
	private ShipmentDAO shipmentDAO;
	private ProductDAO productDAO;
	 
	
	private GenericDao genericDAO;

	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}

	
	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public BaseWrapper loadShipmentByPrimaryKey(Long shipmentId) throws FrameworkCheckedException {
		ShipmentModel shipmentModel = this.shipmentDAO.findByPrimaryKey(shipmentId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(shipmentModel);
		return baseWrapper;
	}

	public SearchBaseWrapper loadProductUnit(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		ProductUnitModel productUnitModel = this.productUnitDAO.findByPrimaryKey(searchBaseWrapper
				.getBasePersistableModel().getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(productUnitModel);
		return searchBaseWrapper;

	}

	public BaseWrapper loadProductUnit(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ProductUnitModel productUnitModel = this.productUnitDAO.findByPrimaryKey(baseWrapper
				.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(productUnitModel);
		return baseWrapper;

	}

	public SearchBaseWrapper searchProductUnit(SearchBaseWrapper searchBaseWrapper)
	{

		CustomList<ProductUnitListViewModel> list = this.productUnitListViewDAO.findByExample(
				(ProductUnitListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;

	}

	public BaseWrapper createOrUpdateProductUnit(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ProductUnitModel newProductUnitModel = new ProductUnitModel();
		ProductUnitModel productUnitModel = (ProductUnitModel) baseWrapper.getBasePersistableModel();

		newProductUnitModel.setPin(productUnitModel.getPin());
		newProductUnitModel.setShipmentId(productUnitModel.getShipmentId());
		newProductUnitModel.setProductId(productUnitModel.getProductId());
		newProductUnitModel.setSerialNo(productUnitModel.getSerialNo());

		int recordCount = productUnitDAO.countByExample(newProductUnitModel);
		//***Check if pin already exists
		if (recordCount == 0 || productUnitModel.getProductUnitId() != null)
		{
			productUnitModel = this.productUnitDAO.saveOrUpdate((ProductUnitModel) baseWrapper
					.getBasePersistableModel());
			if (productUnitModel.getActive()==true)
			{
			updateProductShipment(productUnitModel,0);
			}
			else if (productUnitModel.getActive()==false)
			{
				updateProductShipment(productUnitModel,1);
				
			}
			baseWrapper.setBasePersistableModel(productUnitModel);
			return baseWrapper;

		}
		else
		{
			//set baseWrapper to null if record exists
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;

		}
	}
	
	public int  isProductUnit(ProductUnitModel productUnitModel) throws FrameworkCheckedException
	{
		return  productUnitDAO.countByExample(productUnitModel);
		
	}

	public void setProductUnitDAO(ProductUnitDAO productUnitDAO)
	{
		this.productUnitDAO = productUnitDAO;
	}

	public SearchBaseWrapper getUnsoldUnits(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		ProductUnitModel productUnitModel = (ProductUnitModel) searchBaseWrapper.getBasePersistableModel();
		productUnitModel.setSold(false);

		CustomList<ProductUnitModel> list = productUnitDAO.findByExample(productUnitModel);
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public void setProductUnitListViewDAO(ProductUnitListViewDAO productUnitListViewDAO)
	{
		this.productUnitListViewDAO = productUnitListViewDAO;
	}

	public void setShipmentDAO(ShipmentDAO shipmentDAO)
	{
		this.shipmentDAO = shipmentDAO;
	}

	public void updateShipmentreadfromcsvfile(ShipmentBaseWrapper shipmentBaseWrapper)
			throws FrameworkCheckedException
	{
		this.updateProductShipment(shipmentBaseWrapper.getProductUnitModelList());
		this.productUnitDAO.saveOrUpdateCollection(shipmentBaseWrapper.getProductUnitModelList());

	}

	public void updateProductShipment(ProductUnitModel productUnitModel,int operation) throws FrameworkCheckedException
	{
		ProductModel productModel = new ProductModel();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		ShipmentModel shipmentModel = new ShipmentModel();
		productModel.setProductId(productUnitModel.getProductId());
		shipmentModel.setShipmentId(productUnitModel.getShipmentId());
		searchBaseWrapper.setBasePersistableModel(productModel);
		
		
		productModel=productDAO.findByPrimaryKey(productModel.getProductId()); 
			
		searchBaseWrapper.setBasePersistableModel(shipmentModel);
		
		shipmentModel=shipmentDAO.findByPrimaryKey(shipmentModel.getShipmentId());
		
		if (operation==0)
		{
		shipmentModel.setCreditAmount(shipmentModel.getCreditAmount()+productModel.getUnitPrice());
		shipmentModel.setOutstandingCredit(shipmentModel.getOutstandingCredit()+productModel.getUnitPrice());
		shipmentModel.setQuantity(shipmentModel.getQuantity()+1);
		}
		else if (operation==1)
		{
			shipmentModel.setCreditAmount(shipmentModel.getCreditAmount()-productModel.getUnitPrice());
			if (shipmentModel.getCreditAmount()<0)
			{
				shipmentModel.setCreditAmount(0.0);	
			}
			
			
			shipmentModel.setOutstandingCredit(shipmentModel.getOutstandingCredit()-productModel.getUnitPrice());
			if (shipmentModel.getOutstandingCredit()<0)
			{
				shipmentModel.setOutstandingCredit(0.0);
			}
			shipmentModel.setQuantity(shipmentModel.getQuantity()-1);
			if (shipmentModel.getQuantity()<0)
			{
				shipmentModel.setQuantity(0L);	
			}
			
		}
		shipmentModel.setUpdatedOn(new Date());
		shipmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		
		shipmentDAO.saveOrUpdate(shipmentModel);
		
		
	}
	public void updateProductShipment(List<ProductUnitModel> productUnitModelList) throws FrameworkCheckedException
	{
		ProductUnitModel tempProductUnitModel = productUnitModelList.get(0);
		ProductModel productModel = new ProductModel();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		ShipmentModel shipmentModel = new ShipmentModel();
		productModel.setProductId(tempProductUnitModel.getProductId());
		shipmentModel.setShipmentId(tempProductUnitModel.getShipmentId());
		searchBaseWrapper.setBasePersistableModel(productModel);
		
		
		productModel=productDAO.findByPrimaryKey(productModel.getProductId()); 
			
		searchBaseWrapper.setBasePersistableModel(shipmentModel);
		
		shipmentModel=shipmentDAO.findByPrimaryKey(shipmentModel.getShipmentId());
		shipmentModel.setCreditAmount(shipmentModel.getCreditAmount()+(productModel.getUnitPrice()*productUnitModelList.size()));
		shipmentModel.setOutstandingCredit(shipmentModel.getOutstandingCredit()+(productModel.getUnitPrice()*productUnitModelList.size()));
		shipmentModel.setQuantity(shipmentModel.getQuantity()+productUnitModelList.size());
		shipmentModel.setUpdatedOn(new Date());
		shipmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		shipmentDAO.saveOrUpdate(shipmentModel);
		
		
	}
	public void updateShipment(BaseWrapper baseWrapper) throws FrameworkCheckedException

	{
		ShipmentModel shipmentModel = (ShipmentModel) baseWrapper.getBasePersistableModel();
		Long id = shipmentModel.getShipmentId();
		Long quantity = shipmentModel.getQuantity();
		Double outstandingCredit = shipmentModel.getOutstandingCredit();

		shipmentDAO.updateShipment(id, quantity, outstandingCredit);

	}
	
	public boolean isPriceUnitEmpty(Long productId) throws FrameworkCheckedException
	{
		String productHQL = " select pm.unitPrice from ProductModel pm "
			+ "where pm.productId = ? ";
		List unitPriceList = genericDAO.findByHQL(productHQL,
				new Object[] { productId });
		Object obj = (Object) unitPriceList.get(0);
		if(obj==null)
			throw new FrameworkCheckedException("Priceunitempty");
		
		return false;
	}
	
	

}
