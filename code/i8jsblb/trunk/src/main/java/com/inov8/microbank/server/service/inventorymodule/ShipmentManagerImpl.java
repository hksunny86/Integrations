package com.inov8.microbank.server.service.inventorymodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.inventorymodule.ShipmentListViewModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.server.dao.inventorymodule.ShipmentDAO;
import com.inov8.microbank.server.dao.inventorymodule.ShipmentListViewDAO;

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

public class ShipmentManagerImpl implements ShipmentManager,ServiceTypeConstantsInterface {

	private ShipmentDAO shipmentDAO;

	private ShipmentListViewDAO shipmentListViewDAO;

	private GenericDao genericDAO;

	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}

	public SearchBaseWrapper loadShipment(SearchBaseWrapper searchBaseWrapper) {
		ShipmentModel shipmentModel = this.shipmentDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(shipmentModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadShipmentListViewByPrimaryKey(Long shipmentId)
			throws FrameworkCheckedException {
		ShipmentListViewModel shipmentListViewModel = this.shipmentListViewDAO
				.findByPrimaryKey(shipmentId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(shipmentListViewModel);
		return baseWrapper;
	}

	public BaseWrapper loadShipment(BaseWrapper baseWrapper) {
		ShipmentModel shipmentModel = this.shipmentDAO
				.findByPrimaryKey(baseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		baseWrapper.setBasePersistableModel(shipmentModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchShipment(SearchBaseWrapper searchBaseWrapper) {

		CustomList<ShipmentListViewModel> list = this.shipmentListViewDAO
				.findByExample((ShipmentListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);

		return searchBaseWrapper;

	}

	public BaseWrapper createOrUpdateShipment(BaseWrapper baseWrapper) {

		ShipmentModel shipmentModel = this.shipmentDAO
				.saveOrUpdate((ShipmentModel) baseWrapper
						.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(shipmentModel);
		return baseWrapper;
	}

	public List<ProductModel> searchProductModels(Long supplierId) {

		List productModelList = null;
		List productModelListView = new ArrayList();
		String productHQL = " select pm.productId, pm.name from ProductModel pm, ServiceModel sm "
				+ "where pm.relationSupplierIdSupplierModel.supplierId = ? "
				+ "and pm.relationServiceIdServiceModel.serviceId = sm.serviceId "
				+ "and pm.active= true "
				+ "and sm.relationServiceTypeIdServiceTypeModel.serviceTypeId <> 3"
				+ "order by pm.name";

		productModelList = genericDAO.findByHQL(productHQL,
				new Object[] { supplierId });

		ProductModel productModel = null;
		for (int count = 0; count < productModelList.size(); count++) {
			productModel = new ProductModel();

			Object obj[] = (Object[]) productModelList.get(count);

			productModel.setPrimaryKey((Long) obj[0]);
			productModel.setName((String) obj[1]);

			productModelListView.add(productModel);

		}
		return productModelListView;
	}

	public void setShipmentDAO(ShipmentDAO shipmentDAO) {
		this.shipmentDAO = shipmentDAO;

	}

	public void setShipmentListViewDAO(ShipmentListViewDAO shipmentListViewDAO) {
		this.shipmentListViewDAO = shipmentListViewDAO;
	}

	public SearchBaseWrapper getOutstandingBalance(
			SearchBaseWrapper searchBaseWrapper) {
		CustomList<ShipmentModel> list = this.shipmentDAO
				.findByExample((ShipmentModel) searchBaseWrapper
						.getBasePersistableModel());

		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	/**
	 *  Return true if shipment product is variable
	 */
	
	public boolean isVariableProduct(Long productId) {
		boolean isVarPro = false;
		String productHQL = " select stm.serviceTypeId from ProductModel pm, ServiceModel sm, ServiceTypeModel stm "
				+ "where pm.productId = ? "
				+ "and pm.relationServiceIdServiceModel.serviceId = sm.serviceId "
				+ "and sm.relationServiceTypeIdServiceTypeModel.serviceTypeId = stm.serviceTypeId";
		List serviceTypeIdList = genericDAO.findByHQL(productHQL,
				new Object[] { productId });
		Object obj = (Object) serviceTypeIdList.get(0);
		Long serviceTypeId = (Long) obj;
		if (SERVICE_TYPE_VARIABLE.equals(serviceTypeId))
			isVarPro = true;
		return isVarPro;
	}
	
	/**
	 *  Return true if current date is greater then shipment expiry date
	 */
	
	public boolean isCurDateGTExpiryDate(Date expiryDate) throws Exception {
		Date date = null;
		String curDate = PortalDateUtils.currentFormattedDate("dd/MM/yyyy");
		date = PortalDateUtils.parseStringAsDate(curDate, "dd/MM/yyyy");
		if (expiryDate!=null && date.after(expiryDate))
			return true;
		else
			return false;
	}
}