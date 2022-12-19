package com.inov8.microbank.server.service.addressmodule;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.GenericDAO;
import com.inov8.microbank.common.model.PostalOfficeModel;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.addressmodule.RetailerContactAddressesDAO;


public class AddressManagerImpl implements AddressManager {

	@Autowired
	private GenericDAO genericDAO;
	
	private AddressDAO addressDAO;
	private CustomerAddressesDAO customerAddressesDAO;
	private RetailerContactAddressesDAO retailerContactAddressesDAO;
	
	/**
	 * @author AtifHu
	 */
	@Override
	public SearchBaseWrapper getPostalOfficesByCity(
			SearchBaseWrapper searchBaseWrapper) {
		
		ExampleConfigHolderModel	configHolderModel	=new ExampleConfigHolderModel();
		configHolderModel.setMatchMode(MatchMode.EXACT);
		
		CustomList<BasePersistableModel> postalOfficeModelList = genericDAO
				.findByExample(searchBaseWrapper.getBasePersistableModel(),
						null, null, configHolderModel, null);
		searchBaseWrapper.setCustomList(postalOfficeModelList);
		return searchBaseWrapper;
	}
	

	public void setAddressDAO(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}
	public void setCustomerAddressesDAO(CustomerAddressesDAO customerAddressesDAO) {
		this.customerAddressesDAO = customerAddressesDAO;
	}
	public void setRetailerContactAddressesDAO(
			RetailerContactAddressesDAO retailerContactAddressesDAO) {
		this.retailerContactAddressesDAO = retailerContactAddressesDAO;
	}
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
}
