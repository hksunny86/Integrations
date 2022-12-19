package com.inov8.microbank.server.service.accounttypemodule;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.AccountTypeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.server.dao.accounttypemodule.AccountTypeDAO;


public class AccountTypeManagerImpl implements AccountTypeManager {

	
	private  AccountTypeDAO  accountTypeDAO; 
	
	
	private GenericDao genericDAO;

	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}

	
	public SearchBaseWrapper loadAccountType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
	 
		CustomList list = this.accountTypeDAO.findByExample((AccountTypeModel)searchBaseWrapper.getBasePersistableModel());
		return searchBaseWrapper;	
	}
	
	public BaseWrapper loadAccountType(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		AccountTypeModel accountTypeModel = (AccountTypeModel)baseWrapper.getBasePersistableModel(); 
		baseWrapper.setBasePersistableModel(this.accountTypeDAO.findByPrimaryKey(accountTypeModel.getPrimaryKey()));
		return baseWrapper;	
	}
	
	
	public List<PaymentModeModel> searchPaymentModeModel(Long accountTypeId) {

		List paymentModelList = null;
		List paymentModelListModelListView = new ArrayList();
		/*String productHQL = " select pm.productId, pm.name from ProductModel pm, ServiceModel sm "
				+ "where pm.relationSupplierIdSupplierModel.supplierId = ? "
				+ "and pm.relationServiceIdServiceModel.serviceId = sm.serviceId "
				+ "and pm.active= true "
				+ "and sm.relationServiceTypeIdServiceTypeModel.serviceTypeId <> 3"
				+ "order by pm.name";*/

		String paymentModeHQL="select payMode.paymentModeId ,payMode.name from PaymentModeModel payMode " +
				"where payMode.paymentModeId ="+
				"( select accType.relationPaymentModeIdPaymentModeModel.paymentModeId from AccountTypeModel accType " +
				"where accType.accountTypeId = ? )";
		paymentModelList = genericDAO.findByHQL(paymentModeHQL,
				new Object[] { accountTypeId });

		
		
		PaymentModeModel paymentModeModel = null;
		for (int count = 0; count < paymentModelList.size(); count++) {
			paymentModeModel = new PaymentModeModel();

			Object obj[] = (Object[]) paymentModelList.get(count);

			paymentModeModel.setPrimaryKey((Long) obj[0]);
			paymentModeModel.setName((String) obj[1]);

			paymentModelListModelListView.add(paymentModeModel);

		}
		return paymentModelListModelListView;
	}


	public void setAccountTypeDAO(AccountTypeDAO accountTypeDAO) {
		this.accountTypeDAO = accountTypeDAO;
	}
	
	

}


